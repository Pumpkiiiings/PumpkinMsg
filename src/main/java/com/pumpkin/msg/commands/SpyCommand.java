package com.pumpkin.msg.commands;

import com.pumpkin.msg.core.CrossCommand;
import com.pumpkin.msg.core.CrossPlayer;
import com.pumpkin.msg.core.PumpkinCore;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SpyCommand implements CrossCommand {
    private final PumpkinCore core;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public SpyCommand(PumpkinCore core) {
        this.core = core;
    }

    @Override
    public void execute(CrossPlayer player, String[] args) {
        if (!player.hasPermission("pumpkinmsg.staff.spy")) {
            player.sendMessage(mm.deserialize(core.getConfig().getString("messages.no-permission")));
            return;
        }

        UUID staffUuid = player.getUniqueId();

        if (args.length > 0) {
            CrossPlayer target = core.getPlatform().getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(mm.deserialize(core.getConfig().getString("messages.player-offline")));
                return;
            }

            core.getSpyTargets().put(staffUuid, target.getUniqueId());
            core.getSocialSpyUsers().add(staffUuid);

            player.sendMessage(mm.deserialize(core.getConfig().getString("messages.spy-target-set"),
                    Placeholder.parsed("target", target.getUsername())));
        }
        else {
            if (core.getSocialSpyUsers().contains(staffUuid)) {
                core.getSocialSpyUsers().remove(staffUuid);
                core.getSpyTargets().remove(staffUuid);
                player.sendMessage(mm.deserialize(core.getConfig().getString("messages.spy-disabled")));
            } else {
                core.getSocialSpyUsers().add(staffUuid);
                core.getSpyTargets().remove(staffUuid);
                player.sendMessage(mm.deserialize(core.getConfig().getString("messages.spy-enabled")));
            }
        }

        core.saveData();
    }

    @Override
    public List<String> suggest(CrossPlayer player, String[] args) {
        if (args.length <= 1) {
            String search = args.length == 0 ? "" : args[0].toLowerCase();
            return core.getPlatform().getAllOnlinePlayers().stream()
                    .map(CrossPlayer::getUsername)
                    .filter(name -> name.toLowerCase().startsWith(search))
                    .collect(Collectors.toList());
        }

        return List.of();
    }
}
