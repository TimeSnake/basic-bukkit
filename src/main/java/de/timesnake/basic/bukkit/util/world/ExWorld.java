package de.timesnake.basic.bukkit.util.world;

import com.destroystokyo.paper.HeightmapType;
import de.timesnake.basic.bukkit.core.world.ExWorldFile;
import de.timesnake.basic.bukkit.core.world.WorldManager;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
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

public class ExWorld implements World {

    private World world;
    private final WorldManager.Type type;
    private final ExWorldFile file;

    private boolean safe;

    private boolean exceptService = true;

    private boolean allowBlockBreak = true;
    private boolean allowFluidCollect = true;
    private boolean allowBlockPlace = true;
    private boolean allowFluidPlace = true;
    private boolean allowEntityBlockBreak = true;
    private boolean allowItemFrameRotate = true;
    private boolean allowDropPickItem = true;
    private boolean allowPlayerDamage = true;
    private boolean allowFoodChange = true;
    private boolean allowEntityExplode = true;
    private boolean allowFireSpread = true;
    private boolean allowBlockBurnUp = true;
    private boolean allowBlockIgnite = true;
    private boolean allowLightUpInteraction = true;
    private boolean allowFlintAndSteel = true;
    private boolean allowPlaceInBlock = true;
    private boolean allowFirePunchOut = true;
    private boolean allowCakeEat = true;

    private List<Material> lockedBlockInventories = new LinkedList<>();

    public ExWorld(org.bukkit.World world, WorldManager.Type type, ExWorldFile file) {
        this.world = world;
        this.type = type;
        this.file = file;

        if (this.file.isSafe()) {
            this.setAutoSave(true);
            this.safe = true;
        } else {
            this.setAutoSave(false);
            this.safe = false;
        }
    }

