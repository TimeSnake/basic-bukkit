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

package de.timesnake.basic.bukkit.util.server;

import de.timesnake.database.util.object.TooLongEntryException;
import de.timesnake.database.util.server.DbServer;
import de.timesnake.library.basic.util.Status;

import java.util.Objects;

public class ServerInfo {

    protected DbServer database;

    protected Integer port;
    protected String name;
    protected Status.Server status;
    protected Integer onlinePlayers;
    protected Integer maxPlayers;
    protected String password;

    public ServerInfo(DbServer server) {
        this.database = server;
        this.port = server.getPort();
        this.name = server.getName();
        this.status = server.getStatus();
        this.onlinePlayers = server.getOnlinePlayers();
        if (this.onlinePlayers == null) {
            this.onlinePlayers = 0;
        }
        this.maxPlayers = server.getMaxPlayers();
        this.password = server.getPassword();
    }

    public DbServer getDatabase() {
        return database;
    }

    public Integer getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public Status.Server getStatus() {
        return status;
    }

    public void setStatus(Status.Server status) {
        this.status = status;
    }

    public Integer getOnlinePlayers() {
        return onlinePlayers;
    }

    public void setOnlinePlayers(Integer onlinePlayers) {
        this.onlinePlayers = Objects.requireNonNullElse(onlinePlayers, 0);
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws TooLongEntryException {
        this.password = password;
    }
}
