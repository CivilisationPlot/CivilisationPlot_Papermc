package fr.laptoff.civilisationplot;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public final class CivilisationPlot extends JavaPlugin {

    public static final Logger LOGGER = Logger.getLogger("CivilisationPlot");
    private static CivilisationPlot instance;
    private FileConfiguration configMessages; //Messages Manager (at /resources/config/english.yml)

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        if (getConfig().getBoolean("Database.enable")){

            LOGGER.info(configMessages.getString("Messages.Database.success_connection"));
        }


        LOGGER.info("####              ##      ###       ##                         ##       ##                       ######    ###                ##");
        LOGGER.info("##  ##                      ##                                  ##                                 ##  ##    ##                ##");
        LOGGER.info("##       ##  ##    ###       ##      ###      #####    ####     #####    ###      ####    #####     ##  ##    ##      ####     #####");
        LOGGER.info("##       ##  ##     ##       ##       ##     ##           ##     ##       ##     ##  ##   ##  ##    #####     ##     ##  ##     ##");
        LOGGER.info("##       ##  ##     ##       ##       ##      #####    #####     ##       ##     ##  ##   ##  ##    ##        ##     ##  ##     ##");
        LOGGER.info("##  ##   ####      ##       ##       ##          ##  ##  ##     ## ##    ##     ##  ##   ##  ##    ##        ##     ##  ##     ## ##");
        LOGGER.info("####     ##      ####     ####     ####    ######    #####      ###    ####     ####    ##  ##   ####      ####     ####       ###");

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

    }

    public static CivilisationPlot getInstance(){
        return instance;
    }
    
}
