package de.timesnake.basic.bukkit.core.world;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.AsyncUserJoinEvent;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import de.timesnake.basic.bukkit.util.world.HoloDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class HoloDisplayManager implements Listener, de.timesnake.basic.bukkit.util.world.HoloDisplayManager {

    private final HashMap<ExWorld, Set<HoloDisplay>> displaysPerWorld = new HashMap<>();
    private final HashMap<ExWorld, Set<HoloDisplay>> publicDisplaysPerWorld = new HashMap<>();

    public HoloDisplayManager() {
        Server.registerListener(this, BasicBukkit.getPlugin());
    }

    @Override
    public void addHoloDisplay(HoloDisplay display, User... users) {
        ExWorld world = display.getLocation().getExWorld();
        this.displaysPerWorld.computeIfAbsent(world, (w) -> new HashSet<>()).add(display);

        for (User user : users) {
            display.addWatcher(user);
        }
    }

    @Override
    public void addHoloDisplay(HoloDisplay display, boolean forAll) {
        ExWorld world = display.getLocation().getExWorld();
        this.publicDisplaysPerWorld.computeIfAbsent(world, (w) -> new HashSet<>()).add(display);
    }

    @Override
    public void removeHoloDisplay(HoloDisplay display) {
        Set<HoloDisplay> displays = this.displaysPerWorld.get(display.getLocation().getExWorld());

        if (displays != null) {
            displays.remove(display);
        }

        display.remove();
    }

    @Override
    public Set<HoloDisplay> getHoloDisplays(ExWorld world) {
        Set<HoloDisplay> displays = this.displaysPerWorld.get(world);
        return displays != null ? displays : new HashSet<>();
    }

    @EventHandler
    public void onUserJoin(AsyncUserJoinEvent e) {
        User user = e.getUser();

        Set<HoloDisplay> displays = this.publicDisplaysPerWorld.get(user.getExWorld());

        if (displays == null) {
            return;
        }

        for (HoloDisplay display : displays) {
            display.addWatcher(user);
        }

    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        Server.runTaskAsynchrony(() -> {
            User user = Server.getUser(e.getPlayer());

            if (user == null) {
                return;
            }

            Set<HoloDisplay> publicDisplays = this.publicDisplaysPerWorld.get(user.getExWorld());

            if (publicDisplays != null) {
                for (HoloDisplay display : publicDisplays) {
                    display.sendCreationPaketsTo(user);
                }
            }

            Set<HoloDisplay> displays = this.displaysPerWorld.get(user.getExWorld());

            if (displays != null) {
                for (HoloDisplay display : displays) {
                    if (display.getWatchers().contains(user)) {
                        display.addWatcher(user);
                    }
                }
            }
        }, BasicBukkit.getPlugin());


    }

}
