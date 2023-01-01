/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.chat;

import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.CommandListener;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.library.basic.util.LogHelper;
import de.timesnake.library.basic.util.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Plugin;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.ExCommand;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;

public class LoggerCmd implements CommandListener {
    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (!sender.isConsole(true)) {
            return;
        }

        if (!args.isLengthEquals(2, true)) {
            return;
        }

        String loggerName = args.getString(0);

        Logger logger = LogHelper.LOGGER_BY_NAME.get(loggerName);

        if (logger == null) {
            sender.sendPluginMessage(Component.text("Logger ", ExTextColor.WARNING)
                    .append(Component.text(loggerName, ExTextColor.VALUE))
                    .append(Component.text(" not found", ExTextColor.WARNING)));
            return;
        }

        String levelName = args.getString(1).toUpperCase();

        Level level = Level.parse(levelName);

        if (level == null) {
            sender.sendPluginMessage(Component.text("Unable to parse log-level ", ExTextColor.WARNING)
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
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (args.length() == 1) {
            return new ArrayList<>(LogHelper.LOGGER_BY_NAME.keySet());
        } else if (args.length() == 1) {
            return Stream.of(Level.OFF, Level.SEVERE, Level.WARNING, Level.INFO, Level.ALL).map(Level::getName).toList();
        }
        return null;
    }

    @Override
    public void loadCodes(Plugin plugin) {

    }
}
