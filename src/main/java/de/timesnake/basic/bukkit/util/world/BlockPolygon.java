/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world;

import de.timesnake.library.basic.util.Triple;
import de.timesnake.library.basic.util.Tuple;
import org.apache.commons.lang3.stream.IntStreams;
import org.bukkit.block.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * The {@link BlockPolygon} class encapsulates a description of a closed, two-dimensional, vertically expanded region.
 * <p>
 * A block is described as contained in the polygon,
 * if its horizontall coordinates (x and z) are within the polygon boundaries (exclusively)
 * and its height (y) is higher equals the lowest polygon point and lower equals the highest polygon point.
 */
public class BlockPolygon {

  public static BlockPolygon fromCubic(ExWorld world, Block corner1, Block corner2) {
    int beginX = Math.min(corner1.getX(), corner2.getX());
    int beginZ = Math.min(corner1.getZ(), corner2.getZ());

    int endX = Math.max(corner1.getX(), corner2.getX());
    int endZ = Math.max(corner1.getZ(), corner2.getZ());
    return new BlockPolygon(world, new Polygon(new int[]{beginX, beginX, endX, endX},
        new int[]{beginZ, endZ, endZ, beginZ}, 4),
        Math.min(corner1.getY(), corner2.getY()), Math.max(corner1.getY(), corner2.getY()));
  }

  private transient ExWorld world;
  private final Polygon polygon;
  private final int minHeight;
  private final int maxHeight;
  private final boolean unbounded;

  private transient Triple<Integer, Integer, Integer> center;
  private transient List<Tuple<Integer, Integer>> containedPointsSortedByXZ;
  private transient List<Tuple<Integer, List<Integer>>> containedPointsSortedByXThenZ;
  private transient List<Tuple<Integer, Integer>> containedPointsSortedByDistanceToCenter;

  public BlockPolygon(ExWorld world, Polygon polygon, int minHeight, int maxHeight) {
    this.world = world;
    this.polygon = polygon;
    this.minHeight = minHeight;
    this.maxHeight = maxHeight;
    this.unbounded = false;
  }

  public BlockPolygon(ExWorld world, Collection<ExLocation> locations) {
    this.world = world;
    this.polygon = new Polygon();
    this.unbounded = locations.size() <= 2;

    int minHeight = Integer.MAX_VALUE;
    int maxHeight = Integer.MIN_VALUE;
    for (ExLocation location : locations) {
      this.polygon.addPoint(location.getBlockX(), location.getBlockZ());
      if (location.getBlockY() < minHeight) {
        minHeight = location.getBlockY();
      } else if (location.getBlockY() > maxHeight) {
        maxHeight = location.getBlockY();
      }
    }
    this.minHeight = minHeight;
    this.maxHeight = maxHeight;
  }

  public BlockPolygon(List<Integer> xPoints, List<Integer> zPoints, int minHeight, int maxHeight) {
    this.polygon = new Polygon(xPoints.stream().mapToInt(i -> i).toArray(),
        zPoints.stream().mapToInt(i -> i).toArray(), xPoints.size());
    this.minHeight = minHeight;
    this.maxHeight = maxHeight;
    this.unbounded = false;
  }

  public ExWorld getWorld() {
    return world;
  }

  public void setWorld(ExWorld world) {
    this.world = world;
  }

  public Polygon getPolygon() {
    return polygon;
  }

  public int getMinHeight() {
    return minHeight;
  }

  public int getMaxHeight() {
    return maxHeight;
  }

  /**
   * Checks if this polygon contains a given block.
   * <p>
   * A block is contained in the polygon, if its height (y) {@code minHeight <= y <= maxHeight} and
   * the horizontal polygon contains it (see {@link Polygon}).
   * <b>But:</b> this implementation defines blocks on edges of the polygon as <b>not</b> contained.
   *
   * @param block The {@link ExBlock} to check
   * @return {@code true} if this polygon contains the block, {@code false} otherwise
   */
  public boolean contains(ExBlock block) {
    return this.unbounded || (block.getExWorld().equals(this.world)
                              && block.getY() >= this.minHeight
                              && block.getY() <= this.maxHeight
                              && this.polygon.contains(block.getX(), block.getZ())
                              && this.polygon.contains(block.getX() - 1, block.getZ() - 1));
  }

  public boolean contains(ExLocation location) {
    return this.contains(location.getExBlock());
  }

  public ExBlock getCenterBlock() {
    if (this.center == null) {
      this.getCenter();
    }

    return new ExBlock(this.world, this.center.getA(), this.center.getB(), this.center.getC());
  }

