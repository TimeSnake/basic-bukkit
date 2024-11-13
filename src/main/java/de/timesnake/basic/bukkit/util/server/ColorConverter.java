/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.server;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;


public class ColorConverter {

  public static Material chatColor2Bed(Color color) {
    if (color.equals(Color.WHITE)) {
      return Material.WHITE_BED;
    } else if (color.equals(Color.SILVER)) {
      return Material.LIGHT_GRAY_BED;
    } else if (color.equals(Color.GRAY)) {
      return Material.GRAY_BED;
    } else if (color.equals(Color.BLACK)) {
      return Material.BLACK_BED;
    } else if (color.equals(Color.RED)) {
      return Material.RED_BED;
    } else if (color.equals(Color.YELLOW)) {
      return Material.YELLOW_BED;
    } else if (color.equals(Color.LIME)) {
      return Material.LIME_BED;
    } else if (color.equals(Color.GREEN)) {
      return Material.GREEN_BED;
    } else if (color.equals(Color.AQUA)) {
      return Material.LIGHT_BLUE_BED;
    } else if (color.equals(Color.BLUE)) {
      return Material.BLUE_BED;
    }
    return null;
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

  public static Material colorIterator(Material material) {
    return switch (material) {
      case BLACK_WOOL -> Material.GRAY_WOOL;
      case GRAY_WOOL -> Material.LIGHT_GRAY_WOOL;
      case LIGHT_GRAY_WOOL -> Material.WHITE_WOOL;
      case WHITE_WOOL -> Material.YELLOW_WOOL;
      case YELLOW_WOOL -> Material.ORANGE_WOOL;
      case ORANGE_WOOL -> Material.RED_WOOL;
      case RED_WOOL -> Material.PINK_WOOL;
      case PINK_WOOL -> Material.MAGENTA_WOOL;
      case MAGENTA_WOOL -> Material.PURPLE_WOOL;
      case PURPLE_WOOL -> Material.CYAN_WOOL;
      case CYAN_WOOL -> Material.LIGHT_BLUE_WOOL;
      case LIGHT_BLUE_WOOL -> Material.BLUE_WOOL;
      case BLUE_WOOL -> Material.BROWN_WOOL;
      case BROWN_WOOL -> Material.LIME_WOOL;
      case LIME_WOOL -> Material.GREEN_WOOL;
      case GREEN_WOOL -> Material.BLACK_WOOL;
      default -> Material.BLACK_WOOL;
    };
  }

  public static Material colorIterator2(Material material) {
    return switch (material) {
      case BLACK_WOOL -> Material.GRAY_WOOL;
      case GRAY_WOOL -> Material.LIGHT_GRAY_WOOL;
      case LIGHT_GRAY_WOOL -> Material.WHITE_WOOL;
      case WHITE_WOOL -> Material.YELLOW_WOOL;
      case YELLOW_WOOL -> Material.ORANGE_WOOL;
      case ORANGE_WOOL -> Material.RED_WOOL;
      case RED_WOOL -> Material.PINK_WOOL;
      case PINK_WOOL -> Material.MAGENTA_WOOL;
      case MAGENTA_WOOL -> Material.PURPLE_WOOL;
      case PURPLE_WOOL -> Material.CYAN_WOOL;
      case CYAN_WOOL -> Material.LIGHT_BLUE_WOOL;
      case LIGHT_BLUE_WOOL -> Material.BLUE_WOOL;
      case BLUE_WOOL -> Material.BROWN_WOOL;
      case BROWN_WOOL -> Material.LIME_WOOL;
      case LIME_WOOL -> Material.GREEN_WOOL;
      case GREEN_WOOL -> Material.BLACK_CONCRETE;
      case BLACK_CONCRETE -> Material.GRAY_CONCRETE;
      case GRAY_CONCRETE -> Material.LIGHT_GRAY_CONCRETE;
      case LIGHT_GRAY_CONCRETE -> Material.WHITE_CONCRETE;
      case WHITE_CONCRETE -> Material.YELLOW_CONCRETE;
      case YELLOW_CONCRETE -> Material.ORANGE_CONCRETE;
      case ORANGE_CONCRETE -> Material.RED_CONCRETE;
      case RED_CONCRETE -> Material.PINK_CONCRETE;
      case PINK_CONCRETE -> Material.MAGENTA_CONCRETE;
      case MAGENTA_CONCRETE -> Material.PURPLE_CONCRETE;
      case PURPLE_CONCRETE -> Material.CYAN_CONCRETE;
      case CYAN_CONCRETE -> Material.LIGHT_BLUE_CONCRETE;
      case LIGHT_BLUE_CONCRETE -> Material.BLUE_CONCRETE;
      case BLUE_CONCRETE -> Material.BROWN_CONCRETE;
      case BROWN_CONCRETE -> Material.LIME_CONCRETE;
      case LIME_CONCRETE -> Material.GREEN_CONCRETE;
      case GREEN_CONCRETE -> Material.BLACK_TERRACOTTA;
      case BLACK_TERRACOTTA -> Material.GRAY_TERRACOTTA;
      case GRAY_TERRACOTTA -> Material.LIGHT_GRAY_TERRACOTTA;
      case LIGHT_GRAY_TERRACOTTA -> Material.WHITE_TERRACOTTA;
      case WHITE_TERRACOTTA -> Material.YELLOW_TERRACOTTA;
      case YELLOW_TERRACOTTA -> Material.ORANGE_TERRACOTTA;
      case ORANGE_TERRACOTTA -> Material.RED_TERRACOTTA;
      case RED_TERRACOTTA -> Material.PINK_TERRACOTTA;
      case PINK_TERRACOTTA -> Material.MAGENTA_TERRACOTTA;
      case MAGENTA_TERRACOTTA -> Material.PURPLE_TERRACOTTA;
      case PURPLE_TERRACOTTA -> Material.CYAN_TERRACOTTA;
      case CYAN_TERRACOTTA -> Material.LIGHT_BLUE_TERRACOTTA;
      case LIGHT_BLUE_TERRACOTTA -> Material.BLUE_TERRACOTTA;
      case BLUE_TERRACOTTA -> Material.BROWN_TERRACOTTA;
      case BROWN_TERRACOTTA -> Material.LIME_TERRACOTTA;
      case LIME_TERRACOTTA -> Material.GREEN_TERRACOTTA;
      case GREEN_TERRACOTTA -> Material.BLACK_WOOL;
      default -> Material.BLACK_WOOL;
    };
  }
}
