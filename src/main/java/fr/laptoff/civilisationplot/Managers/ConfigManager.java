package fr.laptoff.civilisationplot.Managers;

import fr.laptoff.civilisationplot.CivilisationPlot;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final File file;
    private final FileConfiguration configFile;

    public ConfigManager(String filePath){
        this.file = new File(CivilisationPlot.getInstance().getDataFolder() + "/config/" + filePath);

        FileManager.createResourceFile(file);

        configFile = new YamlConfiguration();

        try {
            configFile.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getFileConfiguration(){
        return this.configFile;
    }

    public File getFile(){
        return this.file;
    }
}