package com.daansander.reporter.command;

import com.daansander.reporter.Report;
import com.daansander.reporter.Reporter;
import com.daansander.reporter.utils.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Daan on 4-1-2016.
 */
public class ReportCommand implements CommandExecutor {

    private Map<String, UUID> response = new HashMap<>();

    //TODO: Set reported to uuid

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("report") && sender instanceof Player) {
            final Player player = (Player) sender;
            if (!player.hasPermission("reporter.report")) {
                String m = Reporter.getPlugin().getConfig().getString("no-permission").replaceAll("&", "§");
                player.sendMessage(m);
                return true;
            }

            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Insufficient arguments usage: /report <player> <reason>");
                return true;
            }

            if(!Reporter.getPlugin().canReport(player)) {
                String cd = Reporter.getPlugin().getConfig().getString("cooldown-message").replaceAll("&", "§").replaceAll("%time%",
                        Reporter.getPlugin().getCooldowns().get(player.getUniqueId().toString()) + "");
                player.sendMessage(cd);
                return true;
            }

            String reason = "";

            List<String> strings = new ArrayList<>(Arrays.asList(args));

            strings.remove(0);

            String reporter = player.getName();
            UUID reported = null;

            final String targetName = args[0];

            if (Reporter.getPlugin().useUUID) {
                Player target = Bukkit.getPlayer(targetName);

                if (target == null) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Player " + args[0] + " does not exist or is not online trying UUID fetcher");

                    player.sendMessage(ChatColor.RED + "Player is not online or does not exist trying to get player please be patient...");

                    reported = UUIDFetcher.getUUID(args[0]);

                } else {
                    reported = target.getUniqueId();
                }

                if (reported == null) {
                    player.sendMessage(ChatColor.RED + "The player " + args[0] + " does not exist!");
                    return true;
                }

            }
            for (String st : strings) {
                reason = reason + " " + st;
            }

            String mesage = Reporter.getPlugin().getConfig().getString("message-report").replaceAll("&", "§").replaceAll("%target%", Bukkit.getOfflinePlayer(reported).getName())
                    .replaceAll("%reason%", reason);

            player.sendMessage(mesage);

            Report report = new Report(reported.toString(), reporter, reason);

            String am = Reporter.getPlugin().getConfig().getString("message-adminnote").replaceAll("&", "§").replaceAll("%target%", Bukkit.getOfflinePlayer(reported).getName())
                    .replaceAll("%reason%", reason);

            Reporter.getPlugin().sendAdminMessage(am);

            Reporter.getPlugin().getCooldowns().put(player.getUniqueId().toString(), Reporter.getPlugin().getConfig().getLong("cooldown"));

            response.clear();
            report.saveReport();
        }
        return true;
    }
}
