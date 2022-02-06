package de.timesnake.basic.bukkit.util.user;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Predicate;

public interface UserManager {

    Collection<User> getUsers();

    Collection<User> getUsers(Predicate<User> predicate);

    Collection<User> getNotServiceUsers();

    Collection<User> getServiceUsers();

    Collection<User> getPreGameUsers();

    Collection<User> getInGameUsers();

    Collection<User> getOutGameUsers();

    Collection<User> getSpectatorUsers();

    Collection<User> getOnlineUsers();

    Collection<User> getGameUsers();

    Collection<User> getGameNotServiceUsers();

    User getUser(UUID uuid);

    User getUser(Player p);

    Collection<User> getInOutGameUsers();
}
