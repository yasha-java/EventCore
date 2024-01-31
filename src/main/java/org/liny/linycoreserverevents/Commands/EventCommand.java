package org.liny.linycoreserverevents.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
            if (args.length < 1) {
                commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Вы "+LinyColor.RED.toNMSAmpersand()+"не правильно"+
                        LinyColor.WHITE.toNMSAmpersand()+" ввели команду!", false));
                return true;
            } else if (args[0].equalsIgnoreCase("delay")) {
                Optional<ServerEvent> futureEvent = this.main.getManager().getData().getFutureEvent();
                if (futureEvent.isEmpty()) {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Следующего эвента <liny.red>не обнаружено<liny.white>. Скорее всего это ошибка! Администрация скоро решит эту проблему", false));
                } else {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Следующий эвент: " + LinyColor.GREEN.toNMSAmpersand() +
                            futureEvent.get().getName() + LinyColor.WHITE.toNMSAmpersand() + ". До него осталось: " + LinyColor.GREEN.toNMSAmpersand() +
                            this.main.getManager().getFutureEventTime() + LinyColor.WHITE.toNMSAmpersand() + " секунд", true));
                }
                return true;
            } else if (args[0].equalsIgnoreCase("all")) {
                if (this.main.getManager().isEventsEmpty()) {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Нету зарегистрированных эвентов! Обратитесь к администрации!", false));
                    return true;
                }

                TextComponent.Builder allEvents = Component.text().append(NNotifyEngine.getNotifyMessage("Вот список существующих эвентов"));
                this.main.getManager().getEvents().forEach(pair -> {
                    allEvents.appendNewline().append(Component.text(LinyColor.GREEN.toNMSAmpersand() + "✧ " + LinyColor.WHITE.toNMSAmpersand() + pair.getValue()));
                });

                commandSender.sendMessage(allEvents.asComponent());

                return true;
            } else if (args[0].equalsIgnoreCase("start")) {
                if (!commandSender.hasPermission("org.LinyCoreV2.administrator")) {
                    commandSender.sendMessage(NNotifyEngine.getNoPermission("/events start"));
                    return true;
                }

                Optional<ServerEvent> event = this.main.getManager().getEvent(args[1]);

                if (event.isEmpty()) {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Такого эвента не существует!", false));
                    return true;
                }

                if (!event.get().isClosed()) {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Эвент в данное время запущен!", false));
                    return true;
                }

                try {
                    event.get().start();
                } catch (Throwable ignored) {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Произошла ошибка при запуске эвента!", false));
                } finally {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Эвент запущен!", true));
                }
            } else if (args[0].equalsIgnoreCase("disable")) {
                if (!commandSender.hasPermission("org.LinyCoreV2.administrator")) {
                    commandSender.sendMessage(NNotifyEngine.getNoPermission("/events disable"));
                } else {
                    if (this.main.getManager().closeTask()) {
                        commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Вы успешно выключили основной цикл!", true));
                    } else {
                        commandSender.sendMessage(NNotifyEngine.getAcceptMessage("роизошла ошибка при выключении основного цикла!", false));
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
                    } finally {
                        commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Вы успешно включили спавн эвентов!", true));
                    }
                    return true;
                }
                return true;
            } else if (args[0].equalsIgnoreCase("last")) {
                Optional<ServerEvent> previousEvent = this.main.getManager().getData().getPreviousEvent();

                if (previousEvent.isPresent()) {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("Прошлый эвент был: "+
                            LinyColor.GREEN.toNMSAmpersand()+previousEvent.get().getName(), true));
                } else {
                    commandSender.sendMessage(NNotifyEngine.getAcceptMessage("На сервере еще <liny.red>не проходило<liny.white> эвентов", false));
                }

                return true;
            }
            return true;
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
            Bukkit.getConsoleSender().sendMessage(String.join(" ", args));
            if (args.length < 2) {
                complete.add("delay");
                complete.add("all");
                complete.add("last");
                if (commandSender.hasPermission("org.LinyCore.administrator")) {
                    complete.add("start");
                    complete.add("enable");
                    complete.add("disable");
                }
                return complete;
            } else if (args[0].equalsIgnoreCase("start")){
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