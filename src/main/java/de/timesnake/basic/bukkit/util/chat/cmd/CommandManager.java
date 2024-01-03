/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.chat.cmd;

import de.timesnake.library.commands.basis.CommandListenerBasis;
import de.timesnake.library.extension.util.chat.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public interface CommandManager {

  void addCommand(JavaPlugin mainClass, String cmd, CommandListenerBasis listener, Plugin basicPlugin);

  void addCommand(JavaPlugin mainClass, String cmd, List<String> aliases, CommandListenerBasis listener, Plugin basicPlugin);

}
