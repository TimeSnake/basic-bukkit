package de.timesnake.basic.bukkit.util.permission;

import de.timesnake.basic.bukkit.core.permission.ExPermission;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroupType;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistableGroup;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.Set;

public interface Group extends TablistableGroup {

    static TablistGroupType getTablistType() {
        return TablistGroupType.PERM_GROUP;
    }

    String getName();

    Integer getRank();

    String getPrefix();

    ChatColor getPrefixColor();

    String getTablistName();

    Set<User> getUser();

    List<ExPermission> getPermissions();

}
