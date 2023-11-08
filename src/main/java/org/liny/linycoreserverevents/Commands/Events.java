package org.liny.linycoreserverevents.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.liny.linycoreserverevents.LinyCoreServerEvents;

public class Events implements CommandExecutor {

    private final LinyCoreServerEvents main;

    public Events(LinyCoreServerEvents main) {

        this.main = main;

    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!commandSender.hasPermission("org.LinyCoreV2.EventAdministration")) return true;

        if (args.length < 1) {

            commandSender.sendMessage(ChatColor.GREEN + "Установлен EventFlag.isStopped:" + this.main.isStopped);

            this.main.isStopped = !this.main.isStopped;

        } else {

            if (this.main.getEventSpawnManager().isEventRegistered(args[0])) {

                this.main.getEventSpawnManager().getRegisteredServerEvent(args[0]).start();
                commandSender.sendMessage(ChatColor.GREEN + "Вы успешно запустили ServerEvent:" + this.main.getEventSpawnManager().getRegisteredServerEvent(args[0]).getName());

            } else {

                StringBuilder tmp = new StringBuilder(ChatColor.RED + "Такого эвента не существует, вот подробный список о зарегистрированных эвентах: " + ChatColor.GREEN);

                this.main.getEventSpawnManager()
                        .getRegisteredEvents()
                        .keySet()
                        .forEach(name -> tmp.append("\n").append(name));

                commandSender.sendMessage(tmp.toString());

            }

        }

        return true;

    }

}
