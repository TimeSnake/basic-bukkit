package de.timesnake.basic.bukkit.util;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.chat.Chat;
import de.timesnake.basic.bukkit.util.chat.ChatManager;
import de.timesnake.basic.bukkit.util.chat.CommandManager;
import de.timesnake.basic.bukkit.util.exceptions.WorldNotExistException;
import de.timesnake.basic.bukkit.util.permission.Group;
import de.timesnake.basic.bukkit.util.server.Info;
import de.timesnake.basic.bukkit.util.server.LoopTask;
import de.timesnake.basic.bukkit.util.server.Network;
import de.timesnake.basic.bukkit.util.server.TimeTask;
import de.timesnake.basic.bukkit.util.user.*;
import de.timesnake.basic.bukkit.util.user.scoreboard.ScoreboardManager;
import de.timesnake.basic.bukkit.util.world.*;
import de.timesnake.basic.packets.util.PacketManager;
import de.timesnake.basic.packets.util.packet.ExPacketPlayOut;
import de.timesnake.channel.core.Channel;
import de.timesnake.database.util.object.DbLocation;
import de.timesnake.database.util.object.TooLongEntryException;
import de.timesnake.database.util.object.Type;
import de.timesnake.database.util.server.DbServer;
import de.timesnake.library.basic.util.Status;
import de.timesnake.library.basic.util.chat.Plugin;
import de.timesnake.library.basic.util.server.Task;
import de.timesnake.library.extension.util.cmd.CommandHelp;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class Server implements de.timesnake.library.basic.util.server.Server {

    private static final ServerManager server = ServerManager.getInstance();

    public static int getProxyPort() {
        return 25565;
    }

    public static ChatManager getChat() {
        return server.getChatManager();
    }

    /**
     * Gets the {@link Network}
     *
     * @return the {@link Network}
     */
    public static Network getNetwork() {
        return server.getNetwork();
    }

    /**
     * Gets all server users
     *
     * @return a collection of users
     */
    public static Collection<User> getUsers() {
        return server.getUsers();
    }

    /**
     * Gets users with the given predicate
     *
     * @param predicate The {@link Predicate <User>} to filter with
     * @return a collection of users
     */
    public static Collection<User> getUsers(Predicate<User> predicate) {
        return server.getUsers(predicate);
    }

    /**
     * Gets users who are not having the status service
     *
     * @return the users
     */
    public static Collection<User> getNotServiceUsers() {
        return server.getNotServiceUsers();
    }

    /**
     * Gets users who are having the status service
     *
     * @return the users
     */
    public static Collection<User> getServiceUsers() {
        return server.getServiceUsers();
    }

    /**
     * Gets users who are having the status pregame
     *
     * @return the users
     */
    public static Collection<User> getPreGameUsers() {
        return server.getPreGameUsers();
    }

    /**
     * Gets users who are having the status ingame
     *
     * @return the users
     */
    public static Collection<User> getInGameUsers() {
        return server.getInGameUsers();
    }

    /**
     * Gets users who are having the status outgame
     *
     * @return the users
     */
    public static Collection<User> getOutGameUsers() {
        return server.getOutGameUsers();
    }

    /**
     * Gets users who are having the status outgame out ingame
     *
     * @return the users
     */
    public static Collection<User> getInOutGameUsers() {
        return server.getInOutGameUsers();
    }

    /**
     * Gets users who are having the status spectator
     *
     * @return the users
     */
    public static Collection<User> getSpectatorUsers() {
        return server.getSpectatorUsers();
    }

    /**
     * Gets users who are having the status online
     *
     * @return the users
     */
    public static Collection<User> getOnlineUsers() {
        return server.getOnlineUsers();
    }

    /**
     * Gets users who are having the status outgame, ingame, pregame, spectator
     *
     * @return the users
     */
    public static Collection<User> getGameUsers() {
        return server.getGameUsers();
    }

    /**
     * Gets the user who are having the status outgame, ingame, pregame, spectator and are not in service mode
     *
     * @return the users
     */
    public static Collection<User> getGameNotServiceUsers() {
        return server.getGameNotServiceUsers();
    }

    /**
     * Gets the user by UUID
     *
     * @param uuid The uuid of the player
     * @return the user
     */
    public static User getUser(UUID uuid) {
        return server.getUser(uuid);
    }

    /**
     * Gets the user by player
     *
     * @param p The {@link Player} to get
     * @return the user
     */
    public static User getUser(Player p) {
        return server.getUser(p);
    }

    /**
     * Sends a message to all players and console
     *
     * @param messages to broadcast
     */
    public static void broadcastMessage(String... messages) {
        server.broadcastMessage(messages);
    }

    /**
     * Sends a message to all players and console
     *
     * @param messages The messages to broadcast
     * @param plugin   The plugin, who broadcasts the message
     */
    public static void broadcastMessage(Plugin plugin, String... messages) {
        server.broadcastMessage(plugin, messages);
    }

    /**
     * Sends a message to the console as chat message
     *
     * @param message THe message to send
     */
    public static void sendChatMessageToConsole(String message) {
        server.sendChatMessageToConsole(message);
    }

    /**
     * Sends a clickable message to all players and a message to the console
     *
     * @param text   The text to send
     * @param exec   The string to execute
     * @param info   The shown info while hovering
     * @param action The action to execute
     */
    public static void broadcastClickableMessage(String text, String exec, String info, ClickEvent.Action action) {
        server.broadcastClickableMessage(text, exec, info, action);
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
    public static void broadcastClickableMessage(Plugin plugin, String text, String exec, String info, ClickEvent.Action action) {
        server.broadcastClickableMessage(plugin, text, exec, info, action);
    }

    /**
     * Sends a sound to all players
     *
     * @param sound  The sound to send
     * @param volume The volume of the sound
     */
    public static void broadcastSound(Sound sound, float volume) {
        server.broadcastSound(sound, volume);
    }

    /**
     * Sends a note to all players
     *
     * @param instrument The instrument to play
     * @param note       The note to play
     */
    public static void broadcastNote(Instrument instrument, Note note) {
        server.broadcastNote(instrument, note);
    }

    /**
     * Sends a title to all players
     *
     * @param title    The title to send
     * @param subTitle The subtitle to send
     * @param stay     The display time of the title (in ticks)
     */
    public static void broadcastTitle(String title, String subTitle, Duration stay) {
        server.broadcastTitle(title, subTitle, stay);
    }

    public static Chat getChat(String name) {
        return server.getChat(name);
    }

    public static Chat getGlobalChat() {
        return server.getGlobalChat();
    }

    /**
     * Gets the group by name
     *
     * @param group The group name from the group to get
     * @return the group
     */
    public static Group getGroup(String group) {
        return server.getGroup(group);
    }

    /**
     * Gets the guest group
     *
     * @return the guest group
     */
    public static Group getGuestGroup() {
        return server.getGuestGroup();
    }

    /**
     * Gets the member group
     *
     * @return the member group
     */
    public static Group getMemberGroup() {
        return server.getMemberGroup();
    }

    /**
     * Gets all groups
     *
     * @return all groups
     */
    public static Collection<Group> getGroups() {
        return server.getGroups();
    }

    /**
     * Gets the server port
     *
     * @return the server port
     */
    public static Integer getPort() {
        return server.getPort();
    }

    /**
     * Updates the permissions from users
     */
    public static void updateUsersPermissions() {
        server.updateUsersPermissions();
    }

    /**
     * Gets the users with an open inventory
     *
     * @param inventoryTitle The inventory title
     * @return the users with the open inventory
     */
    public static ArrayList<User> getUsersWithOpenInventory(String inventoryTitle) {
        return server.getUsersWithOpenInventory(inventoryTitle);
    }

    /**
     * Runs a command by the {@link ConsoleCommandSender}
     *
     * @param cmd The command to run
     */
    public static void runCommand(String cmd) {
        if (cmd.equalsIgnoreCase("stop")) {
            new BasicBukkit().onDisable();
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }

    public static List<CommandHelp> getCommandHelpList() {
        return server.getCommandHelpList();
    }

    public static Collection<ExWorld> getWorlds() {
        return server.getWorlds();
    }

    public static ExWorld getWorld(String name) {
        return server.getWorld(name);
    }

    public static ExWorld getWorld(World world) {
        return server.getWorld(world);
    }

    /**
     * Converts {@link DbLocation} to {@link Location}
     *
     * @param location The {@link DbLocation} to convert
     * @return The {@link Location}
     *
     * @throws WorldNotExistException if world not exist on server
     */
    public static Location getLocationFromDbLocation(DbLocation location) throws WorldNotExistException {
        return server.getLocationFromDbLocation(location);
    }

    /**
     * Converts {@link DbLocation} to {@link ExLocation}
     *
     * @param location The {@link DbLocation} to convert
     * @return The {@link ExLocation}
     *
     * @throws WorldNotExistException if world not exist on server
     */
    public static ExLocation getExLocationFromDbLocation(DbLocation location) throws WorldNotExistException {
        return server.getExLocationFromDbLocation(location);
    }

    public static DbLocation getDbLocationFromLocation(Location location) {
        return server.getDbLocationFromLocation(location);
    }

    public static Channel getChannel() {
        return server.getChannel();
    }

    public static ExInventory createExInventory(int size, String name, ExItemStack... itemStacks) {
        return server.createExInventory(size, name, itemStacks);
    }

    public static ExInventory createExInventory(int size, String name, InventoryHolder holder, ExItemStack... itemStacks) {
        return server.createExInventory(size, name, holder, itemStacks);
    }

    public static void registerListener(Listener listener, org.bukkit.plugin.Plugin plugin) {
        server.registerListener(listener, plugin);
    }

    @Deprecated
    public static void registerListener(Listener listener) {
        server.registerListener(listener);
    }


    public static void dropItem(Location location, org.bukkit.inventory.ItemStack itemStack) {
        server.dropItem(location, itemStack);
    }

    public static void printText(Plugin plugin, String text, String... subPlugins) {
        server.printText(plugin, text, subPlugins);
    }

    public static void printWarning(Plugin plugin, String warning, String... subPlugins) {
        server.printWarning(plugin, warning, subPlugins);
    }

    public static void printError(Plugin plugin, String error, String... subPlugins) {
        server.printError(plugin, error, subPlugins);
    }

    public static void printSection(Plugin plugin, String title, String... lines) {
        server.printSection(plugin, title, lines);
    }

    public static void printSection(Plugin plugin, String title, List<String> lines) {
        server.printSection(plugin, title, lines);
    }

    public static BukkitTask runTaskSynchrony(Task task, org.bukkit.plugin.Plugin plugin) {
        return server.runTaskSynchrony(task, plugin);
    }

    public static BukkitTask runTaskAsynchrony(Task task, org.bukkit.plugin.Plugin plugin) {
        return server.runTaskAsynchrony(task, plugin);
    }

    public static BukkitTask runTaskLaterSynchrony(Task task, int delay, org.bukkit.plugin.Plugin plugin) {
        return server.runTaskLaterSynchrony(task, delay, plugin);
    }

    public static BukkitTask runTaskLaterAsynchrony(Task task, int delay, org.bukkit.plugin.Plugin plugin) {
        return server.runTaskLaterAsynchrony(task, delay, plugin);
    }

    public static BukkitTask runTaskTimerSynchrony(Task task, int delay, int period, org.bukkit.plugin.Plugin plugin) {
        return server.runTaskTimerSynchrony(task, delay, period, plugin);
    }

    public static BukkitTask runTaskTimerAsynchrony(Task task, int delay, int period, org.bukkit.plugin.Plugin plugin) {
        return server.runTaskTimerAsynchrony(task, delay, period, plugin);
    }

    public static BukkitTask runTaskTimerSynchrony(TimeTask task, Integer time, int delay, int period, org.bukkit.plugin.Plugin plugin) {
        return server.runTaskTimerSynchrony(task, time, delay, period, plugin);
    }

    public static BukkitTask runTaskTimerAsynchrony(TimeTask task, Integer time, int delay, int period, org.bukkit.plugin.Plugin plugin) {
        return server.runTaskTimerAsynchrony(task, time, delay, period, plugin);
    }

    public static BukkitTask runTaskTimerSynchrony(TimeTask task, Integer time, boolean cancelOnZero, int delay, int period, org.bukkit.plugin.Plugin plugin) {
        return server.runTaskTimerSynchrony(task, time, cancelOnZero, delay, period, plugin);
    }

    public static BukkitTask runTaskTimerAsynchrony(TimeTask task, Integer time, boolean cancelOnZero, int delay, int period, org.bukkit.plugin.Plugin plugin) {
        return server.runTaskTimerAsynchrony(task, time, cancelOnZero, delay, period, plugin);
    }

    public static <Element> void runTaskLoopAsynchrony(LoopTask<Element> task, Iterable<Element> iterable, org.bukkit.plugin.Plugin plugin) {
        server.runTaskLoopAsynchrony(task, iterable, plugin);
    }

    public static void broadcastPacket(ExPacketPlayOut packet) {
        server.broadcastPacket(packet);
    }

    public static DbServer getDatabase() {
        return server.getDatabase();
    }

    public static String getName() {
        return server.getName();
    }

    public static Integer getMaxPlayers() {
        return server.getMaxPlayers();
    }

    public static String getTask() {
        return server.getTask();
    }

    public static Status.Server getStatus() {
        return server.getStatus();
    }

    public static void setStatus(Status.Server status) {
        server.setStatus(status);
    }

    public static Type.Server<?> getType() {
        return server.getType();
    }

    public static Integer getOnlinePlayers() {
        return server.getOnlinePlayers();
    }

    public static String getPassword() {
        return server.getPassword();
    }

    public static void setPassword(String password) throws TooLongEntryException {
        server.setPassword(password);
    }

    public static BossBar createBossBar(String title, BarColor color, BarStyle style) {
        return server.createBossBar(title, color, style);
    }

    public static UserEventManager getUserEventManager() {
        return server.getUserEventManager();
    }

    public static UserManager getUserManager() {
        return server.getUserManager();
    }

    public static InventoryEventManager getInventoryEventManager() {
        return server.getInventoryEventManager();
    }

    public static ScoreboardManager getScoreboardManager() {
        return server.getScoreboardManager();
    }

    public static Info getInfo() {
        return server.getInfo();
    }

    public static WorldManager getWorldManager() {
        return server.getWorldManager();
    }

    public static CommandManager getCommandManager() {
        return server.getCommandManager();
    }

    public static ChatManager getChatManager() {
        return server.getChatManager();
    }

    public static void setPvP(boolean oldPvP) {
        server.setPvP(oldPvP);
    }

    public static boolean isOldPvP() {
        return server.isOldPvp();
    }

    public static void broadcastPvPTypeMessage() {
        server.broadcastPvPTypeMessage();
    }

    public static HoloDisplayManager getHoloDisplayManager() {
        return server.getHoloDisplayManager();
    }

    public static PacketManager getPacketManager() {
        return server.getPacketManager();
    }

    public static EntityManager getEntityManager() {
        return server.getEntityManager();
    }

}
