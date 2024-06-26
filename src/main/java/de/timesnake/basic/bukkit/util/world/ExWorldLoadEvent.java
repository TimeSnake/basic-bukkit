/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ExWorldLoadEvent extends Event {

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  private static final HandlerList HANDLERS = new HandlerList();

  private final ExWorld world;
  private final WorldManager.WorldLoadActionType actionType;

  public ExWorldLoadEvent(ExWorld world, WorldManager.WorldLoadActionType actionType) {
    this.world = world;
    this.actionType = actionType;
  }

  public ExWorld getWorld() {
    return world;
  }

  public WorldManager.WorldLoadActionType getActionType() {
    return actionType;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }
}