  public Triple<Integer, Integer, Integer> getCenter() {
    if (this.center != null) {
      return this.center;
    }

    int x = Arrays.stream(this.polygon.xpoints).sum() / this.polygon.npoints;
    int y = (this.minHeight + this.maxHeight) / 2;
    int z = Arrays.stream(this.polygon.ypoints).sum() / this.polygon.npoints;
    this.center = new Triple<>(x, y, z);

    return this.center;
  }

  public List<Tuple<Integer, Integer>> getPointsInside() {
    if (this.containedPointsSortedByXZ != null) {
      return this.containedPointsSortedByXZ;
    }

    this.containedPointsSortedByXZ = new ArrayList<>();
    this.containedPointsSortedByXThenZ = new ArrayList<>();

    Rectangle rectangle = this.polygon.getBounds();
    for (int x = rectangle.x; x < rectangle.x + rectangle.width; x++) {
      List<Integer> zList = new ArrayList<>();
      for (int z = rectangle.y; z < rectangle.y + rectangle.height; z++) {
        if (this.polygon.contains(x, z) && this.polygon.contains(x - 1, z - 1)) {
          this.containedPointsSortedByXZ.add(new Tuple<>(x, z));
          zList.add(z);
        }
      }
      this.containedPointsSortedByXThenZ.add(new Tuple<>(x, zList));
    }

    return this.containedPointsSortedByXZ;
  }

  public List<Tuple<Integer, Integer>> getPointsInsideByDistanceToCenter() {
    if (this.containedPointsSortedByDistanceToCenter != null) {
      return this.containedPointsSortedByDistanceToCenter;
    }

    this.containedPointsSortedByDistanceToCenter = new ArrayList<>(this.getPointsInside());
    this.containedPointsSortedByDistanceToCenter.sort((t1, t2) -> Integer.compare(getDistanceToCenterSquared(t1),
        getDistanceToCenterSquared(t2)));
    return this.containedPointsSortedByDistanceToCenter;
  }

  private int getDistanceToCenterSquared(Tuple<Integer, Integer> point) {
    int aDelta = this.getCenter().getA() - point.getA();
    int bDelta = this.getCenter().getC() - point.getB();
    return Math.abs(aDelta * aDelta + bDelta * bDelta);
  }

  public List<ExBlock> getBlocksInside(Predicate<ExBlock> filter) {
    return this.getPointsInside().stream()
        .map(p -> this.world.getExBlockAt(p.getA(), this.minHeight, p.getB()))
        .flatMap(b -> IntStreams.range(this.maxHeight).mapToObj(i -> b.getExRelative(0, i, 0)))
        .filter(filter)
        .toList();
  }

  public List<ExBlock> getBlocksInsideSortedByXZ(Predicate<ExBlock> filter) {
    return this.getBlocksInside(filter);
  }

  public List<List<ExBlock>> getBlocksInsideSortedByXThenZ() {
    this.getPointsInside();
    return this.containedPointsSortedByXThenZ.stream()
        .map(v -> v.getB().stream().map(z -> this.world.getExBlockAt(v.getA(), this.minHeight, z)).toList())
        .toList();
  }

  public List<ExBlock> getBlocksInsideSortedByDistanceToCenter(Predicate<ExBlock> filter) {
    return this.getPointsInsideByDistanceToCenter().stream()
        .map(p -> this.world.getExBlockAt(p.getA(), this.minHeight, p.getB()))
        .flatMap(b -> IntStreams.range(this.maxHeight).mapToObj(i -> b.getExRelative(0, i, 0)))
        .filter(filter)
        .toList();
  }

  public List<ExBlock> getBlocksInsideOnMinHeight(Predicate<ExBlock> filter) {
    return this.getPointsInside().stream()
        .map(p -> this.world.getExBlockAt(p.getA(), this.minHeight, p.getB()))
        .filter(filter)
        .toList();
  }

  /**
   * Gets the highest not empty blocks.
   * <p>
   * For each point in the polygon, it searches for the highest not empty block.
   * If no empty block exists on a location, it will be skipped.
   *
   * @return Returns a list of highest not empty blocks.
   */
  public List<ExBlock> getHighestBlocksInside() {
    return this.getPointsInside().stream()
        .map(p -> this.world.getHighestExBlockAt(p.getA(), p.getB()))
        .filter(b -> !b.isEmpty())
        .toList();
  }

  public int getMaxDiameter() {
    Rectangle rectangle = this.polygon.getBounds();
    return Math.max(rectangle.height, rectangle.width);
  }

  public int getXDiameter() {
    return this.polygon.getBounds().width;
  }

  public int getZDiameter() {
    return this.polygon.getBounds().height;
  }
}


