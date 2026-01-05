package com.black.minecraft.spawnB0.config;

import com.black.minecraft.spawnB0.SpawnB0;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class LanguageManager {
    private final SpawnB0 plugin;
    private FileConfiguration languageConfig;
    private String language;
    
    public LanguageManager(SpawnB0 plugin) {
        this.plugin = plugin;
        this.language = "ru";
        loadLanguage();
    }
    
    public void loadLanguage() {
        String fileName = "lang_" + language + ".yml";
        File languageFile = new File(plugin.getDataFolder(), fileName);
        
        if (!languageFile.exists()) {
            plugin.saveResource(fileName, false);
        }
        
        languageConfig = YamlConfiguration.loadConfiguration(languageFile);
        
        InputStream defaultStream = plugin.getResource(fileName);
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                new InputStreamReader(defaultStream, StandardCharsets.UTF_8));
            languageConfig.setDefaults(defaultConfig);
        }
    }
    
    public String getMessage(String key) {
        return languageConfig.getString("messages." + key, "Message not found: " + key);
    }
    
    public void setLanguage(String lang) {
        this.language = lang;
        loadLanguage();
    }
}
