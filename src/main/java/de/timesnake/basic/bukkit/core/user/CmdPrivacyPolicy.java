/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.core.user;

import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.CommandListener;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;
import de.timesnake.library.basic.util.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Code;
import de.timesnake.library.extension.util.chat.Plugin;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.ExCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class CmdPrivacyPolicy implements CommandListener, Listener {

    private Code.Help alreadyAccepted;

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (sender.isPlayer(true)) {
            User user = sender.getUser();
            if (args.isLengthHigherEquals(1, true)) {
                if (args.get(0).equalsIgnoreCase("accept") || args.get(0).equalsIgnoreCase("agree")) {

                    if (user.agreedPrivacyPolicy()) {
                        sender.sendPluginMessage(Component.text("You already accepted our privacy policy", ExTextColor.WARNING)
                                .append(alreadyAccepted.asComponent(ExTextColor.WARNING)));
                        return;
                    }

                    user.agreePrivacyPolicy();
                    sender.sendPluginMessage(Component.text("You accepted our privacy policy", ExTextColor.PERSONAL));

                    user.getPlayer().kick(Component.text("You accepted our data privacy policy " +
                                                         "\nPlease rejoin in a few moments", ExTextColor.PUBLIC));


                } else if (args.get(0).equalsIgnoreCase("deny") || args.get(0).equalsIgnoreCase("disagree")) {
                    user.delete();
                    user.getPlayer().kick(Component.text("You disagreed our privacy policy. You must " +
                                                         "accept our privacy policy to play on our Network", ExTextColor.RED, TextDecoration.BOLD));

                }
            } else {
                sender.sendMessageCommandHelp("Agree", "pp agree");
                sender.sendMessageCommandHelp("Disagree", "pp disagree");
            }
        }
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (args.getLength() == 1) {
            return List.of("accept", "deny");
        }
        return null;
    }

    @Override
    public void loadCodes(Plugin plugin) {
        this.alreadyAccepted = plugin.createHelpCode("aad", "Already accepted privacy policy");
    }

    @EventHandler
    public void onUserJoin(UserJoinEvent e) {
        User user = e.getUser();
        if (!user.agreedPrivacyPolicy()) {
            user.clearInventory();
        }
    }
}
