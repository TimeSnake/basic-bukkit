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

import de.timesnake.library.packets.util.packet.ExPacketPlayOutScoreboardObjective;
import org.bukkit.scoreboard.RenderType;

import java.util.LinkedList;

public interface Tablist {

    void setHeader(String header);

    void setFooter(String footer);

    void addEntry(TablistablePlayer value);

    boolean removeEntry(TablistablePlayer value);

    void updateEntryValue(TablistablePlayer entry, Integer value);

    LinkedList<TablistablePlayer> getEntries();

    enum Type {
        DUMMY("dummy", RenderType.INTEGER, ExPacketPlayOutScoreboardObjective.ScoreboardType.INTEGER),
        HEARTS("hearts", RenderType.HEARTS, ExPacketPlayOutScoreboardObjective.ScoreboardType.HEARTS),
        HEALTH("health", RenderType.HEARTS, ExPacketPlayOutScoreboardObjective.ScoreboardType.HEARTS),
        XP("xp", RenderType.INTEGER, ExPacketPlayOutScoreboardObjective.ScoreboardType.INTEGER),
        LEVEL("level", RenderType.INTEGER, ExPacketPlayOutScoreboardObjective.ScoreboardType.INTEGER);


        private final String criteria;
        private final RenderType renderType;
        private final ExPacketPlayOutScoreboardObjective.ScoreboardType packetType;

        Type(String criteria, RenderType renderType, ExPacketPlayOutScoreboardObjective.ScoreboardType packetType) {
            this.criteria = criteria;
            this.renderType = renderType;
            this.packetType = packetType;
        }

        public String getCriteria() {
            return criteria;
        }

        public RenderType getRenderType() {
            return renderType;
        }

        public ExPacketPlayOutScoreboardObjective.ScoreboardType getPacketType() {
            return packetType;
        }
    }

}
