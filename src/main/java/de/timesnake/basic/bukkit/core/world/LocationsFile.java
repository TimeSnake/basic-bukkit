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
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.UUID;

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
                super.getDouble(ExFile.toPath(worldName, YAW)).floatValue(), super.getDouble(ExFile.toPath(worldName,
                PITCH)).floatValue());
    }

    public void removeWorld(World world) {
        this.remove(world.getName());
    }

}
