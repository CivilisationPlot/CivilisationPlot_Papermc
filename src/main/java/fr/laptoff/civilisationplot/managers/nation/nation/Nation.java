package fr.laptoff.civilisationplot.managers.nation.nation;

import fr.laptoff.civilisationplot.CivilisationPlot;
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

public class Nation {

    private String Name;
    private final UUID Uuid;
    private UUID Leader;
    private final List<UUID> MembersList;
    private UUID Capital;
    private final List<UUID> CitiesList;
    private final jsonManager json = new jsonManager();
    private static final CivilisationPlot plugin = CivilisationPlot.getInstance();
    private static final Database database = plugin.getDatabase();
    private final File file;

    public Nation(String name, UUID uuid, UUID leader, List<UUID> membersList, UUID capital, List<UUID> citiesList){
        this.Name = name;
        this.Uuid = uuid;
        this.Leader = leader;
        this.MembersList = membersList;
        this.Capital = capital;
        this.CitiesList = citiesList;

        file = new File(plugin.getDataFolder() + "/Data/Nation/" + this.Uuid + ".json");
    }

    public String getName(){
        return this.Name;
    }

    public UUID getUuid(){
        return this.Uuid;
    }

    public UUID getLeader(){
        return this.Leader;
    }

    public List<UUID> getMembersList(){
        return this.MembersList;
    }

    public UUID getCapital(){
        return this.Capital;
    }

    public List<UUID> getCitiesList(){
        return this.CitiesList;
    }

    public void setName(String name){
        this.Name = name;
    }

    public void setLeader(UUID leader){
        this.Leader = leader;
    }

    public void setCapital(UUID capital){
        this.Capital = capital;
    }

    public void addMemberToList(UUID uuid){
        this.MembersList.add(uuid);
    }

    public void addCityToList(UUID uuid){
        this.CitiesList.add(uuid);
    }

    public void removeMemberToList(UUID uuid){
        this.MembersList.remove(uuid);
    }

    public void removeCityToList(UUID uuid){
        this.CitiesList.remove(uuid);
    }

    public String getNationJson(){
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
            return Files.readString(Path.of(new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/Nation/" + uuid + ".json").getPath()));
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public void localSave(){
        FileManager.createFile(file);
        FileManager.rewrite(file, getNationJson());
    }

    public boolean isLocalExist(){
        return file.exists();
    }

    public static boolean isLocalExist(UUID uuid){
        return new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/Nation/" + uuid + ".json").exists();
    }

    public void databaseSave(){

        if (!database.isConnected())
            return;

        try{
            PreparedStatement pstmt = database.getConnection().prepareStatement("UPDATE nations SET json = '" + this.getNationJson() + "' WHERE uuid = '" + this.Uuid + "';");

            if (getJsonFromDatabase() == null){
                pstmt = database.getConnection().prepareStatement("INSERT INTO nations (uuid, json) VALUES ('" + this.Uuid + "', '" + this.getNationJson() + "');");
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
            PreparedStatement pstmt = database.getConnection().prepareStatement("SELECT * FROM nations WHERE uuid = '" + this.Uuid + "';");
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
            PreparedStatement pstmt = CivilisationPlot.getInstance().getDatabase().getConnection().prepareStatement("SELECT * FROM nations WHERE uuid = '" + uuid + "';");
            ResultSet result = pstmt.executeQuery();

            while(result.next()){
                return result.getString("json");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Nation getNationFromDatabase(UUID uuid){
        if (!database.isConnected())
            return null;

        return new jsonManager().deserialize(getJsonFromDatabase(uuid));
    }

    public static void syncLocalWithDatabase(UUID uuid){
        if (!database.isConnected())
            return;

        if (getNationFromDatabase(uuid) == null){
            deleteFromLocal(uuid);
            return;
        }

        getNationFromDatabase(uuid).localSave();
    }

    public void deleteFromLocal(){
        file.delete();
    }

    public static void deleteFromLocal(UUID uuid){
        new File(plugin.getDataFolder() + "/Data/Nation/" + uuid + ".json").delete();
    }

    public void deleteFromDatabase(){
        if (!database.isConnected())
            return;

        try{
            PreparedStatement pstmt = database.getConnection().prepareStatement("DELETE FROM nations WHERE uuid = '" + this.Uuid + "';");
            pstmt.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void deleteFromDatabase(UUID uuid){
        if (!database.isConnected())
            return;

        try{
            PreparedStatement pstmt = database.getConnection().prepareStatement("DELETE FROM nations WHERE uuid = '" + uuid + "';");
            pstmt.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void delete(){
        deleteFromLocal();
        deleteFromDatabase();
    }

    public static void delete(UUID uuid){
        deleteFromLocal(uuid);
        deleteFromDatabase(uuid);
    }

    public void save(){
        databaseSave();
        localSave();
    }

}
