/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world.entity;

import de.timesnake.basic.bukkit.core.world.PacketEntityManager;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.world.ExLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PacketEntity {

  protected final Logger logger = LogManager.getLogger("packet-entity.entity");

  protected final ExLocation location;
  protected final Set<User> watchers = ConcurrentHashMap.newKeySet();
  protected final Set<User> userLoadedFor = ConcurrentHashMap.newKeySet();
  protected boolean isPublic = false;

  public PacketEntity(ExLocation location) {
    this.location = location;
  }

  public ExLocation getLocation() {
    return location;
  }

  public boolean isPublic() {
    return isPublic;
  }

  public void setPublic(boolean aPublic) {
    isPublic = aPublic;
    for (User user : Server.getUsers()) {
      ((PacketEntityManager) Server.getEntityManager()).tryLoadEntityForUser(this, user);
    }
  }

  public void addWatcher(User... users) {
    for (User user : users) {
      this.watchers.add(user);
      ((PacketEntityManager) Server.getEntityManager()).tryLoadEntityForUser(this, user);
    }
  }

  public void removeWatcher(User... users) {
    for (User user : users) {
      this.watchers.remove(user);
      this.userLoadedFor.remove(user);
      this.despawnForUser(user);
    }
  }

  protected abstract void spawnForUser(User user);

  protected abstract void despawnForUser(User user);

  public void despawnForUsers() {
    for (User user : this.userLoadedFor) {
      this.despawnForUser(user);
    }
    this.userLoadedFor.clear();
    this.watchers.clear();

    this.logger.info("Unloaded entity at '{} {} {} {}' of type '{}' for all users",
        this.location.getWorld().getName(), this.location.getBlockX(), this.location.getBlockY(),
        this.location.getBlockZ(), this.getType());
  }

  public boolean isLoadedForUser(User user) {
    return this.userLoadedFor.contains(user);
  }

  public void loadForUser(User user) {
    this.userLoadedFor.add(user);
    this.spawnForUser(user);

    this.logger.info("Loaded entity at '{} {} {} {}' of type '{}' for user '{}'", this.location.getWorld().getName(),
        this.location.getBlockX(), this.location.getBlockY(), this.location.getBlockZ(), this.getType(),
        user.getName());
  }

  public void unloadForUser(User user) {
    this.userLoadedFor.remove(user);
    this.despawnForUser(user);

    this.logger.info("Unloaded entity at '{} {} {} {}' of type '{}' for user '{}'",
        this.location.getWorld().getName(), this.location.getBlockX(), this.location.getBlockY(),
        this.location.getBlockZ(), this.getType(), user.getName());
  }

  public boolean isUserWatching(User user) {
    return this.isPublic || this.watchers.contains(user);
  }

  public void onUserQuit(User user) {
    this.watchers.remove(user);
  }

  public abstract String getType();
}
