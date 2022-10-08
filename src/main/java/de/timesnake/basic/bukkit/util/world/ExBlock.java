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
