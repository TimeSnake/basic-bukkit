/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard;

import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.library.extension.util.player.UserSet;
import java.util.Set;
import org.bukkit.scoreboard.Scoreboard;

public abstract class Board implements de.timesnake.basic.bukkit.util.user.scoreboard.Board {

  protected final String name;

  protected final UserSet<User> watchingUsers = new UserSet<>();

  protected Board(String name) {
    this.name = name;
  }

  /**
   * Gets the name of the {@link Board}, not the title
   *
   * @return the name
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Adds a {@link User} The {@link Scoreboard} of the {@link User} will be updated by a board
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
