package fr.laptoff.civilisationplot;

import fr.laptoff.civilisationplot.managers.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public final class CivilisationPlot extends JavaPlugin {

    public static final Logger LOGGER = Logger.getLogger("CivilisationPlot");
    private static CivilisationPlot instance;
    private final Database database = new Database();
    private static final ConsoleCommandSender console = Bukkit.getConsoleSender();
    private FileConfiguration configMessages; //Messages Manager (at /resources/config/english.yml)

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        if (getConfig().getBoolean("Database.enable")){

            LOGGER.info(configMessages.getString("Messages.Database.success_connection"));
        }

        if (getConfig().getBoolean("database.enable")){
            database.connection();
            database.setup();
        }


    }

    @Override
    public void onDisable() {


        if (Database.isOnline()){
            database.disconnection();
        }

    }

    public Database getDatabase(){
        return this.database;
    }

    public static CivilisationPlot getInstance(){
        return instance;
    }

    public static String getLanguage(){

        if (getInstance().getConfig().getString("Language.language") == null)
            return "english";

        if (!getInstance().getConfig().getString("Language.language").equalsIgnoreCase("english") || !getInstance().getConfig().getString("Language.language").equalsIgnoreCase("français"))
            return "english";

        return getInstance().getConfig().getString("Language.language");
    }

}
