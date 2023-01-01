/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.world;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.CancelPriority;
import de.timesnake.basic.bukkit.util.user.event.EntityDamageByUserEvent;
import de.timesnake.basic.bukkit.util.user.event.UserBlockBreakEvent;
import de.timesnake.basic.bukkit.util.user.event.UserBlockPlaceEvent;
import de.timesnake.basic.bukkit.util.user.event.UserDamageEvent;
import de.timesnake.basic.bukkit.util.world.ExLocation;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import de.timesnake.basic.bukkit.util.world.ExWorldType;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Candle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.ItemStack;

public class WorldManager implements Listener, de.timesnake.basic.bukkit.util.world.WorldManager {

    public final WorldBorderManager worldBorderManager;
    private final File backupDir;
    private final Map<String, ExWorld> worldsByName = new HashMap<>();
    private final Map<UUID, Map<ExWorld, ExLocation>> locationsPerWorldByUuid = new HashMap<>();

    public WorldManager() {
        Server.registerListener(this, BasicBukkit.getPlugin());

        this.worldBorderManager = new WorldBorderManager();

        this.backupDir = new File("plugins/basic-bukkit/world_backups");

        //add worlds to file
        for (World world : Bukkit.getWorlds()) {
            ExWorldFile file = new ExWorldFile(world.getWorldFolder());
            this.worldsByName.put(world.getName(), new ExWorld(world, file));
        }


        for (File file : Bukkit.getWorldContainer().listFiles()) {
            if (!file.isDirectory()) {
                continue;
            }

            if (file.list((f, n) -> n.equals(ExWorldFile.NAME)).length == 0) {
                continue;
            }

            ExWorldFile exWorldFile = new ExWorldFile(file);

            ExWorld world = this.createWorld(file.getName(), exWorldFile.getWorldType());
            if (world != null) {
                Plugin.WORLDS.getLogger().info("Loaded world '" + world.getName() + "'");
            }
        }

        this.loadUserLocationsFromFile();
    }

    public ExWorld createWorldFromFile(String name) {
        File file = Bukkit.getWorldContainer().toPath().resolve(name).toFile();

        if (!file.isDirectory()) {
            return null;
        }

        if (file.list((f, n) -> n.equals(ExWorldFile.NAME)).length == 0) {
            return null;
        }

        ExWorldFile exWorldFile = new ExWorldFile(file);

        return this.createWorld(file.getName(), exWorldFile.getWorldType());
    }

    private void loadUserLocationsFromFile() {
        File folder = LocationsFile.getFolder();

        if (!folder.exists()) {
            folder.mkdirs();
        }

        for (String fileName : folder.list()) {

            UUID uuid;
            try {
                uuid = UUID.fromString(fileName.replace(".yml", ""));
            } catch (IllegalArgumentException e) {
                Plugin.WORLDS.getLogger().warning("Illegal locations file name '" + fileName + "'");
                continue;
            }

            LocationsFile file = new LocationsFile(uuid);

            HashMap<ExWorld, ExLocation> locationByWorld = new HashMap<>();

            for (ExWorld world : this.getWorlds()) {
                locationByWorld.put(world, new ExLocation(world, file.getUserLocation(world)));
            }

            this.locationsPerWorldByUuid.put(uuid, locationByWorld);
        }

        Plugin.WORLDS.getLogger().info("Loaded user locations");
    }

    public void onDisable() {
        for (ExWorld exWorld : this.getWorlds()) {
            if (!exWorld.isSafe()) {
                this.moveUsersFromWorld(exWorld);
                Bukkit.unloadWorld(exWorld.getName(), false);
            }
        }

        this.worldsByName.clear();

        this.saveLocations();
    }

    public void loadUserLocations(User user) {
        if (!this.locationsPerWorldByUuid.containsKey(user.getUniqueId())) {
            HashMap<ExWorld, ExLocation> locationByWorld = new HashMap<>();

            for (ExWorld world : this.getWorlds()) {
                locationByWorld.put(world, new ExLocation(world, world.getSpawnLocation()));
            }

            this.locationsPerWorldByUuid.put(user.getUniqueId(), locationByWorld);
        }
    }

    public void saveUserLocations(User user) {
        user.setLastLocation(user.getLocation());
        this.locationsPerWorldByUuid.get(user.getUniqueId()).put(user.getExWorld(), user.getExLocation());
    }

