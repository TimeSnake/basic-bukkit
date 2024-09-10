/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.inventory;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.inventory.*;
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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryEventManager implements Listener,
    de.timesnake.basic.bukkit.util.user.inventory.InventoryEventManager {

  private final Map<InventoryHolder, UserInventoryClickListener> clickListenerByHolder = new ConcurrentHashMap<>();
  private final Map<Integer, Set<UserInventoryClickListener>> clickListenerByItemId = new ConcurrentHashMap<>();
  private final Map<Integer, Set<InteractListenerInfo>> interactListenerByItemId = new ConcurrentHashMap<>();
  private boolean excludeServiceUsers = true;

  public InventoryEventManager() {
    Server.registerListener(this, BasicBukkit.getPlugin());
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent e) {
    if (e.getClickedInventory() != null && e.getWhoClicked() instanceof Player) {

      if (e.getClickedInventory().getHolder() instanceof ExcludedInventoryHolder) {
        return;
      }

      if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
        return;
      }

      User user = Server.getUser((Player) e.getWhoClicked());

      if (!this.isUserExcluded(user) && (user.isInventoryLocked() || user.isInventoryItemMoveLocked())) {
        e.setCancelled(true);
      }

      ExItemStack item = ExItemStack.getItem(e.getCurrentItem(), false);
      if (item != null && !item.isImmutable()) {
        item.setSlot(e.getSlot());
      }

      if (item != null) {
        if (!item.isMoveable()) {
          e.setCancelled(true);
        }

        Collection<UserInventoryClickListener> listeners = clickListenerByItemId.get(item.getId());
        if (listeners != null) {
          UserInventoryClickEvent event = new UserInventoryClickEvent(user, e.isCancelled(), e.getView(),
              e.getClickedInventory(), item, e.getSlot(), e.getClick(), e.getAction());

          for (UserInventoryClickListener listener : listeners) {
            listener.onUserInventoryClick(event);
          }

          e.setCancelled(event.isCancelled());
        }
      }

      InventoryHolder holder = e.getClickedInventory().getHolder();
      if (holder != null) {
        UserInventoryClickListener listener = this.clickListenerByHolder.get(holder);
        if (listener != null) {
          item = ExItemStack.getItem(e.getCurrentItem(), true);
          UserInventoryClickEvent event = new UserInventoryClickEvent(user, e.isCancelled(), e.getView(),
              e.getClickedInventory(), item, e.getSlot(), e.getClick(), e.getAction());

          listener.onUserInventoryClick(event);

          if (event.isCancelled()) {
            e.setCancelled(true);
          }
        }
      }
    }
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent e) {
    if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_AIR
        || e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {

      User user = Server.getUser(e.getPlayer());

      if (!this.isUserExcluded(user) && user.isInventoryLocked()) {
        e.setCancelled(true);
      }

      if (e.getItem() == null) {
        return;
      }

      ExItemStack item = ExItemStack.getItem(e.getItem(), false);

      if (item == null) {
        return;
      }

      UserInventoryInteractEvent event = new UserInventoryInteractEvent(user, e.isCancelled(), item,
          e.getClickedBlock(), e.getAction());

      Collection<InteractListenerInfo> listenerInfos = interactListenerByItemId.get(item.getId());

      if (listenerInfos != null && !listenerInfos.isEmpty()) {
        for (InteractListenerInfo listenerInfo : listenerInfos) {
          if (listenerInfo.isPreventDoubleClick()) {
            UUID uuid = user.getUniqueId();
            if (listenerInfo.getClickedUsers().contains(uuid)) {
              e.setCancelled(true);
              return;
            }

            listenerInfo.getClickedUsers().add(uuid);
            Server.runTaskLaterAsynchrony(() -> listenerInfo.getClickedUsers().remove(uuid), 10, BasicBukkit.getPlugin());
          }

          listenerInfo.getListener().onUserInventoryInteract(event);
        }
        if (event.isCancelled()) {
          e.setCancelled(true);
        }
      }
    }
  }

  @EventHandler
  public void onSwapHandItem(PlayerSwapHandItemsEvent e) {
    de.timesnake.basic.bukkit.util.user.User user = Server.getUser(e.getPlayer());
    if (!this.isUserExcluded(user) && (user.isInventoryLocked() || user.isInventoryItemMoveLocked())) {
      e.setCancelled(true);
    } else {
      ExItemStack item = ExItemStack.getItem(e.getMainHandItem(), false);

      if (item != null && !item.isMoveable()) {
        e.setCancelled(true);
      } else {
        item = ExItemStack.getItem(e.getOffHandItem(), false);
        if (item != null) {
          e.setCancelled(true);
        }
      }
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
    return user.isAirMode() || (user.isService() && this.excludeServiceUsers);
  }

  public void addClickListener(UserInventoryClickListener listener, InventoryHolder holder) {
    this.clickListenerByHolder.put(holder, listener);
  }

  @Override
  public void addClickListener(UserInventoryClickListener listener, ExItemStack... itemStacks) {
    for (ExItemStack item : itemStacks) {
      this.addClickListener(item.getId(), listener);
    }
  }

  @Override
  public void addClickListener(UserInventoryClickListener listener, Iterable<ExItemStack> itemStacks) {
    for (ExItemStack item : itemStacks) {
      this.addClickListener(item.getId(), listener);
    }
  }

  @Override
  public void addInteractListener(UserInventoryInteractListener listener, ExItemStack... itemStacks) {
    for (ExItemStack item : itemStacks) {
      this.addInteractListener(item.getId(), listener);
    }
  }

  @Override
  public void addInteractListener(UserInventoryInteractListener listener, boolean preventDoubleClick, ExItemStack... itemStacks) {
    for (ExItemStack item : itemStacks) {
      this.addInteractListener(item.getId(), listener, preventDoubleClick);
    }
  }

  @Override
  public void addInteractListener(UserInventoryInteractListener listener, Iterable<ExItemStack> itemStacks) {
    for (ExItemStack item : itemStacks) {
      this.addInteractListener(item.getId(), listener);
    }
  }

  private void addClickListener(Integer id, UserInventoryClickListener listener) {
    this.clickListenerByItemId.computeIfAbsent(id, k -> ConcurrentHashMap.newKeySet()).add(listener);
  }

  private void addInteractListener(Integer id, UserInventoryInteractListener listener) {
    this.addInteractListener(id, listener, false);
  }

  private void addInteractListener(Integer id, UserInventoryInteractListener listener, boolean preventDoubleClick) {
    this.interactListenerByItemId.computeIfAbsent(id, k -> ConcurrentHashMap.newKeySet())
        .add(new InteractListenerInfo(listener, preventDoubleClick));
  }

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

  @Override
  public void removeInteractListener(UserInventoryInteractListener listener) {
    for (Integer id : this.interactListenerByItemId.keySet()) {
      this.interactListenerByItemId.get(id).remove(new InteractListenerInfo(listener, false));
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

  private static class InteractListenerInfo {

    private final UserInventoryInteractListener listener;
    private final Set<UUID> clickedUsers = ConcurrentHashMap.newKeySet();
    private final boolean preventDoubleClick;

    public InteractListenerInfo(UserInventoryInteractListener listener, boolean preventDoubleClick) {
      this.listener = listener;
      this.preventDoubleClick = preventDoubleClick;
    }

    public UserInventoryInteractListener getListener() {
      return listener;
    }

    public Set<UUID> getClickedUsers() {
      return clickedUsers;
    }

    public boolean isPreventDoubleClick() {
      return preventDoubleClick;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      InteractListenerInfo that = (InteractListenerInfo) o;
      return Objects.equals(listener, that.listener);
    }

    @Override
    public int hashCode() {
      return Objects.hash(listener);
    }
  }
}
