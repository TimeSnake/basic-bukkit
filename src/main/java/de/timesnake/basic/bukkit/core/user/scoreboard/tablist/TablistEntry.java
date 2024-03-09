/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard.tablist;

import java.util.List;

public sealed abstract class TablistEntry implements Comparable<TablistEntry> permits TablistListEntry, TablistRankedEntry {

  public abstract void collectAsSlots(List<TablistSlot> slots, TablistEntryHelper entryHelper);

  public abstract int size(TablistEntryHelper entryHelper);

}
