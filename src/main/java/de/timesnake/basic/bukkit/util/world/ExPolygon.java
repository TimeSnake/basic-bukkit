/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world;

import de.timesnake.library.basic.util.Triple;
import de.timesnake.library.basic.util.Tuple;
import org.apache.commons.lang3.stream.IntStreams;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class ExPolygon {

  private transient ExWorld world;
  private final Polygon polygon;
  private final int minHeight;
  private final int maxHeight;
  private final boolean unbounded;

  private transient Triple<Integer, Integer, Integer> center;
  private transient List<Tuple<Integer, Integer>> containedPoints;

  public ExPolygon(ExWorld world, Polygon polygon, int minHeight, int maxHeight) {
    this.world = world;
    this.polygon = polygon;
    this.minHeight = minHeight;
    this.maxHeight = maxHeight;
    this.unbounded = false;
  }

  public ExPolygon(ExWorld world, Collection<ExLocation> locations) {
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

  public ExPolygon(List<Integer> xPoints, List<Integer> zPoints, int minHeight, int maxHeight) {
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

  public boolean contains(ExLocation location) {
    return this.unbounded || (location.getExWorld().equals(this.world)
                              && location.getY() >= this.minHeight
                              && location.getY() < this.maxHeight
                              && this.polygon.contains(location.getX(), location.getZ())
                              && this.polygon.contains(location.getX() - 1, location.getZ() - 1));
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
    int y = this.minHeight + this.maxHeight / 2;
    int z = Arrays.stream(this.polygon.ypoints).sum() / this.polygon.npoints;
    this.center = new Triple<>(x, y, z);

    return this.center;
  }

  public List<Tuple<Integer, Integer>> getPointsInside() {
    if (this.containedPoints != null) {
      return this.containedPoints;
    }

    this.containedPoints = new ArrayList<>();

    Rectangle rectangle = this.polygon.getBounds();
    for (int x = rectangle.x; x < rectangle.x + rectangle.width; x++) {
      for (int y = rectangle.y; y < rectangle.y + rectangle.height; y++) {
        if (this.polygon.contains(x, y) && this.polygon.contains(x - 1, y - 1)) {
          this.containedPoints.add(new Tuple<>(x, y));
        }
      }
    }

    this.containedPoints.sort((t1, t2) -> Integer.compare(getDistanceToCenterSquared(t1),
        getDistanceToCenterSquared(t2)));
    return this.containedPoints;
  }

  private int getDistanceToCenterSquared(Tuple<Integer, Integer> point) {
    int aDelta = this.getCenter().getA() - point.getA();
    int bDelta = this.getCenter().getC() - point.getB();
    return Math.abs(aDelta * aDelta + bDelta * bDelta);
  }

  public Collection<ExBlock> getBlocksInside(Predicate<ExBlock> filter) {
    return this.getBlocksInsideSortedByDistanceToCenter(filter);
  }

  public List<ExBlock> getBlocksInsideSortedByDistanceToCenter(Predicate<ExBlock> filter) {
    return this.getPointsInside().stream()
        .map(p -> this.world.getExBlockAt(p.getA(), this.minHeight, p.getB()))
        .flatMap(b -> IntStreams.range(this.maxHeight).mapToObj(i -> b.getRelative(0, i, 0)))
        .filter(filter)
        .toList();
  }

  public int getMaxDiameter() {
    Rectangle rectangle = this.polygon.getBounds();
    return Math.max(rectangle.height, rectangle.width);
  }
}
