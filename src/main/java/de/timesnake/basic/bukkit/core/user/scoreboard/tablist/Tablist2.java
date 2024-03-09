/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.user.scoreboard.tablist;

import de.timesnake.basic.bukkit.core.user.scoreboard.ScoreboardPacketManager;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.scoreboard.*;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.packets.core.packet.out.scoreboard.ClientboundSetObjectivePacketBuilder;
import de.timesnake.library.packets.core.packet.out.scoreboard.ClientboundSetPlayerTeamPacketBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Tablist2 extends Tablist implements TablistEntryHelper {

  private static final TablistGroup DEFAULT_DEFAULT_GROUP = new TablistGroup() {
    @Override
    public int getTablistRank() {
      return 80;
    }

    @Override
    public String getTablistName() {
      return null;
    }

    @Override
    public String getTablistPrefix() {
      return null;
    }

    @Override
    public ExTextColor getTablistPrefixChatColor() {
      return null;
    }

    @Override
    public ExTextColor getTablistChatColor() {
      return null;
    }
  };

  private final TablistListEntry tablist = new TablistListEntry() {
    @Override
    public int compareTo(@NotNull TablistEntry o) {
      return 0;
    }
  };

  protected final List<TablistGroupType> groupTypes;
  protected final TablistGroupType colorGroupType;

  protected final HashMap<TablistGroupType, Collection<Consumer<TablistGroupEntry>>> groupDecoratorsByType;
  protected final HashMap<TablistGroupType, Integer> groupGapsByType;
  protected final HashMap<TablistGroupType, TablistGroup> defaultGroupsByType;

  private List<TablistSlot> lastSlots = new ArrayList<>(0);

  public Tablist2(Builder builder, ScoreboardPacketManager packetManager) {
    super(builder.name, builder.type, packetManager, builder.userJoin, builder.userQuit);
    this.groupTypes = builder.groupTypes;
    this.colorGroupType = builder.colorGroupType;
    this.groupDecoratorsByType = builder.groupDecoratorsByType;
    this.groupGapsByType = builder.groupGapsByType;
    this.defaultGroupsByType = builder.defaultGroupsByType;
  }

  @Override
  protected void load(User user) {
    super.load(user);

    int slot = 0;
    for (TablistSlot entry : this.lastSlots) {
      this.sendPacket(user, ClientboundSetPlayerTeamPacketBuilder.ofCreate(String.valueOf(slot),
          Component.nullToEmpty(entry.getPrefix()), ChatFormatting.getByName(entry.getChatColor().toString()),
          this.getNameTagVisibility(user, entry).getPacketTag()));
      this.sendPacket(user, ClientboundSetPlayerTeamPacketBuilder.ofAddPlayer(String.valueOf(slot),
          entry.getPlayer().getPlayer().getName()));

      slot++;
    }

    this.sendPacket(user, ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(
        this.lastSlots.stream().map(p -> p.getPlayer().getMinecraftPlayer()).toList()));

    this.logger.info("Loaded tablist '{}' for user '{}'", this.name, user.getName());
  }

  @Override
  protected void unload(User user) {
    this.packetManager.sendPacket(user, ClientboundSetObjectivePacketBuilder.ofRemove(this.name));
    this.logger.info("Unloaded tablist '{}' for user '{}'", this.name, user.getName());
  }

  @Override
  public boolean addEntry(TablistPlayer value) {
    if (!value.showInTablist()) {
      return false;
    }

    this.tablist.removePlayer(new TablistPlayerEntry(value, this));
    boolean success = this.tablist.addPlayer(value, this, new LinkedList<>(this.groupTypes));

    if (success) {
      this.update();
      this.logger.info("Updated tablist '{}': added '{}'", this.name, value.getTablistName());
    }

    return success;
  }

  @Override
  public boolean removeEntry(TablistPlayer value) {
    boolean success = this.tablist.removePlayer(new TablistPlayerEntry(value, this));

    if (success) {
      this.update();
      this.logger.info("Updated tablist '{}': removed '{}'", this.name, value.getTablistName());
    }

    return success;
  }

  public boolean reloadEntry(TablistPlayer value, boolean addIfNotExists) {
    boolean success = this.tablist.removePlayer(new TablistPlayerEntry(value, this));

    if (success || addIfNotExists) {
      return this.addEntry(value);
    }

    return false;
  }

  public void addGroupDecoration(TablistGroupType groupType, Consumer<TablistGroupEntry> entry) {
    this.groupDecoratorsByType.computeIfAbsent(groupType, k -> new HashSet<>()).add(entry);
  }

  public void clearGroupDecoration(TablistGroupType groupType) {
    this.groupDecoratorsByType.remove(groupType);
  }

  public void setGroupGap(TablistGroupType groupType, int gap) {
    this.groupGapsByType.put(groupType, gap);
  }

  private void update() {
    int size = this.tablist.size(this);

    List<TablistSlot> slots = new ArrayList<>(size);
    this.tablist.collectAsSlots(slots, this);

    Iterator<TablistSlot> lastIt = this.lastSlots.iterator();
    Iterator<TablistSlot> it = slots.iterator();

    Set<TablistPlayer> lastPlayers = lastSlots.stream().map(TablistSlot::getPlayer).collect(Collectors.toSet());
    Set<TablistPlayer> currentPlayers = slots.stream().map(TablistSlot::getPlayer).collect(Collectors.toSet());

    int slot = 0;

    while (lastIt.hasNext() && it.hasNext()) {
      TablistSlot lastEntry = lastIt.next();
      TablistSlot newEntry = it.next();

      if (!lastEntry.equals(newEntry)) {
        Player newPlayer = newEntry.getPlayer().getPlayer();

        int finalSlot = slot;
        this.broadcastPacket(u -> ClientboundSetPlayerTeamPacketBuilder.ofModify(String.valueOf(finalSlot),
            Component.nullToEmpty(newEntry.getPrefix()), ChatFormatting.getByName(newEntry.getChatColor().toString()),
            this.getNameTagVisibility(u, newEntry).getPacketTag()));

        this.broadcastPacket(ClientboundSetPlayerTeamPacketBuilder.ofAddPlayer(String.valueOf(slot),
            newPlayer.getName()));

        this.logger.debug("Update packet for tablist '{}': {} {}", this.name, slot, newPlayer.getName());
      }

      slot++;
    }

    while (lastIt.hasNext()) {
      this.broadcastPacket(ClientboundSetPlayerTeamPacketBuilder.ofRemove(String.valueOf(slot)));
      lastIt.next();
      slot++;
    }

    while (it.hasNext()) {
      TablistSlot entry = it.next();

      int finalSlot = slot;
      this.broadcastPacket(u -> ClientboundSetPlayerTeamPacketBuilder.ofCreate(String.valueOf(finalSlot),
          Component.nullToEmpty(entry.getPrefix()), ChatFormatting.getByName(entry.getChatColor().toString()),
          this.getNameTagVisibility(u, entry).getPacketTag()));
      this.broadcastPacket(ClientboundSetPlayerTeamPacketBuilder.ofAddPlayer(String.valueOf(slot),
          entry.getPlayer().getPlayer().getName()));

      this.logger.debug("Update packet for tablist '{}': {} {}", this.name, slot, entry.getPlayer().getPlayer().getName());
      slot++;
    }

    List<TablistPlayer> toRemove = new ArrayList<>(lastPlayers);
    toRemove.removeAll(currentPlayers);
    this.broadcastPacket(new ClientboundPlayerInfoRemovePacket(toRemove.stream().map(p -> p.getPlayer().getUniqueId()).toList()));

    currentPlayers.removeAll(lastPlayers);
    this.broadcastPacket(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(
        currentPlayers.stream().map(TablistPlayer::getMinecraftPlayer).toList()));

    this.lastSlots = slots;
  }

  private NameTagVisibility getNameTagVisibility(User user, TablistSlot entry) {
    return Objects.requireNonNullElse(user.canSeeNameTagOf(entry.getPlayer()), NameTagVisibility.NEVER);
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
    TablistGroupEntry groupEntry = new TablistGroupEntry(type, group);
    this.groupDecoratorsByType.getOrDefault(type, Set.of()).forEach(c -> c.accept(groupEntry));
    return groupEntry;
  }

  @Override
  public TablistGroup getDefaultGroup(TablistGroupType type) {
    return this.defaultGroupsByType.getOrDefault(type, DEFAULT_DEFAULT_GROUP);
  }

  @Override
  public int getEntryGap(TablistGroupType type) {
    return this.groupGapsByType.getOrDefault(type, 0);
  }

  public static class Builder {

    private final String name;
    private de.timesnake.basic.bukkit.util.user.scoreboard.Tablist.Type type =
        de.timesnake.basic.bukkit.util.user.scoreboard.Tablist.Type.DUMMY;
    private List<TablistGroupType> groupTypes;
    private TablistGroupType colorGroupType;

    private final HashMap<TablistGroupType, Collection<Consumer<TablistGroupEntry>>> groupDecoratorsByType = new HashMap<>();
    private final HashMap<TablistGroupType, Integer> groupGapsByType = new HashMap<>();
    protected final HashMap<TablistGroupType, TablistGroup> defaultGroupsByType = new HashMap<>();

    private TablistUserJoin userJoin = (e, tablist) -> tablist.addEntry(e.getUser());
    private TablistUserQuit userQuit = (e, tablist) -> tablist.removeEntry(e.getUser());

    public Builder(String name) {
      this.name = name;
    }

    public Builder type(de.timesnake.basic.bukkit.util.user.scoreboard.Tablist.Type type) {
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

    public Builder clearGroupDecoration(TablistGroupType groupType) {
      this.groupDecoratorsByType.remove(groupType);
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
