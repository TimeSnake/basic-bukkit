/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

public class TablistGroupType {

  public static final TablistGroupType DISPLAY_GROUP_0 = new TablistGroupType("display_group_0");
  public static final TablistGroupType DISPLAY_GROUP_1 = new TablistGroupType("display_group_1");
  public static final TablistGroupType DISPLAY_GROUP_2 = new TablistGroupType("display_group_2");

  private final String name;

  public TablistGroupType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }
}
