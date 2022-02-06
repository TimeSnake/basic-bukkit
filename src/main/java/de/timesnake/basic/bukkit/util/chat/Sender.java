package de.timesnake.basic.bukkit.util.chat;

import de.timesnake.basic.bukkit.core.chat.ExCommandSender;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.library.basic.util.chat.Plugin;
import org.bukkit.entity.Player;

public class Sender extends de.timesnake.library.basic.util.cmd.Sender {

    public Sender(ExCommandSender cmdSender, Plugin plugin) {
        super(cmdSender, plugin);
    }

    public Sender(Player player, Plugin plugin) {
        super(new ExCommandSender(player), plugin);
    }

    public String getChatName() {
        if (this.isPlayer(false)) {
            return this.getUser().getChatName();
        } else if (this.isConsole(false)) {
            return Plugin.BUKKIT.getName();
        }
        return null;
    }

    public Player getPlayer() {
        return cmdSender.getPlayer();
    }

    public User getUser() {
        return Server.getUser(this.getPlayer());
    }

    @Override
    public void sendConsoleMessage(String message) {
        Server.printText(Plugin.BUKKIT, message);
    }
}
