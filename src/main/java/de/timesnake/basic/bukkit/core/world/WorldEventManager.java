/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.world;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.*;
import de.timesnake.basic.bukkit.util.world.ExBlock;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import de.timesnake.basic.bukkit.util.world.ExWorldOption;
import de.timesnake.library.basic.util.RandomList;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Candle;
import org.bukkit.block.data.type.Fire;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class WorldEventManager implements Listener {

  private final Logger logger = LogManager.getLogger("world.event-manager");

  private final WorldManager worldManager;

  public WorldEventManager(WorldManager worldManager) {
    this.worldManager = worldManager;

    Server.registerListener(this, BasicBukkit.getPlugin());
  }

  @EventHandler
  public void onCraftItem(CraftItemEvent e) {
    ExWorld world = this.worldManager.getWorld(e.getWhoClicked().getWorld());

    if (world == null) {
      return;
    }

    if (world.isExceptService() && Server.getUser(((Player) e.getWhoClicked())).isService()) {
      return;
    }
    if (world.getOption(ExWorldOption.ALLOW_CRAFTING)) {
      return;
    }

    this.logger.info("Cancelled craft item event");
    e.setCancelled(true);
    e.setResult(Result.DENY);
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    ExWorld world = this.worldManager.getWorld(event.getPlayer().getWorld());

    if (world == null) {
      return;
    }

    if (world.isExceptService() && Server.getUser(event.getPlayer()).isService()) {
      return;
    }

    Block clickedBlock = event.getClickedBlock();
    Material blockType = clickedBlock != null ? clickedBlock.getType() : Material.AIR;
    ItemStack item = event.getItem();

    if (!world.getOption(ExWorldOption.ALLOW_BLOCK_BREAK)) {
      if (event.getAction() == Action.PHYSICAL) {
        if (clickedBlock == null) {
          return;
        }
        if (blockType == Material.FARMLAND) {
          event.setCancelled(true);
          this.logger.info("Cancelled interact physical event");
          return;
        }
      }
    }

    if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
      Set<Material> filledBuckets = Set.of(Material.LAVA_BUCKET, Material.COD_BUCKET, Material.AXOLOTL_BUCKET,
          Material.POWDER_SNOW_BUCKET, Material.PUFFERFISH_BUCKET, Material.SALMON_BUCKET,
          Material.TROPICAL_FISH_BUCKET, Material.WATER_BUCKET);

      if (item != null) {
        if (filledBuckets.contains(item.getType())) {
          if (!world.getOption(ExWorldOption.ALLOW_FLUID_PLACE) && !blockType.equals(Material.CAULDRON)) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            this.logger.info("Cancelled interact cauldron event");
            return;
          }
        } else if (item.getType().equals(Material.BUCKET)) {
          Set<Material> filledCauldrons = Set.of(Material.LAVA_CAULDRON, Material.WATER_CAULDRON);

          if (!world.getOption(ExWorldOption.ALLOW_FLUID_COLLECT) && !filledCauldrons.contains(blockType)) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            this.logger.info("Cancelled interact cauldron event");
            return;
          }
        }
      }
    }

    if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

      if (blockType.equals(Material.CAKE)) {
        if (!world.getOption(ExWorldOption.ALLOW_CAKE_EAT)) {
          event.setCancelled(true);
          event.setUseInteractedBlock(Event.Result.DENY);
          event.setUseItemInHand(Event.Result.DENY);
          this.logger.info("Cancelled interact cake event");
          return;
        }
      }

      if (world.getOption(ExWorldOption.FORBIDDEN_BLOCK_INVENTORIES).contains(blockType) || world.getOption(ExWorldOption.FORBIDDEN_BLOCK_INVENTORIES).contains(Material.AIR)) {
        event.setCancelled(true);
        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);
        this.logger.info("Cancelled interact block inventory event");
        return;
      }

      if (item != null) {
        if (item.getType().equals(Material.FLINT_AND_STEEL)) {
          if (world.getOption(ExWorldOption.ALLOW_FLINT_AND_STEEL)) {
            return;
          }

          if (world.getOption(ExWorldOption.ALLOW_LIGHT_UP_INTERACTION) && (Tag.CAMPFIRES.isTagged(blockType) || Tag.CANDLES.isTagged(blockType))) {
            return;
          }

          event.setCancelled(true);
          event.setUseInteractedBlock(Event.Result.DENY);
          event.setUseItemInHand(Event.Result.DENY);
          this.logger.info("Cancelled interact light-up event");
        } else if (item.getType().equals(Material.SPLASH_POTION)) {
          if (world.getOption(ExWorldOption.ALLOW_LIGHT_UP_INTERACTION)) {
            return;
          }

          if (!Tag.CAMPFIRES.isTagged(blockType)) {
            return;
          }

          event.setCancelled(true);
          event.setUseInteractedBlock(Event.Result.DENY);
          event.setUseItemInHand(Event.Result.DENY);
        } else {
          if (world.getOption(ExWorldOption.ALLOW_PLACE_IN_BLOCK)) {
            return;
          }

          if (item.getType().equals(Material.ENDER_EYE) && blockType.equals(Material.END_PORTAL_FRAME)) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            this.logger.info("Cancelled interact place enderpearl event");
            return;
          }

          if (Tag.CANDLES.isTagged(item.getType()) && (blockType.equals(Material.CAKE) || Tag.CANDLES.isTagged(blockType))) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            this.logger.info("Cancelled interact cake candle event");
            return;
          }

          if (item.getType().equals(Material.SEA_PICKLE) && blockType.equals(Material.SEA_PICKLE)) {
            event.setCancelled(true);
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
            this.logger.info("Cancelled interact sea pickle event");
            return;
          }
        }
      }
    } else if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {

      if (blockType.equals(Material.FIRE)) {
        if (!world.getOption(ExWorldOption.ALLOW_FIRE_PUNCH_OUT)) {
          event.setCancelled(true);
          event.setUseInteractedBlock(Event.Result.DENY);
          event.setUseItemInHand(Event.Result.DENY);
          this.logger.info("Cancelled interact fire event");
        }
      }
    }

  }

  @EventHandler
  public void onItemFrameChange(PlayerItemFrameChangeEvent e) {
    ExWorld world = this.worldManager.getWorld(e.getPlayer().getWorld());

    if (world == null) {
      return;
    }

    if (world.isExceptService() && Server.getUser(e.getPlayer()).isService()) {
      return;
    }

    if (world.getOption(ExWorldOption.ALLOW_ENTITY_BLOCK_BREAK)) {
      return;
    }

    if (e.getAction().equals(PlayerItemFrameChangeEvent.ItemFrameChangeAction.ROTATE) && world.getOption(ExWorldOption.ALLOW_ITEM_FRAME_ROTATE)) {
      return;
    }

    e.setCancelled(true);
    this.logger.info("Cancelled item frame rotate event");
  }

  @EventHandler
  public void onPlayerDropItem(PlayerDropItemEvent e) {
    ExWorld world = this.worldManager.getWorld(e.getPlayer().getWorld());

    if (world == null) {
      return;
    }

    if (world.getOption(ExWorldOption.ALLOW_DROP_PICK_ITEM)) {
      return;
    }

    if (world.isExceptService() && Server.getUser(e.getPlayer()).isService()) {
      return;
    }

    e.setCancelled(true);
    this.logger.info("Cancelled drop item event");
  }

  @EventHandler
  public void onEntityDamage(UserDamageEvent e) {
    ExWorld world = e.getUser().getExWorld();

    if (world.getOption(ExWorldOption.ENABLE_PLAYER_DAMAGE)) {
      return;
    }

    if (world.isExceptService() && e.getUser().isService()) {
      return;
    }

    e.setCancelled(true);
    e.setCancelDamage(true);
    this.logger.info("Cancelled user damage event");
  }

  @EventHandler
  public void onPlayerPickUpItem(@Deprecated PlayerPickupItemEvent e) {
    ExWorld world = this.worldManager.getWorld(e.getPlayer().getWorld());

    if (world == null) {
      return;
    }

    if (world.getOption(ExWorldOption.ALLOW_DROP_PICK_ITEM)) {
      return;
    }

    User user = Server.getUser(e.getPlayer());

    if (user != null && world.isExceptService() && user.isService()) {
      return;
    }

    e.setCancelled(true);
    this.logger.info("Cancelled pickup item event");
  }

  @EventHandler
  public void onPlayerPickUpArrow(PlayerPickupArrowEvent e) {
    ExWorld world = this.worldManager.getWorld(e.getPlayer().getWorld());

    if (world == null) {
      return;
    }

    if (world.getOption(ExWorldOption.ALLOW_DROP_PICK_ITEM)) {
      return;
    }

    if (world.isExceptService() && Server.getUser(e.getPlayer()).isService()) {
      return;
    }

    e.setCancelled(true);
    this.logger.info("Cancelled pickup arrow event");
  }

  @EventHandler
  public void onBlockBreak(UserBlockBreakEvent e) {
    ExWorld world = e.getUser().getExWorld();

    if (!world.getOption(ExWorldOption.ALLOW_BLOCK_BREAK)) {
      return;
    }

    if (world.isExceptService() && e.getUser().isService()) {
      return;
    }

    if ((e.getBlock().getType().equals(Material.FIRE)
         || (Tag.CANDLES.isTagged(e.getBlock().getType()))
            && ((Candle) e.getBlock().getState()).isLit()) && world.getOption(ExWorldOption.ALLOW_FIRE_PUNCH_OUT)) {
      return;
    }

    e.setCancelled(CancelPriority.LOW, true);
    this.logger.info("Cancelled block break event");
  }

  @EventHandler
  public void onArmorStand(PlayerArmorStandManipulateEvent e) {
    ExWorld world = this.worldManager.getWorld(e.getPlayer().getWorld());

    if (world == null) {
      return;
    }

    if (world.getOption(ExWorldOption.ALLOW_BLOCK_BREAK) && world.getOption(ExWorldOption.ALLOW_ENTITY_BLOCK_BREAK)) {
      return;
    }

    if (world.isExceptService() && Server.getUser(e.getPlayer()).isService()) {
      return;
    }

    e.setCancelled(true);
    this.logger.info("Cancelled armorstand manipulate event");
  }

  @EventHandler
  public void onBoat(VehicleDestroyEvent e) {
    ExWorld world = this.worldManager.getWorld(e.getVehicle().getWorld());

    if (world == null) {
      return;
    }

    if (world.getOption(ExWorldOption.ALLOW_BLOCK_BREAK) && world.getOption(ExWorldOption.ALLOW_ENTITY_BLOCK_BREAK)) {
      return;
    }

    if (e.getAttacker() != null && world.isExceptService() && e.getAttacker() instanceof Player && Server.getUser(((Player) e.getAttacker())).isService()) {
      return;
    }

    e.setCancelled(true);
    this.logger.info("Cancelled vehicle destroy event");
  }

  @EventHandler
  public void onPainting(HangingBreakByEntityEvent e) {
    ExWorld world = this.worldManager.getWorld(e.getRemover().getWorld());

    if (world == null) {
      return;
    }

    if (world.getOption(ExWorldOption.ALLOW_BLOCK_BREAK) && world.getOption(ExWorldOption.ALLOW_ENTITY_BLOCK_BREAK)) {
      return;
    }

    if (world.isExceptService() && e.getRemover() instanceof Player && Server.getUser(((Player) e.getRemover())).isService()) {
      return;
    }

    e.setCancelled(true);
    this.logger.info("Cancelled hanging destroy event");
  }

  @EventHandler
  public void blockItemFrame(PlayerInteractEntityEvent e) {
    ExWorld world = this.worldManager.getWorld(e.getPlayer().getWorld());

    if (world == null) {
      return;
    }

    if (world.getOption(ExWorldOption.ALLOW_BLOCK_BREAK) && world.getOption(ExWorldOption.ALLOW_ENTITY_BLOCK_BREAK)) {
      return;
    }

    if (world.isExceptService() && Server.getUser(e.getPlayer()).isService()) {
      return;
    }

    if (e.getRightClicked().getType().equals(EntityType.ITEM_FRAME) && !world.getOption(ExWorldOption.ALLOW_ITEM_FRAME_ROTATE)) {
      e.setCancelled(true);
      this.logger.info("Cancelled interact entity event");
    }
  }

  @EventHandler
  public void onEntityDamage(EntityDamageByUserEvent e) {
    ExWorld world = this.worldManager.getWorld(e.getUser().getWorld());

    if (world == null) {
      return;
    }

    if (world.getOption(ExWorldOption.ALLOW_BLOCK_BREAK) && world.getOption(ExWorldOption.ALLOW_ENTITY_BLOCK_BREAK)) {
      return;
    }

    if (!e.getEntity().getType().equals(EntityType.ARMOR_STAND) && !e.getEntity().getType().equals(EntityType.ITEM_FRAME)) {
      return;
    }

    if (world.isExceptService() && e.getUser().isService()) {
      return;
    }

    e.setCancelDamage(true);
    e.setCancelled(true);
    this.logger.info("Cancelled user by user damage event");
  }

  @EventHandler
  public void onFoodLevelChange(FoodLevelChangeEvent e) {
    ExWorld world = this.worldManager.getWorld(e.getEntity().getWorld());

    if (world == null) {
      return;
    }

    if (world.getOption(ExWorldOption.CHANGE_FOOD)) {
      return;
    }

    User user = Server.getUser(e.getEntity().getUniqueId());

    if (user == null) {
      return;
    }

    if (world.isExceptService() && user.isService()) {
      return;
    }

    e.setCancelled(true);
    if (e.getEntity() instanceof Player) {
      e.getEntity().setFoodLevel(20);
    }

    this.logger.info("Cancelled food change event");
  }

  @EventHandler
  public void onEntityExplode(EntityExplodeEvent e) {
    ExWorld world = this.worldManager.getWorld(e.getEntity().getWorld());

    if (world == null) {
      return;
    }

    if (world.getOption(ExWorldOption.ENABLE_ENTITY_EXPLOSION)) {
      return;
    }

    e.setCancelled(true);
    this.logger.info("Cancelled entity explode event");
  }

  @EventHandler
  public void onBlockPlace(UserBlockPlaceEvent e) {
    ExWorld world = e.getUser().getExWorld();

    if (world.isExceptService() && e.getUser().isService()) {
      return;
    }

    ItemStack item = e.getItemInHand();
    Block block = e.getBlock();
    Material blockType = block.getType();

    if (item.getType().equals(Material.ENDER_EYE) && blockType.equals(Material.END_PORTAL_FRAME)) {
      e.setCancelled(!world.getOption(ExWorldOption.ALLOW_PLACE_IN_BLOCK));
      return;
    }

    if (Tag.CANDLES.isTagged(item.getType()) && (blockType.equals(Material.CAKE) || Tag.CANDLES.isTagged(blockType))) {
      e.setCancelled(!world.getOption(ExWorldOption.ALLOW_PLACE_IN_BLOCK));
      return;
    }

    if (item.getType().equals(Material.SEA_PICKLE) && blockType.equals(Material.SEA_PICKLE)) {
      e.setCancelled(!world.getOption(ExWorldOption.ALLOW_PLACE_IN_BLOCK));
      return;
    }

    if (item.getType().equals(Material.FLINT_AND_STEEL)) {
      if (world.getOption(ExWorldOption.ALLOW_FLINT_AND_STEEL)) {
        return;
      }

      if (world.getOption(ExWorldOption.ALLOW_LIGHT_UP_INTERACTION)
          && (Tag.CAMPFIRES.isTagged(e.getBlock().getType()) || Tag.CANDLES.isTagged(e.getBlock().getType()))) {
        return;
      }

      e.setCancelled(CancelPriority.LOW, true);
      return;
    }

    if (e.getBlockPlaced().getType().equals(Material.TNT) && world.getOption(ExWorldOption.AUTO_PRIME_TNT)) {
      Server.runTaskLaterSynchrony(() -> {
        if (e.getBlockPlaced().getType().equals(Material.TNT)) {
          e.getBlockPlaced().setType(Material.AIR);
          e.getBlockPlaced().getWorld().spawn(e.getBlockPlaced().getLocation().add(0.5, 0, 0.5), TNTPrimed.class);
        }
      }, 1, BasicBukkit.getPlugin());
    }

    if (world.getOption(ExWorldOption.ALLOW_BLOCK_PLACE)) {
      return;
    }

    this.logger.info("Cancelled block place event");
    e.setCancelled(CancelPriority.LOW, true);
  }

  @EventHandler
  public void onBlockSpread(BlockSpreadEvent e) {
    ExWorld world = this.worldManager.getWorld(e.getBlock().getWorld());

    if (world == null) {
      return;
    }

    if (world.getOption(ExWorldOption.ENABLE_BLOCK_SPREAD)) {
      return;
    }

    e.setCancelled(true);
    this.logger.info("Cancelled block spread event");
  }

  @EventHandler
  public void onBlockBurn(BlockBurnEvent e) {
    ExWorld world = this.worldManager.getWorld(e.getBlock().getWorld());

    if (world == null) {
      return;
    }

    if (world.getOption(ExWorldOption.BLOCK_BURN_UP)) {
      return;
    }

    e.setCancelled(true);
    this.logger.info("Cancelled block burn-up event");
  }

  @EventHandler
  public void onBlockIgnite(BlockIgniteEvent e) {
    ExWorld world = this.worldManager.getWorld(e.getBlock().getWorld());

    if (world == null) {
      return;
    }

    if (world.getOption(ExWorldOption.ALLOW_BLOCK_IGNITE)) {
      if (world.getOption(ExWorldOption.FIRE_SPREAD_SPEED) == 0) {
        e.setCancelled(true);
        this.logger.info("Cancelled block ignite event");
      } else {
        this.checkFireSpread(e.getBlock().getLocation(), world);
      }
      return;
    }

    if (e.getCause().equals(IgniteCause.FLINT_AND_STEEL) && world.getOption(ExWorldOption.ALLOW_FLINT_AND_STEEL)) {
      return;
    }

    if (world.getOption(ExWorldOption.ALLOW_LIGHT_UP_INTERACTION)
        && (Tag.CAMPFIRES.isTagged(e.getBlock().getType()) || Tag.CANDLES.isTagged(e.getBlock().getType()))) {
      return;
    }

    if (world.isExceptService() && Server.getUser(e.getPlayer()).isService()) {
      return;
    }

    e.setCancelled(true);
    this.logger.info("Cancelled block ignite event");
  }

  @EventHandler
  public void onTNTPrime(TNTPrimeEvent e) {
    ExWorld world = this.worldManager.getWorld(e.getBlock().getWorld());

    if (world == null) {
      return;
    }

    if (world.getOption(ExWorldOption.ALLOW_TNT_PRIME)) {
      return;
    }

    if (world.isExceptService() && e.getPrimingEntity() instanceof Player && Server.getUser(((Player) e.getPrimingEntity())).isService()) {
      return;
    }

    this.logger.info("Cancelled tnt prime event");
    e.setCancelled(true);
  }

  @EventHandler
  public void onRespawnSet(PlayerBedEnterEvent e) {
    ExWorld world = this.worldManager.getWorld(e.getBed().getWorld());

    if (world == null) {
      return;
    }

    if (world.isExceptService() && Server.getUser(e.getPlayer()).isService()) {
      return;
    }

    if (!world.getOption(ExWorldOption.ALLOW_BED_ENTER)) {
      e.setUseBed(Result.DENY);
      this.logger.info("Cancelled bed enter event");
    }

  }

  @EventHandler
  public void onBlockFromTo(BlockFromToEvent e) {
    Block block = e.getBlock();
    ExWorld world = this.worldManager.getWorld(block.getWorld());

    if (world == null) {
      return;
    }

    if (world.getOption(ExWorldOption.ALLOW_FLUID_FLOW)) {
      return;
    }

    if (block.getType() == Material.WATER || block.getType() == Material.LAVA) {
      e.setCancelled(true);
      this.logger.info("Cancelled block burn-up event");
    }
  }

  private void checkFireSpread(Location loc, ExWorld world) {
    float maxFires = world.getOption(ExWorldOption.FIRE_SPREAD_SPEED);

    if (maxFires == 0) {
      return;
    }

    int fires = Math.round(world.getRandom().nextFloat(0, maxFires));

    if (!loc.getNearbyPlayers(32).isEmpty()) {
      if (fires > 0) {
        this.scanForIgnitable(loc, world, fires);
      }
    }
  }

  private void scanForIgnitable(Location loc, ExWorld world, int fires) {
    for (ExBlock block : new RandomList<>(world.getBlocksWithinCubicDistance(loc,
        world.getOption(ExWorldOption.FIRE_SPREAD_DISTANCE)))) {
      if (block.isEmpty() || !block.isBurnable()) {
        continue;
      }

      for (BlockFace face : ExBlock.ADJACENT_BLOCK_FACINGS) {
        ExBlock adjacentBlock = block.getExRelative(face);

        if (!adjacentBlock.isEmpty()) {
          continue;
        }

        adjacentBlock.setType(Material.FIRE);

        BlockData blockData = adjacentBlock.getBlockData();
        if (blockData instanceof Fire && face != BlockFace.UP) {
          ((Fire) blockData).setFace(face.getOppositeFace(), true);
          adjacentBlock.setBlockData(blockData);
          if (--fires == 0) {
            return;
          }
          break;
        }
      }
    }

  }
}
