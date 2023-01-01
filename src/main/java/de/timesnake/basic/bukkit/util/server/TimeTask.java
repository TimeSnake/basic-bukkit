/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.server;

@FunctionalInterface
public interface TimeTask {

    void run(int time);
}
