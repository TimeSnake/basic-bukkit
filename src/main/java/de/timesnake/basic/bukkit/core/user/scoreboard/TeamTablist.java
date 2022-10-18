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

public class TeamTablist extends Tablist implements de.timesnake.basic.bukkit.util.user.scoreboard.TeamTablist {

    public static final int SPACE_LINES = 2;
    public static final int MAX_ONE_COLUMN = 12 - SPACE_LINES;
    public static final int MAX_ONE_COLUMN_REMAIN = 14 - 2 * SPACE_LINES;

    protected final LinkedList<TablistGroupType> teamTypes;

    protected final TeamTab teamTab;
    protected final ColorType colorType;
    protected final HashMap<String, Entry> fakePlayers = new HashMap<>();
    protected final HashMap<TablistablePlayer, Key> userRankKey = new HashMap<>();
    protected TablistTeam remainTeam;
    protected ArrayList<Integer> blankSlots;
    protected Tab<TablistEntry> tablist;
    protected Integer counter = 0;
    protected NameTagVisibility nameTagVisibility = NameTagVisibility.ALWAYS;

    protected TeamTablist(String name, Type tablistType, ScoreboardPacketManager packetManager, ColorType colorType,
                          TablistGroupType teamType, TablistUserJoin userJoin, TablistUserQuit userQuit) {

        super(name, tablistType, packetManager, userJoin, userQuit);
        this.colorType = colorType;
        this.teamTypes = new LinkedList<>();
        this.teamTypes.add(teamType);
        this.teamTab = new TeamTab();
        this.tablist = new Tab<>();

        for (int i = 0; i < 80; i++) {
            String rank = i < 10 ? "0" + i : "" + i;
            this.fakePlayers.put(rank, new Entry(null, null, null, rank, null, null,
                    new FakeTablistPlayer("§" + i / 10 + "§" + i % 10, ExPacketPlayOutTablist.Head.BLANK)));
        }

    }

    public TeamTablist(String name, Type tablistType, ScoreboardPacketManager packetManager, ColorType colorType,
                       TablistGroupType teamType, Collection<? extends TablistableGroup> teams,
                       LinkedList<TablistGroupType> groupTypes, TablistUserJoin userJoin, TablistUserQuit userQuit) {

        this(name, tablistType, packetManager, colorType, teamType, userJoin, userQuit);
        for (TablistableGroup group : teams) {
            this.teamTab.addEntry(new TablistTeam(group.getTablistRank(), group.getTablistChatColor(), true,
                    groupTypes));
        }

        this.updateChanges();

    }

    public TeamTablist(String name, Type tablistType, ColorType colorType, ScoreboardPacketManager packetManager,
                       TablistGroupType teamType, Collection<? extends TablistableGroup> teams,
                       LinkedList<TablistGroupType> groupTypes, TablistableRemainTeam remainTeam,
                       LinkedList<TablistGroupType> remainTeamGroupTypes, TablistUserJoin userJoin,
                       TablistUserQuit userQuit) {

        this(name, tablistType, packetManager, colorType, teamType, userJoin, userQuit);
        for (TablistableGroup group : teams) {
            this.teamTab.addEntry(new TablistTeam(group.getTablistRank(), group.getTablistChatColor(), true,
                    groupTypes));
        }

        this.remainTeam = new TablistTeam("0", remainTeam.getTablistChatColor(), true, remainTeamGroupTypes);

        this.updateChanges();

    }

    @Override
    protected void load(User user) {
        super.load(user);
        // set header footer

        this.packetManager.sendPacket(user, ExPacketPlayOutTablistHeaderFooter.wrap(this.header, this.footer));

        for (int slot = 0; slot < 80; slot++) {
            this.packetManager.sendPacket(user, ExPacketPlayOutTablistTeamCreation.wrap(slot < 10 ? "0" + slot :
                    slot + "", "", ChatColor.WHITE, this.nameTagVisibility.getPacketTag()));
        }

        for (TablistEntry entry : this.tablist) {
            if (entry != null) {
                this.packetManager.sendPacket(user, ExPacketPlayOutTablistTeamUpdate.wrap(entry.getSlot(),
                        entry.getPrefix(), entry.getChatColor()));
                this.packetManager.sendPacket(user, ExPacketPlayOutTablistTeamPlayerAdd.wrap(entry.getSlot(),
                        entry.getPlayer().getPlayer().getName()));
                this.packetManager.sendPacket(user,
                        ExPacketPlayOutTablistPlayerAdd.wrap(entry.getPlayer().getPlayer()));
            }
        }
    }

