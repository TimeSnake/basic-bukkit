package de.timesnake.basic.bukkit.core.permission;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.ChatColor;
import de.timesnake.basic.bukkit.util.exceptions.UnsupportedGroupRankException;
import de.timesnake.database.util.Database;
import de.timesnake.library.basic.util.chat.Plugin;

import java.util.*;

public class GroupManager implements de.timesnake.basic.bukkit.util.permission.GroupManager {

    protected Map<String, Group> groups = new HashMap<>();

    public void loadGroups() {
        LinkedList<Integer> groupRanks = new LinkedList<>(Database.getGroups().getPermGroupRanks());
        groupRanks.sort(Comparator.comparingInt(g -> g));
        Collections.reverse(groupRanks);
        for (Integer rank : groupRanks) {
            String name = Database.getGroups().getPermGroup(rank).getName();
            try {
                this.groups.put(name, new Group(name));
            } catch (UnsupportedGroupRankException e) {
                Server.printError(Plugin.BUKKIT, ChatColor.WARNING + e.getMessage());
            }
        }
    }

    /**
     * Gets the group by name
     *
     * @param group The group name from the group to get
     * @return the group
     */
    @Override
    public final de.timesnake.basic.bukkit.util.permission.Group getGroup(String group) {
        return this.groups.get(group);
    }

    /**
     * Gets the guest group
     *
     * @return the guest group
     */
    @Override
    public final de.timesnake.basic.bukkit.util.permission.Group getGuestGroup() {
        de.timesnake.basic.bukkit.util.permission.Group guest = null;
        int id = 0;
        for (de.timesnake.basic.bukkit.util.permission.Group group : this.getGroups()) {
            if (group.getRank() > id) {
                guest = group;
                id = group.getRank();
            }
        }
        return guest;
    }

    /**
     * Gets the member group
     *
     * @return the member group
     */
    @Override
    public final de.timesnake.basic.bukkit.util.permission.Group getMemberGroup() {
        de.timesnake.basic.bukkit.util.permission.Group guest = this.getGuestGroup();
        int rank = guest.getRank() + 1;
        while (rank < 10) {
            for (de.timesnake.basic.bukkit.util.permission.Group group : this.getGroups()) {
                if (group.getRank() == rank) {
                    return group;
                }
            }
            rank++;
        }
        return guest;
    }

    /**
     * Gets all groups
     *
     * @return all groups
     */
    @Override
    public final Collection<de.timesnake.basic.bukkit.util.permission.Group> getGroups() {
        return new ArrayList<>(this.groups.values());
    }
}
