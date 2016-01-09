package com.daansander.reporter.database;

import com.daansander.reporter.Report;
import com.daansander.reporter.Reporter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daan on 8-1-2016.
 */
public class ReportSql {

    private MySql mySql;

    public void connect() {
        FileConfiguration config = Reporter.getPlugin().getConfig();

        mySql = new MySql(config.getString("mysql-host"), config.getString("mysql-port"), config.getString("mysql-db"), config.getString("mysql-username"), config.getString("mysql-password"));

        try {
            PreparedStatement sql = mySql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `reports` (reporter text, reported text, reason text);");
            sql.executeUpdate();
            sql.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addReport(Report report) {
        try {
            PreparedStatement statement = mySql.getConnection().prepareStatement("INSERT INTO `reports` (`reporter`, `reported`, `reason`) VALUES ('" + report.getReporter()
                    + "', '" + report.getReported() + "', '" + report.getReason() + "');");
            statement.executeUpdate();

            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Report> getAllReports() {
        List<Report> reports = new ArrayList<>();
        try {
            PreparedStatement statement = mySql.getConnection().prepareStatement("SELECT * FROM `reports` WHERE 1;");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                String reporter = resultSet.getString("reporter");
                String reported = resultSet.getString("reported");
                String reason = resultSet.getString("reason");

                reports.add(new Report(reported, reporter, reason));
            }

            statement.close();
            resultSet.close();

            return reports;
        } catch (SQLException ex) {
            ex.printStackTrace();

            return null;
        }
    }

    public boolean isReported(Player player) {
        try {
            PreparedStatement statement = mySql.getConnection().prepareStatement("SELECT * FROM `reports` WHERE `reported`='" + player.getUniqueId().toString() +  "';");
            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.next()) {
                statement.close();
                resultSet.close();
                return false;
            }

            statement.close();
            resultSet.close();

            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<Report> getReports(Player player) {
        try {
            List<Report> reports = new ArrayList<>();
            PreparedStatement statement = mySql.getConnection().prepareStatement("SELECT * FROM `reports` WHERE `reported`='" + player.getUniqueId().toString() + "';");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String reporter = resultSet.getString("reporter");
                String reported = resultSet.getString("reported");
                String reason = resultSet.getString("reason");

                reports.add(new Report(reported, reporter, reason));
            }

            statement.close();
            resultSet.close();

            return reports;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Report> getReports(String uuid) {
        try {
            List<Report> reports = new ArrayList<>();
            PreparedStatement statement = mySql.getConnection().prepareStatement("SELECT * FROM `reports` WHERE `reported`='" + uuid + "';");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String reporter = resultSet.getString("reporter");
                String reported = resultSet.getString("reported");
                String reason = resultSet.getString("reason");

                reports.add(new Report(reported, reporter, reason));
            }

            statement.close();
            resultSet.close();

            return reports;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
