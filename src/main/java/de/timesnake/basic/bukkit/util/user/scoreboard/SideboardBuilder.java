/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import java.util.HashMap;
import java.util.Map;

public class SideboardBuilder {

  protected String name;
  protected String title;
  protected Map<Integer, String> scores = new HashMap<>();

  public SideboardBuilder name(String name) {
    this.name = name;
    return this;
  }

  public SideboardBuilder title(String title) {
    this.title = title;
    return this;
  }

  public SideboardBuilder setScore(int line, String text) {
    this.scores.put(line, text);
    return this;
  }

  public String getName() {
    return name;
  }

  public String getTitle() {
    return title;
  }

  public Map<Integer, String> getScores() {
    return scores;
  }
}
