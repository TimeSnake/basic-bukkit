/*
 * Copyright (C) 2022 timesnake
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
