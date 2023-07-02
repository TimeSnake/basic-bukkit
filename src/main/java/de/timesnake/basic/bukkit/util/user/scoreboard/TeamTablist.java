/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import de.timesnake.library.packets.util.packet.TablistHead;

public interface TeamTablist extends Tablist {

  void addRemainEntry(TablistablePlayer player);

  void addTeamHeader(String teamRank, String headerRank, String name, TablistHead head);

  void addTeamHeader(String teamRank, String headerRank, String name);

  void removeTeamHeader(String teamRank, String headerRank);

  enum ColorType {
    TEAM,
    FIRST_GROUP,
    LAST_GROUP,
    WHITE
  }

}
