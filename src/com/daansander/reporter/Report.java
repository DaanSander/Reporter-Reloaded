package com.daansander.reporter;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by Daan on 4-1-2016.
 */
public class Report {

    private String reported;
    private String reporter;
    private String reason;

    public Report(String reported, String reporter, String reason) {
        this.reported = reported;
        this.reporter = reporter;
        this.reason = reason;
    }

    public String getReported() {
        return reported;
    }

    public String getReporter() {
        return reporter;
    }

    public String getReason() {
        return reason;
    }

    public void saveReport() {
        if (Reporter.getPlugin().useMySql) {
            Reporter.getPlugin().reportSql.addReport(this);
        } else {
            saveToConfig();
        }
    }

    private void saveToConfig() {
        FileConfiguration reports = Reporter.getPlugin().getReports().getConfig();

        int index = 1;



//        reports./**/

        if (reports.getConfigurationSection(reported) != null) {
            for (String s : reports.getConfigurationSection(reported).getKeys(false)) {
                index++;
            }
        }

        reports.set(reported + "." + index + ".reason", reason);
        reports.set(reported + "." + index + ".reporter", reporter);

        Reporter.getPlugin().getReports().save();
    }
}
