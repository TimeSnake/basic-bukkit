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

package de.timesnake.basic.bukkit.core.user;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;
import de.timesnake.database.util.server.DbPvPServer;
import de.timesnake.library.basic.util.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Plugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PvPManager implements Listener, de.timesnake.basic.bukkit.util.user.PvPManager {

    public static final Integer ATTACK_SPEED = 10;
    public static final Integer ATTACK_DAMAGE = 2;
    public static final Integer MAX_NO_DAMAGE_TICKS = 4;

    private boolean oldPvP;

    public PvPManager() {
        if (Server.getDatabase() instanceof DbPvPServer) {
            this.oldPvP = ((DbPvPServer) Server.getDatabase()).isOldPvP();
        } else {
            this.oldPvP = false;
        }
        Server.registerListener(this, BasicBukkit.getPlugin());
    }

    @Override
    public void setPvP(boolean oldPvP) {
        this.oldPvP = oldPvP;
        for (User user : Server.getUsers()) {
            this.updateAttributesOfUser(user);

            if (this.oldPvP) {
                user.getPlayer().setMaximumNoDamageTicks(MAX_NO_DAMAGE_TICKS);
            } else {
                user.getPlayer().setMaximumNoDamageTicks(10);
            }
        }
    }

    @Override
    public boolean isOldPvP() {
        return oldPvP;
    }

    @EventHandler
    public void onUserJoin(UserJoinEvent event) {
        User user = event.getUser();
        this.updateAttributesOfUser(user);
    }

    private void updateAttributesOfUser(User user) {
        user.setPvpMode(this.oldPvP);
    }

    @Override
    public void broadcastPvPTypeMessage() {
        if (this.oldPvP) {
            Server.broadcastMessage(Plugin.BUKKIT, Component.text("§lHint: ", ExTextColor.WARNING, TextDecoration.BOLD)
                    .append(Component.text("Pre1.9 pvp (1.8 pvp) is activated, so you can hit fast.", ExTextColor.WARNING)));
        } else {
            Server.broadcastMessage(Plugin.BUKKIT, Component.text("§lHint: ", ExTextColor.WARNING, TextDecoration.BOLD)
                    .append(Component.text("1.9+ pvp is activated, so you should hit slow.", ExTextColor.WARNING)));
        }
    }

}
