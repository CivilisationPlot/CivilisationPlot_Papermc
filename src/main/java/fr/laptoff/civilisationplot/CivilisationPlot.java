package fr.laptoff.civilisationplot;

import fr.laptoff.civilisationplot.Managers.ConfigManager;
import fr.laptoff.civilisationplot.Managers.DatabaseManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public final class CivilisationPlot extends JavaPlugin {

    public static final Logger LOGGER = Logger.getLogger("CivilisationPlot");
    private static CivilisationPlot instance;
    private DatabaseManager database;
    private FileConfiguration configMessages; //Messages Manager (at /resources/config/Messages.yml)

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
         configMessages = new ConfigManager("Messages.yml").getFileConfiguration();

        LOGGER.info("####              ##      ###       ##                         ##       ##                       ######    ###                ##");
        LOGGER.info("##  ##                      ##                                  ##                                 ##  ##    ##                ##");
        LOGGER.info("##       ##  ##    ###       ##      ###      #####    ####     #####    ###      ####    #####     ##  ##    ##      ####     #####");
        LOGGER.info("##       ##  ##     ##       ##       ##     ##           ##     ##       ##     ##  ##   ##  ##    #####     ##     ##  ##     ##");
        LOGGER.info("##       ##  ##     ##       ##       ##      #####    #####     ##       ##     ##  ##   ##  ##    ##        ##     ##  ##     ##");
        LOGGER.info("##  ##   ####      ##       ##       ##          ##  ##  ##     ## ##    ##     ##  ##   ##  ##    ##        ##     ##  ##     ## ##");
        LOGGER.info("####     ##      ####     ####     ####    ######    #####      ###    ####     ####    ##  ##   ####      ####     ####       ###");

        if (getConfig().getBoolean("Database.enable")){
            database = new DatabaseManager();
            database.connection();

            LOGGER.info(configMessages.getString("Messages.Database.success_connection"));
        }
    }

    @Override
    public void onDisable() {
        LOGGER.info("####              ##      ###       ##                         ##       ##                       ######    ###                ##");
        LOGGER.info("##  ##                      ##                                  ##                                 ##  ##    ##                ##");
        LOGGER.info("##       ##  ##    ###       ##      ###      #####    ####     #####    ###      ####    #####     ##  ##    ##      ####     #####");
        LOGGER.info("##       ##  ##     ##       ##       ##     ##           ##     ##       ##     ##  ##   ##  ##    #####     ##     ##  ##     ##");
        LOGGER.info("##       ##  ##     ##       ##       ##      #####    #####     ##       ##     ##  ##   ##  ##    ##        ##     ##  ##     ##");
        LOGGER.info("##  ##   ####      ##       ##       ##          ##  ##  ##     ## ##    ##     ##  ##   ##  ##    ##        ##     ##  ##     ## ##");
        LOGGER.info("####     ##      ####     ####     ####    ######    #####      ###    ####     ####    ##  ##   ####      ####     ####       ###");

        if (database.isConnected()){
            database.disconnection();

            LOGGER.info(configMessages.getString("Messages.Database.success_disconnection"));
        }
    }

    public static CivilisationPlot getInstance(){
        return instance;
    }

    public DatabaseManager getDatabase(){
        return database;
    }

}
