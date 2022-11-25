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

package de.timesnake.basic.bukkit.util;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.core.server.ConsoleManager;
import de.timesnake.basic.bukkit.core.server.TaskManager;
import de.timesnake.basic.bukkit.core.user.PacketBroadcaster;
import de.timesnake.basic.bukkit.core.world.PacketEntityManager;
import de.timesnake.basic.bukkit.util.chat.Chat;
import de.timesnake.basic.bukkit.util.chat.ChatManager;
import de.timesnake.basic.bukkit.util.chat.CommandManager;
import de.timesnake.basic.bukkit.util.exception.WorldNotExistException;
import de.timesnake.basic.bukkit.util.group.DisplayGroup;
import de.timesnake.basic.bukkit.util.group.GroupManager;
import de.timesnake.basic.bukkit.util.group.PermGroup;
import de.timesnake.basic.bukkit.util.server.Info;
import de.timesnake.basic.bukkit.util.server.LoopTask;
import de.timesnake.basic.bukkit.util.server.Network;
import de.timesnake.basic.bukkit.util.server.TimeTask;
import de.timesnake.basic.bukkit.util.user.*;
import de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardManager;
import de.timesnake.basic.bukkit.util.world.ExLocation;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import de.timesnake.basic.bukkit.util.world.WorldManager;
import de.timesnake.channel.util.Channel;
import de.timesnake.channel.util.listener.ChannelHandler;
import de.timesnake.channel.util.listener.ChannelListener;
import de.timesnake.channel.util.listener.ListenerType;
import de.timesnake.channel.util.message.ChannelGroupMessage;
import de.timesnake.channel.util.message.ChannelServerMessage;
import de.timesnake.channel.util.message.MessageType;
import de.timesnake.database.util.Database;
import de.timesnake.database.util.object.DbLocation;
import de.timesnake.database.util.object.TooLongEntryException;
import de.timesnake.database.util.object.Type;
import de.timesnake.database.util.server.DbServer;
import de.timesnake.library.basic.util.Status;
import de.timesnake.library.basic.util.server.Task;
import de.timesnake.library.extension.util.chat.Plugin;
import de.timesnake.library.packets.util.PacketManager;
import de.timesnake.library.packets.util.packet.ExPacketPlayOut;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;

public class ServerManager implements de.timesnake.library.basic.util.server.Server, ChannelListener {

    public static ServerManager getInstance() {
        if (instance == null) {
            instance = new ServerManager();
        }
        return instance;
    }

    /**
     * Sets the singleton instance. Should only be called from {@code onLoad()} bukkit-method.
     *
     * @param serverManager The {@link ServerManager} instance to set
     */
    public static void setInstance(ServerManager serverManager) {
        instance = serverManager;
    }

    private static ServerManager instance;

    private final Random random = new Random();
    protected ChatManager chatManager;
    protected Network network;
    protected PacketManager packetManager;
    protected WorldManager worldManager;
    protected ScoreboardManager scoreboardManager;
    protected CommandManager commandManager;
    protected de.timesnake.basic.bukkit.util.user.PvPManager pvpManager;
    protected GroupManager groupManager;
    protected de.timesnake.basic.bukkit.util.user.UserManager userManager;
    private ConsoleManager consoleManager;
    private DbServer database;
    private UserEventManager userEventManager;
    private de.timesnake.basic.bukkit.util.user.InventoryEventManager inventoryEventManager;
    private TaskManager taskManager;
    private PacketBroadcaster packetBroadcaster;
    private Info info;
    private PacketEntityManager packetEntityManager;

    public final void onEnable() {
        this.consoleManager = new ConsoleManager();
        this.database = Database.getServers().getServer(Bukkit.getPort());

        this.taskManager = new TaskManager();

        this.info = new de.timesnake.basic.bukkit.core.server.Info(this.database);
        this.userManager = this.initUserManager();
        this.packetManager = new PacketManager(BasicBukkit.getPlugin());
        this.chatManager = this.initChatManager();

        this.groupManager = this.initGroupManager();
        ArrayList<PermGroup> groups = new ArrayList<>(this.getPermGroups());
        groups.sort(PermGroup::compareTo);
        groups.sort(Comparator.reverseOrder());
        for (PermGroup group : groups) {
            group.updatePermissions();
        }

        this.network = this.initNetwork();
        this.commandManager = this.initCommandManager();
        this.worldManager = this.initWorldManager();

        this.userEventManager = new de.timesnake.basic.bukkit.core.user.UserEventManager();
        this.inventoryEventManager = new de.timesnake.basic.bukkit.core.user.inventory.InventoryEventManager();
        this.pvpManager = this.initPvPManager();

        this.packetBroadcaster = new PacketBroadcaster();
        this.packetEntityManager = new PacketEntityManager();
        this.scoreboardManager = this.initScoreboardManager();

        this.getChannel().addListener(this, () -> Collections.singleton(this.getName()));

        Server.runTaskLaterSynchrony(this::loaded, 1, BasicBukkit.getPlugin());
    }

