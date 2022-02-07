package de.timesnake.basic.bukkit.core.chat;

import de.timesnake.basic.bukkit.core.server.Info;
import de.timesnake.basic.bukkit.core.user.UserEventManager;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.ServerManager;
import de.timesnake.basic.bukkit.util.chat.*;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;
import de.timesnake.basic.bukkit.util.user.event.UserQuitEvent;
import de.timesnake.database.util.Database;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.ExCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ChatManager implements de.timesnake.library.extension.util.chat.Chat, Listener, de.timesnake.basic.bukkit.util.chat.ChatManager, CommandListener {

    public static final org.bukkit.ChatColor COLOR = org.bukkit.ChatColor.GRAY;

    public static final String GLOBAL_CHAT_NAME = "global";

    private final HashMap<String, de.timesnake.basic.bukkit.util.chat.Chat> chats = new HashMap<>();

    private boolean broadcastJoinQuit = true;

    public ChatManager() {
        de.timesnake.basic.bukkit.util.chat.Chat chat = new Chat(GLOBAL_CHAT_NAME, null, null, null);
        this.chats.put(GLOBAL_CHAT_NAME, chat);
    }

    @Override
    public de.timesnake.basic.bukkit.util.chat.Chat createChat(String name, String displayName, org.bukkit.ChatColor chatColor, Set<ChatMember> users) {
        if (name.equals(GLOBAL_CHAT_NAME)) {
            return null;
        }
        de.timesnake.basic.bukkit.util.chat.Chat chat = new Chat(name, displayName, chatColor, users);
        this.chats.put(name, chat);
        return chat;
    }

    @Override
    public boolean deleteChat(String name) {
        if (name.equals(GLOBAL_CHAT_NAME)) {
            return false;
        }
        return this.chats.remove(name) != null;
    }

    @Override
    public de.timesnake.basic.bukkit.util.chat.Chat getChat(String name) {
        return this.chats.get(name);
    }

    @Override
    public de.timesnake.basic.bukkit.util.chat.Chat getGlobalChat() {
        return this.chats.get(GLOBAL_CHAT_NAME);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        User user = Server.getUser(e.getPlayer());

        // event
        boolean isCanceled = ((UserEventManager) Server.getUserEventManager()).onUserChat(user, e.isCancelled(), e.getMessage());

        e.setCancelled(true);

        if (isCanceled) {
            return;
        }

        //mute
        if (user.isMuted()) {
            user.asSender(Plugin.BUKKIT).sendMessageMuted();
            return;
        }

        String msg = e.getMessage();

        // air mode
        if (user.isAirMode()) {
            if (!user.getLastChatMessage().equals(msg)) {
                user.sendPluginMessage(Plugin.BUKKIT, ChatColor.WARNING + "You are in air mode. " + "Resend your chat message to send it.");
                return;
            }
        }

        // last chat msg
        user.setLastChatMessage(e.getMessage());

        // color translate
        if (user.getPlayer().hasPermission("basicsystem.chat.color")) {
            msg = org.bukkit.ChatColor.translateAlternateColorCodes('&', msg);
        }

        if (msg.startsWith("!")) {
            if (!user.hasPermission("basicsystem.chat.global", 604, Plugin.BUKKIT)) {
                return;
            }
            msg = msg.replaceFirst("!", "");
            this.getGlobalChat().broadcastMemberMessage(user, msg);
        } else {
            for (de.timesnake.basic.bukkit.util.chat.Chat chat : this.chats.values()) {
                if (chat.containsWriter(user)) {
                    chat.broadcastMemberMessage(user, COLOR + msg);
                }
            }
        }

    }

    @Override
    public String getSenderMember(ChatMember member) {
        return member.getChatName() + de.timesnake.library.extension.util.chat.Chat.getSplitter();
    }

    @Override
    public String getLocationBlockText(Location loc) {
        return ChatColor.VALUE + loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
    }

    @Override
    public String getLocationText(Location loc) {
        return ChatColor.VALUE + loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ();
    }

    @Override
    public de.timesnake.basic.bukkit.util.chat.Argument createArgument(Sender sender, String s) {
        return new Argument(sender, s);
    }

    @EventHandler
    public void onUserQuit(UserQuitEvent e) {
        User user = e.getUser();
        // send quit msg
        if (broadcastJoinQuit && !user.isAirMode()) {
            Server.broadcastMessage("§4<<<§r" + user.getChatName());
        } else {
            Server.printText(Plugin.BUKKIT, "§4<<<§r" + user.getChatName());
        }
        // update online players
        int onlinePlayers = Bukkit.getOnlinePlayers().size() - 1;
        ((Info) ServerManager.getInstance().getInfo()).setOnlinePlayers(onlinePlayers);
        Server.printText(Plugin.BUKKIT, "Players online: " + onlinePlayers);
        // remove from chats
        for (de.timesnake.basic.bukkit.util.chat.Chat chat : this.chats.values()) {
            chat.removeWriter(user);
            chat.removeListener(user);
        }

        if (!user.agreedDataProtection()) {
            user.delete();
        }
    }

    @EventHandler
    public void onUserJoin(UserJoinEvent e) {
        de.timesnake.basic.bukkit.util.user.User user = e.getUser();
        // catch if user not in database -> error (proxy should added user)
        if (!Database.getUsers().containsUser(user.getUniqueId())) {
            user.sendMessage(de.timesnake.library.extension.util.chat.Chat.getSenderPlugin(Plugin.BUKKIT) + ChatColor.WARNING + "§lContact a supporter!!!");
            user.getPlayer().kickPlayer("§c§lContact a supporter!!!\n" + de.timesnake.library.extension.util.chat.Chat.getMessageCode("E", 805, Plugin.BUKKIT) + "\nDO NOT REJOIN");
            return;
        }

        // update online players
        Database.getServers().getServer(Bukkit.getPort()).setOnlinePlayers(Bukkit.getOnlinePlayers().size());
        Server.printText(Plugin.BUKKIT, "Players online: " + Bukkit.getOnlinePlayers().size());

        //add user to global chat
        this.getGlobalChat().addWriter(user);
        this.getGlobalChat().addListener(user);

        // send join msg
        if (broadcastJoinQuit && !user.isAirMode()) {
            Server.broadcastMessage("§2>>>§r" + user.getChatName());
            Server.broadcastSound(Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 200);
        } else {
            Server.printText(Plugin.BUKKIT, "§2>>>§r" + user.getChatName());
        }

    }

    @Override
    public void broadcastJoinQuit(boolean broadcast) {
        this.broadcastJoinQuit = broadcast;
    }

    @Override
    public boolean isBroadcastingJoinQuit() {
        return this.broadcastJoinQuit;
    }

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (!sender.hasPermission("basicsytem.chat.global", 604)) {
            return;
        }

        String msg = args.toMessage();
        if (sender.hasPermission("basicsystem.chat.color")) {
            msg = org.bukkit.ChatColor.translateAlternateColorCodes('&', msg);
        }

        if (sender.isPlayer(false)) {
            this.getGlobalChat().broadcastMemberMessage(sender.getUser(), msg);
        } else {
            this.getGlobalChat().broadcastPluginMessage(Plugin.INFO, ChatColor.WARNING + msg);
        }
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        return null;
    }
}
