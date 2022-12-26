/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.util.user.event;

@FunctionalInterface
public interface UserInventoryClickListener {

    void onUserInventoryClick(UserInventoryClickEvent event);

}
