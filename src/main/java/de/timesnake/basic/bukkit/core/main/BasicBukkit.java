package de.timesnake.basic.bukkit.core.main;

import de.timesnake.basic.bukkit.core.channel.ChannelBroadcastCmd;
import de.timesnake.basic.bukkit.core.database.DatabaseBroadcastCmd;
import de.timesnake.basic.bukkit.core.permission.CustomPermissibleBase;
import de.timesnake.basic.bukkit.core.server.PidCmd;
import de.timesnake.basic.bukkit.core.user.CmdDataProtection;
import de.timesnake.basic.bukkit.core.user.PreCmd;
import de.timesnake.basic.bukkit.core.world.WorldManager;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.ServerManager;
import de.timesnake.basic.bukkit.util.chat.CommandListener;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.server.Network;
import de.timesnake.channel.api.message.ChannelServerMessage;
import de.timesnake.channel.bukkit.main.ChannelBukkit;
import de.timesnake.channel.main.NetworkChannel;
import de.timesnake.database.bukkit.main.DatabaseBukkit;
import de.timesnake.database.util.Database;
import de.timesnake.database.util.object.Status;
import de.timesnake.database.util.object.Type;
import de.timesnake.database.util.server.DbServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Level;

public class BasicBukkit extends JavaPlugin {

    public static final String DATA_PROTECTION_VERSION = "2.0";

    private static JavaPlugin plugin;

    @Override
    public void onEnable() {
        BasicBukkit.plugin = this;

        for (Player p : Bukkit.getOnlinePlayers()) {
            try {
                CustomPermissibleBase.inject(p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new PreCmd(), this);
        pm.registerEvents(new CmdDataProtection(), this);

        ChannelBukkit.start(Network.PROXY_PORT);

        DbServer server = Database.getServers().getServer(Bukkit.getPort());

        if (server != null) {
            if (server.getType().equals(Type.Server.BUILD)) {
                server.setStatusSynchronized(Status.Server.SERVICE);
            } else {
                server.setStatusSynchronized(Status.Server.ONLINE);
            }
            server.setOnlinePlayers(0);
        } else {
            Bukkit.getLogger().log(Level.WARNING, "Server is not in database");
            Bukkit.shutdown();
        }

        ServerManager.getInstance().onEnable();


        Server.getChannel().addServerListener(ServerManager.getInstance(), Bukkit.getPort());

        Server.getCommandManager().addCommand(this, "dpd", List.of("dataprotection", "datadeclaration", "datapd", "dpdeclaration"), new CmdDataProtection(), Plugin.NETWORK);
        Server.getCommandManager().addCommand(this, "channelmsg", List.of("channelmsgs", "channelmessage", "channelmessages"), new ChannelBroadcastCmd(), Plugin.NETWORK);
        Server.getCommandManager().addCommand(this, "databasemessage", List.of("databasemsg", "databasemsgs", "databasemessages"), new DatabaseBroadcastCmd(), Plugin.DATABASE);
        Server.getCommandManager().addCommand(this, "pid", new PidCmd(), Plugin.SYSTEM);

        Server.getCommandManager().addCommand(this, "global", List.of("g", "all"), ((CommandListener) ServerManager.getInstance().getChatManager()), Plugin.BUKKIT);


        ServerManager.getInstance().printText(Plugin.BUKKIT, "Loaded successfully");
    }


    @Override
    public void onDisable() {
        Server.getChannel().sendMessageSynchronized(ChannelServerMessage.getStatusMessage(Server.getPort(), Status.Server.OFFLINE));

        ((WorldManager) Server.getWorldManager()).onDisable();

        DatabaseBukkit.disconnect();
        NetworkChannel.stop();
    }

    public static JavaPlugin getPlugin() {
        return BasicBukkit.plugin;
    }

    public static void registerEvents(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, BasicBukkit.getPlugin());
    }

}
