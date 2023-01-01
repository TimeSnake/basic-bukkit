/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.chat;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.library.extension.util.cmd.CommandSender;
import net.kyori.adventure.text.Component;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ExCommandSender implements CommandSender {

    private final org.bukkit.command.CommandSender cmdSender;

    public ExCommandSender(org.bukkit.command.CommandSender cmdSender) {
        this.cmdSender = cmdSender;
    }

    @Override
    public void sendMessage(String s) {
        cmdSender.sendMessage(s);
    }

    @Override
    public void sendMessage(Component s) {
        this.cmdSender.sendMessage(s);
    }

    @Override
    public void sendMessage(String[] strings) {
        cmdSender.sendMessage(strings);
    }

    @Override
    public void sendMessage(Component[] components) {
        for (Component component : components) {
            this.cmdSender.sendMessage(component);
        }
    }

    @Override
    public String getName() {
        return cmdSender.getName();
    }

    @Override
    public boolean hasPermission(String s) {
        return cmdSender.hasPermission(s);
    }

    @Override
    public boolean isConsole() {
        return cmdSender instanceof ConsoleCommandSender;
    }

    @Override
    public User getUser() {
        if (cmdSender instanceof Player) {
            return Server.getUser((Player) cmdSender);
        }
        return null;
    }

    public Player getPlayer() {
        return (Player) cmdSender;
    }
}
