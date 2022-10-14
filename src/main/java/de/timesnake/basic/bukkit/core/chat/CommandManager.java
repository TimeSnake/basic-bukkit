/*
 * basic-bukkit.main
 * Copyright (C) 2022 timesnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; If not, see <http://www.gnu.org/licenses/>.
 */

package de.timesnake.basic.bukkit.core.chat;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.CommandListener;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.library.basic.util.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Plugin;
import de.timesnake.library.extension.util.cmd.ArgumentParseException;
import de.timesnake.library.extension.util.cmd.CommandExitException;
import de.timesnake.library.extension.util.cmd.DuplicateOptionException;
import de.timesnake.library.extension.util.cmd.ExCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CommandManager implements de.timesnake.basic.bukkit.util.chat.CommandManager, CommandExecutor {

    private final HashMap<String, ExCommand<Sender, Argument>> commands = new HashMap<>();

    private final TabCompleteManager tabCompleteManager = new TabCompleteManager();

    @Override
    public boolean onCommand(@NotNull CommandSender cmdSender, Command cmd, @NotNull String label, String[] args) {
        String cmdName = cmd.getName().toLowerCase();

        if (this.commands.containsKey(cmdName)) {
            ExCommand<Sender, Argument> basicCmd = this.commands.get(cmdName);

            de.timesnake.basic.bukkit.util.chat.Sender sender = new Sender(new ExCommandSender(cmdSender),
                    basicCmd.getPlugin());
            try {
                switch (basicCmd.getListener().getArgumentType(cmdName, args)) {
                    case DEFAULT -> basicCmd.getListener().onCommand(sender, basicCmd, new Arguments(sender, args));
                    case EXTENDED -> basicCmd.getListener().onCommand(sender, basicCmd, new ExArguments(sender, args,
                            basicCmd.getListener().allowDuplicates(cmdName, args)));
                }
            } catch (CommandExitException ignored) {

            } catch (ArgumentParseException | DuplicateOptionException e) {
                sender.sendPluginMessage(Component.text(e.getMessage(), ExTextColor.WARNING));
            }
        }
        return false;
    }

    @Override
    public void addCommand(JavaPlugin mainClass, String cmd, CommandListener listener, Plugin basicPlugin) {
        listener.loadCodes(basicPlugin);
        ExCommand<Sender, Argument> basicCmd = new ExCommand<>(cmd, listener, basicPlugin);

        this.commands.put(cmd, basicCmd);

        PluginCommand pluginCommand = mainClass.getCommand(cmd);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(tabCompleteManager);
        } else {
            Server.printWarning(Plugin.BUKKIT, "Error while adding command " + cmd + ". Not registered in " + "plugin" +
                    ".yml", "Command");
        }
    }

    @Override
    public void addCommand(JavaPlugin mainClass, String cmd, List<String> aliases, CommandListener listener,
                           Plugin basicPlugin) {
        listener.loadCodes(basicPlugin);
        this.addCommand(mainClass, cmd, listener, basicPlugin);

        for (String alias : aliases) {
            this.addCommand(mainClass, alias, listener, basicPlugin);
        }
    }

    public HashMap<String, ExCommand<Sender, Argument>> getCommands() {
        return commands;
    }

    @Override
    public de.timesnake.basic.bukkit.util.chat.TabCompleteManager getTabCompleter() {
        return this.tabCompleteManager;
    }

    public static class Arguments extends de.timesnake.library.extension.util.cmd.Arguments<Argument> {

        public Arguments(Sender sender, String[] args) {
            super(sender, args);
        }

        public Arguments(Sender sender, LinkedList<Argument> args) {
            super(sender, args);
        }

        public Arguments(LinkedList<Argument> args) {
            super(args.getFirst().getSender());
        }

        public Arguments(de.timesnake.library.extension.util.cmd.Arguments<Argument> args) {
            super(args);
        }

        public Arguments(Sender sender, Argument args) {
            super(sender, args);
        }

        @Override
        public Argument createArgument(de.timesnake.library.extension.util.cmd.Sender sender, String arg) {
            return new Argument((Sender) sender, arg);
        }
    }

    public static class ExArguments extends de.timesnake.library.extension.util.cmd.ExArguments<Argument> {

        public ExArguments(de.timesnake.library.extension.util.cmd.Sender sender, String[] args,
                           boolean allowDuplicateOptions) {
            super(sender, args, allowDuplicateOptions);
        }

        @Override
        public Argument createArgument(de.timesnake.library.extension.util.cmd.Sender sender, String arg) {
            return new Argument(((Sender) sender), arg);
        }
    }
}
