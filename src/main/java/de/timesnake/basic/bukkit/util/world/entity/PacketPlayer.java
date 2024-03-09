/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world.entity;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.core.world.PacketEntityManager;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.world.ExLocation;
import de.timesnake.library.packets.core.packet.out.entity.ClientboundSetEntityDataPacketBuilder;
import de.timesnake.library.packets.core.packet.out.scoreboard.ClientboundSetPlayerTeamPacketBuilder;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class PacketPlayer extends PacketEntity {

  protected final Player player;

  public PacketPlayer(Player player, ExLocation location) {
    super(location);
    this.player = player;
  }

  @Override
  public void spawnForUser(User user) {
    if (!player.getBukkitEntity().getChunk().isLoaded()) {
      return;
    }

    Server.getScoreboardManager().getPacketManager().sendPacket(user,
        ClientboundSetPlayerTeamPacketBuilder.ofAddPlayer(PacketEntityManager.FAKE_PLAYER_TEAM.getName(),
            player.getName().getString()));

    Server.getScoreboardManager().getPacketManager().sendPacket(user,
        ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of((ServerPlayer) player)));

    user.sendPacket(new ClientboundAddPlayerPacket(player));
    user.sendPacket(new ClientboundSetEntityDataPacketBuilder(player)
        .setAllFromEntity()
        .build());

    Server.runTaskLaterSynchrony(() -> Server.getScoreboardManager().getPacketManager()
            .sendPacket(user, new ClientboundPlayerInfoRemovePacket(List.of(player.getUUID()))),
        6, BasicBukkit.getPlugin());
  }

  @Override
  public void despawnForUser(User user) {
    Server.getScoreboardManager().getPacketManager().sendPacket(user,
        new ClientboundPlayerInfoRemovePacket(List.of(player.getUUID())));
    Server.getScoreboardManager().getPacketManager()
        .sendPacket(user, new ClientboundRemoveEntitiesPacket(player.getId()));
  }

  @Override
  public String getType() {
    return "player (" + this.getPlayer().getName().getString() + ")";
  }

  public Player getPlayer() {
    return player;
  }

}
