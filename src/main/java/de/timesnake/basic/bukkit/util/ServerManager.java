package de.timesnake.basic.bukkit.util;

import de.timesnake.basic.bukkit.core.chat.ChatManager;
import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.core.server.ConsoleManager;
import de.timesnake.basic.bukkit.core.server.TaskManager;
import de.timesnake.basic.bukkit.core.user.PacketBroadcaster;
import de.timesnake.basic.bukkit.core.user.PvPManager;
import de.timesnake.basic.bukkit.core.user.UserManager;
import de.timesnake.basic.bukkit.core.user.inventory.InventoryEventManager;
import de.timesnake.basic.bukkit.core.world.EntityManager;
import de.timesnake.basic.bukkit.util.chat.Chat;
import de.timesnake.basic.bukkit.util.chat.CommandManager;
import de.timesnake.basic.bukkit.util.exceptions.WorldNotExistException;
import de.timesnake.basic.bukkit.util.permission.Group;
import de.timesnake.basic.bukkit.util.permission.GroupManager;
import de.timesnake.basic.bukkit.util.server.Info;
import de.timesnake.basic.bukkit.util.server.LoopTask;
import de.timesnake.basic.bukkit.util.server.Network;
import de.timesnake.basic.bukkit.util.server.TimeTask;
import de.timesnake.basic.bukkit.util.user.ExInventory;
import de.timesnake.basic.bukkit.util.user.ExItemStack;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.UserEventManager;
import de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardManager;
import de.timesnake.basic.bukkit.util.world.ExLocation;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import de.timesnake.basic.bukkit.util.world.HoloDisplayManager;
import de.timesnake.basic.bukkit.util.world.WorldManager;
import de.timesnake.basic.packets.util.PacketManager;
import de.timesnake.basic.packets.util.packet.ExPacketPlayOut;
import de.timesnake.channel.api.message.ChannelGroupMessage;
import de.timesnake.channel.api.message.ChannelServerMessage;
import de.timesnake.channel.channel.Channel;
import de.timesnake.channel.listener.ChannelGroupListener;
import de.timesnake.channel.listener.ChannelServerListener;
import de.timesnake.channel.main.NetworkChannel;
import de.timesnake.database.util.Database;
import de.timesnake.database.util.object.DbLocation;
import de.timesnake.database.util.object.Status;
import de.timesnake.database.util.object.TooLongEntryException;
import de.timesnake.database.util.object.Type;
import de.timesnake.database.util.server.DbServer;
import de.timesnake.library.basic.util.chat.Plugin;
import de.timesnake.library.basic.util.cmd.CommandHelp;
import de.timesnake.library.basic.util.server.Task;
import net.md_5.bungee.api.chat.ClickEvent;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class ServerManager implements de.timesnake.library.basic.util.server.Server, ChannelServerListener, ChannelGroupListener {


    private static ServerManager instance;

    public static ServerManager getInstance() {
        if (instance == null) {
            instance = new ServerManager() {
                @Override
                public User loadUser(Player player) {
                    return new User(player);
                }
            };
        }
        return instance;
    }

    public static void setInstance(ServerManager serverManager) {
        instance = serverManager;
    }

    private final ConsoleManager consoleManager = new ConsoleManager();

    private de.timesnake.basic.bukkit.util.user.UserManager userManager;

    private DbServer database;

    protected final de.timesnake.basic.bukkit.util.chat.ChatManager chatManager = new ChatManager();

    protected Network network;

    protected PacketManager packetManager;

    protected WorldManager worldManager;

    private UserEventManager userEventManager;

    private de.timesnake.basic.bukkit.util.user.InventoryEventManager inventoryEventManager;

    private CommandManager commandManager;

    private TaskManager taskManager;

    protected ScoreboardManager scoreboardManager;

    private PacketBroadcaster packetBroadcaster;

    private GroupManager groupManager;

    private Info info;

    private PvPManager pvpManager;

    protected List<CommandHelp> commandHelpList = new ArrayList<>();

    private HoloDisplayManager holoDisplayManager;

    private EntityManager entityManager;

    public final void onEnable() {
        this.database = Database.getServers().getServer(Bukkit.getPort());
        this.info = new de.timesnake.basic.bukkit.core.server.Info(this.database);
        this.userManager = new UserManager();
        this.packetManager = new PacketManager(BasicBukkit.getPlugin());
        this.groupManager = new de.timesnake.basic.bukkit.core.permission.GroupManager();
        ((de.timesnake.basic.bukkit.core.permission.GroupManager) this.groupManager).loadGroups();
        this.initNetwork();
        this.commandManager = new de.timesnake.basic.bukkit.core.chat.CommandManager();
        this.initWorldManager();
        this.userEventManager = new de.timesnake.basic.bukkit.core.user.UserEventManager();
        this.inventoryEventManager = new InventoryEventManager();
        this.taskManager = new TaskManager();
        this.packetBroadcaster = new PacketBroadcaster();
        this.pvpManager = new PvPManager();
        this.holoDisplayManager = new de.timesnake.basic.bukkit.core.world.HoloDisplayManager();
        this.entityManager = new EntityManager();
        this.initScoreboardManager();

        getChannel().addGroupListener(this);

        //game and teams

        Bukkit.getPluginManager().registerEvents((de.timesnake.basic.bukkit.core.user.UserEventManager) this.getUserEventManager(), BasicBukkit.getPlugin());
        Bukkit.getPluginManager().registerEvents((Listener) this.chatManager, BasicBukkit.getPlugin());

    }

    protected void initNetwork() {
        this.network = new de.timesnake.basic.bukkit.core.server.Network(0);
    }

    protected void initWorldManager() {
        this.worldManager = new de.timesnake.basic.bukkit.core.world.WorldManager();
    }

    protected void initScoreboardManager() {
        this.scoreboardManager = new de.timesnake.basic.bukkit.core.user.scoreboard.ScoreboardManager();
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
        this.printText(Plugin.BUKKIT, "Status: " + this.info.getStatus().getDatabaseValue());
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
        ((UserManager) this.userManager).storeUser(player.getUniqueId(), BasicBukkit.getPlugin().getServer().getScheduler().callSyncMethod(BasicBukkit.getPlugin(), () -> this.loadUser(player)));
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
    public final void broadcastMessage(String... messages) {
        this.getGlobalChat().broadcastMessage(messages);
    }

    /**
     * Sends a message to all players and console
     *
     * @param messages The message to broadcast
     * @param plugin   The plugin, who broadcasts the message
     */
    public final void broadcastMessage(Plugin plugin, String... messages) {
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
    public final void broadcastClickableMessage(String text, String exec, String info, ClickEvent.Action action) {
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
    public final void broadcastClickableMessage(Plugin plugin, String text, String exec, String info, ClickEvent.Action action) {
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
    public final void broadcastTitle(String title, String subTitle, Duration stay) {
        for (User user : this.getUsers()) {
            user.sendTitle(title, subTitle, stay);
        }
    }

    // GROUP

    /**
     * Gets the group by name
     *
     * @param group The group name from the group to get
     * @return the group
     */
    public final Group getGroup(String group) {
        return this.groupManager.getGroup(group);
    }

    /**
     * Gets the guest group
     *
     * @return the guest group
     */
    public final Group getGuestGroup() {
        return this.groupManager.getGuestGroup();
    }

    /**
     * Gets the member group
     *
     * @return the member group
     */
    public final Group getMemberGroup() {
        return this.groupManager.getMemberGroup();
    }

    /**
     * Gets all groups
     *
     * @return all groups
     */
    public final Collection<Group> getGroups() {
        return this.groupManager.getGroups();
    }

    // COMMAND

    /**
     * Runs a command by the {@link ConsoleCommandSender}
     *
     * @param cmd The command to run
     */
    public void runCommand(String cmd) {
        this.consoleManager.runCommand(cmd);
    }

    /**
     * Not implemented
     *
     * @return the command help list
     */
    public final List<CommandHelp> getCommandHelpList() {
        return commandHelpList;
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
            return new Location(world, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        } else {
            throw new WorldNotExistException(location.getWorldName());
        }
    }

    public final ExLocation getExLocationFromDbLocation(DbLocation location) throws WorldNotExistException {
        ExWorld world = Server.getWorld(location.getWorldName());
        if (world != null) {
            return new ExLocation(world, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        } else {
            throw new WorldNotExistException(location.getWorldName());
        }
    }

    public final DbLocation getDbLocationFromLocation(Location location) {
        return new DbLocation(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public final Channel getChannel() {
        return NetworkChannel.getChannel();
    }

    public final ExInventory createExInventory(int size, String name, ExItemStack... itemStacks) {
        return new de.timesnake.basic.bukkit.core.user.inventory.ExInventory(size, name, itemStacks);
    }

    public final ExInventory createExInventory(int size, String name, InventoryHolder holder, ExItemStack... itemStacks) {
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

    public final void onGroupMessage(ChannelGroupMessage msg) {
        String groupName = msg.getName();
        de.timesnake.basic.bukkit.core.permission.Group group = (de.timesnake.basic.bukkit.core.permission.Group) this.getGroup(groupName);
        if (group == null) {
            return;
        }

        if (msg.getType().equals(ChannelGroupMessage.MessageType.PERMISSION)) {
            group.updatePermissions();
        } else if (msg.getType().equals(ChannelGroupMessage.MessageType.ALIAS)) {
            group.updatePrefix();
        }
    }

    public final void printText(Plugin plugin, String text, String... subPlugins) {
        this.consoleManager.printText(plugin, text, subPlugins);
    }

    public final void printWarning(Plugin plugin, String warning, String... subPlugins) {
        this.consoleManager.printWarning(plugin, warning, subPlugins);
    }

    public final void printError(Plugin plugin, String error, String... subPlugins) {
        this.consoleManager.printError(plugin, error, subPlugins);
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

    public BukkitTask runTaskTimerSynchrony(TimeTask task, Integer time, int delay, int period, org.bukkit.plugin.Plugin plugin) {
        return this.taskManager.runTaskTimerSynchrony(task, time, delay, period, plugin);
    }

    public BukkitTask runTaskTimerAsynchrony(TimeTask task, Integer time, int delay, int period, org.bukkit.plugin.Plugin plugin) {
        return this.taskManager.runTaskTimerAsynchrony(task, time, delay, period, plugin);
    }

    public BukkitTask runTaskTimerSynchrony(TimeTask task, Integer time, boolean cancelOnZero, int delay, int period, org.bukkit.plugin.Plugin plugin) {
        return this.taskManager.runTaskTimerSynchrony(task, time, cancelOnZero, delay, period, plugin);
    }

    public BukkitTask runTaskTimerAsynchrony(TimeTask task, Integer time, boolean cancelOnZero, int delay, int period, org.bukkit.plugin.Plugin plugin) {
        return this.taskManager.runTaskTimerAsynchrony(task, time, cancelOnZero, delay, period, plugin);
    }

    public <Element> void runTaskLoopAsynchrony(LoopTask<Element> task, Iterable<Element> iterable, org.bukkit.plugin.Plugin plugin) {
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

    @Override
    public final void onServerMessage(ChannelServerMessage msg) {
        ChannelServerMessage.MessageType type = msg.getType();
        if (type.equals(ChannelServerMessage.MessageType.PERMISSION)) {
            this.updateUsersPermissions();
        } else if (type.equals(ChannelServerMessage.MessageType.COMMAND)) {
            this.runTaskSynchrony(() -> runCommand(msg.getValue()), BasicBukkit.getPlugin());
        } else if (msg.getPort().equals(this.getPort()) && type.equals(ChannelServerMessage.MessageType.STATUS)) {
            this.runTaskSynchrony(() -> ((de.timesnake.basic.bukkit.core.server.Info) this.info).updateStatus(), BasicBukkit.getPlugin());
        }
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

    public HoloDisplayManager getHoloDisplayManager() {
        return holoDisplayManager;
    }

    public PacketManager getPacketManager() {
        return packetManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
