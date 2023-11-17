/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;
import de.timesnake.basic.bukkit.util.user.event.UserQuitEvent;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistUserJoin;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistUserQuit;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistablePlayer;
import de.timesnake.library.packets.core.packet.out.scoreboard.ClientboundSetDisplayObjectivePacketBuilder;
import de.timesnake.library.packets.core.packet.out.scoreboard.ClientboundSetObjectivePacketBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.server.ServerScoreboard;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public abstract class Tablist extends Scoreboard implements Listener,
    de.timesnake.basic.bukkit.util.user.scoreboard.Tablist {

  protected final ScoreboardPacketManager packetManager;

  protected final TablistUserJoin userJoin;
  protected final TablistUserQuit userQuit;

  protected final Type type;

  protected String header;
  protected String footer;

  public Tablist(String name, Type type, ScoreboardPacketManager packetManager,
                 TablistUserJoin userJoin, TablistUserQuit userQuit) {
    super(name);
    this.type = type;
    this.packetManager = packetManager;
    this.userJoin = userJoin;
    this.userQuit = userQuit;
    Server.registerListener(this, BasicBukkit.getPlugin());
  }

  @Override
  public void setHeader(String header) {
    this.header = Server.getTimeDownParser().parse2Legacy(header);
    this.updateHeaderFooter();
  }

  @Override
  public void setFooter(String footer) {
    this.footer = Server.getTimeDownParser().parse2Legacy(footer);
    this.updateHeaderFooter();
  }

  @Override
  public void addWatchingUser(User user) {
    super.addWatchingUser(user);
    this.load(user);
  }

  @Override
  public void removeWatchingUser(User user) {
    if (this.watchingUsers.contains(user)) {
      this.unload(user);
    }
    super.removeWatchingUser(user);
  }

  @Override
  public void updateEntryValue(TablistablePlayer entry, Integer value) {
    this.packetManager.sendPacket(this.watchingUsers,
        new ClientboundSetScorePacket(ServerScoreboard.Method.CHANGE, this.name, entry.getTablistName(), value));
  }

  protected void broadcastPacket(Packet<?> packet) {
    for (User user : this.watchingUsers) {
      this.packetManager.sendPacket(user, packet);
    }
  }

  protected void updateHeaderFooter() {
    this.packetManager.sendPacket(this.watchingUsers,
        new ClientboundTabListPacket(Component.literal(this.header), Component.literal(this.footer)));
  }

  @EventHandler
  public void onUserJoin(UserJoinEvent e) {
    this.userJoin.onUserJoin(e, this);

    User user = e.getUser();

    if (this.type.equals(Type.HEALTH)) {
      if (user.getPlayer().getGameMode().equals(GameMode.SURVIVAL) || user.getPlayer()
          .getGameMode().equals(GameMode.ADVENTURE)) {
        this.packetManager.sendPacket(this.watchingUsers,
            new ClientboundSetScorePacket(ServerScoreboard.Method.CHANGE, this.name,
                user.getName(), ((int) user.getHealth())));
      }

    }
  }

  @EventHandler
  public void onUserQuit(UserQuitEvent e) {
    this.userQuit.onUserQuit(e, this);
  }

  @EventHandler
  public void onEntityRegainHealth(EntityRegainHealthEvent e) {
    if (!this.type.equals(Type.HEALTH)) {
      return;
    }

    if (!(e.getEntity() instanceof Player)) {
      return;
    }

    User user = Server.getUser(((Player) e.getEntity()));

    Server.runTaskLaterSynchrony(() -> this.updateEntryValue(user,
        (int) Math.ceil(((Player) e.getEntity()).getHealth())), 1, BasicBukkit.getPlugin());

  }

  @EventHandler
  public void onEntityDamage(EntityDamageEvent e) {
    if (!this.type.equals(Type.HEALTH)) {
      return;
    }

    if (!(e.getEntity() instanceof Player)) {
      return;
    }

    User user = Server.getUser(((Player) e.getEntity()));

    Server.runTaskLaterSynchrony(() -> this.updateEntryValue(user,
        (int) Math.ceil(((Player) e.getEntity()).getHealth())), 1, BasicBukkit.getPlugin());
  }

  protected void load(User user) {
    this.packetManager.sendPacket(user, ClientboundSetObjectivePacketBuilder.ofRemove(this.name));

    this.packetManager.sendPacket(user, ClientboundSetObjectivePacketBuilder.ofAdd(this.name, this.name,
        this.type.getPacketType().getDefaultRenderType()));

    this.packetManager.sendPacket(user,
        ClientboundSetDisplayObjectivePacketBuilder.ofAdd(net.minecraft.world.scores.Scoreboard.DISPLAY_SLOT_LIST,
            this.name));

    this.packetManager.sendPacket(user, new ClientboundTabListPacket(Component.literal(this.header), Component.literal(this.footer)));

    for (User u : Server.getUsers()) {
      if (this.type.equals(Type.HEALTH)) {
        this.packetManager.sendPacket(user, new ClientboundSetScorePacket(ServerScoreboard.Method.CHANGE, this.name,
            user.getName(), ((int) u.getHealth())));
      }
    }
  }

  protected abstract void unload(User user);

}
