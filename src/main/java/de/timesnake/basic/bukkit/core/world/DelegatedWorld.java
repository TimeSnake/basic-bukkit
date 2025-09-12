/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.world;

import io.papermc.paper.block.fluid.FluidData;
import io.papermc.paper.math.Position;
import io.papermc.paper.raytracing.PositionedRayTraceConfigurationBuilder;
import io.papermc.paper.world.MoonPhase;
import io.papermc.paper.world.flag.FeatureDependant;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointer;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.resource.ResourcePackInfoLike;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.resource.ResourcePackRequestLike;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.DragonBattle;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.*;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class DelegatedWorld {

  protected World world;

  public DelegatedWorld(World world) {
    this.world = world;
  }

  public boolean isVoidDamageEnabled() {
    return world.isVoidDamageEnabled();
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public long getTicksPerAnimalSpawns() {
    return world.getTicksPerAnimalSpawns();
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsync(int x, int z, boolean gen) {
    return world.getChunkAtAsync(x, z, gen);
  }

  public double getHumidity(int i, int i1, int i2) {
    return world.getHumidity(i, i1, i2);
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public int getWaterAnimalSpawnLimit() {
    return world.getWaterAnimalSpawnLimit();
  }

  public void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory soundCategory, float v,
                        float v1, long l) {
    world.playSound(entity, sound, soundCategory, v, v1, l);
  }

  @NotNull
  public List<MetadataValue> getMetadata(@NotNull String s) {
    return world.getMetadata(s);
  }

  public boolean getAllowAnimals() {
    return world.getAllowAnimals();
  }

  @NotNull
  public Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {
    return world.getPluginChunkTickets();
  }

  /**
   * @param x
   * @param z
   * @param cb
   * @deprecated
   */
  @Deprecated(since = "1.13.1")
  public void getChunkAtAsync(int x, int z, World.@NotNull ChunkLoadCallback cb) {
    world.getChunkAtAsync(x, z, cb);
  }

  @NotNull
  public Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox) {
    return world.getNearbyEntities(boundingBox);
  }

  public void setVoidDamageAmount(float v) {
    world.setVoidDamageAmount(v);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                                double v2, @Nullable T t) {
    world.spawnParticle(particle, location, i, v, v1, v2, t);
  }

  public boolean hasBonusChest() {
    return world.hasBonusChest();
  }

  /**
   * @param i
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public void setTicksPerWaterAmbientSpawns(int i) {
    world.setTicksPerWaterAmbientSpawns(i);
  }

  public boolean createExplosion(@NotNull Entity source, float power, boolean setFire) {
    return world.createExplosion(source, power, setFire);
  }

  /**
   * @param location
   * @param blockData
   * @deprecated
   */
  @ApiStatus.Obsolete(since = "1.20.2")
  @NotNull
  public FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull BlockData blockData) throws IllegalArgumentException {
    return world.spawnFallingBlock(location, blockData);
  }

  @Nullable
  public ChunkGenerator getGenerator() {
    return world.getGenerator();
  }

  /**
   * @param i
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public void setAmbientSpawnLimit(int i) {
    world.setAmbientSpawnLimit(i);
  }

  public void getChunkAtAsync(int x, int z, boolean gen, @NotNull Consumer<? super Chunk> cb) {
    world.getChunkAtAsync(x, z, gen, cb);
  }

  public void clearResourcePacks() {
    world.clearResourcePacks();
  }

  @NotNull
  public Iterable<? extends Audience> audiences() {
    return world.audiences();
  }

  public void setSpawnLimit(@NotNull SpawnCategory spawnCategory, int i) {
    world.setSpawnLimit(spawnCategory, i);
  }

  public void showTitle(@NotNull Title title) {
    world.showTitle(title);
  }

  public void playSound(@NotNull Entity entity, @NotNull Sound sound, float v, float v1) {
    world.playSound(entity, sound, v, v1);
  }

  public void openBook(Book.@NotNull Builder book) {
    world.openBook(book);
  }

  @NotNull
  public Collection<Chunk> getForceLoadedChunks() {
    return world.getForceLoadedChunks();
  }

  @NotNull
  public Collection<GeneratedStructure> getStructures(int i, int i1, @NotNull Structure structure) {
    return world.getStructures(i, i1, structure);
  }

  public void save(boolean b) {
    world.save(b);
  }

  /**
   * @param i
   * @param i1
   * @deprecated
   */
  @Deprecated(since = "1.15")
  public double getTemperature(int i, int i1) {
    return world.getTemperature(i, i1);
  }

  @NotNull
  public Collection<Material> getInfiniburn() {
    return world.getInfiniburn();
  }

  public boolean refreshChunk(int i, int i1) {
    return world.refreshChunk(i, i1);
  }

  public <T> void sendTitlePart(@NotNull TitlePart<T> part, @NotNull T value) {
    world.sendTitlePart(part, value);
  }

  /**
   * @param key
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  @NotNull
  public Block getBlockAtKey(long key) {
    return world.getBlockAtKey(key);
  }

  @NotNull
  public WorldBorder getWorldBorder() {
    return world.getWorldBorder();
  }

  public void playSound(@NotNull Entity entity, @NotNull String s, @NotNull SoundCategory soundCategory, float v,
                        float v1) {
    world.playSound(entity, s, soundCategory, v, v1);
  }

  @NotNull
  public <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T> aClass) {
    return world.getEntitiesByClass(aClass);
  }

  public void stopSound(net.kyori.adventure.sound.@NotNull Sound sound) {
    world.stopSound(sound);
  }

  @NotNull
  public NamespacedKey getKey() {
    return world.getKey();
  }

  public void getChunkAtAsync(int i, int i1, boolean b, boolean b1, @NotNull Consumer<? super Chunk> consumer) {
    world.getChunkAtAsync(i, i1, b, b1, consumer);
  }

  @NotNull
  public BlockState getBlockState(@NotNull Location location) {
    return world.getBlockState(location);
  }

  @NotNull
  public Collection<Player> getNearbyPlayers(@NotNull Location loc, double radius,
                                             @Nullable Predicate<? super Player> predicate) {
    return world.getNearbyPlayers(loc, radius, predicate);
  }

  public void sendMessage(@NotNull SignedMessage signedMessage, ChatType.@NotNull Bound boundChatType) {
    world.sendMessage(signedMessage, boundChatType);
  }

  public void getChunksAtAsync(int i, int i1, int i2, int i3, boolean b, @NotNull Runnable runnable) {
    world.getChunksAtAsync(i, i1, i2, i3, b, runnable);
  }

  public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz, @Nullable Consumer<
      ? super T> function) throws IllegalArgumentException {
    return world.spawn(location, clazz, function);
  }

  public boolean hasCollisionsIn(@NotNull BoundingBox boundingBox) {
    return world.hasCollisionsIn(boundingBox);
  }

  @NotNull
  public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz,
                                                                  @NotNull Location loc, double xRadius,
                                                                  double yRadius, double zRadius) {
    return world.getNearbyEntitiesByType(clazz, loc, xRadius, yRadius, zRadius);
  }

  @NotNull
  public Collection<Entity> getNearbyEntities(@NotNull Location location, double v, double v1, double v2,
                                              @Nullable Predicate<? super Entity> predicate) {
    return world.getNearbyEntities(location, v, v1, v2, predicate);
  }

  public boolean createExplosion(@NotNull Location location, float v) {
    return world.createExplosion(location, v);
  }

  public <T extends LivingEntity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> aClass,
                                                   CreatureSpawnEvent.@NotNull SpawnReason spawnReason, boolean b,
                                                   @Nullable Consumer<? super T> consumer) throws IllegalArgumentException {
    return world.spawn(location, aClass, spawnReason, b, consumer);
  }

  public void sendResourcePacks(@NotNull ResourcePackInfoLike first, @NotNull ResourcePackInfoLike... others) {
    world.sendResourcePacks(first, others);
  }

  /**
   * @param source
   * @param message
   * @deprecated
   */
  @Deprecated
  public void sendMessage(@NotNull Identified source, @NotNull Component message) {
    world.sendMessage(source, message);
  }

  /**
   * @param i
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public void setTicksPerWaterSpawns(int i) {
    world.setTicksPerWaterSpawns(i);
  }

  /**
   * @param i
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public void setWaterAmbientSpawnLimit(int i) {
    world.setWaterAmbientSpawnLimit(i);
  }

  public <T> boolean setGameRule(@NotNull GameRule<T> gameRule, @NotNull T t) {
    return world.setGameRule(gameRule, t);
  }

  public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                            double v2, double v3) {
    world.spawnParticle(particle, location, i, v, v1, v2, v3);
  }

  @Nullable
  public DragonBattle getEnderDragonBattle() {
    return world.getEnderDragonBattle();
  }

  public boolean hasStorm() {
    return world.hasStorm();
  }

  public boolean createExplosion(double v, double v1, double v2, float v3) {
    return world.createExplosion(v, v1, v2, v3);
  }

  public boolean hasCeiling() {
    return world.hasCeiling();
  }

  @NotNull
  public FluidData getFluidData(@NotNull Position position) {
    return world.getFluidData(position);
  }

  @NotNull
  public Chunk getChunkAt(int i, int i1) {
    return world.getChunkAt(i, i1);
  }

  public boolean isChunkLoaded(@NotNull Chunk chunk) {
    return world.isChunkLoaded(chunk);
  }

  public void setAutoSave(boolean b) {
    world.setAutoSave(b);
  }

  @Nullable
  public RayTraceResult rayTrace(@NotNull Consumer<PositionedRayTraceConfigurationBuilder> consumer) {
    return world.rayTrace(consumer);
  }

  public void removePluginChunkTickets(@NotNull Plugin plugin) {
    world.removePluginChunkTickets(plugin);
  }

  public int getHighestBlockYAt(@NotNull Location location) {
    return world.getHighestBlockYAt(location);
  }

  public void playSound(net.kyori.adventure.sound.@NotNull Sound sound, double x, double y, double z) {
    world.playSound(sound, x, y, z);
  }

  @NotNull
  public Collection<Player> getNearbyPlayers(@NotNull Location loc, double xRadius, double yRadius, double zRadius) {
    return world.getNearbyPlayers(loc, xRadius, yRadius, zRadius);
  }

  public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i) {
    world.spawnParticle(particle, v, v1, v2, i);
  }

  /**
   * @param viewDistance
   * @deprecated
   */
  @Deprecated
  public void setNoTickViewDistance(int viewDistance) {
    world.setNoTickViewDistance(viewDistance);
  }

  public void hideBossBar(@NotNull BossBar bar) {
    world.hideBossBar(bar);
  }

  public void removeResourcePacks(@NotNull ResourcePackInfoLike request,
                                  @NotNull ResourcePackInfoLike @NotNull ... others) {
    world.removeResourcePacks(request, others);
  }

  public boolean unloadChunkRequest(int i, int i1) {
    return world.unloadChunkRequest(i, i1);
  }

  public void setType(int i, int i1, int i2, @NotNull Material material) {
    world.setType(i, i1, i2, material);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, @Nullable T t) {
    world.spawnParticle(particle, location, i, t);
  }

  @NotNull
  public Item dropItem(@NotNull Location location, @NotNull ItemStack itemStack) {
    return world.dropItem(location, itemStack);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsync(@NotNull Location loc) {
    return world.getChunkAtAsync(loc);
  }

  public void setWeatherDuration(int i) {
    world.setWeatherDuration(i);
  }

  public <T extends Entity> @NotNull T addEntity(@NotNull T t) {
    return world.addEntity(t);
  }

  public void openBook(@NotNull Book book) {
    world.openBook(book);
  }

  public void loadChunk(int i, int i1) {
    world.loadChunk(i, i1);
  }

  @NotNull
  public Material getType(int i, int i1, int i2) {
    return world.getType(i, i1, i2);
  }

  public void getChunkAtAsync(@NotNull Location loc, @NotNull Consumer<? super Chunk> cb) {
    world.getChunkAtAsync(loc, cb);
  }

  @NotNull
  public Biome getBiome(int i, int i1, int i2) {
    return world.getBiome(i, i1, i2);
  }

  public void sendMessage(@NotNull Component message) {
    world.sendMessage(message);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Block block) {
    return world.getChunkAtAsyncUrgently(block);
  }

  public boolean isNatural() {
    return world.isNatural();
  }

  @NotNull
  public <T> Optional<T> get(@NotNull Pointer<T> pointer) {
    return world.get(pointer);
  }

  public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i) {
    world.spawnParticle(particle, location, i);
  }

  /**
   * @param location
   * @param treeType
   * @param blockChangeDelegate
   * @deprecated
   */
  @Deprecated(since = "1.17.1")
  public boolean generateTree(@NotNull Location location, @NotNull TreeType treeType,
                              @NotNull BlockChangeDelegate blockChangeDelegate) {
    return world.generateTree(location, treeType, blockChangeDelegate);
  }

  /**
   * @param origin
   * @param biome
   * @param radius
   * @param step
   * @deprecated
   */
  @Deprecated
  @Nullable
  public Location locateNearestBiome(@NotNull Location origin, @NotNull Biome biome, int radius, int step) {
    return world.locateNearestBiome(origin, biome, radius, step);
  }

  @NotNull
  public MoonPhase getMoonPhase() {
    return world.getMoonPhase();
  }

  public boolean isChunkLoaded(int i, int i1) {
    return world.isChunkLoaded(i, i1);
  }

  @NotNull
  public Arrow spawnArrow(@NotNull Location location, @NotNull Vector vector, float v, float v1) {
    return world.spawnArrow(location, vector, v, v1);
  }

  public boolean createExplosion(@Nullable Entity entity, @NotNull Location location, float v, boolean b, boolean b1,
                                 boolean b2) {
    return world.createExplosion(entity, location, v, b, b1, b2);
  }

  @NotNull
  public Chunk getChunkAt(@NotNull Location location) {
    return world.getChunkAt(location);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @Nullable List<Player> receivers, @NotNull Player source,
                                double x, double y, double z, int count, double offsetX, double offsetY,
                                double offsetZ, double extra, @Nullable T data) {
    world.spawnParticle(particle, receivers, source, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
  }

  public boolean unloadChunk(int i, int i1, boolean b) {
    return world.unloadChunk(i, i1, b);
  }

  public void sendActionBar(@NotNull Component message) {
    world.sendActionBar(message);
  }

  public void removeResourcePacks(@NotNull Iterable<UUID> ids) {
    world.removeResourcePacks(ids);
  }

  public boolean isGameRule(@NotNull String s) {
    return world.isGameRule(s);
  }

  public <T extends AbstractArrow> @NotNull T spawnArrow(@NotNull Location location, @NotNull Vector vector, float v,
                                                         float v1, @NotNull Class<T> aClass) {
    return world.spawnArrow(location, vector, v, v1, aClass);
  }

  /**
   * @param i
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public void setTicksPerMonsterSpawns(int i) {
    world.setTicksPerMonsterSpawns(i);
  }

  public void setSimulationDistance(int i) {
    world.setSimulationDistance(i);
  }

  @NotNull
  public UUID getUID() {
    return world.getUID();
  }

  public boolean createExplosion(@NotNull Location location, float v, boolean b, boolean b1, @Nullable Entity entity) {
    return world.createExplosion(location, v, b, b1, entity);
  }

  /**
   * @param i
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public void setWaterUndergroundCreatureSpawnLimit(int i) {
    world.setWaterUndergroundCreatureSpawnLimit(i);
  }

  public void sendPlayerListFooter(@NotNull Component footer) {
    world.sendPlayerListFooter(footer);
  }

  @NotNull
  public Audience filterAudience(@NotNull Predicate<? super Audience> filter) {
    return world.filterAudience(filter);
  }

  public void clearTitle() {
    world.clearTitle();
  }

  @Nullable
  public Raid getRaid(int i) {
    return world.getRaid(i);
  }

  @Nullable
  public Location findLightningRod(@NotNull Location location) {
    return world.findLightningRod(location);
  }

  public boolean isDayTime() {
    return world.isDayTime();
  }

  public void playSound(@NotNull Entity entity, @NotNull String s, @NotNull SoundCategory soundCategory, float v,
                        float v1, long l) {
    world.playSound(entity, s, soundCategory, v, v1, l);
  }

  public void getChunkAtAsync(int x, int z, @NotNull Consumer<? super Chunk> cb) {
    world.getChunkAtAsync(x, z, cb);
  }

  /**
   * @param i
   * @param i1
   * @param biome
   * @deprecated
   */
  @Deprecated(since = "1.15")
  public void setBiome(int i, int i1, @NotNull Biome biome) {
    world.setBiome(i, i1, biome);
  }

  public boolean isBedWorks() {
    return world.isBedWorks();
  }

  public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType treeType,
                              @Nullable Consumer<? super BlockState> consumer) {
    return world.generateTree(location, random, treeType, consumer);
  }

  public void removeResourcePacks(@NotNull ResourcePackRequestLike request) {
    world.removeResourcePacks(request);
  }

  public boolean getAllowMonsters() {
    return world.getAllowMonsters();
  }

  @NotNull
  public Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack itemStack, @Nullable Consumer<?
      super Item> consumer) {
    return world.dropItemNaturally(location, itemStack, consumer);
  }

  @NotNull
  public FluidData getFluidData(@NotNull Location location) {
    return world.getFluidData(location);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Location loc, boolean gen) {
    return world.getChunkAtAsyncUrgently(loc, gen);
  }

  public int getMinHeight() {
    return world.getMinHeight();
  }

  @Nullable
  public RayTraceResult rayTraceEntities(@NotNull Location location, @NotNull Vector vector, double v) {
    return world.rayTraceEntities(location, vector, v);
  }

  @NotNull
  public Key key() {
    return world.key();
  }

  public boolean getPVP() {
    return world.getPVP();
  }

  public boolean setSpawnLocation(int i, int i1, int i2, float v) {
    return world.setSpawnLocation(i, i1, i2, v);
  }

  @NotNull
  public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz,
                                                                  @NotNull Location loc, double xzRadius,
                                                                  double yRadius,
                                                                  @Nullable Predicate<? super T> predicate) {
    return world.getNearbyEntitiesByType(clazz, loc, xzRadius, yRadius, predicate);
  }

  @Nullable
  public RayTraceResult rayTrace(@NotNull Position position, @NotNull Vector vector, double v,
                                 @NotNull FluidCollisionMode fluidCollisionMode, boolean b, double v1,
                                 @Nullable Predicate<? super Entity> predicate,
                                 @Nullable Predicate<? super Block> predicate1) {
    return world.rayTrace(position, vector, v, fluidCollisionMode, b, v1, predicate, predicate1);
  }

  public long getTicksPerSpawns(@NotNull SpawnCategory spawnCategory) {
    return world.getTicksPerSpawns(spawnCategory);
  }

  public void setVoidDamageEnabled(boolean b) {
    world.setVoidDamageEnabled(b);
  }

  @NotNull
  public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double xRadius, double yRadius,
                                                          double zRadius,
                                                          @Nullable Predicate<? super LivingEntity> predicate) {
    return world.getNearbyLivingEntities(loc, xRadius, yRadius, zRadius, predicate);
  }

  @Nullable
  public RayTraceResult rayTraceBlocks(@NotNull Location location, @NotNull Vector vector, double v,
                                       @NotNull FluidCollisionMode fluidCollisionMode) {
    return world.rayTraceBlocks(location, vector, v, fluidCollisionMode);
  }

  public void playSound(net.kyori.adventure.sound.@NotNull Sound sound,
                        net.kyori.adventure.sound.Sound.@NotNull Emitter emitter) {
    world.playSound(sound, emitter);
  }

  public void setDifficulty(@NotNull Difficulty difficulty) {
    world.setDifficulty(difficulty);
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public int getAnimalSpawnLimit() {
    return world.getAnimalSpawnLimit();
  }

  public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, @Nullable T t) {
    world.spawnParticle(particle, v, v1, v2, i, t);
  }

  @Nullable
  public Entity getEntity(@NotNull UUID uuid) {
    return world.getEntity(uuid);
  }

  public int getChunkCount() {
    return world.getChunkCount();
  }

  public void setTime(long l) {
    world.setTime(l);
  }

  public int getThunderDuration() {
    return world.getThunderDuration();
  }

  /**
   * @param b
   * @deprecated
   */
  @Deprecated(since = "1.20.5")
  public void setKeepSpawnInMemory(boolean b) {
    world.setKeepSpawnInMemory(b);
  }

  public void getChunkAtAsync(@NotNull Block block, boolean gen, @NotNull Consumer<? super Chunk> cb) {
    world.getChunkAtAsync(block, gen, cb);
  }

  @NotNull
  public Collection<Player> getNearbyPlayers(@NotNull Location loc, double xzRadius, double yRadius) {
    return world.getNearbyPlayers(loc, xzRadius, yRadius);
  }

  /**
   * @param i
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public void setTicksPerAnimalSpawns(int i) {
    world.setTicksPerAnimalSpawns(i);
  }

  /**
   * @param i
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public void setWaterAnimalSpawnLimit(int i) {
    world.setWaterAnimalSpawnLimit(i);
  }

  @NotNull
  public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz,
                                                                  @NotNull Location loc, double radius,
                                                                  @Nullable Predicate<? super T> predicate) {
    return world.getNearbyEntitiesByType(clazz, loc, radius, predicate);
  }

  public void playSound(@NotNull Location location, @NotNull String s, @NotNull SoundCategory soundCategory, float v,
                        float v1) {
    world.playSound(location, s, soundCategory, v, v1);
  }

  @NotNull
  public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double radius) {
    return world.getNearbyLivingEntities(loc, radius);
  }

  @Deprecated(since = "1.19")
  @Nullable
  public StructureSearchResult locateNearestStructure(@NotNull Location location,
                                                      @NotNull StructureType structureType, int i, boolean b) {
    return world.locateNearestStructure(location, structureType, i, b);
  }

  public int getEntityCount() {
    return world.getEntityCount();
  }

  @NotNull
  public LightningStrike strikeLightning(@NotNull Location location) {
    return world.strikeLightning(location);
  }

  @NotNull
  public Block getHighestBlockAt(int i, int i1) {
    return world.getHighestBlockAt(i, i1);
  }

  public boolean createExplosion(@NotNull Entity source, float power, boolean setFire, boolean breakBlocks) {
    return world.createExplosion(source, power, setFire, breakBlocks);
  }

  public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType treeType,
                              @Nullable Predicate<? super BlockState> predicate) {
    return world.generateTree(location, random, treeType, predicate);
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public long getTicksPerAmbientSpawns() {
    return world.getTicksPerAmbientSpawns();
  }

  public boolean hasMetadata(@NotNull String s) {
    return world.hasMetadata(s);
  }

  public double getCoordinateScale() {
    return world.getCoordinateScale();
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.16.1")
  @Nullable
  public WorldType getWorldType() {
    return world.getWorldType();
  }

  @NotNull
  public BlockData getBlockData(int i, int i1, int i2) {
    return world.getBlockData(i, i1, i2);
  }

  @Nullable
  public RayTraceResult rayTraceEntities(@NotNull Location location, @NotNull Vector vector, double v, double v1,
                                         @Nullable Predicate<? super Entity> predicate) {
    return world.rayTraceEntities(location, vector, v, v1, predicate);
  }

  public void sendPlayerListHeaderAndFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
    world.sendPlayerListHeaderAndFooter(header, footer);
  }

  public void sendGameEvent(@Nullable Entity entity, @NotNull GameEvent gameEvent, @NotNull Vector vector) {
    world.sendGameEvent(entity, gameEvent, vector);
  }

  public boolean isPiglinSafe() {
    return world.isPiglinSafe();
  }

  public int getViewDistance() {
    return world.getViewDistance();
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsyncUrgently(int x, int z) {
    return world.getChunkAtAsyncUrgently(x, z);
  }

  public void setStorm(boolean b) {
    world.setStorm(b);
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public int getMonsterSpawnLimit() {
    return world.getMonsterSpawnLimit();
  }

  public boolean unloadChunk(@NotNull Chunk chunk) {
    return world.unloadChunk(chunk);
  }

  @NotNull
  public Collection<Player> getPlayersSeeingChunk(int i, int i1) {
    return world.getPlayersSeeingChunk(i, i1);
  }

  @NotNull
  public Biome getComputedBiome(int i, int i1, int i2) {
    return world.getComputedBiome(i, i1, i2);
  }

  public void setBlockData(@NotNull Location location, @NotNull BlockData blockData) {
    world.setBlockData(location, blockData);
  }

  public boolean isHardcore() {
    return world.isHardcore();
  }

  public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory soundCategory,
                        float v, float v1) {
    world.playSound(location, sound, soundCategory, v, v1);
  }

  @NotNull
  public Collection<GeneratedStructure> getStructures(int i, int i1) {
    return world.getStructures(i, i1);
  }

  /**
   * @param i
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public void setAnimalSpawnLimit(int i) {
    world.setAnimalSpawnLimit(i);
  }

  public int getTileEntityCount() {
    return world.getTileEntityCount();
  }

  /**
   * @param s
   * @param s1
   * @deprecated
   */
  @Deprecated(since = "1.13")
  public boolean setGameRuleValue(@NotNull String s, @NotNull String s1) {
    return world.setGameRuleValue(s, s1);
  }

  public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T t) {
    world.playEffect(location, effect, t);
  }

  public void setBlockData(int i, int i1, int i2, @NotNull BlockData blockData) {
    world.setBlockData(i, i1, i2, blockData);
  }

  @NotNull
  public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double radius, @Nullable Predicate<?
      super LivingEntity> predicate) {
    return world.getNearbyLivingEntities(loc, radius, predicate);
  }

  public long getSeed() {
    return world.getSeed();
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsync(@NotNull Location loc, boolean gen) {
    return world.getChunkAtAsync(loc, gen);
  }


  public void deleteMessage(SignedMessage.@NotNull Signature signature) {
    world.deleteMessage(signature);
  }

  public boolean createExplosion(@NotNull Location location, float v, boolean b) {
    return world.createExplosion(location, v, b);
  }

  public int getSimulationDistance() {
    return world.getSimulationDistance();
  }

  @NotNull
  public List<BlockPopulator> getPopulators() {
    return world.getPopulators();
  }

  public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX,
                                double offsetY, double offsetZ, double extra, @Nullable T data) {
    world.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
  }

  public boolean isRespawnAnchorWorks() {
    return world.isRespawnAnchorWorks();
  }

  public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory soundCategory,
                        float v, float v1, long l) {
    world.playSound(location, sound, soundCategory, v, v1, l);
  }

  @NotNull
  public Material getType(@NotNull Location location) {
    return world.getType(location);
  }

  @NotNull
  public Block getBlockAt(@NotNull Location location) {
    return world.getBlockAt(location);
  }

  @NotNull
  public PersistentDataContainer getPersistentDataContainer() {
    return world.getPersistentDataContainer();
  }

  public void sendMessage(@NotNull ComponentLike message) {
    world.sendMessage(message);
  }

  @NotNull
  public Set<FeatureFlag> getFeatureFlags() {
    return world.getFeatureFlags();
  }

  public long getFullTime() {
    return world.getFullTime();
  }

  @NotNull
  public String @NotNull [] getGameRules() {
    return world.getGameRules();
  }

  public boolean loadChunk(int i, int i1, boolean b) {
    return world.loadChunk(i, i1, b);
  }

  /**
   * @param location
   * @param treeType
   * @deprecated
   */
  @Deprecated(since = "1.21.6")
  public boolean generateTree(@NotNull Location location, @NotNull TreeType treeType) {
    return world.generateTree(location, treeType);
  }

  public void sendPlayerListFooter(@NotNull ComponentLike footer) {
    world.sendPlayerListFooter(footer);
  }

  @Nullable
  public RayTraceResult rayTraceBlocks(@NotNull Position position, @NotNull Vector vector, double v,
                                       @NotNull FluidCollisionMode fluidCollisionMode, boolean b,
                                       @Nullable Predicate<? super Block> predicate) {
    return world.rayTraceBlocks(position, vector, v, fluidCollisionMode, b, predicate);
  }

  @NotNull
  public Set<String> getListeningPluginChannels() {
    return world.getListeningPluginChannels();
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Block block, boolean gen) {
    return world.getChunkAtAsyncUrgently(block, gen);
  }

  @NotNull
  public Chunk getChunkAt(int i, int i1, boolean b) {
    return world.getChunkAt(i, i1, b);
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public long getTicksPerWaterUndergroundCreatureSpawns() {
    return world.getTicksPerWaterUndergroundCreatureSpawns();
  }

  @NotNull
  public Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox,
                                              @Nullable Predicate<? super Entity> predicate) {
    return world.getNearbyEntities(boundingBox, predicate);
  }

  @Nullable
  public BiomeSearchResult locateNearestBiome(@NotNull Location location, int i, int i1, int i2,
                                              @NotNull Biome... biomes) {
    return world.locateNearestBiome(location, i, i1, i2, biomes);
  }

  public int getSpawnLimit(@NotNull SpawnCategory spawnCategory) {
    return world.getSpawnLimit(spawnCategory);
  }

  public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> aClass, boolean b,
                                             @Nullable Consumer<? super T> consumer) throws IllegalArgumentException {
    return world.spawn(location, aClass, b, consumer);
  }

  public void sendPlayerListHeader(@NotNull ComponentLike header) {
    world.sendPlayerListHeader(header);
  }

  public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                            double v2) {
    world.spawnParticle(particle, location, i, v, v1, v2);
  }

  public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3,
                                double v4, double v5, @Nullable T t) {
    world.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, t);
  }

  /**
   * @param block
   * @param cb
   * @deprecated
   */
  @Deprecated(since = "1.13.1")
  public void getChunkAtAsync(@NotNull Block block, World.@NotNull ChunkLoadCallback cb) {
    world.getChunkAtAsync(block, cb);
  }

  public void playEffect(@NotNull Location location, @NotNull Effect effect, int i, int i1) {
    world.playEffect(location, effect, i, i1);
  }

  @NotNull
  public Collection<Entity> getEntitiesByClasses(@NotNull Class<?>... classes) {
    return world.getEntitiesByClasses(classes);
  }

  public <T extends Entity> @NotNull T createEntity(@NotNull Location location, @NotNull Class<T> aClass) {
    return world.createEntity(location, aClass);
  }

  @NotNull
  public ChunkSnapshot getEmptyChunkSnapshot(int i, int i1, boolean b, boolean b1) {
    return world.getEmptyChunkSnapshot(i, i1, b, b1);
  }

  public int getClearWeatherDuration() {
    return world.getClearWeatherDuration();
  }

  public long getGameTime() {
    return world.getGameTime();
  }

  public void playSound(@NotNull Location location, @NotNull Sound sound, float v, float v1) {
    world.playSound(location, sound, v, v1);
  }

  public <T> @Nullable T getGameRuleDefault(@NotNull GameRule<T> gameRule) {
    return world.getGameRuleDefault(gameRule);
  }

  public void deleteMessage(@NotNull SignedMessage signedMessage) {
    world.deleteMessage(signedMessage);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @Nullable List<Player> list, @Nullable Player player,
                                double v, double v1, double v2, int i, double v3, double v4, double v5, double v6,
                                @Nullable T t, boolean b) {
    world.spawnParticle(particle, list, player, v, v1, v2, i, v3, v4, v5, v6, t, b);
  }

  @NotNull
  public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz,
                                                                  @NotNull Location loc, double xRadius,
                                                                  double yRadius, double zRadius,
                                                                  @Nullable Predicate<? super T> predicate) {
    return world.getNearbyEntitiesByType(clazz, loc, xRadius, yRadius, zRadius, predicate);
  }

  public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3,
                                double v4, double v5, double v6, @Nullable T t, boolean b) {
    world.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6, t, b);
  }

  public boolean hasSkyLight() {
    return world.hasSkyLight();
  }

  @NotNull
  public Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack itemStack) {
    return world.dropItemNaturally(location, itemStack);
  }

  public boolean lineOfSightExists(@NotNull Location location, @NotNull Location location1) {
    return world.lineOfSightExists(location, location1);
  }

  public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4,
                            double v5, double v6) {
    world.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6);
  }

  @NotNull
  public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double xRadius, double yRadius,
                                                          double zRadius) {
    return world.getNearbyLivingEntities(loc, xRadius, yRadius, zRadius);
  }

  public boolean isThundering() {
    return world.isThundering();
  }

  @NotNull
  public Collection<Plugin> getPluginChunkTickets(int i, int i1) {
    return world.getPluginChunkTickets(i, i1);
  }

  public void setChunkForceLoaded(int i, int i1, boolean b) {
    world.setChunkForceLoaded(i, i1, b);
  }

  @Nullable
  public RayTraceResult rayTraceEntities(@NotNull Position position, @NotNull Vector vector, double v, double v1,
                                         @Nullable Predicate<? super Entity> predicate) {
    return world.rayTraceEntities(position, vector, v, v1, predicate);
  }

  public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4,
                            double v5) {
    world.spawnParticle(particle, v, v1, v2, i, v3, v4, v5);
  }

  public boolean isFixedTime() {
    return world.isFixedTime();
  }

  /**
   * @param i
   * @param i1
   * @deprecated
   */
  @Deprecated(since = "1.15")
  public double getHumidity(int i, int i1) {
    return world.getHumidity(i, i1);
  }

  public void sendActionBar(@NotNull ComponentLike message) {
    world.sendActionBar(message);
  }

  @NotNull
  public Collection<Chunk> getIntersectingChunks(@NotNull BoundingBox boundingBox) {
    return world.getIntersectingChunks(boundingBox);
  }

  public boolean isPositionLoaded(@NotNull Position position) {
    return world.isPositionLoaded(position);
  }

  @NotNull
  public Block getHighestBlockAt(int i, int i1, @NotNull HeightMap heightMap) {
    return world.getHighestBlockAt(i, i1, heightMap);
  }

  @Nullable
  public RayTraceResult rayTraceBlocks(@NotNull Location location, @NotNull Vector vector, double v,
                                       @NotNull FluidCollisionMode fluidCollisionMode, boolean b) {
    return world.rayTraceBlocks(location, vector, v, fluidCollisionMode, b);
  }

  public boolean createExplosion(@Nullable Entity source, @NotNull Location loc, float power) {
    return world.createExplosion(source, loc, power);
  }

  @NotNull
  public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double xzRadius, double yRadius,
                                                          @Nullable Predicate<? super LivingEntity> predicate) {
    return world.getNearbyLivingEntities(loc, xzRadius, yRadius, predicate);
  }

  @NotNull
  public Chunk getChunkAt(long chunkKey) {
    return world.getChunkAt(chunkKey);
  }

  /**
   * @param i
   * @param i1
   * @deprecated
   */
  @Deprecated(since = "1.14")
  public boolean isChunkInUse(int i, int i1) {
    return world.isChunkInUse(i, i1);
  }

  @NotNull
  public String getName() {
    return world.getName();
  }

  public boolean addPluginChunkTicket(int i, int i1, @NotNull Plugin plugin) {
    return world.addPluginChunkTicket(i, i1, plugin);
  }

  public int getHighestBlockYAt(int i, int i1, @NotNull HeightMap heightMap) {
    return world.getHighestBlockYAt(i, i1, heightMap);
  }

  @NotNull
  public Pointers pointers() {
    return world.pointers();
  }

  @NotNull
  public BlockState getBlockState(int i, int i1, int i2) {
    return world.getBlockState(i, i1, i2);
  }

  public void showBossBar(@NotNull BossBar bar) {
    world.showBossBar(bar);
  }

  public boolean createExplosion(@NotNull Location location, float v, boolean b, boolean b1) {
    return world.createExplosion(location, v, b, b1);
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public int getAmbientSpawnLimit() {
    return world.getAmbientSpawnLimit();
  }

  @Nullable
  public BiomeSearchResult locateNearestBiome(@NotNull Location location, int i, @NotNull Biome... biomes) {
    return world.locateNearestBiome(location, i, biomes);
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public long getTicksPerWaterAmbientSpawns() {
    return world.getTicksPerWaterAmbientSpawns();
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsync(int x, int z) {
    return world.getChunkAtAsync(x, z);
  }

  public void setThunderDuration(int i) {
    world.setThunderDuration(i);
  }

  @NotNull
  public List<Entity> getEntities() {
    return world.getEntities();
  }

  public boolean createExplosion(@Nullable Entity source, @NotNull Location loc, float power, boolean setFire,
                                 boolean breakBlocks) {
    return world.createExplosion(source, loc, power, setFire, breakBlocks);
  }

  @Nullable
  public BiomeProvider getBiomeProvider() {
    return world.getBiomeProvider();
  }

  @NotNull
  public Chunk @NotNull [] getLoadedChunks() {
    return world.getLoadedChunks();
  }

  public void setHardcore(boolean b) {
    world.setHardcore(b);
  }

  @NotNull
  public BiomeProvider vanillaBiomeProvider() {
    return world.vanillaBiomeProvider();
  }

  public void setMetadata(@NotNull String s, @NotNull MetadataValue metadataValue) {
    world.setMetadata(s, metadataValue);
  }

  public int getPlayerCount() {
    return world.getPlayerCount();
  }

  @NotNull
  public Collection<Entity> getNearbyEntities(@NotNull Location location, double v, double v1, double v2) {
    return world.getNearbyEntities(location, v, v1, v2);
  }

  @Nullable
  public Raid locateNearestRaid(@NotNull Location location, int i) {
    return world.locateNearestRaid(location, i);
  }

  public void playNote(@NotNull Location location, @NotNull Instrument instrument, @NotNull Note note) {
    world.playNote(location, instrument, note);
  }

  public int getSeaLevel() {
    return world.getSeaLevel();
  }

  public double getVoidDamageMinBuildHeightOffset() {
    return world.getVoidDamageMinBuildHeightOffset();
  }

  @NotNull
  public Biome getBiome(@NotNull Location location) {
    return world.getBiome(location);
  }

  public void setSpawnFlags(boolean b, boolean b1) {
    world.setSpawnFlags(b, b1);
  }

  public void playSound(net.kyori.adventure.sound.@NotNull Sound sound) {
    world.playSound(sound);
  }

  public boolean isEnabled(FeatureDependant featureDependant) {
    return world.isEnabled(featureDependant);
  }

  @Nullable
  public RayTraceResult rayTraceBlocks(@NotNull Location location, @NotNull Vector vector, double v) {
    return world.rayTraceBlocks(location, vector, v);
  }

  @NotNull
  public Location getSpawnLocation() {
    return world.getSpawnLocation();
  }

  public void sendResourcePacks(@NotNull ResourcePackRequest request) {
    world.sendResourcePacks(request);
  }

  public boolean isChunkGenerated(long chunkKey) {
    return world.isChunkGenerated(chunkKey);
  }

  @NotNull
  public Entity spawnEntity(@NotNull Location location, @NotNull EntityType entityType, boolean b) {
    return world.spawnEntity(location, entityType, b);
  }

  public boolean createExplosion(@Nullable Entity source, @NotNull Location loc, float power, boolean setFire) {
    return world.createExplosion(source, loc, power, setFire);
  }

  public int getSendViewDistance() {
    return world.getSendViewDistance();
  }

  public void getChunkAtAsync(@NotNull Block block, @NotNull Consumer<? super Chunk> cb) {
    world.getChunkAtAsync(block, cb);
  }

  public void setBiome(@NotNull Location location, @NotNull Biome biome) {
    world.setBiome(location, biome);
  }

  public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T t, int i) {
    world.playEffect(location, effect, t, i);
  }

  public int getHighestBlockYAt(int i, int i1) {
    return world.getHighestBlockYAt(i, i1);
  }

  public void forEachAudience(@NotNull Consumer<? super Audience> action) {
    world.forEachAudience(action);
  }

  public void stopSound(@NotNull SoundStop stop) {
    world.stopSound(stop);
  }

  @NotNull
  public Collection<Player> getNearbyPlayers(@NotNull Location loc, double radius) {
    return world.getNearbyPlayers(loc, radius);
  }

  public void setBiome(int i, int i1, int i2, @NotNull Biome biome) {
    world.setBiome(i, i1, i2, biome);
  }

  @NotNull
  public BlockData getBlockData(@NotNull Location location) {
    return world.getBlockData(location);
  }

  public void loadChunk(@NotNull Chunk chunk) {
    world.loadChunk(chunk);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Location loc) {
    return world.getChunkAtAsyncUrgently(loc);
  }

  public void sendPlayerListHeader(@NotNull Component header) {
    world.sendPlayerListHeader(header);
  }

  public void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
    world.sendPlayerListHeaderAndFooter(header, footer);
  }

  public void showDialog(@NotNull DialogLike dialog) {
    world.showDialog(dialog);
  }

  public void sendMessage(@NotNull Component message, ChatType.@NotNull Bound boundChatType) {
    world.sendMessage(message, boundChatType);
  }

  public void removeResourcePacks(@NotNull ResourcePackRequest request) {
    world.removeResourcePacks(request);
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.20.5")
  public boolean getKeepSpawnInMemory() {
    return world.getKeepSpawnInMemory();
  }

  public void setVoidDamageMinBuildHeightOffset(double v) {
    world.setVoidDamageMinBuildHeightOffset(v);
  }

  public boolean hasStructureAt(@NotNull Position position, @NotNull Structure structure) {
    return world.hasStructureAt(position, structure);
  }

  public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> aClass,
                                             @Nullable Consumer<? super T> consumer,
                                             CreatureSpawnEvent.@NotNull SpawnReason spawnReason) throws IllegalArgumentException {
    return world.spawn(location, aClass, consumer, spawnReason);
  }

  public void removeResourcePacks(@NotNull UUID id, @NotNull UUID @NotNull ... others) {
    world.removeResourcePacks(id, others);
  }

  public <T> @Nullable T getGameRuleValue(@NotNull GameRule<T> gameRule) {
    return world.getGameRuleValue(gameRule);
  }

  @NotNull
  public LightningStrike strikeLightningEffect(@NotNull Location location) {
    return world.strikeLightningEffect(location);
  }

  public float getVoidDamageAmount() {
    return world.getVoidDamageAmount();
  }

  @NotNull
  public Block getBlockAt(int i, int i1, int i2) {
    return world.getBlockAt(i, i1, i2);
  }

  public void playEffect(@NotNull Location location, @NotNull Effect effect, int i) {
    world.playEffect(location, effect, i);
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public long getTicksPerWaterSpawns() {
    return world.getTicksPerWaterSpawns();
  }

  @NotNull
  public List<Raid> getRaids() {
    return world.getRaids();
  }

  @NotNull
  public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double xzRadius, double yRadius) {
    return world.getNearbyLivingEntities(loc, xzRadius, yRadius);
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public int getWaterAmbientSpawnLimit() {
    return world.getWaterAmbientSpawnLimit();
  }

  @NotNull
  public List<LivingEntity> getLivingEntities() {
    return world.getLivingEntities();
  }

  public void playSound(@NotNull Location location, @NotNull String s, @NotNull SoundCategory soundCategory, float v,
                        float v1, long l) {
    world.playSound(location, s, soundCategory, v, v1, l);
  }

  public boolean createExplosion(@NotNull Entity source, float power) {
    return world.createExplosion(source, power);
  }

  public void save() {
    world.save();
  }

  /**
   * @param source
   * @param message
   * @deprecated
   */
  @Deprecated
  public void sendMessage(@NotNull Identified source, @NotNull ComponentLike message) {
    world.sendMessage(source, message);
  }

  public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz,
                                             CreatureSpawnEvent.@NotNull SpawnReason reason, @Nullable Consumer<?
          super T> function) throws IllegalArgumentException {
    return world.spawn(location, clazz, reason, function);
  }

  public boolean isChunkForceLoaded(int i, int i1) {
    return world.isChunkForceLoaded(i, i1);
  }

  /**
   * @param loc
   * @param cb
   * @deprecated
   */
  @Deprecated(since = "1.13.1")
  public void getChunkAtAsync(@NotNull Location loc, World.@NotNull ChunkLoadCallback cb) {
    world.getChunkAtAsync(loc, cb);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                                double v2, double v3, @Nullable T t, boolean b) {
    world.spawnParticle(particle, location, i, v, v1, v2, v3, t, b);
  }

  /**
   * @deprecated
   */
  @Deprecated
  public int getNoTickViewDistance() {
    return world.getNoTickViewDistance();
  }

  @NotNull
  public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz,
                                                                  @NotNull Location loc, double radius) {
    return world.getNearbyEntitiesByType(clazz, loc, radius);
  }

  public long getTime() {
    return world.getTime();
  }

  @NotNull
  public Chunk getChunkAt(@NotNull Block block) {
    return world.getChunkAt(block);
  }

  @NotNull
  public Block getHighestBlockAt(@NotNull Location location) {
    return world.getHighestBlockAt(location);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsync(int x, int z, boolean gen, boolean urgent) {
    return world.getChunkAtAsync(x, z, gen, urgent);
  }

  /**
   * @param i
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public void setTicksPerAmbientSpawns(int i) {
    world.setTicksPerAmbientSpawns(i);
  }

  public <T> @UnknownNullability T getOrDefaultFrom(@NotNull Pointer<T> pointer,
                                                    @NotNull Supplier<? extends T> defaultValue) {
    return world.getOrDefaultFrom(pointer, defaultValue);
  }

  @NotNull
  public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz,
                                                                  @NotNull Location loc, double xzRadius,
                                                                  double yRadius) {
    return world.getNearbyEntitiesByType(clazz, loc, xzRadius, yRadius);
  }

  public boolean isChunkGenerated(int i, int i1) {
    return world.isChunkGenerated(i, i1);
  }

  public boolean hasRaids() {
    return world.hasRaids();
  }

  @NotNull
  public File getWorldFolder() {
    return world.getWorldFolder();
  }

  @Nullable
  public Location findLightningTarget(@NotNull Location location) {
    return world.findLightningTarget(location);
  }

  @Nullable
  public RayTraceResult rayTrace(@NotNull Location location, @NotNull Vector vector, double v,
                                 @NotNull FluidCollisionMode fluidCollisionMode, boolean b, double v1,
                                 @Nullable Predicate<? super Entity> predicate) {
    return world.rayTrace(location, vector, v, fluidCollisionMode, b, v1, predicate);
  }

  public void setTicksPerSpawns(@NotNull SpawnCategory spawnCategory, int i) {
    world.setTicksPerSpawns(spawnCategory, i);
  }

  @NotNull
  public Chunk getChunkAt(long chunkKey, boolean generate) {
    return world.getChunkAt(chunkKey, generate);
  }

  @NotNull
  public Difficulty getDifficulty() {
    return world.getDifficulty();
  }

  public boolean setSpawnLocation(@NotNull Location location) {
    return world.setSpawnLocation(location);
  }

  public boolean canGenerateStructures() {
    return world.canGenerateStructures();
  }

  public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz,
                                             CreatureSpawnEvent.@NotNull SpawnReason reason) throws IllegalArgumentException {
    return world.spawn(location, clazz, reason);
  }

  /**
   * @param i
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public void setMonsterSpawnLimit(int i) {
    world.setMonsterSpawnLimit(i);
  }

  public boolean isClearWeather() {
    return world.isClearWeather();
  }

  public int getTickableTileEntityCount() {
    return world.getTickableTileEntityCount();
  }

  public void setFullTime(long l) {
    world.setFullTime(l);
  }

  @NotNull
  public Entity spawnEntity(@NotNull Location loc, @NotNull EntityType type,
                            CreatureSpawnEvent.@NotNull SpawnReason reason,
                            @Nullable Consumer<? super Entity> function) {
    return world.spawnEntity(loc, type, reason, function);
  }

  public boolean createExplosion(double v, double v1, double v2, float v3, boolean b, boolean b1) {
    return world.createExplosion(v, v1, v2, v3, b, b1);
  }

  public void setPVP(boolean b) {
    world.setPVP(b);
  }

  /**
   * @param i
   * @param i1
   * @deprecated
   */
  @Deprecated(since = "1.15")
  @NotNull
  public Biome getBiome(int i, int i1) {
    return world.getBiome(i, i1);
  }

  public boolean isAutoSave() {
    return world.isAutoSave();
  }

  @Contract("_, null -> _; _, !null -> !null")
  public <T> @Nullable T getOrDefault(@NotNull Pointer<T> pointer, @Nullable T defaultValue) {
    return world.getOrDefault(pointer, defaultValue);
  }

  public void setType(@NotNull Location location, @NotNull Material material) {
    world.setType(location, material);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                                double v2, double v3, @Nullable T t) {
    world.spawnParticle(particle, location, i, v, v1, v2, v3, t);
  }

  public int getMaxHeight() {
    return world.getMaxHeight();
  }

  public int getWeatherDuration() {
    return world.getWeatherDuration();
  }

  public int getHighestBlockYAt(@NotNull Location location, @NotNull HeightMap heightMap) {
    return world.getHighestBlockYAt(location, heightMap);
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public long getTicksPerMonsterSpawns() {
    return world.getTicksPerMonsterSpawns();
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public int getWaterUndergroundCreatureSpawnLimit() {
    return world.getWaterUndergroundCreatureSpawnLimit();
  }

  public World.@NotNull Environment getEnvironment() {
    return world.getEnvironment();
  }

  public void playSound(@NotNull Entity entity, @NotNull String s, float v, float v1) {
    world.playSound(entity, s, v, v1);
  }

  public void getChunkAtAsync(@NotNull Location loc, boolean gen, @NotNull Consumer<? super Chunk> cb) {
    world.getChunkAtAsync(loc, gen, cb);
  }

  @NotNull
  public Item dropItem(@NotNull Location location, @NotNull ItemStack itemStack,
                       @Nullable Consumer<? super Item> consumer) {
    return world.dropItem(location, itemStack, consumer);
  }

  @NotNull
  public FluidData getFluidData(int i, int i1, int i2) {
    return world.getFluidData(i, i1, i2);
  }

  public void setSendViewDistance(int i) {
    world.setSendViewDistance(i);
  }

  @NotNull
  public Collection<Player> getPlayersSeeingChunk(@NotNull Chunk chunk) {
    return world.getPlayersSeeingChunk(chunk);
  }

  public void sendMessage(@NotNull ComponentLike message, ChatType.@NotNull Bound boundChatType) {
    world.sendMessage(message, boundChatType);
  }

  /**
   * @param s
   * @deprecated
   */
  @Contract("null -> null; !null -> !null")
  @Deprecated(since = "1.13")
  @Nullable
  public String getGameRuleValue(@Nullable String s) {
    return world.getGameRuleValue(s);
  }

  /**
   * @param key
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  @NotNull
  public Location getLocationAtKey(long key) {
    return world.getLocationAtKey(key);
  }

  public boolean createExplosion(double v, double v1, double v2, float v3, boolean b, boolean b1,
                                 @Nullable Entity entity) {
    return world.createExplosion(v, v1, v2, v3, b, b1, entity);
  }

  public boolean unloadChunk(int i, int i1) {
    return world.unloadChunk(i, i1);
  }

  @NotNull
  public List<Player> getPlayers() {
    return world.getPlayers();
  }

  public void setThundering(boolean b) {
    world.setThundering(b);
  }

  public int getLogicalHeight() {
    return world.getLogicalHeight();
  }

  /**
   * @param i
   * @deprecated
   */
  @Deprecated(since = "1.18.1")
  public void setTicksPerWaterUndergroundCreatureSpawns(int i) {
    world.setTicksPerWaterUndergroundCreatureSpawns(i);
  }

  public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> aClass) throws IllegalArgumentException {
    return world.spawn(location, aClass);
  }

  public void setViewDistance(int i) {
    world.setViewDistance(i);
  }

  public void removeMetadata(@NotNull String s, @NotNull Plugin plugin) {
    world.removeMetadata(s, plugin);
  }

  public boolean createExplosion(double v, double v1, double v2, float v3, boolean b) {
    return world.createExplosion(v, v1, v2, v3, b);
  }

  @NotNull
  public Collection<Player> getNearbyPlayers(@NotNull Location loc, double xzRadius, double yRadius,
                                             @Nullable Predicate<? super Player> predicate) {
    return world.getNearbyPlayers(loc, xzRadius, yRadius, predicate);
  }

  public double getTemperature(int i, int i1, int i2) {
    return world.getTemperature(i, i1, i2);
  }

  public void resetTitle() {
    world.resetTitle();
  }

  /**
   * @param source
   * @param message
   * @deprecated
   */
  @Deprecated
  public void sendMessage(@NotNull Identity source, @NotNull ComponentLike message) {
    world.sendMessage(source, message);
  }

  public void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory soundCategory, float v,
                        float v1) {
    world.playSound(entity, sound, soundCategory, v, v1);
  }

  @NotNull
  public Entity spawnEntity(@NotNull Location loc, @NotNull EntityType type,
                            CreatureSpawnEvent.@NotNull SpawnReason reason) {
    return world.spawnEntity(loc, type, reason);
  }

  @NotNull
  public Collection<Player> getNearbyPlayers(@NotNull Location loc, double xRadius, double yRadius, double zRadius,
                                             @Nullable Predicate<? super Player> predicate) {
    return world.getNearbyPlayers(loc, xRadius, yRadius, zRadius, predicate);
  }

  public void sendPluginMessage(@NotNull Plugin plugin, @NotNull String s, byte @NotNull [] bytes) {
    world.sendPluginMessage(plugin, s, bytes);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsync(@NotNull Block block, boolean gen) {
    return world.getChunkAtAsync(block, gen);
  }

  /**
   * @param source
   * @param message
   * @deprecated
   */
  @Deprecated
  public void sendMessage(@NotNull Identity source, @NotNull Component message) {
    world.sendMessage(source, message);
  }

  public boolean setSpawnLocation(int i, int i1, int i2) {
    return world.setSpawnLocation(i, i1, i2);
  }

  public boolean isUltraWarm() {
    return world.isUltraWarm();
  }

  @NotNull
  public Entity spawnEntity(@NotNull Location location, @NotNull EntityType entityType) {
    return world.spawnEntity(location, entityType);
  }

  @Nullable
  public StructureSearchResult locateNearestStructure(@NotNull Location location, @NotNull Structure structure, int i
      , boolean b) {
    return world.locateNearestStructure(location, structure, i, b);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsync(@NotNull Block block) {
    return world.getChunkAtAsync(block);
  }

  @Nullable
  public RayTraceResult rayTraceEntities(@NotNull Location location, @NotNull Vector vector, double v,
                                         @Nullable Predicate<? super Entity> predicate) {
    return world.rayTraceEntities(location, vector, v, predicate);
  }

  @NotNull
  public Block getHighestBlockAt(@NotNull Location location, @NotNull HeightMap heightMap) {
    return world.getHighestBlockAt(location, heightMap);
  }

  public boolean removePluginChunkTicket(int i, int i1, @NotNull Plugin plugin) {
    return world.removePluginChunkTicket(i, i1, plugin);
  }

  public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType treeType) {
    return world.generateTree(location, random, treeType);
  }

  public void playSound(@NotNull Location location, @NotNull String s, float v, float v1) {
    world.playSound(location, s, v, v1);
  }

  public void sendResourcePacks(@NotNull ResourcePackRequestLike request) {
    world.sendResourcePacks(request);
  }

  /**
   * @param origin
   * @param biome
   * @param radius
   * @deprecated
   */
  @Deprecated
  @Nullable
  public Location locateNearestBiome(@NotNull Location origin, @NotNull Biome biome, int radius) {
    return world.locateNearestBiome(origin, biome, radius);
  }

  /**
   * @param classes
   * @deprecated
   */
  @Deprecated(since = "1.1")
  @NotNull
  public <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T>... classes) {
    return world.getEntitiesByClass(classes);
  }

  @Nullable
  public RayTraceResult rayTraceEntities(@NotNull Location location, @NotNull Vector vector, double v, double v1) {
    return world.rayTraceEntities(location, vector, v, v1);
  }

  public void setClearWeatherDuration(int i) {
    world.setClearWeatherDuration(i);
  }
}
