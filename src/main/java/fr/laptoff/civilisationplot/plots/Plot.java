package fr.laptoff.civilisationplot.plots;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.laptoff.civilisationplot.CivilisationPlot;
import fr.laptoff.civilisationplot.Managers.DatabaseManager;
import fr.laptoff.civilisationplot.Managers.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

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

public class Plot {

    private String WorldName;
    private double XCoordinate;
    private double YCoordinates;
    private String PropertyType;
    private UUID Proprietary;
    private byte Level;
    private UUID Uuid;
    private File file;
    private final static File fileList = new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/Plots/ListPlots.json");
    private Connection co = CivilisationPlot.getInstance().getDatabase().getConnection();
    private static List<UUID> plotsList = new ArrayList<UUID>();

    public Plot(String worldName, double xCoordinates, double yCoordinates , String propertyType, UUID proprietary, byte level, UUID uuid){

        this.WorldName = worldName;
        this.XCoordinate = xCoordinates;
        this.YCoordinates = yCoordinates;
        this.PropertyType = propertyType;
        this.Proprietary = proprietary;
        this.Level = level;
        this.Uuid = uuid;
        this.file = new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/Plots/Plots/" + this.Uuid + ".json");
        plotsList.add(this.Uuid);
    }

    public Chunk getChunk(){
        return new Location(Bukkit.getWorld(this.WorldName), this.XCoordinate, this.YCoordinates, 0).getChunk();
    }

    public World getWorld(){
        return Bukkit.getWorld(this.WorldName);
    }

    public String getPropertyType(){
        return this.PropertyType;
    }

    public UUID getProprietary(){
        return this.Proprietary;
    }

    public byte getLevel(){
        return this.Level;
    }

    public UUID getUuid(){
        return this.Uuid;
    }

    public void setWorld(World world){
        this.WorldName = world.getName();
    }

    public void setPropertyType(String s){
        this.PropertyType = s;
    }

    public void setProprietary(UUID uuid){
        this.Proprietary = uuid;
    }

    public void setLevel(byte lvl){
        this.Level = lvl;
    }

    public static List<UUID> getPlotsList(){
        return plotsList;
    }

    //Database
    public void updateObjectFromDatabase(){
        if (!DatabaseManager.isOnline())
            return;

        try {
            PreparedStatement pstmt = co.prepareStatement("SELECT * FROM plots WHERE uuid = '" + this.Uuid.toString() + "'");
            ResultSet result = pstmt.executeQuery();

            if (!result.next())
                return;

            while (result.next()){
                this.WorldName = result.getString("worldName");
                this.YCoordinates = result.getDouble("yCoordinates");
                this.XCoordinate = result.getDouble("xCoordinates");
                this.Level = result.getByte("level");
                this.PropertyType = result.getString("propertyType");
                this.Proprietary = UUID.fromString(result.getString("uuidProprietary"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateDatabaseFromObject(){
        if (!DatabaseManager.isOnline())
            return;

        try {
            PreparedStatement pstmt = co.prepareStatement("UPDATE plots SET worldName = '" + this.WorldName + "', yCoordinates = " + this.YCoordinates + ", xCoordinates = " + this.XCoordinate + ", level = " + this.Level + ", propertyType = '" + this.PropertyType + "', uuidProprietary = '" + this.Proprietary.toString() + "' WHERE uuid = '" + this.Uuid.toString() + "'");
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFromDatabase(){
        if (!DatabaseManager.isOnline())
            return;

        try {
            PreparedStatement pstmt = co.prepareStatement("DELETE FROM plots WHERE uuid = '" + this.Uuid + "';");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Local (/CivilisationPlot/Data/Plots/)
    public static Plot getPlotLocal(double x, double z){
        for (UUID plotUuid : plotsList){
            Plot plot = getPlotLocal(plotUuid);

            if (plot == null)
                return null;

            if (plot.getChunk().getX() == x && plot.getChunk().getZ() == z)
                return plot;
        }
        return null;
    }

    public static Plot getPlotLocal(UUID uuid){
        File file = new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/Plots/Plots/" + uuid.toString() + ".json");
        Gson gson = new GsonBuilder().create();

        if (!file.exists())
            return null;

        try {
            return gson.fromJson(Files.readString(Path.of(file.getPath())), Plot.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateListFromLocal(){
        try {

            FileManager.createFile(fileList);

            plotsList = new GsonBuilder().create().fromJson(Files.readString(Path.of(fileList.getPath())), ArrayList.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateLocalFromDatabase(){
        updateObjectFromDatabase();
        localSave();
    }

    public void localSave(){
        Gson gson = new GsonBuilder().create();

        FileManager.createFile(this.file);
        FileManager.rewrite(this.file, gson.toJson(this));
    }

    public static void saveListLocal(){
        Gson gson = new GsonBuilder().create();

        FileManager.createFile(fileList);

        FileManager.rewrite(fileList, gson.toJson(plotsList));
    }

    public void delLocal(){
        this.file.delete();
    }

    public void delFromLocalList(){
        plotsList.remove(this.Uuid);
        saveListLocal();
    }
}
