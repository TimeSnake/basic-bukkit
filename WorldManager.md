## World

The world manager loads and unloads all game worlds. On start-up, all folders in the server folder tagged with
an `exworld.yml` file will be loaded. The `exworld.yml` config-file determines the world type. This file allows simple
world exporting and importing on other servers.

### ExWorld (see `ExWorld`)

The `ExWorld` class wraps the bukkit `World` class.

#### ExLocation, ExBlock

### World-Restrictions

The `ExWorld` class provides many world restrictions to block actions.

- `exceptService` - users with service mode are excluded from all restrictions
- `allowBlockBreak` - allows players to break blocks
- `allowBlockPlace` - allows players to place blocks
- `allowBlockIgnite` - allows igniting blocks
- `allowBlockBurnUp` - allows burning up blocks
- `allowEntityBlockBreak` - allows players to destroy entity blocks (like item-frames)
- `allowPlaceInBlock` - allows players to place blocks into blocks (like turtle eggs, sea pickles, candles, ender eyes)
- `allowFluidCollect` - allows players to collect fluids from cauldrons
- `allowFluidPlace` - allows players to place fluids in cauldrons
- `allowFireSpread` - allows fire to spread to other blocks
- `allowLightUpInteraction` - allows players to light up blocks (like candles, fire-camps)
- `allowFlintAndSteel` - allows players to use flint and steel
- `allowFirePunchOut` - allows players to punch out fires
- `allowDropPickUpItem` - allows players to pick up and drop items
- `allowPlayerDamage` - allows players to get damage
- `allowFoodChange` - allows players to lose food
- `allowItemFrameRotate` - allows players to rotate item frames

### World-Border
