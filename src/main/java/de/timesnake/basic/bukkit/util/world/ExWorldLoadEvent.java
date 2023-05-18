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

  public ExWorldLoadEvent(ExWorld world) {
    this.world = world;
  }

  public ExWorld getWorld() {
    return world;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }
}
