package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.entity.Item;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class UserDropItemEvent extends CancelableUserEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    private final Item item;

    public UserDropItemEvent(User user, boolean isCanceled, Item item) {
        super(user, isCanceled);
        this.item = item;
    }

    public Item getItemDrop() {
        return item;
    }

    public ItemStack getItemStack() {
        return this.item.getItemStack();
    }
}
