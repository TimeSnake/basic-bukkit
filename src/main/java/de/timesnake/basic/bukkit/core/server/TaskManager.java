/*
 * workspace.basic-bukkit.main
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

package de.timesnake.basic.bukkit.core.server;

import de.timesnake.basic.bukkit.util.server.LoopTask;
import de.timesnake.basic.bukkit.util.server.TimeTask;
import de.timesnake.library.basic.util.server.Task;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class TaskManager {

    public BukkitTask runTaskSynchrony(Task task, Plugin plugin) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        }.runTask(plugin);
    }

    public BukkitTask runTaskAsynchrony(Task task, Plugin plugin) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        }.runTaskAsynchronously(plugin);
    }

    public BukkitTask runTaskLaterSynchrony(Task task, int delay, Plugin plugin) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        }.runTaskLater(plugin, delay);
    }

    public BukkitTask runTaskLaterAsynchrony(Task task, int delay, Plugin plugin) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        }.runTaskLaterAsynchronously(plugin, delay);
    }

    public BukkitTask runTaskTimerSynchrony(Task task, int delay, int period, Plugin plugin) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        }.runTaskTimer(plugin, delay, period);
    }

    public BukkitTask runTaskTimerAsynchrony(Task task, int delay, int period, Plugin plugin) {
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
}
