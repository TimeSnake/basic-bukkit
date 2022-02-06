package de.timesnake.basic.bukkit.util.server;

import de.timesnake.database.util.object.Status;
import de.timesnake.database.util.object.TooLongEntryException;
import de.timesnake.database.util.object.Type;
import de.timesnake.database.util.server.DbServer;

public interface Info {

    DbServer getDatabase();

    String getName();

    Integer getMaxPlayers();

    String getTask();

    Status.Server getStatus();

    void setStatus(Status.Server status);

    Integer getPort();

    Type.Server getType();

    Integer getOnlinePlayers();

    String getPassword();

    void setPassword(String password) throws TooLongEntryException;
}
