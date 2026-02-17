package org.ptrade.proximityTrade;

import org.bukkit.plugin.java.JavaPlugin;

public final class ProximityTrade extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Proximity Trade Starting");

        Helpers.Init();

        TradeCommand handler = new TradeCommand();

        getCommand("trade").setExecutor(handler);
        getServer().getPluginManager().registerEvents(new EventListener(this), this);

    }

    @Override
    public void onDisable() {
        getLogger().info("Proximity Trade Disabling");
    }
}
