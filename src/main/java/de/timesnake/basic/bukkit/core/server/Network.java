/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.server;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.channel.util.listener.ChannelHandler;
import de.timesnake.channel.util.listener.ChannelListener;
import de.timesnake.channel.util.listener.ListenerType;
import de.timesnake.channel.util.message.ChannelServerMessage;
import de.timesnake.channel.util.message.ChannelUserMessage;
import de.timesnake.channel.util.message.MessageType;
import de.timesnake.database.util.Database;
import de.timesnake.library.network.NetworkUtils;
import de.timesnake.library.network.NetworkVariables;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Network extends NetworkUtils implements de.timesnake.basic.bukkit.util.server.Network,
    ChannelListener {

  private final Set<UUID> userSwitching = new HashSet<>();
  private int playerAmount;

  private final NetworkVariables variables;

  public Network(Integer playerAmount) {
    super(Database.getNetwork().getNetworkFile("network").getFile().toPath());
    this.playerAmount = playerAmount;
    this.variables = new NetworkVariables();
    this.variables.load();
    Server.getChannel().addListener(this, Collections.singleton(this.getProxyName()));
  }

  @ChannelHandler(type = ListenerType.SERVER_ONLINE_PLAYERS, filtered = true)
  public void onServerMessage(ChannelServerMessage<?> msg) {
    if (msg.getValue() == null) {
      return;
    }
    this.setPlayerAmount((Integer) msg.getValue());
  }

  @Override
  public @NotNull String getProxyName() {
    return "proxy";
  }

  @Override
  public @NotNull NetworkVariables getVariables() {
    return this.variables;
  }

  @Override
  public int getPlayerAmount() {
    return playerAmount;
  }

  public void setPlayerAmount(int playerAmount) {
    this.playerAmount = playerAmount;
  }

  @Override
  public boolean sendUserToServer(User user, Integer server) {
    UUID uuid = user.getUniqueId();
    if (userSwitching.contains(uuid)) {
      return false;
    }
    this.userSwitching.add(uuid);

    Server.getChannel()
        .sendMessage(new ChannelUserMessage<>(uuid, MessageType.User.SWITCH_PORT, server));

    Server.runTaskLaterSynchrony(() -> this.userSwitching.remove(uuid), 20,
        BasicBukkit.getPlugin());

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

    Server.runTaskLaterSynchrony(() -> this.userSwitching.remove(uuid), 20,
        BasicBukkit.getPlugin());

    return true;
  }

  @Override
  public boolean sendUserToLobbyLast(User user) {
    return this.sendUserToServer(user, user.getLastLobbyServer().getName());
  }

  @Override
  public boolean sendUserToServerLast(User user) {
    return this.sendUserToServer(user, user.getLastServer().getName());
  }
}
