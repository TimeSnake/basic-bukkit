package de.timesnake.basic.bukkit.util.world;

import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.entities.entity.bukkit.ExArmorStand;
import de.timesnake.basic.packets.util.packet.ExPacketPlayOutEntityDestroy;
import de.timesnake.basic.packets.util.packet.ExPacketPlayOutEntityMetadata;
import de.timesnake.basic.packets.util.packet.ExPacketPlayOutSpawnEntityLiving;

import java.util.*;

public class HoloDisplay {

    private final ExLocation location;
    private final List<String> text;

    private final List<ExArmorStand> stands;

    private final Set<User> watchers = new HashSet<>();

    public HoloDisplay(ExLocation location, List<String> text) {
        this.location = location;
        this.text = text;

        this.stands = new ArrayList<>();

        double offset = (this.text.size() - 1) * 0.275;
        for (String line : this.text) {
            ExArmorStand stand = new ExArmorStand(location.getWorld());

            stand.setCustomName(line);
            stand.setCustomNameVisible(true);
            stand.setSmall(true);
            stand.setInvulnerable(false);
            stand.setInvisible(true);
            stand.setNoGravity(true);
            stand.setPosition(this.location.getX(), this.location.getY() + offset, this.location.getZ());

            offset -= 0.275;

            this.stands.add(stand);
        }
    }

    public ExLocation getLocation() {
        return location;
    }

    public List<String> getText() {
        return text;
    }

    public void addWatcher(User user) {
        this.watchers.add(user);
        this.sendCreationPaketsTo(user);
    }

    public void sendCreationPaketsTo(User user) {
        for (ExArmorStand stand : this.stands) {
            user.sendPacket(ExPacketPlayOutSpawnEntityLiving.wrap(stand));
            user.sendPacket(ExPacketPlayOutEntityMetadata.wrap(stand, ExPacketPlayOutEntityMetadata.DataType.UPDATE));
        }
    }

    public void removeWatcher(User user) {
        this.watchers.remove(user);
        this.sendRemovePacketsTo(user);
    }

    public void sendRemovePacketsTo(User user) {
        for (ExArmorStand stand : this.stands) {
            user.sendPacket(ExPacketPlayOutEntityDestroy.wrap(stand));
        }
    }

    public void sendRemovePacketsTo(Collection<? extends User> users) {
        for (User user : users) {
            this.sendRemovePacketsTo(user);
        }
    }

    public void remove() {
        this.sendRemovePacketsTo(this.watchers);
        this.watchers.clear();
    }

    public Set<User> getWatchers() {
        return watchers;
    }
}
