package de.timesnake.basic.bukkit.core.world;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.UserQuitEvent;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.io.*;
import java.util.*;

public class WorldManager implements Listener, de.timesnake.basic.bukkit.util.world.WorldManager {

    private final File backupDir;

    private final LocationsFile locationsFile;

    private final Map<String, ExWorld> worldsByName = new HashMap<>();

    private final boolean isLastWorldSpawning;

    public final WorldBorderManager worldBorderManager;

    public WorldManager() {
        Server.registerListener(this, BasicBukkit.getPlugin());

        this.worldBorderManager = new WorldBorderManager();

        this.locationsFile = new LocationsFile("plugins/basic-bukkit");

        this.backupDir = new File("plugins/basic-bukkit/world_backups");

        this.isLastWorldSpawning = locationsFile.isLastWorldSpawn();

        //add worlds to file
        for (World world : Bukkit.getWorlds()) {
            ExWorldFile file = new ExWorldFile(world.getWorldFolder());
            this.worldsByName.put(world.getName(), new ExWorld(world, WorldManager.Type.fromWorld(world), file));
        }


        for (File file : Bukkit.getWorldContainer().listFiles()) {
            if (!file.isDirectory()) {
                continue;
            }

            if (file.list((f, n) -> n.equals(ExWorldFile.NAME)).length == 0) {
                continue;
            }

            ExWorldFile exWorldFile = new ExWorldFile(file);

            ExWorld world = this.createWorld(file.getName(), exWorldFile.getWorldType());
            if (world != null) {
                Server.printText(Plugin.WORLDS, "Loaded world " + world.getName());
            }
        }
    }

    public void onDisable() {
        for (ExWorld exWorld : this.getWorlds()) {
            if (!exWorld.isSafe()) {
                this.reloadWorld(exWorld);
            }
        }
    }

    @Override
    public boolean isLastWorldSpawning() {
        return isLastWorldSpawning;
    }

    public LocationsFile getLocationsFile() {
        return locationsFile;
    }

    @Override
    public void resetUserLocations() {
        for (de.timesnake.basic.bukkit.util.world.ExWorld world : this.getWorlds()) {
            this.locationsFile.removeWorld(world);
        }
    }

    @Override
    public WorldBorderManager getWorldBorderManager() {
        return worldBorderManager;
    }

    @Override
    public ExWorld getBasicWorld() {
        return this.getWorld("world");
    }

    @Override
    public ExWorld getWorld(String name) {
        return this.worldsByName.get(name);
    }

    @Override
    public ExWorld getWorld(World world) {
        ExWorld exWorld = this.worldsByName.get(world.getName());
        if (exWorld == null) {
            return this.createWorld(world);
        }
        return exWorld;
    }

    @Override
    public Collection<ExWorld> getWorlds() {
        return this.worldsByName.values();
    }

    @Override
    public ExWorld createWorld(String name) {
        return this.createWorld(name, Type.NORMAL);
    }

    @Override
    public ExWorld createWorld(String name, Type type) {

        if (type == null) {
            Server.printWarning(Plugin.WORLDS, "Can not load type of world " + name);
            return null;
        }

        for (String s : UNSUPPORTED_SYMBOLS) {
            name = name.replace(s, "");
        }

        // world exits already
        if (this.worldsByName.get(name) != null) {
            return this.worldsByName.get(name);
        }

        // world exists in Bukkit, create exworld
        if (Bukkit.getWorld(name) != null) {
            return this.getWorld(Bukkit.getWorld(name));
        }

        WorldCreator worldCreator = new WorldCreator(name);
        if (type.getChunkGenerator() != null) {
            worldCreator.generator(type.getChunkGenerator());
        }
        if (type.getEnvironment() != null) {
            worldCreator.environment(type.getEnvironment());
        }
        if (type.getWorldType() != null) {
            worldCreator.type(type.getWorldType());
        }

        World world = Bukkit.createWorld(worldCreator);

        if (world == null) {
            return null;
        }

        ExWorldFile file = new ExWorldFile(world.getWorldFolder(), type);
        ExWorld exWorld = new ExWorld(world, type, file);
        this.registerExWorld(exWorld);
        return exWorld;
    }

    @Override
    public ExWorld cloneWorld(String name, ExWorld exWorld) {
        for (String s : UNSUPPORTED_SYMBOLS) {
            name = name.replace(s, "");
        }

        // world exits already
        if (this.worldsByName.containsKey(name)) {
            return null;
        }

        // world exists in Bukkit
        if (Bukkit.getWorld(name) != null) {
            return null;
        }

        exWorld.save();

        this.copyWorldFolderFiles(exWorld.getWorldFolder(), new File(Bukkit.getWorldContainer() + File.separator + name));

        World world = Bukkit.createWorld(new WorldCreator(name).copy(exWorld.getBukkitWorld()));

        if (world == null) {
            return null;
        }

        ExWorldFile file = new ExWorldFile(world.getWorldFolder(), exWorld.getType());
        ExWorld clonedExWorld = new ExWorld(world, exWorld.getType(), file);
        this.registerExWorld(clonedExWorld);
        return clonedExWorld;
    }

