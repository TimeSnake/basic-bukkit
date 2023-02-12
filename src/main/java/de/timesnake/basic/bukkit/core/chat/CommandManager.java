/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.chat;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.CommandListener;
import de.timesnake.basic.bukkit.util.chat.ExCommandListener;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Plugin;
import de.timesnake.library.extension.util.cmd.ArgumentParseException;
import de.timesnake.library.extension.util.cmd.CommandExitException;
import de.timesnake.library.extension.util.cmd.CommandListenerBasis;
import de.timesnake.library.extension.util.cmd.DuplicateOptionException;
import de.timesnake.library.extension.util.cmd.ExCommand;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CommandManager implements de.timesnake.basic.bukkit.util.chat.CommandManager,
        CommandExecutor {

    private final HashMap<String, ExCommand<Sender, Argument>> commands = new HashMap<>();

    private final TabCompleteManager tabCompleteManager = new TabCompleteManager();

    @Override
    public boolean onCommand(@NotNull CommandSender cmdSender, Command cmd, @NotNull String label,
            String[] args) {
        String cmdName = cmd.getName().toLowerCase();

        if (this.commands.containsKey(cmdName)) {
            ExCommand<Sender, Argument> basicCmd = this.commands.get(cmdName);

            de.timesnake.basic.bukkit.util.chat.Sender sender = new Sender(
                    new ExCommandSender(cmdSender),
                    basicCmd.getPlugin());
            try {
                if (basicCmd.getListener() instanceof CommandListener listener) {
                    listener.onCommand(sender, basicCmd, new Arguments(sender, args));
                } else if (basicCmd.getListener() instanceof ExCommandListener listener) {
                    listener.onCommand(sender, basicCmd, new ExArguments(sender, args,
                            listener.allowDuplicates(cmdName, args)));
                }
            } catch (CommandExitException ignored) {

            } catch (ArgumentParseException | DuplicateOptionException e) {
                sender.sendPluginMessage(Component.text(e.getMessage(), ExTextColor.WARNING));
            }
        }
        return false;
    }

    @Override
    public void addCommand(JavaPlugin mainClass, String cmd,
            CommandListenerBasis<? extends Sender, ? extends Argument> listener,
            Plugin basicPlugin) {
        listener.loadCodes(basicPlugin);
        ExCommand<Sender, Argument> basicCmd = new ExCommand<>(cmd, listener, basicPlugin);

        this.commands.put(cmd, basicCmd);

        PluginCommand pluginCommand = mainClass.getCommand(cmd);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(tabCompleteManager);
        } else {
            Server.printWarning(Plugin.BUKKIT,
                    "Error while adding command " + cmd + ". Not registered in " + "plugin" +
                            ".yml", "Command");
        }
    }

    @Override
    public void addCommand(JavaPlugin mainClass, String cmd, List<String> aliases,
            CommandListenerBasis<? extends Sender, ? extends Argument> listener,
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

    public static class Arguments extends
            de.timesnake.library.extension.util.cmd.Arguments<Argument> {

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
        public Argument createArgument(de.timesnake.library.extension.util.cmd.Sender sender,
                String arg) {
            return new Argument((Sender) sender, arg);
        }
    }

    public static class ExArguments extends
            de.timesnake.library.extension.util.cmd.ExArguments<Argument> {

        public ExArguments(de.timesnake.library.extension.util.cmd.Sender sender, String[] args,
                boolean allowDuplicateOptions) {
            super(sender, args, allowDuplicateOptions);
        }

        @Override
        public Argument createArgument(de.timesnake.library.extension.util.cmd.Sender sender,
                String arg) {
            return new Argument(((Sender) sender), arg);
        }
    }
}
