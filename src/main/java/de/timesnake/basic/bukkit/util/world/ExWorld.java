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
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;

import java.util.*;
import java.util.stream.Collectors;

public class ExWorld extends DelegatedWorld {

  private final ExWorldType type;
  private final ExWorldFile file;
  private final Random random;
  private final Map<Restriction<?>, Object> restrictionValues;
  private boolean safe;
  private boolean exceptService = true;

  private boolean temporary;

  public ExWorld(org.bukkit.World world, ExWorldFile file) {
    this(world, file.getWorldType(), file);
  }

  public ExWorld(org.bukkit.World world, ExWorldType type, ExWorldFile file) {
    this(world, type, file, Restriction.VALUES.stream().collect(Collectors.toMap(r -> r,
        Restriction::getDefaultValue)));
  }

  public ExWorld(org.bukkit.World world, ExWorldType type, ExWorldFile file, boolean temporary) {
    this(world, type, file, Restriction.VALUES.stream().collect(Collectors.toMap(r -> r,
        Restriction::getDefaultValue)), temporary);
  }

  public ExWorld(org.bukkit.World world, ExWorldType type, ExWorldFile file, Map<Restriction<?>, Object> restrictions) {
    this(world, type, file, restrictions, false);
  }

  public ExWorld(org.bukkit.World world, ExWorldType type, ExWorldFile file, Map<Restriction<?>, Object> restrictions,
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

    this.restrictionValues = Objects.requireNonNullElseGet(restrictions, HashMap::new);
    Restriction.VALUES.stream().filter(restriction -> !this.restrictionValues.containsKey(restriction))
        .forEach(restriction -> this.restrictionValues.put(restriction, restriction.getDefaultValue()));
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

  public <Value> void restrict(Restriction<Value> restriction, Value value) {
    this.restrictionValues.put(restriction, value);
  }

  public <Value> Value isRestricted(Restriction<Value> restriction) {
    return (Value) this.restrictionValues.get(restriction);
  }

  public Map<Restriction<?>, Object> getRestrictionValues() {
    return restrictionValues;
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

  public static class Restriction<Value> {

    public static final Restriction<Boolean> BLOCK_BREAK = new Restriction<>("block_break", false);
    public static final Restriction<Boolean> FLUID_COLLECT = new Restriction<>("fluid_collect", false);
    public static final Restriction<Boolean> BLOCK_PLACE = new Restriction<>("block_place", false);
    public static final Restriction<Boolean> FLUID_PLACE = new Restriction<>("fluid_place", false);
    public static final Restriction<Boolean> FLUID_FLOW = new Restriction<>("fluid_flow", false);
    public static final Restriction<Boolean> ENTITY_BLOCK_BREAK = new Restriction<>("entity_block_break", false);
    public static final Restriction<Boolean> ITEM_FRAME_ROTATE = new Restriction<>("item_frame_rotate", false);
    public static final Restriction<Boolean> DROP_PICK_ITEM = new Restriction<>("drop_pick_item", false);
    public static final Restriction<Boolean> NO_PLAYER_DAMAGE = new Restriction<>("no_player_damage", false);
    public static final Restriction<Boolean> FOOD_CHANGE = new Restriction<>("food_change", false);
    public static final Restriction<Boolean> ENTITY_EXPLODE = new Restriction<>("entity_explode", false);
    public static final Restriction<Float> FIRE_SPREAD_SPEED = new Restriction<>("fire_spread_speed", 1f);
    public static final Restriction<Integer> FIRE_SPREAD_DISTANCE = new Restriction<>("fire_spread_distance", 3);
    public static final Restriction<Boolean> BLOCK_SPREAD = new Restriction<>("block_spread", false);
    public static final Restriction<Boolean> BLOCK_BURN_UP = new Restriction<>("block_burn_up", false);
    public static final Restriction<Boolean> BLOCK_IGNITE = new Restriction<>("block_ignite", false);
    public static final Restriction<Boolean> TNT_PRIME = new Restriction<>("tnt_prime", false);
    public static final Restriction<Boolean> LIGHT_UP_INTERACTION = new Restriction<>("light_up_interaction", false);
    public static final Restriction<Boolean> FLINT_AND_STEEL = new Restriction<>("flint_and_steel", false);
    public static final Restriction<Boolean> PLACE_IN_BLOCK = new Restriction<>("place_in_block", false);
    public static final Restriction<Boolean> FIRE_PUNCH_OUT = new Restriction<>("fire_punch_out", false);
    public static final Restriction<Boolean> CAKE_EAT = new Restriction<>("cake_eat", false);
    public static final Restriction<Boolean> CRAFTING = new Restriction<>("crafting", false);
    public static final Restriction<Boolean> AUTO_PRIME_TNT = new Restriction<>("auto_prime_tnt", true);
    public static final Restriction<Boolean> BED_ENTER = new Restriction<>("bed_enter", false);
    public static final Restriction<List<Material>> OPEN_INVENTORIES = new Restriction<>("open_inventories", List.of());

    public static final List<Restriction<?>> VALUES = List.of(BLOCK_BREAK, FLUID_COLLECT, BLOCK_PLACE, FLUID_PLACE,
        FLUID_FLOW, ENTITY_BLOCK_BREAK, ITEM_FRAME_ROTATE, DROP_PICK_ITEM, NO_PLAYER_DAMAGE, FOOD_CHANGE,
        ENTITY_EXPLODE, FIRE_SPREAD_SPEED, FIRE_SPREAD_DISTANCE, BLOCK_SPREAD, BLOCK_BURN_UP, BLOCK_IGNITE, TNT_PRIME,
        LIGHT_UP_INTERACTION, FLINT_AND_STEEL, PLACE_IN_BLOCK, FIRE_PUNCH_OUT, CAKE_EAT, CRAFTING, AUTO_PRIME_TNT,
        BED_ENTER, OPEN_INVENTORIES);

    private final String name;
    private final Value defaultValue;

    private Restriction(String name, Value defaultValue) {
      this.name = name;
      this.defaultValue = defaultValue;
    }

    public String getName() {
      return name;
    }

    public Value getDefaultValue() {
      return defaultValue;
    }
  }

}
