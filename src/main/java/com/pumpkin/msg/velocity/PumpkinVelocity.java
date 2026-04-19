package com.pumpkin.msg.velocity;

import com.google.inject.Inject;
import com.pumpkin.msg.commands.*;
import com.pumpkin.msg.core.PumpkinCore;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import java.nio.file.Path;
import java.util.logging.Logger;

@Plugin(id = "pumpkinmsg", name = "PumpkinMsg", version = "1.3", authors = {"Pumpkingz"})
public class PumpkinVelocity {
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private PumpkinCore core;

    @Inject
    public PumpkinVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // Inicializamos el Core con la plataforma de Velocity
        this.core = new PumpkinCore(dataDirectory, logger, new VelocityPlatform(server));

        CommandManager cm = server.getCommandManager();
        cm.register(cm.metaBuilder("msg").aliases("w", "tell", "message").build(), new VelocityCommandAdapter(new MsgCommand(core)));
        cm.register(cm.metaBuilder("reply").aliases("r").build(), new VelocityCommandAdapter(new ReplyCommand(core)));
        cm.register(cm.metaBuilder("socialspy").aliases("spy").build(), new VelocityCommandAdapter(new SpyCommand(core)));
        cm.register(cm.metaBuilder("spycommands").aliases("cmdspy").build(), new VelocityCommandAdapter(new CommandSpy(core)));
        cm.register(cm.metaBuilder("ignore").build(), new VelocityCommandAdapter(new IgnoreCommand(core)));
        cm.register(cm.metaBuilder("togglemsg").aliases("tmsg", "pmtoggle").build(), new VelocityCommandAdapter(new ToggleMsgCommand(core)));
        cm.register(cm.metaBuilder("pumpkinreload").build(), new VelocityCommandAdapter(new ReloadCommand(core)));

        server.getEventManager().register(this, new VelocitySpyListener(core));
        logger.info("PumpkinMsg cargado en modo VELOCITY.");
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        if (core != null) core.saveData();
    }
}
