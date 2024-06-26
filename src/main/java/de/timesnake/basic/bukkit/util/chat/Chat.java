/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.chat;

import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.chat.Plugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;

import java.util.Set;

public interface Chat {

  static Component getChatPrefix(String name, TextColor color) {
    return Component.text("(" + name + ")", color);
  }

  String getName();

  String getDisplayName();

  Set<ChatMember> getWriters();

  ExTextColor getTextColor();

  void addWriter(ChatMember member);

  void removeWriter(ChatMember member);

  boolean containsWriter(ChatMember member);

  Set<ChatMember> getListeners();

  void addListener(ChatMember member);

  void removeListener(ChatMember member);

  boolean containsListener(ChatMember member);

  void broadcastMemberTDMessage(ChatMember member, String... msgs);

  void broadcastMemberMessage(ChatMember member, Component... msgs);

  void broadcastPluginTDMessage(Plugin sender, String... msgs);

  void broadcastPluginMessage(Plugin sender, Component... msgs);

  void broadcastTDMessage(String... msgs);

  void broadcastMessage(Component... msgs);

  void broadcastClickableTDMessage(String text, String exec, String info,
      ClickEvent.Action action);

  void broadcastClickableMessage(Component text, String exec, Component info,
      ClickEvent.Action action);

  void broadcastClickableTDMessage(Plugin plugin, String text, String exec, String info,
      ClickEvent.Action action);

  void broadcastClickableMessage(Plugin plugin, Component text, String exec, Component info,
      ClickEvent.Action action);
}
