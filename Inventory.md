## InventoryManager

### ExInventory

The `ExInventory` providInes some extra methods on inventories. Like a constructor with item varargs, which calculates
the needed inventory size.

### ExItemStack (see `ExItemStack`)

The `ExItemStack` class extends the bukkit `ItemStack` class. Each `ExItemStack` is tagged with a unique id. For
instantiating an item many constructors a given. Additionally, many methods are supporting the builder pattern.
The `equals(Object)` and `hashCode()` methods are based on the id. `equals` compares the ids of the items. If the other
item is a bukkit `ItemStack`, it is wrapped to a `ExItemStack` (the id will be extracted, if possible). The `hashCode`
of the item is the hash of the id (allows use of `HashMap`).

#### Clone

For cloning multiple clone types are possible:

- `clone()` - Clones the item with a new id, so the items are no longer the same.
- `cloneWithId()` - Clones the item with the current id, so they are treated as "same" items. Changes on the cloned have
  no effect on the original item. This is useful for user specific item.
- `cloneWithoutId` - Clones the item and clears all tags. This method returns a bukkit `ItemStack`.

#### Extra Tags

**Dropable:**
If not set, the item is not dropable.

**Moveable:**
If not set, the item can not be moved in inventories.

### InventoryListener (see `InventoryEventManager`)

The inventory listeners are based on two registration types:

- `InventoryHolder` - called if the inventory is held by the registered holder
- `ExItemStack` - called if the clicked item is equals to the registered item

#### UserInventoryClickListener

Called by the `InventoryClickEvent`. Each listener is only called if the registered item is clicked or an item is
clicked in the inventory hold by the registered holder.

#### UserInventoryInteractListener

Called by the `InventoryInteractEvent`. Each listener is only called if the registered item is clicked.