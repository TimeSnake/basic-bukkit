package de.timesnake.basic.bukkit.util.user.scoreboard;

import org.bukkit.ChatColor;

public interface TablistableGroup {

    String getTablistRank();

    String getTablistName();

    String getTablistPrefix();

    ChatColor getTablistPrefixChatColor();

    ChatColor getTablistChatColor();

}
