/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import javax.annotation.Nonnull;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Called by {@link PlayerMoveEvent}
 */
public class UserMoveEvent extends CancelableUserEvent {

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    private final Location from;
    private final Location to;

    public UserMoveEvent(User user, boolean isCanceled, Location from, Location to) {
        super(user, isCanceled);
        this.from = from;
        this.to = to;
    }

    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }
}
