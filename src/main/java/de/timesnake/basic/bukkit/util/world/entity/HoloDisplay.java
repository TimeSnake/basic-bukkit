/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world.entity;

import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.world.ExLocation;
import de.timesnake.library.packets.core.packet.out.entity.ClientboundSetEntityDataPacketBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class HoloDisplay extends PacketEntity {

  private static final double OFFSET = 0.275;

  private final List<String> text;

  private final List<ArmorStand> stands;

  public HoloDisplay(ExLocation location, List<String> text) {
    super(location);
    this.text = text;

    this.stands = new LinkedList<>();

    List<String> strings = this.text;
    for (int i = 0; i < strings.size(); i++) {
      String line = strings.get(i);
      this.stands.add(this.addLineEntity(i, line));
    }
  }

  public List<String> getText() {
    return text;
  }

  @Override
  public void spawnForUser(User user) {
    for (ArmorStand stand : this.stands) {
      user.sendPacket(new ClientboundAddEntityPacket(stand));
      user.sendPacket(new ClientboundSetEntityDataPacketBuilder(stand).setFlagsFromEntity().build());
    }
  }

  @Override
  public void despawnForUser(User user) {
    for (ArmorStand stand : this.stands) {
      user.sendPacket(new ClientboundRemoveEntitiesPacket(stand.getId()));
    }
  }

  public void setText(List<String> text) {
    Iterator<ArmorStand> standIterator = this.stands.iterator();
    Iterator<String> textIterator = text.iterator();

    int lineNumber = 0;

    while (standIterator.hasNext() && textIterator.hasNext()) {
      ArmorStand stand = standIterator.next();
      stand.setCustomName(Component.literal(textIterator.next()));
      for (User user : this.userLoadedFor) {
        user.sendPacket(new ClientboundSetEntityDataPacketBuilder(stand).setFlagsFromEntity().build());
      }
      lineNumber++;
    }

    while (standIterator.hasNext()) {
      for (User user : this.userLoadedFor) {
        user.sendPacket(new ClientboundRemoveEntitiesPacket(standIterator.next().getId()));
      }
    }

    while (textIterator.hasNext()) {
      this.addLineEntity(lineNumber, textIterator.next());
      lineNumber++;
    }
  }

  private ArmorStand addLineEntity(int lineNumber, String text) {
    ArmorStand stand = new ArmorStand(EntityType.ARMOR_STAND, ((CraftWorld) location.getWorld()).getHandle());

    stand.setCustomName(Component.literal(text));
    stand.setCustomNameVisible(true);
    stand.setSmall(true);
    stand.setInvulnerable(false);
    stand.setInvisible(true);
    stand.setNoGravity(true);
    stand.setPos(this.location.getX(), this.location.getY() + (lineNumber * OFFSET), this.location.getZ());
    return stand;
  }

  @Override
  public String getType() {
    return "display (" + String.join(";", this.text) + ")";
  }
}
