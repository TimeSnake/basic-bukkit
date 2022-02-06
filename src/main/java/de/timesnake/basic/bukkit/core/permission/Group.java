package de.timesnake.basic.bukkit.core.permission;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.exceptions.UnsupportedGroupRankException;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroupType;
import de.timesnake.database.util.Database;
import de.timesnake.database.util.group.DbPermGroup;
import de.timesnake.database.util.permission.DbPermission;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Group implements de.timesnake.basic.bukkit.util.permission.Group {

    public static final int RANK_LENGTH = 6;

    private final String name;
    private final Integer rank;
    private final DbPermGroup database;
    private String prefix;
    private ChatColor prefixColor;
    private final String tablistRank;
    private final Set<User> users = new HashSet<>();
    private final List<ExPermission> permissions = new ArrayList<>();


    public Group() {
        this.name = null;
        this.rank = null;
        this.database = null;
        this.tablistRank = null;
    }

    public Group(String name) throws UnsupportedGroupRankException {
        this.name = name;
        this.database = Database.getGroups().getPermGroup(name);

        DbPermGroup dbLocal = this.database.toLocal();

        this.prefix = dbLocal.getPrefix();
        if (this.prefix == null) {
            this.prefix = "";
        }
        try {
            this.prefixColor = ChatColor.valueOf(this.database.getChatColorName());
        } catch (IllegalArgumentException e) {
            Server.printError(Plugin.BUKKIT, "Can not load chat-color from group " + name, "Group");
            this.prefixColor = ChatColor.WHITE;
        }

        this.rank = dbLocal.getRank();

        if (String.valueOf(this.rank).length() > RANK_LENGTH) {
            throw new UnsupportedGroupRankException(this.name, this.rank);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < RANK_LENGTH - String.valueOf(this.rank).length(); i++) {
            sb.append("0");
        }
        this.tablistRank = sb.append(this.rank).toString();

        this.updatePermissions();

        Server.printText(Plugin.BUKKIT, "Loaded group " + this.name + " successfully", "Group");
    }


    private void loadPrefix() {
        this.prefix = this.database.getPrefix();
        if (this.prefix == null) {
            this.prefix = "";
        }
        try {
            this.prefixColor = ChatColor.valueOf(this.database.getChatColorName());
        } catch (IllegalArgumentException e) {
            Server.printError(Plugin.BUKKIT, "Can not load chat-color from group " + name, "Group");
            this.prefixColor = ChatColor.WHITE;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getRank() {
        return rank;
    }

    @Override
    public String getTablistRank() {
        return this.tablistRank;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public String getTablistPrefix() {
        return this.getPrefix();
    }

    @Override
    public ChatColor getPrefixColor() {
        return prefixColor;
    }

    @Override
    public ChatColor getTablistPrefixChatColor() {
        return this.getPrefixColor();
    }

    @Override
    public ChatColor getTablistChatColor() {
        return this.prefixColor;
    }

    @Deprecated
    @Override
    public String getTablistName() {
        return tablistRank;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }

    @Override
    public Set<User> getUser() {
        return users;
    }

    @Override
    public List<ExPermission> getPermissions() {
        return this.permissions;
    }

    @Override
    public TablistGroupType getTeamType() {
        return TablistGroupType.PERM_GROUP;
    }

    public void updatePermissions() {
        this.permissions.clear();

        for (DbPermission dbPermission : this.database.getPermissions()) {
            this.permissions.add(new ExPermission(dbPermission.getName(), dbPermission.getMode(), dbPermission.getServers()));
        }

        DbPermGroup group = this.database.getInheritance();
        while (group != null) {
            Group serverGroup = (Group) Server.getGroup(group.getName());
            this.permissions.addAll(serverGroup.getPermissions());
            group = group.getInheritance();
        }

        Server.printText(Plugin.BUKKIT, "Updated permissions of group " + this.name + " from database", "Group");
    }

    public void updatePrefix() {
        this.loadPrefix();
        for (User user : this.users) {
            user.updateAlias();
        }
    }
}
