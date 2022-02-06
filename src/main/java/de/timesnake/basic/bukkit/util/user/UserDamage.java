package de.timesnake.basic.bukkit.util.user;

import org.bukkit.Location;
import org.bukkit.event.entity.EntityDamageEvent;

public class UserDamage {

    public enum DamageType {
        PLAYER_BOW, PLAYER, INSTANT
    }

    private final User damager;
    private final Location damagerLocation;
    private final Location userLocation;
    private final EntityDamageEvent.DamageCause cause;
    private final DamageType damageType;

    public UserDamage(User user, User damager, EntityDamageEvent.DamageCause cause, DamageType damageType) {
        this.damager = damager;
        this.damagerLocation = damager.getLocation();
        this.userLocation = user.getLocation();
        this.cause = cause;
        this.damageType = damageType;
    }

    public User getDamager() {
        return damager;
    }

    public Location getDamagerLocation() {
        return damagerLocation;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public EntityDamageEvent.DamageCause getCause() {
        return cause;
    }

    public Double getDistance() {
        return this.damagerLocation.distance(this.userLocation);
    }

    public DamageType getDamageType() {
        return damageType;
    }
}
