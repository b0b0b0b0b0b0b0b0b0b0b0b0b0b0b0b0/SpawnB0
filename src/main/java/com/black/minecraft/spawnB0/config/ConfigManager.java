package com.black.minecraft.spawnB0.config;

import com.black.minecraft.spawnB0.SpawnB0;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final SpawnB0 plugin;
    private FileConfiguration config;
    
    public ConfigManager(SpawnB0 plugin) {
        this.plugin = plugin;
        loadConfig();
    }
    
    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
    }
    
    public int getCooldown() {
        return config.getInt("spawn.cooldown", 10);
    }
    
    public int getTeleportDelay() {
        return config.getInt("spawn.teleport-delay", 5);
    }
    
    public boolean isCancelOnMove() {
        return config.getBoolean("spawn.cancel-on-move", true);
    }
}
