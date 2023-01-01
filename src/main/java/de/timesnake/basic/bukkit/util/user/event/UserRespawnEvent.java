/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import javax.annotation.Nonnull;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;

public class UserRespawnEvent extends UserEvent {

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    private Location respawnLocation;

    public UserRespawnEvent(User user, Location respawnLocation) {
        super(user);
        this.respawnLocation = respawnLocation;
    }

    @Nonnull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Location getRespawnLocation() {
        return respawnLocation;
    }

    public void setRespawnLocation(Location respawnLocation) {
        this.respawnLocation = respawnLocation;
    }
}
