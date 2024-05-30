/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface TablistPlayer {

  @NotNull
  String getName();

  @NotNull
  String getTablistName();

  default @NotNull String getRank() {
    return this.getTablistName();
  }

  @NotNull
  UUID getUniqueId();

  @Nullable
  TablistGroup getTablistGroup(TablistGroupType type);

  ServerPlayer getMinecraftPlayer();

  default @Nullable String getGroupPrefixes(List<TablistGroupType> types) {
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

  @Nullable
  String getTablistPrefix();

  default @NotNull NameTagVisibility canSeeNameTagOf(TablistPlayer otherPlayer) {
    return NameTagVisibility.ALWAYS;
  }

}
