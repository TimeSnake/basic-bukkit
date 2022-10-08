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
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class UserChatCommandEvent extends CancelableUserEvent {

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    private final String message;
    private boolean removeLisener = true;

    public UserChatCommandEvent(User user, boolean isCanceled, String message) {
        super(user, isCanceled);
        this.message = message;
    }

    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRemoveLisener() {
        return removeLisener;
    }

    public void removeLisener(boolean removeLisener) {
        this.removeLisener = removeLisener;
    }
}
