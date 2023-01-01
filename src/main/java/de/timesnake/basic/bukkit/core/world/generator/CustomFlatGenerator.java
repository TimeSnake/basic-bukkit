/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.world.generator;

import de.timesnake.library.basic.util.Tuple;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.annotation.Nonnull;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public class CustomFlatGenerator extends ChunkGenerator {

    private final List<Tuple<Integer, Material>> materials;

    public CustomFlatGenerator(List<Tuple<Integer, Material>> materials) {
        this.materials = materials;
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

        int height = world.getMinHeight();
        for (Tuple<Integer, Material> tuple : this.materials) {
            int layerHeight = tuple.getA();
            Material material = tuple.getB();

            for (int y = height; y < height + layerHeight; y++) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        chunk.setBlock(x, y, z, material);
                    }
                }
            }

            height += layerHeight;
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
