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
        this.symEntity = new ExZombie(location.getWorld(), false, false);
        this.symEntity.setPosition(location.getX(), location.getY(), location.getZ());
    }

    public CompletableFuture<Location> moveTo(Location location) {
        ExPathEntity path = this.symEntity.getNavigation().calcExPathTo(location.getX(), location.getY(), location.getZ(), 32);
        List<ExPathPoint> points = path.getExPathPoints();

        return null;
    }
}
