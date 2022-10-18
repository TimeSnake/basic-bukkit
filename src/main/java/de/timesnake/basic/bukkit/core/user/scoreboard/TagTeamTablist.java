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

import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistablePlayer;
import de.timesnake.basic.bukkit.util.user.scoreboard.*;
import de.timesnake.library.packets.util.packet.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class TagTeamTablist extends TeamTablist {

    public TagTeamTablist(String name, Type tablistType, ScoreboardPacketManager packetManager, ColorType colorType,
                          TablistGroupType teamType, Collection<? extends TagTablistableGroup> teams,
                          LinkedList<TablistGroupType> groupTypes, TablistUserJoin userJoin, TablistUserQuit userQuit) {
        super(name, tablistType, packetManager, colorType, teamType, teams, groupTypes, userJoin, userQuit);
    }

    public TagTeamTablist(String name, Type tablistType, ColorType colorType, ScoreboardPacketManager packetManager,
                          TablistGroupType teamType, Collection<? extends TagTablistableGroup> teams,
                          LinkedList<TablistGroupType> groupTypes, TagTablistableRemainTeam remainTeam,
                          LinkedList<TablistGroupType> remainTeamGroupTypes, TablistUserJoin userJoin, TablistUserQuit userQuit) {
        super(name, tablistType, colorType, packetManager, teamType, teams, groupTypes, remainTeam,
                remainTeamGroupTypes, userJoin, userQuit);
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

        for (TablistEntry entry : this.tablist) {
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
        Tab<TablistEntry> oldTablist = this.tablist;

        this.recalculateTablist();

        Iterator<TablistEntry> oldIt = oldTablist.iterator();
        Iterator<TablistEntry> it = this.tablist.iterator();

        Set<TablistablePlayer> movedPlayers = new HashSet<>();

        // update slot if entry has changed

        while (oldIt.hasNext() && it.hasNext()) {
            TablistEntry oldEntry = oldIt.next();
            TablistEntry newEntry = it.next();

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
            TablistEntry oldEntry = oldIt.next();
            // not remove moved players
            if (!movedPlayers.remove(oldEntry.getPlayer())) {
                this.broadcastPacket(ExPacketPlayOutTablistPlayerRemove.wrap(oldEntry.getPlayer().getPlayer()));
            }

        }

        // add new entries and update team
        while (it.hasNext()) {
            TablistEntry entry = it.next();
            for (User user : this.wachtingUsers) {
                this.packetManager.sendPacket(user, ExPacketPlayOutTablistTeamUpdate.wrap(entry.getSlot(), entry.getPrefix(),
                        entry.getChatColor(), this.getNameTagVisibility(user, entry).getPacketTag()));
            }
            this.broadcastPacket(ExPacketPlayOutTablistTeamPlayerAdd.wrap(entry.getSlot(),
                    entry.getPlayer().getPlayer().getName()));
            this.broadcastPacket(ExPacketPlayOutTablistPlayerAdd.wrap(entry.getPlayer().getPlayer()));
        }
    }

    private NameTagVisibility getNameTagVisibility(User user, TablistEntry entry) {
        NameTagVisibility tagVisibility = null;
        TablistableGroup userTeam = user.getTablistGroup(teamTypes.getFirst());

        if (userTeam != null) {
            if (entry.getTeam() != null) {
                tagVisibility = ((TagTablistable) entry.getTeam()).isNameTagVisibleBy(user, userTeam);
            }
        } else {
            tagVisibility = ((TagTablistable) entry.getTeam()).isNameTagVisible(user);
        }

        if (tagVisibility == null) {
            tagVisibility = NameTagVisibility.NEVER;
        }
        return tagVisibility;
    }
}
