package org.liny.linycoreserverevents.Managers;

import org.bukkit.Bukkit;
import org.liny.linycoreserverevents.Abstract.ServerEvent;
import org.liny.linycoreserverevents.LinyCoreServerEvents;

import java.util.HashMap;

public class EventSpawnManager {

    private final LinyCoreServerEvents main;

    private final HashMap<String, ServerEvent> serverEvents;

    private ServerEvent nextServerEvent;

    private ServerEvent currentServerEvent;

    private Long lastEventTime;

    public EventSpawnManager(LinyCoreServerEvents main) {

        this.main = main;
        this.serverEvents = new HashMap<>();

    }

    public void registerEvent (ServerEvent event, String name) {

        this.serverEvents.put(name, event);

    }

    public ServerEvent getNextServerEvent () {

        return this.nextServerEvent;

    }

    public ServerEvent getCurrentServerEvent () {

        return this.currentServerEvent;

    }

    public HashMap<String, ServerEvent> getRegisteredEvents () {

        return this.serverEvents;

    }

    public Long getTimeForNextEvent () {

        return this.main.getMath().ms2s(System.currentTimeMillis()) - this.lastEventTime;

    }

    public Boolean isEventRegistered (String name) {

        return this.serverEvents.containsKey(name);

    }

    public ServerEvent getRegisteredServerEvent (String name) {

        return this.serverEvents.get(name);

    }

    public void startMainScheduler () {

        Bukkit.getServer()
                .getScheduler()
                .scheduleSyncRepeatingTask(this.main, () -> {

                    if (!this.main.isStopped) {

                        if (!serverEvents.isEmpty()) {

                            if (this.nextServerEvent == null) {

                                this.nextServerEvent = this.serverEvents.values().stream().toList().get((int) (Math.random() * this.serverEvents.values().size()));

                            }

                            if (this.lastEventTime == null) {

                                this.lastEventTime = this.main.getMath().ms2s(System.currentTimeMillis());

                            }

                        }

                        if (this.main.getMath().ms2s(System.currentTimeMillis()) - this.lastEventTime <= this.nextServerEvent.getTimeSeconds()) {

                            this.nextServerEvent.start();
                            this.currentServerEvent = this.nextServerEvent;
                            this.nextServerEvent = this.serverEvents.values().stream().toList().get((int) (Math.random() * this.serverEvents.values().size()));
                            this.lastEventTime = this.main.getMath().ms2s(System.currentTimeMillis());

                        }

                    }

                }, 1, 20);

    }

}
