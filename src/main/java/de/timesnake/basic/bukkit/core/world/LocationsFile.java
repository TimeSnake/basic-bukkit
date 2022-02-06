package de.timesnake.basic.bukkit.core.world;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LocationsFile {

    private static final String LAST_LOCATION_SPAWN_PATH = "lastworldspawning";

    private File file;
    private YamlConfiguration config;

    public LocationsFile(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        file = new File(path + "/locations.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
        this.load();

        if (!config.contains(LAST_LOCATION_SPAWN_PATH)) {
            this.setIsLastWorldSpawn(false);
        }
        this.save();

        Server.printText(Plugin.BUKKIT, "Locations-file loaded", "World");
    }

    private void load() {
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the user location in file to actual user location
     *
     * @param user user
     */
    public void setUserLocation(User user) {
        this.setUserLocation(user, user.getLocation());
        this.save();
    }

    /**
     * Sets the user location in file to custom location
     *
     * @param user     user
     * @param location location
     */
    public void setUserLocation(User user, Location location) {
        String path = "worlds." + location.getWorld().getName() + "." + user.getUniqueId().toString();
        config.set(path + ".x", location.getX());
        config.set(path + ".y", location.getY());
        config.set(path + ".z", location.getZ());
        config.set(path + ".yaw", (double) location.getYaw());
        config.set(path + ".pitch", (double) location.getPitch());
        this.save();
    }

    /**
     * Sets last seen world from user
     *
     * @param user user
     */
    public void setUserWorld(User user) {
        config.set("users." + user.getUniqueId().toString(), user.getLocation().getWorld().getName());
        this.save();
    }

    /**
     * Gets user last world
     *
     * @param user The user to get the world
     * @return the last world from the user
     */
    public ExWorld getUserLastWorld(User user) {
        this.load();
        if (config.contains("users." + user.getUniqueId().toString())) {
            String world = config.getString("users." + user.getUniqueId().toString());
            if (world != null) {
                return Server.getWorld(world);
            }
        }
        return null;
    }

    /**
     * Gets user last location, by last world
     *
     * @param user The user to get the location
     * @return last location on server
     */
    public Location getUserLastLocation(User user) {
        this.load();
        ExWorld world = this.getUserLastWorld(user);
        if (world != null) {
            return this.getUserLocation(user, world);
        }
        return null;
    }

    /**
     * Gets user location of specific world
     *
     * @param user  user
     * @param world world to be deleted
     * @return returns last location from user in world
     */
    public Location getUserLocation(User user, ExWorld world) {
        this.load();
        if (world != null) {
            String path = "worlds." + world.getName() + "." + user.getUniqueId().toString();
            if (config.contains(path)) {
                if (config.getDouble(path + ".y") != 0) {
                    return new Location(world.getBukkitWorld(), config.getDouble(path + ".x"), config.getDouble(path + ".y"), config.getDouble(path + ".z"), (float) config.getDouble(path + ".yaw"), (float) config.getDouble(path + ".pitch"));
                } else {
                    return world.getSpawnLocation();
                }
            }
        }
        return null;
    }

    /**
     * Removes world from file
     *
     * @param world world to be deleted
     */
    public void removeWorld(ExWorld world) {
        this.removeWorld(world.getName());
    }

    /**
     * Removes world from file
     *
     * @param world name of world to be deleted
     */
    public void removeWorld(String world) {
        if (config.contains("worlds." + world)) {
            config.set("worlds." + world, null);
        }
        this.save();
    }

    public void setIsLastWorldSpawn(boolean lastWorldSpawn) {
        config.set(LAST_LOCATION_SPAWN_PATH, lastWorldSpawn);
        this.save();
    }

    public boolean isLastWorldSpawn() {
        return config.getBoolean(LAST_LOCATION_SPAWN_PATH);
    }
}
