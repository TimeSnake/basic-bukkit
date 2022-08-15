package de.timesnake.basic.bukkit.core.database;

import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.CommandListener;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.database.util.Database;
import de.timesnake.library.basic.util.chat.ExTextColor;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.ExCommand;
import net.kyori.adventure.text.Component;

import java.util.List;

public class DatabaseBroadcastCmd implements CommandListener {

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (sender.isConsole(true)) {
            Database.setBroadcast(!Database.isBroadcast());
            sender.sendPluginMessage(Component.text("Broadcast database-messages: ", ExTextColor.PERSONAL)
                    .append(Component.text(Database.isBroadcast(), ExTextColor.VALUE)));
        }
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        return null;
    }
}
