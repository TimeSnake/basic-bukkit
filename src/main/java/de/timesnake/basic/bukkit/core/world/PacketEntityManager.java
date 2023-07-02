/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.world;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.AsyncUserJoinEvent;
import de.timesnake.basic.bukkit.util.user.event.AsyncUserQuitEvent;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import de.timesnake.basic.bukkit.util.world.entity.MapDisplayBuilder;
import de.timesnake.basic.bukkit.util.world.entity.PacketEntity;
import de.timesnake.library.basic.util.Loggers;
import de.timesnake.library.basic.util.Triple;
import de.timesnake.library.basic.util.Tuple;
import de.timesnake.library.packets.core.packet.out.scoreboard.ClientboundSetPlayerTeamPacketBuilder;
import de.timesnake.library.packets.util.listener.PacketHandler;
import de.timesnake.library.packets.util.listener.PacketPlayOutListener;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PacketEntityManager implements Listener, PacketPlayOutListener,
    de.timesnake.basic.bukkit.util.world.entity.EntityManager {

  public static final String FAKE_PLAYER_TEAM_NAME = "fake_players";
  public static final PlayerTeam FAKE_PLAYER_TEAM = new PlayerTeam(null, FAKE_PLAYER_TEAM_NAME) {
    @Override
    public Component getPlayerPrefix() {
      return Component.empty();
    }

    @Override
    public Component getPlayerSuffix() {
      return Component.empty();
    }

    @Override
    public ChatFormatting getColor() {
      return ChatFormatting.WHITE;
    }

    @Override
    public CollisionRule getCollisionRule() {
      return CollisionRule.NEVER;
    }

    @Override
    public Component getDisplayName() {
      return Component.empty();
    }

    @Override
    public String getName() {
      return FAKE_PLAYER_TEAM_NAME;
    }

    @Override
    public Visibility getDeathMessageVisibility() {
      return Visibility.NEVER;
    }

    @Override
    public Visibility getNameTagVisibility() {
      return Visibility.NEVER;
    }
  };

  private final ConcurrentHashMap<Triple<World, Integer, Integer>, Collection<PacketEntity>> entitiesByChunk = new ConcurrentHashMap<>();

  private final ConcurrentHashMap<UUID, Collection<Chunk>> preLoadedChunksByUuid = new ConcurrentHashMap<>();

  public PacketEntityManager() {
    MapDisplayBuilder.ExMapFont.loadFonts();
    Server.registerListener(this, BasicBukkit.getPlugin());
    Server.getPacketManager().addListener(this);
  }

  @PacketHandler(type = {
      ClientboundLevelChunkWithLightPacket.class,
      ClientboundForgetLevelChunkPacket.class
  })
  public void onPacket(Packet<?> packet, Player receiver) {

    boolean load;
    Chunk chunk;

    ExWorld world = Server.getWorld(receiver.getWorld());

    Tuple<Integer, Integer> coords;

    if (packet instanceof ClientboundLevelChunkWithLightPacket p) {
      load = true;
      coords = new Tuple<>(p.getX(), p.getZ());
    } else if (packet instanceof ClientboundForgetLevelChunkPacket p) {
      load = false;
      coords = new Tuple<>(p.getX(), p.getZ());
    } else {
      return;
    }

    chunk = world.getChunkAt(coords.getA(), coords.getB());

    User user = Server.getUser(receiver);

    if (user == null) {
      this.preLoadedChunksByUuid.computeIfAbsent(receiver.getUniqueId(), p -> new HashSet<>()).add(chunk);
      return;
    }

    if (load) {
      this.loadEntitiesInChunk(user, chunk);
    } else {
      this.unloadEntitiesInChunk(user, chunk);
    }

  }

  private void loadEntitiesInChunk(User user, Chunk chunk) {
    for (PacketEntity entity : this.entitiesByChunk
        .getOrDefault(new Triple<>(chunk.getWorld(), chunk.getX(), chunk.getZ()),
            ConcurrentHashMap.newKeySet(0))
        .stream().filter(e -> e.isUserWatching(user)).toList()) {
      entity.loadForUser(user);
    }
  }

  private void unloadEntitiesInChunk(User user, Chunk chunk) {
    for (PacketEntity entity : this.entitiesByChunk
        .getOrDefault(new Triple<>(chunk.getWorld(), chunk.getX(), chunk.getZ()),
            ConcurrentHashMap.newKeySet(0)).stream()
        .filter(e -> e.isUserWatching(user)).toList()) {
      entity.unloadForUser(user);
    }
  }

  private Collection<PacketEntity> getAllEntities() {
    return this.entitiesByChunk.values().stream().flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  public void tryLoadEntityForUser(PacketEntity entity, User user) {
    if (user.getChunk().getX() - entity.getLocation().getChunk().getX() <= user.getViewDistance()
        && user.getChunk().getZ() - entity.getLocation().getChunk().getZ() <= user.getViewDistance()) {
      entity.loadForUser(user);
    }
  }

  private void addEntity(PacketEntity entity) {
    Chunk chunk = entity.getLocation().getChunk();
    this.entitiesByChunk.computeIfAbsent(new Triple<>(entity.getLocation().getWorld(),
        chunk.getX(), chunk.getZ()), u -> ConcurrentHashMap.newKeySet()).add(entity);
  }

  private void removeEntity(PacketEntity entity) {
    this.entitiesByChunk.forEach((chunk, entities) -> entities.remove(entity));
  }

  @Override
  public void registerEntity(PacketEntity entity) {
    this.addEntity(entity);
    entity.setPublic(true);

    Loggers.ENTITY.info("Registered public entity at '" + entity.getLocation().getWorld().getName() + " "
        + entity.getLocation().getBlockX() + " " + entity.getLocation().getBlockY() + " " + entity.getLocation().getBlockZ()
        + "' of type '" + entity.getType() + "'");
  }

  @Override
  public void registerEntity(PacketEntity entity, Collection<? extends User> users) {
    this.addEntity(entity);
    users.forEach(entity::addWatcher);

    Loggers.ENTITY.info("Registered entity at '" + entity.getLocation().getWorld().getName() + " "
        + entity.getLocation().getBlockX() + " " + entity.getLocation().getBlockY() + " " + entity.getLocation().getBlockZ()
        + "' of type '" + entity.getType() + "' for users '" +
        String.join("' , '", users.stream().map(User::getName).toList()) + "' ");
  }

  @Override
  public void registerEntity(PacketEntity entity, User... users) {
    this.registerEntity(entity, Arrays.asList(users));
  }

  @Override
  public void unregisterEntity(PacketEntity entity) {
    this.removeEntity(entity);
    entity.despawnForUsers();

    Loggers.ENTITY.info("Unregistered entity at '" + entity.getLocation().getWorld().getName() + " "
        + entity.getLocation().getBlockX() + " " + entity.getLocation().getBlockY() + " " + entity.getLocation().getBlockZ()
        + "' of type '" + entity.getType() + "'");
  }

  @Override
  public Set<PacketEntity> getEntitiesByWorld(ExWorld world) {
    return new HashSet<>(this.entitiesByChunk.entrySet().stream()
        .filter(entry -> entry.getKey().getA().equals(world.getBukkitWorld()))
        .map(Map.Entry::getValue).flatMap(Collection::stream)
        .toList());
  }

  @Override
  public <EntityType extends PacketEntity> Set<EntityType> getEntitiesByWorld(ExWorld world,
                                                                              Class<EntityType> entityClass) {
    return ((Set<EntityType>) this.entitiesByChunk.entrySet().stream()
        .filter(entry -> entry.getKey().getA().equals(world.getBukkitWorld()))
        .map(Map.Entry::getValue).flatMap(Collection::stream)
        .filter(entity -> entityClass.isAssignableFrom(entity.getClass()))
        .collect(Collectors.toSet()));
  }

  @EventHandler
  public void onUserJoin(AsyncUserJoinEvent e) {
    PlayerTeam team = new PlayerTeam(ClientboundSetPlayerTeamPacketBuilder.DUMMY, FAKE_PLAYER_TEAM_NAME);
    team.setNameTagVisibility(Team.Visibility.NEVER);
    team.setColor(ChatFormatting.WHITE);
    team.setCollisionRule(Team.CollisionRule.NEVER);
    team.setDeathMessageVisibility(Team.Visibility.NEVER);
    Packet<?> packet = ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, true);

    Server.getScoreboardManager().getPacketManager().sendPacket(e.getUser(), packet);

    Collection<Chunk> preLoadedChunks = this.preLoadedChunksByUuid.remove(
        e.getUser().getUniqueId());

    if (preLoadedChunks != null) {
      for (Chunk chunk : preLoadedChunks) {
        Server.runTaskLaterAsynchrony(
            () -> this.loadEntitiesInChunk(e.getUser(), chunk),
            20 * 3, BasicBukkit.getPlugin());

      }
    }
  }

  @EventHandler
  public void onUserQuit(AsyncUserQuitEvent e) {
    User user = e.getUser();
    this.getAllEntities().forEach(entity -> entity.onUserQuit(user));

    this.preLoadedChunksByUuid.remove(user.getUniqueId());
  }
}
