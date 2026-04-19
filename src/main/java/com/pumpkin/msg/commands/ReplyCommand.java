package com.pumpkin.msg.commands;

import com.pumpkin.msg.core.CrossCommand;
import com.pumpkin.msg.core.CrossPlayer;
import com.pumpkin.msg.core.PumpkinCore;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;
import java.util.UUID;

public class ReplyCommand implements CrossCommand {

    private final PumpkinCore core;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public ReplyCommand(PumpkinCore core) {
        this.core = core;
    }

    @Override
    public void execute(CrossPlayer sender, String[] args) {
        UUID targetUUID = core.getLastMessaged().get(sender.getUniqueId());

        if (targetUUID == null) {
            sender.sendMessage(mm.deserialize(sender.translatePlaceholders(core.getConfig().getString("messages.no-reply-target"))));
            return;
        }

        if (targetUUID.equals(sender.getUniqueId())) {
            core.getLastMessaged().remove(sender.getUniqueId());
            sender.sendMessage(mm.deserialize(sender.translatePlaceholders(core.getConfig().getString("messages.cannot-reply-self"))));
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(mm.deserialize(sender.translatePlaceholders(core.getConfig().getString("messages.reply-usage"))));
            return;
        }

        CrossPlayer target = core.getPlatform().getPlayer(targetUUID);

        if (target == null) {
            sender.sendMessage(mm.deserialize(sender.translatePlaceholders(core.getConfig().getString("messages.player-offline"))));
            return;
        }

        String messageContent = String.join(" ", args);
        String fullCommand = "msg " + target.getUsername() + " " + messageContent;

        core.getPlatform().dispatchCommand(sender, fullCommand);
    }

    @Override
    public List<String> suggest(CrossPlayer sender, String[] args) {
        return List.of();
    }
}
