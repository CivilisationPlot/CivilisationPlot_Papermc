package fr.laptoff.civilisationplot.nation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.laptoff.civilisationplot.CivilisationPlot;
import fr.laptoff.civilisationplot.Managers.DatabaseManager;
import fr.laptoff.civilisationplot.Managers.FileManager;
import fr.laptoff.civilisationplot.civils.Civil;

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

public class Nation {
    
    private String Name;
    private UUID Uuid;
    private Civil Leader;
    private File file;
    private static Connection co = CivilisationPlot.getInstance().getDatabase().getConnection();
    private static List<String> nationList = new ArrayList<String>();

    
    public Nation(String name, UUID uuid, Civil leader){
        this.Name = name;
        this.Uuid = uuid;
        this.Leader = leader;
        this.file = new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/Nations/Nations/" + this.Uuid);
        FileManager.createFile(this.file);
    }

    public String getName(){
        return this.Name;
    }

    public UUID getUuid(){
        return this.Uuid;
    }

    public Civil getLeader(){
        return this.Leader;
    }

    public void setName(String name){
        this.Name = name;
    }

    public void setLeader(Civil leader){
        this.Leader = leader;
    }

    //Registers
    public void registerToList(){
        if (!nationList.contains(this.Uuid.toString()))
            nationList.add(this.Uuid.toString());
    }

    public void registerToLocal(){
        Gson gson = new GsonBuilder().create();

        FileManager.rewrite(this.file, gson.toJson(this));
    }

    public void registerToDatabase(){
        if (co == null)
            return;

        if(this.isExistIntoDatabase())
            this.delFromDatabase();


        try{
            PreparedStatement pstmt = co.prepareStatement("INSERT INTO nations (name, uuid, civil_uuid) VALUES (?, ?, ?)");
            pstmt.setString(1, this.Name);
            pstmt.setString(2, this.Uuid.toString());
            pstmt.setString(3, this.Leader.getUuid().toString());
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    //Getters
    public static Nation getFromNationList(UUID uuid){
        for (String nation : nationList){
            if (UUID.fromString(nation) == uuid)
                return getNation(UUID.fromString(nation));
        }
        return null;
    }

    public static Nation getFromLocal(UUID uuid){
        File file = new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/Nations/Nations/" + uuid.toString());

        if (!file.exists())
            return null;

        Gson gson = new GsonBuilder().create();

        try {
            return gson.fromJson(Files.readString(Path.of(file.getPath())), Nation.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Nation getFromDatabase(UUID uuid){
        if (!DatabaseManager.isOnline())
            return null;

        try {
            PreparedStatement pstmt = co.prepareStatement("SELECT * FROM nations WHERE uuid = '" + uuid + "'");
            ResultSet result = pstmt.executeQuery();

            while(result.next())
                return new Nation(result.getString("name"), uuid, Civil.getCivil(UUID.fromString(result.getString("leader_uuid"))));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static Nation getNation(UUID uuid){
        if (getFromDatabase(uuid) == null)
            return getFromLocal(uuid);

        return getFromDatabase(uuid);
    }

    //Updates
    public static void updateLocalListFromProgramList(){
        File file = new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/Nations/NationsList.json");
        Gson gson = new GsonBuilder().create();


        FileManager.createFile(file);
        FileManager.rewrite(file, gson.toJson(nationList, ArrayList.class));
    }

    public static void updateProgramListFromLocalList(){
        Gson gson = new GsonBuilder().create();
        File file = new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/Nations/NationsList.json");

        FileManager.createFile(file);
        try {
            nationList = gson.fromJson(Files.readString(Path.of(file.getPath())), ArrayList.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateProgramListFromDatabase(){
        if (!DatabaseManager.isOnline())
            return;

        nationList.clear();

        try {
            PreparedStatement pstmt = co.prepareStatement("SELECT * FROM nations");
            ResultSet result = pstmt.executeQuery();

            while(result.next()){
                Nation nation = new Nation(result.getString("name"), UUID.fromString(result.getString("uuid")), Civil.getCivil(UUID.fromString(result.getString("leader_uuid"))));
                nationList.add(nation.getUuid().toString());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateLocalFromDatabase(){
        for (String uuid : nationList){

            Nation nation = getFromDatabase(UUID.fromString(uuid));

            if (nation == null)
                return;

            nation.registerToLocal();
        }
    }

    public static void updateDatabaseFromLocal(){
        if (!DatabaseManager.isOnline())
            return;

        try {
            PreparedStatement pstmt = co.prepareStatement("DELETE FROM nations");
            pstmt.execute();

            for (String uuid : nationList){
                Nation nation = getFromLocal(UUID.fromString(uuid));

                if (nation == null)
                    return;

                nation.registerToDatabase();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Deletes
    public void delFromNationList(){
        nationList.remove(this.Uuid.toString());
    }

    public void delFromLocal(){
        this.file.delete();
    }

    public void delFromDatabase(){
        if (co == null)
            return;

        if (!isExistIntoDatabase())
            return;

        try {
            PreparedStatement pstmt = co.prepareStatement("DELETE FROM nations WHERE uuid = UUID(?);");
            pstmt.setString(1, this.Uuid.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void del(){
        this.delFromDatabase();
        this.delFromLocal();
        this.delFromNationList();
    }

    public boolean isExistIntoDatabase(){
        if (co == null)
            return false;

        try {
            PreparedStatement pstmt = co.prepareStatement("SELECT * FROM nations WHERE uuid = '" + this.Uuid + "'");
            ResultSet result = pstmt.executeQuery();

            while(result.next())
                return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
