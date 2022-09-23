package de.timesnake.basic.bukkit.util.world.entity;

import de.timesnake.basic.bukkit.util.world.ExLocation;
import de.timesnake.library.entities.entity.bukkit.ExPlayer;
import de.timesnake.library.entities.entity.bukkit.ExZombie;
import de.timesnake.library.entities.wrapper.ExPathEntity;
import de.timesnake.library.entities.wrapper.ExPathPoint;
import org.bukkit.Location;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ExtendedPacketPlayer extends PacketPlayer {

    private final ExZombie symEntity;

    public ExtendedPacketPlayer(ExPlayer player, ExLocation location) {
        super(player, location);
        this.symEntity = new ExZombie(location.getWorld(), false);
        this.symEntity.setPosition(location.getX(), location.getY(), location.getZ());
    }

    public CompletableFuture<Location> moveTo(Location location) {
        ExPathEntity path = this.symEntity.getNavigation().calcExPathTo(location.getX(), location.getY(), location.getZ(), 32);
        List<ExPathPoint> points = path.getExPathPoints();

        return null;
    }
}
