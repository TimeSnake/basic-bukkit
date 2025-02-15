/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import de.timesnake.basic.bukkit.util.user.scoreboard.KeyedSideboard.LineId;

import java.util.LinkedList;

public class KeyedSideboardBuilder extends SideboardBuilder {

  protected LinkedList<LineId<?>> lineIds = new LinkedList<>();
  protected boolean lineSpacer = false;

  @Override
  public KeyedSideboardBuilder name(String name) {
    return (KeyedSideboardBuilder) super.name(name);
  }

  @Override
  public KeyedSideboardBuilder title(String title) {
    return (KeyedSideboardBuilder) super.title(title);
  }

  @Override
  public KeyedSideboardBuilder setScore(int line, String text) {
    return (KeyedSideboardBuilder) super.setScore(line, text);
  }

  public KeyedSideboardBuilder addLine(LineId<?> lineId) {
    this.lineIds.addLast(lineId);
    return this;
  }

  public KeyedSideboardBuilder lineSpacer() {
    this.lineSpacer = true;
    return this;
  }

  public LinkedList<LineId<?>> getLineIds() {
    return lineIds;
  }

  public boolean isLineSpacer() {
    return lineSpacer;
  }
}
