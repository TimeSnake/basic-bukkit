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
import de.timesnake.library.commands.simple.Arguments;
import de.timesnake.library.permissions.PermGroup;

import java.util.Collection;
import java.util.List;

public class Completion extends de.timesnake.library.commands.Completion<Completion, Sender, Argument,
    Arguments<Argument>> {

  public static Completion empty() {
    return new Completion(null, List.of());
  }

  public static Completion ofPlayerNames() {
    return new Completion(Server.getUsers().stream().filter(u -> !u.isAirMode()).map(User::getName).toList());
  }

  public static Completion ofGameNames() {
    return new Completion(Database.getGames().getGamesName());
  }

  public static Completion ofPermGroupNames() {
    return new Completion(Server.getPermGroups().stream().map(PermGroup::getName).toList());
  }

  public static Completion ofWorldNames() {
    return new Completion(Server.getWorlds().stream().map(DelegatedWorld::getName).toList());
  }

  public static Completion ofMapNames(String gameName) {
    if (gameName == null) {
      return new Completion();
    }

    DbGame game = Database.getGames().getGame(gameName);
    if (game == null) {
      return new Completion();
    }

    return new Completion(game.getMapNames());
  }

  public Completion() {
    super();
  }

  public Completion(Code permission) {
    super(permission);
  }

  public Completion(Collection<String> values) {
    super(values);
  }

  public Completion(CmdFunction<Sender, Argument, Arguments<Argument>, Collection<String>> valuesProvider) {
    super(valuesProvider);
  }

  public Completion(String... values) {
    this(List.of(values));
  }

  public Completion(Code permission, String... values) {
    this(permission, List.of(values));
  }

  public Completion(Code permission, Collection<String> values) {
    super(permission, values);
  }

  public Completion(Code permission,
                    CmdFunction<Sender, Argument, Arguments<Argument>, Collection<String>> valuesProvider) {
    super(permission, valuesProvider);
  }

}
