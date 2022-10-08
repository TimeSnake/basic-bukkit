/*
 * basic-bukkit.main
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
import java.util.logging.Level;

public class ConsoleManager {

    public void runCommand(String cmd) {
        if (cmd.equalsIgnoreCase("stop")) {
            BasicBukkit.getPlugin().onDisable();
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }

    public final void printText(Plugin plugin, String text, String... subPlugins) {
        StringBuilder sb = buildMessage(plugin, text, subPlugins);
        String msg = PlainTextComponentSerializer.plainText().serialize(
                LegacyComponentSerializer.legacySection().deserialize(sb.toString()));
        Bukkit.getLogger().log(Level.INFO, msg);
    }

    public final void printWarning(Plugin plugin, String warning, String... subPlugins) {
        StringBuilder sb = buildMessage(plugin, warning, subPlugins);
        String msg = PlainTextComponentSerializer.plainText().serialize(
                LegacyComponentSerializer.legacySection().deserialize(sb.toString()));
        Bukkit.getLogger().log(Level.WARNING, msg);
    }

    public final void printError(Plugin plugin, String error, String... subPlugins) {
        StringBuilder sb = buildMessage(plugin, error, subPlugins);
        String msg = PlainTextComponentSerializer.plainText().serialize(
                LegacyComponentSerializer.legacySection().deserialize(sb.toString()));
        Bukkit.getLogger().log(Level.WARNING, msg);
    }

    private StringBuilder buildMessage(Plugin plugin, String text, String[] subPlugins) {
        StringBuilder sb = new StringBuilder("[" + plugin.getName() + "]");
        for (String subPlugin : subPlugins) {
            sb.append("[");
            sb.append(subPlugin);
            sb.append("]");
        }
        sb.append(" ").append(text);
        return sb;
    }

    public final void printSection(Plugin plugin, String title, String... lines) {
        Bukkit.getLogger().log(Level.INFO, "---------- " + title + " ----------");
        for (String line : lines) {
            Bukkit.getLogger().log(Level.INFO, "[" + plugin.getName() + "]" + line);
        }
        Bukkit.getLogger().log(Level.INFO, "---------- " + title + " ----------");
    }

    public final void printSection(Plugin plugin, String title, List<String> lines) {
        Bukkit.getLogger().log(Level.INFO, "---------- " + title + " ----------");
        for (String line : lines) {
            Bukkit.getLogger().log(Level.INFO, "[" + plugin.getName() + "] " + line);
        }
        Bukkit.getLogger().log(Level.INFO, "---------- " + title + " ----------");
    }

}
