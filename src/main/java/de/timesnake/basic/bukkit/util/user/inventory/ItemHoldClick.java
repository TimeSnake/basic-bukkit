/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.inventory;

import org.jetbrains.annotations.Range;

public class ItemHoldClick {

  private final Integer clickTime;
  private Long firstClickMillis;
  private Long lastClickMillis;

  public ItemHoldClick(@Range(from = 700, to = Integer.MAX_VALUE) Integer clickTime) {
    this.clickTime = clickTime;
    long currentMillis = System.currentTimeMillis();
    this.firstClickMillis = currentMillis;
    this.lastClickMillis = currentMillis;
  }

  /**
   * @return True if player has clicked clickedTime, else false
   */
  public boolean click() {
    Long currentMillis = System.currentTimeMillis();
    if (currentMillis - lastClickMillis > 700) {
      this.firstClickMillis = currentMillis;
      this.lastClickMillis = currentMillis;
      return false;
    } else if (currentMillis - firstClickMillis >= clickTime) {
      this.firstClickMillis = currentMillis;
      this.lastClickMillis = currentMillis;
      return true;
    } else {
      this.lastClickMillis = currentMillis;
      return false;
    }
  }

  public Long getFirstClickMillis() {
    return firstClickMillis;
  }

  public Long getLastClickMillis() {
    return lastClickMillis;
  }
}
