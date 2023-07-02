/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.library.basic.util.Loggers;
import de.timesnake.library.packets.util.listener.PacketHandler;
import de.timesnake.library.packets.util.listener.PacketPlayOutListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreboardPacketManager implements PacketPlayOutListener,
    de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardPacketManager {

  private final Map<Player, Set<Packet<?>>> packets = new ConcurrentHashMap<>();

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
    Set<Packet<?>> packets = this.packets.get(receiver);

    if (packets != null && packets.contains(packet)) {
      packets.remove(packet);
      return packet;
    }
    return null;
  }

  @Override
  public void sendPacket(Collection<? extends User> receivers, Packet<?> packet) {
    for (User user : receivers) {
      sendPacket(user, packet);
    }
  }

  @Override
  public void sendPacket(User user, Packet<?> packet) {
    this.packets.computeIfAbsent(user.getPlayer(), v -> new HashSet<>()).add(packet);
    user.sendPacket(packet);
    Loggers.SCOREBOARD.info("Send packet '" + packet.getClass().getSimpleName() + "' to '" + user.getName() + "'");

  }
}
