package org.ptrade.proximityTrade;

import org.bukkit.plugin.Plugin;


public class MainConfig {
    public static int maxTradeDistance = 50;
    public static boolean ignoreWorlds = false;

    public static void Load(Plugin plugin){
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        maxTradeDistance = plugin.getConfig().getInt("maxDistance",50);
        ignoreWorlds = plugin.getConfig().getBoolean("ignoreWorlds",false);

    }
}
