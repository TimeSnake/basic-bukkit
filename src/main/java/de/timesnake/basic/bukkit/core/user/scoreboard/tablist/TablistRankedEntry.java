/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard.tablist;

import org.jetbrains.annotations.NotNull;

public abstract non-sealed class TablistRankedEntry extends TablistEntry {

  public abstract String getRank();

  @Override
  public int compareTo(@NotNull TablistEntry o) {
    if (o instanceof TablistGroupEntry) return 1;
    if (o instanceof TablistRankedEntry e) return this.getRank().compareTo(e.getRank());
    return 0;
  }
}
