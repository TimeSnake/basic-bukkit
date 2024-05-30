/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ExWorldUnloadEvent extends Event {

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  private static final HandlerList HANDLERS = new HandlerList();

  private final ExWorld world;
  private final WorldManager.WorldUnloadActionType actionType;

  public ExWorldUnloadEvent(ExWorld world, WorldManager.WorldUnloadActionType actionType) {
    this.world = world;
    this.actionType = actionType;
  }

  public ExWorld getWorld() {
    return world;
  }

  public WorldManager.WorldUnloadActionType getActionType() {
    return actionType;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

}
