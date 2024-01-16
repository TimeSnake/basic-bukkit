/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.group.PermGroup;
import de.timesnake.database.util.Database;
import de.timesnake.database.util.group.DbPermGroup;
import de.timesnake.database.util.user.DbUser;
import de.timesnake.library.basic.util.Loggers;
import de.timesnake.library.basic.util.Status;
import de.timesnake.library.permissions.ExPermission;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.ServerOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserPermissible extends PermissibleBase {

  private final Player player;
  private final DbUser user;

  private final Set<ExPermission> permissions = ConcurrentHashMap.newKeySet();
  private final Set<String> enabledPermissions = ConcurrentHashMap.newKeySet();

  private PermGroup permGroup;

  public UserPermissible(@Nullable ServerOperator opable) {
    super(opable);

    this.player = ((Player) opable);

    this.user = Database.getUsers().getUser(this.player.getUniqueId());

    if (user == null) {
      return;
    }

    DbPermGroup permGroup = user.getPermGroup();

    if (permGroup != null) {
      String groupName = permGroup.getName();
      this.permGroup = Server.getPermGroup(groupName);
    } else {
      Loggers.USERS.warning("Error while loading group for " + user.getName());
      this.player.kick(Component.text("§cA fatal error occurred\nPlease contact an admin (permission group exception)"
      ));
    }

    this.updatePermissionsSync(true);
  }

  @Override
  public boolean hasPermission(@NotNull String inName) {
    if (this.enabledPermissions.contains("*")) {
      return true;
    }

    if (this.enabledPermissions.contains(inName)) {
      return true;
    }

    String[] needPerm = inName.split("\\.");
    StringBuilder permSum = new StringBuilder();

    for (String permPart : needPerm) {
      permSum.append(permPart).append(".");
      if (this.enabledPermissions.contains(permSum + "*")) {
        return true;
      }
    }

    System.out.println("false " + inName);
    return false;
  }

  /**
   * Updates user permissions async
   *
   * @param fromDatabase Set true to update from database and group
   */
  public void updatePermissions(boolean fromDatabase) {
    Server.runTaskAsynchrony(() -> this.updatePermissionsSync(fromDatabase), BasicBukkit.getPlugin());
  }

  private void updatePermissionsSync(boolean fromDatabase) {
    Status.User status = this.user.getStatus();
    boolean isService = this.user.isService();

    this.enabledPermissions.clear();

    if (fromDatabase) {
      this.permissions.clear();

      this.user.getPermissions().parallelStream()
          .map(p -> new ExPermission(p.getPermission(), p.getMode()))
          .forEach(p -> {
            this.permissions.add(p);
            this.addPermission(p, status, isService);
          });
    } else {
      this.permissions.parallelStream().forEach(p -> this.addPermission(p, status, isService));
    }

    if (this.permGroup != null) {
      this.permGroup.getPermissions().parallelStream()
          .forEach(p -> {
            this.permissions.add(p);
            this.addPermission(p, status, isService);
          });
    }

    if (fromDatabase) {
      Loggers.PERMISSIONS.info("Updated permissions of user '" + this.player.getName() + "' from database ");
    } else {
      Loggers.PERMISSIONS.info("Updated permissions of user '" + this.player.getName() + "'");
    }
  }

  /**
   * Adds the permission to user Adds with status check from server and user
   *
   * @param perm The {@link ExPermission} to add
   */
  public void addPermission(ExPermission perm, Status.User status, boolean isService) {
    Status.Permission mode = perm.getMode();
    Status.Server statusServer = Server.getStatus();

    if (mode.equals(Status.Permission.IN_GAME)) {
      this.enabledPermissions.add(perm.getPermission());
    } else if (statusServer.equals(Status.Server.SERVICE)) {
      this.enabledPermissions.add(perm.getPermission());
    } else if (isService) {
      this.enabledPermissions.add(perm.getPermission());
    } else if (mode.equals(Status.Permission.ONLINE) && (statusServer.equals(Status.Server.ONLINE) && status.equals(Status.User.ONLINE))) {
      this.enabledPermissions.add(perm.getPermission());
    }
  }
}
