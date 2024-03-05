package fr.laptoff.civilisationplot.managers.civil;

import fr.laptoff.civilisationplot.CivilisationPlot;
import fr.laptoff.civilisationplot.managers.configuration.Messages;
import fr.laptoff.civilisationplot.managers.data.Database;
import fr.laptoff.civilisationplot.managers.data.FileManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class Civil {

    private final UUID Uuid;
    private float Money;
    private final List<UUID> Friends;
    private UUID NationUuid;
    private final jsonManager json = new jsonManager();
    private final File file;
    private final static CivilisationPlot plugin = CivilisationPlot.getInstance();
    private final static Database database = plugin.getDatabase();

    public Civil(UUID uuid, float money, List<UUID> friends, UUID nationUuid){
        this.Uuid = uuid;
        this.Money = money;
        this.Friends = friends;
        this.NationUuid = nationUuid;

        file = new File(plugin.getDataFolder() + "/Data/Civil/" + this.Uuid + ".json");
    }

    public UUID getUuid(){
        return this.Uuid;
    }

    public float getMoney(){
        return this.Money;
    }

    public void setMoney(float money){
        this.Money = money;
    }

    public List<UUID> getFriends(){
        return this.Friends;
    }

    public void addFriend(UUID uuid){
        Friends.add(uuid);
    }

    public void removeFriend(UUID uuid){
        Friends.remove(uuid);
    }

    public UUID getNationUuid(){
        return this.NationUuid;
    }

    public void setNation(UUID uuid){
        this.NationUuid = uuid;
    }

    public String getCivilJson(){
        return json.serialize(this);
    }

    public String getJsonFromLocal(){
        if (!isLocalExist())
            return null;

        try {
            return Files.readString(Path.of(file.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getJsonFromLocal(UUID uuid){
        if (!isLocalExist(uuid))
            return null;

        try{
            return Files.readString(Path.of(new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/Civil/" + uuid + ".json").getPath()));
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public void localSave(){
        FileManager.createFile(file);
        FileManager.rewrite(file, getCivilJson());
    }

    public boolean isLocalExist(){
        return file.exists();
    }

    public static boolean isLocalExist(UUID uuid){
        return new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/Civil/" + uuid + ".json").exists();
    }

    public void databaseSave(){

        if (!database.isConnected())
            return;

        try{
            PreparedStatement pstmt = database.getConnection().prepareStatement("UPDATE civils SET json = '" + this.getCivilJson() + "' WHERE uuid = '" + this.Uuid + "';");

            if (getJsonFromDatabase() == null){
                pstmt = database.getConnection().prepareStatement("INSERT INTO civils (uuid, json) VALUES ('" + this.Uuid + "', '" + this.getCivilJson() + "');");
            }

            pstmt.execute();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public String getJsonFromDatabase(){

        if (!database.isConnected())
            return null;

        try{
            PreparedStatement pstmt = database.getConnection().prepareStatement("SELECT * FROM civils WHERE uuid = '" + this.Uuid + "';");
            ResultSet result = pstmt.executeQuery();

            while(result.next()){
                return result.getString("json");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getJsonFromDatabase(UUID uuid){

        if (!CivilisationPlot.getInstance().getDatabase().isConnected())
            return null;

        try{
            PreparedStatement pstmt = CivilisationPlot.getInstance().getDatabase().getConnection().prepareStatement("SELECT * FROM civils WHERE uuid = '" + uuid + "';");
            ResultSet result = pstmt.executeQuery();

            while(result.next()){
                return result.getString("json");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Civil getCivilFromLocal(UUID uuid){
        syncLocalWithDatabase(uuid);

        if (!isLocalExist(uuid))
            return null;

        Civil civil = null;

        try{
            civil = new jsonManager().deserialize(Files.readString(Path.of(new File(plugin.getDataFolder() + "/Data/Civil/" + uuid + ".json").getPath())));
        }catch(IOException e){
            e.printStackTrace();
        }
        return civil;
    }

    public static Civil getCivilFromDatabase(UUID uuid){
        if (!database.isConnected())
            return null;

        if (getJsonFromDatabase(uuid) == null)
            return null;

        return new jsonManager().deserialize(getJsonFromDatabase(uuid));
    }

    public static void syncLocalWithDatabase(UUID uuid){
        if (!database.isConnected())
            return;

        if (getCivilFromDatabase(uuid) == null){
            deleteFromLocal(uuid);
            return;
        }

        getCivilFromDatabase(uuid).localSave();
    }

    public void deleteFromLocal(){
        file.delete();
    }

    public static void deleteFromLocal(UUID uuid){
        new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/Civil/" + uuid + ".json").delete();
    }

    public void deleteFromDatabase(){
        if (!database.isConnected())
            return;

        try{
            PreparedStatement pstmt = database.getConnection().prepareStatement("DELETE FROM civils WHERE uuid = '" + this.Uuid + "';");
            pstmt.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteFromDatabase(UUID uuid){
        Database database = CivilisationPlot.getInstance().getDatabase();

        if (!database.isConnected())
            return;

        try{
            PreparedStatement pstmt = database.getConnection().prepareStatement("DELETE FROM civils WHERE uuid = '" + uuid + "';");
            pstmt.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void delete(){
        deleteFromLocal();
        deleteFromDatabase();
    }

    public void delete(UUID uuid){
        deleteFromLocal(uuid);
        deleteFromDatabase(uuid);
    }

    public void save(){
        localSave();
        databaseSave();
    }

}
