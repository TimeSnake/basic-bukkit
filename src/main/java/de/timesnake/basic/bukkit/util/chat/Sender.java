/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.chat;

import de.timesnake.basic.bukkit.core.chat.ExCommandSender;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.library.basic.util.Loggers;
import de.timesnake.library.extension.util.chat.Plugin;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class Sender extends de.timesnake.library.extension.util.cmd.Sender {

  public Sender(ExCommandSender cmdSender, Plugin plugin) {
    super(cmdSender, plugin, Server.getTimeDownParser());
  }

  public Sender(Player player, Plugin plugin) {
    super(new ExCommandSender(player), plugin, Server.getTimeDownParser());
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
}
