/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard.tablist;

import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroup;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistPlayer;
import de.timesnake.library.chat.ExTextColor;

import java.util.List;
import java.util.Objects;

public class TablistPlayerEntry extends TablistRankedEntry {

  private final TablistPlayer player;
  private final TablistSlot slot;

  public TablistPlayerEntry(TablistPlayer player, TablistEntryHelper entryHelper) {
    this.player = player;

    String prefix = this.player.getGroupPrefixes(entryHelper.getGroupTypes());
    if (player.getTablistPrefix() != null && !player.getTablistPrefix().isEmpty()) {
      prefix += "§r" + this.player.getTablistPrefix();
    }

    ExTextColor color = ExTextColor.WHITE;
    if (entryHelper.getColorGroupType() != null) {
      TablistGroup group = this.player.getTablistGroup(entryHelper.getColorGroupType());

      if (group == null) {
        group = entryHelper.getDefaultGroup(entryHelper.getColorGroupType());
      }

      color = group.getTablistColor();
    }

    this.slot = new TablistSlot(this.player, prefix, color != null ? color : ExTextColor.WHITE);
  }

  @Override
  public String getRank() {
    return player.getRank();
  }

  @Override
  public void collectAsSlots(List<TablistSlot> slots, TablistEntryHelper entryHelper) {
    slots.add(this.getSlot());
  }

  public TablistSlot getSlot() {
    return this.slot;
  }

  @Override
  public int size(TablistEntryHelper entryHelper) {
    return 1;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TablistPlayerEntry that = (TablistPlayerEntry) o;
    return Objects.equals(player, that.player);
  }

  @Override
  public int hashCode() {
    return Objects.hash(player);
  }

  @Override
  public String toString() {
    return "TablistPlayerEntry{" +
           "player=" + player.getTablistName() +
           ", prefix=" + slot.getPrefix() +
           ", color=" + slot.getChatColor().getName() +
           '}';
  }
}
