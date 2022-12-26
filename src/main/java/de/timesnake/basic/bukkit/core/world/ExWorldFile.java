/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.core.world;

import de.timesnake.basic.bukkit.util.file.ExFile;
import de.timesnake.basic.bukkit.util.world.ExWorldType;

import java.io.File;

public class ExWorldFile extends ExFile {

    public static final String NAME = "exworld.yml";
    private static final String TYPE = "type";
    private static final String SAFE = "safe";

    public ExWorldFile(File worldDirectory) {
        super(worldDirectory, NAME);
    }

    public ExWorldFile(File worldDirectory, ExWorldType type) {
        super(worldDirectory, NAME);

        super.set(TYPE, type.toString()).save();

        if (!super.contains(SAFE)) {
            super.set(SAFE, true).save();
        }
    }

    public ExWorldType getWorldType() {
        String type = super.getString(TYPE);
        if (type == null) {
            return null;
        }
        return ExWorldType.valueOf(type);
    }

    public boolean isSafe() {
        return super.getBoolean(SAFE);
    }

    public void setSafe(boolean safe) {
        super.set(SAFE, safe).save();
    }
}
