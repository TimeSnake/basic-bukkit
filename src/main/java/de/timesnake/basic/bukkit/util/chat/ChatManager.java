package de.timesnake.basic.bukkit.util.chat;

import org.bukkit.Location;

import java.util.Set;

public interface ChatManager extends de.timesnake.library.extension.util.chat.Chat {

    Chat createChat(String name, String displayName, org.bukkit.ChatColor chatColor, Set<ChatMember> members);

    boolean deleteChat(String name);

    Chat getChat(String name);

    Chat getGlobalChat();

    String getSenderMember(ChatMember member);

    String getSender(Sender sender);

    String getLocationBlockText(Location loc);

    String getLocationText(Location loc);

    Argument createArgument(Sender sender, String s);

    void broadcastJoinQuit(boolean broadcast);

    boolean isBroadcastingJoinQuit();

}

