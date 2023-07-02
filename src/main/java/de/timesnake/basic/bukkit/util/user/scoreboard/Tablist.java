/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.scoreboard.RenderType;

import java.util.LinkedList;

public interface Tablist {

  void setHeader(String header);

  void setFooter(String footer);

  void addEntry(TablistablePlayer value);

  boolean removeEntry(TablistablePlayer value);

  void updateEntryValue(TablistablePlayer entry, Integer value);

  LinkedList<TablistablePlayer> getEntries();

  @Deprecated
  enum Type {
    DUMMY("dummy", RenderType.INTEGER, ObjectiveCriteria.DUMMY),
    HEALTH("health", RenderType.HEARTS, ObjectiveCriteria.HEALTH),
    XP("xp", RenderType.INTEGER, ObjectiveCriteria.EXPERIENCE),
    LEVEL("level", RenderType.INTEGER, ObjectiveCriteria.LEVEL);


    private final String criteria;
    private final RenderType renderType;
    private final ObjectiveCriteria packetType;

    Type(String criteria, RenderType renderType, ObjectiveCriteria packetType) {
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

    public ObjectiveCriteria getPacketType() {
      return packetType;
    }
  }

}
