/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.chat;

import de.timesnake.basic.bukkit.util.chat.cmd.Argument;
import de.timesnake.basic.bukkit.util.chat.cmd.CommandListener;
import de.timesnake.basic.bukkit.util.chat.cmd.Completion;
import de.timesnake.basic.bukkit.util.chat.cmd.Sender;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;
import de.timesnake.library.permissions.Permission;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.List;
import java.util.stream.Stream;

public class LoggerCmd implements CommandListener {

  @Override
  public void onCommand(Sender sender, PluginCommand cmd, Arguments<Argument> args) {
    sender.isConsoleElseExit(true);
    args.isLengthEqualsElseExit(2, true);

    String loggerName = args.getString(0);
    String levelName = args.getString(1).toUpperCase();

    Level level;
    try {
      level = Level.getLevel(levelName);
    } catch (IllegalArgumentException e) {
      sender.sendPluginTDMessage("§wUnable to parse log-level §v" + loggerName);
      return;
    }

    Configurator.setAllLevels(loggerName, level);
    sender.sendPluginTDMessage("Updated log-level of §v" + loggerName + "§s to §v" + levelName);
  }

  @Override
  public Completion getTabCompletion() {
    return new Completion((sender, cmd, args) -> sender.isConsole(false) ? List.of("<name>") : List.of()).allowAny()
        .addArgument(new Completion("<name>").allowAny()
            .addArgument(new Completion(Stream.of(Level.OFF, Level.TRACE, Level.WARN, Level.INFO, Level.ALL).map(Level::name).toList())));
  }

  @Override
  public String getPermission() {
    return Permission.CONSOLE_PERM;
  }
}
