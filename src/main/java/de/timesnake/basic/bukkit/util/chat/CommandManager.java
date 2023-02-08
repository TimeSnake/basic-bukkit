/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.chat;

import de.timesnake.library.extension.util.chat.Plugin;
import de.timesnake.library.extension.util.cmd.CommandListenerBasis;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;

public interface CommandManager {

    void addCommand(JavaPlugin mainClass, String cmd,
            CommandListenerBasis<? extends Sender, ? extends Argument> listener,
            Plugin basicPlugin);

    void addCommand(JavaPlugin mainClass, String cmd, List<String> aliases,
            CommandListenerBasis<? extends Sender, ? extends Argument> listener,
            Plugin basicPlugin);

    TabCompleteManager getTabCompleter();
}
