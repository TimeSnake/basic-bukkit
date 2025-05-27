/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world;

import de.timesnake.basic.bukkit.core.world.DelegatedWorld;
import de.timesnake.basic.bukkit.core.world.ExWorldFile;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;

import java.util.*;
import java.util.stream.Collectors;

public class ExWorld extends DelegatedWorld {

  private final ExWorldType type;
  private final ExWorldFile file;
  private final Random random;
  private final Map<ExWorldOption<?>, Object> options;
  private boolean safe;
  private boolean exceptService = true;

  private boolean temporary;

  public ExWorld(org.bukkit.World world, ExWorldFile file) {
    this(world, file.getWorldType(), file);
  }

  public ExWorld(org.bukkit.World world, ExWorldType type, ExWorldFile file) {
    this(world, type, file, ExWorldOption.VALUES.stream().collect(Collectors.toMap(r -> r,
        ExWorldOption::getDefaultValue)));
  }

  public ExWorld(org.bukkit.World world, ExWorldType type, ExWorldFile file, boolean temporary) {
    this(world, type, file, ExWorldOption.VALUES.stream().collect(Collectors.toMap(r -> r,
        ExWorldOption::getDefaultValue)), temporary);
  }

  public ExWorld(org.bukkit.World world, ExWorldType type, ExWorldFile file,
                 Map<ExWorldOption<?>, Object> ExWorldOptions) {
    this(world, type, file, ExWorldOptions, false);
  }

  public ExWorld(org.bukkit.World world, ExWorldType type, ExWorldFile file,
                 Map<ExWorldOption<?>, Object> ExWorldOptions,
                 boolean temporary) {
    super(world);
    this.type = type;
    this.file = file;
    this.random = new Random(this.getSeed());
    this.temporary = temporary;

    if (this.file.isSafe() && !temporary) {
      this.setAutoSave(true);
      this.safe = true;
    } else {
      this.setAutoSave(false);
      this.safe = false;
    }

    this.options = Objects.requireNonNullElseGet(ExWorldOptions, HashMap::new);
    ExWorldOption.VALUES.stream().filter(ExWorldOption -> !this.options.containsKey(ExWorldOption))
        .forEach(ExWorldOption -> this.options.put(ExWorldOption, ExWorldOption.getDefaultValue()));
  }

  public ServerLevel getHandle() {
    return ((CraftWorld) this.world).getHandle();
  }

  @Override
  public void setAutoSave(boolean safe) {
    super.setAutoSave(safe);
    this.safe = safe;
  }

  public boolean isTemporary() {
    return temporary;
  }

  public void makePersistent() {
    this.temporary = false;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof World && ((World) obj).getName().equals(this.getName())) {
      return true;
    }
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.world.getName());
  }

  public boolean isSafe() {
    return safe;
  }

  public Random getRandom() {
    return random;
  }

  public void removePlayers() {
    for (User user : Server.getUsers()) {
      if (user.getLocation().getWorld().equals(this.getBukkitWorld())) {
        user.teleport(Server.getWorldManager().getDefaultWorld());
      }
    }
  }

  public ExWorldFile getExFile() {
    return file;
  }

  public Map<ExWorldOption<?>, Object> getOptions() {
    return options;
  }

  public <Value> Value getOption(ExWorldOption<Value> option) {
    return (Value) this.options.get(option);
  }

  public <Value> void setOption(ExWorldOption<Value> option, Value value) {
    this.options.put(option, value);
  }

  public boolean isExceptService() {
    return exceptService;
  }

  public void setExceptService(boolean exceptService) {
    this.exceptService = exceptService;
  }

  public ExWorldType getType() {
    return type;
  }

  public World getBukkitWorld() {
    return this.world;
  }

  public void setBukkitWorld(World world) {
    this.world = world;

    this.world.setAutoSave(this.safe);
  }

  public ExBlock getExBlockAt(int x, int y, int z) {
    return new ExBlock(this.getBlockAt(x, y, z));
  }

  public ExBlock getHighestExBlockAt(int x, int z) {
    return new ExBlock(this.getHighestBlockAt(x, z));
  }

  public Collection<ExBlock> getBlocksWithinCubic(Location loc1, Location loc2) {
    int fromX = Math.min(loc1.getBlockX(), loc2.getBlockX());
    int toX = Math.max(loc1.getBlockX(), loc2.getBlockX());

    int fromY = Math.min(loc1.getBlockY(), loc2.getBlockY());
    int toY = Math.max(loc1.getBlockY(), loc2.getBlockY());

    int fromZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
    int toZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

    Set<ExBlock> blocks = new HashSet<>();

    for (int x = fromX; x <= toX; x++) {
      for (int y = fromY; y <= toY; y++) {
        for (int z = fromZ; z <= toZ; z++) {
          blocks.add(this.getExBlockAt(x, y, z));
        }
      }
    }
    return blocks;
  }

  public Collection<ExBlock> getBlocksWithinCubicDistance(Location loc, int distance) {
    Set<ExBlock> blocks = new HashSet<>();

    for (int x = -distance; x < distance; x++) {
      for (int y = -distance; y < distance; y++) {
        for (int z = -distance; z < distance; z++) {
          blocks.add(this.getExBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z));
        }
      }
    }
    return blocks;
  }

  public Collection<ExBlock> getBlocksWithinCubicDistanceHorizontal(Location loc, int distance) {
    Set<ExBlock> blocks = new HashSet<>();

    for (int x = -distance; x < distance; x++) {
      for (int z = -distance; z < distance; z++) {
        blocks.add(this.getExBlockAt(loc.getBlockX() + x, loc.getBlockY(), loc.getBlockZ() + z));
      }
    }
    return blocks;
  }

  public ExBlock getRandomExBlockWithinCubicDistanceHorizontal(Location loc, int distance) {
    return this.getRandomExBlockWithinCubicDistanceHorizontal(loc.getBlock(), distance);
  }

  public ExBlock getRandomExBlockWithinCubicDistanceHorizontal(Block block, int distance) {
    return this.getExBlockAt(block.getX() + this.random.nextInt(2 * distance) - distance,
        block.getY(), block.getZ() + this.random.nextInt(2 * distance) - distance);
  }

  @Override
  public String toString() {
    return "ExWorld{" +
        "name=" + world.getName() +
        "type=" + type +
        ", safe=" + safe +
        ", exceptService=" + exceptService +
        ", temporary=" + temporary +
        '}';
  }

}
