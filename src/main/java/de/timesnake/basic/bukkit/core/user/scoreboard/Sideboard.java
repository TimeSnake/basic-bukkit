/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard;

import de.timesnake.basic.bukkit.util.user.User;
import java.util.HashMap;

public class Sideboard extends Board implements de.timesnake.basic.bukkit.util.user.scoreboard.Sideboard {

    private final HashMap<Integer, String> scores = new HashMap<>();
    private String title;

    public Sideboard(String name, String title) {
        super(name);
        this.title = title;
    }

    /**
     * Gets the title of the {@link GroupTablist}
     *
     * @return the title
     */
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
        for (User user : super.wachtingUsers) {
            user.setSideboardTitle(title);
        }
    }

    /**
     * Sets the score of the {@link GroupTablist}
     *
     * @param line The line to set
     * @param text The text to set
     */
    @Override
    public void setScore(Integer line, String text) {
        this.scores.put(line, text);
        for (User user : wachtingUsers) {
            user.setSideboardScore(line, text);
        }
    }

    /**
     * Removes the score in line
     *
     * @param line The line to remove
     */
    @Override
    public void removeScore(int line) {
        for (User user : wachtingUsers) {
            user.removeSideboardScore(line);
        }
        this.scores.remove(line);
    }

    /**
     * Gets the scores of the {@link GroupTablist}
     *
     * @return The {@link HashMap} with key line, value text
     */
    @Override
    public HashMap<Integer, String> getScores() {
        return scores;
    }

    /**
     * Clears the scores of the {@link Sideboard}
     */
    @Override
    public void clearScores() {
        for (Integer line : this.scores.keySet()) {
            this.removeScore(line);
        }
    }

}
