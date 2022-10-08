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
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashSet;
import java.util.Set;

public abstract class Board implements de.timesnake.basic.bukkit.util.user.scoreboard.Board {

    protected final String name;

    protected final Set<User> wachtingUsers = new HashSet<>();

    protected Board(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the {@link Board}, not the title
     *
     * @return the name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Adds a {@link User}
     * The {@link Scoreboard} of the {@link User} will be updated by a board change
     *
     * @param user The {@link User} to add
     */
    @Override
    public void addWatchingUser(User user) {
        this.wachtingUsers.add(user);
    }

    /**
     * Removes a {@link User}
     *
     * @param user The {@link User} to remove
     */
    @Override
    public void removeWatchingUser(User user) {
        this.wachtingUsers.remove(user);
    }
}
