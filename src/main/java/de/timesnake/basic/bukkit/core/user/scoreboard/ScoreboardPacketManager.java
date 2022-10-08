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

package de.timesnake.basic.bukkit.core.user.scoreboard;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.library.packets.util.listener.PacketHandler;
import de.timesnake.library.packets.util.listener.PacketPlayOutListener;
import de.timesnake.library.packets.util.packet.ExPacket;
import de.timesnake.library.packets.util.packet.ExPacketPlayOut;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreboardPacketManager implements PacketPlayOutListener,
        de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardPacketManager {

    private final Map<Player, Set<ExPacketPlayOut>> packets = new ConcurrentHashMap<>();

    public ScoreboardPacketManager() {
        Server.getPacketManager().addListener(this);
    }

    @PacketHandler(type = ExPacket.Type.PLAY_OUT_TABLIST, modify = true)
    public ExPacketPlayOut onPacketPlayOut(ExPacketPlayOut packet, Player receiver) {
        Set<ExPacketPlayOut> packets = this.packets.get(receiver);
        if (packets != null && packets.contains(packet)) {
            packets.remove(packet);
            return packet;
        }
        return null;
    }

    @Override
    public void sendPacket(Collection<? extends User> receivers, ExPacketPlayOut packet) {
        for (User user : receivers) {
            sendPacket(user, packet);
        }
    }

    @Override
    public void sendPacket(User user, ExPacketPlayOut packet) {
        Set<ExPacketPlayOut> packets = this.packets.get(user.getPlayer());

        if (packets == null) {
            packets = this.packets.computeIfAbsent(user.getPlayer(), v -> new HashSet<>());
        }

        packets.add(packet);
        user.sendPacket(packet);

    }
}
