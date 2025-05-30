/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user;

import de.timesnake.basic.bukkit.util.ServerManager;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.library.basic.util.Status;
import de.timesnake.library.basic.util.UserMap;
import de.timesnake.library.basic.util.UserSet;
import de.timesnake.library.chat.ExTextColor;
import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserManager implements de.timesnake.basic.bukkit.util.user.UserManager {

  private final Logger logger = LogManager.getLogger("user.manager");

  private final ConcurrentMap<UUID, User> users = new ConcurrentHashMap<>();
  private final ConcurrentMap<UUID, Future<User>> preUsers = new ConcurrentHashMap<>();
  private final ConcurrentMap<UUID, User> postUsers = new ConcurrentHashMap<>();

  /**
   * Stores the loaded user in preUsers map
   *
   * @param user to store
   */
  public final void storeUser(UUID uuid, Future<User> user) {
    this.preUsers.put(uuid, user);
  }

  /**
   * Loads the preUser in users
   *
   * @param uuid of the user
   */
  public final User registerUser(UUID uuid) {
    User user;

    try {
      user = this.preUsers.get(uuid).get(3, TimeUnit.SECONDS);
    } catch (InterruptedException | ExecutionException e) {
      this.logger.error("Failed to register user", e);
      user = ServerManager.getInstance().loadUser(Bukkit.getPlayer(uuid));
    } catch (TimeoutException e) {
      Bukkit.getPlayer(uuid).kick(Component.text("A fatal error occurred (user init timeout)", ExTextColor.WARNING));
      return null;
    }

    this.users.put(uuid, user);
    this.preUsers.remove(uuid);

    user.load();
    return user;
  }

  public final void markUserForRemoval(User user) {
    this.postUsers.put(user.getUniqueId(), user);
    this.users.remove(user.getUniqueId());
  }

  public final void removeUser(UUID uuid) {
    de.timesnake.basic.bukkit.util.user.User user = this.getUser(uuid);
    UserSet.SETS.forEach(l -> l.removeAuto(user));
    UserMap.MAPS.forEach(l -> l.removeAuto(user));
    this.users.remove(uuid);
    this.postUsers.remove(uuid);
  }

  public final Collection<User> getUsers() {
    return this.users.values();
  }

  public final Collection<User> getUsers(Predicate<User> predicate) {
    return this.getUsers().stream().filter(predicate).collect(Collectors.toSet());
  }

  /**
   * Gets the user who are not having the status service
   *
   * @return the users
   */
  public final Collection<User> getNotServiceUsers() {
    return this.getUsers((u) -> !u.isService());
  }

  /**
   * Gets the user who are having the status service
   *
   * @return the users
   */
  public final Collection<User> getServiceUsers() {
    return this.getUsers(User::isService);
  }

  /**
   * Gets the user who are having the status pregrame
   *
   * @return the users
   */
  public final Collection<User> getPreGameUsers() {
    return this.getUsers(u -> u.hasStatus(Status.User.PRE_GAME));
  }

  /**
   * Gets the user who are having the status ingame
   *
   * @return the users
   */
  public final Collection<User> getInGameUsers() {
    return this.getUsers(u -> u.hasStatus(Status.User.IN_GAME));
  }

  /**
   * Gets the user who are having the status outgame
   *
   * @return the users
   */
  public final Collection<User> getOutGameUsers() {
    return this.getUsers(u -> u.hasStatus(Status.User.OUT_GAME));
  }

  /**
   * Gets the user who are having the status online
   *
   * @return the users
   */
  public final Collection<User> getOnlineUsers() {
    return this.getUsers(u -> u.hasStatus(Status.User.ONLINE));
  }

  /**
   * Gets the user who are having the status spectator
   *
   * @return the users
   */
  public final Collection<User> getSpectatorUsers() {
    return this.getUsers(u -> u.hasStatus(Status.User.SPECTATOR));
  }

  /**
   * Gets the user who are having the status outgame out ingame
   *
   * @return the users
   */
  @Override
  public Collection<User> getInOutGameUsers() {
    return this.getUsers(u -> u.hasStatus(Status.User.OUT_GAME, Status.User.IN_GAME));
  }

  /**
   * Gets the user who are having the status outgame, ingame, pregame, spectator
   *
   * @return the users
   */
  public final Collection<User> getGameUsers() {
    return this.getUsers(u -> u.hasStatus(Status.User.OUT_GAME, Status.User.IN_GAME, Status.User.PRE_GAME,
        Status.User.SPECTATOR));
  }

  /**
   * Gets the user who are having the status outgame, ingame, pregame, spectator and are not in
   * service mode
   *
   * @return the users
   */
  @Override
  public final Collection<User> getGameNotServiceUsers() {
    return this.getGameUsers().stream().filter((u) -> !u.isService())
        .collect(Collectors.toList());
  }

  /**
   * Gets the user by UUID
   *
   * @param uuid The uuid of the player
   * @return the user
   */
  public final User getUser(UUID uuid) {
    return this.users.getOrDefault(uuid, this.postUsers.get(uuid));
  }

  /**
   * Gets the user by player
   *
   * @param p The {@link Player} to get
   * @return the user
   */
  public final User getUser(Player p) {
    if (p == null) {
      return null;
    }
    return this.getUser(p.getUniqueId());
  }

}
