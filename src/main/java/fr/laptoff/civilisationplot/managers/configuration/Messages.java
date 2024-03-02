package fr.laptoff.civilisationplot.managers.configuration;

import fr.laptoff.civilisationplot.CivilisationPlot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public enum Messages {

    DATABASE_CONNECTED("database.connected", "<green>Fabulous ! The plugin is connected to the database.", new File(CivilisationPlot.getInstance().getDataFolder() + "/config/" + CivilisationPlot.getLanguage() + ".yml")),
    DATABASE_DISCONNECTED("database.disconnected", "<green> The database is successfully disconnected to the plugin !", new File(CivilisationPlot.getInstance().getDataFolder() + "/config/" + CivilisationPlot.getLanguage() + ".yml")),
    PLUGIN_STARTED("global.plugin_start", "<green> CivilisationPlot started successfully !", new File(CivilisationPlot.getInstance().getDataFolder() + "/config/" + CivilisationPlot.getLanguage() + ".yml")),
    PLUGIN_SHUTDOWNED("global.plugin_shutdown", "<green> CivilisationPlot stopped successfully !", new File(CivilisationPlot.getInstance().getDataFolder() + "/config/" + CivilisationPlot.getLanguage() + ".yml"));

    private final String Path;
    private final String DefaultValue;
    private final File File;

    private Messages(String path, String defaultValue, File file){
        this.Path = path;
        this.DefaultValue = defaultValue;
        this.File = file;
    }

    public String getPath(){
        return this.Path;
    }

    public String getDefaultValue(){
        return this.DefaultValue;
    }

    public File getFile(){
        return this.File;
    }
    public Component getComponent(){
        YamlConfiguration config = YamlConfiguration.loadConfiguration(getFile());

        if (config.getString(getPath()) == null)
            return MiniMessage.miniMessage().deserialize(getDefaultValue());

        return MiniMessage.miniMessage().deserialize(config.getString(getPath()));
    }

    public String getMessage(){
        YamlConfiguration config = YamlConfiguration.loadConfiguration(getFile());

        if (config.getString(getPath()) == null)
            return getDefaultValue();

        return config.getString(getPath());
    }

}
