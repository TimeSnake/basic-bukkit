/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.server;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.server.LoopTask;
import de.timesnake.basic.bukkit.util.server.TimeTask;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.*;

public class TaskManager {

  private final ExecutorService executorService = new ThreadPoolExecutor(5, 100,
      5L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

  public BukkitTask runTaskSynchrony(Runnable task, Plugin plugin) {
    return new BukkitRunnable() {
      @Override
      public void run() {
        task.run();
      }
    }.runTask(plugin);
  }

  public BukkitTask runTaskAsynchrony(Runnable task, Plugin plugin) {
    return new BukkitRunnable() {
      @Override
      public void run() {
        task.run();
      }
    }.runTaskAsynchronously(plugin);
  }

  public BukkitTask runTaskLaterSynchrony(Runnable task, int delay, Plugin plugin) {
    return new BukkitRunnable() {
      @Override
      public void run() {
        task.run();
      }
    }.runTaskLater(plugin, delay);
  }

  public BukkitTask runTaskLaterAsynchrony(Runnable task, int delay, Plugin plugin) {
    return new BukkitRunnable() {
      @Override
      public void run() {
        task.run();
      }
    }.runTaskLaterAsynchronously(plugin, delay);
  }

  public BukkitTask runTaskTimerSynchrony(Runnable task, int delay, int period, Plugin plugin) {
    return new BukkitRunnable() {
      @Override
      public void run() {
        task.run();
      }
    }.runTaskTimer(plugin, delay, period);
  }

  public Future runTaskExpTimerSynchrony(Runnable task, int startPeriod, double increaseMultiplier, int maxSpeed,
                                         boolean async) {
    ExpBukkitRunnable runnable = new ExpBukkitRunnable(task, startPeriod, increaseMultiplier, maxSpeed, async);
    return this.executorService.submit(runnable);
  }


  public BukkitTask runTaskTimerAsynchrony(Runnable task, int delay, int period, Plugin plugin) {
    return new BukkitRunnable() {
      @Override
      public void run() {
        task.run();
      }
    }.runTaskTimerAsynchronously(plugin, delay, period);
  }

  public BukkitTask runTaskTimerSynchrony(TimeTask task, Integer time, int delay, int period,
                                          Plugin plugin) {
    return new TimeBukkitRunnable(task, time, false).runTaskTimer(plugin, delay, period);
  }

  public BukkitTask runTaskTimerAsynchrony(TimeTask task, Integer time, int delay, int period,
                                           Plugin plugin) {
    return new TimeBukkitRunnable(task, time, false)
        .runTaskTimerAsynchronously(plugin, delay, period);
  }

  public BukkitTask runTaskTimerSynchrony(TimeTask task, Integer time, boolean cancelOnZero,
                                          int delay, int period, Plugin plugin) {
    return new TimeBukkitRunnable(task, time, cancelOnZero).runTaskTimer(plugin, delay, period);
  }

  public BukkitTask runTaskTimerAsynchrony(TimeTask task, Integer time, boolean cancelOnZero,
                                           int delay, int period, Plugin plugin) {
    return new TimeBukkitRunnable(task, time, cancelOnZero).runTaskTimerAsynchronously(plugin,
        delay, period);
  }

  public <Element> void runTaskLoopAsynchrony(LoopTask<Element> task, Iterable<Element> iterable,
                                              Plugin plugin) {
    for (Element e : iterable) {
      new BukkitRunnable() {
        @Override
        public void run() {
          task.run(e);
        }
      }.runTaskAsynchronously(plugin);
    }
  }

  private static class TimeBukkitRunnable extends BukkitRunnable {

    private final TimeTask task;
    private final boolean cancelOnZero;
    private int time;

    TimeBukkitRunnable(TimeTask task, int time, boolean cancelOnZero) {
      this.task = task;
      this.time = time;
      this.cancelOnZero = cancelOnZero;
    }

    @Override
    public void run() {
      this.task.run(this.time);

      if (this.cancelOnZero && this.time <= 0) {
        this.cancel();
        return;
      }

      this.time--;
    }
  }

  private static class ExpBukkitRunnable extends BukkitRunnable {

    private final Runnable task;
    private final boolean async;
    private double previousValue;

    private final int startPeriod;
    private final double factor;
    private final int maxSpeed;

    ExpBukkitRunnable(Runnable task, int startPeriod, double speedMultiplier, int maxSpeed, boolean async) {
      this.task = task;
      this.startPeriod = startPeriod;
      this.factor = Math.pow(0.5, speedMultiplier / startPeriod);
      this.previousValue = 1;
      this.maxSpeed = maxSpeed;
      this.async = async;
    }

    @Override
    public void run() {
      while (true) {
        try {
          Thread.sleep((long) (Math.max(this.maxSpeed, this.startPeriod * this.previousValue) * 50));
        } catch (InterruptedException e) {
          break;
        }

        if (this.async) {
          this.task.run();
        } else {
          Server.runTaskSynchrony(this.task, BasicBukkit.getPlugin());
        }

        this.previousValue *= this.factor;
      }

    }
  }
}
