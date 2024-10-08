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
import de.timesnake.library.basic.util.Triple;
import de.timesnake.library.basic.util.Tuple;
import de.timesnake.library.packets.util.listener.PacketHandler;
import de.timesnake.library.packets.util.listener.PacketPlayOutListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PacketEntityManager implements Listener, PacketPlayOutListener,
    de.timesnake.basic.bukkit.util.world.entity.EntityManager {

  private final Logger logger = LogManager.getLogger("packet-entity.manager");

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
      coords = new Tuple<>(p.pos().x, p.pos().z);
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
        .getOrDefault(new Triple<>(chunk.getWorld(), chunk.getX(), chunk.getZ()), ConcurrentHashMap.newKeySet(0))
        .stream().filter(e -> e.isUserWatching(user)).toList()) {
      entity.loadForUser(user);
    }
  }

  private void unloadEntitiesInChunk(User user, Chunk chunk) {
    for (PacketEntity entity : this.entitiesByChunk
        .getOrDefault(new Triple<>(chunk.getWorld(), chunk.getX(), chunk.getZ()), ConcurrentHashMap.newKeySet(0)).stream()
        .filter(e -> e.isUserWatching(user)).toList()) {
      entity.unloadForUser(user);
    }
  }

  private @NotNull Collection<PacketEntity> getAllEntities() {
    return this.entitiesByChunk.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
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

    this.logger.info("Registered public entity at '{} {} {} {}' of type '{}'",
        entity.getLocation().getWorld().getName(), entity.getLocation().getBlockX(),
        entity.getLocation().getBlockY(), entity.getLocation().getBlockZ(),
        entity.getType());
  }

  @Override
  public void registerEntity(PacketEntity entity, Collection<? extends User> users) {
    this.addEntity(entity);
    users.forEach(entity::addWatcher);

    this.logger.info("Registered entity at '{} {} {} {}' of type '{}' for users '{}'",
        entity.getLocation().getWorld().getName(), entity.getLocation().getBlockX(),
        entity.getLocation().getBlockY(), entity.getLocation().getBlockZ(),
        entity.getType(),
        String.join("' , '", users.stream().map(User::getName).toList()));
  }

  @Override
  public void registerEntity(PacketEntity entity, User... users) {
    this.registerEntity(entity, Arrays.asList(users));
  }

  @Override
  public void unregisterEntity(PacketEntity entity) {
    this.removeEntity(entity);
    entity.despawnForUsers();

    this.logger.info("Unregistered entity at '{} {} {} {}' of type '{}'",
        entity.getLocation().getWorld().getName(), entity.getLocation().getBlockX(),
        entity.getLocation().getBlockY(), entity.getLocation().getBlockZ(),
        entity.getType());
  }

  @Override
  public @NotNull Set<PacketEntity> getEntitiesByWorld(ExWorld world) {
    return new HashSet<>(this.entitiesByChunk.entrySet().stream()
        .filter(entry -> entry.getKey().getA().equals(world.getBukkitWorld()))
        .map(Map.Entry::getValue).flatMap(Collection::stream)
        .toList());
  }

  @Override
  public <EntityType extends PacketEntity> @NotNull Set<EntityType> getEntitiesByWorld(ExWorld world,
                                                                              Class<EntityType> entityClass) {
    return ((Set<EntityType>) this.entitiesByChunk.entrySet().stream()
        .filter(entry -> entry.getKey().getA().equals(world.getBukkitWorld()))
        .map(Map.Entry::getValue).flatMap(Collection::stream)
        .filter(entity -> entityClass.isAssignableFrom(entity.getClass()))
        .collect(Collectors.toSet()));
  }

  @EventHandler
  public void onUserJoin(AsyncUserJoinEvent e) {
    Collection<Chunk> preLoadedChunks = this.preLoadedChunksByUuid.remove(e.getUser().getUniqueId());
    if (preLoadedChunks != null) {
      for (Chunk chunk : preLoadedChunks) {
        Server.runTaskLaterAsynchrony(() -> this.loadEntitiesInChunk(e.getUser(), chunk),
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
