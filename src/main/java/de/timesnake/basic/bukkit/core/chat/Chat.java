package de.timesnake.basic.bukkit.core.chat;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.ChatColor;
import de.timesnake.basic.bukkit.util.chat.ChatMember;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import net.md_5.bungee.api.chat.ClickEvent;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Chat implements de.timesnake.basic.bukkit.util.chat.Chat {

    private final String name;
    private final String displayName;
    private final org.bukkit.ChatColor chatColor;
    private final String chatPrefix;

    private final Set<ChatMember> writers;
    private final Set<ChatMember> listeners;

    public Chat(String name, String displayName, org.bukkit.ChatColor chatColor, Set<ChatMember> members) {
        this.name = name;
        this.displayName = displayName;
        this.chatColor = chatColor;
        if (this.displayName != null) {
            this.chatPrefix = de.timesnake.basic.bukkit.util.chat.Chat.getChatPrefix(this.displayName, Objects.requireNonNullElse(this.chatColor, ChatColor.WHITE));
        } else {
            this.chatPrefix = "";
        }
        this.writers = Objects.requireNonNullElseGet(members, HashSet::new);
        this.listeners = Objects.requireNonNullElseGet(members, HashSet::new);

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public Set<ChatMember> getWriters() {
        return this.writers;
    }

    @Override
    public org.bukkit.ChatColor getChatColor() {
        return chatColor;
    }

    @Override
    public void addWriter(ChatMember member) {
        this.writers.add(member);
    }

    @Override
    public void removeWriter(ChatMember member) {
        this.writers.remove(member);
    }

    @Override
    public boolean containsWriter(ChatMember member) {
        return this.writers.contains(member);
    }

    @Override
    public Set<ChatMember> getListeners() {
        return listeners;
    }

    @Override
    public void addListener(ChatMember member) {
        this.listeners.add(member);
    }

    @Override
    public void removeListener(ChatMember member) {
        this.listeners.remove(member);
    }

    @Override
    public boolean containsListener(ChatMember member) {
        return this.listeners.contains(member);
    }

    @Override
    public void broadcastMemberMessage(ChatMember member, String... msgs) {
        if (this.chatPrefix.equals("")) {
            for (String msg : msgs) {
                this.broadcastMessage(this.chatPrefix + Server.getChat().getSenderMember(member) + ChatManager.COLOR + msg);
            }
        } else {
            for (String msg : msgs) {
                this.broadcastMessage(this.chatPrefix + " " + Server.getChat().getSenderMember(member) + ChatManager.COLOR + msg);
            }
        }

    }

    @Override
    public void broadcastPluginMessage(de.timesnake.library.basic.util.chat.Plugin sender, String... msgs) {
        for (String msg : msgs) {
            this.broadcastMessage(this.chatPrefix + Server.getChat().getSenderPlugin(sender) + msg);
        }
    }

    public void broadcastMessage(String... msgs) {
        for (String msg : msgs) {
            for (ChatMember member : this.listeners) {
                member.sendMessage(msg);
            }
            Server.printText(Plugin.BUKKIT, msg, "Chat", this.name);
        }
    }

    @Override
    public void broadcastClickableMessage(String text, String exec, String info, ClickEvent.Action action) {
        for (ChatMember member : this.listeners) {
            member.sendClickableMessage(text, exec, info, action);
        }
    }

    @Override
    public void broadcastClickableMessage(de.timesnake.library.basic.util.chat.Plugin plugin, String text, String exec, String info, ClickEvent.Action action) {
        this.broadcastClickableMessage(Server.getChat().getSenderPlugin(plugin) + text, exec, info, action);
    }


}
