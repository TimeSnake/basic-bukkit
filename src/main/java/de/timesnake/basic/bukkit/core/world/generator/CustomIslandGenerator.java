/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.world.generator;

import de.timesnake.library.basic.util.Tuple;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nonnull;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.PerlinOctaveGenerator;

public class CustomIslandGenerator extends ChunkGenerator {

    private final float density;
    private final double xScale;
    private final double yScale;
    private final double zScale;
    private final double frequency;
    private final double amplitude;
    private final Map<Integer, Material> materialByDepth = new HashMap<>();

    public CustomIslandGenerator(float density, double xScale, double yScale, double zScale, double frequency,
                                 double amplitude, List<Tuple<Integer, Material>> materials) {
        this.density = density;
        this.xScale = xScale;
        this.yScale = yScale;
        this.zScale = zScale;
        this.frequency = frequency;
        this.amplitude = amplitude;

        int depth = 1;
        for (Tuple<Integer, Material> entry : materials) {
            for (int thickness = 0; thickness < entry.getA(); thickness++, depth++) {
                this.materialByDepth.put(depth, entry.getB());
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunk = super.createChunkData(world);

        OctaveGenerator generator = new PerlinOctaveGenerator(new Random(world.getSeed()), 8);
        generator.setXScale(this.xScale);
        generator.setYScale(this.yScale);
        generator.setZScale(this.zScale);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = world.getMaxHeight() - 1; y >= world.getMinHeight(); y--) {
                    double chance = (generator.noise(chunkX * 16 + x, y, chunkZ * 16 + z, this.frequency,
                            this.amplitude) + 1) / 2;
                    if (this.density - chance >= 0) {
                        for (int upper = 1; upper + y < world.getMaxHeight(); upper++) {
                            Material upperType = chunk.getType(x, y + upper, z);
                            if (upperType == null || upperType.isAir()) {
                                chunk.setBlock(x, y, z, this.materialByDepth.get(upper));
                                break;
                            } else if (!this.materialByDepth.containsKey(upper + 1)) {
                                chunk.setBlock(x, y, z, this.materialByDepth.get(upper));
                                break;
                            }
                        }
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
