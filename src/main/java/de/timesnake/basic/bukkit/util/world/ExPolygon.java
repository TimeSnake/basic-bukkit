/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world;

import de.timesnake.library.basic.util.Tuple;
import org.apache.commons.lang3.stream.IntStreams;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExPolygon {

  private transient ExWorld world;
  private final Polygon polygon;
  private final int minHeight;
  private final int maxHeight;
  private final boolean unbounded;
  private Collection<Tuple<Integer, Integer>> containedPoints;

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

  public Collection<Tuple<Integer, Integer>> getPointsInside() {
    if (this.containedPoints != null) {
      return this.containedPoints;
    }

    this.containedPoints = new ArrayList<>();

    Rectangle rectangle = this.polygon.getBounds();
    for (int x = rectangle.x; x < rectangle.x + rectangle.width; x++) {
      for (int y = rectangle.y; y < rectangle.y + rectangle.height; y++) {
        if (this.polygon.contains(x, y)) {
          this.containedPoints.add(new Tuple<>(x, y));
        }
      }
    }
    return this.containedPoints;
  }

  private Collection<ExBlock> getBlocksInside() {
    return this.getPointsInside().stream()
        .map(p -> this.world.getExBlockAt(p.getA(), this.minHeight, p.getB()))
        .flatMap(b -> IntStreams.range(this.maxHeight).mapToObj(i -> b.getRelative(0, i, 0)))
        .toList();
  }

  public Collection<ExBlock> getSolidBlocksInside() {
    return this.getBlocksInside().stream().filter(b -> b.getBlock().isSolid()).toList();
  }

  public int getMaxDiameter() {
    Rectangle rectangle = this.polygon.getBounds();
    return Math.max(rectangle.height, rectangle.width);
  }
}
