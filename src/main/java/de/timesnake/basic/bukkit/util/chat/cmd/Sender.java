/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.chat.cmd;

import de.timesnake.basic.bukkit.core.chat.CommandSender;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.database.util.user.DbUser;
import de.timesnake.library.basic.util.Loggers;
import de.timesnake.library.chat.Plugin;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class Sender extends de.timesnake.library.commands.Sender {

  public Sender(CommandSender cmdSender, Plugin plugin) {
    super(cmdSender, plugin, Server.getTimeDownParser());
  }

  public Sender(Player player, Plugin plugin) {
    super(new CommandSender(player), plugin, Server.getTimeDownParser());
  }

  public Component getChatName() {
    if (this.isPlayer(false)) {
      return this.getUser().getChatNameComponent();
    } else if (this.isConsole(false)) {
      return Component.text(Plugin.SERVER.getName());
    }
    return null;
  }

  public Player getPlayer() {
    return cmdSender.getPlayer();
  }

  public User getUser() {
    return Server.getUser(this.getPlayer());
  }

  @Override
  public void sendConsoleMessage(String message) {
    Loggers.COMMAND.info(message);
  }

  @Override
  public DbUser getDbUser() {
    return this.getUser().getDatabase();
  }
}
