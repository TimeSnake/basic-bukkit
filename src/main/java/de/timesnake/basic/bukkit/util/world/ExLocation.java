/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.library.basic.util.Triple;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class ExLocation extends Location {

  public static ExLocation fromLocation(Location location) {
    if (location == null) {
      return null;
    }
    return new ExLocation(Server.getWorld(location.getWorld()), location.getX(),
        location.getY(), location.getZ()
        , location.getYaw(), location.getPitch());
  }

  public static ExLocation fromList(List<Number> list) {
    if (list == null) {
      return null;
    }

    if (list.size() == 3) {
      return new ExLocation(null, list.get(0).doubleValue(), list.get(1).doubleValue(),
          list.get(2).doubleValue());
    } else if (list.size() == 5) {
      return new ExLocation(null, list.get(0).doubleValue(), list.get(1).doubleValue(),
          list.get(2).doubleValue(),
          list.get(3).floatValue(), list.get(4).floatValue());
    } else {
      throw new RuntimeException("invalid list size for location");
    }
  }

  private ExWorld world;

  public ExLocation(ExWorld world, double x, double y, double z) {
    super(world != null ? world.getBukkitWorld()
            : Server.getWorldManager().getDefaultWorld().getBukkitWorld(), x,
        y, z);
    this.world = world;
  }

  public ExLocation(ExWorld world, Triple<Double, Double, Double> xyz) {
    this(world, xyz.getA(), xyz.getB(), xyz.getC());
  }

  public ExLocation(ExWorld world, double x, double y, double z, float yaw, float pitch) {
    super(world != null ? world.getBukkitWorld()
            : Server.getWorldManager().getDefaultWorld().getBukkitWorld(), x,
        y, z, yaw, pitch);
    this.world = world;
  }

  public ExLocation(ExWorld world, Location location) {
    super(world != null ? world.getBukkitWorld()
            : Server.getWorldManager().getDefaultWorld().getBukkitWorld(),
        location.getX(), location.getY(), location.getZ(), location.getYaw(),
        location.getPitch());
    this.world = world;
  }

  public ExWorld getExWorld() {
    return this.world;
  }

  public ExLocation setExWorld(ExWorld world) {
    this.setWorld(world.getBukkitWorld());
    return this;
  }

  @Override
  public World getWorld() {
    return this.world.getBukkitWorld();
  }

  @Override
  public void setWorld(World world) {
    this.world = Server.getWorld(world);
    super.setWorld(world);
  }

  public double distanceHorizontalSquared(Location location) {
    location = location.clone();
    location.setY(this.getY());
    return this.distanceSquared(location);
  }

  public double distanceHorizontal(Location location) {
    return Math.sqrt(this.distanceSquared(location));
  }

  @Override
  public @NotNull Block getBlock() {
    return this.world.getBlockAt(this);
  }

  public ExBlock getExBlock() {
    return new ExBlock(this.getBlock());
  }

  public ExLocation getRandomNearbyLocation(double maxDistance) {
    double x = Server.getRandom().nextDouble();
    double y = Server.getRandom().nextDouble();
    double z = Server.getRandom().nextDouble();

    double sum = x + y + z;

    x = x / sum * maxDistance;
    y = y / sum * maxDistance;
    z = z / sum * maxDistance;

    return this.add(x, y, z);
  }

  public ExLocation getRandomNearbyLocation(double maxDistanceXZ, double maxDistanceY) {
    double x = Server.getRandom().nextDouble(maxDistanceXZ);
    return this.add(x, Server.getRandom().nextDouble(maxDistanceY), maxDistanceXZ - x);
  }

  public ExLocation middleHorizontalBlock() {
    this.zeroBlock();
    this.add(0.5, 0, 0.5);
    return this;
  }

  public ExLocation zeroBlock() {
    this.set(this.getBlockX(), this.getBlockY(), this.getBlockZ());
    return this;
  }

  public ExLocation zeroFacing() {
    this.setYaw(0);
    this.setPitch(0);
    return this;
  }

  public ExLocation roundFacing() {
    this.setYaw(this.roundAngle(this.getYaw()));
    this.setPitch(this.roundAngle(this.getPitch()));
    return this;
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

  /**
   * Gets the {@link BlockFace} facing in the location direction. The face is rounded to NORTH,
   * EAST, SOUTH, WEST, UP and DOWN.
   *
   * @return the {@link BlockFace}
   */
  public BlockFace getDirectionAsBlockFace() {

    float yaw = this.getYaw();
    float pitch = this.getPitch();

    if (pitch < -45) {
      return BlockFace.UP;
    }
    if (pitch > 45) {
      return BlockFace.DOWN;
    }

    while (yaw < 0) {
      yaw += 360;
    }
    while (yaw >= 360) {
      yaw -= 360;
    }

    if (yaw < 45) {
      return BlockFace.NORTH;
    }
    if (yaw < 135) {
      return BlockFace.WEST;
    }
    if (yaw < 225) {
      return BlockFace.SOUTH;
    }
    return BlockFace.EAST;
  }

  @Override
  public @NotNull ExLocation clone() {
    return new ExLocation(this.world, this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
  }

  @Override
  public @NotNull ExLocation add(double x, double y, double z) {
    super.add(x, y, z);
    return this;
  }

  public ExLocation updateX(double x) {
    this.setX(x);
    return this;
  }

  public ExLocation updateY(double y) {
    this.setY(y);
    return this;
  }

  public ExLocation updateZ(double z) {
    this.setZ(z);
    return this;
  }

  @Override
  public @NotNull ExLocation add(@NotNull Vector vector) {
    super.add(vector);
    return this;
  }

  @Override
  public @NotNull ExLocation toBlockLocation() {
    return ExLocation.fromLocation(super.toBlockLocation());
  }

  public @NotNull SimpleFacingLocation toFacingPosition() {
    return new SimpleFacingLocation(this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
  }

  public @NotNull SimpleLocation toPosition() {
    return new SimpleLocation(this.getX(), this.getY(), this.getZ());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    ExLocation location = (ExLocation) o;
    return Objects.equals(world, location.world);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), world);
  }
}
