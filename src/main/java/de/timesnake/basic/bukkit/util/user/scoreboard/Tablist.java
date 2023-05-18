/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import de.timesnake.library.packets.util.packet.ExPacketPlayOutScoreboardObjective;
import java.util.LinkedList;
import org.bukkit.scoreboard.RenderType;

public interface Tablist {

  void setHeader(String header);

  void setFooter(String footer);

  void addEntry(TablistablePlayer value);

  boolean removeEntry(TablistablePlayer value);

  void updateEntryValue(TablistablePlayer entry, Integer value);

  LinkedList<TablistablePlayer> getEntries();

  enum Type {
    DUMMY("dummy", RenderType.INTEGER, ExPacketPlayOutScoreboardObjective.ScoreboardType.INTEGER),
    HEARTS("hearts", RenderType.HEARTS, ExPacketPlayOutScoreboardObjective.ScoreboardType.HEARTS),
    HEALTH("health", RenderType.HEARTS, ExPacketPlayOutScoreboardObjective.ScoreboardType.HEARTS),
    XP("xp", RenderType.INTEGER, ExPacketPlayOutScoreboardObjective.ScoreboardType.INTEGER),
    LEVEL("level", RenderType.INTEGER, ExPacketPlayOutScoreboardObjective.ScoreboardType.INTEGER);


    private final String criteria;
    private final RenderType renderType;
    private final ExPacketPlayOutScoreboardObjective.ScoreboardType packetType;

    Type(String criteria, RenderType renderType,
        ExPacketPlayOutScoreboardObjective.ScoreboardType packetType) {
      this.criteria = criteria;
      this.renderType = renderType;
      this.packetType = packetType;
    }

    public String getCriteria() {
      return criteria;
    }

    public RenderType getRenderType() {
      return renderType;
    }

    public ExPacketPlayOutScoreboardObjective.ScoreboardType getPacketType() {
      return packetType;
    }
  }

}
