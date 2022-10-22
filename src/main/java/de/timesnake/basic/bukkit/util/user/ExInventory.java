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

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.List;

public interface ExInventory {

    String getTitle();

    Integer getSize();

    void setItemStack(int index, Material material, int amount, String name, List<String> lore);

    void setItemStack(int index, org.bukkit.inventory.ItemStack item);

    void setItemStack(ExItemStack item);

    /**
     * Removes an {@link ExItemStack} from inventory by id
     *
     * @param item The {@link ExItemStack} to remove
     * @return if removed the slot, else null
     */
    Integer removeItemStack(ExItemStack item);

    void removeItemStack(int index);

    void update();

    Integer getFirstEmptySlot();

    Integer getFirstEmptySlot(int begin);

    Inventory getInventory();
}
