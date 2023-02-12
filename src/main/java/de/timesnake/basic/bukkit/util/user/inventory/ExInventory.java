/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.inventory;

import de.timesnake.basic.bukkit.core.user.inventory.InventoryDelegation;
import de.timesnake.basic.bukkit.util.Server;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

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

    public ExInventory(int size, Component title, InventoryHolder holder,
            ExItemStack... itemStacks) {
        size = (int) (Math.ceil(size / 9.0) * 9);
        this.inventory = Bukkit.createInventory(holder, size > 0 ? size : 9, title);
        for (ExItemStack itemStack : itemStacks) {
            this.setItemStack(itemStack);
        }
    }

    public ExInventory(int size, String title, ExItemStack... itemStacks) {
        this(size, Server.getTimeDownParser().parse2Component(title), itemStacks);
    }

    public ExInventory(int size, String title, InventoryHolder holder, ExItemStack... itemStacks) {
        this(size, Server.getTimeDownParser().parse2Component(title), holder, itemStacks);
    }

    public void setItemStack(int index, Material material, int amount, String name,
            List<String> lore) {
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
