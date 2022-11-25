/*
 * workspace.basic-bukkit.main
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

package de.timesnake.basic.bukkit.util.file;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.exception.WorldNotExistException;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.world.ExLocation;
import de.timesnake.library.basic.util.Triple;
import de.timesnake.library.extension.util.chat.Plugin;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Use {@link ExToml} instead
 */
@Deprecated
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

    public static String toPath(String... sections) {
        return String.join(".", sections);
    }

    protected final File configFile;
    protected final YamlConfiguration config;

    public ExFile(String folder, String name) {
        this.configFile = new File("plugins/" + folder + "/" + name + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.configFile);

        this.create();
        this.config.options().copyDefaults(true);
    }

    public ExFile(File parentFile, String fullName) {
        this.configFile = new File(parentFile.getAbsolutePath() + File.separator + fullName);
        this.config = YamlConfiguration.loadConfiguration(this.configFile);

        this.create();
        this.config.options().copyDefaults(true);
    }

    public void create() {
        if (!this.configFile.exists()) {
            try {
                FileUtils.createParentDirectories(this.configFile);
                this.configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ExFile save() {
        try {
            this.config.save(this.configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    protected ExFile load() {
        try {
            this.config.load(this.configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        return this;
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

    public ExFile set(@Nonnull String path, Object value) {
        if (value != null) {
            if (value instanceof Location) {
                this.setLocation(path, (Location) value, true);
                return this;
            } else if (value instanceof Block) {
                this.setBlock(path, ((Block) value));
                return this;
            } else if (value instanceof UUID) {
                this.config.set(path, value.toString());
                return this;
            } else if (value instanceof Player) {
                this.setPlayer(path, ((Player) value));
                return this;
            } else if (value instanceof User) {
                this.setPlayer(path, ((User) value).getPlayer());
                return this;
            } else if (value instanceof Color) {
                this.setHexColor(path, ((Color) value));
                return this;
            }
        }

        this.config.set(path, value);

        return this;
    }

    public ExFile setLocation(@Nonnull String path, @Nonnull Location location, boolean saveYawPitch) {
        setLocBlock(path, location.getWorld(), location.getX(), location.getY(), location.getZ());
        if (saveYawPitch) {
            this.config.set(path + ".yaw", location.getYaw());
            this.config.set(path + ".pitch", location.getPitch());
        }

        return this;
    }

    private ExFile setLocBlock(@Nonnull String path, World world, double x, double y, double z) {
        this.config.set(path + ".world", world.getName());
        this.config.set(path + ".x", x);
        this.config.set(path + ".y", y);
        this.config.set(path + ".z", z);

        return this;
    }

    public ExFile setBlock(@Nonnull String path, @Nonnull Block block) {
        setLocBlock(path, block.getWorld(), block.getX(), block.getY(), block.getZ());
        return this;
    }

    public ExFile setPlayer(String path, Player player) {
        this.config.set(path + ".name", player.getName());
        this.config.set(path + ".uuid", player.getUniqueId().toString());
        return this;
    }

    public ExFile setUuidList(String path, List<UUID> uuids) {
        List<String> strings = new ArrayList<>();
        for (UUID uuid : uuids) {
            strings.add(uuid.toString());
        }
        this.config.set(path, strings);
        return this;
    }

    public ExFile setHexColor(String path, Color color) {
        this.config.set(path, String.format("%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue()));
        return this;
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

    public Double getDouble(@Nonnull String path) {
        this.load();
        if (this.config.isDouble(path)) {
            return this.config.getDouble(path);
        }

        if (this.config.isInt(path)) {
            return ((double) this.config.getInt(path));
        }

        return null;
    }

    public Location getLocation(@Nonnull String path) throws WorldNotExistException {
        this.load();
        if (this.config.contains(path + ".world")) {
            String worldName = this.config.getString(path + ".world");
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                return new Location(world, this.config.getDouble(path + ".x"), this.config.getDouble(path + ".y"),
                        this.config.getDouble(path + ".z"), (float) this.config.getDouble(path + ".yaw", 0),
                        (float) this.config.getDouble(path + ".pitch", 0));
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
                return new ExLocation(Server.getWorld(world), this.config.getDouble(path + ".x"),
                        this.config.getDouble(path + ".y"), this.config.getDouble(path + ".z"),
                        (float) this.config.getDouble(path + ".yaw", 0), (float) this.config.getDouble(path + ".pitch"
                        , 0));
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
            return new Location(world, this.config.getInt(path + ".x"), this.config.getInt(path + ".y"),
                    this.config.getInt(path + ".z")).getBlock();
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

        return Color.fromRGB(Integer.valueOf(string.substring(0, 2), 16), Integer.valueOf(string.substring(2, 4), 16)
                , Integer.valueOf(string.substring(4, 6), 16));
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
                        Server.printWarning(Plugin.SYSTEM, "Can not read id " + idString);
                    }
                }
            }
        }
        return ids;
    }

    protected Triple<String, String, String> getStringTriple(String path, String a, String b, String c) {
        return new Triple<>(this.getString(path + "." + a), this.getString(path + "." + b),
                this.getString(path + "." + c));
    }

    protected Triple<Integer, Integer, Integer> getIntegerTriple(String path, String a, String b, String c) {
        return new Triple<>(this.getInt(path + "." + a), this.getInt(path + "." + b), this.getInt(path + "." + c));
    }

    public Triple<Double, Double, Double> getDoubleTriple(String path, String a, String b, String c) {
        return new Triple<>(this.getDouble(path + "." + a), this.getDouble(path + "." + b),
                this.getDouble(path + "." + c));
    }


    @NotNull
    public String saveToString() {
        return config.saveToString();
    }

    public void loadFromString(@NotNull String contents) throws InvalidConfigurationException {
        config.loadFromString(contents);
    }

    public @NotNull YamlConfigurationOptions options() {
        return config.options();
    }

    public void save(@NotNull File file) throws IOException {
        config.save(file);
    }

    public void save(@NotNull String file) throws IOException {
        config.save(file);
    }

    public void load(@NotNull File file) throws IOException, InvalidConfigurationException {
        config.load(file);
    }

    public void load(@NotNull Reader reader) throws IOException, InvalidConfigurationException {
        config.load(reader);
    }

    public void load(@NotNull String file) throws IOException, InvalidConfigurationException {
        config.load(file);
    }

    public void addDefaults(@NotNull Map<String, Object> defaults) {
        config.addDefaults(defaults);
    }

    public void addDefaults(@NotNull Configuration defaults) {
        config.addDefaults(defaults);
    }

    public @Nullable Configuration getDefaults() {
        return config.getDefaults();
    }

    public void setDefaults(@NotNull Configuration defaults) {
        config.setDefaults(defaults);
    }

    @Nullable
    public ConfigurationSection getParent() {
        return config.getParent();
    }

    @NotNull
    public Set<String> getKeys(boolean deep) {
        return config.getKeys(deep);
    }

    @NotNull
    public Map<String, Object> getValues(boolean deep) {
        return config.getValues(deep);
    }

    public boolean contains(@NotNull String path, boolean ignoreDefault) {
        return config.contains(path, ignoreDefault);
    }

    public boolean isSet(@NotNull String path) {
        return config.isSet(path);
    }

    @NotNull
    public String getCurrentPath() {
        return config.getCurrentPath();
    }

    @NotNull
    public String getName() {
        return config.getName();
    }

    public @Nullable Configuration getRoot() {
        return config.getRoot();
    }

    @Nullable
    public ConfigurationSection getDefaultSection() {
        return config.getDefaultSection();
    }

    @Nullable
    public Object get(@NotNull String path) {
        return config.get(path);
    }

    @Contract("_, !null -> !null")
    @Nullable
    public Object get(@NotNull String path, @Nullable Object def) {
        return config.get(path, def);
    }

    @NotNull
    public ConfigurationSection createSection(@NotNull String path) {
        return config.createSection(path);
    }

    @NotNull
    public ConfigurationSection createSection(@NotNull String path, @NotNull Map<?, ?> map) {
        return config.createSection(path, map);
    }

    @Contract("_, !null -> !null")
    @Nullable
    public String getString(@NotNull String path, @Nullable String def) {
        return config.getString(path, def);
    }

    public boolean isString(@NotNull String path) {
        return config.isString(path);
    }

    public int getInt(@NotNull String path, int def) {
        return config.getInt(path, def);
    }

    public boolean isInt(@NotNull String path) {
        return config.isInt(path);
    }

    public boolean getBoolean(@NotNull String path, boolean def) {
        return config.getBoolean(path, def);
    }

    public boolean isBoolean(@NotNull String path) {
        return config.isBoolean(path);
    }

    public double getDouble(@NotNull String path, double def) {
        return config.getDouble(path, def);
    }

    public boolean isDouble(@NotNull String path) {
        return config.isDouble(path);
    }

    public long getLong(@NotNull String path) {
        return config.getLong(path);
    }

    public long getLong(@NotNull String path, long def) {
        return config.getLong(path, def);
    }

    public boolean isLong(@NotNull String path) {
        return config.isLong(path);
    }

    @Nullable
    public List<?> getList(@NotNull String path) {
        return config.getList(path);
    }

    @Contract("_, !null -> !null")
    @Nullable
    public List<?> getList(@NotNull String path, @Nullable List<?> def) {
        return config.getList(path, def);
    }

    public boolean isList(@NotNull String path) {
        return config.isList(path);
    }

    @NotNull
    public List<String> getStringList(@NotNull String path) {
        return config.getStringList(path);
    }

    @NotNull
    public List<Integer> getIntegerList(@NotNull String path) {
        return config.getIntegerList(path);
    }

    @NotNull
    public List<Boolean> getBooleanList(@NotNull String path) {
        return config.getBooleanList(path);
    }

    @NotNull
    public List<Double> getDoubleList(@NotNull String path) {
        return config.getDoubleList(path);
    }

    @NotNull
    public List<Float> getFloatList(@NotNull String path) {
        return config.getFloatList(path);
    }

    @NotNull
    public List<Long> getLongList(@NotNull String path) {
        return config.getLongList(path);
    }

    @NotNull
    public List<Byte> getByteList(@NotNull String path) {
        return config.getByteList(path);
    }

    @NotNull
    public List<Character> getCharacterList(@NotNull String path) {
        return config.getCharacterList(path);
    }

    @NotNull
    public List<Short> getShortList(@NotNull String path) {
        return config.getShortList(path);
    }

    @NotNull
    public List<Map<?, ?>> getMapList(@NotNull String path) {
        return config.getMapList(path);
    }

    public <T> @Nullable T getObject(@NotNull String path, @NotNull Class<T> clazz) {
        return config.getObject(path, clazz);
    }

    @Contract("_, _, !null -> !null")
    public <T> @Nullable T getObject(@NotNull String path, @NotNull Class<T> clazz, @Nullable T def) {
        return config.getObject(path, clazz, def);
    }

    public <T extends ConfigurationSerializable> @Nullable T getSerializable(@NotNull String path,
                                                                             @NotNull Class<T> clazz) {
        return config.getSerializable(path, clazz);
    }

    @Contract("_, _, !null -> !null")
    public <T extends ConfigurationSerializable> @Nullable T getSerializable(@NotNull String path,
                                                                             @NotNull Class<T> clazz, @Nullable T def) {
        return config.getSerializable(path, clazz, def);
    }

    public @Nullable Vector getVector(@NotNull String path) {
        return config.getVector(path);
    }

    @Contract("_, !null -> !null")
    public @Nullable Vector getVector(@NotNull String path, @Nullable Vector def) {
        return config.getVector(path, def);
    }

    public boolean isVector(@NotNull String path) {
        return config.isVector(path);
    }

    public @Nullable OfflinePlayer getOfflinePlayer(@NotNull String path) {
        return config.getOfflinePlayer(path);
    }

    @Contract("_, !null -> !null")
    public @Nullable OfflinePlayer getOfflinePlayer(@NotNull String path, @Nullable OfflinePlayer def) {
        return config.getOfflinePlayer(path, def);
    }

    public boolean isOfflinePlayer(@NotNull String path) {
        return config.isOfflinePlayer(path);
    }

    @Contract("_, !null -> !null")
    @Nullable
    public ItemStack getItemStack(@NotNull String path, @Nullable ItemStack def) {
        return config.getItemStack(path, def);
    }

    public boolean isItemStack(@NotNull String path) {
        return config.isItemStack(path);
    }

    @Contract("_, !null -> !null")
    @Nullable
    public Color getColor(@NotNull String path, @Nullable Color def) {
        return config.getColor(path, def);
    }

    public boolean isColor(@NotNull String path) {
        return config.isColor(path);
    }

    @Contract("_, !null -> !null")
    @Nullable
    public Location getLocation(@NotNull String path, @Nullable Location def) {
        return config.getLocation(path, def);
    }

    public boolean isLocation(@NotNull String path) {
        return config.isLocation(path);
    }

    @Nullable
    public ConfigurationSection getConfigurationSection(@NotNull String path) {
        return config.getConfigurationSection(path);
    }

    public boolean isConfigurationSection(@NotNull String path) {
        return config.isConfigurationSection(path);
    }

    @NotNull
    public List<String> getComments(@NotNull String path) {
        return config.getComments(path);
    }

    @NotNull
    public List<String> getInlineComments(@NotNull String path) {
        return config.getInlineComments(path);
    }

    public void setComments(@NotNull String path, @Nullable List<String> comments) {
        config.setComments(path, comments);
    }

    public void setInlineComments(@NotNull String path, @Nullable List<String> comments) {
        config.setInlineComments(path, comments);
    }
}
