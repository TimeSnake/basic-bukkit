package de.timesnake.basic.bukkit.core.world;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;
import de.timesnake.basic.entities.entity.bukkit.ExPlayer;
import de.timesnake.basic.packets.util.packet.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.List;

public class EntityManager implements Listener, de.timesnake.basic.bukkit.util.world.EntityManager {

    private static final String FAKE_PLAYER_TEAM_NAME = "fake_players";

    public EntityManager() {
        Server.registerListener(this, BasicBukkit.getPlugin());
    }

    @Override
    public void spawnPlayer(User user, ExPlayer player) {
        this.spawnPlayer(List.of(user), player);
    }

    @Override
    public void spawnPlayer(Collection<? extends User> users, ExPlayer player) {
        Server.getScoreboardManager().getPacketManager().sendPacket(users, ExPacketPlayOutPlayerInfo.wrap(ExPacketPlayOutPlayerInfo.Action.ADD_PLAYER, player));
        Server.getScoreboardManager().getPacketManager().sendPacket(users, ExPacketPlayOutTablistTeamPlayerAdd.wrap(FAKE_PLAYER_TEAM_NAME, player.getName()));

        Server.broadcastPacket(ExPacketPlayOutSpawnNamedEntity.wrap(player));
        Server.broadcastPacket(ExPacketPlayOutEntityMetadata.wrap((Player) player, ExPacketPlayOutEntityMetadata.DataType.UPDATE));

        Server.runTaskLaterSynchrony(() -> Server.getScoreboardManager().getPacketManager().sendPacket(users, ExPacketPlayOutPlayerInfo.wrap(ExPacketPlayOutPlayerInfo.Action.REMOVE_PLAYER, player)), 3, BasicBukkit.getPlugin());

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
