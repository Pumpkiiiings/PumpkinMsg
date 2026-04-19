package com.pumpkin.msg.commands;

import com.pumpkin.msg.core.CrossCommand;
import com.pumpkin.msg.core.CrossPlayer;
import com.pumpkin.msg.core.PumpkinCore;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandSpy implements CrossCommand {
    private final PumpkinCore core;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public CommandSpy(PumpkinCore core) {
        this.core = core;
    }

    @Override
    public void execute(CrossPlayer player, String[] args) {
        if (!player.hasPermission("pumpkinmsg.staff.cmdspy")) {
            player.sendMessage(mm.deserialize(core.getConfig().getString("messages.no-permission")));
            return;
        }

        UUID uuid = player.getUniqueId();

        if (args.length == 0) {
            if (core.getCmdSpyUsers().containsKey(uuid)) {
                core.getCmdSpyUsers().remove(uuid);
                player.sendMessage(mm.deserialize(core.getConfig().getString("messages.cmdspy-disabled")));
            } else {
                core.getCmdSpyUsers().put(uuid, "ALL");
                player.sendMessage(mm.deserialize(core.getConfig().getString("messages.cmdspy-enabled"),
                        Placeholder.parsed("mode", "Global")));
            }
        } else {
            String mode = args[0].toLowerCase();
            if (mode.equalsIgnoreCase("off") || mode.equalsIgnoreCase("disable")) {
                core.getCmdSpyUsers().remove(uuid);
                player.sendMessage(mm.deserialize(core.getConfig().getString("messages.cmdspy-disabled")));
            } else {
                core.getCmdSpyUsers().put(uuid, mode);
                player.sendMessage(mm.deserialize(core.getConfig().getString("messages.cmdspy-enabled"),
                        Placeholder.parsed("mode", mode.equalsIgnoreCase("global") ? "Global" : "Server: " + mode)));
            }
        }
        core.saveData();
    }

    @Override
    public List<String> suggest(CrossPlayer player, String[] args) {
        if (args.length <= 1) {
            String arg = args.length == 0 ? "" : args[0].toLowerCase();
            List<String> s = new ArrayList<>();
            s.add("global");
            s.add("off");
            s.addAll(core.getPlatform().getAllServerNames());
            return s.stream().filter(sv -> sv.toLowerCase().startsWith(arg)).toList();
        }
        return List.of();
    }
}
