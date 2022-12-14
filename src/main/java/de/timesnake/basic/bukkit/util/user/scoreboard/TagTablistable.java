/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

public interface TagTablistable {

    NameTagVisibility isNameTagVisibleBy(TablistablePlayer player, TablistableGroup otherGroup);

    NameTagVisibility isNameTagVisible(TablistablePlayer player);
}
