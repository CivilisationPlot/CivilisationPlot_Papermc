package fr.laptoff.civilisationplot.managers.nation.plot;

import fr.laptoff.civilisationplot.CivilisationPlot;
import fr.laptoff.civilisationplot.managers.data.Database;
import fr.laptoff.civilisationplot.managers.data.FileManager;
import org.bukkit.Chunk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Plot {

    private Chunk chunk;
    private UUID NationUuid;
    private UUID CityUuid;
    private UUID ProprietaryUuid;
    private final JsonManager json = new JsonManager();
    private final File file;
    private static final CivilisationPlot plugin = CivilisationPlot.getInstance();
    private static final Database database = plugin.getDatabase();

    public Plot(Chunk chunk, UUID nationUuid, UUID cityUuid, UUID proprietaryUuid){
        this.chunk = chunk;
        this.NationUuid = nationUuid;
        this.CityUuid = cityUuid;
        this.ProprietaryUuid = proprietaryUuid;

        file = new File(plugin.getDataFolder() + "/Data/Plot/" + this.chunk.getX() + "-" + this.chunk.getZ() + ".json");
    }

    public Chunk getChunk(){
        return this.chunk;
    }

    public UUID getNation(){
        return this.NationUuid;
    }

    public UUID getCity(){
        return this.CityUuid;
    }

    public UUID getProprietary(){
        return this.ProprietaryUuid;
    }

    public String getChunkId(){
        return this.chunk.getX() + "-" + this.chunk.getZ();
    }

    public void setChunk(Chunk chunk){
        this.chunk = chunk;
    }

    public void setNation(UUID uuid){
        this.NationUuid = uuid;
    }

    public void setCity(UUID uuid){
        this.CityUuid = uuid;
    }

    public void setProprietary(UUID uuid){
        this.ProprietaryUuid = uuid;
    }

    public String getPlotJson(){
        return json.serialize(this);
    }

    public boolean isLocalExist(){
        return file.exists();
    }

    public String getFromLocal(){
        if (!file.exists())
            return null;

        try {
            return Files.readString(Path.of(file.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getFromLocal(Chunk chunk){
        File file = new File(plugin.getDataFolder() + "/Data/Plot/" + chunk.getX() + "-" + chunk.getZ() + ".json");

        if (!file.exists())
            return null;

        try {
            return Files.readString(Path.of(file.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void localSave(){
        FileManager.createFile(file);
        FileManager.rewrite(file, getPlotJson());
    }

    public void localDelete(){
        file.delete();
    }

    public void syncLocalWithDatabase(){
        if (!database.isConnected())
            return;

        if (getFromDatabase() == null)
            localDelete();

        if (getFromLocal().equals(getFromDatabase()))
            return;

        json.deserialize(getFromDatabase()).localSave();
    }

    public boolean isDatabaseExist(){
        if (!database.isConnected())
            return false;

        try{
            PreparedStatement pstmt = database.getConnection().prepareStatement("SELECT * FROM plots WHERE chunkid = '" + getChunkId() + "';");
            ResultSet result = pstmt.executeQuery();

            while(result.next()){
                return true;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public String getFromDatabase(){
        if (!database.isConnected())
            return null;

        try{
            PreparedStatement pstmt = database.getConnection().prepareStatement("SELECT * FROM plots WHERE chunkid = '" + getChunkId() + "';");
            ResultSet result = pstmt.executeQuery();

            while(result.next()){
                return result.getString("json");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getFromDatabase(Chunk chunk){
        if (!database.isConnected())
            return null;

        try{
            PreparedStatement pstmt = database.getConnection().prepareStatement("SELECT * FROM plots WHERE chunkid = '" + chunk.getX() + "-" + chunk.getZ() + "';");
            ResultSet result = pstmt.executeQuery();

            while(result.next()){
                return result.getString("json");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    public void databaseSave(){
        if (!database.isConnected())
            return;

        try{
            PreparedStatement pstmt = database.getConnection().prepareStatement("UPDATE plots SET json = '" + this.getPlotJson() + "' WHERE chunkid = '" + getChunkId() + "';");

            if (!isDatabaseExist()){
                pstmt = database.getConnection().prepareStatement("INSERT INTO plots (chunkid, json) VALUES ('" + getChunkId() + "', '" + this.getPlotJson() + "');");
            }

            pstmt.execute();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void databaseDelete(){
        if (!isDatabaseExist())
            return;

        try{
            PreparedStatement pstmt = database.getConnection().prepareStatement("DELETE FROM plots WHERE chunkid = '" + getChunkId() + "';");
            pstmt.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static Plot getPlot(Chunk chunk){

        JsonManager json = new JsonManager();

        if (getFromDatabase(chunk) != null)
            json.deserialize(getFromDatabase(chunk)).syncLocalWithDatabase();

        return json.deserialize(getFromLocal(chunk));
    }

    public void delete(){
        databaseDelete();
        localDelete();
    }

    public void save(){
        databaseSave();
        localSave();
    }

}
