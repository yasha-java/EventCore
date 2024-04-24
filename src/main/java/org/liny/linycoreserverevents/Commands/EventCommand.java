package org.liny.linycoreserverevents.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.liny.linycoreserverevents.Abstract.ServerEvent;
import org.liny.linycoreserverevents.EventCore;
import org.liny.linycoreserverevents.Exceptions.ESMCurrentlyWorking;
import org.liny.notify.LinyColor;
import org.liny.notify.NNotifyEngine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class EventCommand {

    public static class EventsExecutor implements CommandExecutor {
        private final EventCore main;

        public EventsExecutor(EventCore main) {
            this.main = main;
        }

        @Override
        @SuppressWarnings("unstable")
        public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
            if (args.length < 1) {
                commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Вы " + LinyColor.RED.toNMSAmpersand() + "не правильно" +
                        LinyColor.WHITE.toNMSAmpersand() + " ввели команду!", false));
                return true;
            } else if (args[0].equalsIgnoreCase("delay")) {
                Optional<ServerEvent> futureEvent = this.main.getManager().getData().getFutureEvent();
                if (futureEvent.isEmpty()) {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Следующего эвента <liny.red>не обнаружено<liny.white>. Скорее всего это ошибка! Администрация скоро решит эту проблему", false));
                } else {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Следующий эвент: <liny.green>" +
                            futureEvent.get().getName() + "<liny.white>. До него осталось: " +
                            this.main.getManager().getFutureEventTimeHours(), true));
                }
                return true;
            } else if (args[0].equalsIgnoreCase("all")) {
                if (this.main.getManager().isEventsEmpty()) {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Нету зарегистрированных эвентов! Обратитесь к администрации!", false));
                    return true;
                }

                TextComponent.Builder allEvents = Component.text().append(NNotifyEngine.getNotifyMessage("Вот список существующих эвентов"));
                this.main.getManager().getEvents().forEach(pair -> {
                    allEvents.appendNewline()
                            .append(Component.text("✧").color(LinyColor.GREEN.toColor()).decorate(TextDecoration.BOLD))
                            .appendSpace()
                            .append(Component.text(pair.getValue()))
                            .color(LinyColor.WHITE.toColor());
                });

                commandSender.sendMessage(allEvents.asComponent());

                return true;
            } else if (args[0].equalsIgnoreCase("start")) {
                if (!commandSender.hasPermission("org.LinyCoreV2.administrator")) {
                    commandSender.sendMessage(NNotifyEngine.getNoPermission("/events start"));
                    return true;
                }

                LinkedList<String> name = new LinkedList<>(List.of(args));
                name.remove(0);
                String compiledName = String.join(" ", name);

                Optional<ServerEvent> event = this.main.getManager().getEvent(compiledName);

                Bukkit.getConsoleSender().sendMessage(NNotifyEngine.getNotifyMessage("Finding ServerEvent -> " + compiledName));

                if (event.isEmpty()) {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Такого эвента не существует!", false));
                    Bukkit.getConsoleSender().sendMessage(NNotifyEngine.getAcceptMessage("Event not found -> " + compiledName + "!", false));
                    return true;
                }

                if (!event.get().isClosed()) {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Эвент в данное время запущен!", false));
                    Bukkit.getConsoleSender().sendMessage(NNotifyEngine.getAcceptMessage("Event currently working -> " + compiledName + "!", false));
                    return true;
                }

                try {
                    event.get().start();
                } catch (Throwable e) {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Произошла ошибка при запуске эвента!", false));
                    Bukkit.getConsoleSender().sendMessage(NNotifyEngine.getAcceptMessage("Event " + compiledName + " thrown exception -> " + e.getLocalizedMessage() + "!", false));
                }

                commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Эвент запущен!", true));
                this.main.getManager().getData().setCurrentEvent(event.get());
                Bukkit.getConsoleSender().sendMessage(NNotifyEngine.getAcceptMessage("Event started -> " + compiledName + "!", true));
                return true;
            } else if (args[0].equalsIgnoreCase("disable")) {
                if (!commandSender.hasPermission("org.LinyCoreV2.administrator")) {
                    commandSender.sendMessage(NNotifyEngine.getNoPermission("/events disable"));
                } else {
                    if (this.main.getManager().closeTask()) {
                        commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Вы успешно выключили основной цикл!", true));
                    } else {
                        commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Произошла ошибка при выключении основного цикла!", false));
                    }
                }
                return true;
            } else if (args[0].equalsIgnoreCase("enable")) {
                if (!commandSender.hasPermission("org.LinyCoreV2.administrator")) {
                    commandSender.sendMessage(NNotifyEngine.getNoPermission("/events enable"));
                } else {
                    try {
                        this.main.getManager().initTask(1L, TimeUnit.SECONDS, this.main);
                    } catch (ESMCurrentlyWorking e) {
                        commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Произошла ошибка при включении основного цикла!", false));
                        return true;
                    }

                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Вы успешно включили спавн эвентов!", true));
                    return true;
                }
                return true;
            } else if (args[0].equalsIgnoreCase("last")) {
                Optional<ServerEvent> previousEvent = this.main.getManager().getData().getPreviousEvent();

                if (previousEvent.isPresent()) {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Прошлый эвент был: " +
                            LinyColor.GREEN.toNMSAmpersand() + previousEvent.get().getName(), true));
                } else {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("На сервере еще <liny.red>не проходило<liny.white> эвентов", false));
                }

                return true;
            } else if (args[0].equalsIgnoreCase("version")) {
                commandSender.sendMessage(NNotifyEngine.getNotifyMessage("Версия EventCore: <liny.green>" + this.main.getPluginMeta().getVersion() + " by LinyTech"));
                return true;
            } else if (args[0].equalsIgnoreCase("current")) {
                Optional<ServerEvent> currentEvent = this.main.getManager().getData().getCurrentEvent();

                if (currentEvent.isPresent()) {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Сейчас проходит эвент: " +
                            LinyColor.GREEN.toNMSAmpersand() + currentEvent.get().getName(), true));
                } else {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("В данное время "+LinyColor.RED+"нету запущенных"+LinyColor.WHITE+" эвентов!", false));
                }

                return true;
            } else if (args[0].equalsIgnoreCase("status")) {
                if (!this.main.getManager().isTaskWorking()) {
                    commandSender.sendMessage(NNotifyEngine.getNotifyMessage("Спавн эвентов "+LinyColor.RED+"отключен"+LinyColor.WHITE+"!"));
                } else {
                    commandSender.sendMessage(NNotifyEngine.getNotifyMessage("На данный момент спавн эвентов "+LinyColor.GREEN+"включен"+LinyColor.WHITE+"!"));
                }

                return true;
            } else if (args[0].equalsIgnoreCase("stop")) {
                if (!commandSender.hasPermission("org.LinyCoreV2.administrator")) {
                    commandSender.sendMessage(NNotifyEngine.getNoPermission("/events stop"));
                    return true;
                }

                LinkedList<String> name = new LinkedList<>(List.of(args));
                name.remove(0);
                String compiledName = String.join(" ", name);

                Optional<ServerEvent> event = this.main.getManager().getEvent(compiledName);

                Bukkit.getConsoleSender().sendMessage(NNotifyEngine.getNotifyMessage("Finding ServerEvent -> " + compiledName));

                if (event.isEmpty()) {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Такого эвента не существует!", false));
                    Bukkit.getConsoleSender().sendMessage(NNotifyEngine.getAcceptMessage("Event not found -> " + compiledName + "!", false));
                    return true;
                }

                if (event.get().isClosed()) {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Эвент в данное время не запущен!", false));
                    Bukkit.getConsoleSender().sendMessage(NNotifyEngine.getAcceptMessage("Event not working -> " + compiledName + "!", false));
                    return true;
                }

                try {
                    event.get().stop();
                } catch (Throwable e) {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Произошла ошибка при остановке эвента!", false));
                    Bukkit.getConsoleSender().sendMessage(NNotifyEngine.getAcceptMessage("Event " + compiledName + " thrown exception -> " + e.getLocalizedMessage() + "!", false));
                }

                commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Эвент остановлен!", true));
                this.main.getManager().getData().setCurrentEvent(event.get());
                Bukkit.getConsoleSender().sendMessage(NNotifyEngine.getAcceptMessage("Event stopped -> " + compiledName + "!", true));
                return true;
            } else {
                commandSender.sendMessage(NNotifyEngine.getNotifyMessage("Вы <liny.red>не правильно<liny.white> ввели команду! Пожалуйста, пользуйтесь подсказками."));
                return true;
            }
        }
    }

    public static class EventsTabCompleter implements TabCompleter {
        private final EventCore main;

        public EventsTabCompleter(EventCore main) {
            this.main = main;
        }

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
            List<String> complete = new ArrayList<>();
            if (args.length < 2) {
                complete.add("delay");
                complete.add("all");
                complete.add("last");
                complete.add("version");
                complete.add("status");
                complete.add("current");
                if (commandSender.hasPermission("org.LinyCore.administrator")) {
                    complete.add("start");
                    complete.add("enable");
                    complete.add("stop");
                    complete.add("disable");
                }
                return complete;
            } else if (args[0].equalsIgnoreCase("start") || args[0].equalsIgnoreCase("stop")){
                if (this.main.getManager().isEventsEmpty()) {
                    complete.add("Эвентов не зарегистрировано, обратитесь к технической администрации!");
                } else {
                    this.main.getManager().getEvents().parallelStream().forEach(pair -> {
                        complete.add(pair.getValue());
                    });
                }
                return complete;
            } else {
                complete.add("Больше ничего писать не нужно");
                return complete;
            }
        }
    }

}