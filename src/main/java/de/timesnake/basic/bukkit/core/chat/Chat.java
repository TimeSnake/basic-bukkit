/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.chat;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.ChatMember;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.chat.Plugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Chat implements de.timesnake.basic.bukkit.util.chat.Chat {

  private final Logger logger = LogManager.getLogger("server.chat");

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
  public void broadcastMemberTDMessage(ChatMember member, String... msgs) {
    if (this.chatPrefix == null) {
      for (String msg : msgs) {
        Component component;
        if (member.hasColoredChatMessagePermission()) {
          component = Server.getTimeDownParser().parse2Component(msg, '&');
        } else {
          component = Component.text(msg);
        }
        this.broadcastMessage(Server.getChat().getSenderMember(member).append(component));
      }
    } else {
      for (String msg : msgs) {
        Component component;
        if (member.hasColoredChatMessagePermission()) {
          component = Server.getTimeDownParser().parse2Component(msg, '&');
        } else {
          component = Component.text(msg);
        }
        this.broadcastMessage(this.chatPrefix.append(Component.text(" "))
            .append(Server.getChat().getSenderMember(member)).append(component));
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
        this.broadcastMessage(this.chatPrefix.append(Component.text(" ")).append(Server.getChat().getSenderMember(member)).append(msg));
      }
    }

  }

  @Override
  public void broadcastPluginTDMessage(Plugin sender, String... msgs) {
    for (String msg : msgs) {
      this.broadcastMessage(de.timesnake.library.chat.Chat.getSenderPlugin(sender).append(Server.getTimeDownParser().parse2Component(msg)));
    }
  }

  @Override
  public void broadcastPluginMessage(Plugin sender, Component... msgs) {
    for (Component msg : msgs) {
      this.broadcastMessage(de.timesnake.library.chat.Chat.getSenderPlugin(sender).append(msg));
    }
  }

  @Override
  public void broadcastTDMessage(String... msgs) {
    for (String msg : msgs) {
      for (ChatMember member : this.listeners) {
        member.sendTDMessage(msg);
      }
      this.logger.info("{}: {}", this.name, msg);
    }
  }

  @Override
  public void broadcastMessage(Component... msgs) {
    for (Component msg : msgs) {
      for (ChatMember member : this.listeners) {
        member.sendMessage(msg);
      }
      this.logger.info("{}: {}", this.name, PlainTextComponentSerializer.plainText().serialize(msg));
    }
  }

  @Override
  public void broadcastClickableTDMessage(String text, String exec, String info,
                                          net.kyori.adventure.text.event.ClickEvent.Action action) {
    for (ChatMember member : this.listeners) {
      member.sendClickableTDMessage(text, exec, info, action);
    }
  }

  @Override
  public void broadcastClickableMessage(Component text, String exec, Component info,
                                        net.kyori.adventure.text.event.ClickEvent.Action action) {
    for (ChatMember member : this.listeners) {
      member.sendClickableMessage(text, exec, info, action);
    }
  }

  @Override
  public void broadcastClickableTDMessage(Plugin plugin, String text, String exec, String info,
                                          ClickEvent.Action action) {
    this.broadcastClickableMessage(plugin, Server.getTimeDownParser().parse2Component(text), exec,
        Server.getTimeDownParser().parse2Component(info), action);
  }

  @Override
  public void broadcastClickableMessage(Plugin plugin, Component text, String exec, Component info,
                                        ClickEvent.Action action) {
    this.broadcastClickableMessage(de.timesnake.library.chat.Chat.getSenderPlugin(plugin).append(text), exec, info,
        action);
  }


}
