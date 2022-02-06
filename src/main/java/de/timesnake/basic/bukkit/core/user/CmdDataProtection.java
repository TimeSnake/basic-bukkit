package de.timesnake.basic.bukkit.core.user;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.ChatColor;
import de.timesnake.basic.bukkit.util.chat.CommandListener;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;
import de.timesnake.database.util.user.DataProtectionAgreement;
import de.timesnake.library.basic.util.chat.Plugin;
import de.timesnake.library.basic.util.cmd.Arguments;
import de.timesnake.library.basic.util.cmd.ExCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Date;
import java.util.List;

public class CmdDataProtection implements CommandListener, Listener {

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (sender.isPlayer(true)) {
            User user = sender.getUser();
            if (args.isLengthHigherEquals(1, true)) {
                if (args.get(0).equalsIgnoreCase("accept") || args.get(0).equalsIgnoreCase("agree")) {

                    if (user.agreedDataProtection()) {
                        sender.sendPluginMessage(ChatColor.WARNING + "You already accepted our data protection declaration " + Server.getChat().getMessageCode("H", 740, Plugin.BUKKIT));
                        return;
                    }

                    user.agreeDataProtection(DataProtectionAgreement.create(new Date(), BasicBukkit.DATA_PROTECTION_VERSION));
                    sender.sendPluginMessage(ChatColor.PERSONAL + "You accepted our data protection declaration");

                    user.getPlayer().kickPlayer(ChatColor.PUBLIC + "You accepted our data protection declaration " + "\nPlease rejoin in a few moments");


                } else if (args.get(0).equalsIgnoreCase("deny") || args.get(0).equalsIgnoreCase("disagree")) {
                    user.delete();
                    user.getPlayer().kickPlayer("§4§lYou disagreed our data protection declaration. " + "You must accept our data protection declaration to play on our Network");

                }
            } else {
                sender.sendMessageCommandHelp("Agree", "dpd agree");
                sender.sendMessageCommandHelp("Disagree", "/dpd disagree");
            }
        }
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (args.getLength() == 0) {
            return List.of("accept", "deny");
        }
        return null;
    }

    @EventHandler
    public void onUserJoin(UserJoinEvent e) {
        User user = e.getUser();
        if (!user.agreedDataProtection()) {
            user.clearInventory();
        }
    }
}
