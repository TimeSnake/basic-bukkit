package de.timesnake.basic.bukkit.core.user.inventory;

import de.timesnake.basic.bukkit.util.user.ExItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ExInventory implements de.timesnake.basic.bukkit.util.user.ExInventory {

    private final Inventory inventory;
    private final String title;

    public ExInventory(int size, String title, ExItemStack... itemStacks) {
        size = (size + 8) / 9 * 9;
        this.inventory = Bukkit.createInventory(null, size, title);
        this.title = title;
        for (ExItemStack itemStack : itemStacks) {
            this.setItemStack(itemStack);
        }
    }

    public ExInventory(int size, String title, InventoryHolder holder, ExItemStack... itemStacks) {
        size = (size + 8) / 9 * 9;
        this.inventory = Bukkit.createInventory(holder, size > 0 ? size : 9, title);
        this.title = title;
        for (ExItemStack itemStack : itemStacks) {
            this.setItemStack(itemStack);
        }
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public Integer getSize() {
        return this.inventory.getSize();
    }

    @Override
    public void setItemStack(int index, Material material, int amount, String name, List<String> lore) {
        this.inventory.setItem(index, new ExItemStack(material, amount, name, lore));
    }

    @Override
    public void setItemStack(int index, org.bukkit.inventory.ItemStack item) {
        this.inventory.setItem(index, item);
    }

    @Override
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
    @Override
    public Integer removeItemStack(ExItemStack item) {
        for (int slot = 0; slot < this.getInventory().getSize(); slot++) {
            ItemStack i = this.getInventory().getItem(slot);
            if (i == null) {
                continue;
            }

            ExItemStack exItem = new ExItemStack(i, slot);

            if (exItem.equals(item)) {
                this.inventory.setItem(slot, null);
                return slot;
            }
        }
        return null;
    }

    @Override
    public void removeItemStack(int index) {
        this.inventory.remove(this.inventory.getItem(index));
    }

    @Override
    public Integer getFirstEmptySlot() {
        return this.getFirstEmptySlot(0);
    }

    @Override
    public Integer getFirstEmptySlot(int begin) {
        for (int i = begin; i < this.inventory.getSize(); i++) {
            if (this.inventory.getItem(i) == null) {
                return i;
            }
        }
        return null;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }


}
