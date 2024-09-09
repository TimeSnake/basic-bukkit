/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.user.scorboard;

import de.timesnake.basic.bukkit.core.user.scoreboard.tablist.Tablist2;
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

public class Tablist0TeamsTests {

  private Tablist2.Builder builder;
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

      @Override
      protected void update() {

      }
    };
  }

  @Test
  public void testAddOneEntry() {
    tablist.addEntry(new TestTablistPlayer("1"));

    Assertions.assertEquals("""
            Tablist{
              entries=[
                TablistGroupEntry{
                  type=test_team,
                  group=spec,
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
               }
             ]
            }""".replace("\n", "").replace(" ", ""),
        tablist.getTablistEntries().toString().replace(" ", ""));
  }

  @Test
  public void testAdd10Entries() {
    for (int i = 0; i < 10; i++) {
      tablist.addEntry(new TestTablistPlayer("" + i));
    }

    Assertions.assertEquals("""
            Tablist{
              entries=[
                TablistGroupEntry{
                  type=test_team,
                  group=spec,
                  entries=[
                    TablistGroupEntry{
                      type=display_group_0,
                      group=null,
                      entries=[
                        TablistPlayerEntry{
                          player=0,
                          prefix=null,
                          color=white
                        },
                        TablistPlayerEntry{
                          player=1,
                          prefix=null,
                          color=white
                        },
                        TablistPlayerEntry{
                          player=2,
                          prefix=null,
                          color=white
                        },
                        TablistPlayerEntry{
                          player=3,
                          prefix=null,
                          color=white
                        },
                        TablistPlayerEntry{
                          player=4,
                          prefix=null,
                          color=white
                        },
                        TablistPlayerEntry{
                          player=5,
                          prefix=null,
                          color=white
                        },
                        TablistPlayerEntry{
                          player=6,
                          prefix=null,
                          color=white
                        },
                        TablistPlayerEntry{
                          player=7,
                          prefix=null,
                          color=white
                        },
                        TablistPlayerEntry{
                          player=8,
                          prefix=null,
                          color=white
                        },
                        TablistPlayerEntry{
                          player=9,
                          prefix=null,
                          color=white
                        }
                      ]
                    }
                  ]
                }
              ]
            }""".replace("\n", "").replace(" ", ""),
        tablist.getTablistEntries().toString().replace(" ", ""));
  }

  @Test
  public void testAdd10Remove5Entries() {
    List<TestTablistPlayer> players = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      TestTablistPlayer p = new TestTablistPlayer("" + i);
      players.add(p);
      tablist.addEntry(p);
    }

    for (int i = 0; i < 5; i++) {
      tablist.removeEntry(players.get(i));
    }

    Assertions.assertEquals("""
            Tablist{
                  entries=[
                    TablistGroupEntry{
                      type=test_team,
                      group=spec,
                      entries=[
                        TablistGroupEntry{
                          type=display_group_0,
                          group=null,
                          entries=[
                            TablistPlayerEntry{
                              player=5,
                              prefix=null,
                              color=white
                            },
                            TablistPlayerEntry{
                              player=6,
                              prefix=null,
                              color=white
                            },
                            TablistPlayerEntry{
                              player=7,
                              prefix=null,
                              color=white
                            },
                            TablistPlayerEntry{
                              player=8,
                              prefix=null,
                              color=white
                            },
                            TablistPlayerEntry{
                              player=9,
                              prefix=null,
                              color=white
                            }
                          ]
                        }
                      ]
                    }
                  ]
                }""".replace("\n", "").replace(" ", ""),
        tablist.getTablistEntries().toString().replace(" ", ""));
  }
}
