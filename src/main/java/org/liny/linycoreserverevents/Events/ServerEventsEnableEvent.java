package org.liny.linycoreserverevents.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.liny.linycoreserverevents.LinyCoreServerEvents;

public class ServerEventsEnableEvent extends Event {

    public final static HandlerList HANDLER_LIST = new HandlerList();

    private final LinyCoreServerEvents main;

    public ServerEventsEnableEvent (LinyCoreServerEvents main) {

        this.main = main;

    }

    public LinyCoreServerEvents getMain () {
        return this.main;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
