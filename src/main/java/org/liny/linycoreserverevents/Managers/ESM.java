package org.liny.linycoreserverevents.Managers;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.liny.linycoreserverevents.Abstract.EventsManager;
import org.liny.linycoreserverevents.Abstract.ServerEvent;
import org.liny.linycoreserverevents.Data.EventData;
import org.liny.linycoreserverevents.Exceptions.ESMCurrentlyWorking;
import org.liny.linycoreserverevents.Exceptions.EventsEmpty;
import org.liny.linycoreserverevents.Utils.Pair;
import org.liny.notify.FastLogger;
import org.liny.notify.LinyColor;
import org.liny.notify.NNotifyEngine;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class ESM implements EventsManager {
    private final LinkedList<Pair<ServerEvent, String>> events = new LinkedList<>();

    public @NotNull Boolean isEventsEmpty () {
        return this.events.isEmpty();
    }

    public @NotNull List<Pair<ServerEvent, Boolean>> addEvents (@NotNull ServerEvent... events) {
        List<Pair<ServerEvent, Boolean>> isAdded = new ArrayList<>();
        for (ServerEvent event : events) {
            AtomicBoolean currentlyAdded = new AtomicBoolean(true);
            this.events.parallelStream().forEach(registeredEvent -> {
                if (registeredEvent.getValue().equalsIgnoreCase(event.getName())) currentlyAdded.set(false);
            });
            if (currentlyAdded.get()) this.events.add(new Pair<>(event, event.getName()));
            isAdded.add(new Pair<>(event, currentlyAdded.get()));
        }
        return isAdded;
    }

    public @NotNull List<Pair<String, Boolean>> removeEvents (@NotNull String... events) {
        List<Pair<String, Boolean>> isRemoved = new ArrayList<>();
        for (String eventName : events) {
            AtomicReference<Pair<ServerEvent, String>> forRemove = new AtomicReference<>(null);
            this.events.parallelStream().forEach(event -> {
                if (event.getValue().equalsIgnoreCase(eventName)) forRemove.set(event);
            });
            if (forRemove.get() != null) {
                this.events.remove(forRemove.get());
                isRemoved.add(new Pair<>(eventName, true));
            } else isRemoved.add(new Pair<>(eventName, false));
        }
        return isRemoved;
    }

    @SuppressWarnings({"unchecked"})
    public @NotNull LinkedList<Pair<ServerEvent, String>> getEvents () {
        return (LinkedList<Pair<ServerEvent, String>>) this.events.clone(); //clone for safety
    }

    public @NotNull Optional<ServerEvent> getEvent (@NotNull String eventName) {
        AtomicReference<ServerEvent> eventNullable = new AtomicReference<>(null);
        this.events.parallelStream().forEach(pair -> {
            if (pair.getValue().equalsIgnoreCase(eventName)) eventNullable.set(pair.getKey());
        });
        return Optional.ofNullable(eventNullable.get());
    }

    public @NotNull Optional<ServerEvent> getEvent (@NotNull Class<? extends ServerEvent> eventClass) {
        AtomicReference<ServerEvent> eventNullable = new AtomicReference<>(null);
        this.events.parallelStream().forEach(pair -> {
            if (pair.getKey().getClass().equals(eventClass)) eventNullable.set(pair.getKey());
        });
        return Optional.ofNullable(eventNullable.get());
    }

    public @Nullable ServerEvent getRandomServerEvent () throws EventsEmpty {
        if (this.events.isEmpty()) throw new EventsEmpty();

        List<ServerEvent> shoot = new ArrayList<>();
        this.events.parallelStream().forEach(pair -> {
            if (pair.getKey().getPercent() <= Math.random()) {
                shoot.add(pair.getKey());
            }
        });

        ServerEvent shotEvent;

        if (shoot.isEmpty()) return getRandomServerEvent();
        else shotEvent = shoot.get(ThreadLocalRandom.current().nextInt(shoot.size()));

        if (shotEvent.canSpawn()) return shotEvent;
        else return null;
    }

    public void initTask (@NotNull Long time, @NotNull TimeUnit unit, @NotNull Plugin main /* for starting of other plugins */) throws ESMCurrentlyWorking {
        if (currentTask != null) throw new ESMCurrentlyWorking(); //else for joke lol
        Bukkit.getAsyncScheduler().runAtFixedRate(
                main,
                eventTask,
                0,
                time, unit
        );
    }

    @Nullable protected static ScheduledTask currentTask = null; //stop overrunning
    protected final Consumer<ScheduledTask> eventTask = task -> {
        try {
            if (currentTask == null) currentTask = task;
            if (task.isCancelled()) return;
            if (this.isEventsEmpty()) return;

            ServerEvent futureEvent = null;
            if (this.data.getFutureEvent().isEmpty()) {
                if (this.data.getCurrentEvent().isPresent()) {
                    if (!this.data.getCurrentEvent().get().isClosed()) return;
                }
                futureEvent = this.getRandomServerEvent();
                if (futureEvent == null) return;
                this.data.setFutureEvent(futureEvent);
            }
            if (futureEvent == null) futureEvent = this.data.getFutureEvent().get();

            if (this.data.getCurrentEvent().isPresent()) {
                if (this.data.getCurrentEvent().get().isClosed()) {
                    this.data.setPreviousEvent(this.data.getCurrentEvent().get());
                    this.data.setCurrentEvent(null);
                }
            }

            if (this.getFutureEventTime() < 0) {
                if (futureEvent.canSpawn()) {
                    if (this.data.getCurrentEvent().isPresent()) {
                        this.data.setPreviousEvent(this.data.getCurrentEvent().get());
                    }
                    this.data.setCurrentEvent(futureEvent);
                    this.data.setFutureEvent(null);

                    try {
                        Bukkit.getScheduler().runTask(
                                this.data.getCurrentEvent().get().getMain(),
                                this.data.getCurrentEvent().get()::start
                        );
                    } catch (Throwable ignored) {}

                } else {
                    ServerEvent nextEvent;
                    try {
                        nextEvent = getRandomServerEvent();
                    } catch (EventsEmpty ignored) {
                        return;
                    }
                    if (nextEvent == null) return;

                    try {
                        Bukkit.getScheduler().runTask(
                                nextEvent.getMain(),
                                nextEvent::start
                        );
                    } catch (Throwable ignored) {}

                    this.data.setPreviousEvent(futureEvent);
                    this.data.setCurrentEvent(nextEvent);
                    this.data.setFutureEvent(null);
                }
                this.timeOfLastEvent = ms2s(System.currentTimeMillis());
            }
        } catch (Throwable e) {
            Bukkit.getConsoleSender().sendMessage(NNotifyEngine.getAcceptMessage("Error on main task -> "+e.getMessage(), false));

            Bukkit.getConsoleSender().sendMessage(NNotifyEngine.getAcceptMessage("<liny.red>Stacktrace -> ", false));
            e.printStackTrace(System.err);
            /*
            Bukkit.getConsoleSender().sendMessage(NNotifyEngine.getAcceptMessage("Killing EventCore task -> "+e.getLocalizedMessage(), false));
            if (this.closeTask()) {
                Bukkit.getConsoleSender().sendMessage(NNotifyEngine.getAcceptMessage("Successfully closing EventCore task", true));
            } else {
                Bukkit.getConsoleSender().sendMessage(NNotifyEngine.getAcceptMessage("Unsuccessfully closing EventCore task", false));
            }
            task.cancel();
            currentTask = null;
            */
        }
    };

    protected Long timeOfLastEvent = ms2s(System.currentTimeMillis());
    private final EventData data = new EventData();

    public @NotNull EventData getData() {
        return this.data;
    }

    @Override
    public @NotNull Long getFutureEventTime() {
        if (this.data.getFutureEvent().isPresent())
            return (timeOfLastEvent+this.data.getFutureEvent().get().getTimeSeconds()) - ms2s(System.currentTimeMillis());
        else return 0L;
    }

    @Override
    public @NotNull String getFutureEventTimeHours() {
        if (this.getFutureEventTime() < 0) {
            FastLogger.warning("EventManager", "Time < 0");
            return "0";
        }

        long hours = this.getFutureEventTime() / 3600;
        long minutes = (this.getFutureEventTime() % 3600) / 60;
        long seconds = this.getFutureEventTime() % 60;

        return String.format(LinyColor.GREEN+"%d"+LinyColor.WHITE+ "ч. "
                +LinyColor.GREEN+"%d"+LinyColor.WHITE+"мин."
                +LinyColor.GREEN+"%d"+LinyColor.WHITE+"сек.", hours, minutes, seconds);
    }

    @Override
    public @NotNull Boolean isTaskWorking() {
        return currentTask != null;
    }

    @Override
    public @NotNull Boolean isEventRegistered(String name) {
        return this.events.stream()
                .anyMatch(pair -> pair.getValue().equalsIgnoreCase(name));
    }

    @Override
    public @NotNull Boolean isEventRegistered(Class<? extends ServerEvent> eventClass) {
        return this.events.stream()
                .anyMatch(pair -> pair.getKey().getClass().equals(eventClass));
    }

    @Override
    public @NotNull Boolean closeTask() {
        if (currentTask == null) return true;
        try {
            currentTask.cancel();
            currentTask = null;
        } catch (Throwable ignored) {
            return false;
        }
        return true;
    }

    private static Long ms2s (Long time) {
        return time / 1000;
    }
}
