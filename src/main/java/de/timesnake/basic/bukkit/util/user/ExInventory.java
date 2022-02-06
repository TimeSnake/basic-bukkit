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

    void removeItemStack(ExItemStack item);

    void removeItemStack(int index);

    Inventory getInventory();
}
