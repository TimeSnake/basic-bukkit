/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.world;

import io.papermc.paper.block.fluid.FluidData;
import io.papermc.paper.math.Position;
import io.papermc.paper.world.MoonPhase;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.chat.SignedMessage;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.bukkit.util.*;
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

  public int getEntityCount() {
    return world.getEntityCount();
  }

  @NotNull
  public List<LivingEntity> getLivingEntities() {
    return world.getLivingEntities();
  }

  public <T extends Entity> @NotNull T createEntity(@NotNull Location location, @NotNull Class<T> aClass) {
    return world.createEntity(location, aClass);
  }

  @NotNull
  public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double xzRadius, double yRadius) {
    return world.getNearbyLivingEntities(loc, xzRadius, yRadius);
  }

  public boolean unloadChunk(int i, int i1) {
    return world.unloadChunk(i, i1);
  }

  public void setPVP(boolean b) {
    world.setPVP(b);
  }

  @Deprecated
  public void setTicksPerAnimalSpawns(int i) {
    world.setTicksPerAnimalSpawns(i);
  }

  @Deprecated
  public void setWaterAnimalSpawnLimit(int i) {
    world.setWaterAnimalSpawnLimit(i);
  }

  public void playSound(@NotNull Location location, @NotNull String s, @NotNull SoundCategory soundCategory, float v,
                        float v1) {
    world.playSound(location, s, soundCategory, v, v1);
  }

  public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4,
                            double v5, double v6) {
    world.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6);
  }

  @ApiStatus.Obsolete(since = "1.20.2")
  @NotNull
  public FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull BlockData blockData) throws IllegalArgumentException {
    return world.spawnFallingBlock(location, blockData);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsync(int i, int i1, boolean b, boolean b1) {
    return world.getChunkAtAsync(i, i1, b, b1);
  }

  @NotNull
  public LightningStrike strikeLightningEffect(@NotNull Location location) {
    return world.strikeLightningEffect(location);
  }

  @Deprecated
  public double getTemperature(int i, int i1) {
    return world.getTemperature(i, i1);
  }

  public void playSound(@NotNull Location location, @NotNull Sound sound, float v, float v1) {
    world.playSound(location, sound, v, v1);
  }

  public <T> @Nullable T getGameRuleDefault(@NotNull GameRule<T> gameRule) {
    return world.getGameRuleDefault(gameRule);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @Nullable List<Player> list, @Nullable Player player,
                                double v, double v1, double v2, int i, double v3, double v4, double v5, double v6,
                                @Nullable T t, boolean b) {
    world.spawnParticle(particle, list, player, v, v1, v2, i, v3, v4, v5, v6, t, b);
  }

  public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3,
                                double v4, double v5, double v6, @Nullable T t, boolean b) {
    world.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6, t, b);
  }

  public @NotNull FluidData getFluidData(int i, int i1, int i2) {
    return world.getFluidData(i, i1, i2);
  }

  @NotNull
  public Collection<Entity> getNearbyEntities(@NotNull Location location, double v, double v1, double v2,
                                              @Nullable Predicate<? super Entity> predicate) {
    return world.getNearbyEntities(location, v, v1, v2, predicate);
  }

  public boolean hasMetadata(@NotNull String s) {
    return world.hasMetadata(s);
  }

  public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> aClass, boolean b,
                                             @Nullable Consumer<? super T> consumer) throws IllegalArgumentException {
    return world.spawn(location, aClass, b, consumer);
  }

  public void sendPlayerListHeaderAndFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
    world.sendPlayerListHeaderAndFooter(header, footer);
  }

  @NotNull
  public Location getSpawnLocation() {
    return world.getSpawnLocation();
  }

  @Deprecated
  public void setKeepSpawnInMemory(boolean b) {
    world.setKeepSpawnInMemory(b);
  }

  public int getSpawnLimit(@NotNull SpawnCategory spawnCategory) {
    return world.getSpawnLimit(spawnCategory);
  }

  public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3,
                                double v4, double v5, @Nullable T t) {
    world.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, t);
  }

  @Nullable
  public BiomeSearchResult locateNearestBiome(@NotNull Location location, int i, int i1, int i2,
                                              @NotNull Biome... biomes) {
    return world.locateNearestBiome(location, i, i1, i2, biomes);
  }

  @Deprecated
  public long getTicksPerWaterUndergroundCreatureSpawns() {
    return world.getTicksPerWaterUndergroundCreatureSpawns();
  }

  public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                            double v2) {
    world.spawnParticle(particle, location, i, v, v1, v2);
  }

  public void sendMessage(@NotNull ComponentLike message) {
    world.sendMessage(message);
  }

  public void sendPlayerListFooter(@NotNull ComponentLike footer) {
    world.sendPlayerListFooter(footer);
  }

  public boolean lineOfSightExists(@NotNull Location location, @NotNull Location location1) {
    return world.lineOfSightExists(location, location1);
  }

  public void loadChunk(@NotNull Chunk chunk) {
    world.loadChunk(chunk);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsync(@NotNull Location loc) {
    return world.getChunkAtAsync(loc);
  }

  @NotNull
  public String[] getGameRules() {
    return world.getGameRules();
  }

  public void setBlockData(int i, int i1, int i2, @NotNull BlockData blockData) {
    world.setBlockData(i, i1, i2, blockData);
  }

  @Nullable
  public Location findLightningTarget(@NotNull Location location) {
    return world.findLightningTarget(location);
  }

  public boolean createExplosion(@NotNull Location location, float v, boolean b) {
    return world.createExplosion(location, v, b);
  }

  @NotNull
  public Set<FeatureFlag> getFeatureFlags() {
    return world.getFeatureFlags();
  }

  @NotNull
  public PersistentDataContainer getPersistentDataContainer() {
    return world.getPersistentDataContainer();
  }

  public boolean hasStructureAt(@NotNull Position position, @NotNull Structure structure) {
    return world.hasStructureAt(position, structure);
  }

  @NotNull
  public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz,
                                                                  @NotNull Location loc, double xzRadius,
                                                                  double yRadius) {
    return world.getNearbyEntitiesByType(clazz, loc, xzRadius, yRadius);
  }

  public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX,
                                double offsetY, double offsetZ, double extra, @Nullable T data) {
    world.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
  }

  @NotNull
  public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz,
                                                                  @NotNull Location loc, double radius) {
    return world.getNearbyEntitiesByType(clazz, loc, radius);
  }

  @Deprecated(since = "1.13.1")
  public void getChunkAtAsync(@NotNull Location loc, @NotNull World.ChunkLoadCallback cb) {
    world.getChunkAtAsync(loc, cb);
  }

  public long getFullTime() {
    return world.getFullTime();
  }

  public boolean isUltraWarm() {
    return world.isUltraWarm();
  }

  public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory soundCategory,
                        float v, float v1, long l) {
    world.playSound(location, sound, soundCategory, v, v1, l);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @Nullable List<Player> receivers, @NotNull Player source,
                                double x, double y, double z, int count, double offsetX, double offsetY,
                                double offsetZ, double extra, @Nullable T data) {
    world.spawnParticle(particle, receivers, source, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
  }

  @Deprecated
  @Nullable
  public Location locateNearestBiome(@NotNull Location origin, @NotNull Biome biome, int radius, int step) {
    return world.locateNearestBiome(origin, biome, radius, step);
  }

  public void setSimulationDistance(int i) {
    world.setSimulationDistance(i);
  }

  public boolean isHardcore() {
    return world.isHardcore();
  }

  public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory soundCategory,
                        float v, float v1) {
    world.playSound(location, sound, soundCategory, v, v1);
  }

  public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i) {
    world.spawnParticle(particle, location, i);
  }

  public void clearTitle() {
    world.clearTitle();
  }

  public boolean setSpawnLocation(int i, int i1, int i2, float v) {
    return world.setSpawnLocation(i, i1, i2, v);
  }

  @NotNull
  public Audience filterAudience(@NotNull Predicate<? super Audience> filter) {
    return world.filterAudience(filter);
  }

  public void setAutoSave(boolean b) {
    world.setAutoSave(b);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Location loc) {
    return world.getChunkAtAsyncUrgently(loc);
  }

  public void removeResourcePacks(@NotNull ResourcePackRequestLike request) {
    world.removeResourcePacks(request);
  }

  public void setBiome(@NotNull Location location, @NotNull Biome biome) {
    world.setBiome(location, biome);
  }

  @NotNull
  public Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack itemStack) {
    return world.dropItemNaturally(location, itemStack);
  }

  public void sendGameEvent(@Nullable Entity entity, @NotNull GameEvent gameEvent, @NotNull Vector vector) {
    world.sendGameEvent(entity, gameEvent, vector);
  }

  @NotNull
  public Collection<GeneratedStructure> getStructures(int i, int i1) {
    return world.getStructures(i, i1);
  }

  public boolean isThundering() {
    return world.isThundering();
  }

  public void sendResourcePacks(@NotNull ResourcePackRequestLike request) {
    world.sendResourcePacks(request);
  }

  @Deprecated
  public void sendMessage(@NotNull Identity source, @NotNull Component message) {
    world.sendMessage(source, message);
  }

  public double getCoordinateScale() {
    return world.getCoordinateScale();
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsync(@NotNull Block block, boolean gen) {
    return world.getChunkAtAsync(block, gen);
  }

  @NotNull
  public Material getType(@NotNull Location location) {
    return world.getType(location);
  }

  @Deprecated
  public int getMonsterSpawnLimit() {
    return world.getMonsterSpawnLimit();
  }

  @NotNull
  public Biome getBiome(@NotNull Location location) {
    return world.getBiome(location);
  }

  public boolean getAllowAnimals() {
    return world.getAllowAnimals();
  }

  public double getHumidity(int i, int i1, int i2) {
    return world.getHumidity(i, i1, i2);
  }

  @NotNull
  public Collection<Player> getPlayersSeeingChunk(@NotNull Chunk chunk) {
    return world.getPlayersSeeingChunk(chunk);
  }

  @NotNull
  public Collection<Player> getNearbyPlayers(@NotNull Location loc, double xRadius, double yRadius, double zRadius,
                                             @Nullable Predicate<? super Player> predicate) {
    return world.getNearbyPlayers(loc, xRadius, yRadius, zRadius, predicate);
  }

  @Deprecated
  @Nullable
  public Location locateNearestStructure(@NotNull Location location, @NotNull StructureType structureType, int i,
                                         boolean b) {
    return world.locateNearestStructure(location, structureType, i, b);
  }

  public int getHighestBlockYAt(int i, int i1) {
    return world.getHighestBlockYAt(i, i1);
  }

  public boolean isNatural() {
    return world.isNatural();
  }

  @Deprecated
  public long getTicksPerAmbientSpawns() {
    return world.getTicksPerAmbientSpawns();
  }

  @NotNull
  public Item dropItem(@NotNull Location location, @NotNull ItemStack itemStack,
                       @Nullable Consumer<? super Item> consumer) {
    return world.dropItem(location, itemStack, consumer);
  }

  @Deprecated
  @Nullable
  public Location locateNearestBiome(@NotNull Location origin, @NotNull Biome biome, int radius) {
    return world.locateNearestBiome(origin, biome, radius);
  }

  @NotNull
  public Collection<Player> getNearbyPlayers(@NotNull Location loc, double xzRadius, double yRadius,
                                             @Nullable Predicate<? super Player> predicate) {
    return world.getNearbyPlayers(loc, xzRadius, yRadius, predicate);
  }

  @Deprecated(since = "1.18.1")
  @NotNull
  public Location getLocationAtKey(long key) {
    return world.getLocationAtKey(key);
  }

  public void playSound(net.kyori.adventure.sound.@NotNull Sound sound,
                        net.kyori.adventure.sound.Sound.Emitter emitter) {
    world.playSound(sound, emitter);
  }

  @Nullable
  public StructureSearchResult locateNearestStructure(@NotNull Location location, @NotNull Structure structure, int i
      , boolean b) {
    return world.locateNearestStructure(location, structure, i, b);
  }

  @NotNull
  public World.Environment getEnvironment() {
    return world.getEnvironment();
  }

  public void setThunderDuration(int i) {
    world.setThunderDuration(i);
  }

  @Nullable
  public RayTraceResult rayTrace(@NotNull Location location, @NotNull Vector vector, double v,
                                 @NotNull FluidCollisionMode fluidCollisionMode, boolean b, double v1,
                                 @Nullable Predicate<? super Entity> predicate) {
    return world.rayTrace(location, vector, v, fluidCollisionMode, b, v1, predicate);
  }

  @Nullable
  public StructureSearchResult locateNearestStructure(@NotNull Location location,
                                                      org.bukkit.generator.structure.@NotNull StructureType structureType, int i, boolean b) {
    return world.locateNearestStructure(location, structureType, i, b);
  }

  public void playSound(@NotNull Location location, @NotNull String s, float v, float v1) {
    world.playSound(location, s, v, v1);
  }

  @NotNull
  public Entity spawnEntity(@NotNull Location location, @NotNull EntityType entityType, boolean b) {
    return world.spawnEntity(location, entityType, b);
  }

  public boolean createExplosion(@Nullable Entity source, @NotNull Location loc, float power) {
    return world.createExplosion(source, loc, power);
  }

  public boolean isChunkGenerated(long chunkKey) {
    return world.isChunkGenerated(chunkKey);
  }

  @Deprecated
  public int getAnimalSpawnLimit() {
    return world.getAnimalSpawnLimit();
  }

  public void removeResourcePacks(@NotNull ResourcePackInfoLike request,
                                  @NotNull ResourcePackInfoLike @NotNull ... others) {
    world.removeResourcePacks(request, others);
  }

  public void setStorm(boolean b) {
    world.setStorm(b);
  }

  public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, @Nullable T t) {
    world.spawnParticle(particle, v, v1, v2, i, t);
  }

  public void sendMessage(@NotNull SignedMessage signedMessage, ChatType.Bound boundChatType) {
    world.sendMessage(signedMessage, boundChatType);
  }

  @NotNull
  public List<Player> getPlayers() {
    return world.getPlayers();
  }

  public @NotNull FluidData getFluidData(@NotNull Location location) {
    return world.getFluidData(location);
  }

  public int getMaxHeight() {
    return world.getMaxHeight();
  }

  public int getHighestBlockYAt(int i, int i1, @NotNull HeightMap heightMap) {
    return world.getHighestBlockYAt(i, i1, heightMap);
  }

  public long getTicksPerSpawns(@NotNull SpawnCategory spawnCategory) {
    return world.getTicksPerSpawns(spawnCategory);
  }

  public boolean createExplosion(@Nullable Entity entity, @NotNull Location location, float v, boolean b, boolean b1) {
    return world.createExplosion(entity, location, v, b, b1);
  }

  @Deprecated
  @NotNull
  public FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull Material material, byte b) throws IllegalArgumentException {
    return world.spawnFallingBlock(location, material, b);
  }

  public void hideBossBar(@NotNull BossBar bar) {
    world.hideBossBar(bar);
  }

  @Nullable
  public BiomeProvider getBiomeProvider() {
    return world.getBiomeProvider();
  }

  public boolean addPluginChunkTicket(int i, int i1, @NotNull Plugin plugin) {
    return world.addPluginChunkTicket(i, i1, plugin);
  }

  public int getPlayerCount() {
    return world.getPlayerCount();
  }

  @NotNull
  public Difficulty getDifficulty() {
    return world.getDifficulty();
  }

  @NotNull
  public BlockState getBlockState(int i, int i1, int i2) {
    return world.getBlockState(i, i1, i2);
  }

  @NotNull
  public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double radius, @Nullable Predicate<?
      super LivingEntity> predicate) {
    return world.getNearbyLivingEntities(loc, radius, predicate);
  }

  public boolean createExplosion(@NotNull Location location, float v, boolean b, boolean b1) {
    return world.createExplosion(location, v, b, b1);
  }

  @NotNull
  public File getWorldFolder() {
    return world.getWorldFolder();
  }

  public boolean hasRaids() {
    return world.hasRaids();
  }

  @NotNull
  public Block getBlockAt(@NotNull Location location) {
    return world.getBlockAt(location);
  }

  @NotNull
  public MoonPhase getMoonPhase() {
    return world.getMoonPhase();
  }

  public boolean loadChunk(int i, int i1, boolean b) {
    return world.loadChunk(i, i1, b);
  }

  @NotNull
  public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double xRadius, double yRadius,
                                                          double zRadius) {
    return world.getNearbyLivingEntities(loc, xRadius, yRadius, zRadius);
  }

  @NotNull
  public Chunk getChunkAt(int i, int i1, boolean b) {
    return world.getChunkAt(i, i1, b);
  }

  public void playSound(net.kyori.adventure.sound.@NotNull Sound sound, double x, double y, double z) {
    world.playSound(sound, x, y, z);
  }

  public int getTileEntityCount() {
    return world.getTileEntityCount();
  }

  public boolean generateTree(@NotNull Location location, @NotNull TreeType treeType) {
    return world.generateTree(location, treeType);
  }

  @NotNull
  public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends Entity> clazz,
                                                                  @NotNull Location loc, double xRadius,
                                                                  double yRadius, double zRadius,
                                                                  @Nullable Predicate<? super T> predicate) {
    return world.getNearbyEntitiesByType(clazz, loc, xRadius, yRadius, zRadius, predicate);
  }

  @NotNull
  public Collection<Entity> getEntitiesByClasses(@NotNull Class<?>... classes) {
    return world.getEntitiesByClasses(classes);
  }

  @Deprecated
  public void setTicksPerMonsterSpawns(int i) {
    world.setTicksPerMonsterSpawns(i);
  }

  @Nullable
  public Raid getRaid(int i) {
    return world.getRaid(i);
  }

  public void removeResourcePacks(@NotNull Iterable<UUID> ids) {
    world.removeResourcePacks(ids);
  }

  @Deprecated
  public void setWaterUndergroundCreatureSpawnLimit(int i) {
    world.setWaterUndergroundCreatureSpawnLimit(i);
  }

  public void sendPlayerListFooter(@NotNull Component footer) {
    world.sendPlayerListFooter(footer);
  }

  @Deprecated(since = "1.13.1")
  public void getChunkAtAsync(@NotNull Block block, @NotNull World.ChunkLoadCallback cb) {
    world.getChunkAtAsync(block, cb);
  }

  public boolean hasCeiling() {
    return world.hasCeiling();
  }

  public boolean isGameRule(@NotNull String s) {
    return world.isGameRule(s);
  }

  public boolean createExplosion(@Nullable Entity source, @NotNull Location loc, float power, boolean setFire) {
    return world.createExplosion(source, loc, power, setFire);
  }

  @Deprecated
  public boolean regenerateChunk(int i, int i1) {
    return world.regenerateChunk(i, i1);
  }

  public void openBook(@NotNull Book book) {
    world.openBook(book);
  }

  public <T extends LivingEntity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> aClass,
                                                   @NotNull CreatureSpawnEvent.SpawnReason spawnReason, boolean b,
                                                   @Nullable Consumer<? super T> consumer) throws IllegalArgumentException {
    return world.spawn(location, aClass, spawnReason, b, consumer);
  }

  public void sendMessage(@NotNull Component message) {
    world.sendMessage(message);
  }

  public void sendActionBar(@NotNull Component message) {
    world.sendActionBar(message);
  }

  public boolean isDayTime() {
    return world.isDayTime();
  }

  public void getChunkAtAsync(@NotNull Location loc, boolean gen, @NotNull Consumer<? super Chunk> cb) {
    world.getChunkAtAsync(loc, gen, cb);
  }

  public void playSound(@NotNull Entity entity, @NotNull String s, @NotNull SoundCategory soundCategory, float v,
                        float v1, long l) {
    world.playSound(entity, s, soundCategory, v, v1, l);
  }

  @NotNull
  public BlockData getBlockData(int i, int i1, int i2) {
    return world.getBlockData(i, i1, i2);
  }

  public void openBook(Book.Builder book) {
    world.openBook(book);
  }

  @Nullable
  public RayTraceResult rayTraceBlocks(@NotNull Position position, @NotNull Vector vector, double v,
                                       @NotNull FluidCollisionMode fluidCollisionMode, boolean b,
                                       @Nullable Predicate<? super Block> predicate) {
    return world.rayTraceBlocks(position, vector, v, fluidCollisionMode, b, predicate);
  }

  @NotNull
  public Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack itemStack, @Nullable Consumer<?
      super Item> consumer) {
    return world.dropItemNaturally(location, itemStack, consumer);
  }

  public void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory soundCategory, float v,
                        float v1, long l) {
    world.playSound(entity, sound, soundCategory, v, v1, l);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsync(@NotNull Location loc, boolean gen) {
    return world.getChunkAtAsync(loc, gen);
  }

  public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType treeType,
                              @Nullable Predicate<? super BlockState> predicate) {
    return world.generateTree(location, random, treeType, predicate);
  }

  @NotNull
  public Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox,
                                              @Nullable Predicate<? super Entity> predicate) {
    return world.getNearbyEntities(boundingBox, predicate);
  }

  public int getLogicalHeight() {
    return world.getLogicalHeight();
  }

  @Deprecated
  public int getWaterAnimalSpawnLimit() {
    return world.getWaterAnimalSpawnLimit();
  }

  @NotNull
  public List<Entity> getEntities() {
    return world.getEntities();
  }

  @Deprecated
  public long getTicksPerAnimalSpawns() {
    return world.getTicksPerAnimalSpawns();
  }

  public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType treeType,
                              @Nullable Consumer<? super BlockState> consumer) {
    return world.generateTree(location, random, treeType, consumer);
  }

  @NotNull
  public <T> Optional<T> get(@NotNull Pointer<T> pointer) {
    return world.get(pointer);
  }

  public void getChunkAtAsync(@NotNull Block block, @NotNull Consumer<? super Chunk> cb) {
    world.getChunkAtAsync(block, cb);
  }

  @NotNull
  public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double xRadius, double yRadius,
                                                          double zRadius,
                                                          @Nullable Predicate<? super LivingEntity> predicate) {
    return world.getNearbyLivingEntities(loc, xRadius, yRadius, zRadius, predicate);
  }

  public void setTime(long l) {
    world.setTime(l);
  }

  public int getThunderDuration() {
    return world.getThunderDuration();
  }

  public void sendResourcePacks(@NotNull ResourcePackInfoLike first, @NotNull ResourcePackInfoLike... others) {
    world.sendResourcePacks(first, others);
  }

  @NotNull
  public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double xzRadius, double yRadius,
                                                          @Nullable Predicate<? super LivingEntity> predicate) {
    return world.getNearbyLivingEntities(loc, xzRadius, yRadius, predicate);
  }

  public boolean createExplosion(@NotNull Location location, float v, boolean b, boolean b1, @Nullable Entity entity) {
    return world.createExplosion(location, v, b, b1, entity);
  }

  public double getTemperature(int i, int i1, int i2) {
    return world.getTemperature(i, i1, i2);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, @Nullable T t) {
    world.spawnParticle(particle, location, i, t);
  }

  @Deprecated
  public void setNoTickViewDistance(int viewDistance) {
    world.setNoTickViewDistance(viewDistance);
  }

  @NotNull
  public Entity spawnEntity(@NotNull Location location, @NotNull EntityType entityType) {
    return world.spawnEntity(location, entityType);
  }

  public <T> void sendTitlePart(@NotNull TitlePart<T> part, @NotNull T value) {
    world.sendTitlePart(part, value);
  }

  @NotNull
  public Collection<Player> getNearbyPlayers(@NotNull Location loc, double radius) {
    return world.getNearbyPlayers(loc, radius);
  }

  @NotNull
  public Collection<Plugin> getPluginChunkTickets(int i, int i1) {
    return world.getPluginChunkTickets(i, i1);
  }

  @Deprecated
  public boolean getKeepSpawnInMemory() {
    return world.getKeepSpawnInMemory();
  }

  public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i) {
    world.spawnParticle(particle, v, v1, v2, i);
  }

  public void clearResourcePacks() {
    world.clearResourcePacks();
  }

  public boolean getPVP() {
    return world.getPVP();
  }

  @NotNull
  public Chunk[] getLoadedChunks() {
    return world.getLoadedChunks();
  }

  public void stopSound(net.kyori.adventure.sound.@NotNull Sound sound) {
    world.stopSound(sound);
  }

  public boolean setSpawnLocation(int i, int i1, int i2) {
    return world.setSpawnLocation(i, i1, i2);
  }

  @Nullable
  public DragonBattle getEnderDragonBattle() {
    return world.getEnderDragonBattle();
  }

  @NotNull
  public Biome getComputedBiome(int i, int i1, int i2) {
    return world.getComputedBiome(i, i1, i2);
  }

  public void setBlockData(@NotNull Location location, @NotNull BlockData blockData) {
    world.setBlockData(location, blockData);
  }

  public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                            double v2, double v3) {
    world.spawnParticle(particle, location, i, v, v1, v2, v3);
  }

  public <T> boolean setGameRule(@NotNull GameRule<T> gameRule, @NotNull T t) {
    return world.setGameRule(gameRule, t);
  }

  public void playEffect(@NotNull Location location, @NotNull Effect effect, int i) {
    world.playEffect(location, effect, i);
  }

  @Deprecated
  public void setWaterAmbientSpawnLimit(int i) {
    world.setWaterAmbientSpawnLimit(i);
  }

  @Deprecated
  public boolean isChunkInUse(int i, int i1) {
    return world.isChunkInUse(i, i1);
  }

  @NotNull
  public Block getHighestBlockAt(int i, int i1, @NotNull HeightMap heightMap) {
    return world.getHighestBlockAt(i, i1, heightMap);
  }

  @NotNull
  public Chunk getChunkAt(long chunkKey) {
    return world.getChunkAt(chunkKey);
  }

  public void setChunkForceLoaded(int i, int i1, boolean b) {
    world.setChunkForceLoaded(i, i1, b);
  }

  @NotNull
  public Collection<Chunk> getIntersectingChunks(@NotNull BoundingBox boundingBox) {
    return world.getIntersectingChunks(boundingBox);
  }

  public void setClearWeatherDuration(int i) {
    world.setClearWeatherDuration(i);
  }

  @Deprecated
  public void setTicksPerWaterSpawns(int i) {
    world.setTicksPerWaterSpawns(i);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Block block, boolean gen) {
    return world.getChunkAtAsyncUrgently(block, gen);
  }

  @NotNull
  public List<MetadataValue> getMetadata(@NotNull String s) {
    return world.getMetadata(s);
  }

  public boolean isPositionLoaded(@NotNull Position position) {
    return world.isPositionLoaded(position);
  }

  @Deprecated
  public void sendMessage(@NotNull Identified source, @NotNull Component message) {
    world.sendMessage(source, message);
  }

  @Deprecated
  public boolean generateTree(@NotNull Location location, @NotNull TreeType treeType,
                              @NotNull BlockChangeDelegate blockChangeDelegate) {
    return world.generateTree(location, treeType, blockChangeDelegate);
  }

  @NotNull
  public Collection<Entity> getNearbyEntities(@NotNull Location location, double v, double v1, double v2) {
    return world.getNearbyEntities(location, v, v1, v2);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                                double v2, double v3, @Nullable T t) {
    world.spawnParticle(particle, location, i, v, v1, v2, v3, t);
  }

  @Deprecated
  public void sendMessage(@NotNull Identity source, @NotNull ComponentLike message) {
    world.sendMessage(source, message);
  }

  public boolean unloadChunk(@NotNull Chunk chunk) {
    return world.unloadChunk(chunk);
  }

  @NotNull
  public Collection<Player> getPlayersSeeingChunk(int i, int i1) {
    return world.getPlayersSeeingChunk(i, i1);
  }

  public void setWeatherDuration(int i) {
    world.setWeatherDuration(i);
  }

  public void getChunkAtAsync(int x, int z, @NotNull Consumer<? super Chunk> cb) {
    world.getChunkAtAsync(x, z, cb);
  }

  public void playSound(@NotNull Entity entity, @NotNull String s, @NotNull SoundCategory soundCategory, float v,
                        float v1) {
    world.playSound(entity, s, soundCategory, v, v1);
  }

  @NotNull
  public WorldBorder getWorldBorder() {
    return world.getWorldBorder();
  }

  @NotNull
  public Collection<GeneratedStructure> getStructures(int i, int i1, @NotNull Structure structure) {
    return world.getStructures(i, i1, structure);
  }

  public boolean hasSkyLight() {
    return world.hasSkyLight();
  }

  @NotNull
  public Collection<Material> getInfiniburn() {
    return world.getInfiniburn();
  }

  public void removeMetadata(@NotNull String s, @NotNull Plugin plugin) {
    world.removeMetadata(s, plugin);
  }

  @NotNull
  public Block getHighestBlockAt(int i, int i1) {
    return world.getHighestBlockAt(i, i1);
  }

  public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType treeType) {
    return world.generateTree(location, random, treeType);
  }

  @NotNull
  public ChunkSnapshot getEmptyChunkSnapshot(int i, int i1, boolean b, boolean b1) {
    return world.getEmptyChunkSnapshot(i, i1, b, b1);
  }

  public void playEffect(@NotNull Location location, @NotNull Effect effect, int i, int i1) {
    world.playEffect(location, effect, i, i1);
  }

  @Deprecated
  public void setAmbientSpawnLimit(int i) {
    world.setAmbientSpawnLimit(i);
  }

  public boolean createExplosion(@NotNull Entity source, float power, boolean setFire, boolean breakBlocks) {
    return world.createExplosion(source, power, setFire, breakBlocks);
  }

  public boolean canGenerateStructures() {
    return world.canGenerateStructures();
  }

  @Deprecated
  public void setTicksPerWaterAmbientSpawns(int i) {
    world.setTicksPerWaterAmbientSpawns(i);
  }

  public int getMinHeight() {
    return world.getMinHeight();
  }

  @Deprecated
  @NotNull
  public Biome getBiome(int i, int i1) {
    return world.getBiome(i, i1);
  }

  public void playSound(@NotNull Entity entity, @NotNull Sound sound, float v, float v1) {
    world.playSound(entity, sound, v, v1);
  }

  public boolean isAutoSave() {
    return world.isAutoSave();
  }

  public void setSpawnLimit(@NotNull SpawnCategory spawnCategory, int i) {
    world.setSpawnLimit(spawnCategory, i);
  }

  public void showTitle(@NotNull Title title) {
    world.showTitle(title);
  }

  @NotNull
  public UUID getUID() {
    return world.getUID();
  }

  @NotNull
  public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz,
                                                                  @NotNull Location loc, double xzRadius,
                                                                  double yRadius,
                                                                  @Nullable Predicate<? super T> predicate) {
    return world.getNearbyEntitiesByType(clazz, loc, xzRadius, yRadius, predicate);
  }

  @NotNull
  public Chunk getChunkAt(@NotNull Location location) {
    return world.getChunkAt(location);
  }

  public boolean isChunkLoaded(int i, int i1) {
    return world.isChunkLoaded(i, i1);
  }

  public boolean unloadChunk(int i, int i1, boolean b) {
    return world.unloadChunk(i, i1, b);
  }

  public <T extends AbstractArrow> @NotNull T spawnArrow(@NotNull Location location, @NotNull Vector vector, float v,
                                                         float v1, @NotNull Class<T> aClass) {
    return world.spawnArrow(location, vector, v, v1, aClass);
  }

  @Nullable
  public Location findLightningRod(@NotNull Location location) {
    return world.findLightningRod(location);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsync(int x, int z) {
    return world.getChunkAtAsync(x, z);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                                double v2, @Nullable T t) {
    world.spawnParticle(particle, location, i, v, v1, v2, t);
  }

  public void resetTitle() {
    world.resetTitle();
  }

  @NotNull
  public Arrow spawnArrow(@NotNull Location location, @NotNull Vector vector, float v, float v1) {
    return world.spawnArrow(location, vector, v, v1);
  }

  @Nullable
  public RayTraceResult rayTraceBlocks(@NotNull Location location, @NotNull Vector vector, double v) {
    return world.rayTraceBlocks(location, vector, v);
  }

  public boolean setSpawnLocation(@NotNull Location location) {
    return world.setSpawnLocation(location);
  }

  public int getSeaLevel() {
    return world.getSeaLevel();
  }

  public boolean refreshChunk(int i, int i1) {
    return world.refreshChunk(i, i1);
  }

  @Deprecated
  @NotNull
  public <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T>... classes) {
    return world.getEntitiesByClass(classes);
  }

  @NotNull
  public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz,
                                                                  @NotNull Location loc, double radius,
                                                                  @Nullable Predicate<? super T> predicate) {
    return world.getNearbyEntitiesByType(clazz, loc, radius, predicate);
  }

  @NotNull
  public Collection<Player> getNearbyPlayers(@NotNull Location loc, double xzRadius, double yRadius) {
    return world.getNearbyPlayers(loc, xzRadius, yRadius);
  }

  @Nullable
  public RayTraceResult rayTraceEntities(@NotNull Location location, @NotNull Vector vector, double v) {
    return world.rayTraceEntities(location, vector, v);
  }

  public void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
    world.sendPlayerListHeaderAndFooter(header, footer);
  }

  @Nullable
  public RayTraceResult rayTrace(@NotNull Position position, @NotNull Vector vector, double v,
                                 @NotNull FluidCollisionMode fluidCollisionMode, boolean b, double v1,
                                 @Nullable Predicate<? super Entity> predicate,
                                 @Nullable Predicate<? super Block> predicate1) {
    return world.rayTrace(position, vector, v, fluidCollisionMode, b, v1, predicate, predicate1);
  }

  @Deprecated(since = "1.18.1")
  @NotNull
  public Block getBlockAtKey(long key) {
    return world.getBlockAtKey(key);
  }

  @NotNull
  public Collection<Chunk> getForceLoadedChunks() {
    return world.getForceLoadedChunks();
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsyncUrgently(int x, int z) {
    return world.getChunkAtAsyncUrgently(x, z);
  }

  @NotNull
  public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double radius) {
    return world.getNearbyLivingEntities(loc, radius);
  }

  public <T extends Entity> @NotNull T addEntity(@NotNull T t) {
    return world.addEntity(t);
  }

  public void setFullTime(long l) {
    world.setFullTime(l);
  }

  public void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory soundCategory, float v,
                        float v1) {
    world.playSound(entity, sound, soundCategory, v, v1);
  }

  public boolean isClearWeather() {
    return world.isClearWeather();
  }

  public void setSpawnFlags(boolean b, boolean b1) {
    world.setSpawnFlags(b, b1);
  }

  public void setType(int i, int i1, int i2, @NotNull Material material) {
    world.setType(i, i1, i2, material);
  }

  @NotNull
  public NamespacedKey getKey() {
    return world.getKey();
  }

  @Nullable
  public RayTraceResult rayTraceEntities(@NotNull Position position, @NotNull Vector vector, double v, double v1,
                                         @Nullable Predicate<? super Entity> predicate) {
    return world.rayTraceEntities(position, vector, v, v1, predicate);
  }

  public boolean hasStorm() {
    return world.hasStorm();
  }

  public boolean createExplosion(double v, double v1, double v2, float v3) {
    return world.createExplosion(v, v1, v2, v3);
  }

  public boolean createExplosion(@NotNull Location location, float v) {
    return world.createExplosion(location, v);
  }

  public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T t) {
    world.playEffect(location, effect, t);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Location loc, boolean gen) {
    return world.getChunkAtAsyncUrgently(loc, gen);
  }

  public void setViewDistance(int i) {
    world.setViewDistance(i);
  }

  @NotNull
  public Block getHighestBlockAt(@NotNull Location location, @NotNull HeightMap heightMap) {
    return world.getHighestBlockAt(location, heightMap);
  }

  public void sendMessage(@NotNull ComponentLike message, ChatType.Bound boundChatType) {
    world.sendMessage(message, boundChatType);
  }

  public boolean removePluginChunkTicket(int i, int i1, @NotNull Plugin plugin) {
    return world.removePluginChunkTicket(i, i1, plugin);
  }

  @Nullable
  public RayTraceResult rayTraceBlocks(@NotNull Location location, @NotNull Vector vector, double v,
                                       @NotNull FluidCollisionMode fluidCollisionMode, boolean b) {
    return world.rayTraceBlocks(location, vector, v, fluidCollisionMode, b);
  }

  public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T t, int i) {
    world.playEffect(location, effect, t, i);
  }

  @Deprecated
  public void setTicksPerWaterUndergroundCreatureSpawns(int i) {
    world.setTicksPerWaterUndergroundCreatureSpawns(i);
  }

  @Nullable
  public ChunkGenerator getGenerator() {
    return world.getGenerator();
  }

  @Contract("null -> null; !null -> !null")
  @Deprecated
  @Nullable
  public String getGameRuleValue(@Nullable String s) {
    return world.getGameRuleValue(s);
  }

  public @NotNull FluidData getFluidData(@NotNull Position position) {
    return world.getFluidData(position);
  }

  public boolean createExplosion(@NotNull Entity source, float power, boolean setFire) {
    return world.createExplosion(source, power, setFire);
  }

  public void playSound(@NotNull Entity entity, @NotNull String s, float v, float v1) {
    world.playSound(entity, s, v, v1);
  }

  @NotNull
  public List<BlockPopulator> getPopulators() {
    return world.getPopulators();
  }

  @Deprecated
  public long getTicksPerMonsterSpawns() {
    return world.getTicksPerMonsterSpawns();
  }

  @Deprecated
  public int getWaterUndergroundCreatureSpawnLimit() {
    return world.getWaterUndergroundCreatureSpawnLimit();
  }

  public int getChunkCount() {
    return world.getChunkCount();
  }

  @Nullable
  public RayTraceResult rayTraceBlocks(@NotNull Location location, @NotNull Vector vector, double v,
                                       @NotNull FluidCollisionMode fluidCollisionMode) {
    return world.rayTraceBlocks(location, vector, v, fluidCollisionMode);
  }

  @NotNull
  public Material getType(int i, int i1, int i2) {
    return world.getType(i, i1, i2);
  }

  public void setSendViewDistance(int i) {
    world.setSendViewDistance(i);
  }

  @NotNull
  public Biome getBiome(int i, int i1, int i2) {
    return world.getBiome(i, i1, i2);
  }

  public boolean isRespawnAnchorWorks() {
    return world.isRespawnAnchorWorks();
  }

  public int getSimulationDistance() {
    return world.getSimulationDistance();
  }

  @NotNull
  public LightningStrike strikeLightning(@NotNull Location location) {
    return world.strikeLightning(location);
  }

  @NotNull
  public BlockState getBlockState(@NotNull Location location) {
    return world.getBlockState(location);
  }

  @Nullable
  public Entity getEntity(@NotNull UUID uuid) {
    return world.getEntity(uuid);
  }

  @Deprecated(since = "1.13.1")
  public void getChunkAtAsync(int x, int z, @NotNull World.ChunkLoadCallback cb) {
    world.getChunkAtAsync(x, z, cb);
  }

  @NotNull
  public Pointers pointers() {
    return world.pointers();
  }

  public boolean getAllowMonsters() {
    return world.getAllowMonsters();
  }

  public void loadChunk(int i, int i1) {
    world.loadChunk(i, i1);
  }

  public <T> @UnknownNullability T getOrDefaultFrom(@NotNull Pointer<T> pointer,
                                                    @NotNull Supplier<? extends T> defaultValue) {
    return world.getOrDefaultFrom(pointer, defaultValue);
  }

  @Deprecated
  public void setMonsterSpawnLimit(int i) {
    world.setMonsterSpawnLimit(i);
  }

  public void sendPluginMessage(@NotNull Plugin plugin, @NotNull String s, @NotNull byte[] bytes) {
    world.sendPluginMessage(plugin, s, bytes);
  }

  public void getChunkAtAsync(int x, int z, boolean gen, @NotNull Consumer<? super Chunk> cb) {
    world.getChunkAtAsync(x, z, gen, cb);
  }

  public boolean createExplosion(double v, double v1, double v2, float v3, boolean b) {
    return world.createExplosion(v, v1, v2, v3, b);
  }

  public boolean isBedWorks() {
    return world.isBedWorks();
  }

  public int getHighestBlockYAt(@NotNull Location location) {
    return world.getHighestBlockYAt(location);
  }

  public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz,
                                             CreatureSpawnEvent.SpawnReason reason) throws IllegalArgumentException {
    return world.spawn(location, clazz, reason);
  }

  public boolean hasCollisionsIn(@NotNull BoundingBox boundingBox) {
    return world.hasCollisionsIn(boundingBox);
  }

  @Deprecated
  public void setBiome(int i, int i1, @NotNull Biome biome) {
    world.setBiome(i, i1, biome);
  }

  @Nullable
  public RayTraceResult rayTraceEntities(@NotNull Location location, @NotNull Vector vector, double v, double v1,
                                         @Nullable Predicate<? super Entity> predicate) {
    return world.rayTraceEntities(location, vector, v, v1, predicate);
  }

  @NotNull
  public Item dropItem(@NotNull Location location, @NotNull ItemStack itemStack) {
    return world.dropItem(location, itemStack);
  }

  @Deprecated
  public void setTicksPerAmbientSpawns(int i) {
    world.setTicksPerAmbientSpawns(i);
  }

  public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz, @Nullable Consumer<
      ? super T> function) throws IllegalArgumentException {
    return world.spawn(location, clazz, function);
  }

  @Nullable
  public RayTraceResult rayTraceEntities(@NotNull Location location, @NotNull Vector vector, double v,
                                         @Nullable Predicate<? super Entity> predicate) {
    return world.rayTraceEntities(location, vector, v, predicate);
  }

  public void playSound(net.kyori.adventure.sound.@NotNull Sound sound) {
    world.playSound(sound);
  }

  public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz,
                                             CreatureSpawnEvent.SpawnReason reason,
                                             @Nullable Consumer<? super T> function) throws IllegalArgumentException {
    return world.spawn(location, clazz, reason, function);
  }

  public void setTicksPerSpawns(@NotNull SpawnCategory spawnCategory, int i) {
    world.setTicksPerSpawns(spawnCategory, i);
  }

  @Deprecated
  public int getNoTickViewDistance() {
    return world.getNoTickViewDistance();
  }

  @Nullable
  public RayTraceResult rayTraceEntities(@NotNull Location location, @NotNull Vector vector, double v, double v1) {
    return world.rayTraceEntities(location, vector, v, v1);
  }

  public boolean unloadChunkRequest(int i, int i1) {
    return world.unloadChunkRequest(i, i1);
  }

  public void showBossBar(@NotNull BossBar bar) {
    world.showBossBar(bar);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsync(@NotNull Block block) {
    return world.getChunkAtAsync(block);
  }

  public void setBiome(int i, int i1, int i2, @NotNull Biome biome) {
    world.setBiome(i, i1, i2, biome);
  }

  public void setMetadata(@NotNull String s, @NotNull MetadataValue metadataValue) {
    world.setMetadata(s, metadataValue);
  }

  public void playSound(@NotNull Location location, @NotNull String s, @NotNull SoundCategory soundCategory, float v,
                        float v1, long l) {
    world.playSound(location, s, soundCategory, v, v1, l);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                                double v2, double v3, @Nullable T t, boolean b) {
    world.spawnParticle(particle, location, i, v, v1, v2, v3, t, b);
  }

  public void stopSound(@NotNull SoundStop stop) {
    world.stopSound(stop);
  }

  @NotNull
  public BlockData getBlockData(@NotNull Location location) {
    return world.getBlockData(location);
  }

  public void removeResourcePacks(@NotNull ResourcePackRequest request) {
    world.removeResourcePacks(request);
  }

  public void setDifficulty(@NotNull Difficulty difficulty) {
    world.setDifficulty(difficulty);
  }

  @NotNull
  public Entity spawnEntity(@NotNull Location loc, @NotNull EntityType type, CreatureSpawnEvent.SpawnReason reason,
                            @Nullable Consumer<? super Entity> function) {
    return world.spawnEntity(loc, type, reason, function);
  }

  @NotNull
  public List<Raid> getRaids() {
    return world.getRaids();
  }

  @Deprecated
  public void sendMessage(@NotNull Identified source, @NotNull ComponentLike message) {
    world.sendMessage(source, message);
  }

  public void sendMessage(@NotNull Component message, ChatType.Bound boundChatType) {
    world.sendMessage(message, boundChatType);
  }

  public void sendPlayerListHeader(@NotNull Component header) {
    world.sendPlayerListHeader(header);
  }

  public long getTime() {
    return world.getTime();
  }

  public void getChunkAtAsync(@NotNull Block block, boolean gen, @NotNull Consumer<? super Chunk> cb) {
    world.getChunkAtAsync(block, gen, cb);
  }

  public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> aClass,
                                             @Nullable Consumer<? super T> consumer,
                                             CreatureSpawnEvent.SpawnReason spawnReason) throws IllegalArgumentException {
    return world.spawn(location, aClass, consumer, spawnReason);
  }

  public void removeResourcePacks(@NotNull UUID id, @NotNull UUID @NotNull ... others) {
    world.removeResourcePacks(id, others);
  }

  public void forEachAudience(@NotNull Consumer<? super Audience> action) {
    world.forEachAudience(action);
  }

  @NotNull
  public Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {
    return world.getPluginChunkTickets();
  }

  @Deprecated
  public double getHumidity(int i, int i1) {
    return world.getHumidity(i, i1);
  }

  public <T> @Nullable T getGameRuleValue(@NotNull GameRule<T> gameRule) {
    return world.getGameRuleValue(gameRule);
  }

  @Deprecated
  public long getTicksPerWaterSpawns() {
    return world.getTicksPerWaterSpawns();
  }

  public void sendResourcePacks(@NotNull ResourcePackRequest request) {
    world.sendResourcePacks(request);
  }

  @Contract("_, null -> _; _, !null -> !null")
  public <T> @Nullable T getOrDefault(@NotNull Pointer<T> pointer, @Nullable T defaultValue) {
    return world.getOrDefault(pointer, defaultValue);
  }

  @Deprecated
  public int getWaterAmbientSpawnLimit() {
    return world.getWaterAmbientSpawnLimit();
  }

  public void getChunkAtAsync(@NotNull Location loc, @NotNull Consumer<? super Chunk> cb) {
    world.getChunkAtAsync(loc, cb);
  }

  public long getGameTime() {
    return world.getGameTime();
  }

  @Deprecated
  public void setAnimalSpawnLimit(int i) {
    world.setAnimalSpawnLimit(i);
  }

  public int getClearWeatherDuration() {
    return world.getClearWeatherDuration();
  }

  @Deprecated
  public boolean setGameRuleValue(@NotNull String s, @NotNull String s1) {
    return world.setGameRuleValue(s, s1);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Block block) {
    return world.getChunkAtAsyncUrgently(block);
  }

  public long getSeed() {
    return world.getSeed();
  }

  @NotNull
  public Iterable<? extends Audience> audiences() {
    return world.audiences();
  }

  public int getTickableTileEntityCount() {
    return world.getTickableTileEntityCount();
  }

  public void deleteMessage(SignedMessage.Signature signature) {
    world.deleteMessage(signature);
  }

  public void sendPlayerListHeader(@NotNull ComponentLike header) {
    world.sendPlayerListHeader(header);
  }

  @NotNull
  public Set<String> getListeningPluginChannels() {
    return world.getListeningPluginChannels();
  }

  @NotNull
  public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz,
                                                                  @NotNull Location loc, double xRadius,
                                                                  double yRadius, double zRadius) {
    return world.getNearbyEntitiesByType(clazz, loc, xRadius, yRadius, zRadius);
  }

  @NotNull
  public Block getHighestBlockAt(@NotNull Location location) {
    return world.getHighestBlockAt(location);
  }

  public boolean isChunkGenerated(int i, int i1) {
    return world.isChunkGenerated(i, i1);
  }

  @NotNull
  public Collection<Player> getNearbyPlayers(@NotNull Location loc, double radius,
                                             @Nullable Predicate<? super Player> predicate) {
    return world.getNearbyPlayers(loc, radius, predicate);
  }

  @NotNull
  public Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox) {
    return world.getNearbyEntities(boundingBox);
  }

  @NotNull
  public Chunk getChunkAt(@NotNull Block block) {
    return world.getChunkAt(block);
  }

  public boolean createExplosion(@NotNull Entity source, float power) {
    return world.createExplosion(source, power);
  }

  public int getViewDistance() {
    return world.getViewDistance();
  }

  public boolean isPiglinSafe() {
    return world.isPiglinSafe();
  }

  public boolean createExplosion(double v, double v1, double v2, float v3, boolean b, boolean b1) {
    return world.createExplosion(v, v1, v2, v3, b, b1);
  }

  public void sendActionBar(@NotNull ComponentLike message) {
    world.sendActionBar(message);
  }

  public void playNote(@NotNull Location location, @NotNull Instrument instrument, @NotNull Note note) {
    world.playNote(location, instrument, note);
  }

  public void save() {
    world.save();
  }

  public boolean isChunkForceLoaded(int i, int i1) {
    return world.isChunkForceLoaded(i, i1);
  }

  public int getSendViewDistance() {
    return world.getSendViewDistance();
  }

  @NotNull
  public <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T> aClass) {
    return world.getEntitiesByClass(aClass);
  }

  @NotNull
  public Block getBlockAt(int i, int i1, int i2) {
    return world.getBlockAt(i, i1, i2);
  }

  @Deprecated
  @Nullable
  public WorldType getWorldType() {
    return world.getWorldType();
  }

  @Nullable
  public Raid locateNearestRaid(@NotNull Location location, int i) {
    return world.locateNearestRaid(location, i);
  }

  public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> aClass) throws IllegalArgumentException {
    return world.spawn(location, aClass);
  }

  public void deleteMessage(@NotNull SignedMessage signedMessage) {
    world.deleteMessage(signedMessage);
  }

  @NotNull
  public Chunk getChunkAt(int i, int i1) {
    return world.getChunkAt(i, i1);
  }

  public void removePluginChunkTickets(@NotNull Plugin plugin) {
    world.removePluginChunkTickets(plugin);
  }

  public void setHardcore(boolean b) {
    world.setHardcore(b);
  }

  @NotNull
  public Key key() {
    return world.key();
  }

  @NotNull
  public Entity spawnEntity(@NotNull Location loc, @NotNull EntityType type, CreatureSpawnEvent.SpawnReason reason) {
    return world.spawnEntity(loc, type, reason);
  }

  public void setType(@NotNull Location location, @NotNull Material material) {
    world.setType(location, material);
  }

  @Deprecated
  public int getAmbientSpawnLimit() {
    return world.getAmbientSpawnLimit();
  }

  public int getWeatherDuration() {
    return world.getWeatherDuration();
  }

  @Nullable
  public BiomeSearchResult locateNearestBiome(@NotNull Location location, int i, @NotNull Biome... biomes) {
    return world.locateNearestBiome(location, i, biomes);
  }

  public boolean isChunkLoaded(@NotNull Chunk chunk) {
    return world.isChunkLoaded(chunk);
  }

  @NotNull
  public BiomeProvider vanillaBiomeProvider() {
    return world.vanillaBiomeProvider();
  }

  public void setThundering(boolean b) {
    world.setThundering(b);
  }

  @NotNull
  public CompletableFuture<Chunk> getChunkAtAsync(int x, int z, boolean gen) {
    return world.getChunkAtAsync(x, z, gen);
  }

  public int getHighestBlockYAt(@NotNull Location location, @NotNull HeightMap heightMap) {
    return world.getHighestBlockYAt(location, heightMap);
  }

  @NotNull
  public Chunk getChunkAt(long chunkKey, boolean generate) {
    return world.getChunkAt(chunkKey, generate);
  }

  public boolean createExplosion(double v, double v1, double v2, float v3, boolean b, boolean b1,
                                 @Nullable Entity entity) {
    return world.createExplosion(v, v1, v2, v3, b, b1, entity);
  }

  @Deprecated
  public long getTicksPerWaterAmbientSpawns() {
    return world.getTicksPerWaterAmbientSpawns();
  }

  public boolean isFixedTime() {
    return world.isFixedTime();
  }

  public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4,
                            double v5) {
    world.spawnParticle(particle, v, v1, v2, i, v3, v4, v5);
  }

  @NotNull
  public Collection<Player> getNearbyPlayers(@NotNull Location loc, double xRadius, double yRadius, double zRadius) {
    return world.getNearbyPlayers(loc, xRadius, yRadius, zRadius);
  }

  @NotNull
  public String getName() {
    return world.getName();
  }
}
