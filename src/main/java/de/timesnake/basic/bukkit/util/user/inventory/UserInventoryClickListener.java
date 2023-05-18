/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.inventory;

@FunctionalInterface
public interface UserInventoryClickListener {

  void onUserInventoryClick(UserInventoryClickEvent event);

}
