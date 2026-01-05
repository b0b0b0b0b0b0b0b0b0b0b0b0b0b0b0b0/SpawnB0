package com.black.minecraft.spawnB0.commands;

import com.black.minecraft.spawnB0.SpawnB0;
import com.black.minecraft.spawnB0.config.ConfigManager;
import com.black.minecraft.spawnB0.config.LanguageManager;
import com.black.minecraft.spawnB0.managers.CooldownManager;
import com.black.minecraft.spawnB0.managers.SpawnManager;
import com.black.minecraft.spawnB0.managers.TeleportManager;
import com.black.minecraft.spawnB0.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    private final SpawnB0 plugin;
    private final SpawnManager spawnManager;
    private final CooldownManager cooldownManager;
    private final TeleportManager teleportManager;
    private final ConfigManager configManager;
    private final LanguageManager languageManager;
    
    public SpawnCommand(SpawnB0 plugin, SpawnManager spawnManager, CooldownManager cooldownManager,
                       TeleportManager teleportManager, ConfigManager configManager, LanguageManager languageManager) {
        this.plugin = plugin;
        this.spawnManager = spawnManager;
        this.cooldownManager = cooldownManager;
        this.teleportManager = teleportManager;
        this.configManager = configManager;
        this.languageManager = languageManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (!(sender instanceof Player)) {
                String message = languageManager.getMessage("only-player");
                sender.sendMessage(ColorUtils.colorize(message));
                return true;
            }
            
            Player player = (Player) sender;
            
            if (!player.hasPermission("spawnb0.spawn")) {
                String message = languageManager.getMessage("no-permission");
                player.sendMessage(ColorUtils.colorize(message));
                return true;
            }
            
            if (args.length > 0) {
                String message = languageManager.getMessage("usage-spawn");
                player.sendMessage(ColorUtils.colorize(message));
                return true;
            }
            
            if (!spawnManager.hasSpawn()) {
                String message = languageManager.getMessage("spawn-not-set");
                player.sendMessage(ColorUtils.colorize(message));
                return true;
            }
            
            if (!player.hasPermission("spawnb0.bypass") && cooldownManager.hasCooldown(player)) {
                long remaining = cooldownManager.getRemainingCooldown(player);
                String message = languageManager.getMessage("teleport-cooldown");
                message = ColorUtils.replacePlaceholder(message, "seconds", String.valueOf(remaining));
                player.sendMessage(ColorUtils.colorize(message));
                return true;
            }
            
            if (teleportManager.isTeleporting(player)) {
                return true;
            }
            
            teleportManager.teleportPlayer(player, spawnManager.getSpawn());
            
            if (!player.hasPermission("spawnb0.bypass")) {
                cooldownManager.setCooldown(player, configManager.getCooldown());
            }
            
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Error in SpawnCommand: " + e.getMessage());
            e.printStackTrace();
            return true;
        }
    }
}
