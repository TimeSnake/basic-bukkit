/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world.entity;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.inventory.ExItemStack;
import de.timesnake.basic.bukkit.util.world.ExBlock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapDisplayBuilder {

  private static final Logger LOGGER = LogManager.getLogger("server.map-displays");

  private static final @NotNull Color[] colors = new Color[]{c(0, 0, 0), c(0, 0, 0),
      c(0, 0, 0), c(0, 0, 0), c(89, 125, 39), c(109, 153, 48),
      c(127, 178, 56), c(67, 94, 29), c(174, 164, 115), c(213, 201, 140),
      c(247, 233, 163), c(130, 123, 86), c(140, 140, 140), c(171, 171, 171), c(199, 199, 199),
      c(105, 105,
          105), c(180, 0, 0), c(220, 0, 0), c(255, 0, 0), c(135, 0, 0), c(112, 112, 180),
      c(138, 138, 220),
      c(160, 160, 255), c(84, 84, 135), c(117, 117, 117), c(144, 144, 144), c(167, 167, 167),
      c(88, 88, 88)
      , c(0, 87, 0), c(0, 106, 0), c(0, 124, 0), c(0, 65, 0), c(180, 180, 180),
      c(220, 220, 220), c(255,
      255, 255), c(135, 135, 135), c(115, 118, 129), c(141, 144, 158), c(164, 168, 184),
      c(86, 88, 97),
      c(106, 76, 54), c(130, 94, 66), c(151, 109, 77), c(79, 57, 40), c(79, 79, 79),
      c(96, 96, 96), c(112,
      112, 112), c(59, 59, 59), c(45, 45, 180), c(55, 55, 220), c(64, 64, 255),
      c(33, 33, 135), c(100, 84,
      50), c(123, 102, 62), c(143, 119, 72), c(75, 63, 38), c(180, 177, 172),
      c(220, 217, 211), c(255, 252,
      245), c(135, 133, 129), c(152, 89, 36), c(186, 109, 44), c(216, 127, 51),
      c(114, 67, 27), c(125, 53,
      152), c(153, 65, 186), c(178, 76, 216), c(94, 40, 114), c(72, 108, 152),
      c(88, 132, 186), c(102, 153,
      216), c(54, 81, 114), c(161, 161, 36), c(197, 197, 44), c(229, 229, 51),
      c(121, 121, 27), c(89, 144,
      17), c(109, 176, 21), c(127, 204, 25), c(67, 108, 13), c(170, 89, 116),
      c(208, 109, 142), c(242, 127,
      165), c(128, 67, 87), c(53, 53, 53), c(65, 65, 65), c(76, 76, 76), c(40, 40, 40),
      c(108, 108, 108),
      c(132, 132, 132), c(153, 153, 153), c(81, 81, 81), c(53, 89, 108), c(65, 109, 132),
      c(76, 127, 153),
      c(40, 67, 81), c(89, 44, 125), c(109, 54, 153), c(127, 63, 178), c(67, 33, 94),
      c(36, 53, 125), c(44,
      65, 153), c(51, 76, 178), c(27, 40, 94), c(72, 53, 36), c(88, 65, 44), c(102, 76, 51),
      c(54, 40, 27),
      c(72, 89, 36), c(88, 109, 44), c(102, 127, 51), c(54, 67, 27), c(108, 36, 36),
      c(132, 44, 44), c(153,
      51, 51), c(81, 27, 27), c(17, 17, 17), c(21, 21, 21), c(25, 25, 25), c(13, 13, 13),
      c(176, 168, 54),
      c(215, 205, 66), c(250, 238, 77), c(132, 126, 40), c(64, 154, 150), c(79, 188, 183),
      c(92, 219, 213),
      c(48, 115, 112), c(52, 90, 180), c(63, 110, 220), c(74, 128, 255), c(39, 67, 135),
      c(0, 153, 40), c(0
      , 187, 50), c(0, 217, 58), c(0, 114, 30), c(91, 60, 34), c(111, 74, 42), c(129, 86, 49),
      c(68, 45,
          25), c(79, 1, 0), c(96, 1, 0), c(112, 2, 0), c(59, 1, 0), c(147, 124, 113),
      c(180, 152, 138), c(209,
      177, 161), c(110, 93, 85), c(112, 57, 25), c(137, 70, 31), c(159, 82, 36),
      c(84, 43, 19), c(105, 61,
      76), c(128, 75, 93), c(149, 87, 108), c(78, 46, 57), c(79, 76, 97), c(96, 93, 119),
      c(112, 108, 138),
      c(59, 57, 73), c(131, 93, 25), c(160, 114, 31), c(186, 133, 36), c(98, 70, 19),
      c(72, 82, 37), c(88,
      100, 45), c(103, 117, 53), c(54, 61, 28), c(112, 54, 55), c(138, 66, 67),
      c(160, 77, 78), c(84, 40,
      41), c(40, 28, 24), c(49, 35, 30), c(57, 41, 35), c(30, 21, 18), c(95, 75, 69),
      c(116, 92, 84), c(135
      , 107, 98), c(71, 56, 51), c(61, 64, 64), c(75, 79, 79), c(87, 92, 92), c(46, 48, 48),
      c(86, 51, 62),
      c(105, 62, 75), c(122, 73, 88), c(64, 38, 46), c(53, 43, 64), c(65, 53, 79),
      c(76, 62, 92), c(40, 32,
      48), c(53, 35, 24), c(65, 43, 30), c(76, 50, 35), c(40, 26, 18), c(53, 57, 29),
      c(65, 70, 36), c(76,
      82, 42), c(40, 43, 22), c(100, 42, 32), c(122, 51, 39), c(142, 60, 46), c(75, 31, 24),
      c(26, 15, 11),
      c(31, 18, 13), c(37, 22, 16), c(19, 11, 8), c(133, 33, 34), c(163, 41, 42),
      c(189, 48, 49), c(100, 25
      , 25), c(104, 44, 68), c(127, 54, 83), c(148, 63, 97), c(78, 33, 51), c(64, 17, 20),
      c(79, 21, 25),
      c(92, 25, 29), c(48, 13, 15), c(15, 88, 94), c(18, 108, 115), c(22, 126, 134),
      c(11, 66, 70), c(40,
      100, 98), c(50, 122, 120), c(58, 142, 140), c(30, 75, 74), c(60, 31, 43), c(74, 37, 53),
      c(86, 44,
          62), c(45, 23, 32), c(14, 127, 93), c(17, 155, 114), c(20, 180, 133),
      c(10, 95, 70), c(70, 70, 70),
      c(86, 86, 86), c(100, 100, 100), c(52, 52, 52), c(152, 123, 103), c(186, 150, 126),
      c(216, 175, 147),
      c(114, 92, 77), c(89, 117, 105), c(109, 144, 129), c(127, 167, 150), c(67, 88, 79)};
  protected final byte[][][][] pixels;
  private final int width;
  private final int height;

  public MapDisplayBuilder(int width, int height) {
    this.width = width;
    this.height = height;
    this.pixels = new byte[width][height][128][128];
  }

  private static Byte matchColor(@NotNull Color color) {
    if (color.getAlpha() < 128) {
      // transparent to null
      return null;
    } else {
      int index = 0;
      double best = -1.0;

      for (int i = 4; i < colors.length; ++i) {
        double distance = getDistance(color, colors[i]);
        if (distance < best || best == -1.0) {
          best = distance;
          index = i;
        }
      }

      return (byte) (index < 128 ? index : -129 + (index - 127));
    }
  }

  private static @NotNull Byte[] imageToBytes(@NotNull Image image) {
    BufferedImage temp = new BufferedImage(image.getWidth(null), image.getHeight(null),
        BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics = ((Graphics2D) temp.getGraphics());
    graphics.drawImage(image, 0, 0, null);
    graphics.dispose();
    int[] pixels = new int[temp.getWidth() * temp.getHeight()];
    temp.getRGB(0, 0, temp.getWidth(), temp.getHeight(), pixels, 0, temp.getWidth());

    Byte[] result = new Byte[temp.getWidth() * temp.getHeight()];

    for (int i = 0; i < pixels.length; ++i) {
      result[i] = matchColor(new Color(pixels[i], true));
    }

    return result;
  }

  private static @NotNull Color c(int r, int g, int b) {
    return new Color(r, g, b);
  }

  private static double getDistance(@NotNull Color c1, @NotNull Color c2) {
    double rMean = (double) (c1.getRed() + c2.getRed()) / 2.0;
    double r = c1.getRed() - c2.getRed();
    double g = c1.getGreen() - c2.getGreen();
    int b = c1.getBlue() - c2.getBlue();
    double weightR = 2.0 + rMean / 256.0;
    double weightG = 4.0;
    double weightB = 2.0 + (255.0 - rMean) / 256.0;
    return weightR * r * r + weightG * g * g + weightB * (double) b * (double) b;
  }

  public ExItemStack[][] onItems() {
    ExItemStack[][] items = new ExItemStack[width][height];

    for (int x = 0; x < this.width; x++) {
      for (int y = 0; y < this.height; y++) {
        ExItemStack item = new ExItemStack(Material.FILLED_MAP);

        MapMeta meta = ((MapMeta) item.getItemMeta());

        MapView view = Bukkit.createMap(
            Server.getWorldManager().getDefaultWorld().getBukkitWorld());
        view.getRenderers().clear();

        int finalX = x;
        int finalY = y;

        MapRenderer renderer = new MapRenderer() {
          @Override
          public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas,
              @NotNull Player player) {
            for (int x = 0; x < 128; x++) {
              for (int y = 0; y < 128; y++) {
                mapCanvas.setPixel(x, y, pixels[finalX][finalY][x][y]);
              }
            }
          }
        };

        view.addRenderer(renderer);
        view.setLocked(true);

        meta.setMapView(view);
        item.setItemMeta(meta);

        items[x][y] = item;
      }
    }

    return items;
  }

  public MapDisplay onBlock(User user, ExBlock block, BlockFace blockFace, BlockFace orientation) {
    MapDisplay mapDisplay = new MapDisplay(this.onItems(), block, blockFace, orientation);
    Server.getEntityManager().registerEntity(mapDisplay, user);
    return mapDisplay;
  }

  public MapDisplay onBlock(ExBlock block, BlockFace blockFace, BlockFace orientation) {
    MapDisplay mapDisplay = new MapDisplay(this.onItems(), block, blockFace, orientation);
    Server.getEntityManager().registerEntity(mapDisplay);
    return mapDisplay;
  }

  public MapDisplayBuilder drawImage(int xOffset, int yOffset, Image image) {
    Byte[] bytes = imageToBytes(image);

    for (int x = 0; x < image.getWidth(null); ++x) {
      for (int y = 0; y < image.getHeight(null); ++y) {
        Byte color = bytes[y * image.getWidth(null) + x];
        this.drawPixel(xOffset + x, yOffset + y, color);
      }
    }

    return this;
  }

  public MapDisplayBuilder drawText(int xOffset, int yOffset, Font font, Color textColor,
      String text,
      Color backgroundColor, Align align) {
    BufferedImage testImage = new BufferedImage(this.width * 128, this.height * 128, 1);
    Graphics testGraphics = testImage.getGraphics();

    testGraphics.setFont(font);
    int width = testGraphics.getFontMetrics().stringWidth(text);
    int height = testGraphics.getFontMetrics().getHeight();

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics graphics = image.getGraphics();

    graphics.setColor(backgroundColor);
    graphics.drawRect(0, 0, width, height);

    graphics.setFont(font);
    graphics.setColor(textColor);

    graphics.drawString(text, 0, height);

    switch (align) {
      case LEFT -> this.drawImage(xOffset, yOffset, image);
      case RIGHT -> this.drawImage(xOffset - width, yOffset, image);
      case CENTER -> this.drawImage(xOffset - width / 2, yOffset, image);
    }

    return this;
  }

  public MapDisplayBuilder drawPixel(int x, int y, Color color) {
    int xFrame = x / 128;
    int yFrame = y / 128;

    if (xFrame > this.width || yFrame > this.height) {
      throw new IndexOutOfBoundsException();
    }

    this.pixels[xFrame][yFrame][x % 128][y % 128] = matchColor(color);
    return this;
  }

  public MapDisplayBuilder drawPixel(int x, int y, Byte color) {
    if (color == null) {
      return this;
    }

    if (x < 0 || y < 0) {
      return this;
    }

    int xFrame = x / 128;
    int yFrame = y / 128;

    if (xFrame >= this.width || yFrame >= this.height) {
      return this;
    }

    this.pixels[xFrame][yFrame][x % 128][y % 128] = color;
    return this;
  }

  public MapDisplayBuilder drawRectangle(int xOffset, int yOffset, int xLength, int yLength,
      Color color) {
    for (int x = xOffset; x < xLength; x++) {
      for (int y = yOffset; y < yLength; y++) {
        this.drawPixel(x, y, color);
      }
    }
    return this;
  }

  public enum Align {
    RIGHT,
    LEFT,
    CENTER
  }

  public static class ExMapFont {

    public static final String MINECRAFT = "Minecraft Regular";

    public static void loadFonts() {
      for (File fontFile : new File("plugins/basic-bukkit/map-fonts/").listFiles()) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
          Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
          ge.registerFont(font);
          LOGGER.info("Loaded font {}", font.getFontName());
        } catch (FontFormatException | IOException e) {
          e.printStackTrace();
        }
      }

    }
  }
}
