/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.core.chat;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.core.server.Info;
import de.timesnake.basic.bukkit.core.user.UserEventManager;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.ServerManager;
import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.ChatMember;
import de.timesnake.basic.bukkit.util.chat.CommandListener;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;
import de.timesnake.basic.bukkit.util.user.event.UserQuitEvent;
import de.timesnake.database.util.Database;
import de.timesnake.library.basic.util.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Code;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.ExCommand;
import io.papermc.paper.event.player.AsyncChatEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatManager implements de.timesnake.library.extension.util.chat.Chat, Listener,
        de.timesnake.basic.bukkit.util.chat.ChatManager, CommandListener {

    public static final ExTextColor COLOR = ExTextColor.GRAY;

    public static final String GLOBAL_CHAT_NAME = "global";

    private final HashMap<String, de.timesnake.basic.bukkit.util.chat.Chat> chats = new HashMap<>();

    private boolean broadcastJoinQuit = true;
    private Code.Permission globalPerm;

    public ChatManager() {
        de.timesnake.basic.bukkit.util.chat.Chat chat = new Chat(GLOBAL_CHAT_NAME, null, null,
                null);
        this.chats.put(GLOBAL_CHAT_NAME, chat);

        Server.registerListener(this, BasicBukkit.getPlugin());
    }

    @Override
    public de.timesnake.basic.bukkit.util.chat.Chat createChat(String name, String displayName,
            ExTextColor chatColor, Set<ChatMember> users) {
        if (name.equals(GLOBAL_CHAT_NAME)) {
            return null;
        }
        de.timesnake.basic.bukkit.util.chat.Chat chat = new Chat(name, displayName, chatColor,
                users);
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
    public void onPlayerChat(AsyncChatEvent e) {
        User user = Server.getUser(e.getPlayer());

        String msg = PlainTextComponentSerializer.plainText().serialize(e.message());

        // event
        boolean isCanceled = ((UserEventManager) Server.getUserEventManager()).onUserChat(user,
                e.isCancelled(), msg);

        e.setCancelled(true);

        if (isCanceled) {
            return;
        }

        //mute
        if (user.isMuted()) {
            user.asSender(Plugin.BUKKIT).sendMessageMuted();
            return;
        }

        // air mode
        if (user.isAirMode()) {
            if (!user.getLastChatMessage().equals(msg)) {
                user.sendPluginMessage(Plugin.BUKKIT,
                        Component.text("You are in air mode. Resend your chat " +
                                "message to send it.", ExTextColor.PUBLIC));
                return;
            }
        }

        // last chat msg
        user.setLastChatMessage(msg);

        boolean global = false;

        if (msg.startsWith("!") && msg.length() > 1) {
            if (user.hasPermission(this.globalPerm, Plugin.BUKKIT)) {
                msg = msg.replaceFirst("!", "");
                global = true;
            }

        }

        Component component;
        if (user.hasPermission("chat.color")) {
            component = LegacyComponentSerializer.legacyAmpersand().deserialize(msg);
        } else {
            component = PlainTextComponentSerializer.plainText().deserialize(msg);
        }

        if (global) {
            this.getGlobalChat().broadcastMemberMessage(user, component.color(COLOR));
        } else {
            for (de.timesnake.basic.bukkit.util.chat.Chat chat : this.chats.values()) {
                if (chat.containsWriter(user)) {
                    chat.broadcastMemberMessage(user, component.color(COLOR));
                }
            }
        }

    }

    @Override
    public Component getSenderMember(ChatMember member) {
        return member.getChatNameComponent()
                .append(de.timesnake.library.extension.util.chat.Chat.getSplitter());
    }

    @Override
    public Component getSender(Sender sender) {
        return sender.getChatName()
                .append(de.timesnake.library.extension.util.chat.Chat.getSplitter());
    }

    @Override
    public Component getLocationBlockText(Location loc) {
        return Component.text(
                loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() +
                        " " + loc.getBlockZ(), ExTextColor.VALUE);
    }

    @Override
    public Component getLocationText(Location loc) {
        return Component.text(loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " +
                loc.getZ(), ExTextColor.VALUE);
    }

    @Override
    public de.timesnake.basic.bukkit.util.chat.Argument createArgument(Sender sender,
            Component component) {
        return new Argument(sender, PlainTextComponentSerializer.plainText().serialize(component));
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
            Server.broadcastMessage(Component.text("<<<", ExTextColor.DARK_RED)
                    .append(user.getChatNameComponent().color(ExTextColor.WHITE)));
        } else {
            Plugin.CHATS.getLogger().info("<<<" +
                    PlainTextComponentSerializer.plainText()
                            .serialize(user.getChatNameComponent()));
        }
        // update online players
        int onlinePlayers = Bukkit.getOnlinePlayers().size() - 1;
        ((Info) ServerManager.getInstance().getInfo()).setOnlinePlayers(onlinePlayers);
        // remove from chats
        for (de.timesnake.basic.bukkit.util.chat.Chat chat : this.chats.values()) {
            chat.removeWriter(user);
            chat.removeListener(user);
        }

        if (!user.agreedPrivacyPolicy()) {
            user.delete();
        }
    }

    @EventHandler
    public void onUserJoin(UserJoinEvent e) {
        de.timesnake.basic.bukkit.util.user.User user = e.getUser();
        // catch if user not in database -> error (proxy should add user)
        if (!Database.getUsers().containsUser(user.getUniqueId())) {
            user.sendMessage(
                    de.timesnake.library.extension.util.chat.Chat.getSenderPlugin(Plugin.BUKKIT)
                            .append(Component.text("§lContact a supporter!!!",
                                    ExTextColor.WARNING)));
            user.getPlayer().kick(Component.text("Contact a supporter!!!\n", ExTextColor.WARNING,
                            TextDecoration.BOLD)
                    .append(de.timesnake.library.extension.util.chat.Chat.getMessageCode("E", 805,
                            Plugin.BUKKIT))
                    .append(Component.text("\nDO NOT REJOIN", ExTextColor.WARNING,
                            TextDecoration.BOLD)));
            return;
        }

        // update online players
        Database.getServers().getServer(Bukkit.getPort())
                .setOnlinePlayers(Bukkit.getOnlinePlayers().size());

        //add user to global chat
        this.getGlobalChat().addWriter(user);
        this.getGlobalChat().addListener(user);

        // send join msg
        if (broadcastJoinQuit && !user.isAirMode()) {
            Server.broadcastMessage(Component.text(">>>", ExTextColor.DARK_GREEN)
                    .append(user.getChatNameComponent().color(ExTextColor.WHITE)));
            Server.broadcastSound(Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 200);
        } else {
            Plugin.CHATS.getLogger().info(">>>" +
                    PlainTextComponentSerializer.plainText()
                            .serialize(user.getChatNameComponent()));
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
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd,
            Arguments<Argument> args) {
        if (!sender.hasPermission(this.globalPerm)) {
            return;
        }

        String msg = args.toMessage();

        Component component;
        if (sender.hasPermission("chat.color")) {
            component = LegacyComponentSerializer.legacyAmpersand().deserialize(msg);
        } else {
            component = PlainTextComponentSerializer.plainText().deserialize(msg);
        }

        if (sender.isPlayer(false)) {
            this.getGlobalChat().broadcastMemberMessage(sender.getUser(), component);
        } else {
            this.getGlobalChat()
                    .broadcastPluginMessage(Plugin.INFO, component.color(ExTextColor.WARNING));
        }
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd,
            Arguments<Argument> args) {
        return null;
    }

    @Override
    public void loadCodes(de.timesnake.library.extension.util.chat.Plugin plugin) {
        this.globalPerm = plugin.createPermssionCode("chg", "chat.global");

    }
}
