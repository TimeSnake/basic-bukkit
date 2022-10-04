package de.timesnake.basic.bukkit.util.chat;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import de.timesnake.library.extension.util.chat.Code;
import org.bukkit.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.Player;

public class Argument extends de.timesnake.library.extension.util.cmd.Argument {

    public static final Code.Help WORLD_TYPE_NON = new Code.Help(Plugin.SYSTEM, "nwt", 1, "Not a world type");
    public static final Code.Help WORLD_ENVIRONMENT_NON = new Code.Help(Plugin.SYSTEM, "nwe", 1, "Not a world environment");

    public Argument(Sender sender, String string) {
        super(sender, string);
    }

    public boolean isPlayerName(boolean sendMessage) {
        Player player = Bukkit.getPlayer(this.string);
        if (player != null) {
            User user = Server.getUser(player);
            return !user.isAirMode();
        }
        return false;
    }

    public boolean isChatColor(boolean sendMessage) {
        try {
            ChatColor.valueOf(string.toUpperCase());
        } catch (IllegalArgumentException e) {
            if (sendMessage) {
                this.sender.sendMessageNoChatColor(string);
            }
            return false;
        }
        return true;
    }

    public boolean isHexColor(boolean sendMessage) {
        try {
            Color.fromRGB(Integer.valueOf(this.string.substring(0, 2), 16), Integer.valueOf(this.string.substring(2,
                    4), 16), Integer.valueOf(this.string.substring(4, 6), 16));
        } catch (IllegalArgumentException e) {
            if (sendMessage) {
                this.sender.sendMessageNoHexColor(string);
            }
            return false;
        }
        return true;
    }

    public Player toPlayer() {
        return Bukkit.getPlayer(string);
    }

    public User toUser() {
        return Server.getUser(this.toPlayer());
    }

    public ChatColor toChatColor() {
        return ChatColor.valueOf(string.toUpperCase());
    }

    public Color toColorFromHex() {
        return Color.fromRGB(Integer.valueOf(this.string.substring(0, 2), 16),
                Integer.valueOf(this.string.substring(2, 4), 16), Integer.valueOf(this.string.substring(4, 6), 16));
    }

    public boolean isWorldName(boolean sendMessage) {
        if (Bukkit.getWorld(this.string) != null) {
            return true;
        } else if (sendMessage) {
            sender.sendMessageWorldNotExist(string);
        }
        return false;
    }

    public boolean isWorldType(boolean sendMessage) {
        if (WorldType.getByName(this.string) != null) {
            return true;
        } else if (sendMessage) {
            sender.sendMessageNotExist(this.string, WORLD_TYPE_NON, "World-type");
        }
        return false;
    }

    public boolean isWorldEnvironment(boolean sendMessage) {
        try {
            World.Environment.valueOf(this.string.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            if (sendMessage) {
                sender.sendMessageNotExist(this.string, WORLD_ENVIRONMENT_NON, "World-environment");
            }
        }
        return false;
    }

    public ExWorld toWorld() {
        return Server.getWorld(this.string);
    }
}
