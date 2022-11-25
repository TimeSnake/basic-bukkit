/*
 * workspace.basic-bukkit.main
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

package de.timesnake.basic.bukkit.core.server;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.ChatColor;
import de.timesnake.basic.bukkit.util.exception.UnsupportedGroupRankException;
import de.timesnake.basic.bukkit.util.group.DisplayGroup;
import de.timesnake.basic.bukkit.util.group.PermGroup;
import de.timesnake.database.util.Database;
import de.timesnake.database.util.group.DbDisplayGroup;
import de.timesnake.database.util.group.DbPermGroup;
import de.timesnake.library.extension.util.chat.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GroupManager implements de.timesnake.basic.bukkit.util.group.GroupManager {

    private static final String GUEST_GROUP_NAME = "guest";
    private static final String MEMBER_GROUP_NAME = "member";

    protected Map<String, PermGroup> permGroupByName = new HashMap<>();
    protected Map<String, DisplayGroup> displayGroupByName = new HashMap<>();

    public GroupManager() {
        for (DbPermGroup dbPermGroup : Database.getGroups().getPermGroups()) {
            PermGroup group = new PermGroup(dbPermGroup);
            this.permGroupByName.put(group.getName(), group);
        }


        for (DbDisplayGroup dbDisplayGroup : Database.getGroups().getDisplayGroups()) {
            DisplayGroup group;
            try {
                group = new DisplayGroup(dbDisplayGroup);
            } catch (UnsupportedGroupRankException e) {
                Server.printWarning(Plugin.BUKKIT, ChatColor.WARNING + e.getMessage());
                continue;
            }

            this.displayGroupByName.put(group.getName(), group);
        }
    }

    /**
     * Gets the group by name
     *
     * @param group The group name from the group to get
     * @return the group
     */
    @Override
    public final PermGroup getPermGroup(String group) {
        return this.permGroupByName.get(group);
    }

    /**
     * Gets the guest group
     *
     * @return the guest group
     */
    @Override
    public final PermGroup getGuestPermGroup() {
        return this.permGroupByName.get(GUEST_GROUP_NAME);
    }

    /**
     * Gets the member group
     *
     * @return the member group
     */
    @Override
    public final PermGroup getMemberPermGroup() {
        return this.permGroupByName.get(MEMBER_GROUP_NAME);
    }

    /**
     * Gets all groups
     *
     * @return all groups
     */
    @Override
    public final Collection<PermGroup> getPermGroups() {
        return new ArrayList<>(this.permGroupByName.values());
    }

    @Override
    public final DisplayGroup getDisplayGroup(String group) {
        return this.displayGroupByName.get(group);
    }

    @Override
    public final Collection<DisplayGroup> getDisplayGroups() {
        return new ArrayList<>(this.displayGroupByName.values());
    }

    @Override
    public final DisplayGroup getGuestDisplayGroup() {
        return this.displayGroupByName.get(GUEST_GROUP_NAME);
    }

    @Override
    public final DisplayGroup getMemberDisplayGroup() {
        return this.displayGroupByName.get(MEMBER_GROUP_NAME);
    }

}
