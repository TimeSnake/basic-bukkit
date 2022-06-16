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
