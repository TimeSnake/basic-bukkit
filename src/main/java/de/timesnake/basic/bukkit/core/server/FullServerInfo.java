/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.server;

import de.timesnake.basic.bukkit.util.server.ServerInfo;
import de.timesnake.database.util.object.TooLongEntryException;
import de.timesnake.database.util.server.*;
import de.timesnake.library.basic.util.ServerType;
import de.timesnake.library.basic.util.Status;

import java.util.Objects;

public class FullServerInfo extends ServerInfo {

  private final ServerType type;
  private String task;

  public FullServerInfo(DbServer server) {
    super(server);
    if (server instanceof DbNonTmpGameServer) {
      this.type = ServerType.GAME;
      this.task = ((DbNonTmpGameServer) server).getTask();
    } else if (server instanceof DbTmpGameServer) {
      this.type = ServerType.TEMP_GAME;
      this.task = ((DbTmpGameServer) server).getTask();
    } else if (server instanceof DbLoungeServer) {
      this.type = ServerType.LOUNGE;
      this.task = ((DbLoungeServer) server).getTask();
    } else if (server instanceof DbLobbyServer) {
      this.type = ServerType.LOBBY;
      this.task = "lobby";
    } else if (server instanceof DbBuildServer) {
      this.type = ServerType.BUILD;
      this.task = "build";
    } else {
      this.type = null;
    }
  }

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

  public ServerType getType() {
    return type;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public void setPassword(String password) throws TooLongEntryException {
    if (!Objects.equals(password, this.password)) {
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
