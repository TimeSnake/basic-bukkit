package de.timesnake.basic.bukkit.core.user.scoreboard;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.packets.util.listener.PacketPlayOutModifyListener;
import de.timesnake.basic.packets.util.packet.ExPacket;
import de.timesnake.basic.packets.util.packet.ExPacketPlayOut;
import org.bukkit.entity.Player;

import java.util.*;

public class ScoreboardPacketManager implements PacketPlayOutModifyListener, de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardPacketManager {

    private final Map<Player, Set<ExPacketPlayOut>> packets = new HashMap<>();

    public ScoreboardPacketManager() {
        Server.getPacketManager().addPacketPlayOutListener(ExPacket.Type.PLAY_OUT_TABLIST, this);
    }

    @Override
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