    @Override
    public boolean backupWorld(ExWorld world) {
        File backup = new File(this.backupDir + File.separator + world.getName());
        backup.delete();
        backup.mkdir();
        world.save();
        this.copyWorldFolderFiles(world.getWorldFolder(), backup);
        return true;
    }

    @Override
    public boolean loadWorldBackup(ExWorld world) {
        File backup = new File(this.backupDir + File.separator + world.getName());

        if (!backup.exists()) {
            return false;
        }

        this.moveUsersFromWorld(world);

        Bukkit.unloadWorld(world.getName(), false);

        File worldFile = world.getWorldFolder();
        worldFile.delete();

        this.copyWorldFolderFiles(backup, worldFile);

        World worldBackup = Bukkit.createWorld(new WorldCreator(world.getName()));
        world.setBukkitWorld(worldBackup);
        return true;
    }

    @Override
    public boolean reloadWorld(ExWorld exWorld) {
        this.moveUsersFromWorld(exWorld);
        World world = exWorld.getBukkitWorld();

        boolean successfully = Bukkit.unloadWorld(world, false);

        List<String> deleteFiles = List.of("session.lock", "uid.dat");

        for (String fileName : deleteFiles) {
            File file = new File(exWorld.getWorldFolder().getAbsolutePath() + File.separator + fileName);
            if (file.exists()) {
                file.delete();
            } else {
                return false;
            }
        }

        World newWorld = Bukkit.createWorld(new WorldCreator(exWorld.getName()));
        exWorld.setBukkitWorld(newWorld);

        return successfully;
    }

    @Override
    public boolean unloadWorld(ExWorld world) {
        return Bukkit.unloadWorld(world.getName(), false);
    }

    @Override
    public boolean deleteWorld(ExWorld exWorld, boolean deleteFiles) {
        this.moveUsersFromWorld(exWorld);
        boolean unloaded = Bukkit.unloadWorld(exWorld.getBukkitWorld(), false);
        this.unregisterExWorld(exWorld);

        if (deleteFiles) {
            try {
                FileUtils.deleteDirectory(exWorld.getWorldFolder());
            } catch (IOException e) {
                return false;
            }
        }

        return unloaded;
    }


    private void moveUsersFromWorld(ExWorld world) {
        for (User user : Server.getUsers()) {
            if (user.getExWorld().equals(world)) {
                user.teleport(this.getBasicWorld());
            }
        }
    }

    private ExWorld createWorld(World world) {
        if (world == null) {
            return null;
        }

        if (Bukkit.getWorld(world.getName()) == null) {
            world = Bukkit.createWorld(new WorldCreator(world.getName()).copy(world));
        }

        ExWorldFile file = new ExWorldFile(world.getWorldFolder(), WorldManager.Type.fromWorld(world));
        ExWorld exWorld = new ExWorld(world, Type.fromWorld(world), file);
        this.registerExWorld(exWorld);
        return exWorld;
    }

    private void registerExWorld(ExWorld world) {
        this.worldsByName.put(world.getName(), world);
    }

    private void unregisterExWorld(ExWorld world) {
        this.worldsByName.remove(world.getName());
        ExWorldFile exWorldFile = world.getExFile();
        if (exWorldFile.exists()) {
            exWorldFile.delete();
        }
    }

    private void copyWorldFolderFiles(File source, File target) {
        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists())
                        if (!target.mkdirs()) throw new IOException("Couldn't create world directory!");
                    String[] files = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyWorldFolderFiles(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        User user = Server.getUser(e.getPlayer());
        if (user != null) {
            if (e.getFrom() != null) {
                user.setWorldLocation();
                Server.printText(Plugin.BUKKIT, "Saved world " + e.getFrom().getName() + " location of user " + user.getName(), "World");
            }
        }
    }

    @EventHandler
    public void onUserQuit(UserQuitEvent e) {
        User user = e.getUser();
        user.setLastLocation(user.getLocation());
        this.locationsFile.setUserLocation(user);
        this.locationsFile.setUserWorld(user);
        Server.printText(Plugin.BUKKIT, "Saved location " + user.getExWorld().getName() + " of user " + user.getName(), "World");
    }


    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        User user = Server.getUser(e.getPlayer());
        if (e.getFrom() != null && user != null) {
            Location location = e.getFrom();
            user.setLastLocation(location);
        }
    }
}
