/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

public class DuplicateScoreboardException extends RuntimeException {

  public DuplicateScoreboardException(String name) {
    super("Duplicate scoreboard name '" + name + "'");
  }
}
