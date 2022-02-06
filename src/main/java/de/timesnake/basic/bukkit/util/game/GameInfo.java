package de.timesnake.basic.bukkit.util.game;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.database.util.game.DbGame;
import de.timesnake.database.util.object.Type;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameInfo {

    protected String name;
    protected DbGame database;
    protected String displayName;
    protected ChatColor chatColor;
    protected Integer autoStart;
    protected Integer minPlayers;
    protected Integer maxPlayers;
    protected String headLine;
    protected ItemStack item;
    protected Integer slot;
    protected boolean isTemporary;
    protected Collection<Integer> teamSizes;
    protected Type.Availability mapAvailability;
    protected Type.Availability kitAvailability;
    protected List<String> description;

    public GameInfo(DbGame game) {
        ChatColor chatColor;
        this.database = game;

        game = game.toLocal();

        this.name = game.getName();
        this.displayName = game.getDisplayName();

        String chatColorName = game.getChatColorName();
        if (chatColorName != null) {
            try {
                chatColor = ChatColor.valueOf(chatColorName.toUpperCase());
            } catch (IllegalArgumentException e) {
                Server.printError(Plugin.BUKKIT, "Error while loading game " + this.name + " " + Server.getChat().getMessageCode("E", 810, Plugin.BUKKIT), "Game");
                chatColor = ChatColor.WHITE;
            }
        } else {
            chatColor = ChatColor.WHITE;
        }

        this.chatColor = chatColor;
        this.autoStart = game.getAutoStart();
        this.minPlayers = game.getMinPlayers();
        this.maxPlayers = game.getMaxPlayers();
        this.headLine = game.getHeadLine();

        this.slot = game.getSlot();
        this.isTemporary = game.isTemporary();
        this.teamSizes = game.getTeamAmounts();

        String materialName = game.getItemName().toUpperCase();

        try {
            Material.getMaterial(materialName);
        } catch (IllegalArgumentException e) {
            Server.printError(Plugin.BUKKIT, "Error while loading item for game " + this.getName(), "Game");
        }
        Material material = Material.getMaterial(materialName);
        if (material != null) {
            this.item = new ItemStack(material);
            ItemMeta meta = this.item.getItemMeta();

            //displayName
            String prefix = game.getDisplayName();
            if (prefix != null) {
                meta.setDisplayName(ChatColor.valueOf(chatColorName) + prefix);
            } else {
                Server.printError(Plugin.BUKKIT, "Error while loading game " + this.name + " " + Server.getChat().getMessageCode("E", 809, Plugin.BUKKIT), "Game");
                return;
            }

            //description
            String description = game.getHeadLine();
            if (description != null) {
                List<String> lore = new ArrayList<>();
                lore.add("");
                for (String lorePart : description.split("\n")) {
                    lore.add("§f" + lorePart);
                }
                meta.setLore(lore);
            } else {
                Server.printError(Plugin.BUKKIT, "Error while loading game " + this.name + " " + Server.getChat().getMessageCode("E", 811, Plugin.BUKKIT), "Game");
                return;
            }

            this.item.setItemMeta(meta);
            //slot
            Integer slot = game.getSlot();
            if (slot != null) {
                this.slot = slot;
            } else {
                Server.printError(Plugin.BUKKIT, "Error while loading game " + this.name + " " + Server.getChat().getMessageCode("E", 812, Plugin.BUKKIT), "Game");
                return;
            }
        } else {
            Server.printError(Plugin.BUKKIT, "Error while loading game " + this.name + " " + Server.getChat().getMessageCode("E", 808, Plugin.BUKKIT), "Game");
            return;
        }

        this.mapAvailability = game.getMapAvailability();
        this.kitAvailability = game.getKitAvailability();

        this.description = game.getDescription();

    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public Integer getAutoStart() {
        return autoStart;
    }

    public Integer getMinPlayers() {
        return minPlayers;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public String getHeadLine() {
        return headLine;
    }

    public ItemStack getItem() {
        return item;
    }

    public Integer getSlot() {
        return slot;
    }

    public boolean isTemporary() {
        return isTemporary;
    }

    public DbGame getDatabase() {
        return database;
    }

    public Collection<Integer> getTeamSizes() {
        return teamSizes;
    }

    public Type.Availability getMapAvailability() {
        return mapAvailability;
    }

    public Type.Availability getKitAvailability() {
        return kitAvailability;
    }

    public List<String> getDescription() {
        return description;
    }
}
