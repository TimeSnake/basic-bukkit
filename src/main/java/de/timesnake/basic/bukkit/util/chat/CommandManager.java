/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.util.chat;

import de.timesnake.library.extension.util.chat.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public interface CommandManager {

    void addCommand(JavaPlugin mainClass, String cmd, CommandListener listener, Plugin basicPlugin);

    void addCommand(JavaPlugin mainClass, String cmd, List<String> aliases, CommandListener listener,
                    Plugin basicPlugin);

    TabCompleteManager getTabCompleter();
}
