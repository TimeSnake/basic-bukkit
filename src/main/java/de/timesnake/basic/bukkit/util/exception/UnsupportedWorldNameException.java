/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.exception;

public class UnsupportedWorldNameException extends Exception {

  private final String name;

  public UnsupportedWorldNameException(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
