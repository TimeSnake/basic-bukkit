package de.timesnake.basic.bukkit.util.world;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.core.world.ExWorldFile;
import de.timesnake.basic.bukkit.core.world.WorldManager;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.EntityDamageByUserEvent;
import io.papermc.paper.world.MoonPhase;
import net.kyori.adventure.audience.Audience;
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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
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

public class ExWorld implements Listener {

    private World world;
    private final WorldManager.Type type;
    private final ExWorldFile file;

    private boolean safe;

    private boolean exceptService = true;

    private boolean allowBlockBreak = true;
    private boolean allowBlockPlace = true;
    private boolean allowEntityBlockBreak = true;
    private boolean allowDropPickItem = true;
    private boolean allowPlayerDamage = true;
    private boolean allowFoodChange = true;
    private boolean allowEntityExplode = true;
    private boolean allowFireSpread = true;
    private boolean allowBlockBurnUp = true;
    private boolean allowBlockIgnite = true;

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

        Server.registerListener(this, BasicBukkit.getPlugin());
    }

    public void setBukkitWorld(World world) {
        this.world = world;

        this.world.setAutoSave(this.safe);

        for (ItemFrame frame : this.getEntitiesByClass(ItemFrame.class)) {
            frame.setFixed(!allowEntityBlockBreak);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof World && ((World) obj).getName().equals(this.getName())) {
            return true;
        }
        return super.equals(obj);
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

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (this.allowBlockBreak) {
            return;
        }

        if (Server.getUser(event.getPlayer()).isService()) {
            return;
        }

        if (event.getAction() == Action.PHYSICAL) {
            Block block = event.getClickedBlock();
            if (block == null) return;
            if (block.getType() == Material.FARMLAND) {
                // Deny event and set the block
                event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
                event.setCancelled(true);
                block.setType(block.getType(), true);
                block.setBlockData(block.getBlockData());
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        if (this.allowDropPickItem) {
            return;
        }

        if (Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (this.allowPlayerDamage) {
            return;
        }

        if (e.getEntity() instanceof Player) {
            if (Server.getUser(((Player) e.getEntity())).isService()) {
                return;
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickUpItem(@Deprecated PlayerPickupItemEvent e) {
        if (this.allowDropPickItem) {
            return;
        }

        User user = Server.getUser(e.getPlayer());

        if (user != null && user.isService()) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickUpArrow(PlayerPickupArrowEvent e) {
        if (this.allowDropPickItem) {
            return;
        }

        if (Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (this.allowBlockBreak) {
            return;
        }

        if (Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onArmorStand(PlayerArmorStandManipulateEvent e) {
        if (this.allowBlockBreak && this.allowEntityBlockBreak) {
            return;
        }

        if (Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onBoat(VehicleDestroyEvent e) {
        if (this.allowBlockBreak && this.allowEntityBlockBreak) {
            return;
        }

        if (e.getAttacker() instanceof Player && Server.getUser(((Player) e.getAttacker())).isService()) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onPainting(HangingBreakByEntityEvent e) {
        if (this.allowBlockBreak && this.allowEntityBlockBreak) {
            return;
        }

        if (this.exceptService && e.getRemover() instanceof Player && Server.getUser(((Player) e.getRemover())).isService()) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void blockItemFrame(PlayerInteractEntityEvent e) {
        if (this.allowBlockBreak && this.allowEntityBlockBreak) {
            return;
        }

        if (e.getRightClicked().getType().equals(EntityType.ITEM_FRAME)) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onArmorStandDestroy(EntityDamageByUserEvent e) {
        if (!e.getEntity().getType().equals(EntityType.ARMOR_STAND)) {
            return;
        }

        if (this.allowBlockBreak && this.allowEntityBlockBreak) {
            return;
        }

        e.setCancelDamage(true);
        e.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (this.allowFoodChange) {
            return;
        }
        e.setCancelled(true);
        if (e.getEntity() instanceof Player) {
            e.getEntity().setFoodLevel(20);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        if (this.allowEntityExplode) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (allowBlockPlace) {
            return;
        }

        if (Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent e) {
        if (this.allowFireSpread) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent e) {
        if (this.allowBlockBurnUp) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e) {
        if (this.allowBlockIgnite) {
            return;
        }

        e.setCancelled(true);
    }

    public Block getBlockAt(int x, int y, int z) {
        return this.world.getBlockAt(x, y, z);
    }

    public ExBlock getExBlockAt(int x, int y, int z) {
        return new ExBlock(this.world.getBlockAt(x, y, z));
    }

    public Block getBlockAt(Location location) {
        return this.world.getBlockAt(location);
    }

    public ExBlock getExBlockAt(Location location) {
        return new ExBlock(this.world.getBlockAt(location));
    }

    public int getHighestBlockYAt(int x, int z) {
        return this.world.getHighestBlockYAt(x, z);
    }

    public int getHighestBlockYAt(Location location) {
        return this.world.getHighestBlockYAt(location);
    }

    public Block getHighestBlockAt(int x, int z) {
        return this.world.getHighestBlockAt(x, z);
    }

    public Block getHighestBlockAt(Location location) {
        return this.world.getHighestBlockAt(location);
    }

    public int getHighestBlockYAt(int x, int z, HeightMap heightMap) {
        return this.world.getHighestBlockYAt(x, z, heightMap);
    }

    public int getHighestBlockYAt(Location location, HeightMap heightMap) {
        return this.world.getHighestBlockYAt(location, heightMap);
    }

    public Block getHighestBlockAt(int x, int z, HeightMap heightMap) {
        return this.world.getHighestBlockAt(x, z, heightMap);
    }

    public Block getHighestBlockAt(Location location, HeightMap heightMap) {
        return this.world.getHighestBlockAt(location, heightMap);
    }

    public Chunk getChunkAt(int x, int z) {
        return this.world.getChunkAt(x, z);
    }

    public Chunk getChunkAt(Location location) {
        return this.world.getChunkAt(location);
    }

    public Chunk getChunkAt(Block block) {
        return this.world.getChunkAt(block);
    }

    public boolean isChunkLoaded(Chunk chunk) {
        return this.world.isChunkLoaded(chunk);
    }

    public Chunk[] getLoadedChunks() {
        return this.world.getLoadedChunks();
    }

    public void loadChunk(Chunk chunk) {
        this.world.loadChunk(chunk);
    }

    public boolean isChunkLoaded(int x, int z) {
        return this.world.isChunkLoaded(x, z);
    }

    public boolean isChunkGenerated(int x, int z) {
        return this.world.isChunkGenerated(x, z);
    }

    @Deprecated
    public boolean isChunkInUse(int x, int z) {
        return this.world.isChunkInUse(x, z);
    }

    public void loadChunk(int x, int z) {
        this.world.loadChunk(x, z);
    }

    public boolean loadChunk(int x, int z, boolean b) {
        return this.world.loadChunk(x, z, b);
    }

    public boolean unloadChunk(Chunk chunk) {
        return this.world.unloadChunk(chunk);
    }

    public boolean unloadChunk(int x, int z) {
        return this.world.unloadChunk(x, z);
    }

    public boolean unloadChunk(int x, int z, boolean b) {
        return this.world.unloadChunk(x, z, b);
    }

    public boolean unloadChunkRequest(int x, int z) {
        return this.world.unloadChunkRequest(x, z);
    }

    @Deprecated
    public boolean regenerateChunk(int x, int z) {
        return this.world.regenerateChunk(x, z);
    }

    @Deprecated
    public boolean refreshChunk(int x, int z) {
        return this.world.refreshChunk(x, z);
    }

    public boolean isChunkForceLoaded(int x, int z) {
        return this.world.isChunkForceLoaded(x, z);
    }

    public void setChunkForceLoaded(int x, int z, boolean b) {
        this.world.setChunkForceLoaded(x, z, b);
    }

    public Collection<Chunk> getForceLoadedChunks() {
        return this.world.getForceLoadedChunks();
    }

    public boolean addPluginChunkTicket(int x, int z, Plugin plugin) {
        return this.world.addPluginChunkTicket(x, z, plugin);
    }

    public boolean removePluginChunkTicket(int x, int z, Plugin plugin) {
        return this.world.removePluginChunkTicket(x, z, plugin);
    }

    public void removePluginChunkTickets(Plugin plugin) {
        this.world.removePluginChunkTickets(plugin);
    }

    public Collection<Plugin> getPluginChunkTickets(int x, int z) {
        return this.world.getPluginChunkTickets(x, z);
    }

    public Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {
        return this.world.getPluginChunkTickets();
    }

    public Item dropItem(Location location, ItemStack itemStack) {
        return this.world.dropItem(location, itemStack);
    }

    public Item dropItemNaturally(Location location, ItemStack itemStack) {
        return this.world.dropItemNaturally(location, itemStack);
    }

    public Arrow spawnArrow(Location location, Vector vector, float v, float v1) {
        return this.world.spawnArrow(location, vector, v, v1);
    }

    public <T extends AbstractArrow> T spawnArrow(Location location, Vector vector, float v, float v1, Class<T> aClass) {
        return this.world.spawnArrow(location, vector, v, v1, aClass);
    }

    public boolean generateTree(Location location, TreeType treeType) {
        return this.world.generateTree(location, treeType);
    }

    public boolean generateTree(Location location, TreeType treeType, BlockChangeDelegate blockChangeDelegate) {
        return this.world.generateTree(location, treeType, blockChangeDelegate);
    }

    public Entity spawnEntity(Location location, EntityType entityType) {
        return this.world.spawnEntity(location, entityType);
    }

    public LightningStrike strikeLightning(Location location) {
        return this.world.strikeLightning(location);
    }

    public LightningStrike strikeLightningEffect(Location location) {
        return this.world.strikeLightningEffect(location);
    }

    public List<Entity> getEntities() {
        return this.world.getEntities();
    }

    public List<LivingEntity> getLivingEntities() {
        return this.world.getLivingEntities();
    }

    @Deprecated
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... classes) {
        return this.world.getEntitiesByClass(classes);
    }

    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> aClass) {
        return this.world.getEntitiesByClass(aClass);
    }

    public Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
        return this.world.getEntitiesByClasses(classes);
    }

    public List<Player> getPlayers() {
        return this.world.getPlayers();
    }

    public Collection<Entity> getNearbyEntities(Location location, double v, double v1, double v2) {
        return this.world.getNearbyEntities(location, v, v1, v2);
    }

    public Collection<Entity> getNearbyEntities(Location location, double v, double v1, double v2, Predicate<Entity> predicate) {
        return this.world.getNearbyEntities(location, v, v1, v2, predicate);
    }

    public Collection<Entity> getNearbyEntities(BoundingBox boundingBox) {
        return this.world.getNearbyEntities(boundingBox);
    }

    public Collection<Entity> getNearbyEntities(BoundingBox boundingBox, Predicate<Entity> predicate) {
        return this.world.getNearbyEntities(boundingBox, predicate);
    }

    public RayTraceResult rayTraceEntities(Location location, Vector vector, double v) {
        return this.world.rayTraceEntities(location, vector, v);
    }

    public RayTraceResult rayTraceEntities(Location location, Vector vector, double v, double v1) {
        return this.world.rayTraceEntities(location, vector, v, v1);
    }

    public RayTraceResult rayTraceEntities(Location location, Vector vector, double v, Predicate<Entity> predicate) {
        return this.world.rayTraceEntities(location, vector, v, predicate);
    }

    public RayTraceResult rayTraceEntities(Location location, Vector vector, double v, double v1, Predicate<Entity> predicate) {
        return this.world.rayTraceEntities(location, vector, v, v1, predicate);
    }

    public RayTraceResult rayTraceBlocks(Location location, Vector vector, double v) {
        return this.world.rayTraceBlocks(location, vector, v);
    }

    public RayTraceResult rayTraceBlocks(Location location, Vector vector, double v, FluidCollisionMode fluidCollisionMode) {
        return this.world.rayTraceBlocks(location, vector, v, fluidCollisionMode);
    }

    public RayTraceResult rayTraceBlocks(Location location, Vector vector, double v, FluidCollisionMode fluidCollisionMode, boolean b) {
        return this.world.rayTraceBlocks(location, vector, v, fluidCollisionMode, b);
    }

    public RayTraceResult rayTrace(Location location, Vector vector, double v, FluidCollisionMode fluidCollisionMode, boolean b, double v1, Predicate<Entity> predicate) {
        return this.world.rayTrace(location, vector, v, fluidCollisionMode, b, v1, predicate);
    }

    public String getName() {
        return this.world.getName();
    }

    public UUID getUID() {
        return this.world.getUID();
    }

    public Location getSpawnLocation() {
        return this.world.getSpawnLocation();
    }

    public boolean setSpawnLocation(Location location) {
        return this.world.setSpawnLocation(location);
    }

    public boolean setSpawnLocation(int x, int y, int z, float v) {
        return this.world.setSpawnLocation(x, y, z, v);
    }

    public boolean setSpawnLocation(int x, int y, int z) {
        return this.world.setSpawnLocation(x, y, z);
    }

    public long getTime() {
        return this.world.getTime();
    }

    public org.bukkit.World getBukkitWorld() {
        return world;
    }

    public WorldManager.Type getType() {
        return type;
    }

    public void setTime(long ticks) {
        this.world.setTime(ticks);
    }

    public long getFullTime() {
        return this.world.getFullTime();
    }

    public void setFullTime(long l) {
        this.world.setFullTime(l);
    }

    public boolean hasStorm() {
        return this.world.hasStorm();
    }

    public void setStorm(boolean storm) {
        this.world.setStorm(storm);
    }

    public int getWeatherDuration() {
        return this.world.getWeatherDuration();
    }

    public void setWeatherDuration(int i) {
        this.world.setWeatherDuration(i);
    }

    public boolean isThundering() {
        return this.world.isThundering();
    }

    public void setThundering(boolean b) {
        this.world.setThundering(b);
    }

    public int getThunderDuration() {
        return this.world.getThunderDuration();
    }

    public void setThunderDuration(int i) {
        this.world.setThunderDuration(i);
    }

    public boolean createExplosion(double x, double y, double z, float v3) {
        return this.world.createExplosion(x, y, z, v3);
    }

    public boolean createExplosion(double x, double y, double z, float v3, boolean b) {
        return this.world.createExplosion(x, y, z, v3, b);
    }

    public boolean createExplosion(double x, double y, double z, float v3, boolean b, boolean b1) {
        return this.world.createExplosion(x, y, z, v3, b, b1);
    }

    public boolean createExplosion(double x, double y, double z, float v3, boolean b, boolean b1, Entity entity) {
        return this.world.createExplosion(x, y, z, v3, b, b1, entity);
    }

    public boolean createExplosion(Location location, float v) {
        return this.world.createExplosion(location, v);
    }

    public boolean createExplosion(Location location, float v, boolean b) {
        return this.world.createExplosion(location, v, b);
    }

    public boolean createExplosion(Location location, float v, boolean b, boolean b1) {
        return this.world.createExplosion(location, v, b, b1);
    }

    public boolean createExplosion(Location location, float v, boolean b, boolean b1, Entity entity) {
        return this.world.createExplosion(location, v, b, b1, entity);
    }

    public World.Environment getEnvironment() {
        return this.world.getEnvironment();
    }

    public long getSeed() {
        return this.world.getSeed();
    }

    public boolean getPVP() {
        return this.world.getPVP();
    }

    public void setPVP(boolean b) {
        this.world.setPVP(b);
    }

    public ChunkGenerator getGenerator() {
        return this.world.getGenerator();
    }

    public void save() {
        this.world.save();
    }

    public List<BlockPopulator> getPopulators() {
        return this.world.getPopulators();
    }

    public <T extends Entity> T spawn(Location location, Class<T> aClass) throws IllegalArgumentException {
        return this.world.spawn(location, aClass);
    }

    public <T extends Entity> T spawn(Location location, Class<T> aClass, Consumer<T> consumer) throws IllegalArgumentException {
        return this.world.spawn(location, aClass, consumer);
    }

    public FallingBlock spawnFallingBlock(Location location, @Deprecated MaterialData materialData) throws IllegalArgumentException {
        return this.world.spawnFallingBlock(location, materialData);
    }

    public FallingBlock spawnFallingBlock(Location location, BlockData blockData) throws IllegalArgumentException {
        return this.world.spawnFallingBlock(location, blockData);
    }

    @Deprecated
    public FallingBlock spawnFallingBlock(Location location, Material material, byte b) throws IllegalArgumentException {
        return this.world.spawnFallingBlock(location, material, b);
    }

    public void playEffect(Location location, Effect effect, int i) {
        this.world.playEffect(location, effect, i);
    }

    public void playEffect(Location location, Effect effect, int i, int j) {
        this.world.playEffect(location, effect, i, j);
    }

    public <T> void playEffect(Location location, Effect effect, T t) {
        this.world.playEffect(location, effect, t);
    }

    public <T> void playEffect(Location location, Effect effect, T t, int i) {
        this.world.playEffect(location, effect, t, i);
    }

    public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean b, boolean b1) {
        return this.world.getEmptyChunkSnapshot(x, z, b, b1);
    }

    public void setSpawnFlags(boolean b, boolean b1) {
        this.world.setSpawnFlags(b, b1);
    }

    public boolean getAllowAnimals() {
        return this.world.getAllowAnimals();
    }

    public boolean getAllowMonsters() {
        return this.world.getAllowAnimals();
    }

    @Deprecated
    public Biome getBiome(int x, int z) {
        return this.world.getBiome(x, z);
    }

    public Biome getBiome(int x, int y, int z) {
        return this.world.getBiome(x, y, z);
    }

    @Deprecated
    public void setBiome(int x, int z, Biome biome) {
        this.world.setBiome(x, z, biome);
    }

    public void setBiome(int x, int y, int z, Biome biome) {
        this.world.setBiome(x, y, z, biome);
    }

    @Deprecated
    public double getTemperature(int x, int z) {
        return this.world.getTemperature(x, z);
    }

    public double getTemperature(int x, int y, int z) {
        return this.world.getTemperature(x, y, z);
    }

    @Deprecated
    public double getHumidity(int x, int z) {
        return this.world.getHumidity(x, z);
    }

    public double getHumidity(int x, int y, int z) {
        return this.world.getHumidity(x, y, z);
    }

    public int getMaxHeight() {
        return this.world.getMaxHeight();
    }

    public int getSeaLevel() {
        return this.world.getSeaLevel();
    }

    public boolean getKeepSpawnInMemory() {
        return this.world.getKeepSpawnInMemory();
    }

    public void setKeepSpawnInMemory(boolean b) {
        this.world.setKeepSpawnInMemory(b);
    }

    public boolean isAutoSave() {
        return this.world.isAutoSave();
    }

    public void setAutoSave(boolean b) {
        this.world.setAutoSave(b);
        this.safe = b;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.world.setDifficulty(difficulty);
    }

    public Difficulty getDifficulty() {
        return this.world.getDifficulty();
    }

    public File getWorldFolder() {
        return this.world.getWorldFolder();
    }

    @Deprecated
    public WorldType getWorldType() {
        return this.world.getWorldType();
    }

    public boolean canGenerateStructures() {
        return this.world.canGenerateStructures();
    }

    public boolean isHardcore() {
        return this.world.isHardcore();
    }

    public void setHardcore(boolean b) {
        this.world.setHardcore(b);
    }

    public long getTicksPerAnimalSpawns() {
        return this.world.getTicksPerAnimalSpawns();
    }

    public void setTicksPerAnimalSpawns(int i) {
        this.world.setTicksPerAmbientSpawns(i);
    }

    public long getTicksPerMonsterSpawns() {
        return this.world.getTicksPerMonsterSpawns();
    }

    public void setTicksPerMonsterSpawns(int i) {
        this.world.setTicksPerMonsterSpawns(i);
    }

    public long getTicksPerWaterSpawns() {
        return this.world.getTicksPerWaterSpawns();
    }

    public void setTicksPerWaterSpawns(int i) {
        this.world.setTicksPerWaterSpawns(i);
    }

    public long getTicksPerWaterAmbientSpawns() {
        return this.world.getTicksPerWaterAmbientSpawns();
    }

    public void setTicksPerWaterAmbientSpawns(int i) {
        this.world.setTicksPerWaterAmbientSpawns(i);
    }

    public long getTicksPerAmbientSpawns() {
        return this.world.getTicksPerAmbientSpawns();
    }

    public void setTicksPerAmbientSpawns(int i) {
        this.world.setTicksPerAmbientSpawns(i);
    }

    public int getMonsterSpawnLimit() {
        return this.world.getMonsterSpawnLimit();
    }

    public void setMonsterSpawnLimit(int i) {
        this.world.setMonsterSpawnLimit(i);
    }

    public int getAnimalSpawnLimit() {
        return this.world.getAnimalSpawnLimit();
    }

    public void setAnimalSpawnLimit(int i) {
        this.world.setAnimalSpawnLimit(i);
    }

    public int getWaterAnimalSpawnLimit() {
        return this.world.getWaterAnimalSpawnLimit();
    }

    public void setWaterAnimalSpawnLimit(int i) {
        this.world.setAnimalSpawnLimit(i);
    }

    public int getWaterAmbientSpawnLimit() {
        return this.world.getWaterAmbientSpawnLimit();
    }

    public void setWaterAmbientSpawnLimit(int i) {
        this.world.setWaterAmbientSpawnLimit(i);
    }

    public int getAmbientSpawnLimit() {
        return this.world.getAmbientSpawnLimit();
    }

    public void setAmbientSpawnLimit(int i) {
        this.world.setAmbientSpawnLimit(i);
    }

    public void playSound(Location location, Sound sound, float v, float v1) {
        this.world.playSound(location, sound, v, v1);
    }

    public void playSound(Location location, String s, float v, float v1) {
        this.world.playSound(location, s, v, v1);
    }

    public void playSound(Location location, Sound sound, SoundCategory soundCategory, float v, float v1) {
        this.world.playSound(location, sound, soundCategory, v, v1);
    }

    public void playSound(Location location, String s, SoundCategory soundCategory, float v, float v1) {
        this.world.playSound(location, s, soundCategory, v, v1);
    }

    public String[] getGameRules() {
        return this.world.getGameRules();
    }

    @Deprecated
    public String getGameRuleValue(String s) {
        return this.world.getGameRuleValue(s);
    }

    @Deprecated
    public boolean setGameRuleValue(String s, String s1) {
        return this.world.setGameRuleValue(s, s1);
    }

    public boolean isGameRule(String s) {
        return this.world.isGameRule(s);
    }

    public <T> T getGameRuleValue(GameRule<T> gameRule) {
        return this.world.getGameRuleValue(gameRule);
    }

    public <T> T getGameRuleDefault(GameRule<T> gameRule) {
        return this.world.getGameRuleDefault(gameRule);
    }

    public <T> boolean setGameRule(GameRule<T> gameRule, T t) {
        return this.world.setGameRule(gameRule, t);
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

    public void setBlockPlaceAllow(boolean allowBlockPlace) {
        this.allowBlockPlace = allowBlockPlace;
    }

    public boolean isAllowEntityBlockBreak() {
        return allowEntityBlockBreak;
    }

    public void allowEntityBlockBreak(boolean allowEntityBlockBreak) {
        this.allowEntityBlockBreak = allowEntityBlockBreak;
        for (ItemFrame frame : this.getEntitiesByClass(ItemFrame.class)) {
            frame.setFixed(!allowEntityBlockBreak);
        }
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

    public WorldBorder getWorldBorder() {
        return this.world.getWorldBorder();
    }

    public void spawnParticle(Particle particle, Location location, int i) {
        this.world.spawnParticle(particle, location, i);
    }

    public void spawnParticle(Particle particle, double v, double v1, double v2, int i) {
        this.world.spawnParticle(particle, v, v1, v2, i);
    }

    public <T> void spawnParticle(Particle particle, Location location, int i, T t) {
        this.world.spawnParticle(particle, location, i, t);
    }

    public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, T t) {
        this.world.spawnParticle(particle, v, v1, v2, i, t);
    }

    public void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2) {
        this.world.spawnParticle(particle, location, i, v, v1, v2);
    }

    public void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5) {
        this.world.spawnParticle(particle, v, v1, v2, i, v3, v4, v5);
    }

    public <T> void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, T t) {
        this.world.spawnParticle(particle, location, i, v, v1, v2, t);
    }

    public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, T t) {
        this.world.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, t);
    }

    public void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, double v3) {
        this.world.spawnParticle(particle, location, i, v, v1, v2, v3);
    }

    public void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6) {
        this.world.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6);
    }

    public <T> void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, double v3, T t) {
        this.world.spawnParticle(particle, location, i, v, v1, v2, v3, t);
    }

    public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6, T t) {
        this.world.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6, t);
    }

    public <T> void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, double v3, T t, boolean b) {
        this.world.spawnParticle(particle, location, i, v, v1, v2, v3, t, b);
    }

    public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6, T t, boolean b) {
        this.world.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6, t, b);
    }

    public Location locateNearestStructure(Location location, StructureType structureType, int i, boolean b) {
        return this.world.locateNearestStructure(location, structureType, i, b);
    }

    public int getViewDistance() {
        return this.world.getViewDistance();
    }

    public World.Spigot spigot() {
        return this.world.spigot();
    }

    public Raid locateNearestRaid(Location location, int i) {
        return this.world.locateNearestRaid(location, i);
    }

    public List<Raid> getRaids() {
        return this.world.getRaids();
    }

    public DragonBattle getEnderDragonBattle() {
        return this.world.getEnderDragonBattle();
    }

    public void setMetadata(String s, MetadataValue metadataValue) {
        this.world.setMetadata(s, metadataValue);
    }

    public List<MetadataValue> getMetadata(String s) {
        return this.world.getMetadata(s);
    }

    public boolean hasMetadata(String s) {
        return this.world.hasMetadata(s);
    }

    public void removeMetadata(String s, Plugin plugin) {
        this.world.removeMetadata(s, plugin);
    }

    public void sendPluginMessage(Plugin plugin, String s, byte[] bytes) {
        this.world.sendPluginMessage(plugin, s, bytes);
    }

    public Set<String> getListeningPluginChannels() {
        return this.world.getListeningPluginChannels();
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

    public @NotNull MoonPhase getMoonPhase() {
        return world.getMoonPhase();
    }

    public boolean lineOfSightExists(@NotNull Location location, @NotNull Location location1) {
        return world.lineOfSightExists(location, location1);
    }

    @NotNull
    public Block getBlockAtKey(long key) {
        return world.getBlockAtKey(key);
    }

    @NotNull
    public Location getLocationAtKey(long key) {
        return world.getLocationAtKey(key);
    }

    @NotNull
    public Chunk getChunkAt(long chunkKey) {
        return world.getChunkAt(chunkKey);
    }

    public boolean isChunkGenerated(long chunkKey) {
        return world.isChunkGenerated(chunkKey);
    }

    @NotNull
    public Item dropItem(@NotNull Location location, @NotNull ItemStack itemStack, @Nullable Consumer<Item> consumer) {
        return world.dropItem(location, itemStack, consumer);
    }

    @NotNull
    public Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack itemStack, @Nullable Consumer<Item> consumer) {
        return world.dropItemNaturally(location, itemStack, consumer);
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

    @Deprecated
    public void getChunkAtAsync(int x, int z, World.@NotNull ChunkLoadCallback cb) {
        world.getChunkAtAsync(x, z, cb);
    }

    @Deprecated
    public void getChunkAtAsync(@NotNull Location loc, World.@NotNull ChunkLoadCallback cb) {
        world.getChunkAtAsync(loc, cb);
    }

    @Deprecated
    public void getChunkAtAsync(@NotNull Block block, World.@NotNull ChunkLoadCallback cb) {
        world.getChunkAtAsync(block, cb);
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

    public @NotNull CompletableFuture<Chunk> getChunkAtAsync(@NotNull Location loc) {
        return world.getChunkAtAsync(loc);
    }

    public @NotNull CompletableFuture<Chunk> getChunkAtAsync(@NotNull Location loc, boolean gen) {
        return world.getChunkAtAsync(loc, gen);
    }

    public @NotNull CompletableFuture<Chunk> getChunkAtAsync(@NotNull Block block) {
        return world.getChunkAtAsync(block);
    }

    public @NotNull CompletableFuture<Chunk> getChunkAtAsync(@NotNull Block block, boolean gen) {
        return world.getChunkAtAsync(block, gen);
    }

    public @NotNull CompletableFuture<Chunk> getChunkAtAsync(int x, int z) {
        return world.getChunkAtAsync(x, z);
    }

    public @NotNull CompletableFuture<Chunk> getChunkAtAsync(int x, int z, boolean gen) {
        return world.getChunkAtAsync(x, z, gen);
    }

    public @NotNull CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Location loc) {
        return world.getChunkAtAsyncUrgently(loc);
    }

    public @NotNull CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Location loc, boolean gen) {
        return world.getChunkAtAsyncUrgently(loc, gen);
    }

    public @NotNull CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Block block) {
        return world.getChunkAtAsyncUrgently(block);
    }

    public @NotNull CompletableFuture<Chunk> getChunkAtAsyncUrgently(@NotNull Block block, boolean gen) {
        return world.getChunkAtAsyncUrgently(block, gen);
    }

    public @NotNull CompletableFuture<Chunk> getChunkAtAsyncUrgently(int x, int z) {
        return world.getChunkAtAsyncUrgently(x, z);
    }

    public @NotNull CompletableFuture<Chunk> getChunkAtAsync(int i, int i1, boolean b, boolean b1) {
        return world.getChunkAtAsync(i, i1, b, b1);
    }

    @NotNull
    public NamespacedKey getKey() {
        return world.getKey();
    }

    @NotNull
    public Iterable<? extends Audience> audiences() {
        return world.audiences();
    }

    @Nullable
    public Entity getEntity(@NotNull UUID uuid) {
        return world.getEntity(uuid);
    }

    public boolean isDayTime() {
        return world.isDayTime();
    }

    public long getGameTime() {
        return world.getGameTime();
    }

    public boolean isClearWeather() {
        return world.isClearWeather();
    }

    public void setClearWeatherDuration(int i) {
        world.setClearWeatherDuration(i);
    }

    public int getClearWeatherDuration() {
        return world.getClearWeatherDuration();
    }

    public boolean createExplosion(@Nullable Entity entity, @NotNull Location location, float v, boolean b, boolean b1) {
        return world.createExplosion(entity, location, v, b, b1);
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

    public @Nullable BiomeProvider getBiomeProvider() {
        return world.getBiomeProvider();
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

    public long getTicksPerWaterUndergroundCreatureSpawns() {
        return world.getTicksPerWaterUndergroundCreatureSpawns();
    }

    public void setTicksPerWaterUndergroundCreatureSpawns(int i) {
        world.setTicksPerWaterUndergroundCreatureSpawns(i);
    }

    public int getWaterUndergroundCreatureSpawnLimit() {
        return world.getWaterUndergroundCreatureSpawnLimit();
    }

    public void setWaterUndergroundCreatureSpawnLimit(int i) {
        world.setWaterUndergroundCreatureSpawnLimit(i);
    }

    public void playSound(@NotNull Entity entity, @NotNull Sound sound, float v, float v1) {
        world.playSound(entity, sound, v, v1);
    }

    public void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory soundCategory, float v, float v1) {
        world.playSound(entity, sound, soundCategory, v, v1);
    }

    public <T> void spawnParticle(@NotNull Particle particle, @Nullable List<Player> receivers, @NotNull Player source, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {
        world.spawnParticle(particle, receivers, source, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
    }

    public <T> void spawnParticle(@NotNull Particle particle, @Nullable List<Player> list, @Nullable Player player, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6, @Nullable T t, boolean b) {
        world.spawnParticle(particle, list, player, v, v1, v2, i, v3, v4, v5, v6, t, b);
    }

    @Nullable
    public Location locateNearestBiome(@NotNull Location location, @NotNull Biome biome, int i) {
        return world.locateNearestBiome(location, biome, i);
    }

    @Nullable
    public Location locateNearestBiome(@NotNull Location location, @NotNull Biome biome, int i, int i1) {
        return world.locateNearestBiome(location, biome, i, i1);
    }

    public double getCoordinateScale() {
        return world.getCoordinateScale();
    }

    public boolean isFixedTime() {
        return world.isFixedTime();
    }

    @NotNull
    public Collection<Material> getInfiniburn() {
        return world.getInfiniburn();
    }

    public void sendGameEvent(@Nullable Entity entity, @NotNull GameEvent gameEvent, @NotNull Vector vector) {
        world.sendGameEvent(entity, gameEvent, vector);
    }

    public int getSimulationDistance() {
        return world.getSimulationDistance();
    }

    public void setViewDistance(int i) {
        world.setViewDistance(i);
    }

    public int getNoTickViewDistance() {
        return world.getNoTickViewDistance();
    }

    public void setNoTickViewDistance(int i) {
        world.setNoTickViewDistance(i);
    }

    public int getSendViewDistance() {
        return world.getSendViewDistance();
    }

    public void setSendViewDistance(int i) {
        world.setSendViewDistance(i);
    }

    @NotNull
    public Biome getBiome(@NotNull Location location) {
        return world.getBiome(location);
    }

    public void setBiome(@NotNull Location location, @NotNull Biome biome) {
        world.setBiome(location, biome);
    }

    public @NotNull BlockState getBlockState(@NotNull Location location) {
        return world.getBlockState(location);
    }

    public @NotNull BlockState getBlockState(int i, int i1, int i2) {
        return world.getBlockState(i, i1, i2);
    }

    @NotNull
    public BlockData getBlockData(@NotNull Location location) {
        return world.getBlockData(location);
    }

    @NotNull
    public BlockData getBlockData(int i, int i1, int i2) {
        return world.getBlockData(i, i1, i2);
    }

    @NotNull
    public Material getType(@NotNull Location location) {
        return world.getType(location);
    }

    @NotNull
    public Material getType(int i, int i1, int i2) {
        return world.getType(i, i1, i2);
    }

    public void setBlockData(@NotNull Location location, @NotNull BlockData blockData) {
        world.setBlockData(location, blockData);
    }

    public void setBlockData(int i, int i1, int i2, @NotNull BlockData blockData) {
        world.setBlockData(i, i1, i2, blockData);
    }

    public void setType(@NotNull Location location, @NotNull Material material) {
        world.setType(location, material);
    }

    public void setType(int i, int i1, int i2, @NotNull Material material) {
        world.setType(i, i1, i2, material);
    }

    public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType treeType) {
        return world.generateTree(location, random, treeType);
    }

    public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType treeType, @Nullable Consumer<BlockState> consumer) {
        return world.generateTree(location, random, treeType, consumer);
    }

    public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType treeType, @Nullable Predicate<BlockState> predicate) {
        return world.generateTree(location, random, treeType, predicate);
    }

    @NotNull
    public Entity spawnEntity(@NotNull Location location, @NotNull EntityType entityType, boolean b) {
        return world.spawnEntity(location, entityType, b);
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

    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> aClass, @Nullable Consumer<T> consumer, CreatureSpawnEvent.@NotNull SpawnReason spawnReason) throws IllegalArgumentException {
        return world.spawn(location, aClass, consumer, spawnReason);
    }

    public <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> aClass, boolean b, @Nullable Consumer<T> consumer) throws IllegalArgumentException {
        return world.spawn(location, aClass, b, consumer);
    }

    public int getMinHeight() {
        return world.getMinHeight();
    }

    public @NotNull BiomeProvider vanillaBiomeProvider() {
        return world.vanillaBiomeProvider();
    }

    public @NotNull Pointers pointers() {
        return world.pointers();
    }

    public @NotNull Audience filterAudience(@NotNull Predicate<? super Audience> filter) {
        return world.filterAudience(filter);
    }

    public void forEachAudience(java.util.function.@NotNull Consumer<? super Audience> action) {
        world.forEachAudience(action);
    }

    public void sendMessage(@NotNull Identified source, @NotNull Component message, @NotNull MessageType type) {
        world.sendMessage(source, message, type);
    }

    public void sendMessage(@NotNull Identity source, @NotNull Component message, @NotNull MessageType type) {
        world.sendMessage(source, message, type);
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

    public void sendMessage(@NotNull ComponentLike message) {
        world.sendMessage(message);
    }

    public void sendMessage(@NotNull Identified source, @NotNull ComponentLike message) {
        world.sendMessage(source, message);
    }

    public void sendMessage(@NotNull Identity source, @NotNull ComponentLike message) {
        world.sendMessage(source, message);
    }

    public void sendMessage(@NotNull Component message) {
        world.sendMessage(message);
    }

    public void sendMessage(@NotNull Identified source, @NotNull Component message) {
        world.sendMessage(source, message);
    }

    public void sendMessage(@NotNull Identity source, @NotNull Component message) {
        world.sendMessage(source, message);
    }

    public void sendMessage(@NotNull ComponentLike message, @NotNull MessageType type) {
        world.sendMessage(message, type);
    }

    public void sendMessage(@NotNull Identified source, @NotNull ComponentLike message, @NotNull MessageType type) {
        world.sendMessage(source, message, type);
    }

    public void sendMessage(@NotNull Identity source, @NotNull ComponentLike message, @NotNull MessageType type) {
        world.sendMessage(source, message, type);
    }

    public void sendMessage(@NotNull Component message, @NotNull MessageType type) {
        world.sendMessage(message, type);
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

    public @NotNull Key key() {
        return world.key();
    }
}
