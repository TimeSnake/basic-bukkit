package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Called when user is registered, by {@link PlayerJoinEvent}
 */
public class UserJoinEvent extends UserEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public UserJoinEvent(User user) {
        super(user);
    }
}
