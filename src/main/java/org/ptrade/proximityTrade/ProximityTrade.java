package org.ptrade.proximityTrade;

import org.bukkit.plugin.java.JavaPlugin;

public final class ProximityTrade extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Proximity Trade Starting");

        TradeCommand handler = new TradeCommand();

        getCommand("trade").setExecutor(handler);

    }

    @Override
    public void onDisable() {
        getLogger().info("Proximity Trade Disabling");
    }
}
