/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard.tablist;

import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroup;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroupType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.List;

public interface TablistEntryHelper {

  List<TablistGroupType> getGroupTypes();

  TablistGroupType getColorGroupType();

  TablistEntry createGroup(TablistGroupType type, TablistGroup group);

  @NotNull
  TablistGroup getDefaultGroup(TablistGroupType type);

  @Range(from = 0, to = 8)
  int getEntryGapSize(TablistGroupType type);

}
