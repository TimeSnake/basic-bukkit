/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class UserEvent extends Event {

  private final User user;

  protected UserEvent(@NotNull User user) {
    this.user = user;
  }

  protected UserEvent(boolean async, @NotNull User user) {
    super(async);
    this.user = user;
  }

  public @NotNull User getUser() {
    return user;
  }

}
