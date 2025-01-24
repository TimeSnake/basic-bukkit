/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.exception;

public class WorldNotExistException extends RuntimeException {

  private final String worldName;

  public WorldNotExistException(String worldName) {
    this.worldName = worldName;
  }

  public String getMessage() {
    return "World not exists";
  }

  public String getWorldName() {
    return worldName;
  }
}
