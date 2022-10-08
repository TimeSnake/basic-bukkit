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

package de.timesnake.basic.bukkit.core.display;

import de.timesnake.basic.bukkit.util.user.ExItemStack;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapDisplayManager {


    public MapDisplayManager() {

    }

    public boolean placeMap(ExItemStack map, Block block, BlockFace blockFace) {
        /*
        ExItemStack item = this.drawToItem();

        Block mapBlock = block.getRelative(blockFace);

        if (!mapBlock.getType().equals(Material.AIR)) {
            return false;
        }

        Location loc = user.getLocation();

        ExItemFrame frame = new ExItemFrame(user.getWorld().getBukkitWorld());
        frame.setPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), false);
        frame.setFixed(true);
        frame.setItem(item, false);
        frame.setInvulnerable(true);
        frame.setFacingDirection(blockFace);
        frame.setVisible(true);

        MapView view = ((MapMeta) item.getItemMeta()).getMapView();

        Server.runTaskLaterSynchrony(() -> {
            user.sendPacket(ExPacketPlayOutSpawnEntity.wrap(frame, ExPacketPlayOutSpawnEntity.blockFaceToRotation
            (blockFace)));
            user.sendPacket(ExPacketPlayOutEntityMetadata.wrap(frame, ExPacketPlayOutEntityMetadata.DataType.UPDATE,
            false));
            user.sendPacket(new ExPacketPlayOutMap(view.getId(), view.getScale().getValue(), view.isLocked(), view,
            user.getPlayer()));

            System.out.println("send");
        }, 40, BasicBukkit.getPlugin());


         */
        return false;
    }

    public ExItemStack[][] convertImage(File file, int width, int height, ScaleType scaleType) {
        BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            return new ExItemStack[0][0];
        }

        switch (scaleType) {
            case WIDTH -> image = (BufferedImage) image.getScaledInstance(width * 128,
                    (int) (width * 128 / ((double) image.getWidth(null))), Image.SCALE_DEFAULT);
            case HEIGHT ->
                    image = (BufferedImage) image.getScaledInstance((int) (height * 128 / ((double) image.getHeight(null))),
                            height * 128, Image.SCALE_DEFAULT);
            case CUSTOM -> image = (BufferedImage) image.getScaledInstance(width * 128, height * 128, 0);
        }

        ExItemStack[][] maps = new ExItemStack[width][height];

        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                // TODO size
                //maps[w][h] = new MultiMapDisplayBuilder(0, 0).drawImage(0, 0, image.getSubimage(w * 128, h * 128,
                // 128, 128)).onItems();
            }
        }

        return maps;
    }

    public enum ScaleType {
        WIDTH,
        HEIGHT,
        CUSTOM
    }


}
