/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard;


import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistablePlayer;
import de.timesnake.basic.bukkit.util.user.scoreboard.*;
import de.timesnake.library.basic.util.BuilderNotFullyInstantiatedException;
import de.timesnake.library.packets.util.packet.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamTablist extends Tablist implements de.timesnake.basic.bukkit.util.user.scoreboard.TeamTablist {

    public static final int SPACE_LINES = 2;
    public static final int MAX_ONE_COLUMN = 12 - SPACE_LINES;
    public static final int MAX_ONE_COLUMN_REMAIN = 14 - 2 * SPACE_LINES;

    protected final TablistGroupType teamType;

    protected final TeamsTab teamsTab;
    protected final ColorType colorType;
    protected final HashMap<String, Entry> fakePlayers = new HashMap<>();
    protected final HashMap<TablistablePlayer, Key> userRankKey = new HashMap<>();
    protected final Partitioner partitioner = new Partitioner();
    protected TeamTab remainTeam;
    protected ArrayList<Integer> blankSlots;
    protected Tab<SlotEntry> tablist;
    protected Integer counter = 0;
    protected NameTagVisibility nameTagVisibility = NameTagVisibility.ALWAYS;

    public TeamTablist(TeamTablistBuilder builder, ScoreboardPacketManager packetManager) {

        super(builder.getName(), builder.getType(), packetManager, builder.getUserJoin(), builder.getUserQuit());
        this.colorType = builder.getColorType();
        if (builder.getTeamType() == null) {
            throw new BuilderNotFullyInstantiatedException("team tablist: 'teamType' not instantiated");
        }
        this.teamType = builder.getTeamType();
        this.teamsTab = new TeamsTab();
        this.tablist = new Tab<>();

        if (builder.getTeams() == null) {
            throw new BuilderNotFullyInstantiatedException("team tablist: 'teams' not instantiated");
        }

        for (TablistableGroup group : builder.getTeams()) {
            this.teamsTab.addEntry(new TeamTab(group.getTablistRank(), group.getTablistChatColor(), true,
                    builder.getGroupTypes()));
        }

        for (int i = 0; i < 80; i++) {
            String rank = i < 10 ? "0" + i : "" + i;
            this.fakePlayers.put(rank, new Entry(null, null, null, rank, null, null,
                    new FakeTablistPlayer("§" + i / 10 + "§" + i % 10, ExPacketPlayOutTablist.Head.BLANK)));
        }

        if (builder.getRemainTeam() != null) {
            if (builder.getRemainTeamGroupTypes() == null) {
                throw new BuilderNotFullyInstantiatedException("team tablist: 'remainTeamGroupTypes' not instantiated");
            }
            this.remainTeam = new TeamTab("0", builder.getRemainTeam().getTablistChatColor(), true,
                    builder.getRemainTeamGroupTypes());
        }

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

        for (SlotEntry entry : this.tablist) {
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

        for (SlotEntry entry : this.tablist) {
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
        this.partitioner.calcStyle();

        for (TeamTab team : this.teamsTab) {
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
            SlotEntry oldEntry = oldIt.next();
            // not remove moved players
            if (!movedPlayers.remove(oldEntry.getPlayer())) {
                this.broadcastPacket(ExPacketPlayOutTablistPlayerRemove.wrap(oldEntry.getPlayer().getPlayer()));
            }

        }

        // add new entries and update team
        while (it.hasNext()) {
            SlotEntry entry = it.next();
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
        Plugin.SCOREBOARD.getLogger().info("tablist '" + this.name + "' try to add '" + player.getTablistName() + "'");


        if (!player.showInTablist()) {
            return;
        }

        // prevent duplicate
        if (this.userRankKey.containsKey(player)) {
            this.removeEntry(player, false);
        }

        this.teamsTab.addPlayerToEntry(player);

        if (update) {
            this.updateChanges();
            Plugin.SCOREBOARD.getLogger().fine("tablist '" + this.name + "' added '" + player.getTablistName() + "'");
        }
    }

    @Override
    public void addRemainEntry(TablistablePlayer player) {
        this.addRemainEntry(player, true);
    }

    private void addRemainEntry(TablistablePlayer player, boolean update) {
        Plugin.SCOREBOARD.getLogger().info("tablist '" + this.name + "' try to add remain '" + player.getTablistName() + "'");

        if (!player.showInTablist()) {
            return;
        }

        // prevent duplicate
        if (this.userRankKey.containsKey(player)) {
            this.removeEntry(player, false);
        }

        String groupRank = player.getFullRank(this.remainTeam.types);

        // build prefix
        String prefix = player.getFullPrefix(new LinkedList<>(List.of(this.teamType))) +
                player.getFullPrefix(this.remainTeam.types);
        if (player.getTablistPrefix() != null) {
            prefix += player.getTablistPrefix();
        }


        this.remainTeam.addEntry(new Entry(this.remainTeam, player.getTablistGroup(this.teamType),
                this.remainTeam.getRank(), groupRank, prefix, this.remainTeam.getChatColor(), player));

        if (update) {
            this.updateChanges();
            Plugin.SCOREBOARD.getLogger().info("tablist '" + this.name + "' added remain '" + player.getTablistName() + "'");
        }
    }

    @Override
    public boolean removeEntry(TablistablePlayer player) {
        Plugin.SCOREBOARD.getLogger().info("tablist '" + this.name + "' try to remove '" + player.getTablistName() + "'");
        boolean removed = this.removeEntry(player, true);
        if (removed) {
            Plugin.SCOREBOARD.getLogger().info("tablist '" + this.name + "' removed '" + player.getTablistName() + "'");
        }
        return removed;
    }

    private boolean removeEntry(TablistablePlayer player, boolean update) {
        boolean removed = this.teamsTab.removePlayerFromEntry(player);

        // remove from remain team
        if (!removed) {
            if (this.remainTeam != null) {
                if (userRankKey.get(player) != null) {
                    String rank = userRankKey.get(player).teamRank();
                    if (rank != null) {
                        removed |= this.remainTeam.removeEntry(player);
                    }
                }

            }
        }

        if (removed) {
            userRankKey.remove(player);
        }

        if (removed && update) {
            this.updateChanges();
        }
        return removed;
    }

    @Override
    public LinkedList<TablistablePlayer> getEntries() {
        LinkedList<TablistablePlayer> players = new LinkedList<>();
        for (TeamTab team : this.teamsTab) {
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
        this.teamsTab.addHeaderToEntry(teamRank, headerRank, name, head);
        this.updateChanges();
        Plugin.SCOREBOARD.getLogger().info("tablist '" + this.name + "' added team header to team '" + teamRank + "'");
    }

    @Override
    public void addTeamHeader(String teamRank, String headerRank, String name) {
        this.teamsTab.addHeaderToEntry(teamRank, headerRank, name, ExPacketPlayOutTablist.Head.BLANK);
        this.updateChanges();
        Plugin.SCOREBOARD.getLogger().info("tablist '" + this.name + "' added team header to team '" + teamRank + "'");
    }

    @Override
    public void removeTeamHeader(String teamRank, String headerRank) {
        this.teamsTab.removeHeaderFromEntry(teamRank, headerRank);
        this.updateChanges();
        Plugin.SCOREBOARD.getLogger().info("tablist '" + this.name + "' removed team header to team '" + teamRank + "'");
    }

    public TeamTablist setNameTagVisibility(NameTagVisibility visibility) {
        this.nameTagVisibility = visibility;
        this.updateChanges();
        return this;
    }

    private record Key(String teamRank, String groupRank) {

    }

    /**
     * Real tablist entry, represents a player with prefix and color in a certain slot
     */
    public static class SlotEntry extends TabEntry<SlotEntry> {

        private final TablistablePlayer player;
        private final TablistableGroup team;
        private final String prefix;
        private final ChatColor chatColor;

        public SlotEntry(String slot, TablistablePlayer player, TablistableGroup team, String prefix,
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

        @Deprecated
        @Override
        public void merge(SlotEntry entry) {
        }
    }

    /**
     * The main tablist with team entries sorted by rank
     */
    protected class TeamsTab extends Tab<TeamTab> {

        public void addPlayerToEntry(TablistablePlayer player) {
            String teamRank = player.getFullRank(new LinkedList<>(List.of(teamType)));
            for (TeamTab team : this) {
                if (team.getRank().equals(teamRank)) {
                    String groupRank = player.getFullRank(team.types);

                    // build prefix
                    String prefix = player.getFullPrefix(new LinkedList<>(List.of(teamType))) +
                            player.getFullPrefix(team.types);
                    if (player.getTablistPrefix() != null) {
                        prefix += player.getTablistPrefix();
                    }

                    // select color
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


                    team.addEntry(new Entry(team, player.getTablistGroup(teamType), teamRank, groupRank,
                            prefix, chatColor, player));
                    return;
                }
            }
        }

        public boolean removePlayerFromEntry(TablistablePlayer player) {
            if (userRankKey.get(player) == null) {
                return false;
            }
            String teamRank = userRankKey.get(player).teamRank();
            if (teamRank == null) {
                return false;
            }

            for (TeamTab team : this) {
                if (team.getRank().equals(teamRank)) {
                    String groupRank = userRankKey.get(player).groupRank();
                    if (groupRank == null) {
                        return false;
                    }
                    return team.removeEntry(player);
                }
            }
            return false;
        }

        public void addHeaderToEntry(String teamRank, String headerRank, String name,
                                     ExPacketPlayOutTablist.Head head) {
            for (TeamTab team : this) {
                if (team.getRank().equals(teamRank)) {
                    team.addHeader(new Entry(team, null, teamRank, headerRank, null, null,
                            new FakeTablistPlayer(name, head)));
                }
            }
        }

        public boolean removeHeaderFromEntry(String teamRank, String headerRank) {
            for (TeamTab team : this) {
                if (team.getRank().equals(teamRank)) {
                    return team.removeHeader(headerRank);
                }
            }
            return false;
        }
    }

    /**
     * The team tab with player entries sorted by name
     */
    private class TeamTab extends TabEntry<TeamTab> {

        final Tab<Entry> playerTab = new Tab<>();
        private final ChatColor chatColor;
        private final boolean fillFake;
        private final LinkedList<TablistGroupType> types;
        private final Tab<Entry> headerTab = new Tab<>();
        private ArrayList<String> slots;

        public TeamTab(String rank, ChatColor chatColor, boolean fillFake, LinkedList<TablistGroupType> types) {
            super(rank);
            this.chatColor = chatColor;
            this.fillFake = fillFake;
            this.types = types;
        }

        public boolean isFillFake() {
            return fillFake;
        }

        @Override
        public void merge(TeamTab entry) {

        }

        public ChatColor getChatColor() {
            return chatColor;
        }

        public void addEntry(Entry entry) {
            this.playerTab.addEntry(entry);
            userRankKey.put(entry.getPlayer(), new Key(entry.getTeamRank(), entry.getRank()));
        }

        public boolean removeEntry(TablistablePlayer player) {
            for (Entry entry : this.playerTab) {
                if (entry.getPlayer().equals(player)) {
                    return this.playerTab.removeEntry(entry);
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
     * Tablist entry, represents a player with prefix and color
     */
    protected class Entry extends TabEntry<Entry> {

        protected final TeamTab teamTab;

        protected final TablistableGroup team;
        protected final String teamRank;
        protected final TablistablePlayer player;
        protected String prefix;
        protected ChatColor chatColor;

        public Entry(TeamTab teamTab, TablistableGroup team, String teamRank, String rank, String prefix,
                     ChatColor chatColor,
                     TablistablePlayer player) {
            super(rank);
            this.teamTab = teamTab;
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
                this.teamTab.playerTab.insertBefore(entry, this);
            } else {
                // insert after
                if (this.getNext() != null) {
                    this.teamTab.playerTab.insertBefore(entry, this.getNext());
                } else {
                    this.teamTab.playerTab.insertLast(entry);
                }
            }
        }

        public TablistablePlayer getPlayer() {
            return this.player;
        }

        public String getPrefix() {
            return prefix;
        }

        public SlotEntry toTablistEntry(Integer slot) {
            return this.toTablistEntry(slot < 10 ? "0" + slot : "" + slot);
        }

        public SlotEntry toTablistEntry(String slotRank) {
            return new SlotEntry(slotRank, player, team, prefix, chatColor);
        }
    }

    private class Partitioner {

        private void calcTeamsOnly() {
            Iterator<TeamTab> it = TeamTablist.this.teamsTab.iterator();
            switch (TeamTablist.this.teamsTab.size()) {
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
                TeamTablist.this.blankSlots.add(counter);
            }
        }

        private void calcStyle() {
            TeamTablist.this.counter = 0;

            // reset team slots
            for (TeamTab team : teamsTab) {
                team.setSlots(new ArrayList<>());
            }

            // reset remain team slots
            if (TeamTablist.this.remainTeam != null) {
                TeamTablist.this.remainTeam.setSlots(new ArrayList<>());
            }

            if (TeamTablist.this.remainTeam == null) {
                calcTeamsOnly();
            } else {
                calcTeamsAndRemain();
            }
        }

        private void calcTeamsAndRemain() {
            Iterator<TeamTab> it = TeamTablist.this.teamsTab.iterator();
            switch (TeamTablist.this.teamsTab.size()) {
                case 1 -> calcSingleRemain(it.next(), TeamTablist.this.remainTeam);
                case 2 -> calcDoubleRemain(it.next(), it.next(), TeamTablist.this.remainTeam);
                case 3 -> calcTripleRemain(it.next(), it.next(), it.next(), TeamTablist.this.remainTeam);
                case 4 -> calcQuadrupleRemain(it.next(), it.next(), it.next(), it.next(), TeamTablist.this.remainTeam);
                default -> {
                }
            }
        }

        private void calcTriple(TeamTab first, TeamTab second, TeamTab third) {
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

        private void calcSingle(TeamTab first) {
            int size = first.getSize();
            first.addSlots(size);
        }

        private void calcDouble(TeamTab first, TeamTab second) {
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

        private void calcQuadruple(TeamTab first, TeamTab second, TeamTab third, TeamTab fourth) {
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

        private void calcTripleRemain(TeamTab first, TeamTab second, TeamTab third, TeamTab remain) {
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

        private void calcQuintuple(TeamTab first, TeamTab second, TeamTab third, TeamTab fourth,
                                   TeamTab fifth) {
            return;
        }

        private void calcSixtuple(TeamTab first, TeamTab second, TeamTab third, TeamTab fourth,
                                  TeamTab fifth, TeamTab sixth) {
            return;
        }

        private void calcSingleRemain(TeamTab first, TeamTab remain) {
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

        private void calcDoubleRemain(TeamTab first, TeamTab second, TeamTab remain) {
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

        private void calcQuadrupleRemain(TeamTab first, TeamTab second, TeamTab third, TeamTab fourth,
                                         TeamTab remain) {
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
    }
}