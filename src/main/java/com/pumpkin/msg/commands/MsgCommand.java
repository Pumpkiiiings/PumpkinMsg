package com.pumpkin.msg.commands;

import com.pumpkin.msg.core.CrossCommand;
import com.pumpkin.msg.core.CrossPlayer;
import com.pumpkin.msg.core.PumpkinCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class MsgCommand implements CrossCommand {

    private final PumpkinCore core;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public MsgCommand(PumpkinCore core) {
        this.core = core;
    }

    @Override
    public void execute(CrossPlayer sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(mm.deserialize(sender.translatePlaceholders(core.getConfig().getString("messages.usage"))));
            return;
        }

        if (core.getMsgDisabledUsers().contains(sender.getUniqueId())) {
            sender.sendMessage(mm.deserialize(sender.translatePlaceholders(core.getConfig().getString("messages.sender-toggled-off"))));
            return;
        }

        CrossPlayer target = core.getPlatform().getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(mm.deserialize(sender.translatePlaceholders(core.getConfig().getString("messages.player-offline"))));
            return;
        }

        if (sender.getUniqueId().equals(target.getUniqueId())) {
            sender.sendMessage(mm.deserialize(sender.translatePlaceholders(core.getConfig().getString("messages.cannot-msg-self"))));
            return;
        }

        boolean hasBypass = sender.hasPermission("pumpkinmsg.staff.bypass");

        if (core.getMsgDisabledUsers().contains(target.getUniqueId()) && !hasBypass) {
            sender.sendMessage(mm.deserialize(sender.translatePlaceholders(core.getConfig().getString("messages.target-toggled-off"))));
            return;
        }

        Set<UUID> targetIgnoredList = core.getIgnoredPlayers().get(target.getUniqueId());
        if (targetIgnoredList != null && targetIgnoredList.contains(sender.getUniqueId()) && !hasBypass) {
            sender.sendMessage(mm.deserialize(sender.translatePlaceholders(core.getConfig().getString("messages.player-ignoring-you"))));
            return;
        }

        String messageContent = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        String senderFormat = core.getConfig().getString("format.sender");
        sender.sendMessage(mm.deserialize(sender.translatePlaceholders(senderFormat),
                Placeholder.parsed("target", target.getUsername()),
                Placeholder.parsed("message", messageContent)));

        String receiverFormat = core.getConfig().getString("format.receiver");
        target.sendMessage(mm.deserialize(target.translatePlaceholders(receiverFormat),
                Placeholder.parsed("sender", sender.getUsername()),
                Placeholder.parsed("message", messageContent)));

        core.getLastMessaged().put(sender.getUniqueId(), target.getUniqueId());
        core.getLastMessaged().put(target.getUniqueId(), sender.getUniqueId());

        broadcastToStaff(sender, target, messageContent);
    }

    private void broadcastToStaff(CrossPlayer sender, CrossPlayer target, String message) {
        String spyFormatRaw = core.getConfig().getString("format.spy");
        UUID senderId = sender.getUniqueId();
        UUID targetId = target.getUniqueId();

        Component senderPrefix = core.getPrefix(senderId);
        Component targetPrefix = core.getPrefix(targetId);

        for (CrossPlayer staff : core.getPlatform().getAllOnlinePlayers()) {
            UUID staffId = staff.getUniqueId();

            if (core.getSocialSpyUsers().contains(staffId) && !staffId.equals(senderId) && !staffId.equals(targetId)) {
                UUID specificTarget = core.getSpyTargets().get(staffId);

                if (specificTarget == null || senderId.equals(specificTarget) || targetId.equals(specificTarget)) {
                    // Traducimos placeholders para el Staff específico (por si el formato spy usa %rango% del staff)
                    staff.sendMessage(mm.deserialize(staff.translatePlaceholders(spyFormatRaw),
                            Placeholder.component("sender_prefix", senderPrefix),
                            Placeholder.parsed("sender", sender.getUsername()),
                            Placeholder.component("target_prefix", targetPrefix),
                            Placeholder.parsed("target", target.getUsername()),
                            Placeholder.parsed("message", message)));
                }
            }
        }
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
