/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.server;

import java.util.LinkedList;
import java.util.List;

public class MathHelper {

  public static List<Integer> getDecreasingValues(int start, double speedMultiplier, int steps, int min) {
    List<Integer> times = new LinkedList<>();

    double previousValue = 1;
    double factor = Math.pow(0.5, speedMultiplier / start);

    for (int i = 0; i < steps; i++) {
      times.add((int) Math.max(min, start * previousValue));
      previousValue *= factor;
    }

    return times;
  }
}
