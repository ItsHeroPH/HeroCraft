package com.github.itsheroph.herocraft.model.multiplayersleep;

import com.github.itsheroph.herocraft.api.MultiplayerSleepAPI;
import com.github.itsheroph.herocraft.utils.TimeUtil;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SleepWorld {
    private World world;
    private MultiplayerSleepAPI mps;
    public SleepWorld(World world, MultiplayerSleepAPI mps) {
        this.world = world;
        this.mps = mps;
    }
    public List<Player> getAllPlayersInWorld() {
        return world.getPlayers().stream()
                .filter(this::isPlayerInValidEnvironment)
                .collect(Collectors.toList());
    }
    private boolean isPlayerInValidEnvironment(Player player) {
        Environment playerEnv = player.getWorld().getEnvironment();
        return playerEnv == Environment.NORMAL || playerEnv == Environment.CUSTOM;
    }
    public List<Player> getValidPlayersInWorld() {
        return getAllPlayersInWorld().stream()
                .filter(Entity::isValid)
                .collect(Collectors.toList());
    }
    public List<Player> getSleepingPlayersInWorld() {
        return this.getAllPlayersInWorld().stream()
                .filter(LivingEntity::isSleeping)
                .collect(Collectors.toList());
    }
    public long getTime() {
        return world.getTime();
    }
    public String getFormattedTime() {
        long fullTime = getTime() / 20;
        long hours = (int) TimeUnit.SECONDS.toMinutes(fullTime) % 60;
        long minutes = (int) TimeUnit.SECONDS.toSeconds(fullTime) % 60;
        return String.format("%02d:%02d", hours, minutes);
    }
    public void setTime(long time) {
        world.setTime(time);
    }
    public boolean addTime(double deltaTicks) {
        assert deltaTicks >= 0;
        boolean nightSkipped = false;
        long time = (long) (getTime() + deltaTicks);
        if(time >= 24000) {
            nightSkipped = true;
        }
        setTime(time);
        return nightSkipped;
    }
    public boolean isInWorld(Player player)
    {
        return player.getWorld().getName().equals(this.world.getName());
    }
    public int getNumNeeded() {
        int numPlayers = getValidPlayersInWorld().size();
        return Math.max(Math.round(mps.getPercentage() * numPlayers / 100f), 1);
    }
    public void clearWeather() {
        world.setThundering(false);
        world.setStorm(false);
    }
    public World getWorld() {
        return world;
    }
}
