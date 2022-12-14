/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import org.bukkit.ChatColor;

public interface TablistableRemainTeam {

    String getTablistName();

    String getTablistPrefix();

    ChatColor getTablistPrefixChatColor();

    ChatColor getTablistChatColor();
}
