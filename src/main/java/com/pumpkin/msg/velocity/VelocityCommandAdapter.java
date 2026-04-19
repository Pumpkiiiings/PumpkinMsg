package com.pumpkin.msg.velocity;

import com.pumpkin.msg.core.CrossCommand;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

import java.util.List;

public class VelocityCommandAdapter implements SimpleCommand {
    private final CrossCommand crossCommand;

    public VelocityCommandAdapter(CrossCommand crossCommand) {
        this.crossCommand = crossCommand;
    }

    @Override
    public void execute(Invocation invocation) {
        if (invocation.source() instanceof Player player) {
            crossCommand.execute(new VelocityPlayer(player), invocation.arguments());
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        if (invocation.source() instanceof Player player) {
            return crossCommand.suggest(new VelocityPlayer(player), invocation.arguments());
        }
        return List.of();
    }
}
