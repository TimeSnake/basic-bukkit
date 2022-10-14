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
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.PerlinOctaveGenerator;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CustomHeightGenerator extends ChunkGenerator {

    public static final double SCALE = 0.005D;
    public static final double FREQUENCY = 0.5D;
    public static final double AMPLITUDE = 0.5D;

    private final double scale;
    private final double frequency;
    private final double amplitude;
    private final List<Tuple<Integer, Material>> materials;

    public CustomHeightGenerator(double scale, double frequency, double amplitude, List<Tuple<Integer, Material>> materials) {
        this.scale = scale;
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.materials = materials;
    }

    public CustomHeightGenerator(double scale, double frequency, List<Tuple<Integer, Material>> materials) {
        this(scale, frequency, 0.5D, materials);
    }

    public CustomHeightGenerator(double scale, List<Tuple<Integer, Material>> materials) {
        this(scale, 0.5D, materials);
    }

    public CustomHeightGenerator(List<Tuple<Integer, Material>> materials) {
        this(0.005D, materials);
    }

    @Override
    @Nonnull
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Collections.emptyList();
    }


    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunk = super.createChunkData(world);

        OctaveGenerator generator = new PerlinOctaveGenerator(new Random(world.getSeed()), 8);
        generator.setScale(this.scale);

        int minHeight = world.getMinHeight();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int currentHeight = (int) (generator.noise(chunkX * 16 + x, chunkZ * 16 + z, this.frequency, this.amplitude) * 15D + 50D);

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
            }
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
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
