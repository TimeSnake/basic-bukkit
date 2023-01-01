/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world;

import de.timesnake.basic.bukkit.core.world.generator.CustomFlatGenerator;
import de.timesnake.basic.bukkit.core.world.generator.CustomHeightGenerator;
import de.timesnake.basic.bukkit.core.world.generator.CustomIslandGenerator;
import de.timesnake.basic.bukkit.core.world.generator.FlatStoneGenerator;
import de.timesnake.basic.bukkit.core.world.generator.VoidGenerator;
import de.timesnake.basic.bukkit.util.exception.WorldTypeParseException;
import de.timesnake.library.basic.util.Tuple;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

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
        } else if (name.startsWith("custom_island")) {
            return CustomIsland.fromString(name);
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

    private static List<Tuple<Integer, Material>> parseMaterialsFromString(String string) {
        return Arrays.stream(string.split(",")).map(l -> {
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
        }).collect(Collectors.toList());
    }

    private static Double parseDoubleFromType(String type, int index) {
        try {
            return Double.parseDouble(type.split(";")[index]);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            return null;
        }
    }

    private static Integer parseIntegerFromType(String type, int index) {
        try {
            return Integer.parseInt(type.split(";")[index]);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            return null;
        }
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
            return new CustomFlat(ExWorldType.parseMaterialsFromString(string.split(",", 2)[1]));
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

        public static final double SCALE = 0.005;
        public static final double FREQUENCY = 0.5;
        public static final double AMPLITUDE = 0.5;
        public static final int BASE_HEIGHT = 50;

        public static ExWorldType.CustomHeight fromString(String string) {
            String[] values = string.split(";");

            int index = 1;

            boolean simplexGenerator = false;
            if (values[index++].equalsIgnoreCase("true")) {
                simplexGenerator = true;
            } else if (!values[1].equalsIgnoreCase("false")) {
                throw new WorldTypeParseException("Could not parse generator type");
            }

            Double xScale = ExWorldType.parseDoubleFromType(string, index++);
            if (xScale == null) {
                throw new WorldTypeParseException("Could not parse xScale");
            }

            Double yScale = ExWorldType.parseDoubleFromType(string, index++);
            if (yScale == null) {
                throw new WorldTypeParseException("Could not parse yScale");
            }

            Double zScale = ExWorldType.parseDoubleFromType(string, index++);
            if (zScale == null) {
                throw new WorldTypeParseException("Could not parse zScale");
            }

            Double frequency = ExWorldType.parseDoubleFromType(string, index++);
            if (frequency == null) {
                throw new WorldTypeParseException("Could not parse frequency");
            }

            Double amplitude = ExWorldType.parseDoubleFromType(string, index++);
            if (amplitude == null) {
                throw new WorldTypeParseException("Could not parse amplitude");
            }

            Integer baseHeight = ExWorldType.parseIntegerFromType(string, index++);
            if (baseHeight == null) {
                throw new WorldTypeParseException("Could not parse base-height");
            }

            return new CustomHeight(simplexGenerator, xScale, yScale, zScale, frequency, amplitude, baseHeight,
                    ExWorldType.parseMaterialsFromString(values[index]));
        }

        private final boolean simplexGenerator;
        private final double xScale;
        private final double yScale;
        private final double zScale;
        private final double frequency;
        private final double amplitude;
        private final int baseHeight;
        private final List<Tuple<Integer, Material>> materials;

        public CustomHeight(boolean simplexGenerator, double xScale, double yScale, double zScale, double frequency,
                            double amplitude, int baseHeight, List<Tuple<Integer, Material>> materials) {
            super("custom_height", World.Environment.NORMAL, null,
                    new CustomHeightGenerator(simplexGenerator, xScale, yScale, zScale, frequency, amplitude,
                            baseHeight, materials));
            this.simplexGenerator = simplexGenerator;
            this.xScale = xScale;
            this.yScale = yScale;
            this.zScale = zScale;
            this.frequency = frequency;
            this.amplitude = amplitude;
            this.baseHeight = baseHeight;
            this.materials = materials;
        }

        @Override
        public String toString() {
            return String.join(";", List.of("custom_height", "" + this.simplexGenerator, "" + this.xScale,
                    "" + this.yScale, "" + this.zScale, "" + this.frequency, "" + this.amplitude, "" + this.baseHeight,
                    this.materials.stream().map(t -> t.getA() + "#" + t.getB()).collect(Collectors.joining(","))));
        }
    }

    public static class CustomIsland extends ExWorldType {

        public static final double DENSITY = 0.2;
        public static final double SCALE = 0.05;
        public static final double FREQUENCY = 0.5;
        public static final double AMPLITUDE = 0.5;

        public static ExWorldType.CustomIsland fromString(String string) {
            String[] values = string.split(";");

            int index = 1;

            Double density = ExWorldType.parseDoubleFromType(string, index++);
            if (density == null) {
                throw new WorldTypeParseException("Could not parse density");
            }

            Double xScale = ExWorldType.parseDoubleFromType(string, index++);
            if (xScale == null) {
                throw new WorldTypeParseException("Could not parse xScale");
            }

            Double yScale = ExWorldType.parseDoubleFromType(string, index++);
            if (yScale == null) {
                throw new WorldTypeParseException("Could not parse yScale");
            }

            Double zScale = ExWorldType.parseDoubleFromType(string, index++);
            if (zScale == null) {
                throw new WorldTypeParseException("Could not parse zScale");
            }

            Double frequency = ExWorldType.parseDoubleFromType(string, index++);
            if (frequency == null) {
                throw new WorldTypeParseException("Could not parse frequency");
            }

            Double amplitude = ExWorldType.parseDoubleFromType(string, index++);
            if (amplitude == null) {
                throw new WorldTypeParseException("Could not parse amplitude");
            }

            return new CustomIsland(density.floatValue(), xScale, yScale, zScale, frequency, amplitude,
                    ExWorldType.parseMaterialsFromString(values[index]));
        }

        private final float density;
        private final double xScale;
        private final double yScale;
        private final double zScale;
        private final double frequency;
        private final double amplitude;
        private final List<Tuple<Integer, Material>> materials;

        public CustomIsland(float density, double xScale, double yScale, double zScale, double frequency, double amplitude,
                            List<Tuple<Integer, Material>> materials) {
            super("custom_island", World.Environment.NORMAL, null, new CustomIslandGenerator(density,
                    xScale, yScale, zScale, frequency, amplitude, materials));
            this.density = density;
            this.xScale = xScale;
            this.yScale = yScale;
            this.zScale = zScale;
            this.frequency = frequency;
            this.amplitude = amplitude;
            this.materials = materials;
        }

        @Override
        public String toString() {
            return String.join(";", List.of("custom_island", "" + this.density, "" + this.xScale, "" + this.yScale,
                    "" + this.zScale, "" + this.frequency, "" + this.amplitude,
                    this.materials.stream().map(t -> t.getA() + "#" + t.getB()).collect(Collectors.joining(","))));
        }
    }
}
