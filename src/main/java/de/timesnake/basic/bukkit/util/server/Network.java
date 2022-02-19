package de.timesnake.basic.bukkit.util.server;

import de.timesnake.basic.bukkit.util.user.User;

public interface Network {

    Integer PROXY_PORT = 25565;

    Integer getPlayerAmount();

    boolean sendUserToServer(User user, Integer server);

    boolean sendUserToServer(User user, String server);

    boolean sendUserToLobbyLast(User user);

    boolean sendUserToServerLast(User user);
}
