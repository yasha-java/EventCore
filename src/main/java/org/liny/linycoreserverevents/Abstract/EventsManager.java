package org.liny.linycoreserverevents.Abstract;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.liny.Pair;
import org.liny.linycoreserverevents.Data.EventData;
import org.liny.linycoreserverevents.Exceptions.ESMCurrentlyWorking;
import org.liny.linycoreserverevents.Exceptions.EventsEmpty;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * New ESM class for Minecraft Version 1.20.1
 */
public interface EventsManager {

    /**
     * @return is empty list of events
     */
    @SuppressWarnings("unused")
    @NotNull Boolean isEventsEmpty ();

    /**
     * @param events names for add
     *
     * @return list of pairs [Event, isAdded]
     */
    @SuppressWarnings("unused")
    @NotNull List<Pair<ServerEvent, Boolean>> addEvents (@NotNull ServerEvent... events);

    /**
     * @param events names for remove
     *
     * @return list of pairs [EventName, isRemoved]
     */
    @SuppressWarnings("unused")
    @NotNull List<Pair<String, Boolean>> removeEvents (@NotNull String... events);

    /**
     * @return cloned list of events
     */
    @SuppressWarnings("unused")
    @NotNull LinkedList<Pair<ServerEvent, String>> getEvents ();

    /**
     * @param eventName name of needed event
     *
     * @return nullable optional of server event by event name
     */
    @SuppressWarnings("unused")
    @NotNull Optional<ServerEvent> getEvent (@NotNull String eventName);

    /**
     * @param eventClass class of needed event
     *
     * @return nullable optional of server event by event class
     */
    @SuppressWarnings("unused")
    @NotNull Optional<ServerEvent> getEvent (@NotNull Class<? extends ServerEvent> eventClass);

    /**
     * @return random server event with chance
     * @exception EventsEmpty if events not registered
     */
    @SuppressWarnings("unused")
    @Nullable ServerEvent getRandomServerEvent () throws EventsEmpty;

    /**
     * @param time every period of time "time" will play a tick
     * @param unit unit for time
     * @param main main class of plugin
     *
     * @throws ESMCurrentlyWorking if task already started
     */
    @SuppressWarnings("unused")
    void initTask (@NotNull Long time, @NotNull TimeUnit unit, @NotNull Plugin main /* for starting of other plugins */) throws ESMCurrentlyWorking;

    /**
     * @return EventData
     */
    @SuppressWarnings("unused")
    @NotNull EventData getData();

    /**
     * @return time for starting future event
     */
    @SuppressWarnings("unused")
    @NotNull Long getFutureEventTime ();

    /**
     * @return true if event registered,
     *         false if event not registered
     *
     * @param name name of event
     */
    @SuppressWarnings("unused")
    @NotNull Boolean isEventRegistered (String name);

    /**
     * @return true if event registered,
     *         false if event not registered
     *
     * @param eventClass name of event
     */
    @SuppressWarnings("unused")
    @NotNull Boolean isEventRegistered(Class<? extends ServerEvent> eventClass);

    /**
     * @return true if task closed,
     *         false if task not closed
     */
    @SuppressWarnings("unused")
    @NotNull Boolean closeTask ();
}
