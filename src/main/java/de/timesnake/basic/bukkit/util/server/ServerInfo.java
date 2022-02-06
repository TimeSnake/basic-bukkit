package de.timesnake.basic.bukkit.util.server;

import de.timesnake.database.util.object.Status;
import de.timesnake.database.util.object.TooLongEntryException;
import de.timesnake.database.util.server.DbServer;

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

    public Integer getOnlinePlayers() {
        return onlinePlayers;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public String getPassword() {
        return password;
    }

    public void setStatus(Status.Server status) {
        this.status = status;
    }

    public void setOnlinePlayers(Integer onlinePlayers) {
        this.onlinePlayers = Objects.requireNonNullElse(onlinePlayers, 0);
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setPassword(String password) throws TooLongEntryException {
        this.password = password;
    }
}
