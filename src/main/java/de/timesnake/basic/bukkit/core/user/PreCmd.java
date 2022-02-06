package de.timesnake.basic.bukkit.core.user;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;

public class PreCmd implements Listener {

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent e) {
        if (e.getMessage() != null) {
            String[] message = e.getMessage().split(" ");
            switch (message[0].toLowerCase()) {

                case "ban":
                case "tempban":
                case "tmpban":
                case "mute":
                case "kick":
                case "unban":
                case "unmute":
                case "pardon":
                    e.setCancelled(true);
                    Bukkit.dispatchCommand(e.getPlayer(), "net" + Arrays.toString(message));
                    break;
                case "clear":
                    e.setCancelled(true);
                    Bukkit.dispatchCommand(e.getPlayer(), "invclear");
                    break;
                case "stop":
                    e.setCancelled(true);
                    new BasicBukkit().onDisable();
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                    break;
            }
        }
    }

}
