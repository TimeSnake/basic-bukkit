/*
 * basic-bukkit.main
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

package de.timesnake.basic.bukkit.core.user.scoreboard;

import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroupType;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistableGroup;

import java.util.LinkedList;

public interface TablistablePlayer extends de.timesnake.basic.bukkit.util.user.scoreboard.TablistablePlayer {

    default String getFullRank(LinkedList<TablistGroupType> types) {
        StringBuilder rankBuilder = new StringBuilder();
        for (TablistGroupType type : types) {
            TablistableGroup group = this.getTablistGroup(type);

            if (!(group == null || group.getTablistRank() == null)) {
                rankBuilder.append(group.getTablistRank());
            }
        }
        return rankBuilder.toString();
    }

    default String getFullPrefix(LinkedList<TablistGroupType> types) {
        StringBuilder prefixBuilder = new StringBuilder();
        for (TablistGroupType type : types) {
            TablistableGroup group = this.getTablistGroup(type);

            if (!(group == null || group.getTablistPrefix() == null || group.getTablistPrefixChatColor() == null)) {
                prefixBuilder.append(group.getTablistPrefixChatColor()).append(group.getTablistPrefix()).append("§r");
            }
        }

        return prefixBuilder.toString();
    }

}
