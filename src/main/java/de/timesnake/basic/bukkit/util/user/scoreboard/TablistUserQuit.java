package de.timesnake.basic.bukkit.util.user.scoreboard;

import de.timesnake.basic.bukkit.util.user.event.UserQuitEvent;

@FunctionalInterface
public interface TablistUserQuit {

    void onUserQuit(UserQuitEvent e, Tablist tablist);
}
