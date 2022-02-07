package de.timesnake.basic.bukkit.core.chat;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.CommandListener;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.library.basic.util.chat.Plugin;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.ExCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CommandManager implements de.timesnake.basic.bukkit.util.chat.CommandManager, CommandExecutor {

    private final HashMap<String, ExCommand<Sender, Argument>> commands = new HashMap<>();

    private final TabCompleteManager tabCompleteManager = new TabCompleteManager();

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String label, String[] args) {

        if (this.commands.containsKey(cmd.getName())) {
            ExCommand<Sender, Argument> basicCmd = this.commands.get(cmd.getName().toLowerCase());

            de.timesnake.basic.bukkit.util.chat.Sender sender = new Sender(new ExCommandSender(cmdSender), basicCmd.getPlugin());

            LinkedList<de.timesnake.basic.bukkit.util.chat.Argument> extendedArgs = new LinkedList<>();

            for (String arg : args) {
                extendedArgs.addLast(new Argument(sender, arg));
            }
            basicCmd.getListener().onCommand(sender, basicCmd, new Arguments<>(sender, extendedArgs));
        }
        return false;
    }

    @Override
    public void addCommand(JavaPlugin mainClass, String cmd, CommandListener listener, Plugin basicPlugin) {

        ExCommand<Sender, Argument> basicCmd = new ExCommand<>(cmd, listener, basicPlugin);

        this.commands.put(cmd, basicCmd);

        PluginCommand pluginCommand = mainClass.getCommand(cmd);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(tabCompleteManager);
            Server.printText(Plugin.BUKKIT, "Registered command " + cmd + " successfully", "Command");
        } else {
            Server.printWarning(Plugin.BUKKIT, "Error while adding command " + cmd + ". Not registered in " + "plugin.yml", "Command");
        }
    }

    @Override
    public void addCommand(JavaPlugin mainClass, String cmd, List<String> aliases, CommandListener listener, Plugin basicPlugin) {
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
}
