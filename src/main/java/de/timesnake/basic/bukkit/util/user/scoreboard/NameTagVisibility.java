/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import de.timesnake.library.packets.util.packet.ExPacketPlayOutTablistTeamCreation;

public enum NameTagVisibility {

    ALWAYS(ExPacketPlayOutTablistTeamCreation.NameTagVisibility.ALWAYS),
    NEVER(ExPacketPlayOutTablistTeamCreation.NameTagVisibility.NEVER),
    // not required due to explicit check methods
    //HIDE_FOR_OTHER_TEAMS(ExPacketPlayOutTablistTeamCreation.NameTagVisibility.HIDE_FOR_OTHER_TEAMS),
    //HIDE_FOR_OWN_TEAM(ExPacketPlayOutTablistTeamCreation.NameTagVisibility.HIDE_FOR_OWN_TEAM)
    ;

    private final ExPacketPlayOutTablistTeamCreation.NameTagVisibility packetTag;

    NameTagVisibility(ExPacketPlayOutTablistTeamCreation.NameTagVisibility packetTag) {
        this.packetTag = packetTag;
    }

    public ExPacketPlayOutTablistTeamCreation.NameTagVisibility getPacketTag() {
        return packetTag;
    }
}