    public void setBukkitWorld(World world) {
        this.world = world;

        this.world.setAutoSave(this.safe);
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

    @NotNull
    public static Audience empty() {
        return Audience.empty();
    }

    public void removePlayers() {
        for (User user : Server.getUsers()) {
            if (user.getLocation().getWorld().equals(this.getBukkitWorld())) {
                user.teleport(Server.getWorldManager().getBasicWorld());
            }
        }
    }

    @NotNull
    public static Audience audience(@NotNull Audience @NotNull ... audiences) {
        return Audience.audience(audiences);
    }

    public ExWorldFile getExFile() {
        return file;
    }

    @NotNull
    public static ForwardingAudience audience(@NotNull Iterable<? extends Audience> audiences) {
        return Audience.audience(audiences);
    }

    public boolean isBlockBreakAllowed() {
        return allowBlockBreak;
    }

    public void allowBlockBreak(boolean allowBlockBreak) {
        this.allowBlockBreak = allowBlockBreak;
    }

    public boolean isBlockPlaceAllowed() {
        return allowBlockPlace;
    }

    public void allowBlockPlace(boolean allowBlockPlace) {
        this.allowBlockPlace = allowBlockPlace;
    }

    public boolean isEntityBlockBreakAllowed() {
        return allowEntityBlockBreak;
    }

    public void allowEntityBlockBreak(boolean allowEntityBlockBreak) {
        this.allowEntityBlockBreak = allowEntityBlockBreak;
    }

    public boolean isItemFrameRotateAllowed() {
        return this.allowItemFrameRotate;
    }

    public void allowItemFrameRotate(boolean allowRotate) {
        this.allowItemFrameRotate = allowRotate;
    }

    public boolean isDropPickItemAllowed() {
        return allowDropPickItem;
    }

    public void allowDropPickItem(boolean allowDropPickItem) {
        this.allowDropPickItem = allowDropPickItem;
    }

    public boolean isPlayerDamageAllowed() {
        return allowPlayerDamage;
    }

    public void allowPlayerDamage(boolean allowPlayerDamage) {
        this.allowPlayerDamage = allowPlayerDamage;
    }

    public boolean isFoodChangeAllowed() {
        return allowFoodChange;
    }

    public void allowFoodChange(boolean allowFoodChange) {
        this.allowFoodChange = allowFoodChange;
    }

    public boolean isEntityExplodeAllowed() {
        return allowEntityExplode;
    }

    public void allowEntityExplode(boolean allowEntityExplode) {
        this.allowEntityExplode = allowEntityExplode;
    }

    public boolean isFireSpreadAllowed() {
        return allowFireSpread;
    }

    public void allowFireSpread(boolean allowFireSpread) {
        this.allowFireSpread = allowFireSpread;
    }

    public boolean isExceptService() {
        return exceptService;
    }

    public void setExceptService(boolean exceptService) {
        this.exceptService = exceptService;
    }

    public boolean isBlockBurnUpAllowed() {
        return allowBlockBurnUp;
    }

    public void allowBlockBurnUp(boolean allowBlockBurnUp) {
        this.allowBlockBurnUp = allowBlockBurnUp;
    }

    public boolean isBlockIgniteAllowed() {
        return allowBlockIgnite;
    }

    public void allowBlockIgnite(boolean allowBlockIgnite) {
        this.allowBlockIgnite = allowBlockIgnite;
    }

    public boolean isFluidCollectAllowed() {
        return allowFluidCollect;
    }

    public boolean isFluidPlaceAllowed() {
        return allowFluidPlace;
    }

    /**
     * Disallows to collect fluids. Except from cauldrons
     */
    public void allowFluidCollect(boolean allowFluidCollect) {
        this.allowFluidCollect = allowFluidCollect;
    }

    /**
     * Disallows to place fluids. Except into cauldrons
     */
    public void allowFluidPlace(boolean allowFluidPlace) {
        this.allowFluidPlace = allowFluidPlace;
    }

    public void allowLightUpInteraction(boolean allowLightUpInteraction) {
        this.allowLightUpInteraction = allowLightUpInteraction;
    }

    public boolean isLightUpInteractionAllowed() {
        return allowLightUpInteraction;
    }

    public boolean isFlintAndSteelAllowed() {
        return allowFlintAndSteel;
    }

    public void allowFlintAndSteel(boolean allowFlintAndSteel) {
        this.allowFlintAndSteel = allowFlintAndSteel;
    }

    public boolean isPlaceInBlockAllowed() {
        return allowPlaceInBlock;
    }

    public void allowPlaceInBlock(boolean allowPlaceInBlock) {
        this.allowPlaceInBlock = allowPlaceInBlock;
    }

    public boolean isFirePunchOutAllowed() {
        return allowFirePunchOut;
    }

    public void allowFirePunchOut(boolean allowFirePunchOut) {
        this.allowFirePunchOut = allowFirePunchOut;
    }

    public boolean isCakeEatAllowed() {
        return this.allowCakeEat;
    }

    public void allowCakeEat(boolean allowCakeEat) {
        this.allowCakeEat = allowCakeEat;
    }

    public List<Material> getLockedBlockInventories() {
        return lockedBlockInventories;
    }

    public void setLockedBlockInventories(List<Material> lockedBlockInventories) {
        this.lockedBlockInventories = lockedBlockInventories;

        if (this.lockedBlockInventories == null) {
            this.lockedBlockInventories = new LinkedList<>();
        }
    }

    public void addLockedBlockInventory(Material material) {
        this.lockedBlockInventories.add(material);
    }

    public void clearLockedBlockInventories() {
        this.lockedBlockInventories.clear();
    }

    // delegated

    @NotNull
    public static Collector<? super Audience, ?, ForwardingAudience> toAudience() {
        return Audience.toAudience();
    }

    public WorldManager.Type getType() {
        return type;
    }

    public World getBukkitWorld() {
        return this.world;
    }

    public ExBlock getExBlockAt(int x, int y, int z) {
        return new ExBlock(this.getBlockAt(x, y, z));
    }

    @Override
    public int getEntityCount() {
        return world.getEntityCount();
    }

    @Override
    public int getTileEntityCount() {
        return world.getTileEntityCount();
    }

    @Override
    public int getTickableTileEntityCount() {
        return world.getTickableTileEntityCount();
    }

    @Override
    public int getChunkCount() {
        return world.getChunkCount();
    }

    @Override
    public int getPlayerCount() {
        return world.getPlayerCount();
    }

    @Override
    @NotNull
    public MoonPhase getMoonPhase() {
        return world.getMoonPhase();
    }

    @Override
    public boolean lineOfSightExists(@NotNull Location location, @NotNull Location location1) {
        return world.lineOfSightExists(location, location1);
    }

    @Override
    @NotNull
    public Block getBlockAt(int i, int i1, int i2) {
        return world.getBlockAt(i, i1, i2);
    }

    @Override
    @NotNull
    public Block getBlockAt(@NotNull Location location) {
        return world.getBlockAt(location);
    }

    @Override
    @NotNull
    public Block getBlockAtKey(long key) {
        return world.getBlockAtKey(key);
    }

    @Override
    @NotNull
    public Location getLocationAtKey(long key) {
        return world.getLocationAtKey(key);
    }

    @Override
    public int getHighestBlockYAt(int i, int i1) {
        return world.getHighestBlockYAt(i, i1);
    }

    @Override
    public int getHighestBlockYAt(@NotNull Location location) {
        return world.getHighestBlockYAt(location);
    }

    @Override
    @NotNull
    public Block getHighestBlockAt(int i, int i1) {
        return world.getHighestBlockAt(i, i1);
    }

    @Override
    @NotNull
    public Block getHighestBlockAt(@NotNull Location location) {
        return world.getHighestBlockAt(location);
    }

    @Override
    @Deprecated
    public int getHighestBlockYAt(int i, int i1, @NotNull HeightmapType heightmapType) throws UnsupportedOperationException {
        return world.getHighestBlockYAt(i, i1, heightmapType);
    }

    @Override
    @Deprecated
    public int getHighestBlockYAt(@NotNull Location location, @NotNull HeightmapType heightmap) throws UnsupportedOperationException {
        return world.getHighestBlockYAt(location, heightmap);
    }

    @Override
    @Deprecated
    @NotNull
    public Block getHighestBlockAt(int x, int z, @NotNull HeightmapType heightmap) throws UnsupportedOperationException {
        return world.getHighestBlockAt(x, z, heightmap);
    }

    @Override
    @Deprecated
    @NotNull
    public Block getHighestBlockAt(@NotNull Location location, @NotNull HeightmapType heightmap) throws UnsupportedOperationException {
        return world.getHighestBlockAt(location, heightmap);
    }

    @Override
    public int getHighestBlockYAt(int i, int i1, @NotNull HeightMap heightMap) {
        return world.getHighestBlockYAt(i, i1, heightMap);
    }

    @Override
    public int getHighestBlockYAt(@NotNull Location location, @NotNull HeightMap heightMap) {
        return world.getHighestBlockYAt(location, heightMap);
    }

    @Override
    @NotNull
    public Block getHighestBlockAt(int i, int i1, @NotNull HeightMap heightMap) {
        return world.getHighestBlockAt(i, i1, heightMap);
    }

    @Override
    @NotNull
    public Block getHighestBlockAt(@NotNull Location location, @NotNull HeightMap heightMap) {
        return world.getHighestBlockAt(location, heightMap);
    }

    @Override
    @NotNull
    public Chunk getChunkAt(int i, int i1) {
        return world.getChunkAt(i, i1);
    }

    @Override
    @NotNull
    public Chunk getChunkAt(@NotNull Location location) {
        return world.getChunkAt(location);
    }

    @Override
    @NotNull
    public Chunk getChunkAt(@NotNull Block block) {
        return world.getChunkAt(block);
    }

    @Override
    @NotNull
    public Chunk getChunkAt(long chunkKey) {
        return world.getChunkAt(chunkKey);
    }

    @Override
    public boolean isChunkGenerated(long chunkKey) {
        return world.isChunkGenerated(chunkKey);
    }

    @Override
    public boolean isChunkLoaded(@NotNull Chunk chunk) {
        return world.isChunkLoaded(chunk);
    }

    @Override
    @NotNull
    public Chunk[] getLoadedChunks() {
        return world.getLoadedChunks();
    }

    @Override
    public void loadChunk(@NotNull Chunk chunk) {
        world.loadChunk(chunk);
    }

    @Override
    public boolean isChunkLoaded(int i, int i1) {
        return world.isChunkLoaded(i, i1);
    }

    @Override
    public boolean isChunkGenerated(int i, int i1) {
        return world.isChunkGenerated(i, i1);
    }

    @Override
    @Deprecated
    public boolean isChunkInUse(int i, int i1) {
        return world.isChunkInUse(i, i1);
    }

    @Override
    public void loadChunk(int i, int i1) {
        world.loadChunk(i, i1);
    }

    @Override
    public boolean loadChunk(int i, int i1, boolean b) {
        return world.loadChunk(i, i1, b);
    }

    @Override
    public boolean unloadChunk(@NotNull Chunk chunk) {
        return world.unloadChunk(chunk);
    }

    @Override
    public boolean unloadChunk(int i, int i1) {
        return world.unloadChunk(i, i1);
    }

    @Override
    public boolean unloadChunk(int i, int i1, boolean b) {
        return world.unloadChunk(i, i1, b);
    }

    @Override
    public boolean unloadChunkRequest(int i, int i1) {
        return world.unloadChunkRequest(i, i1);
    }

    @Override
    @Deprecated
    public boolean regenerateChunk(int i, int i1) {
        return world.regenerateChunk(i, i1);
    }

    @Override
    @Deprecated
    public boolean refreshChunk(int i, int i1) {
        return world.refreshChunk(i, i1);
    }

    @Override
    public boolean isChunkForceLoaded(int i, int i1) {
        return world.isChunkForceLoaded(i, i1);
    }

    @Override
    public void setChunkForceLoaded(int i, int i1, boolean b) {
        world.setChunkForceLoaded(i, i1, b);
    }

    @Override
    @NotNull
    public Collection<Chunk> getForceLoadedChunks() {
        return world.getForceLoadedChunks();
    }

    @Override
    public boolean addPluginChunkTicket(int i, int i1, @NotNull Plugin plugin) {
        return world.addPluginChunkTicket(i, i1, plugin);
    }

    @Override
    public boolean removePluginChunkTicket(int i, int i1, @NotNull Plugin plugin) {
        return world.removePluginChunkTicket(i, i1, plugin);
    }

    @Override
    public void removePluginChunkTickets(@NotNull Plugin plugin) {
        world.removePluginChunkTickets(plugin);
    }

    @Override
    @NotNull
    public Collection<Plugin> getPluginChunkTickets(int i, int i1) {
        return world.getPluginChunkTickets(i, i1);
    }

    @Override
    @NotNull
    public Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {
        return world.getPluginChunkTickets();
    }

    @Override
    @NotNull
    public Item dropItem(@NotNull Location location, @NotNull ItemStack itemStack) {
        return world.dropItem(location, itemStack);
    }

    @Override
    @NotNull
    public Item dropItem(@NotNull Location location, @NotNull ItemStack itemStack, @Nullable Consumer<Item> consumer) {
        return world.dropItem(location, itemStack, consumer);
    }

    @Override
    @NotNull
    public Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack itemStack) {
        return world.dropItemNaturally(location, itemStack);
    }

