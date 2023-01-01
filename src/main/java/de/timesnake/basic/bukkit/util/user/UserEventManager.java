/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user;

public interface UserEventManager {

    void addUserChatCommand(User user, UserChatCommandListener listener);

    void removeUserChatCommand(User user, UserChatCommandListener listener);

    void removeUserChatCommand(User user);
}
