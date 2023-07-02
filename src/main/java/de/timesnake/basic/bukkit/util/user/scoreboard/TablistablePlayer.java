/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public interface TablistablePlayer {

  String getTablistName();

  TablistableGroup getTablistGroup(TablistGroupType type);

  Player getPlayer();

  ServerPlayer getMinecraftPlayer();

  String getFullRank(LinkedList<TablistGroupType> types);

  String getFullPrefix(LinkedList<TablistGroupType> types);

  boolean showInTablist();

  String getTablistPrefix();

}
