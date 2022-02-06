package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;


public class UserDamageEvent extends CancelableUserEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    private final EntityDamageEvent.DamageCause damageCause;
    private double damage;
    private boolean cancelDamage;

    public UserDamageEvent(User user, boolean isCanceled, double damage, EntityDamageEvent.DamageCause damageCause) {
        super(user, isCanceled);
        this.damage = damage;
        this.damageCause = damageCause;
    }

    public EntityDamageEvent.DamageCause getDamageCause() {
        return damageCause;
    }

    public void setCancelDamage(boolean cancelDamage) {
        this.cancelDamage = cancelDamage;
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
}
