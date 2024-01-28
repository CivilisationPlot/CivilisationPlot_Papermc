package fr.laptoff.civilisationplot.managers.civils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.laptoff.civilisationplot.CivilisationPlot;
import fr.laptoff.civilisationplot.managers.datas.DatabaseManager;
import fr.laptoff.civilisationplot.managers.datas.FileManager;
import fr.laptoff.civilisationplot.managers.nation.Nation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Civil {

    private String PlayerName;
    private float Money;
    private UUID Nation;
    private static final Connection co = CivilisationPlot.getInstance().getDatabase().getConnection();
    private static final List<String> civilsList = new ArrayList<String>();

    public Civil(String playerName, float money, UUID nation){
        this.PlayerName = playerName;
        this.Money = money;
        this.Nation = nation;
    }

    public String getPlayerName(){
        return this.PlayerName;
    }

    public float getMoney(){
        return this.Money;
    }

    public List<String> getCivilsList(){
        return civilsList;
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(this.PlayerName);
    }

    public UUID getUuid(){
        return getPlayer().getUniqueId();
    }

    public UUID getNationUuid(){
        return this.Nation;
    }

    public fr.laptoff.civilisationplot.managers.nation.Nation getNation(){
        return fr.laptoff.civilisationplot.managers.nation.Nation.getNation(this.Nation);
    }

    public void setNation(Nation nation){
        this.Nation = nation.getUuid();
        setNationDatabase(this.Nation);
        localRegister();
    }

    public void setMoney(float money){
        this.Money = money;
        setMoneyDatabase(money);
        localRegister();
    }

    public void setName(String playerName){
        this.PlayerName = playerName;
        setPlayerNameDatabase(playerName);
        localRegister();
    }

    public void setNationDatabase(UUID uuid){
        if (!DatabaseManager.isOnline())
            return;

        try {
            PreparedStatement pstmt = co.prepareStatement("UPDATE civils SET nation = '" + uuid + "' WHERE uuid = '" + this.getUuid() + "';");
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setMoneyDatabase(float money){
        if (!DatabaseManager.isOnline())
            return;

        try {
            PreparedStatement pstmt = co.prepareStatement("UPDATE civils SET money = '" + money + "' WHERE uuid = '" + this.getUuid() + "';");
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPlayerNameDatabase(String name){
        if (!DatabaseManager.isOnline())
            return;

        try {
            PreparedStatement pstmt = co.prepareStatement("UPDATE civils SET name = '" + name + "' WHERE uuid = '" + this.getUuid() + "';");
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean isExist(UUID uuid){
        civilsListFromDatabase();
        for(String uuidCivil : civilsList){
            if (UUID.fromString(uuidCivil) == uuid)
                return true;
        }
        return false;
    }

    public void localRegister(){
        File file = new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/Civils/Civils/" + this.getUuid().toString() + ".json");
        Gson gson = new GsonBuilder().create();

        FileManager.createFile(file);
        FileManager.rewrite(file, gson.toJson(this));
    }

    public static Civil getCivilFromLocal(UUID uuid){
        File file = new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/Civils/Civils/" + uuid.toString() + ".json");

        if (!file.exists())
            return null;

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String content;
        try {
            content = Files.readString(Path.of(file.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return gson.fromJson(content, Civil.class);
    }

    public void insertIntoDatabase(){
        PreparedStatement pstmt = null;

        if (co == null)
            return;

        try {
            pstmt = co.prepareStatement("INSERT INTO civils (uuid, name, money, nation) VALUES (?, ?, ?, ?);");
            pstmt.setString(1, getUuid().toString());
            pstmt.setString(2, this.PlayerName);
            pstmt.setFloat(3, this.Money);
            pstmt.setString(4, this.Nation.toString());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Civil> getCivilsListFromDatabase() throws SQLException {
        if (!CivilisationPlot.getInstance().getDatabase().isConnected())
            return null;

        List<Civil> list = new ArrayList<Civil>();

        PreparedStatement pstmt = CivilisationPlot.getInstance().getDatabase().getConnection().prepareStatement("SELECT * FROM civils;");
        ResultSet result = pstmt.executeQuery();

        while (result.next()){
            Civil civil = new Civil(result.getString("name"), result.getFloat("money"), UUID.fromString(result.getString("nation")));
            list.add(civil);
        }

        return list;
    }

    public static Civil getCivilFromDatabase(UUID uuid){

        if (!CivilisationPlot.getInstance().getDatabase().isConnected())
            return null;

        Connection co = CivilisationPlot.getInstance().getDatabase().getConnection();
        Civil civil = null;

        try {
            PreparedStatement pstmt = co.prepareStatement("SELECT * FROM civils WHERE uuid = '" + uuid.toString() + "';");
            ResultSet result = pstmt.executeQuery();

            while (result.next()){
                civil = new Civil(result.getString("name"), result.getFloat("money"), UUID.fromString(result.getString("nation")));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return civil;
    }

    public static Civil getCivil(UUID uuid){
        Civil civil = getCivilFromDatabase(uuid);
        if (civil == null)
            civil = getCivilFromLocal(uuid);

        return civil;
    }

    public static void localToDatabase(UUID uuid){
        Civil civil = getCivilFromLocal(uuid);
        civil.insertIntoDatabase();
    }

    public static void databaseToLocal(UUID uuid){
        Civil civil = getCivilFromDatabase(uuid);
        if (civil!=null)
            civil.localRegister();
    }

    public void save(){
        this.localRegister();
        this.insertIntoDatabase();
        civilsList.add(this.getUuid().toString());
        updateJsonFromCivilsList();
    }

    public void del() {

        //Delete from database
        if (CivilisationPlot.getInstance().getDatabase().isConnected()){
            String sql = "DELETE FROM civils WHERE uuid = UUID(?);";

            Connection connection = CivilisationPlot.getInstance().getDatabase().getConnection();

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, getUuid().toString());

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //Delete from Json
        File file = new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/Civils/Civils/" + getUuid().toString() + ".json");

        if (file.exists())
            file.delete();

        //Delete from civilsList
        civilsList.remove(this);
        updateJsonFromCivilsList();
    }

    public static void civilsListFromJson(){
        File file = new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/Civils/CivilsList.json");
        FileManager.createFile(file);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        List<String> list = new ArrayList<String>();

        try {
            list = gson.fromJson(Files.readString(Path.of(file.getPath())), ArrayList.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (list == null)
            return;
        if (list.isEmpty())
            return;

        civilsList.clear();
        civilsList.addAll(list);
    }

    public static void civilsListFromDatabase(){
        if (!DatabaseManager.isOnline())
            return;

        try {
            PreparedStatement pstmt = co.prepareStatement("SELECT * FROM civils;");
            ResultSet result = pstmt.executeQuery();
            civilsList.clear();

            while(result.next()){
                Civil civil = new Civil(result.getString("name"), result.getFloat("money"), UUID.fromString(result.getString("nation")));
                civilsList.add(civil.getUuid().toString());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateJsonFromCivilsList(){
        File file = new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/Civils/CivilsList.json");
        FileManager.createFile(file);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        FileManager.rewrite(file, gson.toJson(civilsList));
    }

    public void addToCivilList(){
        File file = new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/Civils/CivilsList.json");

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        civilsList.add(this.getUuid().toString());
        FileManager.rewrite(file, gson.toJson(civilsList));
    }

    public static void load(){
        civilsListFromDatabase();
        updateJsonFromCivilsList();
    }
}
