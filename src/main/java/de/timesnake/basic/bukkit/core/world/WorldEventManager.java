/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.basic.bukkit.core.world;

import de.timesnake.basic.bukkit.core.main.BasicBukkit;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Plugin;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.CancelPriority;
import de.timesnake.basic.bukkit.util.user.event.EntityDamageByUserEvent;
import de.timesnake.basic.bukkit.util.user.event.UserBlockBreakEvent;
import de.timesnake.basic.bukkit.util.user.event.UserBlockPlaceEvent;
import de.timesnake.basic.bukkit.util.user.event.UserDamageEvent;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import de.timesnake.basic.bukkit.util.world.ExWorld.Restriction;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Candle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.ItemStack;

public class WorldEventManager implements Listener {

    private final WorldManager worldManager;

    public WorldEventManager(WorldManager worldManager) {
        this.worldManager = worldManager;

        Server.registerListener(this, BasicBukkit.getPlugin());
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent e) {
        ExWorld world = this.worldManager.getWorld(e.getWhoClicked().getWorld());

        if (world.isExceptService() && Server.getUser(((Player) e.getWhoClicked())).isService()) {
            return;
        }

        if (!world.isRestricted(Restriction.CRAFTING)) {
            return;
        }

        e.setCancelled(true);
        e.setResult(Result.DENY);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ExWorld world = this.worldManager.getWorld(event.getPlayer().getWorld());

        if (world.isExceptService() && Server.getUser(event.getPlayer()).isService()) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        Material blockType = clickedBlock != null ? clickedBlock.getType() : Material.AIR;
        ItemStack item = event.getItem();

        if (world.isRestricted(ExWorld.Restriction.BLOCK_BREAK)) {
            if (event.getAction() == Action.PHYSICAL) {
                if (clickedBlock == null) {
                    return;
                }
                if (blockType == Material.FARMLAND) {
                    event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
                    event.setCancelled(true);
                    clickedBlock.setType(blockType, true);
                    clickedBlock.setBlockData(clickedBlock.getBlockData());
                    Plugin.WORLDS.getLogger().info("Cancelled interact physical event");
                    return;
                }
            }
        }

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction()
                .equals(Action.RIGHT_CLICK_BLOCK)) {
            Set<Material> filledBuckets = Set.of(Material.LAVA_BUCKET, Material.COD_BUCKET,
                    Material.AXOLOTL_BUCKET,
                    Material.POWDER_SNOW_BUCKET, Material.PUFFERFISH_BUCKET, Material.SALMON_BUCKET,
                    Material.TROPICAL_FISH_BUCKET, Material.WATER_BUCKET);

            if (item != null) {
                if (filledBuckets.contains(item.getType())) {
                    if (world.isRestricted(ExWorld.Restriction.FLUID_PLACE) && !blockType.equals(
                            Material.CAULDRON)) {
                        event.setCancelled(true);
                        event.setUseInteractedBlock(Event.Result.DENY);
                        event.setUseItemInHand(Event.Result.DENY);
                        Plugin.WORLDS.getLogger().info("Cancelled interact cauldron event");
                        return;
                    }
                } else if (item.getType().equals(Material.BUCKET)) {
                    Set<Material> filledCauldrons = Set.of(Material.LAVA_CAULDRON,
                            Material.WATER_CAULDRON);

                    if (world.isRestricted(ExWorld.Restriction.FLUID_COLLECT)
                            && !filledCauldrons.contains(blockType)) {
                        event.setCancelled(true);
                        event.setUseInteractedBlock(Event.Result.DENY);
                        event.setUseItemInHand(Event.Result.DENY);
                        Plugin.WORLDS.getLogger().info("Cancelled interact cauldron event");
                        return;
                    }
                }
            }
        }

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            if (blockType.equals(Material.CAKE)) {
                if (world.isRestricted(ExWorld.Restriction.CAKE_EAT)) {
                    event.setCancelled(true);
                    event.setUseInteractedBlock(Event.Result.DENY);
                    event.setUseItemInHand(Event.Result.DENY);
                    Plugin.WORLDS.getLogger().info("Cancelled interact cake event");
                    return;
                }
            }

