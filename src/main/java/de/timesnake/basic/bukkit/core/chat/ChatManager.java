/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.chat;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.core.user.UserEventManager;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.ServerManager;
import de.timesnake.basic.bukkit.util.chat.ChatMember;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.chat.cmd.Argument;
import de.timesnake.basic.bukkit.util.chat.cmd.CommandListener;
import de.timesnake.basic.bukkit.util.chat.cmd.Completion;
import de.timesnake.basic.bukkit.util.chat.cmd.Sender;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;
import de.timesnake.basic.bukkit.util.user.event.UserQuitEvent;
import de.timesnake.database.util.Database;
import de.timesnake.library.basic.util.Loggers;
import de.timesnake.library.chat.Code;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;
import de.timesnake.library.network.NetworkVariables;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ChatManager implements de.timesnake.library.chat.Chat, Listener,
		de.timesnake.basic.bukkit.util.chat.ChatManager, CommandListener {

	public static final ExTextColor COLOR = ExTextColor.GRAY;

	public static final String GLOBAL_CHAT_NAME = "global";

	private final HashMap<String, de.timesnake.basic.bukkit.util.chat.Chat> chats = new HashMap<>();

	private boolean broadcastJoinQuit = true;
	private boolean broadcastDeath = true;

	private final Code globalPerm = Plugin.SERVER.createPermssionCode("chat.global");

	private final Set<UserMessageListener> userMessageListener = new HashSet<>();

	public ChatManager() {
		de.timesnake.basic.bukkit.util.chat.Chat chat = new Chat(GLOBAL_CHAT_NAME, null, null, null);
		this.chats.put(GLOBAL_CHAT_NAME, chat);

		Server.registerListener(this, BasicBukkit.getPlugin());
	}

	@Override
	public de.timesnake.basic.bukkit.util.chat.Chat createChat(String name, String displayName,
																														 ExTextColor chatColor, Set<ChatMember> users) {
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
	public void onPlayerChat(AsyncChatEvent e) {
		User user = Server.getUser(e.getPlayer());

		String msg = PlainTextComponentSerializer.plainText().serialize(e.message());

		// event
		boolean isCanceled = ((UserEventManager) Server.getUserEventManager()).onUserChat(user, e.isCancelled(), msg);

		e.setCancelled(true);

		if (isCanceled) {
			return;
		}

		//mute
		if (user.isMuted()) {
      user.asSender(Plugin.SERVER).sendMessageMuted();
			return;
		}

		// air mode
		if (user.isAirMode()) {
			return;
		}

		boolean global = false;

		if (msg.startsWith("!") && msg.length() > 1) {
      if (user.hasPermission(this.globalPerm, Plugin.SERVER)) {
				msg = msg.replaceFirst("!", "");
				global = true;
			}

		}

		for (UserMessageListener listener : this.userMessageListener) {
			msg = listener.onUserMessage(user, msg);
		}

		if (msg.isBlank()) {
			return;
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
				.append(de.timesnake.library.chat.Chat.getSplitter());
	}

	@Override
	public Component getSender(Sender sender) {
		return sender.getChatName()
				.append(de.timesnake.library.chat.Chat.getSplitter());
	}

	@Override
	public Component getLocationBlockText(Location loc) {
		return Component.text(loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() +
						" " + loc.getBlockZ(), ExTextColor.VALUE);
	}

	@Override
	public Component getLocationText(Location loc) {
		return Component.text(loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " +
				loc.getZ(), ExTextColor.VALUE);
	}

	@Override
	public Argument createArgument(Sender sender, Component component) {
		return new Argument(sender, PlainTextComponentSerializer.plainText().serialize(component));
	}

	@Override
	public Argument createArgument(Sender sender, String s) {
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
			Loggers.CHATS.info("<<<" +
					PlainTextComponentSerializer.plainText()
							.serialize(user.getChatNameComponent()));
		}
		// update online players
		int onlinePlayers = Bukkit.getOnlinePlayers().size() - 1;
		ServerManager.getInstance().getInfo().setOnlinePlayers(onlinePlayers);
		// remove from chats
		for (de.timesnake.basic.bukkit.util.chat.Chat chat : this.chats.values()) {
			chat.removeWriter(user);
			chat.removeListener(user);
		}

		if (Server.getNetwork().getVariables().getValue(NetworkVariables.PRIVACY_POLICY_LINK) != null
				&& !user.agreedPrivacyPolicy()) {
			user.delete();
		}
	}

	@EventHandler
	public void onUserJoin(UserJoinEvent e) {
		de.timesnake.basic.bukkit.util.user.User user = e.getUser();
		// catch if user not in database -> error (proxy should add user)
		if (!Database.getUsers().containsUser(user.getUniqueId())) {
			user.sendMessage(
					de.timesnake.library.chat.Chat.getSenderPlugin(Plugin.SERVER)
							.append(Component.text("§lContact a supporter!!!",
									ExTextColor.WARNING)));
			user.getPlayer().kick(Component.text("Contact a supporter!!!\n", ExTextColor.WARNING,
							TextDecoration.BOLD)
					.append(de.timesnake.library.chat.Chat.getMessageCode("E", 805,
              Plugin.SERVER))
					.append(Component.text("\nDO NOT REJOIN", ExTextColor.WARNING,
							TextDecoration.BOLD)));
			return;
		}

		// update online players
		Database.getServers().getServer(Bukkit.getPort()).setOnlinePlayers(Bukkit.getOnlinePlayers().size());

		//add user to global chat
		this.getGlobalChat().addWriter(user);
		this.getGlobalChat().addListener(user);

		// send join msg
		if (broadcastJoinQuit && !user.isAirMode()) {
			Server.broadcastMessage(Component.text(">>>", ExTextColor.DARK_GREEN)
					.append(user.getChatNameComponent().color(ExTextColor.WHITE)));
			Server.broadcastSound(Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 200);
		} else {
			Loggers.CHATS.info(">>>" + PlainTextComponentSerializer.plainText().serialize(user.getChatNameComponent()));
		}

	}

	@Override
	public void setBroadcastJoinQuit(boolean broadcast) {
		this.broadcastJoinQuit = broadcast;
	}

	@Override
	public boolean isBroadCastJoinQuit() {
		return this.broadcastJoinQuit;
	}

	@Override
	public boolean isBroadcastDeath() {
		return broadcastDeath;
	}

	@Override
	public void setBroadcastDeath(boolean broadcastDeath) {
		this.broadcastDeath = broadcastDeath;
	}

	@Override
	public void addUserMessageListener(UserMessageListener listener) {
		this.userMessageListener.add(listener);
	}

	@Override
	public void removeUserMessageListener(UserMessageListener listener) {
		this.userMessageListener.remove(listener);
	}

	@Override
	public void onCommand(Sender sender, PluginCommand cmd, Arguments<Argument> args) {
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
			User user = sender.getUser();
			if (user.isMuted()) {
				user.asSender(Plugin.SERVER).sendMessageMuted();
				return;
			}

			this.getGlobalChat().broadcastMemberMessage(user, component);
		} else {
			this.getGlobalChat().broadcastPluginMessage(Plugin.INFO, component.color(ExTextColor.WARNING));
		}
	}

	@Override
	public Completion getTabCompletion() {
		return new Completion(this.globalPerm);
	}

	@Override
	public String getPermission() {
		return this.globalPerm.getPermission();
	}
}
