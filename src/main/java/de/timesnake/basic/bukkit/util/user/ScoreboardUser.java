/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.scoreboard.*;
import de.timesnake.library.packets.core.packet.out.scoreboard.ClientboundSetDisplayObjectivePacketBuilder;
import de.timesnake.library.packets.core.packet.out.scoreboard.ClientboundSetObjectivePacketBuilder;
import de.timesnake.library.packets.core.packet.out.scoreboard.ClientboundSetPlayerTeamPacketBuilder;
import net.minecraft.network.chat.numbers.BlankFormat;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundResetScorePacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class ScoreboardUser implements ScoreboardViewer {

  protected Tablist tablist;
  protected Sideboard sideboard;
  protected final HashMap<Integer, String> sideboardScores = new HashMap<>();

  public ScoreboardUser() {

  }

  @Override
  public NameTagVisibility canSeeNameTagOf(TablistPlayer player) {
    return NameTagVisibility.ALWAYS;
  }

  /**
   * Adds a tablist entry to the {@link Scoreboard}
   *
   * @param player The {@link TablistPlayer} to add
   */
  @Override
  public void addTablistEntry(TablistPlayer player) {
    Server.getScoreboardManager().getPacketManager().sendPacket(this,
        ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(player.getMinecraftPlayer())));
  }

  /**
   * Adds a tablist entry to the {@link Scoreboard} with team
   *
   * @param player The {@link TablistPlayer} to add
   * @param rank   The rank of the group
   */
  @Override
  public void addTablistEntry(TablistPlayer player, String rank) {
    Server.getScoreboardManager().getPacketManager().sendPacket(this,
        ClientboundSetPlayerTeamPacketBuilder.ofAddPlayer(rank, player.getTablistName()));
    this.addTablistEntry(player);
  }

  /**
   * Removes the user from the scoreboard (tablist) and scoreboard-team
   *
   * @param player The {@link TablistPlayer} to remove
   */
  @Override
  public void removeTablistEntry(TablistPlayer player) {
    Server.getScoreboardManager().getPacketManager().sendPacket(this,
        new ClientboundPlayerInfoRemovePacket(List.of(player.getUniqueId())));
  }

  /**
   * Removes the user from the scoreboard (tablist) and scoreboard-team
   *
   * @param player The {@link TablistPlayer} to remove
   * @param rank   The rank of the group
   */
  @Override
  public void removeTablistEntry(TablistPlayer player, String rank) {
    this.removeTablistEntry(player);
    Server.getScoreboardManager().getPacketManager().sendPacket(this,
        ClientboundSetPlayerTeamPacketBuilder.ofRemovePlayer(rank, player.getTablistName()));
  }

  /**
   * Sets the {@link Scoreboard} {@link DisplaySlot} player-list
   *
   * @param tablist The {@link Tablist} to set
   */
  @Override
  public void setTablist(Tablist tablist) {

    if (this.tablist != null) {
      if (this.tablist.equals(tablist)) {
        return;
      }
      ((de.timesnake.basic.bukkit.core.user.scoreboard.tablist.Tablist) this.tablist).removeViewer(this);
    }

    this.tablist = tablist;
    if (this.tablist == null) {
      Tablist standard = Server.getScoreboardManager().getTablist(Server.getName());
      this.setTablist(standard);
      return;
    }
    ((de.timesnake.basic.bukkit.core.user.scoreboard.tablist.Tablist) tablist).addViewer(this);

  }

  /**
   * Sets the {@link Scoreboard} {@link DisplaySlot} sideboard
   *
   * @param sideboard The {@link Sideboard} to set
   */
  @Override
  public void setSideboard(Sideboard sideboard) {
    if (this.sideboard != null) {
      if (this.sideboard.equals(sideboard)) {
        return;
      }
      this.sideboard.removeViewer(this);

      for (Map.Entry<Integer, String> score : this.sideboard.getScores().entrySet()) {
        this.removeSideboardScore(score.getKey(), score.getValue());
      }

      Server.getScoreboardManager().getPacketManager().sendPacket(this,
          ClientboundSetObjectivePacketBuilder.ofRemove(this.sideboard.getName()));
    }

    this.sideboard = sideboard;

    if (this.sideboard == null) {
      return;
    }

    sideboard.addViewer(this);

    Server.getScoreboardManager().getPacketManager().sendPacket(this,
        ClientboundSetObjectivePacketBuilder.ofAdd(this.sideboard.getName(), this.sideboard.getTitle(),
            ObjectiveCriteria.DUMMY, ObjectiveCriteria.DUMMY.getDefaultRenderType()));

    Server.getScoreboardManager().getPacketManager().sendPacket(this,
        ClientboundSetDisplayObjectivePacketBuilder.ofAdd(net.minecraft.world.scores.DisplaySlot.SIDEBAR,
            this.sideboard.getName()));

    for (Map.Entry<Integer, String> entry : sideboard.getScores().entrySet()) {
      this.setSideboardScore(entry.getKey(), entry.getValue());
    }

  }

  @Override
  public void setSideboardTitle(String title) {
    if (this.sideboard == null) {
      return;
    }

    Server.getScoreboardManager().getPacketManager().sendPacket(this,
        ClientboundSetObjectivePacketBuilder.ofChange(this.sideboard.getName(), title, ObjectiveCriteria.DUMMY,
            ObjectiveCriteria.DUMMY.getDefaultRenderType()));
  }

  /**
   * Sets a {@link Scoreboard} score
   * <p>
   * <p>
   * Removes previous score in the line
   *
   * @param line The line of the score
   * @param text The text to set
   */
  @Override
  public void setSideboardScore(int line, @Nonnull String text) {
    this.removeSideboardScore(line);

    if (this.sideboard == null) {
      return;
    }

    this.sideboardScores.put(line, text);

    Server.getScoreboardManager().getPacketManager().sendPacket(this,
        new ClientboundSetScorePacket(text, this.sideboard.getName(), line,
            Optional.of(net.minecraft.network.chat.Component.literal(text)), Optional.of(BlankFormat.INSTANCE)));
  }

  /**
   * Removes a {@link Scoreboard} score
   *
   * @param line The line to remove
   * @param text The text to remove
   */
  @Override
  public void removeSideboardScore(int line, @Nonnull String text) {
    if (this.sideboard == null) {
      return;
    }

    if (!this.sideboardScores.containsKey(line)) {
      return;
    }
    this.sideboardScores.remove(line);
    Server.getScoreboardManager().getPacketManager().sendPacket(this,
        new ClientboundResetScorePacket(text, this.sideboard.getName()));
  }

  /**
   * Removes a {@link Scoreboard} score
   *
   * @param line The line to remove
   */
  @Override
  public void removeSideboardScore(int line) {
    this.removeSideboardScore(line, this.sideboardScores.get(line));
  }

  /**
   * Resets the user sideboard
   */
  @Override
  public void resetSideboard() {
    if (this.sideboard == null) {
      return;
    }

    Server.getScoreboardManager().getPacketManager().sendPacket(this,
        ClientboundSetObjectivePacketBuilder.ofRemove(this.sideboard.getName()));
    this.sideboardScores.clear();
    this.setSideboard(null);
  }

  public Sideboard getSideboard() {
    return sideboard;
  }
}
