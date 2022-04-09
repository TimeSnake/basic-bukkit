# Usage

The basic bukkit system is based on the singleton pattern. The singleton `ServerManager` manages all. The `Server` class
provides static access to all `ServerManager` methods. All extra stuff is managed by its own manager, which can be
accessed by the `ServerManager`.

* [InventoryManager](Inventory.md) (inventories, events, item-stacks)
* [WorldManager](WorldManager.md) (world loading, resetting)
* [ScoreboardManager](ScoreboardManager.md) (tablists, sideboards)
* [UserEventManager](UserEventManager.md)
* [CommandManager](CommandManager.md) (chat-commands)
* [ChatManager](ChatManager.md) (global-chat, team-chats)
* [Files](Files.md) (config-files)

# Injections

* [Server](Server.md)
* [User](User.md)
