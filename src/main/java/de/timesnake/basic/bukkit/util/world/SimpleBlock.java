/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world;

import org.bukkit.block.Block;

import java.util.Objects;

public class SimpleBlock {

  private int x;
  private int y;
  private int z;

  public SimpleBlock(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public SimpleBlock(Block block) {
    this(block.getX(), block.getY(), block.getZ());
  }

  public ExBlock toBlock(ExWorld world) {
    return new ExBlock(world, this.x, this.y, this.z);
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getZ() {
    return z;
  }

  public void setZ(int z) {
    this.z = z;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SimpleBlock that = (SimpleBlock) o;
    return x == that.x && y == that.y && z == that.z;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }
}
