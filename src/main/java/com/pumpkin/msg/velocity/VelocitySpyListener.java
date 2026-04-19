package com.pumpkin.msg.velocity;

import com.pumpkin.msg.core.CrossPlayer;
import com.pumpkin.msg.core.PumpkinCore;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import java.util.UUID;

public class VelocitySpyListener {
    private final PumpkinCore core;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public VelocitySpyListener(PumpkinCore core) {
        this.core = core;
    }

    @Subscribe
    public void onCommandExecute(CommandExecuteEvent event) {
        if (!event.getResult().isAllowed() || !(event.getCommandSource() instanceof Player player)) return;

        String command = event.getCommand();
        String checkCmd = command.toLowerCase();

        if (checkCmd.startsWith("login ") || checkCmd.startsWith("l ") ||
                checkCmd.startsWith("register ") || checkCmd.startsWith("reg ") ||
                checkCmd.startsWith("changepassword ")) return;

        String serverName = player.getCurrentServer().map(sv -> sv.getServerInfo().getName()).orElse("Ninguno");
        String spyFormat = core.getConfig().getString("format.cmdspy");

        for (CrossPlayer staff : core.getPlatform().getAllOnlinePlayers()) {
            UUID staffId = staff.getUniqueId();
            if (!core.getCmdSpyUsers().containsKey(staffId) || staffId.equals(player.getUniqueId())) continue;

            String mode = core.getCmdSpyUsers().get(staffId);
            if (mode.equalsIgnoreCase("ALL") || mode.equalsIgnoreCase(serverName)) {
                staff.sendMessage(mm.deserialize(spyFormat,
                        Placeholder.component("player_prefix", core.getPrefix(player.getUniqueId())),
                        Placeholder.parsed("player", player.getUsername()),
                        Placeholder.parsed("server", serverName),
                        Placeholder.parsed("command", command)
                ));
            }
        }
    }
}
