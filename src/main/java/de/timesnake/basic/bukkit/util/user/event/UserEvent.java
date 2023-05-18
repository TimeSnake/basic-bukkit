/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.event.Event;

public abstract class UserEvent extends Event {

  private final User user;

  protected UserEvent(User user) {
    this.user = user;
  }

  protected UserEvent(boolean async, User user) {
    super(async);
    this.user = user;
  }

  public User getUser() {
    return user;
  }

}