    public void loaded() {
        this.setStatus(Status.Server.ONLINE);
    }

    public final void onDisable() {
        if (this.worldManager instanceof de.timesnake.basic.bukkit.core.world.WorldManager) {
            ((de.timesnake.basic.bukkit.core.world.WorldManager) this.worldManager).onDisable();
        }
    }

    // INIT

    protected ChatManager initChatManager() {
        return new de.timesnake.basic.bukkit.core.chat.ChatManager();
    }

    protected Network initNetwork() {
        return new de.timesnake.basic.bukkit.core.server.Network(0);
    }

    protected WorldManager initWorldManager() {
        return new de.timesnake.basic.bukkit.core.world.WorldManager();
    }

    protected ScoreboardManager initScoreboardManager() {
        return new de.timesnake.basic.bukkit.core.user.scoreboard.ScoreboardManager();
    }

    protected CommandManager initCommandManager() {
        return new de.timesnake.basic.bukkit.core.chat.CommandManager();
    }

    protected PvPManager initPvPManager() {
        return new de.timesnake.basic.bukkit.core.user.PvPManager();
    }

    protected UserManager initUserManager() {
        return new de.timesnake.basic.bukkit.core.user.UserManager();
    }

    protected GroupManager initGroupManager() {
        return new de.timesnake.basic.bukkit.core.server.GroupManager();
    }

    // INFO

    /**
     * Gets the server port
     *
     * @return the port
     */
    public Integer getPort() {
        return this.info.getPort();
    }

    public DbServer getDatabase() {
        return this.info.getDatabase();
    }

    public String getName() {
        return this.info.getName();
    }

    public Integer getMaxPlayers() {
        return this.info.getMaxPlayers();
    }

    public String getTask() {
        return this.info.getTask();
    }

    public Status.Server getStatus() {
        return this.info.getStatus();
    }

    public void setStatus(Status.Server status) {
        this.info.setStatus(status);
        this.printText(Plugin.BUKKIT, "Status: " + this.info.getStatus().getSimpleName());
    }

    public Type.Server<?> getType() {
        return this.info.getType();
    }

    public Integer getOnlinePlayers() {
        return this.info.getOnlinePlayers();
    }

    public String getPassword() {
        return this.info.getPassword();
    }

    public void setPassword(String password) throws TooLongEntryException {
        this.info.setPassword(password);
    }


    //user

    public final void createUser(Player player) {
        this.userManager.storeUser(player.getUniqueId(),
                BasicBukkit.getPlugin().getServer().getScheduler().callSyncMethod(BasicBukkit.getPlugin(), () -> this.loadUser(player)));
    }

    public User loadUser(Player player) {
        return new User(player);
    }

    /**
     * Gets all server users
     *
     * @return a collection of users
     */
    public final Collection<User> getUsers() {
        return this.userManager.getUsers();
    }

    /**
     * Gets users with the given predicate
     *
     * @param predicate The {@link Predicate<User>} to filter with
     * @return a collection of users
     */
    public final Collection<User> getUsers(Predicate<User> predicate) {
        return this.userManager.getUsers(predicate);
    }

    /**
     * Gets users who are not having the status service
     *
     * @return a collection of users
     */
    public final Collection<User> getNotServiceUsers() {
        return this.userManager.getNotServiceUsers();
    }

    /**
     * Gets users who are having the status service
     *
     * @return a collection of users
     */
    public final Collection<User> getServiceUsers() {
        return this.userManager.getServiceUsers();
    }

    /**
     * Gets users who are having the status pregame
     *
     * @return a collection of users
     */
    public final Collection<User> getPreGameUsers() {
        return this.userManager.getPreGameUsers();
    }


