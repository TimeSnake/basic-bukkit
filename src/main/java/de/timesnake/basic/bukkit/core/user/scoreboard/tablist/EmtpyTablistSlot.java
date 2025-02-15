/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard.tablist;

import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.packets.util.packet.TablistHead;

public class EmtpyTablistSlot extends TablistSlot {

  public EmtpyTablistSlot() {
    super(null, null, ExTextColor.WHITE);
    this.sameIndexAsNeighbor = true;
  }

  @Override
  public void setIndex(Integer index) {
    super.setIndex(index);

    String indexString = String.valueOf(index);
    StringBuilder tablistName = new StringBuilder();
    for (int i = 0; i < indexString.length(); i++) {
      tablistName.append("§").append(indexString.charAt(i));
    }

    this.player = new DummyTablistPlayer(String.valueOf(index), tablistName.toString(), TablistHead.BLANK);
  }
}
