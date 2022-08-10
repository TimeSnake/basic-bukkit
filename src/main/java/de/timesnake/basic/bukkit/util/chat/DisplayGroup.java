package de.timesnake.basic.bukkit.util.chat;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.exceptions.UnsupportedGroupRankException;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroupType;
import de.timesnake.database.util.group.DbDisplayGroup;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class DisplayGroup extends de.timesnake.library.extension.util.chat.DisplayGroup<ChatColor, User>
        implements Comparable<DisplayGroup>, de.timesnake.basic.bukkit.util.permission.Group {

    public static final TablistGroupType TABLIST_GROUP_TYPE = TablistGroupType.DISPLAY_GROUP;
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

    @Override
    public ChatColor loadPrefixColor(String chatColorName) {
        String colorName = this.database.getChatColorName();
        if (colorName != null) {
            try {
                return ChatColor.valueOf(colorName.toUpperCase());
            } catch (IllegalArgumentException e) {
                Server.printError(Plugin.BUKKIT, "Can not load chat-color from display group " + name, "Group");
            }
        }
        return ChatColor.WHITE;
    }

    private void loadPrefix() {
        this.prefix = this.database.getPrefix();
        if (this.prefix == null) {
            this.prefix = "";
        }
        this.prefixColor = this.loadPrefixColor(this.database.getChatColorName());
    }

    @Override
    public String getTablistRank() {
        return this.tablistRank;
    }

    @Override
    public String getTablistPrefix() {
        return this.getPrefix();
    }

    @Override
    public ChatColor getTablistPrefixChatColor() {
        return this.getPrefixColor();
    }

    @Override
    public ChatColor getTablistChatColor() {
        return this.prefixColor;
    }

    @Override
    public String getTablistName() {
        return tablistRank;
    }

    @Override
    public Set<User> getUser() {
        return users;
    }

    @Override
    public TablistGroupType getTeamType() {
        return TablistGroupType.DISPLAY_GROUP;
    }

    public void updatePrefix() {
        this.loadPrefix();
        for (User user : this.users) {
            user.updateAlias();
        }
    }

    @Override
    public int compareTo(@NotNull DisplayGroup o) {
        return Integer.compare(this.getRank(), o.getRank());
    }

}
