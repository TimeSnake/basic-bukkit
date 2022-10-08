/*
 * basic-bukkit.main
 * Copyright (C) 2022 timesnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; If not, see <http://www.gnu.org/licenses/>.
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
import de.timesnake.library.basic.util.Tuple;
import de.timesnake.library.packets.core.packet.out.ExClientboundLevelChunkWithLightPacket;
import de.timesnake.library.packets.core.packet.out.ExPacketPlayOutChunkUnload;
import de.timesnake.library.packets.util.listener.PacketHandler;
import de.timesnake.library.packets.util.listener.PacketPlayOutListener;
import de.timesnake.library.packets.util.packet.ExPacket;
import de.timesnake.library.packets.util.packet.ExPacketPlayOut;
import de.timesnake.library.packets.util.packet.ExPacketPlayOutTablistTeamCreation;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PacketEntityManager implements Listener, PacketPlayOutListener,
        de.timesnake.basic.bukkit.util.world.entity.EntityManager {

    public static final String FAKE_PLAYER_TEAM_NAME = "fake_players";

    private final ConcurrentHashMap<Chunk, Collection<PacketEntity>> entitiesByChunk = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<UUID, Collection<Chunk>> preLoadedChunksByUuid = new ConcurrentHashMap<>();

    public PacketEntityManager() {
        MapDisplayBuilder.ExMapFont.loadFonts();
        Server.registerListener(this, BasicBukkit.getPlugin());
        Server.getPacketManager().addListener(this);
    }

    @PacketHandler(type = {ExPacket.Type.PLAY_OUT_LEVEL_CHUNK_WITH_LIGHT_PACKET, ExPacket.Type.PLAY_OUT_CHUNK_UNLOAD})
    public void onPacket(ExPacketPlayOut packet, Player receiver) {

        boolean load;
        Chunk chunk;

        ExWorld world = Server.getWorld(receiver.getWorld());

        Tuple<Integer, Integer> coords;

        if (packet instanceof ExClientboundLevelChunkWithLightPacket p) {
            load = true;
            coords = p.getChunkCoordinates();
        } else if (packet instanceof ExPacketPlayOutChunkUnload p) {
            load = false;
            coords = p.getChunkCoordinates();
        } else {
            return;
        }

        chunk = world.getChunkAt(coords.getA(), coords.getB());


        if (load) {
            this.preLoadedChunksByUuid.computeIfAbsent(receiver.getUniqueId(), p -> new HashSet<>()).add(chunk);
        }

        User user = Server.getUser(receiver);

        if (user == null) {
            return;
        }

        if (load) {
            this.loadEntitiesInChunk(user, chunk);
        } else {
            this.unloadEntitiesInChunk(user, chunk);
        }

    }

    private void loadEntitiesInChunk(User user, Chunk chunk) {
        for (PacketEntity entity : this.entitiesByChunk.getOrDefault(chunk,
                ConcurrentHashMap.newKeySet(0)).stream().filter(e -> e.isUserWatching(user)).toList()) {
            entity.loadForUser(user);
        }
    }

    private void unloadEntitiesInChunk(User user, Chunk chunk) {
        for (PacketEntity entity : this.entitiesByChunk.getOrDefault(chunk,
                ConcurrentHashMap.newKeySet(0)).stream().filter(e -> e.isUserWatching(user)).toList()) {
            entity.unloadForUser(user);
        }
    }

    private Collection<PacketEntity> getAllEntities() {
        return this.entitiesByChunk.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public void tryLoadEntityForUser(PacketEntity entity, User user) {
        if (this.preLoadedChunksByUuid.containsKey(user.getUniqueId())) {
            if (this.preLoadedChunksByUuid.get(user.getUniqueId()).contains(entity.getLocation().getChunk())) {
                entity.loadForUser(user);
            }
        }
    }

    private void addEntity(PacketEntity entity) {
        this.entitiesByChunk.computeIfAbsent(entity.getLocation().getChunk(), u -> ConcurrentHashMap.newKeySet()).add(entity);
    }

    private void removeEntity(PacketEntity entity) {
        this.entitiesByChunk.forEach((chunk, entities) -> entities.remove(entity));
    }

    @Override
    public void registerEntity(PacketEntity entity) {
        this.addEntity(entity);
        entity.setPublic(true);

        Server.getUsers().forEach(entity::addWatcher);
    }

    @Override
    public void registerEntity(PacketEntity entity, Collection<? extends User> users) {
        this.addEntity(entity);
        users.forEach(entity::addWatcher);
    }

    @Override
    public void registerEntity(PacketEntity entity, User... users) {
        this.registerEntity(entity, Arrays.asList(users));
    }

    @Override
    public void unregisterEntity(PacketEntity entity) {
        this.removeEntity(entity);
        entity.despawnForUser();
    }

    @Override
    public Set<PacketEntity> getEntitiesByWorld(ExWorld world) {
        return new HashSet<>(this.entitiesByChunk.entrySet().stream()
                .filter(entry -> entry.getKey().getWorld().equals(world.getBukkitWorld()))
                .map(Map.Entry::getValue).flatMap(Collection::stream)
                .toList());
    }

    @Override
    public <EntityType extends PacketEntity> Set<EntityType> getEntitiesByWorld(ExWorld world,
                                                                                Class<EntityType> entityClass) {
        return ((Set<EntityType>) this.entitiesByChunk.entrySet().stream()
                .filter(entry -> entry.getKey().getWorld().equals(world.getBukkitWorld()))
                .map(Map.Entry::getValue).flatMap(Collection::stream)
                .filter(entity -> entityClass.isAssignableFrom(entity.getClass()))
                .collect(Collectors.toSet()));
    }

    @EventHandler
    public void onUserJoin(AsyncUserJoinEvent e) {
        Server.getScoreboardManager().getPacketManager().sendPacket(e.getUser(),
                ExPacketPlayOutTablistTeamCreation.wrap(FAKE_PLAYER_TEAM_NAME, "", ChatColor.WHITE,
                        ExPacketPlayOutTablistTeamCreation.NameTagVisibility.NEVER));

        Collection<Chunk> preLoadedChunks = this.preLoadedChunksByUuid.remove(e.getUser().getUniqueId());

        if (preLoadedChunks != null) {
            for (Chunk chunk : preLoadedChunks) {
                Server.runTaskLaterAsynchrony(() -> {
                    this.loadEntitiesInChunk(e.getUser(), chunk);
                }, 20 * 3, BasicBukkit.getPlugin());

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
