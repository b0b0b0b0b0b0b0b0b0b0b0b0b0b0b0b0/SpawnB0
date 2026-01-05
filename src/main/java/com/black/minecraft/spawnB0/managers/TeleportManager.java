package com.black.minecraft.spawnB0.managers;

import com.black.minecraft.spawnB0.SpawnB0;
import com.black.minecraft.spawnB0.config.ConfigManager;
import com.black.minecraft.spawnB0.config.LanguageManager;
import com.black.minecraft.spawnB0.utils.ColorUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportManager {
    private final SpawnB0 plugin;
    private final ConfigManager configManager;
    private final LanguageManager languageManager;
    private final Map<UUID, BukkitTask> teleportTasks = new HashMap<>();
    private final Map<UUID, Location> teleportLocations = new HashMap<>();
    
    public TeleportManager(SpawnB0 plugin, ConfigManager configManager, LanguageManager languageManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.languageManager = languageManager;
    }
    
    public void teleportPlayer(Player player, Location destination) {
        UUID uuid = player.getUniqueId();
        
        if (teleportTasks.containsKey(uuid)) {
            cancelTeleport(player);
        }
        
        Location startLocation = player.getLocation();
        teleportLocations.put(uuid, startLocation);
        
        int delay = configManager.getTeleportDelay();
        
        BukkitTask task = new BukkitRunnable() {
            private int secondsLeft = delay;
            
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancelTeleport(player);
                    return;
                }
                
                if (configManager.isCancelOnMove()) {
                    Location currentLocation = player.getLocation();
                    Location savedLocation = teleportLocations.get(uuid);
                    
                    if (savedLocation != null && 
                        (currentLocation.getBlockX() != savedLocation.getBlockX() ||
                         currentLocation.getBlockY() != savedLocation.getBlockY() ||
                         currentLocation.getBlockZ() != savedLocation.getBlockZ())) {
                        String message = languageManager.getMessage("teleport-cancelled");
                        player.sendMessage(ColorUtils.colorize(message));
                        cancelTeleport(player);
                        return;
                    }
                }
                
                if (secondsLeft <= 0) {
                    player.teleport(destination);
                    String message = languageManager.getMessage("teleport-success");
                    player.sendMessage(ColorUtils.colorize(message));
                    cancelTeleport(player);
                    return;
                }
                
                String message = languageManager.getMessage("teleporting");
                message = ColorUtils.replacePlaceholder(message, "seconds", String.valueOf(secondsLeft));
                player.sendMessage(ColorUtils.colorize(message));
                
                secondsLeft--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
        
        teleportTasks.put(uuid, task);
    }
    
    public void cancelTeleport(Player player) {
        UUID uuid = player.getUniqueId();
        BukkitTask task = teleportTasks.remove(uuid);
        if (task != null) {
            task.cancel();
        }
        teleportLocations.remove(uuid);
    }
    
    public boolean isTeleporting(Player player) {
        return teleportTasks.containsKey(player.getUniqueId());
    }
}
