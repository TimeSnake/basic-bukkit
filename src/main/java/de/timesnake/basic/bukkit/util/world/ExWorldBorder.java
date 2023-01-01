/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.core.world.WorldManager;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.UserQuitEvent;
import de.timesnake.library.packets.core.packet.out.border.ExPacketPlayOutWorldBorder;
import de.timesnake.library.packets.util.ExClientboundInitializeBorderPacket;
import de.timesnake.library.packets.util.ExClientboundSetBorderLerpSizePacket;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Note;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ExWorldBorder implements Listener {

    private final ExWorld world;

    private final Set<User> users;
    private final Set<User> spectators = new HashSet<>();

    private double centerX;
    private double centerZ;
    private double size;
    private int warningDistance;
    private int warningTime;

    private double damage;

    private boolean shrinking = false;

    private BukkitTask damageTask;

    private BukkitTask shrinkingTask;
    private double shrinkSize;
    private int shrinkTime;
    private double shrinkPerTick;

    private boolean sound;

    public ExWorldBorder(ExWorld world, Set<User> users, double centerX, double centerZ, double size,
                         int warningDistance, int warningTime, double damage, boolean sound) {
        this.world = world;
        this.users = users;
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.size = size;
        this.warningDistance = warningDistance;
        this.warningTime = warningTime;
        this.damage = damage;
        this.sound = sound;

        Server.registerListener(this, BasicBukkit.getPlugin());
        this.startDamageTask();

    }

    public ExWorldBorder(ExWorld world, double centerX, double centerZ, double size, int warningDistance,
                         int warningTime, double damage, boolean sound) {
        this(world, new HashSet<>(), centerX, centerZ, size, warningDistance, warningTime, damage, sound);
    }

    public void destroy() {

        if (this.damageTask != null) {
            this.damageTask.cancel();
        }

        if (this.shrinkingTask != null) {
            this.shrinkingTask.cancel();
        }

        for (User user : this.users) {
            this.removeUser(user);
        }
    }

    public void addUser(User user) {

        if (user == null) {
            return;
        }

        this.spectators.remove(user);

        boolean added = this.users.add(user);

        if (!added) {
            return;
        }

        ((WorldManager) Server.getWorldManager()).getWorldBorderManager().sendPacket(this.getUserInitPacket(), user);
    }

    public void addSpectator(User user) {

        if (user == null) {
            return;
        }

        boolean added = this.spectators.add(user);

        if (!added) {
            return;
        }

        ((WorldManager) Server.getWorldManager()).getWorldBorderManager().sendPacket(this.getSpectatorInitPacket(),
                user);
    }

    public void removeUser(User user) {
        if (user == null) {
            return;
        }

        boolean removed = this.users.remove(user);

        if (!removed) {
            return;
        }

        ((WorldManager) Server.getWorldManager()).getWorldBorderManager().sendPacket(ExClientboundInitializeBorderPacket.wrapToMaxBorder(this.world.getBukkitWorld()), user);
    }

    public void removeSpectator(User user) {
        if (user == null) {
            return;
        }

        boolean removed = this.spectators.remove(user);

        if (!removed) {
            return;
        }

        ((WorldManager) Server.getWorldManager()).getWorldBorderManager().sendPacket(ExClientboundInitializeBorderPacket.wrapToMaxBorder(this.world.getBukkitWorld()), user);
    }

    public void setCenter(double centerX, double centerZ) {
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.broadcastUserPacket(this.getUserInitPacket());
        this.broadcastSpectatorPacket(this.getSpectatorInitPacket());
    }

    public void setSize(double size, int time, boolean forceStop) {
        if (this.shrinking) {
            if (forceStop) {
                this.stopShrink();
            } else {
                return;
            }
        }

        this.shrinkSize = size < 0 ? 0 : size;
        this.shrinkTime = time;
        this.shrinking();
    }

    public void stopShrink() {
        if (this.shrinkingTask != null) {
            this.shrinkingTask.cancel();
        }

        this.shrinkTime = 0;
        this.shrinkSize = this.size;
        this.shrinkPerTick = 0;
    }

    private void shrinking() {
        this.shrinking = true;

        ExPacketPlayOutWorldBorder packet = ExClientboundSetBorderLerpSizePacket.wrap(this.world.getBukkitWorld(),
                this.size, this.shrinkSize, this.shrinkTime * 50L);

        this.broadcastUserPacket(packet);
        this.broadcastSpectatorPacket(packet);

        this.shrinkPerTick = (this.size - this.shrinkSize) / this.shrinkTime;

        this.shrinkingTask = new BukkitRunnable() {
            @Override
            public void run() {
                ExWorldBorder.this.size -= shrinkPerTick;

                if (shrinkTime == 0) {
                    ExWorldBorder.this.size = shrinkSize;
                    shrinkPerTick = 0;
                    shrinking = false;
                    this.cancel();
                    return;
                }
                shrinkTime--;
            }
        }.runTaskTimer(BasicBukkit.getPlugin(), 0, 1);
    }

    public Set<User> getUsers() {
        return users;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterZ() {
        return centerZ;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size < 0 ? 0 : size;
        this.broadcastUserPacket(this.getUserInitPacket());
        this.broadcastSpectatorPacket(this.getSpectatorInitPacket());
        if (this.shrinking) {
            if (this.shrinkingTask != null) {
                this.shrinkingTask.cancel();
            }
            this.shrinking = false;
        }
    }

    public int getWarningDistance() {
        return warningDistance;
    }

    public int getWarningTime() {
        return warningTime;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    private void broadcastUserPacket(ExPacketPlayOutWorldBorder packet) {
        ((WorldManager) Server.getWorldManager()).getWorldBorderManager().sendPacket(packet, this.users);
    }

    private void broadcastSpectatorPacket(ExPacketPlayOutWorldBorder packet) {
        ((WorldManager) Server.getWorldManager()).getWorldBorderManager().sendPacket(packet, this.spectators);
    }

    private ExPacketPlayOutWorldBorder getUserInitPacket() {
        if (!this.shrinking) {
            return ExClientboundInitializeBorderPacket.wrap(this.world.getBukkitWorld(), this.centerX, this.centerZ,
                    this.size, this.size, 0, this.warningDistance, this.warningTime);
        } else {
            return ExClientboundInitializeBorderPacket.wrap(this.world.getBukkitWorld(), this.centerX, this.centerZ,
                    this.size, this.shrinkSize, this.shrinkTime * 50L, this.warningDistance, this.warningTime);
        }
    }

    private ExPacketPlayOutWorldBorder getSpectatorInitPacket() {
        if (!this.shrinking) {
            return ExClientboundInitializeBorderPacket.wrap(this.world.getBukkitWorld(), this.centerX, this.centerZ,
                    this.size, this.size, 0, 0, 0);
        } else {
            return ExClientboundInitializeBorderPacket.wrap(this.world.getBukkitWorld(), this.centerX, this.centerZ,
                    this.size, this.shrinkSize, this.shrinkTime * 50L, 0, 0);
        }
    }

    public boolean isInside(Location loc) {
        return this.centerX + this.size / 2 > loc.getX() && this.centerX - this.size / 2 < loc.getX() && this.centerZ + this.size / 2 > loc.getZ() && this.centerZ - this.size / 2 < loc.getZ();
    }


    private void startDamageTask() {

        this.damageTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (User user : users) {
                    if (!isInside(user.getLocation())) {
                        if (sound) {
                            user.playNote(Instrument.PLING, Note.natural(0, Note.Tone.A));
                        }

                        Server.runTaskSynchrony(() -> user.getPlayer().damage(damage), BasicBukkit.getPlugin());
                    }
                }
            }
        }.runTaskTimerAsynchronously(BasicBukkit.getPlugin(), 0, 20);
    }

    @EventHandler
    public void onUserQuit(UserQuitEvent e) {
        this.removeUser(e.getUser());
        this.removeSpectator(e.getUser());
    }
}
