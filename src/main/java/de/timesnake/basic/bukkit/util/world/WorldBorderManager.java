/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world;

public interface WorldBorderManager {
    boolean isCustomBorders();

    void setCustomBorders(boolean customBorders);

    boolean isEnderpearlThrouBorderAllowed();

    void allowEnderpearlThrouBorder(boolean allowEnderpearl);
}
