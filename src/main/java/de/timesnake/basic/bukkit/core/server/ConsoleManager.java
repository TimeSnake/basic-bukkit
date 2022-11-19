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

package de.timesnake.basic.bukkit.core.server;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.library.extension.util.chat.Plugin;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;

import java.util.List;

public class ConsoleManager {

    public void runCommand(String cmd) {
        if (cmd.equalsIgnoreCase("stop")) {
            BasicBukkit.getPlugin().onDisable();
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }

    public final void printText(Plugin plugin, String text, String... subPlugins) {
        StringBuilder sb = buildMessage(text, subPlugins);
        String msg = PlainTextComponentSerializer.plainText().serialize(
                LegacyComponentSerializer.legacySection().deserialize(sb.toString()));
        plugin.getLogger().info(msg);
    }

    public final void printWarning(Plugin plugin, String warning, String... subPlugins) {
        StringBuilder sb = buildMessage(warning, subPlugins);
        String msg = PlainTextComponentSerializer.plainText().serialize(
                LegacyComponentSerializer.legacySection().deserialize(sb.toString()));
        plugin.getLogger().warning(msg);
    }

    private StringBuilder buildMessage(String text, String[] subPlugins) {
        StringBuilder sb = new StringBuilder();
        for (String subPlugin : subPlugins) {
            sb.append("[");
            sb.append(subPlugin);
            sb.append("]");
        }
        sb.append(" ").append(text);
        return sb;
    }

    public final void printSection(Plugin plugin, String title, String... lines) {
        plugin.getLogger().info("---------- " + title + " ----------");
        for (String line : lines) {
            plugin.getLogger().info(line);
        }
        plugin.getLogger().info("---------- " + title + " ----------");
    }

    public final void printSection(Plugin plugin, String title, List<String> lines) {
        plugin.getLogger().info("---------- " + title + " ----------");
        for (String line : lines) {
            plugin.getLogger().info("[" + plugin.getName() + "] " + line);
        }
        plugin.getLogger().info("---------- " + title + " ----------");
    }

}
