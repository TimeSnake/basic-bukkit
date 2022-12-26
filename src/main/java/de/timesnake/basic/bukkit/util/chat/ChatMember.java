/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.util.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

public interface ChatMember {

    void sendMessage(String message);

    void sendMessage(Component component);

    @Deprecated
    void sendClickableMessage(String text, String exec, String info, ClickEvent.Action action);

    void sendClickableMessage(Component text, String exec, Component info, ClickEvent.Action action);

    String getChatName();

    Component getChatNameComponent();
}
