package fr.laptoff.civilisationplot.managers.city;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.UUID;

public class CityTypeAdapter extends TypeAdapter<City> {
    @Override
    public void write(JsonWriter writer, City city) throws IOException {

        writer.beginObject();
        writer.name("uuid").value(city.getUuid().toString());
        writer.name("name").value(city.getName());
        writer.name("mayor").value(city.getMayor().toString());
        writer.name("nation").value(city.getNation().toString());
        writer.endObject();

    }

    @Override
    public City read(JsonReader reader) throws IOException {

        UUID uuid = null;
        String name = null;
        UUID mayor = null;
        UUID nation = null;

        reader.beginObject();
        while(reader.hasNext()){
            switch(reader.nextName()){
                case "uuid" :
                    uuid = UUID.fromString(reader.nextString());
                    break;
                case "name" :
                    name = reader.nextString();
                    break;
                case "mayor" :
                    mayor = UUID.fromString(reader.nextString());
                    break;
                case "nation" :
                    nation = UUID.fromString(reader.nextString());
                    break;
            }
        }
        reader.endObject();

        return new City(uuid, name, mayor, nation);
    }
}
