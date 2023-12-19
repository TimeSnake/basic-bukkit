/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.server;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class ColorConverter {

  public static Material chatColor2Bed(ChatColor chatColor) {
    return switch (chatColor) {
      case BLACK -> Material.BLACK_BED;
      case DARK_BLUE -> Material.BLUE_BED;
      case DARK_GREEN -> Material.GREEN_BED;
      case DARK_AQUA -> Material.CYAN_BED;
      case DARK_RED -> Material.RED_BED;
      case DARK_PURPLE -> Material.PURPLE_BED;
      case GOLD -> Material.YELLOW_BED;
      case GRAY -> Material.LIGHT_GRAY_BED;
      case DARK_GRAY -> Material.GRAY_BED;
      case BLUE -> Material.BLUE_BED;
      case GREEN -> Material.GREEN_BED;
      case AQUA -> Material.LIGHT_BLUE_BED;
      case RED -> Material.RED_BED;
      case LIGHT_PURPLE -> Material.PINK_BED;
      case YELLOW -> Material.YELLOW_BED;
      case WHITE -> Material.WHITE_BED;
      default -> null;
    };
  }

  public static Material chatColor2Wool(ChatColor chatColor) {
    return switch (chatColor) {
      case BLACK -> Material.BLACK_WOOL;
      case DARK_BLUE -> Material.BLUE_WOOL;
      case DARK_GREEN -> Material.GREEN_WOOL;
      case DARK_AQUA -> Material.CYAN_WOOL;
      case DARK_RED -> Material.RED_WOOL;
      case DARK_PURPLE -> Material.PURPLE_WOOL;
      case GOLD -> Material.YELLOW_WOOL;
      case GRAY -> Material.LIGHT_GRAY_WOOL;
      case DARK_GRAY -> Material.GRAY_WOOL;
      case BLUE -> Material.BLUE_WOOL;
      case GREEN -> Material.LIME_WOOL;
      case AQUA -> Material.LIGHT_BLUE_WOOL;
      case RED -> Material.ORANGE_WOOL;
      case LIGHT_PURPLE -> Material.PINK_WOOL;
      case YELLOW -> Material.YELLOW_WOOL;
      case WHITE -> Material.WHITE_WOOL;
      default -> null;
    };
  }
}
