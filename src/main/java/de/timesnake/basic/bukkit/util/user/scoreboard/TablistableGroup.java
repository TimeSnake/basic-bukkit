package de.timesnake.basic.bukkit.util.user.scoreboard;

import org.bukkit.ChatColor;

public interface TablistableGroup {

    TablistGroupType getTeamType();

    String getTablistRank();

    String getTablistName();

    String getTablistPrefix();

    ChatColor getTablistPrefixChatColor();

    ChatColor getTablistChatColor();

}
