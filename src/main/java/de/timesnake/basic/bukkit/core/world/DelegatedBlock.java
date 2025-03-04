/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.world;

import com.destroystokyo.paper.block.BlockSoundGroup;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.bukkit.util.VoxelShape;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public abstract class DelegatedBlock implements Block {

  public abstract Block getBlock();

  @Deprecated(since = "1.18.1")
  public static long getBlockKey(int x, int y, int z) {
    return Block.getBlockKey(x, y, z);
  }

  @Deprecated(since = "1.18.1")
  public static int getBlockKeyX(long packed) {
    return Block.getBlockKeyX(packed);
  }

  @Deprecated(since = "1.18.1")
  public static int getBlockKeyY(long packed) {
    return Block.getBlockKeyY(packed);
  }

  @Deprecated(since = "1.18.1")
  public static int getBlockKeyZ(long packed) {
    return Block.getBlockKeyZ(packed);
  }

  @Override
  @Deprecated(forRemoval = true, since = "1.18.2")
  public @NotNull BlockSoundGroup getSoundGroup() {
    return getBlock().getSoundGroup();
  }

  @Override
  @Deprecated(forRemoval = true)
  @NotNull
  public String getTranslationKey() {
    return getBlock().getTranslationKey();
  }

  @Deprecated
  public byte getData() {
    return getBlock().getData();
  }

  public boolean isBlockIndirectlyPowered() {
    return getBlock().isBlockIndirectlyPowered();
  }

  public double getTemperature() {
    return getBlock().getTemperature();
  }

  public void setMetadata(@NotNull String s, @NotNull MetadataValue metadataValue) {
    getBlock().setMetadata(s, metadataValue);
  }

  public byte getLightFromSky() {
    return getBlock().getLightFromSky();
  }

  public void tick() {
    getBlock().tick();
  }

  public boolean isValidTool(@NotNull ItemStack itemStack) {
    return getBlock().isValidTool(itemStack);
  }

  public float getDestroySpeed(@NotNull ItemStack itemStack) {
    return getBlock().getDestroySpeed(itemStack);
  }

  @NotNull
  public Block getRelative(@NotNull BlockFace blockFace, int i) {
    return getBlock().getRelative(blockFace, i);
  }

  public boolean isBurnable() {
    return getBlock().isBurnable();
  }

  public boolean breakNaturally(@NotNull ItemStack tool, boolean triggerEffect) {
    return getBlock().breakNaturally(tool, triggerEffect);
  }

  public boolean isSolid() {
    return getBlock().isSolid();
  }

  public boolean breakNaturally(@NotNull ItemStack itemStack, boolean b, boolean b1) {
    return getBlock().breakNaturally(itemStack, b, b1);
  }

  public boolean isEmpty() {
    return getBlock().isEmpty();
  }

  public int getZ() {
    return getBlock().getZ();
  }

  public @NotNull BlockState getState(boolean b) {
    return getBlock().getState(b);
  }

  public @NotNull Chunk getChunk() {
    return getBlock().getChunk();
  }

  @NotNull
  public Block getRelative(@NotNull BlockFace blockFace) {
    return getBlock().getRelative(blockFace);
  }

  public boolean isBuildable() {
    return getBlock().isBuildable();
  }

  public @NotNull BlockData getBlockData() {
    return getBlock().getBlockData();
  }

  public @NotNull Collection<ItemStack> getDrops(@Nullable ItemStack itemStack, @Nullable Entity entity) {
    return getBlock().getDrops(itemStack, entity);
  }

  public @NotNull Biome getBiome() {
    return getBlock().getBiome();
  }

  public @NotNull Biome getComputedBiome() {
    return getBlock().getComputedBiome();
  }

  public boolean breakNaturally(boolean b, boolean b1) {
    return getBlock().breakNaturally(b, b1);
  }

  public void setBlockData(@NotNull BlockData blockData) {
    getBlock().setBlockData(blockData);
  }

  public @NotNull World getWorld() {
    return getBlock().getWorld();
  }

  public boolean breakNaturally() {
    return getBlock().breakNaturally();
  }

  @NotNull
  public String translationKey() {
    return getBlock().translationKey();
  }

  public @NotNull PistonMoveReaction getPistonMoveReaction() {
    return getBlock().getPistonMoveReaction();
  }

  public int getX() {
    return getBlock().getX();
  }

  public @NotNull Location getLocation() {
    return getBlock().getLocation();
  }

  public void randomTick() {
    getBlock().randomTick();
  }

  public @Nullable RayTraceResult rayTrace(@NotNull Location location, @NotNull Vector vector, double v,
                                           @NotNull FluidCollisionMode fluidCollisionMode) {
    return getBlock().rayTrace(location, vector, v, fluidCollisionMode);
  }

  public float getDestroySpeed(@NotNull ItemStack itemStack, boolean considerEnchants) {
    return getBlock().getDestroySpeed(itemStack, considerEnchants);
  }

  public @NotNull BlockState getState() {
    return getBlock().getState();
  }

  @NotNull
  public Block getRelative(int i, int i1, int i2) {
    return getBlock().getRelative(i, i1, i2);
  }

  @Contract("null -> null; !null -> !null")
  public @Nullable Location getLocation(@Nullable Location location) {
    return getBlock().getLocation(location);
  }

  public byte getLightLevel() {
    return getBlock().getLightLevel();
  }

  public void setType(@NotNull Material material, boolean b) {
    getBlock().setType(material, b);
  }

  public boolean isBlockFacePowered(@NotNull BlockFace blockFace) {
    return getBlock().isBlockFacePowered(blockFace);
  }

  public boolean isCollidable() {
    return getBlock().isCollidable();
  }

  public float getBreakSpeed(@NotNull Player player) {
    return getBlock().getBreakSpeed(player);
  }

  public byte getLightFromBlocks() {
    return getBlock().getLightFromBlocks();
  }

  public void fluidTick() {
    getBlock().fluidTick();
  }

  public boolean isBlockFaceIndirectlyPowered(@NotNull BlockFace blockFace) {
    return getBlock().isBlockFaceIndirectlyPowered(blockFace);
  }

  public boolean isPassable() {
    return getBlock().isPassable();
  }

  public double getHumidity() {
    return getBlock().getHumidity();
  }

  public @NotNull List<MetadataValue> getMetadata(@NotNull String s) {
    return getBlock().getMetadata(s);
  }

  public boolean isReplaceable() {
    return getBlock().isReplaceable();
  }

  public void setBlockData(@NotNull BlockData blockData, boolean b) {
    getBlock().setBlockData(blockData, b);
  }

  public void setBiome(@NotNull Biome biome) {
    getBlock().setBiome(biome);
  }

  public @NotNull Material getType() {
    return getBlock().getType();
  }

  public @NotNull SoundGroup getBlockSoundGroup() {
    return getBlock().getBlockSoundGroup();
  }

  public void setType(@NotNull Material material) {
    getBlock().setType(material);
  }

  public boolean isPreferredTool(@NotNull ItemStack itemStack) {
    return getBlock().isPreferredTool(itemStack);
  }

  public boolean isBlockPowered() {
    return getBlock().isBlockPowered();
  }

  public @NotNull VoxelShape getCollisionShape() {
    return getBlock().getCollisionShape();
  }

  public @NotNull Collection<ItemStack> getDrops(@Nullable ItemStack itemStack) {
    return getBlock().getDrops(itemStack);
  }

  @Deprecated(since = "1.18.1")
  public long getBlockKey() {
    return getBlock().getBlockKey();
  }

  public @NotNull Collection<ItemStack> getDrops() {
    return getBlock().getDrops();
  }

  public boolean breakNaturally(boolean triggerEffect) {
    return getBlock().breakNaturally(triggerEffect);
  }

  public @NotNull BoundingBox getBoundingBox() {
    return getBlock().getBoundingBox();
  }

  public void removeMetadata(@NotNull String s, @NotNull Plugin plugin) {
    getBlock().removeMetadata(s, plugin);
  }

  public boolean hasMetadata(@NotNull String s) {
    return getBlock().hasMetadata(s);
  }

  public boolean canPlace(@NotNull BlockData blockData) {
    return getBlock().canPlace(blockData);
  }

  public boolean isLiquid() {
    return getBlock().isLiquid();
  }

  public @Nullable BlockFace getFace(@NotNull Block block) {
    return getBlock().getFace(block);
  }

  public int getBlockPower(@NotNull BlockFace blockFace) {
    return getBlock().getBlockPower(blockFace);
  }

  public int getY() {
    return getBlock().getY();
  }

  public int getBlockPower() {
    return getBlock().getBlockPower();
  }

  public boolean applyBoneMeal(@NotNull BlockFace blockFace) {
    return getBlock().applyBoneMeal(blockFace);
  }

  public boolean breakNaturally(@Nullable ItemStack itemStack) {
    return getBlock().breakNaturally(itemStack);
  }
}
