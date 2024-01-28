package fr.laptoff.civilisationplot.managers.permissions;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.laptoff.civilisationplot.CivilisationPlot;
import fr.laptoff.civilisationplot.managers.datas.FileManager;
import fr.laptoff.civilisationplot.managers.plots.Plot;

public enum PlotPerm {

    PLOT_ALL("*", 1),
    PLOT_BREAK("plot.break", 2),
    PLOT_DESTROY("plot.destroy", 3),
    PLOT_MODIFY_PERMISSIONS("plot.modify.permissions", 4);

    private String Name;
    private int Id;

    private PlotPerm(String name, int id){
        this.Name = name;
        this.Id = id;
    }

    public String getName(){
        return this.Name;
    }

    public int getId(){
        return this.Id;
    }

    public static PlotPerm getPlotspPerm(int id){
        return PlotPerm.values()[id - 1];
    }

    public static PlotPerm getPlotsPerm(String name){
        for (PlotPerm perm : PlotPerm.values()) {
            if (perm.getName() == name)
                return perm;
        }
        return null;
    }

    public static void setPerm(Plot plot, PlotPerm perm, boolean bool){
        File file = new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/Plots/Perms/" + plot.getUuid() + ".yml");
        FileManager.createFile(file);
        YamlConfiguration configuration = new YamlConfiguration().loadConfiguration(file);
        
        configuration.addDefault("Plot." + perm.getName(), bool);
    }

    public static boolean hasPerm(Plot plot, PlotPerm perm){
        File file = new File(CivilisationPlot.getInstance().getDataFolder() + "/Data/Plots/Perms/" + plot.getUuid() + ".yml");
        FileManager.createFile(file);
        YamlConfiguration configuration = new YamlConfiguration().loadConfiguration(file);

        return configuration.getBoolean("Plot." + perm.getName());
    }
}