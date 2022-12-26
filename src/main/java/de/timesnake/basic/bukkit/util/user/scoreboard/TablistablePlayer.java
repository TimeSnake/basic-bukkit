/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import org.bukkit.entity.Player;

import java.util.LinkedList;

public interface TablistablePlayer {

    String getTablistName();

    TablistableGroup getTablistGroup(TablistGroupType type);

    Player getPlayer();

    String getFullRank(LinkedList<TablistGroupType> types);

    String getFullPrefix(LinkedList<TablistGroupType> types);

    boolean showInTablist();

    String getTablistPrefix();

}
