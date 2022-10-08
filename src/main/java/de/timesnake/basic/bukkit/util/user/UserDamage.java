/*
 * basic-bukkit.main
 * Copyright (C) 2022 timesnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; If not, see <http://www.gnu.org/licenses/>.
 */

package de.timesnake.basic.bukkit.util.user;

import org.bukkit.Location;
import org.bukkit.event.entity.EntityDamageEvent;

public class UserDamage {

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

    public enum DamageType {
        PLAYER_BOW,
        PLAYER,
        INSTANT
    }
}
