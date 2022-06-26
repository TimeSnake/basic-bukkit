package de.timesnake.basic.bukkit.util.user;

import de.timesnake.basic.bukkit.util.user.event.UserInventoryClickEvent;
import de.timesnake.library.basic.util.Tuple;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ExItemStack extends org.bukkit.inventory.ItemStack {

    private static Integer newItemId() {
        int id = 1;

        if (!ITEMS_BY_ID.isEmpty()) {
            id = Collections.max(ITEMS_BY_ID.keySet());
        }
        while (ITEMS_BY_ID.containsKey(id)) {
            id++;
        }
        return id;
    }

    public static ExItemStack getItemById(Integer id) {
        if (id == null) {
            return null;
        }
        return ITEMS_BY_ID.get(id);
    }

    private static Integer getIdFromString(String name) {
        if (name == null) {
            return null;
        }

        try {
            return Integer.parseInt(name.split(ATTRIBUTE_SPLITTER)[0]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Integer getIdFromItem(ItemStack item) {
        if (item == null) {
            return null;
        }

        if (item instanceof ExItemStack) {
            return ((ExItemStack) item).getId();
        }

        if (item.getItemMeta() == null) {
            return null;
        }

        return ExItemStack.getIdFromString(item.getItemMeta().getLocalizedName());
    }

    public static ExItemStack getPotion(Material material, PotionType type, boolean extended, boolean upgraded) {
        ExItemStack item = new ExItemStack(material);

        if (!(item.getItemMeta() instanceof PotionMeta meta)) {
            throw new InvalidItemTypeException("PotionMeta");
        }

        meta.setBasePotionData(new PotionData(type, extended, upgraded));
        item.setItemMeta(meta);

        return item;
    }

    public static ExItemStack getPotion(Material material, PotionType type, Color color, boolean extended,
                                        boolean upgraded) {
        ExItemStack item = ExItemStack.getPotion(material, type, extended, upgraded);

        if (!(item.getItemMeta() instanceof PotionMeta meta)) {
            throw new InvalidItemTypeException("PotionMeta");
        }

        meta.setColor(color);
        item.setItemMeta(meta);

        return item;
    }

    public static ExItemStack getPotion(Material material, int amount, PotionType type, boolean extended,
                                        boolean upgraded) {
        return ExItemStack.getPotion(material, type, extended, upgraded).asQuantity(amount);
    }

    public static ExItemStack getPotion(Material material, int amount, String displayName, PotionType type,
                                        boolean extended, boolean upgraded) {
        return ExItemStack.getPotion(material, amount, type, extended, upgraded).setDisplayName(displayName);
    }

    public static ExItemStack getPotion(PotionMaterial type, int amount, String displayName,
                                        PotionEffectType effectType, int duration, int level) {
        ExItemStack item = new ExItemStack(type.getMaterial());

        item.asQuantity(amount);

        if (!(item.getItemMeta() instanceof PotionMeta meta)) {
            throw new InvalidItemTypeException("PotionMeta");
        }

        meta.setColor(effectType.getColor());
        meta.setDisplayName(displayName);
        meta.addCustomEffect(effectType.createEffect(duration, level), true);
        item.setItemMeta(meta);

        return item;
    }

    public static ExItemStack getPotion(PotionMaterial type, int amount, String displayName,
                                        PotionEffectType effectType, int duration, int level, List<String> lore) {
        return ExItemStack.getPotion(type, amount, displayName, effectType, duration, level).setExLore(lore);
    }

    public static ExItemStack getLeatherArmor(Material material, Color color) {
        ExItemStack item = new ExItemStack(material);

        if (!(item.getItemMeta() instanceof LeatherArmorMeta meta)) {
            throw new InvalidItemTypeException("LeatherArmorMeta");
        }

        meta.setColor(color);
        item.setItemMeta(meta);

        return item;
    }


    // id management, converter

    public static ExItemStack getLeatherArmor(Material material, String displayName, Color color) {
        return ExItemStack.getLeatherArmor(material, color).setDisplayName(displayName);
    }

    public static ExItemStack getHead(Player owner, String displayName) {
        ExItemStack item = new ExItemStack(Material.PLAYER_HEAD, displayName);

        if (!(item.getItemMeta() instanceof SkullMeta meta)) {
            throw new InvalidItemTypeException("SkullMeta");
        }

        meta.setOwningPlayer(owner.getPlayer());
        item.setItemMeta(meta);

        return item;
    }

    public static ExItemStack getHead(Player owner, String displayName, List<String> lore) {
        return ExItemStack.getHead(owner, displayName).setExLore(lore);
    }

    public static ExItemStack getItem(ItemStack item, boolean createIfNotExists) {
        if (item == null) {
            return null;
        }

        if (ExItemStack.hasId(item)) {
            return ExItemStack.getItemById(ExItemStack.getIdFromItem(item));
        } else if (createIfNotExists) {
            return new ExItemStack(item, false);
        }

        return null;
    }

    public static ExItemStack getItemByMeta(ItemMeta meta) {
        return ExItemStack.getItemById(getIdFromString(meta.getLocalizedName()));
    }

    public static boolean hasId(ItemStack item) {
        return ExItemStack.getIdFromItem(item) != null;
    }

    private static Boolean getDropableFromString(String name) {
        if (name == null) {
            return null;
        }

        String[] attributes = name.split(ATTRIBUTE_SPLITTER);

        if (attributes.length < 2) {
            return null;
        }

        return Boolean.parseBoolean(attributes[1]);
    }

    private static Boolean getMoveableFromString(String name) {
        if (name == null) {
            return null;
        }

        String[] attributes = name.split(ATTRIBUTE_SPLITTER);

        if (attributes.length < 3) {
            return null;
        }

        return Boolean.parseBoolean(attributes[2]);
    }

    private static void setAttributes(ItemStack item, Integer id, Boolean dropable, Boolean moveable) {
        if (id != null && dropable != null && item.getItemMeta() != null) {
            ItemMeta meta = item.getItemMeta();
            meta.setLocalizedName(id + ATTRIBUTE_SPLITTER + dropable + ATTRIBUTE_SPLITTER + moveable);
            item.setItemMeta(meta);
        }
    }


    // tag helper methods
    private static final String ATTRIBUTE_SPLITTER = ";";
    private static final Map<Integer, ExItemStack> ITEMS_BY_ID = new HashMap<>();

    static {
        ITEMS_BY_ID.put(0, null);
    }

    protected final Integer id;
    protected Integer slot;
    private boolean dropable = true;
    private boolean moveable = true;

    @Deprecated
    public ExItemStack(Material material, Color color) {
        this(material);

        if (!(this.getItemMeta() instanceof LeatherArmorMeta meta)) {
            throw new InvalidItemTypeException("LeatherArmorMeta");
        }

        meta.setColor(color);
        this.setItemMeta(meta);
    }

    @Deprecated
    public ExItemStack(Material material, String displayName, Color color) {
        this(material, color);
        this.setDisplayName(displayName);
    }

    @Deprecated
    public ExItemStack(Material material, PotionType type, boolean extended, boolean upgraded) {
        this(material);

        if (!(this.getItemMeta() instanceof PotionMeta meta)) {
            throw new InvalidItemTypeException("PotionMeta");
        }

        meta.setBasePotionData(new PotionData(type, extended, upgraded));
        this.setItemMeta(meta);
    }

    @Deprecated
    public ExItemStack(Material material, PotionType type, Color color, boolean extended, boolean upgraded) {
        this(material, type, extended, upgraded);

        if (!(this.getItemMeta() instanceof PotionMeta meta)) {
            throw new InvalidItemTypeException("PotionMeta");
        }

        meta.setColor(color);
        this.setItemMeta(meta);
    }

    @Deprecated
    public ExItemStack(Material material, int amount, PotionType type, boolean extended, boolean upgraded) {
        this(material, type, extended, upgraded);
        this.setAmount(amount);
    }

    @Deprecated
    public ExItemStack(Material material, int amount, String displayName, PotionType type, boolean extended,
                       boolean upgraded) {
        this(material, amount, type, extended, upgraded);
        this.setDisplayName(displayName);
    }

    @Deprecated
    public ExItemStack(Material material, boolean unbreakable) {
        this(material);
        ItemMeta meta = this.getItemMeta();
        meta.setUnbreakable(unbreakable);
        this.setItemMeta(meta);
    }

    @Deprecated
    public ExItemStack(Material material, boolean unbreakable, boolean enchant) {
        this(material);
        ItemMeta meta = this.getItemMeta();
        meta.setUnbreakable(unbreakable);
        this.setItemMeta(meta);
        if (enchant) this.enchant();
    }

    @Deprecated
    public ExItemStack(Material material, int amount, int damage) {
        this(material, amount);
        this.setDamage(damage);
    }

    @Deprecated
    public ExItemStack(Material material, int damage, boolean enchant) {
        this(material, 1, damage);
        if (enchant) this.enchant();
    }

    @Deprecated
    public ExItemStack(Material material, List<Enchantment> enchantments, List<Integer> levels) {
        this(material);
        this.addEnchantments(enchantments, levels);
    }

    @Deprecated
    public ExItemStack(Material material, String displayName, List<Enchantment> enchantments, List<Integer> levels) {
        this(material, displayName);
        this.addEnchantments(enchantments, levels);
    }

    @Deprecated
    public ExItemStack(Material material, int damage, List<Enchantment> enchantments, List<Integer> levels) {
        this(material, 1, damage);
        this.addEnchantments(enchantments, levels);
    }

    @Deprecated
    public ExItemStack(Material material, boolean unbreakable, List<Enchantment> enchantments, List<Integer> levels) {
        this(material, enchantments, levels);
        ItemMeta meta = this.getItemMeta();
        meta.setUnbreakable(unbreakable);
        this.setItemMeta(meta);
    }

    @Deprecated
    public ExItemStack(boolean splash, String displayName, PotionEffectType effectType, int duration, int level,
                       int amount) {
        this((splash ? Material.SPLASH_POTION : Material.POTION));
        this.setAmount(amount);
        if (!(this.getItemMeta() instanceof PotionMeta meta)) {
            throw new InvalidItemTypeException("PotionMeta");
        }
        meta.setColor(effectType.getColor());
        meta.setDisplayName(displayName);
        meta.addCustomEffect(effectType.createEffect(duration, level), true);
        this.setItemMeta(meta);
    }

    @Deprecated
    public ExItemStack(boolean splash, String displayName, List<String> lore, PotionEffectType effectType,
                       int duration, int level, int amount) {
        this((splash ? Material.SPLASH_POTION : Material.POTION));
        this.setAmount(amount);
        if (!(this.getItemMeta() instanceof PotionMeta meta)) {
            throw new InvalidItemTypeException("PotionMeta");
        }
        meta.setColor(effectType.getColor());
        meta.setDisplayName(displayName);
        meta.addCustomEffect(effectType.createEffect(duration, level), true);
        meta.setLore(lore);
        this.setItemMeta(meta);
    }

    @Deprecated
    public ExItemStack(String displayName, PotionEffectType effectType, int duration, int level, int amount) {
        this(Material.LINGERING_POTION);
        this.setAmount(amount);
        if (!(this.getItemMeta() instanceof PotionMeta meta)) {
            throw new InvalidItemTypeException("PotionMeta");
        }
        meta.setColor(effectType.getColor());
        meta.setDisplayName(displayName);
        meta.addCustomEffect(effectType.createEffect(duration, level), true);
        this.setItemMeta(meta);
    }

    private ExItemStack(Integer id, ItemStack item, boolean dropable, boolean moveable, boolean clone) {
        super(clone ? item.clone() : item);
        this.id = id;
        this.dropable = dropable;
        this.moveable = moveable;
        ITEMS_BY_ID.put(id, this);
        ExItemStack.setAttributes(this, this.id, this.dropable, this.moveable);
    }

    /**
     * Clones the item stack and tries to read id
     * If item id is null, it gets a new id
     *
     * @param item The item to create
     */
    private ExItemStack(ItemStack item, boolean clone) {
        super(clone ? item.clone() : item);

        // clone from existing
        if (item instanceof ExItemStack) {
            this.id = ((ExItemStack) item).getId();
            this.dropable = ((ExItemStack) item).isDropable();
            this.moveable = ((ExItemStack) item).isMoveable();
            ExItemStack.setAttributes(this, this.id, this.dropable, this.moveable);
        } else if (item.getItemMeta() != null) {
            Integer id = getIdFromString(item.getItemMeta().getLocalizedName());

            if (id != null) {
                this.id = id;
                this.dropable = getDropableFromString(item.getItemMeta().getLocalizedName());
                this.moveable = getMoveableFromString(item.getItemMeta().getLocalizedName());
            } else {
                this.id = newItemId();
                ITEMS_BY_ID.put(this.id, this);
            }

            ExItemStack.setAttributes(this, this.id, this.dropable, this.moveable);
        } else {
            throw new InvalidItemTypeException("No ItemMeta");
        }

    }

    public ExItemStack(Integer id, ItemStack item) {
        this(id, item, true, true, true);
    }

    private ExItemStack(Integer id, ItemStack item, Integer slot, boolean clone) {
        this(id, item, true, true, clone);
        this.slot = slot;
    }

    private ExItemStack(Integer id, ItemStack item, Integer slot, boolean dropable, boolean moveable, boolean clone) {
        this(id, item, slot, clone);
        this.dropable = dropable;
        this.moveable = moveable;
    }

    public ExItemStack(ItemStack item) {
        this(item, true);
    }

    public ExItemStack(Integer id, ItemStack item, Integer slot) {
        this(id, item);
        this.slot = slot;
    }

    public ExItemStack(ItemStack item, Integer slot) {
        this(item, true);
        this.slot = slot;
    }

    public ExItemStack(Material material) {
        this(newItemId(), new ItemStack(material));
    }

    @Deprecated
    public ExItemStack(Player owner, String displayName) {
        this(Material.PLAYER_HEAD, displayName);
        if (!(this.getItemMeta() instanceof SkullMeta meta)) {
            throw new InvalidItemTypeException("SkullMeta");
        }
        meta.setOwningPlayer(owner.getPlayer());
        this.setItemMeta(meta);
    }

    @Deprecated
    public ExItemStack(Player owner, String displayName, List<String> lore) {
        this(Material.PLAYER_HEAD, displayName);
        if (!(this.getItemMeta() instanceof SkullMeta meta)) {
            throw new InvalidItemTypeException("SkullMeta");
        }
        meta.setOwningPlayer(owner.getPlayer());
        this.setItemMeta(meta);
        this.setLore(lore);
    }

    public ExItemStack(Material material, int amount) {
        this(material);
        this.setAmount(amount);
    }

    public ExItemStack(Material material, String displayName) {
        this(material);
        this.setDisplayName(displayName);
    }

    public ExItemStack(Material material, int amount, String displayName) {
        this(material, displayName);
        this.setAmount(amount);
    }

    public ExItemStack(Material material, int amount, String displayName, List<String> lore) {
        this(material, displayName, lore);
        this.setAmount(amount);
    }

    public ExItemStack(Material material, String displayName, List<String> lore) {
        this(material, displayName);
        this.setLore(lore);
    }

    public ExItemStack(Material material, String displayName, String... lines) {
        this(material, displayName, Arrays.asList(lines));
    }

    public ExItemStack(Integer slot, Material material) {
        this(material);
        this.slot = slot;
    }

    public ExItemStack(Integer slot, Material material, String displayName) {
        this(material, displayName);
        this.slot = slot;
    }

    public ExItemStack(Integer slot, Material material, String displayName, List<String> lore) {
        this(material, displayName, lore);
        this.slot = slot;
    }

    public ExItemStack(Integer slot, Material material, Color color) {
        this(material, color);
        this.slot = slot;
    }

    public ExItemStack(Integer slot, String displayName, Color color, Material material) {
        this(slot, material, color);
        this.setDisplayName(displayName);
    }

    public ExItemStack unbreakable() {
        return this.setUnbreakable(true);
    }

    public Integer getId() {
        return id;
    }

    public Integer getSlot() {
        return this.slot;
    }

    public ExItemStack setSlot(Integer slot) {
        this.slot = slot;
        return this;
    }

    /**
     * Sets the item slot
     *
     * @param slot The slot to set
     * @return the item itself
     */
    public ExItemStack setSlot(EquipmentSlot slot) {
        switch (slot) {
            case FEET:
                this.slot = 36;
                break;
            case LEGS:
                this.slot = 37;
                break;
            case CHEST:
                this.slot = 38;
                break;
            case HEAD:
                this.slot = 39;
                break;
            case OFF_HAND:
                this.slot = 40;
                break;
            case HAND:
                break;
        }
        return this;
    }

    public ExItemStack setExType(@NotNull Material type) {
        this.setType(type);
        return this;
    }

    public ExItemStack setDisplayName(String displayName) {
        ItemMeta meta = this.getItemMeta();
        meta.displayName(Component.text(displayName));
        this.setItemMeta(meta);
        return this;
    }

    public ExItemStack setDisplayName(Component displayName) {
        ItemMeta meta = this.getItemMeta();
        meta.displayName(displayName);
        this.setItemMeta(meta);
        return this;
    }

    public ExItemStack setExLore(List<String> lines) {
        ItemMeta meta = this.getItemMeta();
        meta.lore(lines.stream().map(Component::text).collect(Collectors.toList()));
        this.setItemMeta(meta);
        return this;
    }

    public ExItemStack setExLoreComponents(List<Component> lines) {
        ItemMeta meta = this.getItemMeta();
        meta.lore(lines);
        this.setItemMeta(meta);
        return this;
    }

    public ExItemStack setLore(String... lines) {
        ItemMeta meta = this.getItemMeta();
        meta.lore(Arrays.stream(lines).map(Component::text).collect(Collectors.toList()));
        this.setItemMeta(meta);
        return this;
    }

    public ExItemStack replaceLoreLine(int line, String text) {
        ItemMeta meta = this.getItemMeta();
        @Nullable List<Component> lore = meta.lore();
        if (line < lore.size()) {
            lore.set(line, Component.text(text));
        } else {
            lore.add(line, Component.text(text));
        }
        meta.lore(lore);
        this.setItemMeta(meta);
        return this;
    }

    @Override
    public ExItemStack asQuantity(int quantity) {
        this.setAmount(quantity);
        return this;
    }

    @Override
    public ExItemStack asOne() {
        this.setAmount(1);
        return this;
    }

    public ExItemStack setUnbreakable(boolean unbreakable) {
        ItemMeta meta = this.getItemMeta();
        meta.setUnbreakable(unbreakable);
        this.setItemMeta(meta);
        return this;
    }

    @Deprecated
    public ExItemStack addEnchantments(List<Enchantment> enchantments, List<Integer> enchantmentLevels) {
        ItemMeta meta = this.getItemMeta();
        for (int i = 0; i < enchantments.size(); i++) {
            meta.addEnchant(enchantments.get(i), enchantmentLevels.get(i), true);
        }
        this.setItemMeta(meta);
        return this;
    }

    @Deprecated
    public ExItemStack setEnchantments(List<Enchantment> enchantments, List<Integer> enchantmentLevels) {
        this.removeEnchantments();
        this.addEnchantments(enchantments, enchantmentLevels);
        return this;
    }

    public ExItemStack addExEnchantment(Enchantment enchantment, int level) {
        ItemMeta meta = this.getItemMeta();
        meta.addEnchant(enchantment, level, true);
        this.setItemMeta(meta);
        return this;
    }

    public ExItemStack removeEnchantments() {
        ItemMeta meta = this.getItemMeta();
        for (Enchantment enchantment : meta.getEnchants().keySet()) {
            meta.removeEnchant(enchantment);
        }
        return this;
    }

    @SafeVarargs
    public final ExItemStack addEnchantments(Tuple<Enchantment, Integer>... enchantments) {
        ItemMeta meta = this.getItemMeta();
        for (Tuple<Enchantment, Integer> enchantment : enchantments) {
            meta.addEnchant(enchantment.getA(), enchantment.getB(), true);
        }
        this.setItemMeta(meta);
        return this;
    }

    public ExItemStack setDamage(int damage) {
        if (!(this.getItemMeta() instanceof Damageable meta)) {
            return this;
        }

        meta.setDamage(damage);
        this.setItemMeta(meta);
        return this;
    }

    public ExItemStack enchant() {
        this.addEnchantments(List.of(Enchantment.LUCK), List.of(1));
        this.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ExItemStack disenchant() {
        for (Enchantment enchantment : this.getEnchantments().keySet()) {
            this.removeEnchantment(enchantment);
        }
        this.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ExItemStack hideAll() {
        for (ItemFlag flag : ItemFlag.values()) {
            this.addItemFlags(flag);
        }
        return this;
    }

    public boolean isDropable() {
        return this.dropable;
    }

    /**
     * Sets if the item is dropable
     * <p>
     * If set true the item is not dropable, else it is dropable by a player
     * </p>
     *
     * @param flag The flag to set
     * @return this
     */
    public ExItemStack setDropable(boolean flag) {
        this.dropable = flag;

        ExItemStack.setAttributes(this, this.id, this.dropable, this.moveable);

        return this;
    }

    public boolean isMoveable() {
        return moveable;
    }

    public ExItemStack setMoveable(boolean moveable) {
        this.moveable = moveable;

        ExItemStack.setAttributes(this, this.id, this.dropable, this.moveable);

        return this;
    }

    /**
     * Compares item ids
     * <p>
     * If {@code o} is an instance of {@link ExItemStack} the ids are compared.
     * If {@code o} is an instance of {@link ItemStack} the id is being recovered and compared.
     * </p>
     *
     * @param o The object to compare
     * @return true if the ids are equal, else false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof ExItemStack) return Objects.equals(this.id, ((ExItemStack) o).id);
        if (o instanceof ItemStack) return Objects.equals(this.id, new ExItemStack((ItemStack) o, false).id);
        return false;
    }

    @Override
    public boolean isSimilar(ItemStack item) {
        try {
            return this.equals(item);
        } catch (InvalidItemTypeException e) {
            return item.getItemMeta() == null;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    /**
     * Clones the item with new id
     *
     * @return the cloned {@link ExItemStack}
     */
    @Override
    public ExItemStack clone() {
        ItemStack item = super.clone();
        return new ExItemStack(ExItemStack.newItemId(), item, this.slot, this.dropable, this.moveable, false);
    }

    /**
     * Clones the item with the id
     *
     * <p>
     * The cloned item is not in the item list.
     * So, if it will be searched by the id, the original {@link ExItemStack} will be returned.
     * The equals check not differing the original and cloned.
     * <p>
     * So, the {@link UserInventoryClickEvent} will find the original item, but an equality check with the cloned one
     * returns true.
     * </p>
     *
     * @return the cloned {@link ExItemStack}
     */
    public ExItemStack cloneWithId() {
        ItemStack item = super.clone();
        return new ExItemStack(this.id, item, this.slot, this.dropable, this.moveable, false);
    }

    /**
     * Clones the item and resets the id
     *
     * @return the cloned {@link ItemStack}
     */
    public ItemStack cloneWithoutId() {
        ItemStack item = super.clone();
        if (item.getItemMeta() != null) {
            ItemMeta meta = item.getItemMeta();
            meta.setLocalizedName(null);
            item.setItemMeta(meta);
        }
        return item;
    }

    public enum PotionMaterial {
        DRINK(Material.POTION),
        SPLASH(Material.SPLASH_POTION),
        LINGERING(Material.LINGERING_POTION);

        private final Material material;

        PotionMaterial(Material material) {
            this.material = material;
        }

        public Material getMaterial() {
            return material;
        }
    }
}
