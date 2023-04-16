/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.scoreboard.SideboardBuilder;
import de.timesnake.library.basic.util.Loggers;
import java.util.HashMap;

public class Sideboard extends Board implements
        de.timesnake.basic.bukkit.util.user.scoreboard.Sideboard {

    private final HashMap<Integer, String> scores = new HashMap<>();
    private String title;

    public Sideboard(SideboardBuilder builder) {
        super(builder.getName());
        this.title = builder.getTitle();

        scores.putAll(builder.getScores());
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
        this.title = Server.getTimeDownParser().parse2Legacy(title);
        for (User user : super.watchingUsers) {
            user.setSideboardTitle(this.title);
        }
        Loggers.SCOREBOARD.info("sideboard '" + this.name + "' set title '"
                + this.title + "'");
    }

    /**
     * Sets the score of the {@link GroupTablist}
     *
     * @param line The line to set
     * @param text The text to set
     */
    @Override
    public void setScore(Integer line, String text) {
        text = Server.getTimeDownParser().parse2Legacy(text);
        this.scores.put(line, text);
        for (User user : watchingUsers) {
            user.setSideboardScore(line, text);
        }
        Loggers.SCOREBOARD.info("sideboard '" + this.name + "' set score "
                + line + " '" + text + "'");
    }

    /**
     * Removes the score in line
     *
     * @param line The line to remove
     */
    @Override
    public void removeScore(int line) {
        for (User user : watchingUsers) {
            user.removeSideboardScore(line);
        }
        this.scores.remove(line);
        Loggers.SCOREBOARD.info("sideboard '" + this.name + "' remove score " + line);
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
        Loggers.SCOREBOARD.info("sideboard '" + this.name + "' clear scores");
    }

}
