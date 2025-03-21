/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.ServerManager;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.UserChatCommandListener;
import de.timesnake.basic.bukkit.util.user.UserDamage;
import de.timesnake.basic.bukkit.util.user.event.*;
import de.timesnake.library.chat.Chat;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.chat.Plugin;
import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashMap;

public class UserEventManager implements Listener, de.timesnake.basic.bukkit.util.user.UserEventManager {

  private final Logger logger = LogManager.getLogger("user.event-manager");

  private final HashMap<User, UserChatCommandListener> chatListener = new HashMap<>();

  public UserEventManager() {
    Server.registerListener(this, BasicBukkit.getPlugin());
  }

  @EventHandler
  public void onPlayerLogin(PlayerLoginEvent e) {
    Player p = e.getPlayer();

    boolean success = Server.getUserPermissionManager().createUserPermissible(p);

    if (!success) {
      this.logger.warn("Unable to inject player permission checker");
      e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
      e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Component.text("A fatal error has occurred, please contact an " +
          "administrator! (permission checker exception)"));
      return;
    }

    // server full checks
    if (Server.getOnlinePlayers() <= Server.getMaxPlayers() && e.getResult().equals(PlayerLoginEvent.Result.KICK_FULL)) {
      e.allow();
    } else if (Bukkit.getOnlinePlayers().size() > Server.getMaxPlayers()
        && (e.getResult().equals(PlayerLoginEvent.Result.KICK_FULL)) || p.hasPermission("join.full")) {
      e.allow();
    }

    ServerManager.getInstance().createUser(e.getPlayer());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerJoin(PlayerJoinEvent e) {
    e.joinMessage(Component.empty());

    // finalize user creation
    User user = ((UserManager) Server.getUserManager()).registerUser(e.getPlayer().getUniqueId());

    if (user == null) {
      return;
    }

    // air mode
    if (user.isAirMode()) {
      for (de.timesnake.basic.bukkit.util.user.User u : Server.getUsers()) {
        u.hideUser(user);
      }
    }
    for (de.timesnake.basic.bukkit.util.user.User u : Server.getUsers()) {
      if (u.isAirMode()) {
        user.hideUser(u);
      }
    }

    if (user.isService()) {
      user.sendPluginTDMessage(Plugin.SERVER, "§w§lYOU ARE IN SERVICE MODE");
    }

    // async user join event
    Server.runTaskAsynchrony(() -> Bukkit.getPluginManager().callEvent(new AsyncUserJoinEvent(user)),
        BasicBukkit.getPlugin());

    // user join event
    Bukkit.getPluginManager().callEvent(new UserJoinEvent(user));

  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent e) {
    e.quitMessage(Component.empty());
    User user = Server.getUser(e.getPlayer());

    if (user != null) {
      ((UserManager) Server.getUserManager()).markUserForRemoval(user);
      user.quit();
      // async user quit event
      Server.runTaskAsynchrony(() -> Bukkit.getPluginManager().callEvent(new AsyncUserQuitEvent(user)),
          BasicBukkit.getPlugin());

      // user quit event
      Bukkit.getPluginManager().callEvent(new UserQuitEvent(user));

      this.chatListener.remove(user);
      ((UserManager) Server.getUserManager()).removeUser(user.getUniqueId());
    }
  }

  @EventHandler
  public void onEntityDamage(EntityDamageEvent e) {
    if (e.getEntity() instanceof Player) {
      User user = Server.getUser((Player) e.getEntity());
      if (user == null) {
        return;
      }

      // user damage event
      UserDamageEvent event = new UserDamageEvent(user, e.isCancelled(), e.getDamage(), e.getCause());
      Bukkit.getPluginManager().callEvent(event);
      if (event.isCancelled()) {
        e.setCancelled(true);
      }

      e.setDamage(event.getDamage());

      if (event.isCancelDamage()) {
        e.setDamage(0);
      }
    }
  }

  @EventHandler
  public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
    Entity damager = e.getDamager();