            if (world.isRestricted(ExWorld.Restriction.OPEN_INVENTORIES).contains(blockType)) {
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
                event.setUseItemInHand(Event.Result.DENY);
                Plugin.WORLDS.getLogger().info("Cancelled interact block inventory event");
                return;
            }

            if (item != null) {
                if (item.getType().equals(Material.FLINT_AND_STEEL)) {
                    if (!world.isRestricted(ExWorld.Restriction.FLINT_AND_STEEL)) {
                        return;
                    }

                    if (!world.isRestricted(ExWorld.Restriction.LIGHT_UP_INTERACTION)
                            && (Tag.CAMPFIRES.isTagged(blockType) || Tag.CANDLES.isTagged(
                            blockType))) {
                        return;
                    }

                    event.setCancelled(true);
                    event.setUseInteractedBlock(Event.Result.DENY);
                    event.setUseItemInHand(Event.Result.DENY);
                    Plugin.WORLDS.getLogger().info("Cancelled interact light-up event");
                } else if (item.getType().equals(Material.SPLASH_POTION)) {
                    if (!world.isRestricted(ExWorld.Restriction.LIGHT_UP_INTERACTION)) {
                        return;
                    }

                    if (!Tag.CAMPFIRES.isTagged(blockType)) {
                        return;
                    }

                    event.setCancelled(true);
                    event.setUseInteractedBlock(Event.Result.DENY);
                    event.setUseItemInHand(Event.Result.DENY);
                } else {
                    if (!world.isRestricted(ExWorld.Restriction.PLACE_IN_BLOCK)) {
                        return;
                    }

                    if (item.getType().equals(Material.ENDER_EYE) && blockType.equals(
                            Material.END_PORTAL_FRAME)) {
                        event.setCancelled(true);
                        event.setUseInteractedBlock(Event.Result.DENY);
                        event.setUseItemInHand(Event.Result.DENY);
                        Plugin.WORLDS.getLogger().info("Cancelled interact place enderpearl event");
                        return;
                    }

                    if (Tag.CANDLES.isTagged(item.getType()) && (blockType.equals(Material.CAKE)
                            || Tag.CANDLES.isTagged(blockType))) {
                        event.setCancelled(true);
                        event.setUseInteractedBlock(Event.Result.DENY);
                        event.setUseItemInHand(Event.Result.DENY);
                        Plugin.WORLDS.getLogger().info("Cancelled interact cake candle event");
                        return;
                    }

                    if (item.getType().equals(Material.SEA_PICKLE) && blockType.equals(
                            Material.SEA_PICKLE)) {
                        event.setCancelled(true);
                        event.setUseInteractedBlock(Event.Result.DENY);
                        event.setUseItemInHand(Event.Result.DENY);
                        Plugin.WORLDS.getLogger().info("Cancelled interact sea pickle event");
                        return;
                    }
                }
            }
        } else if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {

            if (blockType.equals(Material.FIRE)) {
                if (world.isRestricted(ExWorld.Restriction.FIRE_PUNCH_OUT)) {
                    event.setCancelled(true);
                    event.setUseInteractedBlock(Event.Result.DENY);
                    event.setUseItemInHand(Event.Result.DENY);
                    Plugin.WORLDS.getLogger().info("Cancelled interact fire event");
                }
            }
        }

    }

    @EventHandler
    public void onItemFrameChange(PlayerItemFrameChangeEvent e) {
        ExWorld world = this.worldManager.getWorld(e.getPlayer().getWorld());

        if (world.isExceptService() && Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        if (!world.isRestricted(ExWorld.Restriction.ENTITY_BLOCK_BREAK)) {
            return;
        }

        if (e.getAction().equals(PlayerItemFrameChangeEvent.ItemFrameChangeAction.ROTATE)
                && world.isRestricted(ExWorld.Restriction.ITEM_FRAME_ROTATE)) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled item frame rotate event");
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        ExWorld world = this.worldManager.getWorld(e.getPlayer().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.DROP_PICK_ITEM)) {
            return;
        }

        if (world.isExceptService() && Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled drop item event");
    }

    @EventHandler
    public void onEntityDamage(UserDamageEvent e) {
        ExWorld world = e.getUser().getExWorld();

        if (!world.isRestricted(ExWorld.Restriction.PLAYER_DAMAGE)) {
            return;
        }

        if (world.isExceptService() && e.getUser().isService()) {
            return;
        }

        e.setCancelled(true);
        e.setCancelDamage(true);
        Plugin.WORLDS.getLogger().info("Cancelled user damage event");
    }

    @EventHandler
    public void onPlayerPickUpItem(@Deprecated PlayerPickupItemEvent e) {
        ExWorld world = this.worldManager.getWorld(e.getPlayer().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.DROP_PICK_ITEM)) {
            return;
        }

        User user = Server.getUser(e.getPlayer());

        if (user != null && world.isExceptService() && user.isService()) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled pickup item event");
    }

    @EventHandler
    public void onPlayerPickUpArrow(PlayerPickupArrowEvent e) {
        ExWorld world = this.worldManager.getWorld(e.getPlayer().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.DROP_PICK_ITEM)) {
            return;
        }

        if (world.isExceptService() && Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled pickup arrow event");
    }

    @EventHandler
    public void onBlockBreak(UserBlockBreakEvent e) {
        ExWorld world = e.getUser().getExWorld();

        if (!world.isRestricted(ExWorld.Restriction.BLOCK_BREAK)) {
            return;
        }

        if (world.isExceptService() && e.getUser().isService()) {
            return;
        }

        if ((e.getBlock().getType().equals(Material.FIRE)
                || (Tag.CANDLES.isTagged(e.getBlock().getType())) && ((Candle) e.getBlock()
                .getState()).isLit())
                && !world.isRestricted(ExWorld.Restriction.FIRE_PUNCH_OUT)) {
            return;
        }

        e.setCancelled(CancelPriority.LOW, true);
        Plugin.WORLDS.getLogger().info("Cancelled block break event");
    }

    @EventHandler
    public void onArmorStand(PlayerArmorStandManipulateEvent e) {
        ExWorld world = this.worldManager.getWorld(e.getPlayer().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.BLOCK_BREAK) && !world.isRestricted(
                ExWorld.Restriction.ENTITY_BLOCK_BREAK)) {
            return;
        }

        if (world.isExceptService() && Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled armorstand manipulate event");
    }

    @EventHandler
    public void onBoat(VehicleDestroyEvent e) {
        ExWorld world = this.worldManager.getWorld(e.getVehicle().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.BLOCK_BREAK) && !world.isRestricted(
                ExWorld.Restriction.ENTITY_BLOCK_BREAK)) {
            return;
        }

        if (e.getAttacker() != null && world.isExceptService() && e.getAttacker() instanceof Player
                && Server.getUser(((Player) e.getAttacker())).isService()) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled vehicle destroy event");
    }

    @EventHandler
    public void onPainting(HangingBreakByEntityEvent e) {
        ExWorld world = this.worldManager.getWorld(e.getRemover().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.BLOCK_BREAK) && !world.isRestricted(
                ExWorld.Restriction.ENTITY_BLOCK_BREAK)) {
            return;
        }

        if (world.isExceptService() && e.getRemover() instanceof Player
                && Server.getUser(((Player) e.getRemover())).isService()) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled hanging destroy event");
    }

    @EventHandler
    public void blockItemFrame(PlayerInteractEntityEvent e) {
        ExWorld world = this.worldManager.getWorld(e.getPlayer().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.BLOCK_BREAK) && !world.isRestricted(
                ExWorld.Restriction.ENTITY_BLOCK_BREAK)) {
            return;
        }

        if (world.isExceptService() && Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        if (e.getRightClicked().getType().equals(EntityType.ITEM_FRAME)
                && world.isRestricted(ExWorld.Restriction.ITEM_FRAME_ROTATE)) {
            e.setCancelled(true);
        }

        Plugin.WORLDS.getLogger().info("Cancelled interact entity event");
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByUserEvent e) {
        ExWorld world = this.worldManager.getWorld(e.getUser().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.BLOCK_BREAK) && !world.isRestricted(
                ExWorld.Restriction.ENTITY_BLOCK_BREAK)) {
            return;
        }

        if (!e.getEntity().getType().equals(EntityType.ARMOR_STAND) && !e.getEntity().getType()
                .equals(EntityType.ITEM_FRAME)) {
            return;
        }

        if (world.isExceptService() && e.getUser().isService()) {
            return;
        }

        e.setCancelDamage(true);
        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled user by user damage event");
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        ExWorld world = this.worldManager.getWorld(e.getEntity().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.FOOD_CHANGE)) {
            return;
        }

        if (world.isExceptService() && Server.getUser(e.getEntity().getUniqueId()).isService()) {
            return;
        }

        e.setCancelled(true);
        if (e.getEntity() instanceof Player) {
            e.getEntity().setFoodLevel(20);
        }

        Plugin.WORLDS.getLogger().info("Cancelled food change event");
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        ExWorld world = this.worldManager.getWorld(e.getEntity().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.ENTITY_EXPLODE)) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled entity explode event");
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

        if (item.getType().equals(Material.ENDER_EYE) && blockType.equals(
                Material.END_PORTAL_FRAME)) {
            e.setCancelled(world.isRestricted(ExWorld.Restriction.PLACE_IN_BLOCK));
            return;
        }

        if (Tag.CANDLES.isTagged(item.getType()) && (blockType.equals(Material.CAKE)
                || Tag.CANDLES.isTagged(blockType))) {
            e.setCancelled(world.isRestricted(ExWorld.Restriction.PLACE_IN_BLOCK));
            return;
        }

        if (item.getType().equals(Material.SEA_PICKLE) && blockType.equals(Material.SEA_PICKLE)) {
            e.setCancelled(world.isRestricted(ExWorld.Restriction.PLACE_IN_BLOCK));
            return;
        }

        if (item.getType().equals(Material.FLINT_AND_STEEL)) {
            if (!world.isRestricted(ExWorld.Restriction.FLINT_AND_STEEL)) {
                return;
            }

            if (!world.isRestricted(ExWorld.Restriction.LIGHT_UP_INTERACTION)
                    && (Tag.CAMPFIRES.isTagged(e.getBlock().getType()) || Tag.CANDLES.isTagged(
                    e.getBlock().getType()))) {
                return;
            }

            e.setCancelled(CancelPriority.LOW, true);
            Plugin.WORLDS.getLogger().info("Cancelled block place event");
            return;
        }

        if (!world.isRestricted(ExWorld.Restriction.BLOCK_PLACE)) {
            return;
        }

        e.setCancelled(CancelPriority.LOW, true);
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent e) {
        ExWorld world = this.worldManager.getWorld(e.getBlock().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.FIRE_SPREAD)) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled block spread event");
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent e) {
        ExWorld world = this.worldManager.getWorld(e.getBlock().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.BLOCK_BURN_UP)) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled block burn-up event");
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e) {
        ExWorld world = this.worldManager.getWorld(e.getBlock().getWorld());

        if (!world.isRestricted(ExWorld.Restriction.BLOCK_IGNITE)) {
            return;
        }

        if (!world.isRestricted(ExWorld.Restriction.FLINT_AND_STEEL)) {
            return;
        }

        if (!world.isRestricted(ExWorld.Restriction.LIGHT_UP_INTERACTION)
                && (Tag.CAMPFIRES.isTagged(e.getBlock().getType()) || Tag.CANDLES.isTagged(
                e.getBlock().getType()))) {
            return;
        }

        if (world.isExceptService() && Server.getUser(e.getPlayer()).isService()) {
            return;
        }

        e.setCancelled(true);
        Plugin.WORLDS.getLogger().info("Cancelled block ignite event");
    }
}
