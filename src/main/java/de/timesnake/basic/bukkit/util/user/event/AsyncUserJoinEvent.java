package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.event.HandlerList;

public class AsyncUserJoinEvent extends UserJoinEvent {

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public AsyncUserJoinEvent(User user) {
        super(true, user);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
