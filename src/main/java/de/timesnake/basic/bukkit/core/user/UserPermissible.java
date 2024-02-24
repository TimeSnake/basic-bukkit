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
import de.timesnake.library.basic.util.Status;
import de.timesnake.library.permissions.ExPermission;
import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.ServerOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserPermissible extends PermissibleBase {

  private final Logger logger = LogManager.getLogger("user.permissible");

  private final Player player;
  private final DbUser user;

  private final Set<ExPermission> permissions = ConcurrentHashMap.newKeySet();

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
      this.logger.warn("Error while loading group for {}", user.getName());
      this.player.kick(Component.text("§cA fatal error occurred\nPlease contact an admin (permission group exception)"
      ));
    }

    this.updatePermissionsSync(true);
  }

  @Override
  public boolean hasPermission(@NotNull String inName) {
    if (super.hasPermission("*")) {
      return true;
    }

    if (super.hasPermission(inName)) {
      return true;
    }

    String[] needPerm = inName.split("\\.");
    StringBuilder permSum = new StringBuilder();

    for (String permPart : needPerm) {
      permSum.append(permPart).append(".");
      if (super.hasPermission(permSum + "*")) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean hasPermission(@NotNull Permission perm) {
    return this.hasPermission(perm.getName().toLowerCase(java.util.Locale.ENGLISH));
  }

  public void updatePermissions(boolean fromDatabase) {
    Server.runTaskAsynchrony(() -> this.updatePermissionsSync(fromDatabase), BasicBukkit.getPlugin());
  }

  private void updatePermissionsSync(boolean fromDatabase) {
    Status.User status = this.user.getStatus();
    boolean isService = this.user.isService();

    this.clearAttachments();

    if (fromDatabase) {
      this.permissions.clear();
      this.clearPermissions();

      this.user.getPermissions().forEach(p -> {
        ExPermission exPerm = new ExPermission(p.getPermission(), p.getMode());
        this.permissions.add(exPerm);
        this.addPermission(exPerm, status, isService);
      });
    } else {
      this.permissions.forEach(p -> this.addPermission(p, status, isService));
    }

    if (this.permGroup != null) {
      this.permGroup.getPermissions()
          .forEach(p -> {
            this.permissions.add(p);
            this.addPermission(p, status, isService);
          });
    }

    if (fromDatabase) {
      this.logger.info("Updated permissions of user '{}' from database", this.player.getName());
    } else {
      this.logger.info("Updated permissions of user '{}'", this.player.getName());
    }
  }


  public void addPermission(ExPermission perm, Status.User status, boolean isService) {
    Status.Permission mode = perm.getMode();
    Status.Server statusServer = Server.getStatus();

    if (mode.equals(Status.Permission.IN_GAME)) {
      this.addAttachment(BasicBukkit.getPlugin()).setPermission(perm.getPermission(), true);
    } else if (statusServer.equals(Status.Server.SERVICE)) {
      this.addAttachment(BasicBukkit.getPlugin()).setPermission(perm.getPermission(), true);
    } else if (isService) {
      this.addAttachment(BasicBukkit.getPlugin()).setPermission(perm.getPermission(), true);
    } else if (mode.equals(Status.Permission.ONLINE) && (statusServer.equals(Status.Server.ONLINE) && status.equals(Status.User.ONLINE))) {
      this.addAttachment(BasicBukkit.getPlugin()).setPermission(perm.getPermission(), true);
    }
  }

  @Override
  public synchronized void removeAttachment(@NotNull PermissionAttachment attachment) {
    try {
      super.removeAttachment(attachment);
    } catch (IllegalArgumentException e) {
      this.logger.warn("Exception while removing attachment: {}", e.getMessage());
    }
  }

  private void clearAttachments() {
    try {
      Field Field = PermissibleBase.class.getDeclaredField("attachments");
      Field.setAccessible(true);
      List<?> attachments = (List<?>) Field.get(this);
      attachments.clear();
    } catch (NoSuchFieldException | IllegalAccessException e) {
      this.logger.warn("Failed to clear permission attachments of player '{}'", this.player.getName());
    }
  }

  public void updatePermGroup(PermGroup permGroup) {
    this.permGroup = permGroup;
    this.updatePermissions(false);
  }
}
