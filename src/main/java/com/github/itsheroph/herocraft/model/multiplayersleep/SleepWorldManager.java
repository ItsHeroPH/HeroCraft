package com.github.itsheroph.herocraft.model.multiplayersleep;

import com.github.itsheroph.herocraft.api.MultiplayerSleepAPI;
import com.github.itsheroph.herocraft.listeners.multiplayerSleep.BedEvents;
import com.github.itsheroph.herocraft.runnables.SleepRunnable;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SleepWorldManager {
    private final MultiplayerSleepAPI mps;
    private final Map<World, SleepRunnable> runnables;
    public SleepWorldManager(MultiplayerSleepAPI mps) {
        this.mps = mps;
        this.runnables = new HashMap<>();
        for(World world : Bukkit.getServer().getWorlds()) {
            if(world.getEnvironment() == World.Environment.NORMAL && Boolean.TRUE.equals(world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE))) {
                SleepWorld sleepWorld = new SleepWorld(world, mps);
                SleepRunnable runnable = new SleepRunnable(sleepWorld, mps);
                runnables.put(world, runnable);
                runnable.runTaskTimer(mps.getPlugin(), 1L, 1L);
            }
        }
        Bukkit.getPluginManager().registerEvents(new BedEvents(mps), mps.getPlugin());
    }
    public SleepRunnable getRunnable(World world) {
        return runnables.get(world);
    }
    public SleepWorld getSleepWorld(Player player) {
        return new SleepWorld(player.getWorld(), mps);
    }
    public void addSleeper(Player player) {
        SleepRunnable runnable = getRunnable(player.getWorld());
        if(runnable != null) {
            runnable.addSleeper(player);
        }
    }
    public void removeSleeper(Player player) {
        SleepRunnable runnable = getRunnable(player.getWorld());
        if(runnable != null) {
            runnable.removeSleeper(player);
        }
    }
    public int getNumMissing(Player player) {
        SleepRunnable runnable = getRunnable(player.getWorld());
        if(runnable != null) {
            return runnable.getNumMissing();
        }
        return 0;
    }
    public void sendMessage(List<Player> players, String... message) {
        for(Player player : players) {
            sendMessage(player, message);
        }
    }
    public void sendMessage(List<Player> players, String message) {
        for(Player player : players) {
            sendMessage(player, message);
        }
    }
    public void sendMessage(Player player, String... message) {
        for(String msg : message) {
            sendMessage(player, msg);
        }
    }
    public void sendMessage(Player player, String message) {
        if(mps.isChatMessageEnable()) {
            player.sendMessage(message);
        }
    }

    public void sendActionBar(List<Player> players, String... message) {
        for(Player player : players) {
            sendActionBar(player, message);
        }
    }
    public void sendActionBar(List<Player> players, String message) {
        for(Player player : players) {
            sendActionBar(player, message);
        }
    }
    public void sendActionBar(Player player, String... message) {
        for(String msg : message) {
            sendActionBar(player, msg);
        }
    }
    public void sendActionBar(Player player, String message) {
        if(mps.isActionBarMessageEnable()) {
            player.sendActionBar(Component.text(message));
        }
    }
}
