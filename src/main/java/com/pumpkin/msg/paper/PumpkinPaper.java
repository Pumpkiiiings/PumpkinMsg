package com.pumpkin.msg.paper;

import com.pumpkin.msg.commands.*;
import com.pumpkin.msg.core.PumpkinCore;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class PumpkinPaper extends JavaPlugin {
    private PumpkinCore core;

    @Override
    public void onEnable() {
        this.core = new PumpkinCore(getDataFolder().toPath(), getLogger(), new PaperPlatform());

        // Registro tradicional (Plugin.yml)
        register("msg", new MsgCommand(core));
        register("reply", new ReplyCommand(core));
        register("socialspy", new SpyCommand(core));
        register("spycommands", new CommandSpy(core));
        register("ignore", new IgnoreCommand(core));
        register("togglemsg", new ToggleMsgCommand(core));
        register("pumpkinreload", new ReloadCommand(core));
        getServer().getPluginManager().registerEvents(new PaperSpyListener(core), this);

        getLogger().info("PumpkinMsg cargado correctamente en PAPER (Modo plugin.yml).");
    }

    private void register(String name, com.pumpkin.msg.core.CrossCommand crossCommand) {
        PluginCommand command = getCommand(name);
        if (command != null) {
            PaperCommandAdapter adapter = new PaperCommandAdapter(crossCommand);
            command.setExecutor(adapter);
            command.setTabCompleter(adapter);
        } else {
            getLogger().warning("No se pudo registrar el comando /" + name + " porque no esta en el plugin.yml");
        }
    }

    @Override
    public void onDisable() {
        if (core != null) core.saveData();
    }
}
