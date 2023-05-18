/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;

public class UserInteractEntityEvent extends CancelableUserEvent {

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  private static final HandlerList HANDLERS = new HandlerList();
  private final Entity entity;
  private final EquipmentSlot hand;

  public UserInteractEntityEvent(User user, boolean isCanceled, Entity entity, EquipmentSlot hand) {
    super(user, isCanceled);
    this.entity = entity;
    this.hand = hand;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public Entity getEntity() {
    return entity;
  }

  public EquipmentSlot getHand() {
    return hand;
  }
}
