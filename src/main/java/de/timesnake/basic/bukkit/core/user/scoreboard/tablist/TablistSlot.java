/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard.tablist;

import de.timesnake.basic.bukkit.util.user.scoreboard.TablistPlayer;
import de.timesnake.library.chat.ExTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TablistSlot {

  private final TablistPlayer player;

  private final String prefix;
  private final ExTextColor chatColor;

  public TablistSlot(TablistPlayer player, String prefix, @NotNull ExTextColor chatColor) {
    this.player = player;
    this.prefix = prefix;
    this.chatColor = chatColor;
  }

  public TablistPlayer getPlayer() {
    return player;
  }

  public String getPrefix() {
    return prefix;
  }

  public ExTextColor getChatColor() {
    return chatColor;
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
