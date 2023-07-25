/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user;


import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.Title;
import com.destroystokyo.paper.block.TargetBlockInfo;
import com.destroystokyo.paper.entity.TargetEntityInfo;
import io.papermc.paper.entity.LookAnchor;
import io.papermc.paper.entity.TeleportFlag;
import io.papermc.paper.math.Position;
import io.papermc.paper.threadedregions.scheduler.EntityScheduler;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.pointer.Pointer;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.title.TitlePart;
import net.kyori.adventure.util.TriState;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.sign.Side;
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
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;

public class UserPlayerDelegation implements Player {

  protected final Player player;

  public UserPlayerDelegation(Player player) {
    this.player = player;
  }

  @Override
  public @NotNull Identity identity() {
    return player.identity();
  }

  @Override
  @UnmodifiableView
  @NotNull
  public Iterable<? extends BossBar> activeBossBars() {
    return player.activeBossBars();
  }

  @Override
  public @NotNull Component displayName() {
    return player.displayName();
  }

  @Override
  public void displayName(@Nullable Component displayName) {
    player.displayName(displayName);
  }

  @Override
  @NotNull
  public String getName() {
    return player.getName();
  }

  @Override
  @Deprecated
  @NotNull
  public String getDisplayName() {
    return player.getDisplayName();
  }

  @Override
  @Deprecated
  public void setDisplayName(@Nullable String name) {
    player.setDisplayName(name);
  }

  @Override
  public void playerListName(@Nullable Component name) {
    player.playerListName(name);
  }

  @Override
  public @NotNull Component playerListName() {
    return player.playerListName();
  }

  @Override
  public @Nullable Component playerListHeader() {
    return player.playerListHeader();
  }

  @Override
  public @Nullable Component playerListFooter() {
    return player.playerListFooter();
  }

  @Override
  @Deprecated
  @NotNull
  public String getPlayerListName() {
    return player.getPlayerListName();
  }

  @Override
  @Deprecated
  public void setPlayerListName(@Nullable String name) {
    player.setPlayerListName(name);
  }

  @Override
  @Deprecated
  @Nullable
  public String getPlayerListHeader() {
    return player.getPlayerListHeader();
  }

  @Override
  @Deprecated
  @Nullable
  public String getPlayerListFooter() {
    return player.getPlayerListFooter();
  }

  @Override
  @Deprecated
  public void setPlayerListHeader(@Nullable String header) {
    player.setPlayerListHeader(header);
  }

  @Override
  @Deprecated
  public void setPlayerListFooter(@Nullable String footer) {
    player.setPlayerListFooter(footer);
  }

  @Override
  @Deprecated
  public void setPlayerListHeaderFooter(@Nullable String header, @Nullable String footer) {
    player.setPlayerListHeaderFooter(header, footer);
  }

  @Override
  public void setCompassTarget(@NotNull Location loc) {
    player.setCompassTarget(loc);
  }

  @Override
  public @NotNull Location getCompassTarget() {
    return player.getCompassTarget();
  }

  @Override
  public @Nullable InetSocketAddress getAddress() {
    return player.getAddress();
  }

  @Override
  public void sendRawMessage(@NotNull String message) {
    player.sendRawMessage(message);
  }

  @Override
  @Deprecated
  public void kickPlayer(@Nullable String message) {
    player.kickPlayer(message);
  }

  @Override
  public void kick() {
    player.kick();
  }

  @Override
  public void kick(@Nullable Component message) {
    player.kick(message);
  }

  @Override
  public void kick(@Nullable Component message, PlayerKickEvent.@NotNull Cause cause) {
    player.kick(message, cause);
  }

  @Override
  public @Nullable BanEntry<PlayerProfile> ban(@Nullable String reason, @Nullable Date expires, @Nullable String source, boolean kickPlayer) {
    return player.ban(reason, expires, source, kickPlayer);
  }

  @Override
  public @Nullable BanEntry<InetAddress> banIp(@Nullable String reason, @Nullable Date expires, @Nullable String source, boolean kickPlayer) {
    return player.banIp(reason, expires, source, kickPlayer);
  }

  @Override
  public void chat(@NotNull String msg) {
    player.chat(msg);
  }

  @Override
  public boolean performCommand(@NotNull String command) {
    return player.performCommand(command);
  }

  @Override
  @Deprecated
  public boolean isOnGround() {
    return player.isOnGround();
  }

  @Override
  public boolean isSneaking() {
    return player.isSneaking();
  }

  @Override
  public void setSneaking(boolean sneak) {
    player.setSneaking(sneak);
  }

  @Override
  public boolean isSprinting() {
    return player.isSprinting();
  }

  @Override
  public void setSprinting(boolean sprinting) {
    player.setSprinting(sprinting);
  }

  @Override
  public void saveData() {
    player.saveData();
  }

  @Override
  public void loadData() {
    player.loadData();
  }

  @Override
  public void setSleepingIgnored(boolean isSleeping) {
    player.setSleepingIgnored(isSleeping);
  }

  @Override
  public boolean isSleepingIgnored() {
    return player.isSleepingIgnored();
  }

  @Override
  public @Nullable Location getBedSpawnLocation() {
    return player.getBedSpawnLocation();
  }

  @Override
  public void setBedSpawnLocation(@Nullable Location location) {
    player.setBedSpawnLocation(location);
  }

  @Override
  public void setBedSpawnLocation(@Nullable Location location, boolean force) {
    player.setBedSpawnLocation(location, force);
  }

  @Override
  @Deprecated
  public void playNote(@NotNull Location loc, byte instrument, byte note) {
    player.playNote(loc, instrument, note);
  }

  @Override
  public void playNote(@NotNull Location loc, @NotNull Instrument instrument, @NotNull Note note) {
    player.playNote(loc, instrument, note);
  }

  @Override
  public void playSound(@NotNull Location location, @NotNull Sound sound, float volume, float pitch) {
    player.playSound(location, sound, volume, pitch);
  }

  @Override
  public void playSound(@NotNull Location location, @NotNull String sound, float volume, float pitch) {
    player.playSound(location, sound, volume, pitch);
  }

