/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistBuilder;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroupType;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistablePlayer;
import de.timesnake.library.basic.util.BuilderNotFullyInstantiatedException;
import de.timesnake.library.packets.util.packet.*;
import org.bukkit.ChatColor;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class GroupTablist extends Tablist implements de.timesnake.basic.bukkit.util.user.scoreboard.GroupTablist {

    protected final GroupTab groupTab;

    public GroupTablist(TablistBuilder builder, ScoreboardPacketManager packetManager) {
        super(builder.getName(), builder.getType(), packetManager, builder.getUserJoin(), builder.getUserQuit());
        if (builder.getGroupTypes() == null) {
            throw new BuilderNotFullyInstantiatedException("group tablist: 'groupTypes' not instantiated");
        }
        this.groupTab = new GroupTab(builder.getGroupTypes());
        Server.registerListener(this, BasicBukkit.getPlugin());
    }

    @Override
    public void addEntry(TablistablePlayer player) {
        Plugin.SCOREBOARD.getLogger().info("tablist '" + this.name + "' try to add '" + player.getTablistName() + "'");

        if (!player.showInTablist()) {
            return;
        }

        String rank;
        if (player.getTablistPrefix() != null) {
            rank = player.getFullRank(this.groupTab.getTypes()) + player.getTablistPrefix();
        } else {
            rank = player.getFullRank(this.groupTab.getTypes());
        }
        String prefix;
        if (player.getTablistPrefix() != null) {
            prefix = player.getFullPrefix(this.groupTab.getTypes()) + player.getTablistPrefix();
        } else {
            prefix = player.getFullPrefix(this.groupTab.getTypes());
        }

        this.groupTab.addEntry(new Entry(rank, prefix, player));

        this.packetManager.sendPacket(this.wachtingUsers, ExPacketPlayOutTablistTeamPlayerAdd.wrap(rank,
                player.getPlayer().getName()));
        this.packetManager.sendPacket(this.wachtingUsers, ExPacketPlayOutTablistPlayerAdd.wrap(player.getPlayer()));

        Plugin.SCOREBOARD.getLogger().fine("tablist '" + this.name + "' added '" + player.getTablistName() + "'");
    }

    @Override
    public boolean removeEntry(TablistablePlayer player) {
        Plugin.SCOREBOARD.getLogger().info("tablist '" + this.name + "' try to remove '" + player.getTablistName() + "'");

        boolean removed = this.groupTab.removeEntry(new Entry(null, null, player));
        if (removed) {
            Plugin.SCOREBOARD.getLogger().fine("tablist '" + this.name + "' removed '" + player.getTablistName() + "'");
        }
        return removed;
    }

    @Override
    public LinkedList<TablistablePlayer> getEntries() {
        LinkedList<TablistablePlayer> players = new LinkedList<>();
        for (Entry entry : this.groupTab) {
            players.addAll(entry.getPlayers());
        }
        return players;
    }

    @Override
    protected void load(User user) {
        super.load(user);

        // set entries
        for (Entry entry : this.groupTab) {

            this.packetManager.sendPacket(user, ExPacketPlayOutTablistTeamCreation.wrap(entry.getRank(),
                    entry.getPrefix(), ChatColor.WHITE));

            for (TablistablePlayer player : entry.getPlayers()) {
                this.packetManager.sendPacket(user, ExPacketPlayOutTablistTeamPlayerAdd.wrap(entry.getRank(),
                        player.getPlayer().getName()));
                this.packetManager.sendPacket(user, ExPacketPlayOutTablistPlayerAdd.wrap(player.getPlayer()));
            }

        }
    }

    @Override
    protected void unload(User user) {

        this.packetManager.sendPacket(user, ExPacketPlayOutScoreboardObjective.wrap(this.name, "",
                ExPacketPlayOutScoreboardObjective.Display.REMOVE, this.type.getPacketType()));

        this.packetManager.sendPacket(user, ExPacketPlayOutTablistHeaderFooter.wrap(null, null));

        for (Entry entry : this.groupTab) {

            for (TablistablePlayer player : entry.getPlayers()) {
                this.packetManager.sendPacket(user, ExPacketPlayOutTablistPlayerRemove.wrap(player.getPlayer()));
                this.packetManager.sendPacket(user, ExPacketPlayOutTablistTeamPlayerRemove.wrap(entry.getRank(),
                        player.getPlayer().getName()));
            }


            this.packetManager.sendPacket(user, ExPacketPlayOutTablistTeamRemove.wrap(entry.getRank()));

        }
    }

    protected static class Entry extends TabEntry<Entry> {

        protected final String prefix;

        protected Set<TablistablePlayer> players = new HashSet<>();

        public Entry(String rank, String prefix, TablistablePlayer player) {
            super(rank);
            this.prefix = prefix;
            this.players.add(player);
        }

        @Override
        public void merge(Entry entry) {
            this.players.addAll(entry.getPlayers());
        }

        public String getPrefix() {
            return this.prefix;
        }

        public Integer getSize() {
            return this.players.size();
        }

        public Collection<TablistablePlayer> getPlayers() {
            return this.players;
        }

        public boolean removePlayer(TablistablePlayer player) {
            return this.players.remove(player);
        }

    }

    protected class GroupTab extends Tab<Entry> {

        protected final LinkedList<TablistGroupType> types;

        public GroupTab(LinkedList<TablistGroupType> types) {
            this.types = types;
        }

        @Override
        public boolean removeEntry(Entry entry) {
            boolean removed = false;
            for (Entry current : this) {
                for (TablistablePlayer player : entry.getPlayers()) {
                    removed |= current.removePlayer(player);
                }
            }
            return removed;
        }

        public LinkedList<TablistGroupType> getTypes() {
            return types;
        }

        @Override
        public boolean addEntry(Entry entry) {
            boolean merged = super.addEntry(entry);
            if (!merged) {
                GroupTablist.this.packetManager.sendPacket(GroupTablist.this.wachtingUsers,
                        ExPacketPlayOutTablistTeamCreation.wrap(entry.getRank(), entry.getPrefix(), ChatColor.WHITE));
            }
            return merged;
        }
    }

}
   