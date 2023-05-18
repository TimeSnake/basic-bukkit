/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.world;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.library.packets.core.packet.out.border.ExPacketPlayOutWorldBorder;
import de.timesnake.library.packets.util.listener.PacketHandler;
import de.timesnake.library.packets.util.listener.PacketPlayOutListener;
import de.timesnake.library.packets.util.packet.ExPacket;
import de.timesnake.library.packets.util.packet.ExPacketPlayOut;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class WorldBorderManager implements PacketPlayOutListener, Listener,
    de.timesnake.basic.bukkit.util.world.WorldBorderManager {

  private final HashMap<Player, Set<ExPacketPlayOutWorldBorder>> packetsByPlayer = new HashMap<>();

  private boolean customBorders = true;
  private boolean enderpearlThrouBorder = true;

  public WorldBorderManager() {
    Server.getPacketManager().addListener(this);
    Server.registerListener(this, BasicBukkit.getPlugin());
  }

  @PacketHandler(type = ExPacket.Type.PLAY_OUT_WORLD_BORDER, modify = true)
  public ExPacketPlayOut onPacketPlayOut(ExPacketPlayOut packet, Player receiver) {
    if (packet instanceof ExPacketPlayOutWorldBorder && this.customBorders) {
      Set<ExPacketPlayOutWorldBorder> packets = this.packetsByPlayer.get(receiver);
      if (packets == null || !packets.contains(packet)) {
        return null;
      }
    }
    return packet;
  }

  public void sendPacket(ExPacketPlayOutWorldBorder packet, User user) {
    Set<ExPacketPlayOutWorldBorder> packets = this.packetsByPlayer.get(user.getPlayer());

    if (packets == null) {
      packets = this.packetsByPlayer.computeIfAbsent(user.getPlayer(), p -> new HashSet<>());
    }
    packets.add(packet);
    user.sendPacket(packet);
  }

  public void sendPacket(ExPacketPlayOutWorldBorder packet, Collection<User> users) {
    for (User user : users) {
      this.sendPacket(packet, user);
    }
  }

  @EventHandler
  public void onPlayerTeleport(PlayerTeleportEvent e) {
    if (this.enderpearlThrouBorder) {
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
    return enderpearlThrouBorder;
  }

  @Override
  public void allowEnderpearlThrouBorder(boolean allowEnderpearl) {
    this.enderpearlThrouBorder = allowEnderpearl;
  }

}
