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

import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.world.ExLocation;
import de.timesnake.library.entities.entity.bukkit.ExArmorStand;
import de.timesnake.library.packets.util.packet.ExPacketPlayOutEntityDestroy;
import de.timesnake.library.packets.util.packet.ExPacketPlayOutEntityMetadata;
import de.timesnake.library.packets.util.packet.ExPacketPlayOutSpawnEntity;

import java.util.ArrayList;
import java.util.List;

public class HoloDisplay extends PacketEntity {

    private final List<String> text;

    private final List<ExArmorStand> stands;

    public HoloDisplay(ExLocation location, List<String> text) {
        super(location);
        this.text = text;

        this.stands = new ArrayList<>();

        double offset = (this.text.size() - 1) * 0.275;
        for (String line : this.text) {
            ExArmorStand stand = new ExArmorStand(location.getWorld());

            stand.setCustomName(line);
            stand.setCustomNameVisible(true);
            stand.setSmall(true);
            stand.setInvulnerable(false);
            stand.setInvisible(true);
            stand.setNoGravity(true);
            stand.setPosition(this.location.getX(), this.location.getY() + offset, this.location.getZ());

            offset -= 0.275;

            this.stands.add(stand);
        }
    }

    public List<String> getText() {
        return text;
    }

    @Override
    public void spawnForUser(User user) {
        for (ExArmorStand stand : this.stands) {
            user.sendPacket(ExPacketPlayOutSpawnEntity.wrap(stand));
            user.sendPacket(ExPacketPlayOutEntityMetadata.wrap(stand, ExPacketPlayOutEntityMetadata.DataType.UPDATE));
        }
    }

    @Override
    public void despawnForUser(User user) {
        for (ExArmorStand stand : this.stands) {
            user.sendPacket(ExPacketPlayOutEntityDestroy.wrap(stand));
        }
    }
}
