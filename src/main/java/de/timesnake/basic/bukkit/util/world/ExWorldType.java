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

package de.timesnake.basic.bukkit.util.world;

import de.timesnake.basic.bukkit.core.world.FlatStoneGenerator;
import de.timesnake.basic.bukkit.core.world.VoidGenerator;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.ArrayList;
import java.util.List;

public class ExWorldType {

    public static final ExWorldType NORMAL = new ExWorldType("normal", World.Environment.NORMAL, org.bukkit.WorldType.NORMAL, null);
    public static final ExWorldType NETHER = new ExWorldType("nether", World.Environment.NETHER, org.bukkit.WorldType.NORMAL, null);
    public static final ExWorldType END = new ExWorldType("end", World.Environment.THE_END, org.bukkit.WorldType.NORMAL, null);
    public static final ExWorldType VOID = new ExWorldType("void", null, null, new VoidGenerator());
    public static final ExWorldType FLAT = new ExWorldType("flat", World.Environment.NORMAL, org.bukkit.WorldType.FLAT, null);
    public static final ExWorldType AMPLIFIED = new ExWorldType("amplified", World.Environment.NORMAL, org.bukkit.WorldType.AMPLIFIED, null);
    public static final ExWorldType LARGE_BIOMES = new ExWorldType("largeBiomes", World.Environment.NORMAL, org.bukkit.WorldType.LARGE_BIOMES, null);
    public static final ExWorldType FLAT_STONE = new ExWorldType("flatStone", World.Environment.NORMAL, null, new FlatStoneGenerator());

    public static final List<ExWorldType> TYPES = List.of(NORMAL, NETHER, END, VOID, FLAT, AMPLIFIED, LARGE_BIOMES,
            FLAT_STONE);

    public static ExWorldType valueOf(String name) {
        for (ExWorldType worldType : TYPES) {
            if (name.equalsIgnoreCase(worldType.getName())) {
                return worldType;
            }
        }
        return null;
    }

    public static List<String> getNames() {
        List<String> names = new ArrayList<>();
        for (ExWorldType worldType : TYPES) {
            names.add(worldType.getName());
        }
        return names;
    }

    public static ExWorldType fromWorld(World world) {
        for (ExWorldType type : TYPES) {
            if (type.getWorldType() != null && type.getWorldType().equals(world.getWorldType()) && type.getEnvironment() != null && type.getEnvironment().equals(world.getEnvironment())) {
                return type;
            }
        }
        return NORMAL;
    }

    protected final String name;
    protected final World.Environment environment;
    protected final org.bukkit.WorldType worldType;
    protected final ChunkGenerator chunkGenerator;

    public ExWorldType(String name, World.Environment environment, org.bukkit.WorldType worldType, ChunkGenerator chunkGenerator) {
        this.name = name;
        this.environment = environment;
        this.worldType = worldType;
        this.chunkGenerator = chunkGenerator;
    }

    public String getName() {
        return name;
    }

    public org.bukkit.WorldType getWorldType() {
        return worldType;
    }

    public World.Environment getEnvironment() {
        return environment;
    }

    public ChunkGenerator getChunkGenerator() {
        return chunkGenerator;
    }

}
