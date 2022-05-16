package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class AsyncUserMoveEvent extends UserEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Location from;
    private final Location to;

    public AsyncUserMoveEvent(User user, Location from, Location to) {
        super(true, user);
        this.from = from;
        this.to = to;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
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
