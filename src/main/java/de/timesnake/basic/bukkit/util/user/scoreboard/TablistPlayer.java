/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public interface TablistPlayer {

  String getTablistName();

  TablistGroup getTablistGroup(TablistGroupType type);

  Player getPlayer();

  ServerPlayer getMinecraftPlayer();

  default String getFullPrefix(List<TablistGroupType> types) {
    StringBuilder prefixBuilder = new StringBuilder();
    for (TablistGroupType type : types) {
      TablistGroup group = this.getTablistGroup(type);

      if (group != null && group.getTablistPrefix() != null) {
        if (group.getTablistPrefixChatColor() != null) {
          prefixBuilder.append("§").append(group.getTablistPrefixChatColor().getLegacyToken());
        }

        prefixBuilder.append(group.getTablistPrefix()).append("§r");
      }
    }

    return prefixBuilder.toString();
  }

  boolean showInTablist();

  String getTablistPrefix();

  default NameTagVisibility canSeeNameTagOf(TablistPlayer otherPlayer) {
    return NameTagVisibility.ALWAYS;
  }

}
