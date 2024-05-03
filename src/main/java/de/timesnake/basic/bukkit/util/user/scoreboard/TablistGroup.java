/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import de.timesnake.library.chat.ExTextColor;

import javax.annotation.Nullable;

public interface TablistGroup {

  int getTablistRank();

  @Nullable
  String getTablistName();

  default @Nullable String getTablistPrefix() {
    return null;
  }

  default @Nullable ExTextColor getTablistPrefixColor() {
    return null;
  }

  default @Nullable ExTextColor getTablistColor() {
    return null;
  }

}
