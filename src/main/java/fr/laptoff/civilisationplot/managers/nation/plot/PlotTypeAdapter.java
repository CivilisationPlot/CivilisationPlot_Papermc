package fr.laptoff.civilisationplot.managers.nation.plot;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;
import java.util.UUID;

public class PlotTypeAdapter extends TypeAdapter<Plot> {
    @Override
    public void write(JsonWriter writer, Plot plot) throws IOException {
        writer.beginObject();

        writer.name("X").value((double) plot.getChunk().getX());
        writer.name("Z").value((double) plot.getChunk().getZ());
        writer.name("world").value(plot.getChunk().getWorld().getName());
        writer.name("nation").value(plot.getNation().toString());
        writer.name("city").value(plot.getCity().toString());
        writer.name("proprietary").value(plot.getProprietary().toString());

        writer.endObject();
    }

    @Override
    public Plot read(JsonReader reader) throws IOException {

        reader.beginObject();

        double X = 0;
        double Z = 0;
        World world = null;
        UUID nationUuid = null;
        UUID cityUuid = null;
        UUID proprietaryUuid = null;

        while(reader.hasNext()){
            switch (reader.nextName()){
                case "X" :
                    X = reader.nextDouble();
                    break;
                case "Z" :
                    Z = reader.nextDouble();
                    break;
                case "world" :
                    world = Bukkit.getWorld(reader.nextString());
                    break;
                case "nation" :
                    nationUuid = UUID.fromString(reader.nextString());
                    break;
                case "city" :
                    cityUuid = UUID.fromString(reader.nextString());
                    break;
                case "proprietary" :
                    proprietaryUuid = UUID.fromString(reader.nextString());
                    break;
            }
        }

        reader.endObject();

        return new Plot(new Location(world, X, 0, Z).getChunk(), nationUuid, cityUuid, proprietaryUuid);
    }
}
