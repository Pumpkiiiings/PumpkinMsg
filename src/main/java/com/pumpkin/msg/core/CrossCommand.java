package com.pumpkin.msg.core;

import java.util.List;

public interface CrossCommand {
    void execute(CrossPlayer sender, String[] args);
    List<String> suggest(CrossPlayer sender, String[] args);
}
