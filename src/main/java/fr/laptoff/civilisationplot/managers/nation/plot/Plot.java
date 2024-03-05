package fr.laptoff.civilisationplot.managers.nation.plot;

import fr.laptoff.civilisationplot.CivilisationPlot;
import org.bukkit.Chunk;

import java.io.File;
import java.util.UUID;

public class Plot {

    private Chunk chunk;
    private UUID NationUuid;
    private UUID CityUuid;
    private UUID ProprietaryUuid;
    File file;
    public static final CivilisationPlot plugin = CivilisationPlot.getInstance();

    public Plot(Chunk chunk, UUID nationUuid, UUID cityUuid, UUID proprietaryUuid){
        this.chunk = chunk;
        this.NationUuid = nationUuid;
        this.CityUuid = cityUuid;
        this.ProprietaryUuid = proprietaryUuid;

        String chunkId = this.chunk.getX() + "-" + this.chunk.getZ();

        file = new File(plugin.getDataFolder() + "/Data/Plot/" + chunkId + ".json");
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

}
