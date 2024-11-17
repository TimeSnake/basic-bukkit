/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard.tablist;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.core.user.scoreboard.Scoreboard;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;
import de.timesnake.basic.bukkit.util.user.event.UserQuitEvent;
import de.timesnake.basic.bukkit.util.user.scoreboard.*;
import de.timesnake.library.packets.core.packet.out.scoreboard.ClientboundSetDisplayObjectivePacketBuilder;
import de.timesnake.library.packets.core.packet.out.scoreboard.ClientboundSetObjectivePacketBuilder;
import io.papermc.paper.adventure.AdventureComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.numbers.BlankFormat;
import net.minecraft.network.chat.numbers.NumberFormat;
import net.minecraft.network.chat.numbers.StyledFormat;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.world.scores.DisplaySlot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.GameMode;
import org.bukkit.event.Listener;

import java.util.Optional;
import java.util.function.Function;

public abstract class Tablist extends Scoreboard implements Listener,
    de.timesnake.basic.bukkit.util.user.scoreboard.Tablist {

  protected final Logger logger = LogManager.getLogger("scoreboard.tablist");

  protected final ScoreboardPacketManager packetManager;

  protected final TablistUserJoin userJoin;
  protected final TablistUserQuit userQuit;

  protected final Type type;
  protected final NumberFormat format;

  protected String header;
  protected String footer;

  public Tablist(String name, Type type, ScoreboardPacketManager packetManager,
                 TablistUserJoin userJoin, TablistUserQuit userQuit) {
    super(name, packetManager);
    this.type = type;
    this.packetManager = packetManager;
    this.userJoin = userJoin;
    this.userQuit = userQuit;

    if (type == Type.BLANK) {
      this.format = BlankFormat.INSTANCE;
    } else {
      this.format = new StyledFormat(Style.EMPTY.withColor(ChatFormatting.RED));
    }
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
  public void addViewer(ScoreboardViewer viewer) {
    super.addViewer(viewer);
    this.load(viewer);
  }

  @Override
  public void removeViewer(ScoreboardViewer viewer) {
    if (this.viewers.contains(viewer)) {
      this.unload(viewer);
    }
    super.removeViewer(viewer);
  }

  @Override
  public void updateEntryValue(TablistPlayer entry, Integer value) {
    if (this.type == Type.BLANK) {
      this.logger.warn("Score update in tablist with type blank for entry '{}'", entry.getName());
      return;
    }
    this.sendPacket(new ClientboundSetScorePacket(entry.getName(), this.name, value,
        Optional.of(new AdventureComponent(Server.getTimeDownParser().parse2Component(entry.getTablistName()))),
        Optional.of(this.format)));
  }

  protected void broadcastPacket(Packet<?> packet) {
    this.sendPacket(packet);
  }

  protected void broadcastPacket(Function<ScoreboardViewer, Packet<?>> packetFunction) {
    for (ScoreboardViewer viewer : this.viewers) {
      this.sendPacket(viewer, packetFunction.apply(viewer));
    }
  }

  protected void updateHeaderFooter() {
    this.sendPacket(new ClientboundTabListPacket(Component.nullToEmpty(this.header),
        Component.nullToEmpty(this.footer)));
  }

  public void onUserJoin(UserJoinEvent e) {
    this.userJoin.onUserJoin(e, this);

    User user = e.getUser();

    if (this.type.equals(Type.HEALTH)) {
      if (user.getPlayer().getGameMode().equals(GameMode.SURVIVAL)
          || user.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
        this.sendPacket(new ClientboundSetScorePacket(user.getName(), this.name,
            ((int) user.getHealth()),
            Optional.of(new AdventureComponent(Server.getTimeDownParser().parse2Component(user.getTablistName()))),
            Optional.of(this.format)));
      }
    }
  }

  public void onUserQuit(UserQuitEvent e) {
    this.userQuit.onUserQuit(e, this);
  }

  public void onEntityChangeHealth(User user) {
    if (!this.type.equals(Type.HEALTH)) {
      return;
    }

    Server.runTaskLaterSynchrony(() -> this.updateEntryValue(user, (int) Math.ceil(user.getHealth())),
        1, BasicBukkit.getPlugin());
  }

  protected void load(ScoreboardViewer viewer) {
    this.sendPacket(viewer, ClientboundSetObjectivePacketBuilder.ofRemove(this.name));

    this.sendPacket(viewer, ClientboundSetObjectivePacketBuilder.ofAdd(this.name, this.name, this.type.getPacketType(),
        this.type.getPacketType().getDefaultRenderType()));

    this.sendPacket(viewer, ClientboundSetDisplayObjectivePacketBuilder.ofAdd(DisplaySlot.LIST, this.name));

    this.sendPacket(viewer, new ClientboundTabListPacket(Component.literal(this.header),
        Component.literal(this.footer)));

    for (User user : Server.getUsers()) {
      if (this.type.equals(Type.HEALTH)) {
        this.sendPacket(viewer, new ClientboundSetScorePacket(user.getName(), this.name,
            ((int) user.getHealth()),
            Optional.of(new AdventureComponent(Server.getTimeDownParser().parse2Component(user.getTablistName()))),
            Optional.of(this.format)));
      }
    }
  }

  protected abstract void unload(ScoreboardViewer viewer);

}
