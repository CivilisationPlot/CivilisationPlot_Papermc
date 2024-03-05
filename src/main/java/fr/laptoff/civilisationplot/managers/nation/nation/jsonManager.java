package fr.laptoff.civilisationplot.managers.nation.nation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class jsonManager {

    private final Gson gson;

    public jsonManager(){
        gson = new GsonBuilder()
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
