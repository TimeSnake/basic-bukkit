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

import de.timesnake.basic.bukkit.core.chat.ExCommandSender;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.library.extension.util.chat.Plugin;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class Sender extends de.timesnake.library.extension.util.cmd.Sender {

    public Sender(ExCommandSender cmdSender, Plugin plugin) {
        super(cmdSender, plugin);
    }

    public Sender(Player player, Plugin plugin) {
        super(new ExCommandSender(player), plugin);
    }

    public Component getChatName() {
        if (this.isPlayer(false)) {
            return this.getUser().getChatNameComponent();
        } else if (this.isConsole(false)) {
            return Component.text(Plugin.BUKKIT.getName());
        }
        return null;
    }

    public Player getPlayer() {
        return cmdSender.getPlayer();
    }

    public User getUser() {
        return Server.getUser(this.getPlayer());
    }

    @Override
    public void sendConsoleMessage(String message) {
        Server.printText(Plugin.BUKKIT, message);
    }
}
