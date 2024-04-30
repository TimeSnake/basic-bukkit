/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.scoreboard.RenderType;

public interface Tablist {

  void setHeader(String header);

  void setFooter(String footer);

  boolean addEntry(TablistPlayer value);

  boolean removeEntry(TablistPlayer value);

  boolean reloadEntry(TablistPlayer value, boolean addIfNotExists);

  void updateEntryValue(TablistPlayer entry, Integer value);

  enum Type {
    DUMMY(RenderType.INTEGER, ObjectiveCriteria.DUMMY),
    HEALTH(RenderType.HEARTS, ObjectiveCriteria.HEALTH),
    XP(RenderType.INTEGER, ObjectiveCriteria.EXPERIENCE),
    LEVEL(RenderType.INTEGER, ObjectiveCriteria.LEVEL);


    private final RenderType renderType;
    private final ObjectiveCriteria packetType;

    Type(RenderType renderType, ObjectiveCriteria packetType) {
      this.renderType = renderType;
      this.packetType = packetType;
    }

    public RenderType getRenderType() {
      return renderType;
    }

    public ObjectiveCriteria getPacketType() {
      return packetType;
    }
  }

}
