/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world;

import org.bukkit.Location;

import java.util.Objects;

public class SimpleFacingLocation extends SimpleLocation {

  private float yaw;
  private float pitch;

  public SimpleFacingLocation(double x, double y, double z, float yaw, float pitch) {
    super(x, y, z);
    this.yaw = yaw;
    this.pitch = pitch;
  }

  public SimpleFacingLocation(Location location) {
    super(location);
    this.yaw = location.getYaw();
    this.pitch = location.getPitch();
  }

  public float getYaw() {
    return yaw;
  }

  public void setYaw(float yaw) {
    this.yaw = yaw;
  }

  public float getPitch() {
    return pitch;
  }

  public void setPitch(float pitch) {
    this.pitch = pitch;
  }

  @Override
  public SimpleFacingLocation middleHorizontalBlock() {
    return (SimpleFacingLocation) super.middleHorizontalBlock();
  }

  @Override
  public SimpleFacingLocation zeroBlock() {
    return (SimpleFacingLocation) super.zeroBlock();
  }

  public SimpleFacingLocation zeroFacing() {
    this.setYaw(0);
    this.setPitch(0);
    return this;
  }

  public SimpleFacingLocation roundFacing() {
    this.setYaw(this.roundAngle(this.getYaw()));
    this.setPitch(this.roundAngle(this.getPitch()));
    return this;
  }

  @Override
  public ExLocation toLocation(ExWorld world) {
    return new ExLocation(world, this.x, this.y, this.z, this.yaw, this.pitch);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    SimpleFacingLocation that = (SimpleFacingLocation) o;
    return Float.compare(yaw, that.yaw) == 0 && Float.compare(pitch, that.pitch) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), yaw, pitch);
  }

  private float roundAngle(float angle) {
    angle %= 360;
    if (angle < 0) {
      angle += 360;
    }
    if (angle <= 22.5 && angle >= 337.5) {
      return 0;
    }
    if (angle > 22.5 && angle <= 67.5) {
      return 45;
    }
    if (angle > 67.5 && angle <= 112.5) {
      return 90;
    }
    if (angle > 112.5 && angle <= 157.5) {
      return 135;
    }
    if (angle > 157.5 && angle <= 202.5) {
      return 180;
    }
    if (angle > 202.5 && angle <= 247.5) {
      return 225;
    }
    if (angle > 247.5 && angle <= 292.5) {
      return 270;
    }
    if (angle > 292.5 && angle < 337.5) {
      return 315;
    }
    return 0;
  }
}
