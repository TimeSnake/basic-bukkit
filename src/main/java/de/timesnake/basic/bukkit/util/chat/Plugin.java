/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.chat;

public class Plugin extends de.timesnake.library.chat.Plugin {

  public static final Plugin WORLDS = new Plugin("Worlds", "BSW");

  protected Plugin(String name, String code) {
    super(name, code);
  }
}
