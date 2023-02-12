/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.CommandListener;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.ExCommand;
import de.timesnake.library.packets.util.packet.ExPacket;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class PacketBroadcaster implements CommandListener {

    private boolean broadcast = false;

    public PacketBroadcaster() {
        Server.getCommandManager()
                .addCommand(BasicBukkit.getPlugin(), "packetsmsg", List.of("packetsmsgs",
                        "packetsmessage", "packetsmessages"), this, Plugin.PACKETS);
    }

    public void broadcastPacket(Player player, ExPacket packet) {
        if (this.broadcast) {
            Plugin.PACKETS.getLogger()
                    .info("Send " + packet.getClass().getSimpleName() + " " + packet.getInfo() +
                            " to: " + player.getName());
        }
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd,
            Arguments<Argument> args) {
        if (sender.isConsole(true)) {
            this.broadcast = !this.broadcast;
            if (this.broadcast) {
                sender.sendPluginMessage(
                        Component.text("Enabled packet messages", ExTextColor.PERSONAL));
            } else {
                sender.sendPluginMessage(
                        Component.text("Disabled packet messages", ExTextColor.PERSONAL));
            }

            if (args.isLengthHigherEquals(1, false)) {
                if (args.get(0).equalsIgnoreCase("verbose")) {
                    Server.getPacketManager()
                            .setBroadcast(!Server.getPacketManager().isBroadcast());

                    if (Server.getPacketManager().isBroadcast()) {
                        sender.sendPluginMessage(Component.text("Enabled verbose packet messages",
                                ExTextColor.PERSONAL));
                    } else {
                        sender.sendPluginMessage(Component.text("Disabled verbose packet messages",
                                ExTextColor.PERSONAL));
                    }
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd,
            Arguments<Argument> args) {
        return null;
    }

    @Override
    public void loadCodes(de.timesnake.library.extension.util.chat.Plugin plugin) {

    }
}
