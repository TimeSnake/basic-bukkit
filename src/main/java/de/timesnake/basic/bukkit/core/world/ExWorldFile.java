/*
 * basic-bukkit.main
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
