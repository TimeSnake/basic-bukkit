package de.timesnake.basic.bukkit.core.user;

import de.timesnake.basic.bukkit.core.permission.Group;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.database.util.object.Status;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserManager implements de.timesnake.basic.bukkit.util.user.UserManager {

    private final ConcurrentMap<UUID, User> users = new ConcurrentHashMap<>();
    private final ConcurrentMap<UUID, Future<User>> preUsers = new ConcurrentHashMap<>();

    /**
     * Stores the loaded user in preUsers map
     *
     * @param user to store
     */
    public final void storeUser(UUID uuid, Future<User> user) {
        this.preUsers.put(uuid, user);
    }

    /**
     * Loads the preUser in users
     *
     * @param uuid of the user
     */
    public final void registerUser(UUID uuid) {
        User user;

        try {
            user = this.preUsers.get(uuid).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return;
        }

        this.users.put(uuid, user);
        this.preUsers.remove(uuid);

        user.load();
    }

    /**
     * Removes the user from users
     *
     * @param uuid of the user
     */
    public final void removeUser(UUID uuid) {
        de.timesnake.basic.bukkit.util.user.User user = this.getUser(uuid);
        if (user.getGroup() != null) {
            ((Group) user.getGroup()).removeUser(user);
        }
        this.users.remove(uuid);
    }

    public final Collection<User> getUsers() {
        return this.users.values().stream().filter((u) -> !u.isQuiting()).collect(Collectors.toSet());
    }

    public final Collection<User> getUsers(Predicate<User> predicate) {
        return this.getUsers().stream().filter(predicate).collect(Collectors.toSet());
    }

    /**
     * Gets the user who are not having the status service
     *
     * @return the users
     */
    public final Collection<User> getNotServiceUsers() {
        return this.getUsers((u) -> !u.isService());
    }

    /**
     * Gets the user who are having the status service
     *
     * @return the users
     */
    public final Collection<User> getServiceUsers() {
        return this.getUsers(User::isService);
    }

    /**
     * Gets the user who are having the status pregrame
     *
     * @return the users
     */
    public final Collection<User> getPreGameUsers() {
        return this.getUsers((u) -> u.getStatus().equals(Status.User.PRE_GAME));
    }

    /**
     * Gets the user who are having the status ingame
     *
     * @return the users
     */
    public final Collection<User> getInGameUsers() {
        return this.getUsers((u) -> u.getStatus().equals(Status.User.IN_GAME));
    }

    /**
     * Gets the user who are having the status outgame
     *
     * @return the users
     */
    public final Collection<User> getOutGameUsers() {
        return this.getUsers((u) -> u.getStatus().equals(Status.User.OUT_GAME));
    }

    /**
     * Gets the user who are having the status online
     *
     * @return the users
     */
    public final Collection<User> getOnlineUsers() {
        return this.getUsers((u) -> u.getStatus().equals(Status.User.ONLINE));
    }

    /**
     * Gets the user who are having the status spectator
     *
     * @return the users
     */
    public final Collection<User> getSpectatorUsers() {
        return this.getUsers((u) -> u.getStatus().equals(Status.User.SPECTATOR));
    }

    /**
     * Gets the user who are having the status outgame out ingame
     *
     * @return the users
     */
    @Override
    public Collection<User> getInOutGameUsers() {
        return this.getUsers((u) -> {
            Status.User status = u.getStatus();
            return status.equals(Status.User.OUT_GAME) || status.equals(Status.User.IN_GAME);
        });
    }

    /**
     * Gets the user who are having the status outgame, ingame, pregame, spectator
     *
     * @return the users
     */
    public final Collection<User> getGameUsers() {
        return this.getUsers((u) -> {
            Status.User status = u.getStatus();
            return status.equals(Status.User.OUT_GAME) || status.equals(Status.User.IN_GAME)
                    || status.equals(Status.User.PRE_GAME) || status.equals(Status.User.SPECTATOR);
        });
    }

    /**
     * Gets the user who are having the status outgame, ingame, pregame, spectator and are not in service mode
     *
     * @return the users
     */
    @Override
    public final Collection<User> getGameNotServiceUsers() {
        return this.getGameUsers().stream().filter((u) -> !u.isService()).collect(Collectors.toList());
    }

    /**
     * Gets the user by UUID
     *
     * @param uuid The uuid of the player
     * @return the user
     */
    public final User getUser(UUID uuid) {
        return this.users.get(uuid);
    }

    /**
     * Gets the user by player
     *
     * @param p The {@link Player} to get
     * @return the user
     */
    public final User getUser(Player p) {
        return this.getUser(p.getUniqueId());
    }

}
