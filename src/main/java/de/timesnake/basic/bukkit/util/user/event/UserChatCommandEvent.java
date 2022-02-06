package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class UserChatCommandEvent extends CancelableUserEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    private final String message;

    public UserChatCommandEvent(User user, boolean isCanceled, String message) {
        super(user, isCanceled);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
