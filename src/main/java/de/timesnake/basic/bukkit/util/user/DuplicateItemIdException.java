/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.util.user;

public class DuplicateItemIdException extends RuntimeException {

    public DuplicateItemIdException() {
        super();
    }

    public DuplicateItemIdException(String message) {
        super(message);
    }
}
