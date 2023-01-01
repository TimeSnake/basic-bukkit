/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;


public class UserDamageEvent extends CancelableUserEvent {

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    private final EntityDamageEvent.DamageCause damageCause;
    private double damage;
    private boolean cancelDamage;

    public UserDamageEvent(User user, boolean isCanceled, double damage, EntityDamageEvent.DamageCause damageCause) {
        super(user, isCanceled);
        this.damage = damage;
        this.damageCause = damageCause;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public EntityDamageEvent.DamageCause getDamageCause() {
        return damageCause;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public boolean isCancelDamage() {
        return cancelDamage;
    }

    public void setCancelDamage(boolean cancelDamage) {
        this.cancelDamage = cancelDamage;
    }
}
