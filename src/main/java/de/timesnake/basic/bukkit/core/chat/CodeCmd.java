/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.chat;

import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.ExCommandListener;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Code;
import de.timesnake.library.extension.util.cmd.ExArguments;
import de.timesnake.library.extension.util.cmd.ExCommand;
import java.util.List;
import net.kyori.adventure.text.Component;

public class CodeCmd implements ExCommandListener {

  private final Code perm = Plugin.SYSTEM.createPermssionCode("system.code");

  @Override
  public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd,
      ExArguments<Argument> args) {
    sender.hasPermissionElseExit(this.perm);
    args.assertElseExit(a -> a.isLengthEquals(1, true));

    Argument codeArg = args.get(0);

    codeArg.assertElseExit(a -> a.isInt(true));

    Code code = Code.getCodeById().get(codeArg.toInt());

    if (code == null) {
      if (sender.isPlayer(false)) {
        sender.getPlayer().performCommand("/netcode " + codeArg);
      } else {
        sender.sendPluginMessage(
            Component.text("No valid code or proxy code", ExTextColor.WARNING));
      }
      return;
    }

    Component message = Component.text("Type: ", ExTextColor.PERSONAL)
        .append(Component.text(code.getType().getSymbol(), ExTextColor.VALUE))
        .append(Component.text(" Plugin: ", ExTextColor.PERSONAL))
        .append(Component.text(code.getPlugin().getCode(), ExTextColor.VALUE));

    if (code.getCommand() != null) {
      message = message.append(Component.text(" Cmd: ", ExTextColor.PERSONAL))
          .append(Component.text(code.getCommand(), ExTextColor.VALUE));
    }

    if (code.getPermission() != null) {
      message = message.append(Component.text(" Perm: ", ExTextColor.PERSONAL))
          .append(Component.text(code.getPermission(), ExTextColor.VALUE));
    }

    if (code.getDescription() != null) {
      message = message.append(Component.text(" Desc: ", ExTextColor.PERSONAL))
          .append(Component.text(code.getDescription(), ExTextColor.VALUE));
    }

    if (args.containsFlag('v')) {
      if (code.getReference() != null) {
        message = message.append(Component.text(" Ref: ", ExTextColor.PERSONAL))
            .append(Component.text(code.getReference().getName(), ExTextColor.VALUE));
      }
    }

    sender.sendPluginMessage(message);
  }

  @Override
  public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd,
      ExArguments<Argument> args) {
    return List.of();
  }
}
