/*
 * workspace.basic-bukkit.main
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

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.channel.core.Channel;
import de.timesnake.channel.util.listener.ChannelHandler;
import de.timesnake.channel.util.listener.ChannelListener;
import de.timesnake.channel.util.listener.ListenerType;
import de.timesnake.channel.util.message.ChannelServerMessage;
import de.timesnake.channel.util.message.ChannelUserMessage;
import de.timesnake.channel.util.message.MessageType;
import de.timesnake.database.util.Database;
import de.timesnake.library.network.NetworkUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Network extends NetworkUtils implements de.timesnake.basic.bukkit.util.server.Network, ChannelListener {

    private final Set<UUID> userSwitching = new HashSet<>();
    private Integer playerAmount;

    public Network(Integer playerAmount) {
        super(Database.getNetwork().getNetworkFile("network").getFile().toPath());
        this.playerAmount = playerAmount;
        Server.getChannel().addListener(this, () -> Collections.singleton(this.getName()));
    }

    @ChannelHandler(type = ListenerType.SERVER_ONLINE_PLAYERS, filtered = true)
    public void onServerMessage(ChannelServerMessage<?> msg) {
        this.setPlayerAmount((Integer) msg.getValue());
    }

    @Override
    public String getName() {
        return Channel.PROXY_NAME;
    }

    @Override
    public Integer getPlayerAmount() {
        return playerAmount;
    }

    public void setPlayerAmount(Integer playerAmount) {
        this.playerAmount = playerAmount;
    }

    @Override
    public boolean sendUserToServer(User user, Integer server) {
        UUID uuid = user.getUniqueId();
        if (userSwitching.contains(uuid)) {
            return false;
        }
        this.userSwitching.add(uuid);

        Server.getChannel().sendMessage(new ChannelUserMessage<>(uuid, MessageType.User.SWITCH_PORT, server));

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

        Server.getChannel().sendMessage(new ChannelUserMessage<>(uuid, MessageType.User.SWITCH_NAME, server));

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
