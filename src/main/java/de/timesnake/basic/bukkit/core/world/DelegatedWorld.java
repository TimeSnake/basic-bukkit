/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.core.world;

import com.destroystokyo.paper.HeightmapType;
import io.papermc.paper.world.MoonPhase;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.bossbar.BossBar;
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

public class DelegatedWorld implements World {

    @NotNull
    public static Audience empty() {return Audience.empty();}

    @NotNull
    public static Audience audience(@NotNull Audience @NotNull ... audiences) {return Audience.audience(audiences);}

    public static @NotNull ForwardingAudience audience(@NotNull Iterable<? extends Audience> audiences) {return Audience.audience(audiences);}

    public static @NotNull Collector<? super Audience, ?, ForwardingAudience> toAudience() {return Audience.toAudience();}

    protected World world;

    public DelegatedWorld(World world) {
        this.world = world;
    }

    @Override
    public @Nullable StructureSearchResult locateNearestStructure(@NotNull Location origin,
                                                                  org.bukkit.generator.structure.@NotNull StructureType structureType, int radius, boolean findUnexplored) {
        return world.locateNearestStructure(origin, structureType, radius, findUnexplored);
    }

    @Override
    public @Nullable StructureSearchResult locateNearestStructure(@NotNull Location origin,
                                                                  @NotNull Structure structure, int radius,
                                                                  boolean findUnexplored) {
        return world.locateNearestStructure(origin, structure, radius, findUnexplored);
    }

    @Override
    public int getEntityCount() {return world.getEntityCount();}

    @Override
    public int getTileEntityCount() {return world.getTileEntityCount();}

    @Override
    public int getTickableTileEntityCount() {return world.getTickableTileEntityCount();}

    @Override
    public int getChunkCount() {return world.getChunkCount();}

    @Override
    public int getPlayerCount() {return world.getPlayerCount();}

    @Override
    public @NotNull Block getBlockAt(int x, int y, int z) {return world.getBlockAt(x, y, z);}

    @Override
    public @NotNull Block getBlockAt(@NotNull Location location) {return world.getBlockAt(location);}

    @Override
    @Deprecated
    public @NotNull Block getBlockAtKey(long key) {return world.getBlockAtKey(key);}

    @Override
    public @NotNull Location getLocationAtKey(long key) {return world.getLocationAtKey(key);}

    @Override
    public int getHighestBlockYAt(int x, int z) {return world.getHighestBlockYAt(x, z);}

    @Override
    public int getHighestBlockYAt(@NotNull Location location) {return world.getHighestBlockYAt(location);}

    @Override
    public @NotNull Block getHighestBlockAt(int x, int z) {return world.getHighestBlockAt(x, z);}

    @Override
    public @NotNull Block getHighestBlockAt(@NotNull Location location) {return world.getHighestBlockAt(location);}

    @Override
    @Deprecated
    public int getHighestBlockYAt(int x, int z, @NotNull HeightmapType heightmap) throws UnsupportedOperationException {return world.getHighestBlockYAt(x, z, heightmap);}

    @Override
    @Deprecated
    public int getHighestBlockYAt(@NotNull Location location, @NotNull HeightmapType heightmap) throws UnsupportedOperationException {return world.getHighestBlockYAt(location, heightmap);}

    @Override
    @Deprecated
    public @NotNull Block getHighestBlockAt(int x, int z, @NotNull HeightmapType heightmap) throws UnsupportedOperationException {return world.getHighestBlockAt(x, z, heightmap);}

    @Override
    @Deprecated
    public @NotNull Block getHighestBlockAt(@NotNull Location location, @NotNull HeightmapType heightmap) throws UnsupportedOperationException {return world.getHighestBlockAt(location, heightmap);}

    @Override
    public int getHighestBlockYAt(int x, int z, @NotNull HeightMap heightMap) {
        return world.getHighestBlockYAt(x, z,
                heightMap);
    }

    @Override
    public int getHighestBlockYAt(@NotNull Location location, @NotNull HeightMap heightMap) {return world.getHighestBlockYAt(location, heightMap);}

    @Override
    public @NotNull Block getHighestBlockAt(int x, int z, @NotNull HeightMap heightMap) {return world.getHighestBlockAt(x, z, heightMap);}

    @Override
    public @NotNull Block getHighestBlockAt(@NotNull Location location, @NotNull HeightMap heightMap) {return world.getHighestBlockAt(location, heightMap);}

    @Override
    public @NotNull Chunk getChunkAt(int x, int z) {return world.getChunkAt(x, z);}

    @Override
    public @NotNull Chunk getChunkAt(@NotNull Location location) {return world.getChunkAt(location);}

    @Override
    public @NotNull Chunk getChunkAt(@NotNull Block block) {return world.getChunkAt(block);}

    @Override
    public @NotNull Chunk getChunkAt(long chunkKey) {return world.getChunkAt(chunkKey);}

    @Override
    public boolean isChunkGenerated(long chunkKey) {return world.isChunkGenerated(chunkKey);}

    @Override
    public boolean isChunkLoaded(@NotNull Chunk chunk) {return world.isChunkLoaded(chunk);}

    @Override
    public @NotNull Chunk[] getLoadedChunks() {return world.getLoadedChunks();}

    @Override
    public void loadChunk(@NotNull Chunk chunk) {world.loadChunk(chunk);}

    @Override
    public boolean isChunkLoaded(int x, int z) {return world.isChunkLoaded(x, z);}

    @Override
    public boolean isChunkGenerated(int x, int z) {return world.isChunkGenerated(x, z);}

    @Override
    @Deprecated
    public boolean isChunkInUse(int x, int z) {return world.isChunkInUse(x, z);}

    @Override
    public void loadChunk(int x, int z) {world.loadChunk(x, z);}

    @Override
    public boolean loadChunk(int x, int z, boolean generate) {return world.loadChunk(x, z, generate);}

    @Override
    public boolean unloadChunk(@NotNull Chunk chunk) {return world.unloadChunk(chunk);}

    @Override
    public boolean unloadChunk(int x, int z) {return world.unloadChunk(x, z);}

    @Override
    public boolean unloadChunk(int x, int z, boolean save) {return world.unloadChunk(x, z, save);}

    @Override
    public boolean unloadChunkRequest(int x, int z) {return world.unloadChunkRequest(x, z);}

