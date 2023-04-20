/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world;

import de.timesnake.basic.bukkit.util.Server;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class ExBlock {

    private static final List<Vector> NEAR_BLOCKS = List.of(
            new Vector(1, 0, 0),
            new Vector(-1, 0, 0),
            new Vector(0, 1, 0),
            new Vector(0, -1, 0),
            new Vector(0, 0, 1),
            new Vector(0, 0, -1));

    private final ExLocation location;

    public ExBlock(Block block) {
        Location loc = block.getLocation();
        this.location = new ExLocation(Server.getWorld(block.getWorld()), loc.getX(), loc.getY(),
                loc.getZ());
    }

    public ExLocation getLocation() {
        return location;
    }

    public Block getBlock() {
        return this.location.getBlock();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return this.location.equals(((ExBlock) o).getLocation());
    }

    public ExBlock getRelative(BlockFace face) {
        return new ExBlock(this.location.getBlock().getRelative(face));
    }

    public ExBlock getRelative(Vector vector) {
        return this.location.clone().add(vector).getExBlock();
    }

    public ExBlock getRelative(int x, int y, int z) {
        return new ExBlock(this.location.getBlock().getRelative(x, y, z));
    }

    public boolean isBeside(Block location) {
        if (!this.getBlock().getWorld().equals(location.getWorld())) {
            return false;
        }

        for (Vector vector : NEAR_BLOCKS) {
            if (this.getBlock().getLocation().add(vector).getBlock().equals(location)) {
                return true;
            }
        }

        return false;
    }
}
