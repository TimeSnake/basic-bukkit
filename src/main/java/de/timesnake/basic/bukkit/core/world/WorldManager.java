/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.world;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.world.ExLocation;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import de.timesnake.basic.bukkit.util.world.ExWorldType;
import de.timesnake.library.basic.util.Loggers;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

public class WorldManager implements Listener, de.timesnake.basic.bukkit.util.world.WorldManager {

    private static final int TMP_DESTROY_TICKS = 3 * 60 * 20;

    private final WorldBorderManager worldBorderManager;
    private final WorldEventManager worldEventManager;

    private final File backupDir;
    private final Map<String, ExWorld> worldsByName = new HashMap<>();
    private final Map<UUID, Map<ExWorld, ExLocation>> locationsPerWorldByUuid = new HashMap<>();

    private final Set<BukkitTask> tmpWorldDestroyTasks = new HashSet<>();

    private boolean cacheWorldSpawns = false;

    public WorldManager() {
        Server.registerListener(this, BasicBukkit.getPlugin());

        this.worldBorderManager = new WorldBorderManager();
        this.worldEventManager = new WorldEventManager(this);

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
                Loggers.WORLDS.info("Loaded world '" + world.getName() + "'");
            }
        }

        this.loadUserLocationsFromFile();
    }

    @Override
    public boolean cacheWorldSpawns() {
        return this.cacheWorldSpawns;
    }

    @Override
    public void setCacheWorldSpawns(boolean cacheWorldSpawns) {
        this.cacheWorldSpawns = cacheWorldSpawns;
    }

    public @Nullable ExWorld createWorldFromFile(String name) {
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
                Loggers.WORLDS.warning("Illegal locations file name '" + fileName + "'");
                continue;
            }

            LocationsFile file = new LocationsFile(uuid);

            HashMap<ExWorld, ExLocation> locationByWorld = new HashMap<>();

            for (ExWorld world : this.getWorlds()) {
                locationByWorld.put(world, new ExLocation(world, file.getUserLocation(world)));
            }

            this.locationsPerWorldByUuid.put(uuid, locationByWorld);
        }

        Loggers.WORLDS.info("Loaded user locations");
    }

    public void onDisable() {
        for (ExWorld exWorld : this.getWorlds()) {
            if (!exWorld.isSafe()) {
                this.moveUsersFromWorld(exWorld);
                Bukkit.unloadWorld(exWorld.getName(), false);
            }

            if (exWorld.isTemporary()) {
                this.deleteWorldFiles(exWorld);
            }
        }

        this.tmpWorldDestroyTasks.forEach(BukkitTask::cancel);

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
        this.locationsPerWorldByUuid.get(user.getUniqueId())
                .put(user.getExWorld(), user.getExLocation());
    }

    private void saveLocations() {
        for (Map.Entry<UUID, Map<ExWorld, ExLocation>> entry : this.locationsPerWorldByUuid.entrySet()) {
            UUID uuid = entry.getKey();
            LocationsFile file = new LocationsFile(uuid);
            for (ExLocation location : entry.getValue().values()) {
                file.setUserLocation(location);
            }
        }
        Loggers.WORLDS.info("Saved user locations");
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
    public @Nullable ExWorld getBasicWorld() {
        return this.getWorld("world");
    }

    @Override
    public @Nullable ExWorld getWorld(String name) {
        return this.worldsByName.get(name);
    }

    @Override
    public @Nullable ExWorld getWorld(World world) {
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
    public @Nullable ExWorld createWorld(String name) {
        return this.createWorld(name, ExWorldType.NORMAL);
    }

    @Override
    public @Nullable ExWorld createWorld(String name, ExWorldType type) {
        return this.createWorld(name, type, false);
    }

    @Override
    public @Nullable ExWorld createWorld(String name, ExWorldType type, boolean temporary) {

        if (type == null) {
            Loggers.WORLDS.warning("Can not load type of world '" + name + "'");
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
        ExWorld exWorld = new ExWorld(world, type, file, temporary);
        this.registerExWorld(exWorld);

        if (temporary) {
            this.tmpWorldDestroyTasks.add(
                    Server.runTaskLaterSynchrony(() -> this.deleteWorld(exWorld, true),
                            TMP_DESTROY_TICKS, BasicBukkit.getPlugin()));
        }

        return exWorld;
    }

    @Override
    public @Nullable ExWorld cloneWorld(String name, ExWorld exWorld) {
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
        ExWorld clonedExWorld = new ExWorld(world, exWorld.getType(), file,
                exWorld.getRestrictionValues());
        this.registerExWorld(clonedExWorld);
        return clonedExWorld;
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
            File file = new File(
                    exWorld.getWorldFolder().getAbsolutePath() + File.separator + fileName);
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
            unloaded &= this.deleteWorldFiles(exWorld);
        }

        return unloaded;
    }

    private boolean deleteWorldFiles(ExWorld world) {
        try {
            FileUtils.deleteDirectory(world.getWorldFolder());
        } catch (IOException e) {
            return false;
        }

        return true;
    }


    private void moveUsersFromWorld(ExWorld world) {
        for (User user : Server.getUsers()) {
            if (user.getWorld().equals(world)) {
                user.teleport(this.getBasicWorld());
            }
        }
    }

    private @Nullable ExWorld createWorld(World world) {
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
                    if (!target.exists()) {
                        if (!target.mkdirs()) {
                            throw new IOException("Couldn't create world directory!");
                        }
                    }
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
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
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
            this.locationsPerWorldByUuid.get(user.getUniqueId())
                    .put(fromWorld, new ExLocation(fromWorld, fromLoc));
        }
    }

    @EventHandler
    public void onWorldInit(WorldInitEvent e) {
        e.getWorld().setKeepSpawnInMemory(this.cacheWorldSpawns);
    }
}
