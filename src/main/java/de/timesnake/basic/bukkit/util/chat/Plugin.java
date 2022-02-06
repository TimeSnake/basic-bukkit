package de.timesnake.basic.bukkit.util.chat;

public class Plugin extends de.timesnake.library.basic.util.chat.Plugin {

    public static final Plugin WORLDS = new Plugin("Worlds", "BSW");
    public static final Plugin PACKETS = new Plugin("Packets", "BPS");

    protected Plugin(String name, String code) {
        super(name, code);
    }
}
