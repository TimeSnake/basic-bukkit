/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.inventory;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class UserInventoryClickEvent extends UserInventoryEvent {

  private final InventoryView view;
  private final Inventory inventory;
  private final Integer slot;
  private final ClickType clickType;
  private final InventoryAction action;

  public UserInventoryClickEvent(User user, boolean isCanceled, InventoryView view,
      Inventory inventory,
      ExItemStack clickedItem, Integer slot, ClickType clickType, InventoryAction action) {
    super(user, isCanceled, clickedItem);
    this.view = view;
    this.inventory = inventory;
    this.slot = slot;
    this.clickType = clickType;
    this.action = action;
  }

  public InventoryView getView() {
    return view;
  }

  public Inventory getInventory() {
    return inventory;
  }

  public Integer getSlot() {
    return slot;
  }

  public ClickType getClickType() {
    return clickType;
  }

  public InventoryAction getAction() {
    return action;
  }
}
