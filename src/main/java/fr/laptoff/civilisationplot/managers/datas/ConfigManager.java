package fr.laptoff.civilisationplot.managers.datas;

import fr.laptoff.civilisationplot.CivilisationPlot;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final File file;
    File file_;
    private final FileConfiguration configFile;

    public ConfigManager(String filePath){
        this.file = new File("config/" + filePath);

        FileManager.createResourceFile(file);

        this.file_ = new File(CivilisationPlot.getInstance().getDataFolder() + "/" +  file.getPath());

        configFile = new YamlConfiguration();

        try {
            configFile.load(file_);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getFileConfiguration(){
        return this.configFile;
    }

    public File getFile(){
        return this.file_;
    }
}