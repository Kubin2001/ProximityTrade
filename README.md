# ProximityTrade 🤝

A lightweight, secure, and interactive proximity-based trading plugin for Minecraft (Bukkit/Spigot/Paper).

ProximityTrade eliminates the risk of dropping items on the ground to trade. It introduces a secure, distance-checked GUI trading system, ensuring both parties agree to the transaction before any items change hands.

## ✨ Features

* **Distance-Based Trading:** Players can only trade if they are within a configurable block radius of each other (prevents cross-world or cross-map remote trading).
* **Interactive Dual-GUI:** A 54-slot custom inventory where both players can see what is being offered in real-time.
* **Two-Step Verification:** Both players must confirm their offers, and then finalize the trade, minimizing scamming or accidental trades.
* **Safe Item Handling:** If the GUI is closed prematurely or a player disconnects, all items are safely returned to the players inventories or dropped naturally at their location.
* **Lightweight:** Built with minimal overhead, utilizing Bukkit's built-in scheduler for glitch-free inventory updates.

## ⚙️ Configuration

A default `config.yml` is generated on the first run. You can tweak the trading rules:

```yaml
# Maximum distance (in blocks) players can be from each other to initiate a trade
maxDistance: 50

# Set to true to allow trading across different worlds (bypasses distance check)
ignoreWorlds: false