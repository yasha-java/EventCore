# SynchronizedServerEvents
## Hi everyone.  This is a library for creating synchronized game events for Minecraft server.

# Install
## Maven
You can install this depency from console: ```mvn install:install-file -Dfile="Path/to/EventCore.jar" -DgroupId=org.linytech -DartifactId=EventCore -Dversion=2.0-UNSTABLE```

Note: If you have maven installed from Intellij idea and it is not listed in the PATH for the Windows console, you can try this:
```"<Path/to/Intellij/IDEA>plugins\maven\lib\maven3\bin\mvn.cmd"```

Well! Now you need to add the project to the Maven configuration:
```maven
<dependency>
    <groupId>org.linytech</groupId>
    <artifactId>EventCore</artifactId>
    <version>2.0-UNSTABLE</version>
    <scope>provided</scope>
</dependency>
```
Note: you can import library as system scope. [Click for more information.](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html)

# Usage
To create our own event, we will create a class implemented from ServerEvent:
```java
public class MyEvent implements ServerEvent {
    @Override
    public void start() {}
    @Override
    public void stop() {}
    @Override
    public Integer getTimeSeconds() {}
    @Override
    public String getName() {}
    @Override
    public Boolean canSpawn() {}
    @Override
    public Float getPercent() {}
    @Override
    public Boolean isClosed() {}
    @Override
    public Plugin getMain() {}
}
```

Well, now we can register this event in the EventsManager. You can get it through the Bukkit service system::
```java
RegisteredServiceProvider<EventsManager> rsp = Bukkit.getServicesManager().getRegistration(EventsManager.class);

if (rsp == null)
    throw new RuntimeException("Please install EventCore plugin!");

EventsManager events = rsp.getProvider();
```
Now let's register our event:
```
MyEvent event = new MyEvent();
events.addEvents(event);
```
Complete!

Note: EventsManager.addEvents(ServerEvent... events) can takes many events :)

# Not supported yet. Reason: many different depency close-source