    private void saveLocations() {
        for (Map.Entry<UUID, Map<ExWorld, ExLocation>> entry : this.locationsPerWorldByUuid.entrySet()) {
            UUID uuid = entry.getKey();
            LocationsFile file = new LocationsFile(uuid);
            for (ExLocation location : entry.getValue().values()) {
                file.setUserLocation(location);
            }
        }
        Plugin.WORLDS.getLogger().info("Saved user locations");
    }

    @Override
    public ExLocation getUserLocation(User user, ExWorld world) {
        ExLocation location = this.locationsPerWorldByUuid.get(user.getUniqueId()).get(world);
        if (location == null) {
            location = new ExLocation(world, world.getSpawnLocation());
            this.locationsPerWorldByUuid.get(user.getUniqueId()).put(world, location);
        }
        return location;
    }


    @Override
    public WorldBorderManager getWorldBorderManager() {
        return worldBorderManager;
    }

    @Override
    public ExWorld getBasicWorld() {
        return this.getWorld("world");
    }

    @Override
    public ExWorld getWorld(String name) {
        return this.worldsByName.get(name);
    }

    @Override
    public ExWorld getWorld(World world) {
        ExWorld exWorld = this.worldsByName.get(world.getName());
        if (exWorld == null) {
            return this.createWorld(world);
        }
        return exWorld;
    }

    @Override
    public Collection<ExWorld> getWorlds() {
        return this.worldsByName.values();
    }

    @Override
    public ExWorld createWorld(String name) {
        return this.createWorld(name, ExWorldType.NORMAL);
    }

    @Override
    public ExWorld createWorld(String name, ExWorldType type) {

        if (type == null) {
            Plugin.WORLDS.getLogger().warning("Can not load type of world '" + name + "'");
            return null;
        }

        for (String s : UNSUPPORTED_SYMBOLS) {
            name = name.replace(s, "");
        }

        // world exits already
        if (this.worldsByName.get(name) != null) {
            return this.worldsByName.get(name);
        }

        // world exists in Bukkit, create exworld
        if (Bukkit.getWorld(name) != null) {
            return this.getWorld(Bukkit.getWorld(name));
        }

        WorldCreator worldCreator = new WorldCreator(name);
        if (type.getChunkGenerator() != null) {
            worldCreator.generator(type.getChunkGenerator());
        }
        if (type.getEnvironment() != null) {
            worldCreator.environment(type.getEnvironment());
        }
        if (type.getWorldType() != null) {
            worldCreator.type(type.getWorldType());
        }

        World world = Bukkit.createWorld(worldCreator);

        if (world == null) {
            return null;
        }

        ExWorldFile file = new ExWorldFile(world.getWorldFolder(), type);
        ExWorld exWorld = new ExWorld(world, type, file);
        this.registerExWorld(exWorld);
        return exWorld;
    }

    @Override
    public ExWorld cloneWorld(String name, ExWorld exWorld) {
        if (exWorld == null) {
            return null;
        }

        for (String s : UNSUPPORTED_SYMBOLS) {
            name = name.replace(s, "");
        }

        // world exits already
        if (this.worldsByName.containsKey(name)) {
            return null;
        }

        // world exists in Bukkit
        if (Bukkit.getWorld(name) != null) {
            return null;
        }

        exWorld.save();

        this.copyWorldFolderFiles(exWorld.getWorldFolder(),
                new File(Bukkit.getWorldContainer() + File.separator + name));

        World world = Bukkit.createWorld(new WorldCreator(name).copy(exWorld.getBukkitWorld()));

        if (world == null) {
            return null;
        }

        ExWorldFile file = new ExWorldFile(world.getWorldFolder(), exWorld.getType());
        ExWorld clonedExWorld = new ExWorld(world, exWorld.getType(), file, exWorld.getRestrictionValues());
        this.registerExWorld(clonedExWorld);
        return clonedExWorld;
    }

    @Override
    public boolean backupWorld(ExWorld world) {
        if (world == null) {
            return false;
        }

        File backup = new File(this.backupDir + File.separator + world.getName());
        backup.delete();
        backup.mkdir();
        world.save();
        this.copyWorldFolderFiles(world.getWorldFolder(), backup);
        return true;
    }

    @Override
    public boolean loadWorldBackup(ExWorld world) {
        if (world == null) {
            return false;
        }

        File backup = new File(this.backupDir + File.separator + world.getName());

        if (!backup.exists()) {
            return false;
        }

        this.moveUsersFromWorld(world);

        Bukkit.unloadWorld(world.getName(), false);

        File worldFile = world.getWorldFolder();
        worldFile.delete();

        this.copyWorldFolderFiles(backup, worldFile);

        World worldBackup = Bukkit.createWorld(new WorldCreator(world.getName()));
        world.setBukkitWorld(worldBackup);
        return true;
    }

