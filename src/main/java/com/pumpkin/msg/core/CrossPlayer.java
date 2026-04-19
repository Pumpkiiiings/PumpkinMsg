package com.pumpkin.msg.core;

import net.kyori.adventure.text.Component;
import java.util.UUID;

public interface CrossPlayer {
    UUID getUniqueId();
    String getUsername();
    void sendMessage(Component message);
    boolean hasPermission(String permission);
    String getServerName();

    String translatePlaceholders(String text);
}
