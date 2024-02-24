/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.group.DisplayGroup;
import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;
import de.timesnake.basic.bukkit.util.user.event.UserQuitEvent;
import de.timesnake.basic.bukkit.util.user.scoreboard.ExSideboardBuilder;
import de.timesnake.basic.bukkit.util.user.scoreboard.SideboardBuilder;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistBuilder;
import de.timesnake.basic.bukkit.util.user.scoreboard.TeamTablistBuilder;
import de.timesnake.library.network.NetworkVariables;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class ScoreboardManager implements Listener,
    de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardManager {

  private final Logger logger = LogManager.getLogger("scoreboard.manager");

  private final HashMap<String, de.timesnake.basic.bukkit.util.user.scoreboard.Tablist> tablists = new HashMap<>();
  private final HashMap<String, de.timesnake.basic.bukkit.util.user.scoreboard.Sideboard> sideboards =
      new HashMap<>();
  private final ScoreboardPacketManager packetManager;
  private de.timesnake.basic.bukkit.util.user.scoreboard.Tablist activeTablist;

  public ScoreboardManager() {
    this.packetManager = new ScoreboardPacketManager();

    GroupTablist standard = this.registerGroupTablist(new TablistBuilder(Server.getName())
        .groupTypes(DisplayGroup.MAIN_TABLIST_GROUPS));

    String networkName = Server.getNetwork().getVariables().getValue(NetworkVariables.NETWORK_NAME);
    if (networkName != null) {
      standard.setHeader("§6" + networkName);
    } else {
      standard.setHeader("§6" + "Network");
    }

    standard.setFooter(de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardManager.getDefaultFooter());

    this.activeTablist = standard;

    Server.registerListener(this, BasicBukkit.getPlugin());

    this.logger.info("Loaded manager");
  }

  @Override
  public GroupTablist registerGroupTablist(TablistBuilder builder) {
    GroupTablist tablist = new GroupTablist(builder, this.packetManager);
    this.tablists.put(tablist.getName(), tablist);
    this.logger.info("Created group tablist '{}'", tablist.getName());
    return tablist;
  }

  @Override
  public TeamTablist registerTeamTablist(TeamTablistBuilder builder) {
    TeamTablist tablist = new TeamTablist(builder, this.packetManager);
    this.tablists.put(tablist.getName(), tablist);
    this.logger.info("Created team tablist '{}'", tablist.getName());
    return tablist;
  }

  @Override
  public TagTeamTablist registerTagTeamTablist(TeamTablistBuilder builder) {

    TagTeamTablist tablist = new TagTeamTablist(builder, this.packetManager);
    this.tablists.put(tablist.getName(), tablist);
    this.logger.info("Created tag-team tablist '{}'", tablist.getName());
    return tablist;
  }

  @Override
  public de.timesnake.basic.bukkit.util.user.scoreboard.Tablist getTablist(String name) {
    return this.tablists.get(name);
  }

  @Override
  public void removeTablist(String name) {
    this.tablists.remove(name);
    this.logger.info("Removed tablist '{}'", name);
  }

  @Override
  public Sideboard registerSideboard(SideboardBuilder builder) {
    Sideboard sideboard = new Sideboard(builder);
    this.sideboards.put(sideboard.getName(), sideboard);
    this.logger.info("Created sideboard '{}'", sideboard.getName());
    return sideboard;
  }

  @Override
  public ExSideboard registerExSideboard(ExSideboardBuilder builder) {
    ExSideboard sideboard = new ExSideboard(builder);
    this.sideboards.put(sideboard.getName(), sideboard);
    this.logger.info("Created sideboard '{}'", sideboard.getName());
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
    this.tablists.values().stream().filter(tablist -> tablist.getEntries().contains(player))
        .forEach(tablist -> {
          tablist.removeEntry(player);
          tablist.addEntry(player);
        });
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
