/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.util.user;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.library.basic.util.BuilderBasis;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scheduler.BukkitTask;

public class ReloadableItem {

    protected final User user;
    protected final ExItemStack item;
    protected BukkitTask task;
    protected final int delaySec;
    protected final boolean showActionBarText;
    protected final boolean damageItem;
    protected final Function<Integer, Component> actionBarFunction;

    protected int currentDelaySec;

    public ReloadableItem(Builder builder) {
        this.user = builder.user;
        this.item = builder.item;
        this.delaySec = builder.delaySec;
        this.showActionBarText = builder.showActionBarText;
        this.damageItem = builder.damageItem;
        this.actionBarFunction = builder.actionBarFunction;

        if (this.damageItem && !(item.getItemMeta() instanceof Damageable)) {
            throw new InvalidItemTypeException("item must be damageable");
        }
    }

    public void giveUser() {
        this.user.addItem(this.item.cloneWithId());
    }

    public void reload() {
        this.cancel();

        this.currentDelaySec = 0;

        this.task = Server.runTaskTimerSynchrony(() -> {
            this.update();

            if (this.currentDelaySec >= this.delaySec) {
                this.reloaded();
                this.task.cancel();
                return;
            }

            this.currentDelaySec++;
        }, 20, 20, BasicBukkit.getPlugin());
    }

    public void update() {
        if (this.damageItem) {
            this.user.replaceExItemStack(this.item,
                    this.item.cloneWithId().setDamage(this.getDelayProgress()));
        }

        if (this.showActionBarText) {
            this.user.sendActionBarText(
                    this.actionBarFunction.apply(this.delaySec - this.currentDelaySec));
        }
    }

    public void reloaded() {

    }

    public User getUser() {
        return user;
    }

    public ExItemStack getItem() {
        return item;
    }

    public int getDelaySec() {
        return delaySec;
    }

    public int getCurrentDelaySec() {
        return currentDelaySec;
    }

    public float getDelayProgress() {
        return ((float) this.getCurrentDelaySec()) / this.getDelaySec();
    }

    public void cancel() {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    public static class Builder implements BuilderBasis {

        private User user;
        private ExItemStack item;
        private Integer delaySec;
        private boolean showActionBarText = false;
        private boolean damageItem = false;
        private Function<Integer, Component> actionBarFunction = i -> Component.text(i + " s");

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder item(ExItemStack item) {
            this.item = item;
            return this;
        }

        public Builder delay(int delaySec) {
            this.delaySec = delaySec;
            return this;
        }

        public Builder showActionBarText() {
            this.showActionBarText = true;
            return this;
        }

        public Builder damageItem() {
            this.damageItem = true;
            return this;
        }

        public Builder actionBarText(Function<Integer, Component> function) {
            this.actionBarFunction = function;
            return this;
        }

        @Override
        public void checkBuild() {
            BuilderBasis.checkNotNull(user, "user is null");
            BuilderBasis.checkNotNull(item, "item is null");
            BuilderBasis.checkNotNull(delaySec, "delay is null");
            BuilderBasis.checkNotNull(actionBarFunction, "action bar function is null");
        }

        @Override
        public ReloadableItem build() {
            this.checkBuild();
            return new ReloadableItem(this);
        }
    }
}
