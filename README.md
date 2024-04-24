# EventCore
### Hi everyone.  This is a library for creating synchronized game events for Minecraft server.

# Install
You can install this depency from console: 
```cmd
mvn install:install-file -Dfile="Path/to/EventCore.jar" -DgroupId=org.linytech -DartifactId=EventCore -Dversion=2.0-UNSTABLE
```

Note: If you have maven installed from Intellij idea and it is not listed in the PATH for the Windows console, you can try this:
```cmd
"<Path/to/Intellij/IDEA>plugins\maven\lib\maven3\bin\mvn.cmd"
```

Well! Now you need to add the project to the Maven configuration:
```xml
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

# Simple Area Math
In this project, I decided to make some more algorithms. I present to you the ```java RegionArea<R extends Vector>```:
```java
public RegionArea (@NotNull R fPoint, @NotNull R sPoint);
public RegionArea (@NotNull Vector clazz, @NotNull Location location, @NotNull Double xOffset, @Nullable Double yOffset, @NotNull Double zOffset);
```
This class has algorithms for managing an entire area on the map. It is parameterized from Vector3(for accounting for x, y, z) and from Vector2(for accounting for x, z without accounting for y).
And it also has such simple methods that are probably enough to implement some events:
```java
public @NotNull Collection<Player> getContainedPlayers (); //Parallel StreamAPI Used
public @NotNull Boolean checkPoint (@NotNull Double x, @NotNull Double y, @Nullable Double z);
public @NotNull Boolean checkPoint(@NotNull Location location);
public @NotNull R randomPoint ();
```

# Not supported yet. Reason: many different depency close-source


