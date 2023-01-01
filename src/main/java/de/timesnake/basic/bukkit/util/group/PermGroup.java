/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.group;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.database.util.group.DbPermGroup;
import de.timesnake.database.util.permission.DbPermission;
import de.timesnake.library.extension.util.permission.ExPermission;
import de.timesnake.library.extension.util.player.UserList;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

public class PermGroup extends de.timesnake.library.extension.util.permission.PermGroup<User> {

    private final UserList<User> users = new UserList<>();

    public PermGroup(DbPermGroup database) {
        super(database);
        Plugin.GROUPS.getLogger().info("Loaded perm-group '" + this.name + "'");
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }

    @NotNull
    public Collection<User> getUser() {
        return users;
    }

    @Override
    public void updatePermissions() {
        this.permissions.clear();

        for (DbPermission dbPermission : this.database.getPermissions()) {
            this.permissions.add(new ExPermission(dbPermission.getName(), dbPermission.getMode(),
                    dbPermission.getServers()));
        }

        DbPermGroup group = this.database.getInheritance();
        if (group != null) {
            this.permissions.addAll(Server.getPermGroup(group.getName()).getPermissions());
        }

        Plugin.GROUPS.getLogger().info("Updated permissions of group '" + this.name + "' from database");
    }
}
