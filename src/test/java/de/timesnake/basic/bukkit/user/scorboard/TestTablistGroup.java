/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.user.scorboard;

import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroup;
import org.jetbrains.annotations.NotNull;

public class TestTablistGroup implements TablistGroup {

  private final int rank;
  private final String name;
  private final String prefix;

  public TestTablistGroup(int rank, String name, String prefix) {
    this.rank = rank;
    this.name = name;
    this.prefix = prefix;
  }

  @Override
  public int getTablistRank() {
    return rank;
  }

  @Override
  public @NotNull String getTablistName() {
    return name;
  }

  @Override
  public String getTablistPrefix() {
    return prefix;
  }
}
