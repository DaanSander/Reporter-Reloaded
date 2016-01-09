package com.daansander.reporter.database;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Daan on 4-1-2016.
 */
public class MySql {

    private Connection connection;

    private String host;
    private String port;
    private String db;
    private String user;
    private String pass;

    public MySql(String host, String port, String db, String user, String pass) {
        this.host = host;
        this.port = port;
        this.db = db;
        this.user = user;
        this.pass = pass;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed() || (!connection.isValid(500))) {
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, user, pass);
                System.out.println("Opened MySQL connection!");
            }
            return connection;
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Failed to establish connection to database.");
            e.printStackTrace();
            return null;
        }
    }

    public void closeConnection() {
        try {
            if (connection != null || !connection.isClosed()) {
                connection.close();
                System.out.println("Closed MySQL connection!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
