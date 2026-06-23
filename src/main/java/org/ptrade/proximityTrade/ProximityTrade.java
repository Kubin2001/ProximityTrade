package org.ptrade.proximityTrade;

import org.bukkit.plugin.java.JavaPlugin;

public final class ProximityTrade extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Proximity Trade Starting");

        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            VaultHook ecoHook = new VaultHook();
            if (ecoHook.SetupEconomy()) {
                Helpers.ecoHook = ecoHook;
                Helpers.hasEconomy = true;
                getLogger().info("Vault found economy integrated");
            } else {
                Helpers.hasEconomy = false;
                getLogger().warning("Vault found but no plugin provides economy");
            }
        } else {
            Helpers.hasEconomy = false;
            getLogger().info("No vault eco will be disabled");
        }

        if(!getDataFolder().exists()){
            getDataFolder().mkdirs();
        }
        MainConfig.Load(this);
        Helpers.Init();
        getCommand("reload").setExecutor(new CommandReload(this));
        TradeCommand handler = new TradeCommand();

        getCommand("trade").setExecutor(handler);
        getServer().getPluginManager().registerEvents(new EventListener(this), this);

    }

    @Override
    public void onDisable() {
        getLogger().info("Proximity Trade Disabling");
    }
}
