/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.chat;

import java.util.List;

public interface TabCompleteManager {

  List<String> getPlayerNames();

  List<String> getPermGroupNames();

  List<String> getWorldNames();

  List<String> getMapNames(String gameName);

  List<String> getGameNames();
}