    @Override
    @Deprecated
    public boolean regenerateChunk(int x, int z) {return world.regenerateChunk(x, z);}

    @Override
    public boolean refreshChunk(int x, int z) {return world.refreshChunk(x, z);}

    @Override
    public boolean isChunkForceLoaded(int x, int z) {return world.isChunkForceLoaded(x, z);}

    @Override
    public void setChunkForceLoaded(int x, int z, boolean forced) {world.setChunkForceLoaded(x, z, forced);}

    @Override
    public @NotNull Collection<Chunk> getForceLoadedChunks() {return world.getForceLoadedChunks();}

    @Override
    public boolean addPluginChunkTicket(int x, int z, @NotNull Plugin plugin) {
        return world.addPluginChunkTicket(x, z
                , plugin);
    }

    @Override
    public boolean removePluginChunkTicket(int x, int z, @NotNull Plugin plugin) {return world.removePluginChunkTicket(x, z, plugin);}

    @Override
    public void removePluginChunkTickets(@NotNull Plugin plugin) {world.removePluginChunkTickets(plugin);}

    @Override
    public @NotNull Collection<Plugin> getPluginChunkTickets(int x, int z) {return world.getPluginChunkTickets(x, z);}

    @Override
    public @NotNull Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {return world.getPluginChunkTickets();}

    @Override
    public @NotNull Item dropItem(@NotNull Location location, @NotNull ItemStack item) {return world.dropItem(location, item);}

    @Override
    public @NotNull Item dropItem(@NotNull Location location, @NotNull ItemStack item,
                                  @Nullable Consumer<Item> function) {return world.dropItem(location, item, function);}

