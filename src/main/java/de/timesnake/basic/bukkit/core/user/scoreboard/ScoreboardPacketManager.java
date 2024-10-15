/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardViewer;
import de.timesnake.library.packets.util.listener.PacketHandler;
import de.timesnake.library.packets.util.listener.PacketPlayOutListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreboardPacketManager implements PacketPlayOutListener,
    de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardPacketManager {

  private final Logger logger = LogManager.getLogger("scoreboard.manager.packet");

  private final Map<UUID, Set<Packet<?>>> packets = new ConcurrentHashMap<>();

  public ScoreboardPacketManager() {
    Server.getPacketManager().addListener(this);
  }

  @PacketHandler(type = {
      ClientboundSetPlayerTeamPacket.class,
      ClientboundSetDisplayObjectivePacket.class,
      ClientboundSetObjectivePacket.class,
      ClientboundPlayerInfoUpdatePacket.class,
      ClientboundPlayerInfoRemovePacket.class
  }, modify = true)
  public Packet<?> onPacketPlayOut(Packet<?> packet, Player receiver) {
    Set<Packet<?>> packets = this.packets.get(receiver.getUniqueId());

    if (packets != null && packets.contains(packet)) {
      packets.remove(packet);
      return packet;
    } else {
      if (packet instanceof ClientboundPlayerInfoUpdatePacket infoUpdatePacket) {
        if (!infoUpdatePacket.actions().contains(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER)) {
          return packet;
        }
      }
      this.logger.info("Blocked packet '{}' to '{}'", packet.getClass().getSimpleName(), receiver.getName());
      return null;
    }
  }

  @Override
  public void sendPacket(Collection<? extends ScoreboardViewer> viewers, Packet<?> packet) {
    for (ScoreboardViewer viewer : viewers) {
      sendPacket(viewer, packet);
    }
  }

  @Override
  public void sendPacket(ScoreboardViewer viewer, Packet<?> packet) {
    this.packets.computeIfAbsent(viewer.getUniqueId(), v -> new HashSet<>()).add(packet);
    viewer.sendPacket(packet);
    this.logger.info("Send packet '{}' to '{}'", packet.getClass().getSimpleName(), viewer.getUniqueId());
  }
}
