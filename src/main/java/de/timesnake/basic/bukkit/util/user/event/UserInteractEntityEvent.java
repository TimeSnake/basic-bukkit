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

package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;

public class UserInteractEntityEvent extends CancelableUserEvent {

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    private final Entity entity;
    private final EquipmentSlot hand;

    public UserInteractEntityEvent(User user, boolean isCanceled, Entity entity, EquipmentSlot hand) {
        super(user, isCanceled);
        this.entity = entity;
        this.hand = hand;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Entity getEntity() {
        return entity;
    }

    public EquipmentSlot getHand() {
        return hand;
    }
}
