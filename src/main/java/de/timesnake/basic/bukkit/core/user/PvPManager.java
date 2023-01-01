/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.UserDamageByUserEvent;
import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;
import de.timesnake.database.util.server.DbPvPServer;
import de.timesnake.library.basic.util.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Plugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PvPManager implements Listener, de.timesnake.basic.bukkit.util.user.PvPManager {

    public static final Integer ATTACK_SPEED = 10;
    public static final Integer MAX_NO_DAMAGE_TICKS = 4;
    public static final double DAMAGE_MULTIPLIER = 0.7;

    private boolean oldPvP;

    public PvPManager() {
        if (Server.getDatabase() instanceof DbPvPServer) {
            this.oldPvP = ((DbPvPServer) Server.getDatabase()).isOldPvP();
        } else {
            this.oldPvP = false;
        }
        Server.registerListener(this, BasicBukkit.getPlugin());
    }

    @Override
    public void setPvP(boolean oldPvP) {
        this.oldPvP = oldPvP;
        for (User user : Server.getUsers()) {
            this.updateAttributesOfUser(user);

            if (this.oldPvP) {
                user.getPlayer().setMaximumNoDamageTicks(MAX_NO_DAMAGE_TICKS);
            } else {
                user.getPlayer().setMaximumNoDamageTicks(10);
            }
        }
    }

    @Override
    public boolean isOldPvP() {
        return oldPvP;
    }

    @EventHandler
    public void onUserJoin(UserJoinEvent event) {
        User user = event.getUser();
        this.updateAttributesOfUser(user);
    }

    private void updateAttributesOfUser(User user) {
        user.setPvpMode(this.oldPvP);
    }

    @EventHandler
    public void onUserDamageByUser(UserDamageByUserEvent e) {
        if (this.oldPvP) {
            e.setDamage(DAMAGE_MULTIPLIER * e.getDamage());
        }
    }

    @Override
    public void broadcastPvPTypeMessage() {
        if (this.oldPvP) {
            Server.broadcastMessage(Plugin.BUKKIT, Component.text("§lHint: ", ExTextColor.WARNING, TextDecoration.BOLD)
                    .append(Component.text("Pre1.9 pvp (1.8 pvp) is activated, so you can hit fast.", ExTextColor.WARNING)));
        } else {
            Server.broadcastMessage(Plugin.BUKKIT, Component.text("§lHint: ", ExTextColor.WARNING, TextDecoration.BOLD)
                    .append(Component.text("1.9+ pvp is activated, so you should hit slow.", ExTextColor.WARNING)));
        }
    }

}
