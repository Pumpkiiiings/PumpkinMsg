package com.pumpkin.msg.core;

import com.pumpkin.msg.config.ConfigManager;
import net.kyori.adventure.text.Component;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class PumpkinCore {

    private final ConfigManager configManager;
    private final Logger logger;
    private final ServerPlatform platform; // NUEVO: La conexión con el servidor

    private final Set<UUID> socialSpyUsers = ConcurrentHashMap.newKeySet();
    private final Set<UUID> msgDisabledUsers = ConcurrentHashMap.newKeySet();
    private final Map<UUID, Set<UUID>> ignoredPlayers = new ConcurrentHashMap<>();
    private final Map<UUID, UUID> lastMessaged = new ConcurrentHashMap<>();
    private final Map<UUID, UUID> spyTargets = new ConcurrentHashMap<>();
    private final Map<UUID, String> cmdSpyUsers = new ConcurrentHashMap<>();

    public PumpkinCore(Path dataDirectory, Logger logger, ServerPlatform platform) {
        this.logger = logger;
        this.platform = platform;
        this.configManager = new ConfigManager(dataDirectory);
        loadData();
    }

    public void loadData() {
        try {
            this.socialSpyUsers.addAll(configManager.loadUUIDSet("spy_data.txt"));
            this.msgDisabledUsers.addAll(configManager.loadUUIDSet("toggled_msgs.txt"));
            this.ignoredPlayers.putAll(configManager.loadIgnoreMap());
            this.cmdSpyUsers.putAll(configManager.loadCmdSpy());

            logger.info("Datos cargados: " + socialSpyUsers.size() + " espías, " +
                    msgDisabledUsers.size() + " msgs desactivados.");
        } catch (Exception e) {
            logger.severe("Error crítico cargando los datos de PumpkinMsg");
            e.printStackTrace();
        }
    }

    public void saveData() {
        configManager.saveUUIDSet(socialSpyUsers, "spy_data.txt");
        configManager.saveUUIDSet(msgDisabledUsers, "toggled_msgs.txt");
        configManager.saveIgnoreMap(ignoredPlayers);
        configManager.saveCmdSpy(cmdSpyUsers);
    }

    public Component getPrefix(UUID uuid) {
        try {
            net.luckperms.api.LuckPerms api = net.luckperms.api.LuckPermsProvider.get();
            net.luckperms.api.model.user.User user = api.getUserManager().getUser(uuid);
            if (user != null) {
                String prefix = user.getCachedData().getMetaData().getPrefix();
                if (prefix != null) {
                    return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacyAmpersand().deserialize(prefix);
                }
            }
        } catch (Throwable ignored) {}
        return Component.empty();
    }

    // Getters
    public ConfigManager getConfig() { return configManager; }
    public ServerPlatform getPlatform() { return platform; } // Getter de la plataforma
    public Set<UUID> getSocialSpyUsers() { return socialSpyUsers; }
    public Set<UUID> getMsgDisabledUsers() { return msgDisabledUsers; }
    public Map<UUID, Set<UUID>> getIgnoredPlayers() { return ignoredPlayers; }
    public Map<UUID, UUID> getLastMessaged() { return lastMessaged; }
    public Map<UUID, UUID> getSpyTargets() { return spyTargets; }
    public Map<UUID, String> getCmdSpyUsers() { return cmdSpyUsers; }
}
