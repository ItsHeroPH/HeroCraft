package com.github.itsheroph.herocraft.api;

import com.github.itsheroph.herocraft.HeroCraft;
import com.github.itsheroph.herocraft.commands.MultiplayerSleepCMD;
import com.github.itsheroph.herocraft.model.multiplayersleep.SleepWorldManager;
import com.github.itsheroph.herocraft.utils.Config;
import org.bukkit.configuration.file.FileConfiguration;

public class MultiplayerSleepAPI {
    private final Config config;
    private final HeroCraft plugin;
    private SleepWorldManager manager;

    private boolean enable, chat_enable, action_bar_enable;
    private int percentage, dayDuration, nightDuration, skippedNightDuration, bedEnterDelay;
    public MultiplayerSleepAPI(HeroCraft plugin) {
        this.config = new Config(plugin, "MultiplayerSleepConfig.yml");
        this.plugin = plugin;

        this.enable = getConfig().getBoolean("enable");
        this.percentage = getConfig().getInt("percentage");
        this.dayDuration = getConfig().getInt("day_length");
        this.nightDuration = getConfig().getInt("night_length");
        this.skippedNightDuration = getConfig().getInt("night_skip_length");
        this.bedEnterDelay = getConfig().getInt("bed_enter_delay");
        this.chat_enable = getConfig().getBoolean("chat_message");
        this.action_bar_enable = getConfig().getBoolean("action_bar_message");
    }
    public void activate() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        manager = new SleepWorldManager(this);
        new MultiplayerSleepCMD(this);
        plugin.info("Multiplayer Sleep API is now activated");
    }
    public void deActivate() {
        saveConfig();
        manager = null;
        plugin.info("Multiplayer Sleep API is now deactivated");
    }
    public HeroCraft getPlugin() {
        return plugin;
    }
    public SleepWorldManager getManager() {
        return manager;
    }
    public FileConfiguration getConfig() {
        return config.getConfig();
    }
    public void saveConfig() {
        config.saveConfig();
    }
    public void saveDefaultConfig() {
        config.saveDefaultConfig();
    }
    public Boolean isEnable() {
        return enable;
    }
    public void setEnable(Boolean enable) {
        getConfig().set("enable", enable);
        this.enable = enable;
        saveConfig();
    }
    public int getPercentage() {
        return percentage;
    }
    public void setPercentage(int percentage) {
        getConfig().set("percentage", percentage);
        this.percentage = percentage;
        saveConfig();
    }
    public int getDayLength() {
        return dayDuration;
    }
    public void setDayLength(int length) {
        getConfig().set("day_length", length);
        this.dayDuration = length;
        saveConfig();
    }
    public int getNightLength() {
        return nightDuration;
    }
    public void setNightLength(int length) {
        getConfig().set("night_length", length);
        this.nightDuration = length;
        saveConfig();
    }
    public int getNightSkipLength() {
        return skippedNightDuration;
    }
    public void setNightSkipLength(int length) {
        getConfig().set("night_skip_length", length);
        this.skippedNightDuration = length;
        saveConfig();
    }
    public int getBedEnterDelay() {
        return bedEnterDelay;
    }
    public void setBedEnterDelay(int length) {
        getConfig().set("bed_enter_delay", length);
        this.bedEnterDelay = length;
        saveConfig();
    }
    public Boolean isChatMessageEnable() {
        return chat_enable;
    }
    public void setChatMessageEnable(Boolean enable) {
        getConfig().set("chat_message", enable);
        this.chat_enable = enable;
        saveConfig();
    }
    public Boolean isActionBarMessageEnable() {
        return action_bar_enable;
    }
    public void setActionBarMessageEnable(Boolean enable) {
        getConfig().set("action_bar_message", enable);
        this.action_bar_enable = enable;
        saveConfig();
    }
}
