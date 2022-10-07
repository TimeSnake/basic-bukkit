package de.timesnake.basic.bukkit.util.chat;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.exceptions.UnsupportedGroupRankException;
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

        Server.printText(Plugin.BUKKIT, "Loaded display-group " + this.name, "Group");
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
