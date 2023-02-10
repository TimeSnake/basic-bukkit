/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.inventory;

public class DuplicateItemIdException extends RuntimeException {

    public DuplicateItemIdException() {
        super();
    }

    public DuplicateItemIdException(String message) {
        super(message);
    }
}
