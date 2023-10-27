/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.world.generator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Generates a world from 0,0 to given chunk size
 */
public class MiniWorldGenerator extends ChunkGenerator {

  private int chunkXSize;
  private int chunkZSize;

  public MiniWorldGenerator(int chunkXSize, int chunkZSize) {
    this.chunkXSize = chunkXSize;
    this.chunkZSize = chunkZSize;
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
    ChunkData chunkData = super.createChunkData(world);

    if (chunkX >= 0 && chunkX < this.chunkXSize && chunkZ >= 0 && chunkZ < this.chunkZSize) {
      return chunkData;
    }

    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        for (int y = world.getMinHeight(); y < world.getMaxHeight(); y++) {
          chunkData.setBlock(x, y, z, Material.AIR);
        }
      }
    }

    return chunkData;
  }

  @Override
  public boolean shouldGenerateNoise() {
    return true;
  }

  @Override
  public boolean shouldGenerateSurface() {
    return true;
  }

  @Override
  public boolean shouldGenerateBedrock() {
    return true;
  }

  @Override
  public boolean shouldGenerateCaves() {
    return true;
  }

  @Override
  public boolean shouldGenerateDecorations() {
    return true;
  }

  @Override
  public boolean shouldGenerateMobs() {
    return true;
  }

  @Override
  public boolean shouldGenerateStructures() {
    return false;
  }


  @Override
  public boolean canSpawn(World world, int x, int z) {
    return true;
  }

  @Override
  public Location getFixedSpawnLocation(World world, Random random) {
    return world.getHighestBlockAt(this.chunkXSize * 16 / 2, this.chunkZSize * 16 / 2).getLocation().add(0, 1, 0);
  }

  public void regenerate(World world, int chunkSizeX, int chunkSizeZ) {
    int maxX = Math.max(this.chunkXSize, chunkSizeX);
    int maxZ = Math.max(this.chunkZSize, chunkSizeZ);

    this.chunkXSize = chunkSizeX;
    this.chunkZSize = chunkSizeZ;

    for (int x = 0; x <= maxX; x++) {
      for (int z = 0; z <= maxZ; z++) {
        world.regenerateChunk(x, z);
      }
    }

  }
}
