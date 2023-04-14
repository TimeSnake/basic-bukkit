/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class UserInteractEvent extends UserEvent {

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    protected ItemStack item;
    protected Action action;
    protected Block clickedBlock;
    protected BlockFace blockFace;
    private Result useClickedBlock;
    private Result useItemInHand;
    private EquipmentSlot hand;
    private Location interactionPoint;

    public UserInteractEvent(User user, ItemStack item, Action action, Block clickedBlock,
            BlockFace blockFace, EquipmentSlot hand, Location interactionPoint) {
        super(user);
        this.item = item;
        this.action = action;
        this.clickedBlock = clickedBlock;
        this.blockFace = blockFace;
        this.useItemInHand = Result.DEFAULT;
        this.useClickedBlock = clickedBlock == null ? Result.DENY : Result.ALLOW;
        this.hand = hand;
        this.interactionPoint = interactionPoint;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Block getClickedBlock() {
        return clickedBlock;
    }

    public void setClickedBlock(Block clickedBlock) {
        this.clickedBlock = clickedBlock;
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }

    public void setBlockFace(BlockFace blockFace) {
        this.blockFace = blockFace;
    }

    public Result getUseClickedBlock() {
        return useClickedBlock;
    }

    public void setUseClickedBlock(Result useClickedBlock) {
        this.useClickedBlock = useClickedBlock;
    }

    public Result getUseItemInHand() {
        return useItemInHand;
    }

    public void setUseItemInHand(Result useItemInHand) {
        this.useItemInHand = useItemInHand;
    }

    public EquipmentSlot getHand() {
        return hand;
    }

    public void setHand(EquipmentSlot hand) {
        this.hand = hand;
    }

    public Location getInteractionPoint() {
        return interactionPoint;
    }

    public void setInteractionPoint(Location interactionPoint) {
        this.interactionPoint = interactionPoint;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
