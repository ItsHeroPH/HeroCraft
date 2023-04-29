package com.github.itsheroph.herocraft.runnables;

import com.github.itsheroph.herocraft.api.MultiplayerSleepAPI;
import com.github.itsheroph.herocraft.model.multiplayersleep.SleepWorld;
import com.github.itsheroph.herocraft.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.UUID;

public class SleepRunnable extends BukkitRunnable {
    private final SleepWorld world;
    private final MultiplayerSleepAPI mps;
    private final HashSet<UUID> sleepers;
    private Boolean isSkipping = false;
    public SleepRunnable(SleepWorld world, MultiplayerSleepAPI mps) {
        this.world = world;
        this.mps = mps;
        this.sleepers = new HashSet<>();

    }
    public HashSet<UUID> getSleepers() {
        return sleepers;
    }

    public void addSleeper(Player player) {
        if(!player.isSleeping()) {
            sleepers.add(player.getUniqueId());
        }
    }
    public void removeSleeper(Player player) {
        sleepers.remove(player.getUniqueId());
    }
    public void resetSleeper() {
        sleepers.clear();
    }
    private boolean isNotValidSleeper(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        return !(player != null && player.isOnline() && this.world.isInWorld(player) && player.isSleeping());
    }
    private double calcSpeedup() {
        double dayDuration = mps.getDayLength() * 20;
        double nightDuration = mps.getNightLength() * 20;
        double skippedNightDuration = mps.getNightSkipLength() * 20;

        double daySpeedup = Math.min(TimeUtil.DAY_DURATION / dayDuration, 14000);
        double nightSpeedup = Math.min(TimeUtil.NIGHT_DURATION / nightDuration, 10000);
        double sleepSpeedup = Math.min(TimeUtil.NIGHT_DURATION / skippedNightDuration, 10000);
        final double speedup;

        if(this.world.getTime() >= TimeUtil.TIME_NIGHT_START) {
            speedup = isSkipping ? sleepSpeedup : nightSpeedup;
        } else {
            speedup = daySpeedup;
        }

        return speedup;
    }
    public int getNumMissing() {
        return Math.min(0, world.getNumNeeded() - getSleepers().size());
    }
    @Override
    public void run() {
        this.sleepers.removeIf(this::isNotValidSleeper);
        int numSleepers = sleepers.size();
        int numNeeded = world.getNumNeeded();
        if (numSleepers >= numNeeded && numSleepers > 0) {
            isSkipping = true;
            world.clearWeather();
        } else {
            isSkipping = false;
        }
        final double acceleration = calcSpeedup();
        boolean isNightSkipped = this.world.addTime(acceleration);
        if(isNightSkipped) {
            for(Player player : world.getValidPlayersInWorld()) {
                if(mps.isChatMessageEnable()) {
                    player.sendMessage(ChatColor.BOLD + "" + ChatColor.GOLD + "Good morning " + ChatColor.YELLOW + player.getName() + ChatColor.GOLD + " Have a great day!");
                }
            }
            resetSleeper();
        }
    }
    public SleepWorld getSleepWorld() {
        return world;
    }
}
