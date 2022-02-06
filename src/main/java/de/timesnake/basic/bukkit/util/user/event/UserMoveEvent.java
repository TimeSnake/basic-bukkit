package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

import javax.annotation.Nonnull;

/**
 * Called by {@link PlayerMoveEvent}
 */
public class UserMoveEvent extends CancelableUserEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private final Location from;
    private final Location to;

    public UserMoveEvent(User user, boolean isCanceled, Location from, Location to) {
        super(user, isCanceled);
        this.from = from;
        this.to = to;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }
}
