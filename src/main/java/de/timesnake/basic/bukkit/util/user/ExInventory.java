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

    Integer getFirstEmptySlot();

    Integer getFirstEmptySlot(int begin);

    Inventory getInventory();
}
