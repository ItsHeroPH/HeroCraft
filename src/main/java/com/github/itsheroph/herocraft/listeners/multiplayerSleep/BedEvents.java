package com.github.itsheroph.herocraft.listeners.multiplayerSleep;

import com.github.itsheroph.herocraft.api.MultiplayerSleepAPI;
import com.github.itsheroph.herocraft.model.multiplayersleep.SleepWorld;
import com.github.itsheroph.herocraft.model.multiplayersleep.SleepWorldManager;
import com.github.itsheroph.herocraft.runnables.SleepRunnable;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class BedEvents implements Listener {
    private final MultiplayerSleepAPI mps;
    private final HashSet<BedEnterResult> blockedResults;
    private final HashMap<UUID, Long> lastBedEnterMap;
    private final int cooldownMs;
    public BedEvents(MultiplayerSleepAPI mps) {
        this.mps = mps;
        this.blockedResults = new HashSet<BedEnterResult>() {{
            add(BedEnterResult.NOT_POSSIBLE_NOW);
            add(BedEnterResult.NOT_POSSIBLE_HERE);
            add(BedEnterResult.TOO_FAR_AWAY);
            add(BedEnterResult.OTHER_PROBLEM);
        }};
        this.lastBedEnterMap = new HashMap<UUID, Long>();
        this.cooldownMs = 1000 * mps.getBedEnterDelay();
    }
    @EventHandler(ignoreCancelled = true)
    public void onPlayerSleep(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        SleepWorldManager manager = mps.getManager();
        SleepWorld world = manager.getSleepWorld(player);
        if(!mps.isEnable()) return;
        if(event.getBedEnterResult() == BedEnterResult.NOT_SAFE) {
            if(player.hasPermission("hc.multiplayerSleep.monster")) {
                event.setUseBed(Result.ALLOW);
                manager.sendMessage(world.getAllPlayersInWorld(), ChatColor.BOLD + "" + ChatColor.GOLD + "Player " + ChatColor.YELLOW + player.getName() + ChatColor.GOLD + " was allowed to enter their bed with nearby monsters");
            } else {
                manager.sendMessage(world.getAllPlayersInWorld(), ChatColor.BOLD + "" + ChatColor.GOLD + "Player " + ChatColor.YELLOW + player.getName() + ChatColor.GOLD + " failed to enter their bed with nearby monsters");
                return;
            }
        }
        if(blockedResults.contains(event.getBedEnterResult())) {
            manager.sendMessage(world.getAllPlayersInWorld(), ChatColor.BOLD + "" + ChatColor.GOLD + "Player " + ChatColor.YELLOW + player.getName() + ChatColor.GOLD + " failed to enter their bed: " + ChatColor.YELLOW + event.getBedEnterResult());
            event.setCancelled(true);
        }
        if(!canPlayerSleep(player)) {
            manager.sendMessage(player, ChatColor.BOLD + "" + ChatColor.GOLD + "You have to wait " + ChatColor.YELLOW + Math.round(calcRemainingCooldown(player) / 1000.0) + " seconds " + ChatColor.GOLD + "before you can sleep");
            event.setUseBed(Result.DENY);
            return;
        }
        lastBedEnterMap.put(player.getUniqueId(), System.currentTimeMillis());
        manager.addSleeper(player);
        SleepRunnable runnable = manager.getRunnable(player.getWorld());
        int numMissing = 0;
        int numSleeping = 0;
        if(runnable != null) {
            numMissing = world.getNumNeeded() - runnable.getSleepers().size();
            numSleeping = runnable.getSleepers().size();
        } else {
            numSleeping = world.getSleepingPlayersInWorld().size();
        }
        manager.sendMessage(world.getAllPlayersInWorld(), ChatColor.BOLD + "" + ChatColor.GOLD + "Player " + ChatColor.YELLOW + player.getName() + ChatColor.GOLD + " was falling asleep. Theres " + numMissing + " players are need to sleep to skip the night/storm (" + numSleeping  + "/" + world.getNumNeeded() + ")");
    }
    private long calcRemainingCooldown(Player player)
    {
        UUID uuid = player.getUniqueId();
        if (!this.lastBedEnterMap.containsKey( uuid ))
            return 0;

        long previousTime = this.lastBedEnterMap.get( uuid );
        long currentTime = System.currentTimeMillis();
        return Math.max(0, this.cooldownMs - (currentTime - previousTime));
    }

    public boolean canPlayerSleep(Player player)
    {
        return calcRemainingCooldown(player) <= 0;
    }
    @EventHandler
    public void onWake(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();
        SleepWorldManager manager = mps.getManager();
        SleepWorld world = manager.getSleepWorld(player);
        if(!mps.isEnable()) return;
        if(world != null) {
            manager.sendMessage(world.getAllPlayersInWorld(), ChatColor.BOLD + "" + ChatColor.GOLD + "Player " + ChatColor.YELLOW + player.getName() + ChatColor.GOLD + " leave their bed at: " + ChatColor.YELLOW + world.getFormattedTime());
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        lastBedEnterMap.remove( event.getPlayer().getUniqueId() );
    }
}
