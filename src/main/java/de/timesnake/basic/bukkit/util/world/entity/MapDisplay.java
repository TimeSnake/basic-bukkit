/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world.entity;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.inventory.ExItemStack;
import de.timesnake.basic.bukkit.util.world.ExBlock;
import de.timesnake.library.packets.core.packet.out.ClientboundMapItemDataPacketBuilder;
import de.timesnake.library.packets.core.packet.out.entity.ClientboundSetEntityDataPacketBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import org.bukkit.Rotation;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MapDisplay extends PacketEntity {

  public static Rotation blockFaceToRotation(BlockFace blockFace, BlockFace orientation) {
    if (blockFace.getModY() != 0) {
      if (orientation.equals(BlockFace.SOUTH)) {
        return Rotation.CLOCKWISE;
      }

      if (blockFace.equals(BlockFace.UP)) {
        if (orientation.equals(BlockFace.WEST)) {
          return Rotation.COUNTER_CLOCKWISE_45;
        }
        if (orientation.equals(BlockFace.EAST)) {
          return Rotation.CLOCKWISE_45;
        }
      } else if (blockFace.equals(BlockFace.DOWN)) {
        if (orientation.equals(BlockFace.WEST)) {
          return Rotation.COUNTER_CLOCKWISE_45;
        }
        if (orientation.equals(BlockFace.EAST)) {
          return Rotation.CLOCKWISE_45;
        }
      }

      return Rotation.NONE;
    }

    if (blockFace.getModZ() != 0) {
      if (orientation.equals(BlockFace.DOWN)) {
        return Rotation.CLOCKWISE;
      }

      if (blockFace.equals(BlockFace.SOUTH)) {
        if (orientation.equals(BlockFace.WEST)) {
          return Rotation.COUNTER_CLOCKWISE_45;
        }
        if (orientation.equals(BlockFace.EAST)) {
          return Rotation.CLOCKWISE_45;
        }
      } else if (blockFace.equals(BlockFace.NORTH)) {
        if (orientation.equals(BlockFace.WEST)) {
          return Rotation.CLOCKWISE_45;
        }
        if (orientation.equals(BlockFace.EAST)) {
          return Rotation.COUNTER_CLOCKWISE_45;
        }
      }
      return Rotation.NONE;
    }

    if (blockFace.getModX() != 0) {
      if (orientation.equals(BlockFace.DOWN)) {
        return Rotation.CLOCKWISE;
      }

      if (blockFace.equals(BlockFace.WEST)) {
        if (orientation.equals(BlockFace.SOUTH)) {
          return Rotation.CLOCKWISE_45;
        }
        if (orientation.equals(BlockFace.NORTH)) {
          return Rotation.COUNTER_CLOCKWISE_45;
        }
      } else if (blockFace.equals(BlockFace.EAST)) {
        if (orientation.equals(BlockFace.NORTH)) {
          return Rotation.CLOCKWISE_45;
        }
        if (orientation.equals(BlockFace.SOUTH)) {
          return Rotation.COUNTER_CLOCKWISE_45;
        }
      }
      return Rotation.NONE;
    }

    return null;
  }

  protected final ExBlock baseBlock;
  protected final BlockFace blockFace;
  protected final BlockFace orientation;
  protected final Rotation rotation;
  private final ExItemStack[][] maps;
  private final ExBlock[][] frameLocations;
  private final ConcurrentHashMap<User, ItemFrame[][]> framesByUser = new ConcurrentHashMap<>();

  public MapDisplay(ExItemStack[][] maps, ExBlock baseBlock, BlockFace blockFace,
                    BlockFace orientationUp) {
    super(baseBlock.getLocation());
    this.blockFace = blockFace;
    this.orientation = orientationUp;
    this.rotation = blockFaceToRotation(blockFace, orientationUp);

    this.maps = maps;

    this.baseBlock = baseBlock;

    Vector orientationVector = this.orientation.getDirection();

    Vector xVector = blockFace.getDirection().crossProduct(orientationVector).multiply(-1);
    Vector yVector = orientationVector.clone().multiply(-1);

    this.frameLocations = new ExBlock[maps.length][maps[0].length];

    for (int x = 0; x < maps.length; x++) {

      ExBlock block = this.baseBlock.getRelative(xVector.clone().multiply(x));

      for (int y = 0; y < maps[x].length; y++) {
        frameLocations[x][y] = block;
        block = block.getRelative(yVector);
      }
    }
  }

  @Override
  public void spawnForUser(User user) {
    Server.runTaskAsynchrony(() -> {
      ItemFrame[][] frames = this.framesByUser.get(user);

      if (frames == null) {
        frames = new ItemFrame[this.maps.length][this.maps[0].length];

        for (int x = 0; x < maps.length; x++) {
          for (int y = 0; y < maps[x].length; y++) {
            ExBlock block = this.frameLocations[x][y];
            ExItemStack map = maps[x][y];

            ItemFrame frame = new ItemFrame(EntityType.ITEM_FRAME, block.getLocation().getExWorld().getHandle());
            frame.fixed = true;
            frame.setItem(map.getHandle(), false);
            frame.setInvulnerable(true);
            frame.setDirection(Direction.fromDelta(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ()));
            frame.setInvisible(false);
            frame.setRotation(rotationToInteger(this.rotation));
            frame.setPos(block.getBlock().getX(), block.getBlock().getY(), block.getBlock().getZ());

            frames[x][y] = frame;
          }
        }
        this.framesByUser.put(user, frames);
      }

      for (int x = 0; x < maps.length; x++) {
        for (int y = 0; y < maps[x].length; y++) {
          ItemFrame frame = frames[x][y];
          ExItemStack map = this.maps[x][y];
          Block block = this.frameLocations[x][y].getBlock();

          MapView view = ((MapMeta) map.getItemMeta()).getMapView();

          frame.setItem(map.getHandle(), false);
          frame.setInvisible(false);
          frame.setRotation(rotationToInteger(this.rotation.rotateClockwise()));
          frame.setRotation(rotationToInteger(this.rotation));

          user.sendPacket(new ClientboundAddEntityPacket(frame,
              ItemFrameRotation.blockFaceToRotation(blockFace).getNms(),
              new BlockPos(block.getX(), block.getY(), block.getZ())));
          user.sendPacket(new ClientboundSetEntityDataPacketBuilder(frame).setAllFromEntity().build());

          user.sendPacket(ClientboundMapItemDataPacketBuilder.of(view, user.getCraftPlayer()));
        }
      }
    }, BasicBukkit.getPlugin());
  }

  @Override
  public void despawnForUser(User user) {
    ItemFrame[][] frames = this.framesByUser.get(user);

    if (frames == null) {
      return;
    }

    Server.runTaskLoopAsynchrony(
        (frame) -> user.sendPacket(new ClientboundRemoveEntitiesPacket(frame.getId())),
        Arrays.stream(frames).flatMap(Arrays::stream).collect(Collectors.toList()),
        BasicBukkit.getPlugin());

  }

  @Override
  public void onUserQuit(User user) {
    this.despawnForUser(user);
  }

  @Override
  public String getType() {
    return "map";
  }

  enum ItemFrameRotation {

    DOWN(0),
    UP(1),
    NORTH(2),
    SOUTH(3),
    WEST(4),
    EAST(5);

    private final int nms;

    ItemFrameRotation(int nms) {
      this.nms = nms;
    }

    public int getNms() {
      return nms;
    }

    static ItemFrameRotation blockFaceToRotation(BlockFace face) {
      return switch (face) {
        case DOWN -> ItemFrameRotation.DOWN;
        case UP -> ItemFrameRotation.UP;
        case NORTH -> ItemFrameRotation.NORTH;
        case SOUTH -> ItemFrameRotation.SOUTH;
        case WEST -> ItemFrameRotation.WEST;
        case EAST -> ItemFrameRotation.EAST;
        default -> null;
      };
    }
  }

  static int rotationToInteger(Rotation rotation) {
    return switch (rotation) {
      case NONE -> 0;
      case CLOCKWISE_45 -> 1;
      case CLOCKWISE -> 2;
      case CLOCKWISE_135 -> 3;
      case FLIPPED -> 4;
      case FLIPPED_45 -> 5;
      case COUNTER_CLOCKWISE -> 6;
      case COUNTER_CLOCKWISE_45 -> 7;
      default -> throw new IllegalArgumentException(rotation + " is not applicable to an ItemFrame");
    };
  }
}
