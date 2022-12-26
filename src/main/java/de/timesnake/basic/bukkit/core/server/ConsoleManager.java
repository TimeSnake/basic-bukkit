/*
 * Copyright (C) 2022 timesnake
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
            plugin.getLogger().info(line);
        }
        plugin.getLogger().info("---------- " + title + " ----------");
    }

}
