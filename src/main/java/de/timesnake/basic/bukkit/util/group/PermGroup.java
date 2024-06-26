/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.group;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.database.util.group.DbPermGroup;
import de.timesnake.database.util.permission.DbPermission;
import de.timesnake.library.basic.util.UserSet;
import de.timesnake.library.permissions.ExPermission;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PermGroup extends de.timesnake.library.permissions.PermGroup<User> {

  private final UserSet<User> users = new UserSet<>();

  public PermGroup(DbPermGroup database) {
    super(database);
    this.logger.info("Loaded perm-group '{}'", this.name);
  }

  public void addUser(User user) {
    this.users.add(user);
  }

  public void removeUser(User user) {
    this.users.remove(user);
  }

  public @NotNull Collection<User> getUser() {
    return users;
  }

  @Override
  public void updatePermissions() {
    this.permissions.clear();

    for (DbPermission dbPermission : this.database.getPermissions()) {
      this.permissions.add(new ExPermission(dbPermission.getPermission(), dbPermission.getMode()));
    }

    DbPermGroup group = this.database.getInheritance();
    if (group != null) {
      this.permissions.addAll(Server.getPermGroup(group.getName()).getPermissions());
    }

    this.logger.info("Updated permissions of group '{}' from database", this.name);
  }
}