    if (e.getEntity() instanceof Player) {
      User user = Server.getUser((Player) e.getEntity());
      LivingEntity source = null;

      if (damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof LivingEntity) {
        source = (LivingEntity) ((Projectile) damager).getShooter();
      }

      User userDamager = null;

      // user damage by user event
      if (damager instanceof Player) {
        userDamager = Server.getUser((Player) damager);
      } else if (source instanceof Player) {
        userDamager = Server.getUser(((Player) source));
      }

      if (userDamager != null) {
        UserDamageByUserEvent userEvent = new UserDamageByUserEvent(user, userDamager, e.isCancelled(), e.getDamage()
            , e.getCause());
        Bukkit.getPluginManager().callEvent(userEvent);

        if (userEvent.isCancelDamage()) {
          e.setDamage(0);
        }

        e.setDamage(userEvent.getDamage());
        e.setCancelled(userEvent.isCancelled());

        if (!e.isCancelled()) {
          if (damager instanceof Player) {
            user.setLastDamager(new UserDamage(user, userDamager, e.getCause(), UserDamage.DamageType.PLAYER));
          } else {
            user.setLastDamager(new UserDamage(user, userDamager, e.getCause(), UserDamage.DamageType.PLAYER_BOW));
          }
        }
      }

      UserDamageByEntityEvent userEvent = new UserDamageByEntityEvent(user, e.getDamager(), e.isCancelled(),
          e.getDamage(), e.getCause());
      Bukkit.getPluginManager().callEvent(userEvent);

      if (userEvent.isCancelDamage()) {
        e.setDamage(0);
      }

      e.setDamage(userEvent.getDamage());
      e.setCancelled(userEvent.isCancelled());

    }

    Entity entity = e.getEntity();

    User userDamager;

    ProjectileSource projectileSource = null;

    if (damager instanceof Projectile) {
      projectileSource = ((Projectile) damager).getShooter();
    }

    // user damage by user event
    if (damager instanceof Player) {
      userDamager = Server.getUser((Player) damager);
    } else if (projectileSource instanceof Player) {
      userDamager = Server.getUser(((Player) projectileSource));
    } else {
      return;
    }

    EntityDamageByUserEvent userEvent = new EntityDamageByUserEvent(entity, userDamager, e.isCancelled(),
        e.getDamage(), e.getCause());
    Bukkit.getPluginManager().callEvent(userEvent);
    e.setCancelled(userEvent.isCancelled());

    e.setDamage(userEvent.getDamage());

