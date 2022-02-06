package de.timesnake.basic.bukkit.util.chat;

public interface ChatColor {

    org.bukkit.ChatColor PUBLIC = org.bukkit.ChatColor.WHITE;
    org.bukkit.ChatColor PERSONAL = org.bukkit.ChatColor.YELLOW;
    org.bukkit.ChatColor VALUE = org.bukkit.ChatColor.GRAY;
    org.bukkit.ChatColor WARNING = org.bukkit.ChatColor.RED;
    org.bukkit.ChatColor QUICK_INFO = org.bukkit.ChatColor.BLACK;

    org.bukkit.ChatColor BLACK = org.bukkit.ChatColor.BLACK;
    org.bukkit.ChatColor DARK_BLUE = org.bukkit.ChatColor.DARK_BLUE;
    org.bukkit.ChatColor DARK_GREEN = org.bukkit.ChatColor.DARK_GREEN;
    org.bukkit.ChatColor DARK_AQUA = org.bukkit.ChatColor.DARK_AQUA;
    org.bukkit.ChatColor DARK_RED = org.bukkit.ChatColor.DARK_RED;
    org.bukkit.ChatColor DARK_PURPLE = org.bukkit.ChatColor.DARK_PURPLE;
    org.bukkit.ChatColor GOLD = org.bukkit.ChatColor.GOLD;
    org.bukkit.ChatColor GRAY = org.bukkit.ChatColor.GRAY;
    org.bukkit.ChatColor DARK_GRAY = org.bukkit.ChatColor.DARK_GRAY;
    org.bukkit.ChatColor BLUE = org.bukkit.ChatColor.BLUE;
    org.bukkit.ChatColor GREEN = org.bukkit.ChatColor.GREEN;
    org.bukkit.ChatColor AQUA = org.bukkit.ChatColor.AQUA;
    org.bukkit.ChatColor RED = org.bukkit.ChatColor.RED;
    org.bukkit.ChatColor LIGHT_PURPLE = org.bukkit.ChatColor.LIGHT_PURPLE;
    org.bukkit.ChatColor YELLOW = org.bukkit.ChatColor.YELLOW;
    org.bukkit.ChatColor WHITE = org.bukkit.ChatColor.WHITE;
    org.bukkit.ChatColor MAGIC = org.bukkit.ChatColor.MAGIC;
    org.bukkit.ChatColor BOLD = org.bukkit.ChatColor.BOLD;
    org.bukkit.ChatColor STRIKETHROUGH = org.bukkit.ChatColor.STRIKETHROUGH;
    org.bukkit.ChatColor UNDERLINE = org.bukkit.ChatColor.UNDERLINE;
    org.bukkit.ChatColor ITALIC = org.bukkit.ChatColor.ITALIC;
    org.bukkit.ChatColor RESET = org.bukkit.ChatColor.RESET;

    static String translateAlternateColorCodes(char c, String prefix) {
        return org.bukkit.ChatColor.translateAlternateColorCodes(c, prefix);
    }
}