    /**
     * Gets users who are having the status ingame
     *
     * @return a collection of users
     */
    public final Collection<User> getInGameUsers() {
        return this.userManager.getInGameUsers();
    }

    /**
     * Gets users who are having the status outgame
     *
     * @return a collection of users
     */
    public final Collection<User> getOutGameUsers() {
        return this.userManager.getOutGameUsers();
    }

    /**
     * Gets users who are having the status outgame out ingame
     *
     * @return the users
     */
    public Collection<User> getInOutGameUsers() {
        return this.userManager.getInOutGameUsers();
    }

    /**
     * Gets users who are having the status spectator
     *
     * @return a collection of users
     */
    public final Collection<User> getSpectatorUsers() {
        return this.userManager.getSpectatorUsers();
    }

    /**
     * Gets users who are having the status online
     *
     * @return a collection of users
     */
    public final Collection<User> getOnlineUsers() {
        return this.userManager.getOnlineUsers();
    }

    /**
     * Gets the user who are having the status outgame, ingame, pregame
     *
     * @return a collection of users
     */
    public final Collection<User> getGameUsers() {
        return this.userManager.getGameUsers();
    }

    /**
     * Gets the user who are having the status outgame, ingame, pregame, spectator and are not in service mode
     *
     * @return the users
     */
    public final Collection<User> getGameNotServiceUsers() {
        return this.userManager.getGameNotServiceUsers();
    }

    /**
     * Gets the {@link User} by {@link UUID}
     *
     * @param uuid The {@link UUID} of the user
     * @return a collection of users
     */
    public final User getUser(UUID uuid) {
        return this.userManager.getUser(uuid);
    }

    /**
     * Gets the {@link User} by {@link Player}
     *
     * @param p The {@link Player} to get
     * @return the user
     */
    public final User getUser(Player p) {
        return this.userManager.getUser(p);
    }

    /**
     * Updates the permissions from users
     */
    public final void updateUsersPermissions() {
        for (User user : this.getUsers()) {
            user.updatePermissions(true);
        }
    }

    /**
     * Gets the users with an open inventory
     *
     * @param inventoryTitle The inventory title
     * @return the users with the open inventory
     */
    @Deprecated
    public final ArrayList<User> getUsersWithOpenInventory(String inventoryTitle) {
        ArrayList<User> users = new ArrayList<>();
        for (User user : this.getUsers()) {
            InventoryView inv = user.getPlayer().getOpenInventory();
            if (inv != null) {
                if (inv.getTitle() != null) {
                    if (inv.getTitle().equals(inventoryTitle)) {
                        users.add(user);
                    }
                }
            }
        }
        return users;
    }


    // NETWORK

    /**
     * Gets the {@link Network}
     *
     * @return the {@link Network}
     */
    public final Network getNetwork() {
        return network;
    }


    // CHAT

    /**
     * Sends a message to all players and console
     *
     * @param messages to broadcast
     */
    @Deprecated
    public final void broadcastMessage(String... messages) {
        this.getGlobalChat().broadcastMessage(messages);
    }

    /**
     * Sends a message to all players and console
     *
     * @param messages The message to broadcast
     * @param plugin   The plugin, who broadcasts the message
     */
    @Deprecated
    public final void broadcastMessage(Plugin plugin, String... messages) {
        this.getGlobalChat().broadcastPluginMessage(plugin, messages);
    }

    /**
     * Sends a message to all players and console
     *
     * @param messages to broadcast
     */
    public final void broadcastMessage(Component... messages) {
        this.getGlobalChat().broadcastMessage(messages);
    }

    /**
     * Sends a message to all players and console
     *
     * @param messages The message to broadcast
     * @param plugin   The plugin, who broadcasts the message
     */
    public final void broadcastMessage(Plugin plugin, Component... messages) {
        this.getGlobalChat().broadcastPluginMessage(plugin, messages);
    }

    /**
     * Sends a clickable message to all players and a message to the console
     *
     * @param text   The text to send
     * @param exec   The string to execute
     * @param info   The shown info while hovering
     * @param action The action to execute
     */
    @Deprecated
    public final void broadcastClickableMessage(String text, String exec, String info, ClickEvent.Action action) {
        this.getGlobalChat().broadcastClickableMessage(text, exec, info, action);
    }

