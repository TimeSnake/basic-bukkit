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

  /**
   * @deprecated
   */
  @Deprecated(since = "1.6.2", forRemoval = true)
  @Override
  public byte getData() {
    return getBlock().getData();
  }

  @Override
  public @NotNull BlockData getBlockData() {
    return getBlock().getBlockData();
  }

  @Override
  public @NotNull Block getRelative(int i, int i1, int i2) {
    return getBlock().getRelative(i, i1, i2);
  }

  @Override
  public @NotNull Block getRelative(@NotNull BlockFace blockFace) {
    return getBlock().getRelative(blockFace);
  }

  @Override
  public @NotNull Block getRelative(@NotNull BlockFace blockFace, int i) {
    return getBlock().getRelative(blockFace, i);
  }

  @Override
  public @NotNull Material getType() {
    return getBlock().getType();
  }

  @Override
  public byte getLightLevel() {
    return getBlock().getLightLevel();
  }

  @Override
  public byte getLightFromSky() {
    return getBlock().getLightFromSky();
  }

  @Override
  public byte getLightFromBlocks() {
    return getBlock().getLightFromBlocks();
  }

  @Override
  public @NotNull World getWorld() {
    return getBlock().getWorld();
  }

  @Override
  public int getX() {
    return getBlock().getX();
  }

  @Override
  public int getY() {
    return getBlock().getY();
  }

  @Override
  public int getZ() {
    return getBlock().getZ();
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  @Override
  public long getBlockKey() {
    return getBlock().getBlockKey();
  }

  /**
   * @param x
   * @param y
   * @param z
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public static long getBlockKey(int x, int y, int z) {
    return Block.getBlockKey(x, y, z);
  }

  /**
   * @param packed
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public static int getBlockKeyX(long packed) {
    return Block.getBlockKeyX(packed);
  }

  /**
   * @param packed
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public static int getBlockKeyY(long packed) {
    return Block.getBlockKeyY(packed);
  }

  /**
   * @param packed
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public static int getBlockKeyZ(long packed) {
    return Block.getBlockKeyZ(packed);
  }

  /**
   * @param itemStack
   * @deprecated
   */
  @Deprecated(since = "1.21", forRemoval = true)
  @Override
  public boolean isValidTool(@NotNull ItemStack itemStack) {
    return getBlock().isValidTool(itemStack);
  }

  @Override
  public @NotNull Location getLocation() {
    return getBlock().getLocation();
  }

  @Contract("null -> null; !null -> !null")
  @Override
  public @Nullable Location getLocation(@Nullable Location location) {
    return getBlock().getLocation(location);
  }

  @Override
  public @NotNull Chunk getChunk() {
    return getBlock().getChunk();
  }

  @Override
  public void setBlockData(@NotNull BlockData blockData) {
    getBlock().setBlockData(blockData);
  }

  @Override
  public void setBlockData(@NotNull BlockData blockData, boolean b) {
    getBlock().setBlockData(blockData, b);
  }

  @Override
  public void setType(@NotNull Material material) {
    getBlock().setType(material);
  }

  @Override
  public void setType(@NotNull Material material, boolean b) {
    getBlock().setType(material, b);
  }

  @Override
  public @Nullable BlockFace getFace(@NotNull Block block) {
    return getBlock().getFace(block);
  }

  @Override
  public @NotNull BlockState getState() {
    return getBlock().getState();
  }

  @Override
  public @NotNull BlockState getState(boolean b) {
    return getBlock().getState(b);
  }

  @Override
  public @NotNull Biome getBiome() {
    return getBlock().getBiome();
  }

  @Override
  public @NotNull Biome getComputedBiome() {
    return getBlock().getComputedBiome();
  }

  @Override
  public void setBiome(@NotNull Biome biome) {
    getBlock().setBiome(biome);
  }

  @Override
  public boolean isBlockPowered() {
    return getBlock().isBlockPowered();
  }

  @Override
  public boolean isBlockIndirectlyPowered() {
    return getBlock().isBlockIndirectlyPowered();
  }

  @Override
  public boolean isBlockFacePowered(@NotNull BlockFace blockFace) {
    return getBlock().isBlockFacePowered(blockFace);
  }

  @Override
  public boolean isBlockFaceIndirectlyPowered(@NotNull BlockFace blockFace) {
    return getBlock().isBlockFaceIndirectlyPowered(blockFace);
  }

  @Override
  public int getBlockPower(@NotNull BlockFace blockFace) {
    return getBlock().getBlockPower(blockFace);
  }

  @Override
  public int getBlockPower() {
    return getBlock().getBlockPower();
  }

  @Override
  public boolean isEmpty() {
    return getBlock().isEmpty();
  }

  @Override
  public boolean isLiquid() {
    return getBlock().isLiquid();
  }

  @Override
  public boolean isBuildable() {
    return getBlock().isBuildable();
  }

  @Override
  public boolean isBurnable() {
    return getBlock().isBurnable();
  }

  @Override
  public boolean isReplaceable() {
    return getBlock().isReplaceable();
  }

  @Override
  public boolean isSolid() {
    return getBlock().isSolid();
  }

  @Override
  public boolean isCollidable() {
    return getBlock().isCollidable();
  }

  @Override
  public double getTemperature() {
    return getBlock().getTemperature();
  }

  @Override
  public double getHumidity() {
    return getBlock().getHumidity();
  }

  @Override
  public @NotNull PistonMoveReaction getPistonMoveReaction() {
    return getBlock().getPistonMoveReaction();
  }

  @Override
  public boolean breakNaturally() {
    return getBlock().breakNaturally();
  }

  @Override
  public boolean breakNaturally(@Nullable ItemStack itemStack) {
    return getBlock().breakNaturally(itemStack);
  }

  @Override
  public boolean breakNaturally(boolean triggerEffect) {
    return getBlock().breakNaturally(triggerEffect);
  }

  @Override
  public boolean breakNaturally(boolean b, boolean b1) {
    return getBlock().breakNaturally(b, b1);
  }

  @Override
  public boolean breakNaturally(@NotNull ItemStack tool, boolean triggerEffect) {
    return getBlock().breakNaturally(tool, triggerEffect);
  }

  @Override
  public boolean breakNaturally(@NotNull ItemStack itemStack, boolean b, boolean b1) {
    return getBlock().breakNaturally(itemStack, b, b1);
  }

  @Override
  public boolean breakNaturally(@NotNull ItemStack itemStack, boolean b, boolean b1, boolean b2) {
    return getBlock().breakNaturally(itemStack, b, b1, b2);
  }

  @Override
  public void tick() {
    getBlock().tick();
  }

  @Override
  public void fluidTick() {
    getBlock().fluidTick();
  }

  @Override
  public void randomTick() {
    getBlock().randomTick();
  }

  @Override
  public boolean applyBoneMeal(@NotNull BlockFace blockFace) {
    return getBlock().applyBoneMeal(blockFace);
  }

  @Override
  public @NotNull Collection<ItemStack> getDrops() {
    return getBlock().getDrops();
  }

  @Override
  public @NotNull Collection<ItemStack> getDrops(@Nullable ItemStack itemStack) {
    return getBlock().getDrops(itemStack);
  }

  @Override
  public @NotNull Collection<ItemStack> getDrops(@Nullable ItemStack itemStack, @Nullable Entity entity) {
    return getBlock().getDrops(itemStack, entity);
  }

  @Override
  public boolean isPreferredTool(@NotNull ItemStack itemStack) {
    return getBlock().isPreferredTool(itemStack);
  }

  @Override
  public float getBreakSpeed(@NotNull Player player) {
    return getBlock().getBreakSpeed(player);
  }

  @Override
  public boolean isPassable() {
    return getBlock().isPassable();
  }

  @Override
  public @Nullable RayTraceResult rayTrace(@NotNull Location location, @NotNull Vector vector, double v,
                                           @NotNull FluidCollisionMode fluidCollisionMode) {
    return getBlock().rayTrace(location, vector, v, fluidCollisionMode);
  }

  @Override
  public @NotNull BoundingBox getBoundingBox() {
    return getBlock().getBoundingBox();
  }

  @Override
  public @NotNull VoxelShape getCollisionShape() {
    return getBlock().getCollisionShape();
  }

  @Override
  public boolean canPlace(@NotNull BlockData blockData) {
    return getBlock().canPlace(blockData);
  }

  /**
   * @deprecated
   */
  @Deprecated(forRemoval = true, since = "1.18.2")
  @Override
  public @NotNull BlockSoundGroup getSoundGroup() {
    return getBlock().getSoundGroup();
  }

  @Override
  public @NotNull SoundGroup getBlockSoundGroup() {
    return getBlock().getBlockSoundGroup();
  }

  /**
   * @deprecated
   */
  @Deprecated(forRemoval = true)
  @Override
  public @NotNull String getTranslationKey() {
    return getBlock().getTranslationKey();
  }

  @Override
  public float getDestroySpeed(@NotNull ItemStack itemStack) {
    return getBlock().getDestroySpeed(itemStack);
  }

  @Override
  public float getDestroySpeed(@NotNull ItemStack itemStack, boolean considerEnchants) {
    return getBlock().getDestroySpeed(itemStack, considerEnchants);
  }

  @Override
  public boolean isSuffocating() {
    return getBlock().isSuffocating();
  }

  @Override
  public void setMetadata(@NotNull String s, @NotNull MetadataValue metadataValue) {
    getBlock().setMetadata(s, metadataValue);
  }

  @Override
  public @NotNull List<MetadataValue> getMetadata(@NotNull String s) {
    return getBlock().getMetadata(s);
  }

  @Override
  public boolean hasMetadata(@NotNull String s) {
    return getBlock().hasMetadata(s);
  }

  @Override
  public void removeMetadata(@NotNull String s, @NotNull Plugin plugin) {
    getBlock().removeMetadata(s, plugin);
  }

  /**
   * Gets the translation key.
   *
   * @return the translation key
   * @since 4.8.0
   */
  @Override
  public @NotNull String translationKey() {
    return getBlock().translationKey();
  }
}
