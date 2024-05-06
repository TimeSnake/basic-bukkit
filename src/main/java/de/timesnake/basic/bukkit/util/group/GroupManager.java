/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.group;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface GroupManager {

  PermGroup getPermGroup(String group);

  @NotNull
  PermGroup getGuestPermGroup();

  @NotNull
  PermGroup getMemberPermGroup();

  @NotNull
  Collection<PermGroup> getPermGroups();

  DisplayGroup getDisplayGroup(String group);

  @NotNull
  Collection<DisplayGroup> getDisplayGroups();

  @NotNull
  DisplayGroup getGuestDisplayGroup();

  @NotNull
  DisplayGroup getMemberDisplayGroup();
}
