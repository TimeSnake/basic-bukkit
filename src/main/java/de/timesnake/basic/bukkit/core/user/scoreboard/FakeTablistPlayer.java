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

import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroupType;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistableGroup;
import de.timesnake.library.packets.util.packet.ExPacketPlayOutTablist;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.entity.Player;

public class FakeTablistPlayer implements TablistablePlayer {

    private final EntityPlayer entityPlayer;

    public FakeTablistPlayer(String name, ExPacketPlayOutTablist.Head head) {
        this.entityPlayer = ExPacketPlayOutTablist.newEntry(name, head);
    }

    @Override
    public String getTablistName() {
        return this.getPlayer().getName();
    }

    @Deprecated
    @Override
    public TablistableGroup getTablistGroup(TablistGroupType type) {
        return null;
    }

    @Override
    public Player getPlayer() {
        return this.entityPlayer.getBukkitEntity().getPlayer();
    }

    @Override
    public boolean showInTablist() {
        return true;
    }

    @Override
    public String getTablistPrefix() {
        return null;
    }
}
