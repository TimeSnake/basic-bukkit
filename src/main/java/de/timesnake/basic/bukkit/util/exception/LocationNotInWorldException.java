/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.util.exception;

import org.bukkit.Location;

public class LocationNotInWorldException extends Exception {

    private final Location location;

    public LocationNotInWorldException(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public String getMessage() {
        return "You must be in the right world";
    }
}
