package fr.laptoff.civilisationplot.managers.city;

import fr.laptoff.civilisationplot.CivilisationPlot;
import fr.laptoff.civilisationplot.managers.data.Database;

import java.io.File;
import java.util.UUID;

public class City {

    private final UUID Uuid;
    private String Name;
    private UUID Mayor;
    private UUID Nation;
    private final File file;
    private static final CivilisationPlot plugin = CivilisationPlot.getInstance();
    private static final Database database = plugin.getDatabase();

    public City(UUID uuid, String name, UUID mayor, UUID nation){

        this.Uuid = uuid;
        this.Name = name;
        this.Mayor = mayor;
        this.Nation = nation;

        file = new File(plugin.getDataFolder() + "/Data/City/" + this.Uuid + ".json");

    }

    public UUID getUuid(){
        return this.Uuid;
    }

    public String getName(){
        return this.Name;
    }

    public UUID getMayor(){
        return this.Mayor;
    }

    public UUID getNation(){
        return this.Nation;
    }

    public void setName(String s){
        this.Name = s;
    }

    public void setMayor(UUID m){
        this.Mayor = m;
    }

    public void setNation(UUID n){
        this.Nation = n;
    }

}
