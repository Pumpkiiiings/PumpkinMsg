package com.pumpkin.msg.paper;

import com.pumpkin.msg.core.CrossPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import java.util.UUID;

public class PaperPlayer implements CrossPlayer {
    private final Player player;

    public PaperPlayer(Player player) {
        this.player = player;
    }

    public Player getBukkitPlayer() {
        return player;
    }

    @Override
    public UUID getUniqueId() { return player.getUniqueId(); }

    @Override
    public String getUsername() { return player.getName(); }

    @Override
    public void sendMessage(Component message) { player.sendMessage(message); }

    @Override
    public boolean hasPermission(String permission) { return player.hasPermission(permission); }

    @Override
    public String getServerName() { return "Global"; }

    @Override
    public String translatePlaceholders(String text) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return PAPIHook.parse(player, text);
        }
        return text;
    }
}
