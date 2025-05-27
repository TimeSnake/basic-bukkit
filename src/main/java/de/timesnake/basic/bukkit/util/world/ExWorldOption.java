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
  public static final ExWorldOption<Boolean> ALLOW_FLUID_PLACE = new ExWorldOption<>("allow_fluid_place", false);
  public static final ExWorldOption<Boolean> ALLOW_FLUID_FLOW = new ExWorldOption<>("allow_fluid_flow", false);
  public static final ExWorldOption<Boolean> ALLOW_ENTITY_BLOCK_BREAK = new ExWorldOption<>("allow_entity_block_break"
      , false);
  public static final ExWorldOption<Boolean> ALLOW_ITEM_FRAME_ROTATE = new ExWorldOption<>("allow_item_frame_rotate",
      false);
  public static final ExWorldOption<Boolean> ALLOW_DROP_PICK_ITEM = new ExWorldOption<>("allow_drop_pick_item", false);
  public static final ExWorldOption<Boolean> ENABLE_PLAYER_DAMAGE = new ExWorldOption<>("player_damage", false);
  public static final ExWorldOption<Boolean> CHANGE_FOOD = new ExWorldOption<>("change_food", false);
  public static final ExWorldOption<Boolean> ENABLE_ENTITY_EXPLOSION = new ExWorldOption<>("enable_entity_explosion",
      false);
  public static final ExWorldOption<Float> FIRE_SPREAD_SPEED = new ExWorldOption<>("fire_spread_speed", 1f);
  public static final ExWorldOption<Integer> FIRE_SPREAD_DISTANCE = new ExWorldOption<>("fire_spread_distance", 3);
  public static final ExWorldOption<Boolean> ENABLE_BLOCK_SPREAD = new ExWorldOption<>("enable_block_spread", false);
  public static final ExWorldOption<Boolean> BLOCK_BURN_UP = new ExWorldOption<>("block_burn_up", false);
  public static final ExWorldOption<Boolean> ALLOW_BLOCK_IGNITE = new ExWorldOption<>("allow_block_ignite", false);
  public static final ExWorldOption<Boolean> ALLOW_TNT_PRIME = new ExWorldOption<>("allow_tnt_prime", false);
  public static final ExWorldOption<Boolean> ALLOW_LIGHT_UP_INTERACTION = new ExWorldOption<>(
      "allow_light_up_interaction", false);
  public static final ExWorldOption<Boolean> ALLOW_FLINT_AND_STEEL = new ExWorldOption<>("allow_flint_and_steel",
      false);
  public static final ExWorldOption<Boolean> ALLOW_PLACE_IN_BLOCK = new ExWorldOption<>("allow_place_in_block", false);
  public static final ExWorldOption<Boolean> ALLOW_FIRE_PUNCH_OUT = new ExWorldOption<>("allow_fire_punch_out", false);
  public static final ExWorldOption<Boolean> ALLOW_CAKE_EAT = new ExWorldOption<>("allow_cake_eat", false);
  public static final ExWorldOption<Boolean> ALLOW_CRAFTING = new ExWorldOption<>("allow_crafting", false);
  public static final ExWorldOption<Boolean> AUTO_PRIME_TNT = new ExWorldOption<>("auto_prime_tnt", true);
  public static final ExWorldOption<Boolean> ALLOW_BED_ENTER = new ExWorldOption<>("allow_bed_enter", false);
  public static final ExWorldOption<List<Material>> FORBIDDEN_BLOCK_INVENTORIES = new ExWorldOption<>(
      "forbidden_block_inventories", List.of());

  public static final List<ExWorldOption<?>> VALUES = List.of(ALLOW_BLOCK_BREAK, ALLOW_FLUID_COLLECT,
      ALLOW_BLOCK_PLACE, ALLOW_FLUID_PLACE, ALLOW_FLUID_FLOW, ALLOW_ENTITY_BLOCK_BREAK, ALLOW_ITEM_FRAME_ROTATE,
      ALLOW_DROP_PICK_ITEM, ENABLE_PLAYER_DAMAGE, CHANGE_FOOD, ENABLE_ENTITY_EXPLOSION, FIRE_SPREAD_SPEED,
      FIRE_SPREAD_DISTANCE, ENABLE_BLOCK_SPREAD, BLOCK_BURN_UP, ALLOW_BLOCK_IGNITE, ALLOW_TNT_PRIME,
      ALLOW_LIGHT_UP_INTERACTION, ALLOW_FLINT_AND_STEEL, ALLOW_PLACE_IN_BLOCK, ALLOW_FIRE_PUNCH_OUT, ALLOW_CAKE_EAT,
      ALLOW_CRAFTING, AUTO_PRIME_TNT, ALLOW_BED_ENTER);

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
