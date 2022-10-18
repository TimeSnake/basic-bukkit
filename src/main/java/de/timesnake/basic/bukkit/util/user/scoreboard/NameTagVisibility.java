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

import de.timesnake.library.packets.util.packet.ExPacketPlayOutTablistTeamCreation;

public enum NameTagVisibility {

    ALWAYS(ExPacketPlayOutTablistTeamCreation.NameTagVisibility.ALWAYS),
    NEVER(ExPacketPlayOutTablistTeamCreation.NameTagVisibility.NEVER),
    // not required due to explicit check methods
    //HIDE_FOR_OTHER_TEAMS(ExPacketPlayOutTablistTeamCreation.NameTagVisibility.HIDE_FOR_OTHER_TEAMS),
    //HIDE_FOR_OWN_TEAM(ExPacketPlayOutTablistTeamCreation.NameTagVisibility.HIDE_FOR_OWN_TEAM)
    ;

    private final ExPacketPlayOutTablistTeamCreation.NameTagVisibility packetTag;

    NameTagVisibility(ExPacketPlayOutTablistTeamCreation.NameTagVisibility packetTag) {
        this.packetTag = packetTag;
    }

    public ExPacketPlayOutTablistTeamCreation.NameTagVisibility getPacketTag() {
        return packetTag;
    }
}
