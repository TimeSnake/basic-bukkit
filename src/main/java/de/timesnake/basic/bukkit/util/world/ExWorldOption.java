/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world;

import org.bukkit.Material;

import java.util.List;

public class ExWorldOption<Value> {

  public static final ExWorldOption<Boolean> ALLOW_BLOCK_BREAK = new ExWorldOption<>("allow_block_break", true);
  public static final ExWorldOption<Boolean> ALLOW_FLUID_COLLECT = new ExWorldOption<>("allow_fluid_collect", true);
  public static final ExWorldOption<Boolean> ALLOW_BLOCK_PLACE = new ExWorldOption<>("allow_block_place", true);
  public static final ExWorldOption<Boolean> ALLOW_FLUID_PLACE = new ExWorldOption<>("allow_fluid_place", true);
  public static final ExWorldOption<Boolean> ALLOW_FLUID_FLOW = new ExWorldOption<>("allow_fluid_flow", true);
  public static final ExWorldOption<Boolean> ALLOW_ENTITY_BLOCK_BREAK = new ExWorldOption<>("allow_entity_block_break"
      , true);
  public static final ExWorldOption<Boolean> ALLOW_ITEM_FRAME_ROTATE = new ExWorldOption<>("allow_item_frame_rotate",
      true);
  public static final ExWorldOption<Boolean> ALLOW_DROP_PICK_ITEM = new ExWorldOption<>("allow_drop_pick_item", true);
  public static final ExWorldOption<Boolean> ENABLE_PLAYER_DAMAGE = new ExWorldOption<>("player_damage", true);
  public static final ExWorldOption<Boolean> CHANGE_FOOD = new ExWorldOption<>("change_food", true);
  public static final ExWorldOption<Boolean> ENABLE_ENTITY_EXPLOSION = new ExWorldOption<>("enable_entity_explosion",
      false);
  public static final ExWorldOption<Boolean> ENABLE_BLOCK_EXPLOSION = new ExWorldOption<>("enable_block_explosion",
      false);
  public static final ExWorldOption<Float> FIRE_SPREAD_SPEED = new ExWorldOption<>("fire_spread_speed", 1f);
  public static final ExWorldOption<Integer> FIRE_SPREAD_DISTANCE = new ExWorldOption<>("fire_spread_distance", 3);
  public static final ExWorldOption<Boolean> ENABLE_BLOCK_SPREAD = new ExWorldOption<>("enable_block_spread", true);
  public static final ExWorldOption<Boolean> BLOCK_BURN_UP = new ExWorldOption<>("block_burn_up", true);
  public static final ExWorldOption<Boolean> ALLOW_BLOCK_IGNITE = new ExWorldOption<>("allow_block_ignite", true);
  public static final ExWorldOption<Boolean> ALLOW_TNT_PRIME = new ExWorldOption<>("allow_tnt_prime", true);
  public static final ExWorldOption<Boolean> ALLOW_LIGHT_UP_INTERACTION = new ExWorldOption<>(
      "allow_light_up_interaction", true);
  public static final ExWorldOption<Boolean> ALLOW_FLINT_AND_STEEL_AND_FIRE_CHARGE = new ExWorldOption<>(
      "allow_flint_and_steel",
      true);
  public static final ExWorldOption<Boolean> ALLOW_PLACE_IN_BLOCK = new ExWorldOption<>("allow_place_in_block", true);
  public static final ExWorldOption<Boolean> ALLOW_FIRE_PUNCH_OUT = new ExWorldOption<>("allow_fire_punch_out", true);
  public static final ExWorldOption<Boolean> ALLOW_CAKE_EAT = new ExWorldOption<>("allow_cake_eat", true);
  public static final ExWorldOption<Boolean> ALLOW_CRAFTING = new ExWorldOption<>("allow_crafting", true);
  public static final ExWorldOption<Boolean> AUTO_PRIME_TNT = new ExWorldOption<>("auto_prime_tnt", false);
  public static final ExWorldOption<Boolean> ALLOW_BED_ENTER = new ExWorldOption<>("allow_bed_enter", true);
  public static final ExWorldOption<List<Material>> FORBIDDEN_BLOCK_INVENTORIES = new ExWorldOption<>(
      "forbidden_block_inventories", List.of());

  public static final List<ExWorldOption<?>> VALUES = List.of(ALLOW_BLOCK_BREAK, ALLOW_FLUID_COLLECT,
      ALLOW_BLOCK_PLACE, ALLOW_FLUID_PLACE, ALLOW_FLUID_FLOW, ALLOW_ENTITY_BLOCK_BREAK, ALLOW_ITEM_FRAME_ROTATE,
      ALLOW_DROP_PICK_ITEM, ENABLE_PLAYER_DAMAGE, CHANGE_FOOD, ENABLE_ENTITY_EXPLOSION, FIRE_SPREAD_SPEED,
      FIRE_SPREAD_DISTANCE, ENABLE_BLOCK_SPREAD, BLOCK_BURN_UP, ALLOW_BLOCK_IGNITE, ALLOW_TNT_PRIME,
      ALLOW_LIGHT_UP_INTERACTION, ALLOW_FLINT_AND_STEEL_AND_FIRE_CHARGE, ALLOW_PLACE_IN_BLOCK, ALLOW_FIRE_PUNCH_OUT,
      ALLOW_CAKE_EAT,
      ALLOW_CRAFTING, AUTO_PRIME_TNT, ALLOW_BED_ENTER, FORBIDDEN_BLOCK_INVENTORIES);

  private final String name;
  private final Value defaultValue;

  private ExWorldOption(String name, Value defaultValue) {
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
