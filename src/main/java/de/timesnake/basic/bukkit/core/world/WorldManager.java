/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.world;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.world.*;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;

public class WorldManager implements Listener, de.timesnake.basic.bukkit.util.world.WorldManager {

  private static final int TMP_DESTROY_TICKS = 3 * 60 * 20;

  private final Logger logger = LogManager.getLogger("world.manager");

  private final WorldBorderManager worldBorderManager;
  private final WorldEventManager worldEventManager;

  private final Map<String, ExWorld> worldsByName = new HashMap<>();
  private final Map<UUID, Map<ExWorld, ExLocation>> locationsPerWorldByUuid = new HashMap<>();

  private final Set<BukkitTask> tmpWorldDestroyTasks = new HashSet<>();

  private boolean cacheWorldSpawns = false;

  public WorldManager() {
    Server.registerListener(this, BasicBukkit.getPlugin());

    this.worldBorderManager = new WorldBorderManager();
    this.worldEventManager = new WorldEventManager(this);

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
        this.logger.info("Loaded world '{}'", world.getName());
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
        this.logger.warn("Illegal locations file name '{}'", fileName);
        continue;
      }

      LocationsFile file = new LocationsFile(uuid);

      HashMap<ExWorld, ExLocation> locationByWorld = new HashMap<>();

      for (ExWorld world : this.getWorlds()) {
        locationByWorld.put(world, new ExLocation(world, file.getUserLocation(world.getBukkitWorld())));
      }

