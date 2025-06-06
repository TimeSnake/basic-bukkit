/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util;

import com.google.gson.GsonBuilder;
import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.core.server.FullServerInfo;
import de.timesnake.basic.bukkit.core.server.TaskManager;
import de.timesnake.basic.bukkit.core.user.UserPermissionManager;
import de.timesnake.basic.bukkit.core.world.PacketEntityManager;
import de.timesnake.basic.bukkit.util.chat.Chat;
import de.timesnake.basic.bukkit.util.chat.ChatManager;
import de.timesnake.basic.bukkit.util.chat.cmd.CommandManager;
import de.timesnake.basic.bukkit.util.exception.WorldNotExistException;
import de.timesnake.basic.bukkit.util.file.ExPolygonJsonDeserializer;
import de.timesnake.basic.bukkit.util.file.ExPolygonJsonSerializer;
import de.timesnake.basic.bukkit.util.file.MaterialJsonDeserializer;
import de.timesnake.basic.bukkit.util.file.MaterialJsonSerializer;
import de.timesnake.basic.bukkit.util.group.DisplayGroup;
import de.timesnake.basic.bukkit.util.group.GroupManager;
import de.timesnake.basic.bukkit.util.group.PermGroup;
import de.timesnake.basic.bukkit.util.server.ExTime;
import de.timesnake.basic.bukkit.util.server.Network;
import de.timesnake.basic.bukkit.util.user.PvPManager;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.UserEventManager;
import de.timesnake.basic.bukkit.util.user.UserManager;
import de.timesnake.basic.bukkit.util.user.inventory.ExInventory;
import de.timesnake.basic.bukkit.util.user.inventory.ExItemStack;
import de.timesnake.basic.bukkit.util.user.inventory.InventoryEventManager;
import de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardManager;
import de.timesnake.basic.bukkit.util.world.BlockPolygon;
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
import de.timesnake.database.util.server.DbServer;
import de.timesnake.library.basic.util.ServerType;
import de.timesnake.library.basic.util.Status;
import de.timesnake.library.chat.Plugin;
import de.timesnake.library.chat.TimeDownParser;
import de.timesnake.library.network.NetworkVariables;
import de.timesnake.library.packets.util.PacketManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minecraft.network.protocol.Packet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ServerManager implements de.timesnake.library.basic.util.server.Server,
    ChannelListener {

  public static ServerManager getInstance() {
    return instance;
  }

  /**
   * Sets the singleton instance. Should only be called within {@code onLoad()} bukkit-method.
   *
   * @param serverManager The {@link ServerManager} instance to set
   */
  public static void setInstance(ServerManager serverManager) {
    instance = serverManager;
  }

  private static ServerManager instance;

  protected final Logger logger = LogManager.getLogger("server.manager");

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
  private DbServer database;
  private UserEventManager userEventManager;
  private UserPermissionManager userPermissionManager;
  private InventoryEventManager inventoryEventManager;
  private TaskManager taskManager;
  private FullServerInfo info;
  private PacketEntityManager packetEntityManager;
  protected TimeDownParser timeDownParser;

  public final void onEnable() {
    this.timeDownParser = this.initTimeDownParser();
    this.database = Database.getServers().getServer(Bukkit.getPort());

    this.taskManager = new TaskManager();

    this.info = this.initServerInfo(this.database);
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
    this.userPermissionManager = new UserPermissionManager();
    this.inventoryEventManager = new de.timesnake.basic.bukkit.core.user.inventory.InventoryEventManager();
    this.pvpManager = this.initPvPManager();

    this.packetEntityManager = new PacketEntityManager();
    this.scoreboardManager = this.initScoreboardManager();

    this.getChannel().addListener(this, Collections.singleton(this.getName()));

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

  protected FullServerInfo initServerInfo(DbServer server) {
    return new FullServerInfo(server);
  }

  protected TimeDownParser initTimeDownParser() {
    return new TimeDownParser();
  }

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
    this.logger.info("Status: {}", this.info.getStatus().getShortName());
  }

  public ServerType getType() {
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

  public GsonBuilder getDefaultGsonBuilder() {
    return new GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .registerTypeAdapter(Material.class, new MaterialJsonSerializer())
        .registerTypeAdapter(Material.class, new MaterialJsonDeserializer())
        .registerTypeAdapter(BlockPolygon.class, new ExPolygonJsonSerializer())
        .registerTypeAdapter(BlockPolygon.class, new ExPolygonJsonDeserializer());
  }

  public final void createUser(Player player) {
    this.userManager.storeUser(player.getUniqueId(), CompletableFuture.supplyAsync(() -> this.loadUser(player)));
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
   * Gets the user who are having the status outgame, ingame, pregame, spectator and are not in
   * service mode
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
  public final void broadcastTDMessage(String... messages) {
    this.getGlobalChat().broadcastTDMessage(messages);
  }

  /**
   * Sends a message to all players and console
   *
   * @param messages The message to broadcast
   * @param plugin   The plugin, who broadcasts the message
   */
  public final void broadcastTDMessage(Plugin plugin, String... messages) {
    this.getGlobalChat().broadcastPluginTDMessage(plugin, messages);
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
  public final void broadcastClickableTDMessage(String text, String exec, String info,
                                                ClickEvent.Action action) {
    this.getGlobalChat().broadcastClickableTDMessage(text, exec, info, action);
  }

  /**
   * Sends a clickable message to all players and a message to the console
   *
   * @param text   The text to send
   * @param exec   The string to execute
   * @param info   The shown info while hovering
   * @param action The action to execute
   */
  public final void broadcastClickableMessage(Component text, String exec, Component info,
                                              ClickEvent.Action action) {
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
  public final void broadcastClickableMessage(Plugin plugin, String text, String exec,
                                              String info,
                                              ClickEvent.Action action) {
    this.getGlobalChat().broadcastClickableTDMessage(plugin, text, exec, info, action);
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
  public final void broadcastClickableMessage(Plugin plugin, Component text, String exec,
                                              Component info,
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
  public final void broadcastTDTitle(String title, String subTitle, Duration stay) {
    for (User user : this.getUsers()) {
      user.showTDTitle(title, subTitle, stay);
    }
  }

  /**
   * Sends a title to all players
   *
   * @param title    The title to send
   * @param subTitle The subtitle to send
   * @param stay     The display time of the title
   */
  public final void broadcastTitle(Component title, Component subTitle, Duration stay,
                                   Duration fadeIn, Duration fadeOut) {
    for (User user : this.getUsers()) {
      user.showTitle(title, subTitle, stay, fadeIn, fadeOut);
    }
  }

  /**
   * Sends a title to all players
   *
   * @param title    The title to send
   * @param subTitle The subtitle to send
   * @param stay     The display time of the title
   */
  public final void broadcastTDTitle(String title, String subTitle, Duration stay,
                                     Duration fadeIn, Duration fadeOut) {
    for (User user : this.getUsers()) {
      user.showTDTitle(title, subTitle, stay, fadeIn, fadeOut);
    }
  }

  public PermGroup getPermGroup(String group) {
    return groupManager.getPermGroup(group);
  }

  public PermGroup getGuestPermGroup() {
    return groupManager.getGuestPermGroup();
  }

  public PermGroup getMemberPermGroup() {
    return groupManager.getMemberPermGroup();
  }

  public Collection<PermGroup> getPermGroups() {
    return groupManager.getPermGroups();
  }

  public DisplayGroup getDisplayGroup(String group) {
    return groupManager.getDisplayGroup(group);
  }

  public Collection<DisplayGroup> getDisplayGroups() {
    return groupManager.getDisplayGroups();
  }

  public DisplayGroup getGuestDisplayGroup() {
    return groupManager.getGuestDisplayGroup();
  }

  public DisplayGroup getMemberDisplayGroup() {
    return groupManager.getMemberDisplayGroup();
  }

  /**
   * Runs a command by the {@link ConsoleCommandSender}
   *
   * @param cmd The command to run
   */
  public void runCommand(String cmd) {
    if (cmd.equalsIgnoreCase("stop")) {
      BasicBukkit.getPlugin().onDisable();
    }
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
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
   * @throws WorldNotExistException if world not exist on server
   */
  public final Location getLocationFromDbLocation(DbLocation location)
      throws WorldNotExistException {
    World world = Bukkit.getWorld(location.getWorldName());
    if (world != null) {
      return new Location(world, location.getX(), location.getY(), location.getZ(),
          location.getYaw(),
          location.getPitch());
    } else {
      throw new WorldNotExistException(location.getWorldName());
    }
  }

  public final ExLocation getExLocationFromDbLocation(DbLocation location)
      throws WorldNotExistException {
    ExWorld world = Server.getWorld(location.getWorldName());
    if (world != null) {
      return new ExLocation(world, location.getX(), location.getY(), location.getZ(),
          location.getYaw(),
          location.getPitch());
    } else {
      throw new WorldNotExistException(location.getWorldName());
    }
  }

  public final DbLocation getDbLocationFromLocation(Location location) {
    return new DbLocation(location.getWorld().getName(), location.getX(), location.getY(),
        location.getZ(),
        location.getYaw(), location.getPitch());
  }

  public final Channel getChannel() {
    return Channel.getInstance();
  }

  /**
   * @param size
   * @param name
   * @param itemStacks
   * @return
   * @deprecated in favour of {@link ExInventory}
   */
  @Deprecated
  public final ExInventory createExInventory(int size, String name, ExItemStack... itemStacks) {
    return new ExInventory(size, name, itemStacks);
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

  @ChannelHandler(type = {
      ListenerType.GROUP_ALIAS,
      ListenerType.GROUP_PERMISSION
  })
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

  public BukkitTask runTaskSynchrony(Runnable task, org.bukkit.plugin.Plugin plugin) {
    return this.taskManager.runTaskSynchrony(task, plugin);
  }

  public BukkitTask runTaskAsynchrony(Runnable task, org.bukkit.plugin.Plugin plugin) {
    return this.taskManager.runTaskAsynchrony(task, plugin);
  }

  public BukkitTask runTaskLaterSynchrony(Runnable task, int delay, org.bukkit.plugin.Plugin plugin) {
    return this.taskManager.runTaskLaterSynchrony(task, delay, plugin);
  }

  public BukkitTask runTaskLaterAsynchrony(Runnable task, int delay, org.bukkit.plugin.Plugin plugin) {
    return this.taskManager.runTaskLaterAsynchrony(task, delay, plugin);
  }

  public BukkitTask runTaskTimerSynchrony(Runnable task, int delay, int period, org.bukkit.plugin.Plugin plugin) {
    return this.taskManager.runTaskTimerSynchrony(task, delay, period, plugin);
  }

  public BukkitTask runTaskTimerAsynchrony(Runnable task, int delay, int period, org.bukkit.plugin.Plugin plugin) {
    return this.taskManager.runTaskTimerAsynchrony(task, delay, period, plugin);
  }

  public BukkitTask runTaskTimerSynchrony(Consumer<Integer> task, Integer time, int delay, int period,
                                          org.bukkit.plugin.Plugin plugin) {
    return this.taskManager.runTaskTimerSynchrony(task, time, delay, period, plugin);
  }

  public BukkitTask runTaskTimerAsynchrony(Consumer<Integer> task, Integer time, int delay, int period,
                                           org.bukkit.plugin.Plugin plugin) {
    return this.taskManager.runTaskTimerAsynchrony(task, time, delay, period, plugin);
  }

  public BukkitTask runTaskTimerSynchrony(Consumer<Integer> task, Integer time, boolean cancelOnZero, int delay,
                                          int period,
                                          org.bukkit.plugin.Plugin plugin) {
    return this.taskManager.runTaskTimerSynchrony(task, time, cancelOnZero, delay, period,
        plugin);
  }

  public BukkitTask runTaskTimerAsynchrony(Consumer<Integer> task, Integer time, boolean cancelOnZero, int delay,
                                           int period,
                                           org.bukkit.plugin.Plugin plugin) {
    return this.taskManager.runTaskTimerAsynchrony(task, time, cancelOnZero, delay, period,
        plugin);
  }

  public BukkitTask runTaskLoopSynchrony(Consumer<Integer> loopTask, Runnable endTask, ExTime delay, ExTime period,
                                         int iterations, org.bukkit.plugin.Plugin plugin) {
    return this.taskManager.runTaskLoopSynchrony(loopTask, endTask, delay, period, iterations, plugin);
  }

  public <Element> void runTaskLoopAsynchrony(Consumer<Element> task, Iterable<Element> iterable,
                                              org.bukkit.plugin.Plugin plugin) {
    this.taskManager.runTaskLoopAsynchrony(task, iterable, plugin);
  }

  public Future runTaskExpTimerSynchrony(Runnable task, int startPeriod, double speedMultiplier, int maxSpeed,
                                         boolean async) {
    return this.taskManager.runTaskExpTimerSynchrony(task, startPeriod, speedMultiplier, maxSpeed, async);
  }

  public void broadcastPacket(Packet<?> packet) {
    for (User user : this.getUsers()) {
      user.sendPacket(packet);
    }
  }

  @ChannelHandler(type = {ListenerType.SERVER_PERMISSION, ListenerType.SERVER_COMMAND,
      ListenerType.SERVER_LOAD_WORLD})
  public final void onServerMessage(ChannelServerMessage<?> msg) {
    MessageType<?> type = msg.getMessageType();
    if (type.equals(MessageType.Server.PERMISSION)) {
      this.updateUsersPermissions();
    } else if (type.equals(MessageType.Server.COMMAND)) {
      this.runTaskSynchrony(() -> runCommand((String) msg.getValue()),
          BasicBukkit.getPlugin());
    } else if (type.equals(MessageType.Server.LOAD_WORLD)) {
      this.worldManager.createWorldFromFile(((String) msg.getValue()));
    }
  }

  @ChannelHandler(type = ListenerType.SERVER_STATUS, filtered = true)
  public final void onServerStatusMessage(ChannelServerMessage<?> msg) {
    this.runTaskSynchrony(
        () -> ((FullServerInfo) this.info).updateStatus(),
        BasicBukkit.getPlugin());
  }

  public final UserEventManager getUserEventManager() {
    return userEventManager;
  }

  public final InventoryEventManager getInventoryEventManager() {
    return inventoryEventManager;
  }

  public final de.timesnake.basic.bukkit.util.user.UserManager getUserManager() {
    return this.userManager;
  }

  public final ScoreboardManager getScoreboardManager() {
    return this.scoreboardManager;
  }

  public final FullServerInfo getInfo() {
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

  public TimeDownParser getTimeDownParser() {
    return timeDownParser;
  }

  public Random getRandom() {
    return this.random;
  }

  public UserPermissionManager getUserPermissionManager() {
    return userPermissionManager;
  }

  public String getCoinsName() {
    return this.network.getVariables().getValue(NetworkVariables.COINS_NAME);
  }
}
