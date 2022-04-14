package de.timesnake.basic.bukkit.core.user.inventory;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.ExItemStack;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryEventManager implements Listener, de.timesnake.basic.bukkit.util.user.InventoryEventManager {

    public InventoryEventManager() {
        Server.registerListener(this, BasicBukkit.getPlugin());
    }

    private final Map<InventoryHolder, UserInventoryClickListener> clickListenerByHolder = new ConcurrentHashMap<>();
    private final Map<Integer, Collection<UserInventoryClickListener>> clickListenerByItemId = new ConcurrentHashMap<>();

    private final Map<Integer, Collection<UserInventoryInteractListener>> interactListenerByItemId = new ConcurrentHashMap<>();

    private boolean excludeServiceUsers = true;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != null && e.getWhoClicked() instanceof Player) {

            if (e.getClickedInventory().getHolder() instanceof ExcludedInventoryHolder) {
                return;
            }

            User user = Server.getUser((Player) e.getWhoClicked());

            if (!this.isUserExcluded(user) && (user.isInventoryLocked() || user.isInventoryItemMoveLocked())) {
                e.setCancelled(true);
            }

            ExItemStack item;

            if (e.getCurrentItem() != null && !e.getCurrentItem().getType().equals(Material.AIR)) {
                item = ExItemStack.getItem(e.getCurrentItem(), true).setSlot(e.getSlot());

            } else {
                item = new ExItemStack(Material.AIR);
            }

            if (!item.isMoveable()) {
                e.setCancelled(true);
            }

            UserInventoryClickEvent event = new UserInventoryClickEvent(user, e.isCancelled(), e.getView(),
                    e.getClickedInventory(), item, e.getSlot(), e.getClick(), e.getAction());

            InventoryHolder holder = e.getClickedInventory().getHolder();
            if (holder != null) {
                UserInventoryClickListener listener = this.clickListenerByHolder.get(holder);
                if (listener != null) {
                    listener.onUserInventoryClick(event);
                }
            }

            if (item.getId() != null) {
                Collection<UserInventoryClickListener> listeners = clickListenerByItemId.get(item.getId());
                this.handleInventoryClickListener(listeners, event);
            }

            if (event.isCancelled()) {
                e.setCancelled(true);
            }
        }
    }

    private void handleInventoryClickListener(Collection<UserInventoryClickListener> listeners, UserInventoryClickEvent event) {
        if (listeners != null) {
            for (UserInventoryClickListener listener : listeners) {
                listener.onUserInventoryClick(event);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            User user = Server.getUser(e.getPlayer());

            if (!this.isUserExcluded(user) && user.isInventoryLocked()) {
                e.setCancelled(true);
            }

            if (e.getItem() != null) {
                ExItemStack item = ExItemStack.getItem(e.getItem(), false);

                if (item != null) {
                    UserInventoryInteractEvent event = new UserInventoryInteractEvent(user, e.isCancelled(), item, e.getClickedBlock(), e.getAction());

                    Collection<UserInventoryInteractListener> listeners = interactListenerByItemId.get(item.getId());

                    if (listeners != null && !listeners.isEmpty()) {

                        for (UserInventoryInteractListener listener : listeners) {
                            listener.onUserInventoryInteract(event);
                        }
                        if (event.isCancelled()) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSwapHandItem(PlayerSwapHandItemsEvent e) {
        de.timesnake.basic.bukkit.util.user.User user = Server.getUser(e.getPlayer());
        if (!this.isUserExcluded(user) && user.isInventoryLocked() || user.isInventoryItemMoveLocked()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        de.timesnake.basic.bukkit.util.user.User user = Server.getUser(e.getPlayer());
        if (!this.isUserExcluded(user) && user.isInventoryLocked() || user.isInventoryItemMoveLocked()) {
            e.setCancelled(true);
            return;
        }

        if (e.getItemDrop().getItemStack().getType().equals(Material.AIR)) {
            return;
        }

        ExItemStack item = ExItemStack.getItem(e.getItemDrop().getItemStack(), false);

        if (item == null) {
            return;
        }

        if (!item.isDropable()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(PlayerItemDamageEvent e) {
        de.timesnake.basic.bukkit.util.user.User user = Server.getUser(e.getPlayer());
        if (!this.isUserExcluded(user) && user.isInventoryLocked() || user.isInventoryItemMoveLocked()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent e) {
        if (e.getItem().getType().equals(Material.AIR)) {
            return;
        }

        ExItemStack item = ExItemStack.getItem(e.getItem(), false);

        if (item == null) {
            return;
        }

        if (!item.isMoveable()) {
            e.setCancelled(true);
        }
    }

    private boolean isUserExcluded(User user) {
        return user.isService() && this.excludeServiceUsers;
    }


    /**
     * Adds a new {@link UserInventoryClickListener} for InventoryClickEvent
     *
     * @param listener The {@link UserInventoryClickListener} to add
     * @param holder   The {@link InventoryHolder} to add
     */
    public void addClickListener(UserInventoryClickListener listener, InventoryHolder holder) {
        this.clickListenerByHolder.put(holder, listener);
    }

    /**
     * Adds a new {@link UserInventoryInteractListener} for InventoryClickEvent
     *
     * @param listener   The {@link UserInventoryInteractListener} to add
     * @param itemStacks The {@link ExItemStack}s to add
     */
    @Override
    public void addClickListener(UserInventoryClickListener listener, ExItemStack... itemStacks) {
        for (ExItemStack item : itemStacks) {
            addClickListener(item.getId(), listener);
        }
    }

    /**
     * Adds a new {@link UserInventoryInteractListener} for InventoryClickEvent
     *
     * @param listener   The {@link UserInventoryInteractListener} to add
     * @param itemStacks The {@link ExItemStack}s to add
     */
    @Override
    public void addClickListener(UserInventoryClickListener listener, List<ExItemStack> itemStacks) {
        for (ExItemStack item : itemStacks) {
            addClickListener(item.getId(), listener);
        }
    }

    /**
     * Adds a new {@link UserInventoryInteractListener} for InventoryInteractEvent
     *
     * @param listener   The {@link UserInventoryInteractListener} to add
     * @param itemStacks The {@link ExItemStack}s to add
     */
    @Override
    public void addInteractListener(UserInventoryInteractListener listener, ExItemStack... itemStacks) {
        for (ExItemStack item : itemStacks) {
            addInteractListener(item.getId(), listener);
        }
    }

    /**
     * Adds a new {@link UserInventoryClickListener} for InventoryClickEvent
     *
     * @param id       The item-id where add the listener
     * @param listener The {@link UserInventoryClickListener} to add
     */
    @Override
    public void addClickListener(Integer id, UserInventoryClickListener listener) {
        Collection<UserInventoryClickListener> list = this.clickListenerByItemId.get(id);
        if (list == null) {
            list = new HashSet<>();
        }
        list.add(listener);
        this.clickListenerByItemId.put(id, list);
    }

    /**
     * Adds a new {@link UserInventoryInteractListener} for InventoryInteractEvent
     *
     * @param id       The item-id where add the listener
     * @param listener The {@link UserInventoryInteractListener} to add
     */
    @Override
    public void addInteractListener(Integer id, UserInventoryInteractListener listener) {
        Collection<UserInventoryInteractListener> list = this.interactListenerByItemId.get(id);
        if (list == null) {
            list = new HashSet<>();
        }
        list.add(listener);
        this.interactListenerByItemId.put(id, list);
    }

    /**
     * Removes the click-listener
     *
     * @param listener The {@link UserInventoryClickListener} to remove
     */
    @Override
    public void removeClickListener(UserInventoryClickListener listener) {
        for (Integer id : this.clickListenerByItemId.keySet()) {
            this.clickListenerByItemId.get(id).remove(listener);
        }
        for (Map.Entry<InventoryHolder, UserInventoryClickListener> entry : this.clickListenerByHolder.entrySet()) {
            if (entry.getValue().equals(listener)) {
                this.clickListenerByHolder.remove(entry.getKey());
            }
        }
    }

    /**
     * Removes the interact-listener
     *
     * @param listener The {@link UserInventoryInteractListener} to remove
     */
    @Override
    public void removeInteractListener(UserInventoryInteractListener listener) {
        for (Integer id : this.interactListenerByItemId.keySet()) {
            this.interactListenerByItemId.get(id).remove(listener);
        }
    }

    @Override
    public void excludeServiceUsersFromLocks(boolean exclude) {
        this.excludeServiceUsers = exclude;
    }

    @Override
    public boolean excludingServiceUsers() {
        return excludeServiceUsers;
    }
}
