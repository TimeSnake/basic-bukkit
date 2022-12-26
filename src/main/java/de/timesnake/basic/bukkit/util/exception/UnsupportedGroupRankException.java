/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.util.exception;

public class UnsupportedGroupRankException extends Exception {

    private String name;
    private int rank;

    public UnsupportedGroupRankException(String name, int rank) {
        this.name = name;
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getMessage() {
        return "The group-rank " + this.rank + " of group " + this.name + " is unsupported";
    }
}
