/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard.tablist;

import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroup;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroupType;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistPlayer;

import java.util.*;

public abstract non-sealed class TablistListEntry extends TablistEntry {

  protected final SortedMap<String, TablistEntry> entries = new TreeMap<>();
  protected final int gapSize;
  private final HashMap<TablistEntry, List<EmtpyTablistSlot>> gapSlotsByNextEntry = new HashMap<>();

  public TablistListEntry(int gapSize) {
    this.gapSize = gapSize;
  }

  public boolean addPlayer(TablistPlayer player, TablistEntryHelper entryHelper, Deque<TablistGroupType> groupTypes) {
    if (groupTypes.isEmpty()) {
      TablistPlayerEntry entry = new TablistPlayerEntry(player, entryHelper);
      boolean added = this.entries.put(entry.getRank(), entry) == null;
      if (added) {
        this.addGapSlots(entry);
      }
      return added;
    }

    TablistGroupType type = groupTypes.pop();
    TablistGroup group = player.getTablistGroup(type);

    if (group == null) {
      group = entryHelper.getDefaultGroup(type);
    }

    String listKey = String.valueOf(group.getTablistRank());
    TablistEntry listEntry = this.entries.get(listKey);

    if (listEntry == null) {
      listEntry = entryHelper.createGroup(type, group);
      this.entries.put(listKey, listEntry);
      this.addGapSlots(listEntry);
    }

    return ((TablistListEntry) listEntry).addPlayer(player, entryHelper, groupTypes);
  }

  public boolean removePlayer(TablistPlayer tablistPlayer) {
    TablistEntry tablistEntry = this.entries.remove(tablistPlayer.getRank());
    if (tablistEntry != null) {
      this.removeGapSlots(tablistEntry);
      return true;
    }

    for (Map.Entry<String, TablistEntry> entry : this.entries.entrySet()) {
      if (entry.getValue() instanceof TablistListEntry listEntry) {
        if (listEntry.removePlayer(tablistPlayer)) {
          if (listEntry.isEmpty()) {
            tablistEntry = this.entries.remove(entry.getKey());
            if (tablistEntry != null) {
              this.removeGapSlots(tablistEntry);
            }
          }
          return true;
        }
      }
    }

    return false;
  }

  protected void addGapSlots(TablistEntry nextEntry) {
    if (this.gapSize == 0 || this.entries.size() == 1) {
      return;
    }

    Iterator<TablistEntry> iterator = this.entries.values().iterator();
    if (iterator.next().equals(nextEntry)) {
      nextEntry = iterator.next();
    }

    ArrayList<EmtpyTablistSlot> slots = new ArrayList<>(this.gapSize);
    for (int i = 0; i < this.gapSize; i++) {
      slots.add(new EmtpyTablistSlot());
    }
    this.gapSlotsByNextEntry.put(nextEntry, slots);
  }

  protected void removeGapSlots(TablistEntry nextEntry) {
    if (this.gapSize == 0) {
      return;
    }
    this.gapSlotsByNextEntry.remove(nextEntry);
  }

  @Override
  public void collectAsSlots(List<TablistSlot> slots, TablistEntryHelper entryHelper) {
    if (gapSize > 0) {
      Deque<TablistEntry> queue = new ArrayDeque<>(this.entries.values());

      while (!queue.isEmpty()) {
        TablistEntry entry = queue.pop();
        entry.collectAsSlots(slots, entryHelper);

        if (!queue.isEmpty() && !queue.peek().isEmpty()) {
          slots.addAll(this.gapSlotsByNextEntry.get(queue.peek()));
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
    return this.size(entryHelper, entryHelper.getEntryGapSize(null));
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
