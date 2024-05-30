/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.user.scorboard;

import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroup;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroupType;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistPlayer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class TestTablistPlayer implements TablistPlayer {

  private final String name;
  private final String tablistName;
  private final UUID uuid;

  public TestTablistPlayer(String name) {
    this(name, name);
  }

  public TestTablistPlayer(String name, String tablistName) {
    this.name = name;
    this.tablistName = tablistName;
    this.uuid = UUID.randomUUID();
  }

  @Override
  public @NotNull String getName() {
    return name;
  }

  @Override
  public @NotNull String getTablistName() {
    return tablistName;
  }

  @Override
  public @NotNull UUID getUniqueId() {
    return this.uuid;
  }

  @Override
  public TablistGroup getTablistGroup(TablistGroupType type) {
    return null;
  }

  @Override
  public ServerPlayer getMinecraftPlayer() {
    return null;
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
    TestTablistPlayer that = (TestTablistPlayer) o;
    return Objects.equals(name, that.name) && Objects.equals(uuid, that.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, uuid);
  }
}
