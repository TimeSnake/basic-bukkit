package de.timesnake.basic.bukkit.util.exceptions;

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
