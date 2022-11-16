## InventoryManager

### [ExInventory] (`Server#createExInventory(...)`)

The `ExInventory` provides some additional methods on inventories. Like a constructor with item varargs, which
calculates
the needed inventory size.

### Excluded Inventories

Inventories held by an instance of an [ExcludedInventoryHolder] are completely excluded by move- and drop-lock,
click-events.

### [ExItemStack]

The `ExItemStack` class extends the bukkit `ItemStack` class. Each `ExItemStack` is tagged with a unique id. For
instantiating an item many constructors a given. Additionally, many methods are supporting the builder pattern.
The `equals(Object)` and `hashCode()` methods are based on the id. `equals` compares the ids of the items. If the other
item is a bukkit `ItemStack`, it is wrapped to a `ExItemStack` (the id will be extracted, if possible). The `hashCode`
of the item is the hash of the id (allows use of `HashMap`).

**Important:** The unique id of an item could change on a server restart. So items, which are in a player inventory
tagged
with an id are not valid anymore. To prevent these use the `ExItemStack#getHashedIdItem(Material material, String name)`
method to use a persistent tagging with the given name. If the given name already exists, a `DuplicateItemIdException`
will be thrown.

#### Cloning

For cloning multiple clone types are possible:

- `clone()` - Clones the item with a new id, so the items are no longer the same.
- `cloneWithId()` - Clones the item with the current id, so they are treated as "same" items. Changes on the cloned have
  no effect on the original item. This is useful for user specific item.
- `cloneWithoutId()` - Clones the item and clears all tags. This method returns a bukkit `ItemStack`.

#### Extra Tags

**Dropable:**
If not set, the item is not dropable.

**Moveable:**
If not set, the item can not be moved in inventories.

**Immutable:**
If set, the item can no longer be modified. Cloned item will not be immutable by default.
This tag is recommended for basis items to clone from. Attempts to modify immutable items will
throw a `UnsupportedOperationException`.

### InventoryListener (`Server#getInventoryEventManger()`)

The inventory listeners are based on two registration types:

- `InventoryHolder` - called if the inventory is held by the registered holder
- `ExItemStack` - called if the clicked item is equals to the registered item

#### [UserInventoryClickListener]

Called by the `InventoryClickEvent`. Each listener is only called if the registered item is clicked or an item is
clicked, in the inventory hold by the registered holder.

Example for click-listener:

``` java
public class ClickAction implements InventoryHolder, UserInventoryClickListener {

  private final Inventory inventory;

  public ClickAction() {
    this.inventory = Bukkit.createInventory(this, 6 * 9);
    Server.getInventoryEventManager().addClickListener(this, this);
  }
  
  @Override
  public Inventory getInventory() {
    return this.inventory;
  }
  
  @Override
  public void onUserInventoryClick(UserInventoryClickEvent event) {
    // DO SOMETHING (when a item is clicked in the inventory)
  }
}
```

#### [UserInventoryInteractListener]

Called by the `InventoryInteractEvent`. Each listener is only called if the registered item is clicked.

Example for interact-listener:

``` java
public class InteractAction implements UserInventoryInteractListener {

  private final ExItemStack item;

  public ClickAction() {
    this.item = new ExItemStack(Material.STONE);
    Server.getInventoryEventManager().addInteractListener(this, this.item);
  }
  
  @Override
  public void onUserInventoryInteract(UserInventoryInteractEvent event) {
    // DO SOMETHING
  }
}
```

[ExInventory]: ../src/main/java/de/timesnake/basic/bukkit/util/user/ExInventory.java

[ExcludedInventoryHolder]: ../src/main/java/de/timesnake/basic/bukkit/util/user/ExcludedInventoryHolder.java

[ExItemStack]: ../src/main/java/de/timesnake/basic/bukkit/util/user/ExItemStack.java

[UserInventoryClickListener]: ../src/main/java/de/timesnake/basic/bukkit/util/user/event/UserInventoryClickListener.java

[UserInventoryInteractListener]: ../src/main/java/de/timesnake/basic/bukkit/util/user/event/UserInventoryInteractEvent.java