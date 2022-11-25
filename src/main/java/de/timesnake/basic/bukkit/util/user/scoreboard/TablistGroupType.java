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

package de.timesnake.basic.bukkit.util.user.scoreboard;

import de.timesnake.basic.bukkit.util.group.DisplayGroup;

public class TablistGroupType {

    public static final TablistGroupType DUMMY = new TablistGroupType(null);
    public static final TablistGroupType FAKE_GROUP = new TablistGroupType(null);
    public static final TablistGroupType DISPLAY_GROUP_0 = new TablistGroupType(DisplayGroup.class);
    public static final TablistGroupType DISPLAY_GROUP_1 = new TablistGroupType(DisplayGroup.class);
    public static final TablistGroupType DISPLAY_GROUP_2 = new TablistGroupType(DisplayGroup.class);
    public static final TablistGroupType TAB_HEADER = new TablistGroupType(null);

    private final Class<? extends TablistableGroup> groupClass;

    public TablistGroupType(Class<? extends TablistableGroup> groupClass) {
        this.groupClass = groupClass;
    }

    public Class<? extends TablistableGroup> getGroup() {
        return this.groupClass;
    }
}
