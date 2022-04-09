## ScoreboardManager

This manager provides completely packet based user specific tablists and sideboards.

### Tablist

Two tablist types are available:

#### GroupTablist

This tablist sorts players based on multiple group types, and subgroup types. Within a group, players are sorted
lexicographically. Each group can have its own prefix and color. Each player can have its own prefix and color.

#### TeamTablist

This tablist sorts players based on multiple teams, and subgroups. Teams are separated by empty lines and can have
header lines. The remain team is always at the end of the tablist.

### Sideboard

In sideboards, each line can be set independently.