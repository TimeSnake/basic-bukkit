/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard.tablist;

import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroup;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroupType;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistPlayer;
import de.timesnake.library.packets.util.packet.TablistHead;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class DummyTablistPlayer implements TablistPlayer {

  private final String name;
  private final net.minecraft.world.entity.player.Player entityPlayer;

  public DummyTablistPlayer(String name, String tablistName, TablistHead head) {
    this.name = name;
    this.entityPlayer = head.asPlayer(name, tablistName);
  }

  @Override
  public @NotNull String getName() {
    return this.name;
  }

  @Override
  public @NotNull String getTablistName() {
    return this.name;
  }

  @Override
  public @NotNull UUID getUniqueId() {
    return this.entityPlayer.getUUID();
  }

  @Override
  public TablistGroup getTablistGroup(TablistGroupType type) {
    return null;
  }

  @Override
  public ServerPlayer getMinecraftPlayer() {
    return (ServerPlayer) this.entityPlayer;
  }

  @Override
  public boolean showInTablist() {
    return true;
  }

  @Override
  public String getTablistPrefix() {
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DummyTablistPlayer that = (DummyTablistPlayer) o;
    return Objects.equals(name, that.name) && Objects.equals(entityPlayer, that.entityPlayer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, entityPlayer);
  }
}
