package de.timesnake.basic.bukkit.util.permission;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.database.util.group.DbPermGroup;
import de.timesnake.database.util.permission.DbPermission;
import de.timesnake.library.extension.util.permission.ExPermission;

import java.util.HashSet;
import java.util.Set;

public class PermGroup extends de.timesnake.library.extension.util.permission.PermGroup {

    private final Set<User> users = new HashSet<>();

    public PermGroup(DbPermGroup database) {
        super(database);
        Server.printText(Plugin.BUKKIT, "Loaded perm-group " + this.name, "Group");
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }

    public Set<User> getUser() {
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

        Server.printText(Plugin.BUKKIT, "Updated permissions of group " + this.name + " from database", "Group");
    }
}
