package de.timesnake.basic.bukkit.core.server;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.library.basic.util.chat.Plugin;
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
        Bukkit.getLogger().log(Level.INFO, "" + sb);
    }

    public final void printWarning(Plugin plugin, String warning, String... subPlugins) {
        StringBuilder sb = buildMessage(plugin, warning, subPlugins);
        Bukkit.getLogger().log(Level.WARNING, "§e" + sb);
    }

    public final void printError(Plugin plugin, String error, String... subPlugins) {
        StringBuilder sb = buildMessage(plugin, error, subPlugins);
        Bukkit.getLogger().log(Level.WARNING, "§c" + sb);
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
