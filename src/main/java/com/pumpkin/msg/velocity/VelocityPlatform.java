package com.pumpkin.msg.velocity;

import com.pumpkin.msg.core.CrossPlayer;
import com.pumpkin.msg.core.ServerPlatform;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class VelocityPlatform implements ServerPlatform {
    private final ProxyServer server;

    public VelocityPlatform(ProxyServer server) {
        this.server = server;
    }

    @Override
    public CrossPlayer getPlayer(String name) {
        Optional<Player> p = server.getPlayer(name);
        return p.map(VelocityPlayer::new).orElse(null);
    }

    @Override
    public CrossPlayer getPlayer(UUID uuid) {
        Optional<Player> p = server.getPlayer(uuid);
        return p.map(VelocityPlayer::new).orElse(null);
    }

    @Override
    public List<CrossPlayer> getAllOnlinePlayers() {
        return server.getAllPlayers().stream()
                .map(VelocityPlayer::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllServerNames() {
        return server.getAllServers().stream()
                .map(sv -> sv.getServerInfo().getName())
                .collect(Collectors.toList());
    }

    @Override
    public void dispatchCommand(CrossPlayer sender, String command) {
        if (sender instanceof VelocityPlayer vp) {
            server.getCommandManager().executeAsync(vp.getVelocityPlayer(), command);
        }
    }
}
