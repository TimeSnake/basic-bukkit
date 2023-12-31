# basic-bukkit

## Setup

This module is part of the plugin-project [1]. You can use it as standalone project or as bundle.

### Standalone

Clone this project and enter your gitlab credentials in your gradle user home
properties (`~/.gradle/gradle.propreties`):

```
timesnakeUser=<user>
timesnakePassword=<access_token>

timesnakePluginDir=<plugins_dir>
```

Replace `<user>` with your gitlab username and `<access_token>` with an access-token.
You can optionally replace `<plugins_dir>` with a directory to export the plugin directly (therefore run the
gradle `exportAsPlugin` task).

### Bundle

To use this project in the multimodule plugin-project, read the setup guide in the root module [1].

## Testing

To test this module, you must set up a test server, therefore read regarding guide in the root module [1].

## Usage

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

### Injections

* [ServerManager](wiki/ServerManager.md)
* [User](wiki/User.md)
*

[root module](https://git.timesnake.de/timesnake/minecraft/plugin-root-project)

## Code Style

The code style guide can be found in the plugin root project [1].

## License

The source is licensed under the GNU GPLv2 license that can be found in the [LICENSE](LICENSE)
  file.

[1] https://git.timesnake.de/timesnake/minecraft/plugin-root-project