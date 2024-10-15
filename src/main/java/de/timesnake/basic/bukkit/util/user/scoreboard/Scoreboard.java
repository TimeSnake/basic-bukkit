/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import java.util.Set;

public interface Scoreboard {

  String getName();

  void addViewer(ScoreboardViewer viewer);

  void removeViewer(ScoreboardViewer viewer);

  Set<ScoreboardViewer> getViewers();

}
