/*
 * Copyright (C) 2022 timesnake
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
