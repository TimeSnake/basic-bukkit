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

package de.timesnake.basic.bukkit.util.user.scoreboard;

import java.util.LinkedList;

public class TablistBuilder {

    private final String name;
    private Tablist.Type type = Tablist.Type.DUMMY;
    private LinkedList<TablistGroupType> groupTypes;
    private TablistUserJoin userJoin = (e, tablist) -> tablist.addEntry(e.getUser());
    private TablistUserQuit userQuit = (e, tablist) -> tablist.removeEntry(e.getUser());

    public TablistBuilder(String name) {
        this.name = name;
    }

    public TablistBuilder type(Tablist.Type type) {
        this.type = type;
        return this;
    }

    public TablistBuilder groupTypes(LinkedList<TablistGroupType> groupTypes) {
        this.groupTypes = groupTypes;
        return this;
    }

    public TablistBuilder userJoin(TablistUserJoin userJoin) {
        this.userJoin = userJoin;
        return this;
    }

    public TablistBuilder userQuit(TablistUserQuit userQuit) {
        this.userQuit = userQuit;
        return this;
    }

    public String getName() {
        return name;
    }

    public Tablist.Type getType() {
        return type;
    }

    public LinkedList<TablistGroupType> getGroupTypes() {
        return groupTypes;
    }

    public TablistUserJoin getUserJoin() {
        return userJoin;
    }

    public TablistUserQuit getUserQuit() {
        return userQuit;
    }
}
