/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard;

import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistablePlayer;
import de.timesnake.basic.bukkit.util.user.scoreboard.*;
import de.timesnake.library.packets.core.packet.out.scoreboard.ClientboundSetPlayerTeamPacketBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TagTeamTablist extends TeamTablist {

  public TagTeamTablist(TeamTablistBuilder builder, ScoreboardPacketManager packetManager) {
    super(builder, packetManager);
  }

  @Override
  protected void load(User user) {
    super.load(user);
    // set header footer

    this.packetManager.sendPacket(user, new ClientboundTabListPacket(Component.literal(this.header), Component.literal(this.footer)));

    for (int slot = 0; slot < 80; slot++) {
      this.packetManager.sendPacket(user, ClientboundSetPlayerTeamPacketBuilder.ofCreate(slot < 10 ? "0" + slot : String.valueOf(slot),
          Component.empty(), ChatFormatting.WHITE, this.nameTagVisibility.getPacketTag()));
    }

    for (SlotEntry entry : this.tablist) {
      if (entry != null) {
        this.packetManager.sendPacket(user, ClientboundSetPlayerTeamPacketBuilder.ofModify(entry.getSlot(),
            Component.literal(entry.getPrefix()), ChatFormatting.getByName(entry.getChatColor().name()),
            this.getNameTagVisibility(user, entry).getPacketTag()));

        this.packetManager.sendPacket(user, ClientboundSetPlayerTeamPacketBuilder.ofAddPlayer(entry.getSlot(), entry.getPlayer().getPlayer().getName()));
        this.packetManager.sendPacket(user, ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(entry.getPlayer().getMinecraftPlayer())));
      }
    }
  }

  @Override
  protected void updateChanges() {
    Tab<SlotEntry> oldTablist = this.tablist;

    this.recalculateTablist();

    Iterator<SlotEntry> oldIt = oldTablist.iterator();
    Iterator<SlotEntry> it = this.tablist.iterator();

    Set<TablistablePlayer> movedPlayers = new HashSet<>();

    // update slot if entry has changed

    while (oldIt.hasNext() && it.hasNext()) {
      SlotEntry oldEntry = oldIt.next();
      SlotEntry newEntry = it.next();

      if (!oldEntry.getPlayer().equals(newEntry.getPlayer())) {

        Player newPlayer = newEntry.getPlayer().getPlayer();

        // not remove moved players
        if (!movedPlayers.contains(oldEntry.getPlayer())) {
          this.broadcastPacket(new ClientboundPlayerInfoRemovePacket(List.of(oldEntry.getPlayer().getPlayer().getUniqueId())));
        }

        //update team with color and prefix
        for (User user : this.watchingUsers) {
          this.packetManager.sendPacket(user, ClientboundSetPlayerTeamPacketBuilder.ofModify(newEntry.getSlot(),
              Component.literal(newEntry.getPrefix()), ChatFormatting.getByName(newEntry.getChatColor().name()),
              this.getNameTagVisibility(user, newEntry).getPacketTag()));
        }

        // add new player
        this.broadcastPacket(ClientboundSetPlayerTeamPacketBuilder.ofAddPlayer(newEntry.getSlot(), newPlayer.getName()));
        this.broadcastPacket(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(newEntry.getPlayer().getMinecraftPlayer())));

        // mark as moved
        movedPlayers.add(newEntry.getPlayer());
      }
    }

    // remove old entries and now empty slots
    while (oldIt.hasNext()) {
      SlotEntry oldEntry = oldIt.next();
      // not remove moved players
      if (!movedPlayers.remove(oldEntry.getPlayer())) {
        this.broadcastPacket(new ClientboundPlayerInfoRemovePacket(List.of(oldEntry.getPlayer().getPlayer().getUniqueId())));
      }

    }

    // add new entries and update team
    while (it.hasNext()) {
      SlotEntry entry = it.next();
      for (User user : this.watchingUsers) {
        this.packetManager.sendPacket(user, ClientboundSetPlayerTeamPacketBuilder.ofModify(entry.getSlot(),
            Component.literal(entry.getPrefix()), ChatFormatting.getByName(entry.getChatColor().name()),
            this.getNameTagVisibility(user, entry).getPacketTag()));
      }

      this.broadcastPacket(ClientboundSetPlayerTeamPacketBuilder.ofAddPlayer(entry.getSlot(), entry.getPlayer().getPlayer().getName()));
      this.broadcastPacket(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(entry.getPlayer().getMinecraftPlayer())));
    }
  }

  private NameTagVisibility getNameTagVisibility(User user, SlotEntry entry) {
    NameTagVisibility tagVisibility = null;
    TablistableGroup userTeam = user.getTablistGroup(teamType);

    if (userTeam != null) {
      if (entry.getTeam() != null) {
        tagVisibility = ((TagTablistable) entry.getTeam()).isNameTagVisibleBy(user,
            userTeam);
      }
    } else {
      if (entry.getTeam() != null) {
        tagVisibility = ((TagTablistable) entry.getTeam()).isNameTagVisible(user);
      }
    }

    if (tagVisibility == null) {
      tagVisibility = NameTagVisibility.NEVER;
    }
    return tagVisibility;
  }
}
