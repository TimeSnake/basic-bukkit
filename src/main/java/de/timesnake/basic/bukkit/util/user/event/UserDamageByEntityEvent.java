/*
 * workspace.basic-bukkit.main
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