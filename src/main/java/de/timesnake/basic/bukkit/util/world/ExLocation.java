package de.timesnake.basic.bukkit.util.world;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.library.basic.util.Triple;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class ExLocation extends Location {

    public static ExLocation fromLocation(Location location) {
        if (location == null) return null;
        return new ExLocation(Server.getWorld(location.getWorld()), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    private ExWorld world;

    public ExLocation(ExWorld world, double x, double y, double z) {
        super(world != null ? world.getBukkitWorld() : Server.getWorldManager().getBasicWorld().getBukkitWorld(), x, y, z);
        this.world = world;
    }

    public ExLocation(ExWorld world, Triple<Double, Double, Double> xyz) {
        this(world, xyz.getA(), xyz.getB(), xyz.getC());
    }

    public ExLocation(ExWorld world, double x, double y, double z, float yaw, float pitch) {
        super(world != null ? world.getBukkitWorld() : Server.getWorldManager().getBasicWorld().getBukkitWorld(), x, y, z, yaw, pitch);
        this.world = world;
    }

    public ExWorld getExWorld() {
        return this.world;
    }

    @Override
    public World getWorld() {
        return this.world.getBukkitWorld();
    }

    @Override
    public void setWorld(World world) {
        this.world = Server.getWorld(world);
        super.setWorld(world);
    }

    public ExLocation setExWorld(ExWorld world) {
        this.setWorld(world.getBukkitWorld());
        return this;
    }

    @Override
    public Block getBlock() {
        return this.world.getBlockAt(this);
    }

    public ExBlock getExBlock() {
        return new ExBlock(this.getBlock());
    }

    public ExLocation middleBlock() {
        this.zeroBlock();
        this.add(0.5, 0, 0.5);
        return this;
    }

    public ExLocation zeroBlock() {
        this.set(this.getBlockX(), this.getBlockY(), this.getBlockZ());
        return this;
    }

    public ExLocation zeroFacing() {
        this.setYaw(0);
        this.setPitch(0);
        return this;
    }

    public ExLocation roundFacing() {
        this.setYaw(this.roundAngle(this.getYaw()));
        this.setPitch(this.roundAngle(this.getPitch()));
        return this;
    }

    private float roundAngle(float angle) {
        angle %= 360;
        if (angle < 0) {
            angle += 360;
        }
        if (angle <= 22.5 && angle >= 337.5) {
            return 0;
        }
        if (angle > 22.5 && angle <= 67.5) {
            return 45;
        }
        if (angle > 67.5 && angle <= 112.5) {
            return 90;
        }
        if (angle > 112.5 && angle <= 157.5) {
            return 135;
        }
        if (angle > 157.5 && angle <= 202.5) {
            return 180;
        }
        if (angle > 202.5 && angle <= 247.5) {
            return 225;
        }
        if (angle > 247.5 && angle <= 292.5) {
            return 270;
        }
        if (angle > 292.5 && angle < 337.5) {
            return 315;
        }
        return 0;
    }

    @Override
    public ExLocation clone() {
        return new ExLocation(this.world, this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
    }

    @Override
    public ExLocation add(double x, double y, double z) {
        super.add(x, y, z);
        return this;
    }


}
