package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.annotation.Nonnull;


/**
 * Called by {@link org.bukkit.event.entity.EntityDamageByEntityEvent} with user check
 */
public class UserDamageByUserEvent extends UserDamageEvent {

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    private final User userDamager;

    public UserDamageByUserEvent(User user, User damager, boolean isCanceled, double damage,
                                 EntityDamageEvent.DamageCause cause) {
        super(user, isCanceled, damage, cause);
        this.userDamager = damager;

    }

    @Nonnull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public User getUserDamager() {
        return this.userDamager;
    }

}
