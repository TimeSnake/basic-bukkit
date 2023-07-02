/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import net.minecraft.world.scores.Team;

public enum NameTagVisibility {

  ALWAYS(Team.Visibility.ALWAYS),
  NEVER(Team.Visibility.NEVER),
  // not required due to explicit check methods
  //HIDE_FOR_OTHER_TEAMS(Team.Visibility),
  //HIDE_FOR_OWN_TEAM(Team.Visibility)
  ;

  private final Team.Visibility packetTag;

  NameTagVisibility(Team.Visibility packetTag) {
    this.packetTag = packetTag;
  }

  public Team.Visibility getPacketTag() {
    return packetTag;
  }
}
