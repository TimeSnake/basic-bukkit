/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.chat;

import de.timesnake.basic.bukkit.util.chat.cmd.Argument;
import de.timesnake.basic.bukkit.util.chat.cmd.CommandListener;
import de.timesnake.basic.bukkit.util.chat.cmd.Completion;
import de.timesnake.basic.bukkit.util.chat.cmd.Sender;
import de.timesnake.library.basic.util.LogHelper;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;
import de.timesnake.library.extension.util.permission.Permission;
import net.kyori.adventure.text.Component;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class LoggerCmd implements CommandListener {

  @Override
  public void onCommand(Sender sender, PluginCommand cmd, Arguments<Argument> args) {
    if (!sender.isConsole(true)) {
      return;
    }

    if (!args.isLengthEquals(2, true)) {
      return;
    }

    String loggerName = args.getString(0);

    Logger logger = LogHelper.LOGGER_BY_NAME.get(loggerName.toLowerCase());

    if (logger == null) {
      sender.sendPluginMessage(Component.text("Logger ", ExTextColor.WARNING)
          .append(Component.text(loggerName, ExTextColor.VALUE))
          .append(Component.text(" not found", ExTextColor.WARNING)));
      return;
    }

    String levelName = args.getString(1).toUpperCase();

    Level level;
    try {
      level = Level.parse(levelName);
    } catch (IllegalArgumentException e) {
      sender.sendPluginMessage(
          Component.text("Unable to parse log-level ", ExTextColor.WARNING)
              .append(Component.text(loggerName, ExTextColor.VALUE)));
      return;
    }

    logger.setLevel(level);
    sender.sendPluginMessage(Component.text("Updated log-level of ", ExTextColor.PERSONAL)
        .append(Component.text(loggerName, ExTextColor.VALUE))
        .append(Component.text(" to ", ExTextColor.PERSONAL))
        .append(Component.text(levelName, ExTextColor.VALUE)));
  }

  @Override
  public Completion getTabCompletion() {
    return new Completion()
        .addArgument(new Completion(LogHelper.LOGGER_BY_NAME.keySet())
            .addArgument(new Completion(Stream.of(Level.OFF, Level.SEVERE, Level.WARNING, Level.INFO, Level.ALL).map(Level::getName).toList())));
  }

  @Override
  public String getPermission() {
    return Permission.CONSOLE_PERM;
  }
}
