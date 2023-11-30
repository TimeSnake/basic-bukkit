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

  public static double getGaussian() {
    double u = 0, v = 0;
    while (u == 0) {
      u = Math.random(); //Converting [0,1) to (0,1)
    }
    while (v == 0) {
      v = Math.random();
    }
    double num = Math.sqrt(-2.0 * Math.log(u)) * Math.cos(2.0 * Math.PI * v);

    num = num / 10.0 + 0.5; // Translate to 0 -> 1
    if (num > 1 || num < 0) {
      num = getGaussian(); // resample between 0 and 1 if out of range
    }
    return num;
  }
}
