/*
 * timesnake.basic-bukkit.main
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

public abstract class TabEntry<E> {

    protected final String rank;
    protected E previous;
    protected E next;

    public TabEntry(String rank) {
        this.rank = rank;
    }

    public String getRank() {
        return rank;
    }

    public E getNext() {
        return next;
    }

    public void setNext(E next) {
        this.next = next;
    }

    public E getPrevious() {
        return previous;
    }

    public void setPrevious(E previous) {
        this.previous = previous;
    }

    public abstract void merge(E entry);

}
