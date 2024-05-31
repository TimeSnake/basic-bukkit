/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user;


import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.Title;
import com.destroystokyo.paper.block.TargetBlockInfo;
import com.destroystokyo.paper.entity.TargetEntityInfo;
import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.entity.LookAnchor;
import io.papermc.paper.entity.TeleportFlag;
import io.papermc.paper.math.Position;
import io.papermc.paper.threadedregions.scheduler.EntityScheduler;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.pointer.Pointer;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.resource.ResourcePackInfoLike;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.resource.ResourcePackRequestLike;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
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
import org.bukkit.damage.DamageSource;
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
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class UserPlayerDelegation {

  protected final Player player;

  public UserPlayerDelegation(Player player) {
    this.player = player;
  }


  @NotNull
  public Identity identity() {
    return player.identity();
  }

  public boolean hasMetadata(@NotNull String s) {
    return player.hasMetadata(s);
  }

  public void setResourcePack(@NotNull String url, @NotNull String hash) {
    player.setResourcePack(url, hash);
  }

  public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3,
                                double v4, double v5, double v6, @Nullable T t) {
    player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6, t);
  }

  public boolean hasPermission(@NotNull String s) {
    return player.hasPermission(s);
  }

  @NotNull
  public PersistentDataContainer getPersistentDataContainer() {
    return player.getPersistentDataContainer();
  }

  @Deprecated
  public void setResourcePack(@NotNull String s) {
    player.setResourcePack(s);
  }

  public boolean isGlowing() {
    return player.isGlowing();
  }

  public boolean spawnAt(@NotNull Location location) {
    return player.spawnAt(location);
  }

  @NotNull
  public EntityScheduler getScheduler() {
    return player.getScheduler();
  }

  public double getEyeHeight(boolean b) {
    return player.getEyeHeight(b);
  }

  public boolean setWindowProperty(@NotNull InventoryView.Property property, int i) {
    return player.setWindowProperty(property, i);
  }

  @NotNull
  public GameMode getGameMode() {
    return player.getGameMode();
  }

  public void playPickupItemAnimation(@NotNull Item item, int i) {
    player.playPickupItemAnimation(item, i);
  }

  @NotNull
  public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b) {
    return player.addAttachment(plugin, s, b);
  }

  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
  @Deprecated
  public void sendMessage(@NotNull Component message, @NotNull MessageType type) {
    player.sendMessage(message, type);
  }

  public void sendHealthUpdate() {
    player.sendHealthUpdate();
  }

  @Deprecated
  public long getLastPlayed() {
    return player.getLastPlayed();
  }

  public int getPing() {
    return player.getPing();
  }

  @Deprecated
  public void setPlayerListHeaderFooter(@Nullable BaseComponent[] baseComponents,
                                        @Nullable BaseComponent[] baseComponents1) {
    player.setPlayerListHeaderFooter(baseComponents, baseComponents1);
  }

  public void sendHurtAnimation(float v) {
    player.sendHurtAnimation(v);
  }

  @NotNull
  public BlockFace getFacing() {
    return player.getFacing();
  }

  @ApiStatus.Experimental
  @Nullable
  public EntitySnapshot createSnapshot() {
    return player.createSnapshot();
  }

  public void broadcastSlotBreak(@NotNull EquipmentSlot equipmentSlot, @NotNull Collection<Player> collection) {
    player.broadcastSlotBreak(equipmentSlot, collection);
  }

  public boolean hasNoPhysics() {
    return player.hasNoPhysics();
  }

  @ApiStatus.Experimental
  public void storeCookie(@NotNull NamespacedKey namespacedKey, @NotNull byte[] bytes) {
    player.storeCookie(namespacedKey, bytes);
  }

  public void sendPlainMessage(@NotNull String message) {
    player.sendPlainMessage(message);
  }

  @Nullable
  public Location getRespawnLocation() {
    return player.getRespawnLocation();
  }

  public void sendActionBar(@NotNull ComponentLike message) {
    player.sendActionBar(message);
  }

  public void setFoodLevel(int i) {
    player.setFoodLevel(i);
  }

  public int getMaxFireTicks() {
    return player.getMaxFireTicks();
  }

  @NotNull
  public <T> Optional<T> get(@NotNull Pointer<T> pointer) {
    return player.get(pointer);
  }

  public void setStatistic(@NotNull Statistic statistic, @NotNull Material material, int i) throws IllegalArgumentException {
    player.setStatistic(statistic, material, i);
  }

  @Deprecated
  public int getNoTickViewDistance() {
    return player.getNoTickViewDistance();
  }

  @ApiStatus.Experimental
  public void startUsingItem(@NotNull EquipmentSlot equipmentSlot) {
    player.startUsingItem(equipmentSlot);
  }

  public void setLastDeathLocation(@Nullable Location location) {
    player.setLastDeathLocation(location);
  }

  public void setFlySpeed(float v) throws IllegalArgumentException {
    player.setFlySpeed(v);
  }

  public void setScoreboard(@NotNull Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
    player.setScoreboard(scoreboard);
  }

  public void setAI(boolean b) {
    player.setAI(b);
  }

  public void setOp(boolean b) {
    player.setOp(b);
  }

  public void deleteMessage(@NotNull SignedMessage signedMessage) {
    player.deleteMessage(signedMessage);
  }

  public void setHasSeenWinScreen(boolean b) {
    player.setHasSeenWinScreen(b);
  }

  @Deprecated
  public void setResourcePack(@NotNull String s, @Nullable byte[] bytes, boolean b) {
    player.setResourcePack(s, bytes, b);
  }

  public void setResourcePack(@NotNull UUID uuid, @NotNull String s, byte @Nullable [] bytes,
                              @Nullable Component component, boolean b) {
    player.setResourcePack(uuid, s, bytes, component, b);
  }

  @NotNull
  public Set<UUID> getCollidableExemptions() {
    return player.getCollidableExemptions();
  }

  public boolean isInLava() {
    return player.isInLava();
  }

  @Deprecated(forRemoval = true, since = "1.20.4")
  public int getItemInUseTicks() {
    return player.getItemInUseTicks();
  }

  @NotNull
  public Server getServer() {
    return player.getServer();
  }

  public int getExpCooldown() {
    return player.getExpCooldown();
  }

  public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3,
                                double v4, double v5, @Nullable T t) {
    player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, t);
  }

  public void setPose(@NotNull Pose pose, boolean b) {
    player.setPose(pose, b);
  }

  @NotNull
  public Audience filterAudience(@NotNull Predicate<? super Audience> filter) {
    return player.filterAudience(filter);
  }

  public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                            double v2) {
    player.spawnParticle(particle, location, i, v, v1, v2);
  }

  @Deprecated
  public void hidePlayer(@NotNull Player player) {
    this.player.hidePlayer(player);
  }

  public void stopSound(@NotNull SoundStop stop) {
    player.stopSound(stop);
  }

  public boolean hasLineOfSight(@NotNull Location location) {
    return player.hasLineOfSight(location);
  }

  public void setSneaking(boolean b) {
    player.setSneaking(b);
  }

  public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4,
                            double v5, double v6) {
    player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6);
  }

  @Deprecated
  @Nullable
  public Entity getPassenger() {
    return player.getPassenger();
  }

  public boolean fromMobSpawner() {
    return player.fromMobSpawner();
  }

  public boolean isTransferred() {
    return player.isTransferred();
  }

  public void hideBossBar(@NotNull BossBar bar) {
    player.hideBossBar(bar);
  }

  public void registerAttribute(@NotNull Attribute attribute) {
    player.registerAttribute(attribute);
  }

  public boolean leaveVehicle() {
    return player.leaveVehicle();
  }

  public void openBook(@NotNull Book book) {
    player.openBook(book);
  }

  public boolean setLeashHolder(@Nullable Entity entity) {
    return player.setLeashHolder(entity);
  }

  public void setJumping(boolean b) {
    player.setJumping(b);
  }

  @NotNull
  public List<Entity> getPassengers() {
    return player.getPassengers();
  }

  public void setEnchantmentSeed(int i) {
    player.setEnchantmentSeed(i);
  }

  public int getTicksLived() {
    return player.getTicksLived();
  }

  public boolean isSilent() {
    return player.isSilent();
  }

  @Deprecated
  public void sendMessage(@Nullable UUID uuid, @NotNull String... strings) {
    player.sendMessage(uuid, strings);
  }

  public void removeAttachment(@NotNull PermissionAttachment permissionAttachment) {
    player.removeAttachment(permissionAttachment);
  }

  public <E extends BanEntry<? super PlayerProfile>> @Nullable E ban(@Nullable String s, @Nullable Duration duration,
                                                                     @Nullable String s1, boolean b) {
    return player.ban(s, duration, s1, b);
  }

  public void stopSound(@NotNull String s, @Nullable SoundCategory soundCategory) {
    player.stopSound(s, soundCategory);
  }

  public boolean isAllowingServerListings() {
    return player.isAllowingServerListings();
  }

  public void playSound(@NotNull Sound sound, double x, double y, double z) {
    player.playSound(sound, x, y, z);
  }

  public void removeCustomChatCompletions(@NotNull Collection<String> collection) {
    player.removeCustomChatCompletions(collection);
  }

  @NotNull
  public List<Block> getLastTwoTargetBlocks(@Nullable Set<Material> set, int i) {
    return player.getLastTwoTargetBlocks(set, i);
  }

  public boolean getCanPickupItems() {
    return player.getCanPickupItems();
  }

  public <T> void setMemory(@NotNull MemoryKey<T> memoryKey, @Nullable T t) {
    player.setMemory(memoryKey, t);
  }

  public void remove() {
    player.remove();
  }

  @NotNull
  public String getName() {
    return player.getName();
  }

  public void hideEntity(@NotNull Plugin plugin, @NotNull Entity entity) {
    player.hideEntity(plugin, entity);
  }

  @Deprecated
  public double getMaxHealth() {
    return player.getMaxHealth();
  }

  public float getSaturation() {
    return player.getSaturation();
  }

  public boolean isInWaterOrRain() {
    return player.isInWaterOrRain();
  }

  public boolean isPermissionSet(@NotNull String s) {
    return player.isPermissionSet(s);
  }

  public void openSign(@NotNull Sign sign, @NotNull Side side) {
    player.openSign(sign, side);
  }

  public void updateInventory() {
    player.updateInventory();
  }

  public int getNoActionTicks() {
    return player.getNoActionTicks();
  }

  public boolean isRiptiding() {
    return player.isRiptiding();
  }

  public float getFallDistance() {
    return player.getFallDistance();
  }

  @NotNull
  public org.bukkit.Sound getSwimSplashSound() {
    return player.getSwimSplashSound();
  }

  public float getSidewaysMovement() {
    return player.getSidewaysMovement();
  }

  public boolean collidesAt(@NotNull Location location) {
    return player.collidesAt(location);
  }

  public void setTotalExperience(int i) {
    player.setTotalExperience(i);
  }

  @Nullable
  public InventoryView openEnchanting(@Nullable Location location, boolean b) {
    return player.openEnchanting(location, b);
  }

  public void setRespawnLocation(@Nullable Location location, boolean b) {
    player.setRespawnLocation(location, b);
  }

  @Deprecated
  @NotNull
  public BanEntry banPlayer(@Nullable String reason) {
    return player.banPlayer(reason);
  }

  @Nullable
  public InventoryView openMerchant(@NotNull Villager villager, boolean b) {
    return player.openMerchant(villager, b);
  }

  public void playHurtAnimation(float v) {
    player.playHurtAnimation(v);
  }

  public void setGliding(boolean b) {
    player.setGliding(b);
  }

  @NotNull
  public Component playerListName() {
    return player.playerListName();
  }

  public <T> void sendTitlePart(@NotNull TitlePart<T> part, @NotNull T value) {
    player.sendTitlePart(part, value);
  }

  @NotNull
  public SpawnCategory getSpawnCategory() {
    return player.getSpawnCategory();
  }

  public void sendMessage(@NotNull Component message, ChatType.Bound boundChatType) {
    player.sendMessage(message, boundChatType);
  }

  public long getPlayerTime() {
    return player.getPlayerTime();
  }

  @Deprecated
  public void setArrowsStuck(int i) {
    player.setArrowsStuck(i);
  }

  public int getArrowCooldown() {
    return player.getArrowCooldown();
  }

  public void setActiveItemRemainingTime(@Range(from = 0L, to = 2147483647L) int i) {
    player.setActiveItemRemainingTime(i);
  }

  public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4,
                            double v5) {
    player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5);
  }

  public void setSendViewDistance(int i) {
    player.setSendViewDistance(i);
  }

  public void customName(@Nullable Component component) {
    player.customName(component);
  }

  @NotNull
  public Collection<PotionEffect> getActivePotionEffects() {
    return player.getActivePotionEffects();
  }

  public boolean performCommand(@NotNull String s) {
    return player.performCommand(s);
  }

  @Deprecated
  public void setDisplayName(@Nullable String s) {
    player.setDisplayName(s);
  }

  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
  @Deprecated
  public void sendMessage(@NotNull ComponentLike message, @NotNull MessageType type) {
    player.sendMessage(message, type);
  }

  @Nullable
  public org.bukkit.Sound getHurtSound() {
    return player.getHurtSound();
  }

  @Deprecated
  public void setShoulderEntityRight(@Nullable Entity entity) {
    player.setShoulderEntityRight(entity);
  }

  public int getMaximumNoDamageTicks() {
    return player.getMaximumNoDamageTicks();
  }

  public float getExp() {
    return player.getExp();
  }

  @Nullable
  public InventoryView openCartographyTable(@Nullable Location location, boolean b) {
    return player.openCartographyTable(location, b);
  }

  public void setPortalCooldown(int i) {
    player.setPortalCooldown(i);
  }

  @Deprecated
  public void sendBlockChange(@NotNull Location location, @NotNull Material material, byte b) {
    player.sendBlockChange(location, material, b);
  }

  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
  @Deprecated
  public void sendMessage(@NotNull Identified source, @NotNull Component message, @NotNull MessageType type) {
    player.sendMessage(source, message, type);
  }

  public void sendPlayerListHeaderAndFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
    player.sendPlayerListHeaderAndFooter(header, footer);
  }

  public int getProtocolVersion() {
    return player.getProtocolVersion();
  }

  public void knockback(double v, double v1, double v2) {
    player.knockback(v, v1, v2);
  }

  @ApiStatus.Experimental
  @NotNull
  public Entity copy(@NotNull Location location) {
    return player.copy(location);
  }

  @Deprecated(forRemoval = true)
  @Nullable
  public TargetBlockInfo getTargetBlockInfo(int i, @NotNull TargetBlockInfo.FluidMode fluidMode) {
    return player.getTargetBlockInfo(i, fluidMode);
  }

  public void setLastDamage(double v) {
    player.setLastDamage(v);
  }

  @Deprecated
  public void setShoulderEntityLeft(@Nullable Entity entity) {
    player.setShoulderEntityLeft(entity);
  }

  public void setFlyingFallDamage(@NotNull TriState triState) {
    player.setFlyingFallDamage(triState);
  }

  public void setNextArrowRemoval(@Range(from = 0L, to = 2147483647L) int i) {
    player.setNextArrowRemoval(i);
  }

  public void playSound(@NotNull Entity entity, @NotNull org.bukkit.Sound sound, float v, float v1) {
    player.playSound(entity, sound, v, v1);
  }

  public void setWardenWarningLevel(int i) {
    player.setWardenWarningLevel(i);
  }

  public int getUnsaturatedRegenRate() {
    return player.getUnsaturatedRegenRate();
  }

  @NotNull
  public Vector getVelocity() {
    return player.getVelocity();
  }

  @NotNull
  public Component teamDisplayName() {
    return player.teamDisplayName();
  }

  @Deprecated
  public void kickPlayer(@Nullable String s) {
    player.kickPlayer(s);
  }

  public void stopSound(@NotNull String s) {
    player.stopSound(s);
  }

  @ApiStatus.Obsolete(since = "1.20.4")
  public boolean isHandRaised() {
    return player.isHandRaised();
  }

  @NotNull
  public EquipmentSlot getActiveItemHand() {
    return player.getActiveItemHand();
  }

  @Deprecated
  public void updateTitle(@NotNull Title title) {
    player.updateTitle(title);
  }

  public void setAffectsSpawning(boolean b) {
    player.setAffectsSpawning(b);
  }

  @Deprecated
  public void sendBlockChanges(@NotNull Collection<BlockState> collection, boolean b) {
    player.sendBlockChanges(collection, b);
  }

  public void sendEquipmentChange(@NotNull LivingEntity livingEntity, @NotNull EquipmentSlot equipmentSlot,
                                  @Nullable ItemStack itemStack) {
    player.sendEquipmentChange(livingEntity, equipmentSlot, itemStack);
  }

  public <E extends BanEntry<? super PlayerProfile>> @Nullable E ban(@Nullable String s, @Nullable Instant instant,
                                                                     @Nullable String s1, boolean b) {
    return player.ban(s, instant, s1, b);
  }

  public void setRespawnLocation(@Nullable Location location) {
    player.setRespawnLocation(location);
  }

  @Deprecated(forRemoval = true)
  @Nullable
  public BlockFace getTargetBlockFace(int i, @NotNull TargetBlockInfo.FluidMode fluidMode) {
    return player.getTargetBlockFace(i, fluidMode);
  }

  public void sendPlayerListHeader(@NotNull ComponentLike header) {
    player.sendPlayerListHeader(header);
  }

  @Deprecated
  public void sendSignChange(@NotNull Location location, @Nullable String[] strings) throws IllegalArgumentException {
    player.sendSignChange(location, strings);
  }

  public int getExperiencePointsNeededForNextLevel() {
    return player.getExperiencePointsNeededForNextLevel();
  }

  public int getWardenTimeSinceLastWarning() {
    return player.getWardenTimeSinceLastWarning();
  }

  @Nullable
  public RayTraceResult rayTraceBlocks(double v) {
    return player.rayTraceBlocks(v);
  }

  public void incrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
    player.incrementStatistic(statistic);
  }

  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
  @Deprecated
  public void sendMessage(@NotNull Identified source, @NotNull ComponentLike message, @NotNull MessageType type) {
    player.sendMessage(source, message, type);
  }

  public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
    player.decrementStatistic(statistic, material);
  }

  public boolean teleport(@NotNull Entity entity, @NotNull PlayerTeleportEvent.TeleportCause teleportCause) {
    return player.teleport(entity, teleportCause);
  }

  public void removeResourcePacks(@NotNull Iterable<UUID> ids) {
    player.removeResourcePacks(ids);
  }

  public void resetPlayerTime() {
    player.resetPlayerTime();
  }

  public void setPlayerProfile(@NotNull PlayerProfile playerProfile) {
    player.setPlayerProfile(playerProfile);
  }

  public void setArrowsInBody(int count) {
    player.setArrowsInBody(count);
  }

  @NotNull
  public ItemStack getActiveItem() {
    return player.getActiveItem();
  }

  public void incrementStatistic(@NotNull Statistic statistic, int i) throws IllegalArgumentException {
    player.incrementStatistic(statistic, i);
  }

  @ApiStatus.Obsolete
  public void sendMessage(@NotNull String s) {
    player.sendMessage(s);
  }

  public void sendMessage(@NotNull Identity identity, @NotNull Component message, @NotNull MessageType type) {
    player.sendMessage(identity, message, type);
  }

  public boolean isOnline() {
    return player.isOnline();
  }

  @Deprecated
  public void sendActionBar(char c, @NotNull String s) {
    player.sendActionBar(c, s);
  }

  public boolean isVisibleByDefault() {
    return player.isVisibleByDefault();
  }

  public void sendPluginMessage(@NotNull Plugin plugin, @NotNull String s, @NotNull byte[] bytes) {
    player.sendPluginMessage(plugin, s, bytes);
  }

  public void playSound(@NotNull Entity entity, @NotNull org.bukkit.Sound sound, @NotNull SoundCategory soundCategory
      , float v,
                        float v1, long l) {
    player.playSound(entity, sound, soundCategory, v, v1, l);
  }

  public boolean removePassenger(@NotNull Entity entity) {
    return player.removePassenger(entity);
  }

  public void sendMessage(@NotNull ComponentLike message) {
    player.sendMessage(message);
  }

  public boolean canBreatheUnderwater() {
    return player.canBreatheUnderwater();
  }

  @NotNull
  public Location getCompassTarget() {
    return player.getCompassTarget();
  }

  public void setBeeStingerCooldown(int i) {
    player.setBeeStingerCooldown(i);
  }

  public void sendPlayerListFooter(@NotNull ComponentLike footer) {
    player.sendPlayerListFooter(footer);
  }

  @Deprecated
  public void sendSignChange(@NotNull Location location, @Nullable List<? extends Component> list,
                             @NotNull DyeColor dyeColor, boolean b) throws IllegalArgumentException {
    player.sendSignChange(location, list, dyeColor, b);
  }

  public void setHealthScale(double v) throws IllegalArgumentException {
    player.setHealthScale(v);
  }

  @UnmodifiableView
  @NotNull
  public Iterable<? extends BossBar> activeBossBars() {
    return player.activeBossBars();
  }

  @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
  @Deprecated
  public void sendMessage(@NotNull Identity source, @NotNull ComponentLike message, @NotNull MessageType type) {
    player.sendMessage(source, message, type);
  }

  public long getLastLogin() {
    return player.getLastLogin();
  }

  @Deprecated
  @Nullable
  public BanEntry banPlayerFull(@Nullable String reason, @Nullable String source) {
    return player.banPlayerFull(reason, source);
  }

  public void lookAt(@NotNull Position position, @NotNull LookAnchor playerAnchor) {
    player.lookAt(position, playerAnchor);
  }

  public void resetCooldown() {
    player.resetCooldown();
  }

  @Deprecated
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason, @Nullable Date expires, boolean kickPlayer) {
    return player.banPlayerIP(reason, expires, kickPlayer);
  }

  public int applyMending(int i) {
    return player.applyMending(i);
  }

  @NotNull
  public Map<String, Object> serialize() {
    return player.serialize();
  }

  public boolean unlistPlayer(@NotNull Player player) {
    return this.player.unlistPlayer(player);
  }

  public void setSaturatedRegenRate(int i) {
    player.setSaturatedRegenRate(i);
  }

  public boolean isInWorld() {
    return player.isInWorld();
  }

  @Nullable
  public String getClientBrandName() {
    return player.getClientBrandName();
  }

  public boolean isChunkSent(@NotNull Chunk chunk) {
    return player.isChunkSent(chunk);
  }

  public void stopAllSounds() {
    player.stopAllSounds();
  }

  public <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> aClass) {
    return player.launchProjectile(aClass);
  }

  @Nullable
  public BanEntry<InetAddress> banIp(@Nullable String s, @Nullable Date date, @Nullable String s1, boolean b) {
    return player.banIp(s, date, s1, b);
  }

  public void setInvisible(boolean b) {
    player.setInvisible(b);
  }

  @Deprecated
  @NotNull
  public BanEntry banPlayer(@Nullable String reason, @Nullable String source) {
    return player.banPlayer(reason, source);
  }

  public boolean isFlying() {
    return player.isFlying();
  }

  public void attack(@NotNull Entity entity) {
    player.attack(entity);
  }

  public void abandonConversation(@NotNull Conversation conversation) {
    player.abandonConversation(conversation);
  }

  @Deprecated
  public void sendMessage(@NotNull Identity source, @NotNull Component message) {
    player.sendMessage(source, message);
  }

  public int getStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
    return player.getStatistic(statistic, material);
  }

  @Deprecated
  public void sendSignChange(@NotNull Location loc, @Nullable List<? extends Component> lines,
                             @NotNull DyeColor dyeColor) throws IllegalArgumentException {
    player.sendSignChange(loc, lines, dyeColor);
  }

  public boolean isJumping() {
    return player.isJumping();
  }

  public void resetIdleDuration() {
    player.resetIdleDuration();
  }

  public void setResourcePack(@NotNull String url, byte @Nullable [] hash, @Nullable Component prompt) {
    player.setResourcePack(url, hash, prompt);
  }

  @Nullable
  public Block getTargetBlockExact(int i) {
    return player.getTargetBlockExact(i);
  }

  public void acceptConversationInput(@NotNull String s) {
    player.acceptConversationInput(s);
  }

  public void setHurtDirection(float v) {
    player.setHurtDirection(v);
  }

  public boolean isSleepingIgnored() {
    return player.isSleepingIgnored();
  }

  public void resetPlayerWeather() {
    player.resetPlayerWeather();
  }

  @NotNull
  public HoverEvent<HoverEvent.ShowEntity> asHoverEvent(@NotNull UnaryOperator<HoverEvent.ShowEntity> op) {
    return player.asHoverEvent(op);
  }

  public int getSimulationDistance() {
    return player.getSimulationDistance();
  }

  @NotNull
  public TriState permissionValue(@NotNull Permission permission) {
    return player.permissionValue(permission);
  }

  @Deprecated
  public void setSubtitle(BaseComponent[] baseComponents) {
    player.setSubtitle(baseComponents);
  }

  @Deprecated(forRemoval = true)
  @Nullable
  public TargetEntityInfo getTargetEntityInfo(int maxDistance) {
    return player.getTargetEntityInfo(maxDistance);
  }

  @Deprecated
  public void setItemInHand(@Nullable ItemStack itemStack) {
    player.setItemInHand(itemStack);
  }

  public boolean dropItem(boolean b) {
    return player.dropItem(b);
  }

  @NotNull
  public Set<Player> getTrackedBy() {
    return player.getTrackedBy();
  }

  public double getHeight() {
    return player.getHeight();
  }

  @Nullable
  public GameMode getPreviousGameMode() {
    return player.getPreviousGameMode();
  }

  @Nullable
  public BanEntry<InetAddress> banIp(@Nullable String s, @Nullable Instant instant, @Nullable String s1, boolean b) {
    return player.banIp(s, instant, s1, b);
  }

  public void lookAt(double v, double v1, double v2, @NotNull LookAnchor lookAnchor) {
    player.lookAt(v, v1, v2, lookAnchor);
  }

  @NotNull
  public Player.Spigot spigot() {
    return player.spigot();
  }

  @Deprecated
  public void showTitle(@Nullable BaseComponent[] baseComponents) {
    player.showTitle(baseComponents);
  }

  @Deprecated
  public void showPlayer(@NotNull Player player) {
    this.player.showPlayer(player);
  }

  public void playSound(@NotNull Location location, @NotNull String s, float v, float v1) {
    player.playSound(location, s, v, v1);
  }

  @Deprecated(forRemoval = true)
  @Nullable
  public TargetEntityInfo getTargetEntityInfo(int i, boolean b) {
    return player.getTargetEntityInfo(i, b);
  }

  public void setFallDistance(float v) {
    player.setFallDistance(v);
  }

  @NotNull
  public List<Block> getLineOfSight(@Nullable Set<Material> set, int i) {
    return player.getLineOfSight(set, i);
  }

  public boolean isPersistent() {
    return player.isPersistent();
  }

  public void setItemOnCursor(@Nullable ItemStack itemStack) {
    player.setItemOnCursor(itemStack);
  }

  public void decrementStatistic(@NotNull Statistic statistic, int i) throws IllegalArgumentException {
    player.decrementStatistic(statistic, i);
  }

  @Nullable
  public Player getKiller() {
    return player.getKiller();
  }

  public void setInvulnerable(boolean b) {
    player.setInvulnerable(b);
  }

  public void sendPotionEffectChangeRemove(@NotNull LivingEntity livingEntity,
                                           @NotNull PotionEffectType potionEffectType) {
    player.sendPotionEffectChangeRemove(livingEntity, potionEffectType);
  }

  @Deprecated
  public void sendMessage(@Nullable UUID uuid, @NotNull String s) {
    player.sendMessage(uuid, s);
  }

  @Deprecated
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason) {
    return player.banPlayerIP(reason);
  }

  @Deprecated
  @Nullable
  public Entity getShoulderEntityRight() {
    return player.getShoulderEntityRight();
  }

  @Nullable
  public EntityDamageEvent getLastDamageCause() {
    return player.getLastDamageCause();
  }

  public long getFirstPlayed() {
    return player.getFirstPlayed();
  }

  public int getCooldown(@NotNull Material material) {
    return player.getCooldown(material);
  }

  public int getFoodLevel() {
    return player.getFoodLevel();
  }

  @NotNull
  public Component displayName() {
    return player.displayName();
  }

  @Nullable
  public InetSocketAddress getAddress() {
    return player.getAddress();
  }

  public void playSound(@NotNull Location location, @NotNull String s, @NotNull SoundCategory soundCategory, float v,
                        float v1) {
    player.playSound(location, s, soundCategory, v, v1);
  }

  @Nullable
  public PotionEffect getPotionEffect(@NotNull PotionEffectType potionEffectType) {
    return player.getPotionEffect(potionEffectType);
  }

  public void setFireTicks(int i) {
    player.setFireTicks(i);
  }

  public boolean isFreezeTickingLocked() {
    return player.isFreezeTickingLocked();
  }

  @Deprecated
  @Nullable
  public Entity getShoulderEntityLeft() {
    return player.getShoulderEntityLeft();
  }

  public void sendResourcePacks(@NotNull ResourcePackRequestLike request) {
    player.sendResourcePacks(request);
  }

  public int getMaximumAir() {
    return player.getMaximumAir();
  }

  @Deprecated
  public void sendSignChange(@NotNull Location loc, @Nullable List<? extends Component> lines,
                             boolean hasGlowingText) throws IllegalArgumentException {
    player.sendSignChange(loc, lines, hasGlowingText);
  }

  public void setLevel(int i) {
    player.setLevel(i);
  }

  public void setSpectatorTarget(@Nullable Entity entity) {
    player.setSpectatorTarget(entity);
  }

  @Deprecated(since = "1.20.1")
  public void addAdditionalChatCompletions(@NotNull Collection<String> collection) {
    player.addAdditionalChatCompletions(collection);
  }

  @Nullable
  public InventoryView openInventory(@NotNull Inventory inventory) {
    return player.openInventory(inventory);
  }

  @Nullable
  public BanEntry<InetAddress> banIp(@Nullable String s, @Nullable Duration duration, @Nullable String s1, boolean b) {
    return player.banIp(s, duration, s1, b);
  }

  public boolean addPotionEffects(@NotNull Collection<PotionEffect> collection) {
    return player.addPotionEffects(collection);
  }

  public void setRemoveWhenFarAway(boolean b) {
    player.setRemoveWhenFarAway(b);
  }

  @NotNull
  public EntityType getType() {
    return player.getType();
  }

  public boolean isTicking() {
    return player.isTicking();
  }

  public float getWalkSpeed() {
    return player.getWalkSpeed();
  }

  @NotNull
  public org.bukkit.Sound getDrinkingSound(@NotNull ItemStack itemStack) {
    return player.getDrinkingSound(itemStack);
  }

  public float getBodyYaw() {
    return player.getBodyYaw();
  }

  public boolean teleport(@NotNull Location location) {
    return player.teleport(location);
  }

  @NotNull
  public Set<String> getScoreboardTags() {
    return player.getScoreboardTags();
  }

  @Nullable
  public Firework boostElytra(@NotNull ItemStack firework) {
    return player.boostElytra(firework);
  }

  @ApiStatus.Experimental
  public void damage(double v, @NotNull DamageSource damageSource) {
    player.damage(v, damageSource);
  }

  @Deprecated
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason, @Nullable Date expires, @Nullable String source,
                              boolean kickPlayer) {
    return player.banPlayerIP(reason, expires, source, kickPlayer);
  }

  public void sendExperienceChange(float v) {
    player.sendExperienceChange(v);
  }

  @Deprecated
  public void sendTitle(@Nullable String s, @Nullable String s1, int i, int i1, int i2) {
    player.sendTitle(s, s1, i, i1, i2);
  }

  @Deprecated
  public void sendMessage(ChatMessageType position, BaseComponent... components) {
    player.sendMessage(position, components);
  }

  public int getArrowsInBody() {
    return player.getArrowsInBody();
  }

  public void setWhitelisted(boolean b) {
    player.setWhitelisted(b);
  }

  public void setWardenTimeSinceLastWarning(int i) {
    player.setWardenTimeSinceLastWarning(i);
  }

  @NotNull
  public org.bukkit.Sound getFallDamageSound(int i) {
    return player.getFallDamageSound(i);
  }

  public boolean undiscoverRecipe(@NotNull NamespacedKey namespacedKey) {
    return player.undiscoverRecipe(namespacedKey);
  }

  @NotNull
  public Pointers pointers() {
    return player.pointers();
  }

  public boolean removeScoreboardTag(@NotNull String s) {
    return player.removeScoreboardTag(s);
  }

  @Deprecated
  public void openSign(@NotNull Sign sign) {
    player.openSign(sign);
  }

  @NotNull
  public ItemStack damageItemStack(@NotNull ItemStack itemStack, int i) {
    return player.damageItemStack(itemStack, i);
  }

  public double getLastDamage() {
    return player.getLastDamage();
  }

  public void sendRichMessage(@NotNull String message, @NotNull TagResolver... resolvers) {
    player.sendRichMessage(message, resolvers);
  }

  public void setPlayerWeather(@NotNull WeatherType weatherType) {
    player.setPlayerWeather(weatherType);
  }

  public boolean getAllowFlight() {
    return player.getAllowFlight();
  }

  public void setHealth(double v) {
    player.setHealth(v);
  }

  @Nullable
  public PermissionAttachment addAttachment(@NotNull Plugin plugin, int i) {
    return player.addAttachment(plugin, i);
  }

  @Nullable
  public InventoryView openStonecutter(@Nullable Location location, boolean b) {
    return player.openStonecutter(location, b);
  }

  public boolean discoverRecipe(@NotNull NamespacedKey namespacedKey) {
    return player.discoverRecipe(namespacedKey);
  }

  public boolean isUnderWater() {
    return player.isUnderWater();
  }

  public void showWinScreen() {
    player.showWinScreen();
  }

  public int getClientViewDistance() {
    return player.getClientViewDistance();
  }

  public int getBeeStingerCooldown() {
    return player.getBeeStingerCooldown();
  }

  public void setCanPickupItems(boolean b) {
    player.setCanPickupItems(b);
  }

  @Nullable
  public Entity getTargetEntity(int maxDistance) {
    return player.getTargetEntity(maxDistance);
  }

  public void setGravity(boolean b) {
    player.setGravity(b);
  }

  public double getZ() {
    return player.getZ();
  }

  public void clearResourcePacks() {
    player.clearResourcePacks();
  }

  @Deprecated
  public void setNoTickViewDistance(int viewDistance) {
    player.setNoTickViewDistance(viewDistance);
  }

  @NotNull
  public CompletableFuture<Boolean> teleportAsync(@NotNull Location loc,
                                                  @NotNull PlayerTeleportEvent.TeleportCause cause) {
    return player.teleportAsync(loc, cause);
  }

  @Deprecated
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason, @Nullable String source, boolean kickPlayer) {
    return player.banPlayerIP(reason, source, kickPlayer);
  }

  public void setShieldBlockingDelay(int i) {
    player.setShieldBlockingDelay(i);
  }

  @Nullable
  public Component playerListHeader() {
    return player.playerListHeader();
  }

  @Range(from = 0L, to = 2147483647L)
  public int calculateTotalExperiencePoints() {
    return player.calculateTotalExperiencePoints();
  }

  public void damage(double v) {
    player.damage(v);
  }

  public void sendMessage(@NotNull SignedMessage signedMessage, ChatType.Bound boundChatType) {
    player.sendMessage(signedMessage, boundChatType);
  }

  @NotNull
  public PlayerInventory getInventory() {
    return player.getInventory();
  }

  public void setFreezeTicks(int i) {
    player.setFreezeTicks(i);
  }

  public void removeResourcePacks(@NotNull ResourcePackRequestLike request) {
    player.removeResourcePacks(request);
  }

  public int getStarvationRate() {
    return player.getStarvationRate();
  }

  public void setNoDamageTicks(int i) {
    player.setNoDamageTicks(i);
  }

  public int getStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
    return player.getStatistic(statistic);
  }

  public void sendHealthUpdate(double v, int i, float v1) {
    player.sendHealthUpdate(v, i, v1);
  }

  @Deprecated
  public void setPlayerListFooter(@Nullable String s) {
    player.setPlayerListFooter(s);
  }

  @Nullable
  public BlockFace getTargetBlockFace(int i, @NotNull FluidCollisionMode fluidCollisionMode) {
    return player.getTargetBlockFace(i, fluidCollisionMode);
  }

  public boolean isBanned() {
    return player.isBanned();
  }

  public void setBeeStingersInBody(int i) {
    player.setBeeStingersInBody(i);
  }

  public void removeResourcePacks(@NotNull ResourcePackInfoLike request,
                                  @NotNull ResourcePackInfoLike @NotNull ... others) {
    player.removeResourcePacks(request, others);
  }

  @Deprecated
  public void showTitle(@Nullable BaseComponent[] baseComponents, @Nullable BaseComponent[] baseComponents1, int i,
                        int i1, int i2) {
    player.showTitle(baseComponents, baseComponents1, i, i1, i2);
  }

  @Deprecated
  public void setResourcePack(@NotNull String s, @Nullable byte[] bytes, @Nullable String s1) {
    player.setResourcePack(s, bytes, s1);
  }

  @Deprecated
  @NotNull
  public String getLocale() {
    return player.getLocale();
  }

  public int getWardenWarningCooldown() {
    return player.getWardenWarningCooldown();
  }

  public void sendBlockDamage(@NotNull Location location, float v, int i) {
    player.sendBlockDamage(location, v, i);
  }

  @NotNull
  public Entity getLeashHolder() throws IllegalStateException {
    return player.getLeashHolder();
  }

  @Deprecated
  @Nullable
  public String getPlayerListFooter() {
    return player.getPlayerListFooter();
  }

  public void kick(@Nullable Component component, PlayerKickEvent.Cause cause) {
    player.kick(component, cause);
  }

  public void playSound(@NotNull Entity entity, @NotNull String s, @NotNull SoundCategory soundCategory, float v,
                        float v1, long l) {
    player.playSound(entity, s, soundCategory, v, v1, l);
  }

  @Nullable
  public InventoryView openMerchant(@NotNull Merchant merchant, boolean b) {
    return player.openMerchant(merchant, b);
  }

  public boolean isInPowderedSnow() {
    return player.isInPowderedSnow();
  }

  public boolean addPotionEffect(@NotNull PotionEffect potionEffect) {
    return player.addPotionEffect(potionEffect);
  }

  public boolean hasResourcePack() {
    return player.hasResourcePack();
  }

  public void removeResourcePacks() {
    player.removeResourcePacks();
  }

  @Deprecated
  public void setPlayerListName(@Nullable String s) {
    player.setPlayerListName(s);
  }

  public float getForwardsMovement() {
    return player.getForwardsMovement();
  }

  @Nullable
  public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b, int i) {
    return player.addAttachment(plugin, s, b, i);
  }

  public boolean isCustomNameVisible() {
    return player.isCustomNameVisible();
  }

  @Deprecated(forRemoval = true)
  @Nullable
  public Block getTargetBlock(int i, @NotNull TargetBlockInfo.FluidMode fluidMode) {
    return player.getTargetBlock(i, fluidMode);
  }

  public boolean isClimbing() {
    return player.isClimbing();
  }

  @Nullable
  public Location getPotentialBedLocation() {
    return player.getPotentialBedLocation();
  }

  public void closeInventory(@NotNull InventoryCloseEvent.Reason reason) {
    player.closeInventory(reason);
  }

  @Nullable
  public Entity releaseLeftShoulderEntity() {
    return player.releaseLeftShoulderEntity();
  }

  public boolean isVisualFire() {
    return player.isVisualFire();
  }

  public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
    player.incrementStatistic(statistic, entityType);
  }

  public boolean canSee(@NotNull Entity entity) {
    return player.canSee(entity);
  }

  public boolean isHealthScaled() {
    return player.isHealthScaled();
  }

  @Deprecated
  public void resetMaxHealth() {
    player.resetMaxHealth();
  }

  @Deprecated
  public void sendMessage(@NotNull BaseComponent component) {
    player.sendMessage(component);
  }

  public void setExpCooldown(int i) {
    player.setExpCooldown(i);
  }

  public void playSound(@NotNull Location location, @NotNull org.bukkit.Sound sound,
                        @NotNull SoundCategory soundCategory,
                        float v, float v1) {
    player.playSound(location, sound, soundCategory, v, v1);
  }

  public void sendMultiBlockChange(@NotNull Map<? extends Position, BlockData> map) {
    player.sendMultiBlockChange(map);
  }

  public double getEyeHeight() {
    return player.getEyeHeight();
  }

  public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int i) throws IllegalArgumentException {
    player.incrementStatistic(statistic, entityType, i);
  }

  public boolean isCollidable() {
    return player.isCollidable();
  }

  public boolean isBlocking() {
    return player.isBlocking();
  }

  public boolean hasDiscoveredRecipe(@NotNull NamespacedKey namespacedKey) {
    return player.hasDiscoveredRecipe(namespacedKey);
  }

  @Nullable
  public InventoryView openGrindstone(@Nullable Location location, boolean b) {
    return player.openGrindstone(location, b);
  }

  public void saveData() {
    player.saveData();
  }

  public double getWidth() {
    return player.getWidth();
  }

  public boolean isFrozen() {
    return player.isFrozen();
  }

  public boolean spawnAt(@NotNull Location location, @NotNull CreatureSpawnEvent.SpawnReason spawnReason) {
    return player.spawnAt(location, spawnReason);
  }

  public boolean isSprinting() {
    return player.isSprinting();
  }

  public void playPickupItemAnimation(@NotNull Item item) {
    player.playPickupItemAnimation(item);
  }

  public boolean hasPermission(@NotNull Permission permission) {
    return player.hasPermission(permission);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                                double v2, @Nullable T t) {
    player.spawnParticle(particle, location, i, v, v1, v2, t);
  }

  public void sendPotionEffectChange(@NotNull LivingEntity livingEntity, @NotNull PotionEffect potionEffect) {
    player.sendPotionEffectChange(livingEntity, potionEffect);
  }

  @Deprecated
  public int getArrowsStuck() {
    return player.getArrowsStuck();
  }

  public PlayerResourcePackStatusEvent.Status getResourcePackStatus() {
    return player.getResourcePackStatus();
  }

  public int getViewDistance() {
    return player.getViewDistance();
  }

  public boolean isPermissionSet(@NotNull Permission permission) {
    return player.isPermissionSet(permission);
  }

  @Nullable
  public InventoryView openLoom(@Nullable Location location, boolean b) {
    return player.openLoom(location, b);
  }

  @Deprecated
  public boolean setPassenger(@NotNull Entity entity) {
    return player.setPassenger(entity);
  }

  public double getX() {
    return player.getX();
  }

  @Deprecated
  public void sendSignChange(@NotNull Location loc, @Nullable List<? extends Component> lines) throws IllegalArgumentException {
    player.sendSignChange(loc, lines);
  }

  @Nullable
  public InventoryView openAnvil(@Nullable Location location, boolean b) {
    return player.openAnvil(location, b);
  }

  @Nullable
  public Firework fireworkBoost(@NotNull ItemStack itemStack) {
    return player.fireworkBoost(itemStack);
  }

  @NotNull
  public Block getTargetBlock(@Nullable Set<Material> set, int i) {
    return player.getTargetBlock(set, i);
  }

  @Deprecated
  public void sendActionBar(@NotNull String s) {
    player.sendActionBar(s);
  }

  public boolean isInWater() {
    return player.isInWater();
  }

  public float getCooldownPeriod() {
    return player.getCooldownPeriod();
  }

  @Nullable
  public RayTraceResult rayTraceEntities(int i, boolean b) {
    return player.rayTraceEntities(i, b);
  }

  public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
    player.decrementStatistic(statistic, entityType);
  }

  public void setPose(@NotNull Pose pose) {
    player.setPose(pose);
  }

  public int getSleepTicks() {
    return player.getSleepTicks();
  }

  public int getExpToLevel() {
    return player.getExpToLevel();
  }

  public void playNote(@NotNull Location location, @NotNull Instrument instrument, @NotNull Note note) {
    player.playNote(location, instrument, note);
  }

  public double getHealthScale() {
    return player.getHealthScale();
  }

  public void setExhaustion(float v) {
    player.setExhaustion(v);
  }

  @Deprecated
  @NotNull
  public BanEntry banPlayer(@Nullable String reason, @Nullable Date expires) {
    return player.banPlayer(reason, expires);
  }

  @Deprecated
  public void setTitleTimes(int i, int i1, int i2) {
    player.setTitleTimes(i, i1, i2);
  }

  public void setResourcePack(@NotNull String url, @NotNull String hash, boolean required,
                              @Nullable Component resourcePackPrompt) {
    player.setResourcePack(url, hash, required, resourcePackPrompt);
  }

  public void closeInventory() {
    player.closeInventory();
  }

  public void setVisibleByDefault(boolean b) {
    player.setVisibleByDefault(b);
  }

  public boolean getRemoveWhenFarAway() {
    return player.getRemoveWhenFarAway();
  }

  public void loadData() {
    player.loadData();
  }

  @Deprecated
  public void setTexturePack(@NotNull String s) {
    player.setTexturePack(s);
  }

  public void resetTitle() {
    player.resetTitle();
  }

  public int getFreezeTicks() {
    return player.getFreezeTicks();
  }

  public <E extends BanEntry<? super PlayerProfile>> @Nullable E ban(@Nullable String s, @Nullable Duration duration,
                                                                     @Nullable String s1) {
    return player.ban(s, duration, s1);
  }

  public int getNextBeeStingerRemoval() {
    return player.getNextBeeStingerRemoval();
  }

  public boolean hasLineOfSight(@NotNull Entity entity) {
    return player.hasLineOfSight(entity);
  }

  public boolean isGliding() {
    return player.isGliding();
  }

  public void swingOffHand() {
    player.swingOffHand();
  }

  public int getActiveItemRemainingTime() {
    return player.getActiveItemRemainingTime();
  }

  @ApiStatus.Experimental
  @Nullable
  public String getAsString() {
    return player.getAsString();
  }

  public long getPlayerTimeOffset() {
    return player.getPlayerTimeOffset();
  }

  public void recalculatePermissions() {
    player.recalculatePermissions();
  }

  public int getSaturatedRegenRate() {
    return player.getSaturatedRegenRate();
  }

  @NotNull
  public List<MetadataValue> getMetadata(@NotNull String s) {
    return player.getMetadata(s);
  }

  public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int i) throws IllegalArgumentException {
    player.incrementStatistic(statistic, material, i);
  }

  @Deprecated
  public void playEffect(@NotNull Location location, @NotNull Effect effect, int i) {
    player.playEffect(location, effect, i);
  }

  public void showBossBar(@NotNull BossBar bar) {
    player.showBossBar(bar);
  }

  public void openBook(Book.Builder book) {
    player.openBook(book);
  }

  @ApiStatus.Experimental
  public void completeUsingActiveItem() {
    player.completeUsingActiveItem();
  }

  public void broadcastSlotBreak(@NotNull EquipmentSlot equipmentSlot) {
    player.broadcastSlotBreak(equipmentSlot);
  }

  public void setNoPhysics(boolean b) {
    player.setNoPhysics(b);
  }

  public void setExp(float v) {
    player.setExp(v);
  }

  @Nullable
  public WorldBorder getWorldBorder() {
    return player.getWorldBorder();
  }

  public boolean canSee(@NotNull Player player) {
    return this.player.canSee(player);
  }

  public double getHealth() {
    return player.getHealth();
  }

  public <E extends BanEntry<? super PlayerProfile>> @Nullable E ban(@Nullable String s, @Nullable Date date,
                                                                     @Nullable String s1) {
    return player.ban(s, date, s1);
  }

  public void playSound(@NotNull Sound sound) {
    player.playSound(sound);
  }

  public void addCustomChatCompletions(@NotNull Collection<String> collection) {
    player.addCustomChatCompletions(collection);
  }

  public void sendResourcePacks(@NotNull ResourcePackInfoLike first, @NotNull ResourcePackInfoLike... others) {
    player.sendResourcePacks(first, others);
  }

  @Deprecated(forRemoval = true, since = "1.20.4")
  @Nullable
  public ItemStack getItemInUse() {
    return player.getItemInUse();
  }

  public int getPortalCooldown() {
    return player.getPortalCooldown();
  }

  public boolean isConversing() {
    return player.isConversing();
  }

  public void setWalkSpeed(float v) throws IllegalArgumentException {
    player.setWalkSpeed(v);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                                double v2, double v3, @Nullable T t) {
    player.spawnParticle(particle, location, i, v, v1, v2, v3, t);
  }

  @ApiStatus.Obsolete(since = "1.20.4")
  public int getHandRaisedTime() {
    return player.getHandRaisedTime();
  }

  @Deprecated
  @Nullable
  public Location getBedSpawnLocation() {
    return player.getBedSpawnLocation();
  }

  @Deprecated
  public void sendMessage(@NotNull Identified source, @NotNull Component message) {
    player.sendMessage(source, message);
  }

  public <E extends BanEntry<? super PlayerProfile>> @Nullable E ban(@Nullable String s, @Nullable Instant instant,
                                                                     @Nullable String s1) {
    return player.ban(s, instant, s1);
  }

  public void setStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int i) {
    player.setStatistic(statistic, entityType, i);
  }

  @Deprecated
  public void showTitle(@Nullable BaseComponent baseComponent, @Nullable BaseComponent baseComponent1, int i, int i1,
                        int i2) {
    player.showTitle(baseComponent, baseComponent1, i, i1, i2);
  }

  public void setCustomChatCompletions(@NotNull Collection<String> collection) {
    player.setCustomChatCompletions(collection);
  }

  @Nullable
  public AttributeInstance getAttribute(@NotNull Attribute attribute) {
    return player.getAttribute(attribute);
  }

  public boolean wouldCollideUsing(@NotNull BoundingBox boundingBox) {
    return player.wouldCollideUsing(boundingBox);
  }

  @Deprecated
  @Nullable
  public BanEntry banPlayerFull(@Nullable String reason) {
    return player.banPlayerFull(reason);
  }

  @Deprecated
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason, @Nullable Date expires, @Nullable String source) {
    return player.banPlayerIP(reason, expires, source);
  }

  public float getHurtDirection() {
    return player.getHurtDirection();
  }

  @Deprecated
  public void setSwimming(boolean b) {
    player.setSwimming(b);
  }

  public int getActiveItemUsedTime() {
    return player.getActiveItemUsedTime();
  }

  @Deprecated
  public void sendRawMessage(@Nullable UUID uuid, @NotNull String s) {
    player.sendRawMessage(uuid, s);
  }

  public void setUnsaturatedRegenRate(int i) {
    player.setUnsaturatedRegenRate(i);
  }

  public void removeMetadata(@NotNull String s, @NotNull Plugin plugin) {
    player.removeMetadata(s, plugin);
  }

  public void sendBlockDamage(@NotNull Location location, float v) {
    player.sendBlockDamage(location, v);
  }

  public void stopSound(@NotNull Sound sound) {
    player.stopSound(sound);
  }

  public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T t) {
    player.playEffect(location, effect, t);
  }

  @Deprecated
  public void setBedSpawnLocation(@Nullable Location location, boolean b) {
    player.setBedSpawnLocation(location, b);
  }

  public <T> @NotNull T getClientOption(@NotNull ClientOption<T> clientOption) {
    return player.getClientOption(clientOption);
  }

  public <T> @UnknownNullability T getOrDefaultFrom(@NotNull Pointer<T> pointer,
                                                    @NotNull Supplier<? extends T> defaultValue) {
    return player.getOrDefaultFrom(pointer, defaultValue);
  }

  @Nullable
  public RayTraceResult rayTraceBlocks(double v, @NotNull FluidCollisionMode fluidCollisionMode) {
    return player.rayTraceBlocks(v, fluidCollisionMode);
  }

  public boolean isWhitelisted() {
    return player.isWhitelisted();
  }

  public void decrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
    player.decrementStatistic(statistic);
  }

  public void playerListName(@Nullable Component component) {
    player.playerListName(component);
  }

  @NotNull
  public Location getEyeLocation() {
    return player.getEyeLocation();
  }

  public void sendBlockChanges(@NotNull Collection<BlockState> collection) {
    player.sendBlockChanges(collection);
  }

  public boolean isInRain() {
    return player.isInRain();
  }

  public boolean hasAI() {
    return player.hasAI();
  }

  public void sendActionBar(@NotNull Component message) {
    player.sendActionBar(message);
  }

  public void showTitle(net.kyori.adventure.title.@NotNull Title title) {
    player.showTitle(title);
  }

  public boolean sleep(@NotNull Location location, boolean b) {
    return player.sleep(location, b);
  }

  public boolean teleport(@NotNull Entity entity) {
    return player.teleport(entity);
  }

  @ApiStatus.Experimental
  @NotNull
  public Entity copy() {
    return player.copy();
  }

  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, @Nullable T t) {
    player.spawnParticle(particle, location, i, t);
  }

  public float getAttackCooldown() {
    return player.getAttackCooldown();
  }

  public void playSound(@NotNull Location location, @NotNull org.bukkit.Sound sound,
                        @NotNull SoundCategory soundCategory,
                        float v, float v1, long l) {
    player.playSound(location, sound, soundCategory, v, v1, l);
  }

  @Deprecated
  @NotNull
  public String getDisplayName() {
    return player.getDisplayName();
  }

  public void setRemainingAir(int i) {
    player.setRemainingAir(i);
  }

  public void updateCommands() {
    player.updateCommands();
  }

  @Deprecated
  @Nullable
  public String getCustomName() {
    return player.getCustomName();
  }

  @Deprecated
  public void setResourcePack(@NotNull String s, @Nullable byte[] bytes) {
    player.setResourcePack(s, bytes);
  }

  public void kick() {
    player.kick();
  }

  public boolean addPassenger(@NotNull Entity entity) {
    return player.addPassenger(entity);
  }

  public void deleteMessage(SignedMessage.Signature signature) {
    player.deleteMessage(signature);
  }

  public void removeResourcePacks(@NotNull UUID id, @NotNull UUID @NotNull ... others) {
    player.removeResourcePacks(id, others);
  }

  public void giveExp(int amount) {
    player.giveExp(amount);
  }

  @ApiStatus.Experimental
  @NotNull
  @Unmodifiable
  public Set<Long> getSentChunkKeys() {
    return player.getSentChunkKeys();
  }

  public float getPitch() {
    return player.getPitch();
  }

  public void setFlying(boolean b) {
    player.setFlying(b);
  }

  public boolean isOp() {
    return player.isOp();
  }

  @NotNull
  public org.bukkit.Sound getFallDamageSoundBig() {
    return player.getFallDamageSoundBig();
  }

  public void setCompassTarget(@NotNull Location location) {
    player.setCompassTarget(location);
  }

  @Nullable
  public BlockFace getTargetBlockFace(int maxDistance) {
    return player.getTargetBlockFace(maxDistance);
  }

  public boolean teleport(@NotNull Location location, @NotNull PlayerTeleportEvent.TeleportCause teleportCause,
                          @NotNull TeleportFlag @NotNull ... teleportFlags) {
    return player.teleport(location, teleportCause, teleportFlags);
  }

  public boolean isEmpty() {
    return player.isEmpty();
  }

  public void forEachAudience(@NotNull Consumer<? super Audience> action) {
    player.forEachAudience(action);
  }

  public void hidePlayer(@NotNull Plugin plugin, @NotNull Player player) {
    this.player.hidePlayer(plugin, player);
  }

  public void increaseWardenWarningLevel() {
    player.increaseWardenWarningLevel();
  }

  @Nullable
  public Component playerListFooter() {
    return player.playerListFooter();
  }

  @Deprecated
  public void sendTitle(@NotNull Title title) {
    player.sendTitle(title);
  }

  public void setSimulationDistance(int i) {
    player.setSimulationDistance(i);
  }

  public void sendMessage(@NotNull ComponentLike message, ChatType.Bound boundChatType) {
    player.sendMessage(message, boundChatType);
  }

  public void setFrictionState(@NotNull TriState triState) {
    player.setFrictionState(triState);
  }

  @NotNull
  public HoverEvent<HoverEvent.ShowEntity> asHoverEvent() {
    return player.asHoverEvent();
  }

  public boolean teleport(@NotNull Location location, @NotNull PlayerTeleportEvent.TeleportCause teleportCause) {
    return player.teleport(location, teleportCause);
  }

  public boolean isInvulnerable() {
    return player.isInvulnerable();
  }

  @NotNull
  public CreatureSpawnEvent.SpawnReason getEntitySpawnReason() {
    return player.getEntitySpawnReason();
  }

  public void clearActiveItem() {
    player.clearActiveItem();
  }

  public boolean isPlayerTimeRelative() {
    return player.isPlayerTimeRelative();
  }

  public boolean isListed(@NotNull Player player) {
    return this.player.isListed(player);
  }

  public boolean isChunkSent(long l) {
    return player.isChunkSent(l);
  }

  public void playSound(@NotNull Entity entity, @NotNull org.bukkit.Sound sound, @NotNull SoundCategory soundCategory
      , float v,
                        float v1) {
    player.playSound(entity, sound, soundCategory, v, v1);
  }

  @Deprecated
  public void showTitle(@Nullable BaseComponent baseComponent) {
    player.showTitle(baseComponent);
  }

  @Deprecated(forRemoval = true)
  public void setLastDamageCause(@Nullable EntityDamageEvent entityDamageEvent) {
    player.setLastDamageCause(entityDamageEvent);
  }

  public boolean isInsideVehicle() {
    return player.isInsideVehicle();
  }

  @Deprecated
  public void setPlayerListHeaderFooter(@Nullable String s, @Nullable String s1) {
    player.setPlayerListHeaderFooter(s, s1);
  }

  @ApiStatus.Experimental
  public void transfer(@NotNull String s, int i) {
    player.transfer(s, i);
  }

  public void abandonConversation(@NotNull Conversation conversation,
                                  @NotNull ConversationAbandonedEvent conversationAbandonedEvent) {
    player.abandonConversation(conversation, conversationAbandonedEvent);
  }

  public void setSaturation(float v) {
    player.setSaturation(v);
  }

  public boolean isInBubbleColumn() {
    return player.isInBubbleColumn();
  }

  public void setMetadata(@NotNull String s, @NotNull MetadataValue metadataValue) {
    player.setMetadata(s, metadataValue);
  }

  public boolean getAffectsSpawning() {
    return player.getAffectsSpawning();
  }

  public @NotNull PlayerProfile getPlayerProfile() {
    return player.getPlayerProfile();
  }

  public boolean isSneaking() {
    return player.isSneaking();
  }

  public void stopSound(@NotNull SoundCategory soundCategory) {
    player.stopSound(soundCategory);
  }

  @Deprecated
  public void setPlayerListHeaderFooter(@Nullable BaseComponent baseComponent, @Nullable BaseComponent baseComponent1) {
    player.setPlayerListHeaderFooter(baseComponent, baseComponent1);
  }

  public void showPlayer(@NotNull Plugin plugin, @NotNull Player player) {
    this.player.showPlayer(plugin, player);
  }

  public <E extends BanEntry<? super PlayerProfile>> @Nullable E ban(@Nullable String s, @Nullable Date date,
                                                                     @Nullable String s1, boolean b) {
    return player.ban(s, date, s1, b);
  }

  public void sendBlockChange(@NotNull Location location, @NotNull BlockData blockData) {
    player.sendBlockChange(location, blockData);
  }

  public boolean clearActivePotionEffects() {
    return player.clearActivePotionEffects();
  }

  public void setCustomNameVisible(boolean b) {
    player.setCustomNameVisible(b);
  }

  @Nullable
  public org.bukkit.Sound getDeathSound() {
    return player.getDeathSound();
  }

  public boolean listPlayer(@NotNull Player player) {
    return this.player.listPlayer(player);
  }

  public void swingMainHand() {
    player.swingMainHand();
  }

  public boolean beginConversation(@NotNull Conversation conversation) {
    return player.beginConversation(conversation);
  }

  public void setVisualFire(boolean b) {
    player.setVisualFire(b);
  }

  @Contract("null -> null; !null -> !null")
  @Nullable
  public Location getLocation(@Nullable Location location) {
    return player.getLocation(location);
  }

  @Deprecated
  public void sendSignChange(@NotNull Location location, @Nullable String[] strings, @NotNull DyeColor dyeColor,
                             boolean b) throws IllegalArgumentException {
    player.sendSignChange(location, strings, dyeColor, b);
  }

  @Nullable
  public InventoryView openWorkbench(@Nullable Location location, boolean b) {
    return player.openWorkbench(location, b);
  }

  public boolean teleport(@NotNull Location location, @NotNull TeleportFlag @NotNull ... teleportFlags) {
    return player.teleport(location, teleportFlags);
  }

  public void sendOpLevel(byte b) {
    player.sendOpLevel(b);
  }

  public void lookAt(@NotNull Entity entity, @NotNull LookAnchor lookAnchor, @NotNull LookAnchor lookAnchor1) {
    player.lookAt(entity, lookAnchor, lookAnchor1);
  }

  @Deprecated
  @Nullable
  public String getPlayerListHeader() {
    return player.getPlayerListHeader();
  }

  public void kick(@Nullable Component component) {
    player.kick(component);
  }

  @ApiStatus.Obsolete
  public void sendMessage(@NotNull String... strings) {
    player.sendMessage(strings);
  }

  @Deprecated
  public void setResourcePack(@NotNull UUID uuid, @NotNull String s, @Nullable byte[] bytes, @Nullable String s1,
                              boolean b) {
    player.setResourcePack(uuid, s, bytes, s1, b);
  }

  @Contract("-> fail")
  @Deprecated(forRemoval = true, since = "1.20.5")
  @NotNull
  public EntityCategory getCategory() {
    return player.getCategory();
  }

  @Deprecated
  public void sendActionBar(@NotNull BaseComponent... baseComponents) {
    player.sendActionBar(baseComponents);
  }

  public boolean hasActiveItem() {
    return player.hasActiveItem();
  }

  public void damageItemStack(@NotNull EquipmentSlot equipmentSlot, int i) {
    player.damageItemStack(equipmentSlot, i);
  }

  @Nullable
  public Location getOrigin() {
    return player.getOrigin();
  }

  public void setStatistic(@NotNull Statistic statistic, int i) throws IllegalArgumentException {
    player.setStatistic(statistic, i);
  }

  public void giveExp(int i, boolean b) {
    player.giveExp(i, b);
  }

  @NotNull
  public Inventory getEnderChest() {
    return player.getEnderChest();
  }

  public <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> aClass,
                                                            @Nullable Vector vector,
                                                            @Nullable Consumer<? super T> consumer) {
    return player.launchProjectile(aClass, vector, consumer);
  }

  public void sendEquipmentChange(@NotNull LivingEntity livingEntity, @NotNull Map<EquipmentSlot, ItemStack> map) {
    player.sendEquipmentChange(livingEntity, map);
  }

  @Deprecated
  @NotNull
  public String getPlayerListName() {
    return player.getPlayerListName();
  }

  @NotNull
  public TriState permissionValue(@NotNull String permission) {
    return player.permissionValue(permission);
  }

  @Deprecated
  public void setSubtitle(BaseComponent baseComponent) {
    player.setSubtitle(baseComponent);
  }

  @Nullable
  public Block getTargetBlockExact(int i, @NotNull FluidCollisionMode fluidCollisionMode) {
    return player.getTargetBlockExact(i, fluidCollisionMode);
  }

  public float getYaw() {
    return player.getYaw();
  }

  public boolean eject() {
    return player.eject();
  }

  public boolean hasCooldown(@NotNull Material material) {
    return player.hasCooldown(material);
  }

  public int getStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
    return player.getStatistic(statistic, entityType);
  }

  @Nullable
  public InventoryView openSmithingTable(@Nullable Location location, boolean b) {
    return player.openSmithingTable(location, b);
  }

  public void giveExpLevels(int i) {
    player.giveExpLevels(i);
  }

  public void setSprinting(boolean b) {
    player.setSprinting(b);
  }

  @NotNull
  public BoundingBox getBoundingBox() {
    return player.getBoundingBox();
  }

  public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, @Nullable T t) {
    player.spawnParticle(particle, v, v1, v2, i, t);
  }

  @Nullable
  public RayTraceResult rayTraceEntities(int maxDistance) {
    return player.rayTraceEntities(maxDistance);
  }

  public boolean isSwimming() {
    return player.isSwimming();
  }

  public void setCollidable(boolean b) {
    player.setCollidable(b);
  }

  public void setCooldown(@NotNull Material material, int i) {
    player.setCooldown(material, i);
  }

  @ApiStatus.Obsolete(since = "1.20.4")
  @NotNull
  public EquipmentSlot getHandRaised() {
    return player.getHandRaised();
  }

  @NotNull
  public World getWorld() {
    return player.getWorld();
  }

  public boolean hasGravity() {
    return player.hasGravity();
  }

  public void playSound(@NotNull Entity entity, @NotNull String s, float v, float v1) {
    player.playSound(entity, s, v, v1);
  }

  @Deprecated(since = "1.20.1")
  public void removeAdditionalChatCompletions(@NotNull Collection<String> collection) {
    player.removeAdditionalChatCompletions(collection);
  }

  public void setResourcePack(@NotNull String url, @NotNull String hash, boolean required) {
    player.setResourcePack(url, hash, required);
  }

  public double getAbsorptionAmount() {
    return player.getAbsorptionAmount();
  }

  public void wakeup(boolean b) {
    player.wakeup(b);
  }

  public float getExhaustion() {
    return player.getExhaustion();
  }

  public void setStarvationRate(int i) {
    player.setStarvationRate(i);
  }

  @Deprecated
  public void setCustomName(@Nullable String s) {
    player.setCustomName(s);
  }

  public int getNextArrowRemoval() {
    return player.getNextArrowRemoval();
  }

  public int getNoDamageTicks() {
    return player.getNoDamageTicks();
  }

  public boolean hasSeenWinScreen() {
    return player.hasSeenWinScreen();
  }

  @NotNull
  public EntityEquipment getEquipment() {
    return player.getEquipment();
  }

  public void setSleepingIgnored(boolean b) {
    player.setSleepingIgnored(b);
  }

  public void setAllowFlight(boolean b) {
    player.setAllowFlight(b);
  }

  @NotNull
  public Locale locale() {
    return player.locale();
  }

  public void playEffect(@NotNull EntityEffect entityEffect) {
    player.playEffect(entityEffect);
  }

  @Nullable
  public FishHook getFishHook() {
    return player.getFishHook();
  }

  public double getY() {
    return player.getY();
  }

  @Nullable
  public Entity releaseRightShoulderEntity() {
    return player.releaseRightShoulderEntity();
  }

  @NotNull
  public CompletableFuture<Boolean> teleportAsync(@NotNull Location location,
                                                  @NotNull PlayerTeleportEvent.TeleportCause teleportCause,
                                                  @NotNull TeleportFlag @NotNull ... teleportFlags) {
    return player.teleportAsync(location, teleportCause, teleportFlags);
  }

  public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int i) throws IllegalArgumentException {
    player.decrementStatistic(statistic, material, i);
  }

  public void setExperienceLevelAndProgress(@Range(from = 0L, to = 2147483647L) int i) {
    player.setExperienceLevelAndProgress(i);
  }

  public int getShieldBlockingDelay() {
    return player.getShieldBlockingDelay();
  }

  public void playSound(@NotNull Entity entity, @NotNull String s, @NotNull SoundCategory soundCategory, float v,
                        float v1) {
    player.playSound(entity, s, soundCategory, v, v1);
  }

  public void damage(double v, @Nullable Entity entity) {
    player.damage(v, entity);
  }

  public boolean hasPotionEffect(@NotNull PotionEffectType potionEffectType) {
    return player.hasPotionEffect(potionEffectType);
  }

  public int getMaxFreezeTicks() {
    return player.getMaxFreezeTicks();
  }

  public boolean isDeeplySleeping() {
    return player.isDeeplySleeping();
  }

  public int discoverRecipes(@NotNull Collection<NamespacedKey> collection) {
    return player.discoverRecipes(collection);
  }

  public void setPersistent(boolean b) {
    player.setPersistent(b);
  }

  @NotNull
  public PistonMoveReaction getPistonMoveReaction() {
    return player.getPistonMoveReaction();
  }

  @Deprecated
  public void sendMessage(@NotNull Identified source, @NotNull ComponentLike message) {
    player.sendMessage(source, message);
  }

  public void openBook(@NotNull ItemStack itemStack) {
    player.openBook(itemStack);
  }

  @NotNull
  public List<Entity> getNearbyEntities(double v, double v1, double v2) {
    return player.getNearbyEntities(v, v1, v2);
  }

  public void removeResourcePacks(@NotNull ResourcePackRequest request) {
    player.removeResourcePacks(request);
  }

  public void setNextBeeStingerRemoval(@Range(from = 0L, to = 2147483647L) int i) {
    player.setNextBeeStingerRemoval(i);
  }

  @NotNull
  public Set<PermissionAttachmentInfo> getEffectivePermissions() {
    return player.getEffectivePermissions();
  }

  @NotNull
  public org.bukkit.Sound getFallDamageSoundSmall() {
    return player.getFallDamageSoundSmall();
  }

  @NotNull
  public InventoryView getOpenInventory() {
    return player.getOpenInventory();
  }

  @Nullable
  public InetSocketAddress getVirtualHost() {
    return player.getVirtualHost();
  }

  @NotNull
  public Set<String> getListeningPluginChannels() {
    return player.getListeningPluginChannels();
  }

  public void sendMap(@NotNull MapView mapView) {
    player.sendMap(mapView);
  }

  public int getRemainingAir() {
    return player.getRemainingAir();
  }

  public void setResourcePack(@NotNull String url, byte @Nullable [] hash, @Nullable Component prompt, boolean force) {
    player.setResourcePack(url, hash, prompt, force);
  }

  @Deprecated
  public void setPlayerListHeader(@Nullable String s) {
    player.setPlayerListHeader(s);
  }

  public void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
    player.sendPlayerListHeaderAndFooter(header, footer);
  }

  public void showElderGuardian(boolean b) {
    player.showElderGuardian(b);
  }

  @ApiStatus.Experimental
  @NotNull
  @Unmodifiable
  public Set<Chunk> getSentChunks() {
    return player.getSentChunks();
  }

  public void lockFreezeTicks(boolean b) {
    player.lockFreezeTicks(b);
  }

  public void displayName(@Nullable Component component) {
    player.displayName(component);
  }

  public void setBodyYaw(float v) {
    player.setBodyYaw(v);
  }

  @NotNull
  public CompletableFuture<Boolean> teleportAsync(@NotNull Location loc) {
    return player.teleportAsync(loc);
  }

  public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
    player.incrementStatistic(statistic, material);
  }

  public void sendRichMessage(@NotNull String message) {
    player.sendRichMessage(message);
  }

  @Nullable
  public WeatherType getPlayerWeather() {
    return player.getPlayerWeather();
  }

  @Deprecated(forRemoval = true)
  @Nullable
  public Block getTargetBlock(int maxDistance) {
    return player.getTargetBlock(maxDistance);
  }

  public int getLevel() {
    return player.getLevel();
  }

  public void setMaximumAir(int i) {
    player.setMaximumAir(i);
  }

  public @NotNull Duration getIdleDuration() {
    return player.getIdleDuration();
  }

  public void setGlowing(boolean b) {
    player.setGlowing(b);
  }

  public boolean hasFixedPose() {
    return player.hasFixedPose();
  }

  @Deprecated
  @NotNull
  public Set<Player> getTrackedPlayers() {
    return player.getTrackedPlayers();
  }

  public void sendResourcePacks(@NotNull ResourcePackRequest request) {
    player.sendResourcePacks(request);
  }

  @Deprecated
  public void sendMessage(@NotNull BaseComponent... components) {
    player.sendMessage(components);
  }

  public float getFlySpeed() {
    return player.getFlySpeed();
  }

  @NotNull
  public PermissionAttachment addAttachment(@NotNull Plugin plugin) {
    return player.addAttachment(plugin);
  }

  public long getLastSeen() {
    return player.getLastSeen();
  }

  public void playSound(@NotNull Location location, @NotNull String s, @NotNull SoundCategory soundCategory, float v,
                        float v1, long l) {
    player.playSound(location, s, soundCategory, v, v1, l);
  }

  @Nullable
  public Entity getTargetEntity(int i, boolean b) {
    return player.getTargetEntity(i, b);
  }

  @NotNull
  public Component name() {
    return player.name();
  }

  public boolean isConnected() {
    return player.isConnected();
  }

  public void clearTitle() {
    player.clearTitle();
  }

  public <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> aClass,
                                                            @Nullable Vector vector) {
    return player.launchProjectile(aClass, vector);
  }

  public void setKiller(@Nullable Player player) {
    this.player.setKiller(player);
  }

  @Contract("-> null")
  @Deprecated(forRemoval = true)
  @Nullable
  public String getResourcePackHash() {
    return player.getResourcePackHash();
  }

  @Deprecated
  @Nullable
  public BanEntry banPlayerFull(@Nullable String reason, @Nullable Date expires) {
    return player.banPlayerFull(reason, expires);
  }

  public void setViewDistance(int i) {
    player.setViewDistance(i);
  }

  public float getCooledAttackStrength(float v) {
    return player.getCooledAttackStrength(v);
  }

  public void setArrowCooldown(int i) {
    player.setArrowCooldown(i);
  }

  @Deprecated
  public void setResourcePack(@NotNull String s, @Nullable byte[] bytes, @Nullable String s1, boolean b) {
    player.setResourcePack(s, bytes, s1, b);
  }

  @Deprecated
  @NotNull
  public BanEntry banPlayer(@Nullable String reason, @Nullable Date expires, @Nullable String source,
                            boolean kickIfOnline) {
    return player.banPlayer(reason, expires, source, kickIfOnline);
  }

  @Nullable
  public Component customName() {
    return player.customName();
  }

  public int getFireTicks() {
    return player.getFireTicks();
  }

  public void setMaximumNoDamageTicks(int i) {
    player.setMaximumNoDamageTicks(i);
  }

  @Deprecated
  public boolean addPotionEffect(@NotNull PotionEffect potionEffect, boolean b) {
    return player.addPotionEffect(potionEffect, b);
  }

  public int getSendViewDistance() {
    return player.getSendViewDistance();
  }

  @Deprecated
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason, @Nullable String source) {
    return player.banPlayerIP(reason, source);
  }

  public void sendExperienceChange(float v, int i) {
    player.sendExperienceChange(v, i);
  }

  @Deprecated
  public void sendSignChange(@NotNull Location location, @Nullable String[] strings, @NotNull DyeColor dyeColor) throws IllegalArgumentException {
    player.sendSignChange(location, strings, dyeColor);
  }

  public void setWorldBorder(@Nullable WorldBorder worldBorder) {
    player.setWorldBorder(worldBorder);
  }

  public boolean isInvisible() {
    return player.isInvisible();
  }

  @Nullable
  public Player getPlayer() {
    return player.getPlayer();
  }

  @NotNull
  public UUID getUniqueId() {
    return player.getUniqueId();
  }

  @NotNull
  public Set<NamespacedKey> getDiscoveredRecipes() {
    return player.getDiscoveredRecipes();
  }

  @Deprecated
  @NotNull
  public ItemStack getItemInHand() {
    return player.getItemInHand();
  }

  @ApiStatus.Obsolete(since = "1.20.4")
  public int getItemUseRemainingTime() {
    return player.getItemUseRemainingTime();
  }

  public boolean isInWaterOrRainOrBubbleColumn() {
    return player.isInWaterOrRainOrBubbleColumn();
  }

  @Nullable
  public Entity getSpectatorTarget() {
    return player.getSpectatorTarget();
  }

  @Deprecated
  public void setMaxHealth(double v) {
    player.setMaxHealth(v);
  }

  @Deprecated
  public void sendMultiBlockChange(@NotNull Map<? extends Position, BlockData> blockChanges,
                                   boolean suppressLightUpdates) {
    player.sendMultiBlockChange(blockChanges, suppressLightUpdates);
  }

  public boolean hasPlayedBefore() {
    return player.hasPlayedBefore();
  }

  @Nullable
  public InetSocketAddress getHAProxyAddress() {
    return player.getHAProxyAddress();
  }

  public void setGameMode(@NotNull GameMode gameMode) {
    player.setGameMode(gameMode);
  }

  public int undiscoverRecipes(@NotNull Collection<NamespacedKey> collection) {
    return player.undiscoverRecipes(collection);
  }

  @NotNull
  public ItemStack getItemOnCursor() {
    return player.getItemOnCursor();
  }

  public boolean isValid() {
    return player.isValid();
  }

  public boolean addScoreboardTag(@NotNull String s) {
    return player.addScoreboardTag(s);
  }

  public void playSound(@NotNull Sound sound, Sound.Emitter emitter) {
    player.playSound(sound, emitter);
  }

  @Deprecated
  public void playNote(@NotNull Location location, byte b, byte b1) {
    player.playNote(location, b, b1);
  }

  public void removeResourcePack(@NotNull UUID uuid) {
    player.removeResourcePack(uuid);
  }

  public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i) {
    player.spawnParticle(particle, location, i);
  }

  @NotNull
  public AdvancementProgress getAdvancementProgress(@NotNull Advancement advancement) {
    return player.getAdvancementProgress(advancement);
  }

  @ApiStatus.Experimental
  public void sendBlockUpdate(@NotNull Location location, @NotNull TileState tileState) throws IllegalArgumentException {
    player.sendBlockUpdate(location, tileState);
  }

  public int getWardenWarningLevel() {
    return player.getWardenWarningLevel();
  }

  @NotNull
  public org.bukkit.Sound getSwimSound() {
    return player.getSwimSound();
  }

  @Deprecated
  @NotNull
  public BanEntry banPlayer(@Nullable String reason, @Nullable Date expires, @Nullable String source) {
    return player.banPlayer(reason, expires, source);
  }

  public void setResourcePack(@NotNull UUID uuid, @NotNull String url, @NotNull String hash,
                              @Nullable Component resourcePackPrompt, boolean required) {
    player.setResourcePack(uuid, url, hash, resourcePackPrompt, required);
  }

  @NotNull
  public org.bukkit.Sound getSwimHighSpeedSplashSound() {
    return player.getSwimHighSpeedSplashSound();
  }

  @NotNull
  public Chunk getChunk() {
    return player.getChunk();
  }

  public void sendPlayerListHeader(@NotNull Component header) {
    player.sendPlayerListHeader(header);
  }

  @NotNull
  public Location getBedLocation() {
    return player.getBedLocation();
  }

  @NotNull
  public TriState getFrictionState() {
    return player.getFrictionState();
  }

  public void addResourcePack(@NotNull UUID uuid, @NotNull String s, @Nullable byte[] bytes, @Nullable String s1,
                              boolean b) {
    player.addResourcePack(uuid, s, bytes, s1, b);
  }

  @NotNull
  public String getScoreboardEntryName() {
    return player.getScoreboardEntryName();
  }

  public void setWardenWarningCooldown(int i) {
    player.setWardenWarningCooldown(i);
  }

  @Deprecated
  public void hideTitle() {
    player.hideTitle();
  }

  @NotNull
  public MainHand getMainHand() {
    return player.getMainHand();
  }

  public void openInventory(@NotNull InventoryView inventoryView) {
    player.openInventory(inventoryView);
  }

  @ApiStatus.Experimental
  @NotNull
  public CompletableFuture<byte[]> retrieveCookie(@NotNull NamespacedKey namespacedKey) {
    return player.retrieveCookie(namespacedKey);
  }

  @Deprecated
  public void setBedSpawnLocation(@Nullable Location location) {
    player.setBedSpawnLocation(location);
  }

  public int getEntityId() {
    return player.getEntityId();
  }

  @Nullable
  public Entity getVehicle() {
    return player.getVehicle();
  }

  public void playSound(@NotNull Location location, @NotNull org.bukkit.Sound sound, float v, float v1) {
    player.playSound(location, sound, v, v1);
  }

  public void setArrowsInBody(int i, boolean b) {
    player.setArrowsInBody(i, b);
  }

  @Deprecated
  @Nullable
  public BanEntry banPlayerFull(@Nullable String reason, @Nullable Date expires, @Nullable String source) {
    return player.banPlayerFull(reason, expires, source);
  }

  @Contract("_, null -> _; _, !null -> !null")
  public <T> @Nullable T getOrDefault(@NotNull Pointer<T> pointer, @Nullable T defaultValue) {
    return player.getOrDefault(pointer, defaultValue);
  }

  @Deprecated
  public void sendTitle(@Nullable String s, @Nullable String s1) {
    player.sendTitle(s, s1);
  }

  public void setRotation(float v, float v1) {
    player.setRotation(v, v1);
  }

  public int getEnchantmentSeed() {
    return player.getEnchantmentSeed();
  }

  @Deprecated(forRemoval = true)
  @Nullable
  public TargetBlockInfo getTargetBlockInfo(int maxDistance) {
    return player.getTargetBlockInfo(maxDistance);
  }

  public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                            double v2, double v3) {
    player.spawnParticle(particle, location, i, v, v1, v2, v3);
  }

  public void setSilent(boolean b) {
    player.setSilent(b);
  }

  @NotNull
  public Pose getPose() {
    return player.getPose();
  }

  @Deprecated
  public boolean isOnGround() {
    return player.isOnGround();
  }

  public void sendBlockDamage(@NotNull Location location, float v, @NotNull Entity entity) {
    player.sendBlockDamage(location, v, entity);
  }

  @NotNull
  public Location getLocation() {
    return player.getLocation();
  }

  public void sendMessage(@NotNull Component message) {
    player.sendMessage(message);
  }

  public void swingHand(@NotNull EquipmentSlot hand) {
    player.swingHand(hand);
  }

  public boolean isInWaterOrBubbleColumn() {
    return player.isInWaterOrBubbleColumn();
  }

  public <T> @Nullable T getMemory(@NotNull MemoryKey<T> memoryKey) {
    return player.getMemory(memoryKey);
  }

  public void setTicksLived(int i) {
    player.setTicksLived(i);
  }

  @Deprecated
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason, boolean kickPlayer) {
    return player.banPlayerIP(reason, kickPlayer);
  }

  @NotNull
  public TriState hasFlyingFallDamage() {
    return player.hasFlyingFallDamage();
  }

  @NotNull
  public Scoreboard getScoreboard() {
    return player.getScoreboard();
  }

  public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i) {
    player.spawnParticle(particle, v, v1, v2, i);
  }

  public void showElderGuardian() {
    player.showElderGuardian();
  }

  @NotNull
  public org.bukkit.Sound getEatingSound(@NotNull ItemStack itemStack) {
    return player.getEatingSound(itemStack);
  }

  public void setAbsorptionAmount(double v) {
    player.setAbsorptionAmount(v);
  }

  public float getUpwardsMovement() {
    return player.getUpwardsMovement();
  }

  public int getTotalExperience() {
    return player.getTotalExperience();
  }

  @Nullable
  public Location getLastDeathLocation() {
    return player.getLastDeathLocation();
  }

  @Deprecated
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason, @Nullable Date expires) {
    return player.banPlayerIP(reason, expires);
  }

  public void removePotionEffect(@NotNull PotionEffectType potionEffectType) {
    player.removePotionEffect(potionEffectType);
  }

  public void chat(@NotNull String s) {
    player.chat(s);
  }

  public boolean isLeashed() {
    return player.isLeashed();
  }

  public void setVelocity(@NotNull Vector vector) {
    player.setVelocity(vector);
  }

  public boolean breakBlock(@NotNull Block block) {
    return player.breakBlock(block);
  }

  public void sendPlayerListFooter(@NotNull Component footer) {
    player.sendPlayerListFooter(footer);
  }

  public void sendRawMessage(@NotNull String s) {
    player.sendRawMessage(s);
  }

  public void stopSound(@NotNull org.bukkit.Sound sound, @Nullable SoundCategory soundCategory) {
    player.stopSound(sound, soundCategory);
  }

  public void setNoActionTicks(int i) {
    player.setNoActionTicks(i);
  }

  public void setPlayerTime(long l, boolean b) {
    player.setPlayerTime(l, b);
  }

  public int getBeeStingersInBody() {
    return player.getBeeStingersInBody();
  }

  public void setHealthScaled(boolean b) {
    player.setHealthScaled(b);
  }

  @Deprecated(forRemoval = true, since = "1.20.4")
  public void setItemInUseTicks(int i) {
    player.setItemInUseTicks(i);
  }

  public boolean isDead() {
    return player.isDead();
  }

  @Deprecated
  public void sendMessage(@NotNull Identity source, @NotNull ComponentLike message) {
    player.sendMessage(source, message);
  }

  public void showDemoScreen() {
    player.showDemoScreen();
  }

  public void showEntity(@NotNull Plugin plugin, @NotNull Entity entity) {
    player.showEntity(plugin, entity);
  }

  public boolean isSleeping() {
    return player.isSleeping();
  }

  public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int i) {
    player.decrementStatistic(statistic, entityType, i);
  }
}
