/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.group;

import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroup;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroupType;
import de.timesnake.database.util.group.DbDisplayGroup;
import de.timesnake.library.chat.ExTextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class DisplayGroup extends de.timesnake.library.permissions.DisplayGroup<User> implements TablistGroup {


  public static final TablistGroupType TABLIST_TYPE_0 = TablistGroupType.DISPLAY_GROUP_0;
  public static final TablistGroupType TABLIST_TYPE_1 = TablistGroupType.DISPLAY_GROUP_1;
  public static final TablistGroupType TABLIST_TYPE_2 = TablistGroupType.DISPLAY_GROUP_2;

  public static final LinkedList<TablistGroupType> MAIN_TABLIST_GROUPS = new LinkedList<>(
      List.of(TABLIST_TYPE_0, TABLIST_TYPE_1, TABLIST_TYPE_2));

  public DisplayGroup(DbDisplayGroup database) {
    super(database);
    this.logger.info("Loaded display-group '{}'", this.name);
  }

  private void loadPrefix() {
    this.prefix = this.database.getPrefix();
    if (this.prefix == null) {
      this.prefix = "";
    }
    this.prefixColor = this.database.getChatColor();
  }

  @Override
  public int getTablistRank() {
    return this.rank;
  }

  @Nullable
  @Override
  public String getTablistPrefix() {
    return this.getPrefix();
  }

  @Nullable
  @Override
  public ExTextColor getTablistPrefixColor() {
    return this.getPrefixColor();
  }

  @Nullable
  @Override
  public ExTextColor getTablistColor() {
    return this.getPrefixColor();
  }

  @NotNull
  @Override
  public String getTablistName() {
    return this.name;
  }

  public void updatePrefix() {
    this.loadPrefix();
    for (User user : this.users) {
      user.updateAlias();
    }
  }
}
