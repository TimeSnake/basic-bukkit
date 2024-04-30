/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.user.scorboard;

import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroupType;

public class TestTablistGroupType extends TablistGroupType {

  public static final TablistGroupType TEST_TEAM = new TablistGroupType("test_team");

  public TestTablistGroupType(String name) {
    super(name);
  }
}
