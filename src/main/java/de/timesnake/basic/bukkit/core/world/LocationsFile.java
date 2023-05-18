/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.world;

import de.timesnake.basic.bukkit.util.file.ExFile;
import java.io.File;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationsFile extends ExFile {

  public static String getPath() {
    return "plugins/basic-bukkit/locations";
  }

  public static File getFolder() {
    return new File(getPath());
  }

  private static final String X = "x";
  private static final String Y = "y";
  private static final String Z = "z";
  private static final String YAW = "yaw";
  private static final String PITCH = "pitch";

  public LocationsFile(UUID uuid) {
    super("basic-bukkit/locations", uuid.toString());
  }

  public void setUserLocation(Location location) {
    String worldName = location.getWorld().getName();
    super.setLocation(worldName, location, true).save();
  }

  public Location getUserLocation(World world) {
    this.load();

    String worldName = world.getName();
    if (!super.contains(worldName)) {
      return world.getSpawnLocation();
    }
    return new Location(world, super.getDouble(ExFile.toPath(worldName, X)),
        super.getDouble(ExFile.toPath(worldName, Y)), super.getDouble(ExFile.toPath(worldName, Z)),
        super.getDouble(ExFile.toPath(worldName, YAW)).floatValue(),
        super.getDouble(ExFile.toPath(worldName,
            PITCH)).floatValue());
  }

  public void removeWorld(World world) {
    this.remove(world.getName());
  }

}
