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

package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;

import java.util.HashMap;
import java.util.Map;

public abstract class PriorityCancelableUserEvent extends CancelableUserEvent {

    private final Map<CancelPriority, Boolean> cancelledByPriority = new HashMap<>();

    protected PriorityCancelableUserEvent(User user, boolean isCancelled) {
        super(user, isCancelled);
        this.cancelledByPriority.put(CancelPriority.LOWEST, isCancelled);
    }

    public Boolean isCancelled(CancelPriority priority) {
        Boolean cancelled = this.cancelledByPriority.get(priority);
        return cancelled != null ? cancelled : false;
    }

    @Override
    public boolean isCancelled() {
        boolean cancelled = false;
        for (CancelPriority priority : CancelPriority.values()) {
            Boolean priorityCancel = this.cancelledByPriority.get(priority);
            if (priorityCancel != null) {
                cancelled = priorityCancel;
            }
        }
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelledByPriority.put(CancelPriority.DEFAULT, cancelled);
    }

    public void setCancelled(CancelPriority priority, boolean cancelled) {
        this.cancelledByPriority.put(priority, cancelled);
    }
}
