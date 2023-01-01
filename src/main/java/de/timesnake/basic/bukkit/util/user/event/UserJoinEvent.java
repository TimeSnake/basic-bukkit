/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Called when user is registered, by {@link PlayerJoinEvent}
 */
public class UserJoinEvent extends UserEvent {

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public UserJoinEvent(User user) {
        super(user);
    }

    public UserJoinEvent(boolean async, User user) {
        super(async, user);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
