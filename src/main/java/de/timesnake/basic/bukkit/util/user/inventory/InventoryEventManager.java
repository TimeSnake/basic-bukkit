/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.inventory;

import org.bukkit.inventory.InventoryHolder;

public interface InventoryEventManager {

  /**
   * Adds a new {@link UserInventoryClickListener} for InventoryClickEvent
   *
   * @param listener The {@link UserInventoryClickListener} to add
   * @param holder   The {@link InventoryHolder} to add
   */
  void addClickListener(UserInventoryClickListener listener, InventoryHolder holder);

  /**
   * Adds a new {@link UserInventoryInteractListener} for InventoryClickEvent
   *
   * @param listener   The {@link UserInventoryInteractListener} to add
   * @param itemStacks The {@link ExItemStack}s to add
   */
  void addClickListener(UserInventoryClickListener listener, ExItemStack... itemStacks);

  /**
   * Adds a new {@link UserInventoryInteractListener} for InventoryClickEvent
   *
   * @param listener   The {@link UserInventoryInteractListener} to add
   * @param itemStacks The {@link ExItemStack}s to add
   */
  void addClickListener(UserInventoryClickListener listener, Iterable<ExItemStack> itemStacks);

  /**
   * Adds a new {@link UserInventoryInteractListener} for InventoryInteractEvent
   *
   * @param listener   The {@link UserInventoryInteractListener} to add
   * @param itemStacks The {@link ExItemStack}s to add
   */
  void addInteractListener(UserInventoryInteractListener listener, ExItemStack... itemStacks);

  /**
   * Adds a new {@link UserInventoryInteractListener} for InventoryInteractEvent
   *
   * @param listener   The {@link UserInventoryInteractListener} to add
   * @param preventDoubleClick Set true to prevent double clicks
   * @param itemStacks The {@link ExItemStack}s to add
   */
  void addInteractListener(UserInventoryInteractListener listener, boolean preventDoubleClick, ExItemStack... itemStacks);

  /**
   * Adds a new {@link UserInventoryInteractListener} for InventoryInteractEvent
   *
   * @param listener   The {@link UserInventoryInteractListener} to add
   * @param itemStacks The {@link ExItemStack}s to add
   */
  void addInteractListener(UserInventoryInteractListener listener,
      Iterable<ExItemStack> itemStacks);

  /**
   * Removes the click-listener
   *
   * @param listener The {@link UserInventoryClickListener} to remove
   */
  void removeClickListener(UserInventoryClickListener listener);

  /**
   * Removes the interact-listener
   *
   * @param listener The {@link UserInventoryInteractListener} to remove
   */
  void removeInteractListener(UserInventoryInteractListener listener);

  void excludeServiceUsersFromLocks(boolean exclude);

  boolean excludingServiceUsers();

  /**
   * Removes all listener for this item
   * <p>
   * <b>Warning</b>: This method removes all item listeners matching with this item id
   *
   * @param item The {@link ExItemStack} to remove the listeners from
   */
  void removeListeners(ExItemStack item);
}
