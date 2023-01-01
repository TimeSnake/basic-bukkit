/*
 * Copyright (C) 2023 timesnake
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
