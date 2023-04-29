package com.github.itsheroph.herocraft.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public abstract class CommandBase implements CommandExecutor {
    protected final String name;
    protected final int minArguments;
    protected final int maxArguments;
    protected final boolean playerOnly;
    protected List<String> usages;
    public CommandBase(String name) {
        this(name, 0);
    }
    public CommandBase(String name, boolean playerOnly) {
        this(name, 0, playerOnly);
    }
    public CommandBase(String name, int requiredArguments) {
        this(name, requiredArguments, requiredArguments);
    }
    public CommandBase(String name, int minArguments, int maxArguments) {
        this(name, minArguments, maxArguments, false);
    }
    public CommandBase(String name, int requiredArguments, boolean playerOnly) {
        this(name, requiredArguments, requiredArguments, false);
    }
    public CommandBase(String name, int minArguments, int maxArguments, boolean playerOnly) {
        this.name = name;
        this.minArguments = minArguments;
        this.maxArguments = maxArguments;
        this.playerOnly = playerOnly;
        Bukkit.getPluginCommand(name).setExecutor(this);
    }
    public abstract boolean run(CommandSender sender, String[] arguments);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] arguments) {
        if(playerOnly && !(sender instanceof Player)) {
            sendMessage(sender,ChatColor.BOLD + "" + ChatColor.RED + "Player can only run this command");
            return true;
        }
        if(arguments.length < minArguments || (arguments.length > maxArguments && maxArguments != -1)) {
            sendMessage(sender, ChatColor.BOLD + "" + ChatColor.RED + "Wrong usage of command", getUsage());
            return true;
        }
        if(!run(sender, arguments)) {
            sendMessage(sender, ChatColor.BOLD + "" + ChatColor.RED + "Wrong usage of command", getUsage());
        }
        return true;
    }
    public String getUsage() {
        StringBuilder string = new StringBuilder();
        for(String usage : usages) {
            string.append("/").append(name).append(" ").append(usage).append("\n");
        }
        return string.toString();
    }
    public void setUsage(List<String> usage) {
        this.usages = usage;
    }
    public void sendMessage(List<CommandSender> players, String... message) {
        for(CommandSender player : players) {
            sendMessage(player, message);
        }
    }
    public void sendMessage(List<CommandSender> players, String message) {
        for(CommandSender player : players) {
            sendMessage(player, message);
        }
    }
    public void sendMessage(CommandSender player, String... message) {
        for(String msg : message) {
            sendMessage(player, msg);
        }
    }
    public void sendMessage(CommandSender player, String message) {
        player.sendMessage(message);
    }
    public void sendActionBar(List<CommandSender> players, String... message) {
        for(CommandSender player : players) {
            sendActionBar(player, message);
        }
    }
    public void sendActionBar(List<CommandSender> players, String message) {
        for(CommandSender player : players) {
            sendActionBar(player, message);
        }
    }
    public void sendActionBar(CommandSender player, String... message) {
        for(String msg : message) {
            sendActionBar(player, msg);
        }
    }
    public void sendActionBar(CommandSender player, String message) {
        player.sendActionBar(Component.text(message));
    }
}
