package org.liny.linycoreserverevents;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.liny.linycoreserverevents.Abstract.EventsManager;
import org.liny.linycoreserverevents.Commands.EventCommand;
import org.liny.linycoreserverevents.Exceptions.ESMCurrentlyWorking;
import org.liny.linycoreserverevents.Managers.ESM;
import org.liny.notify.NNotifyEngine;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class EventCore extends JavaPlugin {
    private final ESM manager = new ESM();

    public @NotNull EventsManager getManager() {
        return this.manager;
    }

    @Override
    public void onEnable() {
        Bukkit.getServicesManager().register(
                EventsManager.class,
                manager,
                this,
                ServicePriority.Highest
        ); //registration events manager
        Bukkit.getConsoleSender().sendMessage(NNotifyEngine.getAcceptMessage("EventsManager registered!", true));

        Objects.requireNonNull(getCommand("events")).setExecutor(new EventCommand.EventsExecutor(this));
        Objects.requireNonNull(getCommand("events")).setTabCompleter(new EventCommand.EventsTabCompleter(this));
        Bukkit.getConsoleSender().sendMessage(NNotifyEngine.getAcceptMessage("Commands registered!", true));

        try {
            this.manager.initTask(
                    1L,
                    TimeUnit.SECONDS,
                    this
            );
        } catch (ESMCurrentlyWorking ignored) {
            Bukkit.getConsoleSender().sendMessage(NNotifyEngine.getAcceptMessage("Task can't start! Enable task from command", false));
        } finally {
            Bukkit.getConsoleSender().sendMessage(NNotifyEngine.getAcceptMessage("Task started!", true));
        }
    }
}
