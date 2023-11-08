package org.liny.linycoreserverevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.liny.linycoreserverevents.Commands.Events;
import org.liny.linycoreserverevents.Events.ServerEventsDisableEvent;
import org.liny.linycoreserverevents.Events.ServerEventsEnableEvent;
import org.liny.linycoreserverevents.Managers.EventSpawnManager;
import org.liny.linycoreserverevents.Utils.Math;

import java.util.Objects;

public final class LinyCoreServerEvents extends JavaPlugin {

    private final Math math;
    private final EventSpawnManager eventSpawnManager;
    private final Events events;

    public Boolean isStopped = true;

    public LinyCoreServerEvents () {

        this.math = new Math();
        this.events = new Events(this);
        this.eventSpawnManager = new EventSpawnManager(this);

    }

    public Math getMath () {

        return this.math;

    }

    public EventSpawnManager getEventSpawnManager () {

        return this.eventSpawnManager;

    }

    @Override
    public void onEnable() {

        this.eventSpawnManager.startMainScheduler();

        Objects.requireNonNull(getCommand("events")).setExecutor(this.events);

        ServerEventsEnableEvent event = new ServerEventsEnableEvent();

        Bukkit.getPluginManager().callEvent(event);

        this.isStopped = false;

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "{LinyCoreEvents} LinyCoreEvents started!");

    }

    @Override
    public void onDisable() {

        this.isStopped = true;

        ServerEventsDisableEvent event = new ServerEventsDisableEvent();

        Bukkit.getPluginManager().callEvent(event);

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "{LinyCoreEvents} LinyCoreEvents disabled!");

    }

}
