/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard.tablist;

import de.timesnake.basic.bukkit.util.user.scoreboard.TablistPlayer;
import de.timesnake.library.chat.ExTextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TablistSlot {

  protected TablistPlayer player;
  private final String prefix;
  private final ExTextColor chatColor;
  protected boolean sameIndexAsNeighbor = false;

  private Integer index;
  private boolean dirty = true;

  public TablistSlot(TablistPlayer player, @Nullable String prefix, @NotNull ExTextColor chatColor) {
    this.player = player;
    this.prefix = prefix;
    this.chatColor = chatColor;
  }

  public TablistPlayer getPlayer() {
    return player;
  }

  public @Nullable String getPrefix() {
    return prefix;
  }

  public @NotNull ExTextColor getChatColor() {
    return chatColor;
  }

  public Integer getIndex() {
    return index;
  }

  public void setIndex(Integer index) {
    if (Objects.equals(index, this.index)) {
      return;
    }
    this.index = index;
    this.dirty = true;
  }

  public boolean hasIndex() {
    return this.index != null;
  }

  public boolean isDirty() {
    return dirty;
  }

  public void unmarkAsDirty() {
    this.dirty = false;
  }

  public boolean canHaveSameIndexAsNeighbor() {
    return sameIndexAsNeighbor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TablistSlot that = (TablistSlot) o;
    return Objects.equals(player, that.player) && Objects.equals(prefix, that.prefix) && Objects.equals(chatColor, that.chatColor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(player, prefix, chatColor);
  }
}
