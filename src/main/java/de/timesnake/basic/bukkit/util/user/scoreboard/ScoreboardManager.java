package de.timesnake.basic.bukkit.util.user.scoreboard;

import java.util.Collection;
import java.util.LinkedList;

public interface ScoreboardManager {

    GroupTablist registerNewGroupTablist(String name, Tablist.Type type, LinkedList<TablistGroupType> types,
                                         TablistUserJoin userJoin, TablistUserQuit userQuit);

    TeamTablist registerNewTeamTablist(String name, Tablist.Type type, TeamTablist.ColorType colorType,
                                       Collection<? extends TablistableGroup> teamsList, TablistUserJoin userJoin,
                                       TablistUserQuit userQuit, LinkedList<TablistGroupType> groupTypes,
                                       TablistGroupType teamType);

    TeamTablist registerNewTeamTablist(String name, Tablist.Type type, TeamTablist.ColorType colorType,
                                       Collection<? extends TablistableGroup> teamsList, TablistGroupType teamType,
                                       LinkedList<TablistGroupType> groupTypes, TablistableRemainTeam remainTeam,
                                       LinkedList<TablistGroupType> remainTeamGroupTypes, TablistUserJoin userJoin,
                                       TablistUserQuit userQuit);

    Tablist getTablist(String name);

    void removeTablist(String name);

    Sideboard registerNewSideboard(String name, String title);

    Sideboard getSideboard(String name);

    void removeSideboard(String name);

    Tablist getActiveTablist();

    void setActiveTablist(Tablist tablist);

    ScoreboardPacketManager getPacketManager();
}
