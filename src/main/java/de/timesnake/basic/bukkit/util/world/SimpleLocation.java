/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world;

import org.bukkit.Location;

import java.util.Objects;

public class SimpleLocation {

  protected double x;
  protected double y;
  protected double z;

  public SimpleLocation(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public SimpleLocation(Location location) {
    this(location.getX(), location.getY(), location.getZ());
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getZ() {
    return z;
  }

  public void setZ(double z) {
    this.z = z;
  }

  public void set(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void add(double x, double y, double z) {
    this.x += x;
    this.y += y;
    this.z += z;
  }

  public SimpleLocation middleHorizontalBlock() {
    this.zeroBlock();
    this.add(0.5, 0, 0.5);
    return this;
  }

  public SimpleLocation zeroBlock() {
    this.x = Math.floor(this.x);
    this.y = Math.floor(this.y);
    this.z = Math.floor(this.z);
    return this;
  }

  public ExLocation toLocation(ExWorld world) {
    return new ExLocation(world, this.x, this.y, this.z);
  }

  public SimpleBlock toBlock() {
    return new SimpleBlock((int) this.x, (int) this.y, (int) this.z);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SimpleLocation that = (SimpleLocation) o;
    return Double.compare(x, that.x) == 0 && Double.compare(y, that.y) == 0 && Double.compare(z, that.z) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }
}
