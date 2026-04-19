package com.pumpkin.msg.paper;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PAPIHook {
    public static String parse(Player player, String text) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }
}
