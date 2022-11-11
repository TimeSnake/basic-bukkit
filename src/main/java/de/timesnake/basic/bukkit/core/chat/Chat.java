/*
 * timesnake.basic-bukkit.main
 * Copyright (C) 2022 timesnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; If not, see <http://www.gnu.org/licenses/>.
 */

package de.timesnake.basic.bukkit.core.chat;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.ChatMember;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.library.basic.util.chat.ExTextColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Chat implements de.timesnake.basic.bukkit.util.chat.Chat {

    private final String name;
    private final String displayName;
    private final ExTextColor chatColor;
    private final Component chatPrefix;

    private final Set<ChatMember> writers;
    private final Set<ChatMember> listeners;

    public Chat(String name, String displayName, ExTextColor chatColor, Set<ChatMember> members) {
        this.name = name;
        this.displayName = displayName;
        this.chatColor = chatColor;
        if (this.displayName != null) {
            this.chatPrefix = de.timesnake.basic.bukkit.util.chat.Chat.getChatPrefix(this.displayName,
                    this.chatColor != null ? this.chatColor : ExTextColor.WHITE);
        } else {
            this.chatPrefix = null;
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
    @Deprecated
    public TextColor getChatColor() {
        return chatColor;
    }

    public ExTextColor getTextColor() {
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
    @Deprecated
    public void broadcastMemberMessage(ChatMember member, String... msgs) {
        if (this.chatPrefix == null) {
            for (String msg : msgs) {
                this.broadcastMessage(Server.getChat().getSenderMember(member).append(Component.text(msg)));
            }
        } else {
            for (String msg : msgs) {
                this.broadcastMessage(this.chatPrefix.append(Component.text(" "))
                        .append(Server.getChat().getSenderMember(member))
                        .append(Component.text(msg)));
            }
        }

    }

    @Override
    public void broadcastMemberMessage(ChatMember member, Component... msgs) {
        if (this.chatPrefix == null) {
            for (Component msg : msgs) {
                this.broadcastMessage(Server.getChat().getSenderMember(member).append(msg));
            }
        } else {
            for (Component msg : msgs) {
                this.broadcastMessage(this.chatPrefix.append(Component.text(" "))
                        .append(Server.getChat().getSenderMember(member))
                        .append(msg));
            }
        }

    }

    @Override
    @Deprecated
    public void broadcastPluginMessage(de.timesnake.library.extension.util.chat.Plugin sender, String... msgs) {
        for (String msg : msgs) {
            this.broadcastMessage(de.timesnake.library.extension.util.chat.Chat.getSenderPlugin(sender)
                    .append(Component.text(msg)));
        }
    }

    @Override
    public void broadcastPluginMessage(de.timesnake.library.extension.util.chat.Plugin sender, Component... msgs) {
        for (Component msg : msgs) {
            this.broadcastMessage(de.timesnake.library.extension.util.chat.Chat.getSenderPlugin(sender).append(msg));
        }
    }

    @Override
    @Deprecated
    public void broadcastMessage(String... msgs) {
        for (String msg : msgs) {
            for (ChatMember member : this.listeners) {
                member.sendMessage(msg);
            }
            Plugin.CHATS.getLogger().info(this.name + ": " + msg);
        }
    }

    @Override
    public void broadcastMessage(Component... msgs) {
        for (Component msg : msgs) {
            for (ChatMember member : this.listeners) {
                member.sendMessage(msg);
            }
            Plugin.CHATS.getLogger().info(this.name + ": " + PlainTextComponentSerializer.plainText().serialize(msg));
        }
    }

    @Override
    @Deprecated
    public void broadcastClickableMessage(String text, String exec, String info, net.kyori.adventure.text.event.ClickEvent.Action action) {
        for (ChatMember member : this.listeners) {
            member.sendClickableMessage(text, exec, info, action);
        }
    }

    @Override
    public void broadcastClickableMessage(Component text, String exec, Component info, net.kyori.adventure.text.event.ClickEvent.Action action) {
        for (ChatMember member : this.listeners) {
            member.sendClickableMessage(text, exec, info, action);
        }
    }

    @Override
    @Deprecated
    public void broadcastClickableMessage(de.timesnake.library.extension.util.chat.Plugin plugin, String text,
                                          String exec, String info, ClickEvent.Action action) {
        this.broadcastClickableMessage(plugin,
                LegacyComponentSerializer.legacyAmpersand().deserialize(text), exec,
                LegacyComponentSerializer.legacyAmpersand().deserialize(info), action);
    }

    @Override
    public void broadcastClickableMessage(de.timesnake.library.extension.util.chat.Plugin plugin, Component text,
                                          String exec, Component info, ClickEvent.Action action) {
        this.broadcastClickableMessage(de.timesnake.library.extension.util.chat.Chat.getSenderPlugin(plugin).append(text),
                exec, info, action);
    }


}
