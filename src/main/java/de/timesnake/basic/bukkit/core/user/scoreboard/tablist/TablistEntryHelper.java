/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard.tablist;

import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroup;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroupType;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistPlayer;

import java.util.List;

public interface TablistEntryHelper {

  List<TablistGroupType> getGroupTypes();

  TablistGroupType getColorGroupType();

  TablistEntry createGroup(TablistGroupType type, TablistGroup group);

  TablistGroup getDefaultGroup(TablistGroupType type);

  int getEntryGap(TablistGroupType type);

  TablistPlayer newGapEntry(String rank);
}
