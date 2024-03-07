package fr.laptoff.civilisationplot.managers.city;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonManager {

    private final Gson gson;

    public JsonManager(){
        gson = new GsonBuilder()
                .disableHtmlEscaping()
                .serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(City.class, new CityTypeAdapter())
                .create();
    }

    public String serialize(City c){
        return gson.toJson(c);
    }

    public City deserialize(String s){
        return gson.fromJson(s, City.class);
    }

}
