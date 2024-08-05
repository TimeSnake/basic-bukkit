/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard.sideboard;

import de.timesnake.basic.bukkit.core.user.scoreboard.Scoreboard;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.scoreboard.SideboardBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class Sideboard extends Scoreboard implements de.timesnake.basic.bukkit.util.user.scoreboard.Sideboard {

  protected final Logger logger = LogManager.getLogger("scoreboard.sideboard");
  
  private final HashMap<Integer, String> scores = new HashMap<>();
  private String title;

  public Sideboard(SideboardBuilder builder) {
    super(builder.getName());
    this.title = builder.getTitle();

    scores.putAll(builder.getScores());
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public void setTitle(String title) {
    this.title = Server.getTimeDownParser().parse2Legacy(title);
    for (User user : super.watchingUsers) {
      user.setSideboardTitle(this.title);
    }
    this.logger.info("sideboard '{}' set title '{}'", this.name, this.title);
  }

  @Override
  public void setScore(Integer line, String text) {
    text = Server.getTimeDownParser().parse2Legacy(text);

    while (this.scores.containsValue(text)) {
      text = "§f" + text;
    }

    this.scores.put(line, text);
    for (User user : watchingUsers) {
      user.setSideboardScore(line, text);
    }
    this.logger.info("sideboard '{}' set score {}: '{}'", this.name, line, text);
  }

  @Override
  public void removeScore(int line) {
    for (User user : watchingUsers) {
      user.removeSideboardScore(line);
    }
    this.scores.remove(line);
    this.logger.info("sideboard '{} remove score '{}'", this.name, line);
  }

  @Override
  public HashMap<Integer, String> getScores() {
    return scores;
  }

  @Override
  public void clearScores() {
    for (Integer line : this.scores.keySet()) {
      this.removeScore(line);
    }
    this.logger.info("sideboard '{}' clear scores", this.name);
  }

}
