/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard;

public abstract class TabEntry<E> {

  protected final String rank;
  protected E previous;
  protected E next;

  public TabEntry(String rank) {
    this.rank = rank;
  }

  public String getRank() {
    return rank;
  }

  public E getNext() {
    return next;
  }

  public void setNext(E next) {
    this.next = next;
  }

  public E getPrevious() {
    return previous;
  }

  public void setPrevious(E previous) {
    this.previous = previous;
  }

  public abstract void merge(E entry);

}
