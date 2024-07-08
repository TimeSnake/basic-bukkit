/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;
import java.util.List;

public interface WorldManager {

  List<String> UNSUPPORTED_SYMBOLS = List.of("_backup", ";", ",", ":", "*", "+", "#", "@", "$",
      "&", "/", "\\");

  boolean copyWorldFolderFiles(File source, File target);

  boolean cacheWorldSpawns();

  void setCacheWorldSpawns(boolean cacheWorldSpawns);

  Collection<ExWorld> getWorlds();

  @Nullable ExWorld getWorld(String name);

  @Nullable ExWorld getWorld(World world);

  ExLocation getUserLocation(User user, ExWorld world);

  WorldBorderManager getWorldBorderManager();

  @Nullable
  ExWorld getDefaultWorld();

  /**
   * Deletes a world
   * <p>
   * This method deletes the {@link World} and {@link ExWorld} Beware of saved locations, they are
   * invalid after the deletion.
   *
   * @param world       The {@link ExWorld} to delete
   * @param deleteFiles If set, all world files will be deleted
   * @return true if the deletion was successfully, else false
   */
  boolean deleteWorld(ExWorld world, boolean deleteFiles);

  @Nullable
  ExWorld cloneWorld(String cloneName, ExWorld originalWorld);

  @Nullable ExWorld createWorldFromFile(String name);

  @Nullable ExWorld createWorld(String name);

  @Nullable ExWorld createWorld(String name, ExWorldType type);

  @Nullable ExWorld createWorld(String name, ExWorldType type, boolean temporary);

  /**
   * Reloads a world from disk
   * <p>
   * This method creates a new {@link World} from the disk and inject it into the {@link ExWorld}.
   * Changes in the world will not be saved (except auto-save). It is recommended to use this with
   * auto-save off. Beware of saved locations, they are invalid after the load. Use
   * {@link ExLocation}s to prevent errors.
   *
   * @param world The {@link ExWorld} to load the reload
   * @return true if the reload was successfully, else false
   */
  boolean reloadWorld(ExWorld world);

  boolean unloadWorld(ExWorld world, boolean save);

  enum WorldLoadActionType {
    CREATE, RELOAD, CLONE
  }

  enum WorldUnloadActionType {
    RELOAD, UNLOAD, DELETE
  }
}
