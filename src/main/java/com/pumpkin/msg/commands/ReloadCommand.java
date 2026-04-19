package com.pumpkin.msg.commands;

import com.pumpkin.msg.core.CrossCommand;
import com.pumpkin.msg.core.CrossPlayer;
import com.pumpkin.msg.core.PumpkinCore;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;

public class ReloadCommand implements CrossCommand {

    private final PumpkinCore core;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public ReloadCommand(PumpkinCore core) {
        this.core = core;
    }

    @Override
    public void execute(CrossPlayer sender, String[] args) {
        if (!sender.hasPermission("pumpkinmsg.admin.reload")) {
            sender.sendMessage(mm.deserialize(core.getConfig().getString("messages.no-permission")));
            return;
        }

        try {
            core.getConfig().loadConfiguration();
            sender.sendMessage(mm.deserialize(core.getConfig().getString("messages.reload-success")));
        } catch (Exception e) {
            sender.sendMessage(mm.deserialize("<red>Error crítico al recargar la configuración. Revisa la consola."));
            e.printStackTrace();
        }
    }

    @Override
    public List<String> suggest(CrossPlayer sender, String[] args) {
        return List.of(); // Sin autocompletado
    }
}
