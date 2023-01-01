/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import javax.annotation.Nonnull;
import org.bukkit.entity.Item;
import org.bukkit.event.HandlerList;

public class UserAttemptPickupItemEvent extends CancelableUserEvent {

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final Item item;
    private final int remaining;
    private boolean flyAtPlayer = true;

    public UserAttemptPickupItemEvent(User user, boolean isCancelled, Item item, int remaining) {
        super(user, isCancelled);
        this.item = item;
        this.remaining = remaining;
    }

    @Nonnull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Item getItem() {
        return item;
    }

    public int getRemaining() {
        return remaining;
    }

    public boolean isFlyAtPlayer() {
        return flyAtPlayer;
    }

    public void setFlyAtPlayer(boolean flyAtPlayer) {
        this.flyAtPlayer = flyAtPlayer;
    }
}
