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

import de.timesnake.basic.bukkit.util.user.ExItemStack;
import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;

public class UserInventoryInteractEvent extends UserInventoryEvent {

    private final Block clickedBlock;
    private final Action action;

    public UserInventoryInteractEvent(User user, boolean isCanceled, ExItemStack clickedItem, Block clickedBlock,
                                      Action action) {
        super(user, isCanceled, clickedItem);
        this.clickedBlock = clickedBlock;
        this.action = action;
    }

    public Block getClickedBlock() {
        return clickedBlock;
    }

    public Action getAction() {
        return action;
    }
}
