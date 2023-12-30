package org.liny.linycoreserverevents;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.liny.linycoreserverevents.Commands.EventCommand;
import org.liny.linycoreserverevents.Events.ServerEventsDisableEvent;
import org.liny.linycoreserverevents.Events.ServerEventsEnableEvent;
import org.liny.linycoreserverevents.Managers.EventSpawnManager;

import java.util.Objects;

public final class EventCore extends JavaPlugin {

    private final EventSpawnManager eventSpawnManager;

    public Boolean isStopped = true;

    public EventCore() {

        this.eventSpawnManager = new EventSpawnManager(this);

    }

    public EventSpawnManager getEventSpawnManager () {

        return this.eventSpawnManager;

    }

    @Override
    public void onEnable() {

        this.eventSpawnManager.startMainScheduler();

        Objects.requireNonNull(getCommand("events")).setExecutor(new EventCommand.EventsExecator(this));
        Objects.requireNonNull(getCommand("events")).setTabCompleter(new EventCommand.EventsTabCompleter(this));

        ServerEventsEnableEvent event = new ServerEventsEnableEvent(this);

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
