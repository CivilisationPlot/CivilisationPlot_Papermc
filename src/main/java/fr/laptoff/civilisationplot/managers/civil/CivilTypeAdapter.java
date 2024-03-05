package fr.laptoff.civilisationplot.managers.civil;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CivilTypeAdapter extends TypeAdapter<Civil> {


    @Override
    public void write(JsonWriter writer, Civil civil) throws IOException {
        writer.beginObject();
        writer.name("uuid").value(civil.getUuid().toString());
        writer.name("money").value(civil.getMoney());

        writer.name("friends").beginArray();
            for (UUID friend : civil.getFriends()){
                writer.beginObject();
                writer.name("uuid").value(friend.toString());
                writer.endObject();
            }
        writer.endArray();

        writer.name("nation").value(civil.getNationUuid().toString());

        writer.endObject();
    }

    @Override
    public Civil read(JsonReader reader) throws IOException {

        reader.beginObject();

        UUID uuid = null;
        float money = 0f;
        List<UUID> friends = new ArrayList<UUID>();
        UUID nationUuid = null;

        while(reader.hasNext()){
            switch (reader.nextName()){
                case "uuid" :
                    uuid = UUID.fromString(reader.nextString());
                    break;

                case "money" :
                    money = (float) reader.nextDouble();
                    break;

                case "friends" :
                    reader.beginArray();
                    while(reader.hasNext()){
                        reader.beginObject();

                        while(reader.hasNext()){
                            if (reader.nextName().equals("uuid"))
                                friends.add(UUID.fromString(reader.nextString()));
                        }

                        reader.endObject();
                    }
                    reader.endArray();
                case "nation" :
                    nationUuid = UUID.fromString(reader.nextString());
                    break;
            }
        }
        reader.endObject();

        return new Civil(uuid, money, friends, nationUuid);
    }
}
