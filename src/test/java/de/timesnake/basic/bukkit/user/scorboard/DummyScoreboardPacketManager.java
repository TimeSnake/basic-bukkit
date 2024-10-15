/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.user.scorboard;

import de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardPacketManager;
import de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardViewer;
import net.minecraft.network.protocol.Packet;

import java.util.Collection;

public class DummyScoreboardPacketManager implements ScoreboardPacketManager {

  @Override
  public void sendPacket(Collection<? extends ScoreboardViewer> viewers, Packet<?> packet) {

  }

  @Override
  public void sendPacket(ScoreboardViewer viewer, Packet<?> packet) {

  }
}
