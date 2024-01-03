/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.chat;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.cmd.Argument;
import de.timesnake.basic.bukkit.util.chat.cmd.Sender;
import de.timesnake.library.basic.util.Loggers;
import de.timesnake.library.commands.CommandHandler;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.basis.CommandListenerBasis;
import de.timesnake.library.commands.extended.ExArguments;
import de.timesnake.library.commands.simple.Arguments;
import de.timesnake.library.extension.util.chat.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandManager extends CommandHandler<Sender, Argument, Arguments<Argument>, ExArguments<Argument>>
    implements de.timesnake.basic.bukkit.util.chat.cmd.CommandManager, CommandExecutor, Listener, TabCompleter {

  public CommandManager() {
    Server.registerListener(this, BasicBukkit.getPlugin());
  }

  @Override
  public boolean onCommand(@NotNull org.bukkit.command.CommandSender cmdSender, Command cmd, @NotNull String label, String[] args) {
    this.handleCommand(new CommandSender(cmdSender), cmd.getName().toLowerCase(), args);

    return false;
  }

  @Override
  public List<String> onTabComplete(org.bukkit.command.@NotNull CommandSender commandSender, Command command, @NotNull String alias, String[] args) {
    return this.handleTabCompletion(new CommandSender(commandSender), command.getName().toLowerCase(), args, args.length);
  }

  @Override
  public void addCommand(JavaPlugin mainClass, String cmd, CommandListenerBasis listener, Plugin basicPlugin) {
    this.addCommand(mainClass, cmd, List.of(), listener, basicPlugin);
  }

  @Override
  public void addCommand(JavaPlugin mainClass, String cmd, List<String> aliases, CommandListenerBasis listener, Plugin basicPlugin) {
    org.bukkit.command.PluginCommand bukkitCommand = mainClass.getCommand(cmd);
    if (bukkitCommand != null) {
      bukkitCommand.setExecutor(this);
      bukkitCommand.setTabCompleter(this);
      bukkitCommand.setAliases(aliases);
      bukkitCommand.setPermission(listener.getPermission());

      PluginCommand pluginCommand = new PluginCommand(cmd, basicPlugin);
      this.addCommand(cmd, listener, pluginCommand.getPlugin());
    } else {
      Loggers.COMMAND.warning("Error while adding command " + cmd + ". Not registered in plugin.yml");
    }
  }

  @Override
  public Sender createSender(de.timesnake.library.commands.CommandSender sender, PluginCommand cmd) {
    return new Sender((CommandSender) sender, cmd.getPlugin());
  }

  @Override
  public de.timesnake.library.commands.simple.Arguments<Argument> createArguments(Sender sender, String[] args) {
    return new Arguments(sender, args);
  }

  @Override
  public de.timesnake.library.commands.extended.ExArguments<Argument> createExArguments(Sender sender, String[] args, boolean allowDuplicates) {
    return new ExArguments(sender, args, allowDuplicates);
  }

  private static class Arguments extends de.timesnake.library.commands.simple.Arguments<Argument> {

    public Arguments(Sender sender, String[] args) {
      super(sender, args);
    }

    @Override
    public Argument createArgument(de.timesnake.library.commands.Sender sender, String arg) {
      return new Argument((Sender) sender, arg);
    }
  }

  private static class ExArguments extends de.timesnake.library.commands.extended.ExArguments<Argument> {

    public ExArguments(Sender sender, String[] args, boolean allowDuplicateOptions) {
      super(sender, args, allowDuplicateOptions);
    }

    @Override
    public Argument createArgument(de.timesnake.library.commands.Sender sender, String arg) {
      return new Argument(((Sender) sender), arg);
    }
  }
}