    if (userEvent.isCancelDamage()) {
      e.setDamage(0);
    }
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent e) {
    User user = Server.getUser(e.getPlayer());
    if (user == null) {
      return;
    }

    // async user move event
    Server.runTaskAsynchrony(() -> Bukkit.getPluginManager().callEvent(new AsyncUserMoveEvent(user,
        e.getFrom().clone(), e.getTo().clone())), BasicBukkit.getPlugin());

    // user move event
    UserMoveEvent event = new UserMoveEvent(user, e.isCancelled(), e.getFrom(), e.getTo());
    Bukkit.getPluginManager().callEvent(event);
    if (!e.getFrom().getBlock().equals(e.getTo().getBlock()) && user.isLocationLocked() && !user.isService()) {
      e.setCancelled(true);
    } else {
      e.setCancelled(event.isCancelled());
    }
  }

  @EventHandler
  public void onPlayerTeleport(PlayerTeleportEvent e) {
    de.timesnake.basic.bukkit.util.user.User user = Server.getUser(e.getPlayer());

    if (user == null) {
      return;
    }

    Location location = e.getFrom();
    user.setLastLocation(location);

    UserTeleportEvent event = new UserTeleportEvent(user, e.isCancelled(), e.getTo(), e.getFrom(), e.getCause());
    Bukkit.getPluginManager().callEvent(event);
    e.setCancelled(event.isCancelled());
  }

  @Override
  public void addUserChatCommand(User user, UserChatCommandListener listener) {
    this.chatListener.put(user, listener);
  }

  @Override
  public void removeUserChatCommand(User user, UserChatCommandListener listener) {
    this.chatListener.remove(user, listener);
  }

  @Override
  public void removeUserChatCommand(User user) {
    this.chatListener.remove(user);
  }

  public boolean onUserChat(User user, boolean isCanceled, String message) {
    UserChatCommandEvent event = new UserChatCommandEvent(user, isCanceled, message);
    if (chatListener.containsKey(user)) {
      UserChatCommandListener listener = chatListener.get(user);
      listener.onUserChatCommand(event);

      if (event.isRemoveListener()) {
        chatListener.remove(user);
      }
    }

    return event.isCancelled();
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent e) {
    de.timesnake.basic.bukkit.util.user.User user = Server.getUser(e.getEntity());

    if (user == null) {
      return;
    }

    if (user.isAirMode()) {
      e.deathMessage(Component.empty());
      return;
    }

    // user death event
    UserDeathEvent userDeathEvent = new UserDeathEvent(user, e.getEntity().getKiller(), e.getKeepInventory(),
        e.getDrops());
    Bukkit.getPluginManager().callEvent(userDeathEvent);

    if (userDeathEvent.isAutoRespawn()) {
      Server.runTaskLaterSynchrony(() -> user.getPlayer().spigot().respawn(), 2, BasicBukkit.getPlugin());
    }

    e.setKeepInventory(userDeathEvent.isKeepInventory());

    if (!userDeathEvent.isBroadcastDeathMessage() || !Server.getChatManager().isBroadcastDeath()) {
      if (user.getLastDamager() != null) {
        this.logger.info("{} was slain by {}", user.getName(), user.getLastDamager().getDamager().getName());
      } else {
        if (user.getLastDamageCause() != null) {
          this.logger.info("{} died {}", user.getName(), user.getLastDamageCause().getCause().name());
        } else {
          this.logger.info("{} died", user.getName());
        }
      }
      e.deathMessage(Component.empty());
      return;
    }

    if (e.getEntity().getKiller() == null) {
      if (user.getLastDamager() != null && user.getLastDamager().getDamageType().equals(UserDamage.DamageType.INSTANT)) {
        e.deathMessage(Chat.getSenderPlugin(Plugin.SERVER).append(user.getChatNameComponent()).append(Component.text(
            " was slain by ", ExTextColor.PUBLIC)).append(user.getLastDamager().getDamager().getChatNameComponent()));
      } else {
        Component message = e.deathMessage().color(ExTextColor.PUBLIC);
        for (User u : Server.getUsers()) {
          message = message.replaceText(b -> b.matchLiteral(u.getName()).replacement(u.getChatNameComponent()
              .append(Component.text("", ExTextColor.PUBLIC))));
        }

        e.deathMessage(Chat.getSenderPlugin(Plugin.SERVER).append(message));
      }
    } else if (e.getEntity().getKiller() != null) {
      User killer = Server.getUser(e.getEntity().getKiller());
      UserDamage userDamage = user.getLastDamager();

      if (user.getLastDamager() != null) {
        if (userDamage.getDamageType().equals(UserDamage.DamageType.PLAYER_BOW)) {
          int distance = userDamage.getDistance().intValue();
          e.deathMessage(Chat.getSenderPlugin(Plugin.SERVER)
              .append(user.getChatNameComponent())
              .append(Component.text(" was shot by ", ExTextColor.PUBLIC))
              .append(killer.getChatNameComponent())
              .append(Component.text(" from ", ExTextColor.PUBLIC))
              .append(Component.text(distance, ExTextColor.VALUE))
              .append(Component.text(" blocks", ExTextColor.PUBLIC)));
        } else {
          e.deathMessage(Chat.getSenderPlugin(Plugin.SERVER)
              .append(user.getChatNameComponent())
              .append(Component.text(" was slain by ", ExTextColor.PUBLIC))
              .append(killer.getChatNameComponent()));
        }
      } else {
        e.deathMessage(Chat.getSenderPlugin(Plugin.SERVER)
            .append(user.getChatNameComponent())
            .append(Component.text(" was slain by ", ExTextColor.PUBLIC))
            .append(killer.getChatNameComponent()));
      }
    } else {
      e.deathMessage(Chat.getSenderPlugin(Plugin.SERVER)
          .append(user.getChatNameComponent())
          .append(Component.text(" was slain by ", ExTextColor.PUBLIC))
          .append(Component.text(e.getEntity().getLastDamageCause().getCause().name(), ExTextColor.VALUE)));
    }

    user.setLastDamager(null);

  }

  @EventHandler
  public void onPlayerRespawn(PlayerRespawnEvent e) {
    User user = Server.getUser(e.getPlayer());

    if (user == null) {
      return;
    }

    Location respawnLocation = e.getRespawnLocation();

    // user respawn event
    UserRespawnEvent userEvent = new UserRespawnEvent(user, respawnLocation);
    Bukkit.getPluginManager().callEvent(userEvent);

    e.setRespawnLocation(userEvent.getRespawnLocation());
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent e) {
    User user = Server.getUser(e.getPlayer());

    if (user == null) {
      return;
    }

    if (user.isBlockBreakPlaceLocked()) {
      e.setCancelled(true);
    }

    // user block place event
    UserBlockPlaceEvent userEvent = new UserBlockPlaceEvent(user, e.isCancelled(), e.getBlock(), e.getBlockPlaced(),
        e.getBlockAgainst(), e.getHand(), e.getBlockReplacedState(), e.getItemInHand());
    Bukkit.getPluginManager().callEvent(userEvent);

    boolean userEventCancelled = userEvent.isCancelled();
    e.setCancelled(userEventCancelled);
    e.setBuild(!userEventCancelled);
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent e) {
    User user = Server.getUser(e.getPlayer());

    if (user == null) {
      return;
    }

    if (user.isBlockBreakPlaceLocked()) {
      e.setCancelled(true);
    }

    // user block break event
    UserBlockBreakEvent userEvent = new UserBlockBreakEvent(user, e.isCancelled(), e.getBlock(), e.getExpToDrop(),
        e.isDropItems());
    Bukkit.getPluginManager().callEvent(userEvent);

    e.setDropItems(userEvent.isDropItems());
    e.setExpToDrop(userEvent.getExpToDrop());
    e.setCancelled(userEvent.isCancelled());
  }

  @EventHandler
  public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
    User user = Server.getUser(e.getPlayer());

    if (user == null) {
      return;
    }

    // user interact entity event
    UserInteractEntityEvent userEvent = new UserInteractEntityEvent(user, e.isCancelled(), e.getRightClicked(),
        e.getHand());
    Bukkit.getPluginManager().callEvent(userEvent);

    e.setCancelled(userEvent.isCancelled());
  }

  @EventHandler
  public void onItemDrop(PlayerDropItemEvent e) {
    User user = Server.getUser(e.getPlayer());

    if (user == null) {
      return;
    }

    // user drop item event
    UserDropItemEvent userEvent = new UserDropItemEvent(user, e.isCancelled(), e.getItemDrop());
    Bukkit.getPluginManager().callEvent(userEvent);

    e.setCancelled(userEvent.isCancelled());
  }

  @EventHandler
  public void onAttemptPickUpItem(PlayerAttemptPickupItemEvent e) {
    User user = Server.getUser(e.getPlayer());

    if (user == null) {
      return;
    }

    // user pickup item event
    UserAttemptPickupItemEvent userEvent = new UserAttemptPickupItemEvent(user, e.isCancelled(), e.getItem(),
        e.getRemaining());
    Bukkit.getPluginManager().callEvent(userEvent);

    e.setCancelled(userEvent.isCancelled());
    e.setFlyAtPlayer(userEvent.isFlyAtPlayer());
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent e) {
    User user = Server.getUser(e.getPlayer());

    if (user == null) {
      return;
    }

    // user interact event
    UserInteractEvent userEvent = new UserInteractEvent(user, e.getItem(), e.getAction(), e.getClickedBlock(),
        e.getBlockFace(), e.getHand(), e.getInteractionPoint());
    Bukkit.getPluginManager().callEvent(userEvent);

    e.setUseInteractedBlock(userEvent.getUseClickedBlock());
    e.setUseItemInHand(userEvent.getUseItemInHand());
  }

  @EventHandler
  public void onPlayerToggleSneak(PlayerToggleSneakEvent e) {
    User user = Server.getUser(e.getPlayer());

    if (user == null) {
      return;
    }

    // user toggle sneak event
    UserToggleSneakEvent userEvent = new UserToggleSneakEvent(user, e.isCancelled(), e.isSneaking());
    Bukkit.getPluginManager().callEvent(userEvent);

    e.setCancelled(userEvent.isCancelled());
  }

}