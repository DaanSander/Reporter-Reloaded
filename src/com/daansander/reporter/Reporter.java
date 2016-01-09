package com.daansander.reporter;

import com.daansander.reporter.command.ReportCommand;
import com.daansander.reporter.command.ReportsCommand;
import com.daansander.reporter.config.Config;
import com.daansander.reporter.database.ReportSql;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * Created by Daan on 4-1-2016.
 */
public class Reporter extends JavaPlugin {

    private static Reporter plugin;
    public Config reports;
    public ReportSql reportSql;
    public boolean useUUID, useMySql;
    private Map<String, Long> cooldowns = new HashMap<>();

    public static Reporter getPlugin() {
        return plugin;
    }

    public void onEnable() {
        plugin = this;
        saveDefaultConfig();

        reports = new Config("reports");
        reportSql = new ReportSql();

        reportSql.connect();

        useUUID = getConfig().getBoolean("use-uuid");
        useMySql = getConfig().getBoolean("mysql");

        getCommand("report").setExecutor(new ReportCommand());
        getCommand("reports").setExecutor(new ReportsCommand());


        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "reporter " + (reports.getConfig() == null));

        startCooldownTimer();
    }

    public void startCooldownTimer() {
        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                for (String uuid : cooldowns.keySet()) {
                    if (cooldowns.get(uuid) > 0) {
                        cooldowns.put(uuid, cooldowns.get(uuid) - 1);
                    } else {
                        cooldowns.remove(uuid);
                    }
                }
            }
        }, 0L, 20L);
    }

    public boolean canReport(Player player) {
        if (cooldowns.containsKey(player.getUniqueId().toString())) {
            return false;
        } else {
            return true;
        }
    }

    public Map<String, Long> getCooldowns() {
        return cooldowns;
    }

    public Config getReports() {
        return reports;
    }

    public void sendList(Player player, List<Report> reports, int max, int page) {
        List<Report> current = new ArrayList<>();

        String format = ChatColor.RED + "Player " + ChatColor.GOLD + " %1$s " + ChatColor.RED + " has been reported by " + ChatColor.GOLD
                + " %2$s " + ChatColor.RED + " with the reason " + ChatColor.GOLD + "%3$s";

        if (page > reports.size()) {
            page = 0;
            player.sendMessage(ChatColor.DARK_RED + "Page number was to high setting it to 0 !");
        }


        if (max > reports.size()) {
            player.sendMessage(ChatColor.DARK_RED + "Your entries was to high for the reports setting it to the max value!");
            max = reports.size();
        }

        max = max * page;

        int index = (page > 0) ? 0 : page;


        for (int i = index; i < max; i++) {
            current.add(reports.get(i));
        }

        for (Report report : current) {
            player.sendMessage(String.format(format, Bukkit.getOfflinePlayer(UUID.fromString(report.getReported())).getName(), report.getReporter(), report.getReason()));
        }

    }

    public List<Report> getReports(String uuid) {
        List<Report> reports = new ArrayList<>();

        for (String index : this.reports.getConfig().getConfigurationSection(uuid).getKeys(false)) {
            String reported = uuid;
            String reporter = this.reports.getConfig().getString(uuid + "." + index + ".reporter");
            String reason = this.reports.getConfig().getString(uuid + "." + index + ".reason");

            reports.add(new Report(reported, reporter, reason));

        }

        return reports;
    }

    public void sendAdminMessage(String string) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.hasPermission("reporter.admin") || player.isOp()) {
                player.sendMessage(string);
            }
        }
    }

}
