/*
 * timesnake.basic-bukkit.main
 * Copyright (C) 2022 timesnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; If not, see <http://www.gnu.org/licenses/>.
 */

package de.timesnake.basic.bukkit.util.permission;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.database.util.group.DbPermGroup;
import de.timesnake.database.util.permission.DbPermission;
import de.timesnake.library.extension.util.permission.ExPermission;
import de.timesnake.library.extension.util.player.UserList;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

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
