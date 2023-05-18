/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.inventory;

public class InvalidItemTypeException extends RuntimeException {

  public InvalidItemTypeException() {
    super();
  }

  public InvalidItemTypeException(String type) {
    super("type: " + type);
  }
}
