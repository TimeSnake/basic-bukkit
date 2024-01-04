/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.server;

import de.timesnake.basic.bukkit.util.chat.cmd.Argument;
import de.timesnake.basic.bukkit.util.chat.cmd.CommandListener;
import de.timesnake.basic.bukkit.util.chat.cmd.Completion;
import de.timesnake.basic.bukkit.util.chat.cmd.Sender;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;
import de.timesnake.library.permissions.Permission;
import net.kyori.adventure.text.Component;

public class PidCmd implements CommandListener {

  @Override
  public void onCommand(Sender sender, PluginCommand cmd, Arguments<Argument> args) {
    if (!sender.isConsole(true)) {
      return;
    }

    sender.sendPluginMessage(Component.text("PID: ", ExTextColor.PERSONAL)
        .append(Component.text(ProcessHandle.current().pid(), ExTextColor.VALUE)));
  }

  @Override
  public Completion getTabCompletion() {
    return new Completion();
  }

  @Override
  public String getPermission() {
    return Permission.CONSOLE_PERM;
  }
}
