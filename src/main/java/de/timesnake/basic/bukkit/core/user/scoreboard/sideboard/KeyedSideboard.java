/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard.sideboard;

import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.scoreboard.KeyedSideboardBuilder;
import de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardPacketManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class KeyedSideboard extends Sideboard implements de.timesnake.basic.bukkit.util.user.scoreboard.KeyedSideboard {

  private final HashMap<LineId<?>, Integer> lineByLineId = new HashMap<>();

  public KeyedSideboard(KeyedSideboardBuilder builder, ScoreboardPacketManager packetManager) {
    super(builder, packetManager);

    boolean lineSpacer = builder.isLineSpacer();
    LinkedList<LineId<?>> lines = builder.getLineIds();

    Collections.reverse(lines);

    int line = 0;

    for (Iterator<LineId<?>> iterator = lines.iterator(); iterator.hasNext(); ) {
      LineId<?> lineId = iterator.next();
      if (lineId.isInline()) {
        lineByLineId.put(lineId, line);
      } else {
        lineByLineId.put(lineId, line);
        line++;
        super.setScore(line, lineId.getDisplayName());
      }

      line++;

      if (lineSpacer && iterator.hasNext()) {
        super.setScore(line, "§f".repeat(line) + SPACER);
        line++;
      }
    }
  }

  @Override
  public <V> void updateScore(LineId<V> id, V value) {
    Integer line = this.lineByLineId.get(id);

    if (line == null) {
      throw new IllegalArgumentException("unknown line id '" + id.getId() + "'");
    }

    String text;

    if (id.isInline()) {
      text = id.getDisplayName() + id.parseValue(value);
    } else {
      text = id.parseValue(value);
    }

    super.setScore(line, text);
  }

  @Override
  public <V> void updateScore4User(User user, LineId<V> id, V value) {
    Integer line = this.lineByLineId.get(id);

    if (line == null) {
      throw new IllegalArgumentException("unknown line id '" + id.getId() + "'");
    }

    user.setSideboardScore(line, id.parseValue(value));
    this.logger.info("sideboard '{}' set score {}: '{}' for user '{}'", this.name, line, id.parseValue(value), user.getName());
  }
}
