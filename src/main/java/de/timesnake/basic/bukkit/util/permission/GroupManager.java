package de.timesnake.basic.bukkit.util.permission;

import java.util.Collection;

public interface GroupManager {
    Group getGroup(String group);

    Group getGuestGroup();

    Group getMemberGroup();

    Collection<Group> getGroups();
}
