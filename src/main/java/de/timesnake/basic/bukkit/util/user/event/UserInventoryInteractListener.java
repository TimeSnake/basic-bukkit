/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.event;

@FunctionalInterface
public interface UserInventoryInteractListener {

    void onUserInventoryInteract(UserInventoryInteractEvent event);
}
