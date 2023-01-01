/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard;

import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.scoreboard.NameTagVisibility;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistableGroup;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistablePlayer;
import de.timesnake.basic.bukkit.util.user.scoreboard.TagTablistable;
import de.timesnake.basic.bukkit.util.user.scoreboard.TeamTablistBuilder;
import de.timesnake.library.packets.util.packet.ExPacketPlayOutTablistHeaderFooter;
import de.timesnake.library.packets.util.packet.ExPacketPlayOutTablistPlayerAdd;
import de.timesnake.library.packets.util.packet.ExPacketPlayOutTablistPlayerRemove;
import de.timesnake.library.packets.util.packet.ExPacketPlayOutTablistTeamCreation;
import de.timesnake.library.packets.util.packet.ExPacketPlayOutTablistTeamPlayerAdd;
import de.timesnake.library.packets.util.packet.ExPacketPlayOutTablistTeamUpdate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TagTeamTablist extends TeamTablist {

    public TagTeamTablist(TeamTablistBuilder builder, ScoreboardPacketManager packetManager) {
        super(builder, packetManager);
    }

    @Override
    protected void load(User user) {
        super.load(user);
        // set header footer

        this.packetManager.sendPacket(user, ExPacketPlayOutTablistHeaderFooter.wrap(this.header, this.footer));

        for (int slot = 0; slot < 80; slot++) {
            this.packetManager.sendPacket(user, ExPacketPlayOutTablistTeamCreation.wrap(slot < 10 ? "0" + slot :
                    slot + "", "", ChatColor.WHITE));
        }

        for (SlotEntry entry : this.tablist) {
            if (entry != null) {
                this.packetManager.sendPacket(user, ExPacketPlayOutTablistTeamUpdate.wrap(entry.getSlot(),
                        entry.getPrefix(), entry.getChatColor(), this.getNameTagVisibility(user, entry).getPacketTag()));
                this.packetManager.sendPacket(user, ExPacketPlayOutTablistTeamPlayerAdd.wrap(entry.getSlot(),
                        entry.getPlayer().getPlayer().getName()));
                this.packetManager.sendPacket(user,
                        ExPacketPlayOutTablistPlayerAdd.wrap(entry.getPlayer().getPlayer()));
            }
        }
    }

    @Override
    protected void updateChanges() {
        Tab<SlotEntry> oldTablist = this.tablist;

        this.recalculateTablist();

        Iterator<SlotEntry> oldIt = oldTablist.iterator();
        Iterator<SlotEntry> it = this.tablist.iterator();

        Set<TablistablePlayer> movedPlayers = new HashSet<>();

        // update slot if entry has changed

        while (oldIt.hasNext() && it.hasNext()) {
            SlotEntry oldEntry = oldIt.next();
            SlotEntry newEntry = it.next();

            if (!oldEntry.getPlayer().equals(newEntry.getPlayer())) {

                Player newPlayer = newEntry.getPlayer().getPlayer();

                // not remove moved players
                if (!movedPlayers.contains(oldEntry.getPlayer())) {
                    this.broadcastPacket(ExPacketPlayOutTablistPlayerRemove.wrap(oldEntry.getPlayer().getPlayer()));
                }

                //update team with color and prefix
                for (User user : this.wachtingUsers) {
                    this.packetManager.sendPacket(user, ExPacketPlayOutTablistTeamUpdate.wrap(newEntry.getSlot(),
                            newEntry.getPrefix(), newEntry.getChatColor(),
                            this.getNameTagVisibility(user, newEntry).getPacketTag()));
                }


                // add new player
                this.broadcastPacket(ExPacketPlayOutTablistTeamPlayerAdd.wrap(newEntry.getSlot(), newPlayer.getName()));
                this.broadcastPacket(ExPacketPlayOutTablistPlayerAdd.wrap(newEntry.getPlayer().getPlayer()));

                // mark as moved
                movedPlayers.add(newEntry.getPlayer());
            }
        }


        // remove old entries and now empty slots
        while (oldIt.hasNext()) {
            SlotEntry oldEntry = oldIt.next();
            // not remove moved players
            if (!movedPlayers.remove(oldEntry.getPlayer())) {
                this.broadcastPacket(ExPacketPlayOutTablistPlayerRemove.wrap(oldEntry.getPlayer().getPlayer()));
            }

        }

        // add new entries and update team
        while (it.hasNext()) {
            SlotEntry entry = it.next();
            for (User user : this.wachtingUsers) {
                this.packetManager.sendPacket(user, ExPacketPlayOutTablistTeamUpdate.wrap(entry.getSlot(), entry.getPrefix(),
                        entry.getChatColor(), this.getNameTagVisibility(user, entry).getPacketTag()));
            }
            this.broadcastPacket(ExPacketPlayOutTablistTeamPlayerAdd.wrap(entry.getSlot(),
                    entry.getPlayer().getPlayer().getName()));
            this.broadcastPacket(ExPacketPlayOutTablistPlayerAdd.wrap(entry.getPlayer().getPlayer()));
        }
    }

    private NameTagVisibility getNameTagVisibility(User user, SlotEntry entry) {
        NameTagVisibility tagVisibility = null;
        TablistableGroup userTeam = user.getTablistGroup(teamType);

        if (userTeam != null) {
            if (entry.getTeam() != null) {
                tagVisibility = ((TagTablistable) entry.getTeam()).isNameTagVisibleBy(user, userTeam);
            }
        } else {
            if (entry.getTeam() != null) {
                tagVisibility = ((TagTablistable) entry.getTeam()).isNameTagVisible(user);
            }
        }

        if (tagVisibility == null) {
            tagVisibility = NameTagVisibility.NEVER;
        }
        return tagVisibility;
    }
}
