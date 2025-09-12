/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user;


import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.entity.LookAnchor;
import io.papermc.paper.entity.PlayerGiveResult;
import io.papermc.paper.entity.TeleportFlag;
import io.papermc.paper.math.Position;
import io.papermc.paper.threadedregions.scheduler.EntityScheduler;
import io.papermc.paper.world.damagesource.CombatTracker;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.key.Key;
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
import net.kyori.adventure.title.Title;
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
import org.jspecify.annotations.Nullable;

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

  public Identity identity() {
    return player.identity();
  }

  public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, @Nullable T t) {
    player.spawnParticle(particle, v, v1, v2, i, t);
  }

  public void setCustomNameVisible(boolean b) {
    player.setCustomNameVisible(b);
  }

  public boolean isInWorld() {
    return player.isInWorld();
  }

  public boolean spawnAt(@NotNull Location location) {
    return player.spawnAt(location);
  }

  public void sendMessage(@NotNull Component message) {
    player.sendMessage(message);
  }

  public boolean isInLava() {
    return player.isInLava();
  }

  /**
   * @param location
   * @param strings
   * @param dyeColor
   * @param b
   * @deprecated
   */
  @Deprecated
  public void sendSignChange(Location location, @Nullable String @Nullable [] strings, DyeColor dyeColor, boolean b) throws IllegalArgumentException {
    player.sendSignChange(location, strings, dyeColor, b);
  }

  public void broadcastSlotBreak(@NotNull EquipmentSlot equipmentSlot) {
    player.broadcastSlotBreak(equipmentSlot);
  }

  public boolean isOnline() {
    return player.isOnline();
  }

  public boolean clearActivePotionEffects() {
    return player.clearActivePotionEffects();
  }

  public void setFoodLevel(int i) {
    player.setFoodLevel(i);
  }

  public void setStatistic(Statistic statistic, EntityType entityType, int i) {
    player.setStatistic(statistic, entityType, i);
  }

  public void forEachAudience(@NotNull Consumer<? super Audience> action) {
    player.forEachAudience(action);
  }

  /**
   * @deprecated
   */
  @Deprecated
  public void hideTitle() {
    player.hideTitle();
  }

  @ApiStatus.Obsolete(since = "1.20.4")
  public int getHandRaisedTime() {
    return player.getHandRaisedTime();
  }

  public void showDialog(@NotNull DialogLike dialog) {
    player.showDialog(dialog);
  }

  @ApiStatus.Experimental
  public void completeUsingActiveItem() {
    player.completeUsingActiveItem();
  }

  public boolean canUseEquipmentSlot(@NotNull EquipmentSlot equipmentSlot) {
    return player.canUseEquipmentSlot(equipmentSlot);
  }

  public void showWinScreen() {
    player.showWinScreen();
  }

  public float getHurtDirection() {
    return player.getHurtDirection();
  }

  public void sendBlockChange(Location location, BlockData blockData) {
    player.sendBlockChange(location, blockData);
  }

  public void stopSound(String s) {
    player.stopSound(s);
  }

  @NotNull
  public Sound getDrinkingSound(@NotNull ItemStack itemStack) {
    return player.getDrinkingSound(itemStack);
  }

  public void showTitle(@NotNull Title title) {
    player.showTitle(title);
  }

  public void setWardenWarningCooldown(int i) {
    player.setWardenWarningCooldown(i);
  }

  public void sendHealthUpdate() {
    player.sendHealthUpdate();
  }

  @NotNull
  public ItemStack damageItemStack(@NotNull ItemStack itemStack, int i) {
    return player.damageItemStack(itemStack, i);
  }

  public long getLastLogin() {
    return player.getLastLogin();
  }

  public void playHurtAnimation(float v) {
    player.playHurtAnimation(v);
  }

  public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
    player.incrementStatistic(statistic, entityType);
  }

  public void deleteMessage(SignedMessage.@NotNull Signature signature) {
    player.deleteMessage(signature);
  }

  public boolean collidesAt(@NotNull Location location) {
    return player.collidesAt(location);
  }

  public boolean getAffectsSpawning() {
    return player.getAffectsSpawning();
  }

  /**
   * @deprecated
   */
  @Deprecated
  public long getLastPlayed() {
    return player.getLastPlayed();
  }

  /**
   * @param s
   * @deprecated
   */
  @Deprecated
  public void sendActionBar(String s) {
    player.sendActionBar(s);
  }

  public double getAbsorptionAmount() {
    return player.getAbsorptionAmount();
  }

  public float getCooldownPeriod() {
    return player.getCooldownPeriod();
  }

  @Nullable
  public Sound getHurtSound() {
    return player.getHurtSound();
  }

  /**
   * @param uuid
   * @param s
   * @deprecated
   */
  @Deprecated
  public void sendMessage(@Nullable UUID uuid, @NotNull String s) {
    player.sendMessage(uuid, s);
  }

  @NotNull
  public Set<String> getScoreboardTags() {
    return player.getScoreboardTags();
  }

  public void showBossBar(@NotNull BossBar bar) {
    player.showBossBar(bar);
  }

  public <T> void playEffect(Location location, Effect effect, @Nullable T t) {
    player.playEffect(location, effect, t);
  }

  public void setFlying(boolean b) {
    player.setFlying(b);
  }

  public void showElderGuardian() {
    player.showElderGuardian();
  }

  @Nullable
  public RayTraceResult rayTraceEntities(int maxDistance) {
    return player.rayTraceEntities(maxDistance);
  }

  public boolean teleport(@NotNull Location location, @NotNull TeleportFlag @NotNull ... teleportFlags) {
    return player.teleport(location, teleportFlags);
  }

  @ApiStatus.Obsolete(since = "1.21.4")
  public boolean dropItem(boolean b) {
    return player.dropItem(b);
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.21.4")
  @Nullable
  public Location getPotentialBedLocation() {
    return player.getPotentialBedLocation();
  }

  @Nullable
  public Location getRespawnLocation(boolean b) {
    return player.getRespawnLocation(b);
  }

  public HoverEvent<HoverEvent.ShowEntity> asHoverEvent(UnaryOperator<HoverEvent.ShowEntity> op) {
    return player.asHoverEvent(op);
  }

  /**
   * @param s
   * @param bytes
   * @deprecated
   */
  @Deprecated
  public void setResourcePack(String s, byte @Nullable [] bytes) {
    player.setResourcePack(s, bytes);
  }

  public float getUpwardsMovement() {
    return player.getUpwardsMovement();
  }

  public void swingHand(@NotNull EquipmentSlot hand) {
    player.swingHand(hand);
  }

  @NotNull
  public Vector getVelocity() {
    return player.getVelocity();
  }

  @NotNull
  public Pose getPose() {
    return player.getPose();
  }

  public boolean isDeeplySleeping() {
    return player.isDeeplySleeping();
  }

  public void updateCommands() {
    player.updateCommands();
  }

  public void heal(double amount) {
    player.heal(amount);
  }

  public void sendResourcePacks(@NotNull ResourcePackInfoLike first, @NotNull ResourcePackInfoLike... others) {
    player.sendResourcePacks(first, others);
  }

  /**
   * @param source
   * @param message
   * @deprecated
   */
  @Deprecated
  public void sendMessage(@NotNull Identified source, @NotNull Component message) {
    player.sendMessage(source, message);
  }

  public void stopAllSounds() {
    player.stopAllSounds();
  }

  /**
   * @deprecated
   */
  @Deprecated
  @Nullable
  public String getPlayerListHeader() {
    return player.getPlayerListHeader();
  }

  public double getZ() {
    return player.getZ();
  }

  @NotNull
  public World getWorld() {
    return player.getWorld();
  }

  /**
   * @param reason
   * @param expires
   * @param kickPlayer
   * @deprecated
   */
  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason, @Nullable Date expires, boolean kickPlayer) {
    return player.banPlayerIP(reason, expires, kickPlayer);
  }

  @Nullable
  public Entity getTargetEntity(int i, boolean b) {
    return player.getTargetEntity(i, b);
  }

  public void sendRichMessage(@NotNull String message, @NotNull TagResolver... resolvers) {
    player.sendRichMessage(message, resolvers);
  }

  @Nullable
  public Item dropItem(ItemStack itemStack) {
    return player.dropItem(itemStack);
  }

  public void transfer(String s, int i) {
    player.transfer(s, i);
  }

  /**
   * @param location
   * @param b
   * @deprecated
   */
  @Deprecated(since = "1.21.4")
  @Nullable
  public InventoryView openLoom(@Nullable Location location, boolean b) {
    return player.openLoom(location, b);
  }

  public void setWhitelisted(boolean b) {
    player.setWhitelisted(b);
  }

  public void setNextBeeStingerRemoval(@Range(from = 0L, to = 2147483647L) int i) {
    player.setNextBeeStingerRemoval(i);
  }

  @NotNull
  public TriState permissionValue(@NotNull Permission permission) {
    return player.permissionValue(permission);
  }

  public int undiscoverRecipes(Collection<NamespacedKey> collection) {
    return player.undiscoverRecipes(collection);
  }

  @ApiStatus.Obsolete(since = "1.20.4")
  public boolean isHandRaised() {
    return player.isHandRaised();
  }

  public void setStarvationRate(int i) {
    player.setStarvationRate(i);
  }

  public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
    player.decrementStatistic(statistic, material);
  }

  /**
   * @param location
   * @param list
   * @param dyeColor
   * @param b
   * @deprecated
   */
  @Deprecated
  public void sendSignChange(Location location, @Nullable List<? extends Component> list, DyeColor dyeColor,
                             boolean b) throws IllegalArgumentException {
    player.sendSignChange(location, list, dyeColor, b);
  }

  @ApiStatus.Experimental
  @NotNull
  public Entity copy() {
    return player.copy();
  }

  @Range(from = 0L, to = 2147483647L)
  public int calculateTotalExperiencePoints() {
    return player.calculateTotalExperiencePoints();
  }

  /**
   * @param reason
   * @param expires
   * @param source
   * @deprecated
   */
  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerFull(@Nullable String reason, @Nullable Date expires, @Nullable String source) {
    return player.banPlayerFull(reason, expires, source);
  }

  public boolean unlistPlayer(Player player) {
    return this.player.unlistPlayer(player);
  }

  public void setEnchantmentSeed(int i) {
    player.setEnchantmentSeed(i);
  }

  public boolean sleep(Location location, boolean b) {
    return player.sleep(location, b);
  }

  public void removeResourcePacks(@NotNull ResourcePackInfoLike request,
                                  @NotNull ResourcePackInfoLike @NotNull ... others) {
    player.removeResourcePacks(request, others);
  }

  /**
   * @param reason
   * @deprecated
   */
  @Deprecated(since = "1.20.4")
  public BanEntry banPlayer(@Nullable String reason) {
    return player.banPlayer(reason);
  }

  public void setMaximumNoDamageTicks(int i) {
    player.setMaximumNoDamageTicks(i);
  }

  public <T> void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2,
                                double v3, @Nullable T t) {
    player.spawnParticle(particle, location, i, v, v1, v2, v3, t);
  }

  public void setKiller(@Nullable Player player) {
    this.player.setKiller(player);
  }

  public void hideEntity(Plugin plugin, Entity entity) {
    player.hideEntity(plugin, entity);
  }

  public boolean isPersistent() {
    return player.isPersistent();
  }

  public GameMode getGameMode() {
    return player.getGameMode();
  }

  public int getUnsaturatedRegenRate() {
    return player.getUnsaturatedRegenRate();
  }

  public void sendMessage(@NotNull SignedMessage signedMessage, ChatType.@NotNull Bound boundChatType) {
    player.sendMessage(signedMessage, boundChatType);
  }

  public void sendLinks(ServerLinks serverLinks) {
    player.sendLinks(serverLinks);
  }

  public void setArrowCooldown(int i) {
    player.setArrowCooldown(i);
  }

  public boolean isVisibleByDefault() {
    return player.isVisibleByDefault();
  }

  public void sendExperienceChange(float v) {
    player.sendExperienceChange(v);
  }

  public boolean isInPowderedSnow() {
    return player.isInPowderedSnow();
  }

  @NotNull
  public <T> Optional<T> get(@NotNull Pointer<T> pointer) {
    return player.get(pointer);
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.12")
  @Nullable
  public Entity getShoulderEntityLeft() {
    return player.getShoulderEntityLeft();
  }

  @Nullable
  public RayTraceResult rayTraceEntities(int i, boolean b) {
    return player.rayTraceEntities(i, b);
  }

  public long getPlayerTime() {
    return player.getPlayerTime();
  }

  public boolean addPassenger(@NotNull Entity entity) {
    return player.addPassenger(entity);
  }

  public boolean breakBlock(Block block) {
    return player.breakBlock(block);
  }

  public void playNote(Location location, Instrument instrument, Note note) {
    player.playNote(location, instrument, note);
  }

  @NotNull
  public Block getTargetBlock(@Nullable Set<Material> set, int i) {
    return player.getTargetBlock(set, i);
  }

  /**
   * @param reason
   * @param expires
   * @deprecated
   */
  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason, @Nullable Date expires) {
    return player.banPlayerIP(reason, expires);
  }

  public int getClientViewDistance() {
    return player.getClientViewDistance();
  }

  /**
   * @param location
   * @param b
   * @deprecated
   */
  @Deprecated(since = "1.21.4")
  @Nullable
  public InventoryView openAnvil(@Nullable Location location, boolean b) {
    return player.openAnvil(location, b);
  }

  public void setExpCooldown(int i) {
    player.setExpCooldown(i);
  }

  @Nullable
  public PermissionAttachment addAttachment(@NotNull Plugin plugin, int i) {
    return player.addAttachment(plugin, i);
  }

  public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
    player.incrementStatistic(statistic, material);
  }

  public <T> void setMemory(@NotNull MemoryKey<T> memoryKey, @Nullable T t) {
    player.setMemory(memoryKey, t);
  }

  public boolean beginConversation(@NotNull Conversation conversation) {
    return player.beginConversation(conversation);
  }

  /**
   * @param baseComponent
   * @param baseComponent1
   * @deprecated
   */
  @Deprecated
  public void setPlayerListHeaderFooter(@Nullable BaseComponent baseComponent, @Nullable BaseComponent baseComponent1) {
    player.setPlayerListHeaderFooter(baseComponent, baseComponent1);
  }

  public float getFallDistance() {
    return player.getFallDistance();
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.16.1")
  public boolean isOnGround() {
    return player.isOnGround();
  }

  public int getTicksLived() {
    return player.getTicksLived();
  }

  @Nullable
  public Component playerListHeader() {
    return player.playerListHeader();
  }

  public boolean isGliding() {
    return player.isGliding();
  }

  public void setActiveItemRemainingTime(@Range(from = 0L, to = 2147483647L) int i) {
    player.setActiveItemRemainingTime(i);
  }

  public boolean hasFixedPose() {
    return player.hasFixedPose();
  }

  public void sendEntityEffect(EntityEffect entityEffect, Entity entity) {
    player.sendEntityEffect(entityEffect, entity);
  }

  public boolean leaveVehicle() {
    return player.leaveVehicle();
  }

  public Location getCompassTarget() {
    return player.getCompassTarget();
  }

  public boolean getRemoveWhenFarAway() {
    return player.getRemoveWhenFarAway();
  }

  /**
   * @param entity
   * @deprecated
   */
  @Deprecated(since = "1.11.2")
  public boolean setPassenger(@NotNull Entity entity) {
    return player.setPassenger(entity);
  }

  public void setCooldown(ItemStack itemStack, int i) {
    player.setCooldown(itemStack, i);
  }

  @Nullable
  public Item dropItem(EquipmentSlot equipmentSlot, int i, boolean b, @Nullable Consumer<Item> consumer) {
    return player.dropItem(equipmentSlot, i, b, consumer);
  }

  /**
   * @param loc
   * @param lines
   * @deprecated
   */
  @Deprecated
  public void sendSignChange(Location loc, @Nullable List<? extends Component> lines) throws IllegalArgumentException {
    player.sendSignChange(loc, lines);
  }

  public void resetPlayerWeather() {
    player.resetPlayerWeather();
  }

  @NotNull
  public Sound getSwimSplashSound() {
    return player.getSwimSplashSound();
  }

  public boolean hasCooldown(Material material) {
    return player.hasCooldown(material);
  }

  public int getFreezeTicks() {
    return player.getFreezeTicks();
  }

  public int getDeathScreenScore() {
    return player.getDeathScreenScore();
  }

  public void knockback(double v, double v1, double v2) {
    player.knockback(v, v1, v2);
  }

  public void incrementStatistic(Statistic statistic, int i) throws IllegalArgumentException {
    player.incrementStatistic(statistic, i);
  }

  @Nullable
  public WorldBorder getWorldBorder() {
    return player.getWorldBorder();
  }

  public void setArrowsInBody(int i, boolean b) {
    player.setArrowsInBody(i, b);
  }

  @NotNull
  public HoverEvent<HoverEvent.ShowEntity> asHoverEvent() {
    return player.asHoverEvent();
  }

  public int getNoDamageTicks() {
    return player.getNoDamageTicks();
  }

  /**
   * @deprecated
   */
  @Deprecated
  @Nullable
  public String getCustomName() {
    return player.getCustomName();
  }

  /**
   * @param s
   * @param s1
   * @deprecated
   */
  @Deprecated
  public void setPlayerListHeaderFooter(@Nullable String s, @Nullable String s1) {
    player.setPlayerListHeaderFooter(s, s1);
  }

  public boolean hasPermission(@NotNull Permission permission) {
    return player.hasPermission(permission);
  }

  /**
   * @param baseComponent
   * @deprecated
   */
  @Deprecated
  public void setSubtitle(BaseComponent baseComponent) {
    player.setSubtitle(baseComponent);
  }

  @Nullable
  public Firework fireworkBoost(ItemStack itemStack) {
    return player.fireworkBoost(itemStack);
  }

  public boolean addScoreboardTag(@NotNull String s) {
    return player.addScoreboardTag(s);
  }

  /**
   * @param source
   * @param message
   * @deprecated
   */
  @Deprecated
  public void sendMessage(@NotNull Identity source, @NotNull Component message) {
    player.sendMessage(source, message);
  }

  public void setNoPhysics(boolean b) {
    player.setNoPhysics(b);
  }

  /**
   * @param s
   * @param bytes
   * @param s1
   * @param b
   * @deprecated
   */
  @Deprecated
  public void setResourcePack(String s, byte @Nullable [] bytes, @Nullable String s1, boolean b) {
    player.setResourcePack(s, bytes, s1, b);
  }

  public <E extends BanEntry<? super PlayerProfile>> @Nullable E ban(@Nullable String s, @Nullable Date date,
                                                                     @Nullable String s1, boolean b) {
    return player.ban(s, date, s1, b);
  }

  /**
   * @param location
   * @param b
   * @deprecated
   */
  @Deprecated(since = "1.21.4")
  @Nullable
  public InventoryView openWorkbench(@Nullable Location location, boolean b) {
    return player.openWorkbench(location, b);
  }

  public void setLevel(int i) {
    player.setLevel(i);
  }

  public void resetIdleDuration() {
    player.resetIdleDuration();
  }

  public boolean isFrozen() {
    return player.isFrozen();
  }

  public void setGlowing(boolean b) {
    player.setGlowing(b);
  }

  public void removeResourcePacks() {
    player.removeResourcePacks();
  }

  public void resetPlayerTime() {
    player.resetPlayerTime();
  }

  public PlayerInventory getInventory() {
    return player.getInventory();
  }

  public void setNoActionTicks(int i) {
    player.setNoActionTicks(i);
  }

  public boolean isLeashed() {
    return player.isLeashed();
  }

  /**
   * @param uuid
   * @param s
   * @deprecated
   */
  @Deprecated
  public void sendRawMessage(@Nullable UUID uuid, @NotNull String s) {
    player.sendRawMessage(uuid, s);
  }

  /**
   * @param component
   * @deprecated
   */
  @Deprecated
  public void sendMessage(BaseComponent component) {
    player.sendMessage(component);
  }

  @Nullable
  public AttributeInstance getAttribute(@NotNull Attribute attribute) {
    return player.getAttribute(attribute);
  }

  public void setRemainingAir(int i) {
    player.setRemainingAir(i);
  }

  public void setRiptiding(boolean b) {
    player.setRiptiding(b);
  }

  public void recalculatePermissions() {
    player.recalculatePermissions();
  }

  public void lockFreezeTicks(boolean b) {
    player.lockFreezeTicks(b);
  }

  public void updateInventory() {
    player.updateInventory();
  }

  /**
   * @deprecated
   */
  @Deprecated
  public String getLocale() {
    return player.getLocale();
  }

  public boolean isTransferred() {
    return player.isTransferred();
  }

  public float getWalkSpeed() {
    return player.getWalkSpeed();
  }

  public void sendPlayerListHeaderAndFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
    player.sendPlayerListHeaderAndFooter(header, footer);
  }

  public boolean isJumping() {
    return player.isJumping();
  }

  public void setFlyingFallDamage(TriState triState) {
    player.setFlyingFallDamage(triState);
  }

  @Nullable
  public BlockFace getTargetBlockFace(int maxDistance) {
    return player.getTargetBlockFace(maxDistance);
  }

  public <E extends BanEntry<? super PlayerProfile>> @Nullable E ban(@Nullable String s, @Nullable Instant instant,
                                                                     @Nullable String s1, boolean b) {
    return player.ban(s, instant, s1, b);
  }

  public boolean isConversing() {
    return player.isConversing();
  }

  /**
   * @param firework
   * @deprecated
   */
  @Nullable
  public Firework boostElytra(ItemStack firework) {
    return player.boostElytra(firework);
  }

  public boolean isChunkSent(Chunk chunk) {
    return player.isChunkSent(chunk);
  }

  @NotNull
  public Set<UUID> getCollidableExemptions() {
    return player.getCollidableExemptions();
  }

  public float getExp() {
    return player.getExp();
  }

  public boolean isSleepingIgnored() {
    return player.isSleepingIgnored();
  }

  public void setAI(boolean b) {
    player.setAI(b);
  }

  @NotNull
  public Location getEyeLocation() {
    return player.getEyeLocation();
  }

  public void playSound(Entity entity, String s, SoundCategory soundCategory, float v, float v1) {
    player.playSound(entity, s, soundCategory, v, v1);
  }

  /**
   * @param collection
   * @deprecated
   */
  @Deprecated(since = "1.20.1")
  public void addAdditionalChatCompletions(Collection<String> collection) {
    player.addAdditionalChatCompletions(collection);
  }

  /**
   * @param s
   * @param bytes
   * @param s1
   * @deprecated
   */
  @Deprecated
  public void setResourcePack(String s, byte @Nullable [] bytes, @Nullable String s1) {
    player.setResourcePack(s, bytes, s1);
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.11")
  public void resetMaxHealth() {
    player.resetMaxHealth();
  }

  public void setExhaustion(float v) {
    player.setExhaustion(v);
  }

  public Player.Spigot spigot() {
    return player.spigot();
  }

  public <T> void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2,
                                @Nullable T t) {
    player.spawnParticle(particle, location, i, v, v1, v2, t);
  }

  public CreatureSpawnEvent.@NotNull SpawnReason getEntitySpawnReason() {
    return player.getEntitySpawnReason();
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.21.5")
  @Nullable
  public Location getPotentialRespawnLocation() {
    return player.getPotentialRespawnLocation();
  }

  public void stopSound(SoundCategory soundCategory) {
    player.stopSound(soundCategory);
  }

  /**
   * @param blockChanges
   * @param suppressLightUpdates
   * @deprecated
   */
  @Deprecated
  public void sendMultiBlockChange(Map<? extends Position, BlockData> blockChanges, boolean suppressLightUpdates) {
    player.sendMultiBlockChange(blockChanges, suppressLightUpdates);
  }

  public void setResourcePack(String url, byte @Nullable [] hash, @Nullable Component prompt, boolean force) {
    player.setResourcePack(url, hash, prompt, force);
  }

  public float getAttackCooldown() {
    return player.getAttackCooldown();
  }

  public void setFallDistance(float v) {
    player.setFallDistance(v);
  }

  public void spawnParticle(Particle particle, Location location, int i) {
    player.spawnParticle(particle, location, i);
  }

  @NotNull
  public Set<Player> getTrackedBy() {
    return player.getTrackedBy();
  }

  public void setPersistent(boolean b) {
    player.setPersistent(b);
  }

  public void sendMessage(@NotNull ComponentLike message) {
    player.sendMessage(message);
  }

  @NotNull
  public PersistentDataContainer getPersistentDataContainer() {
    return player.getPersistentDataContainer();
  }

  public void stopSound(Sound sound) {
    player.stopSound(sound);
  }

  /**
   * @param reason
   * @param kickPlayer
   * @deprecated
   */
  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason, boolean kickPlayer) {
    return player.banPlayerIP(reason, kickPlayer);
  }

  /**
   * @param position
   * @param components
   * @deprecated
   */
  @Deprecated
  public void sendMessage(ChatMessageType position, BaseComponent... components) {
    player.sendMessage(position, components);
  }

  /**
   * @param s
   * @deprecated
   */
  @Deprecated
  public void setCustomName(@Nullable String s) {
    player.setCustomName(s);
  }

  @NotNull
  public Set<String> getListeningPluginChannels() {
    return player.getListeningPluginChannels();
  }

  public double getX() {
    return player.getX();
  }

  @ApiStatus.Experimental
  public void damage(double v, @NotNull DamageSource damageSource) {
    player.damage(v, damageSource);
  }

  public boolean isAllowingServerListings() {
    return player.isAllowingServerListings();
  }

  public float getSidewaysMovement() {
    return player.getSidewaysMovement();
  }

  @Nullable
  public EntityDamageEvent getLastDamageCause() {
    return player.getLastDamageCause();
  }

  public int getExpToLevel() {
    return player.getExpToLevel();
  }

  /**
   * @deprecated
   */
  @Deprecated
  public boolean isVisualFire() {
    return player.isVisualFire();
  }

  public void stopSound(@NotNull SoundStop stop) {
    player.stopSound(stop);
  }

  /**
   * @param c
   * @param s
   * @deprecated
   */
  @Deprecated
  public void sendActionBar(char c, String s) {
    player.sendActionBar(c, s);
  }

  public int getExpCooldown() {
    return player.getExpCooldown();
  }

  public void setResourcePack(String url, String hash, boolean required, @Nullable Component resourcePackPrompt) {
    player.setResourcePack(url, hash, required, resourcePackPrompt);
  }

  public void playSound(Entity entity, Sound sound, SoundCategory soundCategory, float v, float v1, long l) {
    player.playSound(entity, sound, soundCategory, v, v1, l);
  }

  @Nullable
  public Block getTargetBlockExact(int i) {
    return player.getTargetBlockExact(i);
  }

  public void playSound(Entity entity, String s, SoundCategory soundCategory, float v, float v1, long l) {
    player.playSound(entity, s, soundCategory, v, v1, l);
  }

  public void setPlayerListOrder(int i) {
    player.setPlayerListOrder(i);
  }

  @NotNull
  public EntityType getType() {
    return player.getType();
  }

  public void deleteMessage(@NotNull SignedMessage signedMessage) {
    player.deleteMessage(signedMessage);
  }

  public void hideBossBar(@NotNull BossBar bar) {
    player.hideBossBar(bar);
  }

  @NotNull
  public Pointers pointers() {
    return player.pointers();
  }

  public void sendEquipmentChange(LivingEntity livingEntity, EquipmentSlot equipmentSlot,
                                  @Nullable ItemStack itemStack) {
    player.sendEquipmentChange(livingEntity, equipmentSlot, itemStack);
  }

  public void damage(double v) {
    player.damage(v);
  }

  public <T> @org.jetbrains.annotations.Nullable T getMemory(@NotNull MemoryKey<T> memoryKey) {
    return player.getMemory(memoryKey);
  }

  /**
   * @param s
   * @deprecated
   */
  @Deprecated
  public void setDisplayName(@Nullable String s) {
    player.setDisplayName(s);
  }

  @Nullable
  public Item dropItem(int slot) {
    return player.dropItem(slot);
  }

  @NotNull
  public List<Block> getLineOfSight(@Nullable Set<Material> set, int i) {
    return player.getLineOfSight(set, i);
  }

  @NotNull
  public Audience filterAudience(@NotNull Predicate<? super Audience> filter) {
    return player.filterAudience(filter);
  }

  /**
   * @param player
   * @deprecated
   */
  @Deprecated(since = "1.12.2")
  public void showPlayer(Player player) {
    this.player.showPlayer(player);
  }

  public Component playerListName() {
    return player.playerListName();
  }

  /**
   * @param b
   * @deprecated
   */
  @Deprecated
  public void setSwimming(boolean b) {
    player.setSwimming(b);
  }

  @NotNull
  public EquipmentSlot getActiveItemHand() {
    return player.getActiveItemHand();
  }

  @Nullable
  public Entity getVehicle() {
    return player.getVehicle();
  }

  /**
   * @param entity
   * @deprecated
   */
  @Deprecated(since = "1.12")
  public void setShoulderEntityLeft(@Nullable Entity entity) {
    player.setShoulderEntityLeft(entity);
  }

  public void sendHealthUpdate(double v, int i, float v1) {
    player.sendHealthUpdate(v, i, v1);
  }

  @NotNull
  public CompletableFuture<Boolean> teleportAsync(@NotNull Location location,
                                                  PlayerTeleportEvent.@NotNull TeleportCause teleportCause,
                                                  @NotNull TeleportFlag @NotNull ... teleportFlags) {
    return player.teleportAsync(location, teleportCause, teleportFlags);
  }

  public void setInvisible(boolean b) {
    player.setInvisible(b);
  }

  /**
   * @param i
   * @param i1
   * @param i2
   * @deprecated
   */
  @Deprecated
  public void setTitleTimes(int i, int i1, int i2) {
    player.setTitleTimes(i, i1, i2);
  }

  public void setTotalExperience(int i) {
    player.setTotalExperience(i);
  }

  public int getPing() {
    return player.getPing();
  }

  public boolean isListed(Player player) {
    return this.player.isListed(player);
  }

  @Nullable
  public Component customName() {
    return player.customName();
  }

  @NotNull
  public SpawnCategory getSpawnCategory() {
    return player.getSpawnCategory();
  }

  @Nullable
  public WeatherType getPlayerWeather() {
    return player.getPlayerWeather();
  }

  public void lookAt(Entity entity, LookAnchor lookAnchor, LookAnchor lookAnchor1) {
    player.lookAt(entity, lookAnchor, lookAnchor1);
  }

  @NotNull
  public ItemStack getActiveItem() {
    return player.getActiveItem();
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.21.5")
  public boolean isInWaterOrRainOrBubbleColumn() {
    return player.isInWaterOrRainOrBubbleColumn();
  }

  public boolean canSee(Player player) {
    return this.player.canSee(player);
  }

  @NotNull
  public TriState permissionValue(@NotNull String permission) {
    return player.permissionValue(permission);
  }

  public void setTicksLived(int i) {
    player.setTicksLived(i);
  }

  public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4,
                                double v5, double v6, @Nullable T t) {
    player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6, t);
  }

  public void sendRichMessage(@NotNull String message) {
    player.sendRichMessage(message);
  }

  public boolean teleport(@NotNull Location location, PlayerTeleportEvent.@NotNull TeleportCause teleportCause) {
    return player.teleport(location, teleportCause);
  }

  public int getMaxFreezeTicks() {
    return player.getMaxFreezeTicks();
  }

  @ApiStatus.Experimental
  @NotNull
  public Entity copy(@NotNull Location location) {
    return player.copy(location);
  }

  @ApiStatus.Experimental
  @Contract(pure = true)
  public <T> @Nullable T getData(DataComponentType.Valued<T> valued) {
    return player.getData(valued);
  }

  public void sendHurtAnimation(float v) {
    player.sendHurtAnimation(v);
  }

  /**
   * @param uuid
   * @param s
   * @param bytes
   * @param s1
   * @param b
   * @deprecated
   */
  @Deprecated
  public void setResourcePack(UUID uuid, String s, byte @Nullable [] bytes, @Nullable String s1, boolean b) {
    player.setResourcePack(uuid, s, bytes, s1, b);
  }

  public void setMetadata(@NotNull String s, @NotNull MetadataValue metadataValue) {
    player.setMetadata(s, metadataValue);
  }

  public void setHurtDirection(float v) {
    player.setHurtDirection(v);
  }

  public void setSendViewDistance(int i) {
    player.setSendViewDistance(i);
  }

  public boolean addPotionEffect(@NotNull PotionEffect potionEffect) {
    return player.addPotionEffect(potionEffect);
  }

  @Nullable
  public Sound getDeathSound() {
    return player.getDeathSound();
  }

  public int getBeeStingerCooldown() {
    return player.getBeeStingerCooldown();
  }

  /**
   * @param player
   * @deprecated
   */
  @Deprecated(since = "1.12.2")
  public void hidePlayer(Player player) {
    this.player.hidePlayer(player);
  }

  /**
   * @deprecated
   */
  @Deprecated
  public String getPlayerListName() {
    return player.getPlayerListName();
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.9")
  public ItemStack getItemInHand() {
    return player.getItemInHand();
  }

  /**
   * @param reason
   * @param source
   * @deprecated
   */
  @Deprecated(since = "1.20.4")
  public BanEntry banPlayer(@Nullable String reason, @Nullable String source) {
    return player.banPlayer(reason, source);
  }

  public void setBeeStingersInBody(int i) {
    player.setBeeStingersInBody(i);
  }

  public int getLevel() {
    return player.getLevel();
  }

  @Nullable
  public InetSocketAddress getVirtualHost() {
    return player.getVirtualHost();
  }

  public void decrementStatistic(Statistic statistic, EntityType entityType, int i) {
    player.decrementStatistic(statistic, entityType, i);
  }

  public void heal(double v, EntityRegainHealthEvent.@NotNull RegainReason regainReason) {
    player.heal(v, regainReason);
  }

  public boolean undiscoverRecipe(NamespacedKey namespacedKey) {
    return player.undiscoverRecipe(namespacedKey);
  }

  public int getFoodLevel() {
    return player.getFoodLevel();
  }

  public void setStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {
    player.setStatistic(statistic, material, i);
  }

  /**
   * @param loc
   * @param lines
   * @param dyeColor
   * @deprecated
   */
  @Deprecated
  public void sendSignChange(Location loc, @Nullable List<? extends Component> lines, DyeColor dyeColor) throws IllegalArgumentException {
    player.sendSignChange(loc, lines, dyeColor);
  }

  public boolean hasPlayedBefore() {
    return player.hasPlayedBefore();
  }

  @ApiStatus.Experimental
  public void startUsingItem(@NotNull EquipmentSlot equipmentSlot) {
    player.startUsingItem(equipmentSlot);
  }

  public void setVelocity(@NotNull Vector vector) {
    player.setVelocity(vector);
  }

  /**
   * @param reason
   * @param expires
   * @param source
   * @deprecated
   */
  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason, @Nullable Date expires, @Nullable String source) {
    return player.banPlayerIP(reason, expires, source);
  }

  /**
   * @param reason
   * @param expires
   * @param source
   * @param kickIfOnline
   * @deprecated
   */
  @Deprecated(since = "1.20.4")
  public BanEntry banPlayer(@Nullable String reason, @Nullable Date expires, @Nullable String source,
                            boolean kickIfOnline) {
    return player.banPlayer(reason, expires, source, kickIfOnline);
  }

  public void storeCookie(NamespacedKey namespacedKey, byte[] bytes) {
    player.storeCookie(namespacedKey, bytes);
  }

  public boolean isPlayerTimeRelative() {
    return player.isPlayerTimeRelative();
  }

  @NotNull
  public BlockFace getFacing() {
    return player.getFacing();
  }

  public void broadcastHurtAnimation(@NotNull Collection<Player> collection) {
    player.broadcastHurtAnimation(collection);
  }

  /**
   * @deprecated
   */
  @Deprecated
  public int getNoTickViewDistance() {
    return player.getNoTickViewDistance();
  }

  public void setResourcePack(UUID uuid, String url, String hash, @Nullable Component resourcePackPrompt,
                              boolean required) {
    player.setResourcePack(uuid, url, hash, resourcePackPrompt, required);
  }

  public <E extends BanEntry<? super PlayerProfile>> @Nullable E ban(@Nullable String s, @Nullable Duration duration,
                                                                     @Nullable String s1) {
    return player.ban(s, duration, s1);
  }

  public void registerAttribute(@NotNull Attribute attribute) {
    player.registerAttribute(attribute);
  }

  public void removeResourcePacks(@NotNull ResourcePackRequest request) {
    player.removeResourcePacks(request);
  }

  public boolean performCommand(String s) {
    return player.performCommand(s);
  }

  public boolean teleport(@NotNull Entity entity, PlayerTeleportEvent.@NotNull TeleportCause teleportCause) {
    return player.teleport(entity, teleportCause);
  }

  public boolean isGlowing() {
    return player.isGlowing();
  }

  public void setCompassTarget(Location location) {
    player.setCompassTarget(location);
  }

  public void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
    player.sendPlayerListHeaderAndFooter(header, footer);
  }

  public TriState getFrictionState() {
    return player.getFrictionState();
  }

  public void swingOffHand() {
    player.swingOffHand();
  }

  public int getWardenWarningLevel() {
    return player.getWardenWarningLevel();
  }

  @ApiStatus.Experimental
  public Collection<EnderPearl> getEnderPearls() {
    return player.getEnderPearls();
  }

  public PlayerGiveResult give(Collection<ItemStack> collection, boolean b) {
    return player.give(collection, b);
  }

  public double getEyeHeight(boolean b) {
    return player.getEyeHeight(b);
  }

  public float getFlySpeed() {
    return player.getFlySpeed();
  }

  @Nullable
  public Item dropItem(EquipmentSlot slot, int amount) {
    return player.dropItem(slot, amount);
  }

  @Nullable
  public InetSocketAddress getHAProxyAddress() {
    return player.getHAProxyAddress();
  }

  public void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5) {
    player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5);
  }

  public void setPose(@NotNull Pose pose, boolean b) {
    player.setPose(pose, b);
  }

  public boolean teleport(@NotNull Location location, PlayerTeleportEvent.@NotNull TeleportCause teleportCause,
                          @NotNull TeleportFlag @NotNull ... teleportFlags) {
    return player.teleport(location, teleportCause, teleportFlags);
  }

  /**
   * @deprecated
   */
  @Deprecated
  @Nullable
  public String getPlayerListFooter() {
    return player.getPlayerListFooter();
  }

  public void acceptConversationInput(@NotNull String s) {
    player.acceptConversationInput(s);
  }

  public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {
    player.decrementStatistic(statistic);
  }

  /**
   * @param location
   * @param b
   * @deprecated
   */
  @Deprecated(since = "1.21.4")
  @Nullable
  public InventoryView openSmithingTable(@Nullable Location location, boolean b) {
    return player.openSmithingTable(location, b);
  }

  public void setSaturatedRegenRate(int i) {
    player.setSaturatedRegenRate(i);
  }

  public void playSound(net.kyori.adventure.sound.@NotNull Sound sound,
                        net.kyori.adventure.sound.Sound.@NotNull Emitter emitter) {
    player.playSound(sound, emitter);
  }

  @ApiStatus.Obsolete(since = "1.20.4")
  @NotNull
  public EquipmentSlot getHandRaised() {
    return player.getHandRaised();
  }

  public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
    player.setScoreboard(scoreboard);
  }

  /**
   * @param source
   * @param message
   * @deprecated
   */
  @Deprecated
  public void sendMessage(@NotNull Identified source, @NotNull ComponentLike message) {
    player.sendMessage(source, message);
  }

  public double getWidth() {
    return player.getWidth();
  }

  public boolean hasNoPhysics() {
    return player.hasNoPhysics();
  }

  public boolean isSilent() {
    return player.isSilent();
  }

  public AdvancementProgress getAdvancementProgress(Advancement advancement) {
    return player.getAdvancementProgress(advancement);
  }

  public int getSleepTicks() {
    return player.getSleepTicks();
  }

  public void setPlayerTime(long l, boolean b) {
    player.setPlayerTime(l, b);
  }

  /**
   * @param location
   * @deprecated
   */
  @Deprecated(since = "1.20.4")
  public void setBedSpawnLocation(@Nullable Location location) {
    player.setBedSpawnLocation(location);
  }

  public int getMaxFireTicks() {
    return player.getMaxFireTicks();
  }

  public boolean fromMobSpawner() {
    return player.fromMobSpawner();
  }

  public <T> @UnknownNullability T getOrDefaultFrom(@NotNull Pointer<T> pointer,
                                                    @NotNull Supplier<? extends T> defaultValue) {
    return player.getOrDefaultFrom(pointer, defaultValue);
  }

  @NotNull
  public Sound getFallDamageSound(int i) {
    return player.getFallDamageSound(i);
  }

  @NotNull
  public Server getServer() {
    return player.getServer();
  }

  public Duration getIdleDuration() {
    return player.getIdleDuration();
  }

  public void addResourcePack(UUID uuid, String s, byte @Nullable [] bytes, @Nullable String s1, boolean b) {
    player.addResourcePack(uuid, s, bytes, s1, b);
  }

  public int getNoActionTicks() {
    return player.getNoActionTicks();
  }

  public void sendResourcePacks(@NotNull ResourcePackRequest request) {
    player.sendResourcePacks(request);
  }

  public <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> aClass,
                                                            @Nullable Vector vector,
                                                            @Nullable Consumer<? super T> consumer) {
    return player.launchProjectile(aClass, vector, consumer);
  }

  public boolean getCanPickupItems() {
    return player.getCanPickupItems();
  }

  @Contract("_, null -> _; _, !null -> !null")
  public <T> @org.jetbrains.annotations.Nullable T getOrDefault(@NotNull Pointer<T> pointer, @Nullable T defaultValue) {
    return player.getOrDefault(pointer, defaultValue);
  }

  public boolean addPotionEffects(@NotNull Collection<PotionEffect> collection) {
    return player.addPotionEffects(collection);
  }

  public boolean isUnderWater() {
    return player.isUnderWater();
  }

  public void setBodyYaw(float v) {
    player.setBodyYaw(v);
  }

  public boolean isBlocking() {
    return player.isBlocking();
  }

  @NotNull
  public List<Entity> getPassengers() {
    return player.getPassengers();
  }

  @NotNull
  public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b) {
    return player.addAttachment(plugin, s, b);
  }

  public int getStarvationRate() {
    return player.getStarvationRate();
  }

  public int getMaximumNoDamageTicks() {
    return player.getMaximumNoDamageTicks();
  }

  public int getEnchantmentSeed() {
    return player.getEnchantmentSeed();
  }

  @Nullable
  public FishHook getFishHook() {
    return player.getFishHook();
  }

  /**
   * @param location
   * @param b
   * @deprecated
   */
  @Deprecated(since = "1.21.4")
  @Nullable
  public InventoryView openCartographyTable(@Nullable Location location, boolean b) {
    return player.openCartographyTable(location, b);
  }

  public void setCustomChatCompletions(Collection<String> collection) {
    player.setCustomChatCompletions(collection);
  }

  /**
   * @param location
   * @param b
   * @deprecated
   */
  @Deprecated(since = "1.20.4")
  public void setBedSpawnLocation(@Nullable Location location, boolean b) {
    player.setBedSpawnLocation(location, b);
  }

  @NotNull
  public ItemStack getPickItemStack() {
    return player.getPickItemStack();
  }

  public boolean isTrackedBy(@NotNull Player player) {
    return this.player.isTrackedBy(player);
  }

  public long getFirstPlayed() {
    return player.getFirstPlayed();
  }

  public int getCooldown(ItemStack itemStack) {
    return player.getCooldown(itemStack);
  }

  public void decrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {
    player.decrementStatistic(statistic, material, i);
  }

  public void setItemOnCursor(@Nullable ItemStack itemStack) {
    player.setItemOnCursor(itemStack);
  }

  public void setHealthScale(double v) throws IllegalArgumentException {
    player.setHealthScale(v);
  }

  public void setAllowFlight(boolean b) {
    player.setAllowFlight(b);
  }

  public void incrementStatistic(Statistic statistic, EntityType entityType, int i) throws IllegalArgumentException {
    player.incrementStatistic(statistic, entityType, i);
  }

  public PlayerResourcePackStatusEvent.@Nullable Status getResourcePackStatus() {
    return player.getResourcePackStatus();
  }

  public Location getBedLocation() {
    return player.getBedLocation();
  }

  public void setViewDistance(int i) {
    player.setViewDistance(i);
  }

  public void remove() {
    player.remove();
  }

  public void sendMessage(@NotNull ComponentLike message, ChatType.@NotNull Bound boundChatType) {
    player.sendMessage(message, boundChatType);
  }

  public <T> T getClientOption(ClientOption<T> clientOption) {
    return player.getClientOption(clientOption);
  }

  public void giveExpLevels(int i) {
    player.giveExpLevels(i);
  }

  public void sendEquipmentChange(LivingEntity livingEntity, Map<EquipmentSlot, @Nullable ItemStack> map) {
    player.sendEquipmentChange(livingEntity, map);
  }

  public void setSleepingIgnored(boolean b) {
    player.setSleepingIgnored(b);
  }

  public <T> void spawnParticle(Particle particle, Location location, int i, @Nullable T t) {
    player.spawnParticle(particle, location, i, t);
  }

  public void setGravity(boolean b) {
    player.setGravity(b);
  }

  public int getArrowCooldown() {
    return player.getArrowCooldown();
  }

  public void setLastDamage(double v) {
    player.setLastDamage(v);
  }

  @NotNull
  public CompletableFuture<Boolean> teleportAsync(@NotNull Location loc,
                                                  PlayerTeleportEvent.@NotNull TeleportCause cause) {
    return player.teleportAsync(loc, cause);
  }

  @NotNull
  public UUID getUniqueId() {
    return player.getUniqueId();
  }

  /**
   * @param components
   * @deprecated
   */
  @Deprecated
  public void sendMessage(BaseComponent... components) {
    player.sendMessage(components);
  }

  /**
   * @param s
   * @param s1
   * @param i
   * @param i1
   * @param i2
   * @deprecated
   */
  @Deprecated
  public void sendTitle(@Nullable String s, @Nullable String s1, int i, int i1, int i2) {
    player.sendTitle(s, s1, i, i1, i2);
  }

  public Set<NamespacedKey> getDiscoveredRecipes() {
    return player.getDiscoveredRecipes();
  }

  public void setArrowsInBody(int count) {
    player.setArrowsInBody(count);
  }

  /**
   * @param baseComponent
   * @param baseComponent1
   * @param i
   * @param i1
   * @param i2
   * @deprecated
   */
  @Deprecated
  public void showTitle(@Nullable BaseComponent baseComponent, @Nullable BaseComponent baseComponent1, int i, int i1,
                        int i2) {
    player.showTitle(baseComponent, baseComponent1, i, i1, i2);
  }

  public Component displayName() {
    return player.displayName();
  }

  public boolean isSneaking() {
    return player.isSneaking();
  }

  public void removeMetadata(@NotNull String s, @NotNull Plugin plugin) {
    player.removeMetadata(s, plugin);
  }

  public void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2) {
    player.spawnParticle(particle, location, i, v, v1, v2);
  }

  public void setLastDeathLocation(@Nullable Location location) {
    player.setLastDeathLocation(location);
  }

  @Nullable
  public InventoryView openInventory(Inventory inventory) {
    return player.openInventory(inventory);
  }

  @NotNull
  public Sound getSwimSound() {
    return player.getSwimSound();
  }

  public boolean isPermissionSet(@NotNull String s) {
    return player.isPermissionSet(s);
  }

  /**
   * @deprecated
   */
  @Deprecated
  public String getDisplayName() {
    return player.getDisplayName();
  }

  public void kick(@Nullable Component component, PlayerKickEvent.Cause cause) {
    player.kick(component, cause);
  }

  /**
   * @param source
   * @param message
   * @deprecated
   */
  @Deprecated
  public void sendMessage(@NotNull Identity source, @NotNull ComponentLike message) {
    player.sendMessage(source, message);
  }

  public void removeResourcePack(UUID uuid) {
    player.removeResourcePack(uuid);
  }

  public void spawnParticle(Particle particle, double v, double v1, double v2, int i) {
    player.spawnParticle(particle, v, v1, v2, i);
  }

  public void setSprinting(boolean b) {
    player.setSprinting(b);
  }

  /**
   * @param reason
   * @deprecated
   */
  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerFull(@Nullable String reason) {
    return player.banPlayerFull(reason);
  }

  public boolean isBanned() {
    return player.isBanned();
  }

  public boolean isWhitelisted() {
    return player.isWhitelisted();
  }

  public void sendPluginMessage(@NotNull Plugin plugin, @NotNull String s, byte @NotNull [] bytes) {
    player.sendPluginMessage(plugin, s, bytes);
  }

  @NotNull
  public TriState getVisualFire() {
    return player.getVisualFire();
  }

  /**
   * @param baseComponents
   * @deprecated
   */
  @Deprecated
  public void showTitle(@Nullable BaseComponent[] baseComponents) {
    player.showTitle(baseComponents);
  }

  @Nullable
  public RayTraceResult rayTraceBlocks(double v) {
    return player.rayTraceBlocks(v);
  }

  public void setSpectatorTarget(@Nullable Entity entity) {
    player.setSpectatorTarget(entity);
  }

  /**
   * @param villager
   * @param b
   * @deprecated
   */
  @Deprecated(since = "1.21.4")
  @Nullable
  public InventoryView openMerchant(Villager villager, boolean b) {
    return player.openMerchant(villager, b);
  }

  @Nullable
  public Component playerListFooter() {
    return player.playerListFooter();
  }

  public <T> void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2,
                                double v3, @Nullable T t, boolean b) {
    player.spawnParticle(particle, location, i, v, v1, v2, v3, t, b);
  }

  @NotNull
  public List<MetadataValue> getMetadata(@NotNull String s) {
    return player.getMetadata(s);
  }

  @NotNull
  public EntityScheduler getScheduler() {
    return player.getScheduler();
  }

  @Nullable
  public InetSocketAddress getAddress() {
    return player.getAddress();
  }

  public void giveExp(int amount) {
    player.giveExp(amount);
  }

  /**
   * @param collection
   * @deprecated
   */
  @Deprecated(since = "1.20.1")
  public void removeAdditionalChatCompletions(Collection<String> collection) {
    player.removeAdditionalChatCompletions(collection);
  }

  public boolean isInsideVehicle() {
    return player.isInsideVehicle();
  }

  public TriState hasFlyingFallDamage() {
    return player.hasFlyingFallDamage();
  }

  public void removeResourcePacks(@NotNull UUID id, @NotNull UUID @NotNull ... others) {
    player.removeResourcePacks(id, others);
  }

  public int getProtocolVersion() {
    return player.getProtocolVersion();
  }

  public void playSound(Location location, Sound sound, SoundCategory soundCategory, float v, float v1) {
    player.playSound(location, sound, soundCategory, v, v1);
  }

  public void increaseWardenWarningLevel() {
    player.increaseWardenWarningLevel();
  }

  public int getActiveItemRemainingTime() {
    return player.getActiveItemRemainingTime();
  }

  @Nullable
  public Block getTargetBlockExact(int i, @NotNull FluidCollisionMode fluidCollisionMode) {
    return player.getTargetBlockExact(i, fluidCollisionMode);
  }

  /**
   * @param location
   * @param b
   * @deprecated
   */
  @Deprecated(since = "1.21.4")
  @Nullable
  public InventoryView openEnchanting(@Nullable Location location, boolean b) {
    return player.openEnchanting(location, b);
  }

  @Nullable
  public BanEntry<InetAddress> banIp(@Nullable String s, @Nullable Duration duration, @Nullable String s1, boolean b) {
    return player.banIp(s, duration, s1, b);
  }

  public long getPlayerTimeOffset() {
    return player.getPlayerTimeOffset();
  }

  public MainHand getMainHand() {
    return player.getMainHand();
  }

  public void sendPlayerListHeader(@NotNull Component header) {
    player.sendPlayerListHeader(header);
  }

  /**
   * @param location
   * @param b
   * @param b1
   * @deprecated
   */
  @Deprecated(since = "1.6.2")
  public void playNote(Location location, byte b, byte b1) {
    player.playNote(location, b, b1);
  }

  public void setFrictionState(TriState triState) {
    player.setFrictionState(triState);
  }

  public int getActiveItemUsedTime() {
    return player.getActiveItemUsedTime();
  }

  /**
   * @param reason
   * @param expires
   * @deprecated
   */
  @Deprecated(since = "1.20.4")
  public BanEntry banPlayer(@Nullable String reason, @Nullable Date expires) {
    return player.banPlayer(reason, expires);
  }

  public void setVisualFire(@NotNull TriState triState) {
    player.setVisualFire(triState);
  }

  public void sendOpLevel(byte b) {
    player.sendOpLevel(b);
  }

  public void sendActionBar(@NotNull Component message) {
    player.sendActionBar(message);
  }

  public boolean isChunkSent(long l) {
    return player.isChunkSent(l);
  }

  public void openBook(Book.@NotNull Builder book) {
    player.openBook(book);
  }

  public boolean isInvisible() {
    return player.isInvisible();
  }

  public Locale locale() {
    return player.locale();
  }

  public <E extends BanEntry<? super PlayerProfile>> @Nullable E ban(@Nullable String s, @Nullable Instant instant,
                                                                     @Nullable String s1) {
    return player.ban(s, instant, s1);
  }

  public boolean canSee(Entity entity) {
    return player.canSee(entity);
  }

  public void playSound(Location location, String s, SoundCategory soundCategory, float v, float v1, long l) {
    player.playSound(location, s, soundCategory, v, v1, l);
  }

  public void stopSound(String s, @Nullable SoundCategory soundCategory) {
    player.stopSound(s, soundCategory);
  }

  public void setInvulnerable(boolean b) {
    player.setInvulnerable(b);
  }

  public boolean isSwimming() {
    return player.isSwimming();
  }

  /**
   * @param itemStack
   * @deprecated
   */
  @Deprecated(since = "1.9")
  public void setItemInHand(@Nullable ItemStack itemStack) {
    player.setItemInHand(itemStack);
  }

  public void playSound(Entity entity, Sound sound, float v, float v1) {
    player.playSound(entity, sound, v, v1);
  }

  public void setPlayerWeather(WeatherType weatherType) {
    player.setPlayerWeather(weatherType);
  }

  public void playSound(Location location, String s, SoundCategory soundCategory, float v, float v1) {
    player.playSound(location, s, soundCategory, v, v1);
  }

  public void incrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {
    player.incrementStatistic(statistic, material, i);
  }

  public void playerListName(@Nullable Component component) {
    player.playerListName(component);
  }

  @Contract("null -> null; !null -> !null")
  @Nullable
  public Location getLocation(@Nullable Location location) {
    return player.getLocation(location);
  }

  public void playEffect(@NotNull EntityEffect entityEffect) {
    player.playEffect(entityEffect);
  }

  public void setCooldown(Material material, int i) {
    player.setCooldown(material, i);
  }

  public void stopSound(net.kyori.adventure.sound.@NotNull Sound sound) {
    player.stopSound(sound);
  }

  /**
   * @param s
   * @deprecated
   */
  @Deprecated
  public void kickPlayer(@Nullable String s) {
    player.kickPlayer(s);
  }

  @Nullable
  public Location getRespawnLocation() {
    return player.getRespawnLocation();
  }

  public int getRemainingAir() {
    return player.getRemainingAir();
  }

  public boolean isRiptiding() {
    return player.isRiptiding();
  }

  public boolean isFreezeTickingLocked() {
    return player.isFreezeTickingLocked();
  }

  public void setSilent(boolean b) {
    player.setSilent(b);
  }

  public void playSound(Location location, Sound sound, SoundCategory soundCategory, float v, float v1, long l) {
    player.playSound(location, sound, soundCategory, v, v1, l);
  }

  public void setFireTicks(int i) {
    player.setFireTicks(i);
  }

  @NotNull
  public Chunk getChunk() {
    return player.getChunk();
  }

  @Nullable
  public GameMode getPreviousGameMode() {
    return player.getPreviousGameMode();
  }

  /**
   * @param s
   * @deprecated
   */
  @Deprecated(since = "1.7.2")
  public void setTexturePack(String s) {
    player.setTexturePack(s);
  }

  @ApiStatus.Experimental
  public void sendBlockUpdate(Location location, TileState tileState) throws IllegalArgumentException {
    player.sendBlockUpdate(location, tileState);
  }

  @NotNull
  public BoundingBox getBoundingBox() {
    return player.getBoundingBox();
  }

  public void playSound(net.kyori.adventure.sound.@NotNull Sound sound) {
    player.playSound(sound);
  }

  @NotNull
  public List<Block> getLastTwoTargetBlocks(@Nullable Set<Material> set, int i) {
    return player.getLastTwoTargetBlocks(set, i);
  }

  public void setOp(boolean b) {
    player.setOp(b);
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.12")
  @Nullable
  public Entity getShoulderEntityRight() {
    return player.getShoulderEntityRight();
  }

  public CompletableFuture<byte @Nullable []> retrieveCookie(NamespacedKey namespacedKey) {
    return player.retrieveCookie(namespacedKey);
  }

  @ApiStatus.Experimental
  @Contract(pure = true)
  public boolean hasData(DataComponentType dataComponentType) {
    return player.hasData(dataComponentType);
  }

  /**
   * @param s
   * @param bytes
   * @param b
   * @deprecated
   */
  @Deprecated
  public void setResourcePack(String s, byte @Nullable [] bytes, boolean b) {
    player.setResourcePack(s, bytes, b);
  }

  public boolean hasResourcePack() {
    return player.hasResourcePack();
  }

  public void setExp(float v) {
    player.setExp(v);
  }

  public boolean teleport(@NotNull Location location) {
    return player.teleport(location);
  }

  @Nullable
  public Entity getSpectatorTarget() {
    return player.getSpectatorTarget();
  }

  /**
   * @param blocks
   * @param suppressLightUpdates
   * @deprecated
   */
  @Deprecated(since = "1.20")
  public void sendBlockChanges(Collection<BlockState> blocks, boolean suppressLightUpdates) {
    player.sendBlockChanges(blocks, suppressLightUpdates);
  }

  public boolean isClimbing() {
    return player.isClimbing();
  }

  public void damage(double v, @Nullable Entity entity) {
    player.damage(v, entity);
  }

  public void setSimulationDistance(int i) {
    player.setSimulationDistance(i);
  }

  public float getExhaustion() {
    return player.getExhaustion();
  }

  public void sendBlockDamage(Location location, float v, int i) {
    player.sendBlockDamage(location, v, i);
  }

  public void setWorldBorder(@Nullable WorldBorder worldBorder) {
    player.setWorldBorder(worldBorder);
  }

  public void hidePlayer(Plugin plugin, Player player) {
    this.player.hidePlayer(plugin, player);
  }

  public boolean isCollidable() {
    return player.isCollidable();
  }

  public int getStatistic(Statistic statistic) throws IllegalArgumentException {
    return player.getStatistic(statistic);
  }

  public boolean isSprinting() {
    return player.isSprinting();
  }

  /**
   * @param potionEffect
   * @param b
   * @deprecated
   */
  @Deprecated(since = "1.15.2")
  public boolean addPotionEffect(@NotNull PotionEffect potionEffect, boolean b) {
    return player.addPotionEffect(potionEffect, b);
  }

  public void setMaximumAir(int i) {
    player.setMaximumAir(i);
  }

  @Nullable
  public Entity releaseRightShoulderEntity() {
    return player.releaseRightShoulderEntity();
  }

  public void startRiptideAttack(int i, float v, @Nullable ItemStack itemStack) {
    player.startRiptideAttack(i, v, itemStack);
  }

  public void attack(@NotNull Entity entity) {
    player.attack(entity);
  }

  /**
   * @deprecated
   */
  @Deprecated
  public int getArrowsStuck() {
    return player.getArrowsStuck();
  }

  public void playPickupItemAnimation(@NotNull Item item) {
    player.playPickupItemAnimation(item);
  }

  public boolean getAllowFlight() {
    return player.getAllowFlight();
  }

  public void setWalkSpeed(float v) throws IllegalArgumentException {
    player.setWalkSpeed(v);
  }

  public void chat(String s) {
    player.chat(s);
  }

  public Scoreboard getScoreboard() {
    return player.getScoreboard();
  }

  @ApiStatus.Experimental
  @Unmodifiable
  public Set<Long> getSentChunkKeys() {
    return player.getSentChunkKeys();
  }

  public void showPlayer(Plugin plugin, Player player) {
    this.player.showPlayer(plugin, player);
  }

  public boolean isInRain() {
    return player.isInRain();
  }

  public void loadData() {
    player.loadData();
  }

  public boolean isDead() {
    return player.isDead();
  }

  public int applyMending(int i) {
    return player.applyMending(i);
  }

  public void closeInventory(InventoryCloseEvent.Reason reason) {
    player.closeInventory(reason);
  }

  public void setSaturation(float v) {
    player.setSaturation(v);
  }

  public void setRespawnLocation(@Nullable Location location, boolean b) {
    player.setRespawnLocation(location, b);
  }

  public boolean isTicking() {
    return player.isTicking();
  }

  public void setResourcePack(String url, String hash, boolean required) {
    player.setResourcePack(url, hash, required);
  }

  @UnmodifiableView
  public Iterable<? extends BossBar> activeBossBars() {
    return player.activeBossBars();
  }

  public long getLastSeen() {
    return player.getLastSeen();
  }

  public int discoverRecipes(Collection<NamespacedKey> collection) {
    return player.discoverRecipes(collection);
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.21.5")
  public boolean isInWaterOrBubbleColumn() {
    return player.isInWaterOrBubbleColumn();
  }

  public int getEntityId() {
    return player.getEntityId();
  }

  public String getName() {
    return player.getName();
  }

  @ApiStatus.OverrideOnly
  @NotNull
  public Map<String, Object> serialize() {
    return player.serialize();
  }

  public void setAbsorptionAmount(double v) {
    player.setAbsorptionAmount(v);
  }

  public void setPose(@NotNull Pose pose) {
    player.setPose(pose);
  }

  public void playSound(Entity entity, String s, float v, float v1) {
    player.playSound(entity, s, v, v1);
  }

  public boolean isEmpty() {
    return player.isEmpty();
  }

  public void removeResourcePacks(@NotNull ResourcePackRequestLike request) {
    player.removeResourcePacks(request);
  }

  /**
   * @param location
   * @param b
   * @deprecated
   */
  @Deprecated(since = "1.21.4")
  @Nullable
  public InventoryView openStonecutter(@Nullable Location location, boolean b) {
    return player.openStonecutter(location, b);
  }

  public void setRespawnLocation(@Nullable Location location) {
    player.setRespawnLocation(location);
  }

  @NotNull
  public Sound getFallDamageSoundSmall() {
    return player.getFallDamageSoundSmall();
  }

  @ApiStatus.Experimental
  @Nullable
  public String getAsString() {
    return player.getAsString();
  }

  public float getBodyYaw() {
    return player.getBodyYaw();
  }

  @Nullable
  public Item dropItem(EquipmentSlot slot) {
    return player.dropItem(slot);
  }

  public void kick(@Nullable Component message) {
    player.kick(message);
  }

  public void setRotation(float v, float v1) {
    player.setRotation(v, v1);
  }

  /**
   * @param i
   * @deprecated
   */
  @Deprecated
  public void setArrowsStuck(int i) {
    player.setArrowsStuck(i);
  }

  public void setWardenTimeSinceLastWarning(int i) {
    player.setWardenTimeSinceLastWarning(i);
  }

  public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4,
                                double v5, @Nullable T t) {
    player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, t);
  }

  /**
   * @param s
   * @deprecated
   */
  @Deprecated
  public void setPlayerListName(@Nullable String s) {
    player.setPlayerListName(s);
  }

  public void removeCustomChatCompletions(Collection<String> collection) {
    player.removeCustomChatCompletions(collection);
  }

  public void resetCooldown() {
    player.resetCooldown();
  }

  public boolean teleport(@NotNull Entity entity) {
    return player.teleport(entity);
  }

  @Nullable
  public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b, int i) {
    return player.addAttachment(plugin, s, b, i);
  }

  public int getViewDistance() {
    return player.getViewDistance();
  }

  public PlayerGiveResult give(Collection<ItemStack> items) {
    return player.give(items);
  }

  public <E extends BanEntry<? super PlayerProfile>> @Nullable E ban(@Nullable String s, @Nullable Date date,
                                                                     @Nullable String s1) {
    return player.ban(s, date, s1);
  }

  public boolean hasLineOfSight(@NotNull Entity entity) {
    return player.hasLineOfSight(entity);
  }

  public void setCooldown(Key key, int i) {
    player.setCooldown(key, i);
  }

  public int getNextBeeStingerRemoval() {
    return player.getNextBeeStingerRemoval();
  }

  public void clearResourcePacks() {
    player.clearResourcePacks();
  }

  public int getWardenWarningCooldown() {
    return player.getWardenWarningCooldown();
  }

  public int getPortalCooldown() {
    return player.getPortalCooldown();
  }

  public void setShieldBlockingDelay(int i) {
    player.setShieldBlockingDelay(i);
  }

  @NotNull
  public PistonMoveReaction getPistonMoveReaction() {
    return player.getPistonMoveReaction();
  }

  public boolean hasPotionEffect(@NotNull PotionEffectType potionEffectType) {
    return player.hasPotionEffect(potionEffectType);
  }

  public double getHealth() {
    return player.getHealth();
  }

  /**
   * @param viewDistance
   * @deprecated
   */
  @Deprecated
  public void setNoTickViewDistance(int viewDistance) {
    player.setNoTickViewDistance(viewDistance);
  }

  @NotNull
  public String getScoreboardEntryName() {
    return player.getScoreboardEntryName();
  }

  /**
   * @param reason
   * @deprecated
   */
  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason) {
    return player.banPlayerIP(reason);
  }

  public void setPlayerProfile(PlayerProfile playerProfile) {
    player.setPlayerProfile(playerProfile);
  }

  public int getNextArrowRemoval() {
    return player.getNextArrowRemoval();
  }

  public boolean isPermissionSet(@NotNull Permission permission) {
    return player.isPermissionSet(permission);
  }

  public <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> aClass) {
    return player.launchProjectile(aClass);
  }

  @ApiStatus.Experimental
  public Input getCurrentInput() {
    return player.getCurrentInput();
  }

  public void setHasSeenWinScreen(boolean b) {
    player.setHasSeenWinScreen(b);
  }

  public int getBeeStingersInBody() {
    return player.getBeeStingersInBody();
  }

  public boolean canBreatheUnderwater() {
    return player.canBreatheUnderwater();
  }

  /**
   * @param sign
   * @deprecated
   */
  @Deprecated
  public void openSign(Sign sign) {
    player.openSign(sign);
  }

  public void sendResourcePacks(@NotNull ResourcePackRequestLike request) {
    player.sendResourcePacks(request);
  }

  /**
   * @param location
   * @param strings
   * @deprecated
   */
  @Deprecated
  public void sendSignChange(Location location, @Nullable String @Nullable [] strings) throws IllegalArgumentException {
    player.sendSignChange(location, strings);
  }

  public float getForwardsMovement() {
    return player.getForwardsMovement();
  }

  public boolean isFlying() {
    return player.isFlying();
  }

  public void removePotionEffect(@NotNull PotionEffectType potionEffectType) {
    player.removePotionEffect(potionEffectType);
  }

  /**
   * @param s
   * @deprecated
   */
  @Deprecated
  public void setPlayerListHeader(@Nullable String s) {
    player.setPlayerListHeader(s);
  }

  public int getExperiencePointsNeededForNextLevel() {
    return player.getExperiencePointsNeededForNextLevel();
  }

  public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
    return player.getStatistic(statistic, entityType);
  }

  public void lookAt(@NotNull Position position, @NotNull LookAnchor entityAnchor) {
    player.lookAt(position, entityAnchor);
  }

  public boolean hasMetadata(@NotNull String s) {
    return player.hasMetadata(s);
  }

  /**
   * @param reason
   * @param source
   * @deprecated
   */
  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerFull(@Nullable String reason, @Nullable String source) {
    return player.banPlayerFull(reason, source);
  }

  @NotNull
  public CompletableFuture<Boolean> teleportAsync(@NotNull Location loc) {
    return player.teleportAsync(loc);
  }

  @Nullable
  public Location getOrigin() {
    return player.getOrigin();
  }

  public void removeResourcePacks(@NotNull Iterable<UUID> ids) {
    player.removeResourcePacks(ids);
  }

  public boolean hasCooldown(ItemStack itemStack) {
    return player.hasCooldown(itemStack);
  }

  public void setHealthScaled(boolean b) {
    player.setHealthScaled(b);
  }

  public int getSaturatedRegenRate() {
    return player.getSaturatedRegenRate();
  }

  @ApiStatus.Experimental
  @Contract(value = "_, !null -> !null", pure = true)
  public <T> @Nullable T getDataOrDefault(DataComponentType.Valued<? extends T> valued, @Nullable T t) {
    return player.getDataOrDefault(valued, t);
  }

  public float getPitch() {
    return player.getPitch();
  }

  public void playSound(Entity entity, Sound sound, SoundCategory soundCategory, float v, float v1) {
    player.playSound(entity, sound, soundCategory, v, v1);
  }

  @Nullable
  public RayTraceResult rayTraceBlocks(double v, @NotNull FluidCollisionMode fluidCollisionMode) {
    return player.rayTraceBlocks(v, fluidCollisionMode);
  }

  public boolean isCustomNameVisible() {
    return player.isCustomNameVisible();
  }

  public void sendMap(MapView mapView) {
    player.sendMap(mapView);
  }

  /**
   * @param v
   * @deprecated
   */
  @Deprecated(since = "1.11")
  public void setMaxHealth(double v) {
    player.setMaxHealth(v);
  }

  @ApiStatus.Experimental
  public void openVirtualSign(Position position, Side side) {
    player.openVirtualSign(position, side);
  }

  /**
   * @param baseComponent
   * @deprecated
   */
  @Deprecated
  public void showTitle(@Nullable BaseComponent baseComponent) {
    player.showTitle(baseComponent);
  }

  public <E extends BanEntry<? super PlayerProfile>> @Nullable E ban(@Nullable String s, @Nullable Duration duration,
                                                                     @Nullable String s1, boolean b) {
    return player.ban(s, duration, s1, b);
  }

  public void sendPotionEffectChange(LivingEntity livingEntity, PotionEffect potionEffect) {
    player.sendPotionEffectChange(livingEntity, potionEffect);
  }

  public void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4, double v5
      , double v6) {
    player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6);
  }

  public int getWardenTimeSinceLastWarning() {
    return player.getWardenTimeSinceLastWarning();
  }

  public <T> void sendTitlePart(@NotNull TitlePart<T> part, @NotNull T value) {
    player.sendTitlePart(part, value);
  }

  public double getHeight() {
    return player.getHeight();
  }

  public boolean isInvulnerable() {
    return player.isInvulnerable();
  }

  @Nullable
  public Item dropItem(int i, int i1, boolean b, @Nullable Consumer<Item> consumer) {
    return player.dropItem(i, i1, b, consumer);
  }

  public void setCollidable(boolean b) {
    player.setCollidable(b);
  }

  public void sendMessage(@NotNull Component message, ChatType.@NotNull Bound boundChatType) {
    player.sendMessage(message, boundChatType);
  }

  public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
    player.decrementStatistic(statistic, entityType);
  }

  public boolean spawnAt(@NotNull Location location, CreatureSpawnEvent.@NotNull SpawnReason spawnReason) {
    return player.spawnAt(location, spawnReason);
  }

  @Nullable
  public String getClientBrandName() {
    return player.getClientBrandName();
  }

  /**
   * @param entity
   * @deprecated
   */
  @Deprecated(since = "1.12")
  public void setShoulderEntityRight(@Nullable Entity entity) {
    player.setShoulderEntityRight(entity);
  }

  public boolean wouldCollideUsing(@NotNull BoundingBox boundingBox) {
    return player.wouldCollideUsing(boundingBox);
  }

  public void saveData() {
    player.saveData();
  }

  public void sendBlockDamage(Location location, float v) {
    player.sendBlockDamage(location, v);
  }

  /**
   * @param location
   * @param b
   * @deprecated
   */
  @Deprecated(since = "1.21.4")
  @Nullable
  public InventoryView openGrindstone(@Nullable Location location, boolean b) {
    return player.openGrindstone(location, b);
  }

  public void setJumping(boolean b) {
    player.setJumping(b);
  }

  @ApiStatus.Obsolete
  public void sendMessage(@NotNull String... strings) {
    player.sendMessage(strings);
  }

  public void setUnsaturatedRegenRate(int i) {
    player.setUnsaturatedRegenRate(i);
  }

  public void sendPlayerListFooter(@NotNull ComponentLike footer) {
    player.sendPlayerListFooter(footer);
  }

  public void setGameMode(GameMode gameMode) {
    player.setGameMode(gameMode);
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.21.5")
  public boolean isInBubbleColumn() {
    return player.isInBubbleColumn();
  }

  @NotNull
  public List<Entity> getNearbyEntities(double v, double v1, double v2) {
    return player.getNearbyEntities(v, v1, v2);
  }

  public boolean isValid() {
    return player.isValid();
  }

  /**
   * @param s
   * @param s1
   * @deprecated
   */
  @Deprecated(since = "1.8.7")
  public void sendTitle(@Nullable String s, @Nullable String s1) {
    player.sendTitle(s, s1);
  }

  public void lookAt(double v, double v1, double v2, @NotNull LookAnchor lookAnchor) {
    player.lookAt(v, v1, v2, lookAnchor);
  }

  public void sendPlayerListHeader(@NotNull ComponentLike header) {
    player.sendPlayerListHeader(header);
  }

  public void playSound(Location location, Sound sound, float v, float v1) {
    player.playSound(location, sound, v, v1);
  }

  public void sendRawMessage(String s) {
    player.sendRawMessage(s);
  }

  public void setHealth(double v) {
    player.setHealth(v);
  }

  @Nullable
  public BanEntry<InetAddress> banIp(@Nullable String s, @Nullable Date date, @Nullable String s1, boolean b) {
    return player.banIp(s, date, s1, b);
  }

  public void setWardenWarningLevel(int i) {
    player.setWardenWarningLevel(i);
  }

  public void openBook(@NotNull Book book) {
    player.openBook(book);
  }

  /**
   * @deprecated
   */
  @Deprecated
  @NotNull
  public Set<Player> getTrackedPlayers() {
    return player.getTrackedPlayers();
  }

  /**
   * @param b
   * @deprecated
   */
  @Deprecated
  public void setVisualFire(boolean b) {
    player.setVisualFire(b);
  }

  public <T> void spawnParticle(Particle particle, double v, double v1, double v2, int i, double v3, double v4,
                                double v5, double v6, @Nullable T t, boolean b) {
    player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6, t, b);
  }

  @Nullable
  public Player getKiller() {
    return player.getKiller();
  }

  public boolean eject() {
    return player.eject();
  }

  public int getFireTicks() {
    return player.getFireTicks();
  }

  public ItemStack getItemOnCursor() {
    return player.getItemOnCursor();
  }

  public void sendBlockChanges(Collection<BlockState> collection) {
    player.sendBlockChanges(collection);
  }

  @Nullable
  public PotionEffect getPotionEffect(@NotNull PotionEffectType potionEffectType) {
    return player.getPotionEffect(potionEffectType);
  }

  @NotNull
  public Sound getEatingSound(@NotNull ItemStack itemStack) {
    return player.getEatingSound(itemStack);
  }

  public double getHealthScale() {
    return player.getHealthScale();
  }

  public boolean hasDiscoveredRecipe(NamespacedKey namespacedKey) {
    return player.hasDiscoveredRecipe(namespacedKey);
  }

  @Nullable
  public Item dropItem(ItemStack itemStack, boolean b, @Nullable Consumer<Item> consumer) {
    return player.dropItem(itemStack, b, consumer);
  }

  public boolean hasGravity() {
    return player.hasGravity();
  }

  public boolean isSleeping() {
    return player.isSleeping();
  }

  public int getSimulationDistance() {
    return player.getSimulationDistance();
  }

  public void stopSound(Sound sound, @Nullable SoundCategory soundCategory) {
    player.stopSound(sound, soundCategory);
  }

  /**
   * @param merchant
   * @param b
   * @deprecated
   */
  @Deprecated(since = "1.21.4")
  @Nullable
  public InventoryView openMerchant(Merchant merchant, boolean b) {
    return player.openMerchant(merchant, b);
  }

  /**
   * @param s
   * @deprecated
   */
  @Deprecated
  public void setResourcePack(String s) {
    player.setResourcePack(s);
  }

  public PlayerGiveResult give(ItemStack... items) {
    return player.give(items);
  }

  public void setSneaking(boolean b) {
    player.setSneaking(b);
  }

  public void sendActionBar(@NotNull ComponentLike message) {
    player.sendActionBar(message);
  }

  public void playSound(net.kyori.adventure.sound.@NotNull Sound sound, double x, double y, double z) {
    player.playSound(sound, x, y, z);
  }

  @NotNull
  public Sound getSwimHighSpeedSplashSound() {
    return player.getSwimHighSpeedSplashSound();
  }

  @Nullable
  public Location getLastDeathLocation() {
    return player.getLastDeathLocation();
  }

  public InventoryView getOpenInventory() {
    return player.getOpenInventory();
  }

  public void kick() {
    player.kick();
  }

  public double getLastDamage() {
    return player.getLastDamage();
  }

  public boolean isHealthScaled() {
    return player.isHealthScaled();
  }

  public void setAffectsSpawning(boolean b) {
    player.setAffectsSpawning(b);
  }

  public void setPortalCooldown(int i) {
    player.setPortalCooldown(i);
  }

  public void showEntity(Plugin plugin, Entity entity) {
    player.showEntity(plugin, entity);
  }

  @NotNull
  public Component teamDisplayName() {
    return player.teamDisplayName();
  }

  public float getCooledAttackStrength(float v) {
    return player.getCooledAttackStrength(v);
  }

  public void setFlySpeed(float v) throws IllegalArgumentException {
    player.setFlySpeed(v);
  }

  public void spawnParticle(Particle particle, Location location, int i, double v, double v1, double v2, double v3) {
    player.spawnParticle(particle, location, i, v, v1, v2, v3);
  }

  public void showElderGuardian(boolean b) {
    player.showElderGuardian(b);
  }

  public int getArrowsInBody() {
    return player.getArrowsInBody();
  }

  public void setCanPickupItems(boolean b) {
    player.setCanPickupItems(b);
  }

  public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
    return player.getStatistic(statistic, material);
  }

  public void openInventory(InventoryView inventoryView) {
    player.openInventory(inventoryView);
  }

  @NotNull
  public Component name() {
    return player.name();
  }

  public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {
    player.incrementStatistic(statistic);
  }

  public void setNoDamageTicks(int i) {
    player.setNoDamageTicks(i);
  }

  public void swingMainHand() {
    player.swingMainHand();
  }

  public void playPickupItemAnimation(@NotNull Item item, int i) {
    player.playPickupItemAnimation(item, i);
  }

  @Nullable
  public Entity getTargetEntity(int maxDistance) {
    return player.getTargetEntity(maxDistance);
  }

  public void abandonConversation(@NotNull Conversation conversation,
                                  @NotNull ConversationAbandonedEvent conversationAbandonedEvent) {
    player.abandonConversation(conversation, conversationAbandonedEvent);
  }

  public double getEyeHeight() {
    return player.getEyeHeight();
  }

  @NotNull
  public Entity getLeashHolder() throws IllegalStateException {
    return player.getLeashHolder();
  }

  public void broadcastSlotBreak(@NotNull EquipmentSlot equipmentSlot, @NotNull Collection<Player> collection) {
    player.broadcastSlotBreak(equipmentSlot, collection);
  }

  public boolean hasSeenWinScreen() {
    return player.hasSeenWinScreen();
  }

  @NotNull
  public Location getLocation() {
    return player.getLocation();
  }

  public boolean isConnected() {
    return player.isConnected();
  }

  public void displayName(@Nullable Component component) {
    player.displayName(component);
  }

  public void setStatistic(Statistic statistic, int i) throws IllegalArgumentException {
    player.setStatistic(statistic, i);
  }

  public <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> aClass,
                                                            @Nullable Vector vector) {
    return player.launchProjectile(aClass, vector);
  }

  @ApiStatus.Experimental
  public @NotNull CombatTracker getCombatTracker() {
    return player.getCombatTracker();
  }

  public void clearTitle() {
    player.clearTitle();
  }

  @Nullable
  public BlockFace getTargetBlockFace(int i, @NotNull FluidCollisionMode fluidCollisionMode) {
    return player.getTargetBlockFace(i, fluidCollisionMode);
  }

  public Inventory getEnderChest() {
    return player.getEnderChest();
  }

  @Nullable
  public Player getPlayer() {
    return player.getPlayer();
  }

  @ApiStatus.Experimental
  @Nullable
  public EntitySnapshot createSnapshot() {
    return player.createSnapshot();
  }

  @NotNull
  public Set<PermissionAttachmentInfo> getEffectivePermissions() {
    return player.getEffectivePermissions();
  }

  public void showDemoScreen() {
    player.showDemoScreen();
  }

  public boolean listPlayer(Player player) {
    return this.player.listPlayer(player);
  }

  public void playSound(Location location, String s, float v, float v1) {
    player.playSound(location, s, v, v1);
  }

  public void damageItemStack(@NotNull EquipmentSlot equipmentSlot, int i) {
    player.damageItemStack(equipmentSlot, i);
  }

  public void wakeup(boolean b) {
    player.wakeup(b);
  }

  public boolean hasActiveItem() {
    return player.hasActiveItem();
  }

  public float getYaw() {
    return player.getYaw();
  }

  public void abandonConversation(@NotNull Conversation conversation) {
    player.abandonConversation(conversation);
  }

  public void sendPlainMessage(@NotNull String message) {
    player.sendPlainMessage(message);
  }

  public void decrementStatistic(Statistic statistic, int i) throws IllegalArgumentException {
    player.decrementStatistic(statistic, i);
  }

  public EntityEquipment getEquipment() {
    return player.getEquipment();
  }

  public void addCustomChatCompletions(Collection<String> collection) {
    player.addCustomChatCompletions(collection);
  }

  public boolean removePassenger(@NotNull Entity entity) {
    return player.removePassenger(entity);
  }

  public void setVisibleByDefault(boolean b) {
    player.setVisibleByDefault(b);
  }

  @NotNull
  public PermissionAttachment addAttachment(@NotNull Plugin plugin) {
    return player.addAttachment(plugin);
  }

  public void sendExperienceChange(float v, int i) {
    player.sendExperienceChange(v, i);
  }

  public void setResourcePack(UUID uuid, String s, byte @Nullable [] bytes, @Nullable Component component, boolean b) {
    player.setResourcePack(uuid, s, bytes, component, b);
  }

  public int getCooldown(Material material) {
    return player.getCooldown(material);
  }

  /**
   * @param location
   * @param strings
   * @param dyeColor
   * @deprecated
   */
  @Deprecated
  public void sendSignChange(Location location, @Nullable String @Nullable [] strings, DyeColor dyeColor) throws IllegalArgumentException {
    player.sendSignChange(location, strings, dyeColor);
  }

  public void customName(@Nullable Component component) {
    player.customName(component);
  }

  public void giveExp(int i, boolean b) {
    player.giveExp(i, b);
  }

  public boolean isInWater() {
    return player.isInWater();
  }

  public boolean isOp() {
    return player.isOp();
  }

  public boolean hasLineOfSight(@NotNull Location location) {
    return player.hasLineOfSight(location);
  }

  /**
   * @param location
   * @param material
   * @param b
   * @deprecated
   */
  @Deprecated(since = "1.6.2")
  public void sendBlockChange(Location location, Material material, byte b) {
    player.sendBlockChange(location, material, b);
  }

  /**
   * @param reason
   * @param expires
   * @deprecated
   */
  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerFull(@Nullable String reason, @Nullable Date expires) {
    return player.banPlayerFull(reason, expires);
  }

  public PlayerProfile getPlayerProfile() {
    return player.getPlayerProfile();
  }

  public int getShieldBlockingDelay() {
    return player.getShieldBlockingDelay();
  }

  /**
   * @param location
   * @param effect
   * @param i
   * @deprecated
   */
  @Deprecated(since = "1.6.2")
  public void playEffect(Location location, Effect effect, int i) {
    player.playEffect(location, effect, i);
  }

  public void setResourcePack(String url, String hash) {
    player.setResourcePack(url, hash);
  }

  /**
   * @param uuid
   * @param strings
   * @deprecated
   */
  @Deprecated
  public void sendMessage(@Nullable UUID uuid, @NotNull String... strings) {
    player.sendMessage(uuid, strings);
  }

  public boolean discoverRecipe(NamespacedKey namespacedKey) {
    return player.discoverRecipe(namespacedKey);
  }

  public boolean setLeashHolder(@Nullable Entity entity) {
    return player.setLeashHolder(entity);
  }

  @Nullable
  public BanEntry<InetAddress> banIp(@Nullable String s, @Nullable Instant instant, @Nullable String s1, boolean b) {
    return player.banIp(s, instant, s1, b);
  }

  public double getY() {
    return player.getY();
  }

  @ApiStatus.Experimental
  @Unmodifiable
  public Set<Chunk> getSentChunks() {
    return player.getSentChunks();
  }

  public void setResourcePack(String url, byte @Nullable [] hash, @Nullable Component prompt) {
    player.setResourcePack(url, hash, prompt);
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.11")
  public double getMaxHealth() {
    return player.getMaxHealth();
  }

  /**
   * @param s
   * @deprecated
   */
  @Deprecated
  public void setPlayerListFooter(@Nullable String s) {
    player.setPlayerListFooter(s);
  }

  public void setRemoveWhenFarAway(boolean b) {
    player.setRemoveWhenFarAway(b);
  }

  /**
   * @param baseComponents
   * @deprecated
   */
  @Deprecated
  public void setSubtitle(BaseComponent[] baseComponents) {
    player.setSubtitle(baseComponents);
  }

  public void setExperienceLevelAndProgress(@Range(from = 0L, to = 2147483647L) int i) {
    player.setExperienceLevelAndProgress(i);
  }

  public int getSendViewDistance() {
    return player.getSendViewDistance();
  }

  @Nullable
  public Entity releaseLeftShoulderEntity() {
    return player.releaseLeftShoulderEntity();
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.11.2")
  @Nullable
  public Entity getPassenger() {
    return player.getPassenger();
  }

  public boolean hasPermission(@NotNull String s) {
    return player.hasPermission(s);
  }

  public void setGliding(boolean b) {
    player.setGliding(b);
  }

  public void sendBlockDamage(Location location, float v, Entity entity) {
    player.sendBlockDamage(location, v, entity);
  }

  /**
   * @param reason
   * @param source
   * @deprecated
   */
  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason, @Nullable String source) {
    return player.banPlayerIP(reason, source);
  }

  public int getMaximumAir() {
    return player.getMaximumAir();
  }

  public boolean removeScoreboardTag(@NotNull String s) {
    return player.removeScoreboardTag(s);
  }

  @NotNull
  public Collection<PotionEffect> getActivePotionEffects() {
    return player.getActivePotionEffects();
  }

  public boolean hasAI() {
    return player.hasAI();
  }

  public int getPlayerListOrder() {
    return player.getPlayerListOrder();
  }

  /**
   * @param baseComponents
   * @param baseComponents1
   * @deprecated
   */
  @Deprecated
  public void setPlayerListHeaderFooter(BaseComponent @Nullable [] baseComponents,
                                        BaseComponent @Nullable [] baseComponents1) {
    player.setPlayerListHeaderFooter(baseComponents, baseComponents1);
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.21.5")
  public boolean isInWaterOrRain() {
    return player.isInWaterOrRain();
  }

  public void openSign(Sign sign, Side side) {
    player.openSign(sign, side);
  }

  public int getCooldown(Key key) {
    return player.getCooldown(key);
  }

  /**
   * @param reason
   * @param source
   * @param kickPlayer
   * @deprecated
   */
  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason, @Nullable String source, boolean kickPlayer) {
    return player.banPlayerIP(reason, source, kickPlayer);
  }

  public void setDeathScreenScore(int i) {
    player.setDeathScreenScore(i);
  }

  @Nullable
  public Item dropItem(int slot, int amount) {
    return player.dropItem(slot, amount);
  }

  @NotNull
  public Sound getFallDamageSoundBig() {
    return player.getFallDamageSoundBig();
  }

  @ApiStatus.Obsolete(since = "1.20.4")
  public int getItemUseRemainingTime() {
    return player.getItemUseRemainingTime();
  }

  public void sendPotionEffectChangeRemove(LivingEntity livingEntity, PotionEffectType potionEffectType) {
    player.sendPotionEffectChangeRemove(livingEntity, potionEffectType);
  }

  /**
   * @deprecated
   */
  @Deprecated(since = "1.20.4")
  @Nullable
  public Location getBedSpawnLocation() {
    return player.getBedSpawnLocation();
  }

  public void closeInventory() {
    player.closeInventory();
  }

  public void removeAttachment(@NotNull PermissionAttachment permissionAttachment) {
    player.removeAttachment(permissionAttachment);
  }

  /**
   * @param loc
   * @param lines
   * @param hasGlowingText
   * @deprecated
   */
  @Deprecated
  public void sendSignChange(Location loc, @Nullable List<? extends Component> lines, boolean hasGlowingText) throws IllegalArgumentException {
    player.sendSignChange(loc, lines, hasGlowingText);
  }

  /**
   * @param baseComponents
   * @deprecated
   */
  @Deprecated
  public void sendActionBar(BaseComponent... baseComponents) {
    player.sendActionBar(baseComponents);
  }

  public void resetTitle() {
    player.resetTitle();
  }

  public float getSaturation() {
    return player.getSaturation();
  }

  public void setNextArrowRemoval(@Range(from = 0L, to = 2147483647L) int i) {
    player.setNextArrowRemoval(i);
  }

  public int getTotalExperience() {
    return player.getTotalExperience();
  }

  public void openBook(ItemStack itemStack) {
    player.openBook(itemStack);
  }

  /**
   * @param baseComponents
   * @param baseComponents1
   * @param i
   * @param i1
   * @param i2
   * @deprecated
   */
  @Deprecated
  public void showTitle(@Nullable BaseComponent[] baseComponents, @Nullable BaseComponent[] baseComponents1, int i,
                        int i1, int i2) {
    player.showTitle(baseComponents, baseComponents1, i, i1, i2);
  }

  public void setFreezeTicks(int i) {
    player.setFreezeTicks(i);
  }

  public void sendMultiBlockChange(Map<? extends Position, BlockData> map) {
    player.sendMultiBlockChange(map);
  }

  public void sendPlayerListFooter(@NotNull Component footer) {
    player.sendPlayerListFooter(footer);
  }

  public void setBeeStingerCooldown(int i) {
    player.setBeeStingerCooldown(i);
  }

  public void clearActiveItem() {
    player.clearActiveItem();
  }

  /**
   * @param reason
   * @param expires
   * @param source
   * @deprecated
   */
  @Deprecated(since = "1.20.4")
  public BanEntry banPlayer(@Nullable String reason, @Nullable Date expires, @Nullable String source) {
    return player.banPlayer(reason, expires, source);
  }

  @ApiStatus.Obsolete
  public void sendMessage(@NotNull String s) {
    player.sendMessage(s);
  }

  /**
   * @param reason
   * @param expires
   * @param source
   * @param kickPlayer
   * @deprecated
   */
  @Deprecated(since = "1.20.4")
  @Nullable
  public BanEntry banPlayerIP(@Nullable String reason, @Nullable Date expires, @Nullable String source,
                              boolean kickPlayer) {
    return player.banPlayerIP(reason, expires, source, kickPlayer);
  }
}
