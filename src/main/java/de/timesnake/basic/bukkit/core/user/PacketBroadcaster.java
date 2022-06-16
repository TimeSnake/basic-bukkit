package de.timesnake.basic.bukkit.core.user;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.*;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.ExCommand;
import de.timesnake.library.packets.util.packet.ExPacket;
import org.bukkit.entity.Player;

import java.util.List;

public class PacketBroadcaster implements CommandListener {

    private boolean broadcast = false;

    public PacketBroadcaster() {
        Server.getCommandManager().addCommand(BasicBukkit.getPlugin(), "packetsmsg", List.of("packetsmsgs",
                "packetsmessage", "packetsmessages"), this, Plugin.PACKETS);
    }

    public void broadcastPacket(Player player, ExPacket packet) {
        if (this.broadcast) {
            Server.printText(Plugin.PACKETS, "Send " + packet.getClass().getSimpleName() + " " + packet.getInfo() +
                    " to: " + player.getName());
        }
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (sender.isConsole(true)) {
            this.broadcast = !this.broadcast;
            if (this.broadcast) {
                sender.sendPluginMessage(ChatColor.PERSONAL + "Enabled packet messages");
            } else {
                sender.sendPluginMessage(ChatColor.PERSONAL + "Disabled packet messages");
            }

            if (args.isLengthHigherEquals(1, false)) {
                if (args.get(0).equalsIgnoreCase("verbose")) {
                    Server.getPacketManager().setBroadcast(!Server.getPacketManager().isBroadcast());

                    if (Server.getPacketManager().isBroadcast()) {
                        sender.sendPluginMessage(ChatColor.PERSONAL + "Enabled verbose packet messages");
                    } else {
                        sender.sendPluginMessage(ChatColor.PERSONAL + "Disabled verbose packet messages");
                    }
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        return null;
    }
}
