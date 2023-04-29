package com.github.itsheroph.herocraft.utils;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginAwareness;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.logging.Level;

public class Config {
    private final JavaPlugin plugin;
    private FileConfiguration config = null;
    private final File configFile;
    private final String path;
    public Config(JavaPlugin plugin, String path) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), path);
        this.path = path;
    }
    public FileConfiguration getConfig() {
        if(config == null) {
            reloadConfig();
        }
        return config;
    }
    private void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        InputStream defConfigStream = plugin.getResource(path);
        if(defConfigStream == null) {
            return;
        }
        YamlConfiguration defConfig;
        if(plugin.getDescription().getAwareness().contains(PluginAwareness.Flags.UTF8)) {
            defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8));
        } else {
            byte[] contents;
            defConfig = new YamlConfiguration();
            try {
                contents = ByteStreams.toByteArray(defConfigStream);
            } catch (final IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Unexpected failure reading " + path, e);
                return;
            }
            final String text = new String(contents, Charset.defaultCharset());
            if (!text.equals(new String(contents, Charsets.UTF_8))) {
                plugin.getLogger().warning("Default system encoding may have misread " + path + " from plugin jar");
            }

            try {
                defConfig.loadFromString(text);
            } catch (final InvalidConfigurationException e) {
                plugin.getLogger().log(Level.SEVERE, "Cannot load configuration from jar", e);
            }
        }
        config.setDefaults(defConfig);
    }
    public void saveConfig() {
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }
    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            plugin.saveResource(path, false);
        }
    }

}
