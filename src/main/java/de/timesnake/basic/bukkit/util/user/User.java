package de.timesnake.basic.bukkit.util.user;

import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.block.TargetBlockInfo;
import com.destroystokyo.paper.entity.TargetEntityInfo;
import com.destroystokyo.paper.profile.PlayerProfile;
import de.timesnake.basic.bukkit.core.chat.ExCommandSender;
import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.core.permission.ExPermission;
import de.timesnake.basic.bukkit.core.user.PvPManager;
import de.timesnake.basic.bukkit.core.user.scoreboard.ScoreboardManager;
import de.timesnake.basic.bukkit.core.user.scoreboard.TablistablePlayer;
import de.timesnake.basic.bukkit.core.world.WorldManager;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.ServerManager;
import de.timesnake.basic.bukkit.util.chat.ChatColor;
import de.timesnake.basic.bukkit.util.chat.ChatMember;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.basic.bukkit.util.permission.Group;
import de.timesnake.basic.bukkit.util.server.ServerInfo;
import de.timesnake.basic.bukkit.util.user.event.UserInventoryClickListener;
import de.timesnake.basic.bukkit.util.user.event.UserInventoryInteractListener;
import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;
import de.timesnake.basic.bukkit.util.user.event.UserQuitEvent;
import de.timesnake.basic.bukkit.util.user.scoreboard.Sideboard;
import de.timesnake.basic.bukkit.util.user.scoreboard.Tablist;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroupType;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistableGroup;
import de.timesnake.basic.bukkit.util.world.ExLocation;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import de.timesnake.basic.entities.entity.bukkit.ExPlayer;
import de.timesnake.basic.packets.util.packet.*;
import de.timesnake.channel.util.listener.ChannelHandler;
import de.timesnake.channel.util.listener.ChannelListener;
import de.timesnake.channel.util.listener.ListenerType;
import de.timesnake.channel.util.message.ChannelUserMessage;
import de.timesnake.channel.util.message.MessageType;
import de.timesnake.database.util.Database;
import de.timesnake.database.util.group.DbPermGroup;
import de.timesnake.database.util.object.DbLocation;
import de.timesnake.database.util.object.Type;
import de.timesnake.database.util.permission.DbPermission;
import de.timesnake.database.util.server.DbServer;
import de.timesnake.database.util.user.DataProtectionAgreement;
import de.timesnake.database.util.user.DbUser;
import de.timesnake.library.basic.util.Status;
import de.timesnake.library.extension.util.chat.Chat;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.pointer.Pointer;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.kyori.adventure.util.TriState;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.*;
import org.bukkit.Note.Tone;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import javax.annotation.Nonnull;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * This user will be created on {@link org.bukkit.event.player.PlayerJoinEvent}
 * Listener:
 * - For user join use the {@link UserJoinEvent} to prevent errors.
 * - For user quit use the {@link UserQuitEvent} to prevent errors.
 * - For user inventory click use the {@link UserInventoryInteractListener}.
 * - For user item interact use the {@link UserInventoryClickListener}.
 * - Many other listeners are in {@link de.timesnake.basic.bukkit.util.user.event}-package and can be used over the
 * bukkit-event api (implement {@link org.bukkit.event.Listener}).
 * All attributes are synchronized with the database.
 * It is recommended to extend these class in a new plugin
 */

public class User implements de.timesnake.library.extension.util.player.User, ChannelListener, TablistablePlayer, ChatMember {

    private final Player player;
    private final DbUser dbUser;

    private final ExPlayer exPlayer;

    private Status.User status;

    private final boolean airMode;
    private boolean service;

    private String task;

    private boolean isQuiting = false;

    protected String chatName;

    private boolean isMuted;
    private String lastChatMessage;

    private Group group;

    private String prefix;
    private String suffix;
    private String nick;

    private Tablist tablist;
    private Sideboard sideboard;
    private final HashMap<Integer, String> scores = new HashMap<>();

    private final Set<BossBar> bossBars = new HashSet<>();

    private Location lastLocation;

    private Location lockedLocation;

    private final ServerInfo lastServer;
    private final ServerInfo lastLobbyServer;

    private DataProtectionAgreement dataProtectionAgreement;

    private float coins;

    private final Set<ExPermission> permissions = new HashSet<>();

    private UserDamage lastUserDamage;

    private boolean inventoryLocked;
    private boolean inventoryItemMoveLocked;
    private boolean blockBreakPlaceLocked;

    public User(Player player) {

        this.player = player;
        UUID uuid = player.getUniqueId();
        this.dbUser = Database.getUsers().getUser(uuid);

        DbUser dbLocalUser = this.dbUser.toLocal();

        this.airMode = dbLocalUser.isAirMode();
        this.setCollitionWithEntites(!this.airMode);
        this.service = dbLocalUser.isService();


        String prefix = dbLocalUser.getPrefix();
        if (prefix != null) {
            this.prefix = prefix;
        }

        String suffix = dbLocalUser.getSuffix();
        if (suffix != null) {
            this.suffix = suffix;
        }

        String nick = dbLocalUser.getNick();
        if (nick != null) {
            this.nick = nick;
        }

        if (dbLocalUser.getPermGroup() != null) {
            String groupName = dbLocalUser.getPermGroup().getName();
            if (groupName != null) {
                this.group = Server.getGroup(groupName);
                ((de.timesnake.basic.bukkit.core.permission.Group) this.group).addUser(this);
            } else {
                Server.printError(Plugin.BUKKIT, "Error while loading group for " + dbLocalUser.getName(), "User");
                this.player.kickPlayer("§c§lContact a supporter!!!\n" + Chat.getMessageCode("E", 807, Plugin.BUKKIT) + "\nDO NOT REJOIN");
            }
        } else {
            Server.printError(Plugin.BUKKIT, "Error while loading group for " + dbLocalUser.getName(), "User");
            this.player.kickPlayer("§c§lContact a supporter!!!\n" + Chat.getMessageCode("E", 807, Plugin.BUKKIT) + "\nDO NOT REJOIN");
        }

        this.updateChatName();

        this.status = dbLocalUser.getStatus();

        this.task = dbLocalUser.getTask();

        this.updatePermissionsSync(true);

        this.updatePunishment();

        this.dataProtectionAgreement = dbLocalUser.getDataProtectionAgreement();

        this.coins = dbLocalUser.getCoins();

        if (dbUser.getServerLast() != null) {
            this.lastServer = new ServerInfo(dbUser.getServerLast());
        } else {
            this.lastServer = null;
        }
        if (dbUser.getServerLobby() != null) {
            this.lastLobbyServer = new ServerInfo(dbUser.getServerLobby());
        } else {
            this.lastLobbyServer = null;
        }

        this.inventoryLocked = false;
        this.blockBreakPlaceLocked = false;

        this.exPlayer = new ExPlayer(this.getPlayer());

        Server.getChannel().addListener(this, () -> Collections.singleton(player.getUniqueId()));
    }

    /**
     * Loads the user completely (only after join)
     */
    @Deprecated
    public void load() {
        ((WorldManager) Server.getWorldManager()).loadUserLocations(this);
        this.lastLocation = Server.getWorldManager().getUserLocation(this, this.getWorld());
    }

    public final void quit() {
        this.isQuiting = true;
        ((WorldManager) Server.getWorldManager()).saveUserLocations(this);
        Server.getChannel().removeListener(this);
        this.onQuit();
    }

    public void onQuit() {

    }

    public final boolean isQuiting() {
        return this.isQuiting;
    }

    //player

    /**
     * Gets the bukkit-player from user
     *
     * @return the {@link Player}
     */
    @Override
    public final Player getPlayer() {
        return player;
    }

    /**
     * Gets the user as a sender
     *
     * @param plugin The plugin, that will send the messages
     * @return the {@link Sender}
     */
    public Sender asSender(de.timesnake.library.basic.util.chat.Plugin plugin) {
        return new Sender(new ExCommandSender(player), plugin);
    }

    public ExPlayer asExPlayer() {
        return this.exPlayer;
    }


    /**
     * {@inheritDoc}
     */
    public final String getName() {
        return this.player.getName();
    }

    protected void updateChatName() {
        Group group = this.getGroup();

        if (this.getNick() == null) {
            String prefix = "&1";
            String suffix = "&1";


            if (this.getPrefix() != null) {
                prefix = this.getPrefix();
            }
            if (this.getSuffix() != null) {
                suffix = this.getSuffix();
            }

            this.chatName = group.getPrefixColor() + group.getPrefix() + "§r" + ChatColor.translateAlternateColorCodes('&', prefix) + "§r" + this.getPlayer().getName() + "§r" + ChatColor.translateAlternateColorCodes('&', suffix) + "§r";

        } else {
            group = Server.getMemberGroup();
            this.chatName = "§r" + group.getPrefixColor() + group.getPrefix() + "§r" + ChatColor.translateAlternateColorCodes('&', this.getNick());

        }
    }

    /**
     * Kills the user
     * Sets the health to 0
     */
    public void kill() {
        this.player.setHealth(0);
    }

    /**
     * Sets default values for user:
     * inventory clear,
     * block break, place unlock
     * inventory unlock,
     * inventory move item unlock
     * unfix location,
     * health to 20,
     * food to 20,
     * invulnerable to false,
     * flight to false,
     * (fly/walk) speed to normal,
     * gamemode to adventure,
     * level reset,
     * fire to off,
     * potionEffects remove
     */
    public void setDefault() {
        this.getInventory().clear();
        this.unlockBlocKBreakPlace();
        this.unlockInventory();
        this.unlockInventoryItemMove();
        this.lockLocation(false);
        this.setHealth(this.getMaxHealth());
        this.setFoodLevel(20);
        this.setInvulnerable(false);
        this.setAllowFlight(false);
        this.setFlying(false);
        this.setGravity(true);
        this.setFlySpeed((float) 0.2);
        this.setWalkSpeed((float) 0.2);
        this.setGameMode(GameMode.ADVENTURE);
        this.setLevel(0);
        this.setExp(0);
        this.setFireTicks(0);
        this.removePotionEffects();
    }

    public void setCollitionWithEntites(boolean collition) {
        if (!this.airMode) {
            this.player.spigot().setCollidesWithEntities(collition);
        }
    }

    public boolean isCollitionWithEntites() {
        return this.player.spigot().getCollidesWithEntities();
    }

    //database

    /**
     * Gets user database equivalent
     *
     * @return The {@link DbUser}
     */
    @Override
    public final DbUser getDatabase() {
        return this.dbUser;
    }

    //dataProtection

    /**
     * Agrees data-protection
     *
     * @param agreement The {@link DataProtectionAgreement}
     */
    public void agreeDataProtection(DataProtectionAgreement agreement) {
        this.dataProtectionAgreement = agreement;
        this.dbUser.agreeDataProtection(agreement);
    }

    /**
     * Disagrees data-protection
     * After that, the user will be kicked and deleted
     */
    public void disagreeDataProtection() {
        this.dataProtectionAgreement = null;
        this.dbUser.disagreeDataProtection();
    }

    /**
     * Gets the data-protection-agreement (date, ip, version)
     *
     * @return the {@link DataProtectionAgreement}
     */
    public DataProtectionAgreement getDataProtectionAgreement() {
        return this.dataProtectionAgreement;
    }

    /**
     * Agreed data-protection
     *
     * @return if user has agreed the data-protection
     */
    public boolean agreedDataProtection() {
        if (this.dataProtectionAgreement != null) {
            if (this.dataProtectionAgreement.getVersion() != null) {
                return this.dataProtectionAgreement.getVersion().equalsIgnoreCase(BasicBukkit.DATA_PROTECTION_VERSION);
            }
        }
        return false;
    }

    /**
     * Sends the data-protection-declaration message
     */
    public void forceDataProtectionAgreement() {
        this.sendPluginMessage(Plugin.NETWORK, ChatColor.WARNING + "Please accept our data protection declaration");
        this.sendPluginMessage(Plugin.NETWORK, ChatColor.WARNING + "Type " + ChatColor.VALUE + "/dpd agree" + ChatColor.PERSONAL + " to accept");
        this.sendPluginMessage(Plugin.NETWORK, ChatColor.WARNING + "Type " + ChatColor.VALUE + "/dpd disagree" + ChatColor.PERSONAL + " to deny");
    }

    /**
     * Deletes the database entries from the user
     */
    public void delete() {
        this.dbUser.deleteEntries();
    }

    /**
     * For debugging (not for common use)
     *
     * @return true if user is in air mode
     */
    public boolean isAirMode() {
        return airMode;
    }

    //chat

    /**
     * Sends a message to the player
     *
     * @param textComponents The {@link TextComponent}s to send
     */
    public void sendMessage(TextComponent... textComponents) {
        this.player.spigot().sendMessage(textComponents);
    }

    /**
     * Sends a plugin-message to the user
     *
     * @param plugin  The {@link Plugin} to send the message
     * @param message The message to send
     */
    public void sendPluginMessage(de.timesnake.library.basic.util.chat.Plugin plugin, String message) {
        this.getPlayer().sendMessage(Chat.getSenderPlugin(plugin) + message);
    }

    /**
     * Gets the user chat-name
     *
     * @return the chat-name
     */
    @Override
    public String getChatName() {
        return chatName;
    }

    /**
     * Gets the user prefix
     *
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the user prefix
     *
     * @param prefix The prefix to set
     */
    public void setPrefix(String prefix) {
        this.dbUser.setPrefix(prefix);
        this.prefix = prefix;
    }

    /**
     * Gets the user suffix
     *
     * @return the suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Sets the user suffix
     *
     * @param suffix The suffix to set
     */
    public void setSuffix(String suffix) {
        this.dbUser.setSuffix(suffix);
        this.suffix = suffix;
    }

    /**
     * Gets the user nick
     *
     * @return the nick
     */
    public String getNick() {
        return nick;
    }

    /**
     * Sets the user nick
     *
     * @param nick The nick to set
     */
    public void setNick(String nick) {
        this.dbUser.setNick(nick);
        this.nick = nick;
    }

    /**
     * Is user muted
     *
     * @return if is user muted
     */
    public boolean isMuted() {
        return isMuted;
    }

