package de.timesnake.basic.bukkit.util.world.entity;

import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.world.ExLocation;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PacketEntity {

    protected final ExLocation location;
    protected final Set<User> watchers = ConcurrentHashMap.newKeySet();
    private final Set<User> userLoadedFor = ConcurrentHashMap.newKeySet();
    protected boolean isPublic = false;

    public PacketEntity(ExLocation location) {
        this.location = location;
    }

    public ExLocation getLocation() {
        return location;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void addWatcher(User... users) {
        for (User user : users) {
            this.watchers.add(user);
            this.spawnForUser(user);
        }
    }

    public void removeWatcher(User... users) {
        for (User user : users) {
            this.watchers.remove(user);
            this.despawnForUser(user);
        }
    }

    protected abstract void spawnForUser(User user);

    protected abstract void despawnForUser(User user);

    public void despawnForUser() {
        for (User user : this.userLoadedFor) {
            this.despawnForUser(user);
        }
        this.userLoadedFor.clear();
        this.watchers.clear();
    }

    public boolean isLoadedForUser(User user) {
        return this.userLoadedFor.contains(user);
    }

    public void loadForUser(User user) {
        this.userLoadedFor.add(user);
        this.spawnForUser(user);
    }

    public void unloadForUser(User user) {
        this.userLoadedFor.remove(user);
        this.despawnForUser(user);
    }

    public boolean isUserWatching(User user) {
        return this.isPublic || this.watchers.contains(user);
    }

    public void onUserQuit(User user) {
        this.watchers.remove(user);
    }
}
