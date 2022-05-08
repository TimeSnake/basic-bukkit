package de.timesnake.basic.bukkit.util.world;

import de.timesnake.basic.bukkit.util.Server;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class ExBlock {

    private final ExLocation location;

    public ExBlock(Block block) {
        Location loc = block.getLocation();
        this.location = new ExLocation(Server.getWorld(block.getWorld()), loc.getX(), loc.getY(), loc.getZ());
    }

    public ExLocation getLocation() {
        return location;
    }

    public Block getBlock() {
        return this.location.getBlock();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
}
