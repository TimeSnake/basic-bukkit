/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import javax.annotation.Nonnull;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerTeleportEvent;

public class UserTeleportEvent extends UserMoveEvent {

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final PlayerTeleportEvent.TeleportCause cause;

    public UserTeleportEvent(User user, boolean cancelled, Location from, Location to, PlayerTeleportEvent.TeleportCause cause) {
        super(user, cancelled, from, to);
        this.cause = cause;
    }

    @Nonnull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public PlayerTeleportEvent.TeleportCause getCause() {
        return cause;
    }
}