    /**
     * Sends a clickable message to all players and a message to the console
     *
     * @param text   The text to send
     * @param exec   The string to execute
     * @param info   The shown info while hovering
     * @param action The action to execute
     */
    public final void broadcastClickableMessage(Component text, String exec, Component info, ClickEvent.Action action) {
        this.getGlobalChat().broadcastClickableMessage(text, exec, info, action);
    }


    /**
     * Sends a clickable message to all players and a message to the console
     *
     * @param plugin The sender plugin
     * @param text   The text to send
     * @param exec   The string to execute
     * @param info   The shown info while hovering
     * @param action The action to execute
     */
    @Deprecated
    public final void broadcastClickableMessage(Plugin plugin, String text, String exec, String info,
                                                ClickEvent.Action action) {
        this.getGlobalChat().broadcastClickableMessage(plugin, text, exec, info, action);
    }

    /**
     * Sends a clickable message to all players and a message to the console
     *
     * @param plugin The sender plugin
     * @param text   The text to send
     * @param exec   The string to execute
     * @param info   The shown info while hovering
     * @param action The action to execute
     */
    public final void broadcastClickableMessage(Plugin plugin, Component text, String exec, Component info,
                                                ClickEvent.Action action) {
        this.getGlobalChat().broadcastClickableMessage(plugin, text, exec, info, action);
    }

    public Chat getChat(String name) {
        return this.chatManager.getChat(name);
    }

    public Chat getGlobalChat() {
        return this.chatManager.getGlobalChat();
    }


    /**
     * Sends a message to the console as chat message
     *
     * @param message THe message to send
     */
    public final void sendChatMessageToConsole(String message) {
        this.printText(Plugin.BUKKIT, message, "Chat");
    }

    /**
     * Sends a sound to all players
     *
     * @param sound  The sound to send
     * @param volume The volume of the sound
     */
    public final void broadcastSound(Sound sound, float volume) {
        for (User user : this.getUsers()) {
            user.playSound(sound, volume);
        }
    }

    public final void broadcastNote(Instrument instrument, Note note) {
        for (User user : this.getUsers()) {
            user.playNote(instrument, note);
        }
    }

    /**
     * Sends a title to all players
     *
     * @param title    The title to send
     * @param subTitle The subtitle to send
     * @param stay     The display time of the title
     */
    @Deprecated
    public final void broadcastTitle(String title, String subTitle, Duration stay) {
        for (User user : this.getUsers()) {
            user.showTitle(title, subTitle, stay);
        }
    }

    /**
     * Sends a title to all players
     *
     * @param title    The title to send
     * @param subTitle The subtitle to send
     * @param stay     The display time of the title
     */
    public final void broadcastTitle(Component title, Component subTitle, Duration stay) {
        for (User user : this.getUsers()) {
            user.showTitle(title, subTitle, stay);
        }
    }

    /**
     * Sends a title to all players
     *
     * @param title    The title to send
     * @param subTitle The subtitle to send
     * @param stay     The display time of the title
     */
    public final void broadcastTitle(Component title, Component subTitle, Duration stay, Duration fadeIn, Duration fadeOut) {
        for (User user : this.getUsers()) {
            user.showTitle(title, subTitle, stay, fadeIn, fadeOut);
        }
    }

    // GROUP

    public PermGroup getPermGroup(String group) {return groupManager.getPermGroup(group);}

    public PermGroup getGuestPermGroup() {return groupManager.getGuestPermGroup();}

    public PermGroup getMemberPermGroup() {return groupManager.getMemberPermGroup();}

    public Collection<PermGroup> getPermGroups() {return groupManager.getPermGroups();}

    public DisplayGroup getDisplayGroup(String group) {return groupManager.getDisplayGroup(group);}

    public Collection<DisplayGroup> getDisplayGroups() {return groupManager.getDisplayGroups();}

    public DisplayGroup getGuestDisplayGroup() {return groupManager.getGuestDisplayGroup();}

    public DisplayGroup getMemberDisplayGroup() {return groupManager.getMemberDisplayGroup();}


    // COMMAND

    /**
     * Runs a command by the {@link ConsoleCommandSender}
     *
     * @param cmd The command to run
     */
    public void runCommand(String cmd) {
        this.consoleManager.runCommand(cmd);
    }

    public Collection<ExWorld> getWorlds() {
        return this.worldManager.getWorlds();
    }

    public ExWorld getWorld(String name) {
        return this.worldManager.getWorld(name);
    }

