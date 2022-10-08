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

import de.timesnake.basic.bukkit.util.user.event.UserInventoryClickListener;
import de.timesnake.basic.bukkit.util.user.event.UserInventoryInteractListener;
import org.bukkit.inventory.InventoryHolder;

public interface InventoryEventManager {

    /**
     * Adds a new {@link UserInventoryClickListener} for InventoryClickEvent
     *
     * @param listener The {@link UserInventoryClickListener} to add
     * @param holder   The {@link InventoryHolder} to add
     */
    void addClickListener(UserInventoryClickListener listener, InventoryHolder holder);

    /**
     * Adds a new {@link UserInventoryInteractListener} for InventoryClickEvent
     *
     * @param listener   The {@link UserInventoryInteractListener} to add
     * @param itemStacks The {@link ExItemStack}s to add
     */
    void addClickListener(UserInventoryClickListener listener, ExItemStack... itemStacks);

    /**
     * Adds a new {@link UserInventoryInteractListener} for InventoryClickEvent
     *
     * @param listener   The {@link UserInventoryInteractListener} to add
     * @param itemStacks The {@link ExItemStack}s to add
     */
    void addClickListener(UserInventoryClickListener listener, Iterable<ExItemStack> itemStacks);

    /**
     * Adds a new {@link UserInventoryInteractListener} for InventoryInteractEvent
     *
     * @param listener   The {@link UserInventoryInteractListener} to add
     * @param itemStacks The {@link ExItemStack}s to add
     */
    void addInteractListener(UserInventoryInteractListener listener, ExItemStack... itemStacks);

    /**
     * Adds a new {@link UserInventoryInteractListener} for InventoryInteractEvent
     *
     * @param listener   The {@link UserInventoryInteractListener} to add
     * @param itemStacks The {@link ExItemStack}s to add
     */
    void addInteractListener(UserInventoryInteractListener listener, Iterable<ExItemStack> itemStacks);

    /**
     * Adds a new {@link UserInventoryClickListener} for InventoryClickEvent
     *
     * @param id       The item-id where add the listener
     * @param listener The {@link UserInventoryClickListener} to add
     */
    void addClickListener(Integer id, UserInventoryClickListener listener);

    /**
     * Adds a new {@link UserInventoryInteractListener} for InventoryInteractEvent
     *
     * @param id       The item-id where add the listener
     * @param listener The {@link UserInventoryInteractListener} to add
     */
    void addInteractListener(Integer id, UserInventoryInteractListener listener);

    /**
     * Removes the click-listener
     *
     * @param listener The {@link UserInventoryClickListener} to remove
     */
    void removeClickListener(UserInventoryClickListener listener);

    /**
     * Removes the interact-listener
     *
     * @param listener The {@link UserInventoryInteractListener} to remove
     */
    void removeInteractListener(UserInventoryInteractListener listener);

    void excludeServiceUsersFromLocks(boolean exclude);

    boolean excludingServiceUsers();
}
