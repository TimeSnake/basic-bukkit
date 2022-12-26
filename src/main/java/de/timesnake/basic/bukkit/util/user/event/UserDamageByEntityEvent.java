/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.annotation.Nonnull;

/**
 * Called by {@link org.bukkit.event.entity.EntityDamageByEntityEvent} with user check
 */
public class UserDamageByEntityEvent extends UserDamageEvent {

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    private final Entity damager;

    public UserDamageByEntityEvent(User user, Entity damager, boolean isCanceled, double damage,
                                   EntityDamageEvent.DamageCause cause) {
        super(user, isCanceled, damage, cause);
        this.damager = damager;

    }

    @Nonnull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Entity getDamager() {
        return this.damager;
    }

}
