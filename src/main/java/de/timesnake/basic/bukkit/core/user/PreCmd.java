/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PreCmd implements Listener {

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent e) {
        if (e.getMessage() != null) {
            String[] message = e.getMessage().split(" ");
            switch (message[0].toLowerCase()) {
                case "ban", "tempban", "tmpban", "mute", "kick", "unban", "unmute", "pardon" -> {
                    e.setCancelled(true);
                    Bukkit.dispatchCommand(e.getPlayer(), "net" + Arrays.toString(message));
                }
                case "clear" -> {
                    e.setCancelled(true);
                    Bukkit.dispatchCommand(e.getPlayer(), "invclear");
                }
                case "stop" -> {
                    e.setCancelled(true);
                    new BasicBukkit().onDisable();
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                }
            }
        }
    }

}
