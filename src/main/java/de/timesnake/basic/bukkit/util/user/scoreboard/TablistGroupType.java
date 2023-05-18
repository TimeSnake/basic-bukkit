/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import de.timesnake.basic.bukkit.util.group.DisplayGroup;

public class TablistGroupType {

  public static final TablistGroupType DUMMY = new TablistGroupType(null);
  public static final TablistGroupType FAKE_GROUP = new TablistGroupType(null);
  public static final TablistGroupType DISPLAY_GROUP_0 = new TablistGroupType(DisplayGroup.class);
  public static final TablistGroupType DISPLAY_GROUP_1 = new TablistGroupType(DisplayGroup.class);
  public static final TablistGroupType DISPLAY_GROUP_2 = new TablistGroupType(DisplayGroup.class);
  public static final TablistGroupType TAB_HEADER = new TablistGroupType(null);

  private final Class<? extends TablistableGroup> groupClass;

  public TablistGroupType(Class<? extends TablistableGroup> groupClass) {
    this.groupClass = groupClass;
  }

  public Class<? extends TablistableGroup> getGroup() {
    return this.groupClass;
  }
}
