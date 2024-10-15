/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import net.minecraft.network.protocol.Packet;

import java.util.Collection;

public interface ScoreboardPacketManager {

  void sendPacket(Collection<? extends ScoreboardViewer> viewers, Packet<?> packet);

  void sendPacket(ScoreboardViewer viewer, Packet<?> packet);
}
