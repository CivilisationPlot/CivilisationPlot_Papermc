package fr.laptoff.civilisationplot.managers.nation.plot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonManager {

    private Gson gson;

    public JsonManager(){
        gson = new GsonBuilder()
                .registerTypeAdapter(Plot.class, new PlotTypeAdapter())
                .setPrettyPrinting()
                .serializeNulls()
                .disableHtmlEscaping()
                .create();
    }

    public String serialize(Plot p){
        return gson.toJson(p);
    }

    public Plot deserialize(String s){
        return gson.fromJson(s, Plot.class);
    }

}
