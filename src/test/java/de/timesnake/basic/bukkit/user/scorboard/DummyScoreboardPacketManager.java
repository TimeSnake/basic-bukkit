/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.user.scorboard;

import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardPacketManager;
import net.minecraft.network.protocol.Packet;

import java.util.Collection;

public class DummyScoreboardPacketManager implements ScoreboardPacketManager {

  @Override
  public void sendPacket(Collection<? extends User> receivers, Packet<?> packet) {

  }

  @Override
  public void sendPacket(User user, Packet<?> packet) {

  }
}
