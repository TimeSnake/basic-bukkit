package de.timesnake.basic.bukkit.util.file;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.exceptions.WorldNotExistException;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.world.ExLocation;
import de.timesnake.library.basic.util.Triple;
import de.timesnake.library.basic.util.chat.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ExFile {

    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            if (Objects.requireNonNull(file.list()).length == 0) file.delete();
            else {
                String[] files = file.list();

                for (String temp : Objects.requireNonNull(files)) {
                    ExFile.deleteFile(new File(file, temp));
                }
                if (Objects.requireNonNull(file.list()).length == 0) file.delete();
            }
        } else file.delete();
    }


    protected final File configFile;
    protected final YamlConfiguration config;

    public ExFile(String folder, String name) {
        this.configFile = new File("plugins/" + folder + "/" + name + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.configFile);

        //directory creation
        File dir = new File("plugins/" + folder);
        if (!dir.exists()) {
            dir.mkdir();
        }

        //file creation
        if (!this.configFile.exists()) {
            try {
                this.configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.config.options().copyDefaults(true);
    }

    public ExFile(File parentFile, String fullName) {
        this.configFile = new File(parentFile.getAbsolutePath() + File.separator + fullName);
        this.config = YamlConfiguration.loadConfiguration(this.configFile);

        //directory creation
        if (!parentFile.exists()) {
            parentFile.mkdir();
        }

        //file creation
        if (!this.configFile.exists()) {
            try {
                this.configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.config.options().copyDefaults(true);
    }

    public void save() {
        try {
            this.config.save(this.configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void load() {
        try {
            this.config.load(this.configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public boolean exists() {
        return this.configFile.exists();
    }

    public boolean delete() {
        return this.configFile.delete();
    }

    public void addDefault(@Nonnull String path, Object value) {
        this.load();
        this.config.addDefault(path, value);
        this.save();
    }

    public void addDefault(@Nonnull String path, boolean value) {
        this.load();
        this.config.addDefault(path, value);
        this.save();
    }

    public void addDefaultLocation(@Nonnull String path, Location loc) {
        this.config.addDefault(path + ".world", loc.getWorld().getName());
        this.config.addDefault(path + ".x", loc.getX());
        this.config.addDefault(path + ".y", loc.getY());
        this.config.addDefault(path + ".z", loc.getZ());
        this.config.addDefault(path + ".yaw", loc.getYaw());
        this.config.addDefault(path + ".pitch", loc.getPitch());
        this.save();
    }

    public void set(@Nonnull String path, Object value) {
        if (value != null) {
            if (value instanceof Location) {
                this.setLocation(path, (Location) value, true);
                return;
            } else if (value instanceof Block) {
                this.setBlock(path, ((Block) value));
                return;
            } else if (value instanceof UUID) {
                this.config.set(path, value.toString());
                return;
            } else if (value instanceof Player) {
                this.setPlayer(path, ((Player) value));
                return;
            } else if (value instanceof User) {
                this.setPlayer(path, ((User) value).getPlayer());
                return;
            } else if (value instanceof Color) {
                this.setHexColor(path, ((Color) value));
                return;
            }
        }
        this.load();
        this.config.set(path, value);
        this.save();
    }

    public void setLocation(@Nonnull String path, @Nonnull Location location, boolean saveYawPitch) {
        setLocBlock(path, location.getWorld(), location.getX(), location.getY(), location.getZ());
        if (saveYawPitch) {
            this.config.set(path + ".yaw", location.getYaw());
            this.config.set(path + ".pitch", location.getPitch());
        }
        this.save();
    }

    private void setLocBlock(@Nonnull String path, World world, double x, double y, double z) {
        this.config.set(path + ".world", world.getName());
        this.config.set(path + ".x", x);
        this.config.set(path + ".y", y);
        this.config.set(path + ".z", z);
        this.save();
    }

    public void setBlock(@Nonnull String path, @Nonnull Block block) {
        setLocBlock(path, block.getWorld(), block.getX(), block.getY(), block.getZ());
        this.save();
    }

    public void setPlayer(String path, Player player) {
        this.config.set(path + ".name", player.getName());
        this.config.set(path + ".uuid", player.getUniqueId().toString());
        this.save();
    }

    public void setUuidList(String path, List<UUID> uuids) {
        List<String> strings = new ArrayList<>();
        for (UUID uuid : uuids) {
            strings.add(uuid.toString());
        }
        this.config.set(path, strings);
        this.save();
    }

    public void setHexColor(String path, Color color) {
        this.load();
        this.config.set(path, String.format("%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue()));
        this.save();
    }

    public boolean contains(@Nonnull String path) {
        return this.config.contains(path);
    }

    public String getString(@Nonnull String path) {
        this.load();
        return this.config.getString(path);
    }

    public Integer getInt(@Nonnull String path) {
        this.load();
        if (this.config.isInt(path)) {
            return this.config.getInt(path);
        }
        return null;
    }

    public boolean getBoolean(@Nonnull String path) {
        this.load();
        if (this.config.isBoolean(path)) {
            return this.config.getBoolean(path);
        }
        return false;
    }

    public double getDouble(@Nonnull String path) {
        this.load();
        if (this.config.isDouble(path)) {
            return this.config.getDouble(path);
        }

        if (this.config.isInt(path)) {
            return this.config.getInt(path);
        }
        return 0;
    }

    public Location getLocation(@Nonnull String path) throws WorldNotExistException {
        this.load();
        if (this.config.contains(path + ".world")) {
            String worldName = this.config.getString(path + ".world");
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                return new Location(world, this.config.getDouble(path + ".x"), this.config.getDouble(path + ".y"), this.config.getDouble(path + ".z"), (float) this.config.getDouble(path + ".yaw", 0), (float) this.config.getDouble(path + ".pitch", 0));
            } else {
                throw new WorldNotExistException(worldName);
            }
        }
        return null;
    }

    public ExLocation getExLocation(@Nonnull String path) throws WorldNotExistException {
        this.load();
        if (this.config.contains(path + ".world")) {
            String worldName = this.config.getString(path + ".world");
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                return new ExLocation(Server.getWorld(world), this.config.getDouble(path + ".x"), this.config.getDouble(path + ".y"), this.config.getDouble(path + ".z"), (float) this.config.getDouble(path + ".yaw", 0), (float) this.config.getDouble(path + ".pitch", 0));
            } else {
                throw new WorldNotExistException(worldName);
            }
        }
        return null;
    }

    public Location getLocationWithId(Integer id, @Nonnull String path) throws WorldNotExistException {
        return this.getLocation(path + "." + id);
    }

    public Block getBlock(@Nonnull String path) throws WorldNotExistException {
        this.load();
        String worldName = this.config.getString(path + ".world");
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            return new Location(world, this.config.getInt(path + ".x"), this.config.getInt(path + ".y"), this.config.getInt(path + ".z")).getBlock();
        } else {
            throw new WorldNotExistException(worldName);
        }
    }

    public ItemStack getItemStack(String path) {
        this.load();
        return this.config.getItemStack(path);
    }

    public boolean remove(@Nonnull String path) {
        if (this.contains(path)) {
            this.config.set(path, null);
            this.save();
            return true;
        }
        return false;
    }

    public List<Integer> getIntegerList(@Nonnull String path) {
        return this.config.getIntegerList(path);
    }

    public List<String> getStringList(@Nonnull String path) {
        return this.config.getStringList(path);
    }

    public List<UUID> getUUIDList(@Nonnull String path) {
        ArrayList<UUID> uuids = new ArrayList<>();
        for (String s : this.getStringList(path)) {
            try {
                uuids.add(UUID.fromString(s));
            } catch (IllegalArgumentException ignored) {
            }
        }
        return uuids;
    }

    public UUID getUUID(@Nonnull String path) {
        try {
            return UUID.fromString(this.getString(path));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Color getColor(@Nonnull String path) {
        return this.config.getColor(path);
    }

    public Color getColorFromHex(@Nonnull String path) {
        String string = this.config.getString(path);
        if (string == null) {
            return null;
        }

        if (string.equals("0")) {
            return Color.BLACK;
        }

        if (string.length() < 6) {
            return null;
        }

        return Color.fromRGB(Integer.valueOf(string.substring(0, 2), 16), Integer.valueOf(string.substring(2, 4), 16), Integer.valueOf(string.substring(4, 6), 16));
    }

    public ConfigurationSection getConfigSection(@Nonnull String path) {
        return this.config.getConfigurationSection(path);
    }

    public Set<String> getPathStringList(@Nonnull String path) {
        this.load();
        if (this.config.contains(path)) {
            return this.getConfigSection(path).getKeys(false);
        }
        return Set.of();
    }

    public Set<Integer> getPathIntegerList(@Nonnull String path) {
        Set<Integer> ids = new HashSet<>();
        if (this.config.contains(path)) {
            Set<String> idStrings = this.getPathStringList(path);
            for (String idString : idStrings) {
                if (idString != null) {
                    try {
                        ids.add(Integer.valueOf(idString));
                    } catch (NumberFormatException e) {
                        Server.printError(Plugin.SYSTEM, "Can not read id " + idString);
                    }
                }
            }
        }
        return ids;
    }

    protected Triple<String, String, String> getStringTriple(String path, String a, String b, String c) {
        return new Triple<>(this.getString(path + "." + a), this.getString(path + "." + b), this.getString(path + "." + c));
    }

    protected Triple<Integer, Integer, Integer> getIntegerTriple(String path, String a, String b, String c) {
        return new Triple<>(this.getInt(path + "." + a), this.getInt(path + "." + b), this.getInt(path + "." + c));
    }

    public Triple<Double, Double, Double> getDoubleTriple(String path, String a, String b, String c) {
        return new Triple<>(this.getDouble(path + "." + a), this.getDouble(path + "." + b), this.getDouble(path + "." + c));
    }
}
