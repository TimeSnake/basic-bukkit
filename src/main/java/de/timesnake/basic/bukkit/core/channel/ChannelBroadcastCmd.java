package de.timesnake.basic.bukkit.core.channel;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.CommandListener;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.library.basic.util.chat.ExTextColor;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.ExCommand;
import net.kyori.adventure.text.Component;

import java.util.List;

public class ChannelBroadcastCmd implements CommandListener {

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (sender.isConsole(true)) {

            Server.getChannel().printInfoLog(!Server.getChannel().isPrintingInfoLog());
            sender.sendPluginMessage(Component.text("Broadcast channel-messages: ", ExTextColor.PERSONAL)
                    .append(Component.text(Server.getChannel().isPrintingInfoLog(), ExTextColor.VALUE)));
        }
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        return null;
    }
}
