/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.main;

import de.timesnake.basic.bukkit.core.chat.CodeCmd;
import de.timesnake.basic.bukkit.core.chat.LoggerCmd;
import de.timesnake.basic.bukkit.core.chat.PasswordCmd;
import de.timesnake.basic.bukkit.core.chat.PidCmd;
import de.timesnake.basic.bukkit.core.user.CmdPrivacyPolicy;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.ServerManager;
import de.timesnake.basic.bukkit.util.chat.cmd.CommandListener;
import de.timesnake.channel.bukkit.main.ChannelBukkit;
import de.timesnake.database.bukkit.main.DatabaseBukkit;
import de.timesnake.database.util.Database;
import de.timesnake.database.util.server.DbServer;
import de.timesnake.library.basic.util.Status;
import de.timesnake.library.basic.util.logger.LogConfig;
import de.timesnake.library.chat.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

public class BasicBukkit extends JavaPlugin {

  public static JavaPlugin getPlugin() {
    return BasicBukkit.plugin;
  }

  public static void registerEvents(Listener listener) {
    Bukkit.getPluginManager().registerEvents(listener, BasicBukkit.getPlugin());
  }

  private static JavaPlugin plugin;

  @Override
  public void onLoad() {
    new LogConfig(new File("log.cfg")).apply();
  }

  @Override
  public void onEnable() {
    BasicBukkit.plugin = this;

    PluginManager pm = Bukkit.getPluginManager();

    //pm.registerEvents(new PreCmd(), this);
    pm.registerEvents(new CmdPrivacyPolicy(), this);

    DbServer server = Database.getServers().getServer(Bukkit.getPort());

    if (server != null) {
      ChannelBukkit.start(server.getName());
      server.setStatusSynchronized(Status.Server.LOADING);
      server.setOnlinePlayers(0);
    } else {
      Bukkit.getLogger().log(Level.WARNING, "Server is not in database");
      Bukkit.shutdown();
    }

    if (ServerManager.getInstance() == null) {
      ServerManager.setInstance(new ServerManager());
    }
    ServerManager.getInstance().onEnable();

    Server.getCommandManager().addCommand(this, "pp", List.of("privacy", "privacypolicy"), new CmdPrivacyPolicy(), Plugin.NETWORK);
    Server.getCommandManager().addCommand(this, "pid", new PidCmd(), Plugin.SERVER);

    Server.getCommandManager().addCommand(this, "global", List.of("g", "all"),
        ((CommandListener) ServerManager.getInstance().getChatManager()), Plugin.SERVER);

    Server.getCommandManager().addCommand(this, "logger", List.of("log"), new LoggerCmd(), Plugin.SERVER);

    Server.getCommandManager().addCommand(this, "password", List.of("pwd"), new PasswordCmd(), Plugin.SERVER);

    Server.getCommandManager().addCommand(this, "code", new CodeCmd(), Plugin.SERVER);
  }

  @Override
  public void onDisable() {
    Server.getDatabase().setStatusSynchronized(Status.Server.OFFLINE);

    ServerManager.getInstance().onDisable();

    DatabaseBukkit.disconnect();
    ChannelBukkit.stop();
  }

}
