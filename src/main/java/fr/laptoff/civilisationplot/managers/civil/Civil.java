package fr.laptoff.civilisationplot.managers.civil;

import java.util.List;
import java.util.UUID;

public class Civil {

    private final UUID Uuid;
    private final float Money;
    private final List<UUID> Friends;

    public Civil(UUID uuid, float money, List<UUID> friends){
        this.Uuid = uuid;
        this.Money = money;
        this.Friends = friends;
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



}
