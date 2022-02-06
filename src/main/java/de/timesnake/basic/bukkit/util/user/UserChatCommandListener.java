package de.timesnake.basic.bukkit.util.user;

import de.timesnake.basic.bukkit.util.user.event.UserChatCommandEvent;

public interface UserChatCommandListener {

    void onUserChatCommand(UserChatCommandEvent event);
}
