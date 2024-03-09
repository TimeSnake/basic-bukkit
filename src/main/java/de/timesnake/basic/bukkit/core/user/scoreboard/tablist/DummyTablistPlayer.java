/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard.tablist;

import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroup;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroupType;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistPlayer;
import de.timesnake.library.packets.util.packet.TablistHead;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;

public class DummyTablistPlayer implements TablistPlayer {

  private final net.minecraft.world.entity.player.Player entityPlayer;

  public DummyTablistPlayer(String name, TablistHead head) {
    this.entityPlayer = head.asPlayer(name);
  }

  @Override
  public String getTablistName() {
    return this.getPlayer().getName();
  }

  @Deprecated
  @Override
  public TablistGroup getTablistGroup(TablistGroupType type) {
    return null;
  }

  @Override
  public Player getPlayer() {
    return (Player) this.entityPlayer.getBukkitEntity();
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
}
