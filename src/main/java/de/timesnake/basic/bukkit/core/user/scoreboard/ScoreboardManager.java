/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.core.user.scoreboard.sideboard.ExSideboard;
import de.timesnake.basic.bukkit.core.user.scoreboard.sideboard.Sideboard;
import de.timesnake.basic.bukkit.core.user.scoreboard.tablist.Tablist;
import de.timesnake.basic.bukkit.core.user.scoreboard.tablist.Tablist2;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.group.DisplayGroup;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;
import de.timesnake.basic.bukkit.util.user.event.UserQuitEvent;
import de.timesnake.basic.bukkit.util.user.scoreboard.ExSideboardBuilder;
import de.timesnake.basic.bukkit.util.user.scoreboard.SideboardBuilder;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistPlayer;
import de.timesnake.library.network.NetworkVariables;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

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

    Tablist2 standard = this.registerTablist(new Tablist2.Builder(Server.getName())
        .groupTypes(DisplayGroup.MAIN_TABLIST_GROUPS));

    String networkName = Server.getNetwork().getVariables().getValue(NetworkVariables.NETWORK_NAME);
    if (networkName != null) {
      standard.setHeader("§6" + networkName);
    } else {
      standard.setHeader("§6Network");
    }

    standard.setFooter(de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardManager.getDefaultFooter());

    this.activeTablist = standard;

    Server.registerListener(this, BasicBukkit.getPlugin());

    this.logger.info("Loaded manager");
  }

  @Override
  public Tablist2 registerTablist(Tablist2.Builder builder) {
    Tablist2 tablist = new Tablist2(builder, this.packetManager);
    this.tablists.put(tablist.getName(), tablist);
    this.logger.info("Created tablist2 '{}'", tablist.getName());
    return tablist;
  }

  @Override
  public de.timesnake.basic.bukkit.util.user.scoreboard.Tablist getTablist(String name) {
    return this.tablists.get(name);
  }

  @Override
  public void removeTablist(String name) {
    de.timesnake.basic.bukkit.util.user.scoreboard.Tablist tablist = this.tablists.get(name);

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

  public void updatePlayerGroup(TablistPlayer player) {
    for (de.timesnake.basic.bukkit.util.user.scoreboard.Tablist tablist : this.tablists.values()) {
      // TODO change to reload
      if (tablist.removeEntry(player)) {
        tablist.addEntry(player);
      }
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onUserJoin(UserJoinEvent e) {
    for (de.timesnake.basic.bukkit.util.user.scoreboard.Tablist tablist : this.tablists.values()) {
      ((Tablist) tablist).onUserJoin(e);
    }

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

    for (de.timesnake.basic.bukkit.util.user.scoreboard.Tablist tablist : this.tablists.values()) {
      ((Tablist) tablist).onUserQuit(e);
    }

  }

  @EventHandler
  public void onEntityRegainHealth(EntityRegainHealthEvent e) {
    this.onEntityChangeHealth(e.getEntity());
  }

  @EventHandler
  public void onEntityDamage(EntityDamageEvent e) {
    this.onEntityChangeHealth(e.getEntity());
  }

  private void onEntityChangeHealth(Entity entity) {
    if (!(entity instanceof Player)) {
      return;
    }

    User user = Server.getUser(((Player) entity));

    for (de.timesnake.basic.bukkit.util.user.scoreboard.Tablist tablist : this.tablists.values()) {
      ((Tablist) tablist).onEntityChangeHealth(user);
    }
  }

  public de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardPacketManager getPacketManager() {
    return packetManager;
  }
}
