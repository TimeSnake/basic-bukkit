package de.timesnake.basic.bukkit.core.world;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.AsyncUserJoinEvent;
import de.timesnake.basic.bukkit.util.user.event.AsyncUserQuitEvent;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import de.timesnake.basic.bukkit.util.world.entity.MapDisplayBuilder;
import de.timesnake.basic.bukkit.util.world.entity.PacketEntity;
import de.timesnake.basic.packets.core.packet.out.ExClientboundLevelChunkWithLightPacket;
import de.timesnake.basic.packets.core.packet.out.ExPacketPlayOutChunkUnload;
import de.timesnake.basic.packets.util.listener.PacketHandler;
import de.timesnake.basic.packets.util.listener.PacketPlayOutListener;
import de.timesnake.basic.packets.util.packet.ExPacket;
import de.timesnake.basic.packets.util.packet.ExPacketPlayOut;
import de.timesnake.basic.packets.util.packet.ExPacketPlayOutTablistTeamCreation;
import de.timesnake.library.basic.util.Tuple;
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

        User user = Server.getUser(receiver);

        if (user == null) {
            System.out.println(receiver.getName() + " null " + load);
            if (load) {
                this.preLoadedChunksByUuid.computeIfAbsent(receiver.getUniqueId(), p -> new HashSet<>()).add(chunk);
            }
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

    private void tryLoadEntityForUser(PacketEntity entity, User user) {
        if (user.getChunk().equals(entity.getLocation().getChunk())) {
            entity.loadForUser(user);
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

        for (User user : Server.getUsers()) {
            this.tryLoadEntityForUser(entity, user);
        }
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
        return (Set<EntityType>) this.entitiesByChunk.entrySet().stream()
                .filter(entry -> entry.getKey().getWorld().equals(world.getBukkitWorld()))
                .map(Map.Entry::getValue).flatMap(Collection::stream)
                .filter(entity -> entityClass.isAssignableFrom(entity.getClass()))
                .toList();
    }

    @EventHandler
    public void onUserJoin(AsyncUserJoinEvent e) {
        Server.getScoreboardManager().getPacketManager().sendPacket(e.getUser(),
                ExPacketPlayOutTablistTeamCreation.wrap(FAKE_PLAYER_TEAM_NAME, "", ChatColor.WHITE,
                        ExPacketPlayOutTablistTeamCreation.NameTagVisibility.NEVER));

        Collection<Chunk> preLoadedChunks = this.preLoadedChunksByUuid.remove(e.getUser().getUniqueId());

        if (preLoadedChunks != null) {
            for (Chunk chunk : preLoadedChunks) {
                this.loadEntitiesInChunk(e.getUser(), chunk);
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
