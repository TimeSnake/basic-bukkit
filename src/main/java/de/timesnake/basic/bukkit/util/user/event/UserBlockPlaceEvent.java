/*
 * Copyright (C) 2022 timesnake
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
