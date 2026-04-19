package com.pumpkin.msg.commands;

import com.pumpkin.msg.core.CrossCommand;
import com.pumpkin.msg.core.CrossPlayer;
import com.pumpkin.msg.core.PumpkinCore;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class IgnoreCommand implements CrossCommand {

    private final PumpkinCore core;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public IgnoreCommand(PumpkinCore core) {
        this.core = core;
    }

    @Override
    public void execute(CrossPlayer sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(mm.deserialize(core.getConfig().getString("messages.ignore-usage")));
            return;
        }

        CrossPlayer target = core.getPlatform().getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(mm.deserialize(core.getConfig().getString("messages.player-offline")));
            return;
        }

        UUID senderId = sender.getUniqueId();
        UUID targetId = target.getUniqueId();

        if (senderId.equals(targetId)) {
            sender.sendMessage(mm.deserialize(core.getConfig().getString("messages.cannot-ignore-self")));
            return;
        }

        Set<UUID> ignored = core.getIgnoredPlayers().computeIfAbsent(senderId, k -> ConcurrentHashMap.newKeySet());

        if (ignored.contains(targetId)) {
            ignored.remove(targetId);
            sender.sendMessage(mm.deserialize(core.getConfig().getString("messages.unignored-player"),
                    Placeholder.parsed("target", target.getUsername())));
        } else {
            ignored.add(targetId);
            sender.sendMessage(mm.deserialize(core.getConfig().getString("messages.ignored-player"),
                    Placeholder.parsed("target", target.getUsername())));
        }

        core.saveData();
    }

    @Override
    public List<String> suggest(CrossPlayer sender, String[] args) {
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
