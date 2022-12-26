/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import de.timesnake.basic.bukkit.util.user.User;

public interface Board {
    String getName();

    void addWatchingUser(User user);

    void removeWatchingUser(User user);
}
