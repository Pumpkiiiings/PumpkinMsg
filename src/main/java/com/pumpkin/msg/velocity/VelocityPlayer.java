package com.pumpkin.msg.velocity;

import com.pumpkin.msg.core.CrossPlayer;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class VelocityPlayer implements CrossPlayer {
    private final Player player;

    public VelocityPlayer(Player player) {
        this.player = player;
    }

    public Player getVelocityPlayer() { return player; }

    @Override
    public UUID getUniqueId() { return player.getUniqueId(); }

    @Override
    public String getUsername() { return player.getUsername(); }

    @Override
    public String translatePlaceholders(String text) {
        if (text == null) return null;
        return text.replace("%player_name%", getUsername())
                .replace("%player%", getUsername())
                .replace("%server%", getServerName());
    }

    @Override
    public void sendMessage(Component message) { player.sendMessage(message); }

    @Override
    public boolean hasPermission(String permission) { return player.hasPermission(permission); }

    @Override
    public String getServerName() {
        return player.getCurrentServer().map(s -> s.getServerInfo().getName()).orElse("Ninguno");
    }
}

