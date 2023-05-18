/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard;

import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroupType;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistableGroup;
import de.timesnake.library.packets.util.packet.ExPacketPlayOutTablist;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.entity.Player;

public class FakeTablistPlayer implements TablistablePlayer {

  private final EntityPlayer entityPlayer;

  public FakeTablistPlayer(String name, ExPacketPlayOutTablist.Head head) {
    this.entityPlayer = ExPacketPlayOutTablist.newEntry(name, head);
  }

  @Override
  public String getTablistName() {
    return this.getPlayer().getName();
  }

  @Deprecated
  @Override
  public TablistableGroup getTablistGroup(TablistGroupType type) {
    return null;
  }

  @Override
  public Player getPlayer() {
    return this.entityPlayer.getBukkitEntity().getPlayer();
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
