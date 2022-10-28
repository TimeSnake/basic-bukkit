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
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class UserBlockPlaceEvent extends PriorityCancelableUserEvent {

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    private final Block block;
    private final Block blockPlaced;
    private final Block blockAgainst;
    private final EquipmentSlot hand;
    private final BlockState blockReplacedState;
    private final ItemStack itemInHand;
    private boolean canBuild;

    public UserBlockPlaceEvent(User user, boolean isCanceled, Block block, Block blockPlaced, Block blockAgainst,
                               EquipmentSlot hand, BlockState blockReplacedState, ItemStack itemInHand,
                               boolean canBuild) {
        super(user, isCanceled);
        this.block = block;
        this.blockAgainst = blockAgainst;
        this.blockPlaced = blockPlaced;
        this.hand = hand;
        this.blockReplacedState = blockReplacedState;
        this.itemInHand = itemInHand;
        this.canBuild = canBuild;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Block getBlock() {
        return block;
    }

    public Block getBlockPlaced() {
        return blockPlaced;
    }

    public Block getBlockAgainst() {
        return blockAgainst;
    }

    public BlockState getBlockReplacedState() {
        return blockReplacedState;
    }

    public EquipmentSlot getHand() {
        return hand;
    }

    public ItemStack getItemInHand() {
        return itemInHand;
    }

    public boolean canBuild() {
        return canBuild;
    }

    public void setBuild(boolean canBuild) {
        this.canBuild = canBuild;
    }
}
