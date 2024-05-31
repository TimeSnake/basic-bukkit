/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world.entity;

import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.world.ExLocation;
import de.timesnake.library.packets.core.packet.out.entity.ClientboundSetEntityDataPacketBuilder;
import de.timesnake.library.packets.core.packet.out.entity.ClientboundSetEquipmentPacketBuilder;
import net.minecraft.core.Rotations;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class ItemEntity extends PacketEntity {

  private final ArmorStand stand;
  private final ItemStack item;

  public ItemEntity(ExLocation location, ItemStack item, float angleX, float angleY, float angleZ, boolean small) {
    super(location);
    this.item = item;

    this.stand = new ArmorStand(EntityType.ARMOR_STAND, ((CraftWorld) location.getWorld()).getHandle());

    this.stand.setSmall(small);
    this.stand.setInvulnerable(false);
    this.stand.setInvisible(true);
    this.stand.setNoGravity(true);
    this.stand.setPos(this.location.getX(), this.location.getY(), this.location.getZ());
    this.stand.setItemSlot(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(item));
    this.stand.setRightArmPose(Rotations.createWithoutValidityChecks(angleX, angleY, angleZ));
  }

  public ItemStack getItem() {
    return item;
  }

  @Override
  public void spawnForUser(User user) {
    user.sendPacket(new ClientboundAddEntityPacket(this.stand));
    user.sendPacket(new ClientboundSetEntityDataPacketBuilder(this.stand).setAllFromEntity().build());
    user.sendPacket(new ClientboundSetEquipmentPacketBuilder(this.stand).addItem(EquipmentSlot.MAINHAND, this.item).build());
  }

  @Override
  public void despawnForUser(User user) {
    user.sendPacket(new ClientboundRemoveEntitiesPacket(this.stand.getId()));
  }

  @Override
  public String getType() {
    return "item (" + this.item.getType().name().toLowerCase() + ")";
  }
}
