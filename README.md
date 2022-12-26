# basic-bukkit

This module is part of the plugin-project and won't run without
the [root module](https://git.timesnake.de/timesnake/plugin-root-project) [1].

---

# Usage

The basic bukkit system is based on the singleton pattern. The singleton class `ServerManager` is
the main api
interface. The `Server` class provides static access to all `ServerManager` methods.
All extra stuff is managed by its own manager, which can be
accessed by the `ServerManager`.

* [InventoryManager](wiki/Inventory.md) (inventories, events, item-stacks)
* [WorldManager](wiki/WorldManager.md) (world loading, resetting)
* [ScoreboardManager](wiki/ScoreboardManager.md) (tablists, sideboards)
* [UserEventManager](wiki/UserEventManager.md)
* [CommandManager](wiki/CommandManager.md) (chat-commands)
* [ChatManager](wiki/ChatManager.md) (global-chat, team-chats)
* [Files](wiki/Files.md) (config-files)

# Injections

* [ServerManager](wiki/ServerManager.md)
* [User](wiki/User.md)
*

---

## Code Style

The code style guide can be found in the plugin root project [1].

## License

- The source is licensed under the GNU GPLv2 license that can be found in the [LICENSE](LICENSE)
  file.

[1] https://git.timesnake.de/timesnake/plugin-root-project