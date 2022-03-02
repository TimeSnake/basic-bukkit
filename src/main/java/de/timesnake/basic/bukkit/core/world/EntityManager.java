package de.timesnake.basic.bukkit.core.world;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;
import de.timesnake.basic.entities.entity.bukkit.ExPlayer;
import de.timesnake.basic.packets.util.packet.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityManager implements Listener, de.timesnake.basic.bukkit.util.world.EntityManager {

    private static final String FAKE_PLAYER_TEAM_NAME = "fake_players";

    public EntityManager() {
        Server.registerListener(this, BasicBukkit.getPlugin());
    }

    @Override
    public void spawnPlayer(User user, ExPlayer player, boolean removeFromTablist) {
        this.spawnPlayer(List.of(user), player, removeFromTablist);
    }

    @Override
    public void spawnPlayer(Collection<? extends User> users, ExPlayer player, boolean removeFromTablist) {
        final Location loc = player.getLocation();

        Map<User, Location> locByUser = new HashMap<>();

        for (User user : users) {
            Location userLoc = user.getLocation().add(user.getLocation().getDirection().normalize().multiply(-1));

            locByUser.put(user, userLoc);

            player.setPosition(userLoc.getX(), userLoc.getY(), userLoc.getZ());
            Server.getScoreboardManager().getPacketManager().sendPacket(user, ExPacketPlayOutPlayerInfo.wrap(ExPacketPlayOutPlayerInfo.Action.ADD_PLAYER, player));
            Server.getScoreboardManager().getPacketManager().sendPacket(user, ExPacketPlayOutTablistTeamPlayerAdd.wrap(FAKE_PLAYER_TEAM_NAME, player.getName()));

            user.sendPacket(ExPacketPlayOutSpawnNamedEntity.wrap(player));
            user.sendPacket(ExPacketPlayOutEntityMetadata.wrap((Player) player, ExPacketPlayOutEntityMetadata.DataType.UPDATE));
        }

        Server.runTaskLaterAsynchrony(() -> {
            player.setPosition(loc.getX(), loc.getY(), loc.getZ());

            for (User user : users) {

                Location userLoc = locByUser.get(user);

                double deltaX = loc.getX() - userLoc.getX();
                double deltaY = loc.getY() - userLoc.getY();
                double deltaZ = loc.getZ() - userLoc.getZ();

                if (Math.abs(deltaX) < 8 && Math.abs(deltaY) < 8 && Math.abs(deltaZ) < 8) {
                    user.sendPacket(ExPacketPlayOutEntityRelMoveLook.wrap(player, deltaX, deltaY, deltaZ, loc.getYaw(), loc.getPitch(), true));
                } else {
                    user.sendPacket(ExPacketPlayOutEntityTeleport.wrap(player));

                }
            }
            if (removeFromTablist) {
                Server.getScoreboardManager().getPacketManager().sendPacket(users, ExPacketPlayOutPlayerInfo.wrap(ExPacketPlayOutPlayerInfo.Action.REMOVE_PLAYER, player));
            }
        }, 10, BasicBukkit.getPlugin());

    }

    @Override
    public void removePlayer(User user, ExPlayer player) {
        this.removePlayer(List.of(user), player);
    }

    @Override
    public void removePlayer(Collection<? extends User> users, ExPlayer player) {
        Server.getScoreboardManager().getPacketManager().sendPacket(users, ExPacketPlayOutPlayerInfo.wrap(ExPacketPlayOutPlayerInfo.Action.REMOVE_PLAYER, player));
        Server.getScoreboardManager().getPacketManager().sendPacket(users, ExPacketPlayOutEntityDestroy.wrap(player));
    }

    @EventHandler
    public void onUserJoin(UserJoinEvent e) {
        Server.getScoreboardManager().getPacketManager().sendPacket(e.getUser(), ExPacketPlayOutTablistTeamCreation.wrap(FAKE_PLAYER_TEAM_NAME, "", ChatColor.WHITE, ExPacketPlayOutTablistTeamCreation.NameTagVisibility.NEVER));
    }
}
