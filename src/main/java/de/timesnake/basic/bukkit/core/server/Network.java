package de.timesnake.basic.bukkit.core.server;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.channel.api.message.ChannelServerMessage;
import de.timesnake.channel.api.message.ChannelUserMessage;
import de.timesnake.channel.listener.ChannelServerListener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Network implements de.timesnake.basic.bukkit.util.server.Network, ChannelServerListener {

    private Integer playerAmount;
    private final Set<UUID> userSwitching = new HashSet<>();

    public Network(Integer playerAmount) {
        this.playerAmount = playerAmount;
        Server.getChannel().addServerListener(this, Network.PROXY_PORT);
    }

    public void setPlayerAmount(Integer playerAmount) {
        this.playerAmount = playerAmount;
    }


    @Override
    public void onServerMessage(ChannelServerMessage msg) {
        if (msg.getType().equals(ChannelServerMessage.MessageType.ONLINE_PLAYERS)) {
            this.setPlayerAmount(Integer.valueOf((String) msg.getValue()));
        }
    }

    @Override
    public Integer getPlayerAmount() {
        return playerAmount;
    }

    @Override
    public boolean sendUserToServer(User user, Integer server) {
        UUID uuid = user.getUniqueId();
        if (userSwitching.contains(uuid)) {
            return false;
        }
        this.userSwitching.add(uuid);

        Server.getChannel().sendMessage(ChannelUserMessage.getSwitchMessage(uuid, server));

        Server.runTaskLaterSynchrony(() -> this.userSwitching.remove(uuid), 20, BasicBukkit.getPlugin());

        return true;
    }

    @Override
    public boolean sendUserToServer(User user, String server) {
        UUID uuid = user.getUniqueId();
        if (userSwitching.contains(uuid)) {
            return false;
        }
        this.userSwitching.add(uuid);

        Server.getChannel().sendMessage(ChannelUserMessage.getSwitchMessage(uuid, server));

        Server.runTaskLaterSynchrony(() -> this.userSwitching.remove(uuid), 20, BasicBukkit.getPlugin());

        return true;
    }

    @Override
    public boolean sendUserToLobbyLast(User user) {
        return this.sendUserToServer(user, user.getLastLobbyServer().getPort());
    }

    @Override
    public boolean sendUserToServerLast(User user) {
        return this.sendUserToServer(user, user.getLastServer().getPort());
    }
}
