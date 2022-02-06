package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.ExItemStack;
import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.event.HandlerList;

public class UserInventoryEvent extends CancelableUserEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    private final ExItemStack clickedItem;

    public UserInventoryEvent(User user, boolean isCanceled, ExItemStack clickedItem) {
        super(user, isCanceled);
        this.clickedItem = clickedItem;
    }


    public ExItemStack getClickedItem() {
        return clickedItem;
    }

}
