/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user;

import de.timesnake.basic.bukkit.util.user.event.UserChatCommandEvent;

@FunctionalInterface
public interface UserChatCommandListener {

    void onUserChatCommand(UserChatCommandEvent event);
}
