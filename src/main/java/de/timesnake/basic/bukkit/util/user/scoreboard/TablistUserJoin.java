/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;

@FunctionalInterface
public interface TablistUserJoin {

    void onUserJoin(UserJoinEvent e, Tablist tablist);
}
