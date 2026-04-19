package com.pumpkin.msg.paper;

import com.pumpkin.msg.core.CrossPlayer;
import com.pumpkin.msg.core.PumpkinCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import java.util.UUID;

public class PaperSpyListener implements Listener {
    private final PumpkinCore core;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public PaperSpyListener(PumpkinCore core) {
        this.core = core;
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;

        String command = event.getMessage().substring(1); // Quitar el '/'
        String checkCmd = command.toLowerCase();

        if (checkCmd.startsWith("login ") || checkCmd.startsWith("l ") ||
                checkCmd.startsWith("register ") || checkCmd.startsWith("reg ") ||
                checkCmd.startsWith("changepassword ")) return;

        String spyFormat = core.getConfig().getString("format.cmdspy");
        UUID playerId = event.getPlayer().getUniqueId();

        for (CrossPlayer staff : core.getPlatform().getAllOnlinePlayers()) {
            UUID staffId = staff.getUniqueId();
            if (!core.getCmdSpyUsers().containsKey(staffId) || staffId.equals(playerId)) continue;

            staff.sendMessage(mm.deserialize(spyFormat,
                    Placeholder.component("player_prefix", core.getPrefix(playerId)),
                    Placeholder.parsed("player", event.getPlayer().getName()),
                    Placeholder.parsed("server", "Global"),
                    Placeholder.parsed("command", command)
            ));
        }
    }
}
