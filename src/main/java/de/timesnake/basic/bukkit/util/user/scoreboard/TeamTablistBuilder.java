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

import java.util.Collection;
import java.util.LinkedList;

public class TeamTablistBuilder extends TablistBuilder {

    private TeamTablist.ColorType colorType = TeamTablist.ColorType.TEAM;
    private Collection<? extends TablistableGroup> teams;
    private TablistGroupType teamType;
    private TablistableRemainTeam remainTeam;
    private LinkedList<TablistGroupType> remainTeamGroupTypes;

    public TeamTablistBuilder(String name) {
        super(name);
    }

    @Override
    public TeamTablistBuilder type(Tablist.Type type) {
        return (TeamTablistBuilder) super.type(type);
    }

    @Override
    public TeamTablistBuilder groupTypes(LinkedList<TablistGroupType> groupTypes) {
        if (this.remainTeamGroupTypes == null) {
            this.remainTeamGroupTypes = groupTypes;
        }
        return (TeamTablistBuilder) super.groupTypes(groupTypes);
    }

    @Override
    public TeamTablistBuilder userJoin(TablistUserJoin userJoin) {
        return (TeamTablistBuilder) super.userJoin(userJoin);
    }

    @Override
    public TeamTablistBuilder userQuit(TablistUserQuit userQuit) {
        return (TeamTablistBuilder) super.userQuit(userQuit);
    }

    public TeamTablistBuilder colorType(TeamTablist.ColorType colorType) {
        this.colorType = colorType;
        return this;
    }

    public TeamTablistBuilder teams(Collection<? extends TablistableGroup> teams) {
        this.teams = teams;
        return this;
    }

    public TeamTablistBuilder teamType(TablistGroupType teamType) {
        this.teamType = teamType;
        return this;
    }

    public TeamTablistBuilder remainTeam(TablistableRemainTeam remainTeam) {
        this.remainTeam = remainTeam;
        return this;
    }

    public TeamTablistBuilder remainTeamGroupTypes(LinkedList<TablistGroupType> remainTeamGroupTypes) {
        this.remainTeamGroupTypes = remainTeamGroupTypes;
        return this;
    }

    public TeamTablist.ColorType getColorType() {
        return colorType;
    }

    public Collection<? extends TablistableGroup> getTeams() {
        return teams;
    }

    public TablistGroupType getTeamType() {
        return teamType;
    }

    public TablistableRemainTeam getRemainTeam() {
        return remainTeam;
    }

    public LinkedList<TablistGroupType> getRemainTeamGroupTypes() {
        return remainTeamGroupTypes;
    }
}
