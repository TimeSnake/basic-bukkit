package de.timesnake.basic.bukkit.util.world;

import de.timesnake.basic.bukkit.core.world.DelegatedWorld;
import de.timesnake.basic.bukkit.core.world.ExWorldFile;
import de.timesnake.basic.bukkit.core.world.WorldManager;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.user.User;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.Material;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ExWorld extends DelegatedWorld implements World {

    private final WorldManager.Type type;
    private final ExWorldFile file;

    private boolean safe;

    private boolean exceptService = true;

    private boolean allowBlockBreak = true;
    private boolean allowFluidCollect = true;
    private boolean allowBlockPlace = true;
    private boolean allowFluidPlace = true;
    private boolean allowEntityBlockBreak = true;
    private boolean allowItemFrameRotate = true;
    private boolean allowDropPickItem = true;
    private boolean allowPlayerDamage = true;
    private boolean allowFoodChange = true;
    private boolean allowEntityExplode = true;
    private boolean allowFireSpread = true;
    private boolean allowBlockBurnUp = true;
    private boolean allowBlockIgnite = true;
    private boolean allowLightUpInteraction = true;
    private boolean allowFlintAndSteel = true;
    private boolean allowPlaceInBlock = true;
    private boolean allowFirePunchOut = true;
    private boolean allowCakeEat = true;

    private List<Material> lockedBlockInventories = new LinkedList<>();

    public ExWorld(org.bukkit.World world, WorldManager.Type type, ExWorldFile file) {
        super(world);
        this.type = type;
        this.file = file;

        if (this.file.isSafe()) {
            this.setAutoSave(true);
            this.safe = true;
        } else {
            this.setAutoSave(false);
            this.safe = false;
        }
    }

    public void setBukkitWorld(World world) {
        this.world = world;

        this.world.setAutoSave(this.safe);
    }

    @Override
    public void setAutoSave(boolean safe) {
        super.setAutoSave(safe);
        this.safe = safe;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof World && ((World) obj).getName().equals(this.getName())) {
            return true;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.world.getName());
    }

    public boolean isSafe() {
        return safe;
    }

    @NotNull
    public static Audience empty() {
        return Audience.empty();
    }

    public void removePlayers() {
        for (User user : Server.getUsers()) {
            if (user.getLocation().getWorld().equals(this.getBukkitWorld())) {
                user.teleport(Server.getWorldManager().getBasicWorld());
            }
        }
    }

    @NotNull
    public static Audience audience(@NotNull Audience @NotNull ... audiences) {
        return Audience.audience(audiences);
    }

    public ExWorldFile getExFile() {
        return file;
    }

    @NotNull
    public static ForwardingAudience audience(@NotNull Iterable<? extends Audience> audiences) {
        return Audience.audience(audiences);
    }

    public boolean isBlockBreakAllowed() {
        return allowBlockBreak;
    }

    public void allowBlockBreak(boolean allowBlockBreak) {
        this.allowBlockBreak = allowBlockBreak;
    }

    public boolean isBlockPlaceAllowed() {
        return allowBlockPlace;
    }

    public void allowBlockPlace(boolean allowBlockPlace) {
        this.allowBlockPlace = allowBlockPlace;
    }

    public boolean isEntityBlockBreakAllowed() {
        return allowEntityBlockBreak;
    }

    public void allowEntityBlockBreak(boolean allowEntityBlockBreak) {
        this.allowEntityBlockBreak = allowEntityBlockBreak;
    }

    public boolean isItemFrameRotateAllowed() {
        return this.allowItemFrameRotate;
    }

    public void allowItemFrameRotate(boolean allowRotate) {
        this.allowItemFrameRotate = allowRotate;
    }

    public boolean isDropPickItemAllowed() {
        return allowDropPickItem;
    }

    public void allowDropPickItem(boolean allowDropPickItem) {
        this.allowDropPickItem = allowDropPickItem;
    }

    public boolean isPlayerDamageAllowed() {
        return allowPlayerDamage;
    }

    public void allowPlayerDamage(boolean allowPlayerDamage) {
        this.allowPlayerDamage = allowPlayerDamage;
    }

    public boolean isFoodChangeAllowed() {
        return allowFoodChange;
    }

    public void allowFoodChange(boolean allowFoodChange) {
        this.allowFoodChange = allowFoodChange;
    }

    public boolean isEntityExplodeAllowed() {
        return allowEntityExplode;
    }

    public void allowEntityExplode(boolean allowEntityExplode) {
        this.allowEntityExplode = allowEntityExplode;
    }

    public boolean isFireSpreadAllowed() {
        return allowFireSpread;
    }

    public void allowFireSpread(boolean allowFireSpread) {
        this.allowFireSpread = allowFireSpread;
    }

    public boolean isExceptService() {
        return exceptService;
    }

    public void setExceptService(boolean exceptService) {
        this.exceptService = exceptService;
    }

    public boolean isBlockBurnUpAllowed() {
        return allowBlockBurnUp;
    }

    public void allowBlockBurnUp(boolean allowBlockBurnUp) {
        this.allowBlockBurnUp = allowBlockBurnUp;
    }

    public boolean isBlockIgniteAllowed() {
        return allowBlockIgnite;
    }

    public void allowBlockIgnite(boolean allowBlockIgnite) {
        this.allowBlockIgnite = allowBlockIgnite;
    }

    public boolean isFluidCollectAllowed() {
        return allowFluidCollect;
    }

    public boolean isFluidPlaceAllowed() {
        return allowFluidPlace;
    }

    /**
     * Disallows to collect fluids. Except from cauldrons
     */
    public void allowFluidCollect(boolean allowFluidCollect) {
        this.allowFluidCollect = allowFluidCollect;
    }

    /**
     * Disallows to place fluids. Except into cauldrons
     */
    public void allowFluidPlace(boolean allowFluidPlace) {
        this.allowFluidPlace = allowFluidPlace;
    }

    public void allowLightUpInteraction(boolean allowLightUpInteraction) {
        this.allowLightUpInteraction = allowLightUpInteraction;
    }

    public boolean isLightUpInteractionAllowed() {
        return allowLightUpInteraction;
    }

    public boolean isFlintAndSteelAllowed() {
        return allowFlintAndSteel;
    }

    public void allowFlintAndSteel(boolean allowFlintAndSteel) {
        this.allowFlintAndSteel = allowFlintAndSteel;
    }

    public boolean isPlaceInBlockAllowed() {
        return allowPlaceInBlock;
    }

    public void allowPlaceInBlock(boolean allowPlaceInBlock) {
        this.allowPlaceInBlock = allowPlaceInBlock;
    }

    public boolean isFirePunchOutAllowed() {
        return allowFirePunchOut;
    }

    public void allowFirePunchOut(boolean allowFirePunchOut) {
        this.allowFirePunchOut = allowFirePunchOut;
    }

    public boolean isCakeEatAllowed() {
        return this.allowCakeEat;
    }

    public void allowCakeEat(boolean allowCakeEat) {
        this.allowCakeEat = allowCakeEat;
    }

    public List<Material> getLockedBlockInventories() {
        return lockedBlockInventories;
    }

    public void setLockedBlockInventories(List<Material> lockedBlockInventories) {
        this.lockedBlockInventories = lockedBlockInventories;

        if (this.lockedBlockInventories == null) {
            this.lockedBlockInventories = new LinkedList<>();
        }
    }

    public void addLockedBlockInventory(Material material) {
        this.lockedBlockInventories.add(material);
    }

    public void clearLockedBlockInventories() {
        this.lockedBlockInventories.clear();
    }

    public WorldManager.Type getType() {
        return type;
    }

    public World getBukkitWorld() {
        return this.world;
    }

    public ExBlock getExBlockAt(int x, int y, int z) {
        return new ExBlock(this.getBlockAt(x, y, z));
    }


}
