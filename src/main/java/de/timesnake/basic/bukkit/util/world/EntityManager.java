package de.timesnake.basic.bukkit.util.world;

import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.entities.entity.bukkit.ExPlayer;

import java.util.Collection;

public interface EntityManager {

    void spawnPlayer(User user, ExPlayer player);

    void spawnPlayer(Collection<? extends User> users, ExPlayer player);

    void removePlayer(User user, ExPlayer player);

    void removePlayer(Collection<? extends User> users, ExPlayer player);
}
