package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;

public class UserInteractEntityEvent extends CancelableUserEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    private final Entity entity;
    private final EquipmentSlot hand;

    public UserInteractEntityEvent(User user, boolean isCanceled, Entity entity, EquipmentSlot hand) {
        super(user, isCanceled);
        this.entity = entity;
        this.hand = hand;
    }

    public Entity getEntity() {
        return entity;
    }

    public EquipmentSlot getHand() {
        return hand;
    }
}
