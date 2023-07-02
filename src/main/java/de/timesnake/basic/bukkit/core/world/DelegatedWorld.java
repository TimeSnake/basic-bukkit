/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.world;

import io.papermc.paper.world.MoonPhase;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointer;
import net.kyori.adventure.pointer.Pointers;
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
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.bukkit.util.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class DelegatedWorld {

  protected World world;

  public DelegatedWorld(World world) {
    this.world = world;
  }

  public int getEntityCount() {
    return world.getEntityCount();
  }

  public int getTileEntityCount() {
    return world.getTileEntityCount();
  }

  public int getTickableTileEntityCount() {
    return world.getTickableTileEntityCount();
  }

  public int getChunkCount() {
    return world.getChunkCount();
  }

  public int getPlayerCount() {
    return world.getPlayerCount();
  }

  @NotNull
  public Block getBlockAt(int x, int y, int z) {
    return world.getBlockAt(x, y, z);
  }

  @NotNull
  public Block getBlockAt(@NotNull Location location) {
    return world.getBlockAt(location);
  }

  @Deprecated
  @NotNull
  public Block getBlockAtKey(long key) {
    return world.getBlockAtKey(key);
  }

  @NotNull
  public Location getLocationAtKey(long key) {
    return world.getLocationAtKey(key);
  }

  @NotNull
  public Block getHighestBlockAt(int x, int z) {
    return world.getHighestBlockAt(x, z);
  }

  @NotNull
  public Block getHighestBlockAt(@NotNull Location location) {
    return world.getHighestBlockAt(location);
  }

  @NotNull
  public Block getHighestBlockAt(int x, int z, @NotNull HeightMap heightMap) {
    return world.getHighestBlockAt(x, z, heightMap);
  }

  @NotNull
  public Block getHighestBlockAt(@NotNull Location location, @NotNull HeightMap heightMap) {
    return world.getHighestBlockAt(location, heightMap);
  }

  @NotNull
  public Chunk getChunkAt(int x, int z) {
    return world.getChunkAt(x, z);
  }

  @NotNull
  public Chunk getChunkAt(int x, int z, boolean generate) {
    return world.getChunkAt(x, z, generate);
  }

  @NotNull
  public Chunk getChunkAt(@NotNull Location location) {
    return world.getChunkAt(location);
  }

  @NotNull
  public Chunk getChunkAt(@NotNull Block block) {
    return world.getChunkAt(block);
  }

  @NotNull
  public Chunk getChunkAt(long chunkKey) {
    return world.getChunkAt(chunkKey);
  }

  public boolean isChunkGenerated(long chunkKey) {
    return world.isChunkGenerated(chunkKey);
  }

  public boolean isChunkLoaded(@NotNull Chunk chunk) {
    return world.isChunkLoaded(chunk);
  }

  @NotNull
  public Chunk[] getLoadedChunks() {
    return world.getLoadedChunks();
  }

  public void loadChunk(@NotNull Chunk chunk) {
    world.loadChunk(chunk);
  }

  public boolean isChunkLoaded(int x, int z) {
    return world.isChunkLoaded(x, z);
  }

  public boolean isChunkGenerated(int x, int z) {
    return world.isChunkGenerated(x, z);
  }

  @Deprecated
  public boolean isChunkInUse(int x, int z) {
    return world.isChunkInUse(x, z);
  }

  public void loadChunk(int x, int z) {
    world.loadChunk(x, z);
  }

  public boolean loadChunk(int x, int z, boolean generate) {
    return world.loadChunk(x, z, generate);
  }

  public boolean unloadChunk(@NotNull Chunk chunk) {
    return world.unloadChunk(chunk);
  }

  public boolean unloadChunk(int x, int z) {
    return world.unloadChunk(x, z);
  }

  public boolean unloadChunk(int x, int z, boolean save) {
    return world.unloadChunk(x, z, save);
  }

  public boolean unloadChunkRequest(int x, int z) {
    return world.unloadChunkRequest(x, z);
  }

  @Deprecated
  public boolean regenerateChunk(int x, int z) {
    return world.regenerateChunk(x, z);
  }

  public boolean refreshChunk(int x, int z) {
    return world.refreshChunk(x, z);
  }

  public boolean isChunkForceLoaded(int x, int z) {
    return world.isChunkForceLoaded(x, z);
  }

  public void setChunkForceLoaded(int x, int z, boolean forced) {
    world.setChunkForceLoaded(x, z, forced);
  }

  @NotNull
  public Collection<Chunk> getForceLoadedChunks() {
    return world.getForceLoadedChunks();
  }

  public boolean addPluginChunkTicket(int x, int z, @NotNull Plugin plugin) {
    return world.addPluginChunkTicket(x, z, plugin);
  }

  public boolean removePluginChunkTicket(int x, int z, @NotNull Plugin plugin) {
    return world.removePluginChunkTicket(x, z, plugin);
  }

  public void removePluginChunkTickets(@NotNull Plugin plugin) {
    world.removePluginChunkTickets(plugin);
  }

  @NotNull
  public Collection<Plugin> getPluginChunkTickets(int x, int z) {
    return world.getPluginChunkTickets(x, z);
  }

  @NotNull
  public Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {
    return world.getPluginChunkTickets();
  }

  @NotNull
  public Item dropItem(@NotNull Location location, @NotNull ItemStack item) {
    return world.dropItem(location, item);
  }

  @NotNull
  public Item dropItem(@NotNull Location location, @NotNull ItemStack item, @Nullable Consumer<Item> function) {
    return world.dropItem(location, item, function);
  }

  @NotNull
  public Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack item) {
    return world.dropItemNaturally(location, item);
  }

  @NotNull
  public Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack item, @Nullable Consumer<Item> function) {
    return world.dropItemNaturally(location, item, function);
  }

  @NotNull
  public Arrow spawnArrow(@NotNull Location location, @NotNull Vector direction, float speed, float spread) {
    return world.spawnArrow(location, direction, speed, spread);
  }

  public <T extends AbstractArrow> @NotNull T spawnArrow(@NotNull Location location, @NotNull Vector direction, float speed, float spread, @NotNull Class<T> clazz) {
    return world.spawnArrow(location, direction, speed, spread, clazz);
  }

  public boolean generateTree(@NotNull Location location, @NotNull TreeType type) {
    return world.generateTree(location, type);
  }

  @Deprecated
  public boolean generateTree(@NotNull Location loc, @NotNull TreeType type, @NotNull BlockChangeDelegate delegate) {
    return world.generateTree(loc, type, delegate);
  }

  @NotNull
  public LightningStrike strikeLightning(@NotNull Location loc) {
    return world.strikeLightning(loc);
  }

  @NotNull
  public LightningStrike strikeLightningEffect(@NotNull Location loc) {
    return world.strikeLightningEffect(loc);
  }

  @Nullable
  public Location findLightningRod(@NotNull Location location) {
    return world.findLightningRod(location);
  }

  @Nullable
  public Location findLightningTarget(@NotNull Location location) {
    return world.findLightningTarget(location);
  }

  @NotNull
  public List<Entity> getEntities() {
    return world.getEntities();
  }

  @NotNull
  public List<LivingEntity> getLivingEntities() {
    return world.getLivingEntities();
  }

  @Deprecated
  @NotNull
  public <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T>... classes) {
    return world.getEntitiesByClass(classes);
  }

  @NotNull
  public <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T> cls) {
    return world.getEntitiesByClass(cls);
  }

  @NotNull
  public Collection<Entity> getEntitiesByClasses(@NotNull Class<?>... classes) {
    return world.getEntitiesByClasses(classes);
  }

  @NotNull
  public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double radius) {
    return world.getNearbyLivingEntities(loc, radius);
  }

  @NotNull
  public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double xzRadius, double yRadius) {
    return world.getNearbyLivingEntities(loc, xzRadius, yRadius);
  }

  @NotNull
  public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double xRadius, double yRadius, double zRadius) {
    return world.getNearbyLivingEntities(loc, xRadius, yRadius, zRadius);
  }

  @NotNull
  public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double radius, @Nullable Predicate<LivingEntity> predicate) {
    return world.getNearbyLivingEntities(loc, radius, predicate);
  }

  @NotNull
  public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double xzRadius, double yRadius, @Nullable Predicate<LivingEntity> predicate) {
    return world.getNearbyLivingEntities(loc, xzRadius, yRadius, predicate);
  }

  @NotNull
  public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double xRadius, double yRadius, double zRadius, @Nullable Predicate<LivingEntity> predicate) {
    return world.getNearbyLivingEntities(loc, xRadius, yRadius, zRadius, predicate);
  }

  @NotNull
  public Collection<Player> getNearbyPlayers(@NotNull Location loc, double radius) {
    return world.getNearbyPlayers(loc, radius);
  }

  @NotNull
  public Collection<Player> getNearbyPlayers(@NotNull Location loc, double xzRadius, double yRadius) {
    return world.getNearbyPlayers(loc, xzRadius, yRadius);
  }

  @NotNull
  public Collection<Player> getNearbyPlayers(@NotNull Location loc, double xRadius, double yRadius, double zRadius) {
    return world.getNearbyPlayers(loc, xRadius, yRadius, zRadius);
  }

  @NotNull
  public Collection<Player> getNearbyPlayers(@NotNull Location loc, double radius, @Nullable Predicate<Player> predicate) {
    return world.getNearbyPlayers(loc, radius, predicate);
  }

  @NotNull
  public Collection<Player> getNearbyPlayers(@NotNull Location loc, double xzRadius, double yRadius, @Nullable Predicate<Player> predicate) {
    return world.getNearbyPlayers(loc, xzRadius, yRadius, predicate);
  }

  @NotNull
  public Collection<Player> getNearbyPlayers(@NotNull Location loc, double xRadius, double yRadius, double zRadius, @Nullable Predicate<Player> predicate) {
    return world.getNearbyPlayers(loc, xRadius, yRadius, zRadius, predicate);
  }

  @NotNull
  public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz, @NotNull Location loc, double radius) {
    return world.getNearbyEntitiesByType(clazz, loc, radius);
  }

  @NotNull
  public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz, @NotNull Location loc, double xzRadius, double yRadius) {
    return world.getNearbyEntitiesByType(clazz, loc, xzRadius, yRadius);
  }

  @NotNull
  public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz, @NotNull Location loc, double xRadius, double yRadius, double zRadius) {
    return world.getNearbyEntitiesByType(clazz, loc, xRadius, yRadius, zRadius);
  }

  @NotNull
  public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz, @NotNull Location loc, double radius, @Nullable Predicate<T> predicate) {
    return world.getNearbyEntitiesByType(clazz, loc, radius, predicate);
  }

  @NotNull
  public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz, @NotNull Location loc, double xzRadius, double yRadius, @Nullable Predicate<T> predicate) {
    return world.getNearbyEntitiesByType(clazz, loc, xzRadius, yRadius, predicate);
  }

  @NotNull
  public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends Entity> clazz, @NotNull Location loc, double xRadius, double yRadius, double zRadius, @Nullable Predicate<T> predicate) {
    return world.getNearbyEntitiesByType(clazz, loc, xRadius, yRadius, zRadius, predicate);
  }

  public void getChunkAtAsync(int x, int z, java.util.function.@NotNull Consumer<Chunk> cb) {
    world.getChunkAtAsync(x, z, cb);
  }

  public void getChunkAtAsync(int x, int z, boolean gen, java.util.function.@NotNull Consumer<Chunk> cb) {
    world.getChunkAtAsync(x, z, gen, cb);
  }

  public void getChunkAtAsync(@NotNull Location loc, java.util.function.@NotNull Consumer<Chunk> cb) {
    world.getChunkAtAsync(loc, cb);
  }

  public void getChunkAtAsync(@NotNull Location loc, boolean gen, java.util.function.@NotNull Consumer<Chunk> cb) {
    world.getChunkAtAsync(loc, gen, cb);
  }

  public void getChunkAtAsync(@NotNull Block block, java.util.function.@NotNull Consumer<Chunk> cb) {
    world.getChunkAtAsync(block, cb);
  }

  public void getChunkAtAsync(@NotNull Block block, boolean gen, java.util.function.@NotNull Consumer<Chunk> cb) {
    world.getChunkAtAsync(block, gen, cb);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsync(@NotNull Location loc) {
    return world.getChunkAtAsync(loc);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsync(@NotNull Location loc, boolean gen) {
    return world.getChunkAtAsync(loc, gen);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsync(@NotNull Block block) {
    return world.getChunkAtAsync(block);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsync(@NotNull Block block, boolean gen) {
    return world.getChunkAtAsync(block, gen);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsync(int x, int z) {
    return world.getChunkAtAsync(x, z);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsync(int x, int z, boolean gen) {
    return world.getChunkAtAsync(x, z, gen);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Location loc) {
    return world.getChunkAtAsyncUrgently(loc);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Location loc, boolean gen) {
    return world.getChunkAtAsyncUrgently(loc, gen);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Block block) {
    return world.getChunkAtAsyncUrgently(block);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Block block, boolean gen) {
    return world.getChunkAtAsyncUrgently(block, gen);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsyncUrgently(int x, int z) {
    return world.getChunkAtAsyncUrgently(x, z);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsync(int x, int z, boolean gen, boolean urgent) {
    return world.getChunkAtAsync(x, z, gen, urgent);
  }

  @NotNull
  public List<Player> getPlayers() {
    return world.getPlayers();
  }

  @NotNull
  public Iterable<? extends Audience> audiences() {
    return world.audiences();
  }

  @NotNull
  public Collection<Entity> getNearbyEntities(@NotNull Location location, double x, double y, double z) {
    return world.getNearbyEntities(location, x, y, z);
  }

  @Nullable
  public Entity getEntity(@NotNull UUID uuid) {
    return world.getEntity(uuid);
  }

  @NotNull
  public Collection<Entity> getNearbyEntities(@NotNull Location location, double x, double y, double z, @Nullable Predicate<Entity> filter) {
    return world.getNearbyEntities(location, x, y, z, filter);
  }

  @NotNull
  public Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox) {
    return world.getNearbyEntities(boundingBox);
  }

  @NotNull
  public Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox, @Nullable Predicate<Entity> filter) {
    return world.getNearbyEntities(boundingBox, filter);
  }

  @Nullable
  public RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance) {
    return world.rayTraceEntities(start, direction, maxDistance);
  }

  @Nullable
  public RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance, double raySize) {
    return world.rayTraceEntities(start, direction, maxDistance, raySize);
  }

  @Nullable
  public RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance, @Nullable Predicate<Entity> filter) {
    return world.rayTraceEntities(start, direction, maxDistance, filter);
  }

  @Nullable
  public RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance, double raySize, @Nullable Predicate<Entity> filter) {
    return world.rayTraceEntities(start, direction, maxDistance, raySize, filter);
  }

  @Nullable
  public RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction, double maxDistance) {
    return world.rayTraceBlocks(start, direction, maxDistance);
  }

  @Nullable
  public RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
    return world.rayTraceBlocks(start, direction, maxDistance, fluidCollisionMode);
  }

  @Nullable
  public RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks) {
    return world.rayTraceBlocks(start, direction, maxDistance, fluidCollisionMode, ignorePassableBlocks);
  }

  @Nullable
  public RayTraceResult rayTrace(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks, double raySize, @Nullable Predicate<Entity> filter) {
    return world.rayTrace(start, direction, maxDistance, fluidCollisionMode, ignorePassableBlocks, raySize, filter);
  }

  @NotNull
  public Location getSpawnLocation() {
    return world.getSpawnLocation();
  }

  public boolean setSpawnLocation(@NotNull Location location) {
    return world.setSpawnLocation(location);
  }

  public boolean setSpawnLocation(int x, int y, int z, float angle) {
    return world.setSpawnLocation(x, y, z, angle);
  }

  public boolean setSpawnLocation(int x, int y, int z) {
    return world.setSpawnLocation(x, y, z);
  }

  public long getTime() {
    return world.getTime();
  }

  public void setTime(long time) {
    world.setTime(time);
  }

  public long getFullTime() {
    return world.getFullTime();
  }

  public void setFullTime(long time) {
    world.setFullTime(time);
  }

  public boolean isDayTime() {
    return world.isDayTime();
  }

  public long getGameTime() {
    return world.getGameTime();
  }

  public boolean hasStorm() {
    return world.hasStorm();
  }

  public void setStorm(boolean hasStorm) {
    world.setStorm(hasStorm);
  }

  public int getWeatherDuration() {
    return world.getWeatherDuration();
  }

  public void setWeatherDuration(int duration) {
    world.setWeatherDuration(duration);
  }

  public boolean isThundering() {
    return world.isThundering();
  }

  public void setThundering(boolean thundering) {
    world.setThundering(thundering);
  }

  public int getThunderDuration() {
    return world.getThunderDuration();
  }

  public void setThunderDuration(int duration) {
    world.setThunderDuration(duration);
  }

  public boolean isClearWeather() {
    return world.isClearWeather();
  }

  public void setClearWeatherDuration(int duration) {
    world.setClearWeatherDuration(duration);
  }

  public int getClearWeatherDuration() {
    return world.getClearWeatherDuration();
  }

  public boolean createExplosion(double x, double y, double z, float power) {
    return world.createExplosion(x, y, z, power);
  }

  public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
    return world.createExplosion(x, y, z, power, setFire);
  }

  public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
    return world.createExplosion(x, y, z, power, setFire, breakBlocks);
  }

  public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks, @Nullable Entity source) {
    return world.createExplosion(x, y, z, power, setFire, breakBlocks, source);
  }

  public boolean createExplosion(@NotNull Location loc, float power) {
    return world.createExplosion(loc, power);
  }

  public boolean createExplosion(@NotNull Location loc, float power, boolean setFire) {
    return world.createExplosion(loc, power, setFire);
  }

  public boolean createExplosion(@Nullable Entity source, @NotNull Location loc, float power, boolean setFire, boolean breakBlocks) {
    return world.createExplosion(source, loc, power, setFire, breakBlocks);
  }

  public boolean createExplosion(@Nullable Entity source, @NotNull Location loc, float power, boolean setFire) {
    return world.createExplosion(source, loc, power, setFire);
  }

  public boolean createExplosion(@Nullable Entity source, @NotNull Location loc, float power) {
    return world.createExplosion(source, loc, power);
  }

  public boolean createExplosion(@NotNull Entity source, float power, boolean setFire, boolean breakBlocks) {
    return world.createExplosion(source, power, setFire, breakBlocks);
  }

  public boolean createExplosion(@NotNull Entity source, float power, boolean setFire) {
    return world.createExplosion(source, power, setFire);
  }

  public boolean createExplosion(@NotNull Entity source, float power) {
    return world.createExplosion(source, power);
  }

  public boolean createExplosion(@NotNull Location loc, float power, boolean setFire, boolean breakBlocks) {
    return world.createExplosion(loc, power, setFire, breakBlocks);
  }

  public boolean createExplosion(@NotNull Location loc, float power, boolean setFire, boolean breakBlocks, @Nullable Entity source) {
    return world.createExplosion(loc, power, setFire, breakBlocks, source);
  }

  public boolean getPVP() {
    return world.getPVP();
  }

  public void setPVP(boolean pvp) {
    world.setPVP(pvp);
  }

  @Nullable
  public ChunkGenerator getGenerator() {
    return world.getGenerator();
  }

  @Nullable
  public BiomeProvider getBiomeProvider() {
    return world.getBiomeProvider();
  }

  public void save() {
    world.save();
  }

  @NotNull
  public List<BlockPopulator> getPopulators() {
    return world.getPopulators();
  }

  @Deprecated
  @NotNull
  public FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull MaterialData data) throws IllegalArgumentException {
    return world.spawnFallingBlock(location, data);
  }

  @NotNull
  public FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull BlockData data) throws IllegalArgumentException {
    return world.spawnFallingBlock(location, data);
  }

  @Deprecated
  @NotNull
  public FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull Material material, byte data) throws IllegalArgumentException {
    return world.spawnFallingBlock(location, material, data);
  }

  public void playEffect(@NotNull Location location, @NotNull Effect effect, int data) {
    world.playEffect(location, effect, data);
  }

  public void playEffect(@NotNull Location location, @NotNull Effect effect, int data, int radius) {
    world.playEffect(location, effect, data, radius);
  }

  public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T data) {
    world.playEffect(location, effect, data);
  }

  public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T data, int radius) {
    world.playEffect(location, effect, data, radius);
  }

  @NotNull
  public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTemp) {
    return world.getEmptyChunkSnapshot(x, z, includeBiome, includeBiomeTemp);
  }

  public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
    world.setSpawnFlags(allowMonsters, allowAnimals);
  }

  public boolean getAllowAnimals() {
    return world.getAllowAnimals();
  }

  public boolean getAllowMonsters() {
    return world.getAllowMonsters();
  }

  @Deprecated
  @NotNull
  public Biome getBiome(int x, int z) {
    return world.getBiome(x, z);
  }

  @Deprecated
  public void setBiome(int x, int z, @NotNull Biome bio) {
    world.setBiome(x, z, bio);
  }

  @Deprecated
  public double getTemperature(int x, int z) {
    return world.getTemperature(x, z);
  }

  public double getTemperature(int x, int y, int z) {
    return world.getTemperature(x, y, z);
  }

  @Deprecated
  public double getHumidity(int x, int z) {
    return world.getHumidity(x, z);
  }

  public double getHumidity(int x, int y, int z) {
    return world.getHumidity(x, y, z);
  }

  public int getLogicalHeight() {
    return world.getLogicalHeight();
  }

  public boolean isNatural() {
    return world.isNatural();
  }

  public boolean isBedWorks() {
    return world.isBedWorks();
  }

  public boolean hasSkyLight() {
    return world.hasSkyLight();
  }

  public boolean hasCeiling() {
    return world.hasCeiling();
  }

  public boolean isPiglinSafe() {
    return world.isPiglinSafe();
  }

  public boolean isRespawnAnchorWorks() {
    return world.isRespawnAnchorWorks();
  }

  public boolean hasRaids() {
    return world.hasRaids();
  }

  public boolean isUltraWarm() {
    return world.isUltraWarm();
  }

  public int getSeaLevel() {
    return world.getSeaLevel();
  }

  public boolean getKeepSpawnInMemory() {
    return world.getKeepSpawnInMemory();
  }

  public void setKeepSpawnInMemory(boolean keepLoaded) {
    world.setKeepSpawnInMemory(keepLoaded);
  }

  public boolean isAutoSave() {
    return world.isAutoSave();
  }

  public void setAutoSave(boolean value) {
    world.setAutoSave(value);
  }

  public void setDifficulty(@NotNull Difficulty difficulty) {
    world.setDifficulty(difficulty);
  }

  @NotNull
  public Difficulty getDifficulty() {
    return world.getDifficulty();
  }

  @NotNull
  public File getWorldFolder() {
    return world.getWorldFolder();
  }

  @Deprecated
  @Nullable
  public WorldType getWorldType() {
    return world.getWorldType();
  }

  public boolean canGenerateStructures() {
    return world.canGenerateStructures();
  }

  public boolean isHardcore() {
    return world.isHardcore();
  }

  public void setHardcore(boolean hardcore) {
    world.setHardcore(hardcore);
  }

  @Deprecated
  public long getTicksPerAnimalSpawns() {
    return world.getTicksPerAnimalSpawns();
  }

  @Deprecated
  public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {
    world.setTicksPerAnimalSpawns(ticksPerAnimalSpawns);
  }

  @Deprecated
  public long getTicksPerMonsterSpawns() {
    return world.getTicksPerMonsterSpawns();
  }

  @Deprecated
  public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {
    world.setTicksPerMonsterSpawns(ticksPerMonsterSpawns);
  }

  @Deprecated
  public long getTicksPerWaterSpawns() {
    return world.getTicksPerWaterSpawns();
  }

  @Deprecated
  public void setTicksPerWaterSpawns(int ticksPerWaterSpawns) {
    world.setTicksPerWaterSpawns(ticksPerWaterSpawns);
  }

  @Deprecated
  public long getTicksPerWaterAmbientSpawns() {
    return world.getTicksPerWaterAmbientSpawns();
  }

  @Deprecated
  public void setTicksPerWaterAmbientSpawns(int ticksPerAmbientSpawns) {
    world.setTicksPerWaterAmbientSpawns(ticksPerAmbientSpawns);
  }

  @Deprecated
  public long getTicksPerWaterUndergroundCreatureSpawns() {
    return world.getTicksPerWaterUndergroundCreatureSpawns();
  }

  @Deprecated
  public void setTicksPerWaterUndergroundCreatureSpawns(int ticksPerWaterUndergroundCreatureSpawns) {
    world.setTicksPerWaterUndergroundCreatureSpawns(ticksPerWaterUndergroundCreatureSpawns);
  }

  @Deprecated
  public long getTicksPerAmbientSpawns() {
    return world.getTicksPerAmbientSpawns();
  }

  @Deprecated
  public void setTicksPerAmbientSpawns(int ticksPerAmbientSpawns) {
    world.setTicksPerAmbientSpawns(ticksPerAmbientSpawns);
  }

  public long getTicksPerSpawns(@NotNull SpawnCategory spawnCategory) {
    return world.getTicksPerSpawns(spawnCategory);
  }

  public void setTicksPerSpawns(@NotNull SpawnCategory spawnCategory, int ticksPerCategorySpawn) {
    world.setTicksPerSpawns(spawnCategory, ticksPerCategorySpawn);
  }

  @Deprecated
  public int getMonsterSpawnLimit() {
    return world.getMonsterSpawnLimit();
  }

  @Deprecated
  public void setMonsterSpawnLimit(int limit) {
    world.setMonsterSpawnLimit(limit);
  }

  @Deprecated
  public int getAnimalSpawnLimit() {
    return world.getAnimalSpawnLimit();
  }

  @Deprecated
  public void setAnimalSpawnLimit(int limit) {
    world.setAnimalSpawnLimit(limit);
  }

  @Deprecated
  public int getWaterAnimalSpawnLimit() {
    return world.getWaterAnimalSpawnLimit();
  }

  @Deprecated
  public void setWaterAnimalSpawnLimit(int limit) {
    world.setWaterAnimalSpawnLimit(limit);
  }

  @Deprecated
  public int getWaterUndergroundCreatureSpawnLimit() {
    return world.getWaterUndergroundCreatureSpawnLimit();
  }

  @Deprecated
  public void setWaterUndergroundCreatureSpawnLimit(int limit) {
    world.setWaterUndergroundCreatureSpawnLimit(limit);
  }

  @Deprecated
  public int getWaterAmbientSpawnLimit() {
    return world.getWaterAmbientSpawnLimit();
  }

  @Deprecated
  public void setWaterAmbientSpawnLimit(int limit) {
    world.setWaterAmbientSpawnLimit(limit);
  }

  @Deprecated
  public int getAmbientSpawnLimit() {
    return world.getAmbientSpawnLimit();
  }

  @Deprecated
  public void setAmbientSpawnLimit(int limit) {
    world.setAmbientSpawnLimit(limit);
  }

  public int getSpawnLimit(@NotNull SpawnCategory spawnCategory) {
    return world.getSpawnLimit(spawnCategory);
  }

  public void setSpawnLimit(@NotNull SpawnCategory spawnCategory, int limit) {
    world.setSpawnLimit(spawnCategory, limit);
  }

  public void playSound(@NotNull Location location, @NotNull Sound sound, float volume, float pitch) {
    world.playSound(location, sound, volume, pitch);
  }

  public void playSound(@NotNull Location location, @NotNull String sound, float volume, float pitch) {
    world.playSound(location, sound, volume, pitch);
  }

  public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch) {
    world.playSound(location, sound, category, volume, pitch);
  }

  public void playSound(@NotNull Location location, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch) {
    world.playSound(location, sound, category, volume, pitch);
  }

  public void playSound(@NotNull Entity entity, @NotNull Sound sound, float volume, float pitch) {
    world.playSound(entity, sound, volume, pitch);
  }

  public void playSound(@NotNull Entity entity, @NotNull String sound, float volume, float pitch) {
    world.playSound(entity, sound, volume, pitch);
  }

  public void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch) {
    world.playSound(entity, sound, category, volume, pitch);
  }

  public void playSound(@NotNull Entity entity, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch) {
    world.playSound(entity, sound, category, volume, pitch);
  }

  @NotNull
  public String[] getGameRules() {
    return world.getGameRules();
  }

  @Contract("null -> null; !null -> !null")
  @Deprecated
  @Nullable
  public String getGameRuleValue(@Nullable String rule) {
    return world.getGameRuleValue(rule);
  }

  @Deprecated
  public boolean setGameRuleValue(@NotNull String rule, @NotNull String value) {
    return world.setGameRuleValue(rule, value);
  }

  public boolean isGameRule(@NotNull String rule) {
    return world.isGameRule(rule);
  }

  public <T> @Nullable T getGameRuleValue(@NotNull GameRule<T> rule) {
    return world.getGameRuleValue(rule);
  }

  public <T> @Nullable T getGameRuleDefault(@NotNull GameRule<T> rule) {
    return world.getGameRuleDefault(rule);
  }

  public <T> boolean setGameRule(@NotNull GameRule<T> rule, @NotNull T newValue) {
    return world.setGameRule(rule, newValue);
  }

  @NotNull
  public WorldBorder getWorldBorder() {
    return world.getWorldBorder();
  }

  public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count) {
    world.spawnParticle(particle, location, count);
  }

  public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count) {
    world.spawnParticle(particle, x, y, z, count);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, @Nullable T data) {
    world.spawnParticle(particle, location, count, data);
  }

  public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, @Nullable T data) {
    world.spawnParticle(particle, x, y, z, count, data);
  }

  public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ) {
    world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ);
  }

  public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
    world.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, @Nullable T data) {
    world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, data);
  }

  public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, @Nullable T data) {
    world.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, data);
  }

  public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
    world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra);
  }

  public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
    world.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {
    world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, data);
  }

  public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {
    world.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @Nullable List<Player> receivers, @NotNull Player source, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {
    world.spawnParticle(particle, receivers, source, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @Nullable List<Player> receivers, @Nullable Player source, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data, boolean force) {
    world.spawnParticle(particle, receivers, source, x, y, z, count, offsetX, offsetY, offsetZ, extra, data, force);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data, boolean force) {
    world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, data, force);
  }

  public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data, boolean force) {
    world.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data, force);
  }

  @Deprecated
  @Nullable
  public Location locateNearestStructure(@NotNull Location origin, @NotNull StructureType structureType, int radius, boolean findUnexplored) {
    return world.locateNearestStructure(origin, structureType, radius, findUnexplored);
  }

  @Nullable
  public StructureSearchResult locateNearestStructure(@NotNull Location origin, org.bukkit.generator.structure.@NotNull StructureType structureType, int radius, boolean findUnexplored) {
    return world.locateNearestStructure(origin, structureType, radius, findUnexplored);
  }

  @Nullable
  public StructureSearchResult locateNearestStructure(@NotNull Location origin, @NotNull Structure structure, int radius, boolean findUnexplored) {
    return world.locateNearestStructure(origin, structure, radius, findUnexplored);
  }

  @Nullable
  public Location locateNearestBiome(@NotNull Location origin, @NotNull Biome biome, int radius) {
    return world.locateNearestBiome(origin, biome, radius);
  }

  @Nullable
  public Location locateNearestBiome(@NotNull Location origin, @NotNull Biome biome, int radius, int step) {
    return world.locateNearestBiome(origin, biome, radius, step);
  }

  public boolean isFixedTime() {
    return world.isFixedTime();
  }

  @NotNull
  public Collection<Material> getInfiniburn() {
    return world.getInfiniburn();
  }

  public void sendGameEvent(@Nullable Entity sourceEntity, @NotNull GameEvent gameEvent, @NotNull Vector position) {
    world.sendGameEvent(sourceEntity, gameEvent, position);
  }

  public int getViewDistance() {
    return world.getViewDistance();
  }

  public int getSimulationDistance() {
    return world.getSimulationDistance();
  }

  public void setViewDistance(int viewDistance) {
    world.setViewDistance(viewDistance);
  }

  public void setSimulationDistance(int simulationDistance) {
    world.setSimulationDistance(simulationDistance);
  }

  @Deprecated
  public int getNoTickViewDistance() {
    return world.getNoTickViewDistance();
  }

  @Deprecated
  public void setNoTickViewDistance(int viewDistance) {
    world.setNoTickViewDistance(viewDistance);
  }

  public int getSendViewDistance() {
    return world.getSendViewDistance();
  }

  public void setSendViewDistance(int viewDistance) {
    world.setSendViewDistance(viewDistance);
  }

  @NotNull
  public World.Spigot spigot() {
    return world.spigot();
  }

  @Nullable
  public Raid locateNearestRaid(@NotNull Location location, int radius) {
    return world.locateNearestRaid(location, radius);
  }

  @NotNull
  public List<Raid> getRaids() {
    return world.getRaids();
  }

  @Nullable
  public DragonBattle getEnderDragonBattle() {
    return world.getEnderDragonBattle();
  }

  @NotNull
  public Set<FeatureFlag> getFeatureFlags() {
    return world.getFeatureFlags();
  }

  @NotNull
  public Biome getBiome(@NotNull Location location) {
    return world.getBiome(location);
  }

  @NotNull
  public Biome getBiome(int x, int y, int z) {
    return world.getBiome(x, y, z);
  }

  @NotNull
  public Biome getComputedBiome(int x, int y, int z) {
    return world.getComputedBiome(x, y, z);
  }

  public void setBiome(@NotNull Location location, @NotNull Biome biome) {
    world.setBiome(location, biome);
  }

  public void setBiome(int x, int y, int z, @NotNull Biome biome) {
    world.setBiome(x, y, z, biome);
  }

  @NotNull
  public BlockState getBlockState(@NotNull Location location) {
    return world.getBlockState(location);
  }

  @NotNull
  public BlockState getBlockState(int x, int y, int z) {
    return world.getBlockState(x, y, z);
  }

  @NotNull
  public BlockData getBlockData(@NotNull Location location) {
    return world.getBlockData(location);
  }

  @NotNull
  public BlockData getBlockData(int x, int y, int z) {
    return world.getBlockData(x, y, z);
  }

  @NotNull
  public Material getType(@NotNull Location location) {
    return world.getType(location);
  }

  @NotNull
  public Material getType(int x, int y, int z) {
    return world.getType(x, y, z);
  }

  public void setBlockData(@NotNull Location location, @NotNull BlockData blockData) {
    world.setBlockData(location, blockData);
  }

  public void setBlockData(int x, int y, int z, @NotNull BlockData blockData) {
    world.setBlockData(x, y, z, blockData);
  }

  public void setType(@NotNull Location location, @NotNull Material material) {
    world.setType(location, material);
  }

  public void setType(int x, int y, int z, @NotNull Material material) {
    world.setType(x, y, z, material);
  }

  public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType type) {
    return world.generateTree(location, random, type);
  }

  public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType type, @Nullable Consumer<BlockState> stateConsumer) {
    return world.generateTree(location, random, type, stateConsumer);
  }

  public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType type, @Nullable Predicate<BlockState> statePredicate) {
    return world.generateTree(location, random, type, statePredicate);
  }

  @NotNull
  public Entity spawnEntity(@NotNull Location location, @NotNull EntityType type) {
    return world.spawnEntity(location, type);
  }

  @NotNull
  public Entity spawnEntity(@NotNull Location loc, @NotNull EntityType type, boolean randomizeData) {
    return world.spawnEntity(loc, type, randomizeData);
  }

  public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz) throws IllegalArgumentException {
    return world.spawn(location, clazz);
  }

  public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz, @Nullable Consumer<T> function) throws IllegalArgumentException {
    return world.spawn(location, clazz, function);
  }

  public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz, CreatureSpawnEvent.@NotNull SpawnReason reason) throws IllegalArgumentException {
    return world.spawn(location, clazz, reason);
  }

  public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz, CreatureSpawnEvent.@NotNull SpawnReason reason, @Nullable Consumer<T> function) throws IllegalArgumentException {
    return world.spawn(location, clazz, reason, function);
  }

  @NotNull
  public Entity spawnEntity(@NotNull Location loc, @NotNull EntityType type, CreatureSpawnEvent.@NotNull SpawnReason reason) {
    return world.spawnEntity(loc, type, reason);
  }

  @NotNull
  public Entity spawnEntity(@NotNull Location loc, @NotNull EntityType type, CreatureSpawnEvent.@NotNull SpawnReason reason, @Nullable Consumer<Entity> function) {
    return world.spawnEntity(loc, type, reason, function);
  }

  public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz, @Nullable Consumer<T> function, CreatureSpawnEvent.@NotNull SpawnReason reason) throws IllegalArgumentException {
    return world.spawn(location, clazz, function, reason);
  }

  public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz, boolean randomizeData, @Nullable Consumer<T> function) throws IllegalArgumentException {
    return world.spawn(location, clazz, randomizeData, function);
  }

  public int getHighestBlockYAt(int x, int z) {
    return world.getHighestBlockYAt(x, z);
  }

  public int getHighestBlockYAt(@NotNull Location location) {
    return world.getHighestBlockYAt(location);
  }

  public int getHighestBlockYAt(int x, int z, @NotNull HeightMap heightMap) {
    return world.getHighestBlockYAt(x, z, heightMap);
  }

  public int getHighestBlockYAt(@NotNull Location location, @NotNull HeightMap heightMap) {
    return world.getHighestBlockYAt(location, heightMap);
  }

  @NotNull
  public MoonPhase getMoonPhase() {
    return world.getMoonPhase();
  }

  @NotNull
  public NamespacedKey getKey() {
    return world.getKey();
  }

  public boolean lineOfSightExists(@NotNull Location from, @NotNull Location to) {
    return world.lineOfSightExists(from, to);
  }

  public boolean hasCollisionsIn(@NotNull BoundingBox boundingBox) {
    return world.hasCollisionsIn(boundingBox);
  }

  @NotNull
  public Key key() {
    return world.key();
  }

  @NotNull
  public String getName() {
    return world.getName();
  }

  @NotNull
  public UUID getUID() {
    return world.getUID();
  }

  @NotNull
  public World.Environment getEnvironment() {
    return world.getEnvironment();
  }

  public long getSeed() {
    return world.getSeed();
  }

  public int getMinHeight() {
    return world.getMinHeight();
  }

  public int getMaxHeight() {
    return world.getMaxHeight();
  }

  @NotNull
  public BiomeProvider vanillaBiomeProvider() {
    return world.vanillaBiomeProvider();
  }

  public void sendPluginMessage(@NotNull Plugin source, @NotNull String channel, @NotNull byte[] message) {
    world.sendPluginMessage(source, channel, message);
  }

  @NotNull
  public Set<String> getListeningPluginChannels() {
    return world.getListeningPluginChannels();
  }

  public void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue) {
    world.setMetadata(metadataKey, newMetadataValue);
  }

  @NotNull
  public List<MetadataValue> getMetadata(@NotNull String metadataKey) {
    return world.getMetadata(metadataKey);
  }

  public boolean hasMetadata(@NotNull String metadataKey) {
    return world.hasMetadata(metadataKey);
  }

  public void removeMetadata(@NotNull String metadataKey, @NotNull Plugin owningPlugin) {
    world.removeMetadata(metadataKey, owningPlugin);
  }

  @NotNull
  public PersistentDataContainer getPersistentDataContainer() {
    return world.getPersistentDataContainer();
  }

  @NotNull
  public Pointers pointers() {
    return world.pointers();
  }

  @NotNull
  public Audience filterAudience(@NotNull Predicate<? super Audience> filter) {
    return world.filterAudience(filter);
  }

  public void forEachAudience(java.util.function.@NotNull Consumer<? super Audience> action) {
    world.forEachAudience(action);
  }

  public void sendMessage(@NotNull Component message) {
    world.sendMessage(message);
  }

  public void sendMessage(@NotNull Component message, ChatType.@NotNull Bound boundChatType) {
    world.sendMessage(message, boundChatType);
  }

  public void sendMessage(@NotNull SignedMessage signedMessage, ChatType.@NotNull Bound boundChatType) {
    world.sendMessage(signedMessage, boundChatType);
  }

  public void deleteMessage(SignedMessage.@NotNull Signature signature) {
    world.deleteMessage(signature);
  }

  public void sendActionBar(@NotNull Component message) {
    world.sendActionBar(message);
  }

  public void sendPlayerListHeader(@NotNull Component header) {
    world.sendPlayerListHeader(header);
  }

  public void sendPlayerListFooter(@NotNull Component footer) {
    world.sendPlayerListFooter(footer);
  }

  public void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
    world.sendPlayerListHeaderAndFooter(header, footer);
  }

  public <T> void sendTitlePart(@NotNull TitlePart<T> part, @NotNull T value) {
    world.sendTitlePart(part, value);
  }

  public void clearTitle() {
    world.clearTitle();
  }

  public void resetTitle() {
    world.resetTitle();
  }

  public void showBossBar(@NotNull BossBar bar) {
    world.showBossBar(bar);
  }

  public void hideBossBar(@NotNull BossBar bar) {
    world.hideBossBar(bar);
  }

  public void playSound(net.kyori.adventure.sound.@NotNull Sound sound) {
    world.playSound(sound);
  }

  public void playSound(net.kyori.adventure.sound.@NotNull Sound sound, double x, double y, double z) {
    world.playSound(sound, x, y, z);
  }

  public void playSound(net.kyori.adventure.sound.@NotNull Sound sound, net.kyori.adventure.sound.Sound.@NotNull Emitter emitter) {
    world.playSound(sound, emitter);
  }

  public void stopSound(@NotNull SoundStop stop) {
    world.stopSound(stop);
  }

  public void openBook(@NotNull Book book) {
    world.openBook(book);
  }

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

  @NotNull
  public static Collector<? super Audience, ?, ForwardingAudience> toAudience() {
    return Audience.toAudience();
  }

  public void sendMessage(@NotNull ComponentLike message) {
    world.sendMessage(message);
  }

  @Deprecated
  public void sendMessage(@NotNull Identified source, @NotNull ComponentLike message) {
    world.sendMessage(source, message);
  }

  @Deprecated
  public void sendMessage(@NotNull Identity source, @NotNull ComponentLike message) {
    world.sendMessage(source, message);
  }

  @Deprecated
  public void sendMessage(@NotNull Identified source, @NotNull Component message) {
    world.sendMessage(source, message);
  }

  @Deprecated
  public void sendMessage(@NotNull Identity source, @NotNull Component message) {
    world.sendMessage(source, message);
  }

  public void sendMessage(@NotNull ComponentLike message, ChatType.@NotNull Bound boundChatType) {
    world.sendMessage(message, boundChatType);
  }

  public void deleteMessage(@NotNull SignedMessage signedMessage) {
    world.deleteMessage(signedMessage);
  }

  public void sendActionBar(@NotNull ComponentLike message) {
    world.sendActionBar(message);
  }

  public void sendPlayerListHeader(@NotNull ComponentLike header) {
    world.sendPlayerListHeader(header);
  }

  public void sendPlayerListFooter(@NotNull ComponentLike footer) {
    world.sendPlayerListFooter(footer);
  }

  public void sendPlayerListHeaderAndFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
    world.sendPlayerListHeaderAndFooter(header, footer);
  }

  public void showTitle(@NotNull Title title) {
    world.showTitle(title);
  }

  public void stopSound(net.kyori.adventure.sound.@NotNull Sound sound) {
    world.stopSound(sound);
  }

  public void openBook(Book.@NotNull Builder book) {
    world.openBook(book);
  }

  @NotNull
  public <T> Optional<T> get(@NotNull Pointer<T> pointer) {
    return world.get(pointer);
  }

  @Contract("_, null -> _; _, !null -> !null")
  public <T> @Nullable T getOrDefault(@NotNull Pointer<T> pointer, @Nullable T defaultValue) {
    return world.getOrDefault(pointer, defaultValue);
  }

  public <T> @UnknownNullability T getOrDefaultFrom(@NotNull Pointer<T> pointer, @NotNull Supplier<? extends T> defaultValue) {
    return world.getOrDefaultFrom(pointer, defaultValue);
  }
}
