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

import de.timesnake.library.extension.util.chat.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public interface CommandManager {

    void addCommand(JavaPlugin mainClass, String cmd, CommandListener listener, Plugin basicPlugin);

    void addCommand(JavaPlugin mainClass, String cmd, List<String> aliases, CommandListener listener,
                    Plugin basicPlugin);

    TabCompleteManager getTabCompleter();
}