  @Override
  public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch) {
    player.playSound(location, sound, category, volume, pitch);
  }

  @Override
  public void playSound(@NotNull Location location, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch) {
    player.playSound(location, sound, category, volume, pitch);
  }

  @Override
  public void playSound(@NotNull Entity entity, @NotNull Sound sound, float volume, float pitch) {
    player.playSound(entity, sound, volume, pitch);
  }

  @Override
  public void playSound(@NotNull Entity entity, @NotNull String sound, float volume, float pitch) {
    player.playSound(entity, sound, volume, pitch);
  }

  @Override
  public void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch) {
    player.playSound(entity, sound, category, volume, pitch);
  }

  @Override
  public void playSound(@NotNull Entity entity, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch) {
    player.playSound(entity, sound, category, volume, pitch);
  }

  @Override
  public void stopSound(@NotNull Sound sound) {
    player.stopSound(sound);
  }

  @Override
  public void stopSound(@NotNull String sound) {
    player.stopSound(sound);
  }

  @Override
  public void stopSound(@NotNull Sound sound, @Nullable SoundCategory category) {
    player.stopSound(sound, category);
  }

  @Override
  public void stopSound(@NotNull String sound, @Nullable SoundCategory category) {
    player.stopSound(sound, category);
  }

  @Override
  public void stopSound(@NotNull SoundCategory category) {
    player.stopSound(category);
  }

  @Override
  public void stopAllSounds() {
    player.stopAllSounds();
  }

  @Override
  @Deprecated
  public void playEffect(@NotNull Location loc, @NotNull Effect effect, int data) {
    player.playEffect(loc, effect, data);
  }

  @Override
  public <T> void playEffect(@NotNull Location loc, @NotNull Effect effect, @Nullable T data) {
    player.playEffect(loc, effect, data);
  }

  @Override
  public boolean breakBlock(@NotNull Block block) {
    return player.breakBlock(block);
  }

  @Override
  @Deprecated
  public void sendBlockChange(@NotNull Location loc, @NotNull Material material, byte data) {
    player.sendBlockChange(loc, material, data);
  }

  @Override
  public void sendBlockChange(@NotNull Location loc, @NotNull BlockData block) {
    player.sendBlockChange(loc, block);
  }

  @Override
  public void sendBlockChanges(@NotNull Collection<BlockState> blocks) {
    player.sendBlockChanges(blocks);
  }

  @Override
  @Deprecated
  public void sendBlockChanges(@NotNull Collection<BlockState> blocks, boolean suppressLightUpdates) {
    player.sendBlockChanges(blocks, suppressLightUpdates);
  }

  @Override
  public void sendBlockDamage(@NotNull Location loc, float progress) {
    player.sendBlockDamage(loc, progress);
  }

  @Override
  public void sendMultiBlockChange(@NotNull Map<? extends Position, BlockData> blockChanges) {
    player.sendMultiBlockChange(blockChanges);
  }

  @Override
  @Deprecated
  public void sendMultiBlockChange(@NotNull Map<? extends Position, BlockData> blockChanges, boolean suppressLightUpdates) {
    player.sendMultiBlockChange(blockChanges, suppressLightUpdates);
  }

  @Override
  public void sendBlockDamage(@NotNull Location loc, float progress, @NotNull Entity source) {
    player.sendBlockDamage(loc, progress, source);
  }

  @Override
  public void sendBlockDamage(@NotNull Location loc, float progress, int sourceId) {
    player.sendBlockDamage(loc, progress, sourceId);
  }

  @Override
  public void sendEquipmentChange(@NotNull LivingEntity entity, @NotNull EquipmentSlot slot, @Nullable ItemStack item) {
    player.sendEquipmentChange(entity, slot, item);
  }

  @Override
  public void sendEquipmentChange(@NotNull LivingEntity entity, @NotNull Map<EquipmentSlot, ItemStack> items) {
    player.sendEquipmentChange(entity, items);
  }

  @Override
  public void sendSignChange(@NotNull Location loc, @Nullable List<? extends Component> lines) throws IllegalArgumentException {
    player.sendSignChange(loc, lines);
  }

  @Override
  public void sendSignChange(@NotNull Location loc, @Nullable List<? extends Component> lines, @NotNull DyeColor dyeColor) throws IllegalArgumentException {
    player.sendSignChange(loc, lines, dyeColor);
  }

  @Override
  public void sendSignChange(@NotNull Location loc, @Nullable List<? extends Component> lines, boolean hasGlowingText) throws IllegalArgumentException {
    player.sendSignChange(loc, lines, hasGlowingText);
  }

  @Override
  public void sendSignChange(@NotNull Location loc, @Nullable List<? extends Component> lines, @NotNull DyeColor dyeColor, boolean hasGlowingText) throws IllegalArgumentException {
    player.sendSignChange(loc, lines, dyeColor, hasGlowingText);
  }

  @Override
  @Deprecated
  public void sendSignChange(@NotNull Location loc, @Nullable String[] lines) throws IllegalArgumentException {
    player.sendSignChange(loc, lines);
  }

  @Override
  @Deprecated
  public void sendSignChange(@NotNull Location loc, @Nullable String[] lines, @NotNull DyeColor dyeColor) throws IllegalArgumentException {
    player.sendSignChange(loc, lines, dyeColor);
  }

  @Override
  @Deprecated
  public void sendSignChange(@NotNull Location loc, @Nullable String[] lines, @NotNull DyeColor dyeColor, boolean hasGlowingText) throws IllegalArgumentException {
    player.sendSignChange(loc, lines, dyeColor, hasGlowingText);
  }

  @Override
  @ApiStatus.Experimental
  public void sendBlockUpdate(@NotNull Location loc, @NotNull TileState tileState) throws IllegalArgumentException {
    player.sendBlockUpdate(loc, tileState);
  }

  @Override
  public void sendMap(@NotNull MapView map) {
    player.sendMap(map);
  }

  @Override
  public void showWinScreen() {
    player.showWinScreen();
  }

  @Override
  public boolean hasSeenWinScreen() {
    return player.hasSeenWinScreen();
  }

  @Override
  public void setHasSeenWinScreen(boolean hasSeenWinScreen) {
    player.setHasSeenWinScreen(hasSeenWinScreen);
  }

  @Override
  public @Nullable BanEntry banPlayerFull(@Nullable String reason) {
    return player.banPlayerFull(reason);
  }

  @Override
  public @Nullable BanEntry banPlayerFull(@Nullable String reason, @Nullable String source) {
    return player.banPlayerFull(reason, source);
  }

  @Override
  public @Nullable BanEntry banPlayerFull(@Nullable String reason, @Nullable Date expires) {
    return player.banPlayerFull(reason, expires);
  }

  @Override
  public @Nullable BanEntry banPlayerFull(@Nullable String reason, @Nullable Date expires, @Nullable String source) {
    return player.banPlayerFull(reason, expires, source);
  }

  @Override
  public @Nullable BanEntry banPlayerIP(@Nullable String reason, boolean kickPlayer) {
    return player.banPlayerIP(reason, kickPlayer);
  }

  @Override
  public @Nullable BanEntry banPlayerIP(@Nullable String reason, @Nullable String source, boolean kickPlayer) {
    return player.banPlayerIP(reason, source, kickPlayer);
  }

  @Override
  public @Nullable BanEntry banPlayerIP(@Nullable String reason, @Nullable Date expires, boolean kickPlayer) {
    return player.banPlayerIP(reason, expires, kickPlayer);
  }

  @Override
  public @Nullable BanEntry banPlayerIP(@Nullable String reason) {
    return player.banPlayerIP(reason);
  }

  @Override
  public @Nullable BanEntry banPlayerIP(@Nullable String reason, @Nullable String source) {
    return player.banPlayerIP(reason, source);
  }

  @Override
  public @Nullable BanEntry banPlayerIP(@Nullable String reason, @Nullable Date expires) {
    return player.banPlayerIP(reason, expires);
  }

  @Override
  public @Nullable BanEntry banPlayerIP(@Nullable String reason, @Nullable Date expires, @Nullable String source) {
    return player.banPlayerIP(reason, expires, source);
  }

  @Override
  public @Nullable BanEntry banPlayerIP(@Nullable String reason, @Nullable Date expires, @Nullable String source, boolean kickPlayer) {
    return player.banPlayerIP(reason, expires, source, kickPlayer);
  }

  @Override
  @Deprecated
  public void sendActionBar(@NotNull String message) {
    player.sendActionBar(message);
  }

  @Override
  @Deprecated
  public void sendActionBar(char alternateChar, @NotNull String message) {
    player.sendActionBar(alternateChar, message);
  }

  @Override
  @Deprecated
  public void sendActionBar(@NotNull BaseComponent... message) {
    player.sendActionBar(message);
  }

  @Override
  @Deprecated
  public void sendMessage(@NotNull BaseComponent component) {
    player.sendMessage(component);
  }

  @Override
  @Deprecated
  public void sendMessage(@NotNull BaseComponent... components) {
    player.sendMessage(components);
  }

  @Override
  @Deprecated
  public void sendMessage(ChatMessageType position, BaseComponent... components) {
    player.sendMessage(position, components);
  }

  @Override
  @Deprecated
  public void setPlayerListHeaderFooter(@Nullable BaseComponent[] header, @Nullable BaseComponent[] footer) {
    player.setPlayerListHeaderFooter(header, footer);
  }

  @Override
  @Deprecated
  public void setPlayerListHeaderFooter(@Nullable BaseComponent header, @Nullable BaseComponent footer) {
    player.setPlayerListHeaderFooter(header, footer);
  }

  @Override
  @Deprecated
  public void setTitleTimes(int fadeInTicks, int stayTicks, int fadeOutTicks) {
    player.setTitleTimes(fadeInTicks, stayTicks, fadeOutTicks);
  }

  @Override
  @Deprecated
  public void setSubtitle(BaseComponent[] subtitle) {
    player.setSubtitle(subtitle);
  }

  @Override
  @Deprecated
  public void setSubtitle(BaseComponent subtitle) {
    player.setSubtitle(subtitle);
  }

  @Override
  @Deprecated
  public void showTitle(@Nullable BaseComponent[] title) {
    player.showTitle(title);
  }

  @Override
  @Deprecated
  public void showTitle(@Nullable BaseComponent title) {
    player.showTitle(title);
  }

  @Override
  @Deprecated
  public void showTitle(@Nullable BaseComponent[] title, @Nullable BaseComponent[] subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
    player.showTitle(title, subtitle, fadeInTicks, stayTicks, fadeOutTicks);
  }

  @Override
  @Deprecated
  public void showTitle(@Nullable BaseComponent title, @Nullable BaseComponent subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
    player.showTitle(title, subtitle, fadeInTicks, stayTicks, fadeOutTicks);
  }

  @Override
  @Deprecated
  public void sendTitle(@NotNull Title title) {
    player.sendTitle(title);
  }

  @Override
  @Deprecated
  public void updateTitle(@NotNull Title title) {
    player.updateTitle(title);
  }

  @Override
  @Deprecated
  public void hideTitle() {
    player.hideTitle();
  }

  @Override
  public void sendHurtAnimation(float yaw) {
    player.sendHurtAnimation(yaw);
  }

  @Override
  public void addCustomChatCompletions(@NotNull Collection<String> completions) {
    player.addCustomChatCompletions(completions);
  }

  @Override
  public void removeCustomChatCompletions(@NotNull Collection<String> completions) {
    player.removeCustomChatCompletions(completions);
  }

  @Override
  public void setCustomChatCompletions(@NotNull Collection<String> completions) {
    player.setCustomChatCompletions(completions);
  }

  @Override
  @ApiStatus.Internal
  public void updateInventory() {
    player.updateInventory();
  }

  @Override
  public @Nullable GameMode getPreviousGameMode() {
    return player.getPreviousGameMode();
  }

  @Override
  public void setPlayerTime(long time, boolean relative) {
    player.setPlayerTime(time, relative);
  }

  @Override
  public long getPlayerTime() {
    return player.getPlayerTime();
  }

  @Override
  public long getPlayerTimeOffset() {
    return player.getPlayerTimeOffset();
  }

  @Override
  public boolean isPlayerTimeRelative() {
    return player.isPlayerTimeRelative();
  }

  @Override
  public void resetPlayerTime() {
    player.resetPlayerTime();
  }

  @Override
  public void setPlayerWeather(@NotNull WeatherType type) {
    player.setPlayerWeather(type);
  }

  @Override
  public @Nullable WeatherType getPlayerWeather() {
    return player.getPlayerWeather();
  }

  @Override
  public void resetPlayerWeather() {
    player.resetPlayerWeather();
  }

  @Override
  public void giveExp(int amount) {
    player.giveExp(amount);
  }

  @Override
  public int getExpCooldown() {
    return player.getExpCooldown();
  }

  @Override
  public void setExpCooldown(int ticks) {
    player.setExpCooldown(ticks);
  }

  @Override
  public void giveExp(int amount, boolean applyMending) {
    player.giveExp(amount, applyMending);
  }

  @Override
  public int applyMending(int amount) {
    return player.applyMending(amount);
  }

  @Override
  public void giveExpLevels(int amount) {
    player.giveExpLevels(amount);
  }

  @Override
  public float getExp() {
    return player.getExp();
  }

  @Override
  public void setExp(float exp) {
    player.setExp(exp);
  }

  @Override
  public int getLevel() {
    return player.getLevel();
  }

  @Override
  public void setLevel(int level) {
    player.setLevel(level);
  }

  @Override
  public int getTotalExperience() {
    return player.getTotalExperience();
  }

  @Override
  public void setTotalExperience(int exp) {
    player.setTotalExperience(exp);
  }

  @Override
  public void sendExperienceChange(float progress) {
    player.sendExperienceChange(progress);
  }

  @Override
  public void sendExperienceChange(float progress, int level) {
    player.sendExperienceChange(progress, level);
  }

  @Override
  public boolean getAllowFlight() {
    return player.getAllowFlight();
  }

  @Override
  public void setAllowFlight(boolean flight) {
    player.setAllowFlight(flight);
  }

  @Override
  public void setFlyingFallDamage(@NotNull TriState flyingFallDamage) {
    player.setFlyingFallDamage(flyingFallDamage);
  }

  @Override
  public @NotNull TriState hasFlyingFallDamage() {
    return player.hasFlyingFallDamage();
  }

  @Override
  @Deprecated
  public void hidePlayer(@NotNull Player player) {
    this.player.hidePlayer(player);
  }

  @Override
  public void hidePlayer(@NotNull Plugin plugin, @NotNull Player player) {
    this.player.hidePlayer(plugin, player);
  }

  @Override
  @Deprecated
  public void showPlayer(@NotNull Player player) {
    this.player.showPlayer(player);
  }

  @Override
  public void showPlayer(@NotNull Plugin plugin, @NotNull Player player) {
    this.player.showPlayer(plugin, player);
  }

  @Override
  public boolean canSee(@NotNull Player player) {
    return this.player.canSee(player);
  }

  @Override
  @ApiStatus.Experimental
  public void hideEntity(@NotNull Plugin plugin, @NotNull Entity entity) {
    player.hideEntity(plugin, entity);
  }

  @Override
  @ApiStatus.Experimental
  public void showEntity(@NotNull Plugin plugin, @NotNull Entity entity) {
    player.showEntity(plugin, entity);
  }

  @Override
  @ApiStatus.Experimental
  public boolean canSee(@NotNull Entity entity) {
    return player.canSee(entity);
  }

  @Override
  public boolean isFlying() {
    return player.isFlying();
  }

  @Override
  public void setFlying(boolean value) {
    player.setFlying(value);
  }

  @Override
  public void setFlySpeed(float value) throws IllegalArgumentException {
    player.setFlySpeed(value);
  }

  @Override
  public void setWalkSpeed(float value) throws IllegalArgumentException {
    player.setWalkSpeed(value);
  }

  @Override
  public float getFlySpeed() {
    return player.getFlySpeed();
  }

  @Override
  public float getWalkSpeed() {
    return player.getWalkSpeed();
  }

  @Override
  @Deprecated
  public void setTexturePack(@NotNull String url) {
    player.setTexturePack(url);
  }

  @Override
  @Deprecated
  public void setResourcePack(@NotNull String url) {
    player.setResourcePack(url);
  }

  @Override
  public void setResourcePack(@NotNull String url, @Nullable byte[] hash) {
    player.setResourcePack(url, hash);
  }

  @Override
  @Deprecated
  public void setResourcePack(@NotNull String url, @Nullable byte[] hash, @Nullable String prompt) {
    player.setResourcePack(url, hash, prompt);
  }

  @Override
  public void setResourcePack(@NotNull String url, byte @Nullable [] hash, @Nullable Component prompt) {
    player.setResourcePack(url, hash, prompt);
  }

  @Override
  public void setResourcePack(@NotNull String url, @Nullable byte[] hash, boolean force) {
    player.setResourcePack(url, hash, force);
  }

  @Override
  @Deprecated
  public void setResourcePack(@NotNull String url, @Nullable byte[] hash, @Nullable String prompt, boolean force) {
    player.setResourcePack(url, hash, prompt, force);
  }

  @Override
  public void setResourcePack(@NotNull String url, byte @Nullable [] hash, @Nullable Component prompt, boolean force) {
    player.setResourcePack(url, hash, prompt, force);
  }

  @Override
  public @NotNull Scoreboard getScoreboard() {
    return player.getScoreboard();
  }

  @Override
  public void setScoreboard(@NotNull Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
    player.setScoreboard(scoreboard);
  }

  @Override
  public @Nullable WorldBorder getWorldBorder() {
    return player.getWorldBorder();
  }

  @Override
  public void setWorldBorder(@Nullable WorldBorder border) {
    player.setWorldBorder(border);
  }

  @Override
  public void sendHealthUpdate(double health, int foodLevel, float saturation) {
    player.sendHealthUpdate(health, foodLevel, saturation);
  }

  @Override
  public void sendHealthUpdate() {
    player.sendHealthUpdate();
  }

  @Override
  public boolean isHealthScaled() {
    return player.isHealthScaled();
  }

  @Override
  public void setHealthScaled(boolean scale) {
    player.setHealthScaled(scale);
  }

  @Override
  public void setHealthScale(double scale) throws IllegalArgumentException {
    player.setHealthScale(scale);
  }

  @Override
  public double getHealthScale() {
    return player.getHealthScale();
  }

  @Override
  public @Nullable Entity getSpectatorTarget() {
    return player.getSpectatorTarget();
  }

  @Override
  public void setSpectatorTarget(@Nullable Entity entity) {
    player.setSpectatorTarget(entity);
  }

  @Override
  @Deprecated
  public void sendTitle(@Nullable String title, @Nullable String subtitle) {
    player.sendTitle(title, subtitle);
  }

  @Override
  @Deprecated
  public void sendTitle(@Nullable String title, @Nullable String subtitle, int fadeIn, int stay, int fadeOut) {
    player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
  }

  @Override
  public void resetTitle() {
    player.resetTitle();
  }

  @Override
  public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count) {
    player.spawnParticle(particle, location, count);
  }

  @Override
  public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count) {
    player.spawnParticle(particle, x, y, z, count);
  }

  @Override
  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, @Nullable T data) {
    player.spawnParticle(particle, location, count, data);
  }

  @Override
  public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, @Nullable T data) {
    player.spawnParticle(particle, x, y, z, count, data);
  }

  @Override
  public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ) {
    player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ);
  }

  @Override
  public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
    player.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ);
  }

  @Override
  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, @Nullable T data) {
    player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, data);
  }

  @Override
  public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, @Nullable T data) {
    player.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, data);
  }

  @Override
  public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
    player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra);
  }

  @Override
  public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
    player.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra);
  }

  @Override
  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {
    player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, data);
  }

  @Override
  public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {
    player.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
  }

  @Override
  public @NotNull AdvancementProgress getAdvancementProgress(@NotNull Advancement advancement) {
    return player.getAdvancementProgress(advancement);
  }

  @Override
  public int getClientViewDistance() {
    return player.getClientViewDistance();
  }

  @Override
  public @NotNull Locale locale() {
    return player.locale();
  }

  @Override
  public int getPing() {
    return player.getPing();
  }

  @Override
  @Deprecated
  @NotNull
  public String getLocale() {
    return player.getLocale();
  }

  @Override
  public boolean getAffectsSpawning() {
    return player.getAffectsSpawning();
  }

  @Override
  public void setAffectsSpawning(boolean affects) {
    player.setAffectsSpawning(affects);
  }

  @Override
  public int getViewDistance() {
    return player.getViewDistance();
  }

  @Override
  public void setViewDistance(int viewDistance) {
    player.setViewDistance(viewDistance);
  }

  @Override
  public int getSimulationDistance() {
    return player.getSimulationDistance();
  }

  @Override
  public void setSimulationDistance(int simulationDistance) {
    player.setSimulationDistance(simulationDistance);
  }

  @Override
  @Deprecated
  public int getNoTickViewDistance() {
    return player.getNoTickViewDistance();
  }

  @Override
  @Deprecated
  public void setNoTickViewDistance(int viewDistance) {
    player.setNoTickViewDistance(viewDistance);
  }

  @Override
  public int getSendViewDistance() {
    return player.getSendViewDistance();
  }

  @Override
  public void setSendViewDistance(int viewDistance) {
    player.setSendViewDistance(viewDistance);
  }

  @Override
  public void updateCommands() {
    player.updateCommands();
  }

  @Override
  public void openBook(@NotNull ItemStack book) {
    player.openBook(book);
  }

  @Override
  @Deprecated
  public void openSign(@NotNull Sign sign) {
    player.openSign(sign);
  }

  @Override
  public void openSign(@NotNull Sign sign, @NotNull Side side) {
    player.openSign(sign, side);
  }

  @Override
  public void showDemoScreen() {
    player.showDemoScreen();
  }

  @Override
  public boolean isAllowingServerListings() {
    return player.isAllowingServerListings();
  }

  @Override
  public @NotNull HoverEvent<HoverEvent.ShowEntity> asHoverEvent(@NotNull UnaryOperator<HoverEvent.ShowEntity> op) {
    return player.asHoverEvent(op);
  }

  @Override
  public void setResourcePack(@NotNull String url, @NotNull String hash) {
    player.setResourcePack(url, hash);
  }

  @Override
  public void setResourcePack(@NotNull String url, @NotNull String hash, boolean required) {
    player.setResourcePack(url, hash, required);
  }

  @Override
  public void setResourcePack(@NotNull String url, @NotNull String hash, boolean required, @Nullable Component resourcePackPrompt) {
    player.setResourcePack(url, hash, required, resourcePackPrompt);
  }

  @Override
  public PlayerResourcePackStatusEvent.@Nullable Status getResourcePackStatus() {
    return player.getResourcePackStatus();
  }

  @Override
  @Deprecated
  @Nullable
  public String getResourcePackHash() {
    return player.getResourcePackHash();
  }

  @Override
  public boolean hasResourcePack() {
    return player.hasResourcePack();
  }

  @Override
  public com.destroystokyo.paper.profile.@NotNull PlayerProfile getPlayerProfile() {
    return player.getPlayerProfile();
  }

  @Override
  public void setPlayerProfile(com.destroystokyo.paper.profile.@NotNull PlayerProfile profile) {
    player.setPlayerProfile(profile);
  }

  @Override
  public float getCooldownPeriod() {
    return player.getCooldownPeriod();
  }

  @Override
  public float getCooledAttackStrength(float adjustTicks) {
    return player.getCooledAttackStrength(adjustTicks);
  }

  @Override
  public void resetCooldown() {
    player.resetCooldown();
  }

  @Override
  public <T> @NotNull T getClientOption(@NotNull ClientOption<T> option) {
    return player.getClientOption(option);
  }

  @Override
  public @Nullable Firework boostElytra(@NotNull ItemStack firework) {
    return player.boostElytra(firework);
  }

  @Override
  public void sendOpLevel(byte level) {
    player.sendOpLevel(level);
  }

  @Override
  @Deprecated(since = "1.20.1")
  public void addAdditionalChatCompletions(@NotNull Collection<String> completions) {
    player.addAdditionalChatCompletions(completions);
  }

  @Override
  @Deprecated(since = "1.20.1")
  public void removeAdditionalChatCompletions(@NotNull Collection<String> completions) {
    player.removeAdditionalChatCompletions(completions);
  }

  @Override
  @Nullable
  public String getClientBrandName() {
    return player.getClientBrandName();
  }

  @Override
  @ApiStatus.Experimental
  public void setRotation(float yaw, float pitch) {
    player.setRotation(yaw, pitch);
  }

  @Override
  @ApiStatus.Experimental
  public void lookAt(double x, double y, double z, @NotNull LookAnchor playerAnchor) {
    player.lookAt(x, y, z, playerAnchor);
  }

  @Override
  @ApiStatus.Experimental
  public void lookAt(@NotNull Position position, @NotNull LookAnchor playerAnchor) {
    player.lookAt(position, playerAnchor);
  }

  @Override
  @ApiStatus.Experimental
  public void lookAt(@NotNull Entity entity, @NotNull LookAnchor playerAnchor, @NotNull LookAnchor entityAnchor) {
    player.lookAt(entity, playerAnchor, entityAnchor);
  }

  @Override
  public void showElderGuardian() {
    player.showElderGuardian();
  }

  @Override
  public void showElderGuardian(boolean silent) {
    player.showElderGuardian(silent);
  }

  @Override
  public int getWardenWarningCooldown() {
    return player.getWardenWarningCooldown();
  }

  @Override
  public void setWardenWarningCooldown(int cooldown) {
    player.setWardenWarningCooldown(cooldown);
  }

  @Override
  public int getWardenTimeSinceLastWarning() {
    return player.getWardenTimeSinceLastWarning();
  }

  @Override
  public void setWardenTimeSinceLastWarning(int time) {
    player.setWardenTimeSinceLastWarning(time);
  }

  @Override
  public int getWardenWarningLevel() {
    return player.getWardenWarningLevel();
  }

  @Override
  public void setWardenWarningLevel(int warningLevel) {
    player.setWardenWarningLevel(warningLevel);
  }

  @Override
  public void increaseWardenWarningLevel() {
    player.increaseWardenWarningLevel();
  }

  @Override
  @NotNull
  public Spigot spigot() {
    return player.spigot();
  }

  @Override
  public @NotNull EntityEquipment getEquipment() {
    return player.getEquipment();
  }

  @Override
  public @NotNull PlayerInventory getInventory() {
    return player.getInventory();
  }

  @Override
  public @NotNull Inventory getEnderChest() {
    return player.getEnderChest();
  }

  @Override
  public @NotNull MainHand getMainHand() {
    return player.getMainHand();
  }

  @Override
  public boolean setWindowProperty(InventoryView.@NotNull Property prop, int value) {
    return player.setWindowProperty(prop, value);
  }

  @Override
  public int getEnchantmentSeed() {
    return player.getEnchantmentSeed();
  }

  @Override
  public void setEnchantmentSeed(int seed) {
    player.setEnchantmentSeed(seed);
  }

  @Override
  public @NotNull InventoryView getOpenInventory() {
    return player.getOpenInventory();
  }

  @Override
  public @Nullable InventoryView openInventory(@NotNull Inventory inventory) {
    return player.openInventory(inventory);
  }

  @Override
  public @Nullable InventoryView openWorkbench(@Nullable Location location, boolean force) {
    return player.openWorkbench(location, force);
  }

  @Override
  public @Nullable InventoryView openEnchanting(@Nullable Location location, boolean force) {
    return player.openEnchanting(location, force);
  }

  @Override
  public void openInventory(@NotNull InventoryView inventory) {
    player.openInventory(inventory);
  }

  @Override
  public @Nullable InventoryView openMerchant(@NotNull Villager trader, boolean force) {
    return player.openMerchant(trader, force);
  }

  @Override
  public @Nullable InventoryView openMerchant(@NotNull Merchant merchant, boolean force) {
    return player.openMerchant(merchant, force);
  }

  @Override
  public @Nullable InventoryView openAnvil(@Nullable Location location, boolean force) {
    return player.openAnvil(location, force);
  }

  @Override
  public @Nullable InventoryView openCartographyTable(@Nullable Location location, boolean force) {
    return player.openCartographyTable(location, force);
  }

  @Override
  public @Nullable InventoryView openGrindstone(@Nullable Location location, boolean force) {
    return player.openGrindstone(location, force);
  }

  @Override
  public @Nullable InventoryView openLoom(@Nullable Location location, boolean force) {
    return player.openLoom(location, force);
  }

  @Override
  public @Nullable InventoryView openSmithingTable(@Nullable Location location, boolean force) {
    return player.openSmithingTable(location, force);
  }

  @Override
  public @Nullable InventoryView openStonecutter(@Nullable Location location, boolean force) {
    return player.openStonecutter(location, force);
  }

  @Override
  public void closeInventory() {
    player.closeInventory();
  }

  @Override
  public void closeInventory(InventoryCloseEvent.@NotNull Reason reason) {
    player.closeInventory(reason);
  }

  @Override
  @Deprecated
  public @NotNull ItemStack getItemInHand() {
    return player.getItemInHand();
  }

  @Override
  @Deprecated
  public void setItemInHand(@Nullable ItemStack item) {
    player.setItemInHand(item);
  }

  @Override
  public @NotNull ItemStack getItemOnCursor() {
    return player.getItemOnCursor();
  }

  @Override
  public void setItemOnCursor(@Nullable ItemStack item) {
    player.setItemOnCursor(item);
  }

  @Override
  public boolean hasCooldown(@NotNull Material material) {
    return player.hasCooldown(material);
  }

  @Override
  public int getCooldown(@NotNull Material material) {
    return player.getCooldown(material);
  }

  @Override
  public void setCooldown(@NotNull Material material, int ticks) {
    player.setCooldown(material, ticks);
  }

  @Override
  public void setHurtDirection(float hurtDirection) {
    player.setHurtDirection(hurtDirection);
  }

  @Override
  public boolean isDeeplySleeping() {
    return player.isDeeplySleeping();
  }

  @Override
  public int getSleepTicks() {
    return player.getSleepTicks();
  }

  @Override
  public @Nullable Location getPotentialBedLocation() {
    return player.getPotentialBedLocation();
  }

  @Override
  public @Nullable FishHook getFishHook() {
    return player.getFishHook();
  }

  @Override
  public boolean sleep(@NotNull Location location, boolean force) {
    return player.sleep(location, force);
  }

  @Override
  public void wakeup(boolean setSpawnLocation) {
    player.wakeup(setSpawnLocation);
  }

  @Override
  public @NotNull Location getBedLocation() {
    return player.getBedLocation();
  }

  @Override
  public @NotNull GameMode getGameMode() {
    return player.getGameMode();
  }

  @Override
  public void setGameMode(@NotNull GameMode mode) {
    player.setGameMode(mode);
  }

  @Override
  public boolean isBlocking() {
    return player.isBlocking();
  }

  @Override
  public boolean isHandRaised() {
    return player.isHandRaised();
  }

  @Override
  @Deprecated
  public @Nullable ItemStack getItemInUse() {
    return player.getItemInUse();
  }

  @Override
  public int getExpToLevel() {
    return player.getExpToLevel();
  }

  @Override
  public @Nullable Entity releaseLeftShoulderEntity() {
    return player.releaseLeftShoulderEntity();
  }

  @Override
  public @Nullable Entity releaseRightShoulderEntity() {
    return player.releaseRightShoulderEntity();
  }

  @Override
  public float getAttackCooldown() {
    return player.getAttackCooldown();
  }

  @Override
  public boolean discoverRecipe(@NotNull NamespacedKey recipe) {
    return player.discoverRecipe(recipe);
  }

  @Override
  public int discoverRecipes(@NotNull Collection<NamespacedKey> recipes) {
    return player.discoverRecipes(recipes);
  }

  @Override
  public boolean undiscoverRecipe(@NotNull NamespacedKey recipe) {
    return player.undiscoverRecipe(recipe);
  }

  @Override
  public int undiscoverRecipes(@NotNull Collection<NamespacedKey> recipes) {
    return player.undiscoverRecipes(recipes);
  }

  @Override
  public boolean hasDiscoveredRecipe(@NotNull NamespacedKey recipe) {
    return player.hasDiscoveredRecipe(recipe);
  }

  @Override
  public @NotNull Set<NamespacedKey> getDiscoveredRecipes() {
    return player.getDiscoveredRecipes();
  }

  @Override
  @Deprecated
  public @Nullable Entity getShoulderEntityLeft() {
    return player.getShoulderEntityLeft();
  }

  @Override
  @Deprecated
  public void setShoulderEntityLeft(@Nullable Entity entity) {
    player.setShoulderEntityLeft(entity);
  }

  @Override
  @Deprecated
  public @Nullable Entity getShoulderEntityRight() {
    return player.getShoulderEntityRight();
  }

  @Override
  @Deprecated
  public void setShoulderEntityRight(@Nullable Entity entity) {
    player.setShoulderEntityRight(entity);
  }

  @Override
  public boolean dropItem(boolean dropAll) {
    return player.dropItem(dropAll);
  }

  @Override
  public float getExhaustion() {
    return player.getExhaustion();
  }

  @Override
  public void setExhaustion(float value) {
    player.setExhaustion(value);
  }

  @Override
  public float getSaturation() {
    return player.getSaturation();
  }

  @Override
  public void setSaturation(float value) {
    player.setSaturation(value);
  }

  @Override
  public int getFoodLevel() {
    return player.getFoodLevel();
  }

  @Override
  public void setFoodLevel(int value) {
    player.setFoodLevel(value);
  }

  @Override
  public int getSaturatedRegenRate() {
    return player.getSaturatedRegenRate();
  }

  @Override
  public void setSaturatedRegenRate(int ticks) {
    player.setSaturatedRegenRate(ticks);
  }

  @Override
  public int getUnsaturatedRegenRate() {
    return player.getUnsaturatedRegenRate();
  }

  @Override
  public void setUnsaturatedRegenRate(int ticks) {
    player.setUnsaturatedRegenRate(ticks);
  }

  @Override
  public int getStarvationRate() {
    return player.getStarvationRate();
  }

  @Override
  public void setStarvationRate(int ticks) {
    player.setStarvationRate(ticks);
  }

  @Override
  public @Nullable Location getLastDeathLocation() {
    return player.getLastDeathLocation();
  }

  @Override
  public void setLastDeathLocation(@Nullable Location location) {
    player.setLastDeathLocation(location);
  }

  @Override
  public @Nullable Firework fireworkBoost(@NotNull ItemStack fireworkItemStack) {
    return player.fireworkBoost(fireworkItemStack);
  }

  @Override
  public double getEyeHeight() {
    return player.getEyeHeight();
  }

  @Override
  public double getEyeHeight(boolean ignorePose) {
    return player.getEyeHeight(ignorePose);
  }

  @Override
  public @NotNull Location getEyeLocation() {
    return player.getEyeLocation();
  }

  @Override
  public @NotNull List<Block> getLineOfSight(@Nullable Set<Material> transparent, int maxDistance) {
    return player.getLineOfSight(transparent, maxDistance);
  }

  @Override
  public @NotNull Block getTargetBlock(@Nullable Set<Material> transparent, int maxDistance) {
    return player.getTargetBlock(transparent, maxDistance);
  }

  @Override
  @Deprecated(forRemoval = true)
  public @Nullable Block getTargetBlock(int maxDistance) {
    return player.getTargetBlock(maxDistance);
  }

  @Override
  @Deprecated(forRemoval = true)
  public @Nullable Block getTargetBlock(int maxDistance, TargetBlockInfo.@NotNull FluidMode fluidMode) {
    return player.getTargetBlock(maxDistance, fluidMode);
  }

  @Override
  public @Nullable BlockFace getTargetBlockFace(int maxDistance) {
    return player.getTargetBlockFace(maxDistance);
  }

  @Override
  @Deprecated(forRemoval = true)
  public @Nullable BlockFace getTargetBlockFace(int maxDistance, TargetBlockInfo.@NotNull FluidMode fluidMode) {
    return player.getTargetBlockFace(maxDistance, fluidMode);
  }

  @Override
  public @Nullable BlockFace getTargetBlockFace(int maxDistance, @NotNull FluidCollisionMode fluidMode) {
    return player.getTargetBlockFace(maxDistance, fluidMode);
  }

  @Override
  @Deprecated(forRemoval = true)
  public @Nullable TargetBlockInfo getTargetBlockInfo(int maxDistance) {
    return player.getTargetBlockInfo(maxDistance);
  }

  @Override
  @Deprecated(forRemoval = true)
  public @Nullable TargetBlockInfo getTargetBlockInfo(int maxDistance, TargetBlockInfo.@NotNull FluidMode fluidMode) {
    return player.getTargetBlockInfo(maxDistance, fluidMode);
  }

  @Override
  public @Nullable Entity getTargetEntity(int maxDistance) {
    return player.getTargetEntity(maxDistance);
  }

  @Override
  public @Nullable Entity getTargetEntity(int maxDistance, boolean ignoreBlocks) {
    return player.getTargetEntity(maxDistance, ignoreBlocks);
  }

  @Override
  @Deprecated(forRemoval = true)
  public @Nullable TargetEntityInfo getTargetEntityInfo(int maxDistance) {
    return player.getTargetEntityInfo(maxDistance);
  }

  @Override
  public @Nullable RayTraceResult rayTraceEntities(int maxDistance) {
    return player.rayTraceEntities(maxDistance);
  }

  @Override
  @Deprecated(forRemoval = true)
  public @Nullable TargetEntityInfo getTargetEntityInfo(int maxDistance, boolean ignoreBlocks) {
    return player.getTargetEntityInfo(maxDistance, ignoreBlocks);
  }

  @Override
  public @Nullable RayTraceResult rayTraceEntities(int maxDistance, boolean ignoreBlocks) {
    return player.rayTraceEntities(maxDistance, ignoreBlocks);
  }

  @Override
  public @NotNull List<Block> getLastTwoTargetBlocks(@Nullable Set<Material> transparent, int maxDistance) {
    return player.getLastTwoTargetBlocks(transparent, maxDistance);
  }

  @Override
  public @Nullable Block getTargetBlockExact(int maxDistance) {
    return player.getTargetBlockExact(maxDistance);
  }

  @Override
  public @Nullable Block getTargetBlockExact(int maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
    return player.getTargetBlockExact(maxDistance, fluidCollisionMode);
  }

  @Override
  public @Nullable RayTraceResult rayTraceBlocks(double maxDistance) {
    return player.rayTraceBlocks(maxDistance);
  }

  @Override
  public @Nullable RayTraceResult rayTraceBlocks(double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
    return player.rayTraceBlocks(maxDistance, fluidCollisionMode);
  }

  @Override
  public int getRemainingAir() {
    return player.getRemainingAir();
  }

  @Override
  public void setRemainingAir(int ticks) {
    player.setRemainingAir(ticks);
  }

  @Override
  public int getMaximumAir() {
    return player.getMaximumAir();
  }

  @Override
  public void setMaximumAir(int ticks) {
    player.setMaximumAir(ticks);
  }

  @Override
  public int getArrowCooldown() {
    return player.getArrowCooldown();
  }

  @Override
  public void setArrowCooldown(int ticks) {
    player.setArrowCooldown(ticks);
  }

  @Override
  public int getArrowsInBody() {
    return player.getArrowsInBody();
  }

  @Override
  public void setArrowsInBody(int count) {
    player.setArrowsInBody(count);
  }

  @Override
  public void setArrowsInBody(int count, boolean fireEvent) {
    player.setArrowsInBody(count, fireEvent);
  }

  @Override
  public int getBeeStingerCooldown() {
    return player.getBeeStingerCooldown();
  }

  @Override
  public void setBeeStingerCooldown(int ticks) {
    player.setBeeStingerCooldown(ticks);
  }

  @Override
  public int getBeeStingersInBody() {
    return player.getBeeStingersInBody();
  }

  @Override
  public void setBeeStingersInBody(int count) {
    player.setBeeStingersInBody(count);
  }

  @Override
  public int getMaximumNoDamageTicks() {
    return player.getMaximumNoDamageTicks();
  }

  @Override
  public void setMaximumNoDamageTicks(int ticks) {
    player.setMaximumNoDamageTicks(ticks);
  }

  @Override
  public double getLastDamage() {
    return player.getLastDamage();
  }

  @Override
  public void setLastDamage(double damage) {
    player.setLastDamage(damage);
  }

  @Override
  public int getNoDamageTicks() {
    return player.getNoDamageTicks();
  }

  @Override
  public void setNoDamageTicks(int ticks) {
    player.setNoDamageTicks(ticks);
  }

  @Override
  public int getNoActionTicks() {
    return player.getNoActionTicks();
  }

  @Override
  public void setNoActionTicks(int ticks) {
    player.setNoActionTicks(ticks);
  }

  @Override
  @Nullable
  public Player getKiller() {
    return player.getKiller();
  }

  @Override
  public void setKiller(@Nullable Player killer) {
    player.setKiller(killer);
  }

  @Override
  public boolean addPotionEffect(@NotNull PotionEffect effect) {
    return player.addPotionEffect(effect);
  }

  @Override
  @Deprecated
  public boolean addPotionEffect(@NotNull PotionEffect effect, boolean force) {
    return player.addPotionEffect(effect, force);
  }

  @Override
  public boolean addPotionEffects(@NotNull Collection<PotionEffect> effects) {
    return player.addPotionEffects(effects);
  }

  @Override
  public boolean hasPotionEffect(@NotNull PotionEffectType type) {
    return player.hasPotionEffect(type);
  }

  @Override
  public @Nullable PotionEffect getPotionEffect(@NotNull PotionEffectType type) {
    return player.getPotionEffect(type);
  }

  @Override
  public void removePotionEffect(@NotNull PotionEffectType type) {
    player.removePotionEffect(type);
  }

  @Override
  public @NotNull Collection<PotionEffect> getActivePotionEffects() {
    return player.getActivePotionEffects();
  }

  @Override
  public boolean clearActivePotionEffects() {
    return player.clearActivePotionEffects();
  }

  @Override
  public boolean hasLineOfSight(@NotNull Entity other) {
    return player.hasLineOfSight(other);
  }

  @Override
  public boolean hasLineOfSight(@NotNull Location location) {
    return player.hasLineOfSight(location);
  }

  @Override
  public boolean getRemoveWhenFarAway() {
    return player.getRemoveWhenFarAway();
  }

  @Override
  public void setRemoveWhenFarAway(boolean remove) {
    player.setRemoveWhenFarAway(remove);
  }

  @Override
  public void setCanPickupItems(boolean pickup) {
    player.setCanPickupItems(pickup);
  }

  @Override
  public boolean getCanPickupItems() {
    return player.getCanPickupItems();
  }

  @Override
  public boolean isLeashed() {
    return player.isLeashed();
  }

  @Override
  public @NotNull Entity getLeashHolder() throws IllegalStateException {
    return player.getLeashHolder();
  }

  @Override
  public boolean setLeashHolder(@Nullable Entity holder) {
    return player.setLeashHolder(holder);
  }

  @Override
  public boolean isGliding() {
    return player.isGliding();
  }

  @Override
  public void setGliding(boolean gliding) {
    player.setGliding(gliding);
  }

  @Override
  public boolean isSwimming() {
    return player.isSwimming();
  }

  @Override
  @Deprecated
  public void setSwimming(boolean swimming) {
    player.setSwimming(swimming);
  }

  @Override
  public boolean isRiptiding() {
    return player.isRiptiding();
  }

  @Override
  public boolean isSleeping() {
    return player.isSleeping();
  }

  @Override
  public boolean isClimbing() {
    return player.isClimbing();
  }

  @Override
  public void setAI(boolean ai) {
    player.setAI(ai);
  }

  @Override
  public boolean hasAI() {
    return player.hasAI();
  }

  @Override
  public void attack(@NotNull Entity target) {
    player.attack(target);
  }

  @Override
  public void swingMainHand() {
    player.swingMainHand();
  }

  @Override
  public void swingOffHand() {
    player.swingOffHand();
  }

  @Override
  public void playHurtAnimation(float yaw) {
    player.playHurtAnimation(yaw);
  }

  @Override
  public void setCollidable(boolean collidable) {
    player.setCollidable(collidable);
  }

  @Override
  public boolean isCollidable() {
    return player.isCollidable();
  }

  @Override
  public @NotNull Set<UUID> getCollidableExemptions() {
    return player.getCollidableExemptions();
  }

  @Override
  public <T> @Nullable T getMemory(@NotNull MemoryKey<T> memoryKey) {
    return player.getMemory(memoryKey);
  }

  @Override
  public <T> void setMemory(@NotNull MemoryKey<T> memoryKey, @Nullable T memoryValue) {
    player.setMemory(memoryKey, memoryValue);
  }

  @Override
  public @Nullable Sound getHurtSound() {
    return player.getHurtSound();
  }

  @Override
  public @Nullable Sound getDeathSound() {
    return player.getDeathSound();
  }

  @Override
  public @NotNull Sound getFallDamageSound(int fallHeight) {
    return player.getFallDamageSound(fallHeight);
  }

  @Override
  public @NotNull Sound getFallDamageSoundSmall() {
    return player.getFallDamageSoundSmall();
  }

  @Override
  public @NotNull Sound getFallDamageSoundBig() {
    return player.getFallDamageSoundBig();
  }

  @Override
  public @NotNull Sound getDrinkingSound(@NotNull ItemStack itemStack) {
    return player.getDrinkingSound(itemStack);
  }

  @Override
  public @NotNull Sound getEatingSound(@NotNull ItemStack itemStack) {
    return player.getEatingSound(itemStack);
  }

  @Override
  public boolean canBreatheUnderwater() {
    return player.canBreatheUnderwater();
  }

  @Override
  public @NotNull EntityCategory getCategory() {
    return player.getCategory();
  }

  @Override
  public void setInvisible(boolean invisible) {
    player.setInvisible(invisible);
  }

  @Override
  public boolean isInvisible() {
    return player.isInvisible();
  }

  @Override
  @Deprecated
  public int getArrowsStuck() {
    return player.getArrowsStuck();
  }

  @Override
  @Deprecated
  public void setArrowsStuck(int arrows) {
    player.setArrowsStuck(arrows);
  }

  @Override
  public int getShieldBlockingDelay() {
    return player.getShieldBlockingDelay();
  }

  @Override
  public void setShieldBlockingDelay(int delay) {
    player.setShieldBlockingDelay(delay);
  }

  @Override
  public @NotNull ItemStack getActiveItem() {
    return player.getActiveItem();
  }

  @Override
  public void clearActiveItem() {
    player.clearActiveItem();
  }

  @Override
  public int getItemUseRemainingTime() {
    return player.getItemUseRemainingTime();
  }

  @Override
  public int getHandRaisedTime() {
    return player.getHandRaisedTime();
  }

  @Override
  public @NotNull EquipmentSlot getHandRaised() {
    return player.getHandRaised();
  }

  @Override
  public boolean isJumping() {
    return player.isJumping();
  }

  @Override
  public void setJumping(boolean jumping) {
    player.setJumping(jumping);
  }

  @Override
  public void playPickupItemAnimation(@NotNull Item item) {
    player.playPickupItemAnimation(item);
  }

  @Override
  public void playPickupItemAnimation(@NotNull Item item, int quantity) {
    player.playPickupItemAnimation(item, quantity);
  }

  @Override
  public float getHurtDirection() {
    return player.getHurtDirection();
  }

  @Override
  public void swingHand(@NotNull EquipmentSlot hand) {
    player.swingHand(hand);
  }

  @Override
  public void knockback(double strength, double directionX, double directionZ) {
    player.knockback(strength, directionX, directionZ);
  }

  @Override
  public void broadcastSlotBreak(@NotNull EquipmentSlot slot) {
    player.broadcastSlotBreak(slot);
  }

  @Override
  public void broadcastSlotBreak(@NotNull EquipmentSlot slot, @NotNull Collection<Player> players) {
    player.broadcastSlotBreak(slot, players);
  }

  @Override
  public @NotNull ItemStack damageItemStack(@NotNull ItemStack stack, int amount) {
    return player.damageItemStack(stack, amount);
  }

  @Override
  public void damageItemStack(@NotNull EquipmentSlot slot, int amount) {
    player.damageItemStack(slot, amount);
  }

  @Override
  public float getBodyYaw() {
    return player.getBodyYaw();
  }

  @Override
  public void setBodyYaw(float bodyYaw) {
    player.setBodyYaw(bodyYaw);
  }

  @Override
  public @Nullable AttributeInstance getAttribute(@NotNull Attribute attribute) {
    return player.getAttribute(attribute);
  }

  @Override
  public void registerAttribute(@NotNull Attribute attribute) {
    player.registerAttribute(attribute);
  }

  @Override
  public void damage(double amount) {
    player.damage(amount);
  }

  @Override
  public void damage(double amount, @Nullable Entity source) {
    player.damage(amount, source);
  }

  @Override
  public double getHealth() {
    return player.getHealth();
  }

  @Override
  public void setHealth(double health) {
    player.setHealth(health);
  }

  @Override
  public double getAbsorptionAmount() {
    return player.getAbsorptionAmount();
  }

  @Override
  public void setAbsorptionAmount(double amount) {
    player.setAbsorptionAmount(amount);
  }

  @Override
  @Deprecated
  public double getMaxHealth() {
    return player.getMaxHealth();
  }

  @Override
  @Deprecated
  public void setMaxHealth(double health) {
    player.setMaxHealth(health);
  }

  @Override
  @Deprecated
  public void resetMaxHealth() {
    player.resetMaxHealth();
  }

  @Override
  public @NotNull Location getLocation() {
    return player.getLocation();
  }

  @Override
  @Contract("null -> null; !null -> !null")
  public @Nullable Location getLocation(@Nullable Location loc) {
    return player.getLocation(loc);
  }

  @Override
  public void setVelocity(@NotNull Vector velocity) {
    player.setVelocity(velocity);
  }

  @Override
  public @NotNull Vector getVelocity() {
    return player.getVelocity();
  }

  @Override
  public double getHeight() {
    return player.getHeight();
  }

  @Override
  public double getWidth() {
    return player.getWidth();
  }

  @Override
  public @NotNull BoundingBox getBoundingBox() {
    return player.getBoundingBox();
  }

  @Override
  public boolean isInWater() {
    return player.isInWater();
  }

  @Override
  public @NotNull World getWorld() {
    return player.getWorld();
  }

  @Override
  @ApiStatus.Experimental
  public boolean teleport(@NotNull Location location, @NotNull TeleportFlag @NotNull ... teleportFlags) {
    return player.teleport(location, teleportFlags);
  }

  @Override
  @ApiStatus.Experimental
  public boolean teleport(@NotNull Location location, PlayerTeleportEvent.@NotNull TeleportCause cause, @NotNull TeleportFlag @NotNull ... teleportFlags) {
    return player.teleport(location, cause, teleportFlags);
  }

  @Override
  public boolean teleport(@NotNull Location location) {
    return player.teleport(location);
  }

  @Override
  public boolean teleport(@NotNull Location location, PlayerTeleportEvent.@NotNull TeleportCause cause) {
    return player.teleport(location, cause);
  }

  @Override
  public boolean teleport(@NotNull Entity destination) {
    return player.teleport(destination);
  }

  @Override
  public boolean teleport(@NotNull Entity destination, PlayerTeleportEvent.@NotNull TeleportCause cause) {
    return player.teleport(destination, cause);
  }

  @Override
  public @NotNull CompletableFuture<Boolean> teleportAsync(@NotNull Location loc) {
    return player.teleportAsync(loc);
  }

  @Override
  public @NotNull CompletableFuture<Boolean> teleportAsync(@NotNull Location loc, PlayerTeleportEvent.@NotNull TeleportCause cause) {
    return player.teleportAsync(loc, cause);
  }

  @Override
  public @NotNull List<Entity> getNearbyEntities(double x, double y, double z) {
    return player.getNearbyEntities(x, y, z);
  }

  @Override
  public int getEntityId() {
    return player.getEntityId();
  }

  @Override
  public int getFireTicks() {
    return player.getFireTicks();
  }

  @Override
  public int getMaxFireTicks() {
    return player.getMaxFireTicks();
  }

  @Override
  public void setFireTicks(int ticks) {
    player.setFireTicks(ticks);
  }

  @Override
  public void setVisualFire(boolean fire) {
    player.setVisualFire(fire);
  }

  @Override
  public boolean isVisualFire() {
    return player.isVisualFire();
  }

  @Override
  public int getFreezeTicks() {
    return player.getFreezeTicks();
  }

  @Override
  public int getMaxFreezeTicks() {
    return player.getMaxFreezeTicks();
  }

  @Override
  public void setFreezeTicks(int ticks) {
    player.setFreezeTicks(ticks);
  }

  @Override
  public boolean isFrozen() {
    return player.isFrozen();
  }

  @Override
  public boolean isFreezeTickingLocked() {
    return player.isFreezeTickingLocked();
  }

  @Override
  public void lockFreezeTicks(boolean locked) {
    player.lockFreezeTicks(locked);
  }

  @Override
  public void remove() {
    player.remove();
  }

  @Override
  public boolean isDead() {
    return player.isDead();
  }

  @Override
  public boolean isValid() {
    return player.isValid();
  }

  @Override
  public @NotNull Server getServer() {
    return player.getServer();
  }

  @Override
  public boolean isPersistent() {
    return player.isPersistent();
  }

  @Override
  public void setPersistent(boolean persistent) {
    player.setPersistent(persistent);
  }

  @Override
  @Deprecated
  public @Nullable Entity getPassenger() {
    return player.getPassenger();
  }

  @Override
  @Deprecated
  public boolean setPassenger(@NotNull Entity passenger) {
    return player.setPassenger(passenger);
  }

  @Override
  public @NotNull List<Entity> getPassengers() {
    return player.getPassengers();
  }

  @Override
  public boolean addPassenger(@NotNull Entity passenger) {
    return player.addPassenger(passenger);
  }

  @Override
  public boolean removePassenger(@NotNull Entity passenger) {
    return player.removePassenger(passenger);
  }

  @Override
  public boolean isEmpty() {
    return player.isEmpty();
  }

  @Override
  public boolean eject() {
    return player.eject();
  }

  @Override
  public float getFallDistance() {
    return player.getFallDistance();
  }

  @Override
  public void setFallDistance(float distance) {
    player.setFallDistance(distance);
  }

  @Override
  public void setLastDamageCause(@Nullable EntityDamageEvent event) {
    player.setLastDamageCause(event);
  }

  @Override
  public @Nullable EntityDamageEvent getLastDamageCause() {
    return player.getLastDamageCause();
  }

  @Override
  public @NotNull UUID getUniqueId() {
    return player.getUniqueId();
  }

  @Override
  public int getTicksLived() {
    return player.getTicksLived();
  }

  @Override
  public void setTicksLived(int value) {
    player.setTicksLived(value);
  }

  @Override
  public void playEffect(@NotNull EntityEffect type) {
    player.playEffect(type);
  }

  @Override
  public @NotNull EntityType getType() {
    return player.getType();
  }

  @Override
  public @NotNull Sound getSwimSound() {
    return player.getSwimSound();
  }

  @Override
  public @NotNull Sound getSwimSplashSound() {
    return player.getSwimSplashSound();
  }

  @Override
  public @NotNull Sound getSwimHighSpeedSplashSound() {
    return player.getSwimHighSpeedSplashSound();
  }

  @Override
  public boolean isInsideVehicle() {
    return player.isInsideVehicle();
  }

  @Override
  public boolean leaveVehicle() {
    return player.leaveVehicle();
  }

  @Override
  public @Nullable Entity getVehicle() {
    return player.getVehicle();
  }

  @Override
  public void setCustomNameVisible(boolean flag) {
    player.setCustomNameVisible(flag);
  }

  @Override
  public boolean isCustomNameVisible() {
    return player.isCustomNameVisible();
  }

  @Override
  @ApiStatus.Experimental
  public void setVisibleByDefault(boolean visible) {
    player.setVisibleByDefault(visible);
  }

  @Override
  @ApiStatus.Experimental
  public boolean isVisibleByDefault() {
    return player.isVisibleByDefault();
  }

  @Override
  public void setGlowing(boolean flag) {
    player.setGlowing(flag);
  }

  @Override
  public boolean isGlowing() {
    return player.isGlowing();
  }

  @Override
  public void setInvulnerable(boolean flag) {
    player.setInvulnerable(flag);
  }

  @Override
  public boolean isInvulnerable() {
    return player.isInvulnerable();
  }

  @Override
  public boolean isSilent() {
    return player.isSilent();
  }

  @Override
  public void setSilent(boolean flag) {
    player.setSilent(flag);
  }

  @Override
  public boolean hasGravity() {
    return player.hasGravity();
  }

  @Override
  public void setGravity(boolean gravity) {
    player.setGravity(gravity);
  }

  @Override
  public int getPortalCooldown() {
    return player.getPortalCooldown();
  }

  @Override
  public void setPortalCooldown(int cooldown) {
    player.setPortalCooldown(cooldown);
  }

  @Override
  public @NotNull Set<String> getScoreboardTags() {
    return player.getScoreboardTags();
  }

  @Override
  public boolean addScoreboardTag(@NotNull String tag) {
    return player.addScoreboardTag(tag);
  }

  @Override
  public boolean removeScoreboardTag(@NotNull String tag) {
    return player.removeScoreboardTag(tag);
  }

  @Override
  public @NotNull PistonMoveReaction getPistonMoveReaction() {
    return player.getPistonMoveReaction();
  }

  @Override
  public @NotNull BlockFace getFacing() {
    return player.getFacing();
  }

  @Override
  public @NotNull Pose getPose() {
    return player.getPose();
  }

  @Override
  public @NotNull SpawnCategory getSpawnCategory() {
    return player.getSpawnCategory();
  }

  @Override
  public @NotNull Component teamDisplayName() {
    return player.teamDisplayName();
  }

  @Override
  public @Nullable Location getOrigin() {
    return player.getOrigin();
  }

  @Override
  public boolean fromMobSpawner() {
    return player.fromMobSpawner();
  }

  @Override
  public @NotNull Chunk getChunk() {
    return player.getChunk();
  }

  @Override
  public CreatureSpawnEvent.@NotNull SpawnReason getEntitySpawnReason() {
    return player.getEntitySpawnReason();
  }

  @Override
  public boolean isUnderWater() {
    return player.isUnderWater();
  }

  @Override
  public boolean isInRain() {
    return player.isInRain();
  }

  @Override
  public boolean isInBubbleColumn() {
    return player.isInBubbleColumn();
  }

  @Override
  public boolean isInWaterOrRain() {
    return player.isInWaterOrRain();
  }

  @Override
  public boolean isInWaterOrBubbleColumn() {
    return player.isInWaterOrBubbleColumn();
  }

  @Override
  public boolean isInWaterOrRainOrBubbleColumn() {
    return player.isInWaterOrRainOrBubbleColumn();
  }

  @Override
  public boolean isInLava() {
    return player.isInLava();
  }

  @Override
  public boolean isTicking() {
    return player.isTicking();
  }

  @Override
  public @NotNull Set<Player> getTrackedPlayers() {
    return player.getTrackedPlayers();
  }

  @Override
  public boolean spawnAt(@NotNull Location location) {
    return player.spawnAt(location);
  }

  @Override
  public boolean spawnAt(@NotNull Location location, CreatureSpawnEvent.@NotNull SpawnReason reason) {
    return player.spawnAt(location, reason);
  }

  @Override
  public boolean isInPowderedSnow() {
    return player.isInPowderedSnow();
  }

  @Override
  public boolean collidesAt(@NotNull Location location) {
    return player.collidesAt(location);
  }

  @Override
  public boolean wouldCollideUsing(@NotNull BoundingBox boundingBox) {
    return player.wouldCollideUsing(boundingBox);
  }

  @Override
  public @NotNull EntityScheduler getScheduler() {
    return player.getScheduler();
  }

  @Override
  public void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue) {
    player.setMetadata(metadataKey, newMetadataValue);
  }

  @Override
  public @NotNull List<MetadataValue> getMetadata(@NotNull String metadataKey) {
    return player.getMetadata(metadataKey);
  }

  @Override
  public boolean hasMetadata(@NotNull String metadataKey) {
    return player.hasMetadata(metadataKey);
  }

  @Override
  public void removeMetadata(@NotNull String metadataKey, @NotNull Plugin owningPlugin) {
    player.removeMetadata(metadataKey, owningPlugin);
  }

  @Override
  @ApiStatus.Obsolete
  public void sendMessage(@NotNull String message) {
    player.sendMessage(message);
  }

  @Override
  @ApiStatus.Obsolete
  public void sendMessage(@NotNull String... messages) {
    player.sendMessage(messages);
  }

  @Override
  @Deprecated
  public void sendMessage(@Nullable UUID sender, @NotNull String message) {
    player.sendMessage(sender, message);
  }

  @Override
  @Deprecated
  public void sendMessage(@Nullable UUID sender, @NotNull String... messages) {
    player.sendMessage(sender, messages);
  }

  @Override
  public @NotNull Component name() {
    return player.name();
  }

  @Override
  public void sendMessage(@NotNull Identity identity, @NotNull Component message, @NotNull MessageType type) {
    player.sendMessage(identity, message, type);
  }

  @Override
  public void sendRichMessage(@NotNull String message) {
    player.sendRichMessage(message);
  }

  @Override
  public void sendPlainMessage(@NotNull String message) {
    player.sendPlainMessage(message);
  }

  public static @NotNull Audience empty() {
    return Audience.empty();
  }

  public static @NotNull Audience audience(@NotNull Audience @NotNull ... audiences) {
    return Audience.audience(audiences);
  }

  public static @NotNull ForwardingAudience audience(@NotNull Iterable<? extends Audience> audiences) {
    return Audience.audience(audiences);
  }

  public static @NotNull Collector<? super Audience, ?, ForwardingAudience> toAudience() {
    return Audience.toAudience();
  }

  @Override
  public @NotNull Audience filterAudience(@NotNull Predicate<? super Audience> filter) {
    return player.filterAudience(filter);
  }

  @Override
  public void forEachAudience(@NotNull Consumer<? super Audience> action) {
    player.forEachAudience(action);
  }

  @Override

  public void sendMessage(@NotNull ComponentLike message) {
    player.sendMessage(message);
  }

  @Override
  public void sendMessage(@NotNull Component message) {
    player.sendMessage(message);
  }

  @Override
  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")

  @Deprecated
  public void sendMessage(@NotNull ComponentLike message, @NotNull MessageType type) {
    player.sendMessage(message, type);
  }

  @Override
  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")

  @Deprecated
  public void sendMessage(@NotNull Component message, @NotNull MessageType type) {
    player.sendMessage(message, type);
  }

  @Override

  @Deprecated
  public void sendMessage(@NotNull Identified source, @NotNull ComponentLike message) {
    player.sendMessage(source, message);
  }

  @Override

  @Deprecated
  public void sendMessage(@NotNull Identity source, @NotNull ComponentLike message) {
    player.sendMessage(source, message);
  }

  @Override

  @Deprecated
  public void sendMessage(@NotNull Identified source, @NotNull Component message) {
    player.sendMessage(source, message);
  }

  @Override

  @Deprecated
  public void sendMessage(@NotNull Identity source, @NotNull Component message) {
    player.sendMessage(source, message);
  }

  @Override
  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")

  @Deprecated
  public void sendMessage(@NotNull Identified source, @NotNull ComponentLike message, @NotNull MessageType type) {
    player.sendMessage(source, message, type);
  }

  @Override
  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")

  @Deprecated
  public void sendMessage(@NotNull Identity source, @NotNull ComponentLike message, @NotNull MessageType type) {
    player.sendMessage(source, message, type);
  }

  @Override
  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
  @Deprecated
  public void sendMessage(@NotNull Identified source, @NotNull Component message, @NotNull MessageType type) {
    player.sendMessage(source, message, type);
  }

  @Override
  public void sendMessage(@NotNull Component message, ChatType.@NotNull Bound boundChatType) {
    player.sendMessage(message, boundChatType);
  }

  @Override

  public void sendMessage(@NotNull ComponentLike message, ChatType.@NotNull Bound boundChatType) {
    player.sendMessage(message, boundChatType);
  }

  @Override
  public void sendMessage(@NotNull SignedMessage signedMessage, ChatType.@NotNull Bound boundChatType) {
    player.sendMessage(signedMessage, boundChatType);
  }

  @Override

  public void deleteMessage(@NotNull SignedMessage signedMessage) {
    player.deleteMessage(signedMessage);
  }

  @Override
  public void deleteMessage(SignedMessage.@NotNull Signature signature) {
    player.deleteMessage(signature);
  }

  @Override

  public void sendActionBar(@NotNull ComponentLike message) {
    player.sendActionBar(message);
  }

  @Override
  public void sendActionBar(@NotNull Component message) {
    player.sendActionBar(message);
  }

  @Override

  public void sendPlayerListHeader(@NotNull ComponentLike header) {
    player.sendPlayerListHeader(header);
  }

  @Override
  public void sendPlayerListHeader(@NotNull Component header) {
    player.sendPlayerListHeader(header);
  }

  @Override

  public void sendPlayerListFooter(@NotNull ComponentLike footer) {
    player.sendPlayerListFooter(footer);
  }

  @Override
  public void sendPlayerListFooter(@NotNull Component footer) {
    player.sendPlayerListFooter(footer);
  }

  @Override

  public void sendPlayerListHeaderAndFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
    player.sendPlayerListHeaderAndFooter(header, footer);
  }

  @Override
  public void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
    player.sendPlayerListHeaderAndFooter(header, footer);
  }

  @Override

  public void showTitle(net.kyori.adventure.title.@NotNull Title title) {
    player.showTitle(title);
  }

  @Override
  public <T> void sendTitlePart(@NotNull TitlePart<T> part, @NotNull T value) {
    player.sendTitlePart(part, value);
  }

  @Override
  public void clearTitle() {
    player.clearTitle();
  }

  @Override
  public void showBossBar(@NotNull BossBar bar) {
    player.showBossBar(bar);
  }

  @Override
  public void hideBossBar(@NotNull BossBar bar) {
    player.hideBossBar(bar);
  }

  @Override
  public void playSound(net.kyori.adventure.sound.@NotNull Sound sound) {
    player.playSound(sound);
  }

  @Override
  public void playSound(net.kyori.adventure.sound.@NotNull Sound sound, double x, double y, double z) {
    player.playSound(sound, x, y, z);
  }

  @Override
  public void playSound(net.kyori.adventure.sound.@NotNull Sound sound, net.kyori.adventure.sound.Sound.@NotNull Emitter emitter) {
    player.playSound(sound, emitter);
  }

  @Override

  public void stopSound(net.kyori.adventure.sound.@NotNull Sound sound) {
    player.stopSound(sound);
  }

  @Override
  public void stopSound(@NotNull SoundStop stop) {
    player.stopSound(stop);
  }

  @Override

  public void openBook(Book.@NotNull Builder book) {
    player.openBook(book);
  }

  @Override
  public void openBook(@NotNull Book book) {
    player.openBook(book);
  }

  @Override
  public @NotNull <T> Optional<T> get(@NotNull Pointer<T> pointer) {
    return player.get(pointer);
  }

  @Override
  @Contract("_, null -> _; _, !null -> !null")
  public <T> @Nullable T getOrDefault(@NotNull Pointer<T> pointer, @Nullable T defaultValue) {
    return player.getOrDefault(pointer, defaultValue);
  }

  @Override
  public <T> @UnknownNullability T getOrDefaultFrom(@NotNull Pointer<T> pointer, @NotNull Supplier<? extends T> defaultValue) {
    return player.getOrDefaultFrom(pointer, defaultValue);
  }

  @Override
  public @NotNull Pointers pointers() {
    return player.pointers();
  }

  @Override
  public boolean isPermissionSet(@NotNull String name) {
    return player.isPermissionSet(name);
  }

  @Override
  public boolean isPermissionSet(@NotNull Permission perm) {
    return player.isPermissionSet(perm);
  }

  @Override
  public boolean hasPermission(@NotNull String name) {
    return player.hasPermission(name);
  }

  @Override
  public boolean hasPermission(@NotNull Permission perm) {
    return player.hasPermission(perm);
  }

  @Override
  public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value) {
    return player.addAttachment(plugin, name, value);
  }

  @Override
  public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin) {
    return player.addAttachment(plugin);
  }

  @Override
  public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value, int ticks) {
    return player.addAttachment(plugin, name, value, ticks);
  }

  @Override
  public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, int ticks) {
    return player.addAttachment(plugin, ticks);
  }

  @Override
  public void removeAttachment(@NotNull PermissionAttachment attachment) {
    player.removeAttachment(attachment);
  }

  @Override
  public void recalculatePermissions() {
    player.recalculatePermissions();
  }

  @Override
  public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
    return player.getEffectivePermissions();
  }

  @Override
  public @NotNull TriState permissionValue(@NotNull Permission permission) {
    return player.permissionValue(permission);
  }

  @Override
  public @NotNull TriState permissionValue(@NotNull String permission) {
    return player.permissionValue(permission);
  }

  @Override
  public boolean isOp() {
    return player.isOp();
  }

  @Override
  public void setOp(boolean value) {
    player.setOp(value);
  }

  @Override
  public @Nullable Component customName() {
    return player.customName();
  }

  @Override
  public void customName(@Nullable Component customName) {
    player.customName(customName);
  }

  @Override
  @Deprecated
  @Nullable
  public String getCustomName() {
    return player.getCustomName();
  }

  @Override
  @Deprecated
  public void setCustomName(@Nullable String name) {
    player.setCustomName(name);
  }

  @Override
  public @NotNull PersistentDataContainer getPersistentDataContainer() {
    return player.getPersistentDataContainer();
  }

  public static @Nullable <V> HoverEvent<V> unbox(@Nullable HoverEventSource<V> source) {
    return HoverEventSource.unbox(source);
  }

  @Override
  public @NotNull HoverEvent<HoverEvent.ShowEntity> asHoverEvent() {
    return player.asHoverEvent();
  }

  public static net.kyori.adventure.sound.Sound.@NotNull Emitter self() {
    return net.kyori.adventure.sound.Sound.Emitter.self();
  }

  @Override
  public <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> projectile) {
    return player.launchProjectile(projectile);
  }

  @Override
  public <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> projectile, @Nullable Vector velocity) {
    return player.launchProjectile(projectile, velocity);
  }

  @Override
  public <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> projectile, @Nullable Vector velocity, org.bukkit.util.@Nullable Consumer<T> function) {
    return player.launchProjectile(projectile, velocity, function);
  }

  @Override
  public @NotNull TriState getFrictionState() {
    return player.getFrictionState();
  }

  @Override
  public void setFrictionState(@NotNull TriState state) {
    player.setFrictionState(state);
  }

  @Override
  public boolean isConversing() {
    return player.isConversing();
  }

  @Override
  public void acceptConversationInput(@NotNull String input) {
    player.acceptConversationInput(input);
  }

  @Override
  public boolean beginConversation(@NotNull Conversation conversation) {
    return player.beginConversation(conversation);
  }

  @Override
  public void abandonConversation(@NotNull Conversation conversation) {
    player.abandonConversation(conversation);
  }

  @Override
  public void abandonConversation(@NotNull Conversation conversation, @NotNull ConversationAbandonedEvent details) {
    player.abandonConversation(conversation, details);
  }

  @Override
  @Deprecated
  public void sendRawMessage(@Nullable UUID sender, @NotNull String message) {
    player.sendRawMessage(sender, message);
  }

  @Override
  public boolean isOnline() {
    return player.isOnline();
  }

  @Override
  public boolean isBanned() {
    return player.isBanned();
  }

  @Override
  public @NotNull BanEntry banPlayer(@Nullable String reason) {
    return player.banPlayer(reason);
  }

  @Override
  public @NotNull BanEntry banPlayer(@Nullable String reason, @Nullable String source) {
    return player.banPlayer(reason, source);
  }

  @Override
  public @NotNull BanEntry banPlayer(@Nullable String reason, @Nullable Date expires) {
    return player.banPlayer(reason, expires);
  }

  @Override
  public @NotNull BanEntry banPlayer(@Nullable String reason, @Nullable Date expires, @Nullable String source) {
    return player.banPlayer(reason, expires, source);
  }

  @Override
  public @NotNull BanEntry banPlayer(@Nullable String reason, @Nullable Date expires, @Nullable String source, boolean kickIfOnline) {
    return player.banPlayer(reason, expires, source, kickIfOnline);
  }

  @Override
  public @Nullable BanEntry<PlayerProfile> ban(@Nullable String reason, @Nullable Date expires, @Nullable String source) {
    return player.ban(reason, expires, source);
  }

  @Override
  public boolean isWhitelisted() {
    return player.isWhitelisted();
  }

  @Override
  public void setWhitelisted(boolean value) {
    player.setWhitelisted(value);
  }

  @Override
  @Nullable
  public Player getPlayer() {
    return player.getPlayer();
  }

  @Override
  public long getFirstPlayed() {
    return player.getFirstPlayed();
  }

  @Override
  @Deprecated
  public long getLastPlayed() {
    return player.getLastPlayed();
  }

  @Override
  public boolean hasPlayedBefore() {
    return player.hasPlayedBefore();
  }

  @Override
  public long getLastLogin() {
    return player.getLastLogin();
  }

  @Override
  public long getLastSeen() {
    return player.getLastSeen();
  }

  @Override
  public void incrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
    player.incrementStatistic(statistic);
  }

  @Override
  public void decrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
    player.decrementStatistic(statistic);
  }

  @Override
  public void incrementStatistic(@NotNull Statistic statistic, int amount) throws IllegalArgumentException {
    player.incrementStatistic(statistic, amount);
  }

  @Override
  public void decrementStatistic(@NotNull Statistic statistic, int amount) throws IllegalArgumentException {
    player.decrementStatistic(statistic, amount);
  }

  @Override
  public void setStatistic(@NotNull Statistic statistic, int newValue) throws IllegalArgumentException {
    player.setStatistic(statistic, newValue);
  }

  @Override
  public int getStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
    return player.getStatistic(statistic);
  }

  @Override
  public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
    player.incrementStatistic(statistic, material);
  }

  @Override
  public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
    player.decrementStatistic(statistic, material);
  }

  @Override
  public int getStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
    return player.getStatistic(statistic, material);
  }

  @Override
  public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int amount) throws IllegalArgumentException {
    player.incrementStatistic(statistic, material, amount);
  }

  @Override
  public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int amount) throws IllegalArgumentException {
    player.decrementStatistic(statistic, material, amount);
  }

  @Override
  public void setStatistic(@NotNull Statistic statistic, @NotNull Material material, int newValue) throws IllegalArgumentException {
    player.setStatistic(statistic, material, newValue);
  }

  @Override
  public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
    player.incrementStatistic(statistic, entityType);
  }

  @Override
  public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
    player.decrementStatistic(statistic, entityType);
  }

  @Override
  public int getStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
    return player.getStatistic(statistic, entityType);
  }

  @Override
  public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int amount) throws IllegalArgumentException {
    player.incrementStatistic(statistic, entityType, amount);
  }

  @Override
  public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int amount) {
    player.decrementStatistic(statistic, entityType, amount);
  }

  @Override
  public void setStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int newValue) {
    player.setStatistic(statistic, entityType, newValue);
  }

  @Override
  public @NotNull Map<String, Object> serialize() {
    return player.serialize();
  }

  @Override
  public void sendPluginMessage(@NotNull Plugin source, @NotNull String channel, @NotNull byte[] message) {
    player.sendPluginMessage(source, channel, message);
  }

  @Override
  public @NotNull Set<String> getListeningPluginChannels() {
    return player.getListeningPluginChannels();
  }

  @Override
  public int getProtocolVersion() {
    return player.getProtocolVersion();
  }

  @Override
  public @Nullable InetSocketAddress getVirtualHost() {
    return player.getVirtualHost();
  }
}
