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

package de.timesnake.basic.bukkit.util.world.entity;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.core.world.PacketEntityManager;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.world.ExLocation;
import de.timesnake.library.entities.entity.bukkit.ExPlayer;
import de.timesnake.library.packets.util.packet.*;

public class PacketPlayer extends PacketEntity {

    protected final ExPlayer player;

    public PacketPlayer(ExPlayer player, ExLocation location) {
        super(location);
        this.player = player;
    }

    @Override
    public void spawnForUser(User user) {
        if (!player.getChunk().isLoaded()) {
            return;
        }

        Server.getScoreboardManager().getPacketManager().sendPacket(user,
                ExPacketPlayOutPlayerInfo.wrap(ExPacketPlayOutPlayerInfo.Action.ADD_PLAYER, player));
        Server.getScoreboardManager().getPacketManager().sendPacket(user,
                ExPacketPlayOutTablistTeamPlayerAdd.wrap(PacketEntityManager.FAKE_PLAYER_TEAM_NAME, player.getName()));

        user.sendPacket(ExPacketPlayOutSpawnNamedEntity.wrap(player));
        user.sendPacket(ExPacketPlayOutEntityMetadata.wrap(player, ExPacketPlayOutEntityMetadata.DataType.UPDATE));

        Server.runTaskLaterSynchrony(() -> Server.getScoreboardManager().getPacketManager().sendPacket(user,
                        ExPacketPlayOutPlayerInfo.wrap(ExPacketPlayOutPlayerInfo.Action.REMOVE_PLAYER, player)), 6,
                BasicBukkit.getPlugin());
    }

    @Override
    public void despawnForUser(User user) {
        Server.getScoreboardManager().getPacketManager().sendPacket(user,
                ExPacketPlayOutPlayerInfo.wrap(ExPacketPlayOutPlayerInfo.Action.REMOVE_PLAYER, player));
        Server.getScoreboardManager().getPacketManager().sendPacket(user, ExPacketPlayOutEntityDestroy.wrap(player));
    }
}
