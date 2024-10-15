/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard;

import de.timesnake.basic.bukkit.util.user.scoreboard.DuplicateScoreboardException;
import de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardPacketManager;
import de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardViewer;
import de.timesnake.library.basic.util.UserSet;
import net.minecraft.network.protocol.Packet;

import java.util.HashSet;
import java.util.Set;

public abstract class Scoreboard implements de.timesnake.basic.bukkit.util.user.scoreboard.Scoreboard {

  private static final Set<String> NAMES = new HashSet<>();

  protected final String name;
  protected final ScoreboardPacketManager packetManager;

  protected final UserSet<ScoreboardViewer> viewers = new UserSet<>();

  protected Scoreboard(String name, ScoreboardPacketManager packetManager) {
    if (NAMES.contains(name)) {
      throw new DuplicateScoreboardException(name);
    }
    NAMES.add(name);

    this.name = name;
    this.packetManager = packetManager;
  }

  /**
   * Gets the name of the {@link Scoreboard}, not the title
   *
   * @return the name
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Adds a {@link ScoreboardViewer} The {@link org.bukkit.scoreboard.Scoreboard} of the {@link ScoreboardViewer}
   * will be updated by a board
   * change
   *
   * @param user The {@link ScoreboardViewer} to add
   */
  @Override
  public void addViewer(ScoreboardViewer user) {
    this.viewers.add(user);
  }

  /**
   * Removes a {@link ScoreboardViewer}
   *
   * @param user The {@link ScoreboardViewer} to remove
   */
  @Override
  public void removeViewer(ScoreboardViewer user) {
    this.viewers.remove(user);
  }

  @Override
  public Set<ScoreboardViewer> getViewers() {
    return viewers;
  }

  protected void sendPacket(ScoreboardViewer user, Packet<?> packet) {
    this.packetManager.sendPacket(user, packet);
  }

  protected void sendPacket(Packet<?> packet) {
    this.packetManager.sendPacket(this.viewers, packet);
  }

}
