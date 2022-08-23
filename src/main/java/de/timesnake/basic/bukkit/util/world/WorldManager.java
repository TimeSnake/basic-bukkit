package de.timesnake.basic.bukkit.util.world;

import de.timesnake.basic.bukkit.core.world.VoidGenerator;
import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface WorldManager {

    List<String> UNSUPPORTED_SYMBOLS = List.of("_backup", ";", ",", ":", "*", "+", "#", "@", "$", "&", "/", "\\");

    void copyWorldFolderFiles(File source, File target);

    Collection<ExWorld> getWorlds();

    ExWorld getWorld(String name);

    ExWorld getWorld(World world);

    ExLocation getUserLocation(User user, ExWorld world);

    WorldBorderManager getWorldBorderManager();

    ExWorld getBasicWorld();

    /**
     * Deletes a world
     * <p>
     * This method deletes the {@link World} and {@link ExWorld}
     * Beware of saved locations, they are invalid after the deletion.
     *
     * @param world       The {@link ExWorld} to delete
     * @param deleteFiles If set, all world files will be deleted
     * @return true if the deletion was successfully, else false
     */
    boolean deleteWorld(ExWorld world, boolean deleteFiles);

    ExWorld cloneWorld(String name, ExWorld world);

    ExWorld createWorldFromFile(String name);

    ExWorld createWorld(String name);

    ExWorld createWorld(String name, Type type);

    boolean backupWorld(ExWorld world);

    /**
     * Loads a world backup
     * <p>
     * This method creates a new {@link World} and inject it into the {@link ExWorld}.
     * Beware of saved locations, they are invalid after the load. Use {@link ExLocation}s to prevent errors.
     *
     * @param world The {@link ExWorld} to load the backup
     * @return true if the load was successfully, else false
     */
    boolean loadWorldBackup(ExWorld world);

    /**
     * Reloads a world from disk
     * <p>
     * This method creates a new {@link World} from the disk and inject it into the {@link ExWorld}.
     * Changes in the world will not be saved (except auto-save). It is recommended to use this with auto-save off.
     * Beware of saved locations, they are invalid after the load. Use {@link ExLocation}s to prevent errors.
     *
     * @param world The {@link ExWorld} to load the reload
     * @return true if the reload was successfully, else false
     */
    boolean reloadWorld(ExWorld world);

    boolean unloadWorld(ExWorld world, boolean save);

    enum Type {
        NORMAL(World.Environment.NORMAL, WorldType.NORMAL, null),
        NETHER(World.Environment.NETHER, WorldType.NORMAL, null),
        END(World.Environment.THE_END, WorldType.NORMAL, null),
        VOID(null, null, new VoidGenerator()),
        FLAT(World.Environment.NORMAL, WorldType.FLAT, null),
        AMPLIFIED(World.Environment.NORMAL, WorldType.AMPLIFIED, null),
        LARGE_BIOMES(World.Environment.NORMAL, WorldType.LARGE_BIOMES, null);

        public static List<String> getNames() {
            List<String> names = new ArrayList<>();
            for (Type worldType : Type.values()) {
                names.add(worldType.name());
            }
            return names;
        }

        public static Type fromWorld(World world) {
            for (Type type : Type.values()) {
                if (type.getWorldType() != null && type.getWorldType().equals(world.getWorldType()) && type.getEnvironment() != null && type.getEnvironment().equals(world.getEnvironment())) {
                    return type;
                }
            }
            return NORMAL;
        }

        private final World.Environment environment;
        private final WorldType worldType;
        private final ChunkGenerator chunkGenerator;

        Type(World.Environment environment, WorldType worldType, ChunkGenerator chunkGenerator) {
            this.environment = environment;
            this.worldType = worldType;
            this.chunkGenerator = chunkGenerator;
        }

        public WorldType getWorldType() {
            return worldType;
        }

        public World.Environment getEnvironment() {
            return environment;
        }

        public ChunkGenerator getChunkGenerator() {
            return chunkGenerator;
        }


    }

}
