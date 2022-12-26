/*
 * Copyright (C) 2022 timesnake
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
