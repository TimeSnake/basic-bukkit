/*
 * timesnake.basic-bukkit.main
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

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.DisplayGroup;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;
import de.timesnake.basic.bukkit.util.user.event.UserQuitEvent;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistBuilder;
import de.timesnake.basic.bukkit.util.user.scoreboard.TeamTablistBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class ScoreboardManager implements Listener, de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardManager {

    private final HashMap<String, de.timesnake.basic.bukkit.util.user.scoreboard.Tablist> tablists = new HashMap<>();
    private final HashMap<String, de.timesnake.basic.bukkit.util.user.scoreboard.Sideboard> sideboards =
            new HashMap<>();
    private final ScoreboardPacketManager packetManager;
    private de.timesnake.basic.bukkit.util.user.scoreboard.Tablist activeTablist;

    public ScoreboardManager() {
        this.packetManager = new ScoreboardPacketManager();

        GroupTablist standard = this.registerGroupTablist(new TablistBuilder(Server.getName())
                .groupTypes(DisplayGroup.MAIN_TABLIST_GROUPS));
        standard.setHeader("§6Time§2Snake§9.de");
        standard.setFooter("§7Server: " + Server.getName() + "\n§cSupport: /ticket or \n" + Server.SUPPORT_EMAIL);

        this.activeTablist = standard;

        Server.registerListener(this, BasicBukkit.getPlugin());

        Plugin.SCOREBOARD.getLogger().info("Loaded manager");
    }

    @Override
    public GroupTablist registerGroupTablist(TablistBuilder builder) {
        GroupTablist tablist = new GroupTablist(builder, this.packetManager);
        this.tablists.put(tablist.getName(), tablist);
        Plugin.SCOREBOARD.getLogger().info("Created group tablist '" + tablist.getName() + "'");
        return tablist;
    }

    @Override
    public TeamTablist registerTeamTablist(TeamTablistBuilder builder) {
        TeamTablist tablist = new TeamTablist(builder, this.packetManager);
        this.tablists.put(tablist.getName(), tablist);
        Plugin.SCOREBOARD.getLogger().info("Created team tablist '" + tablist.getName() + "'");
        return tablist;
    }

    @Override
    public TagTeamTablist registerTagTeamTablist(TeamTablistBuilder builder) {

        TagTeamTablist tablist = new TagTeamTablist(builder, this.packetManager);
        this.tablists.put(tablist.getName(), tablist);
        Plugin.SCOREBOARD.getLogger().info("Created tag-team tablist '" + tablist.getName() + "'");
        return tablist;
    }

    @Override
    public de.timesnake.basic.bukkit.util.user.scoreboard.Tablist getTablist(String name) {
        return this.tablists.get(name);
    }

    @Override
    public void removeTablist(String name) {
        this.tablists.remove(name);
        Plugin.SCOREBOARD.getLogger().info("Removed tablist '" + name + "'");
    }

    @Override
    public de.timesnake.basic.bukkit.util.user.scoreboard.Sideboard registerSideboard(String name, String title) {
        de.timesnake.basic.bukkit.util.user.scoreboard.Sideboard sideboard = new Sideboard(name, title);
        this.sideboards.put(name, sideboard);
        Plugin.SCOREBOARD.getLogger().info("Created sideboard '" + name + "'");
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
