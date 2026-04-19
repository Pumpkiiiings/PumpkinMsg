package com.pumpkin.msg.paper;

import com.pumpkin.msg.core.CrossPlayer;
import com.pumpkin.msg.core.ServerPlatform;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PaperPlatform implements ServerPlatform {

    @Override
    public CrossPlayer getPlayer(String name) {
        Player p = Bukkit.getPlayerExact(name);
        return p != null ? new PaperPlayer(p) : null;
    }

    @Override
    public CrossPlayer getPlayer(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        return p != null ? new PaperPlayer(p) : null;
    }

    @Override
    public List<CrossPlayer> getAllOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream()
                .map(PaperPlayer::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllServerNames() {
        return List.of("Global");
    }

    @Override
    public void dispatchCommand(CrossPlayer sender, String command) {
        Player bukkitPlayer = Bukkit.getPlayer(sender.getUniqueId());
        if (bukkitPlayer != null) {
            Bukkit.dispatchCommand(bukkitPlayer, command);
        }
    }
}
