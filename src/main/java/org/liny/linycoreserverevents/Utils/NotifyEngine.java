package org.liny.linycoreserverevents.Utils;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class NotifyEngine {

    private static final String first_bracket = "[";
    private static final String second_bracket = "]";
    private static final String server_name = "§x§4§d§c§8§a§aL§x§6§3§a§e§b§fi§x§7§9§9§4§d§4n§x§8§f§7§a§e§8y§x§a§5§6§0§f§dX";

    private static final String cancel = ChatColor.RED + "❌";
    private static final String accept = ChatColor.GREEN + "✔";

    private static final ChatColor server_name_color = ChatColor.LIGHT_PURPLE;
    private static final ChatColor brackets_color = ChatColor.GRAY;
    private static final ChatColor text_color = ChatColor.WHITE;
    private static final ChatColor player_color = ChatColor.WHITE;

    public static @NotNull String getServerName () {
        return server_name;
    }

    public static @NotNull String getNotifyMessage (@NotNull String notify) {
        return brackets_color + first_bracket + server_name_color + server_name + brackets_color + second_bracket + " " + text_color + notify;
    }

    public static void playSoundForPlayer(@NotNull Player player, @NotNull Sound sound) {
        player.playSound(player.getLocation(), sound, 0.5F, 0.5F);
    }

    public static void playSound(@NotNull Location location, @NotNull Sound sound) {
        location.getWorld().playSound(location, sound, 0.5F, 0.5F);
    }

    public static @NotNull String getPlayerMessage (@NotNull Player p1, @NotNull Player p2, @NotNull String message) {
        return brackets_color + first_bracket + p1.getDisplayName() + " -> " + p2.getDisplayName() + second_bracket + " " + text_color + message;
    }

    public static @NotNull String getAcceptMessage (@NotNull String message, @NotNull Boolean isAccept) {
        String tmp = isAccept ? accept : cancel;
        return brackets_color + first_bracket + tmp + brackets_color + second_bracket + " " + text_color + message;
    }

    public static @NotNull BaseComponent[] getTeleportationMessage (@NotNull Location location) {
        return new ComponentBuilder(ChatColor.AQUA + "╔ " + ChatColor.WHITE + "Телепортируем вас на координаты " + ChatColor.WHITE + "X:" + ChatColor.GREEN + location.getBlockX() + ChatColor.WHITE + " Y:" + ChatColor.GREEN + location.getBlockY() + ChatColor.WHITE + " Z:" + ChatColor.GREEN + location.getBlockZ())
                .append("\n")
                .append(ChatColor.AQUA + "╚ ")
                .append(ChatColor.RED + "[ОТМЕНИТЬ]").event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpc")).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "Отмена телепортации").create()))
                .create();

    }

    public static @NotNull BaseComponent[] getTeleportationPlayerMessage (@NotNull Player player) {
        return new ComponentBuilder(ChatColor.AQUA + "╔ " + ChatColor.WHITE + " К вам хочет телепортироваться игрок " + ChatColor.AQUA + player.getDisplayName())
                .append("\n")
                .append(ChatColor.AQUA + "╚ " + ChatColor.GREEN + " [ПРИНЯТЬ]").event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa accept")).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GREEN + "Принять запрос").create()))
                .append(ChatColor.RED + " [ОТМЕНИТЬ]").event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa cancel")).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "Отменить запрос").create()))
                .create();

    }

    public static @NotNull String getNoPermission (@NotNull String cmd) {
        return brackets_color + first_bracket + server_name_color + server_name + brackets_color + second_bracket + " " + text_color +
                "У вас не хватает прав на выолнение команды: " + ChatColor.DARK_RED + cmd;
    }

    public static @NotNull String getNoPermission () {
        return brackets_color + first_bracket + server_name_color + server_name + brackets_color + second_bracket + " " + text_color +
                "У вас не хватает прав на выолнение данной команды";
    }

    @Deprecated
    public static @NotNull String getPlayerBanned () {

        String openingLine = ChatColor.RED + "" + ChatColor.BOLD + "┏                                             ┑";
        String endingLine = ChatColor.RED + "" + ChatColor.BOLD + "┕                                             ┙";

        StringBuilder tmp = new StringBuilder(openingLine).append("\n\n");

        tmp.append("§x§f§b§0§0§0§0И§x§f§b§0§9§0§5г§x§f§b§1§1§0§aр§x§f§b§1§a§0§eо§x§f§b§2§2§1§3в§x§f§c§2§b§1§8о§x§f§c§3§3§1§dй §x§f§c§3§c§2§2а§x§f§c§4§4§2§6к§x§f§c§4§d§2§bк§x§f§c§5§6§3§0а§x§f§c§5§e§3§5у§x§f§c§6§7§3§aн§x§f§c§6§f§3§eт §x§f§c§7§8§4§3з§x§f§d§8§0§4§8а§x§f§d§8§9§4§dб§x§f§d§9§1§5§2а§x§f§d§9§a§5§6н§x§f§d§a§2§5§bе§x§f§d§a§b§6§0н§r")
                        .append("\n\n");

        tmp.append(ChatColor.DARK_GRAY + "BAN ID: " + ChatColor.WHITE + "%s\n");
        tmp.append(ChatColor.DARK_GRAY + "Причина: " + ChatColor.WHITE + "%s\n");
        tmp.append(ChatColor.DARK_GRAY + "Дата: " + ChatColor.WHITE + "%s\n");
        tmp.append(ChatColor.DARK_GRAY + "Забанил: " + ChatColor.WHITE + "%s\n")
                .append("\n\n");

        tmp.append(ChatColor.WHITE + "Вы можете купить разбан на нашем сайте\n" + ChatColor.AQUA + "LinyX.ru\n\n");

        tmp.append(endingLine);

        return tmp.toString();

    }

    @Deprecated
    public static @NotNull Component getPlayerBannedAsComponent (String id, String reason, String date, String who) {

        String openingLine = ChatColor.RED + "" + ChatColor.BOLD + "┏                                             ┑";
        String endingLine = ChatColor.RED + "" + ChatColor.BOLD + "┕                                             ┙";

        StringBuilder tmp = new StringBuilder(openingLine).append("\n\n");

        tmp.append("§x§f§b§0§0§0§0И§x§f§b§0§9§0§5г§x§f§b§1§1§0§aр§x§f§b§1§a§0§eо§x§f§b§2§2§1§3в§x§f§c§2§b§1§8о§x§f§c§3§3§1§dй §x§f§c§3§c§2§2а§x§f§c§4§4§2§6к§x§f§c§4§d§2§bк§x§f§c§5§6§3§0а§x§f§c§5§e§3§5у§x§f§c§6§7§3§aн§x§f§c§6§f§3§eт §x§f§c§7§8§4§3з§x§f§d§8§0§4§8а§x§f§d§8§9§4§dб§x§f§d§9§1§5§2а§x§f§d§9§a§5§6н§x§f§d§a§2§5§bе§x§f§d§a§b§6§0н§r")
                .append("\n\n");

        tmp.append(ChatColor.DARK_GRAY + "BAN ID: " + ChatColor.WHITE + id + "\n");
        tmp.append(ChatColor.DARK_GRAY + "Причина: " + ChatColor.WHITE + reason + "%s\n");
        tmp.append(ChatColor.DARK_GRAY + "Дата: " + ChatColor.WHITE + date + "%s\n");
        tmp.append(ChatColor.DARK_GRAY + "Забанил: " + ChatColor.WHITE + who + "%s\n")
                .append("\n\n");

        tmp.append(ChatColor.WHITE + "Вы можете купить разбан на нашем сайте\n" + ChatColor.AQUA + "LinyX.ru\n\n");

        tmp.append(endingLine);

        return Component.text(tmp.toString());

    }

    @Deprecated
    public static BaseComponent[] join (@NotNull ChatColor color, @NotNull String... lines) {

        String openingLine = color + "╔" + ChatColor.RESET;
        String continueLine = "\n" + color + "║  " + ChatColor.RESET;
        String endingLine = "\n" + color + "╚" + ChatColor.RESET;

        ComponentBuilder eventString = new ComponentBuilder(openingLine);

        Arrays.stream(lines).forEach(string -> {

            eventString.append(continueLine).append(string);

        });

        eventString.append(endingLine);

        return eventString.create();

    }

}
