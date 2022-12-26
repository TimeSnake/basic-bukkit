/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.util.server;

@FunctionalInterface
public interface LoopTask<Element> {

    void run(Element e);
}
