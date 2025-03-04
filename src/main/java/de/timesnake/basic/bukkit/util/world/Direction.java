/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Direction {


  public static List<Direction> parseFromString(String string) throws IllegalArgumentException {
    List<Direction> directions = new ArrayList<>();
    for (String s : string.replace(" ", "").split(",")) {
      switch (s.length()) {
        case 1 -> {
          switch (s.charAt(0)) {
            case 'x' -> directions.addAll(List.of(X_POS, X_NEG));
            case 'y' -> directions.addAll(List.of(Y_POS, Y_NEG));
            case 'z' -> directions.addAll(List.of(Z_POS, Z_NEG));
            default -> throw new IllegalArgumentException("invalid key: " + s);
          }
        }
        case 2 -> {
          if (s.contains("+")) {
            switch (s.charAt(0)) {
              case 'x' -> directions.add(X_POS);
              case 'y' -> directions.add(Y_POS);
              case 'z' -> directions.add(Z_POS);
              default -> throw new IllegalArgumentException("invalid key: " + s);
            }
          } else if (s.contains("-")) {
            switch (s.charAt(0)) {
              case 'x' -> directions.add(X_NEG);
              case 'y' -> directions.add(Y_NEG);
              case 'z' -> directions.add(Z_NEG);
              default -> throw new IllegalArgumentException("invalid key: " + s);
            }
          } else {
            switch (s) {
              case "xy" -> directions.addAll(List.of(X_POS_Y_POS, X_POS_Y_NEG, X_NEG_Y_POS, X_NEG_Y_NEG));
              case "yz" -> directions.addAll(List.of(Y_POS_Z_POS, Y_POS_Z_NEG, Y_NEG_Z_POS, Y_NEG_Z_NEG));
              case "xz" -> directions.addAll(List.of(X_POS_Z_POS, X_POS_Z_NEG, X_NEG_Z_POS, X_NEG_Z_NEG));
              default -> throw new IllegalArgumentException("invalid key: " + s);
            }
          }
        }
        case 4 -> {
          switch (s) {
            case "x+y+" -> directions.add(X_POS_Y_POS);
            case "x+y-" -> directions.add(X_POS_Y_NEG);
            case "x-y+" -> directions.add(X_NEG_Y_POS);
            case "x-y-" -> directions.add(X_NEG_Y_NEG);
            case "y+z+" -> directions.add(Y_POS_Z_POS);
            case "y+z-" -> directions.add(Y_POS_Z_NEG);
            case "y-z+" -> directions.add(Y_NEG_Z_POS);
            case "y-z-" -> directions.add(Y_NEG_Z_NEG);
            case "x+z+" -> directions.add(X_POS_Z_POS);
            case "x+z-" -> directions.add(X_POS_Z_NEG);
            case "x-z+" -> directions.add(X_NEG_Z_POS);
            case "x-z-" -> directions.add(X_NEG_Z_NEG);
            default -> throw new IllegalArgumentException("invalid key: " + s);
          }
        }
        default -> throw new IllegalArgumentException("invalid key with invalid length: " + s);
      }
    }
    return directions;
  }

  @SafeVarargs
  public static List<Direction> combine(List<Direction>... directions) {
    return Arrays.stream(directions).flatMap(Collection::stream).distinct().toList();
  }

  public static List<Direction> combine(List<List<Direction>> directions) {
    return directions.stream().flatMap(Collection::stream).distinct().toList();
  }

  public static final Direction X_POS = new Direction(1, 0, 0);
  public static final Direction X_NEG = new Direction(1, 0, 0);
  public static final Direction Y_POS = new Direction(1, 0, 0);
  public static final Direction Y_NEG = new Direction(1, 0, 0);
  public static final Direction Z_POS = new Direction(1, 0, 0);
  public static final Direction Z_NEG = new Direction(1, 0, 0);

  public static final Direction X_POS_Y_POS = new Direction(1, 1, 0);
  public static final Direction X_POS_Y_NEG = new Direction(1, -1, 0);
  public static final Direction X_NEG_Y_POS = new Direction(-1, 1, 0);
  public static final Direction X_NEG_Y_NEG = new Direction(-1, 1, 0);

  public static final Direction Y_POS_Z_POS = new Direction(0, 1, 1);
  public static final Direction Y_POS_Z_NEG = new Direction(0, 1, -1);
  public static final Direction Y_NEG_Z_POS = new Direction(0, -1, 1);
  public static final Direction Y_NEG_Z_NEG = new Direction(0, -1, -1);

  public static final Direction X_POS_Z_POS = new Direction(1, 0, 1);
  public static final Direction X_POS_Z_NEG = new Direction(1, 0, -1);
  public static final Direction X_NEG_Z_POS = new Direction(-1, 0, 1);
  public static final Direction X_NEG_Z_NEG = new Direction(-1, 0, -1);

  public static final List<Direction> X = List.of(X_POS, X_NEG);
  public static final List<Direction> Y = List.of(Y_POS, Y_NEG);
  public static final List<Direction> Z = List.of(Z_POS, Z_NEG);

  public static final List<Direction> XZ = List.of(X_POS, X_NEG, Z_POS, Z_NEG);

  private final Vector vector;

  private Direction(int x, int y, int z) {
    this(new Vector(x, y, z));
  }

  private Direction(Vector vector) {
    this.vector = vector;
  }

  public Vector getVector() {
    return vector;
  }
}
