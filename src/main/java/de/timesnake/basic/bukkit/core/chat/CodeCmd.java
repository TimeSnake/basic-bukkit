/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.chat;

import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.chat.cmd.Argument;
import de.timesnake.basic.bukkit.util.chat.cmd.ExCommandListener;
import de.timesnake.basic.bukkit.util.chat.cmd.ExCompletion;
import de.timesnake.basic.bukkit.util.chat.cmd.Sender;
import de.timesnake.library.chat.Code;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.extended.ExArguments;

public class CodeCmd implements ExCommandListener {

  private final Code perm = Plugin.SERVER.createPermssionCode("system.code");

  @Override
  public void onCommand(Sender sender, PluginCommand cmd, ExArguments<Argument> args) {
    sender.hasPermissionElseExit(this.perm);
    args.assertElseExit(a -> a.isLengthEquals(1, true));

    Argument codeArg = args.get(0);

    codeArg.assertElseExit(a -> a.isInt(true));

    Code code = Code.getCodeById().get(codeArg.toInt());

    if (code == null) {
      if (sender.isPlayer(false)) {
        sender.getPlayer().performCommand("/netcode " + codeArg);
      } else {
        sender.sendPluginTDMessage("§wNo valid code or proxy code");
      }
      return;
    }

    String message = "§sType: §v" + code.getType().getSymbol() + "§s, plugin: §v" + code.getPlugin().getCode();

    if (code.getCommand() != null) {
      message += "§s, cmd: §v" + code.getCommand();
    }

    if (code.getPermission() != null) {
      message += "§s, perm: §v" + code.getPermission();
    }

    if (code.getDescription() != null) {
      message += "§s, desc: §v" + code.getDescription();
    }

    if (args.containsFlag('v')) {
      if (code.getReference() != null) {
        message += "§s, ref: §v" + code.getReference().getName();
      }
    }

    sender.sendPluginTDMessage(message);
  }

  @Override
  public ExCompletion getTabCompletion() {
    return new ExCompletion(this.perm);
  }

  @Override
  public String getPermission() {
    return this.perm.getPermission();
  }
}
