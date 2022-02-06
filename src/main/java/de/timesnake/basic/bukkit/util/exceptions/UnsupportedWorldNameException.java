package de.timesnake.basic.bukkit.util.exceptions;

public class UnsupportedWorldNameException extends Exception {

    private final String name;

    public UnsupportedWorldNameException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
