/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.server;

public enum TimeUnit {

  MINUTES(20 * 60),
  SECONDS(20),
  TICKS(1);

  private final int ticks;

  TimeUnit(int ticks) {
    this.ticks = ticks;
  }

  public int getTicks() {
    return ticks;
  }
}
