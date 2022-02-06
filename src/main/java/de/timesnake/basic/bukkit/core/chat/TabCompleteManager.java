package de.timesnake.basic.bukkit.core.chat;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.basic.bukkit.util.permission.Group;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import de.timesnake.database.util.Database;
import de.timesnake.database.util.game.DbGame;
import de.timesnake.database.util.game.DbMap;
import de.timesnake.library.basic.util.cmd.Arguments;
import de.timesnake.library.basic.util.cmd.ExCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TabCompleteManager implements TabCompleter, de.timesnake.basic.bukkit.util.chat.TabCompleteManager {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {

        if (!((CommandManager) Server.getCommandManager()).getCommands().containsKey(command.getName())) {
            return null;
        }

        ExCommand<Sender, Argument> cmdListener = ((CommandManager) Server.getCommandManager()).getCommands().get(command.getName().toLowerCase());

        Sender sender = new Sender(new ExCommandSender(commandSender), cmdListener.getPlugin());

        LinkedList<Argument> extendedArgs = new LinkedList<>();
        for (String arg : args) {
            extendedArgs.add(new Argument(sender, arg));
        }
        return cmdListener.getListener().getTabCompletion(cmdListener, new Arguments<>(sender, extendedArgs));
    }

    @Override
    public List<String> getPlayerNames() {
        List<String> names = new ArrayList<>();
        for (User user : Server.getUsers()) {
            if (user.isAirMode()) {
                continue;
            }
            names.add(user.getName());
        }
        return names;
    }

    @Override
    public List<String> getPermGroupNames() {
        List<String> names = new ArrayList<>();
        for (Group group : Server.getGroups()) {
            names.add(group.getName());
        }
        return names;
    }

    @Override
    public List<String> getWorldNames() {
        List<String> names = new ArrayList<>();
        for (ExWorld world : Server.getWorlds()) {
            names.add(world.getName());
        }
        return names;
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
