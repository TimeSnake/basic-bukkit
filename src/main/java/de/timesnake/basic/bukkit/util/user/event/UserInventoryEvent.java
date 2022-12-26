/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.ExItemStack;
import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.event.HandlerList;

public class UserInventoryEvent extends CancelableUserEvent {

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    private final ExItemStack clickedItem;

    public UserInventoryEvent(User user, boolean isCanceled, ExItemStack clickedItem) {
        super(user, isCanceled);
        this.clickedItem = clickedItem;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public ExItemStack getClickedItem() {
        return clickedItem;
    }

}
