/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.UUID;

public interface TablistPlayer {

  String getName();

  String getTablistName();

  default String getRank() {
    return this.getTablistName();
  }

  UUID getUniqueId();

  TablistGroup getTablistGroup(TablistGroupType type);

  ServerPlayer getMinecraftPlayer();

  default String getFullPrefix(List<TablistGroupType> types) {
    StringBuilder prefixBuilder = new StringBuilder();
    for (TablistGroupType type : types) {
      TablistGroup group = this.getTablistGroup(type);

      if (group != null && group.getTablistPrefix() != null && !group.getTablistPrefix().isBlank()) {
        if (group.getTablistPrefixColor() != null) {
          prefixBuilder.append("§").append(group.getTablistPrefixColor().getLegacyToken());
        }

        prefixBuilder.append(group.getTablistPrefix()).append("§r");
      }
    }

    return !prefixBuilder.isEmpty() ? prefixBuilder.toString() : null;
  }

  boolean showInTablist();

  String getTablistPrefix();

  default NameTagVisibility canSeeNameTagOf(TablistPlayer otherPlayer) {
    return NameTagVisibility.ALWAYS;
  }

}
