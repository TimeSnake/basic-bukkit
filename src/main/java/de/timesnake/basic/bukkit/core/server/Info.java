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

package de.timesnake.basic.bukkit.core.server;

import de.timesnake.basic.bukkit.util.server.ServerInfo;
import de.timesnake.database.util.object.TooLongEntryException;
import de.timesnake.database.util.object.Type;
import de.timesnake.database.util.server.*;
import de.timesnake.library.basic.util.Status;

public class Info extends ServerInfo implements de.timesnake.basic.bukkit.util.server.Info {

    private final Type.Server<?> type;
    private String task;

    public Info(DbServer server) {
        super(server);
        if (server instanceof DbNonTmpGameServer) {
            this.type = Type.Server.GAME;
            this.task = ((DbNonTmpGameServer) server).getTask();
        } else if (server instanceof DbTmpGameServer) {
            this.type = Type.Server.TEMP_GAME;
            this.task = ((DbTmpGameServer) server).getTask();
        } else if (server instanceof DbLoungeServer) {
            this.type = Type.Server.LOUNGE;
            this.task = ((DbLoungeServer) server).getTask();
        } else if (server instanceof DbLobbyServer) {
            this.type = Type.Server.LOBBY;
            this.task = "lobby";
        } else if (server instanceof DbBuildServer) {
            this.type = Type.Server.BUILD;
            this.task = "build";
        } else {
            this.type = null;
        }
    }

    @Override
    public String getTask() {
        return task;
    }

    @Override
    public void setStatus(Status.Server status) {
        if (super.status != status) {
            this.database.setStatus(status);
        }
        super.setStatus(status);
    }

    public void updateStatus() {
        super.setStatus(this.database.getStatus());
    }

    @Override
    public Type.Server<?> getType() {
        return type;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setPassword(String password) throws TooLongEntryException {
        if (!super.password.equals(password)) {
            this.database.setPassword(password);
        }
        super.setPassword(password);
    }

    public void setOnlinePlayers(Integer players) {
        if (!super.maxPlayers.equals(players)) {
            this.database.setOnlinePlayers(players);
        }
        this.onlinePlayers = players;
    }
}
