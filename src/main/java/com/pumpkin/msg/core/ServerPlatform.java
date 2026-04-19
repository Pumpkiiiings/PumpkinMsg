package com.pumpkin.msg.core;

import java.util.List;
import java.util.UUID;

public interface ServerPlatform {
    CrossPlayer getPlayer(String name);
    CrossPlayer getPlayer(UUID uuid);
    List<CrossPlayer> getAllOnlinePlayers();
    List<String> getAllServerNames();
    void dispatchCommand(CrossPlayer sender, String command);
}
