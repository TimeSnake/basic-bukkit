/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user;


import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.Title;
import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.entity.LookAnchor;
import io.papermc.paper.entity.TeleportFlag;
import io.papermc.paper.math.Position;
import io.papermc.paper.threadedregions.scheduler.EntityScheduler;
import net.kyori.adventure.audience.Audience;
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
import org.bukkit.event.entity.EntityRegainHealthEvent;
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

  public void playSound(@NotNull Entity entity, @NotNull String s, @NotNull SoundCategory soundCategory, float v,
                        float v1) {
    player.playSound(entity, s, soundCategory, v, v1);
  }

  public int getPortalCooldown() {
    return player.getPortalCooldown();
  }

  public boolean isOnline() {
    return player.isOnline();
  }

  @Deprecated
  public double getMaxHealth() {
    return player.getMaxHealth();
  }

  public boolean beginConversation(@NotNull Conversation conversation) {
    return player.beginConversation(conversation);
  }

  public void setMaximumNoDamageTicks(int i) {
    player.setMaximumNoDamageTicks(i);
  }

  public boolean isInvisible() {
    return player.isInvisible();
  }

  @Deprecated
  public boolean addPotionEffect(@NotNull PotionEffect potionEffect, boolean b) {
    return player.addPotionEffect(potionEffect, b);
  }

  public void sendExperienceChange(float v) {
    player.sendExperienceChange(v);
  }

  public long getLastLogin() {
    return player.getLastLogin();
  }

  public void setSleepingIgnored(boolean b) {
    player.setSleepingIgnored(b);
  }

  public void sendMap(@NotNull MapView mapView) {
    player.sendMap(mapView);
  }

  public void setNoPhysics(boolean b) {
    player.setNoPhysics(b);
  }

  public boolean wouldCollideUsing(@NotNull BoundingBox boundingBox) {
    return player.wouldCollideUsing(boundingBox);
  }

  public void registerAttribute(@NotNull Attribute attribute) {
    player.registerAttribute(attribute);
  }

  public void abandonConversation(@NotNull Conversation conversation,
                                  @NotNull ConversationAbandonedEvent conversationAbandonedEvent) {
    player.abandonConversation(conversation, conversationAbandonedEvent);
  }

  public void setKiller(@Nullable Player player) {
    this.player.setKiller(player);
  }

  public int getStarvationRate() {
    return player.getStarvationRate();
  }

  public void sendOpLevel(byte b) {
    player.sendOpLevel(b);
  }

  public void lookAt(@NotNull Entity entity, @NotNull LookAnchor lookAnchor, @NotNull LookAnchor lookAnchor1) {
    player.lookAt(entity, lookAnchor, lookAnchor1);
  }

  public void setArrowCooldown(int i) {
    player.setArrowCooldown(i);
  }

  public void displayName(@Nullable Component component) {
    player.displayName(component);
  }

  public boolean performCommand(@NotNull String s) {
    return player.performCommand(s);
  }

  public void setCustomChatCompletions(@NotNull Collection<String> collection) {
    player.setCustomChatCompletions(collection);
  }

  public void setWalkSpeed(float v) throws IllegalArgumentException {
    player.setWalkSpeed(v);
  }

  public void setResourcePack(@NotNull UUID uuid, @NotNull String url, @NotNull String hash,
                              @Nullable Component resourcePackPrompt, boolean required) {
    player.setResourcePack(uuid, url, hash, resourcePackPrompt, required);
  }

  public boolean spawnAt(@NotNull Location location, @NotNull CreatureSpawnEvent.SpawnReason spawnReason) {
    return player.spawnAt(location, spawnReason);
  }

  @Deprecated(since = "1.20.4")
  @NotNull
  public BanEntry banPlayer(@Nullable String reason) {
    return player.banPlayer(reason);
  }

  @Nullable
  public InetSocketAddress getHAProxyAddress() {
    return player.getHAProxyAddress();
  }

  @Nullable
  public Entity getTargetEntity(int i, boolean b) {
    return player.getTargetEntity(i, b);
  }

  public void setBodyYaw(float v) {
    player.setBodyYaw(v);
  }

  @Deprecated(since = "1.20.1")
  public void removeAdditionalChatCompletions(@NotNull Collection<String> collection) {
    player.removeAdditionalChatCompletions(collection);
  }

  @Deprecated
  public void sendMessage(@Nullable UUID uuid, @NotNull String... strings) {
    player.sendMessage(uuid, strings);
  }

  public void setGlowing(boolean b) {
    player.setGlowing(b);
  }

  public boolean hasFixedPose() {
    return player.hasFixedPose();
  }

  public void addCustomChatCompletions(@NotNull Collection<String> collection) {
    player.addCustomChatCompletions(collection);
  }

  public boolean canSee(@NotNull Player player) {
    return this.player.canSee(player);
  }

  public double getX() {
    return player.getX();
  }

  public boolean hasMetadata(@NotNull String s) {
    return player.hasMetadata(s);
  }

  public void setMaximumAir(int i) {
    player.setMaximumAir(i);
  }

  public void removeResourcePacks(@NotNull Iterable<UUID> ids) {
    player.removeResourcePacks(ids);
  }

  public void setEnchantmentSeed(int i) {
    player.setEnchantmentSeed(i);
  }

  public void setExp(float v) {
    player.setExp(v);
  }

  public void sendPlayerListHeaderAndFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
    player.sendPlayerListHeaderAndFooter(header, footer);
  }

  public void setStatistic(@NotNull Statistic statistic, @NotNull Material material, int i) throws IllegalArgumentException {
    player.setStatistic(statistic, material, i);
  }

  public void stopSound(@NotNull String s) {
    player.stopSound(s);
  }

  public long getPlayerTimeOffset() {
    return player.getPlayerTimeOffset();
  }

  public void setPose(@NotNull Pose pose) {
    player.setPose(pose);
  }

  @Deprecated
  public void setPlayerListHeaderFooter(@Nullable String s, @Nullable String s1) {
    player.setPlayerListHeaderFooter(s, s1);
  }

  public <E extends BanEntry<? super PlayerProfile>> @Nullable E ban(@Nullable String s, @Nullable Instant instant,
                                                                     @Nullable String s1, boolean b) {
    return player.ban(s, instant, s1, b);
  }

  public boolean hasDiscoveredRecipe(@NotNull NamespacedKey namespacedKey) {
    return player.hasDiscoveredRecipe(namespacedKey);
  }

  public void setVisibleByDefault(boolean b) {
    player.setVisibleByDefault(b);
  }

  public int getRemainingAir() {
    return player.getRemainingAir();
  }

  public void sendMessage(@NotNull ComponentLike message) {
    player.sendMessage(message);
  }

  public void sendPlayerListFooter(@NotNull ComponentLike footer) {
    player.sendPlayerListFooter(footer);
  }

  @NotNull
  public TriState permissionValue(@NotNull String permission) {
    return player.permissionValue(permission);
  }

  public boolean isBlocking() {
    return player.isBlocking();
  }

  @Nullable
  public WorldBorder getWorldBorder() {
    return player.getWorldBorder();
  }

  public void closeInventory(@NotNull InventoryCloseEvent.Reason reason) {
    player.closeInventory(reason);
  }

  @Deprecated
  @NotNull
  public Set<Player> getTrackedPlayers() {
    return player.getTrackedPlayers();
  }

  @Deprecated
  public void kickPlayer(@Nullable String s) {
    player.kickPlayer(s);
  }

  public boolean isInWater() {
    return player.isInWater();
  }

  @Deprecated
  public boolean setPassenger(@NotNull Entity entity) {
    return player.setPassenger(entity);
  }

  @Deprecated
  public void setTexturePack(@NotNull String s) {
    player.setTexturePack(s);
  }

  @Deprecated
  public void sendBlockChange(@NotNull Location location, @NotNull Material material, byte b) {
    player.sendBlockChange(location, material, b);
  }

  @NotNull
  public PersistentDataContainer getPersistentDataContainer() {
    return player.getPersistentDataContainer();
  }

  @Nullable
  public Location getPotentialBedLocation() {
    return player.getPotentialBedLocation();
  }

  @Nullable
  public Entity releaseLeftShoulderEntity() {
    return player.releaseLeftShoulderEntity();
  }

  @NotNull
  public PlayerInventory getInventory() {
    return player.getInventory();
  }

  public void sendMessage(@NotNull Component message, ChatType.Bound boundChatType) {
    player.sendMessage(message, boundChatType);
  }

  public void playSound(@NotNull Entity entity, @NotNull org.bukkit.Sound sound, float v, float v1) {
    player.playSound(entity, sound, v, v1);
  }

  public double getHealthScale() {
    return player.getHealthScale();
  }

  @Nullable
  public InventoryView openGrindstone(@Nullable Location location, boolean b) {
    return player.openGrindstone(location, b);
  }

  public void setNextBeeStingerRemoval(@Range(from = 0L, to = 2147483647L) int i) {
    player.setNextBeeStingerRemoval(i);
  }

  @ApiStatus.Experimental
  @Nullable
  public String getAsString() {
    return player.getAsString();
  }

  public <T> void sendTitlePart(@NotNull TitlePart<T> part, @NotNull T value) {
    player.sendTitlePart(part, value);
  }

  @Deprecated
  @Nullable
  public String getPlayerListHeader() {
    return player.getPlayerListHeader();
  }

  public double getWidth() {
    return player.getWidth();
  }

  public boolean isFrozen() {
    return player.isFrozen();
  }

  public boolean discoverRecipe(@NotNull NamespacedKey namespacedKey) {
    return player.discoverRecipe(namespacedKey);
  }

  @NotNull
  public Locale locale() {
    return player.locale();
  }

  public int getNoActionTicks() {
    return player.getNoActionTicks();
  }

  public boolean getCanPickupItems() {
    return player.getCanPickupItems();
  }

  public float getSidewaysMovement() {
    return player.getSidewaysMovement();
  }

  public void resetTitle() {
    player.resetTitle();
  }

  public void incrementStatistic(@NotNull Statistic statistic, int i) throws IllegalArgumentException {
    player.incrementStatistic(statistic, i);
  }

  public void setRotation(float v, float v1) {
    player.setRotation(v, v1);
  }

  public void setRespawnLocation(@Nullable Location location) {
    player.setRespawnLocation(location);
  }

  public void sendEquipmentChange(@NotNull LivingEntity livingEntity, @NotNull EquipmentSlot equipmentSlot,
                                  @Nullable ItemStack itemStack) {
    player.sendEquipmentChange(livingEntity, equipmentSlot, itemStack);
  }

  @Deprecated
  public void setCustomName(@Nullable String s) {
    player.setCustomName(s);
  }

  public void setResourcePack(@NotNull String url, @NotNull String hash, boolean required,
                              @Nullable Component resourcePackPrompt) {
    player.setResourcePack(url, hash, required, resourcePackPrompt);
  }

  @Deprecated
  public void sendBlockChanges(@NotNull Collection<BlockState> collection, boolean b) {
    player.sendBlockChanges(collection, b);
  }

  public boolean undiscoverRecipe(@NotNull NamespacedKey namespacedKey) {
    return player.undiscoverRecipe(namespacedKey);
  }

  @NotNull
  public List<Block> getLastTwoTargetBlocks(@Nullable Set<Material> set, int i) {
    return player.getLastTwoTargetBlocks(set, i);
  }

  public boolean isRiptiding() {
    return player.isRiptiding();
  }

  public @NotNull org.bukkit.Sound getFallDamageSoundSmall() {
    return player.getFallDamageSoundSmall();
  }

  @Nullable
  public InventoryView openStonecutter(@Nullable Location location, boolean b) {
    return player.openStonecutter(location, b);
  }

  public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
    player.decrementStatistic(statistic, material);
  }

  public void setHasSeenWinScreen(boolean b) {
    player.setHasSeenWinScreen(b);
  }

  public void setWardenWarningCooldown(int i) {
    player.setWardenWarningCooldown(i);
  }

  public void incrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
    player.incrementStatistic(statistic);
  }

  public void sendHurtAnimation(float v) {
    player.sendHurtAnimation(v);
  }

  public boolean isInWaterOrRainOrBubbleColumn() {
    return player.isInWaterOrRainOrBubbleColumn();
  }

  @Deprecated
  public void setPlayerListHeaderFooter(@Nullable BaseComponent[] baseComponents,
                                        @Nullable BaseComponent[] baseComponents1) {
    player.setPlayerListHeaderFooter(baseComponents, baseComponents1);
  }

  public PlayerResourcePackStatusEvent.Status getResourcePackStatus() {
    return player.getResourcePackStatus();
  }

  @Deprecated
  public void sendSignChange(@NotNull Location location, @Nullable List<? extends Component> list,
                             @NotNull DyeColor dyeColor, boolean b) throws IllegalArgumentException {
    player.sendSignChange(location, list, dyeColor, b);
  }

  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerFull(@Nullable String reason, @Nullable String source) {
    return player.banPlayerFull(reason, source);
  }

  public void setLastDeathLocation(@Nullable Location location) {
    player.setLastDeathLocation(location);
  }

  @NotNull
  public Component name() {
    return player.name();
  }

  public void showDemoScreen() {
    player.showDemoScreen();
  }

  @Deprecated
  public long getLastPlayed() {
    return player.getLastPlayed();
  }

  @NotNull
  public UUID getUniqueId() {
    return player.getUniqueId();
  }

  @Deprecated
  @NotNull
  public String getPlayerListName() {
    return player.getPlayerListName();
  }

  public void playSound(@NotNull Entity entity, @NotNull org.bukkit.Sound sound, @NotNull SoundCategory soundCategory
      , float v, float v1, long l) {
    player.playSound(entity, sound, soundCategory, v, v1, l);
  }

  public int getFireTicks() {
    return player.getFireTicks();
  }

  public void removeResourcePacks(@NotNull ResourcePackRequestLike request) {
    player.removeResourcePacks(request);
  }

  @Deprecated
  public void resetMaxHealth() {
    player.resetMaxHealth();
  }

  public void hidePlayer(@NotNull Plugin plugin, @NotNull Player player) {
    this.player.hidePlayer(plugin, player);
  }

  @ApiStatus.Obsolete(since = "1.20.4")
  @NotNull
  public EquipmentSlot getHandRaised() {
    return player.getHandRaised();
  }

  @Nullable
  public RayTraceResult rayTraceEntities(int maxDistance) {
    return player.rayTraceEntities(maxDistance);
  }

  public void showElderGuardian() {
    player.showElderGuardian();
  }

  public void sendResourcePacks(@NotNull ResourcePackRequestLike request) {
    player.sendResourcePacks(request);
  }

  @Deprecated
  public void sendMessage(@NotNull Identity source, @NotNull Component message) {
    player.sendMessage(source, message);
  }

  public void giveExp(int amount) {
    player.giveExp(amount);
  }

  @Nullable
  public Location getRespawnLocation() {
    return player.getRespawnLocation();
  }

  @Nullable
  public Block getTargetBlockExact(int i, @NotNull FluidCollisionMode fluidCollisionMode) {
    return player.getTargetBlockExact(i, fluidCollisionMode);
  }

  public void updateCommands() {
    player.updateCommands();
  }

  public int getProtocolVersion() {
    return player.getProtocolVersion();
  }

  public boolean eject() {
    return player.eject();
  }

  @Deprecated
  public void sendActionBar(char c, @NotNull String s) {
    player.sendActionBar(c, s);
  }

  public void setFlying(boolean b) {
    player.setFlying(b);
  }

  public void damageItemStack(@NotNull EquipmentSlot equipmentSlot, int i) {
    player.damageItemStack(equipmentSlot, i);
  }

  public boolean isSwimming() {
    return player.isSwimming();
  }

  public void setCollidable(boolean b) {
    player.setCollidable(b);
  }

  public boolean hasActiveItem() {
    return player.hasActiveItem();
  }

  @NotNull
  public CompletableFuture<Boolean> teleportAsync(@NotNull Location loc) {
    return player.teleportAsync(loc);
  }

  public void lockFreezeTicks(boolean b) {
    player.lockFreezeTicks(b);
  }

  public int getStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
    return player.getStatistic(statistic);
  }

  @NotNull
  public Component playerListName() {
    return player.playerListName();
  }

  public <T> @NotNull T getClientOption(@NotNull ClientOption<T> clientOption) {
    return player.getClientOption(clientOption);
  }

  public boolean clearActivePotionEffects() {
    return player.clearActivePotionEffects();
  }

  public void swingMainHand() {
    player.swingMainHand();
  }

  public float getYaw() {
    return player.getYaw();
  }

  @Deprecated
  public void setResourcePack(@NotNull String s, @Nullable byte[] bytes) {
    player.setResourcePack(s, bytes);
  }

  @NotNull
  public PermissionAttachment addAttachment(@NotNull Plugin plugin) {
    return player.addAttachment(plugin);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, @Nullable T t) {
    player.spawnParticle(particle, location, i, t);
  }

  public @Nullable org.bukkit.Sound getDeathSound() {
    return player.getDeathSound();
  }

  @Deprecated
  public void setDisplayName(@Nullable String s) {
    player.setDisplayName(s);
  }

  @Nullable
  public Component customName() {
    return player.customName();
  }

  public void clearActiveItem() {
    player.clearActiveItem();
  }

  @Nullable
  public Location getOrigin() {
    return player.getOrigin();
  }

  public float getSaturation() {
    return player.getSaturation();
  }

  @NotNull
  public Map<String, Object> serialize() {
    return player.serialize();
  }

  public void setSimulationDistance(int i) {
    player.setSimulationDistance(i);
  }

  @Deprecated
  public void updateTitle(@NotNull Title title) {
    player.updateTitle(title);
  }

  public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i) {
    player.spawnParticle(particle, v, v1, v2, i);
  }

  @NotNull
  public Set<PermissionAttachmentInfo> getEffectivePermissions() {
    return player.getEffectivePermissions();
  }

  @Deprecated
  public void sendSignChange(@NotNull Location location, @Nullable String[] strings) throws IllegalArgumentException {
    player.sendSignChange(location, strings);
  }

  public void setPlayerTime(long l, boolean b) {
    player.setPlayerTime(l, b);
  }

  @NotNull
  public Scoreboard getScoreboard() {
    return player.getScoreboard();
  }

  public void increaseWardenWarningLevel() {
    player.increaseWardenWarningLevel();
  }

  @Nullable
  public InventoryView openEnchanting(@Nullable Location location, boolean b) {
    return player.openEnchanting(location, b);
  }

  @NotNull
  public List<Entity> getNearbyEntities(double v, double v1, double v2) {
    return player.getNearbyEntities(v, v1, v2);
  }

  public void setPersistent(boolean b) {
    player.setPersistent(b);
  }

  @NotNull
  public PistonMoveReaction getPistonMoveReaction() {
    return player.getPistonMoveReaction();
  }

  public void showEntity(@NotNull Plugin plugin, @NotNull Entity entity) {
    player.showEntity(plugin, entity);
  }

  @NotNull
  public TriState hasFlyingFallDamage() {
    return player.hasFlyingFallDamage();
  }

  public void sendPlainMessage(@NotNull String message) {
    player.sendPlainMessage(message);
  }

  public int getTotalExperience() {
    return player.getTotalExperience();
  }

  public void setHealthScaled(boolean b) {
    player.setHealthScaled(b);
  }

  public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4,
                            double v5) {
    player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5);
  }

  @ApiStatus.Obsolete(since = "1.20.4")
  public int getItemUseRemainingTime() {
    return player.getItemUseRemainingTime();
  }

  @ApiStatus.Experimental
  @NotNull
  @Unmodifiable
  public Set<Long> getSentChunkKeys() {
    return player.getSentChunkKeys();
  }

  public boolean isValid() {
    return player.isValid();
  }

  public @NotNull org.bukkit.Sound getSwimSound() {
    return player.getSwimSound();
  }

  public float getCooldownPeriod() {
    return player.getCooldownPeriod();
  }

  public boolean addScoreboardTag(@NotNull String s) {
    return player.addScoreboardTag(s);
  }

  @NotNull
  public GameMode getGameMode() {
    return player.getGameMode();
  }

  public void setSneaking(boolean b) {
    player.setSneaking(b);
  }

  public void setFoodLevel(int i) {
    player.setFoodLevel(i);
  }

  public void addResourcePack(@NotNull UUID uuid, @NotNull String s, @Nullable byte[] bytes, @Nullable String s1,
                              boolean b) {
    player.addResourcePack(uuid, s, bytes, s1, b);
  }

  public double getAbsorptionAmount() {
    return player.getAbsorptionAmount();
  }

  public void playEffect(@NotNull EntityEffect entityEffect) {
    player.playEffect(entityEffect);
  }

  public int getViewDistance() {
    return player.getViewDistance();
  }

  public void removeResourcePacks(@NotNull ResourcePackInfoLike request,
                                  @NotNull ResourcePackInfoLike @NotNull ... others) {
    player.removeResourcePacks(request, others);
  }

  @Nullable
  public Location getLastDeathLocation() {
    return player.getLastDeathLocation();
  }

  @Deprecated
  public void sendTitle(@Nullable String s, @Nullable String s1) {
    player.sendTitle(s, s1);
  }

  @NotNull
  public ItemStack getActiveItem() {
    return player.getActiveItem();
  }

  public void setArrowsInBody(int count) {
    player.setArrowsInBody(count);
  }

  public void sendMessage(@NotNull SignedMessage signedMessage, ChatType.Bound boundChatType) {
    player.sendMessage(signedMessage, boundChatType);
  }

  @Deprecated(since = "1.20.4")
  @NotNull
  public BanEntry banPlayer(@Nullable String reason, @Nullable Date expires, @Nullable String source) {
    return player.banPlayer(reason, expires, source);
  }

  public void setRespawnLocation(@Nullable Location location, boolean b) {
    player.setRespawnLocation(location, b);
  }

  public boolean hasGravity() {
    return player.hasGravity();
  }

  public <E extends BanEntry<? super PlayerProfile>> @Nullable E ban(@Nullable String s, @Nullable Duration duration,
                                                                     @Nullable String s1, boolean b) {
    return player.ban(s, duration, s1, b);
  }

  @Nullable
  public RayTraceResult rayTraceBlocks(double v) {
    return player.rayTraceBlocks(v);
  }

  public void stopSound(@NotNull String s, @Nullable SoundCategory soundCategory) {
    player.stopSound(s, soundCategory);
  }

  public void setExperienceLevelAndProgress(@Range(from = 0L, to = 2147483647L) int i) {
    player.setExperienceLevelAndProgress(i);
  }

  public boolean hasPlayedBefore() {
    return player.hasPlayedBefore();
  }

  @NotNull
  public World getWorld() {
    return player.getWorld();
  }

  public void setOp(boolean b) {
    player.setOp(b);
  }

  @NotNull
  public CreatureSpawnEvent.SpawnReason getEntitySpawnReason() {
    return player.getEntitySpawnReason();
  }

  @Nullable
  public PermissionAttachment addAttachment(@NotNull Plugin plugin, int i) {
    return player.addAttachment(plugin, i);
  }

  @Deprecated
  @Nullable
  public Entity getShoulderEntityRight() {
    return player.getShoulderEntityRight();
  }

  @NotNull
  public EquipmentSlot getActiveItemHand() {
    return player.getActiveItemHand();
  }

  @Deprecated
  public void sendRawMessage(@Nullable UUID uuid, @NotNull String s) {
    player.sendRawMessage(uuid, s);
  }

  @NotNull
  public BoundingBox getBoundingBox() {
    return player.getBoundingBox();
  }

  public void setItemOnCursor(@Nullable ItemStack itemStack) {
    player.setItemOnCursor(itemStack);
  }

  public boolean isInBubbleColumn() {
    return player.isInBubbleColumn();
  }

  @Nullable
  public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b, int i) {
    return player.addAttachment(plugin, s, b, i);
  }

  public void storeCookie(@NotNull NamespacedKey namespacedKey, @NotNull byte[] bytes) {
    player.storeCookie(namespacedKey, bytes);
  }

  public int getFoodLevel() {
    return player.getFoodLevel();
  }

  public void setResourcePack(@NotNull String url, @NotNull String hash, boolean required) {
    player.setResourcePack(url, hash, required);
  }

  public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, @Nullable T t) {
    player.spawnParticle(particle, v, v1, v2, i, t);
  }

  public void setAllowFlight(boolean b) {
    player.setAllowFlight(b);
  }

  public void setNextArrowRemoval(@Range(from = 0L, to = 2147483647L) int i) {
    player.setNextArrowRemoval(i);
  }

  @Deprecated
  public void sendMessage(@Nullable UUID uuid, @NotNull String s) {
    player.sendMessage(uuid, s);
  }

  public void setPortalCooldown(int i) {
    player.setPortalCooldown(i);
  }

  public boolean isConversing() {
    return player.isConversing();
  }

  public void setLastDamage(double v) {
    player.setLastDamage(v);
  }

  @NotNull
  public Vector getVelocity() {
    return player.getVelocity();
  }

  @Deprecated
  public void sendSignChange(@NotNull Location loc, @Nullable List<? extends Component> lines,
                             boolean hasGlowingText) throws IllegalArgumentException {
    player.sendSignChange(loc, lines, hasGlowingText);
  }

  public int getCooldown(@NotNull Material material) {
    return player.getCooldown(material);
  }

  public boolean teleport(@NotNull Location location, @NotNull TeleportFlag @NotNull ... teleportFlags) {
    return player.teleport(location, teleportFlags);
  }

  public void setVisualFire(boolean b) {
    player.setVisualFire(b);
  }

  @NotNull
  public Component teamDisplayName() {
    return player.teamDisplayName();
  }

  public void setActiveItemRemainingTime(@Range(from = 0L, to = 2147483647L) int i) {
    player.setActiveItemRemainingTime(i);
  }

  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason) {
    return player.banPlayerIP(reason);
  }

  @Deprecated
  public void setArrowsStuck(int i) {
    player.setArrowsStuck(i);
  }

  public void giveExpLevels(int i) {
    player.giveExpLevels(i);
  }

  @NotNull
  public HoverEvent<HoverEvent.ShowEntity> asHoverEvent() {
    return player.asHoverEvent();
  }

  public void playHurtAnimation(float v) {
    player.playHurtAnimation(v);
  }

  public int getMaximumNoDamageTicks() {
    return player.getMaximumNoDamageTicks();
  }

  public void setGliding(boolean b) {
    player.setGliding(b);
  }

  @Contract("null -> null; !null -> !null")
  @Nullable
  public Location getLocation(@Nullable Location location) {
    return player.getLocation(location);
  }

  public void setCustomNameVisible(boolean b) {
    player.setCustomNameVisible(b);
  }

  public boolean hasPermission(@NotNull Permission permission) {
    return player.hasPermission(permission);
  }

  public void heal(double amount) {
    player.heal(amount);
  }

  public int getArrowCooldown() {
    return player.getArrowCooldown();
  }

  public @Nullable Sound getHurtSound() {
    return player.getHurtSound();
  }

  @NotNull
  public Location getCompassTarget() {
    return player.getCompassTarget();
  }

  @NotNull
  public MainHand getMainHand() {
    return player.getMainHand();
  }

  public void giveExp(int i, boolean b) {
    player.giveExp(i, b);
  }

  public void sendPotionEffectChangeRemove(@NotNull LivingEntity livingEntity,
                                           @NotNull PotionEffectType potionEffectType) {
    player.sendPotionEffectChangeRemove(livingEntity, potionEffectType);
  }

  @Deprecated
  public void showTitle(@Nullable BaseComponent[] baseComponents) {
    player.showTitle(baseComponents);
  }

  public void setStatistic(@NotNull Statistic statistic, int i) throws IllegalArgumentException {
    player.setStatistic(statistic, i);
  }

  public void knockback(double v, double v1, double v2) {
    player.knockback(v, v1, v2);
  }

  public boolean isInvulnerable() {
    return player.isInvulnerable();
  }

  @NotNull
  public Collection<PotionEffect> getActivePotionEffects() {
    return player.getActivePotionEffects();
  }

  public boolean teleport(@NotNull Location location, @NotNull PlayerTeleportEvent.TeleportCause teleportCause) {
    return player.teleport(location, teleportCause);
  }

  public boolean isInsideVehicle() {
    return player.isInsideVehicle();
  }

  public void playSound(@NotNull Entity entity, @NotNull String s, @NotNull SoundCategory soundCategory, float v,
                        float v1, long l) {
    player.playSound(entity, s, soundCategory, v, v1, l);
  }

  public int getEnchantmentSeed() {
    return player.getEnchantmentSeed();
  }

  public void sendRichMessage(@NotNull String message, @NotNull TagResolver... resolvers) {
    player.sendRichMessage(message, resolvers);
  }

  public void clearResourcePacks() {
    player.clearResourcePacks();
  }

  @Deprecated
  public void setResourcePack(@NotNull UUID uuid, @NotNull String s, @Nullable byte[] bytes, @Nullable String s1,
                              boolean b) {
    player.setResourcePack(uuid, s, bytes, s1, b);
  }

  public void kick(@Nullable Component component, PlayerKickEvent.Cause cause) {
    player.kick(component, cause);
  }

  public void openInventory(@NotNull InventoryView inventoryView) {
    player.openInventory(inventoryView);
  }

  @UnmodifiableView
  @NotNull
  public Iterable<? extends BossBar> activeBossBars() {
    return player.activeBossBars();
  }

  @Deprecated
  public void setSubtitle(BaseComponent[] baseComponents) {
    player.setSubtitle(baseComponents);
  }

  @Nullable
  public PotionEffect getPotionEffect(@NotNull PotionEffectType potionEffectType) {
    return player.getPotionEffect(potionEffectType);
  }

  public float getBodyYaw() {
    return player.getBodyYaw();
  }

  public <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> aClass) {
    return player.launchProjectile(aClass);
  }

  @Nullable
  public Player getPlayer() {
    return player.getPlayer();
  }

  @Deprecated
  public void sendSignChange(@NotNull Location loc, @Nullable List<? extends Component> lines,
                             @NotNull DyeColor dyeColor) throws IllegalArgumentException {
    player.sendSignChange(loc, lines, dyeColor);
  }

  @NotNull
  public CompletableFuture<Boolean> teleportAsync(@NotNull Location location,
                                                  @NotNull PlayerTeleportEvent.TeleportCause teleportCause,
                                                  @NotNull TeleportFlag @NotNull ... teleportFlags) {
    return player.teleportAsync(location, teleportCause, teleportFlags);
  }

  public int getMaximumAir() {
    return player.getMaximumAir();
  }

  public @NotNull Sound getDrinkingSound(@NotNull ItemStack itemStack) {
    return player.getDrinkingSound(itemStack);
  }

  public int getMaxFreezeTicks() {
    return player.getMaxFreezeTicks();
  }

  public double getY() {
    return player.getY();
  }

  public boolean listPlayer(@NotNull Player player) {
    return this.player.listPlayer(player);
  }

  public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3,
                                double v4, double v5, double v6, @Nullable T t) {
    player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6, t);
  }

  public boolean addPotionEffects(@NotNull Collection<PotionEffect> collection) {
    return player.addPotionEffects(collection);
  }

  public void setRemoveWhenFarAway(boolean b) {
    player.setRemoveWhenFarAway(b);
  }

  public void sendBlockDamage(@NotNull Location location, float v, int i) {
    player.sendBlockDamage(location, v, i);
  }

  @NotNull
  public Location getBedLocation() {
    return player.getBedLocation();
  }

  @Deprecated(since = "1.20.4")
  @NotNull
  public BanEntry banPlayer(@Nullable String reason, @Nullable Date expires, @Nullable String source,
                            boolean kickIfOnline) {
    return player.banPlayer(reason, expires, source, kickIfOnline);
  }

  public void showBossBar(@NotNull BossBar bar) {
    player.showBossBar(bar);
  }

  public void openBook(Book.Builder book) {
    player.openBook(book);
  }

  @Deprecated
  public void openSign(@NotNull Sign sign) {
    player.openSign(sign);
  }

  public boolean dropItem(boolean b) {
    return player.dropItem(b);
  }

  public void showPlayer(@NotNull Plugin plugin, @NotNull Player player) {
    this.player.showPlayer(plugin, player);
  }

  public boolean isListed(@NotNull Player player) {
    return this.player.isListed(player);
  }

  public void setJumping(boolean b) {
    player.setJumping(b);
  }

  public boolean isPlayerTimeRelative() {
    return player.isPlayerTimeRelative();
  }

  public void setHurtDirection(float v) {
    player.setHurtDirection(v);
  }

  public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory soundCategory,
                        float v, float v1) {
    player.playSound(location, sound, soundCategory, v, v1);
  }

  public boolean hasLineOfSight(@NotNull Location location) {
    return player.hasLineOfSight(location);
  }

  public void setWardenTimeSinceLastWarning(int i) {
    player.setWardenTimeSinceLastWarning(i);
  }

  public void saveData() {
    player.saveData();
  }

  public void recalculatePermissions() {
    player.recalculatePermissions();
  }

  public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int i) throws IllegalArgumentException {
    player.decrementStatistic(statistic, material, i);
  }

  public boolean teleport(@NotNull Entity entity, @NotNull PlayerTeleportEvent.TeleportCause teleportCause) {
    return player.teleport(entity, teleportCause);
  }

  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason, @Nullable Date expires, boolean kickPlayer) {
    return player.banPlayerIP(reason, expires, kickPlayer);
  }

  @Nullable
  public Entity getSpectatorTarget() {
    return player.getSpectatorTarget();
  }

  @Deprecated
  public void setItemInHand(@Nullable ItemStack itemStack) {
    player.setItemInHand(itemStack);
  }

  public boolean setLeashHolder(@Nullable Entity entity) {
    return player.setLeashHolder(entity);
  }

  public <T> void setMemory(@NotNull MemoryKey<T> memoryKey, @Nullable T t) {
    player.setMemory(memoryKey, t);
  }

  @NotNull
  public String getName() {
    return player.getName();
  }

  public void sendMultiBlockChange(@NotNull Map<? extends Position, BlockData> map) {
    player.sendMultiBlockChange(map);
  }

  public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3,
                                double v4, double v5, @Nullable T t) {
    player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, t);
  }

  public int getClientViewDistance() {
    return player.getClientViewDistance();
  }

  public boolean isPermissionSet(@NotNull Permission permission) {
    return player.isPermissionSet(permission);
  }

  @Deprecated
  public void setNoTickViewDistance(int viewDistance) {
    player.setNoTickViewDistance(viewDistance);
  }

  @NotNull
  public <T> Optional<T> get(@NotNull Pointer<T> pointer) {
    return player.get(pointer);
  }

  @Deprecated
  public void sendMessage(@NotNull BaseComponent component) {
    player.sendMessage(component);
  }

  public boolean isTransferred() {
    return player.isTransferred();
  }

  @Nullable
  public BanEntry<InetAddress> banIp(@Nullable String s, @Nullable Date date, @Nullable String s1, boolean b) {
    return player.banIp(s, date, s1, b);
  }

  public void removeResourcePack(@NotNull UUID uuid) {
    player.removeResourcePack(uuid);
  }

  public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i) {
    player.spawnParticle(particle, location, i);
  }

  public void setAI(boolean b) {
    player.setAI(b);
  }

  @NotNull
  public Server getServer() {
    return player.getServer();
  }

  @Deprecated
  @NotNull
  public String getLocale() {
    return player.getLocale();
  }

  public void playSound(@NotNull net.kyori.adventure.sound.Sound sound) {
    player.playSound(sound);
  }

  public void sendResourcePacks(@NotNull ResourcePackInfoLike first, @NotNull ResourcePackInfoLike... others) {
    player.sendResourcePacks(first, others);
  }

  public int getWardenWarningCooldown() {
    return player.getWardenWarningCooldown();
  }

  @ApiStatus.Experimental
  @NotNull
  public Entity copy(@NotNull Location location) {
    return player.copy(location);
  }

  public void damage(double v, @Nullable Entity entity) {
    player.damage(v, entity);
  }

  @NotNull
  public Set<UUID> getCollidableExemptions() {
    return player.getCollidableExemptions();
  }

  @Nullable
  public InventoryView openMerchant(@NotNull Merchant merchant, boolean b) {
    return player.openMerchant(merchant, b);
  }

  public void setPose(@NotNull Pose pose, boolean b) {
    player.setPose(pose, b);
  }

  public void stopAllSounds() {
    player.stopAllSounds();
  }

  public void setFrictionState(@NotNull TriState triState) {
    player.setFrictionState(triState);
  }

  public boolean isInLava() {
    return player.isInLava();
  }

  @Deprecated
  public void showTitle(@Nullable BaseComponent[] baseComponents, @Nullable BaseComponent[] baseComponents1, int i,
                        int i1, int i2) {
    player.showTitle(baseComponents, baseComponents1, i, i1, i2);
  }

  public void sendExperienceChange(float v, int i) {
    player.sendExperienceChange(v, i);
  }

  @Nullable
  public InventoryView openInventory(@NotNull Inventory inventory) {
    return player.openInventory(inventory);
  }

  @ApiStatus.Obsolete
  public void sendMessage(@NotNull String s) {
    player.sendMessage(s);
  }

  @Deprecated
  public void setResourcePack(@NotNull String s, @Nullable byte[] bytes, @Nullable String s1, boolean b) {
    player.setResourcePack(s, bytes, s1, b);
  }

  public double getEyeHeight(boolean b) {
    return player.getEyeHeight(b);
  }

  public void setWorldBorder(@Nullable WorldBorder worldBorder) {
    player.setWorldBorder(worldBorder);
  }

  public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4,
                            double v5, double v6) {
    player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6);
  }

  public void broadcastSlotBreak(@NotNull EquipmentSlot equipmentSlot, @NotNull Collection<Player> collection) {
    player.broadcastSlotBreak(equipmentSlot, collection);
  }

  public void stopSound(@NotNull Sound sound) {
    player.stopSound(sound);
  }

  public void sendLinks(@NotNull ServerLinks serverLinks) {
    player.sendLinks(serverLinks);
  }

  @NotNull
  public Player.Spigot spigot() {
    return player.spigot();
  }

  public boolean hasNoPhysics() {
    return player.hasNoPhysics();
  }

  public <E extends BanEntry<? super PlayerProfile>> @Nullable E ban(@Nullable String s, @Nullable Duration duration,
                                                                     @Nullable String s1) {
    return player.ban(s, duration, s1);
  }

  @Nullable
  public AttributeInstance getAttribute(@NotNull Attribute attribute) {
    return player.getAttribute(attribute);
  }

  public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T t) {
    player.playEffect(location, effect, t);
  }

  public int undiscoverRecipes(@NotNull Collection<NamespacedKey> collection) {
    return player.undiscoverRecipes(collection);
  }

  @NotNull
  public ItemStack getItemOnCursor() {
    return player.getItemOnCursor();
  }

  public void sendPlayerListHeader(@NotNull Component header) {
    player.sendPlayerListHeader(header);
  }

  public int getExpCooldown() {
    return player.getExpCooldown();
  }

  @Deprecated
  public void hidePlayer(@NotNull Player player) {
    this.player.hidePlayer(player);
  }

  public void setScoreboard(@NotNull Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
    player.setScoreboard(scoreboard);
  }

  public int getMaxFireTicks() {
    return player.getMaxFireTicks();
  }

  public boolean fromMobSpawner() {
    return player.fromMobSpawner();
  }

  @Deprecated
  public void setPlayerListName(@Nullable String s) {
    player.setPlayerListName(s);
  }

  public void chat(@NotNull String s) {
    player.chat(s);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                                double v2, double v3, @Nullable T t, boolean b) {
    player.spawnParticle(particle, location, i, v, v1, v2, v3, t, b);
  }

  @Deprecated(since = "1.20.1")
  public void addAdditionalChatCompletions(@NotNull Collection<String> collection) {
    player.addAdditionalChatCompletions(collection);
  }

  @NotNull
  public TriState getFrictionState() {
    return player.getFrictionState();
  }

  @Deprecated(since = "1.20.4")
  @NotNull
  public BanEntry banPlayer(@Nullable String reason, @Nullable Date expires) {
    return player.banPlayer(reason, expires);
  }

  public void stopSound(@NotNull Sound sound, @Nullable SoundCategory soundCategory) {
    player.stopSound(sound, soundCategory);
  }

  @NotNull
  public BlockFace getFacing() {
    return player.getFacing();
  }

  public void setResourcePack(@NotNull UUID uuid, @NotNull String s, byte @Nullable [] bytes,
                              @Nullable Component component, boolean b) {
    player.setResourcePack(uuid, s, bytes, component, b);
  }

  public boolean breakBlock(@NotNull Block block) {
    return player.breakBlock(block);
  }

  @NotNull
  public Set<NamespacedKey> getDiscoveredRecipes() {
    return player.getDiscoveredRecipes();
  }

  @Deprecated
  public void setResourcePack(@NotNull String s, @Nullable byte[] bytes, boolean b) {
    player.setResourcePack(s, bytes, b);
  }

  @ApiStatus.Obsolete(since = "1.20.4")
  public int getHandRaisedTime() {
    return player.getHandRaisedTime();
  }

  public boolean canUseEquipmentSlot(@NotNull EquipmentSlot equipmentSlot) {
    return player.canUseEquipmentSlot(equipmentSlot);
  }

  public double getHealth() {
    return player.getHealth();
  }

  public void sendActionBar(@NotNull Component message) {
    player.sendActionBar(message);
  }

  public void showTitle(net.kyori.adventure.title.@NotNull Title title) {
    player.showTitle(title);
  }

  public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                            double v2) {
    player.spawnParticle(particle, location, i, v, v1, v2);
  }

  public void decrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
    player.decrementStatistic(statistic);
  }

  public void setSendViewDistance(int i) {
    player.setSendViewDistance(i);
  }

  public boolean isGlowing() {
    return player.isGlowing();
  }

  public void setFlySpeed(float v) throws IllegalArgumentException {
    player.setFlySpeed(v);
  }

  public float getFallDistance() {
    return player.getFallDistance();
  }

  public @NotNull Sound getSwimSplashSound() {
    return player.getSwimSplashSound();
  }

  public double getLastDamage() {
    return player.getLastDamage();
  }

  @Deprecated
  public void setSwimming(boolean b) {
    player.setSwimming(b);
  }

  public int getActiveItemUsedTime() {
    return player.getActiveItemUsedTime();
  }

  @NotNull
  public InventoryView getOpenInventory() {
    return player.getOpenInventory();
  }

  public void setGameMode(@NotNull GameMode gameMode) {
    player.setGameMode(gameMode);
  }

  public void deleteMessage(SignedMessage.Signature signature) {
    player.deleteMessage(signature);
  }

  public void sendHealthUpdate() {
    player.sendHealthUpdate();
  }

  public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory soundCategory,
                        float v, float v1, long l) {
    player.playSound(location, sound, soundCategory, v, v1, l);
  }

  public float getHurtDirection() {
    return player.getHurtDirection();
  }

  public boolean isInWaterOrRain() {
    return player.isInWaterOrRain();
  }

  public boolean isWhitelisted() {
    return player.isWhitelisted();
  }

  @Deprecated
  public void setBedSpawnLocation(@Nullable Location location, boolean b) {
    player.setBedSpawnLocation(location, b);
  }

  public void remove() {
    player.remove();
  }

  public void sendBlockChanges(@NotNull Collection<BlockState> collection) {
    player.sendBlockChanges(collection);
  }

  public int discoverRecipes(@NotNull Collection<NamespacedKey> collection) {
    return player.discoverRecipes(collection);
  }

  @Deprecated
  public void setTitleTimes(int i, int i1, int i2) {
    player.setTitleTimes(i, i1, i2);
  }

  public void sendPotionEffectChange(@NotNull LivingEntity livingEntity, @NotNull PotionEffect potionEffect) {
    player.sendPotionEffectChange(livingEntity, potionEffect);
  }

  public void closeInventory() {
    player.closeInventory();
  }

  public boolean isCustomNameVisible() {
    return player.isCustomNameVisible();
  }

  @Deprecated
  public void setResourcePack(@NotNull String s) {
    player.setResourcePack(s);
  }

  public void setResourcePack(@NotNull String url, @NotNull String hash) {
    player.setResourcePack(url, hash);
  }

  @Deprecated
  public void sendActionBar(@NotNull String s) {
    player.sendActionBar(s);
  }

  @Deprecated
  public void setMaxHealth(double v) {
    player.setMaxHealth(v);
  }

  @Nullable
  public InventoryView openLoom(@Nullable Location location, boolean b) {
    return player.openLoom(location, b);
  }

  public void setWardenWarningLevel(int i) {
    player.setWardenWarningLevel(i);
  }

  public <E extends BanEntry<? super PlayerProfile>> @Nullable E ban(@Nullable String s, @Nullable Instant instant,
                                                                     @Nullable String s1) {
    return player.ban(s, instant, s1);
  }

  @Deprecated
  public void sendSignChange(@NotNull Location loc, @Nullable List<? extends Component> lines) throws IllegalArgumentException {
    player.sendSignChange(loc, lines);
  }

  public void setExhaustion(float v) {
    player.setExhaustion(v);
  }

  @Nullable
  public Player getKiller() {
    return player.getKiller();
  }

  public void sendResourcePacks(@NotNull ResourcePackRequest request) {
    player.sendResourcePacks(request);
  }

  public int getSleepTicks() {
    return player.getSleepTicks();
  }

  @NotNull
  public List<Block> getLineOfSight(@Nullable Set<Material> set, int i) {
    return player.getLineOfSight(set, i);
  }

  public void kick() {
    player.kick();
  }

  public int getExpToLevel() {
    return player.getExpToLevel();
  }

  public void setHealthScale(double v) throws IllegalArgumentException {
    player.setHealthScale(v);
  }

  @Nullable
  public Block getTargetBlockExact(int i) {
    return player.getTargetBlockExact(i);
  }

  public void playNote(@NotNull Location location, @NotNull Instrument instrument, @NotNull Note note) {
    player.playNote(location, instrument, note);
  }

  public double getHeight() {
    return player.getHeight();
  }

  public void setPlayerProfile(@NotNull PlayerProfile playerProfile) {
    player.setPlayerProfile(playerProfile);
  }

  public boolean isUnderWater() {
    return player.isUnderWater();
  }

  public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
    player.decrementStatistic(statistic, entityType);
  }

  @Deprecated
  public void sendTitle(@NotNull Title title) {
    player.sendTitle(title);
  }

  public float getAttackCooldown() {
    return player.getAttackCooldown();
  }

  public boolean sleep(@NotNull Location location, boolean b) {
    return player.sleep(location, b);
  }

  public void resetPlayerTime() {
    player.resetPlayerTime();
  }

  public int getWardenTimeSinceLastWarning() {
    return player.getWardenTimeSinceLastWarning();
  }

  public double getZ() {
    return player.getZ();
  }

  @NotNull
  public Set<Player> getTrackedBy() {
    return player.getTrackedBy();
  }

  public boolean isSprinting() {
    return player.isSprinting();
  }

  public void setAffectsSpawning(boolean b) {
    player.setAffectsSpawning(b);
  }

  public int getExperiencePointsNeededForNextLevel() {
    return player.getExperiencePointsNeededForNextLevel();
  }

  public void setGravity(boolean b) {
    player.setGravity(b);
  }

  public void lookAt(@NotNull Position position, @NotNull LookAnchor playerAnchor) {
    player.lookAt(position, playerAnchor);
  }

  public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
    player.incrementStatistic(statistic, material);
  }

  public long getLastSeen() {
    return player.getLastSeen();
  }

  public boolean isConnected() {
    return player.isConnected();
  }

  public void loadData() {
    player.loadData();
  }

  public void setUnsaturatedRegenRate(int i) {
    player.setUnsaturatedRegenRate(i);
  }

  public void setInvisible(boolean b) {
    player.setInvisible(b);
  }

  public boolean isJumping() {
    return player.isJumping();
  }

  public void sendMessage(@NotNull ComponentLike message, ChatType.Bound boundChatType) {
    player.sendMessage(message, boundChatType);
  }

  @Nullable
  public InetSocketAddress getAddress() {
    return player.getAddress();
  }

  public void attack(@NotNull Entity entity) {
    player.attack(entity);
  }

  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason, boolean kickPlayer) {
    return player.banPlayerIP(reason, kickPlayer);
  }

  public void setFlyingFallDamage(@NotNull TriState triState) {
    player.setFlyingFallDamage(triState);
  }

  @Deprecated
  @Nullable
  public Location getBedSpawnLocation() {
    return player.getBedSpawnLocation();
  }

  @NotNull
  public Component displayName() {
    return player.displayName();
  }

  public float getForwardsMovement() {
    return player.getForwardsMovement();
  }

  public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int i) throws IllegalArgumentException {
    player.incrementStatistic(statistic, material, i);
  }

  public boolean removeScoreboardTag(@NotNull String s) {
    return player.removeScoreboardTag(s);
  }

  public int getPing() {
    return player.getPing();
  }

  public boolean isInPowderedSnow() {
    return player.isInPowderedSnow();
  }

  public <E extends BanEntry<? super PlayerProfile>> @Nullable E ban(@Nullable String s, @Nullable Date date,
                                                                     @Nullable String s1) {
    return player.ban(s, date, s1);
  }

  @NotNull
  public CompletableFuture<byte[]> retrieveCookie(@NotNull NamespacedKey namespacedKey) {
    return player.retrieveCookie(namespacedKey);
  }

  public boolean isClimbing() {
    return player.isClimbing();
  }

  public boolean isCollidable() {
    return player.isCollidable();
  }

  @Nullable
  public InventoryView openAnvil(@Nullable Location location, boolean b) {
    return player.openAnvil(location, b);
  }

  public float getExp() {
    return player.getExp();
  }

  @Nullable
  public Firework fireworkBoost(@NotNull ItemStack itemStack) {
    return player.fireworkBoost(itemStack);
  }

  public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                            double v2, double v3) {
    player.spawnParticle(particle, location, i, v, v1, v2, v3);
  }

  public boolean isVisualFire() {
    return player.isVisualFire();
  }

  public void setStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int i) {
    player.setStatistic(statistic, entityType, i);
  }

  @Deprecated
  public void playEffect(@NotNull Location location, @NotNull Effect effect, int i) {
    player.playEffect(location, effect, i);
  }

  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason, @Nullable Date expires) {
    return player.banPlayerIP(reason, expires);
  }

  @ApiStatus.Obsolete(since = "1.20.4")
  public boolean isHandRaised() {
    return player.isHandRaised();
  }

  public void sendBlockDamage(@NotNull Location location, float v) {
    player.sendBlockDamage(location, v);
  }

  @Nullable
  public InventoryView openCartographyTable(@Nullable Location location, boolean b) {
    return player.openCartographyTable(location, b);
  }

  @NotNull
  public Location getEyeLocation() {
    return player.getEyeLocation();
  }

  @Deprecated
  public void hideTitle() {
    player.hideTitle();
  }

  public long getPlayerTime() {
    return player.getPlayerTime();
  }

  public void clearTitle() {
    player.clearTitle();
  }

  public <T> @UnknownNullability T getOrDefaultFrom(@NotNull Pointer<T> pointer,
                                                    @NotNull Supplier<? extends T> defaultValue) {
    return player.getOrDefaultFrom(pointer, defaultValue);
  }

  public void setRemainingAir(int i) {
    player.setRemainingAir(i);
  }

  public void setFireTicks(int i) {
    player.setFireTicks(i);
  }

  public boolean isTicking() {
    return player.isTicking();
  }

  public void sendPluginMessage(@NotNull Plugin plugin, @NotNull String s, @NotNull byte[] bytes) {
    player.sendPluginMessage(plugin, s, bytes);
  }

  @NotNull
  public EntityEquipment getEquipment() {
    return player.getEquipment();
  }

  public boolean isFreezeTickingLocked() {
    return player.isFreezeTickingLocked();
  }

  public void setSpectatorTarget(@Nullable Entity entity) {
    player.setSpectatorTarget(entity);
  }

  public boolean hasAI() {
    return player.hasAI();
  }

  @Deprecated
  @NotNull
  public String getDisplayName() {
    return player.getDisplayName();
  }

  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerFull(@Nullable String reason, @Nullable Date expires, @Nullable String source) {
    return player.banPlayerFull(reason, expires, source);
  }

  @Deprecated
  public void setShoulderEntityLeft(@Nullable Entity entity) {
    player.setShoulderEntityLeft(entity);
  }

  public boolean isInWorld() {
    return player.isInWorld();
  }

  public void setHealth(double v) {
    player.setHealth(v);
  }

  @Nullable
  public EntityDamageEvent getLastDamageCause() {
    return player.getLastDamageCause();
  }

  @Nullable
  public RayTraceResult rayTraceBlocks(double v, @NotNull FluidCollisionMode fluidCollisionMode) {
    return player.rayTraceBlocks(v, fluidCollisionMode);
  }

  public float getWalkSpeed() {
    return player.getWalkSpeed();
  }

  @Deprecated
  public int getNoTickViewDistance() {
    return player.getNoTickViewDistance();
  }

  public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int i) throws IllegalArgumentException {
    player.incrementStatistic(statistic, entityType, i);
  }

  @Nullable
  public InventoryView openSmithingTable(@Nullable Location location, boolean b) {
    return player.openSmithingTable(location, b);
  }

  public void wakeup(boolean b) {
    player.wakeup(b);
  }

  public boolean hasSeenWinScreen() {
    return player.hasSeenWinScreen();
  }

  public void setLevel(int i) {
    player.setLevel(i);
  }

  public void setWhitelisted(boolean b) {
    player.setWhitelisted(b);
  }

  public boolean isSleeping() {
    return player.isSleeping();
  }

  public void swingHand(@NotNull EquipmentSlot hand) {
    player.swingHand(hand);
  }

  public boolean hasCooldown(@NotNull Material material) {
    return player.hasCooldown(material);
  }

  public void setInvulnerable(boolean b) {
    player.setInvulnerable(b);
  }

  public void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
    player.sendPlayerListHeaderAndFooter(header, footer);
  }

  @ApiStatus.Experimental
  public void damage(double v, @NotNull DamageSource damageSource) {
    player.damage(v, damageSource);
  }

  public void setFallDistance(float v) {
    player.setFallDistance(v);
  }

  public <T> @Nullable T getMemory(@NotNull MemoryKey<T> memoryKey) {
    return player.getMemory(memoryKey);
  }

  public float getUpwardsMovement() {
    return player.getUpwardsMovement();
  }

  public boolean isPersistent() {
    return player.isPersistent();
  }

  public int getUnsaturatedRegenRate() {
    return player.getUnsaturatedRegenRate();
  }

  public @NotNull Sound getEatingSound(@NotNull ItemStack itemStack) {
    return player.getEatingSound(itemStack);
  }

  public void setVelocity(@NotNull Vector vector) {
    player.setVelocity(vector);
  }

  public void setCooldown(@NotNull Material material, int i) {
    player.setCooldown(material, i);
  }

  public void playerListName(@Nullable Component component) {
    player.playerListName(component);
  }

  public boolean isDead() {
    return player.isDead();
  }

  @Deprecated
  public void showPlayer(@NotNull Player player) {
    this.player.showPlayer(player);
  }

  public void playSound(@NotNull Location location, @NotNull String s, @NotNull SoundCategory soundCategory, float v,
                        float v1, long l) {
    player.playSound(location, s, soundCategory, v, v1, l);
  }

  public void setMetadata(@NotNull String s, @NotNull MetadataValue metadataValue) {
    player.setMetadata(s, metadataValue);
  }

  public <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> aClass,
                                                            @Nullable Vector vector,
                                                            @Nullable Consumer<? super T> consumer) {
    return player.launchProjectile(aClass, vector, consumer);
  }

  public void setArrowsInBody(int i, boolean b) {
    player.setArrowsInBody(i, b);
  }

  @Nullable
  public Component playerListFooter() {
    return player.playerListFooter();
  }

  public void setCompassTarget(@NotNull Location location) {
    player.setCompassTarget(location);
  }

  public void openSign(@NotNull Sign sign, @NotNull Side side) {
    player.openSign(sign, side);
  }

  public void removeResourcePacks(@NotNull ResourcePackRequest request) {
    player.removeResourcePacks(request);
  }

  @Deprecated
  public void setSubtitle(BaseComponent baseComponent) {
    player.setSubtitle(baseComponent);
  }

  public int getBeeStingersInBody() {
    return player.getBeeStingersInBody();
  }

  public void setNoActionTicks(int i) {
    player.setNoActionTicks(i);
  }

  public boolean isLeashed() {
    return player.isLeashed();
  }

  @Nullable
  public GameMode getPreviousGameMode() {
    return player.getPreviousGameMode();
  }

  public void removePotionEffect(@NotNull PotionEffectType potionEffectType) {
    player.removePotionEffect(potionEffectType);
  }

  @Deprecated
  public void sendMessage(@NotNull Identified source, @NotNull ComponentLike message) {
    player.sendMessage(source, message);
  }

  @NotNull
  public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b) {
    return player.addAttachment(plugin, s, b);
  }

  public boolean isAllowingServerListings() {
    return player.isAllowingServerListings();
  }

  @Deprecated
  public void sendSignChange(@NotNull Location location, @Nullable String[] strings, @NotNull DyeColor dyeColor,
                             boolean b) throws IllegalArgumentException {
    player.sendSignChange(location, strings, dyeColor, b);
  }

  @Deprecated
  public void sendActionBar(@NotNull BaseComponent... baseComponents) {
    player.sendActionBar(baseComponents);
  }

  public void resetPlayerWeather() {
    player.resetPlayerWeather();
  }

  @Nullable
  public InventoryView openMerchant(@NotNull Villager villager, boolean b) {
    return player.openMerchant(villager, b);
  }

  @Deprecated
  public void setShoulderEntityRight(@Nullable Entity entity) {
    player.setShoulderEntityRight(entity);
  }

  public void setResourcePack(@NotNull String url, byte @Nullable [] hash, @Nullable Component prompt) {
    player.setResourcePack(url, hash, prompt);
  }

  public int getNoDamageTicks() {
    return player.getNoDamageTicks();
  }

  public boolean hasPotionEffect(@NotNull PotionEffectType potionEffectType) {
    return player.hasPotionEffect(potionEffectType);
  }

  @ApiStatus.Experimental
  @NotNull
  @Unmodifiable
  public Set<Chunk> getSentChunks() {
    return player.getSentChunks();
  }

  public boolean isBanned() {
    return player.isBanned();
  }

  public boolean teleport(@NotNull Location location) {
    return player.teleport(location);
  }

  public void showElderGuardian(boolean b) {
    player.showElderGuardian(b);
  }

  public boolean isFlying() {
    return player.isFlying();
  }

  @Deprecated
  public void setPlayerListHeaderFooter(@Nullable BaseComponent baseComponent, @Nullable BaseComponent baseComponent1) {
    player.setPlayerListHeaderFooter(baseComponent, baseComponent1);
  }

  public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3,
                                double v4, double v5, double v6, @Nullable T t, boolean b) {
    player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6, t, b);
  }

  @NotNull
  public Set<String> getScoreboardTags() {
    return player.getScoreboardTags();
  }

  @NotNull
  public EntityType getType() {
    return player.getType();
  }

  public void openBook(@NotNull ItemStack itemStack) {
    player.openBook(itemStack);
  }

  public int getNextArrowRemoval() {
    return player.getNextArrowRemoval();
  }

  public void damage(double v) {
    player.damage(v);
  }

  @Contract("_, null -> _; _, !null -> !null")
  public <T> @Nullable T getOrDefault(@NotNull Pointer<T> pointer, @Nullable T defaultValue) {
    return player.getOrDefault(pointer, defaultValue);
  }

  public void sendRichMessage(@NotNull String message) {
    player.sendRichMessage(message);
  }

  public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
    player.incrementStatistic(statistic, entityType);
  }

  public int getShieldBlockingDelay() {
    return player.getShieldBlockingDelay();
  }

  public int applyMending(int i) {
    return player.applyMending(i);
  }

  public boolean unlistPlayer(@NotNull Player player) {
    return this.player.unlistPlayer(player);
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

  @Deprecated
  public void showTitle(@Nullable BaseComponent baseComponent) {
    player.showTitle(baseComponent);
  }

  @Nullable
  public InventoryView openWorkbench(@Nullable Location location, boolean b) {
    return player.openWorkbench(location, b);
  }

  public boolean addPassenger(@NotNull Entity entity) {
    return player.addPassenger(entity);
  }

  public void customName(@Nullable Component component) {
    player.customName(component);
  }

  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                                double v2, @Nullable T t) {
    player.spawnParticle(particle, location, i, v, v1, v2, t);
  }

  public int getNextBeeStingerRemoval() {
    return player.getNextBeeStingerRemoval();
  }

  public void stopSound(@NotNull SoundStop stop) {
    player.stopSound(stop);
  }

  public void heal(double v, @NotNull EntityRegainHealthEvent.RegainReason regainReason) {
    player.heal(v, regainReason);
  }

  public void setExpCooldown(int i) {
    player.setExpCooldown(i);
  }

  public boolean canSee(@NotNull Entity entity) {
    return player.canSee(entity);
  }

  @ApiStatus.Experimental
  public void completeUsingActiveItem() {
    player.completeUsingActiveItem();
  }

  public void broadcastSlotBreak(@NotNull EquipmentSlot equipmentSlot) {
    player.broadcastSlotBreak(equipmentSlot);
  }

  @Deprecated
  public void playNote(@NotNull Location location, byte b, byte b1) {
    player.playNote(location, b, b1);
  }

  @Deprecated
  public void sendMultiBlockChange(@NotNull Map<? extends Position, BlockData> blockChanges,
                                   boolean suppressLightUpdates) {
    player.sendMultiBlockChange(blockChanges, suppressLightUpdates);
  }

  @NotNull
  public Duration getIdleDuration() {
    return player.getIdleDuration();
  }

  @Deprecated(since = "1.20.4")
  @NotNull
  public BanEntry banPlayer(@Nullable String reason, @Nullable String source) {
    return player.banPlayer(reason, source);
  }

  @NotNull
  public Audience filterAudience(@NotNull Predicate<? super Audience> filter) {
    return player.filterAudience(filter);
  }

  public void sendPlayerListHeader(@NotNull ComponentLike header) {
    player.sendPlayerListHeader(header);
  }

  @NotNull
  public Chunk getChunk() {
    return player.getChunk();
  }

  @NotNull
  public Set<String> getListeningPluginChannels() {
    return player.getListeningPluginChannels();
  }

  public boolean teleport(@NotNull Entity entity) {
    return player.teleport(entity);
  }

  @NotNull
  public String getScoreboardEntryName() {
    return player.getScoreboardEntryName();
  }

  public void removeResourcePacks() {
    player.removeResourcePacks();
  }

  public void sendBlockChange(@NotNull Location location, @NotNull BlockData blockData) {
    player.sendBlockChange(location, blockData);
  }

  public void kick(@Nullable Component component) {
    player.kick(component);
  }

  public void stopSound(@NotNull SoundCategory soundCategory) {
    player.stopSound(soundCategory);
  }

  public boolean isPermissionSet(@NotNull String s) {
    return player.isPermissionSet(s);
  }

  public void playSound(@NotNull net.kyori.adventure.sound.Sound sound, double x, double y, double z) {
    player.playSound(sound, x, y, z);
  }

  public void sendActionBar(@NotNull ComponentLike message) {
    player.sendActionBar(message);
  }

  public boolean hasPermission(@NotNull String s) {
    return player.hasPermission(s);
  }

  public int getSendViewDistance() {
    return player.getSendViewDistance();
  }

  public int getStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
    return player.getStatistic(statistic, material);
  }

  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason, @Nullable String source) {
    return player.banPlayerIP(reason, source);
  }

  @Deprecated
  public void setResourcePack(@NotNull String s, @Nullable byte[] bytes, @Nullable String s1) {
    player.setResourcePack(s, bytes, s1);
  }

  public <E extends BanEntry<? super PlayerProfile>> @Nullable E ban(@Nullable String s, @Nullable Date date,
                                                                     @Nullable String s1, boolean b) {
    return player.ban(s, date, s1, b);
  }

  @ApiStatus.Experimental
  public void sendBlockUpdate(@NotNull Location location, @NotNull TileState tileState) throws IllegalArgumentException {
    player.sendBlockUpdate(location, tileState);
  }

  public boolean isHealthScaled() {
    return player.isHealthScaled();
  }

  public boolean getRemoveWhenFarAway() {
    return player.getRemoveWhenFarAway();
  }

  public boolean isSneaking() {
    return player.isSneaking();
  }

  public void deleteMessage(@NotNull SignedMessage signedMessage) {
    player.deleteMessage(signedMessage);
  }

  public void hideBossBar(@NotNull BossBar bar) {
    player.hideBossBar(bar);
  }

  public void openBook(@NotNull Book book) {
    player.openBook(book);
  }

  public float getCooledAttackStrength(float v) {
    return player.getCooledAttackStrength(v);
  }

  public boolean isInWaterOrBubbleColumn() {
    return player.isInWaterOrBubbleColumn();
  }

  public int getFreezeTicks() {
    return player.getFreezeTicks();
  }

  public void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory soundCategory, float v,
                        float v1) {
    player.playSound(entity, sound, soundCategory, v, v1);
  }

  public void transfer(@NotNull String s, int i) {
    player.transfer(s, i);
  }

  public void setSprinting(boolean b) {
    player.setSprinting(b);
  }

  public void sendEquipmentChange(@NotNull LivingEntity livingEntity, @NotNull Map<EquipmentSlot, ItemStack> map) {
    player.sendEquipmentChange(livingEntity, map);
  }

  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerFull(@Nullable String reason, @Nullable Date expires) {
    return player.banPlayerFull(reason, expires);
  }

  public boolean isDeeplySleeping() {
    return player.isDeeplySleeping();
  }

  public float getExhaustion() {
    return player.getExhaustion();
  }

  public void setStarvationRate(int i) {
    player.setStarvationRate(i);
  }

  public boolean hasResourcePack() {
    return player.hasResourcePack();
  }

  @Deprecated
  public int getArrowsStuck() {
    return player.getArrowsStuck();
  }

  public void playPickupItemAnimation(@NotNull Item item) {
    player.playPickupItemAnimation(item);
  }

  public void sendHealthUpdate(double v, int i, float v1) {
    player.sendHealthUpdate(v, i, v1);
  }

  public void setViewDistance(int i) {
    player.setViewDistance(i);
  }

  public int getWardenWarningLevel() {
    return player.getWardenWarningLevel();
  }

  public boolean isOp() {
    return player.isOp();
  }

  public void setTicksLived(int i) {
    player.setTicksLived(i);
  }

  @NotNull
  public Location getLocation() {
    return player.getLocation();
  }

  @Nullable
  public InetSocketAddress getVirtualHost() {
    return player.getVirtualHost();
  }

  public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int i) {
    player.decrementStatistic(statistic, entityType, i);
  }

  @Nullable
  public RayTraceResult rayTraceEntities(int i, boolean b) {
    return player.rayTraceEntities(i, b);
  }

  @Deprecated
  public void sendSignChange(@NotNull Location location, @Nullable String[] strings, @NotNull DyeColor dyeColor) throws IllegalArgumentException {
    player.sendSignChange(location, strings, dyeColor);
  }

  @Range(from = 0L, to = 2147483647L)
  public int calculateTotalExperiencePoints() {
    return player.calculateTotalExperiencePoints();
  }

  @NotNull
  public Inventory getEnderChest() {
    return player.getEnderChest();
  }

  @Nullable
  public FishHook getFishHook() {
    return player.getFishHook();
  }

  @NotNull
  public Block getTargetBlock(@Nullable Set<Material> set, int i) {
    return player.getTargetBlock(set, i);
  }

  public <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> aClass,
                                                            @Nullable Vector vector) {
    return player.launchProjectile(aClass, vector);
  }

  @Deprecated
  public void sendMessage(@NotNull BaseComponent... components) {
    player.sendMessage(components);
  }

  @Nullable
  public Entity releaseRightShoulderEntity() {
    return player.releaseRightShoulderEntity();
  }

  @Nullable
  public Entity getVehicle() {
    return player.getVehicle();
  }

  public void setSilent(boolean b) {
    player.setSilent(b);
  }

  @NotNull
  public Pose getPose() {
    return player.getPose();
  }

  @NotNull
  public AdvancementProgress getAdvancementProgress(@NotNull Advancement advancement) {
    return player.getAdvancementProgress(advancement);
  }

  public void decrementStatistic(@NotNull Statistic statistic, int i) throws IllegalArgumentException {
    player.decrementStatistic(statistic, i);
  }

  public long getFirstPlayed() {
    return player.getFirstPlayed();
  }

  public int getEntityId() {
    return player.getEntityId();
  }

  public void playSound(@NotNull Entity entity, @NotNull String s, float v, float v1) {
    player.playSound(entity, s, v, v1);
  }

  public @NotNull Sound getSwimHighSpeedSplashSound() {
    return player.getSwimHighSpeedSplashSound();
  }

  public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                                double v2, double v3, @Nullable T t) {
    player.spawnParticle(particle, location, i, v, v1, v2, v3, t);
  }

  public void removeAttachment(@NotNull PermissionAttachment permissionAttachment) {
    player.removeAttachment(permissionAttachment);
  }

  @NotNull
  public PlayerProfile getPlayerProfile() {
    return player.getPlayerProfile();
  }

  @ApiStatus.Experimental
  @NotNull
  public Entity copy() {
    return player.copy();
  }

  public void setSaturation(float v) {
    player.setSaturation(v);
  }

  public boolean isInRain() {
    return player.isInRain();
  }

  @Deprecated
  public void setPlayerListHeader(@Nullable String s) {
    player.setPlayerListHeader(s);
  }

  @ApiStatus.Obsolete
  public void sendMessage(@NotNull String... strings) {
    player.sendMessage(strings);
  }

  public boolean getAffectsSpawning() {
    return player.getAffectsSpawning();
  }

  @Nullable
  public BlockFace getTargetBlockFace(int maxDistance) {
    return player.getTargetBlockFace(maxDistance);
  }

  public void setPlayerWeather(@NotNull WeatherType weatherType) {
    player.setPlayerWeather(weatherType);
  }

  public boolean isChunkSent(long l) {
    return player.isChunkSent(l);
  }

  public boolean isEmpty() {
    return player.isEmpty();
  }

  public boolean teleport(@NotNull Location location, @NotNull PlayerTeleportEvent.TeleportCause teleportCause,
                          @NotNull TeleportFlag @NotNull ... teleportFlags) {
    return player.teleport(location, teleportCause, teleportFlags);
  }

  public float getPitch() {
    return player.getPitch();
  }

  @Deprecated
  public void sendTitle(@Nullable String s, @Nullable String s1, int i, int i1, int i2) {
    player.sendTitle(s, s1, i, i1, i2);
  }

  public @NotNull Sound getFallDamageSoundBig() {
    return player.getFallDamageSoundBig();
  }

  public boolean getAllowFlight() {
    return player.getAllowFlight();
  }

  @Deprecated
  @Nullable
  public Entity getShoulderEntityLeft() {
    return player.getShoulderEntityLeft();
  }

  public boolean isSleepingIgnored() {
    return player.isSleepingIgnored();
  }

  public float getFlySpeed() {
    return player.getFlySpeed();
  }

  @ApiStatus.Experimental
  public void startUsingItem(@NotNull EquipmentSlot equipmentSlot) {
    player.startUsingItem(equipmentSlot);
  }

  public int getStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
    return player.getStatistic(statistic, entityType);
  }

  public boolean canBreatheUnderwater() {
    return player.canBreatheUnderwater();
  }

  public void setSaturatedRegenRate(int i) {
    player.setSaturatedRegenRate(i);
  }

  public void showWinScreen() {
    player.showWinScreen();
  }

  public void setBeeStingerCooldown(int i) {
    player.setBeeStingerCooldown(i);
  }

  @NotNull
  public SpawnCategory getSpawnCategory() {
    return player.getSpawnCategory();
  }

  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason, @Nullable String source, boolean kickPlayer) {
    return player.banPlayerIP(reason, source, kickPlayer);
  }

  @Nullable
  public String getClientBrandName() {
    return player.getClientBrandName();
  }

  @Nullable
  public BanEntry<InetAddress> banIp(@Nullable String s, @Nullable Instant instant, @Nullable String s1, boolean b) {
    return player.banIp(s, instant, s1, b);
  }

  public void playSound(@NotNull Location location, @NotNull String s, float v, float v1) {
    player.playSound(location, s, v, v1);
  }

  public int getLevel() {
    return player.getLevel();
  }

  public void playPickupItemAnimation(@NotNull Item item, int i) {
    player.playPickupItemAnimation(item, i);
  }

  @Nullable
  public WeatherType getPlayerWeather() {
    return player.getPlayerWeather();
  }

  public boolean isChunkSent(@NotNull Chunk chunk) {
    return player.isChunkSent(chunk);
  }

  public boolean removePassenger(@NotNull Entity entity) {
    return player.removePassenger(entity);
  }

  @NotNull
  public List<MetadataValue> getMetadata(@NotNull String s) {
    return player.getMetadata(s);
  }

  public void setResourcePack(@NotNull String url, byte @Nullable [] hash, @Nullable Component prompt, boolean force) {
    player.setResourcePack(url, hash, prompt, force);
  }

  @Deprecated
  public void sendMessage(@NotNull Identified source, @NotNull Component message) {
    player.sendMessage(source, message);
  }

  public boolean isVisibleByDefault() {
    return player.isVisibleByDefault();
  }

  public void acceptConversationInput(@NotNull String s) {
    player.acceptConversationInput(s);
  }

  @Nullable
  public BlockFace getTargetBlockFace(int i, @NotNull FluidCollisionMode fluidCollisionMode) {
    return player.getTargetBlockFace(i, fluidCollisionMode);
  }

  public boolean addPotionEffect(@NotNull PotionEffect potionEffect) {
    return player.addPotionEffect(potionEffect);
  }

  public void resetCooldown() {
    player.resetCooldown();
  }

  public void sendPlayerListFooter(@NotNull Component footer) {
    player.sendPlayerListFooter(footer);
  }

  @Nullable
  public Component playerListHeader() {
    return player.playerListHeader();
  }

  @NotNull
  public List<Entity> getPassengers() {
    return player.getPassengers();
  }

  public int getSimulationDistance() {
    return player.getSimulationDistance();
  }

  public boolean collidesAt(@NotNull Location location) {
    return player.collidesAt(location);
  }

  @Deprecated
  public void sendMessage(@NotNull Identity source, @NotNull ComponentLike message) {
    player.sendMessage(source, message);
  }

  @Nullable
  public BanEntry<InetAddress> banIp(@Nullable String s, @Nullable Duration duration, @Nullable String s1, boolean b) {
    return player.banIp(s, duration, s1, b);
  }

  public double getEyeHeight() {
    return player.getEyeHeight();
  }

  public int getTicksLived() {
    return player.getTicksLived();
  }

  public void abandonConversation(@NotNull Conversation conversation) {
    player.abandonConversation(conversation);
  }

  public void setTotalExperience(int i) {
    player.setTotalExperience(i);
  }

  public void setBeeStingersInBody(int i) {
    player.setBeeStingersInBody(i);
  }

  public void playSound(@NotNull Location location, @NotNull String s, @NotNull SoundCategory soundCategory, float v,
                        float v1) {
    player.playSound(location, s, soundCategory, v, v1);
  }

  @Deprecated
  public void sendMessage(ChatMessageType position, BaseComponent... components) {
    player.sendMessage(position, components);
  }

  @Deprecated
  @Nullable
  public Entity getPassenger() {
    return player.getPassenger();
  }

  public boolean leaveVehicle() {
    return player.leaveVehicle();
  }

  public boolean spawnAt(@NotNull Location location) {
    return player.spawnAt(location);
  }

  @NotNull
  public HoverEvent<HoverEvent.ShowEntity> asHoverEvent(@NotNull UnaryOperator<HoverEvent.ShowEntity> op) {
    return player.asHoverEvent(op);
  }

  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason, @Nullable Date expires, @Nullable String source,
                              boolean kickPlayer) {
    return player.banPlayerIP(reason, expires, source, kickPlayer);
  }

  public boolean isSilent() {
    return player.isSilent();
  }

  @NotNull
  public EntityScheduler getScheduler() {
    return player.getScheduler();
  }

  @NotNull
  public Entity getLeashHolder() throws IllegalStateException {
    return player.getLeashHolder();
  }

  @Nullable
  public Entity getTargetEntity(int maxDistance) {
    return player.getTargetEntity(maxDistance);
  }

  public void setNoDamageTicks(int i) {
    player.setNoDamageTicks(i);
  }

  public void setCanPickupItems(boolean b) {
    player.setCanPickupItems(b);
  }

  public int getSaturatedRegenRate() {
    return player.getSaturatedRegenRate();
  }

  @ApiStatus.Experimental
  @Nullable
  public EntitySnapshot createSnapshot() {
    return player.createSnapshot();
  }

  public int getBeeStingerCooldown() {
    return player.getBeeStingerCooldown();
  }

  @Deprecated
  @Nullable
  public String getPlayerListFooter() {
    return player.getPlayerListFooter();
  }

  @Deprecated
  public boolean isOnGround() {
    return player.isOnGround();
  }

  public void playSound(@NotNull Location location, @NotNull Sound sound, float v, float v1) {
    player.playSound(location, sound, v, v1);
  }

  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason, @Nullable Date expires, @Nullable String source) {
    return player.banPlayerIP(reason, expires, source);
  }

  public void updateInventory() {
    player.updateInventory();
  }

  public void hideEntity(@NotNull Plugin plugin, @NotNull Entity entity) {
    player.hideEntity(plugin, entity);
  }

  public void resetIdleDuration() {
    player.resetIdleDuration();
  }

  @Deprecated
  public void setBedSpawnLocation(@Nullable Location location) {
    player.setBedSpawnLocation(location);
  }

  public void setFreezeTicks(int i) {
    player.setFreezeTicks(i);
  }

  public void removeMetadata(@NotNull String s, @NotNull Plugin plugin) {
    player.removeMetadata(s, plugin);
  }

  public void sendBlockDamage(@NotNull Location location, float v, @NotNull Entity entity) {
    player.sendBlockDamage(location, v, entity);
  }

  public void setShieldBlockingDelay(int i) {
    player.setShieldBlockingDelay(i);
  }

  public void removeResourcePacks(@NotNull UUID id, @NotNull UUID @NotNull ... others) {
    player.removeResourcePacks(id, others);
  }

  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerFull(@Nullable String reason) {
    return player.banPlayerFull(reason);
  }

  @Deprecated
  @Nullable
  public String getCustomName() {
    return player.getCustomName();
  }

  @Deprecated
  public void setPlayerListFooter(@Nullable String s) {
    player.setPlayerListFooter(s);
  }

  @NotNull
  public CompletableFuture<Boolean> teleportAsync(@NotNull Location loc,
                                                  @NotNull PlayerTeleportEvent.TeleportCause cause) {
    return player.teleportAsync(loc, cause);
  }

  public void removeCustomChatCompletions(@NotNull Collection<String> collection) {
    player.removeCustomChatCompletions(collection);
  }

  @Deprecated
  public void showTitle(@Nullable BaseComponent baseComponent, @Nullable BaseComponent baseComponent1, int i, int i1,
                        int i2) {
    player.showTitle(baseComponent, baseComponent1, i, i1, i2);
  }

  public void sendMessage(@NotNull Component message) {
    player.sendMessage(message);
  }

  public int getArrowsInBody() {
    return player.getArrowsInBody();
  }

  public @NotNull Sound getFallDamageSound(int i) {
    return player.getFallDamageSound(i);
  }

  @NotNull
  public ItemStack damageItemStack(@NotNull ItemStack itemStack, int i) {
    return player.damageItemStack(itemStack, i);
  }

  public void setAbsorptionAmount(double v) {
    player.setAbsorptionAmount(v);
  }

  public void forEachAudience(@NotNull Consumer<? super Audience> action) {
    player.forEachAudience(action);
  }

  @NotNull
  public Pointers pointers() {
    return player.pointers();
  }

  @NotNull
  public TriState permissionValue(@NotNull Permission permission) {
    return player.permissionValue(permission);
  }

  @Deprecated
  @NotNull
  public ItemStack getItemInHand() {
    return player.getItemInHand();
  }

  public void lookAt(double v, double v1, double v2, @NotNull LookAnchor lookAnchor) {
    player.lookAt(v, v1, v2, lookAnchor);
  }

  public void sendRawMessage(@NotNull String s) {
    player.sendRawMessage(s);
  }

  @Nullable
  public Firework boostElytra(@NotNull ItemStack firework) {
    return player.boostElytra(firework);
  }
}
