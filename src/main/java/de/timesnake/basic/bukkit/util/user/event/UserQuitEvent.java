/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import javax.annotation.Nonnull;
import org.bukkit.event.HandlerList;

public class UserQuitEvent extends UserEvent {

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  private static final HandlerList HANDLERS = new HandlerList();

  public UserQuitEvent(User user) {
    super(user);
  }

  public UserQuitEvent(boolean async, User user) {
    super(async, user);
  }

  @Nonnull
  public HandlerList getHandlers() {
    return HANDLERS;
  }
}
