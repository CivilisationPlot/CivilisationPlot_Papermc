package fr.laptoff.civilisationplot.managers.city;

import fr.laptoff.civilisationplot.CivilisationPlot;
import fr.laptoff.civilisationplot.managers.data.Database;

import java.io.File;
import java.util.UUID;

public class City {

    private final UUID Uuid;
    private final String Name;
    private final UUID Mayor;
    private final File file;
    private static final CivilisationPlot plugin = CivilisationPlot.getInstance();
    private static final Database database = plugin.getDatabase();

    public City(UUID uuid, String name, UUID mayor){

        this.Uuid = uuid;
        this.Name = name;
        this.Mayor = mayor;

        file = new File(plugin.getDataFolder() + "/Data/City/" + this.Uuid + ".json");

    }

}
