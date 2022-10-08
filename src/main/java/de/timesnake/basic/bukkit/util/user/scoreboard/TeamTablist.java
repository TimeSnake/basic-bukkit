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

import de.timesnake.library.packets.util.packet.ExPacketPlayOutTablist;
import org.bukkit.ChatColor;

public interface TeamTablist extends Tablist {

    static ExPacketPlayOutTablist.Head getHead(ChatColor color) {
        switch (color) {
            case BLACK:
                return ExPacketPlayOutTablist.Head.DOT_BLACK;
            case DARK_BLUE:
                return ExPacketPlayOutTablist.Head.DOT_DARK_BLUE;
            case DARK_GREEN:
                return ExPacketPlayOutTablist.Head.DOT_GREEN;
            case DARK_AQUA:
                return ExPacketPlayOutTablist.Head.DOT_DARK_AQUA;
            case DARK_RED:
                return ExPacketPlayOutTablist.Head.DOT_DARK_RED;
            case DARK_PURPLE:
                return ExPacketPlayOutTablist.Head.DOT_PURPLE;
            case GOLD:
                return ExPacketPlayOutTablist.Head.DOT_GOLD;
            case GRAY:
                return ExPacketPlayOutTablist.Head.DOT_LIGHT_GRAY;
            case DARK_GRAY:
                return ExPacketPlayOutTablist.Head.DOT_DARK_GRAY;
            case BLUE:
                return ExPacketPlayOutTablist.Head.DOT_BLUE;
            case GREEN:
                return ExPacketPlayOutTablist.Head.DOT_LIME;
            case AQUA:
                return ExPacketPlayOutTablist.Head.DOT_LIGHT_BLUE;
            case RED:
                return ExPacketPlayOutTablist.Head.DOT_RED;
            case LIGHT_PURPLE:
                return ExPacketPlayOutTablist.Head.DOT_PINK;
            case YELLOW:
                return ExPacketPlayOutTablist.Head.DOT_YELLOW;
            case WHITE:
                return ExPacketPlayOutTablist.Head.DOT_WHITE;

        }
        return ExPacketPlayOutTablist.Head.BLANK;
    }

    void addRemainEntry(TablistablePlayer player);

    void addTeamHeader(String teamRank, String headerRank, String name, ExPacketPlayOutTablist.Head head);

    void addTeamHeader(String teamRank, String headerRank, String name);

    void removeTeamHeader(String teamRank, String headerRank);

    enum ColorType {
        TEAM,
        FIRST_GROUP,
        LAST_GROUP,
        WHITE
    }

}
