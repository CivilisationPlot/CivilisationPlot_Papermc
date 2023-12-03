package fr.laptoff.civilisationplot.civils;

import fr.laptoff.civilisationplot.Managers.ConfigManager;
import fr.laptoff.civilisationplot.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class joinListener implements org.bukkit.event.Listener {

    private static final ConfigManager configMessages = new ConfigManager("Messages.yml");
    private static final FileConfiguration config = configMessages.getFileConfiguration();

    @EventHandler
    public static void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        if (!Civil.isExist(player.getUniqueId())){
            Civil civil = new Civil(player.getName(), Economy.getStartMoneyBalance(), null);
            civil.save();
            player.sendMessage(config.getString("Messages.Join.first_join"));
        } else {
            Civil civil = Civil.getCivil(player.getUniqueId());
            player.sendMessage(config.getString("Messages.Join.other_join"));
        }
    }
}
