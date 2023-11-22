package fr.laptoff.civilisationplot.civils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.laptoff.civilisationplot.CivilisationPlot;
import fr.laptoff.civilisationplot.Managers.FileManager;
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
    private final Connection co = CivilisationPlot.getInstance().getDatabase().getConnection();
    private static final List<Civil> civilsList = new ArrayList<Civil>();

    public Civil(String playerName, float money){
        this.PlayerName = playerName;
        this.Money = money;
    }

    public String getPlayerName(){
        return this.PlayerName;
    }

    public float getMoney(){
        return this.Money;
    }

    public List<Civil> getCivilsList(){
        return civilsList;
    }

    public void changeMoney(float money){
        del();
        this.Money = money;
        localRegister();
        insertIntoDatabase();
        civilsList.add(this);
        updateJsonFromCivilsList();
    }

    public void changeName(String playerName){
        del();
        this.PlayerName = playerName;
        localRegister();
        insertIntoDatabase();
        civilsList.add(this);
        updateJsonFromCivilsList();
    }

    public void localRegister(){
        File file = new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/" + Bukkit.getPlayer(this.PlayerName).getUniqueId().toString() + ".json");
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        FileManager.createFile(file);
        FileManager.rewrite(file, gson.toJson(this));
    }

    public static Civil getCivilFromLocal(UUID uuid){
        File file = new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/" + uuid.toString() + ".json");
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
            pstmt = co.prepareStatement("INSERT INTO civils (uuid, name, money) VALUES (?, ?)");
            pstmt.setString(1, Bukkit.getPlayer(this.PlayerName).getUniqueId().toString());
            pstmt.setString(2, this.PlayerName);
            pstmt.setFloat(3, this.Money);
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Civil> getCivilsListFromDatabase() throws SQLException {
        if (!CivilisationPlot.getInstance().getDatabase().isConnected())
            return null;

        List<Civil> list = new ArrayList<Civil>();

        PreparedStatement pstmt = CivilisationPlot.getInstance().getDatabase().getConnection().prepareStatement("SELECT * FROM civils");
        ResultSet result = pstmt.executeQuery();

        while (result.next()){
            Civil civil = new Civil(result.getString("name"), result.getFloat("money"));
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
                String playerName = result.getString("name");
                float money = result.getFloat("money");

                civil = new Civil(playerName, money);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
    }

    public void del() {

        //Delete from database
        if (CivilisationPlot.getInstance().getDatabase().isConnected()){
            String sql = "DELETE FROM civils WHERE uuid = UUID(?)";

            Connection connection = CivilisationPlot.getInstance().getDatabase().getConnection();

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, Bukkit.getPlayer(this.PlayerName).getUniqueId().toString());

                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //Delete from Json
        File file = new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/" + Bukkit.getPlayer(this.PlayerName).getUniqueId().toString() + ".json");

        if (file.exists())
            file.delete();

        //Delete from civilsList
        civilsList.remove(this);
        updateJsonFromCivilsList();
    }

    public static void civilsListFromJson(){
        File file = new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/CivilsList.json");
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        List<Civil> list;

        try {
            list = gson.fromJson(Files.readString(Path.of(file.getPath())), List.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        civilsList.clear();
        civilsList.addAll(list);
    }

    public static void updateJsonFromCivilsList(){
        File file = new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/CivilsList.json");
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        FileManager.rewrite(file, gson.toJson(civilsList));
    }

    public void registerToCivilsList(){

        File file = new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/CivilsList.json");
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        civilsList.add(this);
        FileManager.rewrite(file, gson.toJson(civilsList));
    }

    public static void registerAllCivilsToJson(){
        for (Civil civil : civilsList){
            civil.localRegister();
        }
    }

    public static void registerAllCivilsToDatabase(){
        for (Civil civil : civilsList) {
            civil.insertIntoDatabase();
        }
    }

    public void reisterAllCivils(){
        registerAllCivilsToJson();
        registerAllCivilsToDatabase();
    }

    public static void load(){
        civilsListFromJson();
        registerAllCivilsToDatabase();
        registerAllCivilsToJson();
    }
}
