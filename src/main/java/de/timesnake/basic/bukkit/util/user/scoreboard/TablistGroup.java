/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import de.timesnake.library.chat.ExTextColor;

public interface TablistGroup {

  int getTablistRank();

  String getTablistName();

  String getTablistPrefix();

  ExTextColor getTablistPrefixChatColor();

  ExTextColor getTablistChatColor();

}
