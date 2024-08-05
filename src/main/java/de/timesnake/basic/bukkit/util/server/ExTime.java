/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.server;

import java.util.Objects;

public record ExTime(int time, TimeUnit unit) {

  public static ExTime of(int time, TimeUnit unit) {
    return new ExTime(time, unit);
  }

  public static ExTime ofTicks(int ticks) {
    return new ExTime(ticks, TimeUnit.TICKS);
  }

  public static ExTime ofSeconds(int seconds) {
    return new ExTime(seconds, TimeUnit.SECONDS);
  }

  public static ExTime ofMinutes(int minutes) {
    return new ExTime(minutes, TimeUnit.MINUTES);
  }

  public int toTicks() {
    return this.time * this.unit.getTicks();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ExTime exTime = (ExTime) o;
    return time == exTime.time && unit == exTime.unit;
  }

  @Override
  public int hashCode() {
    return Objects.hash(time, unit);
  }
}
