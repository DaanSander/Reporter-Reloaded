package com.daansander.reporter.command;

import com.daansander.reporter.Reporter;
import com.daansander.reporter.utils.UUIDFetcher;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Daan on 7-1-2016.
 */
public class ReportsCommand implements CommandExecutor {


    public static void main(String[] args) {
        boolean b = ("all".equalsIgnoreCase("all")) ? true : false;
        System.out.println(b);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("reports") && sender instanceof Player) {
            Player player = (Player) sender;

            if (!player.hasPermission("reporter.reports")) {
                player.sendMessage(Reporter.getPlugin().getConfig().getString("no-permission").replaceAll("&", "§"));
                return true;
            }
            if (args.length != 3) {
                sender.sendMessage(ChatColor.RED + "Insufficient arguments usage /reports <all | player>");
                return true;
            }

            boolean b = (args[0].equalsIgnoreCase("all")) ? true : false;

            if (Reporter.getPlugin().useMySql) {
                if (b) {
                    Reporter.getPlugin().sendList(player, Reporter.getPlugin().reportSql.getAllReports(), Integer.parseInt(args[1]), Integer.parseInt(args[2]));

                    return true;
                } else {

                    UUID uuid = UUIDFetcher.getUUID(args[0]);

                    if (uuid == null) {
                        player.sendMessage(ChatColor.DARK_RED + "The player does not exist");
                        return true;
                    }

                    Reporter.getPlugin().sendList(player, Reporter.getPlugin().reportSql.getReports(uuid.toString()), Integer.parseInt(args[1]), Integer.parseInt(args[2]));

                    return true;
                }
            } else {
                if (b) {
                    player.sendMessage(ChatColor.RED + "Im sorry the setting 'all' is not available with configurations at the moment");
                    return true;
                } else {
                    UUID uuid = UUIDFetcher.getUUID(args[0]);

                    if (uuid == null) {
                        player.sendMessage(ChatColor.DARK_RED + "The player does not exist");
                        return true;
                    }

                    Reporter.getPlugin().sendList(player, Reporter.getPlugin().getReports(uuid.toString()), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                }
            }

        }
        return true;
    }
}
