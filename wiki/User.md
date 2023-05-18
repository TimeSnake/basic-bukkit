# User

## Injection

To inject your own user class, you must override the [ServerManager]`#loadUser(Player player)`
method.

``` java
public class YourServerManager extends ServerManager() {

    ...
    
    @Override
    public User loadUser(Player player) {
        return new YourUser(player);
    }
    
    ...
    
}
```

[ServerManager]: /src/main/java/de/timesnake/basic/bukkit/util/ServerManager.java