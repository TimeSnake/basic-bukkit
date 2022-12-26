/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.core.chat;

import de.timesnake.basic.bukkit.core.user.UserPlayerDelegation;
import de.timesnake.basic.bukkit.core.world.DelegatedWorld;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.basic.bukkit.util.group.DisplayGroup;
import de.timesnake.basic.bukkit.util.group.PermGroup;
import de.timesnake.database.util.Database;
import de.timesnake.database.util.game.DbGame;
import de.timesnake.database.util.game.DbMap;
import de.timesnake.library.extension.util.cmd.ArgumentParseException;
import de.timesnake.library.extension.util.cmd.CommandExitException;
import de.timesnake.library.extension.util.cmd.DuplicateOptionException;
import de.timesnake.library.extension.util.cmd.ExCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TabCompleteManager implements TabCompleter, de.timesnake.basic.bukkit.util.chat.TabCompleteManager {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {

        String cmdName = command.getName().toLowerCase();

        if (!((CommandManager) Server.getCommandManager()).getCommands().containsKey(cmdName)) {
            return null;
        }

        ExCommand<Sender, Argument> basicCmd =
                ((CommandManager) Server.getCommandManager()).getCommands().get(cmdName);

        Sender sender = new Sender(new ExCommandSender(commandSender), basicCmd.getPlugin());

        try {
            return switch (basicCmd.getListener().getArgumentType(cmdName, args)) {
                case DEFAULT ->
                        basicCmd.getListener().getTabCompletion(basicCmd, new CommandManager.Arguments(sender, args));
                case EXTENDED ->
                        basicCmd.getListener().getTabCompletion(basicCmd, new CommandManager.ExArguments(sender, args,
                                basicCmd.getListener().allowDuplicates(cmdName, args)));
            };
        } catch (CommandExitException | ArgumentParseException | DuplicateOptionException ignored) {
        }
        return List.of();
    }

    @Override
    public List<String> getPlayerNames() {
        return Server.getUsers().stream().filter(user -> !user.isAirMode()).map(UserPlayerDelegation::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getPermGroupNames() {
        return Server.getPermGroups().stream().map(PermGroup::getName).collect(Collectors.toList());
    }

    public List<String> getDisplayGroupNames() {
        return Server.getDisplayGroups().stream().map(DisplayGroup::getName).collect(Collectors.toList());
    }

    @Override
    public List<String> getWorldNames() {
        return Server.getWorlds().stream().map(DelegatedWorld::getName).collect(Collectors.toList());
    }

    @Override
    public List<String> getMapNames(String gameName) {
        if (gameName == null) {
            return List.of();
        }

        DbGame game = Database.getGames().getGame(gameName);
        if (game == null) {
            return List.of();
        }

        List<String> names = new ArrayList<>();
        for (DbMap map : game.getMaps()) {
            names.add(map.getName());
        }
        return names;
    }

    @Override
    public List<String> getGameNames() {
        return new ArrayList<>(Database.getGames().getGamesName());
    }

}
