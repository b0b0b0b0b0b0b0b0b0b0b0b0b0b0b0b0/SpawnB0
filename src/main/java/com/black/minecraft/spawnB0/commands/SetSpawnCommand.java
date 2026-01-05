package com.black.minecraft.spawnB0.commands;

import com.black.minecraft.spawnB0.SpawnB0;
import com.black.minecraft.spawnB0.config.LanguageManager;
import com.black.minecraft.spawnB0.managers.SpawnManager;
import com.black.minecraft.spawnB0.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {
    private final SpawnB0 plugin;
    private final SpawnManager spawnManager;
    private final LanguageManager languageManager;
    
    public SetSpawnCommand(SpawnB0 plugin, SpawnManager spawnManager, LanguageManager languageManager) {
        this.plugin = plugin;
        this.spawnManager = spawnManager;
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
            
            if (!player.hasPermission("spawnb0.setspawn")) {
                String message = languageManager.getMessage("no-permission");
                player.sendMessage(ColorUtils.colorize(message));
                return true;
            }
            
            if (args.length > 0) {
                String message = languageManager.getMessage("usage-setspawn");
                player.sendMessage(ColorUtils.colorize(message));
                return true;
            }
            
            spawnManager.setSpawn(player.getLocation());
            String message = languageManager.getMessage("spawn-set");
            player.sendMessage(ColorUtils.colorize(message));
            
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Error in SetSpawnCommand: " + e.getMessage());
            e.printStackTrace();
            return true;
        }
    }
}
