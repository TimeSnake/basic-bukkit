/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.util.user;

public class InvalidItemTypeException extends RuntimeException {

    public InvalidItemTypeException() {
        super();
    }

    public InvalidItemTypeException(String type) {
        super("type: " + type);
    }
}
