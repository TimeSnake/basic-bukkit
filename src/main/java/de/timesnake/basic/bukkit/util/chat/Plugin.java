/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.chat;

import de.timesnake.library.basic.util.LogHelper;

import java.util.logging.Logger;

public class Plugin extends de.timesnake.library.extension.util.chat.Plugin {

  public static final Plugin WORLDS = new Plugin("Worlds", "BSW", LogHelper.getLogger("Worlds"));

  protected Plugin(String name, String code, Logger logger) {
    super(name, code);
  }
}
