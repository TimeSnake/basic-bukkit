package de.timesnake.basic.bukkit.util.chat;

import de.timesnake.library.basic.util.chat.Plugin;
import net.md_5.bungee.api.chat.ClickEvent;

import java.util.Set;

public interface Chat {

    static String getChatPrefix(String name, org.bukkit.ChatColor chatColor) {
        return chatColor + "(" + name + ")";
    }

    String getName();

    String getDisplayName();

    Set<ChatMember> getWriters();

    org.bukkit.ChatColor getChatColor();

    void addWriter(ChatMember member);

    void removeWriter(ChatMember member);

    boolean containsWriter(ChatMember member);

    Set<ChatMember> getListeners();

    void addListener(ChatMember member);

    void removeListener(ChatMember member);

    boolean containsListener(ChatMember member);

    void broadcastMemberMessage(ChatMember member, String... msgs);

    void broadcastPluginMessage(Plugin sender, String... msgs);

    void broadcastMessage(String... msgs);

    void broadcastClickableMessage(String text, String exec, String info, ClickEvent.Action action);

    void broadcastClickableMessage(Plugin plugin, String text, String exec, String info, ClickEvent.Action action);
}
