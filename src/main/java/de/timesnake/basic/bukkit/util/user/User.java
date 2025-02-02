/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user;

import de.timesnake.basic.bukkit.core.chat.CommandSender;
import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.core.user.PvPManager;
import de.timesnake.basic.bukkit.core.user.UserPermissible;
import de.timesnake.basic.bukkit.core.user.UserPlayerDelegation;
import de.timesnake.basic.bukkit.core.user.scoreboard.ScoreboardManager;
import de.timesnake.basic.bukkit.core.world.WorldManager;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.ChatMember;
import de.timesnake.basic.bukkit.util.chat.cmd.Sender;
import de.timesnake.basic.bukkit.util.group.DisplayGroup;
import de.timesnake.basic.bukkit.util.group.PermGroup;
import de.timesnake.basic.bukkit.util.server.ServerInfo;
import de.timesnake.basic.bukkit.util.user.event.AsyncUserJoinEvent;
import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;
import de.timesnake.basic.bukkit.util.user.event.UserQuitEvent;
import de.timesnake.basic.bukkit.util.user.inventory.ExInventory;
import de.timesnake.basic.bukkit.util.user.inventory.ExItemStack;
import de.timesnake.basic.bukkit.util.user.inventory.UserInventoryClickListener;
import de.timesnake.basic.bukkit.util.user.inventory.UserInventoryInteractListener;
import de.timesnake.basic.bukkit.util.user.scoreboard.*;
import de.timesnake.basic.bukkit.util.world.ExLocation;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import de.timesnake.channel.util.listener.ChannelHandler;
import de.timesnake.channel.util.listener.ChannelListener;
import de.timesnake.channel.util.listener.ListenerType;
import de.timesnake.channel.util.message.ChannelUserMessage;
import de.timesnake.channel.util.message.MessageType;
import de.timesnake.database.util.Database;
import de.timesnake.database.util.group.DbPermGroup;
import de.timesnake.database.util.object.DbLocation;
import de.timesnake.database.util.server.DbServer;
import de.timesnake.database.util.user.DbPunishment;
import de.timesnake.database.util.user.DbUser;
import de.timesnake.library.basic.util.PunishType;
import de.timesnake.library.basic.util.Punishment;
import de.timesnake.library.basic.util.Status;
import de.timesnake.library.chat.Chat;
import de.timesnake.library.chat.Code;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.chat.Plugin;
import de.timesnake.library.entities.entity.PlayerBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.*;
import org.bukkit.Note.Tone;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This user will be created on {@link org.bukkit.event.player.PlayerJoinEvent} Listener: - For user
 * join use the {@link UserJoinEvent} or {@link  AsyncUserJoinEvent} to prevent errors. - For user
 * quit use the {@link UserQuitEvent} to prevent errors. - For user inventory click use the
 * {@link UserInventoryInteractListener}. - For user item interact use the
 * {@link UserInventoryClickListener}. - Many other listeners are in
 * {@link de.timesnake.basic.bukkit.util.user.event}-package and can be used over the bukkit-event
 * api (implement {@link org.bukkit.event.Listener}). All attributes are synchronized with the
 * database. It is recommended to extend these class in a new plugin
 */

public class User extends UserPlayerDelegation implements ChannelListener, TablistPlayer, ChatMember, ScoreboardViewer {

  private final Logger logger = LogManager.getLogger("user");
  private final Logger punishLogger = LogManager.getLogger("user.punish");
  private final Logger groupLogger = LogManager.getLogger("user.group");

  private final DbUser dbUser;

  private Status.User status;
  private boolean service;
  private final boolean airMode;
  private String task;

  private final Set<BossBar> bossBars = ConcurrentHashMap.newKeySet();

  private final ScoreboardUser scoreboardUser;

  private final ServerInfo lastServer;
  private final ServerInfo lastLobbyServer;

  private final UserPermissible permissible;
  private PermGroup permGroup;
  private final SortedSet<DisplayGroup> displayGroups;

  protected Component chatNameComponent;
  protected String TDChatName;

  private LocalDateTime mutedUntil;

  private String prefix;
  private String suffix;
  private String nick;

  private Location lastLocation;
  private Location lockedLocation;

  private UserDamage lastUserDamage;

  private LocalDateTime privacyPolicyDateTime;

  private float coins;

  private boolean inventoryLocked;
  private boolean inventoryItemMoveLocked;
  private boolean blockBreakPlaceLocked;

