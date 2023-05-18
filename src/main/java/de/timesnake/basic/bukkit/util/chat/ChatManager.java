/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.chat;

import de.timesnake.library.chat.ExTextColor;
import java.util.Set;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;

public interface ChatManager extends de.timesnake.library.extension.util.chat.Chat {

  Chat createChat(String name, String displayName, ExTextColor chatColor,
      Set<ChatMember> members);

  boolean deleteChat(String name);

  Chat getChat(String name);

  Chat getGlobalChat();

  Component getSenderMember(ChatMember member);

  Component getSender(Sender sender);

  Component getLocationBlockText(Location loc);

  Component getLocationText(Location loc);

  Argument createArgument(Sender sender, String s);

  Argument createArgument(Sender sender, Component component);

  void broadcastJoinQuit(boolean broadcast);

  boolean isBroadcastingJoinQuit();

}

