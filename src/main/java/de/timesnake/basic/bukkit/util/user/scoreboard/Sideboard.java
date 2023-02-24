/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import java.util.HashMap;

public interface Sideboard extends Board {

    String SPACER = "§8----------------";

    String getTitle();

    void setTitle(String title);

    void setScore(Integer line, String text);

    void removeScore(int line);

    HashMap<Integer, String> getScores();

    void clearScores();
}
