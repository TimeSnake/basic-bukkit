## World Manager (`Server#getWorldManager()`)

The world manager loads and unloads all game worlds. On start-up, all folders in the server folder
tagged with
an `exworld.yml` file will be loaded. The `exworld.yml` config-file determines the world type. This
file allows simple
world exporting and importing on other servers.

### [ExWorld]

The `ExWorld` class wraps the bukkit `World` class.
The class provides the followed world ExWorldOptions.

The `backupWorld(World)` method allows to create a world backup. To load the world backup
call `loadWorldBackup(World)`.

To perform fast world resets, use the `reloadWorld(World)` method. To reset the world completely it
is common to disable
auto-saving on this world.
**Warning:** the reference on this world is invalid after a reset. So all locations, blocks, ... are
invalid. To
encounter this, use the [ExWorld], [ExLocation], [ExBlock] classes. These will be updated by the
world manager.

#### [ExLocation], [ExBlock]

The `ExLocation` class extends the bukkit `Location` class. The class adds some extra methods.
Additionally, the class
holds a reference to the `ExWorld` of the location. So objects of the `ExLocation` class are
remaining valid after a
world reset.
The `ExBlock` class wraps the bukkit `Block` class and acts similar as the `ExLocation` class.

### World-ExWorldOptions

The `ExWorld` class provides many world ExWorldOptions to prevent actions.
See [ExWorldOption] for details.

### World-Border

The world-border manager provides user specific world borders due to packets. A new world border can
be created with the
[ExWorldBorder] class. The new border must not be registered.


[ExWorld]: ../src/main/java/de/timesnake/basic/bukkit/util/world/ExWorld.java

[ExLocation]: ../src/main/java/de/timesnake/basic/bukkit/util/world/ExLocation.java

[ExBlock]: ../src/main/java/de/timesnake/basic/bukkit/util/world/ExBlock.java

[ExWorldBorder]: ../src/main/java/de/timesnake/basic/bukkit/util/world/ExWorldBorder.java

[ExWorldOption]: ../src/main/java/de/timesnake/basic/bukkit/util/world/ExWorldOption.java