package de.timesnake.basic.bukkit.core.world;

import de.timesnake.basic.bukkit.util.file.ExFile;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.UUID;

public class LocationsFile extends ExFile {

    private static final String X = "x";
    private static final String Y = "y";
    private static final String Z = "z";

    private static final String YAW = "yaw";
    private static final String PITCH = "pitch";


    public LocationsFile(UUID uuid) {
        super("basic-bukkit/locations", uuid.toString());
    }

    public static String getPath() {
        return "plugins/basic-bukkit/locations";
    }

    public static File getFolder() {
        return new File(getPath());
    }

    public void setUserLocation(Location location) {
        String worldName = location.getWorld().getName();
        super.set(ExFile.toPath(worldName, X), location.getX());
        super.set(ExFile.toPath(worldName, Y), location.getY());
        super.set(ExFile.toPath(worldName, Z), location.getZ());
        super.set(ExFile.toPath(worldName, YAW), location.getYaw());
        super.set(ExFile.toPath(worldName, PITCH), location.getPitch());
        this.save();
    }

    public Location getUserLocation(World world) {
        this.load();

        String worldName = world.getName();
        if (!super.contains(worldName)) {
            return world.getSpawnLocation();
        }
        return new Location(world, super.getDouble(ExFile.toPath(worldName, X)), super.getDouble(ExFile.toPath(worldName, Y)), super.getDouble(ExFile.toPath(worldName, Z)), super.getDouble(ExFile.toPath(worldName, YAW)).floatValue(), super.getDouble(ExFile.toPath(worldName, PITCH)).floatValue());
    }

    public void removeWorld(World world) {
        this.remove(world.getName());
    }

}
