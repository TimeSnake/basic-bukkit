/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard.tablist;

import de.timesnake.basic.bukkit.util.user.scoreboard.TablistPlayer;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.packets.util.packet.TablistHead;

import java.util.List;

public class TablistTextEntry extends TablistRankedEntry {

  private final String rank;
  private final TablistPlayer player;
  private final TablistSlot slot;

  public TablistTextEntry(String rank, String text, TablistHead tablistHead) {
    this.rank = rank;
    this.player = new DummyTablistPlayer(rank, text, tablistHead);
    this.slot = new TablistSlot(this.player, null, ExTextColor.WHITE);
  }

  @Override
  public String getRank() {
    return this.rank;
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
  public String toString() {
    return "TablistTextEntry{" +
           "rank='" + rank + '\'' +
           ", player=" + player.getTablistName() +
           '}';
  }
}