    @Override
    public boolean reloadWorld(ExWorld exWorld) {
        if (exWorld == null) {
            return false;
        }

        this.moveUsersFromWorld(exWorld);
        World world = exWorld.getBukkitWorld();

        boolean successfully = Bukkit.unloadWorld(world, false);

        List<String> deleteFiles = List.of("session.lock", "uid.dat");

        for (String fileName : deleteFiles) {
            File file = new File(exWorld.getWorldFolder().getAbsolutePath() + File.separator + fileName);
            if (file.exists()) {
                file.delete();
            } else {
                return false;
            }
        }

        World newWorld = Bukkit.createWorld(new WorldCreator(exWorld.getName()));
        exWorld.setBukkitWorld(newWorld);

        return successfully;
    }

    @Override
    public boolean unloadWorld(ExWorld world, boolean save) {
        if (world == null) {
            return false;
        }

        this.moveUsersFromWorld(world);
        this.worldsByName.remove(world.getName());
        return Bukkit.unloadWorld(world.getName(), save);
    }

    @Override
    public boolean deleteWorld(ExWorld exWorld, boolean deleteFiles) {
        if (exWorld == null) {
            return false;
        }

        this.moveUsersFromWorld(exWorld);
        boolean unloaded = Bukkit.unloadWorld(exWorld.getBukkitWorld(), false);
        this.unregisterExWorld(exWorld);

        if (deleteFiles) {
            try {
                FileUtils.deleteDirectory(exWorld.getWorldFolder());
            } catch (IOException e) {
                return false;
            }
        }

        return unloaded;
    }


    private void moveUsersFromWorld(ExWorld world) {
        for (User user : Server.getUsers()) {
            if (user.getWorld().equals(world)) {
                user.teleport(this.getBasicWorld());
            }
        }
    }

    private ExWorld createWorld(World world) {
        if (world == null) {
            return null;
        }

        if (Bukkit.getWorld(world.getName()) == null) {
            world = Bukkit.createWorld(new WorldCreator(world.getName()).copy(world));
        }

        ExWorldFile file = new ExWorldFile(world.getWorldFolder(), ExWorldType.fromWorld(world));
        ExWorld exWorld = new ExWorld(world, ExWorldType.fromWorld(world), file);
        this.registerExWorld(exWorld);
        return exWorld;
    }

    private void registerExWorld(ExWorld world) {
        this.worldsByName.put(world.getName(), world);
    }

    private void unregisterExWorld(ExWorld world) {
        this.worldsByName.remove(world.getName());
        ExWorldFile exWorldFile = world.getExFile();
        if (exWorldFile.exists()) {
            exWorldFile.delete();
        }
    }

