package fr.laptoff.civilisationplot.managers.civil;

import fr.laptoff.civilisationplot.CivilisationPlot;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class Civil {

    private final UUID Uuid;
    private final float Money;
    private final List<UUID> Friends;
    private final jsonManager json = new jsonManager();

    public Civil(UUID uuid, float money, List<UUID> friends){
        this.Uuid = uuid;
        this.Money = money;
        this.Friends = friends;

        CivilisationPlot plugin = CivilisationPlot.getInstance();
        File file = new File(plugin.getDataFolder() + "/Data/Civil/" + this.Uuid + ".yml");
    }

    public UUID getUuid(){
        return this.Uuid;
    }

    public float getMoney(){
        return this.Money;
    }

    public List<UUID> getFriends(){
        return this.Friends;
    }

    public String getCivilJson(){
        return json.serialize(this);
    }

    public void localSave(){

    }

}
