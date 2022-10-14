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

import de.timesnake.basic.bukkit.core.world.CustomFlatGenerator;
import de.timesnake.basic.bukkit.core.world.CustomHeightGenerator;
import de.timesnake.basic.bukkit.core.world.FlatStoneGenerator;
import de.timesnake.basic.bukkit.core.world.VoidGenerator;
import de.timesnake.library.basic.util.Tuple;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        if (name == null) {
            return null;
        }

        if (name.startsWith("custom_flat")) {
            return CustomFlat.fromString(name);
        } else if (name.startsWith("custom_height")) {
            return CustomHeight.fromString(name);
        }

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

    @Deprecated
    public static ExWorldType fromWorld(World world) {
        for (ExWorldType type : TYPES) {
            if (type.getWorldType() != null && type.getWorldType().equals(world.getWorldType())
                    && type.getEnvironment() != null && type.getEnvironment().equals(world.getEnvironment())) {
                return type;
            }
        }
        return NORMAL;
    }

    protected final String name;
    protected final World.Environment environment;
    protected final org.bukkit.WorldType worldType;
    protected final ChunkGenerator chunkGenerator;

    public ExWorldType(String name, World.Environment environment, org.bukkit.WorldType worldType,
                       ChunkGenerator chunkGenerator) {
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

    @Override
    public String toString() {
        return this.name;
    }

    public static class CustomFlat extends ExWorldType {

        public static ExWorldType.CustomFlat fromString(String string) {
            return new CustomFlat(Arrays.stream(string.replace("custom_flat;", "").split(";"))
                    .map(l -> {
                        String[] s = l.split("#");
                        if (s.length == 0) {
                            return new Tuple<>(0, Material.AIR);
                        } else if (s.length == 1) {
                            try {
                                return new Tuple<>(1, Material.valueOf(s[0].toUpperCase()));
                            } catch (IllegalArgumentException e) {
                                return new Tuple<>(1, Material.AIR);
                            }
                        } else {
                            try {
                                return new Tuple<>(Integer.parseInt(s[0]), Material.valueOf(s[1].toUpperCase()));
                            } catch (NumberFormatException e) {
                                try {
                                    return new Tuple<>(1, Material.valueOf(s[1].toUpperCase()));
                                } catch (IllegalArgumentException ex) {
                                    return new Tuple<>(1, Material.AIR);
                                }
                            } catch (IllegalArgumentException e) {
                                return new Tuple<>(Integer.parseInt(s[0]), Material.AIR);
                            }
                        }
                    }).collect(Collectors.toList()));
        }

        private final List<Tuple<Integer, Material>> materials;

        public CustomFlat(List<Tuple<Integer, Material>> materials) {
            super("custom_flat", World.Environment.NORMAL, null, new CustomFlatGenerator(materials));
            this.materials = materials;
        }

        @Override
        public String toString() {
            return "custom_flat;" + materials.stream().map(t -> t.getA() + "#" + t.getB()).collect(Collectors.joining(";"));
        }
    }

    public static class CustomHeight extends ExWorldType {

        public static final double SCALE = CustomHeightGenerator.SCALE;
        public static final double FREQUENCY = CustomHeightGenerator.FREQUENCY;
        public static final double AMPLITUDE = CustomHeightGenerator.AMPLITUDE;

        public static ExWorldType.CustomHeight fromString(String string) {
            double scale;

            String[] values = string.split(";", 4);

            try {
                scale = Double.parseDouble(values[1]);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                return null;
            }

            double frequency;

            try {
                frequency = Double.parseDouble(values[2]);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                return null;
            }

            double amplitude;

            try {
                amplitude = Double.parseDouble(values[3]);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                return null;
            }

            return new CustomHeight(scale, frequency, amplitude, Arrays.stream(values[4].split(";")).map(l -> {
                String[] s = l.split("#");
                if (s.length == 0) {
                    return new Tuple<>(0, Material.AIR);
                } else if (s.length == 1) {
                    try {
                        return new Tuple<>(1, Material.valueOf(s[0].toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        return new Tuple<>(1, Material.AIR);
                    }
                } else {
                    try {
                        return new Tuple<>(Integer.parseInt(s[0]), Material.valueOf(s[1].toUpperCase()));
                    } catch (NumberFormatException e) {
                        try {
                            return new Tuple<>(1, Material.valueOf(s[1].toUpperCase()));
                        } catch (IllegalArgumentException ex) {
                            return new Tuple<>(1, Material.AIR);
                        }
                    } catch (IllegalArgumentException e) {
                        return new Tuple<>(Integer.parseInt(s[0]), Material.AIR);
                    }
                }
            }).collect(Collectors.toList()));
        }

        private final double scale;
        private final double frequency;
        private final double amplitude;
        private final List<Tuple<Integer, Material>> materials;

        public CustomHeight(double scale, double frequency, double amplitude, List<Tuple<Integer, Material>> materials) {
            super("custom_height", World.Environment.NORMAL, null,
                    new CustomHeightGenerator(scale, frequency, amplitude, materials));
            this.scale = scale;
            this.frequency = frequency;
            this.amplitude = amplitude;
            this.materials = materials;
        }

        @Override
        public String toString() {
            return "custom_height;" + this.scale + ";" + this.frequency + ";" + this.amplitude + ";" +
                    this.materials.stream().map(t -> t.getA() + "#" + t.getB()).collect(Collectors.joining(";"));
        }
    }
}
