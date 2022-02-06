package de.timesnake.basic.bukkit.core.world;

import de.timesnake.basic.bukkit.util.file.ExFile;

import java.io.File;

public class ExWorldFile extends ExFile {

    public static final String NAME = "exworld.yml";
    private static final String TYPE = "type";
    private static final String SAFE = "safe";

    public ExWorldFile(File worldDirectory) {
        super(worldDirectory, NAME);
    }

    public ExWorldFile(File worldDirectory, WorldManager.Type type) {
        super(worldDirectory, NAME);

        super.set(TYPE, type.name());

        if (!super.contains(SAFE)) {
            super.set(SAFE, true);
        }
    }

    public WorldManager.Type getWorldType() {
        String type = super.getString(TYPE);
        if (type == null) {
            return null;
        }
        try {
            return WorldManager.Type.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public boolean isSafe() {
        return super.getBoolean(SAFE);
    }

    public void setSafe(boolean safe) {
        super.set(SAFE, safe);
    }
}
