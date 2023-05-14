/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world.entity;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.inventory.ExItemStack;
import de.timesnake.basic.bukkit.util.world.ExBlock;
import de.timesnake.library.entities.entity.bukkit.ExItemFrame;
import de.timesnake.library.packets.core.packet.out.ExPacketPlayOutMap;
import de.timesnake.library.packets.util.packet.ExPacketPlayOutEntityDestroy;
import de.timesnake.library.packets.util.packet.ExPacketPlayOutEntityMetadata;
import de.timesnake.library.packets.util.packet.ExPacketPlayOutSpawnEntity;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.bukkit.Rotation;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.util.Vector;

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
    private final ConcurrentHashMap<User, ExItemFrame[][]> framesByUser = new ConcurrentHashMap<>();

    public MapDisplay(ExItemStack[][] maps, ExBlock baseBlock, BlockFace blockFace,
            BlockFace orientationUp,
            boolean placeOnBlock) {
        super(baseBlock.getLocation());
        this.blockFace = blockFace;
        this.orientation = orientationUp;
        this.rotation = blockFaceToRotation(blockFace, orientationUp);

        this.maps = maps;

        if (placeOnBlock) {
            this.baseBlock = baseBlock.getRelative(blockFace);
        } else {
            this.baseBlock = baseBlock;
        }

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
            ExItemFrame[][] frames = this.framesByUser.get(user);

            if (frames == null) {
                frames = new ExItemFrame[this.maps.length][this.maps[0].length];

                for (int x = 0; x < maps.length; x++) {
                    for (int y = 0; y < maps[x].length; y++) {
                        ExBlock block = this.frameLocations[x][y];
                        ExItemStack map = maps[x][y];

                        ExItemFrame frame = new ExItemFrame(block.getLocation().getWorld());
                        frame.setFixed(true);
                        frame.setItem(map, false);
                        frame.setInvulnerable(true);
                        frame.setFacingDirection(blockFace);
                        frame.setVisible(false);
                        frame.setRotation(this.rotation);
                        frame.setPosition(block.getBlock().getX(), block.getBlock().getY(),
                                block.getBlock().getZ(),
                                false);

                        frames[x][y] = frame;
                    }
                }
                this.framesByUser.put(user, frames);
            }

            for (int x = 0; x < maps.length; x++) {
                for (int y = 0; y < maps[x].length; y++) {
                    ExItemFrame frame = frames[x][y];
                    ExItemStack map = this.maps[x][y];

                    MapView view = ((MapMeta) map.getItemMeta()).getMapView();

                    frame.setItem(map, false);
                    frame.setVisible(true);
                    frame.setVisible(false);
                    frame.setRotation(this.rotation.rotateClockwise());
                    frame.setRotation(this.rotation);

                    user.sendPacket(ExPacketPlayOutSpawnEntity.wrap(frame,
                            ExPacketPlayOutSpawnEntity.blockFaceToRotation(blockFace)));

                    user.sendPacket(ExPacketPlayOutEntityMetadata.wrap(frame,
                            ExPacketPlayOutEntityMetadata.DataType.UPDATE, false));

                    user.sendPacket(new ExPacketPlayOutMap(view.getId(), view.getScale().getValue(),
                            view.isLocked(), view, user.getPlayer()));
                }
            }
        }, BasicBukkit.getPlugin());
    }

    @Override
    public void despawnForUser(User user) {
        ExItemFrame[][] frames = this.framesByUser.get(user);

        if (frames == null) {
            return;
        }

        Server.runTaskLoopAsynchrony(
                (frame) -> user.sendPacket(ExPacketPlayOutEntityDestroy.wrap(frame)),
                Arrays.stream(frames).flatMap(Arrays::stream).collect(Collectors.toList()),
                BasicBukkit.getPlugin());

    }

    @Override
    public void onUserQuit(User user) {
        this.despawnForUser(user);
    }
}
