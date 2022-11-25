/*
 * workspace.basic-bukkit.main
 * Copyright (C) 2022 timesnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; If not, see <http://www.gnu.org/licenses/>.
 */

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
