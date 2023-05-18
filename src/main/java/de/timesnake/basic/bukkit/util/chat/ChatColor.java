/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.chat;

import de.timesnake.library.chat.ExTextColor;

public interface ChatColor {

  org.bukkit.ChatColor PUBLIC = org.bukkit.ChatColor.WHITE;
  org.bukkit.ChatColor PERSONAL = org.bukkit.ChatColor.YELLOW;
  org.bukkit.ChatColor VALUE = org.bukkit.ChatColor.GRAY;
  org.bukkit.ChatColor WARNING = org.bukkit.ChatColor.RED;
  org.bukkit.ChatColor QUICK_INFO = org.bukkit.ChatColor.BLACK;

  org.bukkit.ChatColor BLACK = org.bukkit.ChatColor.BLACK;
  org.bukkit.ChatColor DARK_BLUE = org.bukkit.ChatColor.DARK_BLUE;
  org.bukkit.ChatColor DARK_GREEN = org.bukkit.ChatColor.DARK_GREEN;
  org.bukkit.ChatColor DARK_AQUA = org.bukkit.ChatColor.DARK_AQUA;
  org.bukkit.ChatColor DARK_RED = org.bukkit.ChatColor.DARK_RED;
  org.bukkit.ChatColor DARK_PURPLE = org.bukkit.ChatColor.DARK_PURPLE;
  org.bukkit.ChatColor GOLD = org.bukkit.ChatColor.GOLD;
  org.bukkit.ChatColor GRAY = org.bukkit.ChatColor.GRAY;
  org.bukkit.ChatColor DARK_GRAY = org.bukkit.ChatColor.DARK_GRAY;
  org.bukkit.ChatColor BLUE = org.bukkit.ChatColor.BLUE;
  org.bukkit.ChatColor GREEN = org.bukkit.ChatColor.GREEN;
  org.bukkit.ChatColor AQUA = org.bukkit.ChatColor.AQUA;
  org.bukkit.ChatColor RED = org.bukkit.ChatColor.RED;
  org.bukkit.ChatColor LIGHT_PURPLE = org.bukkit.ChatColor.LIGHT_PURPLE;
  org.bukkit.ChatColor YELLOW = org.bukkit.ChatColor.YELLOW;
  org.bukkit.ChatColor WHITE = org.bukkit.ChatColor.WHITE;
  org.bukkit.ChatColor MAGIC = org.bukkit.ChatColor.MAGIC;
  org.bukkit.ChatColor BOLD = org.bukkit.ChatColor.BOLD;
  org.bukkit.ChatColor STRIKETHROUGH = org.bukkit.ChatColor.STRIKETHROUGH;
  org.bukkit.ChatColor UNDERLINE = org.bukkit.ChatColor.UNDERLINE;
  org.bukkit.ChatColor ITALIC = org.bukkit.ChatColor.ITALIC;
  org.bukkit.ChatColor RESET = org.bukkit.ChatColor.RESET;

  static String translateAlternateColorCodes(char c, String prefix) {
    return org.bukkit.ChatColor.translateAlternateColorCodes(c, prefix);
  }

  static org.bukkit.ChatColor translateFromExTextColor(ExTextColor textColor) {
    if (textColor.equals(ExTextColor.BLACK)) {
      return ChatColor.BLACK;
    }
    if (textColor.equals(ExTextColor.QUICK_INFO)) {
      return ChatColor.QUICK_INFO;
    }
    if (textColor.equals(ExTextColor.DARK_BLUE)) {
      return ChatColor.DARK_BLUE;
    }
    if (textColor.equals(ExTextColor.DARK_GREEN)) {
      return ChatColor.DARK_GREEN;
    }
    if (textColor.equals(ExTextColor.DARK_AQUA)) {
      return ChatColor.DARK_AQUA;
    }
    if (textColor.equals(ExTextColor.DARK_RED)) {
      return ChatColor.DARK_RED;
    }
    if (textColor.equals(ExTextColor.DARK_PURPLE)) {
      return ChatColor.DARK_PURPLE;
    }
    if (textColor.equals(ExTextColor.GOLD)) {
      return ChatColor.GOLD;
    }
    if (textColor.equals(ExTextColor.GRAY)) {
      return ChatColor.GRAY;
    }
    if (textColor.equals(ExTextColor.VALUE)) {
      return ChatColor.VALUE;
    }
    if (textColor.equals(ExTextColor.DARK_GRAY)) {
      return ChatColor.DARK_GRAY;
    }
    if (textColor.equals(ExTextColor.BLUE)) {
      return ChatColor.BLUE;
    }
    if (textColor.equals(ExTextColor.GREEN)) {
      return ChatColor.GREEN;
    }
    if (textColor.equals(ExTextColor.AQUA)) {
      return ChatColor.AQUA;
    }
    if (textColor.equals(ExTextColor.RED)) {
      return ChatColor.RED;
    }
    if (textColor.equals(ExTextColor.WARNING)) {
      return ChatColor.WARNING;
    }
    if (textColor.equals(ExTextColor.LIGHT_PURPLE)) {
      return ChatColor.LIGHT_PURPLE;
    }
    if (textColor.equals(ExTextColor.YELLOW)) {
      return ChatColor.YELLOW;
    }
    if (textColor.equals(ExTextColor.PERSONAL)) {
      return ChatColor.PERSONAL;
    }
    if (textColor.equals(ExTextColor.WHITE)) {
      return ChatColor.WHITE;
    }
    if (textColor.equals(ExTextColor.PUBLIC)) {
      return ChatColor.PUBLIC;
    }
    return null;
  }
}
