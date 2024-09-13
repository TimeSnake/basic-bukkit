/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world.entity;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.world.ExLocation;
import de.timesnake.library.packets.core.packet.out.entity.ClientboundSetEntityDataPacketBuilder;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;
import java.util.List;

public class PacketPlayer extends PacketEntity {

  protected final ServerPlayer player;

  public PacketPlayer(ServerPlayer player, ExLocation location) {
    super(location);
    this.player = player;
  }

  @Override
  public void spawnForUser(User user) {
    if (!player.getBukkitEntity().getChunk().isLoaded()) {
      return;
    }

    Server.getScoreboardManager().getPacketManager().sendPacket(user, new ClientboundPlayerInfoUpdatePacket(
        EnumSet.of(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER,
            ClientboundPlayerInfoUpdatePacket.Action.INITIALIZE_CHAT,
            ClientboundPlayerInfoUpdatePacket.Action.UPDATE_GAME_MODE,
            ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED,
            ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY,
            ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME),
        new ClientboundPlayerInfoUpdatePacket.Entry(player.getUUID(), player.gameProfile, false, 0,
            player.gameMode.getGameModeForPlayer(), player.getTabListDisplayName(), null)));

    user.sendPacket(new ClientboundAddEntityPacket(this.player, 0, this.player.getOnPos()));
    user.sendPacket(new ClientboundSetEntityDataPacketBuilder(player).setAllFromEntity().build());
  }

  @Override
  public void despawnForUser(User user) {
    Server.getScoreboardManager().getPacketManager().sendPacket(user,
        new ClientboundPlayerInfoRemovePacket(List.of(player.getUUID())));
    Server.getScoreboardManager().getPacketManager().sendPacket(user,
        new ClientboundRemoveEntitiesPacket(player.getId()));
  }

  @Override
  public String getType() {
    return "player (" + this.getPlayer().getName().getString() + ")";
  }

  public Player getPlayer() {
    return player;
  }

}
