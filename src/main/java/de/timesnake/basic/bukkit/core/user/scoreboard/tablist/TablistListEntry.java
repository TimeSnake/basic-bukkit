/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard.tablist;

import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroup;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroupType;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistPlayer;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.packets.util.packet.TablistHead;

import java.util.*;

public abstract non-sealed class TablistListEntry extends TablistEntry {

  protected final SortedMap<String, TablistEntry> entries = new TreeMap<>();

  public boolean addPlayer(TablistPlayer player, TablistEntryHelper entryHelper,
                           Deque<TablistGroupType> groupTypes) {
    if (groupTypes.isEmpty()) {
      TablistPlayerEntry entry = new TablistPlayerEntry(player, entryHelper);
      return this.entries.put(entry.getRank(), entry) == null;
    }

    TablistGroupType type = groupTypes.pop();
    TablistGroup group = Objects.requireNonNullElse(player.getTablistGroup(type), entryHelper.getDefaultGroup(type));

    return ((TablistListEntry) this.entries.computeIfAbsent(String.valueOf(group.getTablistRank()),
        rank -> entryHelper.createGroup(type, group))).addPlayer(player, entryHelper, groupTypes);
  }

  public boolean removePlayer(TablistPlayerEntry playerEntry) {
    if (this.entries.remove(playerEntry.getRank(), playerEntry)) {
      return true;
    }

    for (Map.Entry<String, TablistEntry> entry : this.entries.entrySet()) {
      if (entry.getValue() instanceof TablistListEntry listEntry) {
        if (listEntry.removePlayer(playerEntry)) {
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
      Iterator<TablistEntry> iterator = this.entries.values().iterator();
      while (iterator.hasNext()) {
        TablistEntry entry = iterator.next();
        entry.collectAsSlots(slots, entryHelper);

        if (iterator.hasNext()) {
          for (int i = 0; i < gapSize; i++) {
            slots.add(new TablistSlot(new DummyTablistPlayer("§" + (slots.size() / 10) +
                "§" + (slots.size() % 10) + "§" + i, TablistHead.BLANK), null, ExTextColor.WHITE));
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

  public boolean isEmpty() {
    return this.entries.isEmpty();
  }
}
