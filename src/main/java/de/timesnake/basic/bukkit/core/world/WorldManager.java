/*
 * basic-bukkit.main
 * Copyright (C) 2022 timesnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; If not, see <http://www.gnu.org/licenses/>.
 */

package de.timesnake.basic.bukkit.core.world;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.EntityDamageByUserEvent;
import de.timesnake.basic.bukkit.util.user.event.UserDamageEvent;
import de.timesnake.basic.bukkit.util.world.ExLocation;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import de.timesnake.basic.bukkit.util.world.ExWorldType;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Candle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.*;

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
            this.worldsByName.put(world.getName(), new ExWorld(world, ExWorldType.fromWorld(world), file));
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
                Server.printText(Plugin.WORLDS, "Loaded world " + world.getName());
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
                Server.printWarning(Plugin.WORLDS, "Illegal locations file name " + fileName);
                continue;
            }

            LocationsFile file = new LocationsFile(uuid);

            HashMap<ExWorld, ExLocation> locationByWorld = new HashMap<>();

            for (ExWorld world : this.getWorlds()) {
                locationByWorld.put(world, new ExLocation(world, file.getUserLocation(world)));
            }

            this.locationsPerWorldByUuid.put(uuid, locationByWorld);
        }

        Server.printText(Plugin.WORLDS, "Loaded user locations");
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
        Server.printText(Plugin.WORLDS, "Saved user locations");
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
            Server.printWarning(Plugin.WORLDS, "Can not load type of world " + name);
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
        ExWorld clonedExWorld = new ExWorld(world, exWorld.getType(), file);
        this.registerExWorld(clonedExWorld);
        return clonedExWorld;
    }

    @Override
    public boolean backupWorld(ExWorld world) {
        File backup = new File(this.backupDir + File.separator + world.getName());
        backup.delete();
        backup.mkdir();
        world.save();
        this.copyWorldFolderFiles(world.getWorldFolder(), backup);
        return true;
    }

    @Override
    public boolean loadWorldBackup(ExWorld world) {
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
        this.moveUsersFromWorld(world);
        this.worldsByName.remove(world.getName());
        return Bukkit.unloadWorld(world.getName(), save);
    }

    @Override
    public boolean deleteWorld(ExWorld exWorld, boolean deleteFiles) {
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

        if (Server.getUser(event.getPlayer()).isService()) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        Material blockType = clickedBlock != null ? clickedBlock.getType() : Material.AIR;
        ItemStack item = event.getItem();

        if (!world.isBlockBreakAllowed()) {
            if (event.getAction() == Action.PHYSICAL) {
                if (clickedBlock == null) return;
                if (blockType == Material.FARMLAND) {
                    event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
                    event.setCancelled(true);
                    clickedBlock.setType(blockType, true);
                    clickedBlock.setBlockData(clickedBlock.getBlockData());
                }
            }
        }

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Set<Material> filledBuckets = Set.of(Material.LAVA_BUCKET, Material.COD_BUCKET, Material.AXOLOTL_BUCKET,
                    Material.POWDER_SNOW_BUCKET, Material.PUFFERFISH_BUCKET, Material.SALMON_BUCKET,
                    Material.TROPICAL_FISH_BUCKET, Material.WATER_BUCKET);

            if (item != null) {
                if (filledBuckets.contains(item.getType())) {
                    if (!world.isFluidPlaceAllowed() && !blockType.equals(Material.CAULDRON)) {
                        event.setCancelled(true);
                        event.setUseInteractedBlock(Event.Result.DENY);
                        event.setUseItemInHand(Event.Result.DENY);
                    }
                } else if (item.getType().equals(Material.BUCKET)) {
                    Set<Material> filledCauldrons = Set.of(Material.LAVA_CAULDRON, Material.WATER_CAULDRON);

                    if (!world.isFluidCollectAllowed() && !filledCauldrons.contains(blockType)) {
                        event.setCancelled(true);
                        event.setUseInteractedBlock(Event.Result.DENY);
                        event.setUseItemInHand(Event.Result.DENY);
                    }
                }
            }
        }

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            if (blockType.equals(Material.CAKE)) {
                if (!world.isCakeEatAllowed()) {
                    event.setCancelled(true);
                    event.setUseInteractedBlock(Event.Result.DENY);
                    event.setUseItemInHand(Event.Result.DENY);
                }
            }

            if (world.getLockedBlockInventories().contains(blockType)) {
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
                event.setUseItemInHand(Event.Result.DENY);
            }

            if (item != null) {
                if (item.getType().equals(Material.FLINT_AND_STEEL)) {
                    if (world.isFlintAndSteelAllowed()) {
                        return;
                    }

                    if (world.isLightUpInteractionAllowed() && (Tag.CAMPFIRES.isTagged(blockType) || Tag.CANDLES.isTagged(blockType))) {
                        return;
                    }

                    event.setCancelled(true);
                    event.setUseInteractedBlock(Event.Result.DENY);
                    event.setUseItemInHand(Event.Result.DENY);
                } else if (item.getType().equals(Material.SPLASH_POTION)) {
                    if (world.isLightUpInteractionAllowed()) {
                        return;
                    }

                    if (!Tag.CAMPFIRES.isTagged(blockType)) {
                        return;
                    }

                    event.setCancelled(true);
                    event.setUseInteractedBlock(Event.Result.DENY);
                    event.setUseItemInHand(Event.Result.DENY);
                } else {
                    if (world.isPlaceInBlockAllowed()) {
                        return;
                    }

                    if (item.getType().equals(Material.ENDER_EYE) && blockType.equals(Material.END_PORTAL_FRAME)) {
                        event.setCancelled(true);
                        event.setUseInteractedBlock(Event.Result.DENY);
                        event.setUseItemInHand(Event.Result.DENY);
                        return;
                    }

                    if (Tag.CANDLES.isTagged(item.getType()) && (blockType.equals(Material.CAKE) || Tag.CANDLES.isTagged(blockType))) {
                        event.setCancelled(true);
                        event.setUseInteractedBlock(Event.Result.DENY);
                        event.setUseItemInHand(Event.Result.DENY);
                        return;
                    }

                    if (item.getType().equals(Material.SEA_PICKLE) && blockType.equals(Material.SEA_PICKLE)) {
                        event.setCancelled(true);
                        event.setUseInteractedBlock(Event.Result.DENY);
                        event.setUseItemInHand(Event.Result.DENY);
                        return;
                    }
                }
            }
        } else if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {

            if (blockType.equals(Material.FIRE)) {
                if (!world.isFirePunchOutAllowed()) {
                    event.setCancelled(true);
                    event.setUseInteractedBlock(Event.Result.DENY);
                    event.setUseItemInHand(Event.Result.DENY);
                }
            }
        }

    }

    @EventHandler
    public void onItemFrameChange(PlayerItemFrameChangeEvent e) {
        ExWorld world = this.getWorld(e.getPlayer().getWorld());

        if (Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        if (world.isEntityBlockBreakAllowed()) {
            return;
        }

        if (e.getAction().equals(PlayerItemFrameChangeEvent.ItemFrameChangeAction.ROTATE)
                && world.isItemFrameRotateAllowed()) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        ExWorld world = this.getWorld(e.getPlayer().getWorld());

        if (world.isDropPickItemAllowed()) {
            return;
        }

        if (Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(UserDamageEvent e) {
        ExWorld world = e.getUser().getExWorld();

        if (world.isPlayerDamageAllowed()) {
            return;
        }

        if (e.getUser().isService()) {
            return;
        }

        e.setCancelled(true);
        e.setCancelDamage(true);
    }

    @EventHandler
    public void onPlayerPickUpItem(@Deprecated PlayerPickupItemEvent e) {
        ExWorld world = this.getWorld(e.getPlayer().getWorld());

        if (world.isDropPickItemAllowed()) {
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
        ExWorld world = this.getWorld(e.getPlayer().getWorld());

        if (world.isDropPickItemAllowed()) {
            return;
        }

        if (Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        ExWorld world = this.getWorld(e.getPlayer().getWorld());

        if (world.isBlockBreakAllowed()) {
            return;
        }

        if (Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        if ((e.getBlock().getType().equals(Material.FIRE)
                || (Tag.CANDLES.isTagged(e.getBlock().getType())) && ((Candle) e.getBlock().getState()).isLit())
                && world.isFirePunchOutAllowed()) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onArmorStand(PlayerArmorStandManipulateEvent e) {
        ExWorld world = this.getWorld(e.getPlayer().getWorld());

        if (world.isBlockBreakAllowed() && world.isEntityBlockBreakAllowed()) {
            return;
        }

        if (Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onBoat(VehicleDestroyEvent e) {
        ExWorld world = this.getWorld(e.getAttacker().getWorld());

        if (world.isBlockBreakAllowed() && world.isEntityBlockBreakAllowed()) {
            return;
        }

        if (e.getAttacker() instanceof Player && Server.getUser(((Player) e.getAttacker())).isService()) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onPainting(HangingBreakByEntityEvent e) {
        ExWorld world = this.getWorld(e.getRemover().getWorld());

        if (world.isBlockBreakAllowed() && world.isEntityBlockBreakAllowed()) {
            return;
        }

        if (world.isExceptService() && e.getRemover() instanceof Player
                && Server.getUser(((Player) e.getRemover())).isService()) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void blockItemFrame(PlayerInteractEntityEvent e) {
        ExWorld world = this.getWorld(e.getPlayer().getWorld());

        if (world.isBlockBreakAllowed() && world.isEntityBlockBreakAllowed()) {
            return;
        }

        if (Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        if (e.getRightClicked().getType().equals(EntityType.ITEM_FRAME) && !world.isItemFrameRotateAllowed()) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onEntityDamage(EntityDamageByUserEvent e) {
        ExWorld world = this.getWorld(e.getUser().getWorld());

        if (world.isBlockBreakAllowed() && world.isEntityBlockBreakAllowed()) {
            return;
        }

        if (!e.getEntity().getType().equals(EntityType.ARMOR_STAND) && !e.getEntity().getType().equals(EntityType.ITEM_FRAME)) {
            return;
        }

        if (e.getUser().isService()) {
            return;
        }

        e.setCancelDamage(true);
        e.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        ExWorld world = this.getWorld(e.getEntity().getWorld());

        if (world.isFoodChangeAllowed()) {
            return;
        }

        if (Server.getUser(e.getEntity().getUniqueId()).isService()) {
            return;
        }

        e.setCancelled(true);
        if (e.getEntity() instanceof Player) {
            e.getEntity().setFoodLevel(20);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        ExWorld world = this.getWorld(e.getEntity().getWorld());

        if (world.isEntityExplodeAllowed()) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        ExWorld world = this.getWorld(e.getPlayer().getWorld());

        ItemStack item = e.getItemInHand();
        Block block = e.getBlock();
        Material blockType = block.getType();

        if (item.getType().equals(Material.ENDER_EYE) && blockType.equals(Material.END_PORTAL_FRAME)) {
            e.setCancelled(!world.isPlaceInBlockAllowed());
            return;
        }

        if (Tag.CANDLES.isTagged(item.getType()) && (blockType.equals(Material.CAKE) || Tag.CANDLES.isTagged(blockType))) {
            e.setCancelled(!world.isPlaceInBlockAllowed());
            return;
        }

        if (item.getType().equals(Material.SEA_PICKLE) && blockType.equals(Material.SEA_PICKLE)) {
            e.setCancelled(!world.isPlaceInBlockAllowed());
            return;
        }

        if (item.getType().equals(Material.FLINT_AND_STEEL)) {
            if (world.isFlintAndSteelAllowed()) {
                return;
            }

            if (world.isLightUpInteractionAllowed() && (Tag.CAMPFIRES.isTagged(e.getBlock().getType()) || Tag.CANDLES.isTagged(e.getBlock().getType()))) {
                return;
            }

            e.setCancelled(true);
            e.setBuild(false);
            return;
        }

        if (world.isBlockPlaceAllowed()) {
            return;
        }

        if (Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        e.setCancelled(true);
        e.setBuild(false);
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent e) {
        ExWorld world = this.getWorld(e.getBlock().getWorld());

        if (world.isFireSpreadAllowed()) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent e) {
        ExWorld world = this.getWorld(e.getBlock().getWorld());

        if (world.isBlockBurnUpAllowed()) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e) {
        ExWorld world = this.getWorld(e.getBlock().getWorld());

        if (world.isBlockIgniteAllowed()) {
            return;
        }

        if (world.isFlintAndSteelAllowed()) {
            return;
        }

        if (world.isLightUpInteractionAllowed() && (Tag.CAMPFIRES.isTagged(e.getBlock().getType()) || Tag.CANDLES.isTagged(e.getBlock().getType()))) {
            return;
        }

        if (Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        e.setCancelled(true);
    }
}
