## ScoreboardManager

This manager provides completely packet based user specific tablists and sideboards.

### Tablist

To a tablist all objects, which inherit the [TablistablePlayer] interface can be added.
TablistablPlayers can be sorted in the tablist by groups and teams. To allow the use of an object as group or team,
the interface [TablistableGroup] must be implemented.
For a remain team, the [TablistableRemainTeam] must be implemented.
The group and team of a player is determined by invoking the `TablistablePlayer#getTablistGroup(TablistGroupType type)`
method. Due to that, each group and team in a tablist must be a different [TablistGroupType]. So it is
recommended to override this class.

In general, two tablist types are available:

**GroupTablist**

This tablist sorts players based on multiple group types, and subgroup types. Within a group, players are sorted
lexicographically. Each group can have its own prefix and color. Each player can have its own prefix and color.

**TeamTablist**

This tablist sorts players based on multiple teams, and subgroups. Teams are separated by empty lines and can have
header lines. The remain team is always at the end of the tablist.

### Sideboard

In sideboards, each line can be set independently.


[TablistablePlayer]: /src/main/java/de/timesnake/basic/bukkit/util/user/scoreboard/TablistablePlayer.java

[TablistableGroup]: /src/main/java/de/timesnake/basic/bukkit/util/user/scoreboard/TablistableGroup.java

[TablistableRemainTeam]: /src/main/java/de/timesnake/basic/bukkit/util/user/scoreboard/TablistableRemainTeam.java

[TablistGroupType]: /src/main/java/de/timesnake/basic/bukkit/util/user/scoreboard/TablistGroupType.java