/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserPermissionManager implements Listener {

  private static final Field PERMISSION_FIELD;

  static {
    try {
      PERMISSION_FIELD =
          Class.forName("org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().replace(".",
              ",").split(",")[3] + ".entity.CraftHumanEntity").getDeclaredField("perm");
      PERMISSION_FIELD.setAccessible(true);
    } catch (NoSuchFieldException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  private final ConcurrentHashMap<UUID, UserPermissible> userPermissibleByUuid = new ConcurrentHashMap<>();

  public boolean createUserPermissible(Player player) {
    UserPermissible permissible = new UserPermissible(player);

    try {
      PERMISSION_FIELD.set(player, permissible);
      this.userPermissibleByUuid.put(player.getUniqueId(), permissible);
      return true;
    } catch (IllegalAccessException ex) {
      return false;
    }

  }

  public UserPermissible getUserPermissible(UUID uuid) {
    return this.userPermissibleByUuid.get(uuid);
  }

}
