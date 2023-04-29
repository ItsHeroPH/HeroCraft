package com.github.itsheroph.herocraft;

import com.github.itsheroph.herocraft.api.MultiplayerSleepAPI;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class HeroCraft extends JavaPlugin {
    private MultiplayerSleepAPI mps;
    @Override
    public void onEnable() {
        // Plugin startup logic
        if(getDescription().getVersion().contains("-SNAPSHOT")) {
            warning(
                    "You are running on a snapshot version of the plugin",
                    "That means the plugin has potentially have a bugs or errors"
            );
        }
        mps = new MultiplayerSleepAPI(this);
        mps.activate();
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        mps.deActivate();
    }
    public void log(Level level, String... messages) {
        for(String msg : messages) {
            log(level, msg);
        }
    }
    public void log(Level level, String message) {
        getLogger().log(level, message);
    }
    public void info(String... messages) {
        for(String msg : messages) {
            info(msg);
        }
    }
    public void info(String message) {
        getLogger().info(message);
    }
    public void warning(String... messages) {
        for(String msg : messages) {
            warning(msg);
        }
    }
    public void warning(String message) {
        getLogger().warning(message);
    }
}
