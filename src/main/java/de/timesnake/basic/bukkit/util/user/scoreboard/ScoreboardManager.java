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

package de.timesnake.basic.bukkit.util.user.scoreboard;

import java.util.Collection;
import java.util.LinkedList;

public interface ScoreboardManager {

    GroupTablist registerNewGroupTablist(String name, Tablist.Type type, LinkedList<TablistGroupType> types,
                                         TablistUserJoin userJoin, TablistUserQuit userQuit);

    TeamTablist registerNewTeamTablist(String name, Tablist.Type type, TeamTablist.ColorType colorType,
                                       Collection<? extends TablistableGroup> teamsList, TablistUserJoin userJoin,
                                       TablistUserQuit userQuit, LinkedList<TablistGroupType> groupTypes,
                                       TablistGroupType teamType);

    TeamTablist
    registerNewTagTeamTablist(String name, Tablist.Type type, TeamTablist.ColorType colorType,
                              Collection<? extends TagTablistableGroup> teams,
                              TablistUserJoin userJoin, TablistUserQuit userQuit,
                              LinkedList<TablistGroupType> groupTypes, TablistGroupType teamType);

    TeamTablist registerNewTeamTablist(String name, Tablist.Type type, TeamTablist.ColorType colorType,
                                       Collection<? extends TablistableGroup> teamsList, TablistGroupType teamType,
                                       LinkedList<TablistGroupType> groupTypes, TablistableRemainTeam remainTeam,
                                       LinkedList<TablistGroupType> remainTeamGroupTypes, TablistUserJoin userJoin,
                                       TablistUserQuit userQuit);

    TeamTablist
    registerNewTagTeamTablist(String name, Tablist.Type type, TeamTablist.ColorType colorType,
                              Collection<? extends TagTablistableGroup> teams, TablistGroupType teamType,
                              LinkedList<TablistGroupType> groupTypes, TagTablistableRemainTeam remainTeam,
                              LinkedList<TablistGroupType> remainTeamGroupTypes, TablistUserJoin userJoin,
                              TablistUserQuit userQuit);

    Tablist getTablist(String name);

    void removeTablist(String name);

    Sideboard registerNewSideboard(String name, String title);

    Sideboard getSideboard(String name);

    void removeSideboard(String name);

    Tablist getActiveTablist();

    void setActiveTablist(Tablist tablist);

    ScoreboardPacketManager getPacketManager();
}