  public User(Player player) {
    super(player);

    UUID uuid = player.getUniqueId();
    this.dbUser = Database.getUsers().getUser(uuid);

    DbUser dbLocalUser = this.dbUser.toLocal();

    this.airMode = dbLocalUser.isAirMode();
    this.setCollitionWithEntites(!this.airMode);
    this.service = dbLocalUser.isService();

    String prefix = dbLocalUser.getPrefix();
    if (prefix != null) {
      this.prefix = Server.getTimeDownParser().parse2Legacy(prefix, '&', '§');
      ;
    }

    String suffix = dbLocalUser.getSuffix();
    if (suffix != null) {
      this.suffix = Server.getTimeDownParser().parse2Legacy(suffix, '&', '§');
    }

    String nick = dbLocalUser.getNick();
    if (nick != null) {
      this.nick = Server.getTimeDownParser().parse2Legacy(nick, '&', '§');
    }

    if (dbLocalUser.getPermGroup() != null) {
      String groupName = dbLocalUser.getPermGroup().getName();
      this.permGroup = Server.getPermGroup(groupName);
      this.permGroup.addUser(this);
    } else {
      this.groupLogger.warn("Error while loading group for '{}'", dbLocalUser.getName());
      this.player.kick(Component.text("§cA fatal error occurred\nPlease contact an admin (permission group exception)"
      ));
    }

    this.displayGroups = new TreeSet<>(Comparator.comparingInt(DisplayGroup::getRank));
    this.updateDisplayGroups();

    this.permissible = Server.getUserPermissionManager().getUserPermissible(this.getUniqueId());

    // Chat name is being updated by display group update

    this.status = dbLocalUser.getStatus();

    this.task = dbLocalUser.getTask();

    this.updatePunishmentFromDatabase();

    this.privacyPolicyDateTime = dbLocalUser.getPrivacyPolicyDateTime();

    this.coins = dbLocalUser.getCoins();

    if (dbUser.getServerLast() != null) {
      this.lastServer = dbLocalUser.getServerLast() != null ? new ServerInfo(dbLocalUser.getServerLast()) : null;
    } else {
      this.lastServer = null;
    }
    if (dbUser.getServerLobby() != null) {
      this.lastLobbyServer = dbLocalUser.getServerLobby() != null ? new ServerInfo(dbLocalUser.getServerLobby()) : null;
    } else {
      this.lastLobbyServer = null;
    }

    this.inventoryLocked = false;
    this.blockBreakPlaceLocked = false;

    this.scoreboardUser = new ScoreboardUser() {
      @Override
      public String getName() {
        return User.this.getName();
      }

      @Override
      public UUID getUniqueId() {
        return User.this.getUniqueId();
      }

      @Override
      public NameTagVisibility canSeeNameTagOf(TablistPlayer player) {
        return User.this.canSeeNameTagOf(player);
      }

      @Override
      public void sendPacket(Packet<?> packet) {
        User.this.sendPacket(packet);
      }
    };

    Server.getChannel().addListener(this, Collections.singleton(player.getUniqueId()));
  }

  /**
   * Loads the user completely (only after join)
   */
  @Deprecated
  public void load() {
    ((WorldManager) Server.getWorldManager()).loadUserLocations(this);
    this.lastLocation = Server.getWorldManager().getUserLocation(this, this.getExWorld());
  }

  public final void quit() {
    ((WorldManager) Server.getWorldManager()).saveUserLocations(this);
    Server.getChannel().removeListener(this);
    this.onQuit();
  }

  public void onQuit() {

  }


  @Override
  public int hashCode() {
    return Objects.hash(this.player.getUniqueId());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (o instanceof User) {
      return Objects.equals(this.player.getUniqueId(), ((User) o).getUniqueId());
    }
    return false;
  }

  //player

  /**
   * Gets the bukkit-player from user
   *
   * @return the {@link Player}
   */
  @NotNull
  @Override
  public final Player getPlayer() {
    return player;
  }

  @Override
  public final ServerPlayer getMinecraftPlayer() {
    return this.getCraftPlayer().getHandle();
  }

  public final CraftPlayer getCraftPlayer() {
    return ((CraftPlayer) this.getPlayer());
  }

  public PlayerBuilder<net.minecraft.world.entity.player.Player, ?> asPlayerBuilder() {
    return new PlayerBuilder<>(this.getMinecraftPlayer());
  }

  /**
   * Gets the user as a sender
   *
   * @param plugin The plugin, that will send the messages
   * @return the {@link Sender}
   */
  @NotNull
  public Sender asSender(de.timesnake.library.chat.Plugin plugin) {
    return new Sender(new CommandSender(player), plugin);
  }

  /**
   * Kills the user by setting health to 0
   */
  public void kill() {
    this.player.setHealth(0);
  }