    @Override
    public void copyWorldFolderFiles(File source, File target) {
        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists())
                        if (!target.mkdirs()) throw new IOException("Couldn't create world directory!");
                    String[] files = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyWorldFolderFiles(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        User user = Server.getUser(e.getPlayer());

        Location fromLoc = e.getFrom();

        if (fromLoc != null && user != null) {
            user.setLastLocation(fromLoc);
        }

        if (fromLoc != null && !fromLoc.getWorld().equals(e.getTo().getWorld())) {
            ExWorld fromWorld = this.getWorld(fromLoc.getWorld());
            this.locationsPerWorldByUuid.get(user.getUniqueId()).put(fromWorld, new ExLocation(fromWorld, fromLoc));
        }
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ExWorld world = this.getWorld(event.getPlayer().getWorld());

        if (world.isExceptService() && Server.getUser(event.getPlayer()).isService()) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        Material blockType = clickedBlock != null ? clickedBlock.getType() : Material.AIR;
        ItemStack item = event.getItem();

        if (world.isRestricted(ExWorld.Restriction.BLOCK_BREAK)) {
            if (event.getAction() == Action.PHYSICAL) {
                if (clickedBlock == null) return;
                if (blockType == Material.FARMLAND) {
                    event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
                    event.setCancelled(true);
                    clickedBlock.setType(blockType, true);
                    clickedBlock.setBlockData(clickedBlock.getBlockData());
                    Plugin.WORLDS.getLogger().info("Cancelled interact physical event");
                    return;
                }
            }
        }

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Set<Material> filledBuckets = Set.of(Material.LAVA_BUCKET, Material.COD_BUCKET, Material.AXOLOTL_BUCKET,
                    Material.POWDER_SNOW_BUCKET, Material.PUFFERFISH_BUCKET, Material.SALMON_BUCKET,
                    Material.TROPICAL_FISH_BUCKET, Material.WATER_BUCKET);

            if (item != null) {
                if (filledBuckets.contains(item.getType())) {
                    if (world.isRestricted(ExWorld.Restriction.FLUID_PLACE) && !blockType.equals(Material.CAULDRON)) {
                        event.setCancelled(true);
                        event.setUseInteractedBlock(Event.Result.DENY);
                        event.setUseItemInHand(Event.Result.DENY);
                        Plugin.WORLDS.getLogger().info("Cancelled interact cauldron event");
                        return;
                    }
                } else if (item.getType().equals(Material.BUCKET)) {
                    Set<Material> filledCauldrons = Set.of(Material.LAVA_CAULDRON, Material.WATER_CAULDRON);

                    if (world.isRestricted(ExWorld.Restriction.FLUID_COLLECT) && !filledCauldrons.contains(blockType)) {
                        event.setCancelled(true);
                        event.setUseInteractedBlock(Event.Result.DENY);
                        event.setUseItemInHand(Event.Result.DENY);
                        Plugin.WORLDS.getLogger().info("Cancelled interact cauldron event");
                        return;
                    }
                }
            }
        }

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            if (blockType.equals(Material.CAKE)) {
                if (world.isRestricted(ExWorld.Restriction.CAKE_EAT)) {
                    event.setCancelled(true);
                    event.setUseInteractedBlock(Event.Result.DENY);
                    event.setUseItemInHand(Event.Result.DENY);
                    Plugin.WORLDS.getLogger().info("Cancelled interact cake event");
                    return;
                }
            }

            if (world.isRestricted(ExWorld.Restriction.OPEN_INVENTORIES).contains(blockType)) {
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
                event.setUseItemInHand(Event.Result.DENY);
                Plugin.WORLDS.getLogger().info("Cancelled interact block inventory event");
                return;
            }

