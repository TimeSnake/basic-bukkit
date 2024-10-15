/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import net.minecraft.network.protocol.Packet;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface ScoreboardViewer {

  String getName();

  UUID getUniqueId();

  NameTagVisibility canSeeNameTagOf(TablistPlayer player);

  void sendPacket(Packet<?> packet);

  void addTablistEntry(TablistPlayer player);

  void addTablistEntry(TablistPlayer player, String rank);

  void removeTablistEntry(TablistPlayer player);

  void removeTablistEntry(TablistPlayer player, String rank);

  void setTablist(Tablist tablist);

  void setSideboard(Sideboard sideboard);

  void setSideboardTitle(String title);

  void setSideboardScore(int line, @Nonnull String text);

  void removeSideboardScore(int line, @Nonnull String text);

  void removeSideboardScore(int line);

  void resetSideboard();
}
