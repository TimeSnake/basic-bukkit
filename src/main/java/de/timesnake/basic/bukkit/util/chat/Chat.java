/*
 * basic-bukkit.main
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

package de.timesnake.basic.bukkit.util.chat;

import de.timesnake.library.basic.util.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Plugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;

import java.util.Set;

public interface Chat {

    static Component getChatPrefix(String name, TextColor color) {
        return Component.text("(" + name + ")", color);
    }

    String getName();

    String getDisplayName();

    Set<ChatMember> getWriters();

    @Deprecated
    TextColor getChatColor();

    ExTextColor getTextColor();

    void addWriter(ChatMember member);

    void removeWriter(ChatMember member);

    boolean containsWriter(ChatMember member);

    Set<ChatMember> getListeners();

    void addListener(ChatMember member);

    void removeListener(ChatMember member);

    boolean containsListener(ChatMember member);

    @Deprecated
    void broadcastMemberMessage(ChatMember member, String... msgs);

    void broadcastMemberMessage(ChatMember member, Component... msgs);

    @Deprecated
    void broadcastPluginMessage(Plugin sender, String... msgs);

    void broadcastPluginMessage(Plugin sender, Component... msgs);

    @Deprecated
    void broadcastMessage(String... msgs);

    void broadcastMessage(Component... msgs);

    @Deprecated
    void broadcastClickableMessage(String text, String exec, String info, ClickEvent.Action action);

    void broadcastClickableMessage(Component text, String exec, Component info, ClickEvent.Action action);

    @Deprecated
    void broadcastClickableMessage(Plugin plugin, String text, String exec, String info, ClickEvent.Action action);

    void broadcastClickableMessage(Plugin plugin, Component text, String exec, Component info, ClickEvent.Action action);
}
