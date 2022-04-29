package de.timesnake.basic.bukkit.core.user;

import com.destroystokyo.paper.ClientOption;
import com.destroystokyo.paper.block.TargetBlockInfo;
import com.destroystokyo.paper.entity.TargetEntityInfo;
import com.destroystokyo.paper.profile.PlayerProfile;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.pointer.Pointer;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.event.HoverEvent;
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
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
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
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class DelegatedUser {

    protected final Player player;

    public DelegatedUser(Player player) {
        this.player = player;
    }

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
    public void setPlayerListHeader(@Nullable String s) {
        player.setPlayerListHeader(s);
    }

    @Deprecated
    @Nullable
    public String getPlayerListFooter() {
        return player.getPlayerListFooter();
    }

    @Deprecated
    public void setPlayerListFooter(@Nullable String s) {
        player.setPlayerListFooter(s);
    }

    @Deprecated
    public void setPlayerListHeaderFooter(@Nullable String s, @Nullable String s1) {
        player.setPlayerListHeaderFooter(s, s1);
    }

    @NotNull
    public Location getCompassTarget() {
        return player.getCompassTarget();
    }

    public void setCompassTarget(@NotNull Location location) {
        player.setCompassTarget(location);
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

    public boolean isSleepingIgnored() {
        return player.isSleepingIgnored();
    }

    public void setSleepingIgnored(boolean b) {
        player.setSleepingIgnored(b);
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

    public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory soundCategory,
                          float v, float v1) {
        player.playSound(location, sound, soundCategory, v, v1);
    }

    public void playSound(@NotNull Location location, @NotNull String s, @NotNull SoundCategory soundCategory,
                          float v, float v1) {
        player.playSound(location, s, soundCategory, v, v1);
    }

    public void playSound(@NotNull Entity entity, @NotNull Sound sound, float v, float v1) {
        player.playSound(entity, sound, v, v1);
    }

    public void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory soundCategory, float v
            , float v1) {
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

    public void sendEquipmentChange(@NotNull LivingEntity livingEntity, @NotNull EquipmentSlot equipmentSlot,
                                    @NotNull ItemStack itemStack) {
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

    public void sendSignChange(@NotNull Location location, @Nullable List<Component> list, @NotNull DyeColor dyeColor
            , boolean b) throws IllegalArgumentException {
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
    public void sendSignChange(@NotNull Location location, @Nullable String[] strings, @NotNull DyeColor dyeColor,
                               boolean b) throws IllegalArgumentException {
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
    public BanEntry banPlayerIP(@Nullable String reason, @Nullable Date expires, @Nullable String source,
                                boolean kickPlayer) {
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
    public void setPlayerListHeaderFooter(@Nullable BaseComponent[] baseComponents,
                                          @Nullable BaseComponent[] baseComponents1) {
        player.setPlayerListHeaderFooter(baseComponents, baseComponents1);
    }

    @Deprecated
    public void setPlayerListHeaderFooter(@Nullable BaseComponent baseComponent,
                                          @Nullable BaseComponent baseComponent1) {
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
    public void showTitle(@Nullable BaseComponent[] baseComponents, @Nullable BaseComponent[] baseComponents1, int i,
                          int i1, int i2) {
        player.showTitle(baseComponents, baseComponents1, i, i1, i2);
    }

    @Deprecated
    public void showTitle(@Nullable BaseComponent baseComponent, @Nullable BaseComponent baseComponent1, int i,
                          int i1, int i2) {
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

    @Nullable
    public WeatherType getPlayerWeather() {
        return player.getPlayerWeather();
    }

    public void setPlayerWeather(@NotNull WeatherType weatherType) {
        player.setPlayerWeather(weatherType);
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

    public float getFlySpeed() {
        return player.getFlySpeed();
    }

    public void setFlySpeed(float v) throws IllegalArgumentException {
        player.setFlySpeed(v);
    }

    public float getWalkSpeed() {
        return player.getWalkSpeed();
    }

    public void setWalkSpeed(float v) throws IllegalArgumentException {
        player.setWalkSpeed(v);
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

    public double getHealthScale() {
        return player.getHealthScale();
    }

    public void setHealthScale(double v) throws IllegalArgumentException {
        player.setHealthScale(v);
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

    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                              double v2) {
        player.spawnParticle(particle, location, i, v, v1, v2);
    }

    public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4
            , double v5) {
        player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5);
    }

    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                                  double v2, @Nullable T t) {
        player.spawnParticle(particle, location, i, v, v1, v2, t);
    }

    public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3,
                                  double v4, double v5, @Nullable T t) {
        player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, t);
    }

    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                              double v2, double v3) {
        player.spawnParticle(particle, location, i, v, v1, v2, v3);
    }

    public void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3, double v4
            , double v5, double v6) {
        player.spawnParticle(particle, v, v1, v2, i, v3, v4, v5, v6);
    }

    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int i, double v, double v1,
                                  double v2, double v3, @Nullable T t) {
        player.spawnParticle(particle, location, i, v, v1, v2, v3, t);
    }

    public <T> void spawnParticle(@NotNull Particle particle, double v, double v1, double v2, int i, double v3,
                                  double v4, double v5, double v6, @Nullable T t) {
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

    public net.kyori.adventure.text.event.@NotNull HoverEvent<net.kyori.adventure.text.event.HoverEvent.ShowEntity> asHoverEvent(@NotNull UnaryOperator<HoverEvent.ShowEntity> op) {
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

    public boolean getCanPickupItems() {
        return player.getCanPickupItems();
    }

    public void setCanPickupItems(boolean b) {
        player.setCanPickupItems(b);
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

    public boolean isCollidable() {
        return player.isCollidable();
    }

    public void setCollidable(boolean b) {
        player.setCollidable(b);
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

    public boolean isInvisible() {
        return player.isInvisible();
    }

    public void setInvisible(boolean b) {
        player.setInvisible(b);
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

    public @NotNull org.bukkit.util.Vector getVelocity() {
        return player.getVelocity();
    }

    public void setVelocity(@NotNull org.bukkit.util.Vector vector) {
        player.setVelocity(vector);
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
        return de.timesnake.basic.bukkit.util.Server.getWorld(player.getWorld());
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

    public @NotNull CompletableFuture<Boolean> teleportAsync(@NotNull Location loc,
                                                             PlayerTeleportEvent.@NotNull TeleportCause cause) {
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

    public void setFireTicks(int i) {
        player.setFireTicks(i);
    }

    public int getMaxFireTicks() {
        return player.getMaxFireTicks();
    }

    public boolean isVisualFire() {
        return player.isVisualFire();
    }

    public void setVisualFire(boolean b) {
        player.setVisualFire(b);
    }

    public int getFreezeTicks() {
        return player.getFreezeTicks();
    }

    public void setFreezeTicks(int i) {
        player.setFreezeTicks(i);
    }

    public int getMaxFreezeTicks() {
        return player.getMaxFreezeTicks();
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

    public @Nullable EntityDamageEvent getLastDamageCause() {
        return player.getLastDamageCause();
    }

    public void setLastDamageCause(@Nullable EntityDamageEvent entityDamageEvent) {
        player.setLastDamageCause(entityDamageEvent);
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

    public boolean isCustomNameVisible() {
        return player.isCustomNameVisible();
    }

    public void setCustomNameVisible(boolean b) {
        player.setCustomNameVisible(b);
    }

    public boolean isGlowing() {
        return player.isGlowing();
    }

    public void setGlowing(boolean b) {
        player.setGlowing(b);
    }

    public boolean isInvulnerable() {
        return player.isInvulnerable();
    }

    public void setInvulnerable(boolean b) {
        player.setInvulnerable(b);
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

    public void sendMessage(@NotNull Identity identity, @NotNull Component message,
                            @NotNull net.kyori.adventure.audience.MessageType type) {
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

    public void sendMessage(@NotNull Identified source, @NotNull ComponentLike message,
                            @NotNull net.kyori.adventure.audience.MessageType type) {
        player.sendMessage(source, message, type);
    }

    public void sendMessage(@NotNull Identity source, @NotNull ComponentLike message,
                            @NotNull net.kyori.adventure.audience.MessageType type) {
        player.sendMessage(source, message, type);
    }

    public void sendMessage(@NotNull Component message, @NotNull net.kyori.adventure.audience.MessageType type) {
        player.sendMessage(message, type);
    }

    public void sendMessage(@NotNull Identified source, @NotNull Component message,
                            @NotNull net.kyori.adventure.audience.MessageType type) {
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

    public void playSound(net.kyori.adventure.sound.@NotNull Sound sound,
                          net.kyori.adventure.sound.Sound.@NotNull Emitter emitter) {
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

    public <T> @UnknownNullability T getOrDefaultFrom(@NotNull Pointer<T> pointer,
                                                      @NotNull Supplier<? extends T> defaultValue) {
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
    public PermissionAttachment addAttachment(org.bukkit.plugin.@NotNull Plugin plugin, @NotNull String s, boolean b,
                                              int i) {
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

    public <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> aClass,
                                                              @Nullable Vector vector) {
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

    public void abandonConversation(@NotNull Conversation conversation,
                                    @NotNull ConversationAbandonedEvent conversationAbandonedEvent) {
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
    public BanEntry banPlayer(@Nullable String reason, @Nullable Date expires, @Nullable String source,
                              boolean kickIfOnline) {
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
