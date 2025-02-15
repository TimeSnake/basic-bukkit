/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard.tablist;

import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroup;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroupType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class TablistGroupEntry extends TablistListEntry {

  private final TablistGroupType type;
  private final TablistGroup group;

  private final TreeSet<TablistTextEntry> headers = new TreeSet<>();
  private final TreeSet<TablistTextEntry> footers = new TreeSet<>();

  public TablistGroupEntry(int gapSize, TablistGroupType type, TablistGroup group) {
    super(gapSize);
    this.type = type;
    this.group = group;
  }

  @Override
  public int compareTo(@NotNull TablistEntry o) {
    if (o instanceof TablistGroupEntry e) return Integer.compare(this.group.getTablistRank(), e.group.getTablistRank());
    else return -1;
  }

  public TablistGroupType getType() {
    return type;
  }

  public TablistGroup getGroup() {
    return group;
  }

  public boolean addHeader(TablistTextEntry text) {
    return this.headers.add(text);
  }

  public boolean removeHeader(TablistTextEntry text) {
    return this.headers.remove(text);
  }

  public boolean addFooter(TablistTextEntry text) {
    return this.footers.add(text);
  }

  public boolean removeFooter(TablistTextEntry text) {
    return this.footers.remove(text);
  }

  @Override
  public void collectAsSlots(List<TablistSlot> slots, TablistEntryHelper entryHelper) {
    for (TablistEntry entry : this.headers) {
      entry.collectAsSlots(slots, entryHelper);
    }
    super.collectAsSlots(slots, entryHelper);
    for (TablistEntry entry : this.footers) {
      entry.collectAsSlots(slots, entryHelper);
    }
  }

  @Override
  public int size(TablistEntryHelper entryHelper) {
    return super.size(entryHelper, entryHelper.getEntryGapSize(this.type))
        + this.headers.stream().mapToInt(e -> e.size(entryHelper)).sum()
        + this.footers.stream().mapToInt(e -> e.size(entryHelper)).sum();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TablistGroupEntry that = (TablistGroupEntry) o;
    return Objects.equals(group, that.group);
  }

  @Override
  public int hashCode() {
    return Objects.hash(group);
  }

  @Override
  public String toString() {
    return "TablistGroupEntry{" +
           "type=" + type +
           ", group=" + group.getTablistName() +
           (!headers.isEmpty() ?
               ", headers=[" + headers.stream().map(TablistTextEntry::toString).collect(Collectors.joining(", ")) +
               "]" : "") +
           (!footers.isEmpty() ?
               ", footers=[" + footers.stream().map(TablistTextEntry::toString).collect(Collectors.joining(", ")) +
               "]" : "") +
           ", entries=[" + entries.values().stream().map(TablistEntry::toString).collect(Collectors.joining(", ")) +
           "]" +
           '}';
  }
}
