/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

public interface ChatMember {

  void sendTDMessage(String message);

  void sendMessage(Component component);

  void sendClickableTDMessage(String text, String exec, String info, ClickEvent.Action action);

  void sendClickableMessage(Component text, String exec, Component info,
      ClickEvent.Action action);

  Component getChatNameComponent();

  boolean hasColoredChatMessagePermission();
}