    @Override
    public @NotNull Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack item) {return world.dropItemNaturally(location, item);}

    @Override
    public @NotNull Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack item,
                                           @Nullable Consumer<Item> function) {return world.dropItemNaturally(location, item, function);}

    @Override
    public @NotNull Arrow spawnArrow(@NotNull Location location, @NotNull Vector direction, float speed,
                                     float spread) {return world.spawnArrow(location, direction, speed, spread);}

    @Override
    public <T extends AbstractArrow> @NotNull T spawnArrow(@NotNull Location location, @NotNull Vector direction,
                                                           float speed, float spread, @NotNull Class<T> clazz) {return world.spawnArrow(location, direction, speed, spread, clazz);}

    @Override
    public boolean generateTree(@NotNull Location location, @NotNull TreeType type) {return world.generateTree(location, type);}

    @Override
    @Deprecated
    public boolean generateTree(@NotNull Location loc, @NotNull TreeType type, @NotNull BlockChangeDelegate delegate) {return world.generateTree(loc, type, delegate);}

    @Override
    public @NotNull LightningStrike strikeLightning(@NotNull Location loc) {return world.strikeLightning(loc);}

    @Override
    public @NotNull LightningStrike strikeLightningEffect(@NotNull Location loc) {return world.strikeLightningEffect(loc);}

    @Override
    public @Nullable Location findLightningRod(@NotNull Location location) {return world.findLightningRod(location);}

    @Override
    public @Nullable Location findLightningTarget(@NotNull Location location) {return world.findLightningTarget(location);}

    @Override
    public @NotNull List<Entity> getEntities() {return world.getEntities();}

    @Override
    public @NotNull List<LivingEntity> getLivingEntities() {return world.getLivingEntities();}

    @Override
    @Deprecated
    public @NotNull <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T>... classes) {return world.getEntitiesByClass(classes);}

    @Override
    public @NotNull <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T> cls) {return world.getEntitiesByClass(cls);}

    @Override
    public @NotNull Collection<Entity> getEntitiesByClasses(@NotNull Class<?>... classes) {return world.getEntitiesByClasses(classes);}

    @Override
    public @NotNull Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double radius) {return world.getNearbyLivingEntities(loc, radius);}

    @Override
    public @NotNull Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double xzRadius,
                                                                     double yRadius) {return world.getNearbyLivingEntities(loc, xzRadius, yRadius);}

    @Override
    public @NotNull Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double xRadius,
                                                                     double yRadius, double zRadius) {return world.getNearbyLivingEntities(loc, xRadius, yRadius, zRadius);}

    @Override
    public @NotNull Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double radius,
                                                                     @Nullable Predicate<LivingEntity> predicate) {return world.getNearbyLivingEntities(loc, radius, predicate);}

    @Override
    public @NotNull Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double xzRadius,
                                                                     double yRadius,
                                                                     @Nullable Predicate<LivingEntity> predicate) {return world.getNearbyLivingEntities(loc, xzRadius, yRadius, predicate);}

    @Override
    public @NotNull Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double xRadius,
                                                                     double yRadius, double zRadius,
                                                                     @Nullable Predicate<LivingEntity> predicate) {return world.getNearbyLivingEntities(loc, xRadius, yRadius, zRadius, predicate);}

    @Override
    public @NotNull Collection<Player> getNearbyPlayers(@NotNull Location loc, double radius) {return world.getNearbyPlayers(loc, radius);}

    @Override
    public @NotNull Collection<Player> getNearbyPlayers(@NotNull Location loc, double xzRadius, double yRadius) {return world.getNearbyPlayers(loc, xzRadius, yRadius);}

    @Override
    public @NotNull Collection<Player> getNearbyPlayers(@NotNull Location loc, double xRadius, double yRadius,
                                                        double zRadius) {
        return world.getNearbyPlayers(loc, xRadius,
                yRadius, zRadius);
    }

    @Override
    public @NotNull Collection<Player> getNearbyPlayers(@NotNull Location loc, double radius,
                                                        @Nullable Predicate<Player> predicate) {return world.getNearbyPlayers(loc, radius, predicate);}

    @Override
    public @NotNull Collection<Player> getNearbyPlayers(@NotNull Location loc, double xzRadius, double yRadius,
                                                        @Nullable Predicate<Player> predicate) {return world.getNearbyPlayers(loc, xzRadius, yRadius, predicate);}

    @Override
    public @NotNull Collection<Player> getNearbyPlayers(@NotNull Location loc, double xRadius, double yRadius,
                                                        double zRadius, @Nullable Predicate<Player> predicate) {return world.getNearbyPlayers(loc, xRadius, yRadius, zRadius, predicate);}

    @Override
    public @NotNull <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz,
                                                                             @NotNull Location loc, double radius) {return world.getNearbyEntitiesByType(clazz, loc, radius);}

    @Override
    public @NotNull <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz,
                                                                             @NotNull Location loc, double xzRadius,
                                                                             double yRadius) {return world.getNearbyEntitiesByType(clazz, loc, xzRadius, yRadius);}

    @Override
    public @NotNull <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz,
                                                                             @NotNull Location loc, double xRadius,
                                                                             double yRadius, double zRadius) {return world.getNearbyEntitiesByType(clazz, loc, xRadius, yRadius, zRadius);}

    @Override
    public @NotNull <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz,
                                                                             @NotNull Location loc, double radius,
                                                                             @Nullable Predicate<T> predicate) {return world.getNearbyEntitiesByType(clazz, loc, radius, predicate);}

    @Override
    public @NotNull <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz,
                                                                             @NotNull Location loc, double xzRadius,
                                                                             double yRadius,
                                                                             @Nullable Predicate<T> predicate) {return world.getNearbyEntitiesByType(clazz, loc, xzRadius, yRadius, predicate);}

    @Override
    public @NotNull <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends Entity> clazz,
                                                                             @NotNull Location loc, double xRadius,
                                                                             double yRadius, double zRadius,
                                                                             @Nullable Predicate<T> predicate) {return world.getNearbyEntitiesByType(clazz, loc, xRadius, yRadius, zRadius, predicate);}

    @Override
    @Deprecated
    public void getChunkAtAsync(int x, int z, @NotNull ChunkLoadCallback cb) {world.getChunkAtAsync(x, z, cb);}

    @Override
    @Deprecated
    public void getChunkAtAsync(@NotNull Location loc, @NotNull ChunkLoadCallback cb) {world.getChunkAtAsync(loc, cb);}

    @Override
    @Deprecated
    public void getChunkAtAsync(@NotNull Block block, @NotNull ChunkLoadCallback cb) {world.getChunkAtAsync(block, cb);}

    @Override
    public void getChunkAtAsync(int x, int z, java.util.function.@NotNull Consumer<Chunk> cb) {world.getChunkAtAsync(x, z, cb);}

    @Override
    public void getChunkAtAsync(int x, int z, boolean gen, java.util.function.@NotNull Consumer<Chunk> cb) {world.getChunkAtAsync(x, z, gen, cb);}

    @Override
    public void getChunkAtAsync(@NotNull Location loc, java.util.function.@NotNull Consumer<Chunk> cb) {world.getChunkAtAsync(loc, cb);}

    @Override
    public void getChunkAtAsync(@NotNull Location loc, boolean gen, java.util.function.@NotNull Consumer<Chunk> cb) {world.getChunkAtAsync(loc, gen, cb);}

    @Override
    public void getChunkAtAsync(@NotNull Block block, java.util.function.@NotNull Consumer<Chunk> cb) {world.getChunkAtAsync(block, cb);}

    @Override
    public void getChunkAtAsync(@NotNull Block block, boolean gen, java.util.function.@NotNull Consumer<Chunk> cb) {world.getChunkAtAsync(block, gen, cb);}

    @Override
    public @NotNull CompletableFuture<Chunk> getChunkAtAsync(@NotNull Location loc) {return world.getChunkAtAsync(loc);}

    @Override
    public @NotNull CompletableFuture<Chunk> getChunkAtAsync(@NotNull Location loc, boolean gen) {return world.getChunkAtAsync(loc, gen);}

    @Override
    public @NotNull CompletableFuture<Chunk> getChunkAtAsync(@NotNull Block block) {return world.getChunkAtAsync(block);}

    @Override
    public @NotNull CompletableFuture<Chunk> getChunkAtAsync(@NotNull Block block, boolean gen) {return world.getChunkAtAsync(block, gen);}

    @Override
    public @NotNull CompletableFuture<Chunk> getChunkAtAsync(int x, int z) {return world.getChunkAtAsync(x, z);}

    @Override
    public @NotNull CompletableFuture<Chunk> getChunkAtAsync(int x, int z, boolean gen) {return world.getChunkAtAsync(x, z, gen);}

    @Override
    public @NotNull CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Location loc) {return world.getChunkAtAsyncUrgently(loc);}

    @Override
    public @NotNull CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Location loc, boolean gen) {return world.getChunkAtAsyncUrgently(loc, gen);}

    @Override
    public @NotNull CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Block block) {return world.getChunkAtAsyncUrgently(block);}

    @Override
    public @NotNull CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Block block, boolean gen) {return world.getChunkAtAsyncUrgently(block, gen);}

    @Override
    public @NotNull CompletableFuture<Chunk> getChunkAtAsyncUrgently(int x, int z) {return world.getChunkAtAsyncUrgently(x, z);}

    @Override
    public @NotNull CompletableFuture<Chunk> getChunkAtAsync(int x, int z, boolean gen, boolean urgent) {return world.getChunkAtAsync(x, z, gen, urgent);}

    @Override
    public @NotNull List<Player> getPlayers() {return world.getPlayers();}

    @NotNull
    @Override
    public Iterable<? extends Audience> audiences() {return world.audiences();}

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(@NotNull Location location, double x, double y, double z) {return world.getNearbyEntities(location, x, y, z);}

    @Override
    public @Nullable Entity getEntity(@NotNull UUID uuid) {return world.getEntity(uuid);}

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(@NotNull Location location, double x, double y, double z,
                                                         @Nullable Predicate<Entity> filter) {return world.getNearbyEntities(location, x, y, z, filter);}

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox) {return world.getNearbyEntities(boundingBox);}

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox,
                                                         @Nullable Predicate<Entity> filter) {return world.getNearbyEntities(boundingBox, filter);}

    @Override
    public @Nullable RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction,
                                                     double maxDistance) {
        return world.rayTraceEntities(start,
                direction, maxDistance);
    }

    @Override
    public @Nullable RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction,
                                                     double maxDistance, double raySize) {return world.rayTraceEntities(start, direction, maxDistance, raySize);}

    @Override
    public @Nullable RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction,
                                                     double maxDistance, @Nullable Predicate<Entity> filter) {return world.rayTraceEntities(start, direction, maxDistance, filter);}

    @Override
    public @Nullable RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction,
                                                     double maxDistance, double raySize,
                                                     @Nullable Predicate<Entity> filter) {return world.rayTraceEntities(start, direction, maxDistance, raySize, filter);}

    @Override
    public @Nullable RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction,
                                                   double maxDistance) {
        return world.rayTraceBlocks(start, direction,
                maxDistance);
    }

    @Override
    public @Nullable RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction,
                                                   double maxDistance,
                                                   @NotNull FluidCollisionMode fluidCollisionMode) {return world.rayTraceBlocks(start, direction, maxDistance, fluidCollisionMode);}

    @Override
    public @Nullable RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction,
                                                   double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode
            , boolean ignorePassableBlocks) {
        return world.rayTraceBlocks(start, direction, maxDistance,
                fluidCollisionMode, ignorePassableBlocks);
    }

    @Override
    public @Nullable RayTraceResult rayTrace(@NotNull Location start, @NotNull Vector direction, double maxDistance,
                                             @NotNull FluidCollisionMode fluidCollisionMode,
                                             boolean ignorePassableBlocks, double raySize,
                                             @Nullable Predicate<Entity> filter) {
        return world.rayTrace(start,
                direction, maxDistance, fluidCollisionMode, ignorePassableBlocks, raySize, filter);
    }

    @Override
    public @NotNull Location getSpawnLocation() {return world.getSpawnLocation();}

    @Override
    public boolean setSpawnLocation(@NotNull Location location) {return world.setSpawnLocation(location);}

    @Override
    public boolean setSpawnLocation(int x, int y, int z, float angle) {return world.setSpawnLocation(x, y, z, angle);}

    @Override
    public boolean setSpawnLocation(int x, int y, int z) {return world.setSpawnLocation(x, y, z);}

    @Override
    public long getTime() {return world.getTime();}

    @Override
    public void setTime(long time) {world.setTime(time);}

    @Override
    public long getFullTime() {return world.getFullTime();}

    @Override
    public void setFullTime(long time) {world.setFullTime(time);}

    @Override
    public boolean isDayTime() {return world.isDayTime();}

    @Override
    public long getGameTime() {return world.getGameTime();}

    @Override
    public boolean hasStorm() {return world.hasStorm();}

    @Override
    public void setStorm(boolean hasStorm) {world.setStorm(hasStorm);}

    @Override
    public int getWeatherDuration() {return world.getWeatherDuration();}

    @Override
    public void setWeatherDuration(int duration) {world.setWeatherDuration(duration);}

    @Override
    public boolean isThundering() {return world.isThundering();}

    @Override
    public void setThundering(boolean thundering) {world.setThundering(thundering);}

    @Override
    public int getThunderDuration() {return world.getThunderDuration();}

    @Override
    public void setThunderDuration(int duration) {world.setThunderDuration(duration);}

    @Override
    public boolean isClearWeather() {return world.isClearWeather();}

    @Override
    public int getClearWeatherDuration() {return world.getClearWeatherDuration();}

    @Override
    public void setClearWeatherDuration(int duration) {world.setClearWeatherDuration(duration);}

    @Override
    public boolean createExplosion(double x, double y, double z, float power) {
        return world.createExplosion(x, y, z,
                power);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {return world.createExplosion(x, y, z, power, setFire);}

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {return world.createExplosion(x, y, z, power, setFire, breakBlocks);}

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks,
                                   @Nullable Entity source) {
        return world.createExplosion(x, y, z, power, setFire,
                breakBlocks, source);
    }

    @Override
    public boolean createExplosion(@NotNull Location loc, float power) {return world.createExplosion(loc, power);}

    @Override
    public boolean createExplosion(@NotNull Location loc, float power, boolean setFire) {return world.createExplosion(loc, power, setFire);}

    @Override
    public boolean createExplosion(@Nullable Entity source, @NotNull Location loc, float power, boolean setFire,
                                   boolean breakBlocks) {
        return world.createExplosion(source, loc, power, setFire,
                breakBlocks);
    }

    @Override
    public boolean createExplosion(@Nullable Entity source, @NotNull Location loc, float power, boolean setFire) {return world.createExplosion(source, loc, power, setFire);}

    @Override
    public boolean createExplosion(@Nullable Entity source, @NotNull Location loc, float power) {return world.createExplosion(source, loc, power);}

    @Override
    public boolean createExplosion(@NotNull Entity source, float power, boolean setFire, boolean breakBlocks) {return world.createExplosion(source, power, setFire, breakBlocks);}

    @Override
    public boolean createExplosion(@NotNull Entity source, float power, boolean setFire) {return world.createExplosion(source, power, setFire);}

    @Override
    public boolean createExplosion(@NotNull Entity source, float power) {return world.createExplosion(source, power);}

    @Override
    public boolean createExplosion(@NotNull Location loc, float power, boolean setFire, boolean breakBlocks) {return world.createExplosion(loc, power, setFire, breakBlocks);}

    @Override
    public boolean createExplosion(@NotNull Location loc, float power, boolean setFire, boolean breakBlocks,
                                   @Nullable Entity source) {
        return world.createExplosion(loc, power, setFire,
                breakBlocks, source);
    }

    @Override
    public boolean getPVP() {return world.getPVP();}

    @Override
    public void setPVP(boolean pvp) {world.setPVP(pvp);}

    @Override
    public @Nullable ChunkGenerator getGenerator() {return world.getGenerator();}

    @Override
    public @Nullable BiomeProvider getBiomeProvider() {return world.getBiomeProvider();}

    @Override
    public void save() {world.save();}

    @Override
    public @NotNull List<BlockPopulator> getPopulators() {return world.getPopulators();}

    @Override
    @Deprecated
    public @NotNull FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull MaterialData data) throws IllegalArgumentException {return world.spawnFallingBlock(location, data);}

    @Override
    public @NotNull FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull BlockData data) throws IllegalArgumentException {return world.spawnFallingBlock(location, data);}

    @Override
    @Deprecated
    public @NotNull FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull Material material, byte data) throws IllegalArgumentException {return world.spawnFallingBlock(location, material, data);}

    @Override
    public void playEffect(@NotNull Location location, @NotNull Effect effect, int data) {
        world.playEffect(location,
                effect, data);
    }

    @Override
    public void playEffect(@NotNull Location location, @NotNull Effect effect, int data, int radius) {world.playEffect(location, effect, data, radius);}

    @Override
    public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T data) {world.playEffect(location, effect, data);}

    @Override
    public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T data, int radius) {world.playEffect(location, effect, data, radius);}

    @Override
    public @NotNull ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTemp) {return world.getEmptyChunkSnapshot(x, z, includeBiome, includeBiomeTemp);}

    @Override
    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
        world.setSpawnFlags(allowMonsters,
                allowAnimals);
    }

    @Override
    public boolean getAllowAnimals() {return world.getAllowAnimals();}

    @Override
    public boolean getAllowMonsters() {return world.getAllowMonsters();}

    @Override
    @Deprecated
    public @NotNull Biome getBiome(int x, int z) {return world.getBiome(x, z);}

    @Override
    @Deprecated
    public void setBiome(int x, int z, @NotNull Biome bio) {world.setBiome(x, z, bio);}

    @Override
    @Deprecated
    public double getTemperature(int x, int z) {return world.getTemperature(x, z);}

    @Override
    public double getTemperature(int x, int y, int z) {return world.getTemperature(x, y, z);}

    @Override
    @Deprecated
    public double getHumidity(int x, int z) {return world.getHumidity(x, z);}

    @Override
    public double getHumidity(int x, int y, int z) {return world.getHumidity(x, y, z);}

    @Override
    public int getLogicalHeight() {return world.getLogicalHeight();}

    @Override
    public boolean isNatural() {return world.isNatural();}

    @Override
    public boolean isBedWorks() {return world.isBedWorks();}

    @Override
    public boolean hasSkyLight() {return world.hasSkyLight();}

    @Override
    public boolean hasCeiling() {return world.hasCeiling();}

    @Override
    public boolean isPiglinSafe() {return world.isPiglinSafe();}

    @Override
    public boolean isRespawnAnchorWorks() {return world.isRespawnAnchorWorks();}

    @Override
    public boolean hasRaids() {return world.hasRaids();}

    @Override
    public boolean isUltraWarm() {return world.isUltraWarm();}

    @Override
    public int getSeaLevel() {return world.getSeaLevel();}

    @Override
    public boolean getKeepSpawnInMemory() {return world.getKeepSpawnInMemory();}

    @Override
    public void setKeepSpawnInMemory(boolean keepLoaded) {world.setKeepSpawnInMemory(keepLoaded);}

    @Override
    public boolean isAutoSave() {return world.isAutoSave();}

    @Override
    public void setAutoSave(boolean value) {world.setAutoSave(value);}

    @Override
    public @NotNull Difficulty getDifficulty() {return world.getDifficulty();}

    @Override
    public void setDifficulty(@NotNull Difficulty difficulty) {world.setDifficulty(difficulty);}

    @Override
    public @NotNull File getWorldFolder() {return world.getWorldFolder();}

    @Override
    @Deprecated
    public @Nullable WorldType getWorldType() {return world.getWorldType();}

    @Override
    public boolean canGenerateStructures() {return world.canGenerateStructures();}

    @Override
    public boolean isHardcore() {return world.isHardcore();}

    @Override
    public void setHardcore(boolean hardcore) {world.setHardcore(hardcore);}

    @Override
    @Deprecated
    public long getTicksPerAnimalSpawns() {return world.getTicksPerAnimalSpawns();}

    @Override
    @Deprecated
    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {world.setTicksPerAnimalSpawns(ticksPerAnimalSpawns);}

    @Override
    @Deprecated
    public long getTicksPerMonsterSpawns() {return world.getTicksPerMonsterSpawns();}

    @Override
    @Deprecated
    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {world.setTicksPerMonsterSpawns(ticksPerMonsterSpawns);}

    @Override
    @Deprecated
    public long getTicksPerWaterSpawns() {return world.getTicksPerWaterSpawns();}

    @Override
    @Deprecated
    public void setTicksPerWaterSpawns(int ticksPerWaterSpawns) {world.setTicksPerWaterSpawns(ticksPerWaterSpawns);}

    @Override
    @Deprecated
    public long getTicksPerWaterAmbientSpawns() {return world.getTicksPerWaterAmbientSpawns();}

    @Override
    @Deprecated
    public void setTicksPerWaterAmbientSpawns(int ticksPerAmbientSpawns) {world.setTicksPerWaterAmbientSpawns(ticksPerAmbientSpawns);}

    @Override
    @Deprecated
    public long getTicksPerWaterUndergroundCreatureSpawns() {return world.getTicksPerWaterUndergroundCreatureSpawns();}

    @Override
    @Deprecated
    public void setTicksPerWaterUndergroundCreatureSpawns(int ticksPerWaterUndergroundCreatureSpawns) {world.setTicksPerWaterUndergroundCreatureSpawns(ticksPerWaterUndergroundCreatureSpawns);}

    @Override
    @Deprecated
    public long getTicksPerAmbientSpawns() {return world.getTicksPerAmbientSpawns();}

    @Override
    @Deprecated
    public void setTicksPerAmbientSpawns(int ticksPerAmbientSpawns) {world.setTicksPerAmbientSpawns(ticksPerAmbientSpawns);}

    @Override
    public long getTicksPerSpawns(@NotNull SpawnCategory spawnCategory) {return world.getTicksPerSpawns(spawnCategory);}

    @Override
    public void setTicksPerSpawns(@NotNull SpawnCategory spawnCategory, int ticksPerCategorySpawn) {world.setTicksPerSpawns(spawnCategory, ticksPerCategorySpawn);}

    @Override
    @Deprecated
    public int getMonsterSpawnLimit() {return world.getMonsterSpawnLimit();}

    @Override
    @Deprecated
    public void setMonsterSpawnLimit(int limit) {world.setMonsterSpawnLimit(limit);}

    @Override
    @Deprecated
    public int getAnimalSpawnLimit() {return world.getAnimalSpawnLimit();}

    @Override
    @Deprecated
    public void setAnimalSpawnLimit(int limit) {world.setAnimalSpawnLimit(limit);}

    @Override
    @Deprecated
    public int getWaterAnimalSpawnLimit() {return world.getWaterAnimalSpawnLimit();}

    @Override
    @Deprecated
    public void setWaterAnimalSpawnLimit(int limit) {world.setWaterAnimalSpawnLimit(limit);}

    @Override
    @Deprecated
    public int getWaterUndergroundCreatureSpawnLimit() {return world.getWaterUndergroundCreatureSpawnLimit();}

    @Override
    @Deprecated
    public void setWaterUndergroundCreatureSpawnLimit(int limit) {world.setWaterUndergroundCreatureSpawnLimit(limit);}

    @Override
    @Deprecated
    public int getWaterAmbientSpawnLimit() {return world.getWaterAmbientSpawnLimit();}

    @Override
    @Deprecated
    public void setWaterAmbientSpawnLimit(int limit) {world.setWaterAmbientSpawnLimit(limit);}

    @Override
    @Deprecated
    public int getAmbientSpawnLimit() {return world.getAmbientSpawnLimit();}

    @Override
    @Deprecated
    public void setAmbientSpawnLimit(int limit) {world.setAmbientSpawnLimit(limit);}

    @Override
    public int getSpawnLimit(@NotNull SpawnCategory spawnCategory) {return world.getSpawnLimit(spawnCategory);}

    @Override
    public void setSpawnLimit(@NotNull SpawnCategory spawnCategory, int limit) {
        world.setSpawnLimit(spawnCategory,
                limit);
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull Sound sound, float volume, float pitch) {world.playSound(location, sound, volume, pitch);}

    @Override
    public void playSound(@NotNull Location location, @NotNull String sound, float volume, float pitch) {world.playSound(location, sound, volume, pitch);}

    @Override
    public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory category,
                          float volume, float pitch) {world.playSound(location, sound, category, volume, pitch);}

    @Override
    public void playSound(@NotNull Location location, @NotNull String sound, @NotNull SoundCategory category,
                          float volume, float pitch) {world.playSound(location, sound, category, volume, pitch);}

    @Override
    public void playSound(@NotNull Entity entity, @NotNull Sound sound, float volume, float pitch) {world.playSound(entity, sound, volume, pitch);}

    @Override
    public void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory category, float volume
            , float pitch) {world.playSound(entity, sound, category, volume, pitch);}

    @NotNull
    @Override
    public String[] getGameRules() {return world.getGameRules();}

    @Override
    @Contract("null -> null; !null -> !null")
    @Deprecated
    @Nullable
    public String getGameRuleValue(@Nullable String rule) {return world.getGameRuleValue(rule);}

    @Override
    @Deprecated
    public boolean setGameRuleValue(@NotNull String rule, @NotNull String value) {
        return world.setGameRuleValue(rule,
                value);
    }

    @Override
    public boolean isGameRule(@NotNull String rule) {return world.isGameRule(rule);}

    @Override
    public <T> @Nullable T getGameRuleValue(@NotNull GameRule<T> rule) {return world.getGameRuleValue(rule);}

    @Override
    public <T> @Nullable T getGameRuleDefault(@NotNull GameRule<T> rule) {return world.getGameRuleDefault(rule);}

    @Override
    public <T> boolean setGameRule(@NotNull GameRule<T> rule, @NotNull T newValue) {
        return world.setGameRule(rule,
                newValue);
    }

    @Override
    public @NotNull WorldBorder getWorldBorder() {return world.getWorldBorder();}

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count) {world.spawnParticle(particle, location, count);}

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count) {world.spawnParticle(particle, x, y, z, count);}

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count,
                                  @Nullable T data) {world.spawnParticle(particle, location, count, data);}

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count,
                                  @Nullable T data) {world.spawnParticle(particle, x, y, z, count, data);}

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX,
                              double offsetY, double offsetZ) {
        world.spawnParticle(particle, location, count, offsetX
                , offsetY, offsetZ);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX,
                              double offsetY, double offsetZ) {
        world.spawnParticle(particle, x, y, z, count, offsetX,
                offsetY, offsetZ);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX,
                                  double offsetY, double offsetZ, @Nullable T data) {
        world.spawnParticle(particle,
                location, count, offsetX, offsetY, offsetZ, data);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX
            , double offsetY, double offsetZ, @Nullable T data) {
        world.spawnParticle(particle, x, y, z, count,
                offsetX, offsetY, offsetZ, data);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX,
                              double offsetY, double offsetZ, double extra) {
        world.spawnParticle(particle, location,
                count, offsetX, offsetY, offsetZ, extra);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX,
                              double offsetY, double offsetZ, double extra) {
        world.spawnParticle(particle, x, y, z,
                count, offsetX, offsetY, offsetZ, extra);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX,
                                  double offsetY, double offsetZ, double extra, @Nullable T data) {world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, data);}

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX
            , double offsetY, double offsetZ, double extra, @Nullable T data) {
        world.spawnParticle(particle, x, y, z,
                count, offsetX, offsetY, offsetZ, extra, data);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @Nullable List<Player> receivers,
                                  @NotNull Player source, double x, double y, double z, int count, double offsetX,
                                  double offsetY, double offsetZ, double extra, @Nullable T data) {world.spawnParticle(particle, receivers, source, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);}

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @Nullable List<Player> receivers,
                                  @Nullable Player source, double x, double y, double z, int count, double offsetX,
                                  double offsetY, double offsetZ, double extra, @Nullable T data, boolean force) {world.spawnParticle(particle, receivers, source, x, y, z, count, offsetX, offsetY, offsetZ, extra, data, force);}

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX,
                                  double offsetY, double offsetZ, double extra, @Nullable T data, boolean force) {world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, data, force);}

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX
            , double offsetY, double offsetZ, double extra, @Nullable T data, boolean force) {world.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data, force);}

    @Override
    public @Nullable Location locateNearestStructure(@NotNull Location origin, @NotNull StructureType structureType,
                                                     int radius, boolean findUnexplored) {return world.locateNearestStructure(origin, structureType, radius, findUnexplored);}

    @Override
    public @Nullable Location locateNearestBiome(@NotNull Location origin, @NotNull Biome biome, int radius) {return world.locateNearestBiome(origin, biome, radius);}

    @Override
    public @Nullable Location locateNearestBiome(@NotNull Location origin, @NotNull Biome biome, int radius,
                                                 int step) {
        return world.locateNearestBiome(origin, biome, radius,
                step);
    }

    @Override
    @Deprecated
    public boolean isUltrawarm() {return world.isUltrawarm();}

    @Override
    public double getCoordinateScale() {return world.getCoordinateScale();}

    @Override
    @Deprecated
    public boolean hasSkylight() {return world.hasSkylight();}

    @Override
    @Deprecated
    public boolean hasBedrockCeiling() {return world.hasBedrockCeiling();}

    @Override
    @Deprecated
    public boolean doesBedWork() {return world.doesBedWork();}

    @Override
    @Deprecated
    public boolean doesRespawnAnchorWork() {return world.doesRespawnAnchorWork();}

    @Override
    public boolean isFixedTime() {return world.isFixedTime();}

    @Override
    public @NotNull Collection<Material> getInfiniburn() {return world.getInfiniburn();}

    @Override
    public void sendGameEvent(@Nullable Entity sourceEntity, @NotNull GameEvent gameEvent, @NotNull Vector position) {world.sendGameEvent(sourceEntity, gameEvent, position);}

    @Override
    public int getViewDistance() {return world.getViewDistance();}

    @Override
    public void setViewDistance(int viewDistance) {world.setViewDistance(viewDistance);}

    @Override
    public int getSimulationDistance() {return world.getSimulationDistance();}

    @Override
    public void setSimulationDistance(int simulationDistance) {world.setSimulationDistance(simulationDistance);}

    @Override
    @Deprecated
    public int getNoTickViewDistance() {return world.getNoTickViewDistance();}

    @Override
    @Deprecated
    public void setNoTickViewDistance(int viewDistance) {world.setNoTickViewDistance(viewDistance);}

    @Override
    public int getSendViewDistance() {return world.getSendViewDistance();}

    @Override
    public void setSendViewDistance(int viewDistance) {world.setSendViewDistance(viewDistance);}

    @NotNull
    @Override
    public Spigot spigot() {return world.spigot();}

    @Override
    public @Nullable Raid locateNearestRaid(@NotNull Location location, int radius) {return world.locateNearestRaid(location, radius);}

    @Override
    public @NotNull List<Raid> getRaids() {return world.getRaids();}

    @Override
    public @Nullable DragonBattle getEnderDragonBattle() {return world.getEnderDragonBattle();}

    @Override
    public @NotNull Biome getBiome(@NotNull Location location) {return world.getBiome(location);}

    @Override
    public @NotNull Biome getBiome(int x, int y, int z) {return world.getBiome(x, y, z);}

    @Override
    public @NotNull Biome getComputedBiome(int x, int y, int z) {return world.getComputedBiome(x, y, z);}

    @Override
    public void setBiome(@NotNull Location location, @NotNull Biome biome) {world.setBiome(location, biome);}

    @Override
    public void setBiome(int x, int y, int z, @NotNull Biome biome) {world.setBiome(x, y, z, biome);}

    @Override
    public @NotNull BlockState getBlockState(@NotNull Location location) {return world.getBlockState(location);}

    @Override
    public @NotNull BlockState getBlockState(int x, int y, int z) {return world.getBlockState(x, y, z);}

    @Override
    public @NotNull BlockData getBlockData(@NotNull Location location) {return world.getBlockData(location);}

    @Override
    public @NotNull BlockData getBlockData(int x, int y, int z) {return world.getBlockData(x, y, z);}

    @Override
    public @NotNull Material getType(@NotNull Location location) {return world.getType(location);}

    @Override
    public @NotNull Material getType(int x, int y, int z) {return world.getType(x, y, z);}

    @Override
    public void setBlockData(@NotNull Location location, @NotNull BlockData blockData) {
        world.setBlockData(location,
                blockData);
    }

    @Override
    public void setBlockData(int x, int y, int z, @NotNull BlockData blockData) {
        world.setBlockData(x, y, z,
                blockData);
    }

    @Override
    public void setType(@NotNull Location location, @NotNull Material material) {world.setType(location, material);}

    @Override
    public void setType(int x, int y, int z, @NotNull Material material) {world.setType(x, y, z, material);}

    @Override
    public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType type) {return world.generateTree(location, random, type);}

    @Override
    public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType type,
                                @Nullable Consumer<BlockState> stateConsumer) {
        return world.generateTree(location,
                random, type, stateConsumer);
    }

    @Override
    public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType type,
                                @Nullable Predicate<BlockState> statePredicate) {
        return world.generateTree(location,
                random, type, statePredicate);
    }

    @Override
    public @NotNull Entity spawnEntity(@NotNull Location location, @NotNull EntityType type) {return world.spawnEntity(location, type);}

    @Override
    public @NotNull Entity spawnEntity(@NotNull Location loc, @NotNull EntityType type, boolean randomizeData) {return world.spawnEntity(loc, type, randomizeData);}

    @Override
    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz) throws IllegalArgumentException {return world.spawn(location, clazz);}

    @Override
    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz,
                                               @Nullable Consumer<T> function) throws IllegalArgumentException {return world.spawn(location, clazz, function);}

    @Override
    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz,
                                               CreatureSpawnEvent.@NotNull SpawnReason reason) throws IllegalArgumentException {return world.spawn(location, clazz, reason);}

    @Override
    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz,
                                               CreatureSpawnEvent.@NotNull SpawnReason reason,
                                               @Nullable Consumer<T> function) throws IllegalArgumentException {return world.spawn(location, clazz, reason, function);}

    @Override
    public @NotNull Entity spawnEntity(@NotNull Location loc, @NotNull EntityType type,
                                       CreatureSpawnEvent.@NotNull SpawnReason reason) {
        return world.spawnEntity(loc,
                type, reason);
    }

    @Override
    public @NotNull Entity spawnEntity(@NotNull Location loc, @NotNull EntityType type,
                                       CreatureSpawnEvent.@NotNull SpawnReason reason,
                                       @Nullable Consumer<Entity> function) {
        return world.spawnEntity(loc, type,
                reason, function);
    }

    @Override
    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz,
                                               @Nullable Consumer<T> function,
                                               CreatureSpawnEvent.@NotNull SpawnReason reason) throws IllegalArgumentException {return world.spawn(location, clazz, function, reason);}

    @Override
    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz,
                                               boolean randomizeData, @Nullable Consumer<T> function) throws IllegalArgumentException {return world.spawn(location, clazz, randomizeData, function);}

    @Override
    public @NotNull MoonPhase getMoonPhase() {return world.getMoonPhase();}

    @Override
    public @NotNull NamespacedKey getKey() {return world.getKey();}

    @Override
    public boolean lineOfSightExists(@NotNull Location from, @NotNull Location to) {return world.lineOfSightExists(from, to);}

    @Override
    public @NotNull Key key() {return world.key();}

    @NotNull
    @Override
    public String getName() {return world.getName();}

    @Override
    public @NotNull UUID getUID() {return world.getUID();}

    @NotNull
    @Override
    public Environment getEnvironment() {return world.getEnvironment();}

    @Override
    public long getSeed() {return world.getSeed();}

    @Override
    public int getMinHeight() {return world.getMinHeight();}

    @Override
    public int getMaxHeight() {return world.getMaxHeight();}

    @Override
    public @NotNull BiomeProvider vanillaBiomeProvider() {return world.vanillaBiomeProvider();}

    @Override
    public void sendPluginMessage(@NotNull Plugin source, @NotNull String channel, @NotNull byte[] message) {world.sendPluginMessage(source, channel, message);}

    @Override
    public @NotNull Set<String> getListeningPluginChannels() {return world.getListeningPluginChannels();}

    @Override
    public void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue) {world.setMetadata(metadataKey, newMetadataValue);}

    @Override
    public @NotNull List<MetadataValue> getMetadata(@NotNull String metadataKey) {return world.getMetadata(metadataKey);}

    @Override
    public boolean hasMetadata(@NotNull String metadataKey) {return world.hasMetadata(metadataKey);}

    @Override
    public void removeMetadata(@NotNull String metadataKey, @NotNull Plugin owningPlugin) {world.removeMetadata(metadataKey, owningPlugin);}

    @Override
    public @NotNull PersistentDataContainer getPersistentDataContainer() {return world.getPersistentDataContainer();}

    @Override
    public @NotNull Pointers pointers() {return world.pointers();}

    @NotNull
    @Override
    public Audience filterAudience(@NotNull Predicate<? super Audience> filter) {return world.filterAudience(filter);}

    @Override
    public void forEachAudience(java.util.function.@NotNull Consumer<? super Audience> action) {world.forEachAudience(action);}

    @Override
    public void sendMessage(@NotNull Identified source, @NotNull Component message, @NotNull MessageType type) {world.sendMessage(source, message, type);}

    @Override
    public void sendMessage(@NotNull Identity source, @NotNull Component message, @NotNull MessageType type) {world.sendMessage(source, message, type);}

    @Override
    public void sendActionBar(@NotNull Component message) {world.sendActionBar(message);}

    @Override
    public void sendPlayerListHeader(@NotNull Component header) {world.sendPlayerListHeader(header);}

    @Override
    public void sendPlayerListFooter(@NotNull Component footer) {world.sendPlayerListFooter(footer);}

    @Override
    public void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {world.sendPlayerListHeaderAndFooter(header, footer);}

    @Override
    public <T> void sendTitlePart(@NotNull TitlePart<T> part, @NotNull T value) {world.sendTitlePart(part, value);}

    @Override
    public void clearTitle() {world.clearTitle();}

    @Override
    public void resetTitle() {world.resetTitle();}

    @Override
    public void showBossBar(@NotNull BossBar bar) {world.showBossBar(bar);}

    @Override
    public void hideBossBar(@NotNull BossBar bar) {world.hideBossBar(bar);}

    @Override
    public void playSound(net.kyori.adventure.sound.@NotNull Sound sound) {world.playSound(sound);}

    @Override
    public void playSound(net.kyori.adventure.sound.@NotNull Sound sound, double x, double y, double z) {world.playSound(sound, x, y, z);}

    @Override
    public void playSound(net.kyori.adventure.sound.@NotNull Sound sound,
                          net.kyori.adventure.sound.Sound.@NotNull Emitter emitter) {world.playSound(sound, emitter);}

    @Override
    public void stopSound(@NotNull SoundStop stop) {world.stopSound(stop);}

    @Override
    public void openBook(@NotNull Book book) {world.openBook(book);}

    @Override
    public void sendMessage(@NotNull ComponentLike message) {world.sendMessage(message);}

    @Override
    public void sendMessage(@NotNull Identified source, @NotNull ComponentLike message) {
        world.sendMessage(source,
                message);
    }

    @Override
    public void sendMessage(@NotNull Identity source, @NotNull ComponentLike message) {
        world.sendMessage(source,
                message);
    }

    @Override
    public void sendMessage(@NotNull Component message) {world.sendMessage(message);}

    @Override
    public void sendMessage(@NotNull Identified source, @NotNull Component message) {
        world.sendMessage(source,
                message);
    }

    @Override
    public void sendMessage(@NotNull Identity source, @NotNull Component message) {world.sendMessage(source, message);}

    @Override
    public void sendMessage(@NotNull ComponentLike message, @NotNull MessageType type) {
        world.sendMessage(message,
                type);
    }

    @Override
    public void sendMessage(@NotNull Identified source, @NotNull ComponentLike message, @NotNull MessageType type) {world.sendMessage(source, message, type);}

    @Override
    public void sendMessage(@NotNull Identity source, @NotNull ComponentLike message, @NotNull MessageType type) {world.sendMessage(source, message, type);}

    @Override
    public void sendMessage(@NotNull Component message, @NotNull MessageType type) {world.sendMessage(message, type);}

    @Override
    public void sendActionBar(@NotNull ComponentLike message) {world.sendActionBar(message);}

    @Override
    public void sendPlayerListHeader(@NotNull ComponentLike header) {world.sendPlayerListHeader(header);}

    @Override
    public void sendPlayerListFooter(@NotNull ComponentLike footer) {world.sendPlayerListFooter(footer);}

    @Override
    public void sendPlayerListHeaderAndFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {world.sendPlayerListHeaderAndFooter(header, footer);}

    @Override
    public void showTitle(@NotNull Title title) {world.showTitle(title);}

    @Override
    public void stopSound(net.kyori.adventure.sound.@NotNull Sound sound) {world.stopSound(sound);}

    @Override
    public void openBook(Book.@NotNull Builder book) {world.openBook(book);}

    @Override
    public @NotNull <T> Optional<T> get(@NotNull Pointer<T> pointer) {return world.get(pointer);}

    @Override
    @Contract("_, null -> _; _, !null -> !null")
    public <T> @Nullable T getOrDefault(@NotNull Pointer<T> pointer, @Nullable T defaultValue) {return world.getOrDefault(pointer, defaultValue);}

    @Override
    public <T> @UnknownNullability T getOrDefaultFrom(@NotNull Pointer<T> pointer,
                                                      @NotNull Supplier<? extends T> defaultValue) {return world.getOrDefaultFrom(pointer, defaultValue);}
}
