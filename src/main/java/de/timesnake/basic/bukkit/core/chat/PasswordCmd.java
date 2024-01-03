/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.chat;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.cmd.Argument;
import de.timesnake.basic.bukkit.util.chat.cmd.CommandListener;
import de.timesnake.basic.bukkit.util.chat.cmd.Completion;
import de.timesnake.basic.bukkit.util.chat.cmd.Sender;
import de.timesnake.database.util.object.TooLongEntryException;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;
import de.timesnake.library.extension.util.chat.Code;
import de.timesnake.library.extension.util.chat.Plugin;

public class PasswordCmd implements CommandListener {

  private final Code perm = Plugin.SYSTEM.createPermssionCode("password");

  @Override
  public void onCommand(Sender sender, PluginCommand cmd, Arguments<Argument> args) {
    if (!sender.hasPermission(this.perm)) {
      return;
    }

    if (!args.isLengthLowerEquals(1, true)) {
      return;
    }

    String password = null;

    if (args.length() == 1) {
      password = args.getString(0);
    }

    try {
      Server.setPassword(password);
    } catch (TooLongEntryException e) {
      sender.sendPluginTDMessage("§wCould not set password, too long");
      return;
    }

    sender.sendPluginTDMessage("§sUpdated password");
  }

  @Override
  public Completion getTabCompletion() {
    return new Completion(this.perm)
        .addArgument(new Completion("password"));
  }

  @Override
  public String getPermission() {
    return this.perm.getPermission();
  }
}
