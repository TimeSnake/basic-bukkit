/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.event;

import de.timesnake.basic.bukkit.util.user.User;
import javax.annotation.Nonnull;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;


/**
 * Called by {@link org.bukkit.event.entity.EntityDamageByEntityEvent} with user check
 */
public class EntityDamageByUserEvent extends CancelableUserEvent {

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

  private static final HandlerList HANDLERS = new HandlerList();
  private final Entity entity;
  private final EntityDamageEvent.DamageCause damageCause;
  private double damage;
  private boolean cancelDamage;

  public EntityDamageByUserEvent(Entity entity, User damager, boolean isCanceled, double damage,
      EntityDamageEvent.DamageCause cause) {
    super(damager, isCanceled);
    this.entity = entity;
    this.damageCause = cause;
    this.damage = damage;
    this.cancelDamage = false;
  }

  @Nonnull
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public Entity getEntity() {
    return entity;
  }

  public EntityDamageEvent.DamageCause getDamageCause() {
    return damageCause;
  }

  public double getDamage() {
    return damage;
  }

  public void setDamage(double damage) {
    this.damage = damage;
  }

  public boolean isCancelDamage() {
    return cancelDamage;
  }

  public void setCancelDamage(boolean cancelDamage) {
    this.cancelDamage = cancelDamage;
  }
}
