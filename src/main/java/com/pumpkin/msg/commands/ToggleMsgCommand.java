package com.pumpkin.msg.commands;

import com.pumpkin.msg.core.CrossCommand;
import com.pumpkin.msg.core.CrossPlayer;
import com.pumpkin.msg.core.PumpkinCore;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;
import java.util.UUID;

public class ToggleMsgCommand implements CrossCommand {

    private final PumpkinCore core;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public ToggleMsgCommand(PumpkinCore core) {
        this.core = core;
    }

    @Override
    public void execute(CrossPlayer player, String[] args) {
        UUID uuid = player.getUniqueId();

        if (core.getMsgDisabledUsers().contains(uuid)) {
            core.getMsgDisabledUsers().remove(uuid);
            player.sendMessage(mm.deserialize(core.getConfig().getString("messages.toggle-on")));
        } else {
            core.getMsgDisabledUsers().add(uuid);
            player.sendMessage(mm.deserialize(core.getConfig().getString("messages.toggle-off")));
        }

        core.saveData();
    }

    @Override
    public List<String> suggest(CrossPlayer player, String[] args) {
        return List.of();
    }
}
