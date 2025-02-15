/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard.tablist;

import de.timesnake.basic.bukkit.util.user.scoreboard.*;
import de.timesnake.library.packets.core.packet.out.scoreboard.ClientboundSetObjectivePacketBuilder;
import de.timesnake.library.packets.core.packet.out.scoreboard.ClientboundSetPlayerTeamPacketBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Tablist2 extends Tablist implements TablistEntryHelper {

  private static final int MIN_INDEX = 1000;
  private static final int MAX_INDEX = 10000; // exclusive

  private static final TablistGroup DEFAULT_DEFAULT_GROUP = new TablistGroup() {
    @Override
    public int getTablistRank() {
      return 80;
    }

    @Override
    public @Nullable String getTablistName() {
      return null;
    }
  };

  private final TablistListEntry tablist;

  protected final List<TablistGroupType> groupTypes;
  protected final TablistGroupType colorGroupType;

  protected final HashMap<TablistGroupType, Collection<Consumer<TablistGroupEntry>>> groupDecoratorsByType;
  protected final HashMap<TablistGroupType, Integer> groupGapsByType;
  protected final HashMap<TablistGroupType, TablistGroup> defaultGroupsByType;

  private List<TablistSlot> lastBroadcastSlots = new ArrayList<>(0);


  public Tablist2(Builder builder, ScoreboardPacketManager packetManager) {
    super(builder.name, builder.type, packetManager, builder.userJoin, builder.userQuit);
    this.groupTypes = builder.groupTypes;
    this.colorGroupType = builder.colorGroupType;
    this.groupDecoratorsByType = builder.groupDecoratorsByType;
    this.groupGapsByType = builder.groupGapsByType;
    this.defaultGroupsByType = builder.defaultGroupsByType;

    this.tablist = new TablistListEntry(this.getEntryGapSize(null)) {
      @Override
      public String toString() {
        return "Tablist{" +
               "entries=[" + entries.values().stream().map(TablistEntry::toString).collect(Collectors.joining(", ")) +
               "]" +
               '}';
      }

      @Override
      public int compareTo(@NotNull TablistEntry o) {
        return 0;
      }
    };
  }

  @Override
  protected void load(ScoreboardViewer viewer) {
    super.load(viewer);

    List<ClientboundPlayerInfoUpdatePacket.Entry> players = new ArrayList<>();

    for (TablistSlot slot : this.lastBroadcastSlots) {
      TablistPlayer player = slot.getPlayer();

      this.sendPacket(viewer, ClientboundSetPlayerTeamPacketBuilder.ofCreate("" + slot.getIndex(),
          Component.nullToEmpty(slot.getPrefix()),
          ChatFormatting.getByName(slot.getChatColor().toString()),
          this.getNameTagVisibility(viewer, slot).getPacketTag(),
          List.of(player.getName())));

      ServerPlayer mcPlayer = player.getMinecraftPlayer();

      if (mcPlayer != null) {
        players.add(new ClientboundPlayerInfoUpdatePacket.Entry(mcPlayer.getUUID(),
            mcPlayer.gameProfile,
            true, mcPlayer.connection != null ? mcPlayer.connection.latency() : 0,
            mcPlayer.gameMode.getGameModeForPlayer(),
            mcPlayer.getTabListDisplayName(),
            mcPlayer.getChatSession() != null ? mcPlayer.getChatSession().asData() : null));
      }

    }

    this.sendPacket(viewer, new ClientboundPlayerInfoUpdatePacket(
        EnumSet.of(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER,
            ClientboundPlayerInfoUpdatePacket.Action.INITIALIZE_CHAT,
            ClientboundPlayerInfoUpdatePacket.Action.UPDATE_GAME_MODE,
            ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED,
            ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY,
            ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME),
        players));


    this.logger.info("Loaded tablist '{}' for user '{}'", this.name, viewer.getName());
  }

  @Override
  protected void unload(ScoreboardViewer viewer) {
    this.packetManager.sendPacket(viewer, ClientboundSetObjectivePacketBuilder.ofRemove(this.name));
    this.logger.info("Unloaded tablist '{}' for user '{}'", this.name, viewer.getName());
  }

  @Override
  public boolean addEntry(TablistPlayer value) {
    if (!value.showInTablist()) {
      return false;
    }

    this.tablist.removePlayer(value);
    boolean success = this.tablist.addPlayer(value, this, new LinkedList<>(this.groupTypes));

    if (success) {
      this.update();
      this.logger.info("Tablist '{}': added '{}'", this.name, value.getTablistName());
    }
    return success;
  }

  @Override
  public boolean removeEntry(TablistPlayer value) {
    boolean success = this.tablist.removePlayer(value);

    if (success) {
      this.update();
      this.logger.info("Tablist '{}': removed '{}'", this.name, value.getTablistName());
    }
    return success;
  }

  @Override
  public boolean reloadEntry(TablistPlayer value, boolean addIfNotExists) {
    if (this.tablist.removePlayer(value) || addIfNotExists) {
      return this.addEntry(value);
    }
    return false;
  }

  protected void update() {
    List<TablistSlot> slots = new ArrayList<>();
    this.tablist.collectAsSlots(slots, this);

    try {
      this.recomputeSlotIndizes(slots);
    } catch (SlotIndexSpaceExhaustedException e) {
      this.logger.info("Tablist '{}': index space exhausted, re-assigning indizes", this.name);
      this.assignSlotIndizes(slots, MIN_INDEX, MAX_INDEX);
    }

    if (this.viewers.isEmpty()) {
      slots.forEach(TablistSlot::unmarkAsDirty);
      this.lastBroadcastSlots = slots;
      return;
    }

    Set<Integer> indizesToRemove = new HashSet<>();
    Set<UUID> playersToRemove = new HashSet<>();

    for (TablistSlot slot : this.lastBroadcastSlots) {
      indizesToRemove.add(slot.getIndex());
      playersToRemove.add(slot.getPlayer().getUniqueId());
    }

    List<ClientboundPlayerInfoUpdatePacket.Entry> dirtyPlayers = new ArrayList<>();

    for (TablistSlot slot : slots) {
      int index = slot.getIndex();
      TablistPlayer player = slot.getPlayer();

      playersToRemove.remove(player.getUniqueId());
      boolean knownIndex = indizesToRemove.remove(index);

      if (!slot.isDirty()) {
        continue;
      }

      slot.unmarkAsDirty();

      ServerPlayer mcPlayer = player.getMinecraftPlayer();

      if (mcPlayer != null) {
        dirtyPlayers.add(new ClientboundPlayerInfoUpdatePacket.Entry(mcPlayer.getUUID(),
            mcPlayer.gameProfile,
            true, mcPlayer.connection != null ? mcPlayer.connection.latency() : 0,
            mcPlayer.gameMode.getGameModeForPlayer(),
            mcPlayer.getTabListDisplayName(),
            mcPlayer.getChatSession() != null ? mcPlayer.getChatSession().asData() : null));
      }

      if (knownIndex) {
        this.broadcastPacket(u -> ClientboundSetPlayerTeamPacketBuilder.ofModify("" + index,
            Component.nullToEmpty(slot.getPrefix()),
            ChatFormatting.getByName(slot.getChatColor().toString()),
            this.getNameTagVisibility(u, slot).getPacketTag()));
        this.broadcastPacket(ClientboundSetPlayerTeamPacketBuilder.ofAddPlayer("" + index, player.getName()));
        this.logger.info("Team update/add player packet for tablist '{}': {}, player {}", this.name, index,
            player.getName());
      } else {
        this.broadcastPacket(u -> ClientboundSetPlayerTeamPacketBuilder.ofCreate("" + index,
            Component.nullToEmpty(slot.getPrefix()),
            ChatFormatting.getByName(slot.getChatColor().toString()),
            this.getNameTagVisibility(u, slot).getPacketTag(),
            List.of(player.getName())));
        this.logger.info("Team creation packet for tablist '{}': {}, player: {}", this.name, index, player.getName());
      }
    }

    for (Integer index : indizesToRemove) {
      this.broadcastPacket(ClientboundSetPlayerTeamPacketBuilder.ofRemove("" + index));
      this.logger.info("Team delete packet for tablist '{}': {}", this.name, index);
    }

    if (!dirtyPlayers.isEmpty()) {
      this.broadcastPacket(new ClientboundPlayerInfoUpdatePacket(
          EnumSet.of(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER,
              ClientboundPlayerInfoUpdatePacket.Action.INITIALIZE_CHAT,
              ClientboundPlayerInfoUpdatePacket.Action.UPDATE_GAME_MODE,
              ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED,
              ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY,
              ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME),
          dirtyPlayers));
      this.logger.info("Player update packet for tablist '{}': {}", this.name, dirtyPlayers.stream()
          .map(p -> p.profileId().toString()).collect(Collectors.joining(", ")));
    }

    if (!playersToRemove.isEmpty()) {
      this.broadcastPacket(new ClientboundPlayerInfoRemovePacket(new ArrayList<>(playersToRemove)));
      this.logger.info("Players delete packet for tablist '{}': {}", this.name, playersToRemove.stream()
          .map(UUID::toString).collect(Collectors.joining(", ")));
    }

    this.lastBroadcastSlots = slots;
  }

  protected void assignSlotIndizes(List<TablistSlot> slots, int startIndex, int endIndex) {
    int diff = (endIndex - startIndex) / (slots.size() + 2);

    if (diff <= 0) {
      throw new SlotIndexSpaceExhaustedException();
    }

    TablistSlot prev = null;

    int index = startIndex + diff;
    for (TablistSlot slot : slots) {
      if (slot.canHaveSameIndexAsNeighbor() && prev != null && prev.canHaveSameIndexAsNeighbor()) {
        slot.setIndex(prev.getIndex() + 1);
      } else {
        slot.setIndex(index);
        index += diff;
      }
      prev = slot;
    }
  }

  protected void recomputeSlotIndizes(List<TablistSlot> slots) {
    Iterator<TablistSlot> iterator = slots.iterator();

    if (slots.isEmpty()) {
      return;
    }

    TablistSlot current;
    TablistSlot prev = null;

    while (iterator.hasNext()) {
      current = iterator.next();

      if (!current.hasIndex()) {
        if (prev != null && prev.canHaveSameIndexAsNeighbor() && current.canHaveSameIndexAsNeighbor()) {
          current.setIndex(prev.getIndex() + 1);
        } else {
          List<TablistSlot> notIndexedSlots = new ArrayList<>();
          notIndexedSlots.addLast(current);

          while (iterator.hasNext()) {
            current = iterator.next();
            if (current.hasIndex()) {
              break;
            }
            notIndexedSlots.addLast(current);
          }

          this.assignSlotIndizes(notIndexedSlots,
              prev != null ? prev.getIndex() : MIN_INDEX,
              current.hasIndex() ? current.getIndex() : MAX_INDEX);
        }
      }

      prev = current;
    }
  }

  private NameTagVisibility getNameTagVisibility(ScoreboardViewer viewer, TablistSlot entry) {
    return Objects.requireNonNullElse(viewer.canSeeNameTagOf(entry.getPlayer()), NameTagVisibility.NEVER);
  }

  public TablistListEntry getTablistEntries() {
    return tablist;
  }

  @Override
  public List<TablistGroupType> getGroupTypes() {
    return this.groupTypes;
  }

  @Override
  public TablistGroupType getColorGroupType() {
    return this.colorGroupType;
  }

  @Override
  public TablistEntry createGroup(TablistGroupType type, TablistGroup group) {
    TablistGroupEntry groupEntry = new TablistGroupEntry(this.getEntryGapSize(type), type, group);
    this.groupDecoratorsByType.getOrDefault(type, Set.of()).forEach(c -> c.accept(groupEntry));
    return groupEntry;
  }

  @Override
  public @NotNull TablistGroup getDefaultGroup(TablistGroupType type) {
    return this.defaultGroupsByType.getOrDefault(type, DEFAULT_DEFAULT_GROUP);
  }

  @Override
  public int getEntryGapSize(TablistGroupType type) {
    return this.groupGapsByType.getOrDefault(type, 0);
  }

  private static class SlotIndexSpaceExhaustedException extends RuntimeException {

  }

  public static class Builder {

    private final String name;
    private Type type = Type.BLANK;
    private List<TablistGroupType> groupTypes;
    private TablistGroupType colorGroupType;

    private final HashMap<TablistGroupType, Collection<Consumer<TablistGroupEntry>>> groupDecoratorsByType =
        new HashMap<>();
    private final HashMap<TablistGroupType, Integer> groupGapsByType = new HashMap<>();
    protected final HashMap<TablistGroupType, TablistGroup> defaultGroupsByType = new HashMap<>();

    private TablistUserJoin userJoin = (e, tablist) -> tablist.addEntry(e.getUser());
    private TablistUserQuit userQuit = (e, tablist) -> tablist.removeEntry(e.getUser());

    public Builder(String name) {
      this.name = name;
    }

    public Builder type(Type type) {
      this.type = type;
      return this;
    }

    public Builder groupTypes(List<TablistGroupType> groupTypes) {
      this.groupTypes = groupTypes;
      return this;
    }

    public Builder colorGroupType(TablistGroupType colorGroupType) {
      this.colorGroupType = colorGroupType;
      return this;
    }

    public Builder addGroupDecoration(TablistGroupType groupType, Consumer<TablistGroupEntry> entry) {
      this.groupDecoratorsByType.computeIfAbsent(groupType, k -> new HashSet<>()).add(entry);
      return this;
    }

    public Builder addDefaultGroup(TablistGroupType type, TablistGroup group) {
      this.defaultGroupsByType.put(type, group);
      return this;
    }

    public Builder setGroupGap(TablistGroupType groupType, int gap) {
      this.groupGapsByType.put(groupType, gap);
      return this;
    }

    public Builder userJoin(TablistUserJoin userJoin) {
      this.userJoin = userJoin;
      return this;
    }

    public Builder userQuit(TablistUserQuit userQuit) {
      this.userQuit = userQuit;
      return this;
    }
  }
}
