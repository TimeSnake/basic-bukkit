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
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tablist2TeamTests {

  private Tablist2.Builder builder;
  private TablistGroup gameGroup0;
  private TablistGroup gameGroup1;
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
    };

    this.gameGroup0 = new TablistGroup() {
      @Override
      public int getTablistRank() {
        return 0;
      }

      @Override
      public @NotNull String getTablistName() {
        return "game0";
      }
    };

    this.gameGroup1 = new TablistGroup() {
      @Override
      public int getTablistRank() {
        return 1;
      }

      @Override
      public @NotNull String getTablistName() {
        return "game1";
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
      public TablistPlayer newGapEntry(String rank) {
        return new TestTablistPlayer(rank);
      }
    };
  }

  @Test
  public void testAdd1_1_1Entry() {
    tablist.addEntry(newGameTeamPlayer(gameGroup0, "1"));
    tablist.addEntry(newGameTeamPlayer(gameGroup1, "2"));
    tablist.addEntry(new TestTablistPlayer("3"));

    Assertions.assertEquals("""
            Tablist{
              entries=[
                TablistGroupEntry{
                  type=test_team,
                  group=game0,
                  entries=[
                    TablistGroupEntry{
                      type=display_group_0,
                      group=null,
                      entries=[
                        TablistPlayerEntry{
                          player=1
                        }
                      ]
                   }
                 ]
                },
                TablistGroupEntry{
                  type=test_team,
                  group=game1,
                  entries=[
                    TablistGroupEntry{
                      type=display_group_0,
                      group=null,
                      entries=[
                        TablistPlayerEntry{
                          player=2
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
                          player=3
                        }
                      ]
                   }
                 ]
               }
             ]
            }""".replace("\n", "").replace(" ", ""),
        tablist.getTablistEntries().toString().replace(" ", ""));

    Assertions.assertEquals(List.of("1", "", "", "2", "", "", "3"), collectSlots());
  }

  @Test
  public void testAdd10_5_5Entries() {
    for (int i = 0; i < 10; i++) {
      tablist.addEntry(newGameTeamPlayer(gameGroup0, "" + i));
    }

    for (int i = 10; i < 15; i++) {
      tablist.addEntry(newGameTeamPlayer(gameGroup1, "" + i));
    }

    for (int i = 15; i < 20; i++) {
      tablist.addEntry(new TestTablistPlayer("" + i));
    }

    Assertions.assertEquals(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "", "", "10", "11", "12", "13", "14", "", "", "15", "16", "17", "18", "19"), collectSlots());
  }

  @Test
  public void testAdd5_5_2Remove2_2Entries() {
    List<TablistPlayer> players0 = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      TablistPlayer p = newGameTeamPlayer(gameGroup0, "" + i);
      players0.add(p);
      tablist.addEntry(p);
    }

    List<TablistPlayer> players1 = new ArrayList<>();

    for (int i = 5; i < 10; i++) {
      TablistPlayer p = newGameTeamPlayer(gameGroup1, "" + i);
      players1.add(p);
      tablist.addEntry(p);
    }

    tablist.addEntry(new TestTablistPlayer("10"));
    tablist.addEntry(new TestTablistPlayer("11"));

    tablist.removeEntry(players0.get(2));
    tablist.removeEntry(players0.get(3));

    tablist.removeEntry(players1.get(0));
    tablist.removeEntry(players1.get(1));

    Assertions.assertEquals(List.of("0", "1", "4", "", "", "7", "8", "9", "", "",
        "10", "11"), collectSlots());
  }

  public TablistPlayer newGameTeamPlayer(TablistGroup group, String name) {
    return new TestTablistPlayer(name) {
      @Override
      public TablistGroup getTablistGroup(TablistGroupType type) {
        return type.equals(TestTablistGroupType.TEST_TEAM) ? group : null;
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
