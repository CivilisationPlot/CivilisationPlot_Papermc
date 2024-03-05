package fr.laptoff.civilisationplot.managers.nation;

import java.util.List;
import java.util.UUID;

public class Nation {

    private String Name;
    private final UUID Uuid;
    private UUID Leader;
    private final List<UUID> MembersList;
    private UUID Capital;
    private final List<UUID> CitiesList;

    public Nation(String name, UUID uuid, UUID leader, List<UUID> membersList, UUID capital, List<UUID> citiesList){
        this.Name = name;
        this.Uuid = uuid;
        this.Leader = leader;
        this.MembersList = membersList;
        this.Capital = capital;
        this.CitiesList = citiesList;
    }

    public String getName(){
        return this.Name;
    }

    public UUID getUuid(){
        return this.Uuid;
    }

    public UUID getLeader(){
        return this.Leader;
    }

    public List<UUID> getMembersList(){
        return this.MembersList;
    }

    public UUID getCapital(){
        return this.Capital;
    }

    public List<UUID> getCitiesList(){
        return this.CitiesList;
    }

    public void setName(String name){
        this.Name = name;
    }

    public void setLeader(UUID leader){
        this.Leader = leader;
    }

    public void setCapital(UUID capital){
        this.Capital = capital;
    }

    public void addMemberToList(UUID uuid){
        this.MembersList.add(uuid);
    }

    public void addCityToList(UUID uuid){
        this.CitiesList.add(uuid);
    }

    public void removeMemberToList(UUID uuid){
        this.MembersList.remove(uuid);
    }

    public void removeCityToList(UUID uuid){
        this.CitiesList.remove(uuid);
    }


}
