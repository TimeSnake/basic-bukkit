/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

public interface ScoreboardManager {

  GroupTablist registerGroupTablist(TablistBuilder builder);

  TeamTablist registerTeamTablist(TeamTablistBuilder builder);

  TeamTablist registerTagTeamTablist(TeamTablistBuilder builder);

  Tablist getTablist(String name);

  void removeTablist(String name);

  Sideboard registerSideboard(SideboardBuilder builder);

  ExSideboard registerExSideboard(ExSideboardBuilder builder);

  Sideboard getSideboard(String name);

  void removeSideboard(String name);

  Tablist getActiveTablist();

  void setActiveTablist(Tablist tablist);

  ScoreboardPacketManager getPacketManager();
}
