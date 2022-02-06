package de.timesnake.basic.bukkit.core.user;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;
import de.timesnake.database.util.server.DbPvPServer;
import de.timesnake.library.basic.util.chat.ChatColor;
import de.timesnake.library.basic.util.chat.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PvPManager implements Listener {

    public static final Integer FULL_ATTACKS_PER_SECOND = 10;
    public static final Integer ATTACK_DAMAGE = 2;
    public static final Integer MAX_NO_DAMAGE_TICKS = 4;
    public static final Integer ARMOR_POINTS = 5;

    private boolean oldPvP;

    public PvPManager() {
        if (Server.getDatabase() instanceof DbPvPServer) {
            this.oldPvP = ((DbPvPServer) Server.getDatabase()).isOldPvP();
        } else {
            this.oldPvP = false;
        }
        Server.registerListener(this, BasicBukkit.getPlugin());
    }

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

    public void broadcastPvPTypeMessage() {
        if (this.oldPvP) {
            Server.broadcastMessage(Plugin.BUKKIT, ChatColor.WARNING + "§lHint: " + ChatColor.WARNING + "Pre1.9 pvp (1.8 pvp) is activated, so you can hit fast.");
        } else {
            Server.broadcastMessage(Plugin.BUKKIT, ChatColor.WARNING + "§lHint: " + ChatColor.WARNING + "1.9+ pvp is activated, so you should hit slow.");
        }
    }

}
