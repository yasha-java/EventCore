package org.liny.linycoreserverevents.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.liny.linycoreserverevents.EventCore;
import org.liny.linycoreserverevents.Utils.NotifyEngine;

import java.util.ArrayList;
import java.util.List;

public class EventCommand {

    public static class EventsExecator implements CommandExecutor {
        private final EventCore main;

        public EventsExecator(EventCore main) {
            this.main = main;
        }

        @Override
        public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
            if (args.length < 1) {
                commandSender.sendMessage(NotifyEngine.getAcceptMessage("Вы "+ChatColor.RED+"не правильно"+ChatColor.WHITE+" ввели команду!", false));
                return true;
            } else if (args[0].equalsIgnoreCase("delay")) {
                commandSender.sendMessage(NotifyEngine.getAcceptMessage("Следующий эвент: "+ChatColor.RESET+
                        this.main.getEventSpawnManager().getNextServerEvent().getName()+ChatColor.WHITE+". До него осталось: "+ChatColor.GREEN+
                        this.main.getEventSpawnManager().getTimeForNextEvent()+ChatColor.WHITE+" секунд", true));
                return true;
            } else if (args[0].equalsIgnoreCase("all")) {
                if (this.main.getEventSpawnManager().getRegisteredEvents().isEmpty()) {
                    commandSender.sendMessage(NotifyEngine.getAcceptMessage("Нету зарегистрированных эвентов! Обратитесь к администрации!", false));
                    return true;
                }
                StringBuilder allEvents = new StringBuilder(NotifyEngine.getNotifyMessage("Вот список существующих эвентов"));
                this.main.getEventSpawnManager().getRegisteredEvents().keySet().forEach(name -> {
                    allEvents.append("\n"+ChatColor.GREEN + "" + ChatColor.BOLD + "✧ " + ChatColor.RESET+name);
                });
                commandSender.sendMessage(allEvents.toString());
                return true;
            } else if (args[0].equalsIgnoreCase("start")) {
                if (!commandSender.hasPermission("org.LinyCoreV2.administrator")) {
                    commandSender.sendMessage(NotifyEngine.getNoPermission("/events start"));
                    return true;
                } else if (this.main.getEventSpawnManager().isEventRegistered(args[1])) {
                    this.main.getEventSpawnManager().getRegisteredServerEvent(args[1]).start();
                    commandSender.sendMessage(NotifyEngine.getAcceptMessage("Вы успешно запустили эвент - "+ChatColor.RESET+
                            this.main.getEventSpawnManager().getRegisteredServerEvent(args[1]).getName(), true)
                        );
                    return true;
                } else {
                    StringBuilder allEvents = new StringBuilder(NotifyEngine.getAcceptMessage("Такого эвента не существует! Вот список всех эвентов: ", false) + ChatColor.GREEN);
                    this.main.getEventSpawnManager().getRegisteredEvents().keySet().forEach(name -> {
                        allEvents.append("\n"+ChatColor.GREEN + "" + ChatColor.BOLD + "✧ " + ChatColor.RESET+name);
                    });
                    commandSender.sendMessage(allEvents.toString());
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("disable")) {
                if (!commandSender.hasPermission("org.LinyCoreV2.administrator")) {
                    commandSender.sendMessage(NotifyEngine.getNoPermission("/events disable"));
                } else {
                    this.main.isStopped = false;
                    commandSender.sendMessage(NotifyEngine.getAcceptMessage("Вы успешно выключили спавн эвентов!", true));
                }
                return true;
            } else if (args[0].equalsIgnoreCase("enable")) {
                if (!commandSender.hasPermission("org.LinyCoreV2.administrator")) {
                    commandSender.sendMessage(NotifyEngine.getNoPermission("/events enable"));
                } else {
                    this.main.isStopped = true;
                    commandSender.sendMessage(NotifyEngine.getAcceptMessage("Вы успешно включили спавн эвентов!", true));
                }
                return true;
            } else if (args[0].equalsIgnoreCase("last")) {
                if (this.main.getEventSpawnManager().getCurrentServerEvent() == null) {
                    commandSender.sendMessage(NotifyEngine.getAcceptMessage("Еще "+ChatColor.RED+"не проходило"+ChatColor.WHITE+
                            " никак эвентов! Используйте "+ChatColor.GRAY+"/events delay", true));
                } else {
                    commandSender.sendMessage(NotifyEngine.getAcceptMessage("Прошлый эвент был: "+
                            ChatColor.RESET+this.main.getEventSpawnManager().getCurrentServerEvent().getName(), true));
                }
                return true;
            } else {
                int RayID = ((int)(Math.random()*1000));
                commandSender.sendMessage(NotifyEngine.getAcceptMessage("При выполнении команды что то пошло не так! Обратитесь к технической администрации! RayID: "+ChatColor.RED+RayID, false));
                Bukkit.getConsoleSender().sendMessage(("{RayID-%s} Exception of command " + command.getName() + " args [" + String.join(" ", args) + "]").formatted(RayID));
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
                if (this.main.getEventSpawnManager().getRegisteredEvents().isEmpty()) {
                    complete.add("Эвентов не зарегистрировано, обратитесь к технической администрации!");
                } else {
                    complete.addAll(this.main.getEventSpawnManager().getRegisteredEvents().keySet());
                }
                return complete;
            } else {
                complete.add("Больше ничего писать не нужно");
                return complete;
            }
        }

    }

}