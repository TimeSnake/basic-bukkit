package de.timesnake.basic.bukkit.util.permission;

import de.timesnake.basic.bukkit.util.chat.DisplayGroup;

import java.util.Collection;

public interface GroupManager {
    PermGroup getPermGroup(String group);

    PermGroup getGuestPermGroup();

    PermGroup getMemberPermGroup();

    Collection<PermGroup> getPermGroups();

    DisplayGroup getDisplayGroup(String group);

    Collection<DisplayGroup> getDisplayGroups();

    DisplayGroup getGuestDisplayGroup();

    DisplayGroup getMemberDisplayGroup();
}
