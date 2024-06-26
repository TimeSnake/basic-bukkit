/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.server;

import de.timesnake.database.util.object.TooLongEntryException;
import de.timesnake.database.util.server.DbServer;
import de.timesnake.library.basic.util.Status;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

  public @NotNull DbServer getDatabase() {
    return database;
  }

  public @NotNull Integer getPort() {
    return port;
  }

  public @NotNull String getName() {
    return name;
  }

  public @NotNull Status.Server getStatus() {
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

  public @Nullable String getPassword() {
    return password;
  }

  public void setPassword(String password) throws TooLongEntryException {
    this.password = password;
  }
}
