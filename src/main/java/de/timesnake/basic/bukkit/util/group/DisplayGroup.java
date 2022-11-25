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

package de.timesnake.basic.bukkit.util.group;

import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.exception.UnsupportedGroupRankException;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroupType;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistableGroup;
import de.timesnake.database.util.group.DbDisplayGroup;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class DisplayGroup extends de.timesnake.library.extension.util.chat.DisplayGroup<User> implements TablistableGroup {


    public static final TablistGroupType TABLIST_TYPE_0 = TablistGroupType.DISPLAY_GROUP_0;
    public static final TablistGroupType TABLIST_TYPE_1 = TablistGroupType.DISPLAY_GROUP_1;
    public static final TablistGroupType TABLIST_TYPE_2 = TablistGroupType.DISPLAY_GROUP_2;

    public static final LinkedList<TablistGroupType> MAIN_TABLIST_GROUPS = new LinkedList<>(List.of(TABLIST_TYPE_0,
            TABLIST_TYPE_1, TABLIST_TYPE_2));

    public static final int RANK_LENGTH = 6;

    private final String tablistRank;

    public DisplayGroup(DbDisplayGroup database) throws UnsupportedGroupRankException {
        super(database);

        if (String.valueOf(this.rank).length() > RANK_LENGTH) {
            throw new UnsupportedGroupRankException(this.name, this.rank);
        }

        this.tablistRank = "0".repeat(Math.max(0, RANK_LENGTH - String.valueOf(this.rank).length())) + this.rank;

        Plugin.GROUPS.getLogger().info("Loaded display-group '" + this.name + "'");
    }

    private void loadPrefix() {
        this.prefix = this.database.getPrefix();
        if (this.prefix == null) {
            this.prefix = "";
        }
        this.prefixColor = this.database.getChatColor();
    }

    @NotNull
    @Override
    public String getTablistRank() {
        return this.tablistRank;
    }

    @Nullable
    @Override
    public String getTablistPrefix() {
        return this.getPrefix();
    }

    @Nullable
    @Override
    public ChatColor getTablistPrefixChatColor() {
        return de.timesnake.basic.bukkit.util.chat.ChatColor.translateFromExTextColor(this.getPrefixColor());
    }

    @Nullable
    @Override
    public ChatColor getTablistChatColor() {
        return de.timesnake.basic.bukkit.util.chat.ChatColor.translateFromExTextColor(this.prefixColor);
    }

    @NotNull
    @Override
    public String getTablistName() {
        return this.tablistRank;
    }

    public void updatePrefix() {
        this.loadPrefix();
        for (User user : this.users) {
            user.updateAlias();
        }
    }
}
