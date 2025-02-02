/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.chat.cmd;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import de.timesnake.library.chat.Code;
import de.timesnake.library.chat.Code.Type;
import de.timesnake.library.chat.Plugin;
import de.timesnake.library.commands.CommandExitException;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class Argument extends de.timesnake.library.commands.Argument {

  public static final Code WORLD_TYPE_NON = new Code.Builder()
      .setPlugin(Plugin.SERVER)
      .setType(Type.HELP)
      .setDescription("Not a world type")
      .build();
  public static final Code WORLD_ENVIRONMENT_NON = new Code.Builder()
      .setPlugin(Plugin.SERVER)
      .setType(Type.HELP)
      .setDescription("Not a world environment")
      .build();
  public static final Code MATERIAL_NAME_NON = new Code.Builder()
      .setPlugin(Plugin.SERVER)
      .setType(Type.HELP)
      .setDescription("Not a material name")
      .build();

  public Argument(Sender sender, String string) {
    super(sender, string);
  }

  public void assertElseExit(Function<Argument, Boolean> function) {
    super.assertElseExitBasis(function);
  }

  public boolean isPlayerName(boolean sendMessage) {
    Player player = Bukkit.getPlayer(this.string);
    if (player != null) {
      User user = Server.getUser(player);
      return !user.isAirMode();
    }
    return false;
  }

  public boolean isHexColor(boolean sendMessage) {
    try {
      Color.fromRGB(Integer.valueOf(this.string.substring(0, 2), 16),
          Integer.valueOf(this.string.substring(2,
              4), 16), Integer.valueOf(this.string.substring(4, 6), 16));
    } catch (IllegalArgumentException e) {
      if (sendMessage) {
        this.sender.sendMessageNoHexColor(string);
      }
      return false;
    }
    return true;
  }

  public @Nullable Player toPlayer() {
    return Bukkit.getPlayer(string);
  }

  public @Nullable User toUser() {
    return Server.getUser(this.toPlayer());
  }

  public Color toColorFromHex() {
    return Color.fromRGB(Integer.valueOf(this.string.substring(0, 2), 16),
        Integer.valueOf(this.string.substring(2, 4), 16),
        Integer.valueOf(this.string.substring(4, 6), 16));
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

  public boolean isMaterialName(boolean sendMessage) {
    if (Material.getMaterial(this.string.toUpperCase()) != null) {
      return true;
    }

    if (sendMessage) {
      sender.sendMessageNotExist(this.string, MATERIAL_NAME_NON, "Material");
    }
    return false;
  }

  public ExWorld toWorld() {
    return Server.getWorld(this.string);
  }

  public Material toMaterial() {
    return Material.getMaterial(this.string.toUpperCase());
  }

  public Material toMaterialOrExit(boolean sendMessage) {
    Material material = Material.getMaterial(this.string.toUpperCase());

    if (material == null) {
      if (sendMessage) {
        sender.sendMessageNotExist(this.string, MATERIAL_NAME_NON, "Material");
      }
      throw new CommandExitException();
    }

    return material;
  }
}
