/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.world.generator;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.annotation.Nonnull;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public class MiniWorldGenerator extends ChunkGenerator {

  private final int chunkXSize;
  private final int chunkZSize;

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
  public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ,
      BiomeGrid biome) {
    ChunkData chunkData = super.createChunkData(world);

    if (!(chunkX >= -this.chunkXSize && chunkX < this.chunkXSize
        && chunkZ >= -this.chunkZSize && chunkZ < this.chunkZSize)) {
      for (int x = 0; x < 16; x++) {
        for (int z = 0; z < 16; z++) {
          for (int y = world.getMinHeight(); y < world.getMaxHeight(); y++) {
            chunkData.setBlock(x, y, z, Material.AIR);
          }
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
    return world.getHighestBlockAt(0, 0).getLocation();
  }

}
