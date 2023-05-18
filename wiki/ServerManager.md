# ServerManager

## Injection

To inject an own server manager singleton, you need to override the `onLoad()` method in your plugin
main class. In this
method set the singleton instance from the `ServerManager` class to your class by calling
`ServerManager.setInstance(manger)`.

``` java 
public class YourMainClass extends JavaPlugin {

    @Override
    public void onLoad() {
        ServerManager.setInstance(new YourServerManager());
    }
    
    ...
}
```

By using this hook-point, the `ServerManager.getInstance()` method will always return an instance of
your ServerManager
class.

### Manager Initialization

All `init${Name}Manager` can be used to inject own sub manager classes. If the init method is not
overridden, the
default manager will be used.