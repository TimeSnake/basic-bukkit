/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.util.user.scoreboard;

import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.library.chat.Chat;

import java.util.function.Function;

public interface KeyedSideboard extends Sideboard {

  <V> void updateScore(LineId<V> id, V value);

  <V> void updateScore4User(User user, LineId<V> id, V value);

  abstract class LineId<V> {

    public static final LineId<Integer> PLAYERS = new LineId<>("players", "§9§lPlayers", false) {
      @Override
      public String parseValue(Integer value) {
        return value.toString();
      }
    };
    public static final LineId<String> PLAYERS_OF = new LineId<>("players_of", "§9§lPlayers", false) {
      @Override
      public String parseValue(String value) {
        return value;
      }
    };
    public static final LineId<String> MAP = new LineId<>("map", "§3§lMap", false) {
      @Override
      public String parseValue(String value) {
        return value;
      }
    };
    public static final LineId<String> MAP_INLINE = new LineId<>("map_inline", "§3§lMap: ", true) {
      @Override
      public String parseValue(String value) {
        return value;
      }
    };
    public static final LineId<Integer> TIME = new LineId<>("time", "§6§lTime", false) {
      @Override
      public String parseValue(Integer value) {
        return Chat.getTimeString(value);
      }
    };
    public static final LineId<Integer> KILLS = new LineId<>("kills", "§c§lKills", false) {
      @Override
      public String parseValue(Integer value) {
        return value.toString();
      }
    };
    public static final LineId<Integer> DEATHS = new LineId<>("deaths", "§c§lDeaths", false) {
      @Override
      public String parseValue(Integer value) {
        return value.toString();
      }
    };
    public static final LineId<String> TEAM = new LineId<>("team", "§3§lTeam", false) {
      @Override
      public String parseValue(String value) {
        return value;
      }
    };
    public static final LineId<String> EMPTY = new LineId<>("empty", "", false) {
      @Override
      public String parseValue(String value) {
        return value;
      }
    };

    public static final LineId<String> EMPTY_INLINE = new LineId<>("empty", "", true) {
      @Override
      public String parseValue(String value) {
        return value;
      }
    };

    private final String id;
    private final String displayName;
    private final boolean inline;

    public LineId(String id, String displayName, boolean inline) {
      this.id = id;
      this.displayName = displayName;
      this.inline = inline;
    }

    public String getId() {
      return id;
    }

    public String getDisplayName() {
      return displayName;
    }

    public boolean isInline() {
      return inline;
    }

    public abstract String parseValue(V value);

    public static <V> LineId<V> of(String id, String displayName, boolean inline, Function<V, String> valueParser) {
      return new LineId<>(id, displayName, inline) {

        @Override
        public String parseValue(V value) {
          return valueParser.apply(value);
        }
      };
    }
  }
}
