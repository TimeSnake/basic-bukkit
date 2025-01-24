/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.world;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.core.world.WorldManager;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.server.ExTime;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.library.basic.util.BuilderBasis;
import de.timesnake.library.basic.util.BuilderNotFullyInstantiatedException;
import de.timesnake.library.basic.util.UserSet;
import de.timesnake.library.packets.core.packet.out.border.ClientboundInitializeBorderPacketBuilder;
import de.timesnake.library.packets.core.packet.out.border.ClientboundSetBorderLerpSizePacketBuilder;
import net.minecraft.network.protocol.Packet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Note;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ExWorldBorder {

  private final Logger logger = LogManager.getLogger("world.border");

  private final ExWorld world;

  private final UserSet<User> users = new UserSet<>() {
    @Override
    public void onAutoRemove(User user) {
      ((WorldManager) Server.getWorldManager()).getWorldBorderManager()
          .sendPacket(ClientboundInitializeBorderPacketBuilder.of(ExWorldBorder.this.world.getBukkitWorld()), user);
    }
  };
  private final UserSet<User> spectators = new UserSet<>() {
    @Override
    public void onAutoRemove(User user) {
      ((WorldManager) Server.getWorldManager()).getWorldBorderManager()
          .sendPacket(ClientboundInitializeBorderPacketBuilder.of(ExWorldBorder.this.world.getBukkitWorld()), user);
    }
  };

  private double centerX;
  private double centerZ;
  private double size;
  private int warningDistance;
  private int warningTime;

  private double damagePerSec;

  private boolean shrinking = false;

  private BukkitTask damageTask;

  private BukkitTask shrinkingTask;
  private double shrinkSize;
  private int shrinkTimeTicks;
  private double shrinkPerTick;

  private boolean sound;

  public ExWorldBorder(Builder builder) {
    this.world = builder.world;
    this.centerX = builder.centerX;
    this.centerZ = builder.centerZ;
    this.size = builder.size;
    this.warningDistance = builder.warningDistance;
    this.warningTime = builder.warningTime;
    this.damagePerSec = builder.damagePerSec;
    this.sound = builder.sound;

    this.startDamageTask();

    this.logger.info("Created world border: {}", this);
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

    this.logger.info("Destroyed world border in world '{}'", this.world.getName());
  }

  public void addUser(@NotNull User user) {
    this.spectators.remove(user);
    boolean added = this.users.add(user);

    if (!added) {
      return;
    }

    this.logger.info("Added user '{}' to world border in world '{}'", user.getName(), this.world.getName());

    ((WorldManager) Server.getWorldManager()).getWorldBorderManager().sendPacket(this.getUserInitPacket(), user);
  }

  public void addSpectator(@NotNull User user) {
    this.users.remove(user);
    boolean added = this.spectators.add(user);

    if (!added) {
      return;
    }

    this.logger.info("Added spectator '{}' to world border in world '{}'", user.getName(), this.world.getName());

    ((WorldManager) Server.getWorldManager()).getWorldBorderManager().sendPacket(this.getSpectatorInitPacket(), user);
  }

  public void removeUser(User user) {
    this.users.remove(user);
  }

  public void removeSpectator(User user) {
    this.spectators.remove(user);
  }

  public void setCenter(double centerX, double centerZ) {
    this.centerX = centerX;
    this.centerZ = centerZ;
    this.broadcastUserPacket(this.getUserInitPacket());
    this.broadcastSpectatorPacket(this.getSpectatorInitPacket());
  }

  public void setSize(double size, ExTime time, boolean forceStop) {
    if (this.shrinking) {
      if (forceStop) {
        this.stopShrink();
      } else {
        return;
      }
    }

    this.shrinkSize = size < 0 ? 0 : size;
    this.shrinkTimeTicks = time.toTicks();
    this.shrinking();
  }

  public void stopShrink() {
    if (this.shrinkingTask != null) {
      this.shrinkingTask.cancel();
    }

    this.shrinkTimeTicks = 0;
    this.shrinkSize = this.size;
    this.shrinkPerTick = 0;
  }

  private void shrinking() {
    this.shrinking = true;

    Packet<?> packet = ClientboundSetBorderLerpSizePacketBuilder.of(this.size, this.shrinkSize,
        this.shrinkTimeTicks * 50L);

    this.broadcastUserPacket(packet);
    this.broadcastSpectatorPacket(packet);

    this.shrinkPerTick = (this.size - this.shrinkSize) / this.shrinkTimeTicks;

    this.shrinkingTask = new BukkitRunnable() {
      @Override
      public void run() {
        ExWorldBorder.this.size -= shrinkPerTick;

        if (shrinkTimeTicks == 0) {
          ExWorldBorder.this.size = shrinkSize;
          shrinkPerTick = 0;
          shrinking = false;
          this.cancel();
          return;
        }
        shrinkTimeTicks--;
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

  public double getDamagePerSec() {
    return damagePerSec;
  }

  public void setDamagePerSec(double damagePerSec) {
    this.damagePerSec = damagePerSec;
  }

  public boolean isSound() {
    return sound;
  }

  public void setSound(boolean sound) {
    this.sound = sound;
  }

  public boolean isShrinking() {
    return this.shrinking;
  }

  private void broadcastUserPacket(Packet<?> packet) {
    ((WorldManager) Server.getWorldManager()).getWorldBorderManager().sendPacket(packet, this.users);
  }

  private void broadcastSpectatorPacket(Packet<?> packet) {
    ((WorldManager) Server.getWorldManager()).getWorldBorderManager().sendPacket(packet, this.spectators);
  }

  private Packet<?> getUserInitPacket() {
    if (!this.shrinking) {
      return ClientboundInitializeBorderPacketBuilder.of(this.world.getBukkitWorld(), this.centerX, this.centerZ,
          this.size, this.size, 0, this.warningDistance, this.warningTime);
    } else {
      return ClientboundInitializeBorderPacketBuilder.of(this.world.getBukkitWorld(), this.centerX, this.centerZ,
          this.size, this.shrinkSize, this.shrinkTimeTicks * 50L, this.warningDistance, this.warningTime);
    }
  }

  private Packet<?> getSpectatorInitPacket() {
    if (!this.shrinking) {
      return ClientboundInitializeBorderPacketBuilder.of(this.world.getBukkitWorld(), this.centerX, this.centerZ,
          this.size, this.size, 0, 0, 0);
    } else {
      return ClientboundInitializeBorderPacketBuilder.of(this.world.getBukkitWorld(), this.centerX, this.centerZ,
          this.size, this.shrinkSize, this.shrinkTimeTicks * 50L, 0, 0);
    }
  }

  public boolean isInside(Location loc) {
    return this.centerX + this.size / 2 > loc.getX()
        && this.centerX - this.size / 2 < loc.getX()
        && this.centerZ + this.size / 2 > loc.getZ()
        && this.centerZ - this.size / 2 < loc.getZ();
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

            Server.runTaskSynchrony(() -> user.getPlayer().damage(damagePerSec), BasicBukkit.getPlugin());
          }
        }
      }
    }.runTaskTimerAsynchronously(BasicBukkit.getPlugin(), 0, 20);
  }

  @Override
  public String toString() {
    return "ExWorldBorder{" +
        "world=" + world +
        ", centerX=" + centerX +
        ", centerZ=" + centerZ +
        ", size=" + size +
        ", warningDistance=" + warningDistance +
        ", warningTime=" + warningTime +
        ", damagePerSec=" + damagePerSec +
        ", shrinking=" + shrinking +
        ", shrinkSize=" + shrinkSize +
           ", shrinkTime=" + shrinkTimeTicks +
        ", shrinkPerTick=" + shrinkPerTick +
        ", sound=" + sound +
        '}';
  }

  public static class Builder implements BuilderBasis {

    private ExWorld world;
    private double centerX = 0;
    private double centerZ = 0;
    private Double size;
    private int warningDistance = 0;
    private int warningTime = 0;
    private double damagePerSec = 0;
    private boolean sound = false;

    public Builder world(ExWorld world) {
      this.world = world;
      return this;
    }

    public Builder centerX(double x) {
      this.centerX = x;
      return this;
    }

    public Builder centerZ(double z) {
      this.centerZ = z;
      return this;
    }

    public Builder size(double size) {
      this.size = size;
      return this;
    }

    public Builder warningDistance(int warningDistance) {
      this.warningDistance = warningDistance;
      return this;
    }

    public Builder warningTime(int warningTime) {
      this.warningTime = warningTime;
      return this;
    }

    public Builder damagePerSec(double damagePerSec) {
      this.damagePerSec = damagePerSec;
      return this;
    }

    public Builder sound(boolean sound) {
      this.sound = sound;
      return this;
    }

    @Override
    public void checkBuild() {
      if (this.world == null) {
        throw new BuilderNotFullyInstantiatedException("world is null");
      }

      if (this.size == null) {
        throw new BuilderNotFullyInstantiatedException("size is null");
      }
    }

    @Override
    public ExWorldBorder build() {
      this.checkBuild();
      return new ExWorldBorder(this);
    }
  }
}
