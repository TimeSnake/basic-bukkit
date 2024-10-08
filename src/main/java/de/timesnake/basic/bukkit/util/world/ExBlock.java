/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.library.basic.util.Tuple;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExBlock {

  public static final List<Vector> NEAR_BLOCKS = List.of(
      new Vector(1, 0, 0),
      new Vector(-1, 0, 0),
      new Vector(0, 1, 0),
      new Vector(0, -1, 0),
      new Vector(0, 0, 1),
      new Vector(0, 0, -1));

  public static final List<Tuple<Vector, BlockFace>> NEAR_BLOCKS_WITH_FACING = List.of(
      new Tuple<>(new Vector(1, 0, 0), BlockFace.WEST),
      new Tuple<>(new Vector(-1, 0, 0), BlockFace.EAST),
      new Tuple<>(new Vector(0, 1, 0), BlockFace.DOWN),
      new Tuple<>(new Vector(0, -1, 0), BlockFace.UP),
      new Tuple<>(new Vector(0, 0, 1), BlockFace.NORTH),
      new Tuple<>(new Vector(0, 0, -1), BlockFace.SOUTH));

  public static ExBlock fromBlock(Block block) {
    return new ExBlock(block);
  }

  private final ExLocation location;

  public ExBlock(Block block) {
    Location loc = block.getLocation();
    this.location = new ExLocation(Server.getWorld(block.getWorld()), loc.getX(), loc.getY(), loc.getZ());
  }

  public ExBlock(ExWorld world, int x, int y, int z) {
    this.location = new ExLocation(world, x, y, z);
  }

  public ExLocation getLocation() {
    return location;
  }

  public Block getBlock() {
    return this.location.getBlock();
  }

  public Material getType() {
    return this.location.getBlock().getType();
  }

  public int getX() {
    return this.location.getBlockX();
  }

  public int getY() {
    return this.location.getBlockY();
  }

  public int getZ() {
    return this.location.getBlockZ();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return this.location.getExWorld().equals(((ExBlock) o).location.getExWorld())
           && this.getX() == ((ExBlock) o).getX()
           && this.getY() == ((ExBlock) o).getY()
           && this.getZ() == ((ExBlock) o).getZ();
  }

  @Override
  public int hashCode() {
    return Objects.hash(location.getExWorld(), this.getX(), this.getY(), this.getZ());
  }

  public ExBlock getRelative(BlockFace face) {
    return new ExBlock(this.location.getBlock().getRelative(face));
  }

  public ExBlock getRelative(Vector vector) {
    return this.location.clone().add(vector).getExBlock();
  }

  public ExBlock getRelative(int x, int y, int z) {
    return new ExBlock(this.location.getBlock().getRelative(x, y, z));
  }

  public ExBlock up() {
    return this.getRelative(0, 1, 0);
  }

  public ExBlock down() {
    return this.getRelative(0, -1, 0);
  }

  public boolean isBeside(Block location) {
    if (!this.getBlock().getWorld().equals(location.getWorld())) {
      return false;
    }

    for (Vector vector : NEAR_BLOCKS) {
      if (this.getBlock().getLocation().add(vector).getBlock().equals(location)) {
        return true;
      }
    }

    return false;
  }

  public List<Block> getBesideBlocks() {
    return NEAR_BLOCKS.stream().map(v -> this.location.add(v).getBlock()).collect(Collectors.toList());
  }
}
