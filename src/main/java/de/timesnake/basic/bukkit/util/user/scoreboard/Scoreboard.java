/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import de.timesnake.basic.bukkit.util.user.User;

import java.util.Set;

public interface Scoreboard {

  String getName();

  void addWatchingUser(User user);

  void removeWatchingUser(User user);

  Set<User> getWatchingUsers();

}
