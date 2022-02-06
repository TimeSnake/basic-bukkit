package de.timesnake.basic.bukkit.util.user.scoreboard;

import de.timesnake.basic.bukkit.core.permission.Group;

public class TablistGroupType {

    public static final TablistGroupType DUMMY = new TablistGroupType(null);
    public static final TablistGroupType FAKE_GROUP = new TablistGroupType(null);
    public static final TablistGroupType PERM_GROUP = new TablistGroupType(Group.class);
    public static final TablistGroupType TAB_HEADER = new TablistGroupType(null);

    private final Class<? extends TablistableGroup> groupClass;

    public TablistGroupType(Class<? extends TablistableGroup> groupClass) {
        this.groupClass = groupClass;
    }

    public Class<? extends TablistableGroup> getGroup() {
        return this.groupClass;
    }
}
