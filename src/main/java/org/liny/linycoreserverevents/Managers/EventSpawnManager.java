package org.liny.linycoreserverevents.Managers;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.liny.linycoreserverevents.Abstract.ServerEvent;
import org.liny.linycoreserverevents.EventCore;
import org.liny.linycoreserverevents.Utils.Math;

import java.util.HashMap;

public class EventSpawnManager {

    private final EventCore main;
    private final HashMap<String, ServerEvent> serverEvents;
    private ServerEvent nextServerEvent;
    private ServerEvent currentServerEvent;
    private Long lastEventTime;

    public EventSpawnManager(@NotNull EventCore main) {

        this.main = main;
        this.serverEvents = new HashMap<>();

    }

    public void registerEvent (@NotNull ServerEvent event, @NotNull String name) {

        this.serverEvents.put(name, event);

    }

    public @NotNull ServerEvent getNextServerEvent () {

        return this.nextServerEvent;

    }

    public @Nullable ServerEvent getCurrentServerEvent () {

        return this.currentServerEvent;

    }

    public @NotNull HashMap<String, ServerEvent> getRegisteredEvents () {

        return this.serverEvents;

    }

    public @Nullable Long getTimeForNextEvent () {

        return Math.ms2s(System.currentTimeMillis()) - this.lastEventTime;

    }

    public @NotNull Boolean isEventRegistered (@NotNull String name) {

        return this.serverEvents.containsKey(name);

    }

    public @Nullable ServerEvent getRegisteredServerEvent (@NotNull String name) {

        return this.serverEvents.get(name);

    }

    public void startMainScheduler () {

        Bukkit.getServer()
                .getScheduler()
                .scheduleSyncRepeatingTask(this.main, () -> {

                    if (!this.main.isStopped) {

                        if (!serverEvents.isEmpty()) {

                            if (this.nextServerEvent == null) {

                                this.nextServerEvent = this.serverEvents.values().stream().toList().get((int) (java.lang.Math.random() * this.serverEvents.values().size()));

                                while (this.nextServerEvent.getTimeSeconds() == null) {

                                    this.nextServerEvent = this.serverEvents.values().stream().toList().get((int) (java.lang.Math.random() * this.serverEvents.values().size()));

                                }

                            }

                        }

                        if (this.lastEventTime == null) {
                            this.lastEventTime = Math.ms2s(System.currentTimeMillis());
                        }

                        if (this.nextServerEvent != null) {

                            if (Math.ms2s(System.currentTimeMillis()) - this.lastEventTime >= this.nextServerEvent.getTimeSeconds()) {

                                this.nextServerEvent.start();
                                Bukkit.getConsoleSender().sendMessage(this.nextServerEvent.getName());
                                this.currentServerEvent = this.nextServerEvent;
                                this.nextServerEvent = this.serverEvents.values().stream().toList().get((int) (java.lang.Math.random() * this.serverEvents.values().size()));
                                this.lastEventTime = Math.ms2s(System.currentTimeMillis());

                            }

                        }

                    }

                }, 1, 20);

    }

}
