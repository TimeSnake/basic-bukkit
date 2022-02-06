package de.timesnake.basic.bukkit.core.permission;


import de.timesnake.database.util.object.Status;

import java.util.Collection;

public class ExPermission {

    private String permission;
    private Status.Permission mode;
    private Collection<String> server;

    public ExPermission(String permission, Status.Permission mode, Collection<String> server) {
        this.setPermission(permission);
        this.setMode(mode);
        this.setServer(server);
    }

    public String getPermission() {
        return permission;
    }

    public Status.Permission getMode() {
        return mode;
    }

    public Collection<String> getServer() {
        return server;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setMode(Status.Permission mode) {
        this.mode = mode;
    }

    public void setServer(Collection<String> server) {
        this.server = server;
    }
}
