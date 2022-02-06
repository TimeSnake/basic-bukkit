package de.timesnake.basic.bukkit.util.chat;

import de.timesnake.library.basic.util.chat.Plugin;
import de.timesnake.library.basic.util.cmd.Arguments;
import org.bukkit.Location;

import java.util.Set;

public interface ChatManager {

    Chat createChat(String name, String displayName, org.bukkit.ChatColor chatColor, Set<ChatMember> members);

    boolean deleteChat(String name);

    Chat getChat(String name);

    Chat getGlobalChat();

    String getSenderMember(ChatMember member);

    String getLocationBlockText(Location loc);

    String getLocationText(Location loc);

    Argument createArgument(Sender sender, String s);

    void broadcastJoinQuit(boolean broadcast);

    boolean isBroadcastingJoinQuit();

    String getSplitter();

    String getOtherSplitter();

    String getLineSeparator();

    String getLongLineSeparator();

    String getDoubleLineSeparator();

    String getSenderPlugin(Plugin plugin);

    String getMessageCode(String codeType, int code, Plugin plugin);

    Arguments<de.timesnake.library.basic.util.cmd.Argument> createArguments(de.timesnake.library.basic.util.cmd.Sender sender, de.timesnake.library.basic.util.cmd.Argument... args);
}

