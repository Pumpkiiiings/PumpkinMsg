---

# 🎃 PumpkinMsg

**PumpkinMsg** is a high-performance, cross-platform private messaging system designed for **Velocity** proxies and **Paper/Spigot** servers. It allows you to use a **single JAR** file for your entire network while providing seamless communication, advanced moderation tools, and deep integration with the most popular APIs.

-----

## ✨ Key Features

- 🚀 **Dual-Platform Core:** A single `.jar` file that detects if it's running on **Velocity** or **Paper/Spigot**. No need for multiple plugins!
- 🌍 **Global & Local Messaging:** Cross-server messaging for Velocity and high-speed local messaging for Paper.
- 🧩 **PlaceholderAPI Support:** (Paper only) Use any `%placeholder%` in your message formats. Ranks, levels, and custom variables are fully supported.
- 🎨 **LuckPerms Integration:** Native support for prefixes and suffixes in all platforms.
- 🕵️ **Dual Spy System:**
    - **SocialSpy:** Monitor private conversations (`/msg`) between users.
    - **CommandSpy:** Watch player commands in real-time. Includes server-specific filtering and sensitive command masking (login/register).
- 🛡️ **Total Privacy:** Includes `/ignore` system and `/togglemsg` for players who want to fly under the radar.
- 🌈 **MiniMessage Native:** Use RGB, Gradients, and hover events in your `config.toml`.
- 💾 **Safe Persistence:** All user settings (ignores, spy status) are saved in a lightweight database system.

-----

## 📜 Commands & Permissions

| Command | Aliases | Description | Permission |
| :--- | :--- | :--- | :--- |
| `/msg <player> <msg>` | `/w`, `/tell` | Sends a private message. | *None* |
| `/reply <message>` | `/r` | Replies to the last interacted player. | *None* |
| `/ignore <player>` | - | Blocks or unblocks a player. | *None* |
| `/togglemsg` | `/tmsg` | Enables/Disables your private messages. | *None* |
| `/socialspy` | `/spy` | Toggles private chat monitoring. | `pumpkinmsg.staff.spy` |
| `/spycommands` | `/cmdspy` | Toggles command monitoring. | `pumpkinmsg.staff.cmdspy` |
| `/pumpkinreload` | - | Reloads the configuration. | `pumpkinmsg.admin.reload` |

-----

## 🛠️ Configuration (`config.toml`)

Customize your styles using **MiniMessage** and **Placeholders**. The system is safe against "Placeholder Injection" (players cannot trigger placeholders in their chat).

```toml
[format]
# You -> Target (Supports PAPI on Paper)
sender = "<#ABB2BF>[<#FF9500>You <#5C6370>-> <#61AFEF><target><#ABB2BF>] <white><message>"

# Recipient Format (Example with rank placeholder)
receiver = "<#ABB2BF>[%luckperms_prefix% <#61AFEF><sender> <#5C6370>-> <#FF9500>You<#ABB2BF>] <white><message>"

# Staff Spy Format
spy = "<#C678DD>[Spy] <sender_prefix><#61AFEF><sender> <#5C6370>-> <target_prefix><#61AFEF><target><#5C6370>: <#D19A66><message>"
```

-----

## 📦 Installation

### **For Velocity Networks:**
1. Place `PumpkinMsg.jar` in your Velocity `plugins` folder.
2. Ensure **LuckPerms** (Velocity version) is installed for prefix support.
3. Restart your proxy.

### **For Paper/Spigot Servers:**
1. Place the same `PumpkinMsg.jar` in your server's `plugins` folder.
2. (Optional) Install **PlaceholderAPI** to enable custom variables in formats.
3. Restart your server.

-----

*Made with 🎃 by the Pumpkingz.*
