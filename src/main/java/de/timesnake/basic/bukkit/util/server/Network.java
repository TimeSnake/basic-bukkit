package de.timesnake.basic.bukkit.util.server;

import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.channel.api.message.ChannelServerMessage;
import de.timesnake.channel.listener.ChannelServerListener;

public interface Network extends ChannelServerListener {

    Integer PROXY_PORT = 25565;

    void onServerMessage(ChannelServerMessage msg);

    Integer getPlayerAmount();

    boolean sendUserToServer(User user, Integer server);

    boolean sendUserToServer(User user, String server);

    boolean sendUserToLobbyLast(User user);

    boolean sendUserToServerLast(User user);
}
