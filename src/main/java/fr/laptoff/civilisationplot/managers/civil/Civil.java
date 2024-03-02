package fr.laptoff.civilisationplot.managers.civil;

import java.util.UUID;

public class Civil {

    private final UUID Uuid;
    private final float Money;

    public Civil(UUID uuid, float money){
        this.Uuid = uuid;
        this.Money = money;
    }

    public UUID getUuid(){
        return this.Uuid;
    }

    public float getMoney(){
        return this.Money;
    }



}
