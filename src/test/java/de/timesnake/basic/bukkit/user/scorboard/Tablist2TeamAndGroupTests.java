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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tablist2TeamAndGroupTests {

  private Tablist2.Builder builder;
  private TablistGroup gameGroup0;
  private TablistGroup gameGroup1;
  private TablistGroup groupA;
  private TablistGroup groupB;
  private Tablist2 tablist;

  @BeforeEach
  public void build() {
    List<TablistGroupType> types = new ArrayList<>();
    types.add(TestTablistGroupType.TEST_TEAM);
    types.add(DisplayGroup.TABLIST_TYPE_0);

    TablistGroup spectatorGroup = new TestTablistGroup(80, "spec", null) {
      @Override
      public @NotNull ExTextColor getTablistColor() {
        return ExTextColor.GRAY;
      }
    };

    this.gameGroup0 = new TestTablistGroup(0, "game0", null) {
      @Override
      public @NotNull ExTextColor getTablistColor() {
        return ExTextColor.BLUE;
      }
    };
    this.gameGroup1 = new TestTablistGroup(1, "game1", null) {
      @Override
      public @NotNull ExTextColor getTablistColor() {
        return ExTextColor.RED;
      }
    };

    this.groupA = new TestTablistGroup(0, "a", "a");
    this.groupB = new TestTablistGroup(1, "b", "b");

    this.builder = new Tablist2.Builder(UUID.randomUUID().toString())
        .type(Tablist.Type.BLANK)
        .groupTypes(types)
        .colorGroupType(TestTablistGroupType.TEST_TEAM)
        .addDefaultGroup(TestTablistGroupType.TEST_TEAM, spectatorGroup)
        .setGroupGap(null, 2);

    this.tablist = new Tablist2(builder, new DummyScoreboardPacketManager()) {
      @Override
      public TablistPlayer newGapEntry(String name, String tablistName) {
        return new TestTablistPlayer(name, tablistName);
      }

      @Override
      protected void update() {

      }
    };
  }

  @Test
  public void testAdd1_1_1Entry() {
    tablist.addEntry(newGameTeamPlayer(gameGroup0, groupA, "1"));
    tablist.addEntry(newGameTeamPlayer(gameGroup1, groupB, "2"));
    tablist.addEntry(newSpecPlayer(groupA, "3"));

    Assertions.assertEquals("""
            Tablist{
              entries=[
                TablistGroupEntry{
                  type=test_team,
                  group=game0,
                  entries=[
                    TablistGroupEntry{
                      type=display_group_0,
                      group=a,
                      entries=[
                        TablistPlayerEntry{
                          player=1,
                          prefix=a§r,
                          color=blue
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
                      group=b,
                      entries=[
                        TablistPlayerEntry{
                          player=2,
                          prefix=b§r,
                          color=red
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
                      group=a,
                      entries=[
                        TablistPlayerEntry{
                          player=3,
                          prefix=a§r,
                          color=gray
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
      tablist.addEntry(newGameTeamPlayer(gameGroup0, i % 2 == 0 ? groupA : groupB, "" + i));
    }

    for (int i = 10; i < 15; i++) {
      tablist.addEntry(newGameTeamPlayer(gameGroup1, i % 2 == 0 ? groupA : groupB, "" + i));
    }

    for (int i = 15; i < 20; i++) {
      tablist.addEntry(newSpecPlayer(i % 2 == 0 ? groupA : groupB, "" + i));
    }

    tablist.addEntry(newSpecPlayer(null, "20"));

    Assertions.assertEquals(List.of("0", "2", "4", "6", "8", "1", "3", "5", "7", "9",
        "", "", "10", "12", "14", "11", "13", "", "", "16", "18", "15", "17", "19", "20"), collectSlots());
  }

  @Test
  public void testAdd5_5_2Remove2_2Entries() {
    List<TablistPlayer> players0 = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      TablistPlayer p = newGameTeamPlayer(gameGroup0, i % 2 == 0 ? groupA : groupB, "" + i);
      players0.add(p);
      tablist.addEntry(p);
    }

    List<TablistPlayer> players1 = new ArrayList<>();

    for (int i = 5; i < 10; i++) {
      TablistPlayer p = newGameTeamPlayer(gameGroup1, i % 2 == 0 ? groupA : groupB, "" + i);
      players1.add(p);
      tablist.addEntry(p);
    }

    tablist.addEntry(newSpecPlayer(groupB, "10"));
    tablist.addEntry(newSpecPlayer(groupA, "11"));

    tablist.removeEntry(players0.get(2));
    tablist.removeEntry(players0.get(3));

    tablist.removeEntry(players1.get(0));
    tablist.removeEntry(players1.get(1));

    Assertions.assertEquals(List.of("0", "4", "1", "", "", "8", "7", "9", "", "",
        "11", "10"), collectSlots());
  }

  public TablistPlayer newGameTeamPlayer(TablistGroup team, TablistGroup group, String name) {
    return new TestTablistPlayer(name) {
      @Override
      public TablistGroup getTablistGroup(TablistGroupType type) {
        if (type.equals(TestTablistGroupType.TEST_TEAM)) return team;
        if (type.equals(TestTablistGroupType.DISPLAY_GROUP_0)) return group;
        return null;
      }
    };
  }

  public TablistPlayer newSpecPlayer(TablistGroup group, String name) {
    return new TestTablistPlayer(name) {
      @Override
      public TablistGroup getTablistGroup(TablistGroupType type) {
        if (type.equals(TestTablistGroupType.DISPLAY_GROUP_0)) return group;
        return null;
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
