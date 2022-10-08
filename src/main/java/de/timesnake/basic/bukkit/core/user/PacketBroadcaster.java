/*
 * basic-bukkit.main
 * Copyright (C) 2022 timesnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; If not, see <http://www.gnu.org/licenses/>.
 */

package de.timesnake.basic.bukkit.core.user;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.CommandListener;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.library.basic.util.chat.ExTextColor;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.ExCommand;
import de.timesnake.library.packets.util.packet.ExPacket;
import net.kyori.adventure.text.Component;
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
                sender.sendPluginMessage(Component.text("Enabled packet messages", ExTextColor.PERSONAL));
            } else {
                sender.sendPluginMessage(Component.text("Disabled packet messages", ExTextColor.PERSONAL));
            }

            if (args.isLengthHigherEquals(1, false)) {
                if (args.get(0).equalsIgnoreCase("verbose")) {
                    Server.getPacketManager().setBroadcast(!Server.getPacketManager().isBroadcast());

                    if (Server.getPacketManager().isBroadcast()) {
                        sender.sendPluginMessage(Component.text("Enabled verbose packet messages", ExTextColor.PERSONAL));
                    } else {
                        sender.sendPluginMessage(Component.text("Disabled verbose packet messages", ExTextColor.PERSONAL));
                    }
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        return null;
    }

    @Override
    public void loadCodes(de.timesnake.library.extension.util.chat.Plugin plugin) {

    }
}
