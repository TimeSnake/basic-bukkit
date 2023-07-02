/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import de.timesnake.basic.bukkit.util.user.User;
import net.minecraft.network.protocol.Packet;

import java.util.Collection;

public interface ScoreboardPacketManager {

  void sendPacket(Collection<? extends User> receivers, Packet<?> packet);

  void sendPacket(User user, Packet<?> packet);
}
