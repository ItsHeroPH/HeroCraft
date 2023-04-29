package com.github.itsheroph.herocraft.commands;

import com.github.itsheroph.herocraft.api.MultiplayerSleepAPI;
import com.github.itsheroph.herocraft.utils.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MultiplayerSleepCMD extends CommandBase {
    private final MultiplayerSleepAPI mps;
    public MultiplayerSleepCMD(MultiplayerSleepAPI mps) {
        super("multiplayersleep", 3);
        setUsage(new ArrayList<>() {{
            add("enable set <true | false>");
            add("percentage set <integer>");
            add("day_length set <integer>");
            add("night_length set <integer>");
            add("night_skip_length set <integer>");
            add("bed_enter_delay set <integer>");
            add("chat_message set <true | false>");
            add("action_bar_message set <true | false>");
        }});
        this.mps = mps;
    }
    public boolean run(CommandSender sender, String[] arguments) {
        String alias = arguments[0] + " " + arguments[1];
        String value = arguments[2];
        if(alias.equalsIgnoreCase("enable set")) {
            if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                Boolean enable = Boolean.parseBoolean(value);
                mps.setEnable(enable);
                sendMessage(sender, ChatColor.BOLD + "" + ChatColor.GOLD + "Multiplayer Sleep enable set to " + ChatColor.YELLOW + value);
                sendActionBar(sender, ChatColor.BOLD + "" +ChatColor.GREEN + "Success!");
                return true;
            }
        } else if(alias.equalsIgnoreCase("percentage set")) {
            if(Integer.parseInt(value) >= 0 || Integer.parseInt(value) <= 100) {
                int percent = Integer.parseInt(value);
                mps.setPercentage(percent);
                sendMessage(sender, ChatColor.BOLD + "" + ChatColor.GOLD + "Multiplayer Sleep enable set to " + ChatColor.YELLOW + percent + "%");
                sendActionBar(sender, ChatColor.BOLD + "" +ChatColor.GREEN + "Success!");
                return true;
            }
        } else if(alias.equalsIgnoreCase("day_length set")) {
            if(Integer.parseInt(value) >= 0 || Integer.parseInt(value) <= 100) {
                int num = Integer.parseInt(value);
                mps.setDayLength(num);
                sendMessage(sender, ChatColor.BOLD + "" + ChatColor.GOLD + "Multiplayer Sleep day length set to " + ChatColor.YELLOW + num + " seconds");
                sendActionBar(sender, ChatColor.BOLD + "" +ChatColor.GREEN + "Success!");
                return true;
            }
        } else if(alias.equalsIgnoreCase("night_length set")) {
            if(Integer.parseInt(value) >= 0 || Integer.parseInt(value) <= 100) {
                int num = Integer.parseInt(value);
                mps.setNightLength(num);
                sendMessage(sender, ChatColor.BOLD + "" + ChatColor.GOLD + "Multiplayer Sleep night length set to " + ChatColor.YELLOW + num + " seconds");
                sendActionBar(sender, ChatColor.BOLD + "" +ChatColor.GREEN + "Success!");
                return true;
            }
        } else if(alias.equalsIgnoreCase("night_skip_length set")) {
            if(Integer.parseInt(value) >= 0 || Integer.parseInt(value) <= 100) {
                int num = Integer.parseInt(value);
                mps.setNightSkipLength(num);
                sendMessage(sender, ChatColor.BOLD + "" + ChatColor.GOLD + "Multiplayer Sleep night skip length set to " + ChatColor.YELLOW + num + " seconds");
                sendActionBar(sender, ChatColor.BOLD + "" +ChatColor.GREEN + "Success!");
                return true;
            }
        } else if(alias.equalsIgnoreCase("bed_enter_delay set")) {
            if(Integer.parseInt(value) >= 0 || Integer.parseInt(value) <= 100) {
                int num = Integer.parseInt(value);
                mps.setBedEnterDelay(num);
                sendMessage(sender, ChatColor.BOLD + "" + ChatColor.GOLD + "Multiplayer Sleep bed enter delay set to " + ChatColor.YELLOW + num + " seconds");
                sendActionBar(sender, ChatColor.BOLD + "" +ChatColor.GREEN + "Success!");
                return true;
            }
        } else if(alias.equalsIgnoreCase("chat_message set")) {
            if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                Boolean enable = Boolean.parseBoolean(value);
                mps.setChatMessageEnable(enable);
                sendMessage(sender, ChatColor.BOLD + "" + ChatColor.GOLD + "Multiplayer Sleep chat message prompt set to " + ChatColor.YELLOW + value);
                sendActionBar(sender, ChatColor.BOLD + "" +ChatColor.GREEN + "Success!");
                return true;
            }
        } else if(alias.equalsIgnoreCase("action_bar_message set")) {
            if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                Boolean enable = Boolean.parseBoolean(value);
                mps.setActionBarMessageEnable(enable);
                sendMessage(sender, ChatColor.BOLD + "" + ChatColor.GOLD + "Multiplayer Sleep action bar message prompt set to " + ChatColor.YELLOW + value);
                sendActionBar(sender, ChatColor.BOLD + "" +ChatColor.GREEN + "Success!");
                return true;
            }
        }
        return false;
    }

}
