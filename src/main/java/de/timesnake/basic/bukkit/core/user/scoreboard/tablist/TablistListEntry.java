/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard.tablist;

import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroup;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroupType;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistPlayer;
import de.timesnake.library.chat.ExTextColor;

import java.util.*;

public abstract non-sealed class TablistListEntry extends TablistEntry {

  protected final SortedMap<String, TablistEntry> entries = new TreeMap<>();

  public boolean addPlayer(TablistPlayer player, TablistEntryHelper entryHelper, Deque<TablistGroupType> groupTypes) {
    if (groupTypes.isEmpty()) {
      TablistPlayerEntry entry = new TablistPlayerEntry(player, entryHelper);
      return this.entries.put(entry.getRank(), entry) == null;
    }

    TablistGroupType type = groupTypes.pop();
    TablistGroup group;

    if (player.getTablistGroup(type) != null) {
      group = player.getTablistGroup(type);
    } else {
      group = entryHelper.getDefaultGroup(type);
    }

    return ((TablistListEntry) this.entries.computeIfAbsent(String.valueOf(group.getTablistRank()),
        rank -> entryHelper.createGroup(type, group))).addPlayer(player, entryHelper, groupTypes);
  }

  public boolean removePlayer(TablistPlayer tablistPlayer) {
    if (this.entries.remove(tablistPlayer.getRank()) != null) {
      return true;
    }

    for (Map.Entry<String, TablistEntry> entry : this.entries.entrySet()) {
      if (entry.getValue() instanceof TablistListEntry listEntry) {
        if (listEntry.removePlayer(tablistPlayer)) {
          if (listEntry.isEmpty()) {
            this.entries.remove(entry.getKey());
          }
          return true;
        }
      }
    }

    return false;
  }

  @Override
  public void collectAsSlots(List<TablistSlot> slots, TablistEntryHelper entryHelper) {
    this.collectAsSlots(slots, entryHelper, entryHelper.getEntryGap(null));
  }

  protected void collectAsSlots(List<TablistSlot> slots, TablistEntryHelper entryHelper, int gapSize) {
    if (gapSize > 0) {
      Deque<TablistEntry> queue = new ArrayDeque<>(this.entries.values());
      while (!queue.isEmpty()) {
        TablistEntry entry = queue.pop();
        entry.collectAsSlots(slots, entryHelper);

        if (!queue.isEmpty() && !queue.peek().isEmpty()) {
          for (int i = 0; i < gapSize; i++) {
            String name = (slots.size() / 10) + "" + (slots.size() % 10) + i;
            String tablistName = "§" + (slots.size() / 10) + "§" + (slots.size() % 10) + "§" + i;
            slots.add(new TablistSlot(entryHelper.newGapEntry(name, tablistName), null, ExTextColor.WHITE));
          }
        }
      }
    } else {
      for (TablistEntry entry : this.entries.values()) {
        entry.collectAsSlots(slots, entryHelper);
      }
    }
  }

  @Override
  public boolean isEmpty() {
    if (this.entries.isEmpty()) {
      return true;
    }

    return this.entries.values().stream().allMatch(TablistEntry::isEmpty);
  }

  @Override
  public int size(TablistEntryHelper entryHelper) {
    return this.size(entryHelper, entryHelper.getEntryGap(null));
  }

  protected int size(TablistEntryHelper entryHelper, int gapSize) {
    if (gapSize > 0 && !this.entries.isEmpty()) {
      gapSize = (this.entries.size() - 1) * gapSize;
    } else {
      gapSize = 0;
    }

    return this.entries.values().stream().mapToInt(e -> e.size(entryHelper)).sum() + gapSize;
  }
}
