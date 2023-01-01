/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.UserJoinEvent;
import de.timesnake.basic.bukkit.util.user.event.UserQuitEvent;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistUserJoin;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistUserQuit;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistablePlayer;
import de.timesnake.library.packets.util.packet.ExPacketPlayOut;
import de.timesnake.library.packets.util.packet.ExPacketPlayOutScoreboardDisplayObjective;
import de.timesnake.library.packets.util.packet.ExPacketPlayOutScoreboardObjective;
import de.timesnake.library.packets.util.packet.ExPacketPlayOutSideboardScoreSet;
import de.timesnake.library.packets.util.packet.ExPacketPlayOutTablistHeaderFooter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public abstract class Tablist extends Board implements Listener,
        de.timesnake.basic.bukkit.util.user.scoreboard.Tablist {

    protected final ScoreboardPacketManager packetManager;

    protected final TablistUserJoin userJoin;
    protected final TablistUserQuit userQuit;

    protected final Type type;

    protected String header;
    protected String footer;

    public Tablist(String name, Type type, ScoreboardPacketManager packetManager, TablistUserJoin userJoin,
                   TablistUserQuit userQuit) {
        super(name);
        this.type = type;
        this.packetManager = packetManager;
        this.userJoin = userJoin;
        this.userQuit = userQuit;
        Server.registerListener(this, BasicBukkit.getPlugin());
    }

    @Override
    public void setHeader(String header) {
        this.header = header;
        this.updateHeaderFooter();
    }

    @Override
    public void setFooter(String footer) {
        this.footer = footer;
        this.updateHeaderFooter();
    }

    @Override
    public void addWatchingUser(User user) {
        super.addWatchingUser(user);
        this.load(user);
    }

    @Override
    public void removeWatchingUser(User user) {
        if (this.wachtingUsers.contains(user)) {
            this.unload(user);
        }
        super.removeWatchingUser(user);
    }

    @Override
    public void updateEntryValue(TablistablePlayer entry, Integer value) {
        ExPacketPlayOutSideboardScoreSet packet = ExPacketPlayOutSideboardScoreSet.wrap(this.name, value,
                entry.getTablistName());
        this.packetManager.sendPacket(this.wachtingUsers, packet);
    }

    protected void broadcastPacket(ExPacketPlayOut packet) {
        for (User user : this.wachtingUsers) {
            this.packetManager.sendPacket(user, packet);
        }
    }

    protected void updateHeaderFooter() {
        this.packetManager.sendPacket(this.wachtingUsers, ExPacketPlayOutTablistHeaderFooter.wrap(this.header,
                this.footer));
    }

    @EventHandler
    public void onUserJoin(UserJoinEvent e) {
        this.userJoin.onUserJoin(e, this);

        User user = e.getUser();

        if (this.type.equals(Type.HEALTH)) {
            if (user.getPlayer().getGameMode().equals(GameMode.SURVIVAL) || user.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
                this.packetManager.sendPacket(this.wachtingUsers, ExPacketPlayOutSideboardScoreSet.wrap(this.name,
                        ((int) user.getHealth()), user.getName()));
            }

        }
    }

    @EventHandler
    public void onUserQuit(UserQuitEvent e) {
        this.userQuit.onUserQuit(e, this);
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent e) {
        if (!this.type.equals(Type.HEALTH)) {
            return;
        }

        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        User user = Server.getUser(((Player) e.getEntity()));

        Server.runTaskLaterSynchrony(() -> this.updateEntryValue(user,
                (int) Math.ceil(((Player) e.getEntity()).getHealth())), 1, BasicBukkit.getPlugin());

    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (!this.type.equals(Type.HEALTH)) {
            return;
        }

        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        User user = Server.getUser(((Player) e.getEntity()));

        Server.runTaskLaterSynchrony(() -> this.updateEntryValue(user,
                (int) Math.ceil(((Player) e.getEntity()).getHealth())), 1, BasicBukkit.getPlugin());
    }

    protected void load(User user) {
        this.packetManager.sendPacket(user, ExPacketPlayOutScoreboardObjective.wrap(this.name, "",
                ExPacketPlayOutScoreboardObjective.Display.REMOVE, this.type.getPacketType()));

        this.packetManager.sendPacket(user, ExPacketPlayOutScoreboardObjective.wrap(this.name, "",
                ExPacketPlayOutScoreboardObjective.Display.CREATE, this.type.getPacketType()));

        this.packetManager.sendPacket(user, ExPacketPlayOutScoreboardDisplayObjective.wrap(this.name,
                ExPacketPlayOutScoreboardDisplayObjective.Slot.TABLIST));

        this.packetManager.sendPacket(user, ExPacketPlayOutTablistHeaderFooter.wrap(this.header, this.footer));

        for (User u : Server.getUsers()) {
            if (this.type.equals(Type.HEALTH)) {
                this.packetManager.sendPacket(user, ExPacketPlayOutSideboardScoreSet.wrap(this.name,
                        ((int) u.getHealth()), user.getName()));
            }
        }
    }

    protected abstract void unload(User user);

}