            if (item != null) {
                if (item.getType().equals(Material.FLINT_AND_STEEL)) {
                    if (!world.isRestricted(ExWorld.Restriction.FLINT_AND_STEEL)) {
                        return;
                    }

                    if (!world.isRestricted(ExWorld.Restriction.LIGHT_UP_INTERACTION)
                            && (Tag.CAMPFIRES.isTagged(blockType) || Tag.CANDLES.isTagged(blockType))) {
                        return;
                    }

                    event.setCancelled(true);
                    event.setUseInteractedBlock(Event.Result.DENY);
                    event.setUseItemInHand(Event.Result.DENY);
                    Plugin.WORLDS.getLogger().info("Cancelled interact light-up event");
                } else if (item.getType().equals(Material.SPLASH_POTION)) {
                    if (!world.isRestricted(ExWorld.Restriction.LIGHT_UP_INTERACTION)) {
                        return;
                    }

                    if (!Tag.CAMPFIRES.isTagged(blockType)) {
                        return;
                    }

                    event.setCancelled(true);
                    event.setUseInteractedBlock(Event.Result.DENY);
                    event.setUseItemInHand(Event.Result.DENY);
                } else {
                    if (!world.isRestricted(ExWorld.Restriction.PLACE_IN_BLOCK)) {
                        return;
                    }

                    if (item.getType().equals(Material.ENDER_EYE) && blockType.equals(Material.END_PORTAL_FRAME)) {
                        event.setCancelled(true);
                        event.setUseInteractedBlock(Event.Result.DENY);
                        event.setUseItemInHand(Event.Result.DENY);
                        Plugin.WORLDS.getLogger().info("Cancelled interact place enderpearl event");
                        return;
                    }

                    if (Tag.CANDLES.isTagged(item.getType()) && (blockType.equals(Material.CAKE) || Tag.CANDLES.isTagged(blockType))) {
                        event.setCancelled(true);
                        event.setUseInteractedBlock(Event.Result.DENY);
                        event.setUseItemInHand(Event.Result.DENY);
                        Plugin.WORLDS.getLogger().info("Cancelled interact cake candle event");
                        return;
                    }

                    if (item.getType().equals(Material.SEA_PICKLE) && blockType.equals(Material.SEA_PICKLE)) {
                        event.setCancelled(true);
                        event.setUseInteractedBlock(Event.Result.DENY);
                        event.setUseItemInHand(Event.Result.DENY);
                        Plugin.WORLDS.getLogger().info("Cancelled interact sea pickle event");
                        return;
                    }
                }
            }
        } else if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {

            if (blockType.equals(Material.FIRE)) {
                if (world.isRestricted(ExWorld.Restriction.FIRE_PUNCH_OUT)) {
                    event.setCancelled(true);
                    event.setUseInteractedBlock(Event.Result.DENY);
                    event.setUseItemInHand(Event.Result.DENY);
                    Plugin.WORLDS.getLogger().info("Cancelled interact fire event");
                }
            }
        }

    }

    @EventHandler
    public void onItemFrameChange(PlayerItemFrameChangeEvent e) {
        ExWorld world = this.getWorld(e.getPlayer().getWorld());

        if (world.isExceptService() && Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        if (!world.isRestricted(ExWorld.Restriction.ENTITY_BLOCK_BREAK)) {
            return;
        }

        if (e.getAction().equals(PlayerItemFrameChangeEvent.ItemFrameChangeAction.ROTATE)
                && world.isRestricted(ExWorld.Restriction.ITEM_FRAME_ROTATE)) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled item frame rotate event");
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        ExWorld world = this.getWorld(e.getPlayer().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.DROP_PICK_ITEM)) {
            return;
        }

        if (world.isExceptService() && Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled drop item event");
    }

    @EventHandler
    public void onEntityDamage(UserDamageEvent e) {
        ExWorld world = e.getUser().getExWorld();

        if (!world.isRestricted(ExWorld.Restriction.PLAYER_DAMAGE)) {
            return;
        }

        if (world.isExceptService() && e.getUser().isService()) {
            return;
        }

        e.setCancelled(true);
        e.setCancelDamage(true);
        Plugin.WORLDS.getLogger().info("Cancelled user damage event");
    }

    @EventHandler
    public void onPlayerPickUpItem(@Deprecated PlayerPickupItemEvent e) {
        ExWorld world = this.getWorld(e.getPlayer().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.DROP_PICK_ITEM)) {
            return;
        }

        User user = Server.getUser(e.getPlayer());

        if (user != null && world.isExceptService() && user.isService()) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled pickup item event");
    }

    @EventHandler
    public void onPlayerPickUpArrow(PlayerPickupArrowEvent e) {
        ExWorld world = this.getWorld(e.getPlayer().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.DROP_PICK_ITEM)) {
            return;
        }

        if (world.isExceptService() && Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled pickup arrow event");
    }

    @EventHandler
    public void onBlockBreak(UserBlockBreakEvent e) {
        ExWorld world = e.getUser().getExWorld();

        if (!world.isRestricted(ExWorld.Restriction.BLOCK_BREAK)) {
            return;
        }

        if (world.isExceptService() && e.getUser().isService()) {
            return;
        }

        if ((e.getBlock().getType().equals(Material.FIRE)
                || (Tag.CANDLES.isTagged(e.getBlock().getType())) && ((Candle) e.getBlock().getState()).isLit())
                && !world.isRestricted(ExWorld.Restriction.FIRE_PUNCH_OUT)) {
            return;
        }

        e.setCancelled(CancelPriority.LOW, true);
        Plugin.WORLDS.getLogger().info("Cancelled block break event");
    }

    @EventHandler
    public void onArmorStand(PlayerArmorStandManipulateEvent e) {
        ExWorld world = this.getWorld(e.getPlayer().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.BLOCK_BREAK) && !world.isRestricted(ExWorld.Restriction.ENTITY_BLOCK_BREAK)) {
            return;
        }

        if (world.isExceptService() && Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled armorstand manipulate event");
    }

    @EventHandler
    public void onBoat(VehicleDestroyEvent e) {
        ExWorld world = this.getWorld(e.getVehicle().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.BLOCK_BREAK) && !world.isRestricted(ExWorld.Restriction.ENTITY_BLOCK_BREAK)) {
            return;
        }

        if (e.getAttacker() != null && world.isExceptService() && e.getAttacker() instanceof Player
                && Server.getUser(((Player) e.getAttacker())).isService()) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled vehicle destroy event");
    }

    @EventHandler
    public void onPainting(HangingBreakByEntityEvent e) {
        ExWorld world = this.getWorld(e.getRemover().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.BLOCK_BREAK) && !world.isRestricted(ExWorld.Restriction.ENTITY_BLOCK_BREAK)) {
            return;
        }

        if (world.isExceptService() && e.getRemover() instanceof Player
                && Server.getUser(((Player) e.getRemover())).isService()) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled hanging destroy event");
    }

    @EventHandler
    public void blockItemFrame(PlayerInteractEntityEvent e) {
        ExWorld world = this.getWorld(e.getPlayer().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.BLOCK_BREAK) && !world.isRestricted(ExWorld.Restriction.ENTITY_BLOCK_BREAK)) {
            return;
        }

        if (world.isExceptService() && Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        if (e.getRightClicked().getType().equals(EntityType.ITEM_FRAME)
                && world.isRestricted(ExWorld.Restriction.ITEM_FRAME_ROTATE)) {
            e.setCancelled(true);
        }

        Plugin.WORLDS.getLogger().info("Cancelled interact entity event");
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByUserEvent e) {
        ExWorld world = this.getWorld(e.getUser().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.BLOCK_BREAK) && !world.isRestricted(ExWorld.Restriction.ENTITY_BLOCK_BREAK)) {
            return;
        }

        if (!e.getEntity().getType().equals(EntityType.ARMOR_STAND) && !e.getEntity().getType().equals(EntityType.ITEM_FRAME)) {
            return;
        }

        if (world.isExceptService() && e.getUser().isService()) {
            return;
        }

        e.setCancelDamage(true);
        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled user by user damage event");
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        ExWorld world = this.getWorld(e.getEntity().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.FOOD_CHANGE)) {
            return;
        }

        if (world.isExceptService() && Server.getUser(e.getEntity().getUniqueId()).isService()) {
            return;
        }

        e.setCancelled(true);
        if (e.getEntity() instanceof Player) {
            e.getEntity().setFoodLevel(20);
        }

        Plugin.WORLDS.getLogger().info("Cancelled food change event");
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        ExWorld world = this.getWorld(e.getEntity().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.ENTITY_EXPLODE)) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled entity explode event");
    }

    @EventHandler
    public void onBlockPlace(UserBlockPlaceEvent e) {
        ExWorld world = e.getUser().getExWorld();

        if (world.isExceptService() && e.getUser().isService()) {
            return;
        }

        ItemStack item = e.getItemInHand();
        Block block = e.getBlock();
        Material blockType = block.getType();

        if (item.getType().equals(Material.ENDER_EYE) && blockType.equals(Material.END_PORTAL_FRAME)) {
            e.setCancelled(world.isRestricted(ExWorld.Restriction.PLACE_IN_BLOCK));
            return;
        }

        if (Tag.CANDLES.isTagged(item.getType()) && (blockType.equals(Material.CAKE) || Tag.CANDLES.isTagged(blockType))) {
            e.setCancelled(world.isRestricted(ExWorld.Restriction.PLACE_IN_BLOCK));
            return;
        }

        if (item.getType().equals(Material.SEA_PICKLE) && blockType.equals(Material.SEA_PICKLE)) {
            e.setCancelled(world.isRestricted(ExWorld.Restriction.PLACE_IN_BLOCK));
            return;
        }

        if (item.getType().equals(Material.FLINT_AND_STEEL)) {
            if (!world.isRestricted(ExWorld.Restriction.FLINT_AND_STEEL)) {
                return;
            }

            if (!world.isRestricted(ExWorld.Restriction.LIGHT_UP_INTERACTION)
                    && (Tag.CAMPFIRES.isTagged(e.getBlock().getType()) || Tag.CANDLES.isTagged(e.getBlock().getType()))) {
                return;
            }

            e.setCancelled(CancelPriority.LOW, true);
            Plugin.WORLDS.getLogger().info("Cancelled block place event");
            return;
        }

        if (!world.isRestricted(ExWorld.Restriction.BLOCK_PLACE)) {
            return;
        }

        e.setCancelled(CancelPriority.LOW, true);
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent e) {
        ExWorld world = this.getWorld(e.getBlock().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.FIRE_SPREAD)) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled block spread event");
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent e) {
        ExWorld world = this.getWorld(e.getBlock().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.BLOCK_BURN_UP)) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled block burn-up event");
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e) {
        ExWorld world = this.getWorld(e.getBlock().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.BLOCK_IGNITE)) {
            return;
        }

        if (!world.isRestricted(ExWorld.Restriction.FLINT_AND_STEEL)) {
            return;
        }

        if (!world.isRestricted(ExWorld.Restriction.LIGHT_UP_INTERACTION)
                && (Tag.CAMPFIRES.isTagged(e.getBlock().getType()) || Tag.CANDLES.isTagged(e.getBlock().getType()))) {
            return;
        }

        if (world.isExceptService() && Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled block ignite event");
    }
}
