package de.timesnake.basic.bukkit.util.exceptions;

public class WorldNotExistException extends Exception {

    private String worldName;

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
