package com.black.minecraft.spawnB0.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ColorUtils {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    
    public static Component colorize(String text) {
        return miniMessage.deserialize(text);
    }
    
    public static String replacePlaceholder(String text, String placeholder, String value) {
        return text.replace("{" + placeholder + "}", value);
    }
}
