/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.server;

import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.library.network.NetworkVariables;

public interface Network extends de.timesnake.library.network.Network {

  Integer PROXY_PORT = 25565;

  String getProxyName();

  NetworkVariables getVariables();

  Integer getPlayerAmount();

  boolean sendUserToServer(User user, Integer server);

  boolean sendUserToServer(User user, String server);

  boolean sendUserToLobbyLast(User user);

  boolean sendUserToServerLast(User user);
}