      this.locationsPerWorldByUuid.put(uuid, locationByWorld);
    }

    this.logger.info("Loaded user locations");
  }

  public void onDisable() {
    for (ExWorld world : this.getWorlds()) {
      if (!world.isSafe()) {
        this.onWorldUnload(world, WorldUnloadActionType.UNLOAD);
        Bukkit.unloadWorld(world.getName(), false);
      }

      if (world.isTemporary()) {
        this.deleteWorldFiles(world);
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
    this.logger.info("Saved user locations");
  }

  @Override
  public @NotNull ExLocation getUserLocation(User user, ExWorld world) {
    ExLocation location = this.locationsPerWorldByUuid.get(user.getUniqueId()).get(world);
    if (location == null) {
      location = new ExLocation(world, world.getSpawnLocation());
      this.locationsPerWorldByUuid.get(user.getUniqueId()).put(world, location);
    }
    return location;
  }


  @Override
  public @NotNull WorldBorderManager getWorldBorderManager() {
    return worldBorderManager;
  }

  @Override
  public @Nullable ExWorld getDefaultWorld() {
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
  public @NotNull Collection<ExWorld> getWorlds() {
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
      this.logger.warn("Can not load type of world '{}'", name);
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
    this.onWorldLoad(exWorld, WorldLoadActionType.CREATE);

    if (temporary) {
      this.tmpWorldDestroyTasks.add(Server.runTaskLaterSynchrony(() -> this.deleteWorld(exWorld, true),
          TMP_DESTROY_TICKS, BasicBukkit.getPlugin()));
    }

    return exWorld;
  }

  private @Nullable ExWorld createWorld(World bukkitWorld) {
    if (bukkitWorld == null) {
      return null;
    }

    if (Bukkit.getWorld(bukkitWorld.getName()) == null) {
      bukkitWorld = Bukkit.createWorld(new WorldCreator(bukkitWorld.getName()).copy(bukkitWorld));
    }

    ExWorldFile file = new ExWorldFile(bukkitWorld.getWorldFolder(), ExWorldType.fromWorld(bukkitWorld));
    ExWorld world = new ExWorld(bukkitWorld, ExWorldType.fromWorld(bukkitWorld), file);
    this.registerExWorld(world);
    this.onWorldLoad(world, WorldLoadActionType.CREATE);
    return world;
  }

  @Override
  public @Nullable ExWorld cloneWorld(String cloneName, ExWorld originalWorld) {
    if (originalWorld == null) {
      return null;
    }

    for (String s : UNSUPPORTED_SYMBOLS) {
      cloneName = cloneName.replace(s, "");
    }

    // world exits already
    if (this.worldsByName.containsKey(cloneName)) {
      return null;
    }

    // world exists in Bukkit
    if (Bukkit.getWorld(cloneName) != null) {
      return null;
    }

    originalWorld.save();

    this.copyWorldFolderFiles(originalWorld.getWorldFolder(),
        new File(Bukkit.getWorldContainer() + File.separator + cloneName));

    World world = Bukkit.createWorld(new WorldCreator(cloneName).copy(originalWorld.getBukkitWorld()));

    if (world == null) {
      return null;
    }

    ExWorldFile file = new ExWorldFile(world.getWorldFolder(), originalWorld.getType());
    ExWorld clonedExWorld = new ExWorld(world, originalWorld.getType(), file, originalWorld.getRestrictionValues());
    this.registerExWorld(clonedExWorld);
    this.onWorldLoad(clonedExWorld, WorldLoadActionType.CLONE);
    return clonedExWorld;
  }

  @Override
  public boolean reloadWorld(ExWorld world) {
    if (world == null) {
      return false;
    }

    this.onWorldUnload(world, WorldUnloadActionType.RELOAD);
    World bukkitWorld = world.getBukkitWorld();

    boolean successfully = Bukkit.unloadWorld(bukkitWorld, false);

    List<String> deleteFiles = List.of("session.lock", "uid.dat");

    for (String fileName : deleteFiles) {
      File file = new File(world.getWorldFolder().getAbsolutePath() + File.separator + fileName);
      if (file.exists()) {
        file.delete();
      } else {
        return false;
      }
    }

    World newWorld = Bukkit.createWorld(new WorldCreator(world.getName()));
    world.setBukkitWorld(newWorld);
    this.onWorldLoad(world, WorldLoadActionType.RELOAD);

    return successfully;
  }

  @Override
  public boolean unloadWorld(ExWorld world, boolean save) {
    if (world == null) {
      return false;
    }

    this.onWorldUnload(world, WorldUnloadActionType.UNLOAD);
    boolean unloaded = Bukkit.unloadWorld(world.getBukkitWorld(), save);
    if (unloaded) {
      this.worldsByName.remove(world.getName());
    }
    return unloaded;
  }

  @Override
  public boolean deleteWorld(ExWorld world, boolean deleteFiles) {
    if (world == null) {
      return false;
    }

    this.onWorldUnload(world, WorldUnloadActionType.DELETE);
    boolean unloaded = Bukkit.unloadWorld(world.getBukkitWorld(), false);
    this.unregisterExWorld(world);

    if (deleteFiles) {
      unloaded &= this.deleteWorldFiles(world);
    }

    return unloaded;
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

  private void onWorldLoad(ExWorld world, WorldLoadActionType actionType) {
    Bukkit.getPluginManager().callEvent(new ExWorldLoadEvent(world, actionType));
  }

  private void onWorldUnload(ExWorld world, WorldUnloadActionType actionType) {
    Bukkit.getPluginManager().callEvent(new ExWorldUnloadEvent(world, actionType));
    this.moveUsersFromWorld(world);
  }

  private boolean deleteWorldFiles(ExWorld world) {
    try {
      FileUtils.deleteDirectory(world.getWorldFolder());
    } catch (IOException e) {
      this.logger.warn("Exception while deleting world '{}': {}", world.getName(), e.getMessage());
      return false;
    }
    return true;
  }

  private void moveUsersFromWorld(ExWorld world) {
    for (User user : Server.getUsers()) {
      if (user.getExWorld().equals(world)) {
        user.teleport(this.getDefaultWorld());
      }
    }
  }

  @Override
  public boolean copyWorldFolderFiles(File source, File target) {
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

          if (files == null) {
            return false;
          }

          for (String file : files) {
            File srcFile = new File(source, file);
            File destFile = new File(target, file);
            boolean result = copyWorldFolderFiles(srcFile, destFile);
            if (!result) {
              return false;
            }
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
      return true;
    } catch (IOException e) {
      this.logger.warn("Exception while copying world '{}': {}", source.getName(), e.getMessage());
      return false;
    }
  }

  @EventHandler
  public void onPlayerTeleport(PlayerTeleportEvent e) {
    User user = Server.getUser(e.getPlayer());

    if (user == null) {
      return;
    }

    Location fromLoc = e.getFrom();

    user.setLastLocation(fromLoc);

    if (!fromLoc.getWorld().equals(e.getTo().getWorld())) {
      ExWorld fromWorld = this.getWorld(fromLoc.getWorld());
      this.locationsPerWorldByUuid.get(user.getUniqueId()).put(fromWorld, new ExLocation(fromWorld, fromLoc));
    }
  }

  @EventHandler
  public void onWorldInit(WorldInitEvent e) {
    e.getWorld().setKeepSpawnInMemory(this.cacheWorldSpawns);
  }
}
