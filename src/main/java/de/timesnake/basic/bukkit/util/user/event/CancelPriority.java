/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.util.user.event;

/**
 * Determines if the event will be cancelled. The highest priority determines whether the event is cancelled or not.
 */
public enum CancelPriority {
    LOWEST,
    LOW,
    DEFAULT,
    HIGH,
    HIGHEST
}
