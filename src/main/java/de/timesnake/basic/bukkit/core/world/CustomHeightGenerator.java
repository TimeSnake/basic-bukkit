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

package de.timesnake.basic.bukkit.core.world;

import de.timesnake.library.basic.util.Tuple;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.PerlinOctaveGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class CustomHeightGenerator extends ChunkGenerator {

    private final boolean simplexGenerator;
    private final double xScale;
    private final double yScale;
    private final double zScale;
    private final double frequency;
    private final double amplitude;
    private final int baseHeight;
    private final List<Tuple<Integer, Material>> materials;

    public CustomHeightGenerator(boolean simplexGenerator, double xScale, double yScale, double zScale, double frequency,
                                 double amplitude, int baseHeight, List<Tuple<Integer, Material>> materials) {
        this.simplexGenerator = simplexGenerator;
        this.xScale = xScale;
        this.yScale = yScale;
        this.zScale = zScale;
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.baseHeight = baseHeight;
        this.materials = materials;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunk = super.createChunkData(world);

        OctaveGenerator generator;

        if (this.simplexGenerator) {
            generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
        } else {
            generator = new PerlinOctaveGenerator(new Random(world.getSeed()), 8);

        }
        generator.setXScale(this.xScale);
        generator.setYScale(this.zScale);

        int minHeight = world.getMinHeight();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int currentHeight = (int) (generator.noise(chunkX * 16 + x, chunkZ * 16 + z, this.frequency,
                        this.amplitude) * (13.6842105 * this.yScale + 0.3157895) * 15D + this.baseHeight);

                for (Tuple<Integer, Material> tuple : this.materials) {
                    int layerHeight = tuple.getA();
                    Material material = tuple.getB();

                    for (int i = 0; i < layerHeight; i++) {
                        chunk.setBlock(x, currentHeight, z, material);
                        currentHeight--;

                        if (currentHeight < minHeight) {
                            break;
                        }
                    }

                    if (currentHeight < minHeight) {
                        break;
                    }
                }

                biome.setBiome(x, z, Biome.PLAINS);
            }
        }

        return chunk;
    }

    @Override
    public boolean canSpawn(World world, int x, int z) {
        return true;
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0, 65, 0);
    }

}