    public ExWorld getWorld(World world) {
        return this.worldManager.getWorld(world);
    }

    /**
     * Converts {@link DbLocation} to {@link Location}
     *
     * @param location The {@link DbLocation} to convert
     * @return The {@link Location}
     *
     * @throws WorldNotExistException if world not exist on server
     */
    public final Location getLocationFromDbLocation(DbLocation location) throws WorldNotExistException {
        World world = Bukkit.getWorld(location.getWorldName());
        if (world != null) {
            return new Location(world, location.getX(), location.getY(), location.getZ(), location.getYaw(),
                    location.getPitch());
        } else {
            throw new WorldNotExistException(location.getWorldName());
        }
    }

    public final ExLocation getExLocationFromDbLocation(DbLocation location) throws WorldNotExistException {
        ExWorld world = Server.getWorld(location.getWorldName());
        if (world != null) {
            return new ExLocation(world, location.getX(), location.getY(), location.getZ(), location.getYaw(),
                    location.getPitch());
        } else {
            throw new WorldNotExistException(location.getWorldName());
        }
    }

    public final DbLocation getDbLocationFromLocation(Location location) {
        return new DbLocation(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(),
                location.getYaw(), location.getPitch());
    }

    public final Channel getChannel() {
        return Channel.getInstance();
    }

    public final ExInventory createExInventory(int size, String name, ExItemStack... itemStacks) {
        return new de.timesnake.basic.bukkit.core.user.inventory.ExInventory(size, name, itemStacks);
    }

    public final ExInventory createExInventory(int size, String name, InventoryHolder holder,
                                               ExItemStack... itemStacks) {
        return new de.timesnake.basic.bukkit.core.user.inventory.ExInventory(size, name, holder, itemStacks);
    }