    /**
     * Mutes the user
     */
    public void mute() {
        this.isMuted = true;
    }

    /**
     * Unmutes the user
     */
    public void unMute() {
        this.isMuted = false;
    }

    /**
     * Gets the last user chat-message
     *
     * @return the last chat-message
     */
    public String getLastChatMessage() {
        return lastChatMessage;
    }

    /**
     * Sets the last user chat-message
     *
     * @param lastChatMessage The message to set last
     */
    public void setLastChatMessage(String lastChatMessage) {
        this.lastChatMessage = lastChatMessage;
    }

    /**
     * Sends a clickable test to the user
     *
     * @param text   The test to send
     * @param exec   The execution, if the user clicked on the text
     * @param info   The info to show, if the user hovers other the text
     * @param action The action to do
     */
    public void sendClickableMessage(String text, String exec, String info, Action action) {
        TextComponent tc = new TextComponent();
        tc.setText(text);
        tc.setClickEvent(new ClickEvent(action, exec));
        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(info)));
        this.spigot().sendMessage(tc);
    }

    /**
     * Sends a clickable test to the user
     *
     * @param plugin The sender plugin
     * @param text   The test to send
     * @param exec   The execution, if the user clicked on the text
     * @param info   The info to show, if the user hovers other the text
     * @param action The action to do
     */
    public void sendClickablePluginMessage(Plugin plugin, String text, String exec, String info, Action action) {
        TextComponent tc = new TextComponent();
        tc.setText(Chat.getSenderPlugin(plugin) + text);
        tc.setClickEvent(new ClickEvent(action, exec));
        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(info)));
        this.spigot().sendMessage(tc);
    }

    /**
     * Sends a title to the user, with standard fade-times
     * Fade-in time: 250 ms
     * Fade-out time : 250 ms
     *
     * @param title    The title to send
     * @param subTitle The subtitle to send
     * @param stay     The stay time in ms
     */
    public void sendTitle(String title, String subTitle, Duration stay) {
        this.showTitle(Title.title(Component.text(title), Component.text(subTitle), Title.Times.of(Duration.ofMillis(250), stay, Duration.ofMillis(250))));
    }

    /**
     * Sends a title to the user
     *
     * @param title    The title to send
     * @param subTitle The subtitle to send
     * @param fadeIn   The fade-in time
     * @param stay     The stay time
     * @param fadeOut  The fade-out time
     */
    public void sendTitle(String title, String subTitle, Integer fadeIn, Integer stay, Integer fadeOut) {
        this.sendTitle(title, subTitle, fadeIn, stay, fadeOut);
    }

    /**
     * Runs a command, executed by the user
     *
     * @param command The command to run
     */
    public void runCommand(String command) {
        Server.runTaskSynchrony(() -> {
            if (command.startsWith("/say ")) {
                Server.broadcastMessage(Server.getChat().getSenderMember(this) + command.replaceFirst("/say ", ""));
            } else {
                String cmd = command.replaceFirst("/", "");
                Bukkit.dispatchCommand(this.player, cmd);
            }
        }, BasicBukkit.getPlugin());

    }

    //sound

    /**
     * Plays a sound for the user
     *
     * @param sound  The {@link Sound} to play
     * @param volume The volume of the sound
     */

    public void playSound(Sound sound, float volume) {
        this.playSound(this.player.getLocation(), sound, volume, 2);
    }

    /**
     * Plays the standard inventory opened sound for the user
     * Instrument: piano
     * Note: octave 1, tone A
     */

    public void playSoundInventoryOpened() {
        this.playNote(Instrument.PIANO, Note.natural(1, Tone.A));
    }

    /**
     * Plays the standard item clicked sound for the user
     * Instrument: sticks
     * Note: octave 1, tone A
     */
    public void playSoundItemClicked() {
        this.playNote(Instrument.STICKS, Note.natural(1, Tone.A));
    }

    /**
     * Plays a note for the user
     *
     * @param instrument The {@link Instrument} to play
     * @param note       The {@link Note} to play
     */
    public void playNote(Instrument instrument, Note note) {
        this.playNote(this.player.getLocation(), instrument, note);
    }

    // team, group

    /**
     * Gets the user group
     *
     * @return the {@link de.timesnake.basic.bukkit.core.permission.Group}
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Updates the group of the user from the database
     * Removes user from old group and adds to new group
     * Updates permission
     */
    public void updateGroup() {
        DbPermGroup dbGroup = this.getDatabase().getPermGroup();
        if (this.group.getRank().equals(dbGroup.getRank())) {
            return;
        }

        if (this.group != null) {
            ((de.timesnake.basic.bukkit.core.permission.Group) this.group).removeUser(this);
        }

        this.group = Server.getGroup(dbGroup.getName());

        if (this.group == null) {
            this.group = Server.getGuestGroup();
            this.getDatabase().setPermGroup(this.group.getName());
            Server.getChannel().sendMessage(new ChannelUserMessage<>(this.getUniqueId(), MessageType.User.GROUP, this.group.getName()));
        }

        ((de.timesnake.basic.bukkit.core.permission.Group) this.group).addUser(this);

        //permissions are updated by proxy channel message
        this.updateAlias();

        // update tablist
        ((ScoreboardManager) Server.getScoreboardManager()).updatePlayerGroup(this);
    }

    //scoreboard

    @Override
    public boolean showInTablist() {
        return !this.isAirMode();
    }

    public void showUser(User user) {
        if (this.equals(user)) {
            return;
        }
        if (!user.isAirMode()) {
            this.showPlayer(BasicBukkit.getPlugin(), user.getPlayer());
        }
    }

    public void hideUser(User user) {
        if (this.equals(user)) {
            return;
        }
        this.hidePlayer(BasicBukkit.getPlugin(), user.getPlayer());
    }

    @Override
    public String getTablistName() {
        return this.getName();
    }

    @Override
    public String getTablistPrefix() {
        return this.getPrefix();
    }

    @Override
    public TablistableGroup getTablistGroup(TablistGroupType type) {
        if (new de.timesnake.basic.bukkit.core.permission.Group().getTeamType().equals(type)) {
            return this.getGroup();
        }
        return null;
    }

    /**
     * Adds a tablist entry to the {@link Scoreboard}
     *
     * @param player The {@link TablistablePlayer} to add
     */

    public void addTablistEntry(de.timesnake.basic.bukkit.util.user.scoreboard.TablistablePlayer player) {
        this.sendPacket(ExPacketPlayOutTablistPlayerAdd.wrap(player.getPlayer()));
    }

    /**
     * Adds a tablist entry to the {@link Scoreboard} with team
     *
     * @param player The {@link TablistablePlayer} to add
     * @param rank   The rank of the group
     */

    public void addTablistEntry(de.timesnake.basic.bukkit.util.user.scoreboard.TablistablePlayer player, String rank) {
        this.sendPacket(ExPacketPlayOutTablistTeamPlayerAdd.wrap(rank, player.getTablistName()));
        this.addTablistEntry(player);
    }

    /**
     * Adds a tablist entry to the {@link Scoreboard}
     *
     * @param name The name of the entry
     * @param head The {@link ExPacketPlayOutTablist.Head} of the entry
     */
    public void addTablistEntry(String name, ExPacketPlayOutTablist.Head head) {
        this.sendPacket(ExPacketPlayOutTablistPlayerAdd.wrap(name, head));
    }

    /**
     * Removes the user from the scoreboard (tablist) and scoreboard-team
     *
     * @param player The {@link TablistablePlayer} to remove
     */
    public void removeTablistEntry(de.timesnake.basic.bukkit.util.user.scoreboard.TablistablePlayer player) {
        this.sendPacket(ExPacketPlayOutTablistPlayerRemove.wrap(player.getPlayer()));
    }

    /**
     * Removes the user from the scoreboard (tablist) and scoreboard-team
     *
     * @param player The {@link TablistablePlayer} to remove
     * @param rank   The rank of the group
     */
    public void removeTablistEntry(de.timesnake.basic.bukkit.util.user.scoreboard.TablistablePlayer player, String rank) {
        this.removeTablistEntry(player);
        this.sendPacket(ExPacketPlayOutTablistTeamPlayerRemove.wrap(rank, player.getTablistName()));
    }

    /**
     * Sets the {@link Scoreboard} {@link DisplaySlot} player-list
     *
     * @param tablist The {@link Tablist} to set
     */
    public void setTablist(Tablist tablist) {

        if (this.tablist != null) {
            if (this.tablist.equals(tablist)) {
                return;
            }
            ((de.timesnake.basic.bukkit.core.user.scoreboard.Tablist) this.tablist).removeWatchingUser(this);
        }

        this.tablist = tablist;
        if (this.tablist == null) {
            de.timesnake.basic.bukkit.core.user.scoreboard.Tablist standard = (de.timesnake.basic.bukkit.core.user.scoreboard.Tablist) Server.getScoreboardManager().getTablist(Server.getName());
            this.setTablist(standard);
            return;
        }
        ((de.timesnake.basic.bukkit.core.user.scoreboard.Tablist) tablist).addWatchingUser(this);

    }

    /**
     * Sets the {@link Scoreboard} {@link DisplaySlot} sideboard
     *
     * @param sideboard The {@link Sideboard} to set
     */
    public void setSideboard(Sideboard sideboard) {
        if (this.sideboard != null) {
            if (this.sideboard.equals(sideboard)) {
                return;
            }
            this.sideboard.removeWatchingUser(this);

            for (Map.Entry<Integer, String> score : this.sideboard.getScores().entrySet()) {
                this.removeSideboardScore(score.getKey(), score.getValue());
            }

            this.sendPacket(ExPacketPlayOutScoreboardObjective.wrap(this.sideboard.getName(), this.sideboard.getTitle(), ExPacketPlayOutScoreboardObjective.Display.REMOVE, ExPacketPlayOutScoreboardObjective.ScoreboardType.INTEGER));


        }

        this.sideboard = sideboard;

        if (this.sideboard == null) {
            return;
        }

        sideboard.addWatchingUser(this);

        this.sendPacket(ExPacketPlayOutScoreboardObjective.wrap(this.sideboard.getName(), this.sideboard.getTitle(), ExPacketPlayOutScoreboardObjective.Display.CREATE, ExPacketPlayOutScoreboardObjective.ScoreboardType.INTEGER));

        this.sendPacket(ExPacketPlayOutScoreboardDisplayObjective.wrap(this.sideboard.getName(), ExPacketPlayOutScoreboardDisplayObjective.Slot.SIDEBOARD));

        for (Map.Entry<Integer, String> entry : sideboard.getScores().entrySet()) {
            this.setSideboardScore(entry.getKey(), entry.getValue());
        }

    }

    public void setSideboardTitle(String title) {
        if (this.sideboard == null) {
            return;
        }

        this.sendPacket(ExPacketPlayOutScoreboardObjective.wrap(this.sideboard.getName(), title, ExPacketPlayOutScoreboardObjective.Display.UPDATE, ExPacketPlayOutScoreboardObjective.ScoreboardType.INTEGER));
    }

    /**
     * Sets a {@link Scoreboard} score
     * <p>
     * <p>
     * Removes previous score in the line
     *
     * @param line The line of the score
     * @param text The text to set
     */
    public void setSideboardScore(int line, @Nonnull String text) {
        this.removeSideboardScore(line);
        this.scores.put(line, text);
        this.sendPacket(ExPacketPlayOutSideboardScoreSet.wrap(this.sideboard.getName(), line, text));
    }

    /**
     * Removes a {@link Scoreboard} score
     *
     * @param line The line to remove
     * @param text The text to remove
     */
    public void removeSideboardScore(int line, @Nonnull String text) {
        if (!this.scores.containsKey(line)) {
            return;
        }
        this.scores.remove(line);
        this.sendPacket(ExPacketPlayOutSideboardScoreRemove.wrap(this.sideboard.getName(), line, text));
    }

    /**
     * Removes a {@link Scoreboard} score
     *
     * @param line The line to remove
     */
    public void removeSideboardScore(int line) {
        this.removeSideboardScore(line, this.scores.get(line));
    }

    /**
     * Resets the user sideboard
     */
    public void resetSideboard() {
        this.setSideboard(null);

        for (Map.Entry<Integer, String> score : this.scores.entrySet()) {
            this.sendPacket(ExPacketPlayOutSideboardScoreRemove.wrap(this.sideboard.getName(), score.getKey(), score.getValue()));
        }
        this.scores.clear();
    }


    public Scoreboard getScoreboard() {
        return this.getPlayer().getScoreboard();
    }

    /**
     * Displays the text over the action-bar
     *
     * @param text The text to display
     */
    public void sendActionBarText(String text) {
        this.getPlayer().sendActionBar(Component.text(text));
    }

    /**
     * Sets a new {@link BossBar}, removes existing
     *
     * @param bossBar The {@link BossBar} to set
     */
    public void setBossBar(BossBar bossBar) {
        this.clearBossBars();
        this.bossBars.add(bossBar);
        bossBar.addPlayer(this.getPlayer());
    }

    /**
     * Adds a new {@link BossBar}
     *
     * @param bossBar The {@link BossBar} to set
     */
    public void addBossBar(BossBar bossBar) {
        this.bossBars.add(bossBar);
        bossBar.addPlayer(this.getPlayer());
    }

    /**
     * Removes a {@link BossBar}
     *
     * @param bossBar The {@link BossBar} to remove
     */
    public void removeBossBar(BossBar bossBar) {
        bossBar.removePlayer(this.getPlayer());
        this.bossBars.remove(bossBar);
    }

    /**
     * Sets a new {@link BossBar}, removes existing
     *
     * @param title The title
     * @param color The {@link BarColor}
     * @param style The {@link BarStyle}
     */
    public void setBossBar(String title, BarColor color, BarStyle style) {
        this.setBossBar(Bukkit.createBossBar(title, color, style));
    }

    /**
     * Sets the title of the first bar
     *
     * @param title The title to set
     */
    public void setBossBarTitle(String title) {
        if (!this.bossBars.isEmpty()) {
            this.bossBars.iterator().next().setTitle(title);
        }
    }

    /**
     * Sets the {@link BarColor} of the first bar
     *
     * @param color The {@link BarColor} to set
     */
    public void setBossBarColor(BarColor color) {
        if (!this.bossBars.isEmpty()) {
            this.bossBars.iterator().next().setColor(color);
        }
    }

    /**
     * Sets the {@link BarStyle} of the first bar
     *
     * @param style The {@link BarStyle} to set
     */
    public void setBossBarStyle(BarStyle style) {
        if (!this.bossBars.isEmpty()) {
            this.bossBars.iterator().next().setStyle(style);
        }
    }

    /**
     * Sets the visible of the first bar
     *
     * @param flag The flag, true shows the bar, false hides
     */
    public void setBossBarVisible(boolean flag) {
        if (!this.bossBars.isEmpty()) {
            this.bossBars.iterator().next().setVisible(flag);
        }
    }

    /**
     * Sets the bar progress of the first bar
     *
     * @param progress The progress to set
     */
    public void setBossBarProgress(double progress) {
        if (!this.bossBars.isEmpty()) {
            this.bossBars.iterator().next().setProgress(progress);
        }
    }

    /**
     * Gets the first {@link BossBar}
     *
     * @return the first {@link BossBar} if one exists, else null
     */
    public BossBar getBossBar() {
        if (!this.bossBars.isEmpty()) {
            return this.bossBars.iterator().next();
        }
        return null;
    }

    /**
     * Gets the bars
     *
     * @return the {@link Set} of {@link BossBar}s
     */
    public Set<BossBar> getBossBars() {
        return this.bossBars;
    }

    /**
     * Clears all {@link BossBar}s
     */
    public void clearBossBars() {
        for (BossBar bossBar : this.bossBars) {
            this.removeBossBar(bossBar);
        }
    }

    //service, status, task

    /**
     * Is user status service
     *
     * @return if user status is service
     */
    public boolean isService() {
        return this.service || this.isAirMode();
    }

    public void setService(boolean service) {
        if (this.service != service) {
            this.dbUser.setService(service);
        }
        this.service = service;
        this.updatePermissions(false);
    }

    /**
     * Gets the current user status
     *
     * @return the {@link Status.User}
     */
    public Status.User getStatus() {
        return this.status;
    }

    /**
     * Checks if the user is ingame
     *
     * @return true if status is equals ingame
     */
    public boolean isInGame() {
        return this.getStatus().equals(Status.User.IN_GAME);
    }

    /**
     * Sets the user status
     *
     * @param status The {@link Status.User} to set
     */
    public void setStatus(Status.User status) {
        if (this.status != status) {
            this.dbUser.setStatus(status);
        }

        this.status = status;

    }

    /**
     * Gets the current user task
     *
     * @return the task
     */
    public String getTask() {
        return this.task;
    }

    /**
     * Sets the user task
     *
     * @param task task to set (normally a game name or lobby)
     */
    public void setTask(String task) {
        this.task = task;
        this.dbUser.setTask(task);
    }

    //permissions

    /**
     * Has user permission
     *
     * @param permission The permission to has
     * @return if user has permission
     */
    public boolean hasPermission(String permission) {
        return this.player.hasPermission(permission);
    }

    /**
     * Has user permission
     * If false, the user receives an error-message with the code by the plugin
     *
     * @param permission The permission to has
     * @param code       The permission-code of the permission
     * @param plugin     The {@link Plugin} of the permission
     * @return if user has permission
     */
    public boolean hasPermission(String permission, int code, de.timesnake.library.basic.util.chat.Plugin plugin) {
        return this.asSender(plugin).hasPermission(permission, code);
    }

    /**
     * Updates user permissions async
     *
     * @param fromDatabase Set true to update from database and group
     */
    public void updatePermissions(boolean fromDatabase) {
        Server.runTaskAsynchrony(() -> this.updatePermissionsSync(fromDatabase), BasicBukkit.getPlugin());
    }

    private void updatePermissionsSync(boolean fromDatabase) {
        this.permissions.clear();

        if (fromDatabase) {
            for (DbPermission perm : Database.getUsers().getUser(getUniqueId()).getPermissions()) {
                this.permissions.add(new ExPermission(perm.getName(), perm.getMode(), perm.getServers()));
            }
        }

        if (this.group != null) {
            permissions.addAll(this.group.getPermissions());
        }

        if (fromDatabase) {
            Server.printText(Plugin.BUKKIT, "Updated permissions of user " + this.getName() + " from database", "User");
        } else {
            Server.printText(Plugin.BUKKIT, "Updated permissions of user " + this.getName(), "User");
        }

        Server.runTaskSynchrony(this::loadPermissions, BasicBukkit.getPlugin());
    }

    private void loadPermissions() {
        if (player.getEffectivePermissions() != null) {
            for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
                player.addAttachment(BasicBukkit.getPlugin()).setPermission(attachmentInfo.getPermission(), false);
            }
            if (!this.permissions.isEmpty()) {
                for (ExPermission perm : permissions) {
                    addPermission(perm);
                }
            }
            Server.printText(Plugin.BUKKIT, "Loaded permissions of user " + this.getName(), "User");
        }
    }

    /**
     * Adds the permission to user
     * Adds with status check from server and user
     *
     * @param perm The {@link ExPermission} to add
     */
    public void addPermission(ExPermission perm) {
        Status.Permission mode = perm.getMode();
        Status.Server statusServer = Server.getStatus();
        Status.User statusPlayer = this.getStatus();
        Collection<String> server = perm.getServer();
        Player p = this.player;

        if (perm.getPermission() == null) {
            return;
        }

        if (!(server == null || server.contains(Server.getName()) || server.isEmpty())) {
            return;
        }

        if (mode.equals(Status.Permission.IN_GAME)) {
            PermissionAttachment attachment = p.addAttachment(BasicBukkit.getPlugin());
            attachment.setPermission(perm.getPermission(), true);
        } else if (statusServer.equals(Status.Server.SERVICE)) {
            PermissionAttachment attachment = p.addAttachment(BasicBukkit.getPlugin());
            attachment.setPermission(perm.getPermission(), true);
        } else if (this.isService()) {
            PermissionAttachment attachment = p.addAttachment(BasicBukkit.getPlugin());
            attachment.setPermission(perm.getPermission(), true);
        } else if (mode.equals(Status.Permission.ONLINE) && (statusServer.equals(Status.Server.ONLINE) && statusPlayer.equals(Status.User.ONLINE))) {
            PermissionAttachment attachment = p.addAttachment(BasicBukkit.getPlugin());
            attachment.setPermission(perm.getPermission(), true);
        }
    }

    //network

    /**
     * Gets the server the user last on
     *
     * @return the {@link ServerInfo} of the server
     */
    public ServerInfo getLastServer() {
        return this.lastServer;
    }

    /**
     * Gets the lobby-server the user was last on
     *
     * @return the {@link ServerInfo} of the server
     */
    public ServerInfo getLastLobbyServer() {
        return this.lastLobbyServer;
    }

    /**
     * Switches the user to server
     *
     * @param server The server port to switch
     */
    public void switchToServer(Integer server) {
        Server.getNetwork().sendUserToServer(this, server);
    }

    /**
     * Switches the user to server
     *
     * @param server The server name to switch
     */
    public void switchToServer(String server) {
        Server.getNetwork().sendUserToServer(this, server);
    }

    /**
     * Switches the user to server
     *
     * @param server The {@link DbServer} to switch
     */
    public void switchToServer(DbServer server) {
        Server.getNetwork().sendUserToServer(this, server.getPort());
    }

    /**
     * Switches the user to last lobby server
     */
    public void switchToLobbyLast() {
        Server.getNetwork().sendUserToLobbyLast(this);
    }

    /**
     * Switches the user to last server
     */
    public void switchToServerLast() {
        Server.getNetwork().sendUserToServerLast(this);
    }

    public void lockBlocKBreakPlace() {
        this.blockBreakPlaceLocked = true;
    }

    public void unlockBlocKBreakPlace() {
        this.blockBreakPlaceLocked = false;
    }

    public boolean isBlockBreakPlaceLocked() {
        return this.blockBreakPlaceLocked;
    }

    //inventory

    /**
     * Locks all item clicks, drops and interacts
     */
    public void lockInventory() {
        this.inventoryLocked = true;
    }

    /**
     * Unlocks item clicks, drops and interacts
     */
    public void unlockInventory() {
        this.inventoryLocked = false;
    }

    /**
     * Checks if the inventory is locked
     *
     * @return true, if the inventory is locked
     */
    public boolean isInventoryLocked() {
        return this.inventoryLocked;
    }

    /**
     * Locks all item moves and drops
     */
    public void lockInventoryItemMove() {
        this.inventoryItemMoveLocked = true;
    }

    /**
     * Unlocks item clicks and drops
     */
    public void unlockInventoryItemMove() {
        this.inventoryItemMoveLocked = false;
    }

    /**
     * Checks if the inventory is locked against item move or drop
     *
     * @return true, if the inventory is locked
     */
    public boolean isInventoryItemMoveLocked() {
        return this.inventoryItemMoveLocked;
    }

    /**
     * Opens inventory for user
     *
     * @param inv The {@link Inventory} to open
     */
    public void openInventory(Inventory inv) {
        this.player.closeInventory();
        this.player.openInventory(inv);
    }

    /**
     * Opens inventory for user
     *
     * @param inv The {@link de.timesnake.basic.bukkit.core.user.inventory.ExInventory} to open
     */
    public void openInventory(ExInventory inv) {
        this.player.closeInventory();
        this.player.openInventory(inv.getInventory());
    }

    /**
     * Clears the user inventory
     */
    public void clearInventory() {
        this.player.getInventory().clear();
    }

    /**
     * Gets an inventory item by id
     *
     * @param id The id of the searched item
     * @return the item if found, else null
     */
    public ExItemStack getExItem(Integer id) {
        if (id == null) {
            return null;
        }

        for (ItemStack item : this.getInventory()) {
            if (item == null) continue;
            ExItemStack exItem = ExItemStack.getItem(item, true);
            if (exItem.getId().equals(id)) {
                return exItem;
            }
        }
        return null;
    }

    /**
     * Sets an item stack in inventory
     *
     * @param slot The number of the slot to set
     * @param item The {@link org.bukkit.inventory.ItemStack} to set
     */
    public void setItem(Integer slot, org.bukkit.inventory.ItemStack item) {
        this.player.getInventory().setItem(slot, item);
    }

    /**
     * Adds item stacks in inventory
     *
     * @param item The items to add
     */
    public void addItem(org.bukkit.inventory.ItemStack... item) {
        this.player.getInventory().addItem(item);
    }

    /**
     * Sets an item stack in inventory, if slot not null, when in given slot
     *
     * @param item The {@link ExItemStack} to set
     */
    public void setItem(ExItemStack item) {
        if (item.getSlot() != null) {
            this.setItem(item.getSlot(), item);
        } else {
            this.addItem(item);
        }
    }

    /**
     * Removes an item by the id with an amount of the given item
     *
     * @param item The {@link ExItemStack} to remove
     * @return true if it had enough to remove
     */
    public boolean removeCertainItemStack(ExItemStack item) {

        int remain = item.getAmount();

        for (int slot = 0; slot < this.getInventory().getSize(); slot++) {
            ItemStack i = this.getInventory().getItem(slot);
            if (i == null) {
                continue;
            }

            ExItemStack exItem = new ExItemStack(i, slot);

            if (exItem instanceof ExItemStack) {
                if (exItem.equals(item)) {
                    int delta = exItem.getAmount() - remain;
                    if (delta > 0) {
                        exItem.setAmount(delta);
                        this.setItem(exItem.getSlot(), exItem);
                        remain = 0;
                        break;
                    } else {
                        remain -= exItem.getAmount();
                        this.setItem(exItem.getSlot(), null);
                    }

                    if (remain <= 0) {
                        break;
                    }

                    this.updateInventory();
                }

            }
        }
        return remain <= 0;
    }

    /**
     * Removes an {@link ExItemStack} from inventory by id
     *
     * @param item The {@link ExItemStack} to remove
     * @return if removed the slot, else null
     */
    public Integer removeExItemStack(ExItemStack item) {
        for (int slot = 0; slot < this.getInventory().getSize(); slot++) {
            ItemStack i = this.getInventory().getItem(slot);
            if (i == null) {
                continue;
            }

            ExItemStack exItem = new ExItemStack(i, slot);

            if (exItem.equals(item)) {
                this.setItem(slot, null);
                return slot;
            }
        }
        return null;
    }

    /**
     * Replaces an {@link ExItemStack} by another
     *
     * @param item    The {@link ExItemStack} to remove
     * @param replace The {@link ExItemStack} to set
     * @return if removed the slot, else null
     */
    public Integer replaceExItemStack(ExItemStack item, ExItemStack replace) {
        if (replace == null) {
            return this.removeExItemStack(item);
        }

        if (item == null) {
            for (int slot = 0; slot < this.getInventory().getSize(); slot++) {
                ItemStack i = this.getInventory().getItem(slot);
                if (i == null) {
                    this.setItem(slot, replace);
                    return slot;
                }
            }
        }

        for (int slot = 0; slot < this.getInventory().getSize(); slot++) {
            ItemStack i = this.getInventory().getItem(slot);
            if (i == null) {
                continue;
            }

            ExItemStack exItem = new ExItemStack(i, slot);

            if (exItem.equals(item)) {
                this.setItem(slot, replace);
                return slot;
            }
        }
        return null;
    }

    public boolean contains(ExItemStack item) {
        if (item == null) {
            return false;
        }

        for (ItemStack invItem : this.getInventory().getContents()) {
            if (invItem != null && item.equals(ExItemStack.getItem(invItem, false))) {
                return true;
            }
        }

        return false;
    }

    public boolean containsAtLeast(ExItemStack item) {
        return this.getInventory().containsAtLeast(item, item.getAmount());
    }

    public int containsAtLeast(ItemStack item, int amount, boolean checkCursor) {
        if (item == null) {
            return -1;
        } else if (amount <= 0) {
            return 0;
        } else {
            for (ItemStack i : this.getInventory()) {
                if (item.isSimilar(i)) {
                    amount -= i.getAmount();
                }
            }


            if (checkCursor) {
                ItemStack onCursor = this.getPlayer().getItemOnCursor();

                if (item.isSimilar(onCursor)) {
                    amount -= onCursor.getAmount();
                }
            }
        }

        return -amount;
    }

    /**
     * Fills the hot bar with an item stack
     *
     * @param item The {@link org.bukkit.inventory.ItemStack} stack to fill
     */
    public void fillHotBar(org.bukkit.inventory.ItemStack item) {
        for (int i = 0; i <= 8; i++) {
            this.setItem(i, item);
        }
    }

    public void clearArmor() {
        this.getInventory().setHelmet(null);
        this.getInventory().setChestplate(null);
        this.getInventory().setLeggings(null);
        this.getInventory().setBoots(null);
    }

    //potion, effect, level

    /**
     * Adds the potion-effect to the user, with a duration of Integer.MAX_VALUE
     *
     * @param potionEffectType The {@link PotionEffectType} to add
     * @param amplifier        The amplifier of the potion-effect
     */
    public void addPotionEffect(PotionEffectType potionEffectType, int amplifier) {
        this.player.addPotionEffect(new PotionEffect(potionEffectType, Integer.MAX_VALUE, amplifier));
    }

    /**
     * Adds the potion-effect to the user
     *
     * @param potionEffectType The {@link PotionEffectType} to add
     * @param duration         The duration of the potion-effect
     * @param amplifier        The amplifier of the potion-effect
     */
    public void addPotionEffect(PotionEffectType potionEffectType, int duration, int amplifier) {
        this.player.addPotionEffect(new PotionEffect(potionEffectType, duration, amplifier));
    }

    /**
     * Removes all {@link PotionEffectType} from the user
     */
    public void removePotionEffects() {
        for (PotionEffect potionEffect : this.player.getActivePotionEffects()) {
            this.removePotionEffect(potionEffect.getType());
        }
    }

    /**
     * Sets the user-bar to a countdown, with level as time and experience as progress
     *
     * @param current The current time to set
     * @param total   The total time to set
     */
    public void setXpCountdownBar(int current, int total) {
        this.setLevel(current);
        this.setExp((float) current / total);
    }

    /**
     * Sets the user health and food-level to 20
     */
    public void heal() {
        this.getPlayer().setHealth(this.getPlayer().getMaxHealth());
        this.getPlayer().setFoodLevel(20);
    }

    //location

    /**
     * Gets the current user location
     *
     * @return the  {@link ExLocation}
     */
    public ExLocation getExLocation() {
        return new ExLocation(this.getExWorld(), this.getLocation().getX(), this.getLocation().getY(),
         this.getLocation().getZ(), this.getLocation().getYaw(), this.getLocation().getPitch());
    }

    public boolean teleport(double x, double y, double z) {
        return this.teleport(new ExLocation(this.getWorld(), x, y, z, this.getLocation().getYaw(),
         this.getLocation().getPitch()));
    }

    /**
     * Use {@link User#getWorld()}
     *
     * @return the {@link ExWorld}
     */
    @Deprecated
    public ExWorld getExWorld() {
        return Server.getWorld(this.player.getWorld());
    }

    /**
     * Gets the current user location
     *
     * @return the {@link DbLocation}
     */
    public DbLocation getDbLocation() {
        Location loc = this.getLocation();
        return new DbLocation(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    /**
     * Teleports user to other user
     *
     * @param user The other {@link User} for teleport
     */
    public synchronized void teleport(User user) {
        this.player.teleport(user.getLocation());
    }

    /**
     * Teleports user to other player
     *
     * @param player The other {@link Player} for teleport
     */
    public synchronized void teleport(Player player) {
        this.player.teleport(player.getLocation());
    }

    /**
     * Teleports user to last location in world
     *
     * @param world The {@link ExWorld} for teleport
     */
    public synchronized void teleport(ExWorld world) {
        Location loc = Server.getWorldManager().getUserLocation(this, world);
        world.loadChunk(loc.getChunk());
        this.teleport(loc);
    }

    /**
     * Sets the last location of the user
     *
     * @param location The last {@link Location} of the user
     */
    public void setLastLocation(Location location) {
        this.lastLocation = location;
    }

    /**
     * Gets the last location of the user
     *
     * @return the last {@link Location}
     */
    public Location getLastLocation() {
        return lastLocation;
    }

    /**
     * Gets the location of a world from the user
     *
     * @param world The {@link ExWorld} for the location
     * @return the last {@link Location} of the world
     */
    @Deprecated
    public Location getWorldLocation(ExWorld world) {
        return Server.getWorldManager().getUserLocation(this, world);
    }

    public void setLastDamager(UserDamage userDamage) {
        this.lastUserDamage = userDamage;
    }

    public UserDamage getLastDamager() {
        return this.lastUserDamage;
    }

    //coins

    /**
     * Sets the coins
     *
     * @param coins       The amount of coins
     * @param sendMessage Set true to send an action-bar info message to the user
     */
    public void setCoins(float coins, boolean sendMessage) {
        this.coins = coins;
        this.dbUser.setCoins(coins);
        this.sendPluginMessage(Plugin.TIME_COINS, "§6Balance changed to " + coins + " TimeCoins");
        if (sendMessage) {
            this.sendActionBarText(Chat.roundCoinAmount(coins) + " TimeCoins");
        }
    }

    /**
     * Adds coins to current value
     *
     * @param coins       The amount of coins to add
     * @param sendMessage Set true to send an action-bar info message to the user
     */
    public void addCoins(float coins, boolean sendMessage) {
        coins = coins >= 0 ? coins : -coins;
        this.coins += coins;
        this.dbUser.addCoins(coins);
        if (sendMessage) {
            this.sendActionBarText("§6+ " + Chat.roundCoinAmount(coins) + " TimeCoins");
        }
    }

    /**
     * Removes coins from current value
     *
     * @param coins       The amount of coins to remove
     * @param sendMessage Set true to send an action-bar info message to the user
     */
    public void removeCoins(float coins, boolean sendMessage) {
        coins = coins >= 0 ? coins : -coins;
        this.coins -= coins;
        this.dbUser.removeCoins(coins);
        if (sendMessage) {
            this.sendActionBarText("§6 -" + Chat.roundCoinAmount(coins) + " TimeCoins");
        }
    }

    /**
     * Gets the current value of coins
     *
     * @return the value of coins
     */
    public float getCoins() {
        return this.coins;
    }

    /**
     * Locks the location of the user
     *
     * @param lock enable or disable location lock
     */
    public synchronized void lockLocation(boolean lock) {
        if (lock) {
            this.setWalkSpeed(Float.MIN_VALUE);
            this.setFlySpeed(Float.MIN_VALUE);
            this.setAllowFlight(true);
            this.setFlying(true);
            this.lockedLocation = this.getLocation();
        } else {
            this.setWalkSpeed((float) 0.2);
            this.setFlySpeed((float) 0.2);
            this.setAllowFlight(false);
            this.setFlying(false);
            this.lockedLocation = null;
        }
    }

    public boolean isLocationLocked() {
        return lockedLocation != null;
    }

    //update
    @ChannelHandler(type = ListenerType.USER, filtered = true)
    public void onUserMessage(ChannelUserMessage<?> msg) {
        MessageType<?> type = msg.getMessageType();
        if (type.equals(MessageType.User.STATUS)) {
            this.status = this.getDatabase().getStatus();
            this.updatePermissions(false);
        } else if (type.equals(MessageType.User.SERVICE)) {
            this.service = this.dbUser.isService();
        } else if (type.equals(MessageType.User.SOUND)) {
            ChannelUserMessage.Sound sound = (ChannelUserMessage.Sound) msg.getValue();
            switch (sound) {
                case PLING -> this.playNote(Instrument.PLING, Note.natural(1, Tone.C));
                case PLONG -> this.playNote(Instrument.PLING, Note.natural(0, Tone.C));
            }
        } else if (type.equals(MessageType.User.PERMISSION)) {
            this.updatePermissions(true);
        } else if (type.equals(MessageType.User.PUNISH)) {
            this.updatePunishment();
        } else if (type.equals(MessageType.User.ALIAS)) {
            this.updateAlias();
        } else if (type.equals(MessageType.User.TASK)) {
            this.task = this.dbUser.getTask();
        } else if (type.equals(MessageType.User.COMMAND)) {
            this.runCommand((String) msg.getValue());
        } else if (type.equals(MessageType.User.GROUP)) {
            this.updateGroup();
        }
    }

    /**
     * Updates the user alias (prefix, suffix, nick) from database and chat-name
     */
    public void updateAlias() {
        String prefix = this.dbUser.getPrefix();
        if (prefix != null) {
            this.prefix = prefix;
        }

        String suffix = this.dbUser.getSuffix();
        if (suffix != null) {
            this.suffix = suffix;
        }

        String nick = this.dbUser.getNick();
        if (nick != null) {
            this.nick = nick;
        }

        this.updateChatName();
    }

    /**
     * Updates the punishment from database (only mute)
     */
    public void updatePunishment() {
        Type.Punishment type = this.dbUser.getPunishment().getType();
        if (type != null) {
            if (type.equals(Type.Punishment.MUTE)) {
                this.isMuted = true;
            }
        } else {
            this.isMuted = false;
        }
    }

    public void sendPacket(ExPacketPlayOut packet) {
        if (packet == null) {
            return;
        }
        if (this.isQuiting) {
            return;
        }

        Server.getPacketManager().sendPacket(this.getPlayer(), packet);
        ServerManager.getInstance().getPacketBroadcaster().broadcastPacket(this.getPlayer(), packet);
    }

    public void setPvpMode(boolean oldPvP) {
        AttributeInstance speed = this.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
        AttributeInstance damage = this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);

        if (oldPvP) {
            speed.setBaseValue(PvPManager.FULL_ATTACKS_PER_SECOND);
            damage.setBaseValue(PvPManager.ATTACK_DAMAGE);
        } else {
            speed.setBaseValue(4);
            damage.setBaseValue(2);
        }
    }

    public void setPvpMode(boolean oldPvP, double fullAttacksPerSecond) {
        AttributeInstance speed = this.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
        AttributeInstance damage = this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);

        if (oldPvP) {
            speed.setBaseValue(fullAttacksPerSecond);
            damage.setBaseValue(PvPManager.ATTACK_DAMAGE);
        } else {
            speed.setBaseValue(4);
            damage.setBaseValue(2);
        }
    }


    // overwritten delegated methods

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    // player delegation


    public @NotNull Identity identity() {
        return player.identity();
    }

    @NotNull
    public Component displayName() {
        return player.displayName();
    }

    public void displayName(@Nullable Component component) {
        player.displayName(component);
    }

    @Deprecated
    @NotNull
    public String getDisplayName() {
        return player.getDisplayName();
    }

    @Deprecated
    public void setDisplayName(@Nullable String s) {
        player.setDisplayName(s);
    }

    public void playerListName(@Nullable Component component) {
        player.playerListName(component);
    }

    @NotNull
    public Component playerListName() {
        return player.playerListName();
    }

    @Nullable
    public Component playerListHeader() {
        return player.playerListHeader();
    }

    @Nullable
    public Component playerListFooter() {
        return player.playerListFooter();
    }

    @Deprecated
    @NotNull
    public String getPlayerListName() {
        return player.getPlayerListName();
    }

    @Deprecated
    public void setPlayerListName(@Nullable String s) {
        player.setPlayerListName(s);
    }

    @Deprecated
    @Nullable
    public String getPlayerListHeader() {
        return player.getPlayerListHeader();
    }

    @Deprecated
    @Nullable
    public String getPlayerListFooter() {
        return player.getPlayerListFooter();
    }

    @Deprecated
    public void setPlayerListHeader(@Nullable String s) {
        player.setPlayerListHeader(s);
    }

    @Deprecated
    public void setPlayerListFooter(@Nullable String s) {
        player.setPlayerListFooter(s);
    }

    @Deprecated
    public void setPlayerListHeaderFooter(@Nullable String s, @Nullable String s1) {
        player.setPlayerListHeaderFooter(s, s1);
    }

    public void setCompassTarget(@NotNull Location location) {
        player.setCompassTarget(location);
    }

    @NotNull
    public Location getCompassTarget() {
        return player.getCompassTarget();
    }

    public @Nullable InetSocketAddress getAddress() {
        return player.getAddress();
    }

    public void sendRawMessage(@NotNull String s) {
        player.sendRawMessage(s);
    }

    @Deprecated
    public void kickPlayer(@Nullable String s) {
        player.kickPlayer(s);
    }

    public void kick(@Nullable Component component) {
        player.kick(component);
    }

    public void kick(@Nullable Component component, PlayerKickEvent.@NotNull Cause cause) {
        player.kick(component, cause);
    }

    public void chat(@NotNull String s) {
        player.chat(s);
    }

    public boolean performCommand(@NotNull String s) {
        return player.performCommand(s);
    }

    @Deprecated
    public boolean isOnGround() {
        return player.isOnGround();
    }

    public boolean isSneaking() {
        return player.isSneaking();
    }

    public void setSneaking(boolean b) {
        player.setSneaking(b);
    }

    public boolean isSprinting() {
        return player.isSprinting();
    }

    public void setSprinting(boolean b) {
        player.setSprinting(b);
    }

    public void saveData() {
        player.saveData();
    }

    public void loadData() {
        player.loadData();
    }

    public void setSleepingIgnored(boolean b) {
        player.setSleepingIgnored(b);
    }

    public boolean isSleepingIgnored() {
        return player.isSleepingIgnored();
    }

    @Nullable
    public Location getBedSpawnLocation() {
        return player.getBedSpawnLocation();
    }

    public void setBedSpawnLocation(@Nullable Location location) {
        player.setBedSpawnLocation(location);
    }

    public void setBedSpawnLocation(@Nullable Location location, boolean b) {
        player.setBedSpawnLocation(location, b);
    }

    @Deprecated
    public void playNote(@NotNull Location location, byte b, byte b1) {
        player.playNote(location, b, b1);
    }

    public void playNote(@NotNull Location location, @NotNull Instrument instrument, @NotNull Note note) {
        player.playNote(location, instrument, note);
    }

    public void playSound(@NotNull Location location, @NotNull Sound sound, float v, float v1) {
        player.playSound(location, sound, v, v1);
    }

    public void playSound(@NotNull Location location, @NotNull String s, float v, float v1) {
        player.playSound(location, s, v, v1);
    }

    public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory soundCategory, float v, float v1) {
        player.playSound(location, sound, soundCategory, v, v1);
    }

    public void playSound(@NotNull Location location, @NotNull String s, @NotNull SoundCategory soundCategory, float v, float v1) {
        player.playSound(location, s, soundCategory, v, v1);
    }

    public void playSound(@NotNull Entity entity, @NotNull Sound sound, float v, float v1) {
        player.playSound(entity, sound, v, v1);
    }

    public void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory soundCategory, float v, float v1) {
        player.playSound(entity, sound, soundCategory, v, v1);
    }

    public void stopSound(@NotNull Sound sound) {
        player.stopSound(sound);
    }

    public void stopSound(@NotNull String s) {
        player.stopSound(s);
    }

    public void stopSound(@NotNull Sound sound, @Nullable SoundCategory soundCategory) {
        player.stopSound(sound, soundCategory);
    }

    public void stopSound(@NotNull String s, @Nullable SoundCategory soundCategory) {
        player.stopSound(s, soundCategory);
    }

    public void stopAllSounds() {
        player.stopAllSounds();
    }

    @Deprecated
    public void playEffect(@NotNull Location location, @NotNull Effect effect, int i) {
        player.playEffect(location, effect, i);
    }

    public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T t) {
        player.playEffect(location, effect, t);
    }

    public boolean breakBlock(@NotNull Block block) {
        return player.breakBlock(block);
    }

    @Deprecated
    public void sendBlockChange(@NotNull Location location, @NotNull Material material, byte b) {
        player.sendBlockChange(location, material, b);
    }

    public void sendBlockChange(@NotNull Location location, @NotNull BlockData blockData) {
        player.sendBlockChange(location, blockData);
    }

    public void sendBlockDamage(@NotNull Location location, float v) {
        player.sendBlockDamage(location, v);
    }

    public void sendEquipmentChange(@NotNull LivingEntity livingEntity, @NotNull EquipmentSlot equipmentSlot, @NotNull ItemStack itemStack) {
        player.sendEquipmentChange(livingEntity, equipmentSlot, itemStack);
    }

    public void sendSignChange(@NotNull Location loc, @Nullable List<Component> lines) throws IllegalArgumentException {
        player.sendSignChange(loc, lines);
    }

    public void sendSignChange(@NotNull Location loc, @Nullable List<Component> lines, @NotNull DyeColor dyeColor) throws IllegalArgumentException {
        player.sendSignChange(loc, lines, dyeColor);
    }

    public void sendSignChange(@NotNull Location loc, @Nullable List<Component> lines, boolean hasGlowingText) throws IllegalArgumentException {
        player.sendSignChange(loc, lines, hasGlowingText);
    }

    public void sendSignChange(@NotNull Location location, @Nullable List<Component> list, @NotNull DyeColor dyeColor, boolean b) throws IllegalArgumentException {
        player.sendSignChange(location, list, dyeColor, b);
    }

    @Deprecated
    public void sendSignChange(@NotNull Location location, @Nullable String[] strings) throws IllegalArgumentException {
        player.sendSignChange(location, strings);
    }

    @Deprecated
    public void sendSignChange(@NotNull Location location, @Nullable String[] strings, @NotNull DyeColor dyeColor) throws IllegalArgumentException {
        player.sendSignChange(location, strings, dyeColor);
    }

    @Deprecated
    public void sendSignChange(@NotNull Location location, @Nullable String[] strings, @NotNull DyeColor dyeColor, boolean b) throws IllegalArgumentException {
        player.sendSignChange(location, strings, dyeColor, b);
    }

    public void sendMap(@NotNull MapView mapView) {
        player.sendMap(mapView);
    }

    @Nullable
    public BanEntry banPlayerFull(@Nullable String reason) {
        return player.banPlayerFull(reason);
    }

    @Nullable
    public BanEntry banPlayerFull(@Nullable String reason, @Nullable String source) {
        return player.banPlayerFull(reason, source);
    }

    @Nullable
    public BanEntry banPlayerFull(@Nullable String reason, @Nullable Date expires) {
        return player.banPlayerFull(reason, expires);
    }

    @Nullable
    public BanEntry banPlayerFull(@Nullable String reason, @Nullable Date expires, @Nullable String source) {
        return player.banPlayerFull(reason, expires, source);
    }

    @Nullable
    public BanEntry banPlayerIP(@Nullable String reason, boolean kickPlayer) {
        return player.banPlayerIP(reason, kickPlayer);
    }

    @Nullable
    public BanEntry banPlayerIP(@Nullable String reason, @Nullable String source, boolean kickPlayer) {
        return player.banPlayerIP(reason, source, kickPlayer);
    }

    @Nullable
    public BanEntry banPlayerIP(@Nullable String reason, @Nullable Date expires, boolean kickPlayer) {
        return player.banPlayerIP(reason, expires, kickPlayer);
    }

    @Nullable
    public BanEntry banPlayerIP(@Nullable String reason) {
        return player.banPlayerIP(reason);
    }

    @Nullable
    public BanEntry banPlayerIP(@Nullable String reason, @Nullable String source) {
        return player.banPlayerIP(reason, source);
    }

    @Nullable
    public BanEntry banPlayerIP(@Nullable String reason, @Nullable Date expires) {
        return player.banPlayerIP(reason, expires);
    }

    @Nullable
    public BanEntry banPlayerIP(@Nullable String reason, @Nullable Date expires, @Nullable String source) {
        return player.banPlayerIP(reason, expires, source);
    }

    @Nullable
    public BanEntry banPlayerIP(@Nullable String reason, @Nullable Date expires, @Nullable String source, boolean kickPlayer) {
        return player.banPlayerIP(reason, expires, source, kickPlayer);
    }

    @Deprecated
    public void sendActionBar(@NotNull String s) {
        player.sendActionBar(s);
    }

    @Deprecated
    public void sendActionBar(char c, @NotNull String s) {
        player.sendActionBar(c, s);
    }

    @Deprecated
    public void sendActionBar(@NotNull BaseComponent... baseComponents) {
        player.sendActionBar(baseComponents);
    }

    @Deprecated
    public void sendMessage(@NotNull BaseComponent component) {
        player.sendMessage(component);
    }

    @Deprecated
    public void sendMessage(@NotNull BaseComponent... components) {
        player.sendMessage(components);
    }

    @Deprecated
    public void sendMessage(ChatMessageType position, BaseComponent... components) {
        player.sendMessage(position, components);
    }

    @Deprecated
    public void setPlayerListHeaderFooter(@Nullable BaseComponent[] baseComponents, @Nullable BaseComponent[] baseComponents1) {
        player.setPlayerListHeaderFooter(baseComponents, baseComponents1);
    }

    @Deprecated
    public void setPlayerListHeaderFooter(@Nullable BaseComponent baseComponent, @Nullable BaseComponent baseComponent1) {
        player.setPlayerListHeaderFooter(baseComponent, baseComponent1);
    }

    @Deprecated
    public void setTitleTimes(int i, int i1, int i2) {
        player.setTitleTimes(i, i1, i2);
    }

    @Deprecated
    public void setSubtitle(BaseComponent[] baseComponents) {
        player.setSubtitle(baseComponents);
    }

    @Deprecated
    public void setSubtitle(BaseComponent baseComponent) {
        player.setSubtitle(baseComponent);
    }

    @Deprecated
    public void showTitle(@Nullable BaseComponent[] baseComponents) {
        player.showTitle(baseComponents);
    }

    @Deprecated
    public void showTitle(@Nullable BaseComponent baseComponent) {
        player.showTitle(baseComponent);
    }

    @Deprecated
    public void showTitle(@Nullable BaseComponent[] baseComponents, @Nullable BaseComponent[] baseComponents1, int i, int i1, int i2) {
        player.showTitle(baseComponents, baseComponents1, i, i1, i2);
    }

    @Deprecated
    public void showTitle(@Nullable BaseComponent baseComponent, @Nullable BaseComponent baseComponent1, int i, int i1, int i2) {
        player.showTitle(baseComponent, baseComponent1, i, i1, i2);
    }

    @Deprecated
    public void sendTitle(com.destroystokyo.paper.@NotNull Title title) {
        player.sendTitle(title);
    }

    @Deprecated
    public void updateTitle(com.destroystokyo.paper.@NotNull Title title) {
        player.updateTitle(title);
    }

    @Deprecated
    public void hideTitle() {
        player.hideTitle();
    }

    public void updateInventory() {
        player.updateInventory();
    }

    public void setPlayerTime(long l, boolean b) {
        player.setPlayerTime(l, b);
    }

    public long getPlayerTime() {
        return player.getPlayerTime();
    }

    public long getPlayerTimeOffset() {
        return player.getPlayerTimeOffset();
    }

    public boolean isPlayerTimeRelative() {
        return player.isPlayerTimeRelative();
    }

    public void resetPlayerTime() {
        player.resetPlayerTime();
    }

    public void setPlayerWeather(@NotNull WeatherType weatherType) {
        player.setPlayerWeather(weatherType);
    }

    @Nullable
    public WeatherType getPlayerWeather() {
        return player.getPlayerWeather();
    }

    public void resetPlayerWeather() {
        player.resetPlayerWeather();
    }

    public void giveExp(int amount) {
        player.giveExp(amount);
    }

    public void giveExp(int i, boolean b) {
        player.giveExp(i, b);
    }

    public int applyMending(int i) {
        return player.applyMending(i);
    }

    public void giveExpLevels(int i) {
        player.giveExpLevels(i);
    }

    public float getExp() {
        return player.getExp();
    }

    public void setExp(float v) {
        player.setExp(v);
    }

    public int getLevel() {
        return player.getLevel();
    }

    public void setLevel(int i) {
        player.setLevel(i);
    }

    public int getTotalExperience() {
        return player.getTotalExperience();
    }

    public void setTotalExperience(int i) {
        player.setTotalExperience(i);
    }

    public void sendExperienceChange(float v) {
        player.sendExperienceChange(v);
    }

    public void sendExperienceChange(float v, int i) {
        player.sendExperienceChange(v, i);
    }

    public boolean getAllowFlight() {
        return player.getAllowFlight();
    }

    public void setAllowFlight(boolean b) {
        player.setAllowFlight(b);
    }

    @Deprecated
    public void hidePlayer(@NotNull Player player) {
        this.player.hidePlayer(player);
    }

    public void hidePlayer(org.bukkit.plugin.@NotNull Plugin plugin, @NotNull Player player) {
        this.player.hidePlayer(plugin, player);
    }

    @Deprecated
    public void showPlayer(@NotNull Player player) {
        this.player.showPlayer(player);
    }

    public void showPlayer(org.bukkit.plugin.@NotNull Plugin plugin, @NotNull Player player) {
        this.player.showPlayer(plugin, player);
    }

    public boolean canSee(@NotNull Player player) {
        return this.player.canSee(player);
    }

    @Deprecated
    public void hideEntity(org.bukkit.plugin.@NotNull Plugin plugin, @NotNull Entity entity) {
        player.hideEntity(plugin, entity);
    }

    @Deprecated
    public void showEntity(org.bukkit.plugin.@NotNull Plugin plugin, @NotNull Entity entity) {
        player.showEntity(plugin, entity);
    }

    @Deprecated
    public boolean canSee(@NotNull Entity entity) {
        return player.canSee(entity);
    }

    public boolean isFlying() {
        return player.isFlying();
    }

    public void setFlying(boolean b) {
        player.setFlying(b);
    }

    public void setFlySpeed(float v) throws IllegalArgumentException {
        player.setFlySpeed(v);
    }

    public void setWalkSpeed(float v) throws IllegalArgumentException {
        player.setWalkSpeed(v);
    }

    public float getFlySpeed() {
        return player.getFlySpeed();
    }

    public float getWalkSpeed() {
        return player.getWalkSpeed();
    }

    @Deprecated
    public void setTexturePack(@NotNull String s) {
        player.setTexturePack(s);
    }

    @Deprecated
    public void setResourcePack(@NotNull String s) {
        player.setResourcePack(s);
    }

    public void setResourcePack(@NotNull String s, byte[] bytes) {
        player.setResourcePack(s, bytes);
    }

    public void setResourcePack(@NotNull String s, byte[] bytes, @Nullable String s1) {
        player.setResourcePack(s, bytes, s1);
    }

    public void setResourcePack(@NotNull String url, byte @Nullable [] hash, @Nullable Component prompt) {
        player.setResourcePack(url, hash, prompt);
    }

    public void setResourcePack(@NotNull String s, byte[] bytes, boolean b) {
        player.setResourcePack(s, bytes, b);
    }

    public void setResourcePack(@NotNull String s, byte[] bytes, @Nullable String s1, boolean b) {
        player.setResourcePack(s, bytes, s1, b);
    }

    public void setResourcePack(@NotNull String s, byte @Nullable [] bytes, @Nullable Component component, boolean b) {
        player.setResourcePack(s, bytes, component, b);
    }

    public void setScoreboard(@NotNull Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
        player.setScoreboard(scoreboard);
    }

    public boolean isHealthScaled() {
        return player.isHealthScaled();
    }

    public void setHealthScaled(boolean b) {
        player.setHealthScaled(b);
    }

    public void setHealthScale(double v) throws IllegalArgumentException {
        player.setHealthScale(v);
    }

    public double getHealthScale() {
        return player.getHealthScale();
    }

    public void sendHealthUpdate(double v, int i, float v1) {
        player.sendHealthUpdate(v, i, v1);
    }

    public void sendHealthUpdate() {
        player.sendHealthUpdate();
    }

    public @Nullable Entity getSpectatorTarget() {
        return player.getSpectatorTarget();
    }

    public void setSpectatorTarget(@Nullable Entity entity) {
        player.setSpectatorTarget(entity);
    }

    @Deprecated
    public void sendTitle(@Nullable String s, @Nullable String s1) {
        player.sendTitle(s, s1);
    }

    @Deprecated
    public void sendTitle(@Nullable String s, @Nullable String s1, int i, int i1, int i2) {
        player.sendTitle(s, s1, i, i1, i2);
    }

    public void resetTitle() {
        player.resetTitle();
    }

    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i) {
        player.spawnParticle(particle, location, i);
    }

    public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i) {
        player.spawnParticle(particle, v, v1, v2, i);
    }

    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, @Nullable T t) {
        player.spawnParticle(particle, location, i, t);
    }

    public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, @Nullable T t) {
        player.spawnParticle(particle, v, v1, v2, i, t);
    }

    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1, double v2) {
        player.spawnParticle(particle, location, i, v, v1, v2);
    }

    public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5) {
        player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5);
    }

    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1, double v2, @Nullable T t) {
        player.spawnParticle(particle, location, i, v, v1, v2, t);
    }

    public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, @Nullable T t) {
        player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, t);
    }

    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1, double v2, double v3) {
        player.spawnParticle(particle, location, i, v, v1, v2, v3);
    }

    public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6) {
        player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6);
    }

    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1, double v2, double v3, @Nullable T t) {
        player.spawnParticle(particle, location, i, v, v1, v2, v3, t);
    }

    public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5, double v6, @Nullable T t) {
        player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6, t);
    }

    public @NotNull AdvancementProgress getAdvancementProgress(@NotNull Advancement advancement) {
        return player.getAdvancementProgress(advancement);
    }

    public int getClientViewDistance() {
        return player.getClientViewDistance();
    }

    @NotNull
    public Locale locale() {
        return player.locale();
    }

    public int getPing() {
        return player.getPing();
    }

    @Deprecated
    @NotNull
    public String getLocale() {
        return player.getLocale();
    }

    public boolean getAffectsSpawning() {
        return player.getAffectsSpawning();
    }

    public void setAffectsSpawning(boolean b) {
        player.setAffectsSpawning(b);
    }

    public int getViewDistance() {
        return player.getViewDistance();
    }

    public void setViewDistance(int i) {
        player.setViewDistance(i);
    }

    public int getNoTickViewDistance() {
        return player.getNoTickViewDistance();
    }

    public void setNoTickViewDistance(int i) {
        player.setNoTickViewDistance(i);
    }

    public int getSendViewDistance() {
        return player.getSendViewDistance();
    }

    public void setSendViewDistance(int i) {
        player.setSendViewDistance(i);
    }

    public void updateCommands() {
        player.updateCommands();
    }

    public void openBook(@NotNull ItemStack itemStack) {
        player.openBook(itemStack);
    }

    public void openSign(@NotNull Sign sign) {
        player.openSign(sign);
    }

    public void showDemoScreen() {
        player.showDemoScreen();
    }

    public boolean isAllowingServerListings() {
        return player.isAllowingServerListings();
    }

    public net.kyori.adventure.text.event.@NotNull HoverEvent<net.kyori.adventure.text.event.HoverEvent.ShowEntity> asHoverEvent(@NotNull UnaryOperator<net.kyori.adventure.text.event.HoverEvent.ShowEntity> op) {
        return player.asHoverEvent(op);
    }

    public void setResourcePack(@NotNull String s, @NotNull String s1) {
        player.setResourcePack(s, s1);
    }

    public void setResourcePack(@NotNull String s, @NotNull String s1, boolean b) {
        player.setResourcePack(s, s1, b);
    }

    public void setResourcePack(@NotNull String s, @NotNull String s1, boolean b, @Nullable Component component) {
        player.setResourcePack(s, s1, b, component);
    }

    public PlayerResourcePackStatusEvent.@Nullable Status getResourcePackStatus() {
        return player.getResourcePackStatus();
    }

    @Deprecated
    @Nullable
    public String getResourcePackHash() {
        return player.getResourcePackHash();
    }

    public boolean hasResourcePack() {
        return player.hasResourcePack();
    }

    public @NotNull PlayerProfile getPlayerProfile() {
        return player.getPlayerProfile();
    }

    public void setPlayerProfile(@NotNull PlayerProfile playerProfile) {
        player.setPlayerProfile(playerProfile);
    }

    public float getCooldownPeriod() {
        return player.getCooldownPeriod();
    }

    public float getCooledAttackStrength(float v) {
        return player.getCooledAttackStrength(v);
    }

    public void resetCooldown() {
        player.resetCooldown();
    }

    public <T> @NotNull T getClientOption(@NotNull ClientOption<T> clientOption) {
        return player.getClientOption(clientOption);
    }

    public @Nullable Firework boostElytra(@NotNull ItemStack itemStack) {
        return player.boostElytra(itemStack);
    }

    public void sendOpLevel(byte b) {
        player.sendOpLevel(b);
    }

    @Nullable
    public String getClientBrandName() {
        return player.getClientBrandName();
    }

    public Player.@NotNull Spigot spigot() {
        return player.spigot();
    }

    public @NotNull EntityEquipment getEquipment() {
        return player.getEquipment();
    }

    @NotNull
    public PlayerInventory getInventory() {
        return player.getInventory();
    }

    @NotNull
    public Inventory getEnderChest() {
        return player.getEnderChest();
    }

    public @NotNull MainHand getMainHand() {
        return player.getMainHand();
    }

    public boolean setWindowProperty(InventoryView.@NotNull Property property, int i) {
        return player.setWindowProperty(property, i);
    }

    public @NotNull InventoryView getOpenInventory() {
        return player.getOpenInventory();
    }

    public @Nullable InventoryView openWorkbench(@Nullable Location location, boolean b) {
        return player.openWorkbench(location, b);
    }

    public @Nullable InventoryView openEnchanting(@Nullable Location location, boolean b) {
        return player.openEnchanting(location, b);
    }

    public void openInventory(@NotNull InventoryView inventoryView) {
        player.openInventory(inventoryView);
    }

    public @Nullable InventoryView openMerchant(@NotNull Villager villager, boolean b) {
        return player.openMerchant(villager, b);
    }

    public @Nullable InventoryView openMerchant(@NotNull Merchant merchant, boolean b) {
        return player.openMerchant(merchant, b);
    }

    public @Nullable InventoryView openAnvil(@Nullable Location location, boolean b) {
        return player.openAnvil(location, b);
    }

    public @Nullable InventoryView openCartographyTable(@Nullable Location location, boolean b) {
        return player.openCartographyTable(location, b);
    }

    public @Nullable InventoryView openGrindstone(@Nullable Location location, boolean b) {
        return player.openGrindstone(location, b);
    }

    public @Nullable InventoryView openLoom(@Nullable Location location, boolean b) {
        return player.openLoom(location, b);
    }

    public @Nullable InventoryView openSmithingTable(@Nullable Location location, boolean b) {
        return player.openSmithingTable(location, b);
    }

    public @Nullable InventoryView openStonecutter(@Nullable Location location, boolean b) {
        return player.openStonecutter(location, b);
    }

    public void closeInventory() {
        player.closeInventory();
    }

    public void closeInventory(InventoryCloseEvent.@NotNull Reason reason) {
        player.closeInventory(reason);
    }

    @Deprecated
    @NotNull
    public ItemStack getItemInHand() {
        return player.getItemInHand();
    }

    @Deprecated
    public void setItemInHand(@Nullable ItemStack itemStack) {
        player.setItemInHand(itemStack);
    }

    @NotNull
    public ItemStack getItemOnCursor() {
        return player.getItemOnCursor();
    }

    public void setItemOnCursor(@Nullable ItemStack itemStack) {
        player.setItemOnCursor(itemStack);
    }

    public boolean hasCooldown(@NotNull Material material) {
        return player.hasCooldown(material);
    }

    public int getCooldown(@NotNull Material material) {
        return player.getCooldown(material);
    }

    public void setCooldown(@NotNull Material material, int i) {
        player.setCooldown(material, i);
    }

    public boolean isDeeplySleeping() {
        return player.isDeeplySleeping();
    }

    public int getSleepTicks() {
        return player.getSleepTicks();
    }

    @Nullable
    public Location getPotentialBedLocation() {
        return player.getPotentialBedLocation();
    }

    public boolean sleep(@NotNull Location location, boolean b) {
        return player.sleep(location, b);
    }

    public void wakeup(boolean b) {
        player.wakeup(b);
    }

    @NotNull
    public Location getBedLocation() {
        return player.getBedLocation();
    }

    @NotNull
    public GameMode getGameMode() {
        return player.getGameMode();
    }

    public void setGameMode(@NotNull GameMode gameMode) {
        player.setGameMode(gameMode);
    }

    public boolean isBlocking() {
        return player.isBlocking();
    }

    public boolean isHandRaised() {
        return player.isHandRaised();
    }

    @Nullable
    public ItemStack getItemInUse() {
        return player.getItemInUse();
    }

    public int getExpToLevel() {
        return player.getExpToLevel();
    }

    public @Nullable Entity releaseLeftShoulderEntity() {
        return player.releaseLeftShoulderEntity();
    }

    public @Nullable Entity releaseRightShoulderEntity() {
        return player.releaseRightShoulderEntity();
    }

    public float getAttackCooldown() {
        return player.getAttackCooldown();
    }

    public boolean discoverRecipe(@NotNull NamespacedKey namespacedKey) {
        return player.discoverRecipe(namespacedKey);
    }

    public int discoverRecipes(@NotNull Collection<NamespacedKey> collection) {
        return player.discoverRecipes(collection);
    }

    public boolean undiscoverRecipe(@NotNull NamespacedKey namespacedKey) {
        return player.undiscoverRecipe(namespacedKey);
    }

    public int undiscoverRecipes(@NotNull Collection<NamespacedKey> collection) {
        return player.undiscoverRecipes(collection);
    }

    public boolean hasDiscoveredRecipe(@NotNull NamespacedKey namespacedKey) {
        return player.hasDiscoveredRecipe(namespacedKey);
    }

    @NotNull
    public Set<NamespacedKey> getDiscoveredRecipes() {
        return player.getDiscoveredRecipes();
    }

    @Deprecated
    public @Nullable Entity getShoulderEntityLeft() {
        return player.getShoulderEntityLeft();
    }

    @Deprecated
    public void setShoulderEntityLeft(@Nullable Entity entity) {
        player.setShoulderEntityLeft(entity);
    }

    @Deprecated
    public @Nullable Entity getShoulderEntityRight() {
        return player.getShoulderEntityRight();
    }

    @Deprecated
    public void setShoulderEntityRight(@Nullable Entity entity) {
        player.setShoulderEntityRight(entity);
    }

    public boolean dropItem(boolean b) {
        return player.dropItem(b);
    }

    public float getExhaustion() {
        return player.getExhaustion();
    }

    public void setExhaustion(float v) {
        player.setExhaustion(v);
    }

    public float getSaturation() {
        return player.getSaturation();
    }

    public void setSaturation(float v) {
        player.setSaturation(v);
    }

    public int getFoodLevel() {
        return player.getFoodLevel();
    }

    public void setFoodLevel(int i) {
        player.setFoodLevel(i);
    }

    public int getSaturatedRegenRate() {
        return player.getSaturatedRegenRate();
    }

    public void setSaturatedRegenRate(int i) {
        player.setSaturatedRegenRate(i);
    }

    public int getUnsaturatedRegenRate() {
        return player.getUnsaturatedRegenRate();
    }

    public void setUnsaturatedRegenRate(int i) {
        player.setUnsaturatedRegenRate(i);
    }

    public int getStarvationRate() {
        return player.getStarvationRate();
    }

    public void setStarvationRate(int i) {
        player.setStarvationRate(i);
    }

    public double getEyeHeight() {
        return player.getEyeHeight();
    }

    public double getEyeHeight(boolean b) {
        return player.getEyeHeight(b);
    }

    @NotNull
    public Location getEyeLocation() {
        return player.getEyeLocation();
    }

    @NotNull
    public List<Block> getLineOfSight(@Nullable Set<Material> set, int i) {
        return player.getLineOfSight(set, i);
    }

    public @NotNull Block getTargetBlock(@Nullable Set<Material> set, int i) {
        return player.getTargetBlock(set, i);
    }

    public @Nullable Block getTargetBlock(int maxDistance) {
        return player.getTargetBlock(maxDistance);
    }

    public @Nullable Block getTargetBlock(int i, TargetBlockInfo.@NotNull FluidMode fluidMode) {
        return player.getTargetBlock(i, fluidMode);
    }

    public @Nullable BlockFace getTargetBlockFace(int maxDistance) {
        return player.getTargetBlockFace(maxDistance);
    }

    public @Nullable BlockFace getTargetBlockFace(int i, TargetBlockInfo.@NotNull FluidMode fluidMode) {
        return player.getTargetBlockFace(i, fluidMode);
    }

    public @Nullable TargetBlockInfo getTargetBlockInfo(int maxDistance) {
        return player.getTargetBlockInfo(maxDistance);
    }

    public @Nullable TargetBlockInfo getTargetBlockInfo(int i, TargetBlockInfo.@NotNull FluidMode fluidMode) {
        return player.getTargetBlockInfo(i, fluidMode);
    }

    public @Nullable Entity getTargetEntity(int maxDistance) {
        return player.getTargetEntity(maxDistance);
    }

    public @Nullable Entity getTargetEntity(int i, boolean b) {
        return player.getTargetEntity(i, b);
    }

    public @Nullable TargetEntityInfo getTargetEntityInfo(int maxDistance) {
        return player.getTargetEntityInfo(maxDistance);
    }

    public @Nullable TargetEntityInfo getTargetEntityInfo(int i, boolean b) {
        return player.getTargetEntityInfo(i, b);
    }

    @NotNull
    public List<Block> getLastTwoTargetBlocks(@Nullable Set<Material> set, int i) {
        return player.getLastTwoTargetBlocks(set, i);
    }

    public @Nullable Block getTargetBlockExact(int i) {
        return player.getTargetBlockExact(i);
    }

    public @Nullable Block getTargetBlockExact(int i, @NotNull FluidCollisionMode fluidCollisionMode) {
        return player.getTargetBlockExact(i, fluidCollisionMode);
    }

    public @Nullable RayTraceResult rayTraceBlocks(double v) {
        return player.rayTraceBlocks(v);
    }

    public @Nullable RayTraceResult rayTraceBlocks(double v, @NotNull FluidCollisionMode fluidCollisionMode) {
        return player.rayTraceBlocks(v, fluidCollisionMode);
    }

    public int getRemainingAir() {
        return player.getRemainingAir();
    }

    public void setRemainingAir(int i) {
        player.setRemainingAir(i);
    }

    public int getMaximumAir() {
        return player.getMaximumAir();
    }

    public void setMaximumAir(int i) {
        player.setMaximumAir(i);
    }

    public int getArrowCooldown() {
        return player.getArrowCooldown();
    }

    public void setArrowCooldown(int i) {
        player.setArrowCooldown(i);
    }

    public int getArrowsInBody() {
        return player.getArrowsInBody();
    }

    public void setArrowsInBody(int i) {
        player.setArrowsInBody(i);
    }

    public int getBeeStingerCooldown() {
        return player.getBeeStingerCooldown();
    }

    public void setBeeStingerCooldown(int i) {
        player.setBeeStingerCooldown(i);
    }

    public int getBeeStingersInBody() {
        return player.getBeeStingersInBody();
    }

    public void setBeeStingersInBody(int i) {
        player.setBeeStingersInBody(i);
    }

    public int getMaximumNoDamageTicks() {
        return player.getMaximumNoDamageTicks();
    }

    public void setMaximumNoDamageTicks(int i) {
        player.setMaximumNoDamageTicks(i);
    }

    public double getLastDamage() {
        return player.getLastDamage();
    }

    public void setLastDamage(double v) {
        player.setLastDamage(v);
    }

    public int getNoDamageTicks() {
        return player.getNoDamageTicks();
    }

    public void setNoDamageTicks(int i) {
        player.setNoDamageTicks(i);
    }

    @Nullable
    public Player getKiller() {
        return player.getKiller();
    }

    public void setKiller(@Nullable Player player) {
        this.player.setKiller(player);
    }

    public boolean addPotionEffect(@NotNull PotionEffect potionEffect) {
        return player.addPotionEffect(potionEffect);
    }

    @Deprecated
    public boolean addPotionEffect(@NotNull PotionEffect potionEffect, boolean b) {
        return player.addPotionEffect(potionEffect, b);
    }

    public boolean addPotionEffects(@NotNull Collection<PotionEffect> collection) {
        return player.addPotionEffects(collection);
    }

    public boolean hasPotionEffect(@NotNull PotionEffectType potionEffectType) {
        return player.hasPotionEffect(potionEffectType);
    }

    @Nullable
    public PotionEffect getPotionEffect(@NotNull PotionEffectType potionEffectType) {
        return player.getPotionEffect(potionEffectType);
    }

    public void removePotionEffect(@NotNull PotionEffectType potionEffectType) {
        player.removePotionEffect(potionEffectType);
    }

    @NotNull
    public Collection<PotionEffect> getActivePotionEffects() {
        return player.getActivePotionEffects();
    }

    public boolean hasLineOfSight(@NotNull Entity entity) {
        return player.hasLineOfSight(entity);
    }

    public boolean hasLineOfSight(@NotNull Location location) {
        return player.hasLineOfSight(location);
    }

    public boolean getRemoveWhenFarAway() {
        return player.getRemoveWhenFarAway();
    }

    public void setRemoveWhenFarAway(boolean b) {
        player.setRemoveWhenFarAway(b);
    }

    public void setCanPickupItems(boolean b) {
        player.setCanPickupItems(b);
    }

    public boolean getCanPickupItems() {
        return player.getCanPickupItems();
    }

    public boolean isLeashed() {
        return player.isLeashed();
    }

    public @NotNull Entity getLeashHolder() throws IllegalStateException {
        return player.getLeashHolder();
    }

    public boolean setLeashHolder(@Nullable Entity entity) {
        return player.setLeashHolder(entity);
    }

    public boolean isGliding() {
        return player.isGliding();
    }

    public void setGliding(boolean b) {
        player.setGliding(b);
    }

    public boolean isSwimming() {
        return player.isSwimming();
    }

    public void setSwimming(boolean b) {
        player.setSwimming(b);
    }

    public boolean isRiptiding() {
        return player.isRiptiding();
    }

    public boolean isSleeping() {
        return player.isSleeping();
    }

    public boolean isClimbing() {
        return player.isClimbing();
    }

    public void setAI(boolean b) {
        player.setAI(b);
    }

    public boolean hasAI() {
        return player.hasAI();
    }

    public void attack(@NotNull Entity entity) {
        player.attack(entity);
    }

    public void swingMainHand() {
        player.swingMainHand();
    }

    public void swingOffHand() {
        player.swingOffHand();
    }

    public void setCollidable(boolean b) {
        player.setCollidable(b);
    }

    public boolean isCollidable() {
        return player.isCollidable();
    }

    @NotNull
    public Set<UUID> getCollidableExemptions() {
        return player.getCollidableExemptions();
    }

    public <T> @Nullable T getMemory(@NotNull MemoryKey<T> memoryKey) {
        return player.getMemory(memoryKey);
    }

    public <T> void setMemory(@NotNull MemoryKey<T> memoryKey, @Nullable T t) {
        player.setMemory(memoryKey, t);
    }

    public @NotNull EntityCategory getCategory() {
        return player.getCategory();
    }

    public void setInvisible(boolean b) {
        player.setInvisible(b);
    }

    public boolean isInvisible() {
        return player.isInvisible();
    }

    public int getArrowsStuck() {
        return player.getArrowsStuck();
    }

    public void setArrowsStuck(int i) {
        player.setArrowsStuck(i);
    }

    public int getShieldBlockingDelay() {
        return player.getShieldBlockingDelay();
    }

    public void setShieldBlockingDelay(int i) {
        player.setShieldBlockingDelay(i);
    }

    @Nullable
    public ItemStack getActiveItem() {
        return player.getActiveItem();
    }

    public void clearActiveItem() {
        player.clearActiveItem();
    }

    public int getItemUseRemainingTime() {
        return player.getItemUseRemainingTime();
    }

    public int getHandRaisedTime() {
        return player.getHandRaisedTime();
    }

    public @NotNull EquipmentSlot getHandRaised() {
        return player.getHandRaised();
    }

    public boolean isJumping() {
        return player.isJumping();
    }

    public void setJumping(boolean b) {
        player.setJumping(b);
    }

    public void playPickupItemAnimation(@NotNull Item item) {
        player.playPickupItemAnimation(item);
    }

    public void playPickupItemAnimation(@NotNull Item item, int i) {
        player.playPickupItemAnimation(item, i);
    }

    public float getHurtDirection() {
        return player.getHurtDirection();
    }

    public void setHurtDirection(float v) {
        player.setHurtDirection(v);
    }

    @Nullable
    public AttributeInstance getAttribute(@NotNull Attribute attribute) {
        return player.getAttribute(attribute);
    }

    public void registerAttribute(@NotNull Attribute attribute) {
        player.registerAttribute(attribute);
    }

    public void damage(double v) {
        player.damage(v);
    }

    public void damage(double v, @Nullable Entity entity) {
        player.damage(v, entity);
    }

    public double getHealth() {
        return player.getHealth();
    }

    public void setHealth(double v) {
        player.setHealth(v);
    }

    public double getAbsorptionAmount() {
        return player.getAbsorptionAmount();
    }

    public void setAbsorptionAmount(double v) {
        player.setAbsorptionAmount(v);
    }

    public double getMaxHealth() {
        return this.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
    }

    public void setMaxHealth(double health) {
        this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
    }

    @Deprecated
    public void resetMaxHealth() {
        player.resetMaxHealth();
    }

    @NotNull
    public Location getLocation() {
        return player.getLocation();
    }

    @Contract("null -> null; !null -> !null")
    @Nullable
    public Location getLocation(@Nullable Location location) {
        return player.getLocation(location);
    }

    public void setVelocity(@NotNull Vector vector) {
        player.setVelocity(vector);
    }

    public @NotNull Vector getVelocity() {
        return player.getVelocity();
    }

    public double getHeight() {
        return player.getHeight();
    }

    public double getWidth() {
        return player.getWidth();
    }

    public @NotNull BoundingBox getBoundingBox() {
        return player.getBoundingBox();
    }

    public boolean isInWater() {
        return player.isInWater();
    }

    @NotNull
    public ExWorld getWorld() {
        return Server.getWorld(player.getWorld());
    }

    public void setRotation(float v, float v1) {
        player.setRotation(v, v1);
    }

    public boolean teleport(@NotNull Location location) {
        return player.teleport(location);
    }

    public boolean teleport(@NotNull Location location, PlayerTeleportEvent.@NotNull TeleportCause teleportCause) {
        return player.teleport(location, teleportCause);
    }

    public boolean teleport(@NotNull Entity entity) {
        return player.teleport(entity);
    }

    public boolean teleport(@NotNull Entity entity, PlayerTeleportEvent.@NotNull TeleportCause teleportCause) {
        return player.teleport(entity, teleportCause);
    }

    public @NotNull CompletableFuture<Boolean> teleportAsync(@NotNull Location loc) {
        return player.teleportAsync(loc);
    }

    public @NotNull CompletableFuture<Boolean> teleportAsync(@NotNull Location loc, PlayerTeleportEvent.@NotNull TeleportCause cause) {
        return player.teleportAsync(loc, cause);
    }

    @NotNull
    public List<Entity> getNearbyEntities(double v, double v1, double v2) {
        return player.getNearbyEntities(v, v1, v2);
    }

    public int getEntityId() {
        return player.getEntityId();
    }

    public int getFireTicks() {
        return player.getFireTicks();
    }

    public int getMaxFireTicks() {
        return player.getMaxFireTicks();
    }

    public void setFireTicks(int i) {
        player.setFireTicks(i);
    }

    public void setVisualFire(boolean b) {
        player.setVisualFire(b);
    }

    public boolean isVisualFire() {
        return player.isVisualFire();
    }

    public int getFreezeTicks() {
        return player.getFreezeTicks();
    }

    public int getMaxFreezeTicks() {
        return player.getMaxFreezeTicks();
    }

    public void setFreezeTicks(int i) {
        player.setFreezeTicks(i);
    }

    public boolean isFrozen() {
        return player.isFrozen();
    }

    public void remove() {
        player.remove();
    }

    public boolean isDead() {
        return player.isDead();
    }

    public boolean isValid() {
        return player.isValid();
    }

    public org.bukkit.@NotNull Server getServer() {
        return player.getServer();
    }

    public boolean isPersistent() {
        return player.isPersistent();
    }

    public void setPersistent(boolean b) {
        player.setPersistent(b);
    }

    @Deprecated
    public @Nullable Entity getPassenger() {
        return player.getPassenger();
    }

    @Deprecated
    public boolean setPassenger(@NotNull Entity entity) {
        return player.setPassenger(entity);
    }

    @NotNull
    public List<Entity> getPassengers() {
        return player.getPassengers();
    }

    public boolean addPassenger(@NotNull Entity entity) {
        return player.addPassenger(entity);
    }

    public boolean removePassenger(@NotNull Entity entity) {
        return player.removePassenger(entity);
    }

    public boolean isEmpty() {
        return player.isEmpty();
    }

    public boolean eject() {
        return player.eject();
    }

    public float getFallDistance() {
        return player.getFallDistance();
    }

    public void setFallDistance(float v) {
        player.setFallDistance(v);
    }

    public void setLastDamageCause(@Nullable EntityDamageEvent entityDamageEvent) {
        player.setLastDamageCause(entityDamageEvent);
    }

    public @Nullable EntityDamageEvent getLastDamageCause() {
        return player.getLastDamageCause();
    }

    public int getTicksLived() {
        return player.getTicksLived();
    }

    public void setTicksLived(int i) {
        player.setTicksLived(i);
    }

    public void playEffect(@NotNull EntityEffect entityEffect) {
        player.playEffect(entityEffect);
    }

    public @NotNull EntityType getType() {
        return player.getType();
    }

    public boolean isInsideVehicle() {
        return player.isInsideVehicle();
    }

    public boolean leaveVehicle() {
        return player.leaveVehicle();
    }

    public @Nullable Entity getVehicle() {
        return player.getVehicle();
    }

    public void setCustomNameVisible(boolean b) {
        player.setCustomNameVisible(b);
    }

    public boolean isCustomNameVisible() {
        return player.isCustomNameVisible();
    }

    public void setGlowing(boolean b) {
        player.setGlowing(b);
    }

    public boolean isGlowing() {
        return player.isGlowing();
    }

    public void setInvulnerable(boolean b) {
        player.setInvulnerable(b);
    }

    public boolean isInvulnerable() {
        return player.isInvulnerable();
    }

    public boolean isSilent() {
        return player.isSilent();
    }

    public void setSilent(boolean b) {
        player.setSilent(b);
    }

    public boolean hasGravity() {
        return player.hasGravity();
    }

    public void setGravity(boolean b) {
        player.setGravity(b);
    }

    public int getPortalCooldown() {
        return player.getPortalCooldown();
    }

    public void setPortalCooldown(int i) {
        player.setPortalCooldown(i);
    }

    @NotNull
    public Set<String> getScoreboardTags() {
        return player.getScoreboardTags();
    }

    public boolean addScoreboardTag(@NotNull String s) {
        return player.addScoreboardTag(s);
    }

    public boolean removeScoreboardTag(@NotNull String s) {
        return player.removeScoreboardTag(s);
    }

    public @NotNull PistonMoveReaction getPistonMoveReaction() {
        return player.getPistonMoveReaction();
    }

    public @NotNull BlockFace getFacing() {
        return player.getFacing();
    }

    public @NotNull Pose getPose() {
        return player.getPose();
    }

    @NotNull
    public Component teamDisplayName() {
        return player.teamDisplayName();
    }

    @Nullable
    public Location getOrigin() {
        return player.getOrigin();
    }

    public boolean fromMobSpawner() {
        return player.fromMobSpawner();
    }

    @NotNull
    public Chunk getChunk() {
        return player.getChunk();
    }

    public CreatureSpawnEvent.@NotNull SpawnReason getEntitySpawnReason() {
        return player.getEntitySpawnReason();
    }

    public boolean isInRain() {
        return player.isInRain();
    }

    public boolean isInBubbleColumn() {
        return player.isInBubbleColumn();
    }

    public boolean isInWaterOrRain() {
        return player.isInWaterOrRain();
    }

    public boolean isInWaterOrBubbleColumn() {
        return player.isInWaterOrBubbleColumn();
    }

    public boolean isInWaterOrRainOrBubbleColumn() {
        return player.isInWaterOrRainOrBubbleColumn();
    }

    public boolean isInLava() {
        return player.isInLava();
    }

    public boolean isTicking() {
        return player.isTicking();
    }

    @NotNull
    public Set<Player> getTrackedPlayers() {
        return player.getTrackedPlayers();
    }

    public boolean spawnAt(@NotNull Location location) {
        return player.spawnAt(location);
    }

    public boolean spawnAt(@NotNull Location location, CreatureSpawnEvent.@NotNull SpawnReason spawnReason) {
        return player.spawnAt(location, spawnReason);
    }

    public boolean isInPowderedSnow() {
        return player.isInPowderedSnow();
    }

    public void setMetadata(@NotNull String s, @NotNull MetadataValue metadataValue) {
        player.setMetadata(s, metadataValue);
    }

    @NotNull
    public List<MetadataValue> getMetadata(@NotNull String s) {
        return player.getMetadata(s);
    }

    public boolean hasMetadata(@NotNull String s) {
        return player.hasMetadata(s);
    }

    public void removeMetadata(@NotNull String s, org.bukkit.plugin.@NotNull Plugin plugin) {
        player.removeMetadata(s, plugin);
    }

    public void sendMessage(@NotNull String... strings) {
        player.sendMessage(strings);
    }

    public void sendMessage(@Nullable UUID uuid, @NotNull String s) {
        player.sendMessage(uuid, s);
    }

    public void sendMessage(@Nullable UUID uuid, @NotNull String... strings) {
        player.sendMessage(uuid, strings);
    }

    @NotNull
    public Component name() {
        return player.name();
    }

    public void sendMessage(@NotNull Identity identity, @NotNull Component message, @NotNull net.kyori.adventure.audience.MessageType type) {
        player.sendMessage(identity, message, type);
    }

    public @NotNull Audience filterAudience(@NotNull Predicate<? super Audience> filter) {
        return player.filterAudience(filter);
    }

    public void forEachAudience(@NotNull Consumer<? super Audience> action) {
        player.forEachAudience(action);
    }

    public void sendMessage(@NotNull ComponentLike message) {
        player.sendMessage(message);
    }

    public void sendMessage(@NotNull Identified source, @NotNull ComponentLike message) {
        player.sendMessage(source, message);
    }

    public void sendMessage(@NotNull Identity source, @NotNull ComponentLike message) {
        player.sendMessage(source, message);
    }

    public void sendMessage(@NotNull Component message) {
        player.sendMessage(message);
    }

    public void sendMessage(@NotNull Identified source, @NotNull Component message) {
        player.sendMessage(source, message);
    }

    public void sendMessage(@NotNull Identity source, @NotNull Component message) {
        player.sendMessage(source, message);
    }

    public void sendMessage(@NotNull ComponentLike message, @NotNull net.kyori.adventure.audience.MessageType type) {
        player.sendMessage(message, type);
    }

    public void sendMessage(@NotNull Identified source, @NotNull ComponentLike message, @NotNull net.kyori.adventure.audience.MessageType type) {
        player.sendMessage(source, message, type);
    }

    public void sendMessage(@NotNull Identity source, @NotNull ComponentLike message, @NotNull net.kyori.adventure.audience.MessageType type) {
        player.sendMessage(source, message, type);
    }

    public void sendMessage(@NotNull Component message, @NotNull net.kyori.adventure.audience.MessageType type) {
        player.sendMessage(message, type);
    }

    public void sendMessage(@NotNull Identified source, @NotNull Component message, @NotNull net.kyori.adventure.audience.MessageType type) {
        player.sendMessage(source, message, type);
    }

    public void sendActionBar(@NotNull ComponentLike message) {
        player.sendActionBar(message);
    }

    public void sendActionBar(@NotNull Component message) {
        player.sendActionBar(message);
    }

    public void sendPlayerListHeader(@NotNull ComponentLike header) {
        player.sendPlayerListHeader(header);
    }

    public void sendPlayerListHeader(@NotNull Component header) {
        player.sendPlayerListHeader(header);
    }

    public void sendPlayerListFooter(@NotNull ComponentLike footer) {
        player.sendPlayerListFooter(footer);
    }

    public void sendPlayerListFooter(@NotNull Component footer) {
        player.sendPlayerListFooter(footer);
    }

    public void sendPlayerListHeaderAndFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
        player.sendPlayerListHeaderAndFooter(header, footer);
    }

    public void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
        player.sendPlayerListHeaderAndFooter(header, footer);
    }

    public void showTitle(@NotNull Title title) {
        player.showTitle(title);
    }

    public <T> void sendTitlePart(@NotNull TitlePart<T> part, @NotNull T value) {
        player.sendTitlePart(part, value);
    }

    public void clearTitle() {
        player.clearTitle();
    }

    public void showBossBar(net.kyori.adventure.bossbar.@NotNull BossBar bar) {
        player.showBossBar(bar);
    }

    public void hideBossBar(net.kyori.adventure.bossbar.@NotNull BossBar bar) {
        player.hideBossBar(bar);
    }

    public void playSound(net.kyori.adventure.sound.@NotNull Sound sound) {
        player.playSound(sound);
    }

    public void playSound(net.kyori.adventure.sound.@NotNull Sound sound, double x, double y, double z) {
        player.playSound(sound, x, y, z);
    }

    public void stopSound(net.kyori.adventure.sound.@NotNull Sound sound) {
        player.stopSound(sound);
    }

    public void playSound(net.kyori.adventure.sound.@NotNull Sound sound, net.kyori.adventure.sound.Sound.@NotNull Emitter emitter) {
        player.playSound(sound, emitter);
    }

    public void stopSound(@NotNull SoundStop stop) {
        player.stopSound(stop);
    }

    public void openBook(Book.@NotNull Builder book) {
        player.openBook(book);
    }

    public void openBook(@NotNull Book book) {
        player.openBook(book);
    }

    @NotNull
    public <T> Optional<T> get(@NotNull Pointer<T> pointer) {
        return player.get(pointer);
    }

    @Contract("_, null -> _; _, !null -> !null")
    public <T> @Nullable T getOrDefault(@NotNull Pointer<T> pointer, @Nullable T defaultValue) {
        return player.getOrDefault(pointer, defaultValue);
    }

    public <T> @UnknownNullability T getOrDefaultFrom(@NotNull Pointer<T> pointer, @NotNull Supplier<? extends T> defaultValue) {
        return player.getOrDefaultFrom(pointer, defaultValue);
    }

    public @NotNull Pointers pointers() {
        return player.pointers();
    }

    public boolean isPermissionSet(@NotNull String s) {
        return player.isPermissionSet(s);
    }

    public boolean isPermissionSet(@NotNull Permission permission) {
        return player.isPermissionSet(permission);
    }

    public boolean hasPermission(@NotNull Permission permission) {
        return player.hasPermission(permission);
    }

    @NotNull
    public PermissionAttachment addAttachment(org.bukkit.plugin.@NotNull Plugin plugin, @NotNull String s, boolean b) {
        return player.addAttachment(plugin, s, b);
    }

    @NotNull
    public PermissionAttachment addAttachment(org.bukkit.plugin.@NotNull Plugin plugin) {
        return player.addAttachment(plugin);
    }

    @Nullable
    public PermissionAttachment addAttachment(org.bukkit.plugin.@NotNull Plugin plugin, @NotNull String s, boolean b, int i) {
        return player.addAttachment(plugin, s, b, i);
    }

    @Nullable
    public PermissionAttachment addAttachment(org.bukkit.plugin.@NotNull Plugin plugin, int i) {
        return player.addAttachment(plugin, i);
    }

    public void removeAttachment(@NotNull PermissionAttachment permissionAttachment) {
        player.removeAttachment(permissionAttachment);
    }

    public void recalculatePermissions() {
        player.recalculatePermissions();
    }

    @NotNull
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return player.getEffectivePermissions();
    }

    public @NotNull TriState permissionValue(@NotNull Permission permission) {
        return player.permissionValue(permission);
    }

    public @NotNull TriState permissionValue(@NotNull String permission) {
        return player.permissionValue(permission);
    }

    public boolean isOp() {
        return player.isOp();
    }

    public void setOp(boolean b) {
        player.setOp(b);
    }

    @Nullable
    public Component customName() {
        return player.customName();
    }

    public void customName(@Nullable Component component) {
        player.customName(component);
    }

    @Nullable
    public String getCustomName() {
        return player.getCustomName();
    }

    public void setCustomName(@Nullable String s) {
        player.setCustomName(s);
    }

    public @NotNull PersistentDataContainer getPersistentDataContainer() {
        return player.getPersistentDataContainer();
    }

    public net.kyori.adventure.text.event.@NotNull HoverEvent<net.kyori.adventure.text.event.HoverEvent.ShowEntity> asHoverEvent() {
        return player.asHoverEvent();
    }

    public <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> aClass) {
        return player.launchProjectile(aClass);
    }

    public <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> aClass, @Nullable Vector vector) {
        return player.launchProjectile(aClass, vector);
    }

    public boolean isConversing() {
        return player.isConversing();
    }

    public void acceptConversationInput(@NotNull String s) {
        player.acceptConversationInput(s);
    }

    public boolean beginConversation(@NotNull Conversation conversation) {
        return player.beginConversation(conversation);
    }

    public void abandonConversation(@NotNull Conversation conversation) {
        player.abandonConversation(conversation);
    }

    public void abandonConversation(@NotNull Conversation conversation, @NotNull ConversationAbandonedEvent conversationAbandonedEvent) {
        player.abandonConversation(conversation, conversationAbandonedEvent);
    }

    public void sendRawMessage(@Nullable UUID uuid, @NotNull String s) {
        player.sendRawMessage(uuid, s);
    }

    public boolean isOnline() {
        return player.isOnline();
    }

    public boolean isBanned() {
        return player.isBanned();
    }

    @NotNull
    public BanEntry banPlayer(@Nullable String reason) {
        return player.banPlayer(reason);
    }

    @NotNull
    public BanEntry banPlayer(@Nullable String reason, @Nullable String source) {
        return player.banPlayer(reason, source);
    }

    @NotNull
    public BanEntry banPlayer(@Nullable String reason, @Nullable Date expires) {
        return player.banPlayer(reason, expires);
    }

    @NotNull
    public BanEntry banPlayer(@Nullable String reason, @Nullable Date expires, @Nullable String source) {
        return player.banPlayer(reason, expires, source);
    }

    @NotNull
    public BanEntry banPlayer(@Nullable String reason, @Nullable Date expires, @Nullable String source, boolean kickIfOnline) {
        return player.banPlayer(reason, expires, source, kickIfOnline);
    }

    public boolean isWhitelisted() {
        return player.isWhitelisted();
    }

    public void setWhitelisted(boolean b) {
        player.setWhitelisted(b);
    }

    public long getFirstPlayed() {
        return player.getFirstPlayed();
    }

    @Deprecated
    public long getLastPlayed() {
        return player.getLastPlayed();
    }

    public boolean hasPlayedBefore() {
        return player.hasPlayedBefore();
    }

    public long getLastLogin() {
        return player.getLastLogin();
    }

    public long getLastSeen() {
        return player.getLastSeen();
    }

    public void incrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        player.incrementStatistic(statistic);
    }

    public void decrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        player.decrementStatistic(statistic);
    }

    public void incrementStatistic(@NotNull Statistic statistic, int i) throws IllegalArgumentException {
        player.incrementStatistic(statistic, i);
    }

    public void decrementStatistic(@NotNull Statistic statistic, int i) throws IllegalArgumentException {
        player.decrementStatistic(statistic, i);
    }

    public void setStatistic(@NotNull Statistic statistic, int i) throws IllegalArgumentException {
        player.setStatistic(statistic, i);
    }

    public int getStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        return player.getStatistic(statistic);
    }

    public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        player.incrementStatistic(statistic, material);
    }

    public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        player.decrementStatistic(statistic, material);
    }

    public int getStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        return player.getStatistic(statistic, material);
    }

    public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int i) throws IllegalArgumentException {
        player.incrementStatistic(statistic, material, i);
    }

    public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int i) throws IllegalArgumentException {
        player.decrementStatistic(statistic, material, i);
    }

    public void setStatistic(@NotNull Statistic statistic, @NotNull Material material, int i) throws IllegalArgumentException {
        player.setStatistic(statistic, material, i);
    }

    public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        player.incrementStatistic(statistic, entityType);
    }

    public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        player.decrementStatistic(statistic, entityType);
    }

    public int getStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        return player.getStatistic(statistic, entityType);
    }

    public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int i) throws IllegalArgumentException {
        player.incrementStatistic(statistic, entityType, i);
    }

    public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int i) {
        player.decrementStatistic(statistic, entityType, i);
    }

    public void setStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int i) {
        player.setStatistic(statistic, entityType, i);
    }

    @NotNull
    public Map<String, Object> serialize() {
        return player.serialize();
    }

    public void sendPluginMessage(org.bukkit.plugin.@NotNull Plugin plugin, @NotNull String s, byte[] bytes) {
        player.sendPluginMessage(plugin, s, bytes);
    }

    @NotNull
    public Set<String> getListeningPluginChannels() {
        return player.getListeningPluginChannels();
    }

    public int getProtocolVersion() {
        return player.getProtocolVersion();
    }

    public @Nullable InetSocketAddress getVirtualHost() {
        return player.getVirtualHost();
    }
}
