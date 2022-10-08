/*
 * basic-bukkit.main
 * Copyright (C) 2022 timesnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; If not, see <http://www.gnu.org/licenses/>.
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
