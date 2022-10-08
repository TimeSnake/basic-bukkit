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

import de.timesnake.basic.bukkit.util.user.ExItemStack;
import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class UserInventoryClickEvent extends UserInventoryEvent {

    private final InventoryView view;
    private final Inventory inventory;
    private final Integer slot;
    private final ClickType clickType;
    private final InventoryAction action;

    public UserInventoryClickEvent(User user, boolean isCanceled, InventoryView view, Inventory inventory,
                                   ExItemStack clickedItem, Integer slot, ClickType clickType, InventoryAction action) {
        super(user, isCanceled, clickedItem);
        this.view = view;
        this.inventory = inventory;
        this.slot = slot;
        this.clickType = clickType;
        this.action = action;
    }

    public InventoryView getView() {
        return view;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Integer getSlot() {
        return slot;
    }

    public ClickType getClickType() {
        return clickType;
    }

    public InventoryAction getAction() {
        return action;
    }
}
