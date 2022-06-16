package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;

public class UserBlockBreakEvent extends CancelableUserEvent {

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    private final Block block;
    private int expToDrop;
    private boolean dropItems;

    public UserBlockBreakEvent(User user, boolean isCanceled, Block block, int expToDrop, boolean dropItems) {
        super(user, isCanceled);
        this.block = block;
        this.expToDrop = expToDrop;
        this.dropItems = dropItems;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Block getBlock() {
        return block;
    }

    public int getExpToDrop() {
        return expToDrop;
    }

    public void setExpToDrop(int expToDrop) {
        this.expToDrop = expToDrop;
    }

    public boolean isDropItems() {
        return dropItems;
    }

    public void setDropItems(boolean dropItems) {
        this.dropItems = dropItems;
    }
}
