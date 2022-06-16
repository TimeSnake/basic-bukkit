package de.timesnake.basic.bukkit.core.user.scoreboard;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.permission.Group;
import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;
import de.timesnake.basic.bukkit.util.user.event.UserQuitEvent;
import de.timesnake.basic.bukkit.util.user.scoreboard.*;
import de.timesnake.library.basic.util.chat.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class ScoreboardManager implements Listener, de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardManager {

    private final HashMap<String, de.timesnake.basic.bukkit.util.user.scoreboard.Tablist> tablists = new HashMap<>();
    private final HashMap<String, de.timesnake.basic.bukkit.util.user.scoreboard.Sideboard> sideboards =
            new HashMap<>();
    private final ScoreboardPacketManager packetManager;
    private de.timesnake.basic.bukkit.util.user.scoreboard.Tablist activeTablist;

    public ScoreboardManager() {
        this.packetManager = new ScoreboardPacketManager();

        LinkedList<TablistGroupType> types = new LinkedList<>();
        types.add(Group.getTablistType());
        GroupTablist standard = new GroupTablist(Server.getName(),
                de.timesnake.basic.bukkit.util.user.scoreboard.Tablist.Type.DUMMY,
                this.packetManager, (e, tablist) -> tablist.addEntry(e.getUser()),
                (e, tablist) -> tablist.removeEntry(e.getUser()), types);
        standard.setHeader("§6Time§2Snake§9.de");
        standard.setFooter("§7Server: " + Server.getName() + "\n§cSupport: /ticket or \n" + Server.SUPPORT_EMAIL);
        this.tablists.put(standard.getName(), standard);
        this.activeTablist = standard;

        Server.registerListener(this, BasicBukkit.getPlugin());

        Server.printText(Plugin.BUKKIT, "Loaded scoreboard handler", "Scoreboard");
    }

    @Override
    public de.timesnake.basic.bukkit.util.user.scoreboard.GroupTablist registerNewGroupTablist(String name,
                                                                                               de.timesnake.basic.bukkit.util.user.scoreboard.Tablist.Type type, LinkedList<TablistGroupType> types, TablistUserJoin userJoin, TablistUserQuit userQuit) {

        GroupTablist tablist = new GroupTablist(name, type, this.packetManager, userJoin, userQuit, types);
        this.tablists.put(name, tablist);
        return tablist;
    }

    @Override
    public de.timesnake.basic.bukkit.util.user.scoreboard.TeamTablist registerNewTeamTablist(String name,
                                                                                             Tablist.Type type,
                                                                                             TeamTablist.ColorType colorType, Collection<? extends TablistableGroup> teams, TablistUserJoin userJoin, TablistUserQuit userQuit, LinkedList<TablistGroupType> groupTypes, TablistGroupType teamType) {

        TeamTablist tablist = new TeamTablist(name, type, this.packetManager, colorType, teamType, teams, groupTypes,
                userJoin, userQuit);
        this.tablists.put(name, tablist);
        return tablist;
    }

    @Override
    public de.timesnake.basic.bukkit.util.user.scoreboard.TeamTablist registerNewTeamTablist(String name,
                                                                                             Tablist.Type type,
                                                                                             TeamTablist.ColorType colorType, Collection<? extends TablistableGroup> teams, TablistGroupType teamType, LinkedList<TablistGroupType> groupTypes, TablistableRemainTeam remainTeam, LinkedList<TablistGroupType> remainTeamGroupTypes, TablistUserJoin userJoin, TablistUserQuit userQuit) {

        TeamTablist tablist = new TeamTablist(name, type, colorType, this.packetManager, teamType, teams, groupTypes,
                remainTeam, remainTeamGroupTypes, userJoin, userQuit);
        this.tablists.put(name, tablist);
        return tablist;
    }

    @Override
    public de.timesnake.basic.bukkit.util.user.scoreboard.Tablist getTablist(String name) {
        return this.tablists.get(name);
    }

    @Override
    public void removeTablist(String name) {
        this.tablists.remove(name);
    }

    @Override
    public de.timesnake.basic.bukkit.util.user.scoreboard.Sideboard registerNewSideboard(String name, String title) {
        de.timesnake.basic.bukkit.util.user.scoreboard.Sideboard sideboard = new Sideboard(name, title);
        this.sideboards.put(name, sideboard);
        return sideboard;
    }


    @Override
    public de.timesnake.basic.bukkit.util.user.scoreboard.Sideboard getSideboard(String name) {
        return this.sideboards.get(name);
    }

    @Override
    public void removeSideboard(String name) {
        this.sideboards.remove(name);
    }

    @Override
    public de.timesnake.basic.bukkit.util.user.scoreboard.Tablist getActiveTablist() {
        return this.activeTablist;
    }

    @Override
    public void setActiveTablist(de.timesnake.basic.bukkit.util.user.scoreboard.Tablist tablist) {
        this.activeTablist = tablist;
    }

    public void updatePlayerGroup(TablistablePlayer player) {
        for (de.timesnake.basic.bukkit.util.user.scoreboard.Tablist tablist : this.tablists.values()) {
            tablist.removeEntry(player);
            tablist.addEntry(player);
        }
    }

    @EventHandler
    public void onUserJoin(UserJoinEvent e) {
        e.getUser().setTablist(this.activeTablist);
    }

    @EventHandler
    public void onUserQuit(UserQuitEvent e) {
        for (de.timesnake.basic.bukkit.util.user.scoreboard.Tablist tablist : this.tablists.values()) {
            ((Tablist) tablist).removeWatchingUser(e.getUser());
        }
        for (de.timesnake.basic.bukkit.util.user.scoreboard.Sideboard sideboard : this.sideboards.values()) {
            sideboard.removeWatchingUser(e.getUser());
        }
    }

    public de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardPacketManager getPacketManager() {
        return packetManager;
    }
}
