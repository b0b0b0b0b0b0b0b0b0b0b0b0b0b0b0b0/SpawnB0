package com.black.minecraft.spawnB0.managers;

import com.black.minecraft.spawnB0.SpawnB0;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SpawnManager {
    private final SpawnB0 plugin;
    private Location spawnLocation;
    private File spawnFile;
    private FileConfiguration spawnConfig;
    
    public SpawnManager(SpawnB0 plugin) {
        this.plugin = plugin;
        this.spawnFile = new File(plugin.getDataFolder(), "spawn.yml");
        loadSpawn();
    }
    
    public void setSpawn(Location location) {
        this.spawnLocation = location;
        saveSpawn();
    }
    
    public Location getSpawn() {
        return spawnLocation;
    }
    
    public boolean hasSpawn() {
        return spawnLocation != null;
    }
    
    private void saveSpawn() {
        try {
            if (!spawnFile.exists()) {
                spawnFile.getParentFile().mkdirs();
                spawnFile.createNewFile();
            }
            
            spawnConfig = YamlConfiguration.loadConfiguration(spawnFile);
            
            if (spawnLocation != null) {
                spawnConfig.set("spawn-location.world", spawnLocation.getWorld().getName());
                spawnConfig.set("spawn-location.x", spawnLocation.getX());
                spawnConfig.set("spawn-location.y", spawnLocation.getY());
                spawnConfig.set("spawn-location.z", spawnLocation.getZ());
                spawnConfig.set("spawn-location.yaw", spawnLocation.getYaw());
                spawnConfig.set("spawn-location.pitch", spawnLocation.getPitch());
                
                spawnConfig.save(spawnFile);
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save spawn location: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            plugin.getLogger().severe("Unexpected error while saving spawn: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadSpawn() {
        try {
            if (!spawnFile.exists()) {
                return;
            }
            
            spawnConfig = YamlConfiguration.loadConfiguration(spawnFile);
            
            if (spawnConfig.contains("spawn-location.world")) {
                String worldName = spawnConfig.getString("spawn-location.world");
                if (worldName != null && plugin.getServer().getWorld(worldName) != null) {
                    spawnLocation = new Location(
                        plugin.getServer().getWorld(worldName),
                        spawnConfig.getDouble("spawn-location.x"),
                        spawnConfig.getDouble("spawn-location.y"),
                        spawnConfig.getDouble("spawn-location.z"),
                        (float) spawnConfig.getDouble("spawn-location.yaw"),
                        (float) spawnConfig.getDouble("spawn-location.pitch")
                    );
                }
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to load spawn location: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
