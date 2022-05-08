package de.timesnake.basic.bukkit.util.world.entity;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.ExItemStack;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.world.ExBlock;
import de.timesnake.basic.entities.entity.bukkit.ExItemFrame;
import de.timesnake.basic.packets.core.packet.out.ExPacketPlayOutMap;
import de.timesnake.basic.packets.util.packet.ExPacketPlayOutEntityDestroy;
import de.timesnake.basic.packets.util.packet.ExPacketPlayOutEntityMetadata;
import de.timesnake.basic.packets.util.packet.ExPacketPlayOutSpawnEntity;
import de.timesnake.library.basic.util.Tuple;
import org.bukkit.Rotation;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MapDisplay extends PacketEntity {

    protected final BlockFace blockFace;
    protected final BlockFace orientation;
    protected final Rotation rotation;

    private final MapDisplayPart[][] displayParts;

    private final Set<User> mapLoadedForUser = new HashSet<>();

    public MapDisplay(ExItemStack[][] maps, ExBlock baseBlock, BlockFace blockFace, BlockFace orientationUp,
                      boolean placeOnBlock) {
        super(baseBlock.getLocation());
        this.blockFace = blockFace;
        this.orientation = orientationUp;
        this.rotation = blockFaceToRotation(blockFace, orientationUp);

        this.displayParts = new MapDisplayPart[maps.length][maps[0].length];

        if (placeOnBlock) {
            baseBlock = baseBlock.getRelative(blockFace);
        }

        Vector orientationVector = orientationUp.getDirection();

        Vector xVector = blockFace.getDirection().crossProduct(orientationVector).multiply(-1);
        Vector yVector = orientationVector.clone().multiply(-1);

        for (int x = 0; x < maps.length; x++) {

            ExBlock block = baseBlock.getRelative(xVector.clone().multiply(x));

            for (int y = 0; y < maps[x].length; y++) {

                ExItemStack map = maps[x][y];

                ExItemFrame frame = new ExItemFrame(block.getLocation().getWorld());
                frame.setFixed(true);
                frame.setItem(map, false);
                frame.setInvulnerable(true);
                frame.setFacingDirection(blockFace);
                frame.setVisible(false);
                frame.setRotation(this.rotation);
                frame.setPosition(block.getBlock().getX(), block.getBlock().getY(), block.getBlock().getZ(),
                        false);

                this.displayParts[x][y] = new MapDisplayPart(frame, map);

                block = block.getRelative(yVector);
            }
        }
    }

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

    @Override
    public void spawn(User user) {

        boolean loadMap = false;
        if (!this.mapLoadedForUser.contains(user)) {
            loadMap = true;
            this.mapLoadedForUser.add(user);
        }

        this.despawn(user);


        boolean finalLoadMap = loadMap;
        Server.runTaskLoopAsynchrony((part) -> {
            ExItemFrame frame = part.getFrame();
            ExItemStack map = part.getMap();
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

            if (finalLoadMap) {
                user.sendPacket(new ExPacketPlayOutMap(view.getId(), view.getScale().getValue(), view.isLocked(),
                        view, user.getPlayer()));

            }
        }, Arrays.stream(this.displayParts).flatMap(Arrays::stream).collect(Collectors.toList()),
                BasicBukkit.getPlugin());
    }

    @Override
    public void despawn(User user) {
        Server.runTaskLoopAsynchrony((part) -> {
            user.sendPacket(ExPacketPlayOutEntityDestroy.wrap(part.getFrame()));
        }, Arrays.stream(this.displayParts).flatMap(Arrays::stream).collect(Collectors.toList()),
                BasicBukkit.getPlugin());

    }

    @Override
    public void onUserQuit(User user) {
        super.onUserQuit(user);
        this.mapLoadedForUser.remove(user);
    }

    private static class MapDisplayPart extends Tuple<ExItemFrame, ExItemStack> {

        public MapDisplayPart(ExItemFrame frame, ExItemStack map) {
            super(frame, map);
        }

        public ExItemFrame getFrame() {
            return super.getA();
        }

        public ExItemStack getMap() {
            return super.getB();
        }
    }
}
