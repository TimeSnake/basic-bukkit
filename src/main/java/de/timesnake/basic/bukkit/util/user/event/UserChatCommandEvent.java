/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class UserChatCommandEvent extends CancelableUserEvent {

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  private static final HandlerList HANDLERS = new HandlerList();
  private final String message;
  private boolean removeListener = true;

  public UserChatCommandEvent(User user, boolean isCanceled, String message) {
    super(user, isCanceled);
    this.message = message;
  }

  @Override
  @Nonnull
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public String getMessage() {
    return message;
  }

  public boolean isRemoveListener() {
    return removeListener;
  }

  public void removeListener(boolean removeListener) {
    this.removeListener = removeListener;
  }
}