    @Override
    @NotNull
    public Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack itemStack, @Nullable Consumer<Item> consumer) {
        return world.dropItemNaturally(location, itemStack, consumer);
    }

    @Override
    @NotNull
    public Arrow spawnArrow(@NotNull Location location, @NotNull Vector vector, float v, float v1) {
        return world.spawnArrow(location, vector, v, v1);
    }

    @Override
    public <T extends AbstractArrow> @NotNull T spawnArrow(@NotNull Location location, @NotNull Vector vector, float v, float v1, @NotNull Class<T> aClass) {
        return world.spawnArrow(location, vector, v, v1, aClass);
    }

    @Override
    public boolean generateTree(@NotNull Location location, @NotNull TreeType treeType) {
        return world.generateTree(location, treeType);
    }

    @Override
    @Deprecated
    public boolean generateTree(@NotNull Location location, @NotNull TreeType treeType, @NotNull BlockChangeDelegate blockChangeDelegate) {
        return world.generateTree(location, treeType, blockChangeDelegate);
    }

    @Override
    @NotNull
    public LightningStrike strikeLightning(@NotNull Location location) {
        return world.strikeLightning(location);
    }

    @Override
    @NotNull
    public LightningStrike strikeLightningEffect(@NotNull Location location) {
        return world.strikeLightningEffect(location);
    }

    @Override
    @Nullable
    public Location findLightningRod(@NotNull Location location) {
        return world.findLightningRod(location);
    }

    @Override
    @Nullable
    public Location findLightningTarget(@NotNull Location location) {
        return world.findLightningTarget(location);
    }

    @Override
    @NotNull
    public List<Entity> getEntities() {
        return world.getEntities();
    }

    @Override
    @NotNull
    public List<LivingEntity> getLivingEntities() {
        return world.getLivingEntities();
    }

    @Override
    @Deprecated
    @NotNull
    public <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T>... classes) {
        return world.getEntitiesByClass(classes);
    }

    @Override
    @NotNull
    public <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T> aClass) {
        return world.getEntitiesByClass(aClass);
    }

    @Override
    @NotNull
    public Collection<Entity> getEntitiesByClasses(@NotNull Class<?>... classes) {
        return world.getEntitiesByClasses(classes);
    }

    @Override
    @NotNull
    public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double radius) {
        return world.getNearbyLivingEntities(loc, radius);
    }

    @Override
    @NotNull
    public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double xzRadius, double yRadius) {
        return world.getNearbyLivingEntities(loc, xzRadius, yRadius);
    }

    @Override
    @NotNull
    public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double xRadius, double yRadius, double zRadius) {
        return world.getNearbyLivingEntities(loc, xRadius, yRadius, zRadius);
    }

    @Override
    @NotNull
    public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double radius, @Nullable Predicate<LivingEntity> predicate) {
        return world.getNearbyLivingEntities(loc, radius, predicate);
    }

    @Override
    @NotNull
    public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double xzRadius, double yRadius, @Nullable Predicate<LivingEntity> predicate) {
        return world.getNearbyLivingEntities(loc, xzRadius, yRadius, predicate);
    }

    @Override
    @NotNull
    public Collection<LivingEntity> getNearbyLivingEntities(@NotNull Location loc, double xRadius, double yRadius, double zRadius, @Nullable Predicate<LivingEntity> predicate) {
        return world.getNearbyLivingEntities(loc, xRadius, yRadius, zRadius, predicate);
    }

    @Override
    @NotNull
    public Collection<Player> getNearbyPlayers(@NotNull Location loc, double radius) {
        return world.getNearbyPlayers(loc, radius);
    }

    @Override
    @NotNull
    public Collection<Player> getNearbyPlayers(@NotNull Location loc, double xzRadius, double yRadius) {
        return world.getNearbyPlayers(loc, xzRadius, yRadius);
    }

    @Override
    @NotNull
    public Collection<Player> getNearbyPlayers(@NotNull Location loc, double xRadius, double yRadius, double zRadius) {
        return world.getNearbyPlayers(loc, xRadius, yRadius, zRadius);
    }

    @Override
    @NotNull
    public Collection<Player> getNearbyPlayers(@NotNull Location loc, double radius, @Nullable Predicate<Player> predicate) {
        return world.getNearbyPlayers(loc, radius, predicate);
    }

    @Override
    @NotNull
    public Collection<Player> getNearbyPlayers(@NotNull Location loc, double xzRadius, double yRadius, @Nullable Predicate<Player> predicate) {
        return world.getNearbyPlayers(loc, xzRadius, yRadius, predicate);
    }

    @Override
    @NotNull
    public Collection<Player> getNearbyPlayers(@NotNull Location loc, double xRadius, double yRadius, double zRadius, @Nullable Predicate<Player> predicate) {
        return world.getNearbyPlayers(loc, xRadius, yRadius, zRadius, predicate);
    }

    @Override
    @NotNull
    public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz, @NotNull Location loc, double radius) {
        return world.getNearbyEntitiesByType(clazz, loc, radius);
    }

    @Override
    @NotNull
    public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz, @NotNull Location loc, double xzRadius, double yRadius) {
        return world.getNearbyEntitiesByType(clazz, loc, xzRadius, yRadius);
    }

    @Override
    @NotNull
    public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz, @NotNull Location loc, double xRadius, double yRadius, double zRadius) {
        return world.getNearbyEntitiesByType(clazz, loc, xRadius, yRadius, zRadius);
    }

    @Override
    @NotNull
    public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz, @NotNull Location loc, double radius, @Nullable Predicate<T> predicate) {
        return world.getNearbyEntitiesByType(clazz, loc, radius, predicate);
    }

    @Override
    @NotNull
    public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends T> clazz, @NotNull Location loc, double xzRadius, double yRadius, @Nullable Predicate<T> predicate) {
        return world.getNearbyEntitiesByType(clazz, loc, xzRadius, yRadius, predicate);
    }

    @Override
    @NotNull
    public <T extends Entity> Collection<T> getNearbyEntitiesByType(@Nullable Class<? extends Entity> clazz, @NotNull Location loc, double xRadius, double yRadius, double zRadius, @Nullable Predicate<T> predicate) {
        return world.getNearbyEntitiesByType(clazz, loc, xRadius, yRadius, zRadius, predicate);
    }

    @Override
    @Deprecated
    public void getChunkAtAsync(int x, int z, @NotNull ChunkLoadCallback cb) {
        world.getChunkAtAsync(x, z, cb);
    }

    @Override
    @Deprecated
    public void getChunkAtAsync(@NotNull Location loc, @NotNull ChunkLoadCallback cb) {
        world.getChunkAtAsync(loc, cb);
    }

    @Override
    @Deprecated
    public void getChunkAtAsync(@NotNull Block block, @NotNull ChunkLoadCallback cb) {
        world.getChunkAtAsync(block, cb);
    }

    @Override
    public void getChunkAtAsync(int x, int z, java.util.function.@NotNull Consumer<Chunk> cb) {
        world.getChunkAtAsync(x, z, cb);
    }

    @Override
    public void getChunkAtAsync(int x, int z, boolean gen, java.util.function.@NotNull Consumer<Chunk> cb) {
        world.getChunkAtAsync(x, z, gen, cb);
    }

    @Override
    public void getChunkAtAsync(@NotNull Location loc, java.util.function.@NotNull Consumer<Chunk> cb) {
        world.getChunkAtAsync(loc, cb);
    }

    @Override
    public void getChunkAtAsync(@NotNull Location loc, boolean gen, java.util.function.@NotNull Consumer<Chunk> cb) {
        world.getChunkAtAsync(loc, gen, cb);
    }

    @Override
    public void getChunkAtAsync(@NotNull Block block, java.util.function.@NotNull Consumer<Chunk> cb) {
        world.getChunkAtAsync(block, cb);
    }

    @Override
    public void getChunkAtAsync(@NotNull Block block, boolean gen, java.util.function.@NotNull Consumer<Chunk> cb) {
        world.getChunkAtAsync(block, gen, cb);
    }

    @Override
    @NotNull
    public CompletableFuture<Chunk> getChunkAtAsync(@NotNull Location loc) {
        return world.getChunkAtAsync(loc);
    }

    @Override
    @NotNull
    public CompletableFuture<Chunk> getChunkAtAsync(@NotNull Location loc, boolean gen) {
        return world.getChunkAtAsync(loc, gen);
    }

    @Override
    @NotNull
    public CompletableFuture<Chunk> getChunkAtAsync(@NotNull Block block) {
        return world.getChunkAtAsync(block);
    }

    @Override
    @NotNull
    public CompletableFuture<Chunk> getChunkAtAsync(@NotNull Block block, boolean gen) {
        return world.getChunkAtAsync(block, gen);
    }

    @Override
    @NotNull
    public CompletableFuture<Chunk> getChunkAtAsync(int x, int z) {
        return world.getChunkAtAsync(x, z);
    }

    @Override
    @NotNull
    public CompletableFuture<Chunk> getChunkAtAsync(int x, int z, boolean gen) {
        return world.getChunkAtAsync(x, z, gen);
    }

    @Override
    @NotNull
    public CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Location loc) {
        return world.getChunkAtAsyncUrgently(loc);
    }

    @Override
    @NotNull
    public CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Location loc, boolean gen) {
        return world.getChunkAtAsyncUrgently(loc, gen);
    }

    @Override
    @NotNull
    public CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Block block) {
        return world.getChunkAtAsyncUrgently(block);
    }

    @Override
    @NotNull
    public CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Block block, boolean gen) {
        return world.getChunkAtAsyncUrgently(block, gen);
    }

    @Override
    @NotNull
    public CompletableFuture<Chunk> getChunkAtAsyncUrgently(int x, int z) {
        return world.getChunkAtAsyncUrgently(x, z);
    }

    @Override
    @NotNull
    public CompletableFuture<Chunk> getChunkAtAsync(int i, int i1, boolean b, boolean b1) {
        return world.getChunkAtAsync(i, i1, b, b1);
    }

    @Override
    @NotNull
    public NamespacedKey getKey() {
        return world.getKey();
    }

    @Override
    @NotNull
    public List<Player> getPlayers() {
        return world.getPlayers();
    }

    @Override
    @NotNull
    public Iterable<? extends Audience> audiences() {
        return world.audiences();
    }

    @Override
    @NotNull
    public Collection<Entity> getNearbyEntities(@NotNull Location location, double v, double v1, double v2) {
        return world.getNearbyEntities(location, v, v1, v2);
    }

    @Override
    @Nullable
    public Entity getEntity(@NotNull UUID uuid) {
        return world.getEntity(uuid);
    }

    @Override
    @NotNull
    public Collection<Entity> getNearbyEntities(@NotNull Location location, double v, double v1, double v2, @Nullable Predicate<Entity> predicate) {
        return world.getNearbyEntities(location, v, v1, v2, predicate);
    }

    @Override
    @NotNull
    public Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox) {
        return world.getNearbyEntities(boundingBox);
    }

    @Override
    @NotNull
    public Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox, @Nullable Predicate<Entity> predicate) {
        return world.getNearbyEntities(boundingBox, predicate);
    }

    @Override
    @Nullable
    public RayTraceResult rayTraceEntities(@NotNull Location location, @NotNull Vector vector, double v) {
        return world.rayTraceEntities(location, vector, v);
    }

    @Override
    @Nullable
    public RayTraceResult rayTraceEntities(@NotNull Location location, @NotNull Vector vector, double v, double v1) {
        return world.rayTraceEntities(location, vector, v, v1);
    }

    @Override
    @Nullable
    public RayTraceResult rayTraceEntities(@NotNull Location location, @NotNull Vector vector, double v, @Nullable Predicate<Entity> predicate) {
        return world.rayTraceEntities(location, vector, v, predicate);
    }

    @Override
    @Nullable
    public RayTraceResult rayTraceEntities(@NotNull Location location, @NotNull Vector vector, double v, double v1, @Nullable Predicate<Entity> predicate) {
        return world.rayTraceEntities(location, vector, v, v1, predicate);
    }

    @Override
    @Nullable
    public RayTraceResult rayTraceBlocks(@NotNull Location location, @NotNull Vector vector, double v) {
        return world.rayTraceBlocks(location, vector, v);
    }

    @Override
    @Nullable
    public RayTraceResult rayTraceBlocks(@NotNull Location location, @NotNull Vector vector, double v, @NotNull FluidCollisionMode fluidCollisionMode) {
        return world.rayTraceBlocks(location, vector, v, fluidCollisionMode);
    }

    @Override
    @Nullable
    public RayTraceResult rayTraceBlocks(@NotNull Location location, @NotNull Vector vector, double v, @NotNull FluidCollisionMode fluidCollisionMode, boolean b) {
        return world.rayTraceBlocks(location, vector, v, fluidCollisionMode, b);
    }

    @Override
    @Nullable
    public RayTraceResult rayTrace(@NotNull Location location, @NotNull Vector vector, double v, @NotNull FluidCollisionMode fluidCollisionMode, boolean b, double v1, @Nullable Predicate<Entity> predicate) {
        return world.rayTrace(location, vector, v, fluidCollisionMode, b, v1, predicate);
    }

    @Override
    @NotNull
    public Location getSpawnLocation() {
        return world.getSpawnLocation();
    }

    @Override
    public boolean setSpawnLocation(@NotNull Location location) {
        return world.setSpawnLocation(location);
    }

    @Override
    public boolean setSpawnLocation(int i, int i1, int i2, float v) {
        return world.setSpawnLocation(i, i1, i2, v);
    }

    @Override
    public boolean setSpawnLocation(int i, int i1, int i2) {
        return world.setSpawnLocation(i, i1, i2);
    }

    @Override
    public long getTime() {
        return world.getTime();
    }

    @Override
    public void setTime(long l) {
        world.setTime(l);
    }

    @Override
    public long getFullTime() {
        return world.getFullTime();
    }

    @Override
    public void setFullTime(long l) {
        world.setFullTime(l);
    }

    @Override
    public boolean isDayTime() {
        return world.isDayTime();
    }

    @Override
    public long getGameTime() {
        return world.getGameTime();
    }

    @Override
    public boolean hasStorm() {
        return world.hasStorm();
    }

    @Override
    public void setStorm(boolean b) {
        world.setStorm(b);
    }

    @Override
    public int getWeatherDuration() {
        return world.getWeatherDuration();
    }

    @Override
    public void setWeatherDuration(int i) {
        world.setWeatherDuration(i);
    }

    @Override
    public boolean isThundering() {
        return world.isThundering();
    }

    @Override
    public void setThundering(boolean b) {
        world.setThundering(b);
    }

    @Override
    public int getThunderDuration() {
        return world.getThunderDuration();
    }

    @Override
    public void setThunderDuration(int i) {
        world.setThunderDuration(i);
    }

    @Override
    public boolean isClearWeather() {
        return world.isClearWeather();
    }

    @Override
    public int getClearWeatherDuration() {
        return world.getClearWeatherDuration();
    }

    @Override
    public void setClearWeatherDuration(int i) {
        world.setClearWeatherDuration(i);
    }

    @Override
    public boolean createExplosion(double v, double v1, double v2, float v3) {
        return world.createExplosion(v, v1, v2, v3);
    }

    @Override
    public boolean createExplosion(double v, double v1, double v2, float v3, boolean b) {
        return world.createExplosion(v, v1, v2, v3, b);
    }

    @Override
    public boolean createExplosion(double v, double v1, double v2, float v3, boolean b, boolean b1) {
        return world.createExplosion(v, v1, v2, v3, b, b1);
    }

    @Override
    public boolean createExplosion(double v, double v1, double v2, float v3, boolean b, boolean b1, @Nullable Entity entity) {
        return world.createExplosion(v, v1, v2, v3, b, b1, entity);
    }

    @Override
    public boolean createExplosion(@NotNull Location location, float v) {
        return world.createExplosion(location, v);
    }

    @Override
    public boolean createExplosion(@NotNull Location location, float v, boolean b) {
        return world.createExplosion(location, v, b);
    }

    @Override
    public boolean createExplosion(@Nullable Entity entity, @NotNull Location location, float v, boolean b, boolean b1) {
        return world.createExplosion(entity, location, v, b, b1);
    }

    @Override
    public boolean createExplosion(@Nullable Entity source, @NotNull Location loc, float power, boolean setFire) {
        return world.createExplosion(source, loc, power, setFire);
    }

    @Override
    public boolean createExplosion(@Nullable Entity source, @NotNull Location loc, float power) {
        return world.createExplosion(source, loc, power);
    }

    @Override
    public boolean createExplosion(@NotNull Entity source, float power, boolean setFire, boolean breakBlocks) {
        return world.createExplosion(source, power, setFire, breakBlocks);
    }

    @Override
    public boolean createExplosion(@NotNull Entity source, float power, boolean setFire) {
        return world.createExplosion(source, power, setFire);
    }

    @Override
    public boolean createExplosion(@NotNull Entity source, float power) {
        return world.createExplosion(source, power);
    }

    @Override
    public boolean createExplosion(@NotNull Location location, float v, boolean b, boolean b1) {
        return world.createExplosion(location, v, b, b1);
    }

    @Override
    public boolean createExplosion(@NotNull Location location, float v, boolean b, boolean b1, @Nullable Entity entity) {
        return world.createExplosion(location, v, b, b1, entity);
    }

    @Override
    public boolean getPVP() {
        return world.getPVP();
    }

    @Override
    public void setPVP(boolean b) {
        world.setPVP(b);
    }

    @Override
    @Nullable
    public ChunkGenerator getGenerator() {
        return world.getGenerator();
    }

    @Override
    @Nullable
    public BiomeProvider getBiomeProvider() {
        return world.getBiomeProvider();
    }

    @Override
    public void save() {
        world.save();
    }

    @Override
    @NotNull
    public List<BlockPopulator> getPopulators() {
        return world.getPopulators();
    }

    @Override
    @NotNull
    public FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull MaterialData materialData) throws IllegalArgumentException {
        return world.spawnFallingBlock(location, materialData);
    }

    @Override
    @NotNull
    public FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull BlockData blockData) throws IllegalArgumentException {
        return world.spawnFallingBlock(location, blockData);
    }

    @Override
    @Deprecated
    @NotNull
    public FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull Material material, byte b) throws IllegalArgumentException {
        return world.spawnFallingBlock(location, material, b);
    }

    @Override
    public void playEffect(@NotNull Location location, @NotNull Effect effect, int i) {
        world.playEffect(location, effect, i);
    }

    @Override
    public void playEffect(@NotNull Location location, @NotNull Effect effect, int i, int i1) {
        world.playEffect(location, effect, i, i1);
    }

    @Override
    public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T t) {
        world.playEffect(location, effect, t);
    }

    @Override
    public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T t, int i) {
        world.playEffect(location, effect, t, i);
    }

    @Override
    @NotNull
    public ChunkSnapshot getEmptyChunkSnapshot(int i, int i1, boolean b, boolean b1) {
        return world.getEmptyChunkSnapshot(i, i1, b, b1);
    }

    @Override
    public void setSpawnFlags(boolean b, boolean b1) {
        world.setSpawnFlags(b, b1);
    }

    @Override
    public boolean getAllowAnimals() {
        return world.getAllowAnimals();
    }

    @Override
    public boolean getAllowMonsters() {
        return world.getAllowMonsters();
    }

    @Override
    @Deprecated
    @NotNull
    public Biome getBiome(int i, int i1) {
        return world.getBiome(i, i1);
    }

    @Override
    @Deprecated
    public void setBiome(int i, int i1, @NotNull Biome biome) {
        world.setBiome(i, i1, biome);
    }

    @Override
    @Deprecated
    public double getTemperature(int i, int i1) {
        return world.getTemperature(i, i1);
    }

    @Override
    public double getTemperature(int i, int i1, int i2) {
        return world.getTemperature(i, i1, i2);
    }

    @Override
    @Deprecated
    public double getHumidity(int i, int i1) {
        return world.getHumidity(i, i1);
    }

    @Override
    public double getHumidity(int i, int i1, int i2) {
        return world.getHumidity(i, i1, i2);
    }

    @Override
    public int getLogicalHeight() {
        return world.getLogicalHeight();
    }

    @Override
    public boolean isNatural() {
        return world.isNatural();
    }

    @Override
    public boolean isBedWorks() {
        return world.isBedWorks();
    }

    @Override
    public boolean hasSkyLight() {
        return world.hasSkyLight();
    }

    @Override
    public boolean hasCeiling() {
        return world.hasCeiling();
    }

    @Override
    public boolean isPiglinSafe() {
        return world.isPiglinSafe();
    }

    @Override
    public boolean isRespawnAnchorWorks() {
        return world.isRespawnAnchorWorks();
    }

    @Override
    public boolean hasRaids() {
        return world.hasRaids();
    }

    @Override
    public boolean isUltraWarm() {
        return world.isUltraWarm();
    }

    @Override
    public int getSeaLevel() {
        return world.getSeaLevel();
    }

    @Override
    public boolean getKeepSpawnInMemory() {
        return world.getKeepSpawnInMemory();
    }

    @Override
    public void setKeepSpawnInMemory(boolean b) {
        world.setKeepSpawnInMemory(b);
    }

    @Override
    public boolean isAutoSave() {
        return world.isAutoSave();
    }

    @Override
    public void setAutoSave(boolean b) {
        world.setAutoSave(b);
    }

    @Override
    @NotNull
    public Difficulty getDifficulty() {
        return world.getDifficulty();
    }

    @Override
    public void setDifficulty(@NotNull Difficulty difficulty) {
        world.setDifficulty(difficulty);
    }

    @Override
    @NotNull
    public File getWorldFolder() {
        return world.getWorldFolder();
    }

    @Override
    @Deprecated
    @Nullable
    public WorldType getWorldType() {
        return world.getWorldType();
    }

    @Override
    public boolean canGenerateStructures() {
        return world.canGenerateStructures();
    }

    @Override
    public boolean isHardcore() {
        return world.isHardcore();
    }

    @Override
    public void setHardcore(boolean b) {
        world.setHardcore(b);
    }

    @Override
    @Deprecated
    public long getTicksPerAnimalSpawns() {
        return world.getTicksPerAnimalSpawns();
    }

    @Override
    @Deprecated
    public void setTicksPerAnimalSpawns(int i) {
        world.setTicksPerAnimalSpawns(i);
    }

    @Override
    @Deprecated
    public long getTicksPerMonsterSpawns() {
        return world.getTicksPerMonsterSpawns();
    }

    @Override
    @Deprecated
    public void setTicksPerMonsterSpawns(int i) {
        world.setTicksPerMonsterSpawns(i);
    }

    @Override
    @Deprecated
    public long getTicksPerWaterSpawns() {
        return world.getTicksPerWaterSpawns();
    }

    @Override
    @Deprecated
    public void setTicksPerWaterSpawns(int i) {
        world.setTicksPerWaterSpawns(i);
    }

    @Override
    @Deprecated
    public long getTicksPerWaterAmbientSpawns() {
        return world.getTicksPerWaterAmbientSpawns();
    }

    @Override
    @Deprecated
    public void setTicksPerWaterAmbientSpawns(int i) {
        world.setTicksPerWaterAmbientSpawns(i);
    }

    @Override
    @Deprecated
    public long getTicksPerWaterUndergroundCreatureSpawns() {
        return world.getTicksPerWaterUndergroundCreatureSpawns();
    }

    @Override
    @Deprecated
    public void setTicksPerWaterUndergroundCreatureSpawns(int i) {
        world.setTicksPerWaterUndergroundCreatureSpawns(i);
    }

    @Override
    @Deprecated
    public long getTicksPerAmbientSpawns() {
        return world.getTicksPerAmbientSpawns();
    }

    @Override
    @Deprecated
    public void setTicksPerAmbientSpawns(int i) {
        world.setTicksPerAmbientSpawns(i);
    }

    @Override
    public long getTicksPerSpawns(@NotNull SpawnCategory spawnCategory) {
        return world.getTicksPerSpawns(spawnCategory);
    }

    @Override
    public void setTicksPerSpawns(@NotNull SpawnCategory spawnCategory, int i) {
        world.setTicksPerSpawns(spawnCategory, i);
    }

    @Override
    @Deprecated
    public int getMonsterSpawnLimit() {
        return world.getMonsterSpawnLimit();
    }

    @Override
    @Deprecated
    public void setMonsterSpawnLimit(int i) {
        world.setMonsterSpawnLimit(i);
    }

    @Override
    @Deprecated
    public int getAnimalSpawnLimit() {
        return world.getAnimalSpawnLimit();
    }

    @Override
    @Deprecated
    public void setAnimalSpawnLimit(int i) {
        world.setAnimalSpawnLimit(i);
    }

    @Override
    @Deprecated
    public int getWaterAnimalSpawnLimit() {
        return world.getWaterAnimalSpawnLimit();
    }

    @Override
    @Deprecated
    public void setWaterAnimalSpawnLimit(int i) {
        world.setWaterAnimalSpawnLimit(i);
    }

    @Override
    @Deprecated
    public int getWaterUndergroundCreatureSpawnLimit() {
        return world.getWaterUndergroundCreatureSpawnLimit();
    }

    @Override
    @Deprecated
    public void setWaterUndergroundCreatureSpawnLimit(int i) {
        world.setWaterUndergroundCreatureSpawnLimit(i);
    }

    @Override
    @Deprecated
    public int getWaterAmbientSpawnLimit() {
        return world.getWaterAmbientSpawnLimit();
    }

    @Override
    @Deprecated
    public void setWaterAmbientSpawnLimit(int i) {
        world.setWaterAmbientSpawnLimit(i);
    }

    @Override
    @Deprecated
    public int getAmbientSpawnLimit() {
        return world.getAmbientSpawnLimit();
    }

    @Override
    @Deprecated
    public void setAmbientSpawnLimit(int i) {
        world.setAmbientSpawnLimit(i);
    }

    @Override
    public int getSpawnLimit(@NotNull SpawnCategory spawnCategory) {
        return world.getSpawnLimit(spawnCategory);
    }

    @Override
    public void setSpawnLimit(@NotNull SpawnCategory spawnCategory, int i) {
        world.setSpawnLimit(spawnCategory, i);
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull Sound sound, float v, float v1) {
        world.playSound(location, sound, v, v1);
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull String s, float v, float v1) {
        world.playSound(location, s, v, v1);
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory soundCategory, float v, float v1) {
        world.playSound(location, sound, soundCategory, v, v1);
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull String s, @NotNull SoundCategory soundCategory, float v, float v1) {
        world.playSound(location, s, soundCategory, v, v1);
    }

    @Override
    public void playSound(@NotNull Entity entity, @NotNull Sound sound, float v, float v1) {
        world.playSound(entity, sound, v, v1);
    }

    @Override
    public void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory soundCategory, float v, float v1) {
        world.playSound(entity, sound, soundCategory, v, v1);
    }

    @Override
    @NotNull
    public String[] getGameRules() {
        return world.getGameRules();
    }

    @Override
    @Contract("null -> null; !null -> !null")
    @Deprecated
    @Nullable
    public String getGameRuleValue(@Nullable String s) {
        return world.getGameRuleValue(s);
    }

    @Override
    @Deprecated
    public boolean setGameRuleValue(@NotNull String s, @NotNull String s1) {
        return world.setGameRuleValue(s, s1);
    }

    @Override
    public boolean isGameRule(@NotNull String s) {
        return world.isGameRule(s);
    }

    @Override
    public <T> @Nullable T getGameRuleValue(@NotNull GameRule<T> gameRule) {
        return world.getGameRuleValue(gameRule);
    }

    @Override
    public <T> @Nullable T getGameRuleDefault(@NotNull GameRule<T> gameRule) {
        return world.getGameRuleDefault(gameRule);
    }

    @Override
    public <T> boolean setGameRule(@NotNull GameRule<T> gameRule, @NotNull T t) {
        return world.setGameRule(gameRule, t);
    }

    @Override
    @NotNull
    public WorldBorder getWorldBorder() {
        return world.getWorldBorder();
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i) {
        world.spawnParticle(particle, location, i);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i) {
        world.spawnParticle(particle, v, v1, v2, i);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, @Nullable T t) {
        world.spawnParticle(particle, location, i, t);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, @Nullable T t) {
        world.spawnParticle(particle, v, v1, v2, i, t);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1, double v2) {
        world.spawnParticle(particle, location, i, v, v1, v2);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5) {
        world.spawnParticle(particle, v, v1, v2, i, v3, v4, v5);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1, double v2, @Nullable T t) {
        world.spawnParticle(particle, location, i, v, v1, v2, t);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, @Nullable T t) {
        world.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, t);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1, double v2, double v3) {
        world.spawnParticle(particle, location, i, v, v1, v2, v3);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6) {
        world.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1, double v2, double v3, @Nullable T t) {
        world.spawnParticle(particle, location, i, v, v1, v2, v3, t);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {
        world.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @Nullable List<Player> receivers, @NotNull Player source, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {
        world.spawnParticle(particle, receivers, source, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @Nullable List<Player> list, @Nullable Player player, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6, @Nullable T t, boolean b) {
        world.spawnParticle(particle, list, player, v, v1, v2, i, v3, v4, v5, v6, t, b);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1, double v2, double v3, @Nullable T t, boolean b) {
        world.spawnParticle(particle, location, i, v, v1, v2, v3, t, b);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6, @Nullable T t, boolean b) {
        world.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6, t, b);
    }

    @Override
    @Nullable
    public Location locateNearestStructure(@NotNull Location location, @NotNull StructureType structureType, int i, boolean b) {
        return world.locateNearestStructure(location, structureType, i, b);
    }

    @Override
    @Nullable
    public Location locateNearestBiome(@NotNull Location location, @NotNull Biome biome, int i) {
        return world.locateNearestBiome(location, biome, i);
    }

    @Override
    @Nullable
    public Location locateNearestBiome(@NotNull Location location, @NotNull Biome biome, int i, int i1) {
        return world.locateNearestBiome(location, biome, i, i1);
    }

    @Override
    @Deprecated
    public boolean isUltrawarm() {
        return world.isUltrawarm();
    }

    @Override
    public double getCoordinateScale() {
        return world.getCoordinateScale();
    }

    @Override
    @Deprecated
    public boolean hasSkylight() {
        return world.hasSkylight();
    }

    @Override
    @Deprecated
    public boolean hasBedrockCeiling() {
        return world.hasBedrockCeiling();
    }

    @Override
    @Deprecated
    public boolean doesBedWork() {
        return world.doesBedWork();
    }

    @Override
    @Deprecated
    public boolean doesRespawnAnchorWork() {
        return world.doesRespawnAnchorWork();
    }

    @Override
    public boolean isFixedTime() {
        return world.isFixedTime();
    }

    @Override
    @NotNull
    public Collection<Material> getInfiniburn() {
        return world.getInfiniburn();
    }

    @Override
    public void sendGameEvent(@Nullable Entity entity, @NotNull GameEvent gameEvent, @NotNull Vector vector) {
        world.sendGameEvent(entity, gameEvent, vector);
    }

    @Override
    public int getViewDistance() {
        return world.getViewDistance();
    }

    @Override
    public void setViewDistance(int i) {
        world.setViewDistance(i);
    }

    @Override
    public int getSimulationDistance() {
        return world.getSimulationDistance();
    }

    @Override
    public void setSimulationDistance(int i) {
        world.setSimulationDistance(i);
    }

    @Override
    @Deprecated
    public int getNoTickViewDistance() {
        return world.getNoTickViewDistance();
    }

    @Override
    @Deprecated
    public void setNoTickViewDistance(int i) {
        world.setNoTickViewDistance(i);
    }

    @Override
    public int getSendViewDistance() {
        return world.getSendViewDistance();
    }

    @Override
    public void setSendViewDistance(int i) {
        world.setSendViewDistance(i);
    }

    @Override
    @NotNull
    public Spigot spigot() {
        return world.spigot();
    }

    @Override
    @Nullable
    public Raid locateNearestRaid(@NotNull Location location, int i) {
        return world.locateNearestRaid(location, i);
    }

    @Override
    @NotNull
    public List<Raid> getRaids() {
        return world.getRaids();
    }

    @Override
    @Nullable
    public DragonBattle getEnderDragonBattle() {
        return world.getEnderDragonBattle();
    }

    @Override
    @NotNull
    public Biome getBiome(@NotNull Location location) {
        return world.getBiome(location);
    }

    @Override
    @NotNull
    public Biome getBiome(int i, int i1, int i2) {
        return world.getBiome(i, i1, i2);
    }

    @Override
    public @NotNull Biome getComputedBiome(int i, int i1, int i2) {
        return world.getComputedBiome(i, i1, i2);
    }

    @Override
    public void setBiome(@NotNull Location location, @NotNull Biome biome) {
        world.setBiome(location, biome);
    }

    @Override
    public void setBiome(int i, int i1, int i2, @NotNull Biome biome) {
        world.setBiome(i, i1, i2, biome);
    }

    @Override
    @NotNull
    public BlockState getBlockState(@NotNull Location location) {
        return world.getBlockState(location);
    }

    @Override
    @NotNull
    public BlockState getBlockState(int i, int i1, int i2) {
        return world.getBlockState(i, i1, i2);
    }

    @Override
    @NotNull
    public BlockData getBlockData(@NotNull Location location) {
        return world.getBlockData(location);
    }

    @Override
    @NotNull
    public BlockData getBlockData(int i, int i1, int i2) {
        return world.getBlockData(i, i1, i2);
    }

    @Override
    @NotNull
    public Material getType(@NotNull Location location) {
        return world.getType(location);
    }

    @Override
    @NotNull
    public Material getType(int i, int i1, int i2) {
        return world.getType(i, i1, i2);
    }

    @Override
    public void setBlockData(@NotNull Location location, @NotNull BlockData blockData) {
        world.setBlockData(location, blockData);
    }

    @Override
    public void setBlockData(int i, int i1, int i2, @NotNull BlockData blockData) {
        world.setBlockData(i, i1, i2, blockData);
    }

    @Override
    public void setType(@NotNull Location location, @NotNull Material material) {
        world.setType(location, material);
    }

    @Override
    public void setType(int i, int i1, int i2, @NotNull Material material) {
        world.setType(i, i1, i2, material);
    }

    @Override
    public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType treeType) {
        return world.generateTree(location, random, treeType);
    }

    @Override
    public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType treeType, @Nullable Consumer<BlockState> consumer) {
        return world.generateTree(location, random, treeType, consumer);
    }

    @Override
    public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType treeType, @Nullable Predicate<BlockState> predicate) {
        return world.generateTree(location, random, treeType, predicate);
    }

    @Override
    @NotNull
    public Entity spawnEntity(@NotNull Location location, @NotNull EntityType entityType) {
        return world.spawnEntity(location, entityType);
    }

    @Override
    @NotNull
    public Entity spawnEntity(@NotNull Location location, @NotNull EntityType entityType, boolean b) {
        return world.spawnEntity(location, entityType, b);
    }

    @Override
    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> aClass) throws IllegalArgumentException {
        return world.spawn(location, aClass);
    }

    @Override
    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz, @Nullable Consumer<T> function) throws IllegalArgumentException {
        return world.spawn(location, clazz, function);
    }

    @Override
    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz, CreatureSpawnEvent.@NotNull SpawnReason reason) throws IllegalArgumentException {
        return world.spawn(location, clazz, reason);
    }

    @Override
    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz, CreatureSpawnEvent.@NotNull SpawnReason reason, @Nullable Consumer<T> function) throws IllegalArgumentException {
        return world.spawn(location, clazz, reason, function);
    }

    @Override
    @NotNull
    public Entity spawnEntity(@NotNull Location loc, @NotNull EntityType type, CreatureSpawnEvent.@NotNull SpawnReason reason) {
        return world.spawnEntity(loc, type, reason);
    }

    @Override
    @NotNull
    public Entity spawnEntity(@NotNull Location loc, @NotNull EntityType type, CreatureSpawnEvent.@NotNull SpawnReason reason, @Nullable Consumer<Entity> function) {
        return world.spawnEntity(loc, type, reason, function);
    }

    @Override
    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> aClass, @Nullable Consumer<T> consumer, CreatureSpawnEvent.@NotNull SpawnReason spawnReason) throws IllegalArgumentException {
        return world.spawn(location, aClass, consumer, spawnReason);
    }

    @Override
    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> aClass, boolean b, @Nullable Consumer<T> consumer) throws IllegalArgumentException {
        return world.spawn(location, aClass, b, consumer);
    }

    @Override
    @NotNull
    public String getName() {
        return world.getName();
    }

    @Override
    @NotNull
    public UUID getUID() {
        return world.getUID();
    }

    @Override
    @NotNull
    public Environment getEnvironment() {
        return world.getEnvironment();
    }

    @Override
    public long getSeed() {
        return world.getSeed();
    }

    @Override
    public int getMinHeight() {
        return world.getMinHeight();
    }

    @Override
    public int getMaxHeight() {
        return world.getMaxHeight();
    }

    @Override
    @NotNull
    public BiomeProvider vanillaBiomeProvider() {
        return world.vanillaBiomeProvider();
    }

    @Override
    public void sendPluginMessage(@NotNull Plugin plugin, @NotNull String s, @NotNull byte[] bytes) {
        world.sendPluginMessage(plugin, s, bytes);
    }

    @Override
    @NotNull
    public Set<String> getListeningPluginChannels() {
        return world.getListeningPluginChannels();
    }

    @Override
    public void setMetadata(@NotNull String s, @NotNull MetadataValue metadataValue) {
        world.setMetadata(s, metadataValue);
    }

    @Override
    @NotNull
    public List<MetadataValue> getMetadata(@NotNull String s) {
        return world.getMetadata(s);
    }

    @Override
    public boolean hasMetadata(@NotNull String s) {
        return world.hasMetadata(s);
    }

    @Override
    public void removeMetadata(@NotNull String s, @NotNull Plugin plugin) {
        world.removeMetadata(s, plugin);
    }

    @Override
    @NotNull
    public PersistentDataContainer getPersistentDataContainer() {
        return world.getPersistentDataContainer();
    }

    @Override
    @NotNull
    public Pointers pointers() {
        return world.pointers();
    }

    @Override
    @NotNull
    public Audience filterAudience(@NotNull Predicate<? super Audience> filter) {
        return world.filterAudience(filter);
    }

    @Override
    public void forEachAudience(java.util.function.@NotNull Consumer<? super Audience> action) {
        world.forEachAudience(action);
    }

    @Override
    public void sendMessage(@NotNull Identified source, @NotNull Component message, @NotNull MessageType type) {
        world.sendMessage(source, message, type);
    }

    @Override
    public void sendMessage(@NotNull Identity source, @NotNull Component message, @NotNull MessageType type) {
        world.sendMessage(source, message, type);
    }

    @Override
    public void sendActionBar(@NotNull Component message) {
        world.sendActionBar(message);
    }

    @Override
    public void sendPlayerListHeader(@NotNull Component header) {
        world.sendPlayerListHeader(header);
    }

    @Override
    public void sendPlayerListFooter(@NotNull Component footer) {
        world.sendPlayerListFooter(footer);
    }

    @Override
    public void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
        world.sendPlayerListHeaderAndFooter(header, footer);
    }

    @Override
    public <T> void sendTitlePart(@NotNull TitlePart<T> part, @NotNull T value) {
        world.sendTitlePart(part, value);
    }

    @Override
    public void clearTitle() {
        world.clearTitle();
    }

    @Override
    public void resetTitle() {
        world.resetTitle();
    }

    @Override
    public void showBossBar(@NotNull BossBar bar) {
        world.showBossBar(bar);
    }

    @Override
    public void hideBossBar(@NotNull BossBar bar) {
        world.hideBossBar(bar);
    }

    @Override
    public void playSound(net.kyori.adventure.sound.@NotNull Sound sound) {
        world.playSound(sound);
    }

    @Override
    public void playSound(net.kyori.adventure.sound.@NotNull Sound sound, double x, double y, double z) {
        world.playSound(sound, x, y, z);
    }

    @Override
    public void playSound(net.kyori.adventure.sound.@NotNull Sound sound, net.kyori.adventure.sound.Sound.@NotNull Emitter emitter) {
        world.playSound(sound, emitter);
    }

    @Override
    public void stopSound(@NotNull SoundStop stop) {
        world.stopSound(stop);
    }

    @Override
    public void openBook(@NotNull Book book) {
        world.openBook(book);
    }

    @Override
    public void sendMessage(@NotNull ComponentLike message) {
        world.sendMessage(message);
    }

    @Override
    public void sendMessage(@NotNull Identified source, @NotNull ComponentLike message) {
        world.sendMessage(source, message);
    }

    @Override
    public void sendMessage(@NotNull Identity source, @NotNull ComponentLike message) {
        world.sendMessage(source, message);
    }

    @Override
    public void sendMessage(@NotNull Component message) {
        world.sendMessage(message);
    }

    @Override
    public void sendMessage(@NotNull Identified source, @NotNull Component message) {
        world.sendMessage(source, message);
    }

    @Override
    public void sendMessage(@NotNull Identity source, @NotNull Component message) {
        world.sendMessage(source, message);
    }

    @Override
    public void sendMessage(@NotNull ComponentLike message, @NotNull MessageType type) {
        world.sendMessage(message, type);
    }

    @Override
    public void sendMessage(@NotNull Identified source, @NotNull ComponentLike message, @NotNull MessageType type) {
        world.sendMessage(source, message, type);
    }

    @Override
    public void sendMessage(@NotNull Identity source, @NotNull ComponentLike message, @NotNull MessageType type) {
        world.sendMessage(source, message, type);
    }

    @Override
    public void sendMessage(@NotNull Component message, @NotNull MessageType type) {
        world.sendMessage(message, type);
    }

    @Override
    public void sendActionBar(@NotNull ComponentLike message) {
        world.sendActionBar(message);
    }

    @Override
    public void sendPlayerListHeader(@NotNull ComponentLike header) {
        world.sendPlayerListHeader(header);
    }

    @Override
    public void sendPlayerListFooter(@NotNull ComponentLike footer) {
        world.sendPlayerListFooter(footer);
    }

    @Override
    public void sendPlayerListHeaderAndFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
        world.sendPlayerListHeaderAndFooter(header, footer);
    }

    @Override
    public void showTitle(@NotNull Title title) {
        world.showTitle(title);
    }

    @Override
    public void stopSound(net.kyori.adventure.sound.@NotNull Sound sound) {
        world.stopSound(sound);
    }

    @Override
    public void openBook(Book.@NotNull Builder book) {
        world.openBook(book);
    }

    @Override
    @NotNull
    public <T> Optional<T> get(@NotNull Pointer<T> pointer) {
        return world.get(pointer);
    }

    @Override
    @Contract("_, null -> _; _, !null -> !null")
    public <T> @Nullable T getOrDefault(@NotNull Pointer<T> pointer, @Nullable T defaultValue) {
        return world.getOrDefault(pointer, defaultValue);
    }

    @Override
    public <T> @UnknownNullability T getOrDefaultFrom(@NotNull Pointer<T> pointer, @NotNull Supplier<? extends T> defaultValue) {
        return world.getOrDefaultFrom(pointer, defaultValue);
    }

    @Override
    @NotNull
    public Key key() {
        return world.key();
    }
}
