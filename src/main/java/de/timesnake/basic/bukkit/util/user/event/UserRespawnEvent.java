package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class UserRespawnEvent extends UserEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Nonnull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    private Location respawnLocation;

    public UserRespawnEvent(User user, Location respawnLocation) {
        super(user);
        this.respawnLocation = respawnLocation;
    }

    public Location getRespawnLocation() {
        return respawnLocation;
    }

    public void setRespawnLocation(Location respawnLocation) {
        this.respawnLocation = respawnLocation;
    }
}
