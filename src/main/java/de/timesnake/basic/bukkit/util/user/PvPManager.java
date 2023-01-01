/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user;

public interface PvPManager {
    void setPvP(boolean oldPvP);

    boolean isOldPvP();

    void broadcastPvPTypeMessage();
}
