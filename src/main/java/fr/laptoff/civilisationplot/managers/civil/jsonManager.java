package fr.laptoff.civilisationplot.managers.civil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class jsonManager {

    private Gson gson;

    public jsonManager(){
        gson = new GsonBuilder()
                .registerTypeAdapter(Civil.class, new CivilTypeAdapter())
                .setPrettyPrinting()
                .serializeNulls()
                .disableHtmlEscaping()
                .create();
    }

    public String serialize(Civil c){
        return gson.toJson(c);
    }

    public Civil deserialize(String s){
        return gson.fromJson(s, Civil.class);
    }

}
