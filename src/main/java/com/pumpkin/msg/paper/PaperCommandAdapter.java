package com.pumpkin.msg.paper;

import com.pumpkin.msg.core.CrossCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PaperCommandAdapter implements CommandExecutor, TabCompleter {
    private final CrossCommand crossCommand;

    public PaperCommandAdapter(CrossCommand crossCommand) {
        this.crossCommand = crossCommand;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            crossCommand.execute(new PaperPlayer(player), args);
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof Player player) {
            return crossCommand.suggest(new PaperPlayer(player), args);
        }
        return List.of();
    }
}
