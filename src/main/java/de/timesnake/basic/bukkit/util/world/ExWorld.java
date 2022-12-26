/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.util.world;

import de.timesnake.basic.bukkit.core.world.DelegatedWorld;
import de.timesnake.basic.bukkit.core.world.ExWorldFile;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.Material;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExWorld extends DelegatedWorld implements World {

    @NotNull
    public static Audience empty() {
        return Audience.empty();
    }

    @NotNull
    public static Audience audience(@NotNull Audience @NotNull ... audiences) {
        return Audience.audience(audiences);
    }

    @NotNull
    public static ForwardingAudience audience(@NotNull Iterable<? extends Audience> audiences) {
        return Audience.audience(audiences);
    }

    private final ExWorldType type;
    private final ExWorldFile file;
    private final Map<Restriction<?>, Object> restrictionValues;
    private boolean safe;
    private boolean exceptService = true;

    public ExWorld(org.bukkit.World world, ExWorldFile file) {
        this(world, file.getWorldType(), file);
    }

    public ExWorld(org.bukkit.World world, ExWorldType type, ExWorldFile file) {
        this(world, type, file, Restriction.VALUES.stream().collect(Collectors.toMap(r -> r, Restriction::getDefaultValue)));
    }

    public ExWorld(org.bukkit.World world, ExWorldType type, ExWorldFile file, Map<Restriction<?>, Object> restrictions) {
        super(world);
        this.type = type;
        this.file = file;

        if (this.file.isSafe()) {
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

    @Override
    public void setAutoSave(boolean safe) {
        super.setAutoSave(safe);
        this.safe = safe;
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

    public void removePlayers() {
        for (User user : Server.getUsers()) {
            if (user.getLocation().getWorld().equals(this.getBukkitWorld())) {
                user.teleport(Server.getWorldManager().getBasicWorld());
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

    public static class Restriction<Value> {

        public static final Restriction<Boolean> BLOCK_BREAK = new Restriction<>("block_break", false);
        public static final Restriction<Boolean> FLUID_COLLECT = new Restriction<>("fluid_collect", false);
        public static final Restriction<Boolean> BLOCK_PLACE = new Restriction<>("block_place", false);
        public static final Restriction<Boolean> FLUID_PLACE = new Restriction<>("fluid_place", false);
        public static final Restriction<Boolean> ENTITY_BLOCK_BREAK = new Restriction<>("entity_block_break", false);
        public static final Restriction<Boolean> ITEM_FRAME_ROTATE = new Restriction<>("item_frame_rotate", false);
        public static final Restriction<Boolean> DROP_PICK_ITEM = new Restriction<>("drop_pick_item", false);
        public static final Restriction<Boolean> PLAYER_DAMAGE = new Restriction<>("player_damage", false);
        public static final Restriction<Boolean> FOOD_CHANGE = new Restriction<>("food_change", false);
        public static final Restriction<Boolean> ENTITY_EXPLODE = new Restriction<>("entity_explode", false);
        public static final Restriction<Boolean> FIRE_SPREAD = new Restriction<>("fire_spread", false);
        public static final Restriction<Boolean> BLOCK_BURN_UP = new Restriction<>("block_burn_up", false);
        public static final Restriction<Boolean> BLOCK_IGNITE = new Restriction<>("block_ignite", false);
        public static final Restriction<Boolean> LIGHT_UP_INTERACTION = new Restriction<>("light_up_interaction", false);
        public static final Restriction<Boolean> FLINT_AND_STEEL = new Restriction<>("flint_and_steel", false);
        public static final Restriction<Boolean> PLACE_IN_BLOCK = new Restriction<>("place_in_block", false);
        public static final Restriction<Boolean> FIRE_PUNCH_OUT = new Restriction<>("fire_punch_out", false);
        public static final Restriction<Boolean> CAKE_EAT = new Restriction<>("cake_eat", false);
        public static final Restriction<List<Material>> OPEN_INVENTORIES = new Restriction<>("open_inventories", List.of());

        public static final List<Restriction<?>> VALUES = List.of(
                BLOCK_BREAK,
                FLUID_COLLECT,
                BLOCK_PLACE,
                FLUID_PLACE,
                ENTITY_BLOCK_BREAK,
                ITEM_FRAME_ROTATE,
                DROP_PICK_ITEM,
                PLAYER_DAMAGE,
                FOOD_CHANGE,
                ENTITY_EXPLODE,
                FIRE_SPREAD,
                BLOCK_BURN_UP,
                BLOCK_IGNITE,
                LIGHT_UP_INTERACTION,
                FLINT_AND_STEEL,
                PLACE_IN_BLOCK,
                FIRE_PUNCH_OUT,
                CAKE_EAT,
                OPEN_INVENTORIES
        );

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
