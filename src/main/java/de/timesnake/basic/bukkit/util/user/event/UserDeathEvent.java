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
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class UserDeathEvent extends UserEvent {

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    private final Entity killer;
    private List<ItemStack> drops;
    private boolean broadcastDeathMessage = true;
    private boolean keepInventory;
    private boolean autoRespawn = false;

    public UserDeathEvent(User user, Entity killer, boolean keepInventory, List<ItemStack> drops) {
        super(user);
        this.killer = killer;
        this.keepInventory = keepInventory;
        this.drops = drops;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Entity getKiller() {
        return killer;
    }

    public boolean isBroadcastDeathMessage() {
        return broadcastDeathMessage;
    }

    public void setBroadcastDeathMessage(boolean broadcastDeathMessage) {
        this.broadcastDeathMessage = broadcastDeathMessage;
    }

    public boolean isKeepInventory() {
        return keepInventory;
    }

    public void setKeepInventory(boolean keepInventory) {
        this.keepInventory = keepInventory;
    }

    public boolean isAutoRespawn() {
        return autoRespawn;
    }

    public void setAutoRespawn(boolean autoRespawn) {
        this.autoRespawn = autoRespawn;
    }

    public List<ItemStack> getDrops() {
        return drops;
    }

    public void setDrops(List<ItemStack> drops) {
        this.drops = drops;
    }
}