    public final void registerListener(Listener listener, org.bukkit.plugin.Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    @Deprecated
    public final void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, BasicBukkit.getPlugin());
    }

    public final void dropItem(Location location, org.bukkit.inventory.ItemStack itemStack) {
        location.getWorld().dropItem(location, itemStack);
    }

    @ChannelHandler(type = ListenerType.GROUP)
    public final void onGroupMessage(ChannelGroupMessage<?> msg) {
        String groupName = msg.getName();

        if (msg.getMessageType().equals(MessageType.Group.PERMISSION)) {
            PermGroup group = this.getPermGroup(groupName);
            if (group == null) {
                return;
            }
            group.updatePermissions();
        } else if (msg.getMessageType().equals(MessageType.Group.ALIAS)) {
            DisplayGroup group = this.getDisplayGroup(groupName);
            if (group == null) {
                return;
            }
            group.updatePrefix();
        }
    }

    public final void printText(Plugin plugin, String text, String... subPlugins) {
        this.consoleManager.printText(plugin, text, subPlugins);
    }

    public final void printWarning(Plugin plugin, String warning, String... subPlugins) {
        this.consoleManager.printWarning(plugin, warning, subPlugins);
    }

    public final void printSection(Plugin plugin, String title, String... lines) {
        this.consoleManager.printSection(plugin, title, lines);
    }

    public final void printSection(Plugin plugin, String title, List<String> lines) {
        this.consoleManager.printSection(plugin, title, lines);
    }

    public BukkitTask runTaskSynchrony(Task task, org.bukkit.plugin.Plugin plugin) {
        return this.taskManager.runTaskSynchrony(task, plugin);
    }

    public BukkitTask runTaskAsynchrony(Task task, org.bukkit.plugin.Plugin plugin) {
        return this.taskManager.runTaskAsynchrony(task, plugin);
    }

    public BukkitTask runTaskLaterSynchrony(Task task, int delay, org.bukkit.plugin.Plugin plugin) {
        return this.taskManager.runTaskLaterSynchrony(task, delay, plugin);
    }

    public BukkitTask runTaskLaterAsynchrony(Task task, int delay, org.bukkit.plugin.Plugin plugin) {
        return this.taskManager.runTaskLaterAsynchrony(task, delay, plugin);
    }

    public BukkitTask runTaskTimerSynchrony(Task task, int delay, int period, org.bukkit.plugin.Plugin plugin) {
        return this.taskManager.runTaskTimerSynchrony(task, delay, period, plugin);
    }

    public BukkitTask runTaskTimerAsynchrony(Task task, int delay, int period, org.bukkit.plugin.Plugin plugin) {
        return this.taskManager.runTaskTimerAsynchrony(task, delay, period, plugin);
    }

    public BukkitTask runTaskTimerSynchrony(TimeTask task, Integer time, int delay, int period,
                                            org.bukkit.plugin.Plugin plugin) {
        return this.taskManager.runTaskTimerSynchrony(task, time, delay, period, plugin);
    }

    public BukkitTask runTaskTimerAsynchrony(TimeTask task, Integer time, int delay, int period,
                                             org.bukkit.plugin.Plugin plugin) {
        return this.taskManager.runTaskTimerAsynchrony(task, time, delay, period, plugin);
    }

    public BukkitTask runTaskTimerSynchrony(TimeTask task, Integer time, boolean cancelOnZero, int delay, int period,
                                            org.bukkit.plugin.Plugin plugin) {
        return this.taskManager.runTaskTimerSynchrony(task, time, cancelOnZero, delay, period, plugin);
    }

    public BukkitTask runTaskTimerAsynchrony(TimeTask task, Integer time, boolean cancelOnZero, int delay, int period
            , org.bukkit.plugin.Plugin plugin) {
        return this.taskManager.runTaskTimerAsynchrony(task, time, cancelOnZero, delay, period, plugin);
    }

    public <Element> void runTaskLoopAsynchrony(LoopTask<Element> task, Iterable<Element> iterable,
                                                org.bukkit.plugin.Plugin plugin) {
        this.taskManager.runTaskLoopAsynchrony(task, iterable, plugin);
    }

    public PacketBroadcaster getPacketBroadcaster() {
        return packetBroadcaster;
    }

    public void broadcastPacket(ExPacketPlayOut packet) {
        for (User user : this.getUsers()) {
            user.sendPacket(packet);
        }
    }

    @ChannelHandler(type = {ListenerType.SERVER_PERMISSION, ListenerType.SERVER_COMMAND, ListenerType.SERVER_LOAD_WORLD})
    public final void onServerMessage(ChannelServerMessage<?> msg) {
        MessageType<?> type = msg.getMessageType();
        if (type.equals(MessageType.Server.PERMISSION)) {
            this.updateUsersPermissions();
        } else if (type.equals(MessageType.Server.COMMAND)) {
            this.runTaskSynchrony(() -> runCommand((String) msg.getValue()), BasicBukkit.getPlugin());
        } else if (type.equals(MessageType.Server.LOAD_WORLD)) {
            this.worldManager.createWorldFromFile(((String) msg.getValue()));
        }
    }

    @ChannelHandler(type = ListenerType.SERVER_STATUS, filtered = true)
    public final void onServerStatusMessage(ChannelServerMessage<?> msg) {
        this.runTaskSynchrony(() -> ((de.timesnake.basic.bukkit.core.server.Info) this.info).updateStatus(),
                BasicBukkit.getPlugin());
    }

    // MANAGER

    public final UserEventManager getUserEventManager() {
        return userEventManager;
    }

    public final de.timesnake.basic.bukkit.util.user.InventoryEventManager getInventoryEventManager() {
        return inventoryEventManager;
    }

    public final de.timesnake.basic.bukkit.util.user.UserManager getUserManager() {
        return this.userManager;
    }

    public final ScoreboardManager getScoreboardManager() {
        return this.scoreboardManager;
    }

    public final Info getInfo() {
        return this.info;
    }

    public WorldManager getWorldManager() {
        return this.worldManager;
    }

    public final CommandManager getCommandManager() {
        return commandManager;
    }

    public final de.timesnake.basic.bukkit.util.chat.ChatManager getChatManager() {
        return chatManager;
    }

    public BossBar createBossBar(String title, BarColor color, BarStyle style) {
        return Bukkit.createBossBar(title, color, style);
    }

    public void setPvP(boolean oldPvP) {
        this.pvpManager.setPvP(oldPvP);
    }

    public boolean isOldPvp() {
        return this.pvpManager.isOldPvP();
    }

    public void broadcastPvPTypeMessage() {
        this.pvpManager.broadcastPvPTypeMessage();
    }

    public PacketManager getPacketManager() {
        return packetManager;
    }

    public PacketEntityManager getEntityManager() {
        return packetEntityManager;
    }

    public Random getRandom() {
        return this.random;
    }
}