  public void resetPlayerProperties() {
    this.heal();
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

  public void unlockAll() {
    this.unlockBlockBreakPlace();
    this.unlockInventory();
    this.unlockInventoryItemMove();
    this.unlockLocation();
  }

  public boolean isCollitionWithEntites() {
    return this.player.isCollidable();
  }

  public void setCollitionWithEntites(boolean collition) {
    if (!this.airMode) {
      this.player.setCollidable(collition);
    }
  }

  //database

  /**
   * Gets user database equivalent
   *
   * @return The {@link DbUser}
   */
  @NotNull
  public final DbUser getDatabase() {
    return this.dbUser;
  }

  // privacy policy

  public void agreePrivacyPolicy() {
    this.privacyPolicyDateTime = LocalDateTime.now();
    this.dbUser.agreePrivacyPolicy(privacyPolicyDateTime);
  }

  /**
   * Disagrees privacy policy After that, the user will be kicked and deleted
   */
  public void disagreePrivacyPolicy() {
    this.privacyPolicyDateTime = null;
    this.dbUser.disagreePrivacyPolicy();
  }


  @Nullable
  public LocalDateTime getPrivacyPolicyDateTime() {
    return this.privacyPolicyDateTime;
  }


  public boolean agreedPrivacyPolicy() {
    return this.privacyPolicyDateTime != null;
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

  @Override
  public void sendTDMessage(String message) {
    this.player.sendMessage(Server.getTimeDownParser().parse2Component(message));
  }

  /**
   * Sends a plugin-message to the user
   *
   * @param plugin  The {@link Plugin} to send the message
   * @param message The message to send
   */
  public void sendPluginMessage(de.timesnake.library.chat.Plugin plugin,
                                Component message) {
    this.getPlayer().sendMessage(Chat.getSenderPlugin(plugin).append(message));
  }

  /**
   * Sends a plugin-message to the user
   *
   * @param plugin  The {@link Plugin} to send the message
   * @param message The message to send
   */
  public void sendPluginTDMessage(de.timesnake.library.chat.Plugin plugin,
                                  String message) {
    this.getPlayer().sendMessage(Chat.getSenderPlugin(plugin)
        .append(Server.getTimeDownParser().parse2Component(message)));
  }

  @NotNull
  public String getTDChatName() {
    return TDChatName;
  }

  @NotNull
  @Override
  public Component getChatNameComponent() {
    return this.chatNameComponent;
  }

  @Override
  public boolean hasColoredChatMessagePermission() {
    return this.hasPermission("chat.color");
  }

  /**
   * Gets the user prefix
   *
   * @return the prefix
   */
  @Nullable
  public Component getPrefix() {
    return Server.getTimeDownParser().parse2Component(this.prefix);
  }

  @Nullable
  public String getTDPrefix() {
    return this.prefix;
  }

  /**
   * Sets the user prefix
   *
   * @param prefix The prefix to set
   */
  public void setPrefix(String prefix) {
    this.dbUser.setPrefix(Server.getTimeDownParser().parse2Legacy(prefix, '§', '&'));
    this.prefix = Server.getTimeDownParser().parse2Legacy(prefix, '&', '§');
  }

  /**
   * Gets the user suffix
   *
   * @return the suffix
   */
  @Nullable
  public Component getSuffix() {
    return Server.getTimeDownParser().parse2Component(this.suffix);
  }

  @Nullable
  public String getTDSuffix() {
    return this.suffix;
  }

  /**
   * Sets the user suffix
   *
   * @param suffix The suffix to set
   */
  public void setSuffix(String suffix) {
    this.dbUser.setSuffix(Server.getTimeDownParser().parse2Legacy(suffix, '§', '&'));
    this.suffix = Server.getTimeDownParser().parse2Legacy(suffix, '&', '§');
  }

  /**
   * Gets the user nick
   *
   * @return the nick
   */
  @Nullable
  public Component getNick() {
    return Server.getTimeDownParser().parse2Component(this.nick);
  }

  @Nullable
  public String getTDNick() {
    return this.nick;
  }

  /**
   * Sets the user nick
   *
   * @param nick The nick to set
   */
  public void setNick(String nick) {
    this.dbUser.setNick(Server.getTimeDownParser().parse2Legacy(nick, '§', '&'));
    this.nick = Server.getTimeDownParser().parse2Legacy(nick, '&', '§');
  }

  /**
   * Is user muted
   *
   * @return if is user muted
   */
  public boolean isMuted() {
    if (this.mutedUntil == null) {
      return false;
    }

    if (this.mutedUntil.isBefore(LocalDateTime.now())) {
      DbPunishment dbPunishment = this.dbUser.getPunishment();
      Punishment punishment = dbPunishment.asPunishment();
      if (punishment.getType().equals(PunishType.TEMP_MUTE) && punishment.isExpired()) {
        dbPunishment.delete();
      }
      this.mutedUntil = null;
      return false;
    }

    return true;
  }

  public void mute(Duration duration, String reason) {
    this.mutedUntil = LocalDateTime.now().plus(duration);
    Server.getChannel().sendMessage(new ChannelUserMessage<>(this.getUniqueId(), MessageType.User.PUNISH,
        new Punishment(this.getUniqueId(), PunishType.TEMP_MUTE, LocalDateTime.now(),
            duration, "AntiCheat", reason)));
  }

  /**
   * Sends a clickable test to the user
   *
   * @param text   The test to send
   * @param exec   The execution, if the user clicked on the text
   * @param info   The info to show, if the user hovers other the text
   * @param action The action to do
   */
  public void sendClickableTDMessage(String text, String exec, String info,
                                     net.kyori.adventure.text.event.ClickEvent.Action action) {
    Component component = Server.getTimeDownParser().parse2Component(text)
        .clickEvent(net.kyori.adventure.text.event.ClickEvent.clickEvent(action, exec))
        .hoverEvent(net.kyori.adventure.text.event.HoverEvent.hoverEvent(
            net.kyori.adventure.text.event.HoverEvent.Action.SHOW_TEXT,
            Component.text(info)));
    this.sendMessage(component);
  }

  public void sendClickablePluginTDMessage(de.timesnake.library.chat.Plugin plugin, String text, String exec,
                                           String info,
                                           net.kyori.adventure.text.event.ClickEvent.Action action) {
    Component component = Chat.getSenderPlugin(plugin)
        .append(Server.getTimeDownParser().parse2Component(text)
            .clickEvent(net.kyori.adventure.text.event.ClickEvent.clickEvent(action, exec))
            .hoverEvent(net.kyori.adventure.text.event.HoverEvent.hoverEvent(
                net.kyori.adventure.text.event.HoverEvent.Action.SHOW_TEXT,
                Component.text(info))));
    this.sendMessage(component);
  }

  /**
   * Sends a clickable test to the user
   *
   * @param text   The test to send
   * @param exec   The execution, if the user clicked on the text
   * @param info   The info to show, if the user hovers other the text
   * @param action The action to do
   */
  public void sendClickableMessage(Component text, String exec, Component info,
                                   net.kyori.adventure.text.event.ClickEvent.Action action) {
    this.sendMessage(
        text.clickEvent(net.kyori.adventure.text.event.ClickEvent.clickEvent(action, exec))
            .hoverEvent(net.kyori.adventure.text.event.HoverEvent.hoverEvent(
                net.kyori.adventure.text.event.HoverEvent.Action.SHOW_TEXT, info)));
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
  public void sendClickablePluginMessage(Plugin plugin, Component text, String exec,
                                         Component info, net.kyori.adventure.text.event.ClickEvent.Action action) {
    this.sendMessage(Chat.getSenderPlugin(plugin).append(text)
        .clickEvent(net.kyori.adventure.text.event.ClickEvent.clickEvent(action, exec))
        .hoverEvent(net.kyori.adventure.text.event.HoverEvent.hoverEvent(
            net.kyori.adventure.text.event.HoverEvent.Action.SHOW_TEXT, info)));
  }

  /**
   * Shows a title to the user, with standard fade-times Fade-in time: 250 ms Fade-out time : 250
   * ms
   *
   * @param title    The title to send
   * @param subTitle The subtitle to send
   * @param stay     The stay time in ms
   */
  @Deprecated
  public void showTitle(String title, String subTitle, Duration stay) {
    this.showTitle(Title.title(Component.text(title), Component.text(subTitle),
        Title.Times.times(Duration.ofMillis(250), stay, Duration.ofMillis(250))));
  }

  /**
   * Shows a title to the user, with standard fade-times Fade-in time: 250 ms Fade-out time : 250
   * ms
   *
   * @param title    The title to send
   * @param subTitle The subtitle to send
   * @param stay     The stay time in ms
   */
  public void showTitle(Component title, Component subTitle, Duration stay) {
    this.showTitle(Title.title(title, subTitle,
        Title.Times.times(Duration.ofMillis(250), stay, Duration.ofMillis(250))));
  }

  /**
   * Shows a title to the user, with standard fade-times Fade-in time: 250 ms Fade-out time : 250
   * ms
   *
   * @param title    The title to send
   * @param subTitle The subtitle to send
   * @param stay     The stay time in ms
   */
  public void showTDTitle(String title, String subTitle, Duration stay) {
    this.showTDTitle(title, subTitle, Duration.ofMillis(250), stay, Duration.ofMillis(250));
  }

  /**
   * Shows a title to the user
   *
   * @param title    The title to send
   * @param subTitle The subtitle to send
   * @param fadeIn   The fade-in time
   * @param stay     The stay time
   * @param fadeOut  The fade-out time
   */
  @Deprecated
  public void showTitle(String title, String subTitle, Duration fadeIn, Duration stay,
                        Duration fadeOut) {
    this.showTitle(Component.text(title), Component.text(subTitle), fadeIn, stay, fadeOut);
  }

  /**
   * Shows a title to the user
   *
   * @param title    The title to send
   * @param subTitle The subtitle to send
   * @param fadeIn   The fade-in time
   * @param stay     The stay time
   * @param fadeOut  The fade-out time
   */
  public void showTitle(Component title, Component subTitle, Duration fadeIn, Duration stay,
                        Duration fadeOut) {
    this.showTitle(Title.title(title, subTitle, Title.Times.times(fadeIn, stay, fadeOut)));
  }

  /**
   * Shows a title to the user
   *
   * @param title    The title to send
   * @param subTitle The subtitle to send
   * @param fadeIn   The fade-in time
   * @param stay     The stay time
   * @param fadeOut  The fade-out time
   */
  public void showTDTitle(String title, String subTitle, Duration fadeIn, Duration stay,
                          Duration fadeOut) {
    this.showTitle(Title.title(Server.getTimeDownParser().parse2Component(title),
        Server.getTimeDownParser().parse2Component(subTitle),
        Title.Times.times(fadeIn, stay, fadeOut)));
  }

  /**
   * Runs a command, executed by the user
   *
   * @param command The command to run
   */
  public void runCommand(String command) {
    Server.runTaskSynchrony(() -> {
      if (command.startsWith("/say ")) {
        Server.broadcastMessage(Server.getChat().getSenderMember(this)
            .append(Component.text(command.replaceFirst("/say ", ""))));
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
   * Plays the standard inventory opened sound for the user Instrument: piano Note: octave 1, tone
   * A
   */
  public void playSoundInventoryOpened() {
    this.playNote(Instrument.PIANO, Note.natural(1, Tone.A));
  }

  /**
   * Plays the standard item clicked sound for the user Instrument: sticks Note: octave 1, tone A
   */
  public void playSoundItemClickSuccessful() {
    this.playNote(Instrument.STICKS, Note.natural(1, Tone.A));
  }

  public void playSoundItemClickFailed() {
    this.playNote(Instrument.STICKS, Note.natural(0, Tone.A));
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

  public void playNoteBlocked() {
    this.playNote(Instrument.PIANO, Note.natural(0, Note.Tone.C));
  }

  public void playNoteSuccess() {
    this.playNote(Instrument.PIANO, Note.natural(1, Note.Tone.C));
  }

  // team, group

  /**
   * Gets the user group
   *
   * @return the {@link PermGroup}
   */
  @NotNull
  public PermGroup getPermGroup() {
    return permGroup;
  }

  /**
   * Updates the group of the user from the database Removes user from old group and adds to new
   * group Updates permission
   */
  public void updatePermGroup() {
    DbPermGroup dbGroup = this.getDatabase().getPermGroup();
    if (this.permGroup.getRank().equals(dbGroup.getPriority())) {
      return;
    }

    if (this.permGroup != null) {
      this.permGroup.removeUser(this);
    }

    this.permGroup = Server.getPermGroup(dbGroup.getName());

    if (this.permGroup == null) {
      this.permGroup = Server.getGuestPermGroup();
      this.getDatabase().setPermGroup(this.permGroup.getName());
      Server.getChannel().sendMessage(new ChannelUserMessage<>(this.getUniqueId(), MessageType.User.PERM_GROUP,
          this.permGroup.getName()));
    }

    this.permissible.updatePermGroup(this.permGroup);

    this.permGroup.addUser(this);
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

  @NotNull
  @Override
  public String getTablistName() {
    return this.getName();
  }

  @Nullable
  @Override
  public String getTablistPrefix() {
    return this.getPrefix() != null ? LegacyComponentSerializer.legacySection().serialize(this.getPrefix()) : null;
  }

  @Nullable
  @Override
  public TablistGroup getTablistGroup(TablistGroupType type) {
    if (DisplayGroup.TABLIST_TYPE_0.equals(type)) {
      return this.getMasterDisplayGroup();
    } else if (DisplayGroup.TABLIST_TYPE_1.equals(type)) {
      List<DisplayGroup> list = this.getMainDisplayGroups();
      if (list.size() < 2) {
        return null;
      }
      return list.get(1);

    } else if (DisplayGroup.TABLIST_TYPE_2.equals(type)) {
      List<DisplayGroup> list = this.getMainDisplayGroups();
      if (list.size() < 3) {
        return null;
      }
      return list.get(2);
    }
    return null;
  }

  public void sendActionBarTDText(String text) {
    this.getPlayer().sendActionBar(Server.getTimeDownParser().parse2Component(text));
  }

  public void sendActionBarText(Component text) {
    this.getPlayer().sendActionBar(text);
  }

  /**
   * Adds a new {@link BossBar}
   *
   * @param bossBar The {@link BossBar} to set
   */
  public void addBossBar(BossBar bossBar) {
    if (bossBar == null) {
      return;
    }
    this.bossBars.add(bossBar);
    bossBar.addPlayer(this.getPlayer());
  }

  /**
   * Removes a {@link BossBar}
   *
   * @param bossBar The {@link BossBar} to remove
   */
  public void removeBossBar(BossBar bossBar) {
    if (bossBar == null) {
      return;
    }
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
   * Gets the first {@link BossBar}
   *
   * @return the first {@link BossBar} if one exists, else null
   */
  @Nullable
  public BossBar getBossBar() {
    if (!this.bossBars.isEmpty()) {
      return this.bossBars.iterator().next();
    }
    return null;
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
   * Gets the bars
   *
   * @return the {@link Set} of {@link BossBar}s
   */
  @NotNull
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
    this.permissible.updatePermissions(false);
  }

  /**
   * Gets the current user status
   *
   * @return the {@link Status.User}
   */
  @NotNull
  public Status.User getStatus() {
    return this.status;
  }

  public boolean hasStatus(Status.User... status) {
    return Arrays.stream(status).anyMatch(s -> s.equals(this.status));
  }

  /**
   * Sets the user status
   *
   * @param status The {@link Status.User} to set
   */
  public void setStatus(Status.User status) {
    if (this.status.equals(status)) {
      return;
    }
    this.dbUser.setStatus(status);
    this.status = status;
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
   * Gets the current user task
   *
   * @return the task
   */
  @Nullable
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
   * @return if user has permission, else false
   */
  public boolean hasPermission(@NotNull String permission) {
    return this.player.hasPermission(permission);
  }

  /**
   * Has user permission If false, the user receives an error-message with the code by the plugin
   *
   * @param code   The code of the permission
   * @param plugin The {@link Plugin} of the permission
   * @return if user has
   */
  public boolean hasPermission(Code code, de.timesnake.library.chat.Plugin plugin) {
    return this.asSender(plugin).hasPermission(code);
  }

  /**
   * Updates user permissions async
   *
   * @param fromDatabase Set true to update from database and group
   */
  public void updatePermissions(boolean fromDatabase) {
    this.permissible.updatePermissions(fromDatabase);
  }

  //network

  /**
   * Gets the server the user last on
   *
   * @return the {@link ServerInfo} of the server
   */
  @Nullable
  public ServerInfo getLastServer() {
    return this.lastServer;
  }

  /**
   * Gets the lobby-server the user was last on
   *
   * @return the {@link ServerInfo} of the server
   */
  @Nullable
  public ServerInfo getLastLobbyServer() {
    return this.lastLobbyServer;
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
    Server.getNetwork().sendUserToServer(this, server.getName());
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

  public void lockBlockBreakPlace() {
    this.blockBreakPlaceLocked = true;
  }

  public void unlockBlockBreakPlace() {
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
   * @param inv The {@link ExInventory} to open
   */
  public void openInventory(ExInventory inv) {
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
  @Nullable
  public ExItemStack getItem(Integer id) {
    if (id == null) {
      return null;
    }

    for (ItemStack item : this.getInventory()) {
      if (item == null) {
        continue;
      }
      ExItemStack exItem = ExItemStack.getItem(item, true);
      if (exItem.getId().equals(id)) {
        return exItem;
      }
    }
    return null;
  }

  /**
   * Gets an inventory item by id
   *
   * @param item The {@link ExItemStack} to search for
   * @return the item if found, else null
   */
  @Nullable
  public ExItemStack getItem(ExItemStack item) {
    if (item == null) {
      return null;
    }

    for (ItemStack i : this.getInventory()) {
      if (i == null) {
        continue;
      }
      ExItemStack exItem = ExItemStack.getItem(i, true);
      if (exItem.equals(item)) {
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
   * Sets an item stack in inventory
   *
   * @param slot The {@link EquipmentSlot} to set
   * @param item The {@link org.bukkit.inventory.ItemStack} to set
   */
  public void setItem(EquipmentSlot slot, org.bukkit.inventory.ItemStack item) {
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

      ExItemStack exItem = new ExItemStack(i).setSlot(slot);

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
    return remain <= 0;
  }

  /**
   * Removes an {@link ExItemStack} from inventory by id
   *
   * @param item The {@link ExItemStack} to remove
   * @return if removed the slot, else null
   */
  @Nullable
  public Integer removeExItemStack(ExItemStack item) {
    for (int slot = 0; slot < this.getInventory().getSize(); slot++) {
      ItemStack i = this.getInventory().getItem(slot);
      if (i == null) {
        continue;
      }

      ExItemStack exItem = new ExItemStack(i).setSlot(slot);

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
  @Nullable
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

      ExItemStack exItem = new ExItemStack(i).setSlot(slot);

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
    this.player.addPotionEffect(
        new PotionEffect(potionEffectType, Integer.MAX_VALUE, amplifier));
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
   * Adds the potion-effect to the user
   *
   * @param potionEffectType The {@link PotionEffectType} to add
   * @param duration         The duration of the potion-effect
   * @param amplifier        The amplifier of the potion-effect
   */
  public void addPotionEffect(PotionEffectType potionEffectType, int duration, int amplifier,
                              boolean showParticles) {
    this.player.addPotionEffect(new PotionEffect(potionEffectType, duration, amplifier,
        showParticles, showParticles));
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
   * Sets the user health to max health and food-level to 20
   */
  public void heal() {
    this.getPlayer().setHealth(
        this.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
    this.getPlayer().setFoodLevel(20);
  }

  //location

  /**
   * Gets the current user location
   *
   * @return the  {@link ExLocation}
   */
  @NotNull
  public ExLocation getExLocation() {
    return new ExLocation(this.getExWorld(), this.getLocation().getX(),
        this.getLocation().getY(),
        this.getLocation().getZ(), this.getLocation().getYaw(),
        this.getLocation().getPitch());
  }

  public boolean teleport(double x, double y, double z) {
    return this.teleport(new ExLocation(this.getExWorld(), x, y, z, this.getLocation().getYaw(),
        this.getLocation().getPitch()));
  }

  public boolean teleport(double x, double y, double z, float yaw, float pitch) {
    return this.teleport(new ExLocation(this.getExWorld(), x, y, z, yaw, pitch));
  }

  /**
   * Gets the {@link ExWorld}, which is equivalent to the world returned from
   * {@code this.getWorld()}
   *
   * @return the {@link ExWorld}
   */
  @NotNull
  public ExWorld getExWorld() {
    return Server.getWorld(this.player.getWorld());
  }

  @Override
  public @NotNull World getWorld() {
    return this.player.getWorld();
  }

  /**
   * Gets the current user location
   *
   * @return the {@link DbLocation}
   */
  @NotNull
  public DbLocation getDbLocation() {
    Location loc = this.getLocation();
    return new DbLocation(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(),
        loc.getYaw(),
        loc.getPitch());
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
   * Gets the last location of the user
   *
   * @return the last {@link Location}
   */
  @Nullable
  public Location getLastLocation() {
    return lastLocation;
  }

  /**
   * Sets the last location of the user
   *
   * @param location The last {@link Location} of the user
   */
  public void setLastLocation(Location location) {
    this.lastLocation = location;
  }

  @Nullable
  public UserDamage getLastDamager() {
    return this.lastUserDamage;
  }

  public void setLastDamager(UserDamage userDamage) {
    this.lastUserDamage = userDamage;
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
    this.sendPluginTDMessage(Plugin.TIME_COINS, "§6Balance changed to " + coins + " TimeCoins");
    if (sendMessage) {
      this.sendActionBarTDText(Chat.roundCoinAmount(coins) + " TimeCoins");
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
      this.sendActionBarTDText("§6+ " + Chat.roundCoinAmount(coins) + " TimeCoins");
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
      this.sendActionBarTDText("§6 -" + Chat.roundCoinAmount(coins) + " TimeCoins");
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

  public synchronized void lockLocation() {
    this.lockedLocation = this.getLocation();
    this.setWalkSpeed(Float.MIN_VALUE);
    this.setFlySpeed(Float.MIN_VALUE);
    super.setAllowFlight(true);
    super.setFlying(true);
    this.setGravity(false);
  }

  public synchronized void unlockLocation() {
    this.lockedLocation = null;
    this.setWalkSpeed(0.2f);
    this.setFlySpeed(0.2f);
    this.setAllowFlight(false);
    this.setFlying(false);
    this.setGravity(true);
  }

  public boolean isLocationLocked() {
    return lockedLocation != null;
  }

  @Override
  public void setFlying(boolean b) {
    super.setFlying(b);
    this.logger.warn("Enable flying for user {} during location lock", this.getName());
  }

  @ChannelHandler(type = {
      ListenerType.USER_STATUS,
      ListenerType.USER_SERVICE,
      ListenerType.USER_SOUND,
      ListenerType.USER_PERMISSION,
      ListenerType.USER_PUNISH,
      ListenerType.USER_ALIAS,
      ListenerType.USER_TASK,
      ListenerType.USER_COMMAND,
      ListenerType.USER_PERM_GROUP,
      ListenerType.USER_DISPLAY_GROUP,
  },
      filtered = true)
  public void onUserMessage(ChannelUserMessage<?> msg) {
    MessageType<?> type = msg.getMessageType();
    if (type.equals(MessageType.User.STATUS)) {
      Status.User dbStatus = this.getDatabase().getStatus();
      if (!this.status.equals(dbStatus)) {
        this.status = dbStatus;
        this.updatePermissions(false);
      }
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
      this.updatePunishmentFromDatabase();
    } else if (type.equals(MessageType.User.ALIAS)) {
      this.updateAlias();
    } else if (type.equals(MessageType.User.TASK)) {
      this.task = this.dbUser.getTask();
    } else if (type.equals(MessageType.User.COMMAND)) {
      this.runCommand((String) msg.getValue());
    } else if (type.equals(MessageType.User.PERM_GROUP)) {
      this.updatePermGroup();
    } else if (type.equals(MessageType.User.DISPLAY_GROUP)) {
      this.updateDisplayGroups();
    }
  }

  /**
   * Updates the user alias (prefix, suffix, nick) from database and chat-name
   */
  public void updateAlias() {
    String prefix = this.dbUser.getPrefix();
    if (prefix != null) {
      this.prefix = Server.getTimeDownParser().parse2Legacy(prefix, '&', '§');
      ;
    }

    String suffix = this.dbUser.getSuffix();
    if (suffix != null) {
      this.suffix = Server.getTimeDownParser().parse2Legacy(suffix, '&', '§');
    }

    String nick = this.dbUser.getNick();
    if (nick != null) {
      this.nick = Server.getTimeDownParser().parse2Legacy(nick, '&', '§');
    }

    this.updateChatName();
  }

  public void updateDisplayGroups() {
    this.displayGroups.clear();

    for (String groupName : this.getDatabase().getDisplayGroupNames()) {
      DisplayGroup group = Server.getDisplayGroup(groupName);
      if (group != null) {
        this.displayGroups.add(group);
        group.addUser(this);
      } else {
        this.groupLogger.warn("Can not find display group '{}' for user '{}'", groupName, this.getName());
      }
    }

    if (this.displayGroups.isEmpty()) {
      this.displayGroups.add(Server.getGuestDisplayGroup());
      Server.getGuestDisplayGroup().addUser(this);
    }

    this.updateChatName();

    ((ScoreboardManager) Server.getScoreboardManager()).updatePlayerGroup(this);
  }

  protected void updateChatName() {
    Component component = Component.text("");
    StringBuilder nameTD = new StringBuilder("§r");

    if (this.getNick() == null) {
      for (DisplayGroup group : this.getMainDisplayGroups()) {
        if (group.getPrefix() != null) {
          component = component.append(
              Component.text(group.getPrefix()).color(group.getPrefixColor()));
          nameTD.append(group.getTDPrefixColor()).append(group.getPrefix());
        }
      }

      if (this.getPrefix() != null) {
        component = component.append(this.getPrefix());
        nameTD.append(this.getTDPrefix());
      }

      component = component.append(this.getPlayerChatName());
      nameTD.append("§r").append(this.getPlayer().getName());

      if (this.getSuffix() != null) {
        component = component.append(this.getSuffix());
        nameTD.append(this.getTDSuffix());
      }
    } else {
      DisplayGroup group = Server.getMemberDisplayGroup();
      if (group.getPrefix() != null) {
        component = component.append(
            Component.text(group.getPrefix(), group.getPrefixColor()));
        nameTD.append(group.getTDPrefixColor()).append(group.getPrefix());
      }
      component.append(this.getNick());
      nameTD.append("§r").append(this.getTDNick());

    }
    this.chatNameComponent = component;
    this.TDChatName = nameTD.toString();
  }

  @NotNull
  protected Component getPlayerChatName() {
    return Component.text(this.getPlayer().getName(), ExTextColor.WHITE);
  }

  @NotNull
  public SortedSet<DisplayGroup> getDisplayGroups() {
    return this.displayGroups;
  }

  @NotNull
  public DisplayGroup getMasterDisplayGroup() {
    return this.displayGroups.first();
  }

  @NotNull
  public List<DisplayGroup> getMainDisplayGroups() {
    return this.displayGroups.stream().filter(displayGroup -> displayGroup.isShowAlways()
                                                              || displayGroup.equals(this.getMasterDisplayGroup())).sorted()
        .limit(DisplayGroup.MAX_PREFIX_LENGTH).toList();
  }

  public void updatePunishmentFromDatabase() {
    Punishment punishment = this.dbUser.getPunishment().asPunishment();
    PunishType type = punishment.getType();

    if (punishment.isExpired()) {
      if (this.mutedUntil != null) {
        this.punishLogger.info("Un-muted user '{}' (invoked by channel/database update)", this.getName());
      }
      this.mutedUntil = null;
    } else if (type.equals(PunishType.TEMP_MUTE)) {
      this.mutedUntil = punishment.getDate().plusSeconds(punishment.getDuration().toSeconds());
      this.punishLogger.info("Muted user '{}' (invoked by channel/database update)", this.getName());
    }
  }

  @Override
  public void sendPacket(Packet<?> packet) {
    if (packet == null) {
      return;
    }

    Server.getPacketManager().sendPacket(this.getPlayer(), packet);
  }

  public void setPvpMode(boolean oldPvP) {
    if (oldPvP) {
      this.setAttackSpeed(PvPManager.ATTACK_SPEED);
    } else {
      this.resetAttackSpeed();
    }
  }

  public void setAttackSpeed(double attackSpeed) {
    this.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(attackSpeed);
  }

  public void resetAttackSpeed() {
    this.setAttackSpeed(4);
  }

  @Override
  public NameTagVisibility canSeeNameTagOf(TablistPlayer player) {
    return NameTagVisibility.ALWAYS;
  }

  @Override
  public void addTablistEntry(TablistPlayer player, String rank) {
    scoreboardUser.addTablistEntry(player, rank);
  }

  @Override
  public void addTablistEntry(TablistPlayer player) {
    scoreboardUser.addTablistEntry(player);
  }

  @Override
  public void removeTablistEntry(TablistPlayer player) {
    scoreboardUser.removeTablistEntry(player);
  }

  @Override
  public void removeTablistEntry(TablistPlayer player, String rank) {
    scoreboardUser.removeTablistEntry(player, rank);
  }

  @Override
  public void setTablist(Tablist tablist) {
    scoreboardUser.setTablist(tablist);
  }

  @Override
  public void setSideboard(Sideboard sideboard) {
    scoreboardUser.setSideboard(sideboard);
  }

  @Override
  public void setSideboardTitle(String title) {
    scoreboardUser.setSideboardTitle(title);
  }

  @Override
  public void setSideboardScore(int line, @NotNull String text) {
    scoreboardUser.setSideboardScore(line, text);
  }

  @Override
  public void removeSideboardScore(int line, @NotNull String text) {
    scoreboardUser.removeSideboardScore(line, text);
  }

  @Override
  public void removeSideboardScore(int line) {
    scoreboardUser.removeSideboardScore(line);
  }

  @Override
  public void resetSideboard() {
    scoreboardUser.resetSideboard();
  }
}
