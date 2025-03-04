/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.chat.cmd;

import de.timesnake.basic.bukkit.core.world.DelegatedWorld;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.database.util.Database;
import de.timesnake.database.util.game.DbGame;
import de.timesnake.library.chat.Code;
import de.timesnake.library.commands.Completion;
import de.timesnake.library.commands.extended.ExArguments;
import de.timesnake.library.permissions.PermGroup;

import java.util.Collection;
import java.util.List;

public class ExCompletion extends Completion<ExCompletion, Sender, Argument, ExArguments<Argument>> {

  public static ExCompletion ofPlayerNames() {
    return new ExCompletion(Server.getUsers().stream().filter(u -> !u.isAirMode()).map(User::getName).toList());
  }

  public static ExCompletion ofGameNames() {
    return new ExCompletion(Database.getGames().getGamesName());
  }

  public static ExCompletion ofPermGroupNames() {
    return new ExCompletion(Server.getPermGroups().stream().map(PermGroup::getName).toList());
  }

  public static ExCompletion ofWorldNames() {
    return new ExCompletion(Server.getWorlds().stream().map(DelegatedWorld::getName).toList());
  }

  public static ExCompletion ofMapNames(String gameName) {
    if (gameName == null) {
      return new ExCompletion();
    }

    DbGame game = Database.getGames().getGame(gameName);
    if (game == null) {
      return new ExCompletion();
    }
    return new ExCompletion(game.getMapNames());
  }

  public ExCompletion() {
  }

  public ExCompletion(Code permission) {
    super(permission);
  }

  public ExCompletion(Collection<String> values) {
    super(values);
  }

  public ExCompletion(String... values) {
    super(List.of(values));
  }

  public ExCompletion(CompleteSupplier<Sender, Argument, ExArguments<Argument>, Collection<String>> valuesProvider) {
    super(valuesProvider);
  }

  public ExCompletion(Code permission, Collection<String> values) {
    super(permission, values);
  }

  public ExCompletion(Code permission, String... values) {
    super(permission, List.of(values));
  }

  public ExCompletion(Code permission,
                      CompleteSupplier<Sender, Argument, ExArguments<Argument>, Collection<String>> valuesProvider) {
    super(permission, valuesProvider);
  }
}
