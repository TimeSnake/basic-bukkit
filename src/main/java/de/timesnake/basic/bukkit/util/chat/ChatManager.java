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
import net.kyori.adventure.text.Component;
import org.bukkit.Location;

import java.util.Set;

public interface ChatManager extends de.timesnake.library.extension.util.chat.Chat {

    Chat createChat(String name, String displayName, ExTextColor chatColor, Set<ChatMember> members);

    boolean deleteChat(String name);

    Chat getChat(String name);

    Chat getGlobalChat();

    Component getSenderMember(ChatMember member);

    Component getSender(Sender sender);

    Component getLocationBlockText(Location loc);

    Component getLocationText(Location loc);

    Argument createArgument(Sender sender, String s);

    Argument createArgument(Sender sender, Component component);

    void broadcastJoinQuit(boolean broadcast);

    boolean isBroadcastingJoinQuit();

}

