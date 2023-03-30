/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.library.basic.util.Loggers;
import de.timesnake.library.packets.util.listener.PacketHandler;
import de.timesnake.library.packets.util.listener.PacketPlayOutListener;
import de.timesnake.library.packets.util.packet.ExPacket;
import de.timesnake.library.packets.util.packet.ExPacketPlayOut;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;

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
        Loggers.SCOREBOARD.fine(
                "Send packet '" + packet.getInfo() + "' to '" + user.getName() + "'");

    }
}
