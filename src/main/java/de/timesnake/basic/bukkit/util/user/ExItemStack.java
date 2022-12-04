/*
 * workspace.basic-bukkit.main
 * Copyright (C) 2022 timesnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; If not, see <http://www.gnu.org/licenses/>.
 */

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
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
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

    /**
     * Gets a new item stack with an id equals to the hash code of the given name.
     *
     * <p>
     * This is usefully to have a persistent item id.
     * The name of the item must be unique. Else a {@link DuplicateItemIdException} will be thrown.
     * </p>
     *
     * @param material The material
     * @param name     The name of the item, must be unique
     * @return Returns a new {@link ExItemStack}
     */
    public static ExItemStack getHashedIdItem(Material material, String name) {
        int hash = name.hashCode();
        if (ITEMS_BY_ID.containsKey(hash)) {
            throw new DuplicateItemIdException("NameHash: Item name " + name + " is already used, name can not be hashed");
        }
        return new ExItemStack(hash, new ItemStack(material));
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
    private boolean immutable = false;

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

    public ExItemStack(Material material, int amount) {
        this(material);
        this._setAmount(amount);
    }

    public ExItemStack(Material material, String displayName) {
        this(material);
        this._setDisplayName(displayName);
    }

    public ExItemStack(Material material, int amount, String displayName) {
        this(material, displayName);
        this._setAmount(amount);
    }

    public ExItemStack(Material material, int amount, String displayName, List<String> lore) {
        this(material, displayName, lore);
        this._setAmount(amount);
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

    public boolean isImmutable() {
        return immutable;
    }

    /**
     * Makes the item stack immutable to all changes. Recommended as basis item to clone from.
     *
     * @return Returns this
     */
    public ExItemStack immutable() {
        this.immutable = true;
        return this;
    }

    private void checkImmutable() {
        if (this.isImmutable()) {
            throw new UnsupportedOperationException("Item " + this.id + " is immutable");
        }
    }

    public ExItemStack unbreakable() {
        return this._unbreakable();
    }

    protected ExItemStack _unbreakable() {
        this.checkImmutable();
        return this._setUnbreakable(true);
    }

    public Integer getId() {
        return id;
    }

    public Integer getSlot() {
        return this.slot;
    }

    public ExItemStack setSlot(Integer slot) {
        this.checkImmutable();
        return this._setSlot(slot);
    }

    /**
     * Sets the item slot
     *
     * @param slot The slot to set
     * @return the item itself
     */
    public ExItemStack setSlot(EquipmentSlot slot) {
        this.checkImmutable();
        return this._setSlot(slot);
    }

    protected ExItemStack _setSlot(Integer slot) {
        this.slot = slot;
        return this;
    }

    protected ExItemStack _setSlot(EquipmentSlot slot) {
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
        this.checkImmutable();
        return this._setExType(type);
    }

    protected ExItemStack _setExType(@NotNull Material type) {
        this._setType(type);
        return this;
    }

    public void setType(@NotNull Material type) {
        this.checkImmutable();
        this._setType(type);
    }

    protected void _setType(@NotNull Material type) {
        super.setType(type);
    }

    public ExItemStack setDisplayName(String displayName) {
        return this.setDisplayName(Component.text(displayName));
    }

    protected ExItemStack _setDisplayName(String displayName) {
        return this._setDisplayName(Component.text(displayName));
    }

    public ExItemStack setDisplayName(Component displayName) {
        this.checkImmutable();
        return this._setDisplayName(displayName);
    }

    protected ExItemStack _setDisplayName(Component displayName) {
        ItemMeta meta = this.getItemMeta();
        meta.displayName(displayName);
        this.setItemMeta(meta);
        return this;
    }

    @Override
    public void setLore(@Nullable List<String> lore) {
        this.checkImmutable();
        super.setLore(lore);
    }

    public ExItemStack setExLore(List<String> lines) {
        this.checkImmutable();
        return this._setExLore(lines);
    }

    protected ExItemStack _setExLore(List<String> lines) {
        ItemMeta meta = this.getItemMeta();
        meta.lore(lines.stream().map(Component::text).collect(Collectors.toList()));
        this.setItemMeta(meta);
        return this;
    }

    public ExItemStack setExLoreComponents(List<Component> lines) {
        this.checkImmutable();
        return this._setExLoreComponents(lines);
    }

    protected ExItemStack _setExLoreComponents(List<Component> lines) {
        ItemMeta meta = this.getItemMeta();
        meta.lore(lines);
        this.setItemMeta(meta);
        return this;
    }

    public ExItemStack setLore(String... lines) {
        this.checkImmutable();
        return this._setLore(lines);
    }

    protected ExItemStack _setLore(String... lines) {
        ItemMeta meta = this.getItemMeta();
        meta.lore(Arrays.stream(lines).map(Component::text).collect(Collectors.toList()));
        this.setItemMeta(meta);
        return this;
    }

    public ExItemStack replaceLoreLine(int line, String text) {
        this.checkImmutable();
        return this._replaceLoreLine(line, text);
    }

    protected ExItemStack _replaceLoreLine(int line, String text) {
        ItemMeta meta = this.getItemMeta();
        @Nullable List<Component> lore = meta.lore();
        if (lore == null) lore = new LinkedList<>();
        while (lore.size() <= line) lore.add(lore.size(), Component.text(""));
        lore.set(line, Component.text(text));
        meta.lore(lore);
        this.setItemMeta(meta);
        return this;
    }

    @Override
    public void lore(@Nullable List<net.kyori.adventure.text.Component> lore) {
        this.checkImmutable();
        this._lore(lore);
    }

    protected void _lore(@Nullable List<net.kyori.adventure.text.Component> lore) {
        super.lore(lore);
    }

    @Override
    public ExItemStack asQuantity(int quantity) {
        this.checkImmutable();
        return this._asQuantity(quantity);
    }

    protected ExItemStack _asQuantity(int quantity) {
        this.setAmount(quantity);
        return this;
    }

    @Override
    public void setAmount(int amount) {
        this.checkImmutable();
        this._setAmount(amount);
    }

    protected void _setAmount(int amount) {
        super.setAmount(amount);
    }

    @Override
    public ExItemStack asOne() {
        this.checkImmutable();
        return this._asOne();
    }

    protected ExItemStack _asOne() {
        this.setAmount(1);
        return this;
    }

    public ExItemStack setUnbreakable(boolean unbreakable) {
        this.checkImmutable();
        return this._setUnbreakable(unbreakable);
    }

    protected ExItemStack _setUnbreakable(boolean unbreakable) {
        ItemMeta meta = this.getItemMeta();
        meta.setUnbreakable(unbreakable);
        this.setItemMeta(meta);
        return this;
    }

    public ExItemStack addExEnchantment(Enchantment enchantment, int level) {
        this.checkImmutable();
        return this._addExEnchantment(enchantment, level);
    }

    protected ExItemStack _addExEnchantment(Enchantment enchantment, int level) {
        ItemMeta meta = this.getItemMeta();
        meta.addEnchant(enchantment, level, true);
        this._setItemMeta(meta);
        return this;
    }

    public ExItemStack removeEnchantments() {
        this.checkImmutable();
        return this._removeEnchantments();
    }

    protected ExItemStack _removeEnchantments() {
        ItemMeta meta = this.getItemMeta();
        for (Enchantment enchantment : meta.getEnchants().keySet()) {
            meta.removeEnchant(enchantment);
        }
        return this;
    }

    @SafeVarargs
    public final ExItemStack addEnchantments(Tuple<Enchantment, Integer>... enchantments) {
        this.checkImmutable();
        return this._addEnchantments(enchantments);
    }

    @SafeVarargs
    protected final ExItemStack _addEnchantments(Tuple<Enchantment, Integer>... enchantments) {
        ItemMeta meta = this.getItemMeta();
        for (Tuple<Enchantment, Integer> enchantment : enchantments) {
            meta.addEnchant(enchantment.getA(), enchantment.getB(), true);
        }
        this.setItemMeta(meta);
        return this;
    }

    @Override
    public void addEnchantment(@NotNull Enchantment ench, int level) {
        this.checkImmutable();
        this._addEnchantment(ench, level);
    }

    protected void _addEnchantment(@NotNull Enchantment ench, int level) {
        super.addEnchantment(ench, level);
    }

    @Override
    public void addUnsafeEnchantment(@NotNull Enchantment ench, int level) {
        this.checkImmutable();
        this._addUnsafeEnchantment(ench, level);
    }

    protected void _addUnsafeEnchantment(@NotNull Enchantment ench, int level) {
        super.addUnsafeEnchantment(ench, level);
    }

    @Override
    public void addUnsafeEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        this.checkImmutable();
        this._addUnsafeEnchantments(enchantments);
    }

    protected void _addUnsafeEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        super.addUnsafeEnchantments(enchantments);
    }

    @Override
    public int removeEnchantment(@NotNull Enchantment ench) {
        this.checkImmutable();
        return this._removeEnchantment(ench);
    }

    protected int _removeEnchantment(@NotNull Enchantment ench) {
        return super.removeEnchantment(ench);
    }

    @Override
    public void addEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        this.checkImmutable();
        this._addEnchantments(enchantments);
    }

    protected void _addEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        super.addEnchantments(enchantments);
    }

    public ExItemStack setDamage(@Range(from = 0, to = 1) float percent) {
        this.checkImmutable();
        return this._setDamage((int) (this.getType().getMaxDurability() * percent));
    }

    public ExItemStack setDamage(int damage) {
        this.checkImmutable();
        return this._setDamage(damage);
    }

    protected ExItemStack _setDamage(int damage) {
        if (!(this.getItemMeta() instanceof Damageable meta)) {
            return this;
        }

        meta.setDamage(damage);
        this.setItemMeta(meta);
        return this;
    }

    @Override
    public boolean setItemMeta(@Nullable ItemMeta itemMeta) {
        this.checkImmutable();
        return this._setItemMeta(itemMeta);
    }

    protected boolean _setItemMeta(@Nullable ItemMeta itemMeta) {
        return super.setItemMeta(itemMeta);
    }

    public ExItemStack enchant() {
        this.checkImmutable();
        return this._enchant();
    }

    protected ExItemStack _enchant() {
        this._addUnsafeEnchantment(Enchantment.LUCK, 1);
        this._addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ExItemStack disenchant() {
        this.checkImmutable();
        return this._disenchant();
    }

    protected ExItemStack _disenchant() {
        for (Enchantment enchantment : this.getEnchantments().keySet()) {
            this._removeEnchantment(enchantment);
        }
        this._removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ExItemStack hideAll() {
        this.checkImmutable();

        for (ItemFlag flag : ItemFlag.values()) {
            this.addItemFlags(flag);
        }
        return this;
    }

    @Override
    public boolean editMeta(final @NotNull java.util.function.Consumer<? super ItemMeta> consumer) {
        this.checkImmutable();
        return this._editMeta(consumer);
    }

    protected boolean _editMeta(final @NotNull java.util.function.Consumer<? super ItemMeta> consumer) {
        return super.editMeta(consumer);
    }

    @Override
    public <M extends ItemMeta> boolean editMeta(final @NotNull Class<M> metaClass, final @NotNull java.util.function.Consumer<@NotNull ? super M> consumer) {
        this.checkImmutable();
        return this._editMeta(metaClass, consumer);
    }

    protected <M extends ItemMeta> boolean _editMeta(final @NotNull Class<M> metaClass, final @NotNull java.util.function.Consumer<@NotNull ? super M> consumer) {
        return super.editMeta(metaClass, consumer);
    }

    @Override
    public ExItemStack add(int qty) {
        this.checkImmutable();
        return this._add(qty);
    }

    protected ExItemStack _add(int qty) {
        return (ExItemStack) super.add(qty);
    }

    @Override
    public ExItemStack subtract(int qty) {
        this.checkImmutable();
        return this._subtract(qty);
    }

    protected ExItemStack _subtract(int qty) {
        return (ExItemStack) super.subtract(qty);
    }

    @Override
    public void addItemFlags(@NotNull ItemFlag... itemFlags) {
        this.checkImmutable();
        this._addItemFlags(itemFlags);
    }

    protected void _addItemFlags(@NotNull ItemFlag... itemFlags) {
        super.addItemFlags(itemFlags);
    }

    @Override
    public void removeItemFlags(@NotNull ItemFlag... itemFlags) {
        this.checkImmutable();
        this._removeItemFlags(itemFlags);
    }

    protected void _removeItemFlags(@NotNull ItemFlag... itemFlags) {
        super.removeItemFlags(itemFlags);
    }

    public ExItemStack accept(Consumer<ExItemStack> consumer) {
        consumer.accept(this);
        return this;
    }

    public ExItemStack apply(Function<ExItemStack, ExItemStack> function) {
        return function.apply(this);
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
        this.checkImmutable();

        this.dropable = flag;

        ExItemStack.setAttributes(this, this.id, this.dropable, this.moveable);

        return this;
    }

    public boolean isMoveable() {
        return moveable;
    }

    public ExItemStack setMoveable(boolean moveable) {
        this.checkImmutable();

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
