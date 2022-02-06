package de.timesnake.basic.bukkit.util.chat;

import net.md_5.bungee.api.chat.ClickEvent;

public interface ChatMember {

    void sendMessage(String message);

    void sendClickableMessage(String text, String exec, String info, ClickEvent.Action action);

    String getChatName();
}
