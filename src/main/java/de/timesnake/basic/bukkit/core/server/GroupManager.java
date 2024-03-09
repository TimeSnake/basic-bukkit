/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.server;

import de.timesnake.basic.bukkit.util.group.DisplayGroup;
import de.timesnake.basic.bukkit.util.group.PermGroup;
import de.timesnake.database.util.Database;
import de.timesnake.database.util.group.DbDisplayGroup;
import de.timesnake.database.util.group.DbPermGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GroupManager implements de.timesnake.basic.bukkit.util.group.GroupManager {

  private static final String GUEST_GROUP_NAME = "guest";
  private static final String MEMBER_GROUP_NAME = "member";

  private final Logger logger = LogManager.getLogger("group.manager");

  protected Map<String, PermGroup> permGroupByName = new HashMap<>();
  protected Map<String, DisplayGroup> displayGroupByName = new HashMap<>();

  public GroupManager() {
    for (DbPermGroup dbPermGroup : Database.getGroups().getPermGroups()) {
      PermGroup group = new PermGroup(dbPermGroup);
      this.permGroupByName.put(group.getName(), group);
    }

    for (DbDisplayGroup dbDisplayGroup : Database.getGroups().getDisplayGroups()) {
      DisplayGroup group = new DisplayGroup(dbDisplayGroup);
      this.displayGroupByName.put(group.getName(), group);
    }
  }

  /**
   * Gets the group by name
   *
   * @param group The group name from the group to get
   * @return the group
   */
  @Override
  public final PermGroup getPermGroup(String group) {
    return this.permGroupByName.get(group);
  }

  /**
   * Gets the guest group
   *
   * @return the guest group
   */
  @Override
  public final PermGroup getGuestPermGroup() {
    return this.permGroupByName.get(GUEST_GROUP_NAME);
  }

  /**
   * Gets the member group
   *
   * @return the member group
   */
  @Override
  public final PermGroup getMemberPermGroup() {
    return this.permGroupByName.get(MEMBER_GROUP_NAME);
  }

  /**
   * Gets all groups
   *
   * @return all groups
   */
  @Override
  public final Collection<PermGroup> getPermGroups() {
    return new ArrayList<>(this.permGroupByName.values());
  }

  @Override
  public final DisplayGroup getDisplayGroup(String group) {
    return this.displayGroupByName.get(group);
  }

  @Override
  public final Collection<DisplayGroup> getDisplayGroups() {
    return new ArrayList<>(this.displayGroupByName.values());
  }

  @Override
  public final DisplayGroup getGuestDisplayGroup() {
    return this.displayGroupByName.get(GUEST_GROUP_NAME);
  }

  @Override
  public final DisplayGroup getMemberDisplayGroup() {
    return this.displayGroupByName.get(MEMBER_GROUP_NAME);
  }

}
