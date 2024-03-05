package fr.laptoff.civilisationplot.managers.nation;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NationTypeAdapter extends TypeAdapter<Nation> {

    @Override
    public void write(JsonWriter writer, Nation nation) throws IOException {

        writer.beginObject();

        writer.name("name").value(nation.getName());
        writer.name("uuid").value(nation.getUuid().toString());
        writer.name("leader").value(nation.getLeader().toString());

        writer.name("members").beginArray();
            for (UUID uuid : nation.getMembersList()){
                writer.beginObject();
                writer.name("memberUuid").value(uuid.toString());
                writer.endObject();
            }
        writer.endArray();

        writer.name("capital").value(nation.getCapital().toString());

        writer.name("cities").beginArray();
            for (UUID uuid : nation.getCitiesList()){
                writer.beginObject();
                writer.name("cityUuid").value(uuid.toString());
                writer.endObject();
            }
        writer.endArray();

        writer.endObject();

    }

    @Override
    public Nation read(JsonReader reader) throws IOException {

        reader.beginObject();

        String name = null;
        UUID uuid = null;
        UUID leaderUuid = null;
        List<UUID> membersList = new ArrayList<UUID>();
        UUID capitalUuid = null;
        List<UUID> citiesList = new ArrayList<UUID>();

        while(reader.hasNext()){
            switch(reader.nextName()){
                case "name" :
                    name = reader.nextString();
                    break;
                case "uuid" :
                    uuid = UUID.fromString(reader.nextString());
                    break;
                case "leader" :
                    leaderUuid = UUID.fromString(reader.nextString());
                    break;

                case "members" :
                    reader.beginArray();
                    while(reader.hasNext()){
                        reader.beginObject();

                        while(reader.hasNext()){
                            if (reader.nextName().equals("memberUuid"))
                                membersList.add(UUID.fromString(reader.nextString()));
                        }

                        reader.endObject();
                    }
                    reader.endArray();

                case "capital" :
                    capitalUuid = UUID.fromString(reader.nextString());
                    break;

                case "cities" :
                    reader.beginArray();

                    while(reader.hasNext()){
                        reader.beginObject();
                        while(reader.hasNext()){
                            if (reader.nextName().equals("cityUuid"))
                                citiesList.add(UUID.fromString(reader.nextString()));
                        }
                        reader.endObject();
                    }

                    reader.endArray();
            }
        }

        reader.endObject();

        return new Nation(name, uuid, leaderUuid, membersList, capitalUuid, citiesList);
    }
}