    @Override
    protected void unload(User user) {

        this.packetManager.sendPacket(user, ExPacketPlayOutScoreboardObjective.wrap(this.name, "",
                ExPacketPlayOutScoreboardObjective.Display.REMOVE, this.type.getPacketType()));

        for (TablistEntry entry : this.tablist) {
            if (entry != null) {
                this.packetManager.sendPacket(user, ExPacketPlayOutTablistTeamPlayerRemove.wrap(entry.getSlot(),
                        entry.getPlayer().getPlayer().getName()));
                this.packetManager.sendPacket(user,
                        ExPacketPlayOutTablistPlayerRemove.wrap(entry.getPlayer().getPlayer()));
            }
        }

        for (int slot = 0; slot < 80; slot++) {
            this.packetManager.sendPacket(user, ExPacketPlayOutTablistTeamRemove.wrap(slot < 10 ? "0" + slot :
                    slot + ""));
        }
    }

    // check player size for all teams -> calc slots (includes sizes)
    // fill up with fake players to size
    // set players in slot team
    // store in head-tail, changed entry -> update to user
    protected void recalculateTablist() {
        this.tablist = new Tab<>();
        this.blankSlots = new ArrayList<>();
        this.calcStyle();

        for (TablistTeam team : this.teamTab) {
            team.fillSlots();
        }

        //spec
        if (this.remainTeam != null) {
            this.remainTeam.fillSlots();
        }

        //blank
        if (this.blankSlots != null) {
            for (Integer slot : this.blankSlots) {
                String slotRank = slot < 10 ? "0" + slot : "" + slot;
                this.tablist.addEntry(this.fakePlayers.get(slotRank).toTablistEntry(slot));
            }
        }
    }

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
                this.broadcastPacket(ExPacketPlayOutTablistTeamUpdate.wrap(newEntry.getSlot(), newEntry.getPrefix(),
                        newEntry.getChatColor()));

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
            this.broadcastPacket(ExPacketPlayOutTablistTeamUpdate.wrap(entry.getSlot(), entry.getPrefix(),
                    entry.getChatColor()));
            this.broadcastPacket(ExPacketPlayOutTablistTeamPlayerAdd.wrap(entry.getSlot(),
                    entry.getPlayer().getPlayer().getName()));
            this.broadcastPacket(ExPacketPlayOutTablistPlayerAdd.wrap(entry.getPlayer().getPlayer()));
        }
    }

    @Override
    public void addEntry(TablistablePlayer player) {
        this.addEntry(player, true);
    }

    private void addEntry(TablistablePlayer player, boolean update) {
        if (!player.showInTablist()) {
            return;
        }

        // prevent duplicate
        if (this.userRankKey.containsKey(player)) {
            this.removeEntry(player, false);
        }

        this.teamTab.addPlayerToEntry(player);

        if (update) {
            this.updateChanges();
        }
    }

    @Override
    public void addRemainEntry(TablistablePlayer player) {
        this.addRemainEntry(player, true);
    }

    private void addRemainEntry(TablistablePlayer player, boolean update) {
        if (!player.showInTablist()) {
            return;
        }

        // prevent duplicate
        if (this.userRankKey.containsKey(player)) {
            this.removeEntry(player, false);
        }

        String groupRank = player.getFullRank(this.remainTeam.types);
        String prefix;
        if (player.getTablistPrefix() != null) {
            prefix = player.getFullPrefix(this.teamTypes) + player.getFullPrefix(this.remainTeam.types) +
                    player.getTablistPrefix();
        } else {
            prefix = player.getFullPrefix(this.teamTypes) + player.getFullPrefix(this.remainTeam.types);
        }
        this.remainTeam.addEntry(new Entry(this.remainTeam, player.getTablistGroup(this.teamTypes.getFirst()),
                this.remainTeam.getRank(), groupRank, prefix, this.remainTeam.getChatColor(), player));

        if (update) {
            this.updateChanges();
        }
    }

    @Override
    public boolean removeEntry(TablistablePlayer player) {
        return this.removeEntry(player, true);
    }

    private boolean removeEntry(TablistablePlayer player, boolean update) {
        boolean removed = this.teamTab.removePlayerFromEntry(player);

        // remove from remain team
        if (!removed) {
            if (this.remainTeam != null) {
                if (userRankKey.get(player) != null) {
                    String rank = userRankKey.get(player).teamRank();
                    if (rank != null) {
                        removed |= this.remainTeam.removeEntry(rank, player);
                    }
                }

            }
        }

        if (removed && update) {
            this.updateChanges();
        }
        return removed;
    }

    @Override
    public LinkedList<TablistablePlayer> getEntries() {
        LinkedList<TablistablePlayer> players = new LinkedList<>();
        for (TablistTeam team : this.teamTab) {
            for (Entry entry : team.headerTab) {
                players.add(entry.getPlayer());
            }
            for (Entry entry : team.playerTab) {
                players.add(entry.getPlayer());
            }
        }
        return players;
    }

    @Override
    public void addTeamHeader(String teamRank, String headerRank, String name, ExPacketPlayOutTablist.Head head) {
        this.teamTab.addHeaderToEntry(teamRank, headerRank, name, head);
        this.updateChanges();
    }

    @Override
    public void addTeamHeader(String teamRank, String headerRank, String name) {
        this.teamTab.addHeaderToEntry(teamRank, headerRank, name, ExPacketPlayOutTablist.Head.BLANK);
        this.updateChanges();
    }

    @Override
    public void removeTeamHeader(String teamRank, String headerRank) {
        this.teamTab.removeHeaderFromEntry(teamRank, headerRank);
        this.updateChanges();
    }

    public TeamTablist setNameTagVisibility(NameTagVisibility visibility) {
        this.nameTagVisibility = visibility;
        this.updateChanges();
        return this;
    }

    private void calcTeamsOnly() {
        Iterator<TablistTeam> it = this.teamTab.iterator();
        switch (this.teamTab.size()) {
            case 1 -> calcSingle(it.next());
            case 2 -> calcDouble(it.next(), it.next());
            case 3 -> calcTriple(it.next(), it.next(), it.next());
            case 4 -> calcQuadruple(it.next(), it.next(), it.next(), it.next());
            case 5 -> calcQuintuple(it.next(), it.next(), it.next(), it.next(), it.next());
            case 6 -> calcSixtuple(it.next(), it.next(), it.next(), it.next(), it.next(), it.next());
            default -> {
            }
        }
    }

    private void addBlankSlots() {
        this.addBlankSlots(SPACE_LINES);
    }

    private void addBlankSlots(int amount) {
        int to = counter + amount;
        for (; counter < to; counter++) {
            this.blankSlots.add(counter);
        }
    }

    private void calcStyle() {
        this.counter = 0;

        // reset team slots
        for (TablistTeam team : teamTab) {
            team.setSlots(new ArrayList<>());
        }

        // reset remain team slots
        if (this.remainTeam != null) {
            this.remainTeam.setSlots(new ArrayList<>());
        }

        if (this.remainTeam == null) {
            calcTeamsOnly();
        } else {
            calcTeamsAndRemain();
        }
    }

    private void calcTeamsAndRemain() {
        Iterator<TablistTeam> it = this.teamTab.iterator();
        switch (this.teamTab.size()) {
            case 1 -> calcSingleRemain(it.next(), this.remainTeam);
            case 2 -> calcDoubleRemain(it.next(), it.next(), this.remainTeam);
            case 3 -> calcTripleRemain(it.next(), it.next(), it.next(), this.remainTeam);
            case 4 -> calcQuadrupleRemain(it.next(), it.next(), it.next(), it.next(), this.remainTeam);
            default -> {
            }
        }
    }

    private void calcTriple(TablistTeam first, TablistTeam second, TablistTeam third) {
        int firstSize = first.getSize();
        int secondSize = second.getSize();
        int thirdSize = third.getSize();

        int firstSlots = (firstSize % 20) > 0 ? (firstSize / 20) + 1 : (firstSize / 20);
        int secondSlots = (secondSize % 20) > 0 ? (secondSize / 20) + 1 : (secondSize / 20);
        int thirdSlots = (thirdSize % 20) > 0 ? (thirdSize / 20) + 1 : (thirdSize / 20);

        if (firstSlots + secondSlots + thirdSlots > 4) {
            if (firstSize + secondSize + thirdSize + 2 * (SPACE_LINES) > 80) {
                //TODO equal distribution
            } else {
                first.addSlots(firstSize);
                this.addBlankSlots();
                second.addSlots(secondSize);
                this.addBlankSlots();
                third.addSlots(thirdSize);
            }
        } else {
            int height = this.max(Math.ceil((double) firstSize / firstSlots),
                    Math.ceil((double) secondSize / secondSlots),
                    Math.ceil((double) thirdSize / thirdSlots));

            if (height <= 10) {
                height = 11;
            }

            for (int i = 0; i < firstSlots; i++) {
                first.addSlots(height);
            }
            for (int i = 0; i < secondSlots; i++) {
                second.addSlots(height);
            }
            for (int i = 0; i < thirdSlots; i++) {
                third.addSlots(height);
            }
        }
    }

    private void calcSingle(TablistTeam first) {
        int size = first.getSize();
        first.addSlots(size);
    }

    private void calcDouble(TablistTeam first, TablistTeam second) {
        int firstSize = first.getSize();
        int secondSize = second.getSize();

        int firstSlots = (firstSize % 20) > 0 ? (firstSize / 20) + 1 : (firstSize / 20);
        int secondSlots = (secondSize % 20) > 0 ? (secondSize / 20) + 1 : (secondSize / 20);

        if (firstSlots + secondSlots > 4) {
            if (firstSize + secondSize + SPACE_LINES > 80) {
                //TODO equal distribution
            } else {
                first.addSlots(firstSize);
                this.addBlankSlots();
                second.addSlots(secondSize);
            }
        } else {
            int height = this.max(Math.ceil((double) firstSize / firstSlots),
                    Math.ceil((double) secondSize / secondSlots));
            if (height <= 10) {
                if (firstSize + secondSize < MAX_ONE_COLUMN) {
                    first.addSlots(firstSize);
                    this.addBlankSlots();
                    second.addSlots(secondSize);
                    return;
                } else {
                    height = 11;
                }

            }

            for (int i = 0; i < firstSlots; i++) {
                first.addSlots(height);
            }
            for (int i = 0; i < secondSlots; i++) {
                second.addSlots(height);
            }
        }
    }

    private void calcQuadruple(TablistTeam first, TablistTeam second, TablistTeam third, TablistTeam fourth) {
        int firstSize = first.getSize();
        int secondSize = second.getSize();
        int thirdSize = third.getSize();
        int fourthSize = fourth.getSize();

        int firstSlots = (firstSize % 20) > 0 ? (firstSize / 20) + 1 : (firstSize / 20);
        int secondSlots = (secondSize % 20) > 0 ? (secondSize / 20) + 1 : (secondSize / 20);
        int thirdSlots = (thirdSize % 20) > 0 ? (thirdSize / 20) + 1 : (thirdSize / 20);
        int fourthSlots = (fourthSize % 20) > 0 ? (fourthSize / 20) + 1 : (fourthSize / 20);

        if (firstSlots + secondSlots + thirdSlots + fourthSlots > 4) {
            if (firstSize + secondSize + thirdSize + fourthSlots + 3 * (SPACE_LINES) > 80) {
                //TODO equal distribution
            } else {
                first.addSlots(firstSize);
                this.addBlankSlots();
                second.addSlots(secondSize);
                this.addBlankSlots();
                third.addSlots(thirdSize);
                this.addBlankSlots();
                fourth.addSlots(fourthSize);
            }
        } else {
            int height = this.max(Math.ceil((double) firstSize / firstSlots),
                    Math.ceil((double) secondSize / secondSlots),
                    Math.ceil((double) thirdSize / thirdSlots), Math.ceil((double) fourthSize / fourthSlots));

            if (height <= 10) {
                height = 11;
            }

            for (int i = 0; i < firstSlots; i++) {
                first.addSlots(height);
            }
            for (int i = 0; i < secondSlots; i++) {
                second.addSlots(height);
            }
            for (int i = 0; i < thirdSlots; i++) {
                third.addSlots(height);
            }
            for (int i = 0; i < fourthSlots; i++) {
                fourth.addSlots(height);
            }
        }
    }

    private void calcTripleRemain(TablistTeam first, TablistTeam second, TablistTeam third, TablistTeam remain) {
        int firstSize = first.getSize();
        int secondSize = second.getSize();
        int thirdSize = third.getSize();
        int remainSize = remain != null ? remain.getSize() : 0;

        if (firstSize + secondSize + thirdSize <= 80 - 4 - 4 * SPACE_LINES && remainSize > 0) { //Remain team can fit
            while ((3 * this.max(firstSize, secondSize, thirdSize) + remainSize) > (80 - 4 * SPACE_LINES) && (remainSize > 0)) {
                remainSize--;
            }

            int remainLines = (remainSize % 4) > 0 ? (remainSize / 4) + 1 : (remainSize / 4);
            remainLines += SPACE_LINES;
            int availableLines = 20 - remainLines;

            int firstSlots = (firstSize % availableLines) > 0 ? (firstSize / availableLines) + 1 :
                    (firstSize / availableLines);
            int secondSlots = (secondSize % availableLines) > 0 ? (secondSize / availableLines) + 1 :
                    (secondSize / availableLines);
            int thirdSlots = (thirdSize % availableLines) > 0 ? (thirdSize / availableLines) + 1 :
                    (thirdSize / availableLines);

            if (firstSlots + secondSlots + thirdSlots > 4) {
                if (firstSize + secondSize + thirdSize + 2 * SPACE_LINES > 4 * availableLines) {
                    //TODO equal distribution
                } else {
                    first.addSlots(firstSize);
                    this.addBlankSlots();
                    second.addSlots(secondSize);
                    this.addBlankSlots();
                    third.addSlots(thirdSize);
                }
            } else {
                int height = this.max(Math.ceil((double) firstSize / firstSlots),
                        Math.ceil((double) secondSize / secondSlots),
                        Math.ceil((double) thirdSize / thirdSlots));
                if (height <= (availableLines / 2)) {
                    height = (availableLines / 2) + 1;
                }

                for (int i = 0; i < firstSlots; i++) {
                    first.addSlots(height);
                    this.addBlankSlots();
                    remainTeam.addSlots(remainLines - SPACE_LINES);
                }
                for (int i = 0; i < secondSlots; i++) {
                    second.addSlots(height);
                    this.addBlankSlots();
                    remainTeam.addSlots(remainLines - SPACE_LINES);
                }
                for (int i = 0; i < thirdSlots; i++) {
                    third.addSlots(height);
                    this.addBlankSlots();
                    remainTeam.addSlots(remainLines - SPACE_LINES);
                }
            }
        } else {                                    //It is impossible to fit the remaining team or is empty
            calcTriple(first, second, third);
        }
    }

    private void calcQuintuple(TablistTeam first, TablistTeam second, TablistTeam third, TablistTeam fourth,
                               TablistTeam fifth) {
        return;
    }

    private void calcSixtuple(TablistTeam first, TablistTeam second, TablistTeam third, TablistTeam fourth,
                              TablistTeam fifth, TablistTeam sixth) {
        return;
    }

    private void calcSingleRemain(TablistTeam first, TablistTeam remain) {
        int size = first.getSize();
        int remainSize = remain != null ? remain.getSize() : 0;
        int sumSize = size + remainSize;
        int columns;

        if (remainSize == 0) {
            this.calcSingle(first);
            return;
        }

        if (sumSize + SPACE_LINES <= 20) {
            columns = 1;
        } else if (sumSize + 2 * SPACE_LINES <= 40) {
            columns = 2;
        } else if (sumSize + 3 * SPACE_LINES <= 60) {
            columns = 3;
        } else if (sumSize + 4 * SPACE_LINES > 80) {
            columns = 4;
            int diff = sumSize - 80;
            if (size >= 80 || diff <= 4 * SPACE_LINES) {
                remainSize = 0;
            } else if (remainSize > (diff / 4) * 4) {
                remainSize = (diff / 4) * 4;
            }
        } else {
            columns = 4;
        }

        int remainDiv = (int) Math.ceil(remainSize / (float) columns);
        int team = (int) Math.ceil(size / (float) columns);

        for (int i = 0; i < columns; i++) {
            first.addSlots(team);
            if (remainSize > 0) {
                this.addBlankSlots();
                remainTeam.addSlots(remainDiv);
            }
        }
    }

    private void calcDoubleRemain(TablistTeam first, TablistTeam second, TablistTeam remain) {
        int firstSize = first.getSize();
        int secondSize = second.getSize();
        int remainSize = remain != null ? remain.getSize() : 0;

        if (firstSize + secondSize <= 80 - 4 - 4 * SPACE_LINES && remainSize > 0) { //Remain team can fit
            while (firstSize + secondSize + remainSize > 80 - 4 * SPACE_LINES && remainSize > 0) {
                remainSize--;
            }

            int remainLines = (remainSize % 4) > 0 ? (remainSize / 4) + 1 : (remainSize / 4);
            remainLines += SPACE_LINES;
            int availableLines = 20 - remainLines;

            int firstSlots = (firstSize % availableLines) > 0 ? (firstSize / availableLines) + 1 :
                    (firstSize / availableLines);
            int secondSlots = (secondSize % availableLines) > 0 ? (secondSize / availableLines) + 1 :
                    (secondSize / availableLines);

            if (firstSlots + secondSlots > 4) {
                if (firstSize + secondSize + SPACE_LINES > 4 * availableLines) {
                    //TODO equal distribution
                } else {
                    first.addSlots(firstSize);
                    this.addBlankSlots();
                    second.addSlots(secondSize);
                }
            } else {
                int height = this.max(Math.ceil((double) firstSize / firstSlots),
                        Math.ceil((double) secondSize / secondSlots));

                if (height <= (availableLines / 2)) {
                    if (firstSize + secondSize + remainSize < MAX_ONE_COLUMN_REMAIN) {
                        first.addSlots(firstSize);
                        this.addBlankSlots();
                        second.addSlots(secondSize);
                        this.addBlankSlots();
                        remainTeam.addSlots(remainSize);
                        return;
                    } else {
                        height = (availableLines / 2) + 1;
                    }

                }

                for (int i = 0; i < firstSlots; i++) {
                    first.addSlots(height);
                    this.addBlankSlots();
                    remainTeam.addSlots(remainLines - SPACE_LINES);
                }
                for (int i = 0; i < secondSlots; i++) {
                    second.addSlots(height);
                    this.addBlankSlots();
                    remainTeam.addSlots(remainLines - SPACE_LINES);
                }
            }
        } else { //It is impossible to fit the remaining team or is empty
            calcDouble(first, second);
        }
    }

    private void calcQuadrupleRemain(TablistTeam first, TablistTeam second, TablistTeam third, TablistTeam fourth,
                                     TablistTeam remain) {
        int firstSize = first.getSize();
        int secondSize = second.getSize();
        int thirdSize = third.getSize();
        int fourthSize = fourth.getSize();
        int remainSize = remain != null ? remain.getSize() : 0;

        if (firstSize + secondSize + thirdSize + fourthSize <= 80 - 4 - 4 * SPACE_LINES && remainSize > 0) {//Remain
            // team can fit
            while ((this.max(firstSize, secondSize, thirdSize, fourthSize) + remainSize) > (80 - 4 * SPACE_LINES) && remainSize > 0) {
                remainSize--;
            }

            int remainLines = (remainSize % 4) > 0 ? (remainSize / 4) + 1 : (remainSize / 4);
            remainLines += SPACE_LINES;
            int availableLines = 20 - remainLines;

            int firstSlots = (firstSize % availableLines) > 0 ? (firstSize / availableLines) + 1 :
                    (firstSize / availableLines);
            int secondSlots = (secondSize % availableLines) > 0 ? (secondSize / availableLines) + 1 :
                    (secondSize / availableLines);
            int thirdSlots = (thirdSize % availableLines) > 0 ? (thirdSize / availableLines) + 1 :
                    (thirdSize / availableLines);
            int fourthSlots = (fourthSize % availableLines) > 0 ? (fourthSize / availableLines) + 1 :
                    (fourthSize / availableLines);

            if (firstSlots + secondSlots + thirdSlots + fourthSlots > 4) {
                if (firstSize + secondSize + thirdSize + fourthSize + 3 * SPACE_LINES > 4 * availableLines) {
                    //TODO equal distribution
                } else {
                    first.addSlots(firstSize);
                    this.addBlankSlots();
                    second.addSlots(secondSize);
                    this.addBlankSlots();
                    third.addSlots(thirdSize);
                    this.addBlankSlots();
                    fourth.addSlots(fourthSlots);
                }
            } else {
                int height = this.max(Math.ceil((double) firstSize / firstSlots),
                        Math.ceil((double) secondSize / secondSlots),
                        Math.ceil((double) thirdSize / thirdSlots), Math.ceil((double) fourthSize / fourthSlots));
                if (height <= (availableLines / 2)) {
                    height = (availableLines / 2) + 1;
                }

                for (int i = 0; i < firstSlots; i++) {
                    first.addSlots(height);
                    this.addBlankSlots();
                    remainTeam.addSlots(remainLines - SPACE_LINES);
                }
                for (int i = 0; i < secondSlots; i++) {
                    second.addSlots(height);
                    this.addBlankSlots();
                    remainTeam.addSlots(remainLines - SPACE_LINES);
                }
                for (int i = 0; i < thirdSlots; i++) {
                    third.addSlots(height);
                    this.addBlankSlots();
                    remainTeam.addSlots(remainLines - SPACE_LINES);
                }
                for (int i = 0; i < fourthSlots; i++) {
                    fourth.addSlots(height);
                    this.addBlankSlots();
                    remainTeam.addSlots(remainLines - SPACE_LINES);

                }
            }
        } else {                                    //It is impossible to fit the remaining team or is empty
            calcQuadruple(first, second, third, fourth);
        }
    }

    private int max(double... values) {
        int max = -1;
        for (double value : values) {
            if (value > max) {
                max = (int) value;
            }
        }
        return max;
    }

    /**
     * Real tablist entry, represents a player with prefix and color in a certain slot
     */
    public static class TablistEntry extends Tab.TabEntry<TablistEntry> {

        private final TablistablePlayer player;
        private final TablistableGroup team;
        private final String prefix;
        private final ChatColor chatColor;

        public TablistEntry(String slot, TablistablePlayer player, TablistableGroup team, String prefix,
                            ChatColor chatColor) {
            super(slot);
            this.player = player;
            this.team = team;
            this.prefix = Objects.requireNonNullElse(prefix, "");
            this.chatColor = Objects.requireNonNullElse(chatColor, ChatColor.WHITE);

        }

        public String getSlot() {
            return this.rank;
        }

        public TablistablePlayer getPlayer() {
            return player;
        }

        public TablistableGroup getTeam() {
            return team;
        }

        public String getPrefix() {
            return prefix;
        }

        public ChatColor getChatColor() {
            return chatColor;
        }

        // not supported
        @Override
        public void merge(TablistEntry entry) {
        }
    }

    private record Key(String teamRank, String groupRank) {

    }

    /**
     * The team tab with player entries sorted by name
     */
    private class TablistTeam extends Tab.TabEntry<TablistTeam> {

        private final ChatColor chatColor;
        private final boolean fillFake;
        private final LinkedList<TablistGroupType> types;
        private final Tab<Entry> headerTab = new Tab<>();
        private final Tab<Entry> playerTab = new Tab<>();
        private ArrayList<String> slots;

        public TablistTeam(String rank, ChatColor chatColor, boolean fillFake, LinkedList<TablistGroupType> types) {
            super(rank);
            this.chatColor = chatColor;
            this.fillFake = fillFake;
            this.types = types;
        }

        public boolean isFillFake() {
            return fillFake;
        }

        @Override
        public void merge(TablistTeam entry) {

        }

        public ChatColor getChatColor() {
            return chatColor;
        }

        public void addEntry(Entry entry) {
            this.playerTab.addEntry(entry);
            userRankKey.put(entry.getPlayer(), new Key(entry.getTeamRank(), entry.getRank()));
        }

        public boolean removeEntry(String rank, TablistablePlayer player) {
            for (Entry current : this.playerTab) {
                if (current.getRank().equals(rank) && current.getPlayer().equals(player)) {
                    boolean removed = this.playerTab.removeEntry(current);
                    if (removed) {
                        userRankKey.remove(player);
                    }
                    return removed;
                }
            }
            return false;
        }

        public void addHeader(Entry entry) {
            this.headerTab.addEntry(entry);
        }

        public boolean removeHeader(String rank) {
            for (Entry current : this.headerTab) {
                if (current.getRank().equals(rank)) {
                    return this.headerTab.removeEntry(current);
                }
            }
            return false;
        }

        public void fillSlots() {
            Iterator<Entry> headerIt = this.headerTab.iterator();
            Iterator<Entry> entryIt = this.playerTab.iterator();
            for (String slot : this.slots) {
                if (headerIt.hasNext()) {
                    tablist.addEntry(headerIt.next().toTablistEntry(slot));
                } else if (entryIt.hasNext()) {
                    Entry entry = entryIt.next();
                    tablist.addEntry(entry.toTablistEntry(slot));
                } else if (this.fillFake) {
                    tablist.addEntry(fakePlayers.get(slot).toTablistEntry(slot));
                }
            }
        }

        public ArrayList<String> getSlots() {
            return slots;
        }

        public void setSlots(ArrayList<String> slots) {
            this.slots = slots;
        }

        public Integer getSize() {
            return this.headerTab.size() + this.playerTab.size();
        }

        private void addSlots(Integer size) {
            int to = counter + size;
            for (; counter < to; counter++) {
                this.slots.add(counter < 10 ? "0" + counter : "" + counter);
            }
        }
    }

    /**
     *
     */
    protected class Entry extends Tab.TabEntry<Entry> {

        protected final TablistTeam tablistTeam;

        protected final TablistableGroup team;
        protected final String teamRank;
        protected final TablistablePlayer player;
        protected String prefix;
        protected ChatColor chatColor;

        public Entry(TablistTeam tablistTeam, TablistableGroup team, String teamRank, String rank, String prefix,
                     ChatColor chatColor,
                     TablistablePlayer player) {
            super(rank);
            this.tablistTeam = tablistTeam;
            this.team = team;
            this.teamRank = teamRank;
            this.prefix = prefix;
            this.chatColor = chatColor;
            this.player = player;
        }

        public TablistableGroup getTeam() {
            return team;
        }

        public String getTeamRank() {
            return teamRank;
        }

        @Override
        public void merge(Entry entry) {
            if (this.player.getTablistName().compareToIgnoreCase(entry.getRank()) > 0) {
                //insert before
                this.tablistTeam.playerTab.insertBefore(entry, this);
            } else {
                // insert after
                if (this.getNext() != null) {
                    this.tablistTeam.playerTab.insertBefore(entry, this.getNext());
                } else {
                    this.tablistTeam.playerTab.insertLast(entry);
                }
            }
        }

        public TablistablePlayer getPlayer() {
            return this.player;
        }

        public String getPrefix() {
            return prefix;
        }

        public TablistEntry toTablistEntry(Integer slot) {
            return this.toTablistEntry(slot < 10 ? "0" + slot : "" + slot);
        }

        public TablistEntry toTablistEntry(String slotRank) {
            return new TablistEntry(slotRank, player, team, prefix, chatColor);
        }
    }

    /**
     * The main tablist with team entries sorted by rank
     */
    protected class TeamTab extends Tab<TablistTeam> {

        public void addPlayerToEntry(TablistablePlayer player) {
            String rank = player.getFullRank(teamTypes);
            for (TablistTeam team : this) {
                if (team.getRank().equals(rank)) {
                    String groupRank = player.getFullRank(team.types);
                    String prefix;
                    if (player.getTablistPrefix() != null) {
                        prefix =
                                player.getFullPrefix(teamTypes) + player.getFullPrefix(team.types) + player.getTablistPrefix();
                    } else {
                        prefix = player.getFullPrefix(teamTypes) + player.getFullPrefix(team.types);
                    }
                    ChatColor chatColor = ChatColor.WHITE;
                    switch (colorType) {
                        case TEAM -> chatColor = team.getChatColor();
                        case LAST_GROUP -> {
                            if (team.types.size() == 0) {
                                break;
                            }
                            TablistableGroup last = player.getTablistGroup(team.types.getLast());
                            if (last.getTablistChatColor() == null) {
                                break;
                            }
                            chatColor = last.getTablistChatColor();
                        }
                        case FIRST_GROUP -> {
                            if (team.types.size() == 0) {
                                break;
                            }
                            TablistableGroup first = player.getTablistGroup(team.types.getFirst());
                            if (first.getTablistChatColor() == null) {
                                break;
                            }
                            chatColor = first.getTablistChatColor();
                        }
                        case WHITE -> chatColor = ChatColor.WHITE;
                    }
                    team.addEntry(new Entry(team, player.getTablistGroup(teamTypes.getFirst()), rank, groupRank, prefix,
                            chatColor, player));
                    return;
                }
            }
        }

        public boolean removePlayerFromEntry(TablistablePlayer player) {
            if (userRankKey.get(player) == null) {
                return false;
            }
            String rank = userRankKey.get(player).teamRank();
            if (rank == null) {
                return false;
            }
            for (TablistTeam team : this) {
                if (team.getRank().equals(rank)) {
                    String groupRank = userRankKey.get(player).groupRank();
                    if (groupRank == null) {
                        return false;
                    }
                    return team.removeEntry(rank, player);
                }
            }
            return false;
        }

        public void addHeaderToEntry(String teamRank, String headerRank, String name,
                                     ExPacketPlayOutTablist.Head head) {
            for (TablistTeam team : this) {
                if (team.getRank().equals(teamRank)) {
                    team.addHeader(new Entry(team, null, teamRank, headerRank, null, null, new FakeTablistPlayer(name,
                            head)));
                }
            }
        }

        public boolean removeHeaderFromEntry(String teamRank, String headerRank) {
            for (TablistTeam team : this) {
                if (team.getRank().equals(teamRank)) {
                    return team.removeHeader(headerRank);
                }
            }
            return false;
        }
    }
}