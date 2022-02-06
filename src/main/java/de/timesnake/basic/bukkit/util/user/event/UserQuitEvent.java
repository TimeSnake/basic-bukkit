package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class UserQuitEvent extends UserEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Nonnull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public UserQuitEvent(User user) {
        super(user);
    }
}
