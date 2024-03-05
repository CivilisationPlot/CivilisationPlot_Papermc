package fr.laptoff.civilisationplot.managers.nation.nation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonManager {

    private final Gson gson;

    public JsonManager(){
        gson = new GsonBuilder()
                .registerTypeAdapter(Nation.class, new NationTypeAdapter())
                .disableHtmlEscaping()
                .serializeNulls()
                .setPrettyPrinting()
                .create();
    }

    public String serialize(Nation n){
        return gson.toJson(n);
    }

    public Nation deserialize(String s){
        return gson.fromJson(s, Nation.class);
    }

}
