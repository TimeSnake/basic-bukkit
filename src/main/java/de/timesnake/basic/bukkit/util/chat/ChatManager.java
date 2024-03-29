/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.chat;

import de.timesnake.basic.bukkit.util.chat.cmd.Argument;
import de.timesnake.basic.bukkit.util.chat.cmd.Sender;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.library.chat.ExTextColor;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;

import java.util.Set;

public interface ChatManager extends de.timesnake.library.chat.Chat {

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

  void setBroadcastJoinQuit(boolean broadcast);

  boolean isBroadCastJoinQuit();

  void setBroadcastDeath(boolean broadcast);

  boolean isBroadcastDeath();

  void addUserMessageListener(UserMessageListener listener);

  void removeUserMessageListener(UserMessageListener listener);

  interface UserMessageListener {

    String onUserMessage(User user, String message);
  }
}

