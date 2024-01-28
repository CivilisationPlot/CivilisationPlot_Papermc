package fr.laptoff.civilisationplot;

import fr.laptoff.civilisationplot.managers.datas.ConfigManager;
import fr.laptoff.civilisationplot.managers.datas.DatabaseManager;
import fr.laptoff.civilisationplot.managers.civils.Civil;
import fr.laptoff.civilisationplot.managers.civils.joinListener;
import fr.laptoff.civilisationplot.managers.nation.Nation;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public final class CivilisationPlot extends JavaPlugin {

    public static final Logger LOGGER = Logger.getLogger("CivilisationPlot");
    private static CivilisationPlot instance;
    private DatabaseManager database;
    private FileConfiguration configMessages; //Messages Manager (at /resources/config/english.yml)

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        ConfigManager configManagerMessages = new ConfigManager(getConfig().getString("Language.language"));
        configMessages = configManagerMessages.getFileConfiguration();

        if (getConfig().getBoolean("Database.enable")){
            database = new DatabaseManager();
            database.connection();

            database.setup();

            LOGGER.info(configMessages.getString("Messages.Database.success_connection"));
        }

        Civil.load();
        Nation.load();

        LOGGER.info("####              ##      ###       ##                         ##       ##                       ######    ###                ##");
        LOGGER.info("##  ##                      ##                                  ##                                 ##  ##    ##                ##");
        LOGGER.info("##       ##  ##    ###       ##      ###      #####    ####     #####    ###      ####    #####     ##  ##    ##      ####     #####");
        LOGGER.info("##       ##  ##     ##       ##       ##     ##           ##     ##       ##     ##  ##   ##  ##    #####     ##     ##  ##     ##");
        LOGGER.info("##       ##  ##     ##       ##       ##      #####    #####     ##       ##     ##  ##   ##  ##    ##        ##     ##  ##     ##");
        LOGGER.info("##  ##   ####      ##       ##       ##          ##  ##  ##     ## ##    ##     ##  ##   ##  ##    ##        ##     ##  ##     ## ##");
        LOGGER.info("####     ##      ####     ####     ####    ######    #####      ###    ####     ####    ##  ##   ####      ####     ####       ###");

        getServer().getPluginManager().registerEvents(new joinListener(), this);
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

        if (DatabaseManager.isOnline()){
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
