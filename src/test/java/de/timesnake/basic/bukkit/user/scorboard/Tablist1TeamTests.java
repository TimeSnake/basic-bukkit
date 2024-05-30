/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.user.scorboard;

import de.timesnake.basic.bukkit.core.user.scoreboard.tablist.Tablist2;
import de.timesnake.basic.bukkit.core.user.scoreboard.tablist.TablistSlot;
import de.timesnake.basic.bukkit.util.group.DisplayGroup;
import de.timesnake.basic.bukkit.util.user.scoreboard.Tablist;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroup;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistGroupType;
import de.timesnake.basic.bukkit.util.user.scoreboard.TablistPlayer;
import de.timesnake.library.chat.ExTextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tablist1TeamTests {

  private Tablist2.Builder builder;
  private TablistGroup gameGroup;
  private Tablist2 tablist;

  @BeforeEach
  public void build() {
    List<TablistGroupType> types = new ArrayList<>();
    types.add(TestTablistGroupType.TEST_TEAM);
    types.add(DisplayGroup.TABLIST_TYPE_0);

    TablistGroup spectatorGroup = new TablistGroup() {
      @Override
      public int getTablistRank() {
        return 80;
      }

      @Override
      public @NotNull String getTablistName() {
        return "spec";
      }

      @Nullable
      @Override
      public ExTextColor getTablistColor() {
        return ExTextColor.GRAY;
      }
    };

    this.gameGroup = new TablistGroup() {
      @Override
      public int getTablistRank() {
        return 0;
      }

      @Override
      public @NotNull String getTablistName() {
        return "game";
      }
    };

    this.builder = new Tablist2.Builder(UUID.randomUUID().toString())
        .type(Tablist.Type.DUMMY)
        .groupTypes(types)
        .colorGroupType(TestTablistGroupType.TEST_TEAM)
        .addDefaultGroup(TestTablistGroupType.TEST_TEAM, spectatorGroup)
        .setGroupGap(null, 2);

    this.tablist = new Tablist2(builder, new DummyScoreboardPacketManager()) {
      @Override
      public TablistPlayer newGapEntry(String name, String tablistName) {
        return new TestTablistPlayer(name, tablistName);
      }
    };
  }

  @Test
  public void testAdd1_1Entry() {
    tablist.addEntry(newGameTeamPlayer("1"));
    tablist.addEntry(new TestTablistPlayer("2"));

    Assertions.assertEquals("""
            Tablist{
              entries=[
                TablistGroupEntry{
                  type=test_team,
                  group=game,
                  entries=[
                    TablistGroupEntry{
                      type=display_group_0,
                      group=null,
                      entries=[
                        TablistPlayerEntry{
                          player=1,
                          prefix=null,
                          color=white
                        }
                      ]
                   }
                 ]
                },
                TablistGroupEntry{
                  type=test_team,
                  group=spec,
                  entries=[
                    TablistGroupEntry{
                      type=display_group_0,
                      group=null,
                      entries=[
                        TablistPlayerEntry{
                          player=2,
                          prefix=null,
                          color=gray
                        }
                      ]
                   }
                 ]
               }
             ]
            }""".replace("\n", "").replace(" ", ""),
        tablist.getTablistEntries().toString().replace(" ", ""));

    Assertions.assertEquals(List.of("1", "", "", "2"), collectSlots());
  }

  @Test
  public void testAdd10_5Entries() {
    for (int i = 0; i < 10; i++) {
      tablist.addEntry(newGameTeamPlayer("" + i));
    }

    for (int i = 10; i < 15; i++) {
      tablist.addEntry(new TestTablistPlayer("" + i));
    }

    Assertions.assertEquals(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "", "", "10", "11", "12", "13", "14"), collectSlots());
  }

  @Test
  public void testAdd5_2Remove2Entries() {
    List<TablistPlayer> players = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      TablistPlayer p = newGameTeamPlayer("" + i);
      players.add(p);
      tablist.addEntry(p);
    }

    tablist.addEntry(new TestTablistPlayer("5"));
    tablist.addEntry(new TestTablistPlayer("6"));

    tablist.removeEntry(players.get(2));
    tablist.removeEntry(players.get(3));

    Assertions.assertEquals(List.of("0", "1", "4", "", "", "5", "6"), collectSlots());
  }

  public TablistPlayer newGameTeamPlayer(String name) {
    return new TestTablistPlayer(name) {
      @Override
      public TablistGroup getTablistGroup(TablistGroupType type) {
        return type.equals(TestTablistGroupType.TEST_TEAM) ? gameGroup : null;
      }
    };
  }

  public List<String> collectSlots() {
    int size = this.tablist.getTablistEntries().size(tablist);

    List<TablistSlot> slots = new ArrayList<>(size);

    tablist.getTablistEntries().collectAsSlots(slots, tablist);

    return slots.stream().map(s -> s.getPlayer().getTablistName().replaceAll("§.", "")).toList();
  }
}
