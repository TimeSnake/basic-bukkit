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

package de.timesnake.basic.bukkit.util.user;

import de.timesnake.basic.bukkit.core.user.inventory.InventoryDelegation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExInventory extends InventoryDelegation {

    public ExInventory(@NotNull Inventory inventory) {
        super(inventory);
    }

    public ExInventory(int size, Component title, ExItemStack... itemStacks) {
        size = (int) (Math.ceil(size / 9.0) * 9);
        this.inventory = Bukkit.createInventory(null, size, title);
        for (ExItemStack itemStack : itemStacks) {
            this.setItemStack(itemStack);
        }
    }

    public ExInventory(int size, Component title, InventoryHolder holder, ExItemStack... itemStacks) {
        size = (int) (Math.ceil(size / 9.0) * 9);
        this.inventory = Bukkit.createInventory(holder, size > 0 ? size : 9, title);
        for (ExItemStack itemStack : itemStacks) {
            this.setItemStack(itemStack);
        }
    }

    @Deprecated
    public ExInventory(int size, String title, ExItemStack... itemStacks) {
        this(size, LegacyComponentSerializer.legacyAmpersand().deserialize(title), itemStacks);
    }

    @Deprecated
    public ExInventory(int size, String title, InventoryHolder holder, ExItemStack... itemStacks) {
        this(size, LegacyComponentSerializer.legacyAmpersand().deserialize(title), holder, itemStacks);
    }

    public void setItemStack(int index, Material material, int amount, String name, List<String> lore) {
        this.inventory.setItem(index, new ExItemStack(material, amount, name, lore));
    }

    public void setItemStack(int index, org.bukkit.inventory.ItemStack item) {
        this.inventory.setItem(index, item);
    }

    public void setItemStack(ExItemStack item) {
        if (item.getSlot() != null) {
            this.inventory.setItem(item.getSlot(), item);
        } else {
            this.inventory.addItem(item);
        }
    }

    /**
     * Removes an {@link ExItemStack} from inventory by id
     *
     * @param item The {@link ExItemStack} to remove
     * @return if removed the slot, else null
     */
    public Integer removeItemStack(ExItemStack item) {
        for (int slot = 0; slot < this.getInventory().getSize(); slot++) {
            ItemStack i = this.getInventory().getItem(slot);
            if (i == null) {
                continue;
            }

            ExItemStack exItem = ExItemStack.getItem(i, true);

            if (exItem.equals(item)) {
                this.inventory.setItem(slot, null);
                return slot;
            }
        }
        return null;
    }

    public void removeItemStack(int index) {
        this.inventory.clear(index);
    }

    public void update() {
        this.inventory.getViewers().forEach((h -> ((Player) h).updateInventory()));
    }

    public Integer getFirstEmptySlot() {
        return this.getFirstEmptySlot(0);
    }

    public Integer getFirstEmptySlot(int begin) {
        for (int i = begin; i < this.inventory.getSize(); i++) {
            if (this.inventory.getItem(i) == null) {
                return i;
            }
        }
        return null;
    }
}
