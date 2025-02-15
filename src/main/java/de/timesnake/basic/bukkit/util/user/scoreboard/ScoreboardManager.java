/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import de.timesnake.basic.bukkit.core.user.scoreboard.tablist.Tablist2;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.library.network.NetworkVariables;

public interface ScoreboardManager {

  static String getDefaultFooter() {
    String supportEmail = Server.getNetwork().getVariables().getValue(NetworkVariables.SUPPORT_EMAIL);
    if (supportEmail != null) {
      return "§7Server: " + Server.getName()
          + "\n§cSupport: /ticket or "
          + "\n" + supportEmail
          + "\n§8Powered by " + Server.CREATOR_NAME;
    } else {
      return "§7Server: " + Server.getName()
          + "\n§cSupport: /ticket"
          + "\n§8Powered by " + Server.CREATOR_NAME;
    }
  }

  Tablist2 registerTablist(Tablist2.Builder builder);

  Tablist getTablist(String name);

  void removeTablist(String name);

  Sideboard registerSideboard(SideboardBuilder builder);

  KeyedSideboard registerExSideboard(KeyedSideboardBuilder builder);

  Sideboard getSideboard(String name);

  void removeSideboard(String name);

  Tablist getActiveTablist();

  void setActiveTablist(Tablist tablist);

  ScoreboardPacketManager getPacketManager();
}
