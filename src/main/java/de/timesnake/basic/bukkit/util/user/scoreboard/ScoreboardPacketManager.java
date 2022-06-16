package de.timesnake.basic.bukkit.util.user.scoreboard;

import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.library.packets.util.packet.ExPacketPlayOut;

import java.util.Collection;

public interface ScoreboardPacketManager {

    void sendPacket(Collection<? extends User> receivers, ExPacketPlayOut packet);

    void sendPacket(User user, ExPacketPlayOut packet);
}
