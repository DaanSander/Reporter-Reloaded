package com.daansander.reporter.config;

import com.daansander.reporter.Report;
import com.daansander.reporter.Reporter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Daan on 4-1-2016.
 */
public class Config {

    private final String NAME;
    private FileConfiguration config;
    private File file;

    public Config(String NAME) {
        this.NAME = NAME;

        setupConfig();
    }

    private void setupConfig() {

        this.file = new File(Reporter.getPlugin().getDataFolder(), NAME + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
                Reporter.getPlugin().saveResource(NAME + ".yml", true);
            } catch (IOException e) {
                e.printStackTrace();
                Bukkit.getLogger().severe("There was an error while creating " + NAME + ".yml please send this log to the developer");
                return;
            }



            config = YamlConfiguration.loadConfiguration(file);


            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "configsetup " + (config == null));
        }
    }

    public FileConfiguration getConfig() {
        if(config == null) {
            config = YamlConfiguration.loadConfiguration(file);
            return YamlConfiguration.loadConfiguration(file);
        }
        return config;
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        if(file == null) return;

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
