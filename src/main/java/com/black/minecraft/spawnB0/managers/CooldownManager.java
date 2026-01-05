package com.black.minecraft.spawnB0.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    
    public void setCooldown(Player player, long seconds) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + (seconds * 1000));
    }
    
    public boolean hasCooldown(Player player) {
        UUID uuid = player.getUniqueId();
        if (!cooldowns.containsKey(uuid)) {
            return false;
        }
        
        long cooldownTime = cooldowns.get(uuid);
        if (System.currentTimeMillis() >= cooldownTime) {
            cooldowns.remove(uuid);
            return false;
        }
        
        return true;
    }
    
    public long getRemainingCooldown(Player player) {
        UUID uuid = player.getUniqueId();
        if (!cooldowns.containsKey(uuid)) {
            return 0;
        }
        
        long cooldownTime = cooldowns.get(uuid);
        long remaining = (cooldownTime - System.currentTimeMillis()) / 1000;
        
        if (remaining <= 0) {
            cooldowns.remove(uuid);
            return 0;
        }
        
        return remaining;
    }
    
    public void removeCooldown(Player player) {
        cooldowns.remove(player.getUniqueId());
    }
}
