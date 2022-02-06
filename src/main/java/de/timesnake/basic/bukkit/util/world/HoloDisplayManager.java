package de.timesnake.basic.bukkit.util.world;

import de.timesnake.basic.bukkit.util.user.User;

import java.util.Set;

public interface HoloDisplayManager {

    void addHoloDisplay(HoloDisplay display, User... users);

    void addHoloDisplay(HoloDisplay display, boolean forAll);

    void removeHoloDisplay(HoloDisplay display);

    Set<HoloDisplay> getHoloDisplays(ExWorld world);
}
