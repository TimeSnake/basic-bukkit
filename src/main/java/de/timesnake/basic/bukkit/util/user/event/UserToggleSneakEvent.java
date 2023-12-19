/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class UserToggleSneakEvent extends CancelableUserEvent {

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  private static final HandlerList HANDLERS = new HandlerList();

  private final boolean isSneaking;

  public UserToggleSneakEvent(@NotNull User user, boolean isCancelled, boolean isSneaking) {
    super(user, isCancelled);
    this.isSneaking = isSneaking;
  }

  public boolean isSneaking() {
    return isSneaking;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS;
  }
}
