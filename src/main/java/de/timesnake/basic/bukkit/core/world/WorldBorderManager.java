/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.world;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.library.packets.util.listener.PacketHandler;
import de.timesnake.library.packets.util.listener.PacketPlayOutListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class WorldBorderManager implements PacketPlayOutListener, Listener,
    de.timesnake.basic.bukkit.util.world.WorldBorderManager {

  private final Logger logger = LogManager.getLogger("world.border-manager");

  private final HashMap<Player, Set<Packet<?>>> packetsByPlayer = new HashMap<>();

  private boolean customBorders = true;
  private boolean allowEnderpearlThroughBorder = false;

  public WorldBorderManager() {
    Server.getPacketManager().addListener(this);
    Server.registerListener(this, BasicBukkit.getPlugin());
  }

  @PacketHandler(type = {
      ClientboundInitializeBorderPacket.class,
      ClientboundSetBorderLerpSizePacket.class,
      ClientboundSetBorderCenterPacket.class,
      ClientboundSetBorderLerpSizePacket.class,
      ClientboundSetBorderSizePacket.class,
      ClientboundSetBorderWarningDelayPacket.class,
      ClientboundSetBorderWarningDistancePacket.class,
  }, modify = true)
  public Packet<?> onPacketPlayOut(Packet<?> packet, Player receiver) {
    if (this.customBorders) {
      Set<Packet<?>> packets = this.packetsByPlayer.get(receiver);
      if (packets == null || !packets.contains(packet)) {
        return null;
      }
    }
    return packet;
  }

  public void sendPacket(Packet<?> packet, User user) {
    Set<Packet<?>> packets = this.packetsByPlayer.get(user.getPlayer());

    if (packets == null) {
      packets = this.packetsByPlayer.computeIfAbsent(user.getPlayer(), p -> new HashSet<>());
    }
    packets.add(packet);
    user.sendPacket(packet);
    this.logger.info("Sent world border packet '{}' to user '{}'", packet.getClass().getSimpleName(), user.getName());
  }

  public void sendPacket(Packet<?> packet, Collection<User> users) {
    for (User user : users) {
      this.sendPacket(packet, user);
    }
  }

  @EventHandler
  public void onPlayerTeleport(PlayerTeleportEvent e) {
    if (this.allowEnderpearlThroughBorder) {
      return;
    }

    if (!e.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
      return;
    }

    if (e.getTo().getWorld().getWorldBorder().isInside(e.getTo())) {
      return;
    }

    e.setCancelled(true);
  }

  @Override
  public boolean isCustomBorders() {
    return customBorders;
  }

  @Override
  public void setCustomBorders(boolean customBorders) {
    this.customBorders = customBorders;
  }

  @Override
  public boolean isEnderpearlThrouBorderAllowed() {
    return allowEnderpearlThroughBorder;
  }

  @Override
  public void allowEnderpearlThrouBorder(boolean allowEnderpearl) {
    this.allowEnderpearlThroughBorder = allowEnderpearl;
  }

}
