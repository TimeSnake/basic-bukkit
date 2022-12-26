/*
 * Copyright (C) 2022 timesnake
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
