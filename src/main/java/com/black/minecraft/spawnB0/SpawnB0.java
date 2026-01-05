package com.black.minecraft.spawnB0;

import com.black.minecraft.spawnB0.commands.SetSpawnCommand;
import com.black.minecraft.spawnB0.commands.SetSpawnTabCompleter;
import com.black.minecraft.spawnB0.commands.SpawnCommand;
import com.black.minecraft.spawnB0.commands.SpawnTabCompleter;
import com.black.minecraft.spawnB0.config.ConfigManager;
import com.black.minecraft.spawnB0.config.LanguageManager;
import com.black.minecraft.spawnB0.managers.CooldownManager;
import com.black.minecraft.spawnB0.managers.SpawnManager;
import com.black.minecraft.spawnB0.managers.TeleportManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpawnB0 extends JavaPlugin {
    private ConfigManager configManager;
    private LanguageManager languageManager;
    private SpawnManager spawnManager;
    private CooldownManager cooldownManager;
    private TeleportManager teleportManager;

    @Override
    public void onEnable() {
        try {
            saveDefaultConfig();
            
            configManager = new ConfigManager(this);
            languageManager = new LanguageManager(this);
            spawnManager = new SpawnManager(this);
            cooldownManager = new CooldownManager();
            teleportManager = new TeleportManager(this, configManager, languageManager);
            
            getCommand("setspawn").setExecutor(new SetSpawnCommand(this, spawnManager, languageManager));
            getCommand("setspawn").setTabCompleter(new SetSpawnTabCompleter());
            
            getCommand("spawn").setExecutor(new SpawnCommand(this, spawnManager, cooldownManager, 
                teleportManager, configManager, languageManager));
            getCommand("spawn").setTabCompleter(new SpawnTabCompleter());
            
            getLogger().info("SpawnB0 plugin has been enabled!");
        } catch (Exception e) {
            getLogger().severe("Failed to enable SpawnB0 plugin: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        try {
            if (teleportManager != null) {
                getServer().getOnlinePlayers().forEach(player -> {
                    if (teleportManager.isTeleporting(player)) {
                        teleportManager.cancelTeleport(player);
                    }
                });
            }
            
            getLogger().info("SpawnB0 plugin has been disabled!");
        } catch (Exception e) {
            getLogger().severe("Error during plugin disable: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
