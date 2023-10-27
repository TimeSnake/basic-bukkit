/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.jetbrains.annotations.NotNull;

public abstract class CancelableUserEvent extends UserEvent {

  private boolean isCancelled;

  protected CancelableUserEvent(@NotNull User user, boolean isCancelled) {
    super(user);
    this.isCancelled = isCancelled;
  }

  public boolean isCancelled() {
    return isCancelled;
  }

  public void setCancelled(boolean cancelled) {
    isCancelled = isCancelled || cancelled;
  }
}
