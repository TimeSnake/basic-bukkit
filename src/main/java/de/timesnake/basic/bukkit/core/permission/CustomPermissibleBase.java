package de.timesnake.basic.bukkit.core.permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;

import java.lang.reflect.Field;


public class CustomPermissibleBase extends PermissibleBase {

    public CustomPermissibleBase(Player op) {
        super(op);
    }

    @Override
    public boolean hasPermission(String inName) {
        if (super.hasPermission("*")) {
            return true;
        }
        if (inName == null) {
            return true;
        }

        if (super.hasPermission(inName)) {
            return true;
        }

        String[] needPerm = inName.split("\\.");
        StringBuilder permSum = new StringBuilder();

        for (String permPart : needPerm) {
            permSum.append(permPart).append(".");
            if (super.hasPermission(permSum + "*")) {
                return true;
            }
        }
        return false;
    }


    public static void inject(Player p) throws Exception {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        Field field = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftHumanEntity").getDeclaredField("perm");
        field.setAccessible(true);
        field.set(p, new CustomPermissibleBase(p));
    }

}
