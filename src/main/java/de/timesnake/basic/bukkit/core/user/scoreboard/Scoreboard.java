/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard;

import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.scoreboard.DuplicateScoreboardException;
import de.timesnake.library.extension.util.player.UserSet;

import java.util.HashSet;
import java.util.Set;

public abstract class Scoreboard implements de.timesnake.basic.bukkit.util.user.scoreboard.Scoreboard {

  private static final Set<String> NAMES = new HashSet<>();

  protected final String name;

  protected final UserSet<User> watchingUsers = new UserSet<>();

  protected Scoreboard(String name) {
    if (NAMES.contains(name)) {
      throw new DuplicateScoreboardException(name);
    }
    NAMES.add(name);

    this.name = name;
  }

  /**
   * Gets the name of the {@link Scoreboard}, not the title
   *
   * @return the name
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Adds a {@link User} The {@link org.bukkit.scoreboard.Scoreboard} of the {@link User} will be updated by a board
   * change
   *
   * @param user The {@link User} to add
   */
  @Override
  public void addWatchingUser(User user) {
    this.watchingUsers.add(user);
  }

  /**
   * Removes a {@link User}
   *
   * @param user The {@link User} to remove
   */
  @Override
  public void removeWatchingUser(User user) {
    this.watchingUsers.remove(user);
  }

  @Override
  public Set<User> getWatchingUsers() {
    return watchingUsers;
  }
}
