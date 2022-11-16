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

package de.timesnake.basic.bukkit.core.main;

import de.timesnake.basic.bukkit.core.chat.LoggerCmd;
import de.timesnake.basic.bukkit.core.database.DatabaseBroadcastCmd;
import de.timesnake.basic.bukkit.core.server.PidCmd;
import de.timesnake.basic.bukkit.core.user.CmdDataProtection;
import de.timesnake.basic.bukkit.core.user.PreCmd;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.ServerManager;
import de.timesnake.basic.bukkit.util.chat.CommandListener;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.server.Network;
import de.timesnake.channel.bukkit.main.ChannelBukkit;
import de.timesnake.channel.core.NetworkChannel;
import de.timesnake.channel.util.message.ChannelServerMessage;
import de.timesnake.channel.util.message.MessageType;
import de.timesnake.database.bukkit.main.DatabaseBukkit;
import de.timesnake.database.util.Database;
import de.timesnake.database.util.server.DbServer;
import de.timesnake.library.basic.util.Status;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Level;

public class BasicBukkit extends JavaPlugin {

    public static final String DATA_PROTECTION_VERSION = "2.0";

    public static JavaPlugin getPlugin() {
        return BasicBukkit.plugin;
    }

    public static void registerEvents(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, BasicBukkit.getPlugin());
    }

    private static JavaPlugin plugin;

    @Override
    public void onEnable() {
        BasicBukkit.plugin = this;

        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new PreCmd(), this);
        pm.registerEvents(new CmdDataProtection(), this);

        DbServer server = Database.getServers().getServer(Bukkit.getPort());

        if (server != null) {
            ChannelBukkit.start(server.getName(), Network.PROXY_PORT);
            server.setStatusSynchronized(Status.Server.LOADING);
            server.setOnlinePlayers(0);
        } else {
            Bukkit.getLogger().log(Level.WARNING, "Server is not in database");
            Bukkit.shutdown();
        }

        ServerManager.getInstance().onEnable();


        Server.getCommandManager().addCommand(this, "dpd", List.of("dataprotection", "datadeclaration", "datapd",
                "dpdeclaration"), new CmdDataProtection(), Plugin.NETWORK);
        Server.getCommandManager().addCommand(this, "databasemessage", List.of("databasemsg", "databasemsgs",
                "databasemessages"), new DatabaseBroadcastCmd(), Plugin.SYSTEM);
        Server.getCommandManager().addCommand(this, "pid", new PidCmd(), Plugin.SYSTEM);

        Server.getCommandManager().addCommand(this, "global", List.of("g", "all"),
                ((CommandListener) ServerManager.getInstance().getChatManager()), Plugin.BUKKIT);

        Server.getCommandManager().addCommand(this, "logger", List.of("log"),
                new LoggerCmd(), Plugin.SYSTEM);

    }

    @Override
    public void onDisable() {
        Server.getChannel().sendMessageSynchronized(new ChannelServerMessage<>(Server.getName(),
                MessageType.Server.STATUS, Status.Server.OFFLINE));

        ServerManager.getInstance().onDisable();

        DatabaseBukkit.disconnect();
        NetworkChannel.stop();
    }

}
