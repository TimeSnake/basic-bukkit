package de.timesnake.basic.bukkit.core.world;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;
import de.timesnake.basic.entities.entity.bukkit.ExPlayer;
import de.timesnake.basic.packets.util.packet.*;
import de.timesnake.library.basic.util.Tuple;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class EntityManager implements Listener, de.timesnake.basic.bukkit.util.world.EntityManager {

    private static final double TRACKING_RANGE = 48; // blocks
    private static final String FAKE_PLAYER_TEAM_NAME = "fake_players";

    private final Map<User, Collection<Tuple<ExPlayer, Boolean>>> playersByUser = new HashMap<>();

    private BukkitTask task;

    public EntityManager() {
        Server.registerListener(this, BasicBukkit.getPlugin());
        this.runPlayerLoader();
    }

    public void onDisable() {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    private void runPlayerLoader() {
        this.task = Server.runTaskTimerAsynchrony(() -> {
            for (Map.Entry<User, Collection<Tuple<ExPlayer, Boolean>>> entry : this.playersByUser.entrySet()) {

                User user = entry.getKey();
                Collection<Tuple<ExPlayer, Boolean>> playerLoadedTuples = entry.getValue();

                if (playerLoadedTuples == null || playerLoadedTuples.isEmpty()) {
                    return;
                }

                Location loc = user.getLocation();

                for (Tuple<ExPlayer, Boolean> playerLoadedTuple : playerLoadedTuples) {
                    if (playerLoadedTuple.getA().getLocation().distanceSquared(loc) < TRACKING_RANGE * TRACKING_RANGE) {
                        if (!playerLoadedTuple.getB()) {
                            this.loadPlayer(user, playerLoadedTuple);
                        }
                    } else {
                        if (playerLoadedTuple.getB()) {
                            this.unloadPlayer(user, playerLoadedTuple);
                        }
                    }
                }
            }


        }, 0, 10, BasicBukkit.getPlugin());
    }

    @Override
    public void registerPlayer(User user, ExPlayer player, boolean removeFromTablist) {
        this.registerPlayer(List.of(user), player, removeFromTablist);
    }

    @Override
    public void registerPlayer(Collection<? extends User> users, ExPlayer player, boolean removeFromTablist) {
        for (User user : users) {
            Collection<Tuple<ExPlayer, Boolean>> entities = this.playersByUser.computeIfAbsent(user,
                    k -> new LinkedList<>());

            Tuple<ExPlayer, Boolean> playerLoadedTuple = new Tuple<>(player, false);
            entities.add(playerLoadedTuple);

            if (player.getLocation().distanceSquared(user.getLocation()) < TRACKING_RANGE * TRACKING_RANGE) {
                this.loadPlayer(user, playerLoadedTuple);
            }
        }
    }

    private void loadPlayer(User user, Tuple<ExPlayer, Boolean> playerLoadedTuple) {

        ExPlayer player = playerLoadedTuple.getA();

        if (!player.getChunk().isLoaded()) {
            return;
        }

        playerLoadedTuple.setB(true);

        Server.getScoreboardManager().getPacketManager().sendPacket(user,
                ExPacketPlayOutPlayerInfo.wrap(ExPacketPlayOutPlayerInfo.Action.ADD_PLAYER, player));
        Server.getScoreboardManager().getPacketManager().sendPacket(user,
                ExPacketPlayOutTablistTeamPlayerAdd.wrap(FAKE_PLAYER_TEAM_NAME, player.getName()));

        user.sendPacket(ExPacketPlayOutSpawnNamedEntity.wrap(player));
        user.sendPacket(ExPacketPlayOutEntityMetadata.wrap((Player) player,
                ExPacketPlayOutEntityMetadata.DataType.UPDATE));

        Server.runTaskLaterAsynchrony(() -> Server.getScoreboardManager().getPacketManager().sendPacket(user,
                ExPacketPlayOutPlayerInfo.wrap(ExPacketPlayOutPlayerInfo.Action.REMOVE_PLAYER, player)), 4,
                BasicBukkit.getPlugin());
    }

    @Override
    public void removePlayer(User user, ExPlayer player) {
        this.removePlayer(List.of(user), player);
    }

    private void unloadPlayer(User user, Tuple<ExPlayer, Boolean> playerLoadedTuple) {
        ExPlayer player = playerLoadedTuple.getA();
        playerLoadedTuple.setB(false);

        Server.getScoreboardManager().getPacketManager().sendPacket(user,
                ExPacketPlayOutPlayerInfo.wrap(ExPacketPlayOutPlayerInfo.Action.REMOVE_PLAYER, player));
        Server.getScoreboardManager().getPacketManager().sendPacket(user, ExPacketPlayOutEntityDestroy.wrap(player));
    }

    @Override
    public void removePlayer(Collection<? extends User> users, ExPlayer player) {
        for (User user : users) {
            this.unloadPlayer(user, new Tuple<>(player, false));
            this.playersByUser.get(user).remove(new Tuple<>(player, true));
            this.playersByUser.get(user).remove(new Tuple<>(player, false));
        }
    }

    @EventHandler
    public void onUserJoin(UserJoinEvent e) {
        Server.getScoreboardManager().getPacketManager().sendPacket(e.getUser(),
                ExPacketPlayOutTablistTeamCreation.wrap(FAKE_PLAYER_TEAM_NAME, "", ChatColor.WHITE,
                        ExPacketPlayOutTablistTeamCreation.NameTagVisibility.NEVER));
    }
}
