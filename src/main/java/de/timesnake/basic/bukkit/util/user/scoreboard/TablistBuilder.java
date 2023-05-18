/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import java.util.LinkedList;

public class TablistBuilder {

  private final String name;
  private Tablist.Type type = Tablist.Type.DUMMY;
  private LinkedList<TablistGroupType> groupTypes;
  private TablistUserJoin userJoin = (e, tablist) -> tablist.addEntry(e.getUser());
  private TablistUserQuit userQuit = (e, tablist) -> tablist.removeEntry(e.getUser());

  public TablistBuilder(String name) {
    this.name = name;
  }

  public TablistBuilder type(Tablist.Type type) {
    this.type = type;
    return this;
  }

  public TablistBuilder groupTypes(LinkedList<TablistGroupType> groupTypes) {
    this.groupTypes = groupTypes;
    return this;
  }

  public TablistBuilder userJoin(TablistUserJoin userJoin) {
    this.userJoin = userJoin;
    return this;
  }

  public TablistBuilder userQuit(TablistUserQuit userQuit) {
    this.userQuit = userQuit;
    return this;
  }

  public String getName() {
    return name;
  }

  public Tablist.Type getType() {
    return type;
  }

  public LinkedList<TablistGroupType> getGroupTypes() {
    return groupTypes;
  }

  public TablistUserJoin getUserJoin() {
    return userJoin;
  }

  public TablistUserQuit getUserQuit() {
    return userQuit;
  }
}